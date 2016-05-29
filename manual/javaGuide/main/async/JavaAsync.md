<!--- Copyright (C) 2009-2014 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Handling asynchronous results
-->
# 非同期レスポンスの処理

<!--
## Make controllers asynchronous
-->
## コントローラを非同期にする

<!--
Internally, Play Framework is asynchronous from the bottom up. Play handles every request in an asynchronous, non-blocking way.
-->
内部的には、Play Framework は根本から非同期です。Play はあらゆるリクエストを非同期かつノンブロッキングに取り扱います。

<!--
The default configuration is tuned for asynchronous controllers. In other words, the application code should avoid blocking in controllers, i.e., having the controller code wait for an operation. Common examples of such blocking operations are JDBC calls, streaming API, HTTP requests and long computations.
-->
デフォルトの設定は非同期なコントローラに最適化されています。別の言い方をすると、アプリケーションコードは、例えばコントローラのコードで命令の実行を待ってしまうなど、ブロッキングするコントローラを避けなければなりません。ブロッキング操作の一般的な例は、JDBC 呼び出し、streaming API、HTTP リクエスト、そして長時間の計算です。

<!--
Although it's possible to increase the number of threads in the default execution context to allow more concurrent requests to be processed by blocking controllers, following the recommended approach of keeping the controllers asynchronous makes it easier to scale and to keep the system responsive under load.
-->
ブロッキングコントローラでもより多くのリクエストを並列に処理できるよう、デフォルト実行コンテキストのスレッド数を増強することもできますが、推奨されるアプローチに従ってコントローラを非同期に保つことで、容易にスケールし、負荷時でもシステム応答を維持することができます。

<!--
## Creating non-blocking actions
-->
## ノンブロッキングなアクションの作成

<!--
Because of the way Play works, action code must be as fast as possible, i.e., non-blocking. So what should we return from our action if we are not yet able to compute the result? We should return the *promise* of a result!
-->
Play では、例えばノンブロッキングなどのように、アクションコードはできる限り速くなければなりません。それでは、まだ処理結果を計算できていない場合にアクションは何を返すべきでしょうか? Result の *promsie* を返すべきです!

<!--
A `Promise<Result>` will eventually be redeemed with a value of type `Result`. By using a `Promise<Result>` instead of a normal `Result`, we are able to return from our action quickly without blocking anything. Play will then serve the result as soon as the promise is redeemed.
-->
`Promise<Result>` は、最終的に `Result` 型の値に置き換えられます。通常の `Result` の代わりに `Promise<Result>` を使うことで、なにもブロックせず速やかにアクションからリターンすることができます。その後、promise が置き換え可能になり次第、Play は Result を提供します。

<!--
The web client will be blocked while waiting for the response, but nothing will be blocked on the server, and server resources can be used to serve other clients.
-->
レスポンスを待つ間に web クライアントはブロックされますが、サーバではなにもブロックされず、他のクライアントにサービスを提供するためにリソースを使うことできます。

<!--
## How to create a `Promise<Result>`
-->
## `Promise<Result>` の生成

<!--
To create a `Promise<Result>` we need another promise first: the promise that will give us the actual value we need to compute the result:
-->
`Promise<Result>` を生成するためには、まず別の promise が必要です: この promise は Result を計算するために必要な実際の値を提供します。

Java
: @[promise-pi](code/javaguide/async/JavaAsync.java)
<!--
> **Note:** Writing functional composition in Java is verbose. See the Java 8 sample for a more readable version using lambdas.
-->
> **注意:** Java で関数合成を書くのは冗長です。ラムダを使ったより読み易い Java 8 のサンプルを確認してください。

Java 8
: @[promise-pi](java8code/java8guide/async/JavaAsync.java)

<!--
Play asynchronous API methods give you a `Promise`. This is the case when you are calling an external web service using the `play.libs.WS` API, or if you are using Akka to schedule asynchronous tasks or to communicate with Actors using `play.libs.Akka`.
-->
Play の非同期 API メソッドは `Promise` を提供します。これは、`play.libs.WS` API を使って外部の Web サービスを呼び出す場合や、`play.libs.Akka` を使って Akka を使った非同期タスクを実行したり、アクターと通信したりする場合などが該当します。

