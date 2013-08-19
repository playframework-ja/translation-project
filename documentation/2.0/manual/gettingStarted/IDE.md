<!-- translated -->
<!-- # Setting-up your preferred IDE -->
# 好きな IDE で開発する

<!--
Working with Play is easy. You don’t even need a sophisticated IDE, because Play compiles and refreshes the modifications you make to your source files automatically, so you can easily work using a simple text editor.
-->
Play で開発するのは簡単です。Play は自動的にソースファイルに加えた変更をコンパイルし、変更内容をリフレシュするので、洗練された IDE は必要なく、単純なテキストエディタで簡単に開発することができます。

<!--
However, using a modern Java or Scala IDE provides cool productivity features like auto-completion, on-the-fly compilation, assisted refactoring and debugging.
-->
しかし、モダンな Java, Scala IDE は自動コンパイル、実行中のコンパイル、リファクタリングのアシスト、デバッギングのようなクールで生産的な機能を提供しています。

## Eclipse

<!--
### Generate configuration
-->
### 設定ファイルの生成

<!--
Play provides a command to simplify Eclipse configuration. To transform a Play application into a working Eclipse project, use the `eclipsify` command:
-->
Play は単純な Eclipse の設定のためのコマンドを提供します。Play アプリケーションを Eclipse のプロジェクトに変換するには `eclipsify` コマンドを使います。

```
[My first application] $ eclipsify
```

<!--
You then need to import the application into your Workspace with the **File/Import/General/Existing project…** menu (compile your project first).
-->
その後、 **File/Import/General/Existing project…** メニューを使用してワークスペースにアプリケーションをインポートする必要があります（最初にあなたのプロジェクトをコンパイルします）。

[[images/eclipse.png]] 

<!--
You can also start your application with `play debug run` and then you can use the Connect JPDA launcher using **Debug As** to start a debugging session at any time. Stopping the debugging session will not stop the server.
-->
`play debug run` でアプリケーションを起動すると、**Debug AS** をつかってデバッグセッションを開始できる the Connect JPDA launcher を使うことができます。デバッグセッションを停止しても、サーバは停止しません。

