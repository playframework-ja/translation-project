<!--- Copyright (C) 2009-2014 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Handling asynchronous results
-->
# 非同期レスポンスの処理

## Make controllers asynchronous

Internally, Play Framework is asynchronous from the bottom up. Play handles every request in an asynchronous, non-blocking way.

The default configuration is tuned for asynchronous controllers. In other words, the application code should avoid blocking in controllers, i.e., having the controller code wait for an operation. Common examples of such blocking operations are JDBC calls, streaming API, HTTP requests and long computations.

Although it's possible to increase the number of threads in the default execution context to allow more concurrent requests to be processed by blocking controllers, following the recommended approach of keeping the controllers asynchronous makes it easier to scale and to keep the system responsive under load.

## Creating non-blocking actions

Because of the way Play works, action code must be as fast as possible, i.e., non-blocking. So what should we return as result if we are not yet able to generate it? The response is a *future* result!

A `Future[Result]` will eventually be redeemed with a value of type `Result`. By giving a `Future[Result]` instead of a normal `Result`, we are able to quickly generate the result without blocking. Play will then serve the result as soon as the promise is redeemed.

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

> **Note:** It's important to understand which thread code runs on with futures. In the two code blocks above, there is an import on Plays default execution context. This is an implicit parameter that gets passed to all methods on the future API that accept callbacks. The execution context will often be equivalent to a thread pool, though not necessarily.
>
> You can't magically turn synchronous IO into asynchronous by wrapping it in a `Future`. If you can't change the application's architecture to avoid blocking operations, at some point that operation will have to be executed, and that thread is going to block. So in addition to enclosing the operation in a `Future`, it's necessary to configure it to run in a separate execution context that has been configured with enough threads to deal with the expected concurrency. See [[Understanding Play thread pools|ThreadPools]] for more information.
>
> It can also be helpful to use Actors for blocking operations. Actors provide a clean model for handling timeouts and failures, setting up blocking execution contexts, and managing any state that may be associated with the service. Also Actors provide patterns like `ScatterGatherFirstCompletedRouter` to address simultaneous cache and database requests and allow remote execution on a cluster of backend servers. But an Actor may be overkill depending on what you need.

<!--
## Returning futures
-->
## Future を返す

While we were using the `Action.apply` builder method to build actions until now, to send an asynchronous result we need to use the `Action.async` builder method:

@[async-result](code/ScalaAsync.scala)

## Actions are asynchronous by default

Play [[actions|ScalaActions]] are asynchronous by default. For instance, in the controller code below, the `{ Ok(...) }` part of the code is not the method body of the controller. It is an anonymous function that is being passed to the `Action` object's `apply` method, which creates an object of type `Action`. Internally, the anonymous function that you wrote will be called and its result will be enclosed in a `Future`.

@[echo-action](/manual/scalaGuide/main/http/code/ScalaActions.scala)

> **Note:** Both `Action.apply` and `Action.async` create `Action` objects that are handled internally in the same way. There is a single kind of `Action`, which is asynchronous, and not two kinds (a synchronous one and an asynchronous one). The `.async` builder is just a facility to simplify creating actions based on APIs that return a `Future`, which makes it easier to write non-blocking code.

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
