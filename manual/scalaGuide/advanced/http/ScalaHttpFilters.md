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

```scala
import play.api.mvc._

object LoggingFilter extends Filter {
  def apply(next: (RequestHeader) => Result)(rh: RequestHeader) = {
    val start = System.currentTimeMillis

    def logTime(result: PlainResult): Result = {
      val time = System.currentTimeMillis - start
      Logger.info(s"${rh.method} ${rh.uri} took ${time}ms and returned ${result.header.status}")
      result.withHeaders("Request-Time" -> time.toString)
    }
    
    next(rh) match {
      case plain: PlainResult => logTime(plain)
      case async: AsyncResult => async.transform(logTime)
    }
  }
}
```

<!--
Let's understand what's happening here.  The first thing to notice is the signature of the `apply` method.  It's a curried function, with the first parameter, `next`, being a function that takes a request header and produces a result, and the second parameter, `rh`, being a request header.
-->
ここで何が起きているのか理解してみましょう。まず最初に気付くべき点は `apply` メソッドのシグネチャです。これはカリー化された関数で、第一引数はリクエストヘッダーを受け取って結果を返す関数となる `next` で、第二引数はリクエストヘッダーとなる `rh` です。

<!--
The `next` parameter represents the next action in the filter chain.  Invoking it will cause the action to be invoked.  In most cases you will probably want to invoke this at some point in your future.  You may decide to not invoke it if for some reason you want to block the request.
-->
`next` 引数は、フィルターチェーンにおける次のアクションを表現しています。これを実行すると、次に呼ばれるべきアクションが起動します。ほとんどの場合において、後続の処理のいくつかのタイミングで次のアクションを実行したくなることでしょう。何らかの理由により、リクエストをブロックしたいのであれば、次のアクションを実行しないと決断するのも良いかもしれません。

<!--
The `rh` parameter is the actual request header for the request.
-->
`rh` 引数は、このリクエストの実際のヘッダーです。

<!--
The next thing in the code is a function that logs the request.  This function takes a `PlainResult`, and after logging the request time, adds a header to the response that records the `Request-Time`, and returns that result.
-->
コードの次の部分は、リクエストのログを出力する関数です。この関数は `PlainResult` を受け取り、リクエストの処理に掛かった時間をログに出力した後、レスポンスにこの `Request-Time` を記録するヘッダを記録して返します。

<!--
Finally the next action is invoked, and pattern matched on the result it returns.  A result can either be a `PlainResult` or a `AsyncResult`, an `AsyncResult` is a result that will eventually be redeemed as a `PlainResult`.  In both cases, the `logTime` function needs to be invoked, but is invoked in a slightly different way for each.  Since if it's a `PlainResult` the result is available now, it just invokes `logTime` directly.  However, if it's `AsyncResult` the result is not yet available.  So, the `logTime` function is passed to the `transform` method to be invoked later, when the `PlainResult` is available.
-->
最後に、次のアクションが実行され、このアクションが返す結果のパターンマッチが行われます。結果は `PlainResult` または `AsyncResult` のどちらかであり、`AsyncResult` は最終的には `PlainResult` として実行されます。いずれの場合においても `logTime` 関数が実行される必要がありますが、その実行方法はそれぞれ少しだけ異なっています。`PlainResult` の場合、結果はすぐに利用できるので `logTime` を直ちに実行します。一方、`AsyncResult` の場合、結果はまだ利用できません。そのため `logTime` 関数は、`PlainResult` が利用できるようになってから実行されるよう `transform` メソッドに渡されます。

<!--
### A simpler syntax
-->
### 簡易な文法

<!--
You can use a simpler syntax for declaring a filter if you wish:
-->
より簡易な文法でフィルタを宣言することができます:

```scala
val loggingFilter = Filter { (next, rh) =>
  val start = System.currentTimeMillis

  def logTime(result: PlainResult): Result = {
    val time = System.currentTimeMillis - start
    Logger.info(s"${rh.method} ${rh.uri} took ${time}ms and returned ${result.header.status}")
    result.withHeaders("Request-Time" -> time.toString)
  }
    
  next(rh) match {
    case plain: PlainResult => logTime(plain)
    case async: AsyncResult => async.transform(logTime)
  }
}
```

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

```scala
import play.api.mvc._

object Global extends WithFilters(LoggingFilter, new GzipFilter()) {
  ...
}
```

<!--
You can also invoke a filter manually:
-->
手動でフィルターを実行することもできます:

```scala
import play.api._

object Global extends GlobalSettings {
  override def doFilter(action: EssentialAction) = LoggingFilter(action)
}
```

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

```scala
  def logTime(result: PlainResult): Result = {
    val time = System.currentTimeMillis - start
    val action = rh.tags(Routes.ROUTE_CONTROLLER) + "." + rh.tags(Routes.ROUTE_ACTION_METHOD)
    Logger.info(s"${action} took ${time}ms and returned ${result.header.status}")
    result.withHeaders("Request-Time" -> time.toString)
  }
```

<!--
> Routing tags are a feature of the Play router.  If you use a custom router, or return a custom action in `Glodal.onRouteRequest`, these parameters may not be available.
-->
> ルーティングタグは Play ルーターの機能です。独自のルーターを使ったり、`Glodal.onRouteRequest` から独自のアクションを返す場合は、これらのパラメーターは利用できない場合があります。

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

```scala
import play.api.mvc._

object LoggingFilter extends EssentialFilter {
  def apply(next: EssentialAction) = new EssentialAction {
    def apply(rh: RequestHeader) = {
      val start = System.currentTimeMillis

      def logTime(result: PlainResult): Result = {
        val time = System.currentTimeMillis - start
        Logger.info(s"${rh.method} ${rh.uri} took ${time}ms and returned ${result.header.status}")
        result.withHeaders("Request-Time" -> time.toString)
      }
    
      next(rh).map {
        case plain: PlainResult => logTime(plain)
        case async: AsyncResult => async.transform(logTime)
      }
    }
  }
}
```

<!--
The key difference here, apart from creating a new `EssentialAction` to wrap the passed in `next` action, is when we invoke next, we get back an `Iteratee`.  You could wrap this in an `Enumeratee` to do some transformations if you wished.  We then `map` the result of the iteratee, and handle it with a partial function, in the same way as in the simple form.
-->
`next` に渡されたアクションをラップするために `EssentialAction` を新しく作っている点は置いておくとして、ここでポイントとなる差異は next を実行すると `Iteratee` が得られる点です。お望みであれば、`Enumeratee` 内においてこれをラップすることで、様々な変換を行うことができます。その後、シンプルなフォームにおいて行ったのと同じ方法で、この iteratee の結果を `map` し、partial function と共に取り扱います。

<!--
> Although it may seem that there are two different filter APIs, there is only one, `EssentialFilter`.  The simpler `Filter` API in the earlier examples extends `EssentialFilter`, and implements it by creating a new `EssentialAction`.  The passed in callback makes it appear to skip the body parsing by creating a promise for the `Result`, and returning that in an `AsyncResult`, while the body parsing and the rest of the action are executed asynchronously.
-->
> 異なるふたつのフィルター API が存在するように見えるかもしれませんが、存在するのはただひとつ、`EssentialFilter` だけです。先の例で登場したシンプルな `Filter` API は `EssentialFilter` を継承し、`EssentialAction` を新しく作ることでこれを実装しています。引数に渡されたコールバックは、リクエストボディを解析して残りのアクションが非同期に実行されている間、`Result` 用の promise を作り、その結果を `AsyncResult` として返すことで、リクエストボディの解析をスキップしているように見せています。
