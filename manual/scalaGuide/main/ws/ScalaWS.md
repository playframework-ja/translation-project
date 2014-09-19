<!--
# The Play WS API
-->
# Play WS API

<!--
Sometimes we would like to call other HTTP services from within a Play application. Play supports this via its [WS library](api/scala/index.html#play.api.libs.ws.package), which provides a way to make asynchronous HTTP calls.
-->
ときどき、Play アプリケーションから他の HTTP サービスを呼び出したくなることがあります。Play は非同期の HTTP 呼び出しを実現する [WS ライブラリ](api/scala/index.html#play.api.libs.ws.package) でこれをサポートしています。

<!--
There are two important parts to using the WS API: making a request, and processing the response.  We'll discuss how to make both GET and POST HTTP requests first, and then show how to process the response from WS.  Finally, we'll discuss some common use cases.
-->
WS API には、リクエストの作成とレスポンスの処理という2つの重要な部品があります。 まず、 GET および　POST の HTTP リクエストを作成する方法について紹介し、 次に WS からレスポンスを処理する方法について紹介します。 最後に、よくあるユースケースを紹介します。

<!--
## Making a Request
-->
## リクエストの作成

<!--
To use WS, first import the following:
-->
WS を使うには、まず以下のインポートを行います。

```scala
import play.api.libs.ws._
import scala.concurrent.Future
```

<!--
To build an HTTP request, you start with `WS.url()` to specify the URL.
-->
HTTP リクエストを構築するために、　`WS.url()` を URL を設定して呼び出します。

```scala
val holder : WSRequestHolder = WS.url(url)
```

<!--
This returns a [WSRequestHolder](api/scala/index.html#play.api.libs.ws.WS$$WSRequestHolder) that you can use to specify various HTTP options, such as setting headers.  You can chain calls together to construct complex requests.
-->
これは [WSRequestHolder](api/scala/index.html#play.api.libs.ws.WS$$WSRequestHolder) を返し、 ヘッダの設定のような様々な HTTP のオプションを設定するために使用します。　メソッド呼び出しを連鎖して、複雑なリクエストの構築をまとめることができます。

```scala
val complexHolder : WSRequestHolder = holder.withHeaders(...)
                                            .withTimeout(...)
                                            .withQueryString(...)
```

<!--
You end by calling a method corresponding to the HTTP method you want to use.  This ends the chain, and uses all the options defined on the built request in the `WSRequestHolder`.
-->
使用したい HTTP メソッドに対応するメソッドを最後に呼びだします。　これで連鎖が終了し、 `WSRequestHolder`　のリクエストに設定した全てのオプションが使用されます。

```scala
val futureResponse : Future[Response] = complexHolder.get()
```

<!--
This returns a `Future[Response]` where the [Response](api/scala/index.html#play.api.libs.ws.Response) contains the data returned from the server.
-->
これは、 `Future[Response]` を返し、 [Response](api/scala/index.html#play.api.libs.ws.Response) にはサーバーから返されるデータが含まれます。

<!--
### Request with authentication
-->
### 認証の設定

<!--
If you need to use HTTP authentication, you can specify it in the builder, using a username, password, and an [AuthScheme](http://sonatype.github.io/async-http-client/apidocs/reference/com/ning/http/client/Realm.AuthScheme.html).  Options for the AuthScheme are `BASIC`, `DIGEST`, `KERBEROS`, `NONE`, `NTLM`, and `SPNEGO`.
-->
もしHTTP認証が必要なら、 ユーザー名、パスワード、および、 [AuthScheme](http://sonatype.github.io/async-http-client/apidocs/reference/com/ning/http/client/Realm.AuthScheme.html) をビルダーに設定します。 AuthScheme　のオプションは、 `BASIC`、 `DIGEST`、 `KERBEROS`、 `NONE`、`NTLM`、 `SPNEGO` があります。

```scala
import com.ning.http.client.Realm.AuthScheme

WS.url(url).withAuth(user, password, AuthScheme.BASIC).get()
```

<!--
### Request with follow redirects
-->
### リダイレクトの設定

<!--
If an HTTP call results in a 302 or a 301 redirect, you can automatically follow the redirect without having to make another call.
-->
もし、 HTTP 呼び出しの結果が、 302 や 301 のようなリダイレクトであるなら、 他のメソッド呼び出しをしなくとも自動的にリダイレクトされます。

```scala
WS.url(url).withFollowRedirects(true).get()
```

<!--
### Request with query parameters
-->
### クエリストリングの設定

<!--
Parameters can be specified as a series of key/value tuples.
-->
パラメーターは、 キー/値のタプルをつなげて設定することもできます

```scala
WS.url(url).withQueryString("paramKey" -> "paramValue").get()
```

<!--
### Request with additional headers
-->
### ヘッダの設定

<!--
Headers can be specified as a series of key/value tuples.
-->
ヘッダは、キー/値のタプルをつなげて設定します。

```scala
WS.url(url).withHeaders("headerKey" -> "headerValue").get()
```

<!--
If you are sending plain text in a particular format, you may want to define the content type explicitly.
-->
もし、　プレーンなテキストを特定のフォーマットで送信したいのなら、 コンテントタイプを明示的に設定する必要があります。

```scala
WS.url(url).withHeaders("Content-Type" -> "text-xml").post(xmlString)
```

<!--
### Request with virtual host
-->
### バーチャルホストの設定

<!--
A virtual host can be specified as a string.
-->
バーチャルホストは文字列で設定します。

```scala
WS.url(url).withVirtualHost("192.168.1.1").get()
```

<!--
### Request with time out
-->
### タイムアウトの設定

<!--
If you need to give a server more time to process, you can use `withTimeout` to set a value in milliseconds.  You may want to use this for extremely large files.
-->
もし、サーバーでの処理に要する時間が多くなるなら、 `withTimeout`　をミリ秒で設定することができます。 巨大なファイルを扱う場合などに使うことができます。


```scala
WS.url(url).withTimeout(1000).get()
```

<!--
### Submitting form data
-->
### フォームデータの送信

<!--
To post url-form-encoded data a `Map[String, Seq[String]]` needs to be passed into `post`.
-->
フォームエンコードされたデータを POST で送信するには、 `post` に `Map[String, Seq[String]]` を渡す必要があります。

```scala
WS.url(url).post(Map("key" -> Seq("value")))
```

<!--
### Submitting JSON data
-->
### JSON データの送信

<!--
The easiest way to post JSON data is to use the [[JSON|ScalaJson]] library.
-->
JSON データを送信する最も簡単な方法は、 [[JSON|ScalaJson]] ライブラリを使うことです。

@[scalaws-post-json](code/ScalaWSSpec.scala)

<!--
### Submitting XML data
-->
### XML データの送信

<!--
The easiest way to post XML data is to use XML literals.  XML literals are convenient, but not very fast.  For efficiency, consider using an XML view template, or a JAXB library.
-->
XML データを送信する最も簡単な方法は、 XML リテラルを使う事です。 XML リテラルは便利ですが、　それほど速くはありません。　効率を重視するなら、 XML ビューテンプレート や JAXB ライブラリを使う事を検討してください。

@[scalaws-post-xml](code/ScalaWSSpec.scala)


<!--
## Processing the Response
-->
## レスポンスの処理

<!--
Working with the [Response](api/scala/index.html#play.api.libs.ws.Response) is easily done by mapping inside the [Future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future).
-->
[Response](api/scala/index.html#play.api.libs.ws.Response)　に対する操作は [Future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future) の中でマッピングをすることで簡単に行えます。

<!--
The examples given below have some common dependencies that will be shown once here for brevity.
-->
以降の例には、いくつか共通の依存コードがあります。簡潔にするため、ここで1度だけ掲載します。

<!--
An execution context, required for Future.map:
-->
Future.map が必要とする実行時コンテキストです。

@[scalaws-context](code/ScalaWSSpec.scala)

<!--
and a case class that will be used for serialization / deserialization:
-->
シリアライゼーション/デシリアラーゼーションで使用するケースクラスです。

@[scalaws-person](code/ScalaWSSpec.scala)

<!--
### Processing a response as JSON
-->
### JSON レスポンスの処理

<!--
You can process the response as a [JSON object](api/scala/index.html#play.api.libs.json.JsValue) by calling `response.json`.
-->
レスポンスを [JSON オブジェクト](api/scala/index.html#play.api.libs.json.JsValue) として処理するには、 `response.json` を呼び出します。

@[scalaws-process-json](code/ScalaWSSpec.scala)

<!--
The JSON library has a [[useful feature|ScalaJsonCombinators]] that will map an implicit [`Reads[T]`](api/scala/index.html#play.api.libs.json.Reads) directly to a class:
-->
JSON ライブラリには、暗黙の [`Reads[T]`](api/scala/index.html#play.api.libs.json.Reads) をクラスに直接マッピングする [[便利な機能|ScalaJsonCombinators]] があります。

@[scalaws-process-json-with-implicit](code/ScalaWSSpec.scala)

<!--
### Processing a response as XML
-->
### XML レスポンスの処理

<!--
You can process the response as an [XML literal](http://www.scala-lang.org/api/current/index.html#scala.xml.NodeSeq) by calling `response.xml`.
-->
レスポンスを　[XML リテラル](http://www.scala-lang.org/api/current/index.html#scala.xml.NodeSeq) として処理するには、 `response.xml` を呼び出します。

@[scalaws-process-xml](code/ScalaWSSpec.scala)

<!--
### Processing large responses
-->
### 巨大なレスポンスの処理

<!--
Calling `get()` or `post()` will cause the body of the request to be loaded into memory before the response is made available.  When you are downloading with large, multi-gigabyte files, this may result in unwelcome garbage collection or even out of memory errors.
-->
`get()` や `post()` を実行すると、レスポンスが使用可能になる前に、リクエストの本体をメモリに読込みます。 数ギガバイトのファイルのような大量のダウンロードを行うと、　不愉快なガベージコレクションや、アウトオブメモリーエラーを招くかもしれません。

<!--
`WS` lets you use the response incrementally by using an [[iteratee|Iteratees]].
-->
`WS` では、 [[Iteratee|Iteratees]] を使うことにより、レスポンスをインクリメンタルに扱うことができます。

@[scalaws-fileupload](code/ScalaWSSpec.scala)

<!--
This is an iteratee that will receive a portion of the file as an array of bytes, write those bytes to an OutputStream, and close the stream when it receives the `EOF` signal.  Until it receives an `EOF` signal, the iteratee will keep running.
-->
Iteratee は ファイルの一部をバイト配列として受け取り、それを OutputStream に書き込みます。 また、 `EOF` シグナルを受け取ると　ストリームを閉じます。　`EOF` シグナルを受け取るまで、 Iteratee は実行され続けます。

<!--
`WS` doesn't send `EOF` to the iteratee when it's finished -- instead, it redeems the returned future.
In fact, `WS` has no right to feed `EOF`, since it doesn't control the input.  You may want to feed the result of multiple WS calls into that iteratee (maybe you're building a tar file on the fly), and if `WS` feeds `EOF`, the stream will close unexpectedly.  Sending `EOF` to the stream is the caller's responsibility.
-->
`WS` は終了時に `EOF` を Iteratee に送信しません。　その代わりに、 Future　を返すようにしています。
実際、 `WS` は入力の制御を行わないため、 `EOF` を送信するべきではありません。 複数の WS の呼び出し (その場で tar ファイルを構築するような) を Iteratee に送信したくなるかもしれません。 そして、ここでもし `WS` が `EOF` を送信するなら、 ストリームは期待したとおりに閉じられません。 `EOF` の送信は呼び出し側の責任です。

<!--
We do this by calling [Iteratee.run](http://www.playframework.com/documentation/2.1.x/api/scala/index.html#play.api.libs.iteratee.Iteratee) which will push an `EOF` into the iteratee when the future is redeemed.
-->
[Iteratee.run](http://www.playframework.com/documentation/2.1.x/api/scala/index.html#play.api.libs.iteratee.Iteratee) を呼び出す事で　Future　が実行されるときに、 Iteratee に `EOF` が送信されます。

<!--
`POST` and `PUT` calls use a slightly different API than `GET` calls: instead of `post()`, you call `postAndRetrieveStream(body)` which has the same effect.
-->
`POST` と `PUT` の呼び出しでは、　`GET` の方法とは少し違う API を使います。 `post()` の代わりに、 同じ動作をする `postAndRetrieveStream(body)` を使用してください。

```scala
WS.url(url).postAndRetrieveStream(body) { headers =>
  Iteratee.foreach { bytes => logger.info("Received bytes: " + bytes.length) }
}
```

<!--
## Common Patterns and Use Cases
-->
## 共通パターンとユースケース

<!--
### Chaining WS calls
-->
### WS 呼び出しの連結

<!--
Using for comprehensions is a good way to chain WS calls in a trusted environment.  You should use for comprehensions together with [Future.recover](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future) to handle possible failure.
-->
for 内包を使うのは、 信頼できる環境で WS の呼び出しを連結する良い方法です。 起こりうる失敗に対応するために、 for 内包と一緒に　[Future.recover](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future)　を使用してください。

@[scalaws-forcomprehension](code/ScalaWSSpec.scala)

<!--
### Using in a controller
-->
### コントローラーでの使用

<!--
You can compose several promises and end with a `Future[Result]` that can be handled directly by the Play server, using the `Async` method defined in [[Handling Asynchronous Results|ScalaAsync]].
-->
[[非同期レスポンスの処理|ScalaAsync]] で定義されている `Async` メソッドを使うと、 Promise の構成と `Future[Result]` の終了を Play サーバーが直接ハンドルします。

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
## Advanced Usage
-->
## 高度な使用方法

<!--
You can also get access to the underlying [async client](http://sonatype.github.io/async-http-client/apidocs/reference/com/ning/http/client/AsyncHttpClient.html).
-->
基礎的な [非同期クライアント](http://sonatype.github.io/async-http-client/apidocs/reference/com/ning/http/client/AsyncHttpClient.html) に触ることも可能です。


```scala
import com.ning.http.client.AsyncHttpClient

val client:AsyncHttpClient = WS.client
```

<!--
This is important in a couple of cases.  WS has a couple of limitations that require access to the client:
-->
重要な事柄が2点あります。　WS はクライアントへのアクセス要求時に2つの制限があります。

<!--
* `WS` does not support multi part form upload directly.  You can use the underlying client with [RequestBuilder.addBodyPart](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/RequestBuilder.html).
* `WS` does not support client certificates (aka mutual TLS / MTLS / client authentication).  You should set the `SSLContext` directly in an instance of [AsyncHttpClientConfig](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/AsyncHttpClientConfig.html) and set up the appropriate KeyStore and TrustStore.
-->
* `WS` はマルチパートのフォームアップロードを直接サポートしません。 基礎的なクライアントの [RequestBuilder.addBodyPart](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/RequestBuilder.html) を使う事ができます。
* `WS` は (相互 TLS / MTLS / クライアント認証として知られている) クライアント証明書をサポートしません。  [AsyncHttpClientConfig](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/AsyncHttpClientConfig.html) のインスタンスに、 `SSLContext` を直接設定し、 適切なキーストアとトラストストアを設定します。

<!--
## Configuring WS 
-->
## WS の設定

<!--
Use the following properties to configure the WS client

* `ws.timeout` sets both the connection and request timeout in milliseconds
* `ws.followRedirects` configures the client to follow 301 and 302 redirects
* `ws.useProxyProperties`to use the system http proxy settings(http.proxyHost, http.proxyPort) 
* `ws.useragent` to configure the User-Agent header field
* `ws.acceptAnyCertificate` set it to false to use the default SSLContext
-->
WS クライアントの設定には、以下のプロパティを使います。

* `ws.timeout` は接続、および、リクエストタイムアウトの両方をミリ秒で設定します。
* `ws.followRedirects` は301、および、302 でのリダイレクトにクライアントが従うかを設定します。
* `ws.useProxyProperties` はシステムhttpプロキシ設定(http.proxyHost、http.proxyPort)を使用するかを設定します。
* `ws.useragent` は User-Agent ヘッダーフィールドを設定します。
* `ws.acceptAnyCertificate` はデフォルトの SSLContext を使用するため false を設定します。

<!--
### Timeouts
-->
### タイムアウト

<!--
There ore 3 different timeouts in WS. Reaching a timeout causes the WS request to interrupt.
-->
WS には3種類のタイムアウトがあります。 タイムアウトになると、 WS のリクエストに割り込みが発生します。

<!--
* **Connection Timeout**: The maximum time to wait when connecting to the remote host *(default is **120 seconds**)*.
* **Connection Idle Timeout**: The maximum time the request can stay idle (connexion is established but waiting for more data) *(default is **120 seconds**)*.
* **Request Timeout**: The total time you accept a request to take (it will be interrupted, whatever if the remote host is still sending data) *(default is **none**, to allow stream consuming)*.
-->
* **コネクションタイムアウト**: リモートホストとの接続を行う最大の時間です。 *( デフォルトは **120秒** )* 
* **コネクションアイドルタイムアウト**: アイドル状態(コネクションは確立したが、データを待っている状態)を保持する最大の時間です。 *( デフォルトは **120秒** )*
* **リクエストタイムアウト**: リクエストにかかる全ての時間(リモートホストがデータを送信中であっても、割り込みが発生します)です。 *(ストリームによる処理を許容するため、デフォルトは **なし** )*

<!--
You can define each timeout in `application.conf` with respectively: `ws.timeout.connection`, `ws.timeout.idle`, `ws.timeout.request`.
-->
各タイムアウトは `application.conf` の中の、それぞれ、 `ws.timeout.connection`、 `ws.timeout.idle`、 `ws.timeout.request`　に設定します。

<!--
Alternatively, `ws.timeout` can be defined to target both *Connection Timeout* and *Connection Idle Timeout*.
-->
あるいは、 `ws.timeout` で、 *コネクションタイムアウト* と *コネクションアイドルタイムアウト* の両方を設定できます。

<!--
The request timeout can be specified for a given connection with `withRequestTimeout`.
-->
リクエストタイムアウトは、 `withRequestTimeout` を使用してコネクションに設定する事ができます。

<!--
Example:
-->
例:

<!--
```scala
WS.url("http://playframework.org/").withRequestTimeout(10000 /* in milliseconds */)
```
-->
```scala
WS.url("http://playframework.org/").withRequestTimeout(10000 /* ミリ秒で */)
```

<!--
> **Next:** [[OpenID Support in Play|ScalaOpenID]]
-->
> **Next:** [[Play の OpenID サポート |ScalaOpenID]]
