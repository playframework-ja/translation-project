<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
# Building Play from source

To benefit from the latest improvements and bug fixes after the initial beta release, you may want to compile Play from source. You’ll need a [Git client](http://git-scm.com/) to fetch the source.

<!--
## Grab the source
-->
## ソースを取得する
From the shell, first checkout the Play source:

```bash
$ git clone git://github.com/playframework/playframework.git
```

<!--
Then go to the `playframework/framework` directory and launch the `build` script to enter the sbt build console:
-->
ソースコードを取得したら、 `playframework/framework` ディレクトリに移動して、 `build` スクリプトを起動し、 sbt によるビルド・コンソールに入ります。

```bash
$ cd playframework/framework
$ ./build
> publish-local
```

<!--
> Note that you don’t need to install sbt yourself: Play embeds its own version.
-->
> sbt を別途インストールする必要はありません。 Play には必要なバージョンの sbt が既に組み込まれているからです。

<!--
If you want to make changes to the code you can use `publish-local` to rebuild the framework.
-->
もし、 Play に独自の変更を加えた場合は、 `publish-local` を使ってフレームワークを再ビルドするとよいでしょう。

<!--
## Build the documentation
-->
## ドキュメントをビルドする

Documentation is available at playframework/documentation as Markdown files.  To see HTML, run the following:

```bash
$ cd playframework/documentation
$ ./build run
```

To see documentation at [http://localhost:9000/@documentation](http://localhost:9000/@documentation)

For more details on developing the Play documentation, see the [[Documentation Guidelines|Documentation]].

<!--
## Run tests
-->
## テストを実行する

<!--
You can run basic tests from the sbt console using the `test` task:
-->
sbt コンソールで `test` タスクを起動すると、基本的なテストを実行することができます。

```
> test
```

<!--
We are also using several Play applications to test the framework. To run this complete test suite, use the `runtests` script:
-->
私達はサンプルアプリケーションを通したフレームワークのテストも行なっています。`runtests` スクリプトを起動すると、サンプルに対するテストを含む完全なテスト・スイートを実行することができます。

```
$ ./runtests
```

<!--
## Use in projects
-->
## プロジェクト内で利用する

Compiling and running projects using the Play version you have built from source requires some custom configuration.

Navigate to your existing Play project and make the following edits in `project/plugins.sbt`:

```
// Change the sbt plugin to use the local Play build (2.3-SNAPSHOT) 
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3-SNAPSHOT")
```

Once you have done this, you can start the console and interact with your project normally:

```
% cd <projectdir>
% activator
```

<!--
## Using Code in Eclipse
-->
## コードを Eclipse で開く

<!--
You can find at [Stackoverflow](http://stackoverflow.com/questions/10053201/how-to-setup-eclipse-ide-work-on-the-playframework-2-0/10055419#10055419) some information how to setup eclipse to work on the code.
-->
Play のコードを eclipse で開くために必要な設定については、 [Stackoverflow](http://stackoverflow.com/questions/10053201/how-to-setup-eclipse-ide-work-on-the-playframework-2-0/10055419#10055419) を参照してください。
