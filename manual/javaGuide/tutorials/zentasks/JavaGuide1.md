<!--
# Starting up the project
-->
# プロジェクトの立ち上げ

<!--
## Introduction
-->
## はじめに

<!--
In this tutorial you will learn the Play Framework by coding a real web application, from start to finish.  In this application, we will try to use everything you would need in a real project, while introducing good practices for Play application development.
-->
このチュートリアルでは、実際の web アプリケーションを始めから終わりまでコーディングすることによって、Play フレームワークについて学びます。このアプリケーションでは、Play アプリケーション開発の優れたプラクティスを紹介しつつ、実際のプロジェクトで必要となるあらゆるものを使っていきます。

<!--
We have split the tutorial into several independent parts.  Each part will introduce more complex features, and provide everything that a real project needs: validation, error handling, a complete security framework, an automated test suite, a shiny web interface, an administration area etc.
-->
このチュートリアルは、いくつかの独立した部分に分かれています。それぞれの部分では、より複雑な機能を紹介し、実際のプロジェクトが必要とするすべてのこと: バリデーション、エラー処理、完成されたセキュリティフレームワーク、自動化されたテストスイート、きらびやかな web インタフェース、管理領域などを提供します。

<!--
> **All the code** included in this tutorial can be used for your projects.  We encourage you to copy and paste snippets of code or steal whole chunks.
-->
> このチュートリアルに含まれる **すべてのコード** は、あなたのプロジェクトに使用することができます。コードの断片をコピーアンドペーストするか、または全体をまるごと流用することを推奨します。

<!--
## The project
-->
## プロジェクト

<!--
We are going to create a task management system.  It's not a very imaginative choice but it will allow us to explore most of the functionality needed by a modern web application.
-->
これからタスク管理システムを作成します。これは、とても想像力に富んだ選択ではありませんが、モダンな web アプリケーションで必要な機能のほとんどの部分を試すことができます。

<!--
We will call this task engine project **ZenTasks**.
-->
このタスク管理プロジェクトを **ZenTasks** と呼ぶことにしましょう。

[[images/zentasks.png]]

<!--
> This tutorial is also distributed as a sample application. You can find the final code in the `samples/java/zentasks` directory of your Play installation.
-->
> このチュートリアルはサンプルアプリケーションとしても配布されています。最終的なコードは Play インストール先の `samples/java/zentasks` ディレクトリにあります。

<!--
## Prerequisites
-->
## 前提条件

<!--
First of all, make sure you have a working Java installation.  Play requires **Java 6 or later.**
-->
何よりも、まず Java がインストールされていることを確認してください。Play は **Java 6 以降** を必要とします。

<!--
As we will use the command line a lot, it's better to use a Unix-like OS.  If you run a Windows system, it will also work fine; you'll just have to type a few commands in the command prompt.
-->
私たちはコマンドラインを多用するので、Unix ライクな OS のほうが良いでしょう。もし Windows 上で動かす場合でも、問題なく動作します。ただコマンドプロンプトでいくつかのコマンドを実行しなければならないだけです。

<!--
We will assume that you already have knowledge of Java and Web development (especially HTML, CSS and Javascript). However you don't need to have a deep knowledge of all the JEE components.  Play is a 'full stack' Java framework and it provides or encapsulates all the parts of the Java API that you will need.  No need to know how to configure a JPA entity manager or deploy a JEE component.
-->
このチュートリアルは、あなたが Java と Web 開発 (特に HTML, CSS, および JavaScript) に関する知識を既に持つものとします。しかしながら、あらゆる JEE コンポーネントに関する深い知識は必要としません。Play は 'フルスタック' な Java フレームワークであり、必要な Java API のすべての部分を提供するか、またはカプセル化します。JPA エンティティマネージャを設定する方法や、あるいは JEE コンポーネントをデプロイする方法を知っている必要はありません。

<!--
You will, of course, need a text editor.  If you are accustomed to using a full featured Java IDE like Eclipse or IntelliJ you can, of course, use it.  However with Play you can have fun working with a simple text editor like Textmate, Emacs or VI.  This is because the framework manages the compilation and the deployment process itself, as we will soon see...
-->
もちろんテキストエディタが必要です。もし Eclipse や Netbeans のようなフル機能の Java IDE に慣れているなら、もちろんそれを使用できます。しかし、Play であれば、Textmate, Emacs または VI のようにシンプルなテキストエディタでも楽しく作業できます。これは、フレームワーク自身がコンパイルとデプロイを管理するからです。間もなくその様子をご覧に入れます...

