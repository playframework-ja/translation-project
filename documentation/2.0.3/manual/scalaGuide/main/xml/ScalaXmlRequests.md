<!-- translated -->
<!--
# Handling and serving XML requests
-->
# XML リクエストとレスポンス

<!--
## Handling an XML request
-->
## XML リクエストの処理

<!--
An XML request is an HTTP request using a valid XML payload as the request body. It must specify the `text/xml` MIME type in its `Content-Type` header.
-->
XML リクエストはリクエストボディに XML データを含む HTTP リクエストです。XML リクエストの `Content-Type` ヘッダには、`text/xml` という MIME タイプを指定する必要があります。

<!--
By default an `Action` uses a **any content** body parser, which lets you retrieve the body as XML (actually as a `NodeSeq`):
-->
`Action` はリクエストボディを XML (具体的には `NodeSeq`) として取得できる **any content** ボディパーサーをデフォルトで利用します。

```scala
def sayHello = Action { request =>
  request.body.asXml.map { xml =>
    (xml \\ "name" headOption).map(_.text).map { name =>
      Ok("Hello " + name)
    }.getOrElse {
      BadRequest("Missing parameter [name]")
    }
  }.getOrElse {
    BadRequest("Expecting Xml data")
  }
}
```

<!--
It’s way better (and simpler) to specify our own `BodyParser` to ask Play to parse the content body directly as XML:
-->
Play にコンテントボディを直接的に XML としてパースさせるために、`BodyParser` を指定すると良いでしょう。

```scala
def sayHello = Action(parse.xml) { request =>
  (request.body \\ "name" headOption).map(_.text).map { name =>
    Ok("Hello " + name)
  }.getOrElse {
    BadRequest("Missing parameter [name]")
  }
}
```

<!--
> **Note:** When using an XML body parser, the `request.body` value is directly a valid `NodeSeq`. 
-->
> **Note:** XML ボディパーサーを利用するときには、`request.body` の値が直接 `NodeSeq` になります。

<!--
You can test it with **cURL** from a command line:
-->
このアクションは、コマンドラインから **cURL** を使って以下のようにテストできます。

```
curl 
  --header "Content-type: text/xml" 
  --request POST 
  --data '<name>Guillaume</name>' 
  http://localhost:9000/sayHello
```

<!--
It replies with:
-->
レスポンスは以下のようになります。

```
HTTP/1.1 200 OK
Content-Type: text/plain; charset=utf-8
Content-Length: 15

Hello Guillaume
```

<!--
## Serving an XML response
-->
## XML レスポンスの送信

<!--
In our previous example we handle an XML request, but we reply with a `text/plain` response. Let’s change that to send back a valid XML HTTP response:
-->
前述の例では XML リクエストを処理して、`text/plain` のレスポンスを返してしまっていました。これを、正しい XML HTTP レスポンスを送り返すように変更してみましょう。

```scala
def sayHello = Action(parse.xml) { request =>
  (request.body \\ "name" headOption).map(_.text).map { name =>
    Ok(<message status="OK">Hello {name}</message>)
  }.getOrElse {
    BadRequest(<message status="KO">Missing parameter [name]</message>)
  }
}
```

<!--
Now it replies with:
-->
レスポンスは以下のようになります。

```
HTTP/1.1 200 OK
Content-Type: text/xml; charset=utf-8
Content-Length: 46

<message status="OK">Hello Guillaume</message>
```

<!--
> **Next:** [[Handling file upload | ScalaFileUpload]]
-->
> **次ページ:** [[ファイルアップロードの処理 | ScalaFileUpload]]