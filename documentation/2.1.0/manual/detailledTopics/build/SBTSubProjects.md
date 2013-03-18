<!-- translated -->
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
You can make your application depend on a simple library project. Just add another sbt project definition in your `project/Build.scala` build file:
-->
アプリケーションからシンプルな「ライブラリプロジェクト」を切り出すことができます。`project/Build.scala` に、次のような sbt のプロジェクト定義を追加してください。

```
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "my-first-application"
  val appVersion      = "1.0"

  val appDependencies = Seq(
    //if it's a java project add javaCore, javaJdbc, jdbc etc.
  )
  
  val mySubProject = Project("my-library", file("myLibrary"))

  val main = play.Project(
    appName, appVersion, appDependencies, path = file("myProject")
  ).dependsOn(mySubProject)


}
```

<!--
Here we have defined a sub-project in the application’s `myLibrary` folder. This sub-project is a standard sbt project, using the default layout:
-->
これで、`myLibrary` ディレクトリにサブプロジェクトを定義しました。サブプロジェクトは普通の sbt プロジェクトの一種であり、標準的なディレクトリ構成に従います。

```
myProject
 └ app
 └ conf
 └ public
myLibrary
 └ src
    └ main
       └ java
       └ scala
project
 └ Build.scala
```

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

<!--
## Splitting your web application into several parts
-->
## Webアプリケーションを複数のプロジェクトに分割する

<!--
As a Play application is just a standard sbt project with a default configuration, it can depend on another Play application. 
-->
Play アプリケーションはデフォルト設定に従った普通のsbtプロジェクトでもあるため、お互いに依存性を持たせることができます。

<!--
The configuration is very close to the previous one. Simply configure your sub-project as a `play.Project`:
-->
設定方法は、サブプロジェクトの場合ととてもよく似ています。サブプロジェクトを `play.Project` として定義すれば OK です。

```
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "zenexity.com"
  val appVersion = "1.2"

  val common = play.Project(
    appName + "-common", appVersion, path = file("common")
  )
  
  val website = play.Project(
    appName + "-website", appVersion, path = file("website")
  ).dependsOn(common)
  
  val adminArea = play.Project(
    appName + "-admin", appVersion, path = file("admin")
  ).dependsOn(common)
  
  val main = play.Project(
    appName, appVersion, path = file("main")
  ).dependsOn(
    website, adminArea
  )
}
```

<!--
Here we define a complete project split in two main parts: the website and the admin area. Moreover these two parts depend themselves on a common module.
-->
これで、Web サイトと管理者向けサイトから構成される main プロジェクトを定義できました。また、両方のプロジェクトが common モジュールに依存しています。

<!--
If you would like the dependent projects to be recompiled and tested when you recompile and test the main project then you will need to add an "aggregate" clause.
-->
main プロジェクトを再コンパイルしてテストするとき、依存するプロジェクトが再コンパイルされてテストされるようにしたい場合は、"aggregate" 句を追加する必要があります。

```
val main = PlayProject(
  appName, appVersion
).dependsOn(
  website, adminArea
).aggregate(
  website, adminArea
)
```

<!--
> Note: in order to avoid naming collision, make sure your controllers, including the Assets controller in your subprojects are using a different name space than the main project
-->
> ノート: 名前の衝突を避けるため、サブプロジェクトのコントローラは、アセットのコントローラーも含め、メインプロジェクトと別の名前空間を使用するようにして下さい。

<!--
## Splitting the route file
-->
## ルートファイルを分割する

<!--
As of `play 2.1` it's also possible to split the route file into smaller pieces. This is a very handy feature if you want to create a robust, reusable multi-module play application
-->
`play 2.1` からはルートファイルを小さな部品に分割することもできます。これはロバストで再利用可能な、マルチモジュールの play アプリケーションを作成したい時にとても便利な機能です。

<!--
### Consider the following build file
-->
### 以下のビルドファイルを考えます

`project/Build.scala`:

```scala
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "myproject"
    val appVersion      = "1.0-SNAPSHOT"

    val adminDeps = Seq(
      // Add your project dependencies here,
       "mysql" % "mysql-connector-java" % "5.1.18",
      jdbc,
      anorm
    )

    val mainDeps = Seq()
  
   lazy val admin = play.Project(appName + "-admin", appVersion, adminDeps, path = file("modules/admin"))


  lazy  val main = play.Project(appName, appVersion, mainDeps).settings(
      // Add your own project settings here      
    ).dependsOn(admin).aggregate(admin)

}
```

<!--
### project structure
-->
### プロジェクト構成

```
app
  └ controllers
  └ models
  └ views
conf
  └ application.conf
  └ routes
modules
  └ admin
    └ conf/admin.routes
    └ app/controllers
    └ app/models
    └ app/views     
project
 └ build.properties
 └ Build.scala
 └ plugins.sbt
```

<!--
> Note: there is only a single instance of `application.conf`. Also, the route file in `admin` is called `admin.routes`
-->
> ノート: `application.conf` は一つしかありません。また `admin` のルートファイルは `admin.routes` という名前になっています。

`conf/routes`:

```
GET /index                          controllers.Application.index()

->  /admin admin.Routes

GET     /assets/*file               controllers.Assets.at(path="/public", file)
```

`modules/admin/conf/admin.routes`:

```
GET /index                           controllers.admin.Application.index()

GET     /assets/*file               controllers.admin.Assets.at(path="/public", file)

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
package controllers.my;
public class Assets {
//can be referenced as `controllers.my.Assets.delegate.at` in the route file
public static controllers.AssetsBuilder delegate = new controllers.AssetsBuilder();
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
triggers 
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

<!--
triggers 
-->
は以下を呼び出します。

```
controllers.admin.Application.index
```

