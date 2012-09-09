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
`Action` は、リクエストボディを JSON (具体的には、`JsValue`) として取得することができる **any content** ボディパーサーをデフォルトで利用します。

```scala
def sayHello = Action { request =>
  request.body.asJson.map { json =>
    (json \ "name").asOpt[String].map { name =>
      Ok("Hello " + name)
    }.getOrElse {
      BadRequest("Missing parameter [name]")
    }
  }.getOrElse {
    BadRequest("Expecting Json data")
  }
}
```

<!--
It's better (and simpler) to specify our own `BodyParser` to ask Play to parse the content body directly as JSON:
-->
Play にリクエストボディを直接 `JSON` としてパースさせるために `BodyParser` を指定するとよいでしょう。

```scala
def sayHello = Action(parse.json) { request =>
  (request.body \ "name").asOpt[String].map { name =>
    Ok("Hello " + name)
  }.getOrElse {
    BadRequest("Missing parameter [name]")
  }
}
```

<!--
> **Note:** When using a JSON body parser, the `request.body` value is directly a valid `JsValue`. 
-->
> **Note:** JSON ボディパーサーを利用すると、`request.body` の値が直接 `JsValue` として扱えるようになります。

<!--
You can test it with **cURL** from the command line:
-->
コマンドラインから **cURL** を起動してテストしてみましょう。

```
curl 
  --header "Content-type: application/json" 
  --request POST 
  --data '{"name": "Guillaume"}' 
  http://localhost:9000/sayHello
```

<!--
It replies with:
-->
出力結果は以下のようになります。

```
HTTP/1.1 200 OK
Content-Type: text/plain; charset=utf-8
Content-Length: 15

Hello Guillaume
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
  (request.body \ "name").asOpt[String].map { name =>
    Ok(toJson(
      Map("status" -> "OK", "message" -> ("Hello " + name))
    ))
  }.getOrElse {
    BadRequest(toJson(
      Map("status" -> "KO", "message" -> "Missing parameter [name]")
    ))
  }
}
```

<!--
Now it replies with:
-->
これで、次のようなレスポンスが返せるようになります。

```
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 43

{"status":"OK","message":"Hello Guillaume"}
```

<!--
> **Next:** [[Working with XML | ScalaXmlRequests]]
-->
> **次ページ:** [[XML を使う | ScalaXmlRequests]]