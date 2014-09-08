<!--
# Testing your application
-->
# アプリケーションのテスト

<!--
Writing tests for your application can be an involved process.  Play provides a default test framework for you, and provides helpers and application stubs to make testing your application as easy as possible.
-->
PlayFrameworkでは、標準でテストフレームワークをサポートしており、ヘルパーやアプリケーションスタブを用意しているため、楽しく簡単にテストを組むことができます。

<!--
## Overview
-->
## 概要

<!--
The location for tests is in the "test" folder.  There are two sample test files created in the test folder which can be used as templates.
-->
テストのソースファイルは`test`フォルダに配置します。
２つテンプレートとして使えるサンプルファイルが`test`フォルダに置いてあるので、参照してみてください。

<!--
You can run tests from the Play console.
-->
テストを実行する際、Playの場合コンソールから実行できます。

<!--
* To run all tests, run `test`.
* To run only one test class, run `test-only` followed by the name of the class i.e. `test-only my.namespace.MySpec`.
* To run only the tests that have failed, run `test-quick`.
* To run tests continually, run a command with a tilde in front, i.e. `~test-quick`.
-->
* `test`を実行すると全てのテストが走ります。
* `test-only`を実行すると、その後に続くクラス名のテストクラスのみが実行されます。  例　）`test-only my.namespace.MySpec`
* 失敗したテストだけ走らせたい場合は、`test-quick`を実行します。
* 継続的にテストを走らせたい場合は、実行するテストコマンドの前にチルダをつけます。 例）`~test-quick`

