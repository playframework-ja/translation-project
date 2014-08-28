<!--
# Integrating with Akka
-->
# Akka との統合

<!--
[Akka](http://akka.io/) uses the Actor Model to raise the abstraction level and provide a better platform to build correct concurrent and scalable applications. For fault-tolerance it adopts the ‘Let it crash’ model, which has been used with great success in the telecoms industry to build applications that self-heal - systems that never stop. Actors also provide the abstraction for transparent distribution and the basis for truly scalable and fault-tolerant applications.
-->
[Akka](http://akka.io/) は抽象レベルを上げるためにアクターモデルを利用し、正しい平行処理のスケーラブルなアプリケーションを構築するためにより良いプラットフォームを提供します。
耐障害性を確保するために、通信業界において、自己回復することで決して停止することがないアプリケーションの構築で大きな成功をおさめている「Let it crash」という設計モデルを採用しています。
また、アクターモデルは透過的な分散環境や本当にスケーラブルで耐障害性の高いアプリケーションのための抽象化も提供します。

<!--
## The application actor system
-->
## アプリケーションのアクターシステム

<!--
Akka 2.0 can work with several containers called `ActorSystems`. An actor system manages the resources it is configured to use in order to run the actors which it contains. 
-->
Akka 2.0 は `ActorSystems` と呼ばれる複数のコンテナと共に動作することができます。
アクターシステムはそれが含むアクターを実行するために順番に利用されるよう設定されたリソースを管理します。

<!--
A Play application defines a special actor system to be used by the application. This actor system follows the application life-cycle and restarts automatically when the application restarts.
-->
Play アプリケーションは Play アプリケーションで利用される特殊なアクターシステムを定義します。
このアクターシステムは Play アプリケーションのライフサイクルに従い、Play アプリケーションがリスタートすると自動的に再起動します。　

<!--
> **Note:** Nothing prevents you from using another actor system from within a Play application. The provided default is convenient if you only need to start a few actors without bothering to set-up your own actor system.
-->
> **注釈:** あなたが別のアクターシステムを Play アプリケーション内で利用することは阻害されることはありません。提供されているデフォルトのアクターシステムはもしあなたがアクターシステムをセットアップすることなくアクターを使い始める必要があれば便利です。

<!--
You can access the default application actor system using the `play.api.libs.concurrent.Akka` helper:
-->
デフォルトのアプリケーションアクターシステムには `play.api.libs.concurrent.Akka` ヘルパーでアクセスすることができます。

@[play-akka-myactor](code/ScalaAkka.scala)


<!--
## Configuration
-->
## 設定

<!--
The default actor system configuration is read from the Play application configuration file. For example, to configure the default dispatcher of the application actor system, add these lines to the `conf/application.conf` file:
-->

デフォルトのアクターシステムの設定は Play のアプリケーション設定ファイルから読み込まれます。
例えば、アプリケーションアクターシステムのデフォルトのディスパッチャーを設定するには、以下のような行を `conf/application.conf` に追加します：

```
akka.default-dispatcher.fork-join-executor.pool-size-max =64
akka.actor.debug.receive = on
```

<!--
> **Note:** You can also configure any other actor system from the same file; just provide a top configuration key.
-->
> **注釈:** 別のアクターシステムを同じ設定ファイルで設定することもできます。ただ最上位の設定キーを指定するだけです。


<!--
## Scheduling asynchronous tasks
-->
## 非同期のタスクをスケジューリングする

<!--
You can schedule sending messages to actors and executing tasks (functions or `Runnable`). You will get a `Cancellable` back that you can call `cancel` on to cancel the execution of the scheduled operation.
-->

アクターへのメッセージ送信やタスク実行（関数もしくは `Runnable` インタフェースを実装したインスタンス）をスケジューリングすることができます。
`Cancellable` オブジェクトが返されるので、それの `cancel` メソッドを呼び出せばスケジューリングされたオペレーションの実行をキャンセルすることができます。

<!--
For example, to send a message to the `testActor` every 30 microseconds:
-->

例えば、`testActor` というアクターに 30 マイクロ秒毎にメッセージを送信するにはこのようにします：

@[play-akka-actor-schedule-repeat](code/ScalaAkka.scala)

<!--
> **Note:** This example uses implicit conversions defined in `scala.concurrent.duration` to convert numbers to `Duration` objects with various time units.
-->

> **注釈:** この例は数字を時間単位つきの `Duration` オブジェクトに変換するための  `scala.concurrent.duration` で定義されている暗黙の型変換（implicit conversion）を使っています。

<!--
Similarly, to run a block of code one seconds from now:
-->

以下は別の例でコードブロックを今から 1 秒後に実行します：

@[play-akka-actor-schedule-run-once](code/ScalaAkka.scala)

<!--
> **Next:** [[Internationalization | ScalaI18N]]
-->

> **次:** [[国際化 | ScalaI18N]]

