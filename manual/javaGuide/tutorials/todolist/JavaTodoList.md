<!--
# Your first Play application
-->
# 初めての Play アプリケーション

<!--
Let’s write a simple task list application with Play and deploy it to the cloud. This is a very small example which can be managed in a few hours.
-->
Play でシンプルな TODO 管理アプリケーションを構築し、クラウドにデプロイしましょう。このサンプルはとても小規模で、数時間でやり通すことができます。  

<!--
## Prerequisites
-->
## 前提条件

<!--
First of all, make sure that you have a [[working Play installation|Installing]]. You only need Java (version 6 minimum), and to unzip the Play binary package to start; everything is included.
-->
はじめに、 [[Play のインストールが成功している|Installing]] 事を確かめてください。 Java (バージョン 6 以上) のインストールと、Play のバイナリパッケージの unzip さえすれば始められます。

<!--
As we will use the command line a lot, it’s better to use a Unix-like OS. If you run a Windows system, it will also work fine; you’ll just have to type a few commands in the command prompt.
-->
多くのコマンドを使用するため、 Unix 系 OS を利用する方が効率が良いです。 もし Windows システムを動作させているなら、コマンドプロンプトにいくつかのコマンドを入力する事で、動作させる事も可能です。

<!--
You will of course need a text editor. If you are used-to a fully-featured Java IDE, such as Eclipse or IntelliJ, you can of course use it. However, with Play you can have fun working with a simple text editor like TextMate, Emacs or vi. This is because the framework manages compilation and the deployment process itself.
-->
もちろん、テキストエディタは必要です。もし Eclipse や IntelliJ のようなフル機能の Java の IDE に慣れているのなら、それらを使うことも可能です。しかし、 Playであれば、編集や開発プロセス自身をフレームワークが管理してくれるため、 Textmate, Emacs もしくは vi のようなシンプルなテキストエディタで楽しみながら作業する事が可能です。

<!--
> **Note:** Read more about [[Setting-up your preferred IDE | IDE]].
-->
> **補足:** 詳細については [[好きな IDE で開発する | IDE]] を参照して下さい。

<!--
## Project creation
-->
## プロジェクト作成

<!--
Now that Play is correctly installed, it’s time to create the new application. Creating a Play application is pretty easy and fully managed by the Play command line utility. This encourages a standard project layout across all Play applications.
-->
Play が正しくインストールされているとして、新しくアプリケーションを作成してみましょう。 Play アプリケーションを作成する事はとても簡単であり、 Play コマンドで全て管理されています。全ての Play アプリケーション間で標準のプロジェクト構成になります。

<!--
Open a new command line and enter:
-->
コマンドラインを開き以下のように入力してください:

```
$ play new todolist
```

<!--
The Play tool will ask you a few questions. Choose to create a **simple Java application** project template.
-->
いくつかの質問が表示されます。**simple Java application** を選択しましょう。

[[images/new.png]]

<!--
The `play new` command creates a new directory `todolist/` and populates it with a series of files and directories. The most important are as follows.
-->
`play new` コマンドが新しいディレクトリ `todolist/` を作成し、一連のファイル、ディレクトリを生成します、重要なファイルは以下の通りです:

<!--
* `app/` contains the application’s core, split between models, controllers and views directories. This is the directory where .java source files live.
* `conf/` contains all the application’s configuration files, especially the main `application.conf` file, the `routes` definition files and the `messages` files used for internationalization.
* `project/` contains the build scripts. The build system is based on sbt. But a new play application comes with a default build script that will just works fine for our application.
* `public/` contains all the publicly available resources, which includes JavaScript, stylesheets and images directories.
* `test/` contains all the application tests. Tests can be written as JUnit tests.
-->
* `app/` ディレクトリには models 、 controllers 、そして views ディレクトリに分かれたアプリケーションのコアが入っています。本ディレクトリには .java ソースファイルが入っています。
* `conf/` ディレクトリには全てのアプリケーションの設定ファイル (特にメインとなる `application.conf` ファイル、 `routes` 定義ファイル、国際化のための `messages` ファイル) が入っています。
* `project/` ディレクトリにはビルドスクリプトが入っています。ビルドシステムは sbt に基づいています。新しい Play アプリケーションはアプリケーションを正常動作させるデフォルトのビルドスクリプトが同梱されています。
* `public/` ディレクトリには全てのパブリックに利用可能なリソース (JavaScript 、スタイルシート、画像イメージ) が入っています。
* `test/` ディレクトリにはアプリケーションのテストが入っています。 テストは JUnit で書くことができます。

