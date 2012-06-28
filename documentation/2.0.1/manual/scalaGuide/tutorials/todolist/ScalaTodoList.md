<!--
# Your first Play application
-->
# 初めての Play アプリケーション

<!--
Let’s write a simple to do list application with Play 2.0 and deploy it to the cloud.
-->
Play 2.0 でシンプルな TODO 管理アプリケーションを構築し、クラウドにデプロイしましょう。

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
You will of course need a text editor. You can also use a Scala IDE such as Eclipse or IntelliJ if you like. However, with Play you can have fun working with a simple text editor like Textmate, Emacs or vi. This is because the framework manages the compilation and the deployment process itself.
-->
もちろん、テキストエディタは必要です。 Eclipse もしくは IntelliJ のようなお好みの Scala IDE を利用することもできます。しかし、 Playであれば、編集や開発プロセス自身をフレームワークが管理してくれるため、 Textmate, Emacs もしくは vi のようなシンプルなテキストエディタで楽しみながら作業する事が可能です。

<!--
## Project creation
-->
## プロジェクト作成

<!--
Now that Play is correctly installed, it’s time to create the new application. Creating a Play application is pretty easy and fully managed by the Play command line utility. That allows for standard project layouts between all Play applications.
-->
Play が正しくインストールされているとして、新しくアプリケーションを作成してみましょう。 Play アプリケーションを作成する事はとても簡単であり、 Play コマンドで全て管理されています。全ての Play アプリケーション間で標準のプロジェクト構成になります。

<!--
On the command line type:
-->
コマンドラインで以下のように入力してください:

```
$ play new todolist
```

<!--
It will prompt you for a few questions. Select the _Create a simple Scala application_ project template.
-->
いくつかの選択肢からアプリケーションを選択するように指示されます。 _Create a simple Scala application_ プロジェクトのテンプレートを選択しましょう。

[[images/new.png]]

<!--
The `play new` command creates a new directory `todolist/` and populates it with a series of files and directories, the most important being:
-->
`play new` コマンドが新しいディレクトリ `todolist/` を作成し、一連のファイル、ディレクトリを生成します、重要なファイルは以下の通りです:

<!--
- `app/` contains the application’s core, split between models, controllers and views directories. This is the directory where .scala source files live.
- `conf/` contains all the application’s configuration files, especially the main `application.conf` file, the `routes` definition files and the `messages` files used for internationalization.
- `project` contains the build scripts. The build system is based on sbt. But a new Play application comes with a default build script that will just work fine for our application.
- `public/` contains all the publicly available resources, which includes JavaScript, stylesheets and images directories.
- `test/` contains all the application tests. Tests are written as Specs2 specifications.
-->
- `app/` ディレクトリには models 、 controllers 、そして views ディレクトリに分かれたアプリケーションのコアが入っています。本ディレクトリには .scala ソースファイルが入っています。
- `conf/` ディレクトリには全てのアプリケーションの設定ファイル (特にメインとなる `application.conf` ファイル、 `routes` 定義ファイル、国際化のための `messages` ファイル) が入っています。
- `project` ディレクトリにはビルドスクリプトが入っています。ビルドシステムは sbt に基づいています。新しい Play アプリケーションはアプリケーションを正常動作させるデフォルトのビルドスクリプトが同梱されています。
- `public/` ディレクトリには全てのパブリックに利用可能なリソース (Javascript 、スタイルシート、画像イメージ) が入っています。
- `test/` ディレクトリにはアプリケーションのテストが入っています。 テストは Specs2 の仕様で書かれています。

