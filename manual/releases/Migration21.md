<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Play 2.1 migration guide
-->
# Play 2.1 移行ガイド

This is a guide for migrating from Play 2.0 to Play 2.1.

<!--
To migrate a **Play 2.0.x** application to **Play 2.1.0** first update Play's `sbt-plugin` in the `project/plugins.sbt` file:
-->
**Play 2.0.x** アプリケーションを **Play 2.1.0** に移行するためには、まず `project/plugins.sbt` 内でPlayの `sbt-plugin` のバージョンを上げましょう。

```
addSbtPlugin("play" % "sbt-plugin" % "2.1.0")
```

<!--
Now update the `project/Build.scala` file to use the new `play.Project` class instead of the `PlayProject` class:
-->
次に `project/Build.scala` 内で `PlayProject` の代わりに新しい `play.Project` を使うように修正しましょう。

<!--
First the import:
-->
import を記述します。

```
import play.Project._
```

<!--
Then the `main` project creation:
-->
`main` プロジェクトの生成部分を次のように変更します。

```
val main = play.Project(appName, appVersion, appDependencies).settings(
```

<!--
Lastly, update your `project/build.properties` file:
-->
`project/build.properties` ファイルを次のようにアップデートします。

```
sbt.version=0.12.2
```

<!--
Then clean and re-compile your project using the `play` command in the **Play 2.1.0** distribution:
-->
最後に **Play 2.1.0** のディストリビューションに含まれる `play` コマンドを起動して、プロジェクトを一旦 clean して、そして再コンパイルしましょう。

```
play clean
play ~run
```

<!--
If any compilation errors cropped up, this document will help you figure out what deprecations or incompatible changes may have caused the errors.
-->
もし何らかのコンパイルエラーが発生したら、このドキュメントを参考にして、エラーの原因となった非推奨機能、互換性のない変更などを発見することができます。

<!--
## Changes to the build file
-->
## ビルドファイルへの変更

<!--
Because Play 2.1 introduces further modularization, you now have to explicitly specify the dependencies your application needs. By default any `play.Project` will only contain a dependency to the core Play library.  You have to select the exact set of optional dependencies your application need.  Here are the new modularized dependencies in **Play 2.1**:
-->
Play 2.1 では今まで以上にモジュール化が進められました。そのため、これからはアプリケーションに必要なモジュールへの依存性を明示的に指定する必要があります。デフォルトの `play.Project` には Play の核となるたった一つのコアモジュールへの依存性のみが定義されています。それ以外のモジュールは必要に応じて選択しましょう。 **Play 2.1** で新たに切りだされたモジュールは次の通りです。

- `jdbc` : The **JDBC** connection pool and the the `play.api.db` API.
- `anorm` : The **Anorm** component.
- `javaCore` : The core **Java** API.
- `javaJdbc` : The Java database API.
- `javaEbean` : The Ebean plugin for Java.
- `javaJpa` : The JPA plugin for Java.
- `filters` : A set of build-in filters for Play (such as the CSRF filter).

<!--
Here is a typical `Build.scala` file for **Play 2.1**:
-->
**Play 2.1** の典型的な `Build.scala` ファイルの内容は次のようになります。

```
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "app-name"
    val appVersion      = "1.0"

    val appDependencies = Seq(
       javaCore, javaJdbc, javaEbean
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
```

<!--
The `mainLang` parameter for the project is not required anymore. The main language is determined based on the dependencies added to the project. If dependencies contains `javaCore` then the language is set to `JAVA` otherwise `SCALA`.Notice the modularized dependencies in the `appDependencies` section. 
-->
プロジェクトの `mainLang` パラメータはもう必要なくなりました。 Play 2.1 からは、プロジェクトのメイン言語は追加されたモジュールによって自動的に決定されます。具体的には、 `javaCore` モジュールが追加されていればメイン言語は `JAVA` となり、それ以外のケースでは `SCALA` となります。上記の `Build.scala` ファイルの例でいうと、 `appDependencies` の部分にモジュール依存性が書かれています。

<!--
## play.mvc.Controller.form() renamed to play.data.Form.form()
-->
## play.mvc.Controller.form() が play.data.Form.form() へ名称変更

<!--
Also related to modularization, the `play.data` package and its dependencies were moved out from play core to `javaCore` artifact. As a consequence of this, `play.mvc.Controller#form` was moved to `play.data.Form#form`
-->
Play のモジュール化に関連して、 `play.data` パッケージとその依存ライブラリはPlayのコアモジュールから分離して `javaCore` モジュールに移動しました。結果的に、 `play.mvc.Controller#form` も `play.data.Form#form` へ移動しています。

<!--
## play.db.ebean.Model.Finder.join() renamed to fetch()
-->
## play.db.ebean.Model.Finder.join() が fetch() へ名称変更

