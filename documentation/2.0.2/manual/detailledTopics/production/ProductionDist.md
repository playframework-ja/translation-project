<!-- translated -->
<!--
# Creating a standalone version of your application
-->
# スタンドアロンで実行可能なアプリケーションのビルド

<!--
## Using the dist task
-->
## dist タスクを使う

<!--
The simplest way to deploy a Play 2.0 application is to retrieve the source (typically via a git workflow) on the server and to use either `play start` or `play stage` to start it in place.

However, you sometimes need to build a binary version of your application and deploy it to the server without any dependencies on Play itself. You can do this with the `dist` task.

In the Play console, simply type `dist`:
-->
Play 2.0 アプリケーションをデプロイする最も簡単な方法は、(git などを使って) ソースコードをサーバに保存して、 `play start` もしくは `play stage` のどちらかでアプリケーションを起動します。

その他に、Play のフレームワークに依存しないアプリケーションのバイナリをビルドしたいこともあるでしょう。その場合は、 `dist` タスクが利用できます。

Play コンソールで、 `dist` を入力してみましょう。

```bash
[My first application] $ dist
```

[[images/dist.png]]

<!--
> one can easily use an external application.conf by using a special system property called ```conf.file```, so assuming your production ```application.conf``` is stored under your home directory, the following command should create a play distribution using the custom ```application.conf```:_ 
-->
> ```conf.file``` という特別なシステムプロパティにより、外部の application.conf を使ってアプリケーションをビルドすることができます。仮に本番環境向けの ```application.conf``` がホームディレクトリ以下に保存されているとすると、以下のコマンドでデフォルト以外の ```application.conf``` を組み込んだ play アプリケーションのバイナリをビルドすることができます。
> ```bash
>  $ play -Dconfig.file=/home/peter/prod/application.conf dist 
> ```

<!--
This produces a ZIP file containing all JAR files needed to run your application in the `target` folder of your application, the ZIP file’s contents are organized as:
-->
タスクが完了すると、`target` フォルダに、アプリケーションを実行するための必要な全ての JAR ファイルを含む ZIP ファイルが生成されます。この ZIP ファイルの中身は次のような構成になっています。

```
my-first-application-1.0
 └ lib
    └ *.jar
 └ start
```

<!--
You can use the generated `start` script to run your application.
-->
アプリケーションを実行するには `start` スクリプトを実行します。

<!--
Alternatively you can run `play dist` directly from your OS shell prompt, which does the same thing:
-->
Play コンソールを使う代わりに、 OS のシェルプロンプトから `play dist` を直接起動しても同じ事ができます。

```bash
$ play dist
```

<!--
## Publishing to a Maven (or Ivy) repository
-->
## Maven (または Ivy) レポジトリへパブリッシュする

<!--
You can also publish your application to a Maven repository. This publishes both the JAR file containing your application and the corresponding POM file.
-->
アプリケーションを Maven レポジトリへパブリッシュすることもできます。パブリッシュされるのは、アプリケーションの JAR と、POM ファイルの二つです。

<!--
You have to configure the repository you want to publish to, in the `project/Build.scala` file:
-->
パブリッシュしたい場合は、そのための設定を `project/Build.scala` に記述する必要があります。

```scala
val main = PlayProject(appName, appVersion, appDependencies).settings(
  
  publishTo := Some(
    "My resolver" at "http://mycompany.com/repo"
  )
  
  credentials += Credentials(
    "Repo", "http://mycompany.com/repo", "admin", "admin123"
  )
  
)
```

<!--
Then in the Play console, use the `publish` task:
-->
設定を記述したら、Play コンソールで `publish` タスクを実行します。

```bash
[My first application] $ publish
```

<!--
> Check the sbt documentation to get more information about the resolvers and credentials definition.
-->
> 上記の設定中にも登場している resolver や credentials の詳細な定義方法については、sbt のドキュメントを参照してください。
