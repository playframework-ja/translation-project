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
The simplest way to deploy a Play application is to retrieve the source (typically via a git workflow) on the server and to use either `play start` or `play stage` to start it in place.
-->
Play アプリケーションをデプロイする最も簡単な方法は、(git などを使って) ソースコードをサーバに保存して、 `play start` もしくは `play stage` のどちらかでアプリケーションを起動することです。

<!--
However, you sometimes need to build a binary version of your application and deploy it to the server without any dependency on Play itself. You can do this with the `dist` task.
-->
その他に、Play のフレームワークに依存しないアプリケーションのバイナリをビルドしたいこともあるでしょう。その場合は、 `dist` タスクが利用できます。

<!--
In the Play console, simply type `dist`:
-->
Play コンソールで、 `dist` を入力してみましょう。

```bash
[My first application] $ dist
```

[[images/dist.png]]

<!--
This produces a ZIP file containing all JAR files needed to run your application in the `target/universal` folder of your application. Alternatively you can run `play dist` directly from your OS shell prompt, which does the same thing:
-->
こうすることで、アプリケーションを実行するために必要なすべての JAR ファイルを含む ZIP ファイルがアプリケーションの `target/univeral` フォルダに作られます。代わりに OS のシェルプロンプトから `play dist` を直接実行することもできます。これらは同じことです:

```bash
$ play dist
```

<!--
> For Windows users a start script will be produced with a .bat file extension. Use this file when running a Play application on Windows.
-->
> Windows ユーザー向けの起動スクリプトは拡張子 .bat のファイルとして提供されています。Windows 上で Play アプリケーションを実行する場合は、このファイルを使用してください。
>
<!--
> For Unix users, zip files do not retain Unix file permissions so when the file is expanded the start script will be required to be set as an executable:
-->
Unix ユーザーのみなさん、zip ファイルは Unix ファイルパーミッションを保持しないので、ファイルを展開したらこの起動スクリプトに実行権限を付与する必要があるでしょう。
>
> ```bash
> $ chmod +x /path/to/bin/<project-name>
> ```
>
<!--
> Alternatively a tar.gz file can be produced instead. Tar files retain permissions. Invoke the `universal:package-zip-tarball` task instead of the `dist` task:
-->
> この代わりに tar.gz ファイルを作ることができます。このファイルはパーミッションを保持します。`dist` タスクの代わりに `universal:package-zip-tarball` タスクを実行してください:
>
> ```bash
> play universal:package-zip-tarball
> ```

<!--
## The Native Packager
-->
## Native Packager

<!--
Play uses the [SBT Native Packager plugin](http://www.scala-sbt.org/sbt-native-packager/). The native packager plugin declares the `dist` task to create a zip file. Invoking the `dist` task is directly equivalent to invoking the following:
-->
Play は [SBT Native Packager プラグイン](http://www.scala-sbt.org/sbt-native-packager/) を使っています。この native packager プラグインは zip ファイルを作る `dist` タスクを宣言しています。`dist` タスクを実行することは、以下を実行することとまったく等価です:

```bash
$ play universal:package-bin
```

<!--
Many other types of archive can be generated including:
-->
この他にも、以下を含む多くの種類のアーカイブを生成することができます。

<!--
* tar.gz
* OS X disk images
* Microsoft Installer (MSI)
* RPMs
* Debian files
-->
* tar.gz
* OS X ディスクイメージ
* Microsoft インストーラ (MSI)
* RPM
* Debian ファイル

<!--
Please consult the [documentation](http://www.scala-sbt.org/sbt-native-packager) on the native packager for more information.
-->
もっと詳しい情報は、native packager の [ドキュメント](http://www.scala-sbt.org/sbt-native-packager) をご覧ください。

<!--
## Publishing to a Maven (or Ivy) repository
-->
## Maven (または Ivy) レポジトリへパブリッシュする

<!--
You can also publish your application to a Maven repository. This publishes both the JAR file containing your application and the corresponding POM file.
-->
アプリケーションを Maven レポジトリへパブリッシュすることもできます。パブリッシュされるのは、アプリケーションの JAR と、POM ファイルの二つです。

<!--
You have to configure the repository you want to publish to, in your `build.sbt` file:
-->
`build.sbt` にパブリッシュしたいリポジトリを設定する必要があります:

```scala
 publishTo := Some(
   "My resolver" at "http://mycompany.com/repo"
 ),
 
 credentials += Credentials(
   "Repo", "http://mycompany.com/repo", "admin", "admin123"
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
> Check the [sbt documentation](http://www.scala-sbt.org/release/docs/index.html) to get more information about the resolvers and credentials definition.
-->
リゾルバと認証情報の定義に関するさらに詳しい情報については [sbt ドキュメント](http://www.scala-sbt.org/release/docs/index.html) を確認してください。

<!--
> **Next:** [[Production configuration|ProductionConfiguration]]
-->
> **Next:** [[本番環境の設定|ProductionConfiguration]]