<!--
# Understanding Play thread pools
-->
# Play のスレッドプールを理解する

<!--
Play framework is, from the bottom up, an asynchronous web framework.  Streams are handled asynchronously using iteratees.  Thread pools are tuned to be low, in comparison to traditional web frameworks, since IO in play-core never blocks.
-->
Play framework は、下から上まで、非同期な web フレームワークです。ストリームは Iteratee を使って非同期に扱われます。play-core 内の IO はブロックされないので、伝統的な web フレームワークと比較して、スレッドプールは低目に調整されています。

<!--
Because of this, if you plan to write blocking IO code, or code that could potentially do a lot of CPU intensive work, you need to know exactly which thread pool is bearing that workload, and you need to tune it accordingly.  Doing blocking IO without taking this into account is likely to result in very poor performance from Play framework, for example, you may see only a few requests per second being handled, while CPU usage sits at 5%.  In comparison, benchmarks on typical development hardware (eg, a MacBook Pro) have shown Play to be able to handle workloads in the hundreds or even thousands of requests per second without a sweat when tuned correctly.
-->
このため、ブロッキング IO や、潜在的に多くの CPU を集約して実行可能なコードを書きたいと考えた場合に、どのスレッドプールがその処理を実行しているかを知り、それに応じて調整する必要があります。これを考慮に入れずにブロッキング IO を行うと、Play framework のパフォーマンスは貧弱になり得ます。例えば、1 秒あたりほんの少数のリクエストを扱う場合にも、 CPU 使用率が 5% に貼り付くのを目にするかもしれません。それに比べて、 MacBook Pro のような典型的な開発ハードウェア上のベンチマークでは、汗を流して正確に調整しなくても、 Play が毎秒何百、何千リクエストの仕事量を扱うことができることを示しました。

<!--
## Knowing when you are blocking
-->
## いつブロッキングされるかを知る

