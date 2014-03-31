<!-- translated -->
<!--
# Building Play from sources
-->
# Play をソースコードからビルドする

To benefit from the latest improvements and bug fixes after the initial beta release, you may want to compile Play from sources. You’ll need a [Git client](http://git-scm.com/) to fetch the sources.

## Grab the source

<!--
From the shell, first checkout the Play sources:
-->
初めに、シェルから Play のソースをチェックアウトします:

```bash
$ git clone git://github.com/playframework/playframework.git
```

Then go to the `playframework/framework` directory and launch the `build` script to enter the sbt build console:

```bash
$ cd playframework/framework
$ ./build
> publish-local
```

> Note that you don’t need to install sbt yourself: Play embeds its own version.

<!--
If you want to make changes to the code you can use `publish-local` to rebuild the framework.
-->
もし、 Play に独自の変更を加えた場合は、 `publish-local` を使ってフレームワークを再ビルドするとよいでしょう。

## Build the documentation

Documentation is available at playframework/documentation as Markdown files.  You can generate formatted documentation, javadoc and scaladoc:

```bash
$ cd playframework/framework
$ ./build doc
```
<!--
If done properly, once you run a project, you should be able to see documentation available locally at [http://localhost:9000/@documentation](http://localhost:9000/@documentation)
-->
正しく実行できたら、プロジェクトを実行すると手元の [http://localhost:9000/@documentation](http://localhost:9000/@documentation) でドキュメントを参照できるようになるはずです。

## Run tests

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

## Use in projects

<!--
Creating projects using the Play version you have built from source works much the same as a regular Play application.
-->
ソースコードから自分でビルドしたバージョンの Play を使ってプロジェクトを作成する方法は、通常の Play アプリケーションを作成する方法とほとんど同じです。

export PATH=$PATH:<projdir>/playframework

If you have an existing Play application that you are upgrading, please add

```
resolvers ++= Seq(
  ...
  Resolver.file("Local Repository", file("<projdir>/playframework/repository/local"))(Resolver.ivyStylePatterns),
  ...
)

addSbtPlugin("play" % "sbt-plugin" % "2.2-SNAPSHOT")
```

to project/plugins.sbt. 

## Using Code in Eclipse

<!--
You can find at [Stackoverflow](http://stackoverflow.com/questions/10053201/how-to-setup-eclipse-ide-work-on-the-playframework-2-0/10055419#10055419) some information how to setup eclipse to work on the code.
-->
Play のコードを eclipse で開くために必要な設定については、 [Stackoverflow](http://stackoverflow.com/questions/10053201/how-to-setup-eclipse-ide-work-on-the-playframework-2-0/10055419#10055419) を参照してください。