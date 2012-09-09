<!--translated-->
<!--
# Handling asynchronous results
-->
# 非同期な結果の処理

<!--
## Why asynchronous results?
-->
## 非同期な結果はなぜ必要？

<!--
Until now, we were able to compute the result to send to the web client directly. This is not always the case: the result may depend of an expensive computation or on a long web service call.

Because of the way Play 2.0 works, action code must be as fast as possible (i.e. non blocking). So what should we return as result if we are not yet able to compute it? The response should be a promise of a result!

A `Promise<Result>` will eventually be redeemed with a value of type `Result`. By giving a `Promise<Result>` instead of a normal `Result`, we are able to compute the result quickly without blocking anything. Play will then serve this result as soon as the promise is redeemed. 

The web client will be blocked while waiting for the response but nothing will be blocked on the server, and server resources can be used to serve other clients.
-->
これまでは、Webクライアントへ送信する結果を即座に計算することができました。しかし、常にそれが可能なわけではありません。例えば、結果が非常に時間のかかる計算やWebサービス呼び出しに依存している場合はどうでしょうか。

Play 2.0の仕組み上、アクションのコードの実行は可能な限り高速でなければなりません（つまり、ノンブロッキングでなければならない）。だとすると、すぐに結果を計算できないときは、いったい何を返せばよいのでしょうか？その答えは、「結果の約束(Promise)」です！

`Promise<Result>`という「約束」は、最終的にResult型の値によって「果たされ」ます。通常の`Result`のかわりに`Promise<Result>`を返すことで、何もブロックせずに即座に結果を返すことができます。Playは後にPromiseが果たされたときに、内包された結果を自動的にクライアントへ送信します。

このとき、Webクライアントはレスポンスを待っている間ブロックされますが、サーバ側では何もブロックされていません。つまり、その間サーバ側のリソースは別のクライアントとやり取りするために利用することができます。

<!--
## How to create a `Promise<Result>`
-->
## `Promise<Result>`を作成する

<!--
To create a `Promise<Result>` we need another promise first: the promise that will give us the actual value we need to compute the result:  
-->
`Promise<Result>`を作成するには、まず別の`Promise`が必要です。そのPromiseとは、結果を計算するために必要な入力となる値を提供してくれるPromiseのことです。

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
> **Note:** Writing functional composition in Java is really verbose for at the moment, but it should be better when Java supports [[lambda notation| http://mail.openjdk.java.net/pipermail/lambda-dev/2011-September/003936.html]].

Play 2.0 asynchronous API methods give you a `Promise`. This is the case when you are calling an external web service using the `play.libs.WS` API, or if you are using Akka to schedule asynchronous tasks or to communicate with Actors using `play.libs.Akka`.

A simple way to execute a block of code asynchronously and to get a `Promise` is to use the `play.libs.Akka` helpers:
-->
> **ノート:** Javaで関数合成を記述することは現状では本当に面倒です。しかし、これはJavaが[[ラムダ記法| http://mail.openjdk.java.net/pipermail/lambda-dev/2011-September/003936.html]]をサポートすれば改善されるでしょう。

Play 2.0の非同期APIメソッドは結果として`Promise`を返します。例えば、`play.libs.WS` API経由で外部のWebサービスを呼び出したり、`play.libs.Akka`経由でAkkaを操ってタスクを予約したり、Actorと通信した結果は、全て`Promise`型の値として返ってきます。

具体例として、コードブロックを非同期で実行して、`Promise`を結果として得るには、`play.libs.Akka`ヘルパーを利用して次のコードを記述します。


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
> **ノート:** ここでは、非常に時間のかかる計算を別スレッドで実行しています。その他に、このような計算をAkka remoteを利用してバックエンドサーバのクラスタ上で実行することもできます。

<!--
## AsyncResult

While we were using `Results.Status` until now, to send an asynchronous result we need an `Results.AsyncResult` that wraps the actual result:
-->
## AsyncResult

非同期の結果を返すためには、これまで`Results.Status`を返していたところで、代わりに`Results.AsyncResult`によって結果をラップして返す必要があります。

```
public static Result index() {
  Promise<Integer> promiseOfInt = Akka.future(
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
> **ノート:** `async()`は`Promise<Result>`から`AsyncResult`を作成するためのヘルパーメソッドです。

<!--
> **Next:** [[Streaming HTTP responses | JavaStream]]
-->
> **次ページ:** [[HTTPレスポンスのストリーミング | JavaStream]]