<!--
Later in this tutorial we will use Lighttpd and MySql to show how to deploy a Play application in 'production' mode. But Play can work without these components so if you can't install them, it's not a problem.
-->
このチュートリアルの後半で、Lighttpd と MySql を使い、'production' モードの Play アプリケーションをデプロイする方法を示します。しかし、Play はこれらのコンポーネントなしでも動作することができるので、もしこれらをインストールできなくても問題ありません。

<!--
## Installation of the Play Framework
-->
## Play Framework のインストール

Installation is very simple.  Just download the latest binary package from the download page and unzip it to any path.

<!--
> If you're using Windows, it is generally a good idea to avoid space characters in the path, so for example `c:\play` would be a better choice than `c:\Documents And Settings\user\play.`
-->
> Windows を使用している場合、パスにスペースを含めないのは一般的に良い案です。例えば、 `c:\play` は `c:\Documents And Settings\user\play\` よりも良い選択です。

<!--
To work efficiently, you need to add the Play directory to your working path.  It allows you to just type `play` at the command prompt to use the Play utility.  To check that the installation worked, just open a new command line and type `play`; it should show you the Play basic usage help.
-->
効率的に作業するためには、作業パスに Play ディレクトリを加える必要があります。パスを追加することで、コマンドプロンプトでただ `play` とタイプすれば、play ユーティリティを使用できるようになります。正常にインストールが完了したことを確認するには、新しいコマンドラインを開いて  `play` とタイプしてください; このコマンドは play の基本的なヘルプを表示します。

<!--
## Project creation
-->
## プロジェクトの作成

<!--
Now that Play is correctly installed, it's time to create the task application.  Creating a Play application is pretty easy and fully managed by the Play command line utility.  That allows for standard project layouts between all Play applications.
-->
Play が正しくインストールされたので、いよいよタスクアプリケーションを作成します。Play アプリケーションの作成は、Play コマンドラインユーティリティによって完全に管理されており、とても簡単です。Play コマンドラインユーティリティは、すべての Play アプリケーション間において標準的なプロジェクトレイアウトを割り当てます。

<!--
Open a new command line and type:
-->
新しいコマンドラインを開いて、以下をタイプしてください:

```bash
$ play new zentasks
```

<!--
It will prompt you for the application full name.  Type **'ZenTasks'**.  It will then prompt you for a template to use.  We are creating a Java application, so type **2**.
-->
アプリケーションの完全な名前を入力するプロンプトが表示されます。 **'ZenTasks'** とタイプしてください。その後、使用するテンプレートを選択するプロンプトが表示されます。Java アプリケーションを作成するので、 **2** をタイプしてください。

<!--
> Whether you select Java or Scala now, you can always change it later.
-->
> ここで Java または Scala を選んでも、後からいつでも変更することができます。

[[images/new.png]]

<!--
The `play new` command creates a new directory `zentasks/` and populates it with a series of files and directories, the most important being:
-->
`play new` コマンドは `zentasks/` ディレクトリを新規に作成し、一連のファイルやディレクトリをここに追加します。以下は、もっとも重要なディレクトリです:

<!--
`app/` contains the core of the application, split between models, controllers and views directories.  It can contain other Java packages as well.
-->
`app/` ディレクトリはアプリケーションの中心部であり、モデル、コントローラ、およびビュー用のディレクトリに分けられています。 他の Java パッケージを含むこともできます。 

<!--
`conf/` contains all the configuration files for the application, especially the main `application.conf` file, the `routes` definition file and the `messages` files used for internationalization.
-->
`conf/` ディレクトリは、特に重要な `application.conf` ファイル、ルーティングを定義する `routes` ファイル、国際化のための `messages` ファイルなど、アプリケーションを設定する全てのファイルを保存します。

<!--
`public/` contains all the publicly available resources, which includes Javascript files, stylesheets and images directories.
-->
`public/` は JavaScript, スタイルシート、および画像ディレクトリを含む公的に利用可能なリソースを保存します。

<!--
`project/` contains the project build files, which is in particular where you can declare dependencies on other libraries and plugins for the Play Framework.
-->
`project/` は、プロジェクトのビルドファイル、特に Play Framework が依存する外部のライブラリやプラグインを定義することのできるファイルを保存します。

<!--
`test/` contains all the application tests.  Tests are written either as Java JUnit tests or as Selenium tests.
-->
`test/` には、すべてのアプリケーションテストを保存します。テストは Java の JUnit として書かれるか、または Selenium テストとして書かれます。

<!--
> Because **Play uses UTF-8** as the single encoding, it's very important that all text files hosted in these directories are encoded using this charset.  Make sure to configure your text editor accordingly.
-->
> **Play は UTF-8 を**唯一のエンコーディングとして使用するので、これらのディレクトリでホスティングされたすべてのテキストファイルが UTF-8 でエンコードされていることは非常に重要です。テキストエディタの設定がこれに従っていることを確認してください。

<!--
Now if you're a seasoned Java developer, you may wonder where all the .class files go.  The answer is nowhere: Play doesn't use any class files; instead it reads the java source files directly.  Under the hood we use the SBT compiler to compile Java sources on the fly.
-->
もし、あなたが熟練した Java 開発者であれば、すべての .class ファイルがどこに行ってしまったのか不思議に思うことでしょう。実は、どこにもありません: Play はいかなる class ファイルも使用せず、Java ソースファイルを直接読み込みます。舞台裏では、実行中の Java ソースをコンパイルするために SBT コンパイラを使用しています。

<!--
That allows two very important things in the development process.  The first one is that Play will detect changes you make to any Java source file and automatically reload them at runtime.  The second is that when a Java exception occurs, Play will create better error reports showing you the exact source code.
-->
これは、開発工程において 2 つの非常に重要なことを可能にします。最初の 1 つは、Play はあなたが Java ソースファイルに加えたいかなる変更をも検出して、実行時にそれを自動的にリロードするということです。 2 番目は、Java の例外が発生したとき、Play はソースコードそのものを示しながらより良いエラーレポートを作成するということです。

<!--
> In fact Play can keep a bytecode cache in the application `/target` directory, but only to speed up things between restart on large applications.  You can discard this cache using the `play clean` command if needed.
-->
> 実際のところ、Play はアプリケーションの `/target` ディレクトリにバイトコードのキャッシュを保持しますが、これは大きなアプリケーションの再起動を高速化するためだけのものです。必要であれば、 `play clean` コマンドを使うことでこのキャッシュを破棄することができます。

<!--
## Running the application
-->
## アプリケーションの実行

<!--
We can now test the newly created application.  Just return to the command line, go to the newly created `zentasks/` directory and type `play`.  You have now loaded the Play console.  From here, type `run`. Play will now load the application and start a web server on port 9000.
-->
ここまで来れば、新しく作成したアプリケーションを試してみることができます。コマンドラインに戻り、新たに作成された `zentasks/` ディレクトリに移動したら、 `play` とタイプしてください。Play コンソールが起動します。ここで `run` とタイプしてください。Play がアプリケーションをロードし、9000 番ポートで web サーバを起動します。

<!--
You can see the new application by opening a browser to <http://localhost:9000>.  A new application has a standard welcome page and just tells you that it was successfully created.
-->
ブラウザから <http://localhost:9000> を開くことで、新しいアプリケーションを閲覧することができます。新しいアプリケーションは、それが首尾よく作成されたこと示す標準のウェルカムページを表示します。

[[images/welcome.png]]

<!--
Let's see how the new application can display this page.
-->
新しいアプリケーションがどのようにしてこのページを表示するのかを見てみましょう。

<!--
The main entry point of your application is the `conf/routes` file.  This file defines all accessible URLs for the application.  If you open the generated routes file you will see this first 'route':
-->
アプリケーションのエントリーポイントは `conf/routes` ファイルです。このファイルはアプリケーション上のアクセス可能な URL をすべて定義します。 生成された routes ファイルを開くと、最初の 'route' が確認できるでしょう:

    GET     /                           controllers.Application.index()

<!--
That simply tells Play that when the web server receives a `GET` request for the `/` path, it must call the `controllers.Application.index()` Java method.
-->
この設定は、web サーバが `/` パスに対する `GET` リクエストを受け取った場合には `controllers.Application.index()` という Java メソッドをコールしなければならないことを Play に伝えています。

<!--
When you create standalone Java applications you generally use a single entry point defined by a method such as:
-->
スタンドアロンな Java アプリケーションを作成するとき、一般的に以下のようなメソッドを定義することで単一のエントリーポイントを設けます:

```java
public static void main(String[] args) {
  ...
} 
```

<!--
A Play application has several entry points, one for each URL.  We call these methods **'action'** methods. Action methods are defined in special classes that we call **'controllers'**.
-->
Play アプリケーションには、各 URL あたり 1 つのエントリーポイントがあります。 私たちは、これらのメソッドを **'アクション'** メソッドと呼びます。アクションメソッドは **'コントローラ'** と呼ぶ特別なクラスで定義されます。

<!--
Let's see what the `controllers.Application` controller looks like.  Open the `app/controllers/Application.java` source file:
-->
`controllers.Application` コントローラがどのようなものか見てみましょう。`app/controllers/Application.java` ソースファイルを開いてください:

```java
package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

  public static Result index() {
    return ok(index.render("Your new application is ready."));
  }

}
```

<!--
Notice that the controller classes extend the `play.mvc.Controller` class.  This class provides many useful methods for controllers, like the `ok()` method we use in the index action.
-->
コントローラクラスは `play.mvc.Controller` クラスを継承します。このクラスは index アクションで使用した `ok()` メソッドのような、コントローラ向けの便利なメソッドを提供します。

<!--
The index action is defined as a `public static Result` method.  This is how action methods are defined.  You can see that action methods are static, because the controller classes are never instantiated.  They are marked as public to authorize the framework to call them in response to a URL.  They always return `play.mvc.Result`, this is the result of running the action.
-->
index アクションは `public static Result` なメソッドとして定義されています。アクションメソッドはこのようにして定義されます。コントローラクラスは決してインスタンス化されないので、アクションメソッドは static です。アクションメソッドは、フレームワークがリクエストされた URL にレスポンスできるよう public として宣言されます。アクションメソッドは常にアクションの実行結果である `play.mvc.Result` を返します。

<!--
The default index action is simple: it calls the `render()` method on the `views.html.index` template, passing in the String `"Your new application is ready."`.  It then wraps this in an `ok()` result, and returns it.
-->
デフォルトの index アクションはシンプルです: `views.html.index` テンプレートの `render()` メソッドに文字列 `"Your new application is ready."` を引き渡してコールします。そして、これを `ok()` の結果としてラップし、返却します。

<!--
Templates are simple text files that live in the `/app/views` directory.  To see what the template looks like, open the `app/views/Application/index.scala.html` file:
-->
テンプレートは `/app/views` ディレクトリに存在するシンプルなテキストファイルです。テンプレートがどのようなものかを見るには、`app/views/Application/index.scala.html` ファイルを開いてください:

```html
@(message: String)

