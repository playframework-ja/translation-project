<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Understanding Play thread pools
-->
# Play のスレッドプールを理解する

<!--
Play framework is, from the bottom up, an asynchronous web framework.  Streams are handled asynchronously using iteratees.  Thread pools in Play are tuned to use fewer threads than in traditional web frameworks, since IO in play-core never blocks.
-->
Play framework は、完全に非同期な web フレームワークです。ストリームは Iteratee を使って非同期に扱われます。play-core 内の IO はブロックされないので、伝統的な web フレームワークと比較して、Play のスレッドプールは低目に調整されています。

<!--
Because of this, if you plan to write blocking IO code, or code that could potentially do a lot of CPU intensive work, you need to know exactly which thread pool is bearing that workload, and you need to tune it accordingly.  Doing blocking IO without taking this into account is likely to result in very poor performance from Play framework, for example, you may see only a few requests per second being handled, while CPU usage sits at 5%.  In comparison, benchmarks on typical development hardware (eg, a MacBook Pro) have shown Play to be able to handle workloads in the hundreds or even thousands of requests per second without a sweat when tuned correctly.
-->
このため、ブロッキング IO や、CPU 負荷が高くなりうるコードを書く場合には、どのスレッドプールがその処理を実行しているかを知り、それに応じて調整する必要があります。これを考慮に入れずにブロッキング IO を行うと、Play framework のパフォーマンスは貧弱になり得ます。例えば、1 秒あたりほんの少数のリクエストしかない場合にも、CPU 使用率が 5% を下回らないことがあるかもしれません。それに比べて、 MacBook Pro のような典型的な開発ハードウェア上のベンチマークでは、正確に調整すれば、 Play が毎秒何百、何千リクエストの仕事量を難なく扱うことができることを示しました。

<!--
## Knowing when you are blocking
-->
## いつブロッキングされるかを知る

