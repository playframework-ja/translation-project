<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
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
To use WS, first add `ws` to your `build.sbt` file:
-->
WS を使うには、まず最初に `ws` を `build.sbt` ファイルに追加します。

```scala
libraryDependencies ++= Seq(
  ws
)
```

<!--
Then import the following:
-->
そして、次のようにインポートします。

@[imports](code/ScalaWSSpec.scala)

<!--
To build an HTTP request, you start with `WS.url()` to specify the URL.
-->
HTTP リクエストを構築するために、　`WS.url()` を URL を設定して呼び出します。

@[simple-holder](code/ScalaWSSpec.scala)

<!--
This returns a [WSRequestHolder](api/scala/index.html#play.api.libs.ws.WS$$WSRequestHolder) that you can use to specify various HTTP options, such as setting headers.  You can chain calls together to construct complex requests.
-->
これは [WSRequestHolder](api/scala/index.html#play.api.libs.ws.WS$$WSRequestHolder) を返し、 ヘッダの設定のような様々な HTTP のオプションを設定するために使用します。　メソッド呼び出しを連鎖して、複雑なリクエストの構築をまとめることができます。

@[complex-holder](code/ScalaWSSpec.scala)

<!--
You end by calling a method corresponding to the HTTP method you want to use.  This ends the chain, and uses all the options defined on the built request in the `WSRequestHolder`.
-->
使用したい HTTP メソッドに対応するメソッドを最後に呼びだします。　これで連鎖が終了し、 `WSRequestHolder`　のリクエストに設定した全てのオプションが使用されます。

@[holder-get](code/ScalaWSSpec.scala)

This returns a `Future[WSResponse]` where the [Response](api/scala/index.html#play.api.libs.ws.WSResponse) contains the data returned from the server.

<!--
### Request with authentication
-->
### 認証の設定

<!--
If you need to use HTTP authentication, you can specify it in the builder, using a username, password, and an [AuthScheme](api/scala/index.html#play.api.libs.ws.WSAuthScheme).  Valid case objects for the AuthScheme are `BASIC`, `DIGEST`, `KERBEROS`, `NONE`, `NTLM`, and `SPNEGO`.
-->
HTTP認証を使う必要があるなら、 ビルダーにユーザー名、パスワード、 [AuthScheme](api/scala/index.html#play.api.libs.ws.WSAuthScheme) を設定します。 AuthScheme に適用可能なケースオブジェクトは、`BASIC` , `DIGEST` , `KERBEROS` , `NONE` , `NTLM` そして `SPNEGO` です。


@[auth-request](code/ScalaWSSpec.scala)

<!--
### Request with follow redirects
-->
### リダイレクトの設定

<!--
If an HTTP call results in a 302 or a 301 redirect, you can automatically follow the redirect without having to make another call.
-->
もし、 HTTP 呼び出しの結果が、 302 や 301 のようなリダイレクトであるなら、 他のメソッド呼び出しをしなくとも自動的にリダイレクトされます。

@[redirects](code/ScalaWSSpec.scala)

<!--
### Request with query parameters
-->
### クエリストリングの設定

<!--
Parameters can be specified as a series of key/value tuples.
-->
パラメーターは、 キー/値のタプルをつなげて設定することもできます

@[query-string](code/ScalaWSSpec.scala)

<!--
### Request with additional headers
-->
### ヘッダの設定

<!--
Headers can be specified as a series of key/value tuples.
-->
ヘッダは、キー/値のタプルをつなげて設定します。

@[headers](code/ScalaWSSpec.scala)

<!--
If you are sending plain text in a particular format, you may want to define the content type explicitly.
-->
もし、　プレーンなテキストを特定のフォーマットで送信したいのなら、 コンテントタイプを明示的に設定する必要があります。

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
If you wish to specify a request timeout, you can use `withRequestTimeout` to set a value in milliseconds.
-->
リクエストにタイムアウトを設定するなら、　`withRequestTimeout`　を使用しミリ秒で値を設定します。

@[request-timeout](code/ScalaWSSpec.scala)

<!--
### Submitting form data
-->
### フォームデータの送信

<!--
To post url-form-encoded data a `Map[String, Seq[String]]` needs to be passed into `post`.
-->
フォームエンコードされたデータを POST で送信するには、 `post` に `Map[String, Seq[String]]` を渡す必要があります。

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
Whenever an operation is done on a `Future`, an implicit execution context must be available - this declares which thread pool the callback to the future should run in.  The default Play execution context is often sufficient:
-->
`Future` 上で処理を実行するときにはいつでも、 暗黙的な実行コンテキストが必要となります。 実行コンテキストとは futruer が実行されてコールバックを行うスレッドプールのことです 。 Play のデフォルトの実行コンテキストを使えば、通常では充分でしょう。

@[scalaws-context](code/ScalaWSSpec.scala)

<!-- 
The examples also use the folowing case class for serialization / deserialization:
-->
次の例も、以降のコードで使用するシリアライゼーション/デシリアラーゼーションのためのケースクラスです。

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
`WS` lets you use the response incrementally by using an [[iteratee|Iteratees]].  The `stream()` and `getStream()` methods on `WSRequestHolder` return `Future[(WSResponseHeaders, Enumerator[Array[Byte]])]`.  The enumerator contains the response body.
-->
`WS` では　インクリメンタルなレスポンスを [[iteratee|Iteratees]] によって扱うことができます。　`WSRequestHolder` の `stream()` と `getStream()` メソッドは` Future[(WSResponseHeaders, Enumerator[Array[Byte]])]` を返します。 Enumerator には　レスポンスボディが含まれています。

<!--
Here is a trivial example that uses an iteratee to count the number of bytes returned by the response:
-->
Iteratee を使用して、レスポンスによって返却されたバイト数を数える、ささいな例を示します。


@[stream-count-bytes](code/ScalaWSSpec.scala)

<!--
Of course, usually you won't want to consume large bodies like this, the more common use case is to stream the body out to another location.  For example, to stream the body to a file:
-->
もちろん、通常はこのような方法で大きなボディを消費したくはないでしょう。　より一般的な使用方法は、ボディを別の場所にストリームによって出力することです。　次の例では、ボディをファイルにストリームで出力します。

@[stream-to-file](code/ScalaWSSpec.scala)

<!--
Another common destination for response bodies is to stream them through to a response that this server is currently serving:
-->
レスポンスボディの行き先として他にありうるのは、　このサーバーが現在処理しているレスポンスへのストリームによる出力です。

@[stream-to-result](code/ScalaWSSpec.scala)

<!--
`POST` and `PUT` calls require manually calling the `withMethod` method, eg:
-->
`POST` と `PUT` は、 `withMethod` メソッドを明示的に呼び出す必要があります。例を示します。

@[stream-put](code/ScalaWSSpec.scala)

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
When making a request from a controller, you can map the response to a `Future[Result]`.  This can be used in combination with Play's `Action.async` action builder, as described in [[Handling Asynchronous Results|ScalaAsync]].
-->
コントローラーからリクエストを作成するとき、　レスポンスを `Future[Result]` へマッピングできます。　これは　Play の　`Action.async` アクションビルダーと組み合わせることで使用できます。 詳細は [[非同期レスポンスの処理|ScalaAsync]] にあります。

@[async-result](code/ScalaWSSpec.scala)

<!--
## Using WSClient
-->
## WSClient　を使用する

<!--
WSClient is a wrapper around the underlying AsyncHttpClient.  It is useful for defining multiple clients with different profiles, or using a mock.
-->
WSClient　は内部的な　AsyncHttpClient　を包むラッパーです。　いくつかのプロパティを指定した複数のクライアントを定義したり、モックを使用できたりする点で有用です。

<!--
The default client can be called from the WS singleton:
-->
デフォルトのクライアントは WS シングルトンから呼び出すことができます。

@[get-client](code/ScalaWSSpec.scala)

<!--
You can define a WS client directly from code without going through WS, and use implicitly with `WS.clientUrl()`
-->
WS　クライアントを、　WS を介さず直接コードから定義することができます。　その場合、暗黙的なクライアントと一緒に`WS.clientUrl()` を使用します。

@[implicit-client](code/ScalaWSSpec.scala)

<!--
> NOTE: if you instantiate a NingWSClient object, it does not use the WS plugin system, and so will not be automatically closed in `Application.onStop`. Instead, the client must be manually shutdown using `client.close()` when processing has completed.  This will release the underlying ThreadPoolExecutor used by AsyncHttpClient.  Failure to close the client may result in out of memory exceptions (especially if you are reloading an application frequently in development mode).
-->
> 注意: もし NingWSClient オブジェクトをインスタンス化した場合、　それは WS のプラグインシステムを使用していません。　そのため、　`Application.onStop` の実行時に自動で閉じられません。　その代わり、　クライアントは処理が完了した際に、 `client.close()` を用いて手動でシャットダウンする必要があります。　これは　AsyncHttpClient　が内部で使用している ThreadPoolExecutor を解放します。　クライアントを閉じることを行わないと、(開発モードで頻繁にアプリケーションをリロードしている場合は、特に)アウトオブメモリー例外を引き起こすかもしれません。

<!--
or directly:
-->
あるいは、直接的に使用します。

@[direct-client](code/ScalaWSSpec.scala)

<!--
Or use a magnet pattern to match up certain clients automatically:
-->
また、　適切なクライアントを自動的に結びつけるマグネットパターンを使用できます。

@[pair-magnet](code/ScalaWSSpec.scala)

<!--
By default, configuration happens in `application.conf`, but you can also set up the builder directly from configuration:
-->
デフォルトでは、 設定は `application.conf` で行われますが、 設定から直接ビルダーを構築することも可能です。

@[programmatic-config](code/ScalaWSSpec.scala)

<!--
You can also get access to the underlying [async client](http://sonatype.github.io/async-http-client/apidocs/reference/com/ning/http/client/AsyncHttpClient.html).
-->
内部的な [非同期クライアント](http://sonatype.github.io/async-http-client/apidocs/reference/com/ning/http/client/AsyncHttpClient.html) に触ることも可能です。

@[underlying](code/ScalaWSSpec.scala)

<!--
This is important in a couple of cases.  WS has a couple of limitations that require access to the client:
-->
重要な事柄が2点あります。　WS はクライアントへのアクセス要求時に2つの制限があります。

<!--
* `WS` does not support multi part form upload directly.  You can use the underlying client with [RequestBuilder.addBodyPart](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/RequestBuilder.html).
* `WS` does not support streaming body upload.  In this case, you should use the `FeedableBodyGenerator` provided by AsyncHttpClient.
-->
* `WS` はマルチパートのフォームのアップロードを直接サポートしていません。　内部的なクライアントの [RequestBuilder.addBodyPart](http://asynchttpclient.github.io/async-http-client/apidocs/com/ning/http/client/RequestBuilder.html) を使用してください。
* `WS` はストリームによるボディのアップロードをサポートしていません。　この場合は、　AsyncHttpClient　によって提供される　`FeedableBodyGenerator` を使用してください。

<!--
## Configuring WS
-->
## WS の設定

<!--
Use the following properties in `application.conf` to configure the WS client:
-->
WS クライアントの設定は、 `application.conf` にある以下のプロパティで行います。

<!--
* `ws.followRedirects`: Configures the client to follow 301 and 302 redirects *(default is **true**)*.
* `ws.useProxyProperties`: To use the system http proxy settings(http.proxyHost, http.proxyPort) *(default is **true**)*. 
* `ws.useragent`: To configure the User-Agent header field.
* `ws.compressionEnabled`: Set it to true to use gzip/deflater encoding *(default is **false**)*.
-->
* `ws.followRedirects`: 301、および、302 でのリダイレクトにクライアントが従うかを設定します。 *(デフォルトは **true**)*
* `ws.useProxyProperties`: システムのHTTPプロキシ設定(http.proxyHost, http.proxyPort)を使用するか設定します。 *(デフォルトは **true**)* 
* `ws.useragent`: User-Agent ヘッダーフィールドを設定します。
* `ws.compressionEnabled`: このプロパティが true の場合 gzip/deflater によるエンコーディングを行います。 *(デフォルトは **false**)*. 

<!--
### Configuring WS with SSL
-->
### SSL を用いた WS の設定

<!--
To configure WS for use with HTTP over SSL/TLS (HTTPS), please see [[Configuring WS SSL|WsSSL]].
-->
HTTP オーバー SSL/TLS (HTTPS) を使用するための WS の設定については、 [[WS SSLの設定]]　を参照してください。

<!--
### Configuring Timeouts
-->
### タイムアウトの設定

<!--
There are 3 different timeouts in WS. Reaching a timeout causes the WS request to interrupt.
-->
WS には3種類のタイムアウト設定があります。 タイムアウトになると、WS リクエストに割り込みが発生します。

<!--
* `ws.timeout.connection`: The maximum time to wait when connecting to the remote host *(default is **120 seconds**)*.
* `ws.timeout.idle`: The maximum time the request can stay idle (connection is established but waiting for more data) *(default is **120 seconds**)*.
* `ws.timeout.request`: The total time you accept a request to take (it will be interrupted even if the remote host is still sending data) *(default is **none**, to allow stream consuming)*.
-->

* `ws.timeout.connection`: リモートホストとの接続を行う最大の時間です。 *( デフォルトは **120秒** )* 
* `ws.timeout.idle`: アイドル状態(コネクションは確立したが、データを待っている状態)を保持する最大の時間です。 *( デフォルトは **120秒** )*
* `ws.timeout.request`: リクエストにかかる全ての時間(リモートホストがデータを送信中であっても、割り込みが発生します)です。 *(ストリームによる処理を許容するため、デフォルトは **none** )*

<!--
The request timeout can be overridden for a specific connection with `withRequestTimeout()` (see "Making a Request" section).
-->
リクエストのタイムアウトは　`withRequestTimeout()`　を使用した接続において上書き可能です。("リクエストの作成"の節を参照してください。)

<!--
> **Next:** [[OpenID Support in Play|ScalaOpenID]]
-->
> **Next:** [[Play の OpenID サポート |ScalaOpenID]]
