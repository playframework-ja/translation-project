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
プロジェクト内に `Global` オブジェクトを定義して、アプリケーションのグローバル設定をカスタマイズすることができます。このオブジェクトは、デフォルトパッケージ (無名パッケージ) 内に定義されている必要があります。

```scala
import play.api._

object Global extends GlobalSettings {

}
```

<!--
> **Tip:** You can also specify a custom `GlobalSettings` implementation class name using the `application.global` configuration key.
-->
> **Tip:** `Global` という名前のオブジェクトを使う代わりに、`application.global` という設定キーに `GlobalSettings` の実装クラスの名前を指定するという方法もあります。

<!--
## Hooking into application start and stop events
-->
## アプリケーションの開始と停止をフックする

<!--
You can override the `onStart` and `onStop` methods to be notified of the events in the application life-cycle:
-->
`onStart` と `onStop` メソッドをオーバーライドして、アプリケーションのライフサイクルにおけるイベント通知を受け取ることができます。

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
アプリケーションで例外が発生した場合、 `onError` が呼び出されます。デフォルトの `onError` は、フレームワーク標準のエラーページを出力します。

```scala
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

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
## アクションが存在しない、またはバインドエラーが発生したときの処理

<!--
If the framework doesn’t find an `Action` for a request, the `onHandlerNotFound` operation will be called:
-->
フレームワークがリクエストに対応する `Action` を見つけられなかった場合、 `onHandlerNotFound` が呼び出されます。

```scala
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

object Global extends GlobalSettings {

  override def onHandlerNotFound(request: RequestHeader): Result = {
    NotFound(
      views.html.notFoundPage(request.path)
    )
  }  
    
}
```

<!--
The `onBadRequest` operation will be called if a route was found, but it was not possible to bind the request parameters:
-->
フレームワークが受け取ったリクエストから実行すべきアクションへのルートを見つけたものの、必要なリクエストパラメータをバインドできなかった場合、 `onBadRequest` が呼び出されます。

```scala
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

object Global extends GlobalSettings {

  override def onBadRequest(request: RequestHeader, error: String) = {
    BadRequest("Bad Request: " + error)
  }  
    
}
```

<!--
> **Next:** [[Intercepting requests | ScalaInterceptors]]
-->
> **次ページ:** [[リクエストインターセプター | ScalaInterceptors]]
