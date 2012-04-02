# Working with JSON

The recommend way of dealing with JSON is using Play’s type class-based JSON library, located at ```play.api.libs.json```. This library is built on top of [Jerkson](https://github.com/codahale/jerkson/), which is a Scala wrapper around the super-fast Java-based JSON library, [Jackson](http://jackson.codehaus.org/).

The benefit of this approach is that both the Java and the Scala side of Play can share the same underlying library (Jackson), while Scala users can enjoy the extra type safety that Play’s JSON support brings to the table.

# How to parse JSON and marshal data to domain objects

`play.api.libs.json` package contains seven JSON data types: 

* ```JsObject```
* ```JsNull```
* ```JsUndefined```
* ```JsBoolean```
* ```JsNumber```
* ```JsArray```
* ```JsString```

All of them inherit from the generic JSON value, ```JsValue```.

Using these one can build a type safe JSON deserializer and encapsulate the whole logic like this. For example:

```scala
case class User(id: Long, name: String, friends: List[User])

  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue): User = User(
      (json \ "id").as[Long],
      (json \ "name").as[String],
      (json \ "friends").asOpt[List[User]].getOrElse(List())
    )

    //unmarshaling to JSValue is covered in the next paragraph
    def writes(u: User): JsValue = JsObject(Seq(
        "id" -> JsNumber(u.id),
        "name" -> JsString(u.name),
        "friends" -> JsArray(u.friends.map(toJson(_)))
    ))  
  }
```

> **Note:** `Format` defines two methods: ```reads``` and ```writes```, which are responsible for marshalling to and from `JsValue`.

Given this, one can marshall an incoming JSON string into a `User` case class like this:  

```scala
val data = play.api.libs.json.Json.parse(incomingJSONstring).as[User]
```

Alternatively, if the data is coming from a `request.body`:

```scala
val user = request.body.asJson.map(_.as[User]).getOrElse(
  throw new RuntimeException("could not create user")
)
```

It’s also possible to pattern match on ```JsValue``` in cases where the underlying JSON type is not homogenous (say a JSON Map with different JSON types) or if one wants to manage object creation.

```scala
import play.api.libs.json._
import Json._

//{"newspaper":{"url":"http://nytimes.com","attributes":{"name":"nytimes","country":"US","id":25,"links":["http://link1","http://link2"]}}}
val jsonMap = com.codahale.jerkson.Json.generate(Map(
        "newspaper" -> Map(
          "url" -> "http://nytimes.com",
          "attributes" -> Map("name" -> "nytimes", "country" -> "US", "id" -> 25, "links" -> List("http://link1", "http://link2"))
      )))

val data = parse(jsonMap)

case class Attributes(name: String, id: Int, links: List[String])

val attributes = (data \ "attributes") 

println(Attributes( (attributes \ "name") match {case JsString(name)=>name;case _ => ""},
                    (attributes \ "id") match {case JsNumber(id)=>id.toInt;case _ => 0},
                    (attributes \ "links") match {case JsArray(links)=>links.map(_.as[String]);case _ => Nil}))
   
```

> **Note:** `\\` means look-up in the current object and all descendants, `\` means only look-up the corresponding property.

 

# How to unmarshall from domain objects to JSON

Parsing JSON is just half of the story, since in most situations we would like to return JSON as well. Let’s revisit the previous example:

```scala
import play.api.libs.json._

case class User(id: Long, name: String, friends: List[User])

  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue): User = User(
      (json \ "id").as[Long],
      (json \ "name").as[String],
      (json \ "friends").asOpt[List[User]].getOrElse(List()))
    def writes(u: User): JsValue = JsObject(List(
      "id" -> JsNumber(u.id),
      "name" -> JsString(u.name),
      "friends" -> JsArray(u.friends.map(fr => JsObject(List("id" -> JsNumber(fr.id), "name" -> JsString(fr.name))))))) 
  }
```

> **Note:** The main building block is ```JsObject```, which takes a ```Seq[String,JsValue]```.

Then:

```scala
import play.api.libs.json._
import play.api._
import play.api.mvc._

object MyController extends Controller{

 def sendJson(id: String) = Action {
   val peter = User(id.toLong, "peter", friends: List())
    Ok(toJson(peter))
  }

}
```

Alternatively, one can build an unnamed `JsObject` structure and return it:

```scala
import play.api.libs.json._
import play.api._
import play.api.mvc._

object MyController extends Controller{

 def sendJson(id: String) = Action {
    Ok(toJson(JsObject(List("key"->JsString("cool"), "links"->JsObject(List("name"->JsString("foo"), "links" ->JsArray(List(JsNumber(25)))))))))
  }

}
```

Finally, it’s also possible to unmarshall standard and typed data structures directly:

```scala
import play.api.libs.json._
import play.api._
import play.api.mvc._

object MyController extends Controller{

 def sendJson(id: String) = Action {

    //this won't work, since it's not properly typed
    //toJson(Map("f"->Map[String,Any]("s"->List("1","f"),"f"->"f" )))
    
    //this will return JSON as a String
    //Json.stringify(toJson(Map("f"->Map("s"->List("1","f"))))) 
    
    //this will return a JSON string with content type "application/json"
    Ok(toJson(Map("f"->Map("s"->List("1","f")))))
  }

}
```

The benefit of the type class-based solution is that it significantly increases type safety, albeit at the cost of maintaining the extra mapping or type conversion to `JsValue` that is necessary to unmarshall data from domain objects to JSON.


# Other options

While the type class-based solution described above is the one that’s recommended, there is nothing stopping you from using other JSON libraries.

For example, here is a small snippet that demonstrates how to marshall a plain Scala object into JSON and send it over the wire using the bundled, reflection-based [Jerkson](https://github.com/codahale/jerkson/) library:

```scala
object MyController extends Controller{

 import com.codahale.jerkson.Json._

 def sendJson(id: String) = Action {
    val dataFromDataStore =  Map("url"->"http://nytimes.com","attributes"-> Map("name"->"nytimes", "country"->"US","id"->25), "links"->List("http://link1","http://link2")
    Ok(generate(dataFromDataStore)).as("application/json")
  }

}
```

> **Next:** [[Handling and serving JSON requests | ScalaJsonRequests]]