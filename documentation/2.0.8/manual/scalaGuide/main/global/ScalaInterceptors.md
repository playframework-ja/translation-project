<!-- translated -->
<!--
# Intercepting requests
-->
# インターセプター

<!--
## Overriding onRouteRequest
-->
## onRouteRequest のオーバーライド

<!--
One another important aspect of  the ```Global``` object is that it provides a way to intercept requests and execute business logic before a request is dispatched to an Action. 
-->
 ```Global``` オブジェクトには、リクエストがアクションに渡される前にインターセプトして、任意のビジネスロジックを実行する方法を提供する、という重要な側面もあります。

<!--
> **Tip** This hook can be also used for hijacking requests, allowing developers to plug-in their own request routing mechanism. 
-->
> **Tip** このフックはリクエストを乗っ取って、独自のルーティング機構をプラグインするという用途にも使えます。

<!--
Let’s see how this works in practice:
-->
実際に、どのようにインターセプトさせるか見てみましょう。

```scala
import play.api._

// Note: this is in the default package.
object Global extends GlobalSettings {

  def onRouteRequest(request: RequestHeader): Option[Handler] = {
     println("executed before every request:" + request.toString)
     super.onRouteRequest(request)
  }

}
```

<!--
It’s also possible to intercept a specific Action method, using [[Action composition | ScalaActionsComposition]].
-->
[[アクションの合成| ScalaActionsComposition]] を使って特定のアクションメソッドへのリクエストだけをインターセプトすることも可能です。

<!--
> **Next:** [[Testing your application | ScalaTest]]
-->
> **次ページ:** [[テスト | ScalaTest]]