<!--
The most common place that a typical Play application will block is when it's talking to a database.  Unfortunately, none of the major databases provide asynchronous database drivers for the JVM, so for most databases, your only option is to using blocking IO.  A notable exception to this is [ReactiveMongo](http://reactivemongo.org/), a driver for MongoDB that uses Play's Iteratee library to talk to MongoDB.
-->
データベースと通信との通信は Play アプリケーションがブロックされる典型的な例です。不幸なことに、メジャーなデータベースでも JVM での非同期なドライバーを提供しているものはありませんし、ほとんどのデータベースではブロック IO を使うことを選ぶことになります。MongoDB 用のドライバーである [ReactiveMongo](http://reactivemongo.org/) は、Play の Iteratee ライブラリを使う注目すべき例外です。

<!--
Other cases when your code may block include:
-->
その他のブロックするかもしれないコードは以下のようになっています。

<!--
* Using REST/WebService APIs through a 3rd party client library (ie, not using Play's asynchronous WS API)
* Some messaging technologies only provide synchronous APIs to send messages
* When you open files or sockets directly yourself
* CPU intensive operations that block by virtue of the fact that they take a long time to execute
-->
* (Play の非同期 WS API を使わずに) サードパーティのクライアントライブラリで REST/Web サービス API を使う
* メッセージを送る際に、同期 API のみ提供されているメッセージ技術を使っている
* ファイル、ソケットを直接自分で Open した場合
* 実行に長い時間がかかるためブロックする CPU 集約的なオペレーション

<!--
In general, if the API you are using returns futures, it is non blocking, otherwise it is blocking.
-->
一般的に、使用している API が future を返す場合はノンブロッキングですが、そうでなければブロッキングです。

<!--
> Note that you may be tempted to therefore wrap your blocking code in Futures.  This does not make it non blocking, it just means the blocking will happen in a different thread.  You still need to make sure that the thread pool that you are using there has enough threads to handle the blocking.
-->
> このため、Future でブロッキングコードをラップするという誘惑に駆られるかもしれないことに注意してください。こうすることではノンブロッキングになりませんし、ブロッキングが他のスレッド内で起こるかもしれません。使用しているスレッドプールがブロッキングを扱うための十分なスレッドを持っていることを確かめる必要があります。

<!--
In contrast, the following types of IO do not block:
-->
対照的に、以下のタイプの IO はブロックしません:

<!--
* The Play WS API
* Asynchronous database drivers such as ReactiveMongo
* Sending/receiving messages to/from Akka actors
-->
* Play WS API
* ReactiveMongo のような非同期データベースドライバー
* Akka アクターへメッセージを送ったり、Akka アクターからのメッセージを受け取る

<!--
## Play's thread pools
-->
## Play のスレッドプール

<!--
Play uses a number of different thread pools for different purposes:
-->
Play は複数の異なる目的のためのスレッドプールを使っています。

* **Netty boss/worker thread pools** - These are used internally by Netty for handling Netty IO.  An applications code should never be executed by a thread in these thread pools.
* **Play Internal Thread Pool** - This is used internally by Play.  No application code should ever be executed by a thread in this thread pool, and no blocking should ever be done in this thread pool.  Its size can be configured by setting `internal-threadpool-size` in `application.conf`, and it defaults to the number of available processors.
* **Play default thread pool** - This is the default thread pool in which all application code in Play Framework is executed.  It is an Akka dispatcher, and can be configured by configuring Akka, described below.  By default, it has one thread per processor.
* **Akka thread pool** - This is used by the Play Akka plugin, and can be configured the same way that you would configure Akka.


<!--
## Using the default thread pool
-->
## デフォルトスレッドプールを使用する

<!--
All actions in Play Framework use the default thread pool.  When doing certain asynchronous operations, for example, calling `map` or `flatMap` on a future, you may need to provide an implicit execution context to execute the given functions in.  An execution context is basically another name for a thread pool.
-->
Play Framework での全アクションはデフォルトスレッドプールを使用します。例えば、ある非同期動作を行う場合、future での `map` あるいは `flatMap` を呼ぶ場合に、与えられた機能内での実行するために暗黙の実行コンテキストを提供する必要があるかもしれません。実行コンテキストは基本的にスレッドプール用の別名です。

<!--
In most situations, the appropriate execution context to use will be the Play default thread pool.  This can be used by importing it into your Scala source file:
-->
ほとんどの状況で、使用する適切な実行コンテキストは Play デフォルトスレッドプールになるでしょう。
Scala ソースファイルへ import することによって使用することができます:

@[global-thread-pool](code/ThreadPools.scala)

<!--
### Configuring the default thread pool
-->
### デフォルトスレッドプールを設定する

<!--
The default thread pool can be configured using standard Akka configuration in `application.conf` under the `play` namespace.  Here is the default configuration:
-->
デフォルトスレッドプールは `application.conf` 内の `play` 名前空間で標準 Akka 設定を使用して設定することができます。デフォルト設定は以下の通りです:

@[default-config](code/ThreadPools.scala)

<!--
This configuration instructs Akka to create one thread per available processor, with a maximum of 24 threads in the pool.  The full configuration options available to you can be found [here](http://doc.akka.io/docs/akka/2.2.0/general/configuration.html#Listing_of_the_Reference_Configuration).
-->
この設定はプール内で 24 スレッドを最大として、有効なプロセッサーごとにスレッドを一つ作成することを Akka に指示します。すべての設定可能なオプションは [ここ](http://doc.akka.io/docs/akka/2.2.0/general/configuration.html#Listing_of_the_Reference_Configuration) で見ることができます。

<!--
> Note that this configuration is separate from the configuration that the Play Akka plugin uses.  The Play Akka plugin is configured separately, by configuring akka in the root namespace (without the play { } surrounding it).
-->
> この設定が Play Akka plugin が使用する設定と分離していることに注意してください。Play Akka プラグインは、(play {} で囲まれていない) ルートネームスペース中の akka の設定によって、別々に設定されます。

<!--
## Using other thread pools
-->
## 他のスレッドプールを使う

<!--
In certain circumstances, you may wish to dispatch work to other thread pools.  This may include CPU heavy work, or IO work, such as database access.  To do this, you should first create a thread pool, this can be done easily in Scala:
-->
ある状況では、他のスレッドプールに仕事を割り当てたくなることがあります。このような状況には、データベースアクセスのような CPU 負荷の高い作業や IO が含まれるかもしれません。この処理を行うために、最初にスレッドプールを作成するべきです。これは、 Scala 内で簡単に行うことができます:

@[my-context-usage](code/ThreadPools.scala)

<!--
In this case, we are using Akka to create the execution context, but you could also easily create your own execution contexts using Java executors, or the Scala fork join thread pool, for example.  To configure this Akka execution context, you can add the following configuration to your `application.conf`:
-->
この場合、実行コンテキストを作成するために Akka を使います。しかし Java executor を使って自分の実行コンテキストを簡単に作りたくなることがあるかもしれません。 `application.conf` で以下のように設定することで追加することができます。

@[my-context-config](code/ThreadPools.scala)

<!--
To use this execution context in Scala, you would simply use the scala `Future` companion object function:
-->
Scala 内でのこの実行コンテキストを使用することで、 scala `Future` コンパニオンオブジェクトの関数で簡単に使うことができます。

@[my-context-explicit](code/ThreadPools.scala)

<!--
or you could just use it implicitly:
-->
または、暗黙的に使うこともできます。

@[my-context-implicit](code/ThreadPools.scala)

<!--
## Best practices
-->
## ベストプラクティス

<!--
How you should best divide work in your application between different thread pools greatly depends on the types work that your application is doing, and the control you want to have over how much of which work can be done in parallel.  There is no one size fits all solution to the problem, and the best decision for you will come from understanding the blocking IO requirements of your application and the implications they have on your thread pools.  It may help to do load testing on your application to tune and verify your configuration.
-->
アプリケーションにおける作業を異なるスレッドプール間でどのように割り振るべきかは、アプリケーションが実行している作業の種類、およびどれだけの作業を平行して行えるよう制御したいのかという要望に大きく依存します。全てのソリューションに合うただひとつの設定値はありませんので、アプリケーションのブロッキング IO 要件と、それらのスレッドプール上における意味を理解することで、最良の決定を行うことができます。設定値の調整および検証にはアプリケーションの負荷テストが役立つでしょう。

<!--
> Given the fact that JDBC is blocking thread pools can be sized to the # of connections available to a db pool assuming that the thread pool is used exclusively for database access. Any lesser amount of threads will not consume the number of connections available. Any more threads than the number of connections available could be wasteful given contention for the connections.
-->
> JDBC がスレッドをブロックするという事実を踏まえ、スレッドプールがデータベースアクセスのためだけに使われると仮定すると、スレッドプールの大きさは利用できる db プールへのコネクションのサイズに設定することができます。これより少ない量のスレッドでは、利用可能なコネクションの数を使い切ることはないでしょう。利用可能なコネクションの数よりも多いスレッドは、コネクションの競合により無駄になる可能性があります。

<!--
Below we outline a few common profiles that people may want to use in Play Framework:
-->
以下で、 Play Flamework で使用できるいくつかの一般的なプロファイルの概要を説明します。

<!--
### Pure asynchronous
-->
### ピュアな非同期化

<!--
In this case, you are doing no blocking IO in your application.  Since you are never blocking, the default configuration of one thread per processor suits your use case perfectly, so no extra configuration needs to be done.  The Play default execution context can be used in all cases.
-->
この場合、アプリケーションではブロッキング IO を行いません。決してブロッキングを行わないので、プロセッサーごとにひとつのスレッドを割り当てるデフォルトの設定がこのユースケースにぴったりですし、追加の設定を行う必要はありません。Play のデフォルト実行コンテキストがあらゆる状況で使われます。

<!--
### Highly synchronous
-->
### 高度な同期化

<!--
This profile matches that of a traditional synchronous IO based web framework, such as a Java servlet container.  It uses large thread pools to handle blocking IO.  It is useful for applications where most actions are doing database synchronous IO calls, such as accessing a database, and you don't want or need control over concurrency for different types of work.  This profile is the simplest for handling blocking IO.
-->
このプロファイルは Java servlet container のような従来の同期 IO ベースの web フレームワークにマッチします。ブロッキング IO を扱うために大きなスレッドプールを使います。ほとんどのアクションがデータベースアクセスを行う際に、同期 IO を行うようなアプリケーションで有効です。また、他のタイプの作業での平行性に対するコントロールを望まないし必要としない場合にも有効です。このプロファイルはブロッキング IO を扱う場合にもっとも簡単です。

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
### Many specific thread pools
-->
### 多くの特定のスレッドプール

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
Then in your code, you would create futures and pass the relevant execution context for the type of work that future was doing.
-->
この後、コードにて future を作成し、future が実行していた作業と関係のある実行コンテキストを引き渡します。

<!--
### Few specific thread pools
-->
### わずかな特定のスレッドプール

<!--
This is a combination between the many specific thread pools and the highly synchronized profile.  You would do most simple IO in the default execution context and set the number of threads there to be reasonably high (say 100), but then dispatch certain expensive operations to specific contexts, where you can limit the number of them that are done at one time.
-->
これは、多くの特定のスレッドプールと高度に同期化されたプロファイルの組み合わせです。デフォルト実行コンテキスト中でほとんどの単純な IO を行い、(100 くらいの) 合理的な複数のスレッドを設定しますが、その後、一度に行われる数を制限することのできる特定のコンテキストに負荷の高いオペレーションを割り振ります。
