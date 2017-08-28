<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
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
Akka can work with several containers called actor systems. An actor system manages the resources it is configured to use in order to run the actors which it contains.
-->
Akka は アクターシステムと呼ばれるいくつかのコンテナを持ちます。それぞれのアクターシステムは、それに含まれるアクターを動かすためのリソースを管理します。

<!--
A Play application defines a special actor system to be used by the application. This actor system follows the application life-cycle and restarts automatically when the application restarts.
-->
Play アプリケーションには、アプリケーション自身が使う特別なアクターシステムが定義されています。このアクターシステムはアプリケーションのライフサイクルに追従し、アプリケーションと共に自動的に再起動します。

<!--
### Writing actors
-->
### アクターの記述

<!--
To start using Akka, you need to write an actor.  Below is a simple actor that simply says hello to whoever asks it to.
-->
Akka を使い始めるには、アクターを記述する必要があります。以下の例は、どんな問い合わせに対しても単純に Hello を返すだけのシンプルなアクターです。

@[actor](code/ScalaAkka.scala)

<!--
This actor follows a few Akka conventions:
-->
このアクターは、いくつかの Akka の慣例に従っています。

<!--
* The messages it sends/receives, or its _protocol_, are defined on its companion object
* It also defines a `props` method on its companion object that returns the props for creating it
-->
* アクターが送受信するメッセージや _手順_ は、コンパニオンオブジェクトに定義されています。
* 同様に、アクターを作成するためのプロパティを返す `props` メソッドをコンパニオンオブジェクトに定義しています。

<!--
### Creating and using actors
-->
### アクターの作成と使用

<!--
To create and/or use an actor, you need an `ActorSystem`.  This can be obtained by declaring a dependency on an ActorSystem, like so:
-->
アクターの作成や使用には、`ActorSystem` が必要です。この ActorSystem は次のように依存関係を宣言することで取得できます。

@[controller](code/ScalaAkka.scala)

<!--
The `actorOf` method is used to create a new actor.  Notice that we've declared this controller to be a singleton.  This is necessary since we are creating the actor and storing a reference to it, if the controller was not scoped as singleton, this would mean a new actor would be created every time the controller was created, which would ultimate throw an exception because you can't have two actors in the same system with the same name.
-->
`actorOf` メソッドは新規のアクターを作成するために使われます。このコントローラーはシングルトンとして宣言することに注意して下さい。これはアクターの作成や参照の保存のために必要で、仮に、このコントローラーがシングルトンの適用範囲外の場合、新しいアクターはコントローラーが作成される都度作成されることになり、同一システム内で同一名称のアクターを 2 つは持てないため、最終的に例外をスローすることになります。

<!--
### Asking things of actors
-->
### アクターについて

<!--
The most basic thing that you can do with an actor is send it a message.  When you send a message to an actor, there is no response, it's fire and forget.  This is also known as the _tell_ pattern.
-->
アクターを使ってできる最も基本的なことは、アクターにメッセージを送信することです。メッセージをアクターに送信しても応答は得られず、送信しっ放しとなります。これは _tell_ パターンとしても知られています。

<!--
In a web application however, the _tell_ pattern is often not useful, since HTTP is a protocol that has requests and responses.  In this case, it is much more likely that you will want to use the _ask_ pattern.  The ask pattern returns a `Future`, which you can then map to your own result type.
-->
しかし、web アプリケーションにおいては、HTTP がリクエストとレスポンスを持つプロトコルであるため、この _tell_ パターンはあまり有用ではありません。この場合、_ask_ パターンが使われる傾向にあるようです。ask パターンは、結果の型に関連付けられる `Future` を返します。

<!--
Below is an example of using our `HelloActor` with the ask pattern:
-->
以下は、ask パターンを用いた `HelloActor` の使用例です。

@[ask](code/ScalaAkka.scala)

<!--
A few things to notice:
-->
いくつかの注意事項があります。

<!--
* The ask pattern needs to be imported, and then this provides a `?` operator on the actor.
* The return type of the ask is a `Future[Any]`, usually the first thing you will want to do after asking actor is map that to the type you are expecting, using the `mapTo` method.
* An implicit timeout is needed in scope - the ask pattern must have a timeout.  If the actor takes longer than that to respond, the returned future will be completed with a timeout error.
-->
* ask パターンはインポートされる必要があり、そしてアクター上で `?` 演算子を提供します。
* ask の戻り値の型は `Future[Any]` で、通常、アクターに要求した後に最初にまずやりたいことは、期待する型への対応付けを `mapTo` メソッドを使用して行うことです。
* 暗黙のタイムアウトは適用範囲内において必要で、ask パターンはタイムアウトを持つべきです。アクターが応答にタイムアウトよりも長い時間を掛けた場合は、返された future はタイムアウトエラーで完了します。

<!--
## Dependency injecting actors
-->
## アクターの依存性注入

<!--
If you prefer, you can have Guice instantiate your actors and bind actor refs to them for your controllers and components to depend on.
-->
必要に応じて、Guice を用いてアクターのインスタンスを生成し、コントローラーとコンポーネントを依存させるためにアクターの参照をバインドすることができます。

