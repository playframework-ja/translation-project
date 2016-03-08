<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# The Play WS API
-->
# The Play WS API

<!--
Sometimes we would like to call other HTTP services from within a Play application. Play supports this via its [WS library](api/scala/play/api/libs/ws/package.html), which provides a way to make asynchronous HTTP calls.
-->
Play アプリケーションから他の HTTP サービスを呼び出したくなることがあります。Play は非同期の HTTP 呼び出しを実現する [WS ライブラリ](api/scala/play/api/libs/ws/package.html) でこれをサポートしています。

<!--
There are two important parts to using the WS API: making a request, and processing the response.  We'll discuss how to make both GET and POST HTTP requests first, and then show how to process the response from WS.  Finally, we'll discuss some common use cases.
-->
WS API には、リクエストの作成とレスポンスの処理という2つの重要な部品があります。まず、GET および POST の HTTP リクエストを作成する方法について紹介し、次に WS からレスポンスを処理する方法について紹介します。最後に、よくあるユースケースを紹介します。

<!--
## Making a Request
-->
## リクエストの作成

<!--
To use WS, first add `ws` to your `build.sbt` file:
-->
WS を使用するには、まず `ws` を `build.sbt` ファイルに追加してください。

```scala
libraryDependencies ++= Seq(
  ws
)
```

<!--
Now any controller or component that wants to use WS will have to declare a dependency on the `WSClient`:
-->
そして、WS を利用したいコントローラやコンポーネントで `WSClient` への依存を宣言します。

@[dependency](code/ScalaWSSpec.scala)

<!--
We've called the `WSClient` instance `ws`, all the following examples will assume this name.
-->
`WSClient` のインスタンスを `ws` と名付けたので、以下の例ではこの名前を用いることとします。

<!--
To build an HTTP request, you start with `ws.url()` to specify the URL.
-->
HTTP リクエストを構築するには、 `WS.url()` を URL を設定して呼び出します。

@[simple-holder](code/ScalaWSSpec.scala)

<!--
This returns a [WSRequest](api/scala/play/api/libs/ws/WSRequest.html) that you can use to specify various HTTP options, such as setting headers. You can chain calls together to construct complex requests.
-->
これは [WSRequest](api/scala/play/api/libs/ws/WSRequest.html) を返します。WSRequest はヘッダの設定のような様々な HTTP のオプションを設定するために使用します。メソッド呼び出しを連鎖すると、複雑なリクエストをまとめて構築できます。

@[complex-holder](code/ScalaWSSpec.scala)


You end by calling a method corresponding to the HTTP method you want to use.  This ends the chain, and uses all the options defined on the built request in the `WSRequest`.

最後に、使用したい HTTP メソッドに対応するメソッドを呼び出します。連鎖はこれで終了し、 `WSRequest` のリクエストに設定した全てのオプションが使用されます。

@[holder-get](code/ScalaWSSpec.scala)

<!--
This returns a `Future[WSResponse]` where the [Response](api/scala/play/api/libs/ws/WSResponse.html) contains the data returned from the server.
-->
すると、 `Future[WSResponse]` が返されます。ここで [Response](api/scala/play/api/libs/ws/WSResponse.html) がサーバから返されたデータを保持しています。

<!--
### Request with authentication
-->
### 認証の設定

<!--
If you need to use HTTP authentication, you can specify it in the builder, using a username, password, and an [AuthScheme](api/scala/play/api/libs/ws/WSAuthScheme.html).  Valid case objects for the AuthScheme are `BASIC`, `DIGEST`, `KERBEROS`, `NONE`, `NTLM`, and `SPNEGO`.
-->
HTTP 認証を使う必要がある場合には、ビルダーにユーザー名、パスワード、 [AuthScheme](api/scala/play/api/libs/ws/WSAuthScheme.html) を設定します。AuthScheme に適用可能なケースオブジェクトは、`BASIC`, `DIGEST`, `KERBEROS`, `NONE`, `NTLM` そして `SPNEGO` です。

@[auth-request](code/ScalaWSSpec.scala)

<!--
### Request with follow redirects
-->
### リダイレクトの設定

<!--
If an HTTP call results in a 302 or a 301 redirect, you can automatically follow the redirect without having to make another call.
-->
もし、HTTP 呼び出しの結果が、302 や 301 のようなリダイレクトである場合には、他のメソッド呼び出しをしなくとも自動的にリダイレクトされます。