<!--
> Because Play uses UTF-8 as the single encoding, it’s very important that all text files hosted in these directories use this encoding. Make sure to configure your text editor accordingly. In the windows system the editor configuration must be ANSI encode
> **Note:** Read more about [[Anatomy of a Play application | Anatomy]].
-->
> Play では UTF-8 をエンコーディング形式として採用しており、上記のディレクトリ内に配置された全テキストファイルは本文字セットを使用してエンコードされます。テキストエディタに応じて設定を確認してください。Windows の場合はエディタの文字コード設定は ANSI にする必要があります。
> **補足:** 詳細については [[Play アプリケーションの構造 | Anatomy]] を参照して下さい。

<!--
## Using the Play console
-->
## Play コンソールの使用

<!--
Once you have an application created, you can run the Play console. Go to the new `todolist/` directory and run:
-->
一度アプリケーションを作成したら、 Play コンソールを実行できます。新しく作成された `todolist/` ディレクトリに行き、以下のコマンドを実行して下さい:

```
$ play
```

<!--
This launches the Play console. There are several things you can do from the Play console, but let’s start by running the application. From the console prompt, type `run`:
-->
このコマンドで Play コンソールが起動されます。 Play コンソールから実行できることはいくつかありますが、まずアプリケーションを実行させてみましょう。コンソールプロンプトから、 `run` とタイプしてください:

```
[todolist] $ run
```

[[images/run.png]]

<!--
Now the application is running in development mode. Open a browser at <http://localhost:9000/>:
-->
development モードで本アプリケーションが実行されています。ブラウザを開き、 <http://localhost:9000/> へアクセスしてください:

[[images/welcome.png]]

<!--
> **Note:** Read more about [[The Play Console|PlayConsole]].
-->
> **補足:** 詳細は [[Play 2.0 コンソールを使う|PlayConsole]] を確認してください。

<!--
## Overview
-->
## 概要

<!--
Let’s see how the new application can display this page.
-->
このアプリケーションがどうやってページを表示しているか確認して行きましょう。

<!--
The main entry point of your application is the `conf/routes` file. This file defines all of the application’s accessible URLs. If you open the generated routes file you will see this first _route_:
-->
本アプリケーションの主なエントリーポイントは `conf/routes` ファイルです。このファイルはアプリケーションがアクセス可能な URL の全てを定義しています。生成された routes ファイルを開けば、初期の _route_ を確認できます:

```
GET	/       controllers.Application.index()
```

<!--
That simply tells Play that when the web server receives a GET request for the `/` path, it must call the `controllers.Application.index()` method. 
-->
上記の記述は web サーバーが `/` パスへの GET リクエストを受信した時に、 `controllers.Application.index()` メソッドを呼び出すという事を簡単に説明しています。

<!--
Let’s see what the `controllers.Application.index` method looks like. Open the `todolist/app/controllers/Application.java` source file:
-->
どうやって `controllers.Application.index` メソッドが実行されるのかを確認しましょう。 `todolist/app/controllers/Application.java` ソースファイルを開いてください:

