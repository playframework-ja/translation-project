<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Application global settings
-->
# アプリケーションのグローバル設定

<!--
## The Global object
-->
## Global オブジェクト

Defining a `Global` object in your project allows you to handle global settings for your application. This object must be defined in the default (empty) package and must extend [`GlobalSettings`](api/scala/index.html#play.api.GlobalSettings).

@[global-define](code/ScalaGlobal.scala)

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

@[global-hooking](code/ScalaGlobal.scala)

<!--
## Providing an application error page
-->
## アプリケーションのエラーページを提供する

<!--
When an exception occurs in your application, the `onError` operation will be called. The default is to use the internal framework error page:
-->
アプリケーションで例外が発生した場合、 `onError` が呼び出されます。デフォルトの `onError` は、フレームワーク標準のエラーページを出力します。

@[global-hooking-error](code/ScalaGlobal.scala)

<!--
## Handling missing actions and binding errors
-->
## アクションが存在しない、またはバインドエラーが発生したときの処理

<!--
If the framework doesn’t find an `Action` for a request, the `onHandlerNotFound` operation will be called:
-->
フレームワークがリクエストに対応する `Action` を見つけられなかった場合、 `onHandlerNotFound` が呼び出されます。

@[global-hooking-notfound](code/ScalaGlobal.scala)


<!--
The `onBadRequest` operation will be called if a route was found, but it was not possible to bind the request parameters:
-->
フレームワークが受け取ったリクエストから実行すべきアクションへのルートを見つけたものの、必要なリクエストパラメータをバインドできなかった場合、 `onBadRequest` が呼び出されます。

@[global-hooking-bad-request](code/ScalaGlobal.scala)

<!--
> **Next:** [[Intercepting requests | ScalaInterceptors]]
-->
> **次ページ:** [[リクエストインターセプター | ScalaInterceptors]]