@main("Welcome to Play") {

    @play20.welcome(message, style = "Java")

}
```

<!--
The template content seems pretty light.  In fact, all you see are Scala template directives.
-->
テンプレートの内容はとても簡単に見えます。実際、ここにあるものはすべて Scala テンプレートディレクティブです。

<!--
The `@(message: String)` directive declares the arguments that this template accepts, in this case, it is a single parameter called `message` of type `String`.  The `message` parameter gets used later in the template.
-->
`@(message: String)` ディレクティブは、このテンプレートが受け付ける引数を宣言します。この場合、引数は `message` と呼ばれる `String` 型のひとつの変数です。この `message` 引数は後でテンプレート内で使用されます。

<!--
The `@play20.welcome()` directive is a call to the built in Play welcome template that generate the welcome message you saw in the browser.  You can see that it passes the `message` parameter that our arguments directive declared earlier.
-->
`@play20.welcome()` ディレクティブは、ブラウザに表示されたウェルカムメッセージを生成する組み込みの Play テンプレートを呼び出します。先に引数ディレクティブで宣言した`message` パラメータを引き渡していることが分かります。

<!--
The `@main()` directive is a call to another template called `main.scala.html`.  Both the `@play20.welcome()` and the `@main()` calls are examples of template composition.  Template composition is a powerful concept that allows you to create complex web pages by reusing common parts.
-->
`@main()` ディレクティブは、`main.scala.html` という別のテンプレートを呼び出します。`@play20.welcome()` と `@main()` は、いずれもテンプレート合成の使用例を示しています。テンプレートの合成は、共通化された部分を再利用することで複雑なウェブページを作成することができる強力な概念です。

<!--
Open the `app/views/main.scala.html` template:
-->
`app/views/main.scala.html` テンプレートを開いていてください:

```html
@(title: String)(content: Html)
<!DOCTYPE html>

