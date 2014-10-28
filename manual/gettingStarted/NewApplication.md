<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Creating a new application
-->
# 新規アプリケーションを作成する

<!--
## Create a new application with the activator command
-->
## activatorコマンドで新しいアプリケーションを作成する

<!--
The `activator` command can be used to create a new Play application.  Activator allows you to select a template that your new application should be based off.  For vanilla Play projects, the names of these templates are `play-scala` for Scala based Play applications, and `play-java` for Java based Play applications.
-->
`activator` コマンドは新しい Play のアプリケーションを作成することに使えます。 Activator は新しいアプリケーションがベースにするテンプレートを選択することを可能にします。単純な Play プロジェクトとして、Scalaベースのアプリケーション向けには `play-scala` が、 Java ベースの Play アプリケーション向けには `play-java` が、それぞれのテンプレートテンプレートとして存在します。

<!--
> Note that choosing a template for either Scala or Java at this point does not imply that you can’t change language later. For example, you can create a new application using the default Java application template and start adding Scala code whenever you like.
-->
> 注意点として、ScalaとJavaどちらのテンプレートを選択したらその後、実装する言語を変更出来ないことを意味しません。例えば、新しいアプリケーションを標準のJavaアプリケーションテンプレートを使って作り、Scalaのコードを追加することはいつでも可能です。

<!--
To create a new vanilla Play Scala application, run:
-->
単純な Play の Scala アプリケーションを作るには、以下のコマンドを実行します。

```bash
$ activator new my-first-app play-scala
```

<!--
To create a new vanilla Play Java application, run:
-->
単純な Play の Java アプリケーションを作るには、以下のコマンドを実行します。

```bash
$ activator new my-first-app play-java
```
<!--
In either case, you can replace `my-first-app` with whatever name you want your application to use.  Activator will use this as the directory name to create the application in.  You can change this name later if you choose.
-->
どちらの場合も、 `my-first-app` のところはお好みでアプリケーション名として使いたい名前に置き換えることが出来ます。Activatorはこれをアプリケーション内で作成するディレクトリ名として使用します。この名前は後で変更することが出来ます。

[[images/activatorNew.png]]

<!--
Once the application has been created you can use the `activator` command again to enter the [[Play console|PlayConsole]].
-->
一度アプリケーションを作成したら、 [[Play console|PlayConsole]] に入るために `activator` コマンドを繰り返し使うことが出来る。

```bash
$ cd my-first-app
$ activator
```

<!--
> If you wish to use other Activator templates, you can do this by running `activator new`.  This will prompt you for an application name, and then give you a chance to browse and select an appropriate template.
-->
> もし他の Activator テンプレートを使用したい場合、 `activator new` が使えまえす。これはアプリケーション名をさせ、そして適切なテンプレートを閲覧、選択するチャンスを提供してくれます。

<!--
## Create a new application with the Activator UI
-->
## Activator UI で新しいアプリケーションを作成する

<!--
New Play applications can also be created with the Activator UI.  To use the Activator UI, run:
-->
Play の新しいアプリケーションは Activator UI でも作ることができる。 Activator UI を使うには以下のように入力します。

```bash
$ activator ui
```

<!--
You can read the documentation for using the Activator UI [here](https://typesafe.com/activator/docs).
-->
Activator UIの使い方については[こちら](https://typesafe.com/activator/docs)のドキュメントをお読みください。

<!--
## Create a new application without Activator
-->
## Activator を使わずに新しいアプリケーションを作成する

<!--
It is also possible to create a new Play application without installing Activator, using sbt directly.
-->
sbt を直接使うことで、 Activator をインストールせずに新しい Play アプリケーションを作成することが可能です。

<!--
> First install [sbt](http://www.scala-sbt.org/) if needed.
-->
> 必要に応じて [sbt](http://www.scala-sbt.org/) をインストールしてください。

<!--
Create a new directory for your new application and configure your sbt build script with two additions.
--?
新しいアプリケーションのために新しいディレクトリを作成し、そして2つのスクリプトを sbt のビルドスクリプトに追加します。

<!--
In `project/plugins.sbt`, add:
-->
具体的には、`project/plugins.sbt` に次の内容を記述します。

```scala
// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "%PLAY_VERSION%")
```

<!--
Be sure to replace `%PLAY_VERSION%` here by the exact version you want to use. If you want to use a snapshot version, you will have to specify this additional resolver: 
-->
`%PLAY_VERSION%` のところは実行したいバージョンに書き換えてください。もし、 snapshot のバージョンを使いたい場合、以下の追加リゾルバを記述する必要があります。

```
// Typesafe snapshots
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
```

<!--
In `build.sbt` for Java projects:
-->
`build.sbt` の Java プロジェクトの記述:

```scala
name := "my-first-app"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
```

<!--
...or Scala projects:
-->
そして Scala プロジェクトの記述:

```scala
name := "my-first-app"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
```

<!--
You can then launch the sbt console in this directory:
-->
以上の手順を終えると、このディレクトリで sbt コンソールを起動できるようになります。

```bash
$ cd my-first-app
$ sbt
```

<!--
sbt will load your project and fetch the dependencies.
-->
sbt コンソールが起動すると、プロジェクトがロードされて、全ての依存モジュールがダウンロードされます。

> **Next:** [[Anatomy of a Play application|Anatomy]]