@[redirects](code/ScalaWSSpec.scala)

<!--
### Request with query parameters
-->
### クエリストリングの設定

<!--
Parameters can be specified as a series of key/value tuples.
-->
パラメーターは、キーと値のタプルで設定します。

@[query-string](code/ScalaWSSpec.scala)

<!--
### Request with additional headers
-->
### ヘッダの設定

<!--
Headers can be specified as a series of key/value tuples.
-->
ヘッダは、キーと値のタプルで設定します。

@[headers](code/ScalaWSSpec.scala)

<!--
If you are sending plain text in a particular format, you may want to define the content type explicitly.
-->
もし、プレーンなテキストを特定のフォーマットで送信したいのなら、コンテントタイプを明示的に設定する必要があります。

@[content-type](code/ScalaWSSpec.scala)

<!--
### Request with virtual host
-->
### バーチャルホストの設定

<!--
A virtual host can be specified as a string.
-->
バーチャルホストは文字列で設定します。

@[virtual-host](code/ScalaWSSpec.scala)

<!--
### Request with timeout
-->
### タイムアウトの設定

<!--
If you wish to specify a request timeout, you can use `withRequestTimeout` to set a value in milliseconds. A value of `-1` can be used to set an infinite timeout. 
-->
リクエストにタイムアウトを設定する場合には、 `withRequestTimeout` を使用しミリ秒で値を設定します。 `-1` を設定すると無期限になります。

@[request-timeout](code/ScalaWSSpec.scala)

<!--
### Submitting form data
-->
### フォームデータの送信

<!--
To post url-form-encoded data a `Map[String, Seq[String]]` needs to be passed into `post`.
-->
フォームエンコードされたデータを POST で送信するには、`post` に `Map[String, Seq[String]]` を渡してください。

@[url-encoded](code/ScalaWSSpec.scala)

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
XML データを送信する最も簡単な方法は、XML リテラルを使うことです。XML リテラルは便利ですが、それほど速くはありません。効率を重視するなら、XML ビューテンプレート や JAXB ライブラリを使うことを検討してください。

@[scalaws-post-xml](code/ScalaWSSpec.scala)

<!--
## Processing the Response
-->
## レスポンスの処理

<!--
Working with the [Response](api/scala/play/api/libs/ws/WSResponse.html) is easily done by mapping inside the [Future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future).
-->
[Response](api/scala/play/api/libs/ws/WSResponse.html) に対する操作は [Future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future) の中でマッピングをすることで簡単に行えます。

<!--
The examples given below have some common dependencies that will be shown once here for brevity.
-->
以降の例には、いくつか共通の依存コードがあります。簡潔にするため、ここで1度だけ掲載します。

<!--
Whenever an operation is done on a `Future`, an implicit execution context must be available - this declares which thread pool the callback to the future should run in.  The default Play execution context is often sufficient:
-->
`Future` 上で処理を実行するときにはいつでも、暗黙的な実行コンテキストが必要となります。実行コンテキストとは `Future` が実行されてコールバックを行うスレッドプールのことです 。通常は、Play のデフォルトの実行コンテキストで充分でしょう。

@[scalaws-context](code/ScalaWSSpec.scala)

<!--
The examples also use the folowing case class for serialization / deserialization:
-->
次の例も、以降のコードで使用するシリアライゼーション、あるいはデシリアラーゼーションのためのケースクラスです。

@[scalaws-person](code/ScalaWSSpec.scala)

<!--
### Processing a response as JSON
-->
### JSON レスポンスの処理

<!--
You can process the response as a [JSON object](api/scala/play/api/libs/json/JsValue.html) by calling `response.json`.
-->
レスポンスを [JSON object](api/scala/play/api/libs/json/JsValue.html) として処理するには、`response.json` を呼び出します。

@[scalaws-process-json](code/ScalaWSSpec.scala)

<!--
The JSON library has a [[useful feature|ScalaJsonCombinators]] that will map an implicit [`Reads[T]`](api/scala/play/api/libs/json/Reads.html) directly to a class:
-->
JSON ライブラリには、暗黙の [`Reads[T]`](api/scala/play/api/libs/json/Reads.html) をクラスに直接マッピングする [[便利な機能|ScalaJsonCombinators]] があります。