<!--
Testing in Play is based on SBT, and a full description is available in the [testing SBT](http://www.scala-sbt.org/0.13.0/docs/Detailed-Topics/Testing) chapter.
-->
SBTでテストを実行する際は、 [testing SBT](http://www.scala-sbt.org/0.13.0/docs/Detailed-Topics/Testing) に詳細が記載されています。


<!--
## Using specs2
-->
## specs2 を使う

<!--
The default way to test in Play is using [specs2](http://etorreborre.github.io/specs2/).  In specs2, tests are organized into specifications, which contain examples which run the system under test through various different code paths.
-->
Playの標準的なテストでは、[specs2](http://etorreborre.github.io/specs2/)を利用します。spec2のテストでは、異なったコードパスのテストを実行する例をまとめて記述します。

<!--
Specifications extend the [`Specification`](http://etorreborre.github.io/specs2/api/SPECS2-2.2/index.html#org.specs2.mutable.Specification) trait and are using the should/in format:
-->
[`Specification`](http://etorreborre.github.io/specs2/api/SPECS2-2.2/index.html#org.specs2.mutable.Specification)traitを継承した仕様でshould/inのフォーマットを使って記述します。：

@[scalatest-helloworldspec](code/HelloWorldSpec.scala)

<!--
Specifications can be run in either IntelliJ IDEA (using the [Scala plugin](http://blog.jetbrains.com/scala/)) or in Eclipse (using the [Scala IDE](http://scala-ide.org/)).  Please see the [[IDE page|IDE]] for more details.
-->
仕様はIntelliJ IDEA ([Scala plugin](http://blog.jetbrains.com/scala/)を利用) か Eclipse ([Scala IDE](http://scala-ide.org/)を利用)で実行することができます.  詳しくは [[IDE page|IDE]] を見てください。

<!--
NOTE: Due to a bug in the [presentation compiler](https://scala-ide-portfolio.assembla.com/spaces/scala-ide/support/tickets/1001843-specs2-tests-with-junit-runner-are-not-recognized-if-there-is-package-directory-mismatch#/activity/ticket:), tests must be defined in a specific format to work with Eclipse:
-->
メモ：[presentation compiler](https://scala-ide-portfolio.assembla.com/spaces/scala-ide/support/tickets/1001843-specs2-tests-with-junit-runner-are-not-recognized-if-there-is-package-directory-mismatch#/activity/ticket:)のバグで、Eclipseでコーディングする場合、特定のフォーマットで書く必要があります。

<!--
* The package must be exactly the same as the directory path.
* The specification must be annotated with `@RunWith(classOf[JUnitRunner])`.
-->
* ディレクトリパスとパッケージは同じでなければいけません。
* 仕様は、`@RunWith(classOf[JUnitRunner])`でアノテーションされなければいけません。

<!--
Here is a valid specification for Eclipse:
-->
Eclipseでは以下のように書きます。

```scala
package models // this file must be in a directory called "models"

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {
  ...
}
```
### Matchers

<!--
When you use an example, you must return an example result. Usually, you will see a statement containing a `must`:
-->
テスト例を使った際、・テスト例の結果を返さなければいけません。大体、`must`という宣言がよく使われます。

```scala
"Hello world" must endWith("world")
```

<!--
The expression that follows the `must` keyword are known as [`matchers`](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html). Matchers return an example result, typically Success or Failure.  The example will not compile if it does not return a result.
-->
`must`に続く表現は、[`matchers`](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html)と呼ばれます。Matchersはテスト例の結果を成功か失敗かで返します。結果を返さない場合、テスト例はコンパイルされません。

<!--
The most useful matchers are the [match results](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html#Match+results).  These are used to check for equality, determine the result of Option and Either, and even check if exceptions are thrown.
-->
最も使いやすいmatchersは、[match results](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html#Match+results)です。等しいかどうかやOptionやEitherの結果を判定したり、例外が投げられたかどうかをチェックします。

<!--
There are also [optional matchers](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html#Optional) that allow for XML and JSON matching in tests.
-->
他にもXMLやJSONを比較するテストの[optional matchers](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html#Optional)などもあります。

### Mockito

<!--
Mocks are used to isolate unit tests against external dependencies.  For example, if your class depends on an external `DataService` class, you can feed appropriate data to your class without instantiating a `DataService` object.
-->
モックは外部の依存関係から独立したユニットテストを行う際に用います。例えば、外部の`DataService`クラスに依存している場合、`DataService`オブジェクトのインスタンスを作らなくても、モックを使ってデータを提供できます。

<!--
[Mockito](https://code.google.com/p/mockito/) is integrated into specs2 as the default [mocking library](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html#Mock+expectations).
-->
[Mockito](https://code.google.com/p/mockito/) は、標準の[mocking library](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Matchers.html#Mock+expectations)として、spec2に組み込まれています。

<!--
To use Mockito, add the following:
-->
Mockitoは以下のように使います。

```scala
import org.specs2.mock._
```

<!--
and then add the [library dependency](http://mvnrepository.com/artifact/org.mockito/mockito-core) to the build.
-->
ビルドに[library dependency](http://mvnrepository.com/artifact/org.mockito/mockito-core)を追加します。

<!--
Using Mockito, you can mock out references to classes like so:
-->
Mockitoを使えば、以下のように参照をモックに向けさせられます。
@[scalaws-mockito](code/ExampleMockitoSpec.scala)

<!--
Mocking is especially useful for testing the public methods of classes.  Mocking objects and private methods is possible, but considerably harder.
-->
モックは、パブリックなメソッドに対してのテストとしてとても便利です。モックオブジェクトとプライベートメソッドの組み合わせでも可能ですが、大変です。

<!--
## Unit Testing Models
-->
## モデルのユニットテスト

<!--
Play does not require models to use a particular database data access layer.  However, if the application uses Anorm or Slick, then frequently the Model will have a reference to database access internally.
-->
Playはモデルを使う際に、特定ののデータベースアクセス層を必要としません。しかしながら、アプリケーションがAnormやSlickを使っていた場合、内部的にモデルからデータベースへの参照が頻繁に行われるでしょう。

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
モデルのユニットテストを行うにあたり、`roles`メソッドを使うことでモック化することが出来ます。

<!--
A common approach is to keep the models isolated from the database and as much logic as possible, and abstract database access behind a repository layer.
-->
一般的なアプローチでは、モデルをデータベースから隔離し、ロジックに集中させ、リポジトリ層を利用して抽象的にデータベースアクセスを行います。
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
サービスを経由してアクセスします。

@[scalatest-userservice](code/services/UserService.scala)

<!--
In this way, the `isAdmin` method can be tested by mocking out the `UserRepository` reference and passing it into the service:
-->
この方法で、`UserRepository`を参照しサービス層へ渡すことで、`isAdmin`メソッドをモックでテストすることができます
@[scalatest-userservicespec](code/UserServiceSpec.scala)

<!--
## Unit Testing Controllers
-->
##コントローラーのユニットテスト

<!--
Controllers are defined as objects in Play, and so can be trickier to unit test.  In Play this can be alleviated by [[dependency injection|ScalaDependencyInjection]] using [`getControllerInstance`](api/scala/index.html#play.api.GlobalSettings@getControllerInstance).  Another way to finesse unit testing with a controller is to use a trait with an [explicitly typed self reference](http://www.naildrivin5.com/scalatour/wiki_pages/ExplcitlyTypedSelfReferences) to the controller:
-->
Playではコントローラーはオブジェクトとして定義されるため、ユニットテストでトリッキーなことができます。Playでは、[`getControllerInstance`](api/scala/index.html#play.api.GlobalSettings@getControllerInstance)を使って、[[dependency injection|ScalaDependencyInjection]]でユニットテストを簡単に出来ます。コントローラーをテストするトリッキーな他の方法は、[explicitly typed self reference](http://www.naildrivin5.com/scalatour/wiki_pages/ExplcitlyTypedSelfReferences)上のtraitをコントローラーに使います。

@[scalatest-examplecontroller](code/ExampleControllerSpec.scala)

<!--
and then test the trait:
-->
traitのテストを追加します。
@[scalatest-examplecontrollerspec](code/ExampleControllerSpec.scala)

> **Next:** [[Writing functional tests|ScalaFunctionalTest]]
