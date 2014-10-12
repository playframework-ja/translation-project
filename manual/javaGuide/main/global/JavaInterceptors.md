<!--
# Intercepting requests
-->
# インターセプター

<!--
## Overriding onRequest
-->
## onRouteRequest のオーバーライド

<!--
One important aspect of  the ```GlobalSettings``` class is that it provides a way to intercept requests and execute business logic before a request is dispatched to an action.
-->
 ```GlobalSettings``` クラスには、リクエストがアクションに渡される前にインターセプトして、任意のビジネスロジックを実行する方法を提供する、という重要な側面もあります。

<!--
For example:
-->
例えば、

@[global](code/javaguide/global/intercept/Global.java)

<!--
It’s also possible to intercept a specific action method. This can be achieved via [[Action composition| JavaActionsComposition]].
-->
特定のアクションメソッドへのリクエストだけをインターセプトすることも可能です。そのためには、[[アクションの合成| JavaActionsComposition]]を使います。

<!--
> **Next:** [[Testing your application | JavaTest]]
-->
> **次ページ:** [[テスト | JavaTest]]