```
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
You see that `controllers.Application.index()` returns a `Result`. All action methods must return a `Result`, which represents the HTTP response to send back to the web browser.
-->
 `controllers.Application.index()` は `Result` を返す事が確認できます。全てのアクションメソッドは web ブラウザへの HTTP レスポンスを表現する `Result` を返す必要があります。

<!--
> **Note:** Read more about [[Actions|JavaActions]].
-->
> **補足:** アクションについての詳細は、 [[アクション、コントローラ、レスポンス|JavaActions]] を確認してください。

<!--
Here, the action returns a **200 OK** response with an HTML response body. The HTML content is provided by a template. Play templates are compiled to standard Java methods, here as `views.html.index.render(String message)`.
-->
ここでは、アクションは HTML コンテントを含む **200 OK** のレスポンスを返します。 HTML コンテントはテンプレートによって提供されます。 Play テンプレートは標準 Java メソッド、 `views.html.index.render(String message)` としてコンパイルされます。

<!--
This template is defined in the `app/views/index.scala.html` source file:
-->
本テンプレートは `app/views/index.scala.html` ソースファイル上で定義されます。

```
@(message: String)

@main("Welcome to Play") {
    
    @play20.welcome(message, style = "Java")
    
}
```

<!--
The first line defines the function signature. Here it takes a single `String` parameter. Then the template content mixes HTML (or any text-based language) with Scala statements. The Scala statements start with the special `@` character.
-->
最初の行は関数の仕様を定義します。ここでは、単一の `String` 型のパラメータを受け取ります。テンプレートの内容には Scala と HTML (もしくはテキストベースの言語) を一緒に記入しています。 Scala の式は特殊な `@` 文字から始まります。

> **Note:** Don’t worry about the template engine using Scala as its expression language. This is not a problem for a Java developer, and you can almost use it as if the language was Java. We explain the templating system in a bit more detail below.

<!--
## Development work-flow
-->
## 開発フロー

<!--
Now let’s make some modifications to the new application. In the `Application.java` change the content of the response:
-->
さて、新しいアプリケーションにいくつかの変更を加えていきましょう。 `Application.java` 内でレスポンスの内容を書き換えてみます:

```
public static Result index() {
  return ok("Hello world");
}
```

<!--
With this change, the `index` action will now respond with a simple `text/plain` **Hello world** response. To see this change, just refresh the home page in your browser:
-->
上記の変更によって `index` アクションはシンプルな `text/plain` の **Hello world** レスポンスを返すようになります。この変更を確かめるために、ブラウザ上でホームページを更新してみましょう:

[[images/hello.png]]

<!--
There is no need to compile the code yourself or restart the server to see the modification. It is automatically reloaded when a change is detected. But what happens when you make a mistake in your code? Let’s try:
-->
変更を確認するのにコードを明示的にコンパイルする必要もサーバーを再起動する必要もありません。変更が検出されると、自動的にリロードされます。もし、コードを誤って変更したらどうなるでしょう? やってみましょう:

```
public static Result index() {
  return ok("Hello world);
}
```

<!--
Now reload the home page in your browser:
-->
上記のように変更したら、ブラウザ上でホームページをリロードしてください:

[[images/error.png]]

<!--
As you can see, errors are beautifully displayed directly in your browser.
-->
確認された通り、ブラウザ上に直接エラーが表示されます。

<!--
## Preparing the application
-->
## アプリケーションの準備

<!--
For our todo list application, we need a few actions and the corresponding URLs. Let’s start by defining the **routes**. 
-->
本 TODO 管理アプリケーションのために、いくつかのアクションと一致する URL が必要になります。 **routes** ファイルを定義することから始めましょう。

<!--
Edit the `conf/routes` file:
-->
`conf/routes` ファイルを編集してください。

```
# Home page
GET     /                       controllers.Application.index()
                                
# Tasks          
GET     /tasks                  controllers.Application.tasks()
POST    /tasks                  controllers.Application.newTask()
POST    /tasks/:id/delete       controllers.Application.deleteTask(id: Long)
```

<!--
We create a route to list all tasks, and a couple of others to handle task creation and deletion. The route to handle task deletion defines a variable argument `id` in the URL path. This value is then passed to the `deleteTask` action method.
-->
全てのタスクを一覧する route 定義を作成し、タスクの作成と削除を処理する2つの定義を作成します。タスク削除を扱うための route には URL パス内に `id` 変数を定義しています。 `id` の値は `deleteTask` アクションメソッドへ渡されます。

<!--
Now if your reload in your browser, you will see that Play cannot compile your routes files:
-->
ブラウザをリロードすれば、 Play が routes ファイルをコンパイルできないことが確認できます。

[[images/routes.png]]

<!--
This is because the routes reference non-existent action methods. So let’s add them to the `Application.java` file:
-->
これは routes ファイルが参照しているアクションメソッドが存在していないことが原因です。 `Application.java` ファイルに追記していきましょう。

```
public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render("Your new application is ready."));
  }
  
  public static Result tasks() {
    return TODO;
  }
  
  public static Result newTask() {
    return TODO;
  }
  
  public static Result deleteTask(Long id) {
    return TODO;
  }
  
}
```

<!--
As you see we use `TODO` as result in our actions implementation. Because we don’t want to write the actions implementation yet, we can use the built-in `TODO` result that will return a `501 Not Implemented` response. 
-->
上記のコードでアクションの暫定的な実装をするために `TODO` という Result を使用しました。まだアクションの実装を書きたくない場合に、ビルドインの `TODO` という Result を使用することが可能です。このアクションからは `501 Not Implemented` HTTP レスポンスが返るようになっています。

<!--
You can try to access the <http://localhost:9000/tasks> to see that:
-->
確認するために、 <http://localhost:9000/tasks> にアクセスできるか試してみましょう。

[[images/todo.png]]

<!--
Now the last thing we need to fix before starting the action implementation is the `index` action. We want it to redirect automatically to the tasks list page:
-->
アクションの実装をはじめる前に修正が必要な最後の項目は `index` アクションです。タスクの一覧ページに自動リダイレクトするようにします:

```
public static Result index() {
  return redirect(routes.Application.tasks());
}
```

<!--
As you see we use `redirect` instead of `ok` to specify a `303 See Other` response type. We also use the reverse router to get the URL needed to fetch the `tasks` actions.
-->
見ての通り、 `303 See Other` の HTTP レスポンスを使用するために `ok` の代わりに `redirect` を使用しています。また、`tasks` アクションの呼び出しに必要な URL を生成するため、リバースルーターを使っています。

<!--
> **Note:** Read more about the [[Router and reverse router|JavaRouting]].
-->
> **補足:** 詳細については [[HTTP ルーティング|JavaRouting]] を参照してください。

<!--
## Prepare the `Task` model
-->
## `Task` モデルの準備

<!--
Before continuing the implementation we need to define what a `Task` looks like in our application. Create a `class` for it in the `app/models/Task.java` file:
-->
実装を続ける前にアプリケーション内で `Task` の見え方を定義する必要があります。 `class` を `app/models/Task.java` ファイルに作成してください:

```
package models;

