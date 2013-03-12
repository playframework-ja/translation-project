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
Until now, we were able to compute the result to send to the web client directly. This is not always the case: the result may depend of an expensive computation or on a long web service call.
-->
これまでは、web クライアントに送信するレスポンスをすぐに生成できることとしていました。しかし、常にこのような場合だけではありません。レスポンスは、高価な計算や長い web サービスの呼び出しに依存するかもしれません。

<!--
Because of the way Play 2.0 works, action code must be as fast as possible (i.e. non blocking). So what should we return as result if we are not yet able to compute it? The response should be a promise of a result!
-->
Play 2.0 の仕組み上、アクションの実行は可能な限り早く (言い換えると、ノンブロッキングに)  完了しなければなりません。では、レスポンスがまだ生成可能でないときに、一体何を返すべきでしょうか? それは、レスポンスの約束 (promise) です!

<!--
A `Promise<Result>` will eventually be redeemed with a value of type `Result`. By giving a `Promise<Result>` instead of a normal `Result`, we are able to compute the result quickly without blocking anything. Play will then serve this result as soon as the promise is redeemed. 
-->
`Promise<Result>` という約束は、最終的に `Result` 型の値によって果たされます。通常の `Result` のかわりに `Promise<Result>` を返すことで、何もブロックせずに即座に結果を返すことができます。Play は後に Promise が果たされたときに、内包された結果を自動的にクライアントへ送信します。

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

```
Promise<Double> promiseOfPIValue = computePIAsynchronously();
Promise<Result> promiseOfResult = promiseOfPIValue.map(
  new Function<Double,Result>() {
    public Result apply(Double pi) {
      return ok("PI value computed: " + pi);
    } 
  }
);
```

<!--
> **Note:** Writing functional composition in Java is really verbose for the moment, but it should be better when Java supports [[lambda notation| http://mail.openjdk.java.net/pipermail/lambda-dev/2011-September/003936.html]].
-->
> **ノート:** Java で関数合成を記述することは現状では本当に面倒です。しかし、これは Java が [[ラムダ記法| http://mail.openjdk.java.net/pipermail/lambda-dev/2011-September/003936.html]] をサポートすれば改善されるでしょう。

<!--
Play 2.0 asynchronous API methods give you a `Promise`. This is the case when you are calling an external web service using the `play.libs.WS` API, or if you are using Akka to schedule asynchronous tasks or to communicate with Actors using `play.libs.Akka`.
-->
Play 2.0 の非同期処理に関する API 呼び出しは `Promise` を返します。例えば、`play.libs.WS` API を使って外部の Web サービスを呼び出す場合や、`play.libs.Akka` API 経由で Akka を使った非同期タスクを実行したり、アクターと通信したりする場合がそうです。

<!--
A simple way to execute a block of code asynchronously and to get a `Promise` is to use the `play.libs.Akka` helpers:
-->
コードブロックを非同期で実行して `Promise` を得る簡単な方法は、`play.libs.Akka` ヘルパーを利用することです。

```
Promise<Integer> promiseOfInt = Akka.future(
  new Callable<Integer>() {
    public Integer call() {
      return intensiveComputation();
    }
  }
);
```

<!--
> **Note:** Here, the intensive computation will just be run on another thread. It is also possible to run it remotely on a cluster of backend servers using Akka remote.
-->
> **ノート:** ここでは、非常に時間のかかる計算を別スレッドで実行しています。その他に、このような計算を Akka remote を利用してバックエンドサーバのクラスタ上で実行することもできます。

<!--
## AsyncResult
-->
## AsyncResult

<!--
While we were using `Results.Status` until now, to send an asynchronous result we need an `Results.AsyncResult` that wraps the actual result:
-->
これまでは `Results.Status` を使ってきましたが、非同期な結果を送信するためには、結果をラップする `Results.AsyncResult` が必要です。

```java
public static Result index() {
  Promise<Integer> promiseOfInt = play.libs.Akka.future(
    new Callable<Integer>() {
      public Integer call() {
        return intensiveComputation();
      }
    }
  );
  return async(
    promiseOfInt.map(
      new Function<Integer,Result>() {
        public Result apply(Integer i) {
          return ok("Got result: " + i);
        } 
      }
    )
  );
}
```

<!--
> **Note:** `async()` is an helper method building an `AsyncResult` from a `Promise<Result>`.
-->
> **ノート:** `async()` は `Promise<Result>` から `AsyncResult` を作成するためのヘルパーメソッドです。

<!--
> **Next:** [[Streaming HTTP responses | JavaStream]]
-->
> **次ページ:** [[HTTPレスポンスのストリーミング | JavaStream]]