<!-- translated -->
<!--
# Application global settings
-->
# アプリケーションの全般設定

<!--
## The Global object
-->
## Global オブジェクト

<!--
Defining a `Global` object in your project allows you to handle global settings for your application. This object must be defined in the default (empty) package.
-->
プロジェクト内に `Global` オブジェクトを定義すると、アプリケーション全般の設定を変更することができます。このオブジェクトは、デフォルトパッケージ (ルートパッケージ) に定義する必要があります。

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
## アプリケーションの起動・停止にフックする

<!--
You can override the `onStart` and `onStop` methods to be notified of the events in the application life-cycle:
-->
`onStart` や `onStop` メソッドをオーバーライドすると、アプリケーションのライフサイクルイベントが発生したタイミングで、任意の処理を実行することができます。

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
## アプリケーションのエラーページを変更する

<!--
When an exception occurs in your application, the `onError` operation will be called. The default is to use the internal framework error page:
-->
アプリケーションで例外が発生したときは、 `onError` メソッドが呼び出されます。その際、デフォルトではフレームワークに予め用意されているエラーページが表示されます。

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

<!--
If the framework doesn’t find an `Action` for a request, the `onHandlerNotFound` operation will be called:
-->
フレームワークが受け取ったリクエストに対する `Action` を見つけられなかった場合、 `onHandlerNotFound` メソッドが呼び出されます。

```scala
import play.api._
import play.api.mvc._
import play.api.mvc.Results.__

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
また、ルートは存在したものの、決められたリクエスト・パラメータが取得できなかった場合には、 `onBadRequest` メソッドが呼び出されます。

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
> **次ページ:** [[インターセプター | ScalaInterceptors]]