<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# The Build System
-->
# ビルドシステム

<!--
The Play build system uses [sbt](http://www.scala-sbt.org/), a high-performance integrated build for Scala and Java projects.  Using `sbt` as our build tool brings certain requirements to play which are explained on this page.
-->
Play のビルドシステムは、Scala および Java プロジェクトに統合された高性能なビルドツールである [sbt](http://www.scala-sbt.org/) を使います。ビルドツールに `sbt` を使うことは、このページで説明するいくつかの要件を play にもたらします。

<!--
## Understanding sbt
--> 
## sbt を理解する

<!--
sbt functions quite differently to the way many traditional build tasks.  Fundamentally, sbt is a task engine.  Your build is represented as a tree of task dependencies that need to be executed, for example, the `compile` task depends on the `sources` task, which depends on the `sourceDirectories` task and the `sourceGenerators` task, and so on.
-->
sbt の機能は、多くの古典的なビルドタスクのやり方と完全に異なっています。基本的に、sbt はタスクエンジンです。例えば `compile` タスクは、`sourceDirectories` と `sourceGenerators` に依存する `sources` タスクに依存すると言ったように、ビルドは実行する必要のあるタスクの依存ツリーとして表現されます。

<!--
sbt breaks typical build executions up into very fine grained tasks, and any task at any point in the tree can be arbitrarily redefined in your build.  This makes sbt very powerful, but also requires a shift in thinking if you've come from other build tools that break your build up into very coarsely grained tasks.
-->
sbt は典型的なビルドタスクをきめ細やかなタスクに分解し、ビルドツリーのあらゆる場所にあるすべてのタスクは、ビルド内にて任意に再定義することができます。これにより sbt はとてもパワフルになりますが、荒削りなビルドツール由来の考え方を切り替える必要もあります。

<!--
The documentation here describes Play's usage of sbt at a very high level.  As you start to use sbt more in your project, it is recommended that you follow the [sbt tutorial](http://www.scala-sbt.org/0.13/tutorial/index.html) to get an understanding for how sbt fits together.  Another resource that many people have found useful is [this series of blog posts](https://jazzy.id.au/2015/03/03/sbt-task-engine.html).
-->
このドキュメントでは、Play における sbt を上位のレベルで記述します。プロジェクトにおいて sbt をより使い込んでいく際は、[sbt チュートリアル](http://www.scala-sbt.org/0.13/docs/ja/Getting-Started.html) を参照して sbt の組み合わせ方を理解することをお勧めします。このほかに、[この一連のブログ](https://jazzy.id.au/2015/03/03/sbt-task-engine.html) も多くの人に利用されています。

<!--
## Play application directory structure
-->
## Play アプリケーションディレクトリ構成

<!--
Most people get started with Play using the `activator new` command which produces a directory structure like this:
-->
多くのユーザが Play を始める際に使用する `activator new` コマンドは、以下のようなディレクトリ構造を生成します:

<!--
- `/`: The root folder of your application
- `/README`: A text file describing your application that will get deployed with it.
- `/app`: Where your application code will be stored.
- `/build.sbt`: The [sbt](http://www.scala-sbt.org/) settings that describe building your application.
- `/conf`: Configuration files for your application
- `/project`: Further build description information
- `/public`: Where static, public assets for your application are stored.
- `/test`: Where your application's test code will be stored.
-->
- `/`: アプリケーションのルートフォルダ
- `/README`: アプリケーションと共にデプロイされる、アプリケーションの説明ファイル
- `/app`: コードの格納場所
- `/build.sbt`: アプリケーションのビルドを記述する [sbt](http://www.scala-sbt.org/) 設定
- `/conf`: アプリケーションの設定ファイル
- `/project`: 追加のビルド記述情報
- `/public`: アプリケーションの静的かつ公開されたアセットの格納場所
- `/test`: アプリケーションテストコードの格納場所

<!--
For now, we are going to concern ourselves with the `/build.sbt` file and the `/project` directory.
-->
今後は この `/build.sbt` ファイルと `/project` ディレクトリが重要になります。

<!--
## The `/build.sbt` file.
-->
## `/build.sbt` ファイル

<!--
When you use the `activator new foo` command, the build description file, `/build.sbt`, will be generated like this:
-->
`activator new foo` コマンドを使うと、以下のようなビルド定義ファイル `/build.sbt` が生成されます:

@[default](code/build.sbt)

<!--
The `name` line defines the name of your application and it will be the same as the name of your application's root directory, `/`, which is derived from the argument that you gave to the `activator new` command.
-->
`name` 行は、 `/`、すなわち `activator new` コマンドに与えた引数より引き渡されるアプリケーションルートと同じ名前となる、アプリケーションの名前を定義します。

<!--
The `version` line provides  the version of your application which is used as part of the name for the artifacts your build will produce.
-->
`version` 行は、ビルドするアーティファクト名の一部に使われる、アプリケーションのバージョンを提供します。

<!--
The `libraryDependencies` line specifies the libraries that your application depends on. More on this below.
-->
`libraryDependencies` 行はアプリケーションが依存するライブラリを指定します。詳しくは後述します。

<!--
You should use the `PlayJava` or `PlayScala` plugin to configure sbt for Java or Scala respectively.
-->
Java または Scala 向けに、`PlayJava` または `PlayScala` プラグインを sbt に個別に設定しなければなりません。

<!--
### Using scala for building
-->
### ビルドに scala を使う

<!--
Activator is also able to construct the build requirements from scala files inside your project's `project` folder. The recommended practice is to use `build.sbt` but there are times when using scala directly is required. If you find yourself, perhaps because you're migrating an older project, then here are a few useful imports:
-->
Activator は、プロジェクトの `project` フォルダ内にある scala ファイルからビルド要件を組み立てることもできます。おすすめは build.sbt を使うことですが、ことによると古いプロジェクトから移行する場合などに、scala ディレクトリが必要になるときがあります。その場合は、いくつかの便利なインポートがあります:

```scala
import sbt._
import Keys._
import play.Play.autoImport._
import PlayKeys._
```

<!--
The line indicating `autoImport` is the correct means of importing an sbt plugin's automatically declared properties. Along the same lines, if you're importing an sbt-web plugin then you might well:
-->
`autoImport` と書かれた行は、sbt プラグインに宣言されたプロパティを自動的にインポートする手段を是正したものです。これらと併せて、例えば sbt-web プラグインをインポートすることもできます:

```scala
import com.typesafe.sbt.less.autoImport._
import LessKeys._
```

<!--
## The `/project` directory
-->
## `/project` ディレクトリ

<!--
Everything related to building your project is kept in the `/project` directory underneath your application directory.  This is an [sbt](http://www.scala-sbt.org/) requirement. Inside that directory, there are two files:
-->
ビルドに関するものはすべてアプリケーションディレクトリ配下の `/project` ディレクトリに保持されます。これは [sbt](http://www.scala-sbt.org/) の要件です。このディレクトリには次の二つのファイルがあります:

<!--
- `/project/build.properties`: This is a marker file that declares the sbt version used.
- `/project/plugins.sbt`: SBT plugins used by the project build including Play itself.
-->
- `/project/build.properties`: 使用する sbt のバージョンを宣言するマーカーファイルです。
- `/project/plugins.sbt`: Play 自身を含む、プロジェクトが使用する SBT プラグイン群です。

<!--
## Play plugin for sbt (`/project/plugins.sbt`)
-->
## sbt 用の Play プラグイン (`/project/plugins.sbt`)

<!--
The Play console and all of its development features like live reloading are implemented via an sbt plugin.  It is registered in the `/project/plugins.sbt` file:
-->
Play コンソールや、ライブリロードのような開発系の機能は sbt プラグインとして実装されています。これらは `/project/plugins.sbt` ファイルに登録されています:

```scala
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % playVersion) // where version is the current Play version, i.e. "%PLAY_VERSION%"
```
<!--
> Note that `build.properties` and `plugins.sbt` must be manually updated when you are changing the play version.
-->
> play のバージョンを変更する際は、手動で `build.properties` と `plugins.sbt` を更新しなければならないことに注意してください。