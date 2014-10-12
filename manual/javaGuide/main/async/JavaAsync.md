<!--
# Handling asynchronous results
-->
# 非同期レスポンスの処理

<!--
## Why asynchronous results?
-->
## 非同期レスポンスはなぜ必要か?

<!--
Until now, we have been able to compute the result to send to the web client directly. This is not always the case: the result may depend on an expensive computation or on a long web service call.
-->
これまでは、web クライアントに送信するレスポンスをすぐに生成できることとしていました。しかし、常にこのような場合だけではありません。レスポンスは、高価な計算や長い web サービスの呼び出しに依存するかもしれません。

Because of the way Play works, action code must be as fast as possible (i.e. non blocking). So what should we return from our action if we are not yet able to compute the result? We should return the *promise* of a result!

A `Promise<Result>` will eventually be redeemed with a value of type `Result`. By using a `Promise<Result>` instead of a normal `Result`, we are able to return from our action quickly without blocking anything. Play will then serve the result as soon as the promise is redeemed.

<!--
The web client will be blocked while waiting for the response but nothing will be blocked on the server, and server resources can be used to serve other clients.
-->
web クライアントはレスポンスを待っている間ずっとブロックされますが、その間でもサーバ側の処理は全くブロックされないため、計算リソースを他のクライアントのために使うことができます。

<!--
## How to create a `Promise<Result>`
-->
## `Promise<Result>` の生成

<!--
To create a `Promise<Result>` we need another promise first: the promise that will give us the actual value we need to compute the result:
-->
`Promise<Result>` を生成するためには、元となる `Promise`、つまり結果を計算するために必要な値についての Promise が先に必要になります。

@[promise-pi](code/javaguide/async/JavaAsync.java)

<!--
> **Note:** Writing functional composition in Java is really verbose at the moment, but it should be better when Java supports [lambda notation](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html).
-->
> **ノート:** Java で関数合成を記述することは現状では本当に面倒です。しかし、これは Java が [ラムダ記法](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) をサポートすれば改善されるでしょう。

<!--
Play asynchronous API methods give you a `Promise`. This is the case when you are calling an external web service using the `play.libs.WS` API, or if you are using Akka to schedule asynchronous tasks or to communicate with Actors using `play.libs.Akka`.
-->
Play の非同期処理に関する API 呼び出しは `Promise` を返します。例えば、`play.libs.WS` API を使って外部の Web サービスを呼び出す場合や、`play.libs.Akka` API 経由で Akka を使った非同期タスクを実行したり、アクターと通信したりする場合がそうです。

<!--
A simple way to execute a block of code asynchronously and to get a `Promise` is to use the `promise()` helper:
-->
コードブロックを非同期で実行して `Promise` を得る簡単な方法は、`promise()` ヘルパーを利用することです。

@[promise-async](code/javaguide/async/JavaAsync.java)

<!--
> **Note:** Here, the intensive computation will just be run on another thread. It is also possible to run it remotely on a cluster of backend servers using Akka remote.
-->
> **ノート:** ここでは、非常に時間のかかる計算を別スレッドで実行しています。その他に、このような計算を Akka remote を利用してバックエンドサーバのクラスタ上で実行することもできます。

## Async results

We have been returning `Result` up until now. To send an asynchronous result our action needs to return a `Promise<Result>`:

@[async](code/javaguide/async/controllers/Application.java)

<!--
> **Next:** [[Streaming HTTP responses | JavaStream]]
-->
> **次ページ:** [[HTTP レスポンスのストリーミング | JavaStream]]
