<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Using the Play console
-->
# Play コンソールを使う

<!--
## Launching the console
-->
## コンソールの起動

<!--
The Play console is a development console based on sbt that allows you to manage a Play application’s complete development cycle.
-->
Play コンソールは、sbt をベースにした開発コンソールです。Play アプリケーションの全ての開発サイクルをサポートしてくれます。

<!--
To launch the Play console, change to the directory of your project, and run Activator:
-->
Play コンソールを起動するにはプロジェクトのディレクトリに移動し、Activator を実行します。

```bash
$ cd my-first-app
$ activator
```

[[images/console.png]]

<!--
## Getting help
-->
## ヘルプの表示

<!--
Use the `help` command to get basic help about the available commands.  You can also use this with a specific command to get information about that command:
-->
`help` コマンドを使えば、利用できるコマンドについての基本的な解説を得られます。これを使えばさらに、特定のコマンドに関する情報を得ることもできます。

```bash
[my-first-app] $ help run
```

<!--
## Running the server in development mode
-->
## 開発モードでサーバを起動する

<!--
To run the current application in development mode, use the `run` command:
-->
アプリケーションを開発モードで起動するためには、 `run` コマンドを利用しましょう。

```bash
[my-first-app] $ run
```

[[images/consoleRun.png]]

<!--
In this mode, the server will be launched with the auto-reload feature enabled, meaning that for each request Play will check your project and recompile required sources. If needed the application will restart automatically.
-->
このモードでは、サーバはホットデプロイ機能が有効になった状態でサーバが起動します。つまり、リクエストがくるたびに、 Play がプロジェクト全体をチェックして、必要なソースコードだけを再コンパイルします。サーバの再起動が必要なときは、それも自動的に行います。

<!--
If there are any compilation errors you will see the result of the compilation directly in your browser:
-->
コンパイルエラーが発生したときは、その内容をブラウザ上で直接確認することができます。

[[images/errorPage.png]]

<!--
To stop the server, type `Crtl+D` key, and you will be returned to the Play console prompt.
-->
サーバを停止させるためには、 `Ctrl+D` キーをタイプします。サーバが停止して、Play コンソールのプロンプトへ戻ります。

<!--
## Compiling
-->
## コンパイル

<!--
In Play you can also compile your application without running the server. Just use the `compile` command:
-->
Play では、 `compile` コマンドを使って、サーバを起動せずにアプリケーションをコンパイルすることもできます。

```bash
[my-first-app] $ compile
```

[[images/consoleCompile.png]]

<!--
## Launch the interactive console
-->
## 対話コンソールを起動する

<!--
Type `console` to enter the interactive Scala console, which allows you to test your code interactively:
-->
`console` コマンドを実行すると、Scala の REPL が起動して、アプリケーションのコードを対話的にテストすることができます。

```bash
[my-first-app] $ console
```

<!--
To start application inside scala console (e.g to access database):
-->
以下のようにして (例えばデータベースにアクセスする) アプリケーションを scala コンソールから開始することができます。
```bash
scala> new play.core.StaticApplication(new java.io.File("."))
```

[[images/consoleEval.png]] 

<!--
## Debugging
-->
## デバッグモード

<!--
You can ask Play to start a **JPDA** debug port when starting the console. You can then connect using Java debugger. Use the `activator -jvm-debug <port>` command to do that:
-->
コンソールを起動する時に **JPDA** デバッグポートの開始を依頼できます。その後は Java デバッガを使って接続することが可能です。そうする場合は `activator -jvm-debug <port>` コマンドを使ってください。

```
$ activator -jvm-debug 9999
```

<!--
When a JPDA port is available, the JVM will log this line during boot:
-->
JPDA ポートが利用可能になると、JVM はブート中以下のようなログを出力します。

```
Listening for transport dt_socket at address: 9999
```

<!--
## Using sbt features
-->
## sbt の機能を利用する

<!--
The Play console is just a normal sbt console, so you can use sbt features such as **triggered execution**. 
-->
Play コンソールは普通の sbt コンソールでもあるため、 ***triggered execution*** のような sbt の機能も利用することができます。

<!--
For example, using `~ compile`
-->
例えば、 `~ compile` コマンドを実行すると、ソースコードを変更するたびに必要なソースコードだけが再コンパイルさせることができます。

```bash
[my-first-app] $ ~ compile
```

<!--
The compilation will be triggered each time you change a source file.
-->
コンパイルはソースファイルを変更するたびに実行されます。

<!--
If you are using `~ run`
-->
もし `~ run` コマンドを実行している場合

```bash
[my-first-app] $ ~ run
```

<!--
The triggered compilation will be enabled while a development server is running.
-->
開発サーバの稼働中は、コンパイルの実行は有効になります。

<!--
You can also do the same for `~ test`, to continuously test your project each time you modify a source file:
-->
同様に、`~ test` コマンドを実行すると、ソースコードが変更されるたびにテストが実行されます。

```bash
[my-first-app] $ ~ test
```

<!--
## Using the play commands directly
-->
## Play コマンドを直接実行する

<!--
You can also run commands directly without entering the Play console. For example, enter `activator run`:
-->
Play コンソールに入らずに直接コマンドを実行することができます。例えば、 `actovator run` と入力してみましょう。

```bash
$ activator run
[info] Loading project definition from /Users/jroper/tmp/my-first-app/project
[info] Set current project to my-first-app (in build file:/Users/jroper/tmp/my-first-app/)

--- (Running the application from SBT, auto-reloading is enabled) ---

[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Ctrl+D to stop and go back to the console...)
```

<!--
The application starts directly. When you quit the server using `Ctrl+D`, you will come back to your OS prompt.
-->
アプリケーションを直接起動しているとき、サーバの停止のため `Ctrl+D` コマンドを利用すると、すぐに OS のプロンプト画面に戻るでしょう。

<!--
> **Next:** [[Setting-up your preferred IDE | IDE]]
-->
> **Next:** [[好きな IDE で開発する | IDE]]
