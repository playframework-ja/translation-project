<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# HTTP Request Handlers
-->
# HTTP リクエストハンドラ

<!--
Play provides a range of abstractions for routing requests to actions, providing routers and filters to allow most common needs.  Sometimes however an application will have more advanced needs that aren't met by Play's abstractions.  When this is the case, applications can provide custom implementations of Play's lowest level HTTP pipeline API, the [`HttpRequestHandler`](api/scala/play/api/http/HttpRequestHandler.html).
-->
Play は、多くの一般的な要求を満たすためのルータとフィルタを提供することで、リクエストをアクションにルーティングするためのさまざまな抽象化を提供します。しかし、アプリケーションによっては、Play の抽象化では満たされない、より高度なものを要求する場合もあります。この場合、アプリケーションは Play のもっとも低レベルな HTTP パイプライン API である [`HttpRequestHandler`](api/scala/play/api/http/HttpRequestHandler.html) の独自実装を提供できます。

<!--
Providing a custom `HttpRequestHandler` should be a last course of action.  Most custom needs can be met through implementing a custom router or a [[filter|ScalaHttpFilters]].
-->
独自の `HttpRequestHandler` の提供は、最後の手段にするべきです。特別な要求の多くは、独自のルータや [[filter|ScalaHttpFilters]] を実装することで満たせます。

<!--
## Implementing a custom request handler
-->
## カスタムリクエストハンドラの実装

<!--
The `HttpRequestHandler` trait has one method to be implemented, `handlerForRequest`.  This takes the request to get a handler for, and returns a tuple of a `RequestHeader` and a `Handler`.
-->
`HttpRequestHandler` トレイトは、実装されるべき 1 つのメソッド `handlerForRequest` を持ちます。これは、ハンドラを取得するリクエストを受け取り、`RequestHeader` と `Handler` のタプルを返します。

<!--
The reason why a request header is returned is so that information can be added to the request, for example, routing information.  In this way, the router is able to tag requests with routing information, such as which route matched the request, which can be useful for monitoring or even for injecting cross cutting functionality.
-->
リクエストヘッダが返される理由は、リクエストにルーティング情報などの情報を追加できるようにするためです。このようにしてルータは、リクエストに一致したルートなどのルーティング情報をリクエストにタグ付けすることができ、モニタリングや横断的な機能のインジェクションにも役立ちます。

<!--
A very simple request handler that simply delegates to a router might look like this:
-->
ルータに委譲するだけの単純なリクエストハンドラは次のようになります。

@[simple](code/ScalaHttpRequestHandlers.scala)

<!--
## Extending the default request handler
-->
## デフォルトリクエストハンドラの拡張

<!--
In most cases you probably won't want to create a new request handler from scratch, you'll want to build on the default one.  This can be done by extending [DefaultHttpRequestHandler](api/scala/play/api/http/HttpRequestHandler.html).  The default request handler provides a number of methods that can be overridden, this allows you to implement your custom functionality without reimplementing the code to tag requests, handle errors, etc.
-->
ほとんどの場合、新しいリクエストハンドラを最初から作るのではなく、デフォルトに基づいて作りたいと思うことでしょう。これは、[DefaultHttpRequestHandler](api/scala/play/api/http/HttpRequestHandler.html) を拡張することによって行うことができます。デフォルトのリクエストハンドラでは、オーバーライド可能ないくつかのメソッドが用意されていて、これにより、リクエストへのタグ付けやエラー処理のコードを再実装することなく、カスタム機能を実装できます。

<!--
One use case for a custom request handler may be that you want to delegate to a different router, depending on what host the request is for.  Here is an example of how this might be done:
-->
カスタムリクエストハンドラのユースケースの 1 つは、リクエストの対象となるホストに応じて、別のルータに委譲することです。これがどのように行われるかの例を以下に示します。

@[virtualhost](code/ScalaHttpRequestHandlers.scala)

<!--
## Configuring the http request handler
-->
## HTTP リクエストハンドラの設定

<!--
A custom http handler can be supplied by creating a class in the root package called `RequestHandler` that implements `HttpRequestHandler`.
-->
`HttpRequestHandler` を実装する `RequestHandler` と呼ばれるクラスをルートパッケージに作成することで、カスタム HTTP ハンドラを提供できます。

<!--
If you don’t want to place your request handler in the root package, or if you want to be able to configure different request handlers for different environments, you can do this by configuring the `play.http.requestHandler` configuration property in `application.conf`:
-->
リクエストハンドラをルートパッケージに配置したくない場合や、異なる環境に対して異なるリクエストハンドラを設定したい場合は、`application.conf` の `play.http.requestHandler` プロパティを設定します。

    play.http.requestHandler = "com.example.RequestHandler"
    
<!--
### Performance notes
-->
### パフォーマンスメモ

<!--
The http request handler that Play uses if none is configured is one that delegates to the legacy `GlobalSettings` methods.  This may have a performance impact as it will mean your application has to do many lookups out of Guice to handle a single request.  If you are not using a `Global` object, then you don't need this, instead you can configure Play to use the default http request handler:
-->
何も設定されていない場合に Play が使用する HTTP リクエストハンドラは、従来の `GlobalSettings` メソッドに委譲するものです。これは、アプリケーションがひとつのリクエストを処理する際に、Guice を用いずに多くの検索を行わなければならないことを意味するため、パフォーマンスに影響を与える可能性があります。`Global` オブジェクトを使用していない場合、これは必要ありません。代わりに、デフォルト HTTP リクエストハンドラを使用するように Play を設定することができます。

    play.http.requestHandler = "play.api.http.DefaultHttpRequestHandler"