<!-- > Workaround Scala Template with Play 2.0 RC2 and Scala IDE : run your application using `~ run` to enable direct compilation on file change. This way scala templates files are auto discovered when you create new template in `view` and auto compiled when file change. If you use normal `run` then you have to hit `Refresh` on you browser each time. Play team will check this issue with Scala IDE team.  (source : http://bit.ly/wCyR5j) -->
> Play 2.0 RC2 と Scala IDE で Scala Template を使う際の回避策 : `~ run` を使ってアプリケーションを起動することで、ファイルを変更するとすぐにコンパイルされるようになります。こうすることで、`view` ディレクトリに scala テンプレートを新しく作成すると自動的に検出され、ファイルを変更すると自動的にコンパイルされます。通常の `run` を使った場合、毎回ブラウザで `更新` を叩く必要があります。Play チームは、この問題について Scala IDE チームと確認していきます。(ソース : http://bit.ly/wCyR5j)

<!-- If you make any important changes to your application, such as changing the classpath, use `eclipsify` again to regenerate the configuration files. -->
クラスパスの変更のようなアプリケーションにとって重要な変更を行った場合、`eclipsify` コマンドを使って設定ファイルの再作成を行います。

<!-- > Tip: Do not commit Eclipse configuration files when you work in a team! -->
> Tip: チームで作業を行なっている場合は Eclipse の設定ファイルはコミットしないようにしてください。

<!--
The generated configuration files contain absolute references to your framework installation. These are specific to your own installation. When you work in a team, each developer must keep his Eclipse configuration files private.
-->
生成された設定ファイルはあなたのフレームワークのインストールに関する絶対参照を含みます。これはあなた自身のインストールに関する設定です。あなたがチーム内で作業している時には、各開発者は Eclipse の設定ファイルをプライベートにしておく必要があります。

## IntelliJ

<!--
### Generate configuration
-->
### 設定ファイルの生成

<!--
Play provides a command to simplify Intellij IDEA configuration. To transform a Play application into a working IDEA module, use the idea command:
-->
Playは単純なIntellij IDEAの設定のためのコマンドを提供します。PlayアプリケーションをIDEAモジュールに変換するには idea コマンドを使います。

```
[My first application] $ idea
```

<!--
You then need to import the application into your project (File->New Module->Import existing Module)
-->
その後プロジェクトにアプリケーションをインポートする必要があります。（File->New Module->Import existing Module）

[[images/idea.png]] 

<!-- > Tip: There is an [Intellij IDEA issue](http://devnet.jetbrains.net/thread/433870) regarding building Java based Play2 apps while having the scala plugin installed. Until it's fixed, the recommended workaround is to disable scala plugin. -->
> Tip: scala プラグインがインストールされている場合、Java ベースの Play2 アプリで [IntelliJ の問題](http://devnet.jetbrains.net/thread/433870) が発生しています。この問題が解決するまでは、scala プラグインを無効にすることが推奨されます。

<!--
To debug, first add a debug configuration
-->
デバッグを実行するために、最初にデバッグの設定を追加します。

<!--
- Open Run/Debug Configurations dialog, then click Run -> Edit Configurations
- Add a Remote configuration, then select `Remote`
- Configure it:
    - Set a name
    - Transport: Socket
    - Debugger mode: Attach
    - Host: localhost
    - Port: 9999
    - Select module you imported
- Close dialog - click Apply
-->
- Run/Debug Configurations の設定ダイアログを開き、Run -> Edit Configurations をクリックします
- Remote 設定を追加し、`Remote` を選択します
- 以下の内容を設定します。
    - 名前を設定する
    - Transport: Socket を設定する
    - Debugger mode: Attach を設定する
    - Host: localhost を設定する
    - Port: 9999 を設定する
    - インポートしたモジュールを選択する
- Apply をクリックし、ダイアログをクローズします

<!--
Start play in debug mode:
-->
playをデバッグモードで起動します。

```
$ play debug
```

<!--
which should print: 
-->
以下のような出力が行われます。

```
Listening for transport dt_socket at address: 9999
```

<!--
Set some breakpoints. Run the web app by executing the task `play` (again I had to do this in same terminal I ran `play debug`). Finally, browse `http://localhost:9000`. IntelliJ should stop at your breakpoint.
-->
いくつかのブレークポイントをセットします。`play` タスクを実行しアプリケーションを起動します (`play debug` を実行したターミナルでも同じ事を再実行する必要があります) 。`http://localhost:9000` をブラウズすると IntelliJ のブレークポイントで処理が止まっているはずです。

<!--
If you make any important changes to your application, such as changing the classpath, use `idea` again to regenerate the configuration files.
-->
クラスパスの変更のようなアプリケーションにとって重要な変更を行った場合、`idea` コマンドを使って設定ファイルの再作成を行います。


## Netbeans

<!--
### Generate Configuration
-->
### 設定ファイルの生成

<!--
Play does not have native Netbeans project generation support at this time.  For now you can generate a Netbeans Scala project with the [Netbeans SBT plugin](https://github.com/remeniuk/sbt-netbeans-plugin).
-->
Play は現時点ではネイティブの Netbeans プロジェクトの生成をサポートしていません。今のところは [NetBeans SBT Plugin](https://github.com/remeniuk/sbt-netbeans-plugin) を使うと NetBeans の Scala プロジェクトを生成することができます。

<!--
First edit the plugins.sbt file
-->
最初に plugins.sbt ファイルを編集します。

```
// Comment to get more information during initialization
logLevel := Level.Warn

resolvers ++= Seq( 
    DefaultMavenRepository, 
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/", 
    "remeniuk repo" at "http://remeniuk.github.com/maven" 
)

addSbtPlugin("play" % "sbt-plugin" % "2.0-RC2") 

libraryDependencies += "org.netbeans" %% "sbt-netbeans-plugin" % "0.1.4"
```

<!--
Now run
-->
編集が完了したら以下のコマンドを実行します。

```
$ play netbeans
```

<!-- > **Next:** 
>
> - Explore the [[sample applications | Samples]]
>
> - Read the Play 2.0 manual:
>     - [[Play 2.0 for Scala developers | ScalaHome]]
>     - [[Play 2.0 for Java developers | JavaHome]] -->
> **Next:** 
>
> - [[サンプルアプリケーション | Samples]] を調べる
>
> - Play 2.0 マニュアルを読む:
>     - [[Scala 開発者のための Play 2.0 | ScalaHome]]
>     - [[Java 開発者のための Play 2.0 | JavaHome]]