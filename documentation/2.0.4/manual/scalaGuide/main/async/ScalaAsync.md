<!-- translated -->
<!--
# Handling asynchronous results
-->
# 非同期レスポンスの処理

<!--
## Why asynchronous results?
-->
## 非同期レスポンスはなぜ必要か?

<!--
Until now, we were able to generate the result to send to the web client directly. However, this is not always the case: the result might depend on an expensive computation or of a long web service call.
-->
これまでは、web クライアントに送信するレスポンスをすぐに生成できることとしていました。しかし、常にこのような場合だけではありません。レスポンスは、高価な計算や長い web サービスの呼び出しに依存するかもしれません。

<!--
Because of the way Play 2.0 works, the action code must be as fast as possible (ie. non blocking). So what should we return as result if we are not yet able to generate it? The response is a promise of result! 
-->
Play 2.0 の仕組み上、アクションの実行は可能な限り早く (言い換えると、ノンブロッキングに)  完了しなければなりません。では、レスポンスがまだ生成可能でないときに、一体何を返すべきでしょうか? それは、レスポンスの約束 (promise) です!

<!--
A `Promise[Result]` will eventually be redeemed with a value of type `Result`. By giving a `Promise[Result]` instead if a normal `Result`, we are able to quickly generate the result without blocking. Then, Play will serve this result as soon as the promise is redeemed. 
-->
`Promise[Result]` は、直訳すると「`Result` 型の値についての約束」で、「いつか `Result` 型の値が与えられたときに実行される計算」を意味しています。アクションで通常の `Result` の代わりに `Promise[Result]` を返すようにすると、その他の処理をブロックせず即座に結果を生成することができます。そして、Promise の値が実際に有効になったタイミングですぐに Play がレスポンスを生成・送信します。

<!--
The web client will be blocked while waiting for the response, but nothing will be blocked on the server, and server resources can be used to serve other clients.
-->
web クライアントはレスポンスを待っている間ずっとブロックされますが、その間でもサーバ側の処理は全くブロックされないため、計算リソースを他のクライアントのために使うことができます。

<!--
## How to create a `Promise[Result]`
-->
## `Promise[Result]` の生成

<!--
To create a `Promise[Result]` we need another promise first: the promise that will give us the actual value we need to compute the result:
-->
`Promise[Result]` を生成するためには、元となる `Promise`、つまり結果を計算するために必要な値についての Promise が先に必要になります。

```scala
val promiseOfPIValue: Promise[Double] = computePIAsynchronously()
val promiseOfResult: Promise[Result] = promiseOfPIValue.map { pi =>
  Ok("PI value computed: " + pi)    
}
```

<!--
All of Play 2.0’s asynchronous API calls give you a `Promise`. This is the case whether you are calling an external web service using the `play.api.libs.WS` API, or using Akka to schedule asynchonous tasks or to communicate with actors using `play.api.libs.Akka`.
-->
Play 2.0 の全ての非同期処理に関する API 呼び出しは `Promise` を返します。例えば、`play.api.libs.WS` API を使って外部の Web サービスを呼び出す場合や、`play.api.libs.Akka` API 経由で Akka を使った非同期タスクを実行したり、アクターと通信したりする場合がそうです。

<!--
A simple way to execute a block of code asynchronously and to get a `Promise` is to use the `play.api.libs.concurrent.Akka` helpers:
-->
コードブロックを非同期で実行して `Promise` を得る簡単な方法は、`play.api.libs.concurrent.Akka` ヘルパーを利用することです。

```scala
val promiseOfInt: Promise[Int] = Akka.future {
  intensiveComputation()
}
```

<!--
> **Note:** Here, the intensive computation will just be run on another thread. It is also possible to run it remotely on a cluster of backend servers using Akka remote.
-->
> **注目:** この例における intensive computation (高コストな計算) は別スレッドで実行されます。Akka remote を利用して、この計算をバックエンドサーバのクラスタ上で実行させることもできます。

<!--
## AsyncResult
-->
## AsyncResult (非同期な結果)

<!--
While we were using `SimpleResult` until now, to send an asynchronous result, we need an `AsyncResult` to wrap the actual `SimpleResult`:
-->
これまでは `SimpleResult` を使ってきましたが、非同期な結果を送信するためには、`SimpleResult` をラップする `AsyncResult` が必要です。

```scala
def index = Action {
  val promiseOfInt = Akka.future { intensiveComputation() }
  Async {
    promiseOfInt.map(i => Ok("Got result: " + i))
  }
}
```

<!--
> **Note:** `Async { }` is an helper method that builds an `AsyncResult` from a `Promise[Result]`.
-->
> **注目:** `Async { }` は `Promise[Result]` から `AsyncResult` を組み立てるヘルパーメソッドです。

<!--
## Handling time-outs
-->
## タイムアウト処理

<!--
It is often useful to handle time-outs properly, to avoid having the web browser block and wait if something goes wrong. You can easily compose a promise with a promise timeout to handle these cases:
-->
何らかの問題が発生したとき web ブラウザが延々とブロックしてしまうことを避けるために、タイムアウトが役立つことが多々あります。そのような場合、 Promise と Promise のタイムアウトを簡単に組み合わせることができます。

```scala
def index = Action {
  val promiseOfInt = Akka.future { intensiveComputation() }
  Async {
    promiseOfInt.orTimeout("Oops", 1000).map { eitherIntorTimeout =>
      eitherIorTimeout.fold(
        i => Ok("Got result: " + i),
        timeout => InternalServerError(timeout)
      )    
    }  
  }
}
```

<!--
> **Next:** [[Streaming HTTP responses | ScalaStream]]
-->
> **次ページ:** [[HTTP レスポンスのストリーミング | ScalaStream]]