<!--
A simple way to execute a block of code asynchronously and to get a `Promise` is to use the `promise()` helper:
-->
`promise()` ヘルパーを使うと、簡単にコードブロックを非同期で実行して `Promise` を取得できます:

Java
: @[promise-async](code/javaguide/async/JavaAsync.java)

Java 8
: @[promise-async](java8code/java8guide/async/JavaAsync.java)

<!--
> **Note:** It's important to understand which thread code runs on with promises. Here, the intensive computation will just be run on another thread.
>
> You can't magically turn synchronous IO into asynchronous by wrapping it in a `Promise`. If you can't change the application's architecture to avoid blocking operations, at some point that operation will have to be executed, and that thread is going to block. So in addition to enclosing the operation in a `Promise`, it's necessary to configure it to run in a separate execution context that has been configured with enough threads to deal with the expected concurrency. See [[Understanding Play thread pools|ThreadPools]] for more information.
>
> It can also be helpful to use Actors for blocking operations. Actors provide a clean model for handling timeouts and failures, setting up blocking execution contexts, and managing any state that may be associated with the service. Also Actors provide patterns like `ScatterGatherFirstCompletedRouter` to address simultaneous cache and database requests and allow remote execution on a cluster of backend servers. But an Actor may be overkill depending on what you need.
-->
> **注意:** どのスレッドコードがどの promise を実行しているか理解することが重要です。ここでは、集中的な計算は別のスレッドで実行されることになります。
> 
> 同期 IO を `Promise` で包んで魔法のように非同期にすることはできません。ブロッキング操作を避けるようアプリケーションアーキテクチャを変更できなければ、様々な場所でこの操作が実行され、スレッドがブロックされます。このため、操作を `Promsie` で包むだけではなく、期待する並列処理を取り扱うために充分なスレッドが設定されている別の実行コンテキストで実行されるよう、この操作を設定する必要があります。詳しくは [[Play のスレッドプールを理解する|ThreadPools]] を参照してください。
> 
> ブロッキング操作に Actor を使うのも便利です。Actor はタイムアウトおよび失敗の取り扱い、ブロッキング実行コンテキストの設定、そしてサービスに関連し得るあらゆる状態の管理のための、洗練されたモデルを備えています。また、Actor は同時に発生するキャッシュおよびデータベースへのリクエストに対応し、バックエンドサーバのクラスタ上でリモート実行できる `ScatterGatherFirstCompletedRouter` パターンを備えています。ただし、Actor は要件に対してやり過ぎかも知れません。

<!--
## Async results
-->
## 非同期な Result

<!--
We have been returning `Result` up until now. To send an asynchronous result our action needs to return a `Promise<Result>`:
-->
あとから作成される `Result` を返すことになります。非同期に Result を返すため、アクションは `Promise<Result>` を返す必要があります:

Java
: @[async](code/javaguide/async/controllers/Application.java)

Java 8
: @[async](java8code/java8guide/async/controllers/Application.java)

<!--
## Actions are asynchronous by default
-->
## アクションはデフォルトで非同期

<!--
Play [[actions|JavaActions]] are asynchronous by default. For instance, in the controller code below, the returned `Result` is internally enclosed in a promise:
-->
Play の [[actions|JavaActions]] はデフォルトで非同期です。例えば、以下のコントローラコードで返される `Result` は、内部的には promise に包まれています:

@[simple-action](/manual/javaGuide/main/http/code/javaguide/http/JavaActions.java)

<!--
> **Note:** Whether the action code returns a `Result` or a `Promise<Result>`, both kinds of returned object are handled internally in the same way. There is a single kind of `Action`, which is asynchronous, and not two kinds (a synchronous one and an asynchronous one). Returning a `Promise` is a technique for writing non-blocking code.
-->
> **注意:** アクションコードが `Result` または `Promise<Result>` のどちらを返そうとも、返却される各種オブジェクトは内部的に同じように取り扱われます。ただ一種類の非同期な `Action` があるだけで、(同期的なものと非同期的なものの) 二種類があるわけではありません。`Promise` を返却するのは、ノンブロッキングなコードを書くためのテクニックです。

<!--
> **Next:** [[Streaming HTTP responses | JavaStream]]
-->
> **次ページ:** [[HTTP レスポンスのストリーミング | JavaStream]]
