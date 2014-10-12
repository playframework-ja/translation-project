<!--
# Working with sub-projects
-->
# サブプロジェクト

<!--
A complex project is not necessarily composed of a single Play application. You may want to split a large project into several smaller applications, or even extract some logic into a standard Java or Scala library that has nothing to do with a Play application.
-->
複雑なプロジェクトを、一つの Play アプリケーションにまとめる必要はありません。大きなプロジェクトは、小さなアプリケーションに分割できます。また、アプリケーションから独立したロジックは Java や Scala ライブラリに切り出すのもよいでしょう。

<!--
It will be helpful to read the [SBT documentation on multi-project builds](http://www.scala-sbt.org/release/docs/Getting-Started/Multi-Project).  Sub-projects do not have their own build file, but share the parent project's build file.
-->
詳細については[「マルチプロジェクト・ビルドについての SBT ドキュメント」](http://scalajp.github.com/sbt-getting-started-guide-ja/multi-project/) [(原文)](http://www.scala-sbt.org/release/docs/Getting-Started/Multi-Project) を参照してください。また、サブプロジェクトを定義するにあたって最も基本的なこととして、サブプロジェクト用のビルドファイルというものはありません。親のビルドファイルにサブプロジェクトを定義します。

<!--
## Adding a simple library sub-project
-->
## ライブラリをサブプロジェクトとして切り出す

<!--
You can make your application depend on a simple library project. Just add another sbt project definition in your `build.sbt` file:
-->
アプリケーションからシンプルな「ライブラリプロジェクト」を切り出すことができます。`build.sbt` に、次のような sbt のプロジェクト定義を追加してください。

```
import play.Project._

name := "my-first-application"

version := "1.0"

playScalaSettings

lazy val myFirstApplication = project.in(file("."))
    .aggregate(myLibrary)
    .dependsOn(myLibrary)

lazy val myLibrary = project
```

<!--
The lowercased `project` on the last line is a Scala Macro which will use the name of the val it is being assigned to in order to determine the project's name and folder.
-->
最終行の小文字の `project` は、代入先の val の名前を使ってプロジェクトの名前とフォルダを決定する Scala マクロです。

The `myFirstApplication` project declares the base project.  If you don't have any sub projects, this is already implied, however when declaring sub projects, it's usually required to declare it so that you can ensure that it aggregates (that is, runs things like compile/test etc on the sub projects when run in the base project) and depends on (that is, adds the sub projects to the main projects classpath) the sub projects.

<!--
The above example defines a sub-project in the application’s `myLibrary` folder. This sub-project is a standard sbt project, using the default layout:
-->
上記の例では `myLibrary` ディレクトリにサブプロジェクトを定義しました。サブプロジェクトは普通の sbt プロジェクトの一種であり、標準的なディレクトリ構成に従います。

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

`myLibrary` has its own `build.sbt` file, this is where it can declare its own settings, dependencies etc.
> Note: A file in myLibrary/project/Build.scala will not be discovered. So the modules should use only the myLibrary/build.sbt file.

<!--
When you have a sub-project enabled in your build, you can focus on this project and compile, test or run it individually. Just use the `projects` command in the Play console prompt to display all projects:
-->
ビルド設定でサブプロジェクトを有効にした場合、それぞれのプロジェクトを個別にコンパイル、テスト、実行することができます。Play コンソールで `projects` コマンドを実行すると、全てのプロジェクトが表示されます。

```
[my-first-application] $ projects
[info] In file:/Volumes/Data/gbo/myFirstApp/
[info] 	 * my-first-application
[info] 	   my-library
```

<!--
The default project is the one whose variable name comes first alphabetically.  You may make your main project by making its variable name aaaMain.  To change the current project use the `project` command:
-->
デフォルトのプロジェクトは変数名がアルファベット順で最初の物になります。メインプロジェクトを指定したい場合は変数名を aaaMain 等にする事で可能になります。現在のプロジェクトを切り替えるには、`project` コマンドを使ってください。

```
[my-first-application] $ project my-library
[info] Set current project to my-library
>
```

<!--
When you run your Play application in dev mode, the dependent projects are automatically recompiled, and if something cannot compile you will see the result in your browser:
-->
Play アプリケーションを開発モードで起動している場合、依存するサブプロジェクトも自動的に再コンパイルされます。サブプロジェクトのコンパイルエラーも、ブラウザ上で確認できます。

[[subprojectError.png]]

## Sharing common variables and code

If you want your sub projects and root projects to share some common settings or code, then these can be placed in a Scala file in the `project` directory of the root project.  For example, in `project/Common.scala` you might have:

```scala
import sbt._
import Keys._

object Common {
  val settings: Seq[Setting[_]] = {
    organization := "com.example",
    version := "1.2.3-SNAPSHOT"
  }

  val fooDependency = "com.foo" %% "foo" % "2.4"
}
```

Then in each of your `build.sbt` files, you can reference anything declared in the file:

```scala
name := "my-sub-module"

Common.settings

libraryDependencies += fooDependency
```

<!--
## Splitting your web application into several parts
-->
## Webアプリケーションを複数のプロジェクトに分割する

As a Play application is just a standard sbt project with a default configuration, it can depend on another Play application.  You can make any sub module a Play application by including `playScalaSettings` or `playJavaSettings`, depending on whether your project is a Java or Scala project, in its corresponding `build.sbt` file.

<!--
> Note: in order to avoid naming collision, make sure your controllers, including the Assets controller in your subprojects are using a different name space than the main project
-->
> ノート: 名前の衝突を避けるため、サブプロジェクトのコントローラは、アセットのコントローラーも含め、メインプロジェクトと別の名前空間を使用するようにして下さい。

<!--
## Splitting the route file
-->
## ルートファイルを分割する

It's also possible to split the route file into smaller pieces. This is a very handy feature if you want to create a robust, reusable multi-module play application

### Consider the following build configuration

`build.sbt`:

```scala
name := "myproject"

playScalaSettings

lazy val admin = project.in(file("modules/admin"))

lazy val main = project.in(file("."))
    .dependsOn(admin).aggregate(admin)
```

`modules/admin/build.sbt`

```scala
name := "myadmin"

playScalaSettings

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.18",
  jdbc,
  anorm
)
```

### Project structure

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
> Note: there is only a single instance of `application.conf`. Also, the route file in `admin` is called `admin.routes`
-->
> ノート: `application.conf` は一つしかありません。また `admin` のルートファイルは `admin.routes` という名前になっています。

`conf/routes`:

```
GET /index                  controllers.Application.index()

->  /admin admin.Routes

GET     /assets/*file       controllers.Assets.at(path="/public", file)
```

`modules/admin/conf/admin.routes`:

```
GET /index                  controllers.admin.Application.index()

GET /assets/*file           controllers.admin.Assets.at(path="/public", file)

```

<!--
### Assets and controller classes should be all defined in the `controllers.admin` package
-->
### アセットとコントローラクラスは `controllers.admin` パッケージ以下に無ければなりません。

`modules/admin/controllers/Assets.scala`:

```scala
package controllers.admin
object Assets extends controllers.AssetsBuilder
```

<!--
> Note: Java users can do something very similar i.e.
-->
> ノート: Java ユーザは以下のように類似のことができます。

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
`Assets` の場合は以下のようになります:

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
<!--
triggers 
-->
は以下を呼び出します。
-->
は以下を呼び出します。

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

triggers 

```
controllers.admin.Application.index
```
