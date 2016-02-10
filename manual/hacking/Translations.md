<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Translating the Play Documentation
-->
# Play ドキュメント翻訳

<!--
Play 2.3+ provides infrastructure to aid documentation translators in translating the Play documentation and keeping it up to date.
-->
Play 2.3 以降、ドキュメント翻訳者たちが Play ドキュメントを翻訳し、そしてそれを更新し続けるための助けとなるインフラが提供されます。

<!--
As described in the [[Documentation Guidelines|Documentation]], Play's documentation is written in markdown format with code samples extracted to external files.  Play allows the markdown components of the documentation to be translated, while allowing the original code samples from the English documentation to be included in the translated documentation.  This assists translators in maintaining translation quality - the code samples are kept up to date as part of the core Play project, while the translated descriptions have to be maintained manually.
-->
[[ドキュメントガイドライン|Documentation]] で述べられているとおり、Play のドキュメントは外部ファイルに抽出されたコードサンプルと共に、markdown フォーマットで記述されています。Play ドキュメントの markdown 部分は翻訳できるようになっており、また英語版ドキュメントのコードサンプルを翻訳したドキュメントに含めることができるようになっています。この仕組みは、翻訳者たちが翻訳の品質を保つ手助けとなります - 翻訳された表現は手作業でメンテナンスしなければならない一方、コードサンプルは Play プロジェクトの主要な部分として更新され続けます。

<!--
In addition to this, Play also provides facilities for validating the integrity of translated documentation.  This includes validating all internal links, including links to code snippets, in the translation.
-->
この仕組みに加えて、Play は翻訳されたドキュメントの完全性を検証する機能も提供しています。この機能には、コードスニペットへのリンクも含め、翻訳ドキュメント内のすべての内部リンクの妥当性検証が含まれています。

<!--
## Prerequisites
-->
## 前提条件

<!--
You need to have `activator` or `sbt` installed.  It will also be very useful to have a clone of the Play repository, with the branch that you're translating checked out, so that you have something to copy to start with.
-->
`activator` または `sbt` がインストールされている必要があります。翻訳するブランチがチェックアウトされた Play リポジトリの clone があるととても便利なので、何らかのコピーと共に始めましょう。

<!--
If you're translating an unreleased version of the Play documentation, then you'll need to build that version of Play and publish it locally on your machine first.  This can be done by running:
-->
まだリリースされていないバージョンの Play ドキュメントを翻訳する場合、まずそのバージョンの Play をビルドして、ローカルマシンに publish する必要があります。これは、以下のように実行します:

```bash
./build publishLocal
```

<!--
in the `framework` directory of the Play project.
-->
上記コマンドは Play プロジェクトの `framework` ディレクトリで実行してください。

<!--
## Setting up a translation
-->
## 翻訳環境のセットアップ

<!--
Create a new SBT project with the following structure:
-->
以下の構成に従って新規 SBT プロジェクトを作成します:

```
translation-project
  |- manual
  | |- javaGuide
  | |- scalaGuide
  | |- gettingStarted
  | `- etc...
  |- project
  | |- build.properties
  | `- plugins.sbt
  `- build.sbt
