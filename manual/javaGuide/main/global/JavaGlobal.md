<!--
# Application global settings
-->
# アプリケーションのグローバル設定

<!--
## The Global object
-->
## Global オブジェクト

Defining a `Global` object in your project allows you to handle global settings for your application. This object must be defined in the root package. If you want to personalize package (or class name) you must modify `application.global` key into `conf/application.conf`.

@[global](code/javaguide/global/simple/Global.java)

<!--
## Intercepting application start-up and shutdown
-->
## アプリケーションの起動や停止をインターセプトする

<!--
You can override the `onStart` and `onStop` operation to be notified of the corresponding application lifecycle events:
-->
`onStart` や `onStop` 操作をオーバーライドすることで、対応するアプリケーションのライフサイクルイベントの通知を受けることができます。

@[global](code/javaguide/global/startstop/Global.java)

<!--
## Providing an application error page
-->
## アプリケーションのエラーページを提供する

<!--
When an exception occurs in your application, the `onError` operation will be called. The default is to use the internal framework error page. You can override this:
-->
アプリケーションで例外が発生すると、`onError` 操作が呼び出されます。デフォルトでは、フレームワークに用意されている汎用的なエラーページを表示するようになっていますが、これをオーバーライドすることができます。

@[global](code/javaguide/global/onerror/Global.java)

<!--
## Handling action not found
-->
## 存在しないアクションを扱う

<!--
If the framework doesn’t find an action method for a request, the `onHandlerNotFound` operation will be called:
-->
フレームワークがリクエストに対応するアクションを見つけられなかった場合、`onHandlerNotFound` 操作が呼び出されます。

@[global](code/javaguide/global/notfound/Global.java)

<!--
The `onBadRequest` operation will be called if a route was found, but it was not possible to bind the request parameters:
-->
また、ルートは存在するものの、リクエストパラメータをバインドできなかった場合は、`onBadRequest` 操作が呼び出されます。

@[global](code/javaguide/global/badrequest/Global.java)

<!--
> **Next:** [[Intercepting requests | JavaInterceptors]]
-->
> **次ページ:** [[リクエストのインターセプト | JavaInterceptors]]
