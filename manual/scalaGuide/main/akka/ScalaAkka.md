<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Integrating with Akka
-->
# Akka の統合

<!--
[Akka](http://akka.io/) uses the Actor Model to raise the abstraction level and provide a better platform to build correct concurrent and scalable applications. For fault-tolerance it adopts the ‘Let it crash’ model, which has been used with great success in the telecoms industry to build applications that self-heal - systems that never stop. Actors also provide the abstraction for transparent distribution and the basis for truly scalable and fault-tolerant applications.
-->
[Akka](http://akka.io/) は抽象レベルを上げるためにアクターモデルを利用し、正しい平行処理のスケーラブルなアプリケーションを構築するためにより良いプラットフォームを提供します。耐障害性を確保するために、通信業界において、自己回復することで決して停止することがないアプリケーションの構築で大きな成功をおさめている 'Let it crash' という設計モデルを採用しています。また、アクターモデルは透過的な分散環境や本当にスケーラブルで耐障害性の高いアプリケーションのための抽象化も提供します。

<!--
## The application actor system
-->
## アプリケーションのアクターシステム

<!--
Akka can work with several containers called `ActorSystems`. An actor system manages the resources it is configured to use in order to run the actors which it contains. 
-->
Akka は `アクターシステム` と呼ばれるいくつかのコンテナを持ちます。それぞれのアクターシステムは、それに含まれるアクターを動かすためのリソースを管理します。

<!--
A Play application defines a special actor system to be used by the application. This actor system follows the application life-cycle and restarts automatically when the application restarts.
-->
Play アプリケーションには、アプリケーション自身が使う特別なアクターシステムが定義されています。このアクターシステムはアプリケーションのライフサイクルに追従し、アプリケーションと共に自動的に再起動します。

<!--
> **Note:** Nothing prevents you from using another actor system from within a Play application. The provided default is convenient if you only need to start a few actors without bothering to set-up your own actor system.
-->
> **Note:** 独自のアクターシステムを利用しても全く問題ありません。デフォルトのアクターシステムは、実行するアクターの数が少なく、別のアクター・システムを自分で用意するまでもないような場合に利用するとよいでしょう。

<!--
You can access the default application actor system using the `play.api.libs.concurrent.Akka` helper:
-->
アプリケーションのデフォルトのアクターシステムを利用するためには、`play.api.libs.concurrent.Akka` ヘルパーを利用します。

@[play-akka-myactor](code/ScalaAkka.scala)


<!--
## Configuration
-->
## 設定

<!--
The default actor system configuration is read from the Play application configuration file. For example, to configure the default dispatcher of the application actor system, add these lines to the `conf/application.conf` file:
-->
デフォルトのアクターシステムの設定は、Play アプリケーションの設定ファイルから読み込まれます。例えば、アプリケーションのアクターシステムのデフォルトディスパッチャを変更したい場合は、 `conf/application.conf` ファイルにその設定を数行追加します。

```
akka.default-dispatcher.fork-join-executor.pool-size-max =64
akka.actor.debug.receive = on
```

<!--
> **Note:** You can also configure any other actor system from the same file; just provide a top configuration key.
-->
> **Note:** Akka の規約に基づいて設定ファイルにトップの設定キーを記述することで、同じファイル内で全く別のアクターシステムを構成することもできます。

<!--
For Akka logging configuration, see [[configuring logging|SettingsLogger]].
-->
Akka のログ設定については、 [[ログの設定|SettingsLogger]] を参照してください。

<!--
## Scheduling asynchronous tasks
-->
## 非同期タスクのスケジューリング

<!--
You can schedule sending messages to actors and executing tasks (functions or `Runnable`). You will get a `Cancellable` back that you can call `cancel` on to cancel the execution of the scheduled operation.
-->
Akka では、アクターへのメッセージ送信やタスク(関数または `Runnable`)の実行を予約することができます。予約を行うと、結果として `Cancellable` のインスタンスが返ってきます。その `cancel` メソッドを呼び出すことで、予約した操作の実行をキャンセルすることができます。

<!--
For example, to send a message to the `testActor` every 300 microseconds:
-->
例えば、`testActor` というアクターに 300 マイクロ秒毎にメッセージを送信するにはこのようにします:

@[play-akka-actor-schedule-repeat](code/ScalaAkka.scala)

<!--
> **Note:** This example uses implicit conversions defined in `scala.concurrent.duration` to convert numbers to `Duration` objects with various time units.
-->
> **Note:** この例では `scala.concurrent.duration` に定義されている implicit conversion　を利用して、数値を時間単位の異なる `Duration` オブジェクトへ変換しています。

<!--
Similarly, to run a block of code one seconds from now:
-->
同様に、コードブロックを今から 1 秒後に実行するには、次のように書きます:

@[play-akka-actor-schedule-run-once](code/ScalaAkka.scala)

<!--
> **Next:** [[Internationalization | ScalaI18N]]
-->
> **次ページ:** [[多言語対応 | ScalaI18N]]