@[scalaws-process-json-with-implicit](code/ScalaWSSpec.scala)

<!--
### Processing a response as XML
-->
### XML レスポンスの処理

<!--
You can process the response as an [XML literal](http://www.scala-lang.org/api/current/index.html#scala.xml.NodeSeq) by calling `response.xml`.
-->
レスポンスを [XML リテラル](http://www.scala-lang.org/api/current/index.html#scala.xml.NodeSeq) として処理するには、`response.xml` を呼び出します。

@[scalaws-process-xml](code/ScalaWSSpec.scala)

<!--
### Processing large responses
-->
### 巨大なレスポンスの処理

<!--
Calling `get()` or `post()` will cause the body of the request to be loaded into memory before the response is made available.  When you are downloading with large, multi-gigabyte files, this may result in unwelcome garbage collection or even out of memory errors.
-->
`get()` や `post()` を実行すると、レスポンスが使用可能になる前に、リクエストボディをメモリに読み込みます。数ギガバイトのファイルのような大量のダウンロードを行うと、不愉快なガベージコレクション、ひいてはアウトオブメモリーエラーを招くかもしれません。

<!--
`WS` lets you use the response incrementally by using an [[iteratee|Iteratees]].  The `stream()` and `getStream()` methods on `WSRequest` return `Future[(WSResponseHeaders, Enumerator[Array[Byte]])]`.  The enumerator contains the response body.
-->
WS では インクリメンタルなレスポンスを [[iteratee|Iteratees]] によって扱うことができます。`WSRequest` の `stream()` と `getStream()` メソッドは `Future[(WSResponseHeaders, Enumerator[Array[Byte]])]` を返します。enumerator には レスポンスボディが含まれています。

<!--
Here is a trivial example that uses an iteratee to count the number of bytes returned by the response:
-->
iteratee を使用して、レスポンスによって返却されたバイト数を数える、ちょっとした例を示します。

@[stream-count-bytes](code/ScalaWSSpec.scala)

<!--
Of course, usually you won't want to consume large bodies like this, the more common use case is to stream the body out to another location.  For example, to stream the body to a file:
-->
もちろん、通常はこのような方法で大きなボディを消費したくはないでしょう。より一般的な使用方法は、ボディを別の場所にストリームによって出力することです。次の例では、ボディをファイルにストリームで出力します。

@[stream-to-file](code/ScalaWSSpec.scala)

<!--
Another common destination for response bodies is to stream them through to a response that this server is currently serving:
-->
レスポンスボディの送り先としては他に、このサーバーが現在処理しているレスポンスへのストリームによる出力があります。

@[stream-to-result](code/ScalaWSSpec.scala)

<!--
`POST` and `PUT` calls require manually calling the `withMethod` method, eg:
-->
`POST` と `PUT` は、 `withMethod` メソッドを明示的に呼び出す必要があります。

@[stream-put](code/ScalaWSSpec.scala)

<!--
## Common Patterns and Use Cases
-->
## よくある例と使用方法

<!--
### Chaining WS calls
-->
### WS 呼び出しの連結

<!--
Using for comprehensions is a good way to chain WS calls in a trusted environment.  You should use for comprehensions together with [Future.recover](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future) to handle possible failure.
-->
for 内包表記は、信頼された環境で WS の呼び出しを連結するのに適しています。起こりうる失敗を処理するには、for 内包表記と一緒に [Future.recover](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future) を使用してください。

@[scalaws-forcomprehension](code/ScalaWSSpec.scala)

<!--
### Using in a controller
-->
### コントローラーでの使用

<!--
When making a request from a controller, you can map the response to a `Future[Result]`. This can be used in combination with Play's `Action.async` action builder, as described in [[Handling Asynchronous Results|ScalaAsync]].
-->
コントローラーからリクエストを作成するときには、レスポンスを `Future[Result]` へマッピングできます。これは Play の `Action.async` アクションビルダーと組み合わせることで使用できます。詳細は [[非同期レスポンスの処理|ScalaAsync]] にあります。

@[async-result](code/ScalaWSSpec.scala)

<!--
## Using WSClient
-->
## WSClient を使用する

<!--
WSClient is a wrapper around the underlying AsyncHttpClient.  It is useful for defining multiple clients with different profiles, or using a mock.
-->
WSClient は AsyncHttpClient を包むラッパーです。別々のプロファイルを指定した複数のクライアントを定義したり、モックを使用できたりして便利です。

<!--
You can define a WS client directly from code without having it injected by WS, and then use it implicitly with `WS.clientUrl()`:
-->
WSによる注入を受けることなく、コードから直接 WS クライアントを定義することができます。そして、 `WS.clientUrl()` の際にはクライアントを暗黙的に使用することができます。

@[implicit-client](code/ScalaWSSpec.scala)

<!--
> NOTE: if you instantiate a NingWSClient object, it does not use the WS module lifecycle, and so will not be automatically closed in `Application.onStop`. Instead, the client must be manually shutdown using `client.close()` when processing has completed. This will release the underlying ThreadPoolExecutor used by AsyncHttpClient. Failure to close the client may result in out of memory exceptions (especially if you are reloading an application frequently in development mode).
-->
> NOTE: NingWSClient オブジェクトをインスタンス化した場合には、それは WS モジュールのライフサイクルに乗らないので、`Application.onStop` の際に自動的にはクローズされません。そのかわりに、処理が終了した際に `client.close()` することによりクライアントを手動でシャットダウンしなければなりません。これによって、AsyncHttpClient が内部で使用している ThreadPoolExecutor を開放します。クライアントを閉じることに失敗すると、アウトオブメモリー例外が生じるおそれがあります (特に、開発モードでアプリケーションを頻繁にリロードする場合)。

<!--
or directly:
-->
または直接、

@[direct-client](code/ScalaWSSpec.scala)

<!--
Or use a magnet pattern to match up certain clients automatically:
-->
あるいは、マグネットパターンを利用して、特定のクライアントを自動的に紐付けることもできます。

@[pair-magnet](code/ScalaWSSpec.scala)

<!--
By default, configuration happens in `application.conf`, but you can also set up the builder directly from configuration:
-->
デフォルトでは、設定は `application.conf` で行われますが、設定から直接ビルダーを構築することも可能です。

@[programmatic-config](code/ScalaWSSpec.scala)

<!--
You can also get access to the underlying [async client](https://static.javadoc.io/com.ning/async-http-client/1.9.20/com/ning/http/client/AsyncHttpClient.html).
-->
内部の [非同期クライアント](https://static.javadoc.io/com.ning/async-http-client/1.9.20/com/ning/http/client/AsyncHttpClient.html) を使用することも可能です。

@[underlying](code/ScalaWSSpec.scala)

<!--
This is important in a couple of cases.  WS has a couple of limitations that require access to the client:
-->
重要な事柄が 2 点あります。WS はクライアントへのアクセス要求時に 2 つの制限があります。

<!--
* `WS` does not support multi part form upload directly.  You can use the underlying client with [RequestBuilder.addBodyPart](https://static.javadoc.io/com.ning/async-http-client/1.9.20/com/ning/http/client/RequestBuilder.html#addBodyPart\(com.ning.http.client.multipart.Part\)).
* `WS` does not support streaming body upload.  In this case, you should use the `FeedableBodyGenerator` provided by AsyncHttpClient.
-->
* `WS` はマルチパートのフォームのアップロードを直接サポートしていません。内部的なクライアントの [RequestBuilder.addBodyPart](https://static.javadoc.io/com.ning/async-http-client/1.9.20/com/ning/http/client/RequestBuilder.html#addBodyPart\(com.ning.http.client.multipart.Part\)) を使用してください。
* `WS` はストリームによるボディのアップロードをサポートしていません。この場合、AsyncHttpClient によって提供される `FeedableBodyGenerator` を使用してください。

<!--
## Configuring WS
-->
## WS の設定

<!--
Use the following properties in `application.conf` to configure the WS client:
-->
WS クライアントの設定は、 `application.conf` にある以下のプロパティで行います。

<!--
* `play.ws.followRedirects`: Configures the client to follow 301 and 302 redirects *(default is **true**)*.
* `play.ws.useProxyProperties`: To use the system http proxy settings(http.proxyHost, http.proxyPort) *(default is **true**)*.
* `play.ws.useragent`: To configure the User-Agent header field.
* `play.ws.compressionEnabled`: Set it to true to use gzip/deflater encoding *(default is **false**)*.
-->
* `play.ws.followRedirects`: 301、および、302 でのリダイレクトにクライアントが従うかを設定します。 *(デフォルトは **true**)*
* `play.ws.useProxyProperties`: システムのHTTPプロキシ設定(http.proxyHost, http.proxyPort)を使用するか設定します。 *(デフォルトは **true**)*
* `play.ws.useragent`: User-Agent ヘッダーフィールドを設定します。
* `play.ws.compressionEnabled`: このプロパティが true の場合 gzip/deflater によるエンコーディングを行います。 *(デフォルトは **false**)*.

<!--
### Configuring WS with SSL
-->
### SSL を用いた WS の設定

<!--
To configure WS for use with HTTP over SSL/TLS (HTTPS), please see [[Configuring WS SSL|WsSSL]].
-->
HTTP over SSL/TLS (HTTPS) を使用するための WS の設定については、 [[WS SSLの設定|WsSSL]] を参照してください。

<!--
### Configuring Timeouts
-->
### タイムアウトの設定

<!--
There are 3 different timeouts in WS. Reaching a timeout causes the WS request to interrupt.
-->
WS には3種類のタイムアウト設定があります。タイムアウトになると、WS リクエストが中断します。

<!--
* `play.ws.timeout.connection`: The maximum time to wait when connecting to the remote host *(default is **120 seconds**)*.
* `play.ws.timeout.idle`: The maximum time the request can stay idle (connection is established but waiting for more data) *(default is **120 seconds**)*.
* `play.ws.timeout.request`: The total time you accept a request to take (it will be interrupted even if the remote host is still sending data) *(default is **120 seconds**)*.
-->
* `play.ws.timeout.connection`: リモートホストとの接続を行う最大の時間です。 *( デフォルトは **120秒** )*
* `play.ws.timeout.idle`: アイドル状態(コネクションは確立したが、データを待っている状態)を保持する最大の時間です。 *( デフォルトは **120秒** )*
* `play.ws.timeout.request`: リクエストにかかる全体の時間です(リモートホストがデータを送信中であっても、中断する可能性があります)。 *( デフォルトは **120秒** )*

<!--
The request timeout can be overridden for a specific connection with `withRequestTimeout()` (see "Making a Request" section).
-->
リクエストのタイムアウトは `withRequestTimeout()` を使用した接続において上書き可能です。 (“リクエストの作成”の節を参照してください。)

<!--
### Configuring AsyncHttpClientConfig
-->
### AsyncHttpClientConfig の設定

<!--
The following advanced settings can be configured on the underlying AsyncHttpClientConfig.
-->
内部のAsyncHttpClientConfig にて、以下のような高度な設定を行うことができます。

<!--
Please refer to the [AsyncHttpClientConfig Documentation](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/AsyncHttpClientConfig.Builder.html) for more information.
-->
詳しくは[AsyncHttpClientConfig のドキュメント](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/AsyncHttpClientConfig.Builder.html)を参照してください。

<!--
* `play.ws.ning.allowPoolingConnection`
* `play.ws.ning.allowSslConnectionPool`
* `play.ws.ning.ioThreadMultiplier`
* `play.ws.ning.maxConnectionsPerHost`
* `play.ws.ning.maxConnectionsTotal`
* `play.ws.ning.maxConnectionLifeTime`
* `play.ws.ning.idleConnectionInPoolTimeout`
* `play.ws.ning.webSocketIdleTimeout`
* `play.ws.ning.maxNumberOfRedirects`
* `play.ws.ning.maxRequestRetry`
* `play.ws.ning.disableUrlEncoding`
-->
* `play.ws.ning.allowPoolingConnection`
* `play.ws.ning.allowSslConnectionPool`
* `play.ws.ning.ioThreadMultiplier`
* `play.ws.ning.maxConnectionsPerHost`
* `play.ws.ning.maxConnectionsTotal`
* `play.ws.ning.maxConnectionLifeTime`
* `play.ws.ning.idleConnectionInPoolTimeout`
* `play.ws.ning.webSocketIdleTimeout`
* `play.ws.ning.maxNumberOfRedirects`
* `play.ws.ning.maxRequestRetry`
* `play.ws.ning.disableUrlEncoding`
