<!--
# Filters
-->
# フィルター

<!--
Play provides a simple filter API for applying global filters to each request.
-->
Play は、あらゆるリクエストに適用するグローバルフィルター向けの、シンプルなフィルター API を提供しています。

<!--
## Filters vs action composition
-->
## フィルター vs アクション合成

<!--
The filter API is intended for cross cutting concerns that are applied indiscriminately to all routes.  For example, here are some common use cases for filters:
-->
フィルター API は、すべてのルートに無差別に適用される横断的な関心事を対象としています。フィルターの一般的なユースケースは、例えば以下のようなものです。

<!--
* Logging/metrics collection
* GZIP encoding
* Blanket security filters
-->
* ログ/測定値の収集
* GZIP エンコード
* 包括的なセキュリティフィルター

<!--
In contrast, [[action composition|ScalaActionsComposition]] is intended for route specific concerns, such as authentication and authorisation, caching and so on.  If your filter is not one that you want applied to every route, consider using action composition instead, it is far more powerful.  And don't forget that you can create your own action builders that compose your own custom defined sets of actions to each route, to minimise boilerplate.
-->
対照的に、[[アクション合成|ScalaActionsComposition]] は認証や認可、キャッシュなど、特定のルートに対する関心事を対象としています。もし、フィルターをすべてのルートに適用したいのでなければ、代わりにアクション合成の使用を検討してみてください。アクション合成はフィルターよりも遙かに強力です。また、定型的なコードを最小限にするために、ルート毎に独自の定義済みアクション群を構成する、アクションビルダーを作成できることも忘れないでください。

<!--
## A simple logging filter
-->
## シンプルなロギングフィルター

<!--
The following is a simple filter that times and logs how long a request takes to execute in Play framework:
-->
以下は、Play framework があるリクエストを処理するためにどれくらい時間が掛かったのか計測してロギングする、シンプルなフィルターです:

@[simple-filter](code/Filters.scala)

Let's understand what's happening here.  The first thing to notice is the signature of the `apply` method.  It's a curried function, with the first parameter, `nextFilter`, being a function that takes a request header and produces a result, and the second parameter, `requestHeader`, being the actual request header of the incoming request.

The `nextFilter` parameter represents the next action in the filter chain. Invoking it will cause the action to be invoked.  In most cases you will probably want to invoke this at some point in your future.  You may decide to not invoke it if for some reason you want to block the request.

We save a timestamp before invoking the next filter in the chain. Invoking the next filter returns a `Future[SimpleResult]` that will redeemed eventually. Take a look at the [[Handling asynchronous results|ScalaAsync]] chapter for more details on asynchronous results. We then manipulate the `SimpleResult` in the `Future` by calling the `map` method with a closure that takes a `SimpleResult`. We calculate the time it took for the request, log it and send it back to the client in the response headers by calling `result.withHeaders("Request-Time" -> requestTime.toString)`.

### A more concise syntax

You can use a more concise syntax for declaring a filter if you wish:

@[concise-filter-syntax](code/Filters.scala)

<!--
Since this is a val, this can only be used inside some scope.
-->
これは val なので、なんらかのスコープ内においてのみ使用することができます。

<!--
## Using filters
-->
## フィルターを使う

<!--
The simplest way to use a filter is to extends the `WithFilters` trait on your `Global` object:
-->
もっともシンプルにフィルターを使うには、`Global` オブジェクトで `WithFilters` トレイトを継承します:

@[filter-trait-example](code/GlobalWithFilters.scala)

<!--
You can also invoke a filter manually:
-->
手動でフィルターを実行することもできます:

@[filter-method-example](code/GlobalWithFilters.scala)

<!--
## Where do filters fit in?
-->
## フィルターはどこに合う?

<!--
Filters wrap the action after the action has been looked up by the router.  This means you cannot use a filter to transform a path, method or query parameter to impact the router.  However you can direct the request to a different action by invoking that action directly from the filter, though be aware that this will bypass the rest of the filter chain.  If you do need to modify the request before the router is invoked, a better way to do this would be to place your logic in `Global.onRouteRequest` instead.
-->
フィルターは、アクションがルーターによって見つけられた後に、そのアクションをラップします。これは、フィルターを使ってルーターに影響を与えるパスやメソッド、そしてクエリパラメーターを変換できないことを意味します。フィルターから別のアクションを実行することで、リクエストをそのアクションに移動させてしまうこともできますが、これをするとフィルターチェーンの残りをバイパスしてしまうことに気を付けてください。ルーターが実行される前にリクエストを変更する必要がある場合は、フィルターを使う代わりに、そのロジックを `Global.onRouteRequest` に配置するのが、より良いやり方でしょう。

<!--
Since filters are applied after routing is done, it is possible to access routing information from the request, via the `tags` map on the `RequestHeader`.  For example, you might want to log the time against the action method.  In that case, you might update the `logTime` method to look like this:
-->
フィルターはルーティングが完了した後に適用されるので、`RequestHeader` の `tags` マップによってリクエストからルーティング情報にアクセスすることができます。例えば、アクションメソッドに対する実行時間をログに出力したいとします。この場合、`logTime` を以下のように書き換えることができます:

@[routing-info-access](code/FiltersRouting.scala)

> Routing tags are a feature of the Play router.  If you use a custom router, or return a custom action in `Global.onRouteRequest`, these parameters may not be available.

<!--
## More powerful filters
-->
## より強力なフィルター

<!--
Play provides a lower level filter API called `EssentialFilter` which gives you full access to the body of the request.  This API allows you to wrap [[EssentialAction|HttpApi]] with another action.
-->
Play は リクエストボディ全体にアクセスすることのできる、`EssentialFilter` と呼ばれるより低レベルなフィルター API を提供しています。この API により、[[EssentialAction|HttpApi]] を他のアクションでラップすることができます。

<!--
Here is the above filter example rewritten as an `EssentialFilter`:
-->
上記のフィルター例を `EssentialFilter` として書き直すと、以下のようになります:

@[essential-filter-example](code/EssentialFilter.scala)

The key difference here, apart from creating a new `EssentialAction` to wrap the passed in `next` action, is when we invoke next, we get back an `Iteratee`.  You could wrap this in an `Enumeratee` to do some transformations if you wished.  We then `map` the result of the iteratee and thus handle it.

> Although it may seem that there are two different filter APIs, there is only one, `EssentialFilter`.  The simpler `Filter` API in the earlier examples extends `EssentialFilter`, and implements it by creating a new `EssentialAction`.  The passed in callback makes it appear to skip the body parsing by creating a promise for the `Result`, and returning that in a `SimpleResult`, while the body parsing and the rest of the action are executed asynchronously.
