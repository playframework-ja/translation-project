<!-- translated -->
<!--
# Starting your application in production mode
-->
# アプリケーションを本番モードで起動する

<!--
There are several ways to deploy a Play application in production mode. Let's start by using the simplest way, using a local Play installation.
-->
Play アプリケーションを本番モードで起動する方法はいくつかあります。まずは、ローカル環境に Play をインストールするという最も簡単な方法を試してみましょう。

<!--
## Using the start command
-->
## start コマンドを使う

<!--
The easiest way to start an application in production mode is to use the `start` command from the Play console. This requires a Play 2.0 installation on the server.
-->
アプリケーションを本番モードで起動する最も簡単な方法は、 Play コンソールから `start` コマンドを実行することです。そのためには、サーバに予め Play 2.0 をインストールしておく必要があります。

```bash
[My first application] $ start
```

<!--
> Note that the `run` command is only for development mode and should never be used to run an application in production. For each request a complete check is handled by sbt.
-->
> `run` コマンドは開発モードのためだけに用意されたものなので、本番環境では絶対に利用しないでください。開発モードの場合、リクエスト毎に sbt が全てのソースファイルの更新チェックを行ってしまいます。

[[images/start.png]]

<!--
When you run the `start` command, Play forks a new JVM and runs the default Netty HTTP server. The standard output stream is redirected to the Play console, so you can monitor its status.
-->
`start` コマンドを実行すると、 Play は JVM を新たにフォークして、標準の Netty HTTP サーバを起動します。そのとき、標準出力が Play コンソールにリダイレクトされるので、そこからアプリケーションの状態をモニタリングすることができます。

<!--
> The server’s process id is displayed at bootstrap and written to the `RUNNING_PID` file. To kill a running Play server, it is enough to send a `SIGTERM` to the process to properly shutdown the application.
-->
> サーバの起動時にはサーバのプロセスIDが表示されると同時に、 `RUNNING_PID` というファイルに書き込まれます。起動中の Play サーバを停止させるためには、単にそのプロセスに `SIGTERM` シグナルを送信してアプリケーションを終了すればよいでしょう。

<!--
If you type `Ctrl+D`, the Play console will quit, but the created server process will continue running in background. The forked JVM’s standard output stream is then closed, and logging can be read from the `logs/application.log` file.
-->
`Ctrl+D` を入力すると、 Play コンソールは終了しますが、生成されたサーバ・プロセスはバックグラウンドで動作を続けます。そのとき、フォークした JVM の標準出力は閉じられてしまいますが、ログは `logs/application.log` というファイルに出力され続けます。

<!--
If you type `Ctrl+C`, you will kill both JVMs: the Play console and the forked Play server. 
-->
`Ctrl+C` を入力すると、Play コンソールとフォークした Play サーバの両方の JVM を終了します。

<!--
Alternatively you can directly use `play start` at your OS command prompt, which does the same thing:
-->
前述の方法の他に、 OS のコマンドプロンプトから `play start` を直接実行しても、同じことができます。

```bash
$ play start
```

<!--
> Note: the HTTP port can be set by passing -Dhttp.port system variable
-->
> Note: -Dhttp.port システム変数を渡すことで、 HTTP のポート番号を変更することができます。

<!--
## Using the stage task
-->
## stage タスクを使う

<!--
The problem with the `start` command is that it starts the application interactively, which means that human interaction is needed, and `Ctrl+D` is required to detach the process. This solution is not really convenient for automated deployment.
-->
`start` コマンドの問題点は、アプリケーションを対話的に起動するため手作業が必要だということと、 プロセスを切り離すために `Ctrl+D` が必要だということです。つまり、この方法はデプロイを自動化するには向いていません。

<!--
You can use the `stage` task to prepare your application to be run in place. The typical command for preparing a project to be run in place is:
-->
こんなとき `stage` タスクを利用すると、アプリケーションが後で即座に起動できるように、準備をすることができます。プロジェクトを後で即座に実行できるようにするための典型的なコマンドは次のようなものです。

```bash
$ play clean compile stage
```

[[images/stage.png]]

<!--
This cleans and compiles your application, retrieves the required dependencies and copies them to the `target/staged` directory. It also creates a `target/start` script that runs the Play server.
-->
このコマンドにより、アプリケーションのコンパイル時に生成された一時ファイルが削除され、改めてコンパイルが実行され、必要な依存モジュールがダウンロードされ、これら全てが `target/staged` ディレクトリへコピーされます。さらに、 Play サーバを起動するための `target/startP というスクリプトも生成されます。

<!--
You can start your application using:
-->
アプリケーションは次のように起動できます。

```bash
$ target/start
```

<!--
The generated `start` script is very simple - in fact, you could even execute the `java` command directly.
-->
生成された `start` スクリプトはとてもシンプルです。実際のところ、このスクリプトを使わず `java` コマンドにより直接アプリケーションを起動してもいいくらいです。

<!--
If you don’t have Play installed on the server, you can use sbt to do the same thing:
-->
もし Play がサーバにインストールされていないのだえれば、同じことを行うために sbt を使ってもよいでしょう。

```bash
$ sbt clean compile stage
```