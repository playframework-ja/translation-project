<!-- translated -->
<!--
# Creating a new application
-->
# 新規アプリケーションを作成する

<!--
## Create a new application with the play command
-->
## play コマンドでアプリケーションを新規作成する

<!--
The easiest way to create a new application is to use the `play new` command.
-->
新規アプリケーションを作成する最も基本的な方法は、`play new` コマンドを利用することです。

```bash
$ play new myFirstApp
```
<!--
This will ask for some information.
-->
このコマンドを実行すると、次のような必要事項の入力が求められます。

<!--
- The application name (just for display, this name will be used later in several messages).
- The template to use for this application. You can choose either a default Scala application, a default Java application, or an empty application.
-->
- アプリケーション名 (アプリケーションのログなどで利用されます)
- 利用するアプリケーションのひな形。Scala ベース、Java ベース、空アプリケーションの３種類のいずれかを選択できます。

[[images/playNew.png]]

<!--
> Note that choosing a template at this point does not imply that you can’t change language later. For example, you can create a new application using the default Java application template and start adding Scala code whenever you like.
-->
> ここでひな形を選択したからといって、後で言語を変更できなくなるわけではありません。例えば、最初に Java のテンプレートでアプリケーションを作成し、好きなときに Scala コードを追加することができます。

<!--
Once the application has been created you can use the `play` command again to enter the [[Play 2.0 console | PlayConsole]].
-->
アプリケーションが生成されたら、もう一度 `play` コマンドを実行して、[[Play 2.0 コンソール | PlayConsole]] を起動しましょう。

```bash
$ cd myFirstApp
$ play
```

<!--
## Create a new application without having Play installed
-->
## Play をインストールせずに新規アプリケーションを作成する

<!--
You can also create a new Play application without installing Play, by using sbt. 
-->
sbt を使って、Play をインストールせずに新規 Play アプリケーションを作成することもできます。

<!--
> First install [[sbt 0.11.2 | https://github.com/harrah/xsbt/wiki/Getting-Started-Setup]] if needed.
-->
> 必要に応じて [[sbt 0.11.2 | https://github.com/harrah/xsbt/wiki/Getting-Started-Setup]]　をインストールしておきましょう。

<!--
Just create a new directory for your new application and configure your sbt build script with two additions.
-->
まず、アプリケーションを保存するためのディレクトリをつくります。それから、sbt ビルドスクリプトを作成して、Play 向けの設定を 2 つ追加します。

<!--
In `project/plugins.sbt`, add:
-->
具体的には、`project/plugins.sbt` に次の内容を記述します。

```scala
// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.0")
```

<!--
Be sure to replace `2.0` here by the exact version you want to use. If you want to use a snapshot version, you will have to specify this additional resolver: 
-->
使用したい正確なバージョンをこの`2.0`と置き換えてください。スナップショットバージョンを使用したい場合は、以下の追加のリゾルバを指定する必要があります。

```
// Typesafe snapshots
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
```

<!--
In `project/Build.scala`:
-->
また、`project/Build.scala` に次の内容を記述します。

```scala
import sbt._
import Keys._
import PlayProject._
 
object ApplicationBuild extends Build {
 
  val appName         = "My first application"
  val appVersion      = "1.0"
 
  val appDependencies = Nil
 
  val main = PlayProject(
    appName, appVersion, appDependencies, mainLang = SCALA
  ) 
 
}
```

<!--
You can then launch the sbt console in this directory:
-->
以上の手順を終えると、このディレクトリで sbt コンソールを起動できるようになります。

```bash
$ cd myFirstApp
$ sbt
```

<!--
sbt will load your project and fetch the dependencies.
-->
sbt コンソールが起動すると、プロジェクトがロードされて、全ての依存モジュールがダウンロードされます。

<!--
> **Next:** [[Anatomy of a Play 2.0 application | Anatomy]]
-->
> **Next:** [[Play 2.0 アプリケーションの構造 | Anatomy]]