<html>
    <head>
        <title>@title</title>
        <link rel="stylesheet" media="screen" 
            href="@routes.Assets.at("stylesheets/main.css")">
        <link rel="shortcut icon" type="image/png" 
            href="@routes.Assets.at("images/favicon.png")">
        <script src="@routes.Assets.at("javascripts/jquery-1.7.1.min.js")" 
            type="text/javascript"></script>
    </head>
    <body>
        @content
    </body>
</html>
```

<!--
Note the argument declaration, this time we are accepting a `title` parameter, and also a second argument called `content` of type `Html`.  The second argument is in its own set of braces, this allows the syntax we saw before in the `index.scala.html` template:
-->
引数の宣言に注目してください。ここでは、`title` パラメータと、第二引数として `Html` 型の `content` を受け取っています。第二引数をそれ自身の括弧内に置くことで、先に `index.scala.html` テンプレートで見た構文を利用することができます:

```html
@main("Welcome to Play") {
   ...
}
```

<!--
The `content` argument is obtained by executing the block inside the curly braces after the `@main` directive.  You can see `@content` is then inserted between the `<body>` tags, in this way we have used template composition to wrap content from one template in another template.
-->
`content` 引数は、`@main` ディレクティブに続く波括弧の中にあるブロックを実行することで取得されます。そして、`@content` は `<body>` タグの間に挿入されています。このようにして、あるテンプレートを別のテンプレートでラップする、テンプレート合成を使用しています。

<!--
We can try to edit the controller file to see how Play automatically reloads it.  Open the `app/controllers/Application.java` file in a text editor, and add a mistake by removing the trailing semicolon after the `ok()` call:
-->
コントローラファイルを編集することで、Play がどのようにして自動的にこれをロードするのか試してみることができます。 `app/controllers/Application.java` ファイルをテキストエディタで開いて、 `ok()` 呼び出しの後に続くセミコロンを削除することで間違いを埋め込んでください:

```java
public static Result index() {
  return ok(index.render("Your new application is ready."))
}
```

<!--
Go to the browser and refresh the page.  You can see that Play detected the change and tried to reload the `Application` controller.  But because you made a mistake, you get a compilation error.
-->
ブラウザに戻りページをリフレッシュしてください。Play が変更を検出して `Application` コントローラをリロードしようとする様子を見ることができます。しかし、間違いがあるためコンパイルエラーが発生します。

[[images/compileerror.png]]

<!--
Ok, let's correct the error, and make a real modification:
-->
OK, エラーを修正して、実際の変更を加えましょう:


```java
public static Result index() {
  return ok(index.render("ZenTasks will be here"));
}
```

<!--
This time, Play has correctly reloaded the controller and replaced the old code in the JVM.  The heading on the page now contains the updated text that you modified.
-->
今度は、Play は適切にコントローラをリロードし、JVM 上の古いコードを置き換えました。 ページの表題には変更した後の文字が表示されています。

<!--
Now edit the `app/views/Application/index.scala.html` template to replace the welcome message:
-->
ここで、`app/views/Application/index.scala.html` テンプレートを編集してウェルカムメッセージを置き換えてみましょう:

```html
@(message: String)

