<!-- translated -->
<!--
# Application global settings
-->
# アプリケーションのグローバル設定

<!--
## The Global object
-->
## Global オブジェクト

<!--
Defining a `Global` object in your project allows you to handle global settings for your application. This object must be defined in the default (empty) package.
-->
プロジェクト内に `Global` オブジェクトを定義すると、アプリケーションのグローバル設定を行うことができます。このオブジェクトは (空の) デフォルトパッケージに定義される必要があります。

```scala
import play.api._

object Global extends GlobalSettings {

}
```

<!--
> **Tip:** You can also specify a custom `GlobalSettings` implementation class name using the `application.global` configuration key.
-->
> **Tip:** `application.global` という設定キーで `GlobalSettings` の実装クラスを明示的に指定することもできます。

<!--
## Hooking into application start and stop events
-->
## アプリケーションの起動や停止をフックする

<!--
You can override the `onStart` and `onStop` methods to be notified of the events in the application life-cycle:
-->
`onStart` や `onStop` メソッドをオーバーライドすることで、対応するアプリケーションのライフサイクルイベントの通知を受けることができます。

```scala
import play.api._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
  }  
  
  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }  
    
}
```

<!--
## Providing an application error page
-->
## アプリケーションのエラーページを提供する

<!--
When an exception occurs in your application, the `onError` operation will be called. The default is to use the internal framework error page:
-->
アプリケーションで例外が発生すると、`onError` 操作が呼び出されます。デフォルトでは、フレームワークに用意されている汎用的なエラーページを表示するようになっています。

```scala
import play.api._
import play.api.mvc._
import play.api.mvc.Results.__

object Global extends GlobalSettings {

  override def onError(request: RequestHeader, ex: Throwable) = {
    InternalServerError(
      views.html.errorPage(ex)
    )
  }  
    
}
```

<!--
## Handling missing actions and binding errors
-->
## アクションが存在しない場合のエラー処理

If the framework doesn’t find an `Action` for a request, the `onActionNotFound` operation will be called:

```scala
import play.api._
import play.api.mvc._
import play.api.mvc.Results.__

object Global extends GlobalSettings {

  override def onActionNotFound(request: RequestHeader) = {
    NotFound(
      views.html.notFoundPage(request.path)
    )
  }  
    
}
```

<!--
The `onBadRequest` operation will be called if a route was found, but it was not possible to bind the request parameters:
-->
また、ルートは存在するものの、リクエストパラメータをバインドできなかった場合は、`onBadRequest` 操作が呼び出されます。

```scala
import play.api._
import play.api.mvc._
import play.api.mvc.Results.__

object Global extends GlobalSettings {

  override def onBadRequest(request: RequestHeader, error: String) = {
    BadRequest("Bad Request: " + error)
  }  
    
}
```

<!--
> **Next:** [[Intercepting requests | ScalaInterceptors]]
-->
> **次ページ:** [[リクエストのインターセプト | ScalaInterceptors]]