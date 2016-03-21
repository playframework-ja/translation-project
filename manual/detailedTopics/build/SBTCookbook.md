<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# SBT Cookbook
-->
# SBT クックブック

<!--
## Hook actions around `play run`
-->
## `play run` 周辺のアクションフック

<!--
You can apply actions around the `play run` command by extending `PlayRunHook`.
This trait define the following methods:
-->
`PlayRunHook` を継承することで `play run` コマンドにアクションを関連付けることができます。
このトレイトは以下のメソッドを定義します:

<!--
 * `beforeStarted(): Unit`
 * `afterStarted(addr: InetSocketAddress): Unit`
 * `afterStopped(): Unit`
 -->
 * `beforeStarted(): Unit`
 * `afterStarted(addr: InetSocketAddress): Unit`
 * `afterStopped(): Unit`

<!--
`beforeStarted` method is called before the play application is started, but after all "before run" tasks have been completed.
-->
`beforeStarted` メソッドは play アプリケーションが開始する前、最終的には "before run" タスクが完了した後に呼び出されます。

<!--
`afterStarted` method is called after the play application has been started.
-->
`afterStarted` は play アプリケーションが開始した後に呼び出されます。

<!--
`afterStopped` method is called after the play process has been stopped.
-->
`afterStopped` は play アプリケーションが終了した後に呼び出されます。

<!--
> **Note:** The following example illustrate how you can start and stop a command with play run hook.
> In the near future [sbt-web](https://github.com/sbt/sbt-web) will provide a better way to integrate Grunt with an SBT build.
-->
> **注意:** 以下の例では play の実行フックを使ってコマンドを開始そして終了する方法を解説します。
> 近い将来、[sbt-web](https://github.com/sbt/sbt-web) は SBT ビルドを Grunt と共に実行するより良い方法を提供します。

<!--
Now let's say you want to build a Web application with `grunt` before the application is started.
First, you need to create a Scala object in the `project/` directory to extend `PlayRunHook`.
Let's name it `Grunt.scala`:
-->
さて、アプリケーションを開始する前に `grunt` で Web アプリケーションをビルドしたいとしましょう。
まず、`project/` ディレクトリ内に `PlayRunHook` を継承する Scala プロジェクトを作る必要があります。
これを `Grunt.scala` という名前にしましょう:

```scala
import play.PlayRunHook
import sbt._

object Grunt {
  def apply(base: File): PlayRunHook = {

    object GruntProcess extends PlayRunHook {

      override def beforeStarted(): Unit = {
        Process("grunt dist", base).run
      }
    }

    GruntProcess
  }
}
```

<!--
Then in the `build.sbt` file you need to register this hook:
-->
それから `build.sbt` ファイル内にこのフックを登録する必要があります:

```scala
import Grunt._
import play.PlayImport.PlayKeys.playRunHooks

playRunHooks <+= baseDirectory.map(base => Grunt(base))
```

<!--
This will execute the `grunt dist` command in `baseDirectory` before the application is started.
-->
これにより、アプリケーションの開始前に `baseDirectory` で `grunt dist` コマンドを実行します。

<!--
Now we want to execute `grunt watch` command to observe changes and rebuild the Web application when that happen:
-->
ここで、`grunt watch` コマンドを実行してファイルを監視し、変更が発生したら Web アプリケーションをリビルドしたいと思います:

```scala
import play.PlayRunHook
import sbt._

import java.net.InetSocketAddress

object Grunt {
  def apply(base: File): PlayRunHook = {

    object GruntProcess extends PlayRunHook {

      var process: Option[Process] = None

      override def beforeStarted(): Unit = {
        Process("grunt dist", base).run
      }

      override def afterStarted(addr: InetSocketAddress): Unit = {
        process = Some(Process("grunt watch", base).run)
      }

      override def afterStopped(): Unit = {
        process.map(p => p.destroy())
        process = None
      }
    }

    GruntProcess
  }
}
```

<!--
Once the application has been started we execute `grunt watch` and when the application has been stopped we destroy the grunt process. There's nothing to change in `build.sbt`
-->
アプリケーションが起動するとすぐに `grunt watch` を実行して、アプリケーションが停止すると grunt プロセスを終了します。`build.sbt` を変更する必要はありません。

<!--
## Add compiler options
-->
## コンパイラオプションの追加

<!--
For example, you may want to add the feature flag to have details on feature warnings:
-->
例えば、feature 警告の詳細を得るために feature フラグを追加したいとします:

```
[info] Compiling 1 Scala source to ~/target/scala-2.10/classes...
[warn] there were 1 feature warnings; re-run with -feature for details
```

<!--
Simply add `-feature` to the `scalacOptions` attribute:
-->
シンプルに `scalacOptions` に `-feature` を追加します:

```scala
scalacOptions += "-feature"
```

<!--
## Add additional asset directory
-->
## アセットディレクトリの追加

<!--
For example you can add the `pictures` folder to be included as an additional asset directory:
-->
例えば、`pictures` フォルダを追加のアセットディレクトリに含めることができます:

```scala
unmanagedResourceDirectories in Assets <+= baseDirectory { _ / "pictures" }
```

<!--
This will allow you to use `routes.Assets.at` with this folder.
-->
これにより、このフォルダを `routes.Assets.at` で使うことができます。

<!--
## Disable documentation
-->
## ドキュメントの無効化

<!--
To speed up compilation you can disable documentation generation:
-->
コンパイル速度を向上するために、ドキュメントの生成を無効にすることができます:

```scala
sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false
```

<!--
The first line will disable documentation generation and the second one will avoid to publish the documentation artifact.
-->
一行目はドキュメントの生成を無効化し、二行目はドキュメント成果物の発行を無効にします。

<!--
## Configure ivy logging level
-->
## ivi ログレベル設定

<!--
By default `ivyLoggingLevel` is set on `UpdateLogging.DownloadOnly`. You can change this value with:
-->
`ivyLoggingLevel` はデフォルトで `UpdateLogging.DownloadOnly` に設定されています。この値は以下のように変更することができます:

<!--
 * `UpdateLogging.Quiet` only displays errors
 * `UpdateLogging.FULL` logs the most
-->
* `UpdateLogging.Quiet` はエラーのみ表示します
* `UpdateLogging.FULL` は最大限のログを表示します

<!--
For example if you want to only display errors:
-->
例えばエラーのみ表示したい場合は、以下のように設定します:

```scala
ivyLoggingLevel := UpdateLogging.Quiet
```

<!--
## Fork and parallel execution in test
-->
## テストのフォークおよび並列実行

<!--
By default parallel execution is disabled and fork is enabled. You can change this behavior by setting `parallelExecution in Test` and/or `fork in Test`:
-->
デフォルトでは、並列実行は無効化され、フォークが有効になっています。この振る舞いは `parallelExecution in Test` および/または `fork in Test` と設定して変更することができます:

```scala
parallelExecution in Test := true

fork in Test := false
```