<!--
As part of the cleanup the Finder API join methods are replaced with fetch methods. They behave exactly same.
-->
コード整理の一環として、 Finder API の join メソッドが fetch メソッドに置き換えられました。機能には変更ありません。

<!--
## Play's Promise to become Scala's Future
-->
## Play の Promise から、Scala の Future への移行

<!--
With the introduction of `scala.concurrent.Future` in Scala 2.10 the scala ecosystem made a huge jump to unify the various Future and Promise libraries out there.
-->
Scala 2.10 で `scala.concurrent.Future` が導入されたことで、Scala のエコシステムに散在していた様々な Future 、 Promise ライブラリが統合され始めました。

<!--
The fact that Play is now using `scala.concurrent.Future` directly means that users can effortlessly combine futures/promises coming from both internal API-s or external libraries.
-->
Play が `scala.concurrent.Future` を利用するということは、つまり開発者が Play の内部 API および外部ライブラリ両方の Future/Promise をそのまま組み合わせられるようになったということです。

<!--
> Java users will continue to use a Play's wrapper around scala.concurrent.Future for now. 
-->
> 今のところ、Java ユーザは引き続き scala.concurrent.Future をラップした Play の Promise を利用することが想定されています

<!--
Consider the following snippet:
-->
次のコードスニペットを見てみましょう。


```
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import akka.util.duration._

def stream = Action {
    AsyncResult {
      implicit val timeout = Timeout(5.seconds)
      val akkaFuture =  (ChatRoomActor.ref ? (Join()) ).mapTo[Enumerator[String]]
      //convert to play promise before sending the response
      akkaFuture.asPromise.map { chunks =>
        Ok.stream(chunks &> Comet( callback = "parent.message"))
      }
    }
  }
  
```

<!--
Using the new `scala.concurrent.Future` this will become:
-->
新しい `scala.concurrent.Future` を利用すると、このコードは次のように変わります。

```
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration._

  def stream = Action {
    AsyncResult {
      implicit val timeout = Timeout(5.seconds)
      val scalaFuture = (ChatRoomActor.ref ? (Join()) ).mapTo[Enumerator[String]]
      scalaFuture.map { chunks =>
        Ok.stream(chunks &> Comet( callback = "parent.message"))
      }
    }
  }
```

<!--
Notice the extra imports for:
-->
インポートについての変更は次の通りです。

<!--
- The new import for the execution context `play.api.libs.concurrent.Execution.Implicits`
- The change for duration `scala.concurrent.duration` (instead of using the Akka API) 
- The `asPromise` method has been removed
-->
- 実行コンテキスト `play.api.libs.concurrent.Execution.Implicits` を新たにインポートする
- Akka APIのDuration の代わりに、 `scala.concurrent.duration` を使う
- `asPromise` メソッドが必要無くなっている

<!--
Generally speaking, if you see error message "error: could not find implicit value for parameter executor", you probably need to add:
-->
一般的に言うと、 "error: could not find implicit value for parameter executor" というエラーが出た場合、以下のインポートが足りない可能性があります。

```
import play.api.libs.concurrent.Execution.Implicits._
```

