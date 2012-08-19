<!-- translated -->
<!--
# Intercepting requests
-->
# インターセプター

<!--
## Overriding onRequest
-->
## onRequest をオーバーライドする

<!--
One important aspect of  the ```GlobalSettings``` class is that it provides a way to intercept requests and execute business logic before a request is dispatched to an action.

For example:
-->
```GlobalSettings``` クラスには、リクエストがアクションに渡される前にインターセプトして、任意のビジネスロジックを実行するために使える、という重要な側面もあります。

例えば、

```java
import play.*;

public class Global extends GlobalSettings {

@Override
public Action onRequest(Request request, Method actionMethod) {
   System.out.println("before each request..." + request.toString());
   return super.onRequest(request, actionMethod);
}

}
```

<!--
It’s also possible to intercept a specific action method. This can be achieved via [[Action composition| JavaActionsComposition]].
-->
特定のアクションメソッドへのリクエストだけをインターセプトすることも可能です。そのためには、[[アクションの合成| JavaActionsComposition]]を使います。

<!--
> **Next:** [[Testing your application | JavaTest]]
-->
> **次ページ:** [[アプリケーションのテスト | JavaTest]]