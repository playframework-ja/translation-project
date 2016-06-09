<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Working with sub-projects
-->
# サブプロジェクト

<!--
A complex project is not necessarily composed of a single Play application. You may want to split a large project into several smaller applications, or even extract some logic into a standard Java or Scala library that has nothing to do with a Play application.
-->
複雑なプロジェクトを、ひとつの Play アプリケーションにまとめる必要はありません。大きなプロジェクトを小さないくつかのアプリケーションに分割したり、Play アプリケーションと関係のないロジックは標準的な Java や Scala のライブラリを抽出したくなることがあるでしょう。

<!--
It will be helpful to read the [SBT documentation on multi-project builds](http://www.scala-sbt.org/release/docs/Getting-Started/Multi-Project).  Sub-projects do not have their own build file, but share the parent project's build file.
-->
[マルチプロジェクト・ビルドについての SBT ドキュメント](http://www.scala-sbt.org/0.13/docs/ja/Multi-Project.html)  を読むといいでしょう。サブプロジェクトは独自のビルドファイルを持たず、親プロジェクトのビルドファイルを共有します。

<!--
## Adding a simple library sub-project
-->
## ライブラリをサブプロジェクトとして切り出す

<!--
You can make your application depend on a simple library project. Just add another sbt project definition in your `build.sbt` file:
-->
アプリケーションをシンプルなライブラリプロジェクトに依存させることができます。`build.sbt` に、次のような sbt のプロジェクト定義を追加してください:

```
name := "my-first-application"

version := "1.0"

lazy val myFirstApplication = (project in file("."))
    .enablePlugins(PlayScala)
    .aggregate(myLibrary)
    .dependsOn(myLibrary)

lazy val myLibrary = project
```

<!--
The lowercased `project` on the last line is a Scala Macro which will use the name of the val it is being assigned to in order to determine the project's name and folder.
-->
最終行の小文字の `project` は、代入先の val の名前を使ってプロジェクトの名前とフォルダを決定する Scala マクロです。

<!--
The `myFirstApplication` project declares the base project.  If you don't have any sub projects, this is already implied, however when declaring sub projects, it's usually required to declare it so that you can ensure that it aggregates (that is, runs things like compile/test etc on the sub projects when run in the base project) and depends on (that is, adds the sub projects to the main projects classpath) the sub projects.
-->
この `myFirstApplication` は、ベースプロジェクトを宣言します。サブプロジェクトを持たない場合は特に設定する必要はありませんが、サブプロジェクトを持つ場合は宣言する必要があり、これによりベースプロジェクトがサブプロジェクトを集約 (つまり、ベースプロジェクトを実行すると、サブプロジェクトがコンパイル/テストされます) し、またサブプロジェクトに依存 (つまり、サブプロジェクトをベースプロジェクトのクラスパスに追加します) することを確認できます。

<!--
The above example defines a sub-project in the application’s `myLibrary` folder. This sub-project is a standard sbt project, using the default layout:
-->
上記の例では `myLibrary` ディレクトリにサブプロジェクトを定義しました。サブプロジェクトは普通の sbt プロジェクトの一種であり、標準的なディレクトリ構成に従います:

```
myProject
 └ build.sbt
 └ app
 └ conf
 └ public
 └ myLibrary
   └ build.sbt
   └ src
     └ main
       └ java
       └ scala
```

<!--
`myLibrary` has its own `build.sbt` file, this is where it can declare its own settings, dependencies etc.
-->
`myLibrary` にも、独自の設定や依存性などを宣言する、専用の `build.sbt` ファイルがあります。

<!--
When you have a sub-project enabled in your build, you can focus on this project and compile, test or run it individually. Just use the `projects` command in the Play console prompt to display all projects:
-->
ビルド設定でサブプロジェクトを有効にした場合、それぞれのプロジェクトを個別にコンパイル、テスト、実行することができます。Play コンソールで `projects` コマンドを実行すると、全てのプロジェクトが表示されます:

```
[my-first-application] $ projects
[info] In file:/Volumes/Data/gbo/myFirstApp/
[info] 	 * my-first-application
[info] 	   my-library
```

<!--
The default project is the one whose variable name comes first alphabetically.  You may make your main project by making its variable name aaaMain.  To change the current project use the `project` command:
-->
デフォルトのプロジェクトは変数名がアルファベット順で最初の物になります。メインプロジェクトを指定したい場合は変数名を aaaMain 等にする事で可能になります。現在のプロジェクトを切り替えるには、`project` コマンドを使ってください:

```
[my-first-application] $ project my-library
[info] Set current project to my-library
>
```

<!--
When you run your Play application in dev mode, the dependent projects are automatically recompiled, and if something cannot compile you will see the result in your browser:
-->
Play アプリケーションを開発モードで起動している場合、依存するサブプロジェクトも自動的に再コンパイルされます。サブプロジェクトのコンパイルエラーも、ブラウザ上で確認できます:

[[subprojectError.png]]

<!--
## Sharing common variables and code
-->
## 共通変数とコードの共有

<!--
If you want your sub projects and root projects to share some common settings or code, then these can be placed in a Scala file in the `project` directory of the root project.  For example, in `project/Common.scala` you might have:
-->
ルートプロジェクトとサブプロジェクトで共通の設定やコードを共有したい場合は、ルートプロジェクトの `project` 内の Scala ファイルに配置することができます。例えば、次のような `project/Common.scala` があるとします:

```scala
import sbt._
import Keys._

object Common {
  val settings: Seq[Setting[_]] = Seq(
    organization := "com.example",
    version := "1.2.3-SNAPSHOT"
  )

  val fooDependency = "com.foo" %% "foo" % "2.4"
}
```

<!--
Then in each of your `build.sbt` files, you can reference anything declared in the file:
-->
こうすることで、すべての `build.sbt` から、このファイルに定義されたあらゆるものを参照できるようになります:

```scala
name := "my-sub-module"

Common.settings

libraryDependencies += Common.fooDependency
```

<!--
## Splitting your web application into several parts
-->
## Webアプリケーションを複数のプロジェクトに分割する

<!--
As a Play application is just a standard sbt project with a default configuration, it can depend on another Play application.  You can make any sub module a Play application by adding the `PlayJava` or `PlayScala` plugins, depending on whether your project is a Java or Scala project, in its corresponding `build.sbt` file.
-->
Play アプリケーションの実体は、デフォルトの設定を持った標準的な sbt プロジェクトなので、他の Play アプリケーションに依存することができます。対応する `build.sbt` ファイルに、プロジェクトが Java または Scala いずれのプロジェクトであるかに応じて `PlayJava` もしくは `PlayScala` プラグインを追加することで、あらゆるサブモジュールを Play アプリケーションにすることができます。

<!--
> **Note:** In order to avoid naming collision, make sure your controllers, including the Assets controller in your subprojects are using a different name space than the main project
-->
> **注意:** 名前の衝突を避けるために、サブプロジェクト内の Assets コントローラを含むコントローラが、メインプロジェクトとは異なる名前空間を使っていることを確認してください。

<!--
## Splitting the route file
-->
## ルートファイルを分割する

<!--
It's also possible to split the route file into smaller pieces. This is a very handy feature if you want to create a robust, reusable multi-module play application
-->
route ファイルを、より小さなファイルに分割することもできます。この機能は、堅牢で、再利用性の高いマルチモジュール Play アプリケーションを作るときにとても便利です。

<!--
### Consider the following build configuration
-->
### 以下のビルド設定を検討してみましょう

`build.sbt`:

```scala
name := "myproject"

lazy val admin = (project in file("modules/admin")).enablePlugins(PlayScala)

lazy val main = (project in file("."))
    .enablePlugins(PlayScala).dependsOn(admin).aggregate(admin)
```

`modules/admin/build.sbt`

```scala
name := "myadmin"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.35",
  jdbc,
  anorm
)
```

<!--
### Project structure
-->
### プロジェクト構成

```
build.sbt
app
  └ controllers
  └ models
  └ views
conf
  └ application.conf
  └ routes
modules
  └ admin
    └ build.sbt
    └ conf
      └ admin.routes
    └ app
      └ controllers
      └ models
      └ views
project
  └ build.properties
  └ plugins.sbt
```

<!--
> **Note:** Configuration and route file names must be unique in the whole project structure. Particularly, there must be only one `application.conf` file and only one `routes` file. To define additional routes or configuration in sub-projects, use sub-project-specific names. For instance, the route file in `admin` is called `admin.routes`. To use a specific set of settings in development mode for a sub project, it would be even better to put these settings into the build file, e.g. `PlayKeys.devSettings += ("play.http.router", "admin.Routes")`.
-->
> **注意:** 設定ファイルおよび route ファイルの名前はプロジェクト構成全体でユニークでなければならず、`application.conf` ファイルと `routes` ファイルはそれぞれひとつだけです。サブプロジェクトに routes または設定ファイルを定義する場合は、サブプロジェクトに特化した名前を使います。例えば、`admin` 中の route ファイルは `admin.route` と呼ばれます。開発モードのサブプロジェクトにおいて特別な設定セットを使う際、例えば `PlayKeys.devSettings += ("play.http.router", "admin.Routes")`.のように、その設定セットをビルドファイルに書くとなお良いでしょう。

`conf/routes`:

```
GET /index                  controllers.Application.index()

->  /admin admin.Routes

GET     /assets/*file       controllers.Assets.at(path="/public", file)
```

`modules/admin/conf/admin.routes`:

@[assets-routes](code/detailedtopics.build.subprojects.assets.routes)

<!--
> **Note:** Resources are served from a unique classloader, and thus resource path must be relative from project classpath root.
> Subprojects resources are generated in `target/web/public/main/lib/{module-name}`, so the resources are accessible from `/public/lib/{module-name}` when using `play.api.Application#resources(uri)` method, which is what the `Assets.at` method does.
-->
> **注意:** リソースは単一のクラスローダから提供されるので、リソースパスはルートプロジェクトのクラスパスから見た相対パスでなければなりません。
> サブプロジェクトのリソースは `target/web/public/main/lib/{module-name}` に生成されるので、`Assets.at` メソッドが行っているように `play.api.Application#resources(uri)` を使った場合、そのリソースには `/public/lib/{module-name}` でアクセスできるようになります。

<!--
### Assets and controller classes should be all defined in the `controllers.admin` package
-->
### アセットとコントローラクラスは `controllers.admin` パッケージ以下に無ければなりません

`modules/admin/controllers/Assets.scala`:

@[assets-builder](code/SubProjectsAssetsBuilder.scala)

<!--
> **Note:** Java users can do something very similar i.e.:
-->
Java のユーザは、以下のようにして同様のことを実現できます:

```java
// Assets.java
package controllers.admin;
import play.api.mvc.*;

public class Assets {
  public static Action<AnyContent> at(String path, String file) {
    return controllers.Assets.at(path, file);
  }
}
```

<!--
and a controller:
-->
コントローラも同様です:

`modules/admin/controllers/Application.scala`:

```scala
package controllers.admin

import play.api._
import play.api.mvc._
import views.html._

object Application extends Controller {

  def index = Action { implicit request =>
    Ok("admin")
  }
}
```

<!--
### Reverse routing in ```admin```
-->
### ```admin``` でのリバースルーティング

<!--
in case of a regular controller call:
-->
通常のコントローラの場合は以下のように呼び出します:


```
controllers.admin.routes.Application.index
```

<!--
and for `Assets`:
-->
`Assets` の場合は以下のようにします:

```
controllers.admin.routes.Assets.at("...")
```

<!--
### Through the browser
-->
### ブラウザ経由の場合

```
http://localhost:9000/index
```

<!--
triggers
-->
上記 URL は、以下を呼び出します。

```
controllers.Application.index
```

<!--
and
-->
また

```
http://localhost:9000/admin/index
```

<!--
triggers
-->
上記 URL は、以下を呼び出します。

```
controllers.admin.Application.index
```
