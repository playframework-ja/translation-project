<!-- translated -->
<!--
# The Play JSON library
-->
# Play JSON ライブラリ

<!--
## Overview
-->
## 概要

<!--
The recommend way of dealing with JSON is using Play’s typeclass based JSON library, located at ```play.api.libs.json```. 
-->
JSON を使いたければ、```play.api.libs.json``` にある JSON ライブラリを基にした Play の型クラスを使うことをおすすめします。

<!--
This library is built on top of [Jerkson](https://github.com/codahale/jerkson/), which is a Scala wrapper around the super-fast Java based JSON library, [Jackson](http://jackson.codehaus.org/). 
-->
このライブラリは、超高速の Java ベースの JSON ライブラリ、[Jerkson](https://github.com/codahale/jerkson/) の Scala ラッパー [Jackson](http://jackson.codehaus.org/) の上に構築されています。

<!--
The benefit of this approach is that both the Java and the Scala side of Play can share the same underlying library (Jackson), while Scala users can enjoy the extra type safety that Play’s JSON support brings to the table.
-->
このアプローチの利点は、Scala と Java の両方で Play は同じ基礎となるライブラリ (Jackson) を共有することができます。Scala ユーザーは Play の JSON サポートがテーブルにもたらす特上の型安全性を楽しむことができます。

<!--
`play.api.libs.json` package contains seven JSON data types: 
-->
`play.api.libs.json` パッケージは 7 つの JSON データ型を含みます。

- ```JsObject```
- ```JsNull```
- ```JsUndefined```
- ```JsBoolean```
- ```JsNumber```
- ```JsArray```
- ```JsString```

<!--
All of them inherit from the generic JSON value, ```JsValue```.
-->
これらは全て一般的な JSON の値である ```JsValue``` から継承されます。

<!--
## Parsing a Json String
-->
## Json 文字列のパース

<!--
You can easily parse any JSON string as a `JsValue`:
-->
どのような JSON 文字列でも簡単に `JsValue` としてパースすることができます:

```
val json: JsValue = Json.parse(jsonString)
```

<!--
## Navigating into a Json tree
-->
## Json ツリーの探索

<!--
As soon as you have a `JsValue` you can navigate into the tree. The API looks like the one provided to navigate into XML document by Scala using `NodeSeq`:
-->
`JsValue` を取得し次第、このツリーを探索することができます。この API は Scala で `NodeSeq` を使って XML ドキュメントを探索するために提供されているものと似ています:

```
val json = Json.parse(jsonString)

val maybeName = (json \ "user" \ name).asOpt[String]
val emails = (json \ "user" \\ "emails").map(_.as[String])
```

<!--
> **Note** that navigating using \ and \\ never fails. You must handle the error case at the end using `asOpt[T]` that will return `None` if the value is missing. Otherwiser you can use `as[T]` that we fail with an exception if the value was missing.
-->
> **注意** \\ と \\\\ を使った探索が失敗することはありません。処理の最後で、値が見つからなかった場合に `None` を返す `asOpt[T]` を使って、エラーが発生した場合の処理をしなければいけません。または、値が見つからなかった場合に例外と共に失敗する `as[T]` を使うことができます。

<!--
## Converting a Scala value to Json
-->
## Scala から Json への変換

<!--
As soon as you have a type class able to transform the Scala type to Json, it is pretty easy to generate any Scala value to Json. For example let's create a simple Json object:
-->
Scala 型を Json に変換することのできる型クラスがあれば、とても簡単に Scala の値から Json を生成することができます。例として、シンプルな Json オブジェクトを作成してみましょう:

```
val jsonNumber = Json.toJson(4)
```

<!--
Or create a json array:
-->
または、json 配列を作ってみましょう:

```
val jsonArray = Json.toJson(Seq(1, 2, 3, 4))
```

<!--
Here we have no problem to convert a `Seq[Int]` into a Json array. However it is more complicated if the `Seq` contains heterogeous values:
-->
ここでは何の問題もなく `Seq[Int]` を Json 配列に変換しています。しかし、`Seq` が異なる種類の値を含む場合、もっと複雑になります:

```
val jsonArray = Json.toJson(Seq(1, "Bob", 3, 4))
```

<!--
Because there is no way to convert a `Seq[Any]` to Json (`Any` could be anything including something not supported by Json right?)
-->
これは、`Seq[Any]` を Json に変換する方法がないためです。(`Any` は Json がサポートしていないものを含むことだってありますよね?)

<!--
A simple solution is to handle it as a `Seq[JsValue]`:
-->
シンプルに解決するには、これを `Seq[JsValue]` として扱います:

```
val jsonArray = Json.toJson(Seq(
  toJson(1), toJson("Bob"), toJson(3), toJson(4)
))
```

<!--
Now let's see a last example of creating a more complex Json object:
-->
もっと複雑な Json オブジェクトを作成する最後の例を見てみましょう:

```
val jsonObject = Json.toJson(
  Map(
    "users" -> Seq(
      toJson(
        Map(
          "name" -> toJson("Bob"),
          "age" -> toJson(31),
          "email" -> toJson("bob@gmail.com")
        )
      ),
      toJson(
        Map(
          "name" -> toJson("Kiki"),
          "age" -> toJson(25),
          "email" -> JsNull
        )
      )
    )
  )
)
```

<!--
That will generate this Json result:
-->
これは以下の Json を生成します:

```
{
  "users":[
    {
      "name": "Bob",
      "age": 31.0,
      "email": "bob@gmail.com"
    },
    {
      "name": "Kiki",
      "age":  25.0,
      "email": null
    }
  ]
}
```

<!--
## Serializing Json
-->
## Json シリアライズ

<!--
Serializing a `JsValue` to its json String representation is easy:
-->
`JsValue` を、それ自身を表現する Json 文字列にシリアライズするのは簡単です:

```
val jsonString: String = Json.stringify(jsValue)
```

<!--
## Other options
-->
## その他のオプション

<!--
While the typeclass based solution describe above is the on that's recommended, nothing stopping users from using any other JSON libraries if needed.
-->
上記の型クラスに基づいた解決策がお勧めではありますが、もし必要であれば、ユーザがこの他のどのような JSON ライブラリを使うことも止めはしません。

<!--
For example, here is a small snippet which demonstrates how to marshal plain scala objects into JSON and send it over the wire using the bundled, reflection based [[Jerkson | https://github.com/codahale/jerkson/]] library:
-->
例えば以下は、バンドルされたリフレクションベースの [[Jerkson | https://github.com/codahale/jerkson/]] ライブラリを使って、素の scala オブジェクトを JSON にマーシャリングして回線越しに送信する小さなスニペットです。

```scala
import com.codahale.jerkson.Json._

val json = generate(
  Map( 
    "url"-> "http://nytimes.com",
    "attributes" -> Map(
      "name" -> "nytimes", 
      "country" -> "US",
      "id" -> 25
    ), 
    "links" -> List(
      "http://link1",
      "http://link2"
    )
  )
)
```

<!--
> **Next:** [[Handling and serving Json requests | ScalaJsonRequests]]
-->
> **次:** [[JSON リクエストの扱いと提供 | ScalaJsonRequests]]