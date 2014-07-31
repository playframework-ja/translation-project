<!-- translated -->
<!--
# Handling and serving JSON requests
-->
# JSON リクエストとレスポンス

<!--
## Handling a JSON request
-->
## JSON リクエストの処理

<!--
A JSON request is an HTTP request using a valid JSON payload as request body. It must specify the `text/json` or `application/json` mime type in its `Content-Type` header.
-->
JSON リクエストは JSON データをリクエストボディに含む HTTP リクエストです。JSON リクエストは、`Content-Type` ヘッダに `text/json` か `application/json` という MIME タイプを指定する必要があります。

<!--
By default an `Action` uses an **any content** body parser, which lets you retrieve the body as JSON (actually as a `JsValue`):
-->
`Action` は **any content** ボディパーサーをデフォルトで使います。これを利用して、リクエストボディを JSON (具体的には、`JsValue`) として取得することができます。

```scala
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
// you need this import to have combinators
import play.api.libs.functional.syntax._

object Application extends Controller {
  
  implicit val rds = (
    (__ \ 'name).read[String] and
    (__ \ 'age).read[Long]
  ) tupled

  def sayHello = Action { request =>
    request.body.asJson.map { json =>
      json.validate[(String, Long)].map{ 
        case (name, age) => Ok("Hello " + name + ", you're "+age)
      }.recoverTotal{
        e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }
}
```

<!--
It's better (and simpler) to specify our own `BodyParser` to ask Play to parse the content body directly as JSON:
-->
この場合、専用の `BodyParser` を指定することで Play にコンテントボディを直接的に JSON としてパースさせると、記述がシンプル化されてなお良いでしょう。

```scala
  def sayHello = Action(parse.json) { request =>
    request.body.validate[(String, Long)].map{ 
      case (name, age) => Ok("Hello " + name + ", you're "+age)
    }.recoverTotal{
      e => BadRequest("Detected error:"+ JsError.toFlatJson(e))
    }
  }
```

<!--
> **Note:** When using a JSON body parser, the `request.body` value is directly a valid `JsValue`. 
-->
> **Note:** JSON ボディパーサーを利用すると、`request.body` の値が直接 `JsValue` として扱えるようになります。

<!--
Please note:
-->
注意:

#### `implicits Reads[(String, Long)]` 
<!--
It defines an implicits Reads using combinators which can validate and transform input JSON.
-->
入力された JSON のバリデーションと変換を行うことのできるコンビネータを使う、暗黙の Reads を定義します。

#### `json.validate[(String, Long)]` 
<!--
It explicitly validates & transforms input JSON according to implicit `Reads[(String, Long)]`
-->
暗黙の `Reads[(String, Long)]` に従って、入力された JSON のバリデーションと変換を明示的に行います。


<!--
You can test it with **cURL** from the command line:
-->
このアクションは、コマンドラインから **cURL** を使って以下のようにテストできます。

#### `json.validate[(String, Long)].map{ (String, Long) => ... } `

<!--
This maps the result in case of success to transform it into an action result.
-->
JSON の変換に成功した場合、その結果をアクションの結果にマップします。

#### `json.validate[(String, Long)].recoverTotal{ e: JsError => ... }`

<!--
`recoverTotal` takes a function to manage errors and returns a default value:
- it ends the `JsResult` modification chain and returns the successful inner value 
- or if detected a failure, it returns the result of the function provided to `recoverTotal`.
-->
`recoverTotal` は、発生したエラーに対応して、デフォルト値を返す関数を受け取ります:
- `JsResult` の変更チェーンを終了し、正常に作成された内部文字列を返します
- または、失敗を検出した場合は `recoverToal` に渡された関数の実行結果を返します。

#### `JsError.toFlatJson(e)`
<!--
This is a helper that transforms the `JsError` into a flattened JsObject form :
-->
`JsError` を平べったい JsObject 形式に変換するヘルパーです :

```
JsError(List((/age,List(ValidationError(validate.error.missing-path,WrappedArray()))), (/name,List(ValidationError(validate.error.missing-path,WrappedArray())))))
```

<!--
would become JsValue:
-->
これは、次のような JsValue になります:

```
{"obj.age":[{"msg":"validate.error.missing-path","args":[]}],"obj.name":[{"msg":"validate.error.missing-path","args":[]}]}
```

<!--
> Please note a few other helpers should be provided later.
-->
> 今後、この他にもいくつかのヘルパーが提供される予定です。


<!--
**Let's try it**
-->
**試してみましょう**

<!--
### case OK
-->
### 成功した場合
```
curl 
  --header "Content-type: application/json" 
  --request POST 
  --data '{"name": "Toto", "age": 32}' 
  http://localhost:9000/sayHello
```

<!--
It replies with:
-->
レスポンスは以下のようになります。

```
HTTP/1.1 200 OK
Content-Type: text/plain; charset=utf-8
Content-Length: 47

Hello Toto, you're 32
```

<!--
### case KO "JSON missing field"
-->
### 失敗した場合 "見つからない JSON フィールド"
```
curl 
  --header "Content-type: application/json" 
  --request POST 
  --data '{"name2": "Toto", "age2": 32}' 
  http://localhost:9000/sayHello
```

<!--
It replies with:
-->
レスポンスは以下のようになります。

```
HTTP/1.1 400 Bad Request
Content-Type: text/plain; charset=utf-8
Content-Length: 106

Detected error:{"obj.age":[{"msg":"validate.error.missing-path","args":[]}],"obj.name":[{"msg":"validate.error.missing-path","args":[]}]}
```

<!--
### case KO "JSON bad type"
-->
### 失敗した場合 "不正な JSON 型"
```
curl 
  --header "Content-type: application/json" 
  --request POST 
  --data '{"name": "Toto", "age": "chboing"}' 
  http://localhost:9000/sayHello
```

<!--
It replies with:
-->
レスポンスは以下のようになります。

```
HTTP/1.1 400 Bad Request
Content-Type: text/plain; charset=utf-8
Content-Length: 100

Detected error:{"obj.age":[{"msg":"validate.error.expected.jsnumber","args":[]}]}
```


<!--
## Serving a JSON response
-->
## JSON レスポンスの送信

<!--
In our previous example we handle a JSON request, but we reply with a `text/plain` response. Let’s change that to send back a valid JSON HTTP response:
-->
前述の例ではリクエストを JSON で受けていましたが、レスポンスは `text/plain` として送信していました。これを、正しい JSON HTTP レスポンスを送り返すように変更してみましょう。

```scala
  def sayHello = Action(parse.json) { request =>
    request.body.validate[(String, Long)].map{ 
      case (name, age) => Ok(Json.obj("status" ->"OK", "message" -> ("Hello "+name+" , you're "+age) ))
    }.recoverTotal{
      e => BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(e)))
    }
  }
```

<!--
Now it replies with:
-->
レスポンスは以下のようになります。

```
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 47

{"status":"OK","message":"Hello Toto, you're 32"}
```

<!--
## Sending JSON directly
-->
## JSON を直接返す

<!--
Sending the list of Todos with Play and JSON is very simple:
-->
Play と JSON で Todo リストを返すのはとても簡単です:

```scala
import play.api.libs.json.Json

def tasksAsJson() = Action {
  Ok(Json.toJson(Task.all().map { t=>
    (t.id.toString, t.label)
  } toMap))
}
```

<!--
> **Next:** [[Working with XML | ScalaXmlRequests]]
-->
> **次ページ:** [[XML | ScalaXmlRequests]]