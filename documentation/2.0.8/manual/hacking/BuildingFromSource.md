<!-- translated -->
<!--
# Building Play 2.0 from sources
-->
# Play 2.0 をソースコードからビルドする

<!--
To benefit from the latest improvements and bug fixes after the initial beta release, you may want to compile Play 2.0 from sources. You’ll need a [[Git client | http://git-scm.com/]] to fetch the sources.
-->
初回のベータ版リリース以降の最新の改善やバグフィックスの恩恵を受けたいときは、 Play 2.0 をソースコードからコンパイルしましょう。ソースコードは [[Git client | http://git-scm.com/]] で取得します。

<!--
From the shell, first checkout the Play 2.0 sources:
-->
シェルから Play 2.0 のソースを初めてチェックアウトします:

```bash
$ git clone git://github.com/playframework/Play20.git
```

<!--
Then go to the `Play20/framework` directory and launch the `build` script to enter the sbt build console:
-->
ソースコードを取得したら、 `Play20/framework` ディレクトリに移動して、 `build` スクリプトを起動し、 sbt によるビルド・コンソールに入ります。

```bash
$ cd Play20/framework
$ ./build
> build-repository
```

<!--
Once in the sbt console, run `build-repository` to compile and build everything. This will also create the local Ivy repository containing all of the required dependencies.
-->
sbt コンソールに入ったら、 `build-repository` タスクを実行して、フレームワーク全体をコンパイルしましょう。このタスクにより、Play が依存する全ての依存モジュールを含むローカル Ivy レポジトリも作成されます。

<!--
> Note that you don’t need to install sbt yourself: Play 2.0 embeds its own version (currently sbt 0.11.2).
-->
> sbt を別途インストールする必要はありません。 Play 2.0 には必要なバージョンの sbt （現在は sbt 0.11.2)が既に組み込まれているからです。

<!--
If you want to make changes to the code you can use `compile` and `publish-local` to rebuild the framework.
-->
もし、 Play に独自の変更を加えた場合は、 `compile` と `publish-local` を使ってフレームワークを再ビルドするとよいでしょう。

<!--
## Running tests
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
## Creating projects
-->
## プロジェクトを作成する

<!--
Creating projects using the Play version you have built from source works much the same as a regular Play application.
-->
ソースコードから自分でビルドしたバージョンの Play を使ってプロジェクトを作成する方法は、通常の Play アプリケーションを作成する方法とほとんど同じです。

export PATH=$PATH:<projdir>/Play20

<!--
If you have an existing Play 2.0 application that you are upgrading from Play 2.0 Beta to edge, please add 
-->
既に Play 2.0 Beta で開発された Play 2.0 アプリケーションを、自分でビルドしたバージョンにアップグレードしたいときは、

```
resolvers ++= Seq(
  ...
  Resolver.file("Local Repository", file("<projdir>/Play20/repository/local"))(Resolver.ivyStylePatterns),
  ...
)

addSbtPlugin("play" % "sbt-plugin" % "2.1-SNAPSHOT")
```

<!--
to project/plugins.sbt.
-->
のような設定を、 project/plugins.sbt に追加しましょう。

<!--
## Using Code in eclipse.
-->
## コードを eclipse で開く

<!--
You can find at [Stackoverflow](http://stackoverflow.com/questions/10053201/how-to-setup-eclipse-ide-work-on-the-playframework-2-0/10055419#10055419) some information how to setup eclipse to work on the code.
-->
Play のコードを eclipse で開くために必要な設定については、 [Stackoverflow](http://stackoverflow.com/questions/10053201/how-to-setup-eclipse-ide-work-on-the-playframework-2-0/10055419#10055419) を参照してください。
