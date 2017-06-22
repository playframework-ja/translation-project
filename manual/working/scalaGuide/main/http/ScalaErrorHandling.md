<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Handling errors
-->
# エラーハンドリング

<!--
There are two main types of errors that an HTTP application can return - client errors and server errors.  Client errors indicate that the connecting client has done something wrong, server errors indicate that there is something wrong with the server.
-->
HTTP アプリケーションが返すことのできるエラーは主に二つあります - クライアントエラーとサーバエラーです。クライアントエラーは接続しているクライアントがなにかおかしなことをしていることを示し、サーバエラーはサーバでなにかおかしなことが起こっていることを示します。

<!--
Play will in many circumstances automatically detect client errors - these include errors such as malformed header values, unsupported content types, and requests for resources that can't be found.  Play will also in many circumstances automatically handle server errors - if your action code throws an exception, Play will catch this and generate a server error page to send to the client.
-->
Play は様々な状況で - 不正な形式のヘッダー値、サポートしていないコンテントタイプ、そしてリクエストされたリソースが見つからない、などを含むクライアントエラーを自動的に検出します。また、Play は様々な状況でサーバエラーをハンドリングします - アクションコードが例外をスローすると、Play はこれをキャッチし、サーバエラーページを生成してクライアントに送信します。

<!--
The interface through which Play handles these errors is [`HttpErrorHandler`](api/scala/play/api/http/HttpErrorHandler.html).  It defines two methods, `onClientError`, and `onServerError`.
-->
Play はこれらのエラーを [`HttpErrorHandler`](api/java/play/http/HttpErrorHandler.html) インタフェースを通じてハンドリングします。このインタフェースには `onClientError` と `onServerError` の二つのメソッドが定義されています。

<!--
## Supplying a custom error handler
-->
## カスタムエラーハンドラの提供

<!--
A custom error handler can be supplied by creating a class in the root package called `ErrorHandler` that implements [`HttpErrorHandler`](api/scala/play/api/http/HttpErrorHandler.html), for example:
-->
カスタムエラーハンドラは、例えば以下のように [`HttpErrorHandler`](api/java/play/http/HttpErrorHandler.html) を実装する `ErrorHandler` というクラスをルートパッケージに作成することで提供できます:

@[root](code/ScalaErrorHandling.scala)

<!--
If you don't want to place your error handler in the root package, or if you want to be able to configure different error handlers for different environments, you can do this by configuring the `play.http.errorHandler` configuration property in `application.conf`:
-->
エラーハンドラをルートパッケージに置きたくない場合や、環境ごとに別のエラーハンドラを設定できるようにしたい場合は、`application.conf` 内に `play.http.errorHandler` プロパティを設定することができます:

    play.http.errorHandler = "com.example.ErrorHandler"

<!--
## Extending the default error handler
-->
## デフォルトエラーハンドラの拡張

<!--
Out of the box, Play's default error handler provides a lot of useful functionality.  For example, in dev mode, when a server error occurs, Play will attempt to locate and render the piece of code in your application that caused that exception, so that you can quickly see and identify the problem.  You may want to provide custom server errors in production, while still maintaining that functionality in development.  To facilitate this, Play provides a [`DefaultHttpErrorHandler`](api/scala/play/api/http/DefaultHttpErrorHandler.html) that has some convenience methods that you can override so that you can mix in your custom logic with Play's existing behavior.
-->
Play のデフォルトエラーハンドラは、そのままでも多数の便利な機能を備えています。例えば、dev モードにおいてサーバエラーが発生すると、Play は例外を引き起こしたアプリケーションコードの一部を見つけてレンダリングしようと試みるので、すぐに問題を参照して識別することができます。開発中はその機能を維持しながら、運用環境ではカスタムサーバエラーを提供したいこともあるでしょう。Play はこれを容易にするために、オーバーライドできる便利なメソッドをいくつも持つ [`DefaultHttpErrorHandler`](api/java/play/http/DefaultHttpErrorHandler.html) を提供しているので、Play の既存の振る舞いにカスタムロジックを混ぜ込むことができます。

<!--
For example, to just provide a custom server error message in production, leaving the development error message untouched, and you also wanted to provide a specific forbidden error page:
-->
例えば、商用環境のみカスタマイズしたサーバエラーメッセージを表示し、開発用エラーメッセージには手を触れず、かつ指定したアクセス拒否エラーページを提供したい場合、以下のようにします:

@[default](code/ScalaErrorHandling.scala)

<!--
Checkout the full API documentation for [`DefaultHttpErrorHandler`](api/scala/play/api/http/DefaultHttpErrorHandler.html) to see what methods are available to override, and how you can take advantage of them.
-->
[`DefaultHttpErrorHandler`](api/java/play/http/DefaultHttpErrorHandler.html) にある完全な API ドキュメントを参照して、オーバーライドして活用することのできるメソッドを確認してください。