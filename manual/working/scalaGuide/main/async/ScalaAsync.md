<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Handling asynchronous results
-->
# 非同期レスポンスの処理

<!--
## Make controllers asynchronous
-->
## コントローラーを非同期に

<!--
Internally, Play Framework is asynchronous from the bottom up. Play handles every request in an asynchronous, non-blocking way.
-->
内部的に、Play Framework は完全に非同期処理です。Play は全てのリクエストを非同期で、ノンブロッキングに処理します。

<!--
The default configuration is tuned for asynchronous controllers. In other words, the application code should avoid blocking in controllers, i.e., having the controller code wait for an operation. Common examples of such blocking operations are JDBC calls, streaming API, HTTP requests and long computations.
-->
標準設定は非同期のコントローラー用に最適化されています。言い換えると、アプリケーションコードはコントローラーをブロックさせないべきで、すなわち、コントローラーのコードをオペレーションのために待たせることです。ブロックするオペレーションの一般的な例として、JDBC の呼び出し、ストリーミング API 、HTTP のリクエスト、そして長い計算処理に当たります。

<!--
Although it's possible to increase the number of threads in the default execution context to allow more concurrent requests to be processed by blocking controllers, following the recommended approach of keeping the controllers asynchronous makes it easier to scale and to keep the system responsive under load.
-->
より多くの並列のリクエストをコントローラーをブロックすることで可能とする標準の実行コンテキストでスレッド数を増やすことは可能ですが、以下に示す非同期のコントローラを維持するおすすめのアプローチはより簡単にスケールし、システムを低負荷なレスポンスに抑えられます。

<!--
## Creating non-blocking actions
-->
## ノンブロッキングなアクションの作成

<!--
Because of the way Play works, action code must be as fast as possible, i.e., non-blocking. So what should we return as result if we are not yet able to generate it? The response is a *future* result!
-->
Play の仕組み上、アクションの実行は可能な限り早く (言い換えると、ノンブロッキングに) 完了しなければなりません。では、レスポンスがまだ生成可能でないときに、一体何を返すべきでしょうか? それは、 レスポンスの *Future*  です!

<!--
A `Future[Result]` will eventually be redeemed with a value of type `Result`. By giving a `Future[Result]` instead of a normal `Result`, we are able to quickly generate the result without blocking. Play will then serve the result as soon as the promise is redeemed.
-->
`Future[Result]` は最終的に `Result` 型の値を埋めます。通常の `Result` のかわりに `Future[Result]` を返すことで、何もブロックせずに即座に結果を返すことができます。Play は約束が果たされるとすぐに結果を返します。

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
> **Note:** It's important to understand which thread code runs on with futures. In the two code blocks above, there is an import on Plays default execution context. This is an implicit parameter that gets passed to all methods on the future API that accept callbacks. The execution context will often be equivalent to a thread pool, though not necessarily.
-->
> **メモ:** スレッドのコードは Future と実行されることを理解することが重要です。上記の二つのコードブロックでは、 Play の標準実行コンテキストのインポートがあります。これは implicit parameter で、コールバックを許容している Future API 上の全てのメソッドに渡されます。実行コンテキストはしばしばスレッドプールと等価で、必要ではありません。
>
<!--
> You can't magically turn synchronous IO into asynchronous by wrapping it in a `Future`. If you can't change the application's architecture to avoid blocking operations, at some point that operation will have to be executed, and that thread is going to block. So in addition to enclosing the operation in a `Future`, it's necessary to configure it to run in a separate execution context that has been configured with enough threads to deal with the expected concurrency. See [[Understanding Play thread pools|ThreadPools]] for more information.
-->
> 魔法のようにに同期の IO を `Future` で内包しても、非同期には変えられません。もしアプリケーションの構造的にオペレーションのブロックを避けられないのであれば、その場合はスレッドをブロックしてオペレーションを実行するしかないでしょう。`Future` でオペレーションを囲んだ際に、想定される並列数を扱えるのに十分なスレッドを構成できるような別の実行コンテキストで動かせるように設計する必要があります。詳しくは [[Play のスレッドプールを理解する | ThreadPools]] を参照してください。
>
<!--
> It can also be helpful to use Actors for blocking operations. Actors provide a clean model for handling timeouts and failures, setting up blocking execution contexts, and managing any state that may be associated with the service. Also Actors provide patterns like `ScatterGatherFirstCompletedRouter` to address simultaneous cache and database requests and allow remote execution on a cluster of backend servers. But an Actor may be overkill depending on what you need.
-->
ブロッキングオペレーションを扱うのに Actor もまた便利です。 Actor はタイムアウトや失敗、ブロッキング実行コンテキストの設定やサービスに関連した状態を扱うのに完全なモデルです。 Actor はまた、`ScatterGatherFirstCompletedRouter` のような同時的なキャッシュへのアクセスやデータベースへのリクエストやクラスタのバックエンドサーバ上の遠隔の実行もできるようなものを提供しています。しかし Actor は過剰供給でもあるでしょう。

<!--
## Returning futures
-->
## Future を返す

<!--
While we were using the `Action.apply` builder method to build actions until now, to send an asynchronous result we need to use the `Action.async` builder method:
-->
これまでは、アクションをビルドするために `Action.apply` ビルダーメソッドを使ってきましたが、非同期に Result を返すためには `Action.async` ビルダーメソッドを使う必要があります

@[async-result](code/ScalaAsync.scala)

<!--
## Actions are asynchronous by default
-->
## アクションは標準で非同期

<!--
Play [[actions|ScalaActions]] are asynchronous by default. For instance, in the controller code below, the `{ Ok(...) }` part of the code is not the method body of the controller. It is an anonymous function that is being passed to the `Action` object's `apply` method, which creates an object of type `Action`. Internally, the anonymous function that you wrote will be called and its result will be enclosed in a `Future`.
-->
Play [[actions|ScalaActions]] は、標準で非同期です。例えば、以下のコントローラーコードを見ると、`{ Ok(...) }` 部分は、コントローラーのボディのメソッドではありません。`Action` 型のオブジェクトを作る `Action` オブジェクトの `apply` メソッドに渡された無名関数です。内部的には、あなたが書いた無名関数はいずれ呼ばれ、結果は `Future` に囲まれます。

@[echo-action](../http/code/ScalaActions.scala)

<!--
> **Note:** Both `Action.apply` and `Action.async` create `Action` objects that are handled internally in the same way. There is a single kind of `Action`, which is asynchronous, and not two kinds (a synchronous one and an asynchronous one). The `.async` builder is just a facility to simplify creating actions based on APIs that return a `Future`, which makes it easier to write non-blocking code.
-->
> **メモ:** `Action.apply` と `Action.async` は、内部的に同じように扱う `Action` オブジェクトを作成しています。非同期の一種類の `Action` があり、(同期のものと非同期のもののように)二種類あるわけではありません。`.async` ビルダーは、ノンブロッキングコードを簡単に書ける `Future` を返す API ベースの Action を簡単にそして単純に作れます。 

<!--
## Handling time-outs
-->
## タイムアウト処理

<!--
It is often useful to handle time-outs properly, to avoid having the web browser block and wait if something goes wrong. You can easily compose a promise with a promise timeout to handle these cases:
-->
何らかの問題が発生したとき web ブラウザが延々とブロックしてしまうことを避けるために、タイムアウトが役立つことが多々あります。そのような場合、 Promise と Promise のタイムアウトを簡単に組み合わせることができます。

@[timeout](code/ScalaAsync.scala)
