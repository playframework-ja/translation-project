<!--
# The Play WS API
-->
# Play WS API

<!--
Sometimes we would like to call other HTTP services from within a Play application. Play supports this via its `play.api.libs.ws.WS` library, which provides a way to make asynchronous HTTP calls.
-->
Play アプリケーションから他の HTTP サービスを呼び出したいということはありませんか? Play は非同期の HTTP 呼び出しを実現する `play.api.libs.ws.WS` というライブラリでこれをサポートしています。

<!--
Any calls made by `play.api.libs.ws.WS` should return a `Promise[play.api.libs.ws.Response]` which we can later handle with Play’s asynchronous mechanisms.
-->
`play.api.libs.ws.WS` による全ての HTTP 通信は、Play の非同期処理のメカニズムで処理される `Promise[play.api.libs.ws.Response]` を返します。

<!--
## Making an HTTP call
-->
## HTTP 通信の開始

<!--
To send an HTTP request you start with `WS.url()` to specify the URL. Then you get a builder that you can use to specify various HTTP options, such as setting headers. You end by calling a final method corresponding to the HTTP method you want to use. For example:
-->
HTTP リクエストを送信するためには、まず `WS.url()` を使って URL を指定します。このメソッドは、ヘッダの設定等の HTTP に関する設定を行うためのビルダを返します。最終的には、HTTP メソッドに対応するメソッドを呼び出して、HTTP リクエストを送信します。これは、次のように書きます。

```scala
val homePage: Promise[ws.Response] = WS.url("http://mysite.com").get()
```

<!--
Or:
-->
または、次のようにも書けます。

```scala
val result: Promise[ws.Response] = {
  WS.url("http://localhost:9001/post").post("content")
}
```

<!--
## Retrieving the HTTP response result
-->
## HTTP レスポンスの取得

<!--
The call is asynchronous and you need to manipulate it as a `Promise[ws.Response]` to get the actual content. You can compose several promises and end with a `Promise[Result]` that can be handled directly by the Play server:
-->
HTTP 通信は非同期であるため、実際のコンテンツを取得するためには `Promise[ws.Response]` を操作しなければなりません。流れとしては、`Promise[ws.Response]` をはじめとする複数の Promise を組み合わせて、最終的には Play サーバーが処理できるように `Promise[Result]` を返すようにします。

```scala
def feedTitle(feedUrl: String) = Action {
  Async {
    WS.url(feedUrl).get().map { response =>
      Ok("Feed title: " + (response.json \ "title").as[String])
    }
  }  
}
```

<!--
## Post url-form-encoded data
-->
## URL フォームエンコードされたデータの送信

<!--
To post url-form-encoded data a `Map[String, Seq[String]]` needs to be passed into post()
-->
URL フォームエンコードされたデータを送信するためには、`Map[String, Seq[String]]` を post() に渡す必要があります。

```scala
WS.url(url).post(Map("key" -> Seq("value")))
```
<!--
> **Next:** [[OpenID Support in Play | ScalaOpenID]]
-->
> **次ページ:** [[Play の OpenID サポート | ScalaOpenID]]