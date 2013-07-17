<!-- translated -->
<!--
# The Play WS API
-->
# Play WS API

<!--
Sometimes we would like to call other HTTP services from within a Play application. Play supports this via its `play.api.libs.ws.WS` library, which provides a way to make asynchronous HTTP calls.
-->
ときどき、Play アプリケーションから他の HTTP サービスを呼び出したくなることがあります。Play は非同期の HTTP 呼び出しを実現する `play.api.libs.ws.WS` というライブラリでこれをサポートしています。

<!--
Any calls made by `play.api.libs.ws.WS` should return a `scala.concurrent.Future[play.api.libs.ws.Response]` which we can later handle with Play窶冱 asynchronous mechanisms.
-->
`play.api.libs.ws.WS` による全ての HTTP 通信は、Play の非同期処理のメカニズムで処理される `scala.concurrent.Future[play.api.libs.ws.Response]` を返します。

<!--
## Making an HTTP call
-->
## HTTP 通信の開始

<!--
To send an HTTP request you start with `WS.url()` to specify the URL. Then you get a builder that you can use to specify various HTTP options, such as setting headers. You end by calling a final method corresponding to the HTTP method you want to use. For example:
-->
HTTP リクエストを送信するためには、まず `WS.url()` を使って URL を指定します。その結果、例えばヘッダをセットする、といったような 各種 HTTP オプションを指定するためのビルダが返ってきます。最後に利用したい HTTP メソッドに対応するメソッドを呼び出します。例えば:

```scala
val homePage: Future[play.api.libs.ws.Response] = WS.url("http://mysite.com").get()
```

<!--
Or:
-->
または、以下のように記述します。

```scala
val result: Future[ws.Response] = {
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
HTTP 呼び出しは非同期で行われ、実際のコンテンツを取得するためには `Promise[ws.Response]` を操作する必要があります。また、複数の Promise を合成して、最終的に Play サーバが直接的に処理できるように `Promise[Result]`  を返す、という方法も使えます。

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
## Configuring WS client
-->

## WS クライアントの設定

<!--
Use the following properties to configure the WS client

* `ws.timeout` sets both the connection and request timeout in milliseconds
* `ws.followRedirects` configures the client to follow 301 and 302 redirects
* `ws.useProxyProperties`to use the system http proxy settings(http.proxyHost, http.proxyPort) 
* `ws.useragent` to configure the User-Agent header field
* `ws.acceptAnyCertificate` set it to fail to use the default SSLContext

You can also get access to the underlying client using `def client` method  
-->

WSクライアントの設定には、以下のプロパティを使います。

* `ws.timeout` は接続、および、リクエストタイムアウトの両方をミリ秒で設定します。
* `ws.followRedirects` は301、および、302 でのリダイレクトにクライアントが従うかを設定します。
* `ws.useProxyProperties` はシステムhttpプロキシ設定(http.proxyHost、http.proxyPort)を使用するかを設定します。
* `ws.useragent` は User-Agent ヘッダーフィールドを設定します。
* `ws.acceptAnyCertificate` はデフォルトのSSLContextを使用すると失敗するように設定します。

`def client` メソッドを使うと、クライアント本体にアクセスすることが可能になります。

<!--
> **Next:** [[OpenID Support in Play | ScalaOpenID]]
-->
> **次ページ:** [[Play の OpenID サポート | ScalaOpenID]]