<!--
_(Please see the [Scala documentation about Execution context](http://docs.scala-lang.org/overviews/core/futures.html) for more information)_
-->
_(詳細については [Scalaの実行コンテキストに関するドキュメント](http://docs.scala-lang.org/overviews/core/futures.html) を参照してください)_

<!--
And remember that:

- A Play `Promise` is now a Scala `Future`
- A Play `Redeemable` is now a Scala `Promise`
-->
最後に、次の二点を改めて確認しましょう。

<!--
## Changes to the Scala JSON API
-->
## Scala JSON API への変更

<!--
**Play 2.1** comes with a shiny new Scala JSON validator and path navigator. This new API however breaks compatibility with existing JSON parsers.
-->
**Play 2.1**で は、全く新しい Scala 向けの JSON のバリデータとパスナビゲータ API が導入されました。この API は既存の JSON パーサとは互換性がありません。

<!--
The `play.api.libs.json.Reads` type signature has changed. Consider:
-->
`play.api.libs.json.Reads`型のシグネチャも変更されました。次のコードを見てください。

```
trait play.api.libs.json.Reads[A] {
  self =>

  def reads(jsValue: JsValue): A

}
```

<!--
In 2.1 this becomes:
-->
このコードは、 Play 2.1 では次のように変わりました。

```
trait play.api.libs.json.Reads[A] {
  self =>

  def reads(jsValue: JsValue): JsResult[A]

}
```

<!--
So, in **Play 2.0** an implementation for a JSON serializer for the `User` type was:
-->
例えば、 **Play 2.0** で `User` 型の JSON シリアライザは次のように実装していました。

```
implicit object UserFormat extends Format[User] {

  def writes(o: User): JsValue = JsObject(
    List("id" -> JsNumber(o.id),
      "name" -> JsString(o.name),
      "favThings" -> JsArray(o.favThings.map(JsString(_)))
    )
  )

  def reads(json: JsValue): User = User(
    (json \ "id").as[Long],
    (json \ "name").as[String],
    (json \ "favThings").as[List[String]]
  )

}
```

<!--
In **Play 2.1** you will need to refactor it as: 
-->
これを **Play 2.1** では次のようにリファクタリングする必要があります。

```
implicit object UserFormat extends Format[User] {

  def writes(o: User): JsValue = JsObject(
    List("id" -> JsNumber(o.id),
      "name" -> JsString(o.name),
      "favThings" -> JsArray(o.favThings.map(JsString(_)))
    )   
  )   

  def reads(json: JsValue): JsResult[User] = JsSuccess(User(
    (json \ "id").as[Long],
    (json \ "name").as[String],
    (json \ "favThings").as[List[String]]
  ))  

}
```

<!--
The API to generate JSON also evolved. Consider:
-->
JSON を生成する API も同様に進化しました。次のコードを見てください。

```
val jsonObject = Json.toJson(
  Map(
    "users" -> Seq(
      toJson(
        Map(
          "name" -> toJson("Bob"),
          "age" -> toJson(31),
          "email" -> toJson("bob@gmail.com")
        )
      ),
      toJson(
        Map(
          "name" -> toJson("Kiki"),
          "age" -> toJson(25),
          "email" -> JsNull
        )
      )
    )
  )
)
```

<!--
With **Play 2.1** this becomes:
-->
これを **Play 2.1** で書くと次のようになります。

```
val jsonObject = Json.obj(
  "users" -> Json.arr(
    Json.obj(
      "name" -> "Bob",
      "age" -> 31,
      "email" -> "bob@gmail.com"
    ),
    Json.obj(
      "name" -> "Kiki",
      "age" -> 25,
      "email" -> JsNull
    )
  )
)
```

<!--
More information about these features can be found [[at the Json documentation|ScalaJson]].
-->
これら機能の詳細については [[JSON関連のドキュメント|ScalaJson]] を参照してください。

<!--
## Changes to Cookie handling
-->
## Cookie 周りの変更

<!--
Due to a change in _JBoss Netty_, cookies are made to be transient by setting their `maxAge` to be `null` or `None` (depending of the API) instead of setting the `maxAge` to -1.  Any value equal to 0 or less for `maxAge` will cause the cookie to be expired immediately.
-->
Cookie を永続化するためにはこれまで `maxAge` に -1 を設定していました。しかし、 _JBoss Netty_ 側の変更により、これからは -1 の代わりに `null` または `None` (利用する API に依ります)を設定するようになりました。 `maxAge` に 0 以下の値を設定した場合、Cookie は直ちに期限切れします。

<!--
The `discardingCookies(String\*)` (Scala) and `discardCookies(String...)` (Java) methods on `SimpleResult` have been deprecated, since these methods are unable to handle cookies set on a particular path, domain or set to be secure.  Please use the `discardingCookies(DiscardingCookie*)` (Scala) and `discardCookie` (Java) methods instead.
-->
`SimpleResult` の `discardingCookies(String\*)` (Scala) と `discardCookies(String…)` (Java) メソッドは特定のパスやドメインにセットされた Cookie や、セキュア Cookie を扱えなかったため、非推奨になりました。代わりに、`discardingCookies(DiscardingCookie*)` (Scala) と `discardCookie` (Java) メソッドを利用してください。

## RequireJS

<!--
In **Play 2.0** the default behavior for Javascript was to use Google's Closure CommonJS module support. In **Play 2.1** this was changed to use RequireJS instead.
-->
**Play 2.0** では JavaScript は Google Closure の CommonJS モジュールサポートによって処理されていました。 **Play 2.1** からは代わりに RequireJS が使われるようになりました。

<!--
What this means in practice is that by default Play will only minify and combine files in stage, dist, start modes only. In dev mode Play will resolve dependencies client side.
-->
実利用においてこれが意味するのは、特に何か設定しない限り、 Play は stage 、 dist 、 start 時のみ JavaScript を minify するということです。開発モード時は、クライアント側で JavaScript モジュール間の依存性が解決されます。

<!--
If you wish to use this feature, you will need to add your modules to the settings block of your `project/Build.scala` file:
-->
この機能を利用する場合は、 `project/Build.scala` 内の設定に RequireJS で管理したいモジュールを追加する必要があります。

```
requireJs := "main.js"
```

<!--
More information about this feature can be found on the [[RequireJS documentation page|RequireJS-support]].
-->
この機能の詳細については [[RequireJS関連のドキュメント|RequireJS-support]] を参照してください。
