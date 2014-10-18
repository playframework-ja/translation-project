<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Handling and serving XML requests
-->
# XML リクエストとレスポンス

<!--
## Handling an XML request
-->
## XML リクエストの処理

<!--
An XML request is an HTTP request using a valid XML payload as request body. It must specify the `application/xml` or `text/xml` MIME type in its `Content-Type` header.
-->
XML リクエストはリクエストボディに XML データを含む HTTP リクエストです。XML リクエストの `Content-Type` ヘッダには、 `application/xml` もしくは `text/xml` という MIME タイプを指定する必要があります。

<!--
By default, an action uses an **any content** body parser, which you can use to retrieve the body as XML (actually as a `org.w3c.Document`):
-->
アクションは **any content** ボディパーサーをデフォルトで使います。これを利用して、リクエストボディを XML (具体的には `org.w3c.Document`) として取得することができます。

```
public static Result sayHello() {
  Document dom = request().body().asXml();
  if(dom == null) {
    return badRequest("Expecting Xml data");
  } else {
    String name = XPath.selectText("//name", dom);
    if(name == null) {
      return badRequest("Missing parameter [name]");
    } else {
      return ok("Hello " + name);
    }
  }
}
```

<!--
Of course it’s way better (and simpler) to specify our own `BodyParser` to ask Play to parse the content body directly as XML:
-->
この場合、専用の`BodyParser` を指定することで Play にコンテントボディを直接的に XML としてパースさせると、記述がシンプル化されてなお良いでしょう。

```
@BodyParser.Of(Xml.class)
public static Result sayHello() {
  Document dom = request().body().asXml();
  if(dom == null) {
    return badRequest("Expecting Xml data");
  } else {
    String name = XPath.selectText("//name", dom);
    if(name == null) {
      return badRequest("Missing parameter [name]");
    } else {
      return ok("Hello " + name);
    }
  }
}
```

<!--
> **Note:** This way, a 400 HTTP response will be automatically returned for non-XML requests.
-->
> **ノート:** この方法では、XML 以外のリクエストに対しては自動的に HTTP の 400 番のレスポンスが返ってきます。

<!--
You can test it with **cURL** on the command line:
-->
このアクションは、コマンドラインから **cURL** を使って以下のようにテストできます。

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
In our previous example, we handled an XML request, but replied with a `text/plain` response. Let’s change it to send back a valid XML HTTP response:
-->
前述の例では XML リクエストを処理して、`text/plain` のレスポンスを返してしまっていました。これを、正しい XML HTTP レスポンスを送り返すように変更してみましょう。

```
@BodyParser.Of(Xml.class)
public static Result sayHello() {
  Document dom = request().body().asXml();
  if(dom == null) {
    return badRequest("Expecting Xml data");
  } else {
    String name = XPath.selectText("//name", dom);
    if(name == null) {
      return badRequest("<message \"status\"=\"KO\">Missing parameter [name]</message>");
    } else {
      return ok("<message \"status\"=\"OK\">Hello " + name + "</message>");
    }
  }
}
```

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

<!--
> **Next:** [[Handling file upload | JavaFileUpload]]
-->
> **次ページ:** [[ファイルアップロードの処理 | JavaFileUpload]]