<!--
> Because Play uses UTF-8 as single encoding, it’s very important that all text files hosted in these directories are encoded using this charset. Make sure to configure your text editor accordingly.
-->
> Play では UTF-8 をエンコーディング形式として採用しており、上記のディレクトリ内に配置された全テキストファイルは本文字セットを使用してエンコードされます。テキストエディタに応じて設定を確認してください。

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
The application is now running in development mode. Open a browser at [[http://localhost:9000/]]:
-->
development モードで本アプリケーションが実行されています。ブラウザを開き、 [[http://localhost:9000/]] へアクセスしてください:

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
GET	/       controllers.Application.index
```

<!--
That simply tells Play that when the web server receives a GET request for the / path, it must retrieve the `Action` to execute from the `controllers.Application.index` method. 
-->
上記の記述は web サーバーが / パスへの GET リクエストを受信した時に、 `controllers.Application.index` メソッドから実行するための `Action` を検索するという事を簡単に説明しています。

<!--
Let’s see how the `controllers.Application.index` method looks like. Open the `todolist/app/controllers/Application.scala` source file:
-->
どうやって `controllers.Application.index` メソッドが実行されるのかを確認しましょう。 `todolist/app/controllers/Application.scala` ソースファイルを開いてください:

```
package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
}
```

<!--
You see that `controllers.Application.index` returns an `Action` that will handle the request. An `Action` must return a `Result` that represents the HTTP response to send back to the web browser.
-->
 `controllers.Application.index` はリクエスト処理を扱う `Action` 型の戻り値を返す事が確認できます。 `Action` は web ブラウザへの HTTP レスポンスを表現する `Result` を返す必要があります。

<!--
> **Note:** Read more about [[Actions|ScalaActions]].
-->
> **補足:** Action についての詳細は、 [[アクション、コントローラ、レスポンス|ScalaActions]] を確認してください。

<!--
Here the action returns a **200 OK** response filled with HTML content. The HTML content is provided by a template. Play templates are compiled to standard Scala functions, here as `views.html.index(message: String)`.
-->
ここでは、アクションは HTML コンテントを含む **200 OK** のレスポンスを返します。 HTML コンテントはテンプレートによって提供されます。 Play テンプレートは標準 Scala 関数、 `views.html.index(message: String)` としてコンパイルされます。

<!--
This template is defined in the `app/views/index.scala.html` source file:
-->
本テンプレートは `app/views/index.scala.html` ソースファイル上で定義されます。

```
@(message: String)

@main("Welcome to Play 2.0") {
    
    @play20.welcome(message)
    
}
```

<!--
The first line defines the function signature. Here it takes a single `String` parameter. Then the template content mix HTML (or any text based language) with Scala statements. The Scala statements starts with the special `@` character.
-->
最初の行は関数の仕様を定義します。ここでは、単一の `String` 型のパラメータを受け取ります。テンプレートの内容には Scala と HTML (もしくはテキストベースの言語) を一緒に記入しています。 Scala の式は特殊な `@` 文字から始まります。

<!--
## Development workflow
-->
## 開発フロー

<!--
Now let’s make some modifications to the new application. In the `Application.scala` change the content of the response:
-->
さて、新しいアプリケーションにいくつかの変更を加えていきましょう。 `Application.scala` 内でレスポンスの内容を書き換えてみます:

```
def index = Action {
  Ok("Hello world")
}
```

<!--
With this change the **index** action will now respond with a simple `text/plain` **Hello world** response. To test this change, just refresh the home page in your browser:
-->
上記の変更によって **index** アクションはシンプルな `text/plain` の **Hello world** レスポンスを返すようになります。この変更を確かめるために、ブラウザ上でホームページを更新してみましょう:

[[images/hello.png]]

<!--
There is no need to compile the code yourself or restart the server to see the modification. It is automatically reloaded when a change is detected. But what happens when you make a mistake in your code?
-->
変更を確認するのにコードを明示的にコンパイルする必要もサーバーを再起動する必要もありません。変更が検出されると、自動的にリロードされます。もし、コードを誤って変更したらどうなるでしょう?

<!--
Let’s try:
-->
やってみましょう:

```
def index = Action {
  Ok("Hello world)
}
```

<!--
Now reload the home page in your browser:
-->
上記のように変更したら、ブラウザ上でホームページをリロードしてください:

[[images/error.png]]

<!--
As you see errors are beautifully displayed directly in your browser.
-->
確認された通り、ブラウザ上に直接エラーが表示されます。

<!--
## Preparing the application
-->
## アプリケーションの準備

<!--
For our to do list application, we need a few actions and the corresponding URLs. Let’s start by defining the **routes**. 
-->
本 TODO 管理アプリケーションのために、いくつかのアクションと一致する URL が必要になります。 **routes** ファイルを定義することから始めましょう。

<!--
Edit the `conf/routes` file:
-->
`conf/routes` ファイルを編集してください。

```
# Home page
GET     /                       controllers.Application.index
                                
# Tasks          
GET     /tasks                  controllers.Application.tasks
POST    /tasks                  controllers.Application.newTask
POST    /tasks/:id/delete       controllers.Application.deleteTask(id: Long)
```

<!--
We create a route to list all tasks, and a couple of others to handle task creation and deletion. The route to handle task deletion defines a variable argument `id` in the URL path. This value is then passed to the `deleteTask` method that will create the `Action`.
-->
全てのタスクを一覧する route 定義を作成し、タスクの作成と削除を処理する2つの定義を作成します。タスク削除を扱うための route には URL パス内に `id` 変数を定義しています。 `id` の値は `Action` を作成する `deleteTask` メソッドへ渡されます。

<!--
Now if you reload in your browser, you will see that Play cannot compile your `routes` file:
-->
ブラウザをリロードすれば、 Play が `routes` ファイルをコンパイルできないことが確認できます。

[[images/routes.png]]

<!--
This is because the new routes reference non-existent action methods. So let’s add them to the `Application.scala` file:
-->
これは新しい routes ファイルが参照しているアクションメソッドが存在していないことが原因です。 `Application.scala` ファイルに追記していきましょう。

```
object Application extends Controller {
  
  def index = Action {
    Ok("Hello world")
  }
  
  def tasks = TODO
  
  def newTask = TODO
  
  def deleteTask(id: Long) = TODO
  
}
```

<!--
As you see we use `TODO` to define our action implementations. Because we don’t want to write the action implementations yet, we can use the built-in `TODO` action that will return a `501 Not Implemented` HTTP response. 
-->
上記のコードでアクションの暫定的な実装をするために `TODO` という定義を使用しました。まだアクションの実装を書きたくない場合に、ビルドインの `TODO` アクションを使用することが可能です。このアクションからは `501 Not Implemented` HTTP レスポンスが返るようになっています。

<!--
You can try to access the [[http://localhost:9000/tasks]] to see that:
-->
確認するために、 [[http://localhost:9000/tasks]] にアクセスできるか試してみましょう。

[[images/todo.png]]

<!--
Now the last thing we need to fix before starting the action implementation is the `index` action. We want it to automatically redirect to the tasks list page:
-->
アクションの実装をはじめる前に修正が必要な最後の項目は `index` アクションです。タスクの一覧ページに自動リダイレクトするようにします:

```
def index = Action {
  Redirect(routes.Application.tasks)
}
```

<!--
As you can see, we use `Redirect` instead of `Ok` to specify a `303 See Other` HTTP response type. We also use the reverse router to get the URL needed to fetch the `tasks` actions.
-->
見ての通り、 `303 See Other` の HTTP レスポンスを使用するために `Ok` の代わりに `Redirect` を使用しています。また、`tasks` アクションの呼び出しに必要な URL を生成するため、リバースルーターを使っています。

<!--
> **Note:** Read more about the [[Router and reverse router|ScalaRouting]].
-->
> **補足:** 詳細については [[HTTPルーティング|ScalaRouting]] を参照してください。

<!--
## Prepare the `Task` model
-->
## `Task` モデルの準備

<!--
Before continuing the implementation we need to define what a `Task` looks like in our application. Create a `case class` for it in the `app/models/Task.scala` file:
-->
実装を続ける前にアプリケーション内で `Task` の見え方を定義する必要があります。 `case class` を `app/models/Task.scala` ファイルに作成してください:

```
package models

case class Task(id: Long, label: String)

object Task {
  
  def all(): List[Task] = Nil
  
  def create(label: String) {}
  
  def delete(id: Long) {}
  
}
```

<!--
We have also created a companion object to manage `Task` operations. For now we wrote a dummy implementation for each operation, but later in this tutorial we will write implementations that store the tasks in a relational database.
-->
`Task` の操作を管理するためのコンパニオンオブジェクトも上記のように作成しました。ここでは各操作のダミー実装を記載しましたが、このチュートリアルの後半、リレーショナルデータベースに関する項目の中でタスクを保存する実装を追加していきます。

<!--
## The application template
-->
## アプリケーションテンプレート

<!--
Our simple application will use a single web page that shows both the tasks list and the task creation form. Let’s modify the `index.scala.html` template for that:
-->
本アプリケーションはタスクリストとタスク作成フォームの両方を見せる単一のウェブページを用います。
`index.scala.html` テンプレートを変更していきましょう:

```
@(tasks: List[Task], taskForm: Form[String])

@import helper._

@main("Todo list") {
    
    <h1>@tasks.size task(s)</h1>
    
    <ul>
        @tasks.map { task =>
            <li>
                @task.label
                
                @form(routes.Application.deleteTask(task.id)) {
                    <input type="submit" value="Delete">
                }
            </li>
        }
    </ul>
    
    <h2>Add a new task</h2>
    
    @form(routes.Application.newTask) {
        
        @inputText(taskForm("label")) 
        
        <input type="submit" value="Create">
        
    }
    
}
```

<!--
We changed the template signature to take two parameters:
-->
2つのパラメータを受け取るように以下のテンプレートのシグネチャを変更しました:

<!--
- a list of tasks to display
- a task form.
-->
- 表示用のタスクのリスト
- タスクのフォーム

<!--
We also imported `helper._` that gives us the form creation helpers, typically the `form` function, which creates an HTML `<form>` with filled `action` and `method` attributes, and the `inputText` function that creates an HTML input for a form field.
-->
フォーム作成用のヘルパー関数を提供する `helper._` をインポートしました。これにより、 `action` や `method` 属性がついた `<form>` タグ付きの HTML を生成する `form` 関数や入力用のフォーム HTML を作成する `inputText` 関数が使用できます。
    
<!--
> **Note:** Read more about the [[Templating system|ScalaTemplates]] and [[Forms helper|ScalaFormHelpers]].
-->    
> **補足:** 詳細については [[テンプレートエンジン|ScalaTemplates]] と [[フォームテンプレートヘルパーの利用|ScalaFormHelpers]] を確認してください。

<!--
## The task form
-->
## タスクフォーム

<!--
A `Form` object encapsulates an HTML form definition, including validation constraints. Let’s create a very simple form in the `Application` controller: we only need a form with a single **label** field. The form will also check that the label provided by the user is not empty:
-->
`Form` オブジェクトはバリデーションを含んだ HTML フォームの定義をカプセル化します。本アプリケーションでは単一の **label** フィールドを持つフォームが必要なため、フォームを `Application` コントローラ内に作成してみましょう。フォームは以下のようにする事で、ユーザーによって入力された label が空ではないことをチェックします。

```
import play.api.data._
import play.api.data.Forms._

val taskForm = Form(
  "label" -> nonEmptyText
)
```

<!--
The type of `taskForm` is then `Form[String]` since it is a form generating a simple `String`. You also need to import some `play.api.data` classes.
-->
`taskForm` の型はシンプルな `String` を生成するフォームである事を表す `Form[String]` 型になります。 `play.api.data` 内にあるいくつかのクラスをインポートする必要があります。

<!--
> **Note:** Read more about the [[Form definitions|ScalaForms]].
-->
> **補足:** 詳細は [[フォームの定義|ScalaForms]] を参照してください。

<!--
## Rendering the first page
-->
## 最初のページをレンダリングする

<!--
Now we have all elements needed to display the application page. Let’s write the `tasks` action:
-->
現在、アプリケーションページを表示するために要求された全ての要素の用意ができました。 `tasks` アクションを実装しましょう:

```
import models.Task

def tasks = Action {
  Ok(views.html.index(Task.all(), taskForm))
}
```

<!--
This renders a **200 OK** result filled with the HTML rendered by the `index.scala.html` template called with the tasks list and the task form.
-->
本メソッドがタスクリストとタスクフォームを呼出し、 `index.scala.html` テンプレートによってレンダリングされた HTML を含む **200 OK** の結果を返します。

<!--
You can now try to access [[http://localhost:9000/tasks]] in your browser:
-->
ブラウザ内で [[http://localhost:9000/tasks]] にアクセスしてみましょう:

[[images/blank.png]]

<!--
## Handling the form submission
-->
## フォーム投稿処理

<!--
For now, if we submit the task creation form, we still get the TODO page. Let’s write the implementation of the `newTask` action:
-->
現在では、タスク作成フォームを投稿した場合でも、まだ TODO ページが取得されます。 `newTask` アクションの実装をしていきましょう。

```
def newTask = Action { implicit request =>
  taskForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(Task.all(), errors)),
    label => {
      Task.create(label)
      Redirect(routes.Application.tasks)
    }
  )
}
```

<!--
To fill the form we need to have the `request` in the scope, so it can be used by `bindFromRequest` to create a new form filled with the request data. If there are any errors in the form, we redisplay it (here we use **400 Bad Request** instead of **200 OK**). If there are no errors, we create the task and then redirect to the task list.
-->
フォームに入力するため、スコープ内で `request` を持つことが必要になります。その場合、リクエストデータが入力されている新しいフォームを作成する `bindFromRequest` によってそれを扱うことができます。もしフォーム内に何らかのエラーがあった場合、エラーを再表示します (ここでは、 **200 OK** の代わりに **400 Bad Request** を使用します) 。もし何のエラーも発生しなければ、タスクを作成し、タスクリストにリダイレクトします。

<!--
> **Note:** Read more about the [[Form submissions|ScalaForms]].
-->
> **補足:** 詳細は [[フォーム送信を扱う|ScalaForms]] を参考にしてください。

<!--
## Persist the tasks in a database
-->
## データベース内のタスクを永続化する

<!--
It’s now time to persist the tasks in a database to make the application useful. Let’s start by enabling a database in our application. In the `conf/application.conf` file, add:
-->
アプリケーションを使いやすくするため、データベースにタスクの情報を永続化するようにしましょう。本アプリケーション内でデータベースを利用可能にします。 `conf/application.conf` ファイル内に以下の内容を追記してください:

```
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
```

<!--
For now we will use a simple in memory database using **H2**. No need to restart the server, refreshing the browser is enough to set up the database.
-->
現在、 **H2** というシンプルなインメモリデータベースを使用するようにしています。この定義を有効にするためにサーバーを再起動する必要はありません、ブラウザを更新するだけでデータベースをセットアップします。

<!--
We will use **Anorm** in this tutorial to query the database. First we need to define the database schema. Let’s use Play evolutions for that, so create a first evolution script in `conf/evolutions/default/1.sql`:
-->
これからチュートリアル内でデータベースを検索するために **Anorm** を使用します。はじめにデータベーススキーマを定義する必要があります。 `conf/evolutions/default/1.sql` に最初のエボリューションスクリプトを作成し、 Play エボリューション機能を使っていきましょう。

```
# Tasks schema
 
# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    label varchar(255)
);
 
# --- !Downs
 
DROP TABLE task;
DROP SEQUENCE task_id_seq;
```

<!--
Now if you refresh your browser, Play will warn you that your database needs evolution:
-->
ブラウザを更新すれば、 Play はデータベースがエボリューションを要求していることを警告するでしょう。

[[images/evolutions.png]]

<!--
Just click the **Apply script** button to run the script. Your database schema is now ready!
-->
**Apply script** ボタンをクリックするだけで、スクリプトが実行され、あなたのデータベーススキーマは定義済みになります!

<!--
> **Note:** Read more about [[Evolutions|Evolutions]].
-->
> **補足:** エボリューションの詳細については [[エボリューション|Evolutions]] を参照してください。

<!--
It’s now time to implement the SQL queries in the `Task` companion object, starting with the `all()` operation. Using **Anorm** we can define a parser that will transform a JDBC `ResultSet` row to a `Task` value:
-->
`all()` 操作を実行するための SQL クエリーを `Task` コンパニオンオブジェクト内に実装していきましょう。 **Anorm** を使用することで、 JDBCの `ResultSet` の一行を `Task` の値に変換するパーサーを定義することができます。

```
import anorm._
import anorm.SqlParser._

val task = {
  get[Long]("id") ~ 
  get[String]("label") map {
    case id~label => Task(id, label)
  }
}
```

<!--
Here, `task` is a parser that, given a JDBC `ResultSet` row with at least an `id` and a `label` column, is able to create a `Task` value. 
-->
ここでは、少なくとも `id` や `label` 列を持つ JDBC の `ResultSet` の一行が与えられ、 `Task` の値を作成することが可能な `task` というパーサーになります。

<!--
We can now use this parser to write the `all()` method implementation:
-->
`all()` メソッドの実装を構築するパーサーを使用することができます:

```
import play.api.db._
import play.api.Play.current

def all(): List[Task] = DB.withConnection { implicit c =>
  SQL("select * from task").as(task *)
}
```

<!--
We use the Play `DB.withConnection` helper to create and release automatically a JDBC connection. 
-->
自動的に JDBC 接続の作成と解放を行う Play の `DB.withConnection` ヘルパーを使用します。

<!--
Then we use the **Anorm** `SQL` method to create the query. The `as` method allows to parse the `ResultSet` using the `task *` parser: it will parse as many task rows as possible and then return a `List[Task]` (since our `task` parser returns a `Task`).
-->
クエリーを作成するために、 **Anorm** の `SQL` メソッドを使用します。 `as` メソッドが `task *` パーサーを用いて `ResultSet` をパースする事ができるようになります。そのため一度に多くのタスク行をする場合は、一割でパースし、 `List[Task]` を返り値に戻します (しかし、ここでの `task` パーサーは `Task` を戻します。)

<!--
It’s time to complete the implementation:
-->
実装を完成させる時が来ました:

```
def create(label: String) {
  DB.withConnection { implicit c =>
    SQL("insert into task (label) values ({label})").on(
      'label -> label
    ).executeUpdate()
  }
}

def delete(id: Long) {
  DB.withConnection { implicit c =>
    SQL("delete from task where id = {id}").on(
      'id -> id
    ).executeUpdate()
  }
}
```

<!--
Now you can play with the application; creating new tasks should work.
-->
本アプリケーションで遊んでみましょう、新しいタスク作成は動作すると考えられます。

[[images/filled.png]]

<!--
> **Note:** Read more about [[Anorm|ScalaAnorm]].
-->
> **補足:** Anormの詳細は [[Anorm|ScalaAnorm]] を参照してください。

<!--
## Deleting tasks
-->
## タスクの削除

<!--
Now that we can create tasks, we need to be able to delete them. Very simple: we just need to finish the implementation of the `deleteTask` action:
-->
タスクを作成することができるようになった後はそれらのタスクを削除する事ができるようにする必要があります。 `deleteTask` アクションの実装を (とてもシンプルに) 終わらせるだけです。

```
def deleteTask(id: Long) = Action {
  Task.delete(id)
  Redirect(routes.Application.tasks)
}
```

<!--
## Deploying to Heroku
-->
## Heroku へのデプロイ

<!--
All features are complete, so it’s time to deploy our application to production. Let’s deploy it to Heroku. First, you need to create a `Procfile` for Heroku. Create the `Procfile` in the root application directory:
-->
全ての機能が完成しました。本アプリケーションを成果物としてデプロイしましょう。 Heroku へデプロイします。はじめに `Procfile` を Heroku 用に作成する必要があります。 `Procfile` をアプリケーションの root ディレクトリに作成してください。

```
web: target/start -Dhttp.port=${PORT} -DapplyEvolutions.default=true -Ddb.default.url=${DATABASE_URL} -Ddb.default.driver=org.postgresql.Driver
```

<!--
> **Note:** Read more about [[Deploying to Heroku|ProductionHeroku]].
-->
> **補足:** Heroku へのデプロイの詳細については [[Heroku へのデプロイ |ProductionHeroku]] を参照してください。

<!--
We use system properties to override the application configuration, when running on Heroku. Since Heroku provides a PostgreSQL database, we need to add the required driver to our application dependencies. 
-->
Heroku 上で起動する際に、アプリケーションの設定を上書きするためにシステムプロパティを使用します。 Heroku は PostgreSQL データベースを提供しているため、 要求されたドライバーをアプリケーションの依存関係に追加する必要があります。

<!--
Specify it into the `project/Build.scala` file:
-->
`project/Build.scala` ファイルに依存関係を明記してください。

```
val appDependencies = Seq(
  "postgresql" % "postgresql" % "8.4-702.jdbc4"
)
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
And then deploy it using simple `git push heroku master`:
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
起動していたら、ブラウザで開いてみましょう。

<!--
> Your first application is now up and running in production!
-->
> あなたの初めてのアプリケーションがアップロードされ、稼働しています!