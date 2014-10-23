<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
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

```
[my-first-app] $ eclipse
```

<!--
If you want to grab the available source jars (this will take longer and it's possible a few sources might be missing):
-->
もし取得可能なjarのソースを持ってくる場合は以下のようにします。(これはいくつかのソースが見つからない可能性があるので、時間がかかるかもしれません。)

```
[my-first-app] $ eclipse with-source=true
```

<!--
> Note if you are using sub-projects with aggregate, you would need to set `skipParents` appropriately in `build.sbt`:
-->
> サブプロジェクトを集める場合、`build.sbt`にある`skipParents`を以下のように設定する必要があるので注意してください。

```
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys

EclipseKeys.skipParents in ThisBuild := false
```

<!--
or from the play console, type:
-->
あるいは play コンソールから以下のようにタイプします:

``` 
[my-first-app] $ eclipse skip-parents=false
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
To debug, start your application with `activator -jvm-debug 9999 run` and in Eclipse right-click on the project and select **Debug As**, **Debug Configurations**. In the **Debug Configurations** dialog, right-click on **Remote Java Application** and select **New**. Change **Port** to 9999 and click **Apply**. From now on you can click on **Debug** to connect to the running application. Stopping the debugging session will not stop the server.
-->
デバッグを開始するには`activator -jvm-debug 9999 run`を実行し、アプリケーションを起動します。そして、Eclipseでプロジェクトを右クリックし、**Debug As**、**Debug Configurations**を選びます。**Debug Configurations**のダイアログ画面で**Remote Java Application**を右クリックし、**New**を選びます。そして、**Port**を9999に変更し、**Apply**をクリックします。これで実行中のアプリケーションに接続するために**Debug**をクリックできるようになります。デバッグを中止してもサーバは終了しません。

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

<!--Intellij IDEA lets you quickly create a Play application without using a command prompt. You don't need to configure anything outside of the IDE, the SBT build tool takes care of downloading appropriate libraries, resolving dependencies and building the project.
-->
IntelliJ IDEAはコマンドプロンプトを使用せずに新しいPlayアプリケーションを素早く作成させてくれます。IDEの外部での設定なしに、SBTビルドツールが必要なライブラリをダウンロードし、依存性を解決し、プロジェクトをビルドするまで面倒を見てくれます。

<!--
Before you start creating a Play application in IntelliJ IDEA, make sure that the latest Scala (if you develop with Scala) and 
Play 2 plugins are enabled in IntelliJ IDEA.
-->
PlayアプリケーションをIntelliJ IDEAで作成する前に、最新のScala(もしScalaで開発する場合)とPlay 2プラグインがIntelliJ IDEAで有効になっているか確認しましょう。

<!--
To create a Play application:
-->
Playアプリケーションを作るには:

<!--
- Open ***New Project*** wizard, select ***Play 2.x*** under ***Scala*** section and click ***Next***.
- Enter your project's information and click ***Finish***.
-->
- ***New Project***ウィザードを開き、***Scala***セクションにある***Play 2.x***を選択し***Next***をクリックする。
- プロジェクトの情報を入力し、***Finish***をクリックする。

<!--
IntelliJ IDEA creates an empty application using SBT.
-->
IntelliJ IDEAはSBTを使って空のアプリケーションを作ることができます。

<!--
You can also import an existing Play project.
-->
今あるPlayのプロジェクトをインポートすることも可能です。

<!--
To import a Play project:
- Open Project wizard, select ***Import Project***.
- In the window that opens, select a project you want to import and click ***OK***.
- On the next page of the wizard, select ***Import project from external model*** option, choose ***SBT project*** and click ***Next***. 
- On the next page of the wizard, select additional import options and click ***Finish***. 
-->

Playのプロジェクトをインポートするには:

- プロジェクトウィザードを開き、***Import Project***を選びます。
- 表示されたウィンドウの中に表示されている、インポートしたいプロジェクトを選択し***OK***をクリックします。
- ウィザードの次のページで***Import project from external model***オプションを選択し、***SBT project***を選択し、***Next***をクリックします。
- ウィザードの次のページで追加のインポートオプションを選択し、***Finish***をクリックします。

<!--
Check the project's structure, make sure all necessary dependencies are downloaded.
You can use code assistance, navigation and on-the-fly code analysis features.
-->
プロジェクトの構造をチェックし、必要な依存関係にあるものが全てダウンロードされているか確認してください。
コードアシスタンスを案内やコード解析フィーチャーに使用することができます。

<!--
You can run the created application and view the result in the default browser `http://localhost:9000`. 
-->
作成済みのアプリケーションを実行することができ、結果をデフォルトブラウザ上の`http://localhost:9000`で見ることができます。

<!--
To run a Play application:
-	In the project tree, right-click the application.
-	From the list in the context menu, select ***Run Pla2 App***.
-->
Playアプリケーションを実行するには:

- プロジェクトツリーでアプリケーションを右クリックする。
- コンテキストメニューメニューの中から***Run Play2 App***を選択する。

<!--
You can easily start a debugger session for a Play application using default Run/Debug Configuration settings.
-->
Playアプリケーションのデバッガセッションは標準のRun/Debug Configuration settingを使えば簡単に開始することができます。

<!--
For more detailed information, see the Play Framework 2.x tutorial at the following URL:
-->
もっと詳細な情報は以下のPlay Framework 2.x チュートリアルをご覧ください。

<http://confluence.jetbrains.com/display/IntelliJIDEA/Play+Framework+2.0> 


## Netbeans

<!--
### Generate Configuration
-->
### 設定ファイルの生成

<!--
Play does not have native Netbeans project generation support at this time.
-->
現在、PlayはNetbeansプロジェクトの生成をサポートしていません。


## ENSIME

<!--
### Install ENSIME
-->
### ENSIME のインストール

<!--
Follow the installation instructions at <https://github.com/aemoncannon/ensime>
-->
<http://github.com/aemoncannon/ensime> の手順に従ってインストールします。

<!--
### Generate configuration
-->
### 設定ファイルの生成

<!--
Edit your project/plugins.sbt file, and add the following line (you should first check <https://github.com/ensime/ensime-sbt-cmd> for the latest version of the plugin):
-->
project/plugins.sbtファイルを編集し、以下の行を追加します。(まず、プラグインのバージョンが最新かどうかここで確認する必要があります。<https://github.com/ensime/ensime-sbt-cmd>):

```
addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.1.3")
```

<!--
Start Play:
-->
Play を起動します:

```
$ activator
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

<!--
Check out the ENSIME manual at <http://ensime.github.io/ensime-src/index.html>.
If you have questions, post them in the ensime group at <https://groups.google.com/forum/?fromgroups=#!forum/ensime>.
-->
このENSIMEマニュアル<http://ensime.github.io/ensime-src/index.html>をチェックしてください。
疑問があればensimeグループ<https://groups.google.com/forum/?fromgroups=#!forum/ensime>に投稿してください。

<!--
## All Scala Plugins if needed
-->
## Scala プラグインが必要な場合は

<!--
Scala is a newer programming language, so the functionality is provided in plugins rather than in the core IDE.
-->
Scala は新しいプログラミング言語です。そのため、機能は IDE のコアではなくプラグインとして組み込まれています。

<!--
- Eclipse Scala IDE: <http://scala-ide.org/>
- NetBeans Scala Plugin: <https://java.net/projects/nbscala>
- IntelliJ IDEA Scala Plugin: <http://confluence.jetbrains.net/display/SCA/Scala+Plugin+for+IntelliJ+IDEA>
- IntelliJ IDEA's plugin is under active development, and so using the nightly build may give you additional functionality at the cost of some minor hiccups.
- Nika (11.x) Plugin Repository: <http://www.jetbrains.com/idea/plugins/scala-nightly-nika.xml>
- Leda (12.x) Plugin Repository: <http://www.jetbrains.com/idea/plugins/scala-nightly-leda.xml>
- IntelliJ IDEA Play plugin (available only for Leda 12.x): <http://plugins.intellij.net/plugin/?idea&pluginId=7080>
- ENSIME - Scala IDE Mode for Emacs: <https://github.com/aemoncannon/ensime>
(see below for ENSIME/Play instructions)
-->
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

> **Next:** 
>
> – [[Play Tutorials|Tutorials]]
