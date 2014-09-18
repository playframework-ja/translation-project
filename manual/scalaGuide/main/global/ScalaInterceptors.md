<!-- translated -->
<!--
# Intercepting requests
-->
<!-- 「リクエストをインターセプトする」という直訳の表現はあまり馴染みないような気がしたので、同じWebフレームワークであり有名なStrutsの機能名などで使われる「リクエストインターセプター」という言葉を選びました -->
# リクエストインターセプター

<!--
## Using Filters
-->
## フィルタの利用

<!--
The filter component allows you to intercept requests coming to the application, transform request and responses. Filters provide a nice ability to implement cross-cutting concerns of your application. You can create a filter by extending the `Filter` trait and then add the filter to `Global` object. The following example creates an access log filter that logs the result of all the actions:
-->
フィルターとは、アプリケーションへ送られてきたリクエストをインターセプト(傍受)したり、リクエストまたはレスポンスに変換処理を行うことを可能にするコンポーネントです。フィルターを使うと、アプリケーションを横断する関心事をうまく実装することができます。フィルターは `Filter` トレイトを継承して実装され、 `Global` へ追加されることで機能します。次の例では、全てのアクションの実行結果のログをとるアクセスログフィルターを実装しています。

@[filter-log](code/ScalaInterceptors.scala)


<!--
The `Global` object extends the `WithFilters` class that allows you to pass one more filters to form a filter chain.

> **Note** `WithFilters` now extends the `GlobalSettings` trait

Here is another example where filter is very useful, check authorization before invoking certain actions:
-->
`Global` オブジェクトは `WithFilters` クラスを継承しており、複数のフィルタを渡すとフィルタチェインをつくってくれます。

> **Note** 現在、 `WithFilters` は `GlobalSettings` トレイトを継承しています

次の例は非常に有用なフィルタで、アクションの呼び出し前にアクセス認可チェックを行うというものです。

@[filter-authorize](code/ScalaInterceptors.scala)


<!--
> **Tip** `RequestHeader.tags` provides lots of useful information about the route used to invoke the action. 
-->
> **Tip** `RequestHeader.tags` には、アクションの呼び出しに利用されたルートに関する有益な情報が色々と含まれています

<!--
## Overriding onRouteRequest
-->
## onRouteRequest をオーバライドする

<!--
One another important aspect of  the ```Global``` object is that it provides a way to intercept requests and execute business logic before a request is dispatched to an Action. 

> **Tip** This hook can be also used for hijacking requests, allowing developers to plug-in their own request routing mechanism. 

Let’s see how this works in practice:
-->
```Global``` オブジェクトにはもう一つ重要な側面があります。それは、リクエストをインターセプトする手段を提供してくれる、ということです。これを利用すると、アクションへリクエストが送られる前に任意のビジネスロジックを実行することができます。

> **Tip** このフックは、リクエストをハイジャックして、開発者が任意のリクエストルーティング機構をプラグインする、という用途にも利用できます

早速、これを実践するとどうなるのか見てみましょう。


@[onroute-request](code/ScalaInterceptors.scala)


<!--
It’s also possible to intercept a specific Action method, using [[Action composition | ScalaActionsComposition]].
-->
ここで紹介した方法以外にも、 [[アクション合成 | ScalaActionsComposition]] を使って特定のアクションメソッドだけをインターセプトする、というテクニックもあります。


<!--
> **Next:** [[Testing your application | ScalaTest]]
-->
> **次ページ:** [[アプリケーションのテスト | ScalaTest]]
