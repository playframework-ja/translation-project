<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Handling and serving XML requests
-->
# XML リクエストとレスポンス

<!--
## Handling an XML request
-->
## XML リクエストの処理

<!--
An XML request is an HTTP request using a valid XML payload as the request body. It must specify the `application/xml` or `text/xml` MIME type in its `Content-Type` header.
-->
XML リクエストはリクエストボディに XML データを含む HTTP リクエストです。XML リクエストの `Content-Type` ヘッダには、 `application/xml` もしくは `text/xml` という MIME タイプを指定する必要があります。

<!--
By default an `Action` uses a **any content** body parser, which lets you retrieve the body as XML (actually as a `NodeSeq`):
-->
`Action` は **any content** ボディパーサーをデフォルトで使います。これを利用して、リクエストボディを XML (具体的には `NodeSeq`) として取得することができます。

@[xml-request-body-asXml](code/ScalaXmlRequests.scala)

<!--
It’s way better (and simpler) to specify our own `BodyParser` to ask Play to parse the content body directly as XML:
-->
この場合、専用の `BodyParser` を指定して Play にコンテントボディを直接的に XML としてパースさせると、記述がシンプル化されてなお良いでしょう。

@[xml-request-body-parser](code/ScalaXmlRequests.scala)

<!--
> **Note:** When using an XML body parser, the `request.body` value is directly a valid `NodeSeq`.
-->
> **Note:** XML ボディパーサーを利用するときには、 `request.body` の値が直接 `NodeSeq` になります。

<!--
You can test it with [cURL](http://curl.haxx.se/) from a command line:
-->
このアクションは、コマンドラインから [cURL](http://curl.haxx.se/) を使って以下のようにテストできます。

```
curl 
  --header "Content-type: application/xml" 
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
前述の例では XML リクエストを処理して、 `text/plain` のレスポンスを返してしまっていました。これを、正しい XML HTTP レスポンスを送り返すように変更してみましょう。

@[xml-request-body-parser-xml-response](code/ScalaXmlRequests.scala)

<!--
Now it replies with:
-->
レスポンスは以下のようになります。

```
HTTP/1.1 200 OK
Content-Type: application/xml; charset=utf-8
Content-Length: 46

<message status="OK">Hello Guillaume</message>
```