```

<!--
`build.properties` should contain the SBT version, ie:
-->
`build.properties` は、例えば以下のような SBT バージョンを含んでいなければなりません:

```
sbt.version=0.13.8
```

<!--
`plugins.sbt` should include the Play docs sbt plugin, ie:
-->
`plugins.sbt` は、例えば以下のような Play ドキュメント用 sbt プラグインを含んでいなければなりません:

```scala
addSbtPlugin("com.typesafe.play" % "play-docs-sbt-plugin" % "%PLAY_VERSION%")
```

<!--
Finally, `build.sbt` should enable the Play docs plugin, ie:
-->
最後に、`build.sbt` で以下のようにして Play ドキュメントプラグインを有効にします:

```scala
lazy val root = (project in file(".")).enablePlugins(PlayDocsPlugin)
```

<!--
Now you're ready to start translating!
-->
これで翻訳を始める準備が整いました!

<!--
## Translating documentation
-->
## ドキュメントの翻訳

<!--
First off, start the documentation server.  The documentation server will serve your documentation up for you so you can see what it looks like as you're going.  To do this you'll need `sbt` or `activator` installed, either one is fine, in the examples here we'll be using `sbt`:
-->
まず最初に、ドキュメントサーバから始めます。このドキュメントサーバは、ドキュメントがどのように見えるか確認することができるよう、サーバにドキュメントをアップロードして提供します。これを行うためには `sbt` または `activator` がインストールされている必要があります。どちらでも結構ですが、この例では `sbt` を使います:

```bash
$ sbt run
[info] Set current project to root (in build file:/Users/jroper/tmp/foo-translation/)
[info] play - Application started (Dev)
[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

Documentation server started, you can now view the docs by going to http://0:0:0:0:0:0:0:0:9000
```

<!--
Now open <http://localhost:9000> in your browser.  You should be able to see the default Play documentation.  It's time to translate your first page.
-->
ここでブラウザから <http://localhost:9000> を開きます。デフォルトの Play ドキュメントが表示されているはずです。最初のページを翻訳する時がやってきました。

<!--
Copy a markdown page from the Play repository into your project.  It is important to ensure that the directory structure in your project matches the directory in Play, this will ensure that the code samples work.
-->
Play のリポジトリから自分のプロジェクトに markdown ページをコピーしてください。ここで重要なのは、プロジェクトのディレクトリ構成が Play のディレクトリと合っていることです。このことにより、コードサンプルの動作が保証されます。

<!--
For example, if you choose to start with `manual/scalaGuide/main/http/ScalaActions.md`, then you need to ensure that it is in `manual/scalaGuide/main/http/ScalaActions.md` in your project.
-->
例えば、`manual/scalaGuide/main/http/ScalaActions.md` から始めることにした場合、このファイルが自分のプロジェクトの `manual/scalaGuide/main/http/ScalaActions.md` にあることを確認する必要があります。

<!--
> **Note:** It may be tempting to start by copying the entire Play manual into your project.  If you do do this, make sure you only copy the markdown files, that you don't copy the code samples as well.  If you copy the code samples, they will override the code samples from Play, and you will lose the benefit of having those code samples automatically maintained for you.
-->
> **注意:** Play マニュアルを自分のプロジェクトに丸ごとコピーするところから始めるのが魅力的に見えるかもしれません。もしそうする場合、markdown ファイルだけをコピーして、コードサンプルまでコピーしないように気を付けてください。コードサンプルをコピーすると、Play のコードサンプルを上書きすることになり、コードサンプルが自動的にメンテナンスされる利点を失うことになります。

<!--
Now you can start translating the file.
-->
これでドキュメントの翻訳を始めることができます。

<!--
## Dealing with code samples
-->
## コードサンプルの取扱い

<!--
The Play documentation is full of code samples.  As described in the [[Documentation Guidelines|Documentation]], these code samples live outside of the markdown documentation and live in compiled and tested source files.  Snippets from these files get included in the documentation using the following syntax:
-->
Play ドキュメントには数多くのコードサンプルがあります。 [[ドキュメントガイドライン|Documentation]] で述べられているとおり、これらのコードサンプルは markdown ドキュメントから切り離されており、コンパイル、そしてテストされたソースファイル内に存在しています。これらファイル内のスニペットは、以下の文法を使ってドキュメント内に取り込まれています:

```
@[label](code/path/to/SourceFile.java)
```

<!--
Generally, you will want to leave these snippets as is in your translation, this will ensure that the code snippets your translation stays up to date with Play.
-->
通常、これらのコードスニペットが Play と共に更新されることが保証されることになるので、翻訳内のスニペットはそのままにしておきたいことでしょう。

<!--
In some situations, it may make sense to override them.  You can either do this by putting the code directly in the documentation, using a fenced block, or by extracting them into your projects own compile code samples.  If you do that, checkout the Play documentation sbt build files for how you might setup SBT to compile them.
-->
時として、コードスニペットを上書きすることが理にかなっている場合もあります。ブロックを使ってドキュメント内に直接コードを埋め込むこともできますし、独自のコンパイルされたコードサンプルとして抽出することもできます。これを行う場合、Play ドキュメントの sbt ビルドファイルをチェックアウトして、コードサンプルをコンパイルするための SBT セットアップ方法を確認してください。

<!--
## Validating the documentation
-->
## ドキュメントのバリデーション

<!--
The Play docs sbt plugin provides a documentation validation task that runs some simple tests over the documentation, to ensure the integrity of links and code sample references.  You can run this by running:
-->
Play docs sbt プラグインは、リンクとコードサンプル参照の完全性を保証するため、ドキュメント全体に対してシンプルなテストを実行するドキュメント妥当性検証タスクを提供しています。これは、以下のようにして実行することができます:

```bash
sbt validateDocs
```

<!--
You can also validate the links to external sites in Play's documentation.  This is a separate task because it's dependent on many sites on the internet that Play's documentation links to, and the validation task in fact actually triggers DDoS filters on some sites.  To run it, run:
-->
Play ドキュメント内にある外部サイトへのリンクを検証することもできます。このタスクは Play ドキュメントがリンクしているインターネット上の多くのサイトに依存し、また実際のところ、この検証タスクはいくつかのサイトの DDoS フィルタを引き起こしてしまうので、別のタスクに切り出されています。このタスクは、以下のように実行します:

```bash
sbt validateExternalLinks
```

## Translation report

Another very helpful tool provided by Play is a translation report, which shows which files have not been translated, and also tries to detect issues, for example, if the translation introduces new files, or if the translation is missing code samples.  This can particularly help when translating a new version of the documentation, since the addition or removal of code samples will often be a good signal that something has changed.

To view the translation report, run the documentation server (like normal), and then visit <http://localhost:9000/@report> in your browser.  By default it will serve a cached version of the report if it has been generated in the past, you can rerun the report by clicking the rerun report link.

<!--
## Deploying documentation to playframework.com
-->
## playframework.com にドキュメントをデプロイする

<!--
[playframework.com](https://playframework.com) serves documentation out of git repositories.  If you want your translation to be served from playframework.com, you'll need to put your documentation into a GitHub repository, and contact the Play team to have them add it to playframework.com.
-->
[playframework.com](https://playframework.com) は git リポジトリとは別の場所でドキュメントを提供しています。翻訳を playframework.com から提供したい場合、ドキュメントを GitHub リポジトリに配置して、これを playframework.com に追加するよう Play チームに連絡する必要があります。

<!--
The git repository needs to be in a very particular format.  The current master branch is for the documentation of the latest development version of Play.  Documentation for stable versions of Play must be in branches such as 2.3.x.  Documentation specific to a particular release of Play will be served from a tag of the repository with that name, for example, 2.3.1.
-->
git リポジトリはかなり特別なフォーマットである必要があります。現在の master ブランチは、最新版開発バージョン Play のドキュメントのためのものです。安定板 Play のドキュメントは 2.3.x のようなブランチでなければなりません。特定リリースの Play に特化したドキュメントは、例えば 2.3.1 のような名前のリポジトリタグから提供されます。

<!--
Once the Play team has configured playframework.com to serve your translation, any changes pushed to your GitHub repository will be picked up within about 10 minutes, as playframework.com does a `git fetch` on all repos it uses once every 10 minutes.
-->
playframework.com はすべてのリポジトリに対して 10 分ごとに `git fetch` を実行するので、Play チームがあなたの翻訳を提供するよう playframework.com を設定したあとは、GitHub リポジトリに push したあらゆる変更は、おおよそ 10 分以内に拾い上げられます。

## Specifying the documentation version

By default, the `play-docs-sbt-plugin` uses the same version of the Play documentation code samples and fallback markdown files as itself, so if in `plugins.sbt` you're using `2.4.0`, when you run the documentation, you will get `2.4.0` of the documentation code samples.  You can control this version by setting `PlayDocsKeys.docsVersion` in `build.sbt`:

```scala
PlayDocsKeys.docsVersion := "2.3.1"
```

This is particularly useful if you are wanting to provide documentation for versions of Play prior to when the `play-docs-sbt-plugin` was introduced, as far back as `2.2.0`.  For `2.1.x` and earlier, the documentation was not packaged and published as a jar file, so the tooling will not work for those older versions.
