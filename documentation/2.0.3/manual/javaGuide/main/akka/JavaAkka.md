<!-- translated -->
<!--
# Integrating with Akka
-->
# Akka の統合

<!--
[[Akka| http://akka.io/]] uses the Actor Model to raise the abstraction level and provide a better platform to build correct concurrent and scalable applications. For fault-tolerance it adopts the ‘Let it crash’ model, which has been used with great success in the telecoms industry to build applications that self-heal - systems that never stop. Actors also provide the abstraction for transparent distribution and the basis for truly scalable and fault-tolerant applications.
-->
[[Akka| http://akka.io/]] はアクター・モデルに基づく高度な抽象化により、並列かつスケーラブルなアプリケーションを「正しく」構築するための基盤を提供してくれるライブラリです。Akka はアプリケーションのフォールトトレランス（耐障害性）を実現するために、`Let it crash` モデルを採用しています。このモデルは、自己回復により停止することなく動き続けるようなアプリケーションを構築できるということで、通信業界において大きな成功を収めています。また、Actor という概念を用いると、透過的に分散処理を行うことができるため、真にスケーラブルかつフォールトトレラントなアプリケーションを構築する足がかりになります。

<!--
## The application actor system
-->
## アプリケーションのアクターシステム

<!--
Akka 2.0 can work with several containers called `ActorSystems`. An actor system manages the resources it is configured to use in order to run the actors it contains. 

A Play application defines a special actor system to be used by the application. This actor system follows the application life-cycle and restarts automatically when the application restarts.

> **Note:** Nothing prevents you from using another actor system from within a Play application. The provided default actor system is just a convenient way to start a few actors without having to set-up your own.

You can access the default application actor system using the `play.libs.Akka` helper:
-->
Akka 2.0 は `アクターシステム` と呼ばれるいくつかのコンテナを持ちます。それぞれのアクターシステムは、それに含まれるアクターを動かすためのリソースを管理します。

Play 2.0 アプリケーションには、アプリケーション自身が使う特別なアクターシステムが用意されています。このアクターシステムはアプリケーションのライフサイクルに追従し、アプリケーションと共に自動的に再起動します。

> **ノート:** Play アプリケーション内から独自のアクターシステムを使っても何の問題もありません。最初から用意されているアクターシステムは、いちいちアクターシステムをセットアップしなくてすぐにアクターを利用できる、という利便性のためだけにあります。

アプリケーションのデフォルトのアクターシステムを利用するためには、`play.libs.Akka` ヘルパーを利用します。

```
ActorRef myActor = Akka.system().actorOf(new Props(MyActor.class));
```

<!--
## Configuration
-->
## 設定


<!--
The default actor system configuration is read from the Play application configuration file. For example to configure the default dispatcher of the application actor system, add these lines to the `conf/application.conf` file:
-->
デフォルトのアクターシステムの設定は、Play アプリケーションの設定ファイルから読み込まれます。例えば、アプリケーションのアクターシステムのデフォルトディスパッチャを変更したい場合は、 `conf/application.conf` ファイルにその設定を数行追加します。

```
akka.default-dispatcher.fork-join-executor.pool-size-max =64
akka.actor.debug.receive = on
```

<!--
> **Note:** You can also configure any other actor system from the same file, just provide a top configuration key.
-->
> **ノート:* Akka の規約に基づいて設定ファイルにトップの設定キーを記述することで、同じファイル内で全く別のアクターシステムを構成することもできます。

<!--
## Converting Akka `Future` to Play `Promise`
-->
## Akka の `Future` を Play の Promise に変換する

<!--
When you interact asynchronously with an Akka actor we will get `Future` object. You can easily convert them to play `Promise` using the conversion method provided in `play.libs.Akka.asPromise()`:
-->
Akka アクターと非同期的にやり取りをすると、 `Future` オブジェクトが返ってきます。`play.libs.Akka.asPromise()` メソッドを利用すると、この `Future` を Play の `Promise` オブジェクトに変換することができます。

```java
import static akka.pattern.Patterns.ask;
import play.libs.Akka;
import play.mvc.Result;
import static play.mvc.Results.async;
import play.libs.F.Function;

public static Result index() {
  return async(
    Akka.asPromise(ask(myActor,"hello", 1000)).map(
      new Function<Object,Result>() {
        public Result apply(Object response) {
          return ok(response.toString());
        }
      }
    )
  );
}
```

<!--
## Executing a block of code asynchronously
-->
## コードブロックを非同期的に実行する

<!--
A common use case within Akka is to have some computation performed concurrently without needing the extra utility of an Actor. If you find yourself creating a pool of Actors for the sole reason of performing a calculation in parallel, there is an easier (and faster) way:
-->
Akka の典型的なユースケースは、Actor を特別難しい使い方をせずに、計算を並列化することです。例えば、あなたが並列計算を行うためにアクターのプールを作成しているようなら、もっと簡単（かつ高速な）方法があります。

```java

import static play.libs.Akka.future;
import play.libs.F.*;
import java.util.concurrent.Callable;

public static Result index() {
  return async(
    future(new Callable<Integer>() {
      public Integer call() {
        return longComputation();
      }   
    }).map(new Function<Integer,Result>() {
      public Result apply(Integer i) {
        return ok("Got " + i);
      }   
    })
  );
}
```

<!--
## Scheduling asynchronous tasks
-->
## 非同期タスクをスケジューリングする

<!--
You can schedule sending messages to actors and executing tasks (functions or `Runnable` instances). You will get a `Cancellable` back that you can call `cancel` on to cancel the execution of the scheduled operation.

For example, to send a message to the `testActor` every 30 minutes:
-->
Akka では、アクターへのメッセージ送信やタスク(関数か、または `Runnable` インスタンス)の実行を予約することができます。予約を行うと、結果として `Cancellable` のインスタンスが返ってきます。その `cancel` メソッドを呼び出すことで、予約した操作の実行をキャンセルすることができます。

例えば、 `testActor` へ30秒おきにメッセージを送信したい場合は、次のようにします。

```
Akka.system().scheduler().schedule(
  Duration.create(0, TimeUnit.MILLISECONDS),
  Duration.create(30, TimeUnit.MINUTES)
  testActor, 
  "tick"
)
```

<!--
Alternatively, to run a block of code ten seconds from now:
-->
さらに、今から10秒後にコードブロックを一度だけ実行したい場合は、次のようにします。

```
Akka.system().scheduler().scheduleOnce(
  Duration.create(10, TimeUnit.SECONDS),
  new Runnable() {
    public void run() {
      file.delete()
    }
  }
); 
```

<!--
> **Next:** [[Internationalization | JavaI18N]]
-->
> **次ページ:** [[多言語化 | JavaI18N]]