import java.util.*;

public class Task {
    
  public Long id;
  public String label;
  
  public static List<Task> all() {
    return new ArrayList<Task>();
  }
  
  public static void create(Task task) {
  }
  
  public static void delete(Long id) {
  }
    
}
```

<!--
We have also created a bunch of static methods to manage `Task` operations. For now we wrote dummy implementation for each operation, but later in this tutorial we will write implementations that will store the tasks into a relational database.
-->
`Task` の操作を管理するための static メソッドも上記のように作成しました。ここでは各操作のダミー実装を記載しましたが、このチュートリアルの後半、リレーショナルデータベースに関する項目の中でタスクを保存する実装を追加していきます。

<!--
## The application template
-->
## アプリケーションテンプレート

<!--
Our simple application will use a single Web page containing both the tasks list and the task creation form. Let’s modify the `index.scala.html` template for that:
-->
本アプリケーションはタスクリストとタスク作成フォームの両方を持つ単一のウェブページを用います。`index.scala.html` テンプレートを変更していきましょう:

```
@(tasks: List[Task], taskForm: Form[Task])

@import helper._

@main("Todo list") {
    
    <h1>@tasks.size() task(s)</h1>
    
    <ul>
        @for(task <- tasks) {
            <li>
                @task.label
                
                @form(routes.Application.deleteTask(task.id)) {
                    <input type="submit" value="Delete">
                }
            </li>
        }
    </ul>
    
    <h2>Add a new task</h2>
    
    @form(routes.Application.newTask()) {
        
        @inputText(taskForm("label")) 
        
        <input type="submit" value="Create">
        
    }
    
}
```

<!--
We changed the template signature to take 2 parameters:
-->
2つのパラメータを受け取るように以下のテンプレートのシグネチャを変更しました:

<!--
- A list of tasks to display
- A task form
-->
- 表示用のタスクのリスト
- タスクのフォーム

<!--
We also imported `helper._` that give us the form creation helpers, typically the `form` function that creates the HTML `<form>` with filled `action` and `method` attributes, and the `inputText` function that creates the HTML input given a form field.
-->
フォーム作成用のヘルパー関数を提供する `helper._` をインポートしました。これにより、 `action` や `method` 属性がついた `<form>` タグ付きの HTML を生成する `form` 関数や入力用のフォーム HTML を作成する `inputText` 関数が使用できます。
    
<!--
> **Note:** Read more about the [[Templating system|JavaTemplates]] and [[Forms helper|JavaFormHelpers]].
-->
> **補足:** 詳細については [[テンプレートエンジン|JavaTemplates]] と [[フォームテンプレートヘルパーの利用|JavaFormHelpers]] を確認してください。

<!--
## The task form
-->
## タスクフォーム

<!--
A `Form` object encapsulates an HTML form definition, including validation constraints. Let’s create a form for our `Task` class. Add this to your `Application` controller:
-->
`Form` オブジェクトはバリデーションを含んだ HTML フォームの定義をカプセル化します。`Task` クラスのためにフォームを作成しましょう。`Application` コントローラに以下のコードを追加します。

```
static Form<Task> taskForm = Form.form(Task.class);
```

<!--
The type of `taskForm` is then `Form<Task>` since it is a form generating a simple `Task`.  
You also need to import `play.data.*` and `models.*`.
-->
`taskForm` の型はシンプルな `Task` を生成するフォームである事を表す `Form<Task>` 型になります。その他に `play.data.*` と `models.*` をインポートする必要があります。
    
<!--
We can add a constraint to the `Task` type using **JSR-303** annotations. Let’s make the `label` field required:
-->
**JSR-303** のアノテーションを使う事で `Task` クラスに制約を追加することができます。`label` フィールドを必須にしてみましょう。

```
package models;