<!--
For example, if you wanted to have an actor that depended on the Play configuration, you might do this:
-->
例えば、Play の設定に依存したアクターが必要な場合、このようにします。

@[injected](code/ScalaAkka.scala)

<!--
Play provides some helpers to help providing actor bindings.  These allow the actor itself to be dependency injected, and allows the actor ref for the actor to be injected into other components.  To bind an actor using these helpers, create a module as described in the [[dependency injection documentation|ScalaDependencyInjection#Play-applications]], then mix in the [`AkkaGuiceSupport`](api/scala/play/api/libs/concurrent/AkkaGuiceSupport.html) trait and use the `bindActor` method to bind the actor:
-->
Play は、アクターのバインドに役立つ、いくつかのヘルパーを提供します。それらはアクター自身が依存性注入されることを許可し、そのアクターに対するアクター参照が他のコンポーネントに注入されることも許可します。これらのヘルパーを用いたアクターをバインドするには、[[依存性注入ドキュメント|ScalaDependencyInjection#Play-applications]] の説明に従ってモジュールを作成し、[`AkkaGuiceSupport`](api/scala/play/api/libs/concurrent/AkkaGuiceSupport.html) トレイトをミックスインし、`bindActor` メソッドを使用してアクターをバインドします。

@[binding](code/ScalaAkka.scala)

<!--
This actor will both be named `configured-actor`, and will also be qualified with the `configured-actor` name for injection.  You can now depend on the actor in your controllers and other components:
-->
このアクターは `configured-actor` という名前になり、また `configured-actor` という名前で注入を限定します。これにより、コントローラーや他のコンポーネントでこのアクターに依存することができます。

@[inject](code/ScalaAkka.scala)

<!--
### Dependency injecting child actors
-->
### 子アクターの依存性注入

<!--
The above is good for injecting root actors, but many of the actors you create will be child actors that are not bound to the lifecycle of the Play app, and may have additional state passed to them.
-->
上記はルートアクターの注入については適していますが、作成するアクターの多くは、Play アプリのライフサイクルにバインドされない子アクターであり、追加の状態が渡される可能性があります。

<!--
In order to assist in dependency injecting child actors, Play utilises Guice's [AssistedInject](https://github.com/google/guice/wiki/AssistedInject) support.
-->
子アクターの依存性注入を援助するために、Play は、Guice の [AssistedInject](https://github.com/google/guice/wiki/AssistedInject) のサポートを利用しています。

<!--
Let's say you have the following actor, which depends configuration to be injected, plus a key:
-->
注入される設定に依存する、次のようなアクターと、キーがあるとします。

@[injectedchild](code/ScalaAkka.scala)

<!--
Note that the `key` parameter is declared to be `@Assisted`, this tells that it's going to be manually provided.
-->
`key` パラメータは `@Assisted` として宣言されており、これは手動で供給されることを表していることに注意してください。

<!--
We've also defined a `Factory` trait, this takes the `key`, and returns an `Actor`.  We won't implement this, Guice will do that for us, providing an implementation that not only passes our `key` parameter, but also locates the `Configuration` dependency and injects that.  Since the trait just returns an `Actor`, when testing this actor we can inject a factory that returns any actor, for example this allows us to inject a mocked child actor, instead of the actual one.
-->
また、`key` を持ち `Actor` を返す `Factory` トレイトを定義しました。このトレイトの実装は我々ではなく、Guice が行なってくれ、引数の `key` を渡すだけでなく、`Configuration` への依存性の注入も行ないます。トレイトは単に `Actor` を返すだけなので、このアクターのテストを行った時、任意のアクターを返すファクトリーを注入することができます。例えば、実際の子アクターの代わりにモック化された子アクターを注入することができます。

<!--
Now, the actor that depends on this can extend [`InjectedActorSupport`](api/scala/play/api/libs/concurrent/InjectedActorSupport.html), and it can depend on the factory we created:
-->
これに依存するアクターは [`InjectedActorSupport`](api/scala/play/api/libs/concurrent/InjectedActorSupport.html) を継承することができ、作成したファクトリーに依存することができます。

@[injectedparent](code/ScalaAkka.scala)

<!--
It uses the `injectedChild` to create and get a reference to the child actor, passing in the key.
-->
子アクターの参照を作成、取得するために `injectedChild` を使い、キーを渡します。

<!--
Finally, we need to bind our actors.  In our module, we use the `bindActorFactory` method to bind the parent actor, and also bind the child factory to the child implementation:
-->
最後に、アクターをバインドする必要があります。モジュールの中で `bindActorFactory` メソッドを使って親アクターをバインドし、子の実装のために子ファクトリーもバインドします。

@[factorybinding](code/ScalaAkka.scala)

<!--
This will get Guice to automatically bind an instance of `ConfiguredChildActor.Factory`, which will provide an instance of `Configuration` to `ConfiguredChildActor` when it's instantiated.
-->
これで Guice に `ConfiguredChildActor.Factory` のインスタンスを自動的にバインドさせます。`ConfiguredChildActor.Factory` は、インスタンス化の際に `ConfiguredChildActor` に `Configuration` のインスタンスを提供します。

<!--
## Configuration
-->
## 設定

<!--
The default actor system configuration is read from the Play application configuration file. For example, to configure the default dispatcher of the application actor system, add these lines to the `conf/application.conf` file:
-->
デフォルトのアクターシステムの設定は、Play アプリケーションの設定ファイルから読み込まれます。例えば、アプリケーションのアクターシステムのデフォルトディスパッチャを変更したい場合は、 `conf/application.conf` ファイルにその設定を数行追加します。

```
akka.actor.default-dispatcher.fork-join-executor.parallelism-max = 64
akka.actor.debug.receive = on
```

<!--
For Akka logging configuration, see [[configuring logging|SettingsLogger]].
-->
Akka のロギングの設定については、[[ロギング設定|SettingsLogger]] を参照して下さい。

<!--
### Changing configuration prefix
-->
### 設定接頭辞の変更

<!--
In case you want to use the `akka.*` settings for another Akka actor system, you can tell Play to load its Akka settings from another location.
-->
別の Akka アクターシステムに `akka.*` 設定を使用したい場合は、別の場所から Akka 設定をロードすることを Play に指示することができます。

```
play.akka.config = "my-akka"
```

<!--
Now settings will be read from the `my-akka` prefix instead of the `akka` prefix.
-->
これにより、`akka` 接頭辞の代わりに `my-akka` 接頭辞から設定が読み込まれます。

```
my-akka.actor.default-dispatcher.fork-join-executor.pool-size-max = 64
my-akka.actor.debug.receive = on
```

<!--
### Built-in actor system name
-->
組み込みアクターシステム名

<!--
By default the name of the Play actor system is `application`. You can change this via an entry in the `conf/application.conf`:
-->
Play のアクターシステム名はデフォルトでは `application` となっています。`conf/application.conf` の設定で変更することができます。

```
play.akka.actor-system = "custom-name"
```

<!--
> **Note:** This feature is useful if you want to put your play application ActorSystem in an Akka cluster.
-->
> **メモ:** この機能は Play アプリケーションのアクターシステムを Akka クラスターに配置したい場合に便利です。

<!--
## Scheduling asynchronous tasks
-->
## 非同期タスクのスケジューリング

<!--
You can schedule sending messages to actors and executing tasks (functions or `Runnable`). You will get a `Cancellable` back that you can call `cancel` on to cancel the execution of the scheduled operation.
-->
Akka では、アクターへのメッセージ送信やタスク (関数または `Runnable`) の実行を予約することができます。予約を行うと、結果として `Cancellable` のインスタンスが返ってきます。その `cancel` メソッドを呼び出すことで、予約した操作の実行をキャンセルすることができます。

<!--
For example, to send a message to the `testActor` every 300 microseconds:
-->
例えば、`testActor` というアクターに 300 マイクロ秒毎にメッセージを送信するにはこのようにします。

@[schedule-actor](code/ScalaAkka.scala)

<!--
> **Note:** This example uses implicit conversions defined in `scala.concurrent.duration` to convert numbers to `Duration` objects with various time units.
-->
> **メモ:** この例では `scala.concurrent.duration` に定義されている implicit conversion を利用して、数値を時間単位の異なる `Duration` オブジェクトへ変換しています。

<!--
Similarly, to run a block of code 10 milliseconds from now:
-->
同様に、コードブロックを今から 10 ミリ秒後に実行するには、次のように書きます。

@[schedule-callback](code/ScalaAkka.scala)

<!--
## Using your own Actor system
-->
## 独自のアクターシステムの使用

<!--
While we recommend you use the built in actor system, as it sets up everything such as the correct classloader, lifecycle hooks, etc, there is nothing stopping you from using your own actor system.  It is important however to ensure you do the following:
-->
クラスローダー、ライフサイクルのフックなどをすべて正しくセットアップするので、組み込みのアクターシステムの使用をおすすめします。独自のアクターシステムを使用することは全然構いません。ただし、次のことを確実に行うことが重要です。

<!--
* Register a [[stop hook|ScalaDependencyInjection#Stopping/cleaning-up]] to shut the actor system down when Play shuts down
* Pass in the correct classloader from the Play [Environment](api/scala/play/api/Application.html) otherwise Akka won't be able to find your applications classes
* Ensure that either you change the location that Play reads it's akka configuration from using `play.akka.config`, or that you don't read your akka configuration from the default `akka` config, as this will cause problems such as when the systems try to bind to the same remote ports
-->
* Play が停止した時にアクターシステムを停止させるための [[停止フック|ScalaDependencyInjection#Stopping/cleaning-up]] を登録する。
* Play [環境](api/scala/play/api/Application.html) から正しいクラスローダーを渡す。そうでないと、Akka はアプリケーションクラスを見つけることができません。
* Play が Akka の設定を読み込む場所を `play.akka.config` から変更するか、デフォルトの `akka` 設定から akka の設定を読み込まないようにする。これは、システムが同一のリモートポートにバインドしようとする時のような問題を引き起こすからです。