<!--
The most common place where a typical Play application will block is when it's talking to a database.  Unfortunately, none of the major databases provide asynchronous database drivers for the JVM, so for most databases, your only option is to using blocking IO.  A notable exception to this is [ReactiveMongo](http://reactivemongo.org/), a driver for MongoDB that uses Play's Iteratee library to talk to MongoDB.
-->
データベースとの通信は Play アプリケーションがブロックされる典型的な例です。残念ながら、主要なデータベースに JVM での非同期なドライバを提供しているものはありませんので、ほとんどのデータベースではブロック IO を使わざるを得ません。注目すべき例外は [ReactiveMongo](http://reactivemongo.org/) で、これは Play の Iteratee ライブラリを使った MongoDB 用のドライバーです。

<!--
Other cases when your code may block include:
-->
ブロックが発生しうるその他の例は以下の通りです。

<!--
* Using REST/WebService APIs through a 3rd party client library (ie, not using Play's asynchronous WS API)
* Some messaging technologies only provide synchronous APIs to send messages
* When you open files or sockets directly yourself
* CPU intensive operations that block by virtue of the fact that they take a long time to execute
-->
* (Play の非同期 WS API を使わずに) サードパーティのクライアントライブラリで REST/Web サービス API を使用する場合
* メッセージを送る際に、同期 API のみ提供されているメッセージ技術を使用する場合
* ファイル、ソケットを直接自分で Open した場合
* 実行に長い時間がかかるためにブロックしてしまうような CPU 負荷の高い処理をおこなう場合

<!--
In general, if the API you are using returns `Future`s, it is non-blocking, otherwise it is blocking.
-->
一般的に、使用している API が `Future` を返す場合はノンブロッキングですが、そうでなければブロッキングです。

<!--
> Note that you may be tempted to therefore wrap your blocking code in Futures.  This does not make it non-blocking, it just means the blocking will happen in a different thread.  You still need to make sure that the thread pool that you are using has enough threads to handle the blocking.
-->
> このため、Future でブロッキングコードをラップするという誘惑に駆られるかもしれないことに注意してください。こうすることではノンブロッキングになりませんし、ブロッキングが他のスレッド内で起こるかもしれません。使用しているスレッドプールがブロッキングを扱うための十分なスレッドを持っていることを確かめる必要があります。

<!--
In contrast, the following types of IO do not block:
-->
これに対して、以下のタイプの IO はブロックしません。

<!--
* The Play WS API
* Asynchronous database drivers such as ReactiveMongo
* Sending/receiving messages to/from Akka actors
-->
* Play WS API
* ReactiveMongo のような非同期データベースドライバー
* Akka アクターとのメッセージの送受信

<!--
## Play's thread pools
-->
## Play のスレッドプール

<!--
Play uses a number of different thread pools for different purposes:
-->
Play は複数の異なる目的のためのスレッドプールを使っています。

<!--
* **Netty boss/worker thread pools** - These are used internally by Netty for handling Netty IO.  An application's code should never be executed by a thread in these thread pools.
* **Play default thread pool** - This is the thread pool in which all of your application code in Play Framework is executed.  It is an Akka dispatcher, and is used by the application `ActorSystem`. It can be configured by configuring Akka, described below.
-->
* **Netty のボス/ワーカー スレッドプール** - Netty IO を扱うために Netty によって内部的に使われています。アプリケーションのコードは、このスレッドプール内のスレッドによって実行されることはありません。
* **Play デフォルトスレッドプール** - Play Framework のすべてのアプリケーションコードが実行されるスレッドプールです。これは Akka のディスパッチャーであり、 `ActorSystem` アプリケーションで使用されます。Akka の設定で設定することができます。後述します。

<!--
> Note that in Play 2.4 several thread pools were combined together into the Play default thread pool.
-->
> Play 2.4 では、いくつかのスレッドプールが Play のデフォルトスレッドプールに統合されたことに注意してください。

<!--
## Using the default thread pool
-->
## デフォルトスレッドプールを使用する

<!--
All actions in Play Framework use the default thread pool.  When doing certain asynchronous operations, for example, calling `map` or `flatMap` on a future, you may need to provide an implicit execution context to execute the given functions in.  An execution context is basically another name for a `ThreadPool`.
-->
Play Framework での全てのアクションはデフォルトスレッドプールを使用します。例えば、ある非同期処理を行う場合、future での `map` あるいは `flatMap` を呼ぶ場合に、与えられた機能内での実行するために暗黙の実行コンテキストを提供する必要があるかもしれません。実行コンテキストは基本的に `ThreadPool` の別名です。

<!--
In most situations, the appropriate execution context to use will be the **Play default thread pool**.  This can be used by importing it into your Scala source file:
-->
大抵の場合は、実行コンテキストとして **Play のデフォルトスレッドプール** を使用するのが適切でしょう。これは Scala ソースファイルへ import することで使用できます。

@[global-thread-pool](code/ThreadPools.scala)

<!--
### Configuring the Play default thread pool
-->
### Play のデフォルトスレッドプールを設定する 

<!--
The default thread pool can be configured using standard Akka configuration in `application.conf` under the `akka` namespace. Here is default configuration for Play's thread pool:
-->
デフォルトスレッドプールは `application.conf` 内の akka 名前空間における標準 Akka 設定を使用することで設定できます。Play のスレッドプールのデフォルト設定は以下の通りです。

@[default-config](code/ThreadPools.scala)

<!--
This configuration instructs Akka to create 1 thread per available processor, with a maximum of 24 threads in the pool.
-->
この設定はプール内で 24 スレッドを最大として、有効なプロセッサーごとにスレッドを一つ作成することを Akka に指示します。

<!--
You can also try the default Akka configuration:
-->
あるいは、Akka のデフォルトの設定を試してみましょう。

@[akka-default-config](code/ThreadPools.scala)

<!--
The full configuration options available to you can be found [here](http://doc.akka.io/docs/akka/2.3.11/general/configuration.html#Listing_of_the_Reference_Configuration).
-->
すべての設定可能なオプションは [ここ](http://doc.akka.io/docs/akka/2.3.11/general/configuration.html#Listing_of_the_Reference_Configuration) で見ることができます。

<!--
## Using other thread pools
-->
## 他のスレッドプールを使う

<!--
In certain circumstances, you may wish to dispatch work to other thread pools.  This may include CPU heavy work, or IO work, such as database access.  To do this, you should first create a `ThreadPool`, this can be done easily in Scala:
-->
ある状況では、他のスレッドプールに仕事を割り当てたくなることがあります。例えば、CPU 負荷の高い処理やデータベースアクセスのような IO 処理です。そのためには、まず `スレッドプール` を作成するべきです。これは、 Scala 内で簡単に行うことができます。

@[my-context-usage](code/ThreadPools.scala)

<!--
In this case, we are using Akka to create the `ExecutionContext`, but you could also easily create your own `ExecutionContext`s using Java executors, or the Scala fork join thread pool, for example.  To configure this Akka execution context, you can add the following configuration to your `application.conf`:
-->
この場合、`ExecutionContext` を作成するために Akka を使います。しかし、例えば Java executor や Scala のフォークジョインスレッドプールを使って、自分の `ExecutionContext` を作ることもできます。Akka の実行コンテキストを設定するには、 `application.conf` で以下のような設定を追加してください。

@[my-context-config](code/ThreadPools.scala)

<!--
To use this execution context in Scala, you would simply use the scala `Future` companion object function:
-->
Scala 内でのこの実行コンテキストを使用するには、Scala の `Future` コンパニオンオブジェクトの関数をただ使うだけです。

@[my-context-explicit](code/ThreadPools.scala)

<!--
or you could just use it implicitly:
-->
または、暗黙的に使うこともできます。

@[my-context-implicit](code/ThreadPools.scala)

<!--
## Class loaders and thread locals
-->
## クラスローダとスレッドローカル

<!--
Class loaders and thread locals need special handling in a multithreaded environment such as a Play program.
-->
クラスローダとスレッドローカルは Play プログラムのようなマルチスレッド環境では特別に扱う必要があります。

<!--
### Application class loader
-->
### アプリケーションクラスローダ

<!--
In a Play application the [thread context class loader](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#getContextClassLoader--) may not always be able to load application classes. You should explicitly use the application class loader to load classes.
-->
Play アプリケーションでは、 [スレッドコンテキストクラスローダ](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#getContextClassLoader--) は必ずしも常にアプリケーションクラスをロードできるわけではありません。クラスをロードするにはアプリケーションクラスローダを明示的に使用する必要があります。

Java
: @[using-app-classloader](code/ThreadPoolsJava.java)

Scala
: @[using-app-classloader](code/ThreadPools.scala)

<!--
Being explicit about loading classes is most important when running Play in development mode (using `run`) rather than production mode. That's because Play's development mode uses multiple class loaders so that it can support automatic application reloading. Some of Play's threads might be bound to a class loader that only knows about a subset of your application's classes.
-->
クラスのロードを明示的に行うことは、本番モードよりもむしろ、(`run` を使って) Play を開発モードで動かす際に最も重要です。なぜなら、Play の開発モードは複数のクラスローダを使用することで、自動的なアプリケーションリロードをサポートしているからです。Play のスレッドのなかには、アプリケーションのクラスのサブセットについて知るただ唯一のクラスローダに紐付いているものもあります。

<!--
In some cases you may not be able to explicitly use the application classloader. This is sometimes the case when using third party libraries. In this case you may need to set the [thread context class loader](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#getContextClassLoader--) explicitly before you call the third party code. If you do, remember to restore the context class loader back to its previous value once you've finished calling the third party code.
-->
アプリケーションのクラスローダを明示的に使用できない場合もあります。例えばサードパーティのライブラリを使用している場合です。この場合、 [スレッドコンテキストクラスローダ](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#getContextClassLoader--) をサードパーティのコードを呼ぶ前に明示的に渡す必要があります。その場合、サードパーティのコードが終了した際には、コンテキストクラスローダを元の値に戻すことを忘れないでください。

<!--
### Java thread locals
-->
### Java のスレッドローカル

<!--
Java code in Play uses a `ThreadLocal` to find out about contextual information such as the current HTTP request. Scala code doesn't need to use `ThreadLocal`s because it can use implicit parameters to pass context instead. `ThreadLocal`s are used in Java so that Java code can access contextual information without needing to pass context parameters everywhere.
-->
Play 内の Java コードは、HTTP リクエストなどコンテキストのある情報を取得するのに `ThreadLocal` を使用します。 これに対し、 Scala コードでは暗黙のパラメータとしてコンテキストを渡すので、`ThreadLocal` を使用する必要はありません。 `ThreadLocal` が Java で使用されるのは、 Java のコードがコンテキストのある情報にアクセスする際に、コンテキストパラメータをあちこちで引き回さなくても済むようにするためです。

<!--
Java `ThreadLocal`s, along with the correct context `ClassLoader`, are propagated automatically by `ExecutionContextExecutor` objects provided through the `HttpExecution` class. (An `ExecutionContextExecutor` is both a Scala `ExecutionContext` and a Java `Executor`.) These special `ExecutionContextExecutor` objects are automatically created and used by Java actions and Java `Promise` methods. The default objects wrap the default user thread pool. If you want to do your own threading then you should use the `HttpExecution` class' helper methods to get an `ExecutionContextExecutor` object yourself.
-->
Java の `ThreadLocal` (正しいコンテキストに沿うと `ClassLoader`) は、 `HttpExecution` クラスから提供する `ExecutionContextExecutor` オブジェクトが自動的に伝播します (`ExecutionContextExecutor` は Scala でいう `ExecutionContext` で、Java では `Executor`です)。これらの特別な `ExecutionContextExecutor` オブジェクトは自動的に作成され、Java のアクションや Java の `Promise` メソッドで使用されます。デフォルトのオブジェクトはユーザーのデフォルトスレッドプールをラップします。独自にスレッドを運用したい場合は、 `HttpExecution` クラスのヘルパーメソッドを使用して  `ExecutionContextExecutor` オブジェクトを自分で取得してください。

<!--
In the example below, a user thread pool is wrapped to create a new `ExecutionContext` that propagates thread locals correctly.
-->
以下の例では、ユーザーのスレッドプールをラップして、スレッドローカルを正しく伝播する新しい `ExecutionContext` を作成しています。

@[async-explicit-ec-imports](../../working/javaGuide/main/async/code/javaguide/async/controllers/Application.java)
@[async-explicit-ec](../../working/javaGuide/main/async/code/javaguide/async/controllers/Application.java)

<!--
## Best practices
-->
## ベストプラクティス

<!--
How you should best divide work in your application between different thread pools greatly depends on the types of work that your application is doing, and the control you want to have over how much of which work can be done in parallel.  There is no one size fits all solution to the problem, and the best decision for you will come from understanding the blocking-IO requirements of your application and the implications they have on your thread pools. It may help to do load testing on your application to tune and verify your configuration.
-->
アプリケーションにおける作業を異なるスレッドプール間でどのように割り振るべきかは、アプリケーションが実行している作業の種類、およびどれだけの作業を平行して行えるよう制御したいのかという要望に大きく依存します。万能策はありません。アプリケーションのブロッキング IO 要件と、それらのスレッドプール上における意味を理解することで、最良の決定を行うことができます。設定値の調整および検証にはアプリケーションの負荷テストが役立つでしょう。

<!--
> Given the fact that JDBC is blocking thread pools can be sized to the # of connections available to a db pool assuming that the thread pool is used exclusively for database access. Any lesser amount of threads will not consume the number of connections available. Any more threads than the number of connections available could be wasteful given contention for the connections.
-->
> 前述のとおり JDBC はスレッドをブロックするので、スレッドプールがデータベースアクセスのためだけに使われると仮定すると、スレッドプールの数は利用可能な db プールへのコネクションの数に設定することができます。スレッドがこれ未満の数であれば、利用可能なコネクションの数を使い切ることはないでしょう。スレッドが利用可能なコネクションの数よりも多いと、コネクションの競合により無駄になる可能性があります。

<!--
Below we outline a few common profiles that people may want to use in Play Framework:
-->
以下で、 Play Framework で使用できるいくつかの一般的なプロファイルの概要を説明します。

<!--
### Pure asynchronous
-->
### 純粋に非同期的

<!--
In this case, you are doing no blocking IO in your application.  Since you are never blocking, the default configuration of one thread per processor suits your use case perfectly, so no extra configuration needs to be done.  The Play default execution context can be used in all cases.
-->
この場合、アプリケーションではブロッキング IO を行いません。決してブロッキングを行わないので、プロセッサーごとにひとつのスレッドを割り当てるデフォルトの設定がこのユースケースにぴったりですし、追加の設定を行う必要はありません。Play のデフォルト実行コンテキストがあらゆる状況で使われます。

<!--
### Highly synchronous
-->
### 高度に同期的

<!--
This profile matches that of a traditional synchronous IO based web framework, such as a Java servlet container.  It uses large thread pools to handle blocking IO.  It is useful for applications where most actions are doing database synchronous IO calls, such as accessing a database, and you don't want or need control over concurrency for different types of work.  This profile is the simplest for handling blocking IO.
-->
このプロファイルは、Java のサーブレットコンテナのように伝統的な同期 IO ベースの web フレームワークにマッチします。ブロッキング IO を扱うために大きなスレッドプールを使います。特に、ほとんどのアクションがデータベースアクセスを行う際に同期 IO を行い、他の種類の処理での平行性に対する制御を望まない・必要としないようなアプリケーションで有効です。このプロファイルはブロッキング IO を扱う場合にもっとも単純です。

<!--
In this profile, you would simply use the default execution context everywhere, but configure it to have a very large number of threads in its pool, like so:
-->
このプロファイルでは、どこでも単純にデフォルト実行コンテキストを使いますが、以下のように、そのプールに非常に多くのスレッドを持つように設定する必要があります。

@[highly-synchronous](code/ThreadPools.scala)

<!--
This profile is recommended for Java applications that do synchronous IO, since it is harder in Java to dispatch work to other threads.
-->
このプロファイルは同期 IO を行う Java アプリケーションで推奨されます。Java では他のスレッドに作業を割り当てるのがより難しいためです。

<!--
Note that we use the same value for `parallelism-min` and `parallelism-max`. The reason is that the number of threads is defined by the following formulas :
-->
`parallelism-min` と `parallelism-max` に同じ値を設定していることに着目してください。これは、スレッドの数が以下の式によって決定されるからです。

<!--
>base-nb-threads = nb-processors * parallelism-factor
 parallelism-min <= actual-nb-threads <= parallelism-max
-->
>基底スレッド数 = プロセッサ数 * 平行因数
 parallelism-min <= 実際のスレッド数 <= parallelism-max

<!--
So if you don't have enough available processors, you will never be able to reach the `parallelism-max` setting.
-->
したがって、使用可能なプロセッサが十分にない場合には、 `parallelism-max` 設定値に達することができません。

<!--
### Many specific thread pools
-->
### 多くの特別なスレッドプール

<!--
This profile is for when you want to do a lot of synchronous IO, but you also want to control exactly how much of which types of operations your application does at once.  In this profile, you would only do non blocking operations in the default execution context, and then dispatch blocking operations to different execution contexts for those specific operations.
-->
このプロファイルはたくさんの同期 IO を、アプリケーションがどのタイプの作業を直ちに実行するか正確にコントロールしながら実行したい場合のためのものです。このプロファイルでは、デフォルト実行コンテキストでノンブロッキングに作業を行い、特別な作業を異なる実行コンテキストでブロッキングオペレーションに割り当てます。

<!--
In this case, you might create a number of different execution contexts for different types of operations, like this:
-->
この場合、以下のような異なるタイプのオペレーションに対して複数の異なる実行コンテキストを作る必要があります。

@[many-specific-contexts](code/ThreadPools.scala)

<!--
These might then be configured like so:
-->
これらは以下のように設定します。

@[many-specific-config](code/ThreadPools.scala)

<!--
Then in your code, you would create `Future`s and pass the relevant `ExecutionContext` for the type of work that `Future` was doing.
-->
それからコードにて `Future` を作成し、この `Future` が実行していた作業と関連する `ExecutionContext` を引き渡します。

<!--
> **Note:** The configuration namespace can be chosen freely, as long as it matches the dispatcher ID passed to `Akka.system.dispatchers.lookup`.
-->
> **Note:** 設定の名前空間は、 `Akka.system.dispatchers.lookup` に渡したディスパッチャー ID がマッチする限り、自由に設定できます。

<!--
### Few specific thread pools
-->
### 少数の特別なスレッドプール

<!--
This is a combination between the many specific thread pools and the highly synchronized profile.  You would do most simple IO in the default execution context and set the number of threads there to be reasonably high (say 100), but then dispatch certain expensive operations to specific contexts, where you can limit the number of them that are done at one time.
-->
これは、多くの特別なスレッドプールと高度に同期化されたプロファイルの組み合わせです。デフォルト実行コンテキスト中でほとんどの単純な IO を行い、(100 くらいの) 合理的な複数のスレッドを設定しますが、その後、一度に行われる数を制限することのできる特別なコンテキストに負荷の高いオペレーションを割り振ります。
