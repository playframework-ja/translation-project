<!-- translated -->
<!--
# Using the Play 2.0 console
-->
# Play 2.0 コンソールを使う

<!--
## Launching the console
-->
## コンソールの起動

<!--
The Play 2.0 console is a development console based on sbt that allows you to manage a Play application’s complete development cycle.
-->
Play 2.0 コンソールは、sbt をベースにした開発コンソールです。Play アプリケーションの全ての開発サイクルをサポートしてくれます。

<!--
To launch the console, enter any existing Play application directory and run the `play` script:
-->
コンソールを起動するためには、Play アプリケーションのディレクトリで、`play` スクリプトを実行してください。

```bash
$ cd /path/to/any/application
$ play 
```

[[images/console.png]]

<!--
## Getting help
-->
## ヘルプの表示

<!--
Use the `help play` command to get basic help about the available commands:
-->
`help play` コマンドを実行すると、利用可能なコマンドとその簡単な説明が確認できます。

```bash
[My first application] $ help play
```

<!--
## Running the server in development mode
-->
## 開発モードでサーバを起動する

<!--
To run the current application in development mode, use the `run` command:
-->
アプリケーションを開発モードで起動するためには、`run` コマンドを利用しましょう。

```bash
[My first application] $ run
```

[[images/consoleRun.png]]

<!--
In this mode, the server will be launched with the auto-reload feature enabled, meaning that for each request Play will check your project and recompile required sources. If needed the application will restart automatically.
-->
このモードでは、サーバはホットデプロイ機能が有効になった状態でサーバが起動します。つまり、リクエストがくるたびに、Play がプロジェクト全体をチェックして、必要なソースコードだけを再コンパイルします。サーバの再起動が必要なときは、それも自動的に行います。

<!--
If there are any compilation errors you will see the result of the compilation directly in your browser:
-->
コンパイルエラーが発生したときは、その内容をブラウザ上で直接確認することができます。

[[images/errorPage.png]]

<!--
To stop the server, type `Crtl+D` key, and you will be returned to the Play console prompt.
-->
サーバを停止させるためには、`Ctrl+D` キーをタイプします。サーバが停止して、Play コンソールのプロンプトへ戻ります。

<!--
## Compiling
-->
## コンパイル

<!--
In Play 2.0 you can also compile your application without running the server. Just use the `compile` command:
-->
Play 2.0 では、`compile` コマンドを使って、サーバを起動せずにアプリケーションをコンパイルすることもできます。

```bash
[My first application] $ compile
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
[My first application] $ console
```

[[images/consoleEval.png]] 

<!--
## Debugging
-->
## デバッグモード

<!--
You can ask Play to start a **JPDA** debug port when starting the console. You can then connect using Java debugger. Use the `play debug` command to do that:
-->
**JPDA** (Java Platform Debugger Architecture) のデバッグポートの利用をコンソールの開始時に指定できます。デバッグモードの利用には `play debug` コマンドを利用します。

```
$ play debug
```

<!-- When a JPDA port is available, the JVM wil log this line during boot: -->
JPDA ポートが利用可能になると、JVM はブート中以下のようなログを出力します。

```
Listening for transport dt_socket at address: 9999
```

<!--
> **Note:** Using `play debug` the JPDA socket will be opened on port `9999`. You can also set the `JPDA_PORT` environment variable yourself using `set JPDA_PORT=1234`.
-->
> **Note:** `play debug`の利用するとデフォルトで JPDA ソケットは `9999` ポートでオープンします。`set JPDA_PORT=1234` のように環境変数 `JPDA_PORT` でポートを設定することができます。

<!--
## Using sbt features
-->
## sbt の機能を利用する

<!--
The Play console is just a normal sbt console, so you can use sbt features such as **triggered execution**. 
-->
Play コンソールは普通の sbt コンソールでもあるため、***triggered execution*** のような sbt の機能も利用することができます。

<!--
For example, using `~ compile`
-->
例えば、`~ compile` コマンドを実行すると、ソースコードを変更するたびに必要なソースコードだけが再コンパイルさせることができます。

```bash
[My first application] $ ~ compile
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
[My first application] $ ~ run
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
[My first application] $ ~ test
```

<!--
## Using the play commands directly
-->
## Play コマンドを直接実行する

<!--
You can also run commands directly without entering the Play console. For example, enter `play run`:
-->
Play コンソールを起動せずに、コマンドを直接実行することもできます。例えば、`play run` は次のようにも起動できます。

```bash
$ play run
[info] Loading project definition from myFirstApp/project
[info] Set current project to My first application

--- (Running the application from SBT, auto-reloading is enabled) ---

[info] play - Listening for HTTP on port 9000...

(Server started, use Ctrl+D to stop and go back to the console...)
```

<!--
The application starts directly. When you quit the server using `Ctrl+D`, you will come back to your OS prompt.
-->
アプリケーションを直接起動しているとき、サーバの停止のため `Ctrl+D` コマンドを利用すると、すぐに OS のプロンプト画面に戻るでしょう。

<!--
## Force clean
-->
## 強制クリーニング

<!--
If something goes wrong and you think that the sbt cache is corrupted, use the `clean-all` command for your OS command line to clean all generated directories.
-->
何かを間違えて sbt キャッシュが邪魔になったような場合、全ての生成されたディレクトリをクリーニングにするために、OS のコマンドラインツールで `clean-all` コマンドを実行します。

```
$ play clean-all
```

<!--
> **Next:** [[Setting-up your preferred IDE | IDE]]
-->
> **Next:** [[好きな IDE で開発する | IDE]]