import java.util.*;

import play.data.validation.Constraints.*;

public class Task {
    
  public Long id;
  
  @Required
  public String label;
  
  ...
```

<!--
> **Note:** Read more about the [[Form definitions|JavaForms]].
-->
> **補足:** 詳細は [[フォームの定義|JavaForms]] を参照してください。

<!--
## Rendering the first page
-->
## 最初のページをレンダリングする

<!--
Now we have all elements needed to display the application page. Let’s write the `tasks` action:
-->
現在、アプリケーションページを表示するために要求された全ての要素の用意ができました。 `tasks` アクションを実装しましょう:

```
public static Result tasks() {
  return ok(
    views.html.index.render(Task.all(), taskForm)
  );
}
```

<!--
It renders a **200 OK** result filled with the HTML rendered by the `index.scala.html` template called with the tasks list and the task form.
-->
本メソッドがタスクリストとタスクフォームを呼出し、 `index.scala.html` テンプレートによってレンダリングされた HTML を含む **200 OK** の結果を返します。

<!--
You can now try to access <http://localhost:9000/tasks> in your browser:
-->
ブラウザ内で <http://localhost:9000/tasks> にアクセスしてみましょう:

[[images/blank.png]]

<!--
## Handling the form submission
-->
## フォーム投稿処理

<!--
For now if we submit the task creation form, we still get the TODO page. Let’s write the implementation of the `newTask` action:
-->
現在では、タスク作成フォームを投稿した場合でも、まだ TODO ページが取得されます。 `newTask` アクションの実装をしていきましょう。

```
public static Result newTask() {
  Form<Task> filledForm = taskForm.bindFromRequest();
  if(filledForm.hasErrors()) {
    return badRequest(
      views.html.index.render(Task.all(), filledForm)
    );
  } else {
    Task.create(filledForm.get());
    return redirect(routes.Application.tasks());  
  }
}
```

<!--
We use `bindFromRequest` to create a new form filled with the request data. If there are any errors in the form, we redisplay it (here we use **400 Bad Request** instead of **200 OK**). If there are no errors, we create the task and then redirect to the tasks list.
-->
リクエストデータが入力されている新しいフォームを作成するために `bindFromRequest` を扱うことができます。もしフォーム内に何らかのエラーがあった場合、エラーを再表示します (ここでは、 **200 OK** の代わりに **400 Bad Request** を使用します) 。もし何のエラーも発生しなければ、タスクを作成し、タスクリストにリダイレクトします。

<!--
> **Note:** Read more about the [[Form submissions|JavaForms]].
-->
> **補足:** 詳細は [[フォーム送信を扱う|JavaForms]] を参考にしてください。

<!--
## Persist the tasks in a database
-->
## データベース内のタスクを永続化する

<!--
It’s now time to persist the tasks in a database to make the application useful. Let’s start by enabling a database in our application. 
-->
アプリケーションを使いやすくするため、データベースにタスクの情報を永続化するようにしましょう。本アプリケーション内でデータベースを利用可能にします。

<!--
For now we will use a simple in memory database using **H2**, follow the process described in the [[Accessing an SQL database|JavaDatabase]] page.
-->
それでは、[[SQL データベースへのアクセス|JavaDatabase]] ページに書かれているプロセスに従って、H2 というシンプルなインメモリデータベースを使っていきましょう。

<!--
No need to restart the server, refreshing the browser is enough to set up the database.
-->
サーバーを再起動する必要はありません、ブラウザを更新するだけでデータベースをセットアップします。

<!--
We will use **EBean** (Play's default ORM) in this tutorial to query the database. So you’ll have to enable it as well. Define a default Ebean server following instructions on [[Using the Ebean ORM page|JavaEbean]] page.
-->
これからチュートリアル内でデータベースを検索するために (Play のデフォルト ORM である) **Ebean** を使用します。使用するためには Ebean を有効にする必要があります。[[Ebean ORM を使う|JavaEbean]]ページの操作に従ってデフォルトの Ebean サーバーを定義します。

<!--
By doing this we create an Ebean server connected to the `default` datasource, managing all entities found in the `models` package. Now it’s time to transform our `Task` class to a valid EBean entity:
-->
これで、 `default` データソースに接続された Ebean サーバーが作成され、`models` パッケージ以下にある全てのエンティティを管理するようになります。`Task` クラスを正しい Ebean エンティティに変換しましょう。

```
package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class Task extends Model {

  @Id
  public Long id;
  
  @Required
  public String label;
  
  public static Finder<Long,Task> find = new Finder(
    Long.class, Task.class
  );
  
  ...
```

<!--
We made the `Task` class extend the `play.db.ebean.Model` super class to have access to Play built-in Ebean helper. We also added proper persistence annotations, and created a `find` helper to initiate queries.
-->
`Task` クラスを `play.db.ebean.Model` スーパークラスから継承するようにしました。これで Play 組み込みの Ebean ヘルパーにアクセスする事ができます。また永続化のためのアノテーションと、クエリを作成するための `find` ヘルパーを作成しました。

<!--
Let’s implement the CRUD operations:
-->
CRUD 操作のメソッドを実装しましょう。

```
public static List<Task> all() {
  return find.all();
}

public static void create(Task task) {
  task.save();
}

public static void delete(Long id) {
  find.ref(id).delete();
}
```

<!--
Now you can play again with the application, creating new tasks should work.
-->
本アプリケーションで遊んでみましょう、新しいタスク作成は動作すると考えられます。

[[images/filled.png]]

<!--
> **Note:** Read more about [[Ebean|JavaEbean]].
-->
> **補足:** 詳細は [[Ebean|JavaEbean]] を参照してください。

<!--
## Deleting tasks
-->
## タスクの削除

<!--
Now that we can create tasks, we need to be able to delete them. Very simple, we just need to finish the implementation of the `deleteTask` action:
-->
タスクを作成することができるようになった後はそれらのタスクを削除する事ができるようにする必要があります。 `deleteTask` アクションの実装を (とてもシンプルに) 終わらせるだけです。

```java
public static Result deleteTask(Long id) {
  Task.delete(id);
  return redirect(routes.Application.tasks());
}
```

<!--
## Deploying to Heroku
-->
## Heroku へのデプロイ

<!--
All features are completed. It’s time to deploy our application in production. Let’s deploy it to heroku. First you have to create a `Procfile` for Heroku in the root application directory:
-->
全ての機能が完成しました。本アプリケーションを成果物としてデプロイしましょう。 Heroku へデプロイします。はじめに `Procfile` を Heroku 用に作成する必要があります。 `Procfile` をアプリケーションの root ディレクトリに作成してください。

```
web: target/universal/stage/bin/{your project name} -Dhttp.port=${PORT} -DapplyEvolutions.default=true -Ddb.default.url=${DATABASE_URL} -Ddb.default.driver=org.postgresql.Driver
```

<!--
> **Note:** Read more about [[Deploying to Heroku|ProductionHeroku]].
-->
> **補足:** Heroku へのデプロイの詳細については [[Heroku へのデプロイ |ProductionHeroku]] を参照してください。

<!--
Using system properties we override the application configuration when running on Heroku. But since heroku provides a PostgreSQL database we’ll have to add the required driver to our application dependencies. 
-->
Heroku 上で起動する際に、アプリケーションの設定を上書きするためにシステムプロパティを使用します。 Heroku は PostgreSQL データベースを提供しているため、 要求されたドライバーをアプリケーションの依存関係に追加する必要があります。

Specify it in the `build.sbt` file:

```scala
libraryDependencies += "postgresql" % "postgresql" % "8.4-702.jdbc4"
```

<!--
> **Note:** Read more about [[Dependencies management|SBTDependencies]].
-->
> **補足:** 依存性の管理については [[依存性の管理|SBTDependencies]] を参照して下さい。

<!--
Heroku uses **git** to deploy your application. Let’s initialize the git repository:
-->
Heroku はアプリケーションのデプロイに **git** を使用します。まずは git リポジトリを初期化しましょう。

```
$ git init
$ git add .
$ git commit -m "init"
```

<!--
Now we can create the application on Heroku:
-->
これで Heroku 上にアプリケーションを作ることが出来ます。

```
$ heroku create --stack cedar

Creating warm-frost-1289... done, stack is cedar
http://warm-1289.herokuapp.com/ | git@heroku.com:warm-1289.git
Git remote heroku added
```

<!--
And then deploy it using a simple `git push heroku master`:
-->
さらに `git push heroku master` を使ってアプリケーションをデプロイします。

```
$ git push heroku master

Counting objects: 34, done.
Delta compression using up to 8 threads.
Compressing objects: 100% (20/20), done.
Writing objects: 100% (34/34), 35.45 KiB, done.
Total 34 (delta 0), reused 0 (delta 0)

-----> Heroku receiving push
-----> Scala app detected
-----> Building app with sbt v0.11.0
-----> Running: sbt clean compile stage
       ...
-----> Discovering process types
       Procfile declares types -> web
-----> Compiled slug size is 46.3MB
-----> Launching... done, v5
       http://8044.herokuapp.com deployed to Heroku

To git@heroku.com:floating-lightning-8044.git
* [new branch]      master -> master
```

<!--
Heroku will build your application and deploy it to a node somewhere on the cloud. You can check the state of the application’s processes:
-->
Heroku はアプリケーションを構築し、クラウド上のどこかのノードにデプロイします。アプリケーションのプロセス状態を確認できます。

```
$ heroku ps

Process       State               Command
------------  ------------------  ----------------------
web.1         up for 10s          target/start
```

<!--
It’s started, you can now open it in your browser. 
-->
起動したら、ブラウザで開いてみましょう。

<!--
> Your first application is now up and running in production!
-->
> あなたの初めてのアプリケーションがアップロードされ、稼働しています!


