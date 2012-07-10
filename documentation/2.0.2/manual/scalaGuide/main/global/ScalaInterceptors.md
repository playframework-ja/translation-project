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

Global オブジェクトにはもう一つ重要な点があります。それは、 Global オブジェクトにリクエストをインターセプトさせて、リクエストがアクションへ渡される前に任意のビジネスロジックを実行できる、ということです。

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
一方で、全てのリクエストをインターセプトさせるのではなく、特定のアクションのみをインターセプトさせたい場合は、[[アクションの合成 | ScalaActionsComposition]] を利用するとよいでしょう。

<!--
> **Next:** [[Testing your application | ScalaTest]]
-->
> **次ページ:** [[テスト | ScalaTest]]