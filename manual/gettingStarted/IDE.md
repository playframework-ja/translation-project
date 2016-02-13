<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
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

<!--
## Eclipse
-->
## Eclipse

<!--
### Setup sbteclipse
-->
### sbteclipse の設定

<!--
Play requires [sbteclipse](https://github.com/typesafehub/sbteclipse) 4.0.0 or newer.  Append the following to project/plugins.sbt:
-->
Play には 4.0.0 か、それより新しい [sbteclipse](https://github.com/typesafehub/sbteclipse) が必要です。project/plugins.sbt に以下を追記します:

```scala
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")
```

<!--
You must `compile` your project before running the `eclipse` command. You can force compilation to happen when the `eclipse` command is run by adding the following setting to build.sbt:
-->
`eclipse` コマンドを実行する前にプロジェクトを `compile` する必要があります。以下を build.sbt に追加することで、`eclipse` コマンドが実行されたときに強制的にコンパイルを実行することができます:

```scala
// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)
```

<!--
If you have Scala sources in your project, you will need to install [Scala IDE](http://scala-ide.org/).
-->
プロジェクト内に Scala ソースがある場合、[Scala IDE](http://scala-ide.org/) をインストールしている必要があります。

<!--
If you do not want to install Scala IDE and have only Java sources in your project, then you can set the following:
-->
プロジェクト内は Java ソースのみで Scala IDE をインストールしたくない場合は、以下のように設定することができます:

```scala
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  // Use .class files instead of generated .scala files for views and routes 
```

<!--
### Generate configuration
-->
### 設定ファイルの生成

<!--
Play provides a command to simplify [Eclipse](https://eclipse.org/) configuration. To transform a Play application into a working Eclipse project, use the `eclipse` command:
-->
Play は単純な [Eclipse](https://eclipse.org/) の設定のためのコマンドを提供します。Play アプリケーションを Eclipse のプロジェクトに変換するには `eclipse` コマンドを使います:

```bash
[my-first-app] $ eclipse
```

<!--
If you want to grab the available source jars (this will take longer and it's possible a few sources might be missing):
-->
もし取得可能な jar のソースを持ってくる場合は以下のようにします (これには時間がかかるでしょうし、いくつかのソースが見つからない場合もあります)。

```bash
[my-first-app] $ eclipse with-source=true
```

<!--
> Note if you are using sub-projects with aggregate, you would need to set `skipParents` appropriately in `build.sbt`:
-->
> もしサブプロジェクトを使っている場合は、 `build.sbt` にある `skipParents` を以下のように適切に設定する必要があるので注意してください:

```scala
EclipseKeys.skipParents in ThisBuild := false
```

<!--
or from the play console, type:
-->
あるいは play コンソールから以下のようにタイプします:

```bash
[my-first-app] $ eclipse skip-parents=false
```

<!--
You then need to import the application into your Workspace with the **File/Import/General/Existing project…** menu (compile your project first).
-->
その後、 **File/Import/General/Existing project…** メニューを使用してワークスペースにアプリケーションをインポートする必要があります（最初にあなたのプロジェクトをコンパイルします）。

[[images/eclipse.png]] 

<!--
To debug, start your application with `activator -jvm-debug 9999 run` and in Eclipse right-click on the project and select **Debug As**, **Debug Configurations**. In the **Debug Configurations** dialog, right-click on **Remote Java Application** and select **New**. Change **Port** to 9999 and click **Apply**. From now on you can click on **Debug** to connect to the running application. Stopping the debugging session will not stop the server.
-->
デバッグを開始するには `activator -jvm-debug 9999 run` を実行し、アプリケーションを起動します。そして、 Eclipse でプロジェクトを右クリックし、 **Debug As** 、 **Debug Configurations** を選びます。 **Debug Configurations** のダイアログ画面で **Remote Java Application** を右クリックし、 **New** を選びます。そして、 **Port** を 9999 に変更し、 **Apply** をクリックします。これで実行中のアプリケーションに接続するために **Debug** をクリックできるようになります。デバッグを中止してもサーバは終了しません。

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

<!--
## IntelliJ
-->
## IntelliJ

<!--
[Intellij IDEA](https://www.jetbrains.com/idea/) lets you quickly create a Play application without using a command prompt. You don't need to configure anything outside of the IDE, the SBT build tool takes care of downloading appropriate libraries, resolving dependencies and building the project.
-->
[Intellij IDEA](https://www.jetbrains.com/idea/) はコマンドプロンプトを使用せずに新しい Play アプリケーションを素早く作成させてくれます。 IDE の外部での設定なしに、SBT ビルドツールが必要なライブラリをダウンロードし、依存性を解決し、プロジェクトをビルドするまで面倒を見てくれます。

<!--
Before you start creating a Play application in IntelliJ IDEA, make sure that the latest [Scala Plugin](http://www.jetbrains.com/idea/features/scala.html) is installed and enabled in IntelliJ IDEA. Even if you don't develop in Scala, it will help with the template engine and also resolving dependencies.
-->
IntelliJ IDEA を使って Play アプリケーションを作り始める前に、[Scala プラグイン](http://www.jetbrains.com/idea/features/scala.html) がインストールされ、IntelliJ IDEA で使えるようになっていることを確認してください。Scala を使って開発しない場合でも、このプラグインはテンプレートエンジンの役に立ちますし、依存性も解決してくれます。

<!--
To create a Play application:
-->
Play アプリケーションは以下のように作ります:

<!--
1. Open ***New Project*** wizard, select ***Play 2.x*** under ***Scala*** section and click ***Next***.
2. Enter your project's information and click ***Finish***.
-->
1. ***New Project*** ウィザードを開き、 ***Scala*** セクションにある ***Play 2.x*** を選択し ***Next*** をクリックします。
2. プロジェクトの情報を入力し、 ***Finish*** をクリックします。

<!--
IntelliJ IDEA creates an empty application using SBT.
-->
IntelliJ IDEA は SBT を使って空のアプリケーションを作ることができます。

<!--
Currently, for Play 2.4.x, instead of using the IntelliJ wizard to create a new project, we suggest that you create it using Activator and then Import it to IntelliJ.
-->
現在の　Play 2.4.x では、IntelliJ のウィザードを使って新しいプロジェクトを作る代わりに、Activator を使ってプロジェクトを作り、IntelliJ にインポートすることを推奨しています。

<!--
You can also import an existing Play project.
-->
既存の Play プロジェクトをインポートすることも可能です。

<!--
To import a Play project:
-->
Play のプロジェクトは以下のようにインポートします:

<!--
1. Open Project wizard, select ***Import Project***.
2. In the window that opens, select a project you want to import and click ***OK***.
3. On the next page of the wizard, select ***Import project from external model*** option, choose ***SBT project*** and click ***Next***. 
4. On the next page of the wizard, select additional import options and click ***Finish***. 
-->
1. プロジェクトウィザードを開き、 ***Import Project*** を選びます。
2. 表示されたウィンドウの中に表示されている、インポートしたいプロジェクトを選択し ***OK*** をクリックします。
3. ウィザードの次のページで ***Import project from external model*** オプションを選択し、 ***SBT project*** を選択し、 ***Next***をクリックします。
4. ウィザードの次のページで追加のインポートオプションを選択し、 ***Finish*** をクリックします。

<!--
Check the project's structure, make sure all necessary dependencies are downloaded. You can use code assistance, navigation and on-the-fly code analysis features.
-->
プロジェクトの構造をチェックし、必要な依存関係にあるものが全てダウンロードされているか確認してください。コードアシスタントやナビゲーション、そして、その場で動作するコード解析機能を使用することができます。

<!--
You can run the created application and view the result in the default browser `http://localhost:9000`. To run a Play application:
-->
作成済みのアプリケーションを実行することができ、結果をデフォルトブラウザ上の `http://localhost:9000` で見ることができます。Play アプリケーションは以下のように実行します:

<!--
1. Create a new Run Configuration -- From the main menu, select Run -> Edit Configurations
2. Click on the + to add a new configuration
3. From the list of configurations, choose "SBT Task"
4. In the "tasks" input box, simply put "run"
5. Apply changes and select OK.
6. Now you can choose "Run" from the main Run menu and run your application
-->
1. 新しい起動設定を作成します -- メインメニューから Run -> Edit Configurations を選択します
2. \+ をクリックして新しい設定を追加します
3. 設定リストから "SBT Task" を選択します
4. "tasks" 入力ボックス内に "run" を追加します
5. 変更を適用して OK を選択します
6. これでメインの起動メニューから "Run" を選んでアプリケーションを起動できます

<!--
You can easily start a debugger session for a Play application using default Run/Debug Configuration settings.
-->
Play アプリケーションのデバッガセッションは標準の Run/Debug Configuration setting を使えば簡単に開始することができます。

<!--
For more detailed information, see the Play Framework 2.x tutorial at the following URL:
-->
より詳しい情報は以下の Play Framework 2.x チュートリアルをご覧ください:

<https://confluence.jetbrains.com/display/IntelliJIDEA/Play+Framework+2.0> 

<!--
### Navigate from an error page to the source code
-->
### エラーページからソースコードへの移動

<!--
Using the `play.editor` configuration option, you can set up Play to add hyperlinks to an error page. Since then, you can easily navigate from error pages to IntelliJ, directly into the source code (you need to install the Remote Call <https://github.com/Zolotov/RemoteCall> IntelliJ plugin first).
-->
`play.editor` 設定オプションを使って、Play がエラーページにハイパーリンクを追加するよう設定することができます。これにより、エラーページから IntelliJ 内のソースコードに直接移動できるようになります (はじめに Remote Call <https://github.com/Zolotov/RemoteCall> IntelliJ プラグインをインストールする必要があります)。

<!--
Just install the Remote Call plugin and run your app with the following options:
`-Dplay.editor=http://localhost:8091/?message=%s:%s -Dapplication.mode=dev`
-->
普通に Remote Call プラグインをインストールして `-Dplay.editor=http://localhost:8091/?message=%s:%s -Dapplication.mode=dev` オプションを付けてアプリケーションを起動してください: 


<!--
## Netbeans
-->
## Netbeans

<!--
### Generate Configuration
-->
### 設定ファイルの生成

<!--
Play does not have native [Netbeans](https://netbeans.org/) project generation support at this time, but there is a Scala plugin for NetBeans which can help with both Scala language and SBT:
-->
Play は今のところ [Netbeans](https://netbeans.org/) のプロジェクト生成をサポートしていませんが、Scala 言語と SBT 両方を支援する NetBeans 用 Scala プラグインがあります:

<https://github.com/dcaoyuan/nbscala>

<!--
There is also a SBT plugin to create Netbeans project definition:
-->
Netbeans プロジェクト定義を生成する SBT プラグインもあります:

<https://github.com/dcaoyuan/nbsbt>

<!--
## ENSIME
-->
## ENSIME

<!--
### Install ENSIME
-->
### ENSIME のインストール

<!--
Follow the installation instructions at <https://github.com/ensime/ensime-emacs>.
-->
<https://github.com/ensime/ensime-emacs> の手順に従ってインストールします。

<!--
### Generate configuration
-->
### 設定ファイルの生成

<!--
Edit your project/plugins.sbt file, and add the following line (you should first check <https://github.com/ensime/ensime-sbt> for the latest version of the plugin):
-->
project/plugins.sbt ファイルを編集し、以下の行を追加します。(まず、プラグインのバージョンが最新かどうかここで確認する必要があります。<https://github.com/ensime/ensime-sbt>):

```scala
addSbtPlugin("org.ensime" % "ensime-sbt" % "0.1.5-SNAPSHOT")
```

<!--
Start Play:
-->
Play を起動します:

```bash
$ activator
```

<!--
Enter 'ensime generate' at the play console. The plugin should generate a .ensime file in the root of your Play project.
-->
Play コンソールから 'ensime generate' と入力します。このプラグインにより Play プロジェクトのルートに .ensime ファイルが作成されているはずです。

```bash
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
### 詳しい情報

<!--
Check out the ENSIME README at <https://github.com/ensime/ensime-emacs>. If you have questions, post them in the ensime group at <https://groups.google.com/forum/?fromgroups=#!forum/ensime>.
-->
この ENSIME マニュアル <https://github.com/ensime/ensime-emacs> をチェックしてください。
疑問があれば ensime グループ <https://groups.google.com/forum/?fromgroups=#!forum/ensime> に投稿してください。

<!--
## All Scala Plugins if needed
-->
## Scala プラグインが必要な場合

<!--
Scala is a newer programming language, so the functionality is provided in plugins rather than in the core IDE.
-->
Scala は新しいプログラミング言語です。そのため、機能は IDE のコアではなくプラグインとして組み込まれています。

<!--
1. Eclipse Scala IDE: <http://scala-ide.org/>
2. NetBeans Scala Plugin: <https://github.com/dcaoyuan/nbscala>
3. IntelliJ IDEA Scala Plugin: <http://confluence.jetbrains.net/display/SCA/Scala+Plugin+for+IntelliJ+IDEA>
4. IntelliJ IDEA's plugin is under active development, and so using the nightly build may give you additional functionality at the cost of some minor hiccups.
5. Nika (11.x) Plugin Repository: <https://www.jetbrains.com/idea/plugins/scala-nightly-nika.xml>
6. Leda (12.x) Plugin Repository: <https://www.jetbrains.com/idea/plugins/scala-nightly-leda.xml>
7. IntelliJ IDEA Play plugin (available only for Leda 12.x): <http://plugins.intellij.net/plugin/?idea&pluginId=7080>
8. ENSIME - Scala IDE Mode for Emacs: <https://github.com/aemoncannon/ensime>
(see below for ENSIME/Play instructions)
-->
1. Eclipse Scala IDE: <http://scala-ide.org/>
2. NetBeans Scala プラグイン: <https://github.com/dcaoyuan/nbscala>
3. IntelliJ IDEA Scala プラグイン: <http://confluence.jetbrains.net/display/SCA/Scala+Plugin+for+IntelliJ+IDEA>
4. IntelliJ IDEA のプラグインは活発に開発されているため、ナイトリービルドを使うことで、小さな問題と引き換えにより多くの機能を使えるようになります。
5. Nika (11.x) プラグインリポジトリ: <https://www.jetbrains.com/idea/plugins/scala-nightly-nika.xml>
6. Leda (12.x) プラグインリポジトリ: <https://www.jetbrains.com/idea/plugins/scala-nightly-leda.xml>
7. IntelliJ IDEA Play プラグイン (Leda 12.x のみに対応): <http://plugins.intellij.net/plugin/?idea&pluginId=7080>
8. ENSIME - Emacs 用の Scala IDE モード: <https://github.com/aemoncannon/ensime>
(ENSIME/Play の使い方は以下を参照してください)