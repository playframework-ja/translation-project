<!--
# Handling asynchronous results
-->
# 非同期レスポンスの処理

<!--
## Why asynchronous results?
-->
## 非同期レスポンスはなぜ必要か?

<!--
Until now, we were able to generate the result to send to the web client directly. However, this is not always the case: the result might depend on an expensive computation or on a long web service call.
-->
これまでは、web クライアントに送信するレスポンスをすぐに生成できることとしていました。しかし、常にこのような場合だけではありません。レスポンスは、高価な計算や長い web サービスの呼び出しに依存するかもしれません。

<!--
Because of the way Play works, the action code must be as fast as possible (ie. non blocking). So what should we return as result if we are not yet able to generate it? The response is a future result! 
-->
Play の仕組み上、アクションの実行は可能な限り早く (言い換えると、ノンブロッキングに)  完了しなければなりません。では、レスポンスがまだ生成可能でないときに、一体何を返すべきでしょうか? それは、 レスポンスの Future です!

<!--
A `Future[Result]` will eventually be redeemed with a value of type `Result`. By giving a `Future[Result]` instead of a normal `Result`, we are able to quickly generate the result without blocking. Then, Play will serve this result as soon as the promise is redeemed. 
-->
`Future[Result]` は、最終的に `Result` 型の値を補償します。通常の `Result` のかわりに `Future[Result]` を返すことで、何もブロックせずに即座に結果を返すことができます。Play は後に約束が果たされたときに、内包された結果を自動的にクライアントへ送信します。

<!--
The web client will be blocked while waiting for the response, but nothing will be blocked on the server, and server resources can be used to serve other clients.
-->
web クライアントはレスポンスを待っている間ずっとブロックされますが、その間でもサーバ側の処理は全くブロックされないため、計算リソースを他のクライアントのために使うことができます。

<!--
## How to create a `Future[Result]`
-->
## `Future[Result]` の生成

<!--
To create a `Future[Result]` we need another future first: the future that will give us the actual value we need to compute the result:
-->
`Future[Result]` を生成するためには、元となる Future 、つまり結果を計算するために必要な値についての Future が先に必要になります。

@[future-result](code/ScalaAsync.scala)

<!--
All of Play’s asynchronous API calls give you a `Future`. This is the case whether you are calling an external web service using the `play.api.libs.WS` API, or using Akka to schedule asynchronous tasks or to communicate with actors using `play.api.libs.Akka`.
-->
Play の全ての非同期処理に関する API 呼び出しは `Future` を返します。例えば、`play.api.libs.WS` API を使って外部の Web サービスを呼び出す場合や、`play.api.libs.Akka` API 経由で Akka を使った非同期タスクを実行したり、アクターと通信したりする場合がそうです。

<!--
Here is a simple way to execute a block of code asynchronously and to get a `Future`:
-->
以下はコードブロックを非同期で実行して `Future` を得る簡単な方法です。

@[intensive-computation](code/ScalaAsync.scala)

<!--
It's important to understand which thread code runs on with futures.  In the two code blocks above, there is an import on Plays default execution context.  This is an implicit parameter that gets passed to all methods on the future API that accept callbacks.  The execution context will often be equivalent to a thread pool, though not necessarily.
-->
Future がどのスレッド上で動作するか理解することが重要です。上記ふたつのコードブロックでは、Play のデフォルト実行コンテキストを import しています。これは、コールバックを受け取る Future API のすべてのメソッドに渡される implicit パラメータです。この実行コンテキストは、必ずしもそうではありませんが、多くの場合においてスレッドプールと等価です。

<!--
## Returning futures
-->
## Future を返す

<!--
While we were using the `Action.apply` builder methods to build actions until now, to send an asynchronous result, we need to use the `Action.async` buider method:
-->
これまでは、アクションをビルドするために `Action.apply` ビルダーメソッドを使ってきましたが、非同期に Result を返すためには `Action.async` ビルダーメソッドを使う必要があります:

@[async-result](code/ScalaAsync.scala)

<!--
## Handling time-outs
-->
## タイムアウト処理

<!--
It is often useful to handle time-outs properly, to avoid having the web browser block and wait if something goes wrong. You can easily compose a promise with a promise timeout to handle these cases:
-->
何らかの問題が発生したとき web ブラウザが延々とブロックしてしまうことを避けるために、タイムアウトが役立つことが多々あります。そのような場合、 Promise と Promise のタイムアウトを簡単に組み合わせることができます。

@[timeout](code/ScalaAsync.scala)

<!--
> **Next:** [[Streaming HTTP responses | ScalaStream]]
-->
> **次ページ:** [[HTTP レスポンスのストリーミング | ScalaStream]]
