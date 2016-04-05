<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Testing your application with ScalaTest
-->
# ScalaTest を使用したアプリケーションのテスト

<!--
Writing tests for your application can be an involved process. Play provides helpers and application stubs, and ScalaTest provides an integration library, [ScalaTest + Play](http://scalatest.org/plus/play), to make testing your application as easy as possible.
-->
アプリケーションのテストを作成するのは複雑な作業となりがちです。Play はヘルパーやスタブを提供し、ScalaTest は統合ライブラリ「 [ScalaTest + Play](http://scalatest.org/plus/play) 」を提供しており、テストの作成をできる限り容易にしています。

<!--
## Overview
-->
## 概要

<!--
The location for tests is in the "test" folder.  <!-- There are two sample test files created in the test folder which can be used as templates. -->
テストのソースファイルは “test” フォルダに配置します。

<!--
You can run tests from the Play console.
-->
テストは Play のコンソールから実行できます。

<!--
* To run all tests, run `test`.
* To run only one test class, run `test-only` followed by the name of the class, i.e., `test-only my.namespace.MySpec`.
* To run only the tests that have failed, run `test-quick`.
* To run tests continually, run a command with a tilde in front, i.e. `~test-quick`.
* To access test helpers such as `FakeApplication` in console, run `test:console`.
-->
* `test` を実行すると全てのテストが実行されます。
* `test-only` を実行すると、その後に続くクラス名のテストクラスのみが実行されます。 例）`test-only my.namespace.MySpec`
* 失敗したテストだけ走らせたい場合は、 `test-quick` を実行します。
* 継続的にテストを走らせたい場合は、実行するテストコマンドの前にチルダをつけます。 例）`~test-quick`
* `FakeApplication` などのヘルパーにアクセスしたい場合は、 `test:console` を実行します。

<!--
Testing in Play is based on SBT, and a full description is available in the [testing SBT](http://www.scala-sbt.org/0.13.0/docs/Detailed-Topics/Testing) chapter.
-->
Play のテストは SBT に基づいており、 [testing SBT](http://www.scala-sbt.org/0.13.0/docs/Detailed-Topics/Testing) に詳細が記載されています。

<!--
## Using ScalaTest + Play
-->
## ScalaTest + Play を使う

<!--
To use _ScalaTest + Play_, you'll need to add it to your build, by changing `build.sbt` like this:
-->
_ScalaTest + Play_ を使用するには、 `build.sbt` を以下のように変更し、ビルドに加えます。

```scala
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalatestplus" %% "play" % "1.4.0-M3" % "test",
)
```

<!--
You do not need to add ScalaTest to your build explicitly. The proper version of ScalaTest will be brought in automatically as a transitive dependency of _ScalaTest + Play_. You will, however, need to select a version of _ScalaTest + Play_ that matches your Play version. You can do so by checking the [Versions, Versions, Versions](http://www.scalatest.org/plus/play/versions) page for _ScalaTest + Play_.
-->
ScalaTest を明示的にビルドに追加する必要はありません。適切なバージョンの ScalaTest が _ScalaTest + Play_ の推移従属性にしたがって自動的に取得されます。しかし、特定の Play のバージョンに合った _ScalaTest + Play_ のバージョンを選択する必要があるかもしれません。その方法は [Versions, Versions, Versions](http://www.scalatest.org/plus/play/versions) ページに記載されています。

<!--
In [_ScalaTest + Play_](http://scalatest.org/plus/play), you define test classes by extending the [`PlaySpec`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.PlaySpec) trait. Here's an example:
-->
[_ScalaTest + Play_](http://scalatest.org/plus/play) では、 [`PlaySpec`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.PlaySpec) トレイトを拡張することでテストクラスを定義します。以下がその例です。

@[scalatest-stackspec](code-scalatestplus-play/StackSpec.scala)

<!--
You can alternatively [define your own base classes](http://scalatest.org/user_guide/defining_base_classes) instead of using `PlaySpec`.
-->
あるいは、PlaySpec のかわりに [自前のベースクラスを定義する](http://scalatest.org/user_guide/defining_base_classes) こともできます。

<!--
You can run your tests with Play itself, or in IntelliJ IDEA (using the [Scala plugin](https://blog.jetbrains.com/scala/)) or in Eclipse (using the [Scala IDE](http://scala-ide.org/) and the [ScalaTest Eclipse plugin](http://scalatest.org/user_guide/using_scalatest_with_eclipse)).  Please see the [[IDE page|IDE]] for more details.
-->
テストは Play 自体、あるいは ([Scala plugin](https://blog.jetbrains.com/scala/) を使用した) IntelliJ IDEA、([Scala IDE](http://scala-ide.org/) と [ScalaTest Eclipse plugin](http://scalatest.org/user_guide/using_scalatest_with_eclipse)) を使用した Eclipse によって実行できます。詳しくは [[IDE のページ|IDE]] を参照してください。

<!--
### Matchers
-->
### Matchers

<!--
`PlaySpec` mixes in ScalaTest's [`MustMatchers`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.MustMatchers), so you can write assertions using ScalaTest's matchers DSL:
-->
`PlaySpec` は ScalaTest の [`MustMatchers`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.MustMatchers) をミックスインしているので、 ScalaTest の matchers DSL を使用してアサーションを書くことができます。

```scala
import play.api.test.Helpers._

"Hello world" must endWith ("world")
```

<!--
For more information, see the documentation for [`MustMatchers`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.MustMatchers).
-->
詳しくは、 [`MustMatchers`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.MustMatchers) のドキュメントを参照してください。

<!--
### Mockito
-->
### Mockito

<!--
You can use mocks to isolate unit tests against external dependencies.  For example, if your class depends on an external `DataService` class, you can feed appropriate data to your class without instantiating a `DataService` object.
-->
ユニットテストを外部の依存性から隔離するためにモックを利用できます。例えば、テスト対象クラスが外部の `DataService` クラスに依存している場合、 `DataService` オブジェクトをインスタンス化することなく適当なデータをテスト対象クラスに与えることができます。 

<!--
ScalaTest provides integration with [Mockito](https://github.com/mockito/mockito) via its [`MockitoSugar`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.mock.MockitoSugar) trait.
-->
ScalaTest は [`MockitoSugar`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.mock.MockitoSugar) トレイトを介して [Mockito](https://github.com/mockito/mockito) との統合を提供しています。

<!--
To use Mockito, mix `MockitoSugar` into your test class and then use the Mockito library to mock dependencies:
-->
Mockito を使用するには、 `MockitoSugar` をテストクラスにミックスインし、 依存性をモックするために Mockito ライブラリを使用します。

@[scalatest-mockito-dataservice](code-scalatestplus-play/ExampleMockitoSpec.scala)

@[scalatest-mockitosugar](code-scalatestplus-play/ExampleMockitoSpec.scala)

<!--
Mocking is especially useful for testing the public methods of classes.  Mocking objects and private methods is possible, but considerably harder.
-->
モックは特に、public なメソッドに対してのテストに便利です。object や private メソッドのモックも可能ではありますが、非常に困難です。

<!--
## Unit Testing Models
-->
## モデルのユニットテスト

<!--
Play does not require models to use a particular database data access layer.  However, if the application uses Anorm or Slick, then frequently the Model will have a reference to database access internally.
-->
Play はモデルを使う際に特定のデータベースアクセス層を必要としません。しかし、アプリケーションが Anorm や Slick を使っていた場合、モデルは内部的にデータベースアクセス層への参照を頻繁に行うでしょう。

```scala
import anorm._
import anorm.SqlParser._

case class User(id: String, name: String, email: String) {
   def roles = DB.withConnection { implicit connection =>
      ...
    }
}
```

<!--
For unit testing, this approach can make mocking out the `roles` method tricky.
-->
ユニットテストをするには、 `roles` メソッドをうまく使うことでモック化することが出来ます。

<!--
A common approach is to keep the models isolated from the database and as much logic as possible, and abstract database access behind a repository layer.
-->
一般的には、モデルをデータベースから隔離し、ロジックに集中させ、リポジトリ層を利用して抽象的にデータベースアクセスを行うというアプローチをとります。

@[scalatest-models](code/models/User.scala)

@[scalatest-repository](code/services/UserRepository.scala)

```scala
class AnormUserRepository extends UserRepository {
  import anorm._
  import anorm.SqlParser._

  def roles(user:User) : Set[Role] = {
    ...
  }
}
```

<!--
and then access them through services:
-->
そして、サービスを経由してアクセスします。

@[scalatest-userservice](code/services/UserService.scala)

<!--
In this way, the `isAdmin` method can be tested by mocking out the `UserRepository` reference and passing it into the service:
-->
こうすることで、モック化した `UserRepository` の参照をサービスに渡して `isAdmin` メソッドをテストすることができます

@[scalatest-userservicespec](code-scalatestplus-play/UserServiceSpec.scala)

<!--
## Unit Testing Controllers
-->
## コントローラのユニットテスト

<!--
When defining controllers as objects, they can be trickier to unit test. In Play this can be alleviated by [[dependency injection|ScalaDependencyInjection]]. Another way to finesse unit testing with a controller declared as a object is to use a trait with an [explicitly typed self reference](http://www.naildrivin5.com/scalatour/wiki_pages/ExplcitlyTypedSelfReferences) to the controller:
-->
コントローラを object として定義すると、ユニットテストするのが難しくなります。Play では [[依存性注入|ScalaDependencyInjection]] で緩和できます。あるいは、コントローラに [明示的に型付けられた自己参照](http://www.naildrivin5.com/scalatour/wiki_pages/ExplcitlyTypedSelfReferences) のトレイトを使用するという方法によっても、object として定義されたコントローラのユニットテストを幾分楽にすることができます。

@[scalatest-examplecontroller](code-scalatestplus-play/ExampleControllerSpec.scala)

<!--
and then test the trait:
-->
そして、トレイトのテストを追加します。

@[scalatest-examplecontrollerspec](code-scalatestplus-play/ExampleControllerSpec.scala)

<!--
When testing POST requests with, for example, JSON bodies, you won't be able to
use the pattern shown above (`apply(fakeRequest)`); instead you should use
`call()` on the `testController`:
-->
JSON ボディなどの POST リクエストのテストを行う場合、上述のパターン (`apply(fakeRequest)`) は使用できません。そのかわり、 `testController` で `call()` メソッドを使用してください。

@[scalatest-examplepost](code-scalatestplus-play/ExamplePostSpec.scala)

<!--
## Unit Testing EssentialAction
-->
## EssentialAction のテスト

<!--
Testing [`Action`](api/scala/play/api/mvc/Action.html) or [`Filter`](api/scala/play/api/mvc/Filter.html) can require to test an an [`EssentialAction`](api/scala/play/api/mvc/EssentialAction.html) ([[more information about what an EssentialAction is|HttpApi]])
-->
[`Action`](api/scala/play/api/mvc/Action.html) や [`Filter`](api/scala/play/api/mvc/Filter.html) のテストには [`EssentialAction`](api/scala/play/api/mvc/EssentialAction.html) のテストが必要になることがあります。 ([[EssentialAction の詳細はこちら|HttpApi]])

<!--
For this, the test [`Helpers.call`](api/scala/play/api/test/Helpers$.html#call) can be used like that:
-->
そのためには、以下のように [`Helpers.call`](api/scala/play/api/test/Helpers$.html#call) テストを使用します。

@[scalatest-exampleessentialactionspec](code-scalatestplus-play/ExampleEssentialActionSpec.scala)