@main("Welcome to Play") {

  <h1>@message</h1>

}
```

<!--
Like for the Java code changes, just refresh the page in the browser to see the modification.
-->
Java コードの変更と同じように、変更内容を確認するには、ただブラウザでページをリフレッシュするだけです。

<!--
> We will now start to code the tasks application.  You can either continue to work with a text editor or open the project in a Java IDE like Eclipse or Netbeans.  If you want to set up a Java IDE, please check [[this page|IDE]].
-->
> いよいよタスクアプリケーションのコーディングを始めました。このままテキストエディタで作業し続けることも、Eclipse や NetBeans のような Java IDE でプロジェクトを開くことも可能です。Java IDE をセットアップする場合は、 [[このページ|IDE]] を確認してください。

<!--
One more thing before starting to code.  For the task engine, we will need a database.  For development purposes, Play comes with a standalone SQL database management system called HSQLDB.  This is the best way to start a project before switching to a more robust database if needed.  You can choose to have either an in-memory database or a filesystem database that will keep your data between application restarts.
-->
コーディングを始める前にもうひとつ。ブログエンジンにはデータベースが必要です。開発目的のために Play には HSQLDB と呼ばれるスタンドアロンの SQL データベース管理システムが付属しています。より堅牢なデータベースが必要になって切り替える前までは、これが開発を開始するにはベストな方法です。インメモリデータベースか、またはアプリケーションを再起動してもデータを保持するファイルシステムデータベースのいずれかを選ぶこともできます。

<!--
At the beginning, we will do a lot of testing and changes in the application model.  For that reason, it's better to use an in-memory database so we always start with a fresh data set.
-->
初めのうちは、アプリケーションモデルを何度もテストし、変更します。このため、常にフレッシュなデータセットで起動するインメモリデータベースはより良い方法です。

<!--
Follow instructions to setup an in-memory H2 database on [[Accessing an SQL database|JavaDatabase]] page.
-->
[[SQL データベースアクセス|JavaDatabase]] ページの説明に従って、インメモリ H2 データベースを設定してください。

<!--
You can easily set up any JDBC compliant database and even configure the connection pool, but for now we'll keep it at this.  Additionally, we need to enable Ebean.
Define a default Ebean server following instructions on [[Using the Ebean ORM page|JavaEbean]] page.
-->
どのような JDBC 対応データベースについても、そしてコネクションプールの設定すらも容易に構成することができますが、今はここまでにしておきます。加えて、Ebean を有効にする必要があります。
[[Ebean ORM を使う|JavaEbean]] ページの説明に従って、デフォルトの Ebean サーバを定義してください。

<!--
## Using a version control system (VCS) to track changes
-->
## バージョン管理システム (VCS) を使った変更管理

<!--
When you work on a project, it's highly recommended to store your source code in a VCS.  It allows you to revert to a previous version if a change breaks something, work with several people and give access to all the successive versions of the application.  Of course, you can use any VCS to store your project, but here we will use Git as an example.  Git is a distributed source version control system, and Play has built in support for configuring a Play application inside a Git repository.
-->
プロジェクトに従事する場合、ソースコードをバージョン管理システム (VCS) に保存することを強くお勧めします。VCS は、変更が何かを壊した場合に以前のバージョンに戻ることや、複数人で作業すること、そしてアプリケーションの連続したバージョンすべてにアクセスすることを可能にします。もちろん、プロジェクトを保存するには、どのような VCS を使用することができますが、ここでは例として Git を使用します。Git は分散バージョン管理システムで、Play には Play アプリケーションを Git リポジトリ内で構成するサポートが組み込まれています。

<!--
Installing Git is out of the scope of this tutorial but it is very easy on any system.  Once you have a working installation of Git, go to the `zentasks/` directory and init the application versioning by typing:
-->
Git のインストールはこのチュートリアルの範囲を超えていますが、どのようなシステム上においても非常に簡単です。Git をインストールしたら、`zentasks/` ディレクトリに戻り、以下をタイプしてアプリケーションのバージョン管理を初期化してください:

```bash
$ git init
```

<!--
Now add the root directory to the repository.  You don't need to worry about ignoring any files, Play has already automatically generated a `.gitignore` file that contains the appropriate list of files to ignore:
-->
これでリポジトリに対するルートディレクトリが作成されます。Play が既に無視するファイルの適切なリストを含む `.gitignore` ファイルを自動的に生成しているので、無視するファイルについて心配する必要はありません:

```bash
$ git add .
```

<!--
Finally you can commit your changes:
-->
最後に変更をコミットします:

```bash
$ git commit -m "ZenTasks initial commit":
```

<!--
Our initial version is committed, and we have a solid foundation for our project.
-->
最初のバージョンがコミットされ、プロジェクトのための信頼できる基盤ができました。

<!--
> Go to the [[next part|JavaGuide2]]
-->
> [[次章|JavaGuide2]] に進みましょう
