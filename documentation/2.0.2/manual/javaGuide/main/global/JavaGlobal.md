<!-- translated -->
<!--
# Application global settings
-->
# アプリケーションのグローバル設定
  
<!--
## The Global object
-->
## `Global`オブジェクト

<!--
Defining a `Global` object in your project allows you to handle global settings for your application. This object must be defined in the root package.
-->
プロジェクトに`Global`オブジェクトを定義すると、アプリケーションのグローバル設定を行うことができます。このオブジェクトはルートパッケージに定義される必要があります。

```java
import play.*;

public class Global extends GlobalSettings {

}
```

<!--
## Intercepting application start-up and shutdown
-->
## アプリケーションの起動または終了時に処理を行う

<!--
You can override the `onStart` and `onStop` operation to be notified of the corresponding application lifecycle events:
-->
`onStart`や`onStop`操作をオーバーライドすることで、それぞれアプリケーションのライフサイクルにおけるイベントが発生した際に通知を受けることができます。

```java
import play.*;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
    
}
```

<!--
## Providing an application error page
-->
## アプリケーションのエラーページを提供する
  
<!--
When an exception occurs in your application, the `onError` operation will be called. The default is to use the internal framework error page. You can override this:
-->
アプリケーションで例外が発生すると、`onError`操作が呼び出されます。デフォルトでは、フレームワークに用意されている汎用的なエラーページを表示するようになっていますが、これをオーバーライドすることができます。

```java
import play.*;
import play.mvc.*;

import static play.mvc.Results.*;

public class Global extends GlobalSettings {

  @Override
  public Result onError(Throwable t) {
    return internalServerError(
      views.html.errorPage(t)
    );
  }  
    
}
```

<!--
## Handling action not found
-->
## 存在しないアクションを扱う

<!--
If the framework doesn’t find an action method for a request, the `onHandlerNotFound` operation will be called:
-->
フレームワークがリクエストに対応するアクションを見つけられなかった場合、`onHandlerNotFound`操作が呼び出されます。

```java
import play.*;
import play.mvc.*;

import static play.mvc.Results.*;

public class Global extends GlobalSettings {

  @Override
  public Result onHandlerNotFound(String uri) {
    return notFound(
      views.html.pageNotFound(uri)
    );
  }  
    
}
```

<!--
The `onBadRequest` operation will be called if a route was found, but it was not possible to bind the request parameters:
-->
また、ルートは定義されているものの、リクエストパラメータをバインドできなかった場合は、`onBadRequest`操作が呼び出されます。

```scala
import play.*;
import play.mvc.*;

import static play.mvc.Results.*;

public class Global extends GlobalSettings {

  @Override
  public Result onBadRequest(String uri, String error) {
    return badRequest("Don't try to hack the URI!");
  }  
    
}
```

<!--
> **Next:** [[Intercepting requests | JavaInterceptors]]
-->
> **次ページ:** [[リクエストをインターセプトする | JavaInterceptors]]
