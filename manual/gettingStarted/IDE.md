<!-- translated -->
<!--
# Setting up your preferred IDE
-->
# 好きな IDE で開発する

<!--
Working with Play is easy. You don’t even need a sophisticated IDE, because Play compiles and refreshes the modifications you make to your source files automatically, so you can easily work using a simple text editor.
-->
Play で開発するのは簡単です。Play は自動的にソースファイルに加えた変更をコンパイルし、変更内容をリフレッシュするので、洗練された IDE は必要なく、単純なテキストエディタで簡単に開発することができます。

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
Play provides a command to simplify Eclipse configuration. To transform a Play application into a working Eclipse project, use the `eclipse` command:
-->
Play は単純な Eclipse の設定のためのコマンドを提供します。Play アプリケーションを Eclipse のプロジェクトに変換するには `eclipse` コマンドを使います。

<!--
without the source jars:
-->
ソースの jar ファイルが必要ない場合は、以下のコマンドを実行します。

```
[My first application] $ eclipse
```

<!--
if you want to grab the available source jars (this will take longer and it's possible a few sources might be missing):
-->
使用可能なソースの jar ファイルを取得したい場合は、以下のコマンドを実行します (この場合より多くの時間がかかり、いくつかのソースはなくなっているかもしれません) 。

```
[My first application] $ eclipse with-source=true
```

<!--
> Note if you are using sub-projects with aggregate, you would need to set `skipParents` appropriately:
-->
> もしサブプロジェクト構成にしている場合は、以下のように `skipParents` を適切に設定する必要があるでしょう。

```
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys

object ApplicationBuild extends Build {

  override def settings = super.settings ++ Seq(
      EclipseKeys.skipParents in ThisBuild := false
  )

  ...
 
}
```
<!--
or from the play console, type:
-->
あるいは play コンソールから以下のようにタイプします:

``` 
[My first application] $ eclipse skip-parents=false
```

<!--
> Also, if you did not want to trigger a compilation before running `eclipse`, then just add the following to your settings:
-->
> また、もし `eclipse` の実行時にコンパイルをトリガーさせたくない場合は、以下の設定を追加します:

```
EclipsePlugin.EclipseKeys.preTasks := Seq()
```

<!--
You then need to import the application into your Workspace with the **File/Import/General/Existing project…** menu (compile your project first).
-->
その後、 **File/Import/General/Existing project…** メニューを使用してワークスペースにアプリケーションをインポートする必要があります（最初にあなたのプロジェクトをコンパイルします）。

[[images/eclipse.png]] 

<!--
You can also start your application with `play debug run` and then you can use the Connect JPDA launcher using **Debug As** to start a debugging session at any time. Stopping the debugging session will not stop the server.
-->
`play debug run` でアプリケーションを起動すると、**Debug AS** をつかってデバッグセッションを開始できる the Connect JPDA launcher を使うことができます。
デバッグセッションを停止しても、サーバは停止しません。


<!--
> **Tip**: You can run your application using `~run` to enable direct compilation on file change. This way scala template files are auto discovered when you create a new template in `view` and auto compiled when the file changes. If you use normal `run` then you have to hit `Refresh` on your browser each time.
-->
> **Tip**:  `~run` を使うとファイルの変更時の直接コンパイルを有効にしてアプリケーションを実行できます。これにより `view` に新しいテンプレートファイルが作成されたことを自動的に検出し、ファイルに変更が加わった時に自動的にコンパイルされます。通常の `run` コマンドを使っていると、毎回ブラウザでのリフレッシュを行わなければいけません。

<!--
If you make any important changes to your application, such as changing the classpath, use `eclipse` again to regenerate the configuration files.
-->
クラスパスの変更のようなアプリケーションにとって重要な変更を行った場合、`eclipse` コマンドを使って設定ファイルの再作成を行います。

<!--
> **Tip**: Do not commit Eclipse configuration files when you work in a team!
-->
> **Tip**: チームで作業を行なっている場合は Eclipse の設定ファイルはコミットしないようにしてください。

<!--
The generated configuration files contain absolute references to your framework installation. These are specific to your own installation. When you work in a team, each developer must keep his Eclipse configuration files private.
-->
生成された設定ファイルはあなたのフレームワークのインストールに関する絶対参照を含みます。これはあなた自身のインストールに関する設定です。あなたがチーム内で作業している時には、各開発者は Eclipse の設定ファイルをプライベートにしておく必要があります。

## IntelliJ

<!--
### Generate configuration
-->
### 設定ファイルの生成

<!-- Play provides a command to simplify Intellij IDEA configuration. To transform a Play application into a working IDEA module, use the idea command from the play console: -->
Play は単純な Intellij IDEA の設定のためのコマンドを提供します。Play アプリケーションを IDEA モジュールに変換するには play コンソールから idea コマンドを使います。

<!--
without the source jars:
-->
ソースの jar ファイルが必要ない場合は、以下のコマンドを実行します。

```
[My first application] $ idea
```

<!--
if you want to grab the available source jars (this will take longer and it's possible a few sources might be missing):
-->
使用可能なソースの jar ファイルを取得したい場合は、以下のコマンドを実行します (この場合より多くの時間がかかり、いくつかのソースはなくなっているかもしれません) 。

```
[My first application] $ idea with-sources=yes
```

<!--
This will create the configuration files IntelliJ needs to open your play application as a project. The files are named <project>.iml and <project>-build.iml. The file menu (IntelliJ 11.1 CE) contains the Open Project command.
-->
これで IntelliJ が play アプリケーションをプロジェクトとして開くための設定ファイルが作成されます。これらのファイル名は <project>.iml と <project>-build.iml です。 (IntelliJ 11.1 CE の) file メニューには Open Project コマンドがあります。

<!--
> Tip: There is an [Intellij IDEA issue](http://devnet.jetbrains.net/thread/433870) regarding building Java based Play2 apps while having the Scala plugin installed. Until it's fixed, the recommended workaround is to disable the Scala plugin.
-->
> Tip: Scala プラグインがインストールされている場合、Java ベースの Play2 アプリで [IntelliJ の問題](http://devnet.jetbrains.net/thread/433870) が発生しています。この問題が解決するまでは、Scala プラグインを無効にすることが推奨されます。

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
Start play in debug mode (in a separate command line console, NOT in IDEA's Play console):
-->
play をデバッグモードで起動 (IDEA の Play コンソールではなく別のコマンドラインコンソールで) します。

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
Set some breakpoints then start your new debug configuration from within IDEA. The console output should be:
-->
いくつかのブレークポイントをセットしてから IDEA のデバッグ設定を開始します。コンソール出力は以下のようになっているはずです。

```
Connected to the target VM, address: 'localhost:9999', transport: 'socket'
```

<!--
Run the web app by executing the task `run` in the Play console. Finally, browse to `http://localhost:9000`. IntelliJ should stop at your breakpoint.
-->
Play コンソールの `run` タスクを呼び出す事で web アプリを実行します。最後に `http://localhost:9000` をブラウザで開きます。 IntelliJ はブレークポイントで停止しているはずです。

<!--
Alternatively, in order not to run more command prompts, first run "play debug run" in IDEA's Play console, then launch debug configuration.
-->
その他の方法として、新たなコマンドプロンプトを開かずに IDEA の Play コンソールで "play debug run" を実行し、それからデバッグ設定を起動する事も出来ます。

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
resolvers += {
  "remeniuk repo" at "http://remeniuk.github.com/maven" 
}

libraryDependencies += {
  "org.netbeans" %% "sbt-netbeans-plugin" % "0.1.4"
}
```

<!--
Now run
-->
編集が完了したら以下のコマンドを実行します。

```
$ play netbeans
```


## ENSIME

<!--
### Install ENSIME
-->
### ENSIME のインストール

<!-- Follow the installation instructions at <https://github.com/aemoncannon/ensime> -->
<http://github.com/aemoncannon/ensime> の手順に従ってインストールします。

<!--
### Generate configuration
-->
### 設定の作成

<!-- Edit your project/plugins.sbt file, and add the following line (you should first check <https://github.com/aemoncannon/ensime-sbt-cmd> for the latest version of the plugin): -->
project/plugins.sbt ファイルを編集して、以下の行を追加します (最新版のプラグインを入手するためにまずは <http://github.com/aemoncannon/ensime-sbt-cmd> をチェックすると良いです)。

```
addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.1.0")
```

<!--
Start Play:
-->
Play を起動します:

```
$ play
```

<!--
Enter 'ensime generate' at the play console. The plugin should generate a .ensime file in the root of your Play project.
-->
Play コンソールから 'ensime generate' と入力します。このプラグインにより Play プロジェクトのルートに .ensime ファイルが作成されているはずです。

```
$ [MYPROJECT] ensime generate
[info] Gathering project information...
[info] Processing project: ProjectRef(file:/Users/aemon/projects/www/MYPROJECT/,MYPROJECT)...
[info]  Reading setting: name...
[info]  Reading setting: organization...
[info]  Reading setting: version...
[info]  Reading setting: scala-version...
[info]  Reading setting: module-name...
[info]  Evaluating task: project-dependencies...
[info]  Evaluating task: unmanaged-classpath...
[info]  Evaluating task: managed-classpath...
[info] Updating {file:/Users/aemon/projects/www/MYPROJECT/}MYPROJECT...
[info] Done updating.
[info]  Evaluating task: internal-dependency-classpath...
[info]  Evaluating task: unmanaged-classpath...
[info]  Evaluating task: managed-classpath...
[info]  Evaluating task: internal-dependency-classpath...
[info] Compiling 5 Scala sources and 1 Java source to /Users/aemon/projects/www/MYPROJECT/target/scala-2.9.1/classes...
[info]  Evaluating task: exported-products...
[info]  Evaluating task: unmanaged-classpath...
[info]  Evaluating task: managed-classpath...
[info]  Evaluating task: internal-dependency-classpath...
[info]  Evaluating task: exported-products...
[info]  Reading setting: source-directories...
[info]  Reading setting: source-directories...
[info]  Reading setting: class-directory...
[info]  Reading setting: class-directory...
[info]  Reading setting: ensime-config...
[info] Wrote configuration to .ensime
```

<!--
### Start ENSIME
-->
### ENSIME の起動

<!--
From Emacs, execute M-x ensime and follow the on-screen instructions.
-->
Emacs から M-x ensime を実行して、画面に表示された手順に沿って手続きを行います。

<!--
That's all there is to it. You should now get type-checking, completion, etc. for your Play project. Note, if you add new library dependencies to your play project, you'll need to re-run "ensime generate" and re-launch ENSIME.
-->
手続きは以上です。これで Play プロジェクトでタイプチェックやコンパイル等を行うことができます。もし Play プロジェクトで新しいライブラリの依存性を追加した場合は、再度 "ensime generate" を実行して ENSIME を再起動する必要があります。

<!--
### More Information
-->
### 詳しい情報は

<!-- Check out the ENSIME manual at <http://aemoncannon.github.com/ensime/index.html>
If you have questions, post them in the ensime group at <https://groups.google.com/forum/?fromgroups=#!forum/ensime> -->
<http://aemoncannon.github.com/ensime/index.html> にある ENSIME マニュアルをチェックして下さい。
質問がある場合は <https://groups.google.com/forum/?fromgroups=#!forum/ensime> にある ensime グループに投稿して下さい。

<!--
## All Scala Plugins if needed
-->
## Scala プラグインが必要な場合は

<!--
Scala is a newer programming language, so the functionality is provided in plugins rather than in the core IDE.
-->
Scala は新しいプログラミング言語です。そのため、機能は IDE のコアではなくプラグインとして組み込まれています。

<!-- - Eclipse Scala IDE: <http://scala-ide.org/>
- NetBeans Scala Plugin: <https://java.net/projects/nbscala>
- IntelliJ IDEA Scala Plugin: <http://confluence.jetbrains.net/display/SCA/Scala+Plugin+for+IntelliJ+IDEA>
- IntelliJ IDEA's plugin is under active development, and so using the nightly build may give you additional functionality at the cost of some minor hiccups.
- Nika (11.x) Plugin Repository: <http://www.jetbrains.com/idea/plugins/scala-nightly-nika.xml>
- Leda (12.x) Plugin Repository: <http://www.jetbrains.com/idea/plugins/scala-nightly-leda.xml>
- IntelliJ IDEA Play plugin (available only for Leda 12.x): <http://plugins.intellij.net/plugin/?idea&pluginId=7080>
- ENSIME - Scala IDE Mode for Emacs: <https://github.com/aemoncannon/ensime>
(see below for ENSIME/Play instructions) -->
- Eclipse Scala IDE: <http://scala-ide.org/>
- NetBeans Scala プラグイン: <https://java.net/projects/nbscala>
- IntelliJ IDEA Scala プラグイン: <http://confluence.jetbrains.net/display/SCA/Scala+Plugin+for+IntelliJ+IDEA>
- IntelliJ IDEA のプラグインは活発に開発されているため、ナイトリービルドを使う事で、小さな問題と引き換えにより多くの機能を使えるようになります。
- Nika (11.x) プラグインリポジトリ: <http://www.jetbrains.com/idea/plugins/scala-nightly-nika.xml>
- Leda (12.x) プラグインリポジトリ: <http://www.jetbrains.com/idea/plugins/scala-nightly-leda.xml>
- IntelliJ IDEA Play プラグイン (Leda 12.x のみに対応): <http://plugins.intellij.net/plugin/?idea&pluginId=7080>
- ENSIME - Emacs 用の Scala IDE モード: <https://github.com/aemoncannon/ensime>
(see below for ENSIME/Play instructions)

&nbsp;

<!-- > **Next:** 
>
> – [[Play for Scala developers|ScalaHome]]
> – [[Play for Java developers|JavaHome]] -->
> **Next:** 
>
> – [[Play for Scala developers|ScalaHome]]
> – [[Play for Java developers|JavaHome]]