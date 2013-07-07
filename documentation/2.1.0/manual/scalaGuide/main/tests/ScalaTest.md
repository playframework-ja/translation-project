<!-- translated -->
<!--
# Testing your application
-->
# アプリケーションのテスト

<!-- Test source files must be placed in your application’s `test` folder. You can run them from the Play console using the ``test` (run all tests) and `test-only` (run one test class: `test-only my.namespace.MySpec`) tasks. -->
テストのソースファイルは `test` フォルダに配置します。 Play コンソールで `test` (すべてのテストを実行します) や `test-only` (`test-only my.namespace.MySpec` のようにして、ひとつのテストクラスを実行します) タスクを実行すると、テストを実行することができます。

<!--
## Using specs2
-->
## specs2 を使う

<!--
The default way to test a Play 2 application is by using [[specs2| http://etorreborre.github.com/specs2/]].
-->
Play 2 アプリケーションのテストは、デフォルトで [[specs2| http://etorreborre.github.com/specs2/]] を使います。

<!--
Unit specifications extend the `org.specs2.mutable.Specification` trait and are using the should/in format:
-->
specs2 でクラス単体の仕様を記述する場合は、`org.specs2.mutable.Specification` trait を継承したクラス内で、 should/in のフォーマットを使って記述します。

```scala
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class HelloWorldSpec extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}
```

<!--
## Running in a fake application
-->
## フェイクアプリケーション上で実行する

<!-- If the code you want to test depends on a running application, you can easily run a fake application with the `WithApplication` around
scope: -->
起動中のアプリケーションに依存するコードをテストする場合は、`WithApplication` で囲まれたスコープで簡単にフェイクアプリケーションを実行することができます。

```scala
"Computer model" should {

  "be retrieved by id" in new WithApplication {
    val Some(macintosh) = Computer.findById(21)

    macintosh.name must equalTo("Macintosh")
    macintosh.introduced must beSome.which(dateIs(_, "1984-01-24"))  
  }
}
```

<!-- You can access the application directly using `app`, and it is also avialable implicitly. -->
暗黙的に利用可能な `app` を使って、フェイクアプリケーションに直接アクセスすることができます。

<!-- You can also pass (or override) additional configuration to the fake application, or mock any plug-in. For example to create a `FakeApplication` using a `default` in memory database: -->
このフェイクアプリケーションに対して設定値を追加 (または上書き) したり、プラグインをモックすることも可能です。例えば、 `default` という名前の インメモリデータベースに接続された `FakeApplication` を起動する場合は、次のように書きます。

```scala
  "be retrieved by id" in new WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    ...
  }
```

<!-- ## Running multiple examples inside the same specification -->
## 同じ仕様で複数のテストを実行する

<!-- In Unit specifications (see the first part of this page) you use ``should`` method to create groups of ``Example`` and the ``in`` method to create an ``Example`` , which contains a ``Result``. If you want to create a group of Examples where multiple examples needs a Play! application to be running, you cannot share the application and you have to provide a new one to each example like the following: -->
ユニットテスト仕様 (このページの最初の部分を見てください) において、``should`` メソッドを使って ``Example`` のグループを作り、``in`` メソッドを使って ``Result`` を含む ``Example`` を作ります。Play アプリケーションが起動している必要のある複数のテストのグループを作りたい場合、このアプリケーションを共有することはできず、以下のようにして、それぞれのテスト毎に新しいアプリケーションを提供しなければなりません:

<!-- ```scala
"Computer model" should {

  "be retrieved by id" in new WithApplication {
    // your test code
  }
  "be retrieved by email" in new WithApplication {
    // your test code
  }
}
``` -->
```scala
"Computer model" should {

  "be retrieved by id" in new WithApplication {
    // テストコード
  }
  "be retrieved by email" in new WithApplication {
    // テストコード
  }
}
```

<!-- In some cases, you want to run some operations with the application started before executing your example. Using Specs2
you can factor out your code by implementing your own ``org.specs2.specification.Around``, this can even extend one of
the built in arounds like in the following example: -->
テストを実行する前に、起動済みのアプリケーションを使っていくつかの操作を実行したいケースもいくつかあります。Specs2 を使えば、以下のようにして組み込みの arounds を拡張することすらできる ``org.specs2.specification.Around`` を独自に実装することで、コードを取り出すことができます:

<!-- ```scala
abstract class WithDbData extends WithApplication {
  override def around[T](t: => T)(implicit evidence: (T) => Result) = super.around {
    prepareDbWithData() 
    t
  }
}

"Computer model" should {

  "be retrieved by id" in new WithDbData {
       // your test code
  }
  "be retrieved by email" in new WithDbData {
       // your test code
  }
}
``` -->
```scala
abstract class WithDbData extends WithApplication {
  override def around[T](t: => T)(implicit evidence: (T) => Result) = super.around {
    prepareDbWithData() 
    t
  }
}

"Computer model" should {

  "be retrieved by id" in new WithDbData {
       // テストコード
  }
  "be retrieved by email" in new WithDbData {
       // テストコード
  }
}
```

<!-- ## Unit Testing Controllers -->
## コントローラのユニットテスト

<!-- Controllers are defined as objects in Play, and so can be trickier to unit test.  In Play 2.1 this can be alleviated by [dependency injection](https://github.com/playframework/Play20/wiki/ScalaDependencyInjection). Another way to finesse unit testing with a controller is to use a trait with an [explicitly typed self reference](http://www.naildrivin5.com/scalatour/wiki_pages/ExplcitlyTypedSelfReferences) to the controller: -->
Play においてコントローラはオブジェクトとして定義されているので、トリッキーなユニットテストを行うことができます。Play 2.1 では、この負担は [DI](https://github.com/playframework/Play20/wiki/ScalaDependencyInjection) で軽減することができます。コントローラのユニットテストを手際よく行う別の方法は、コントローラに対する [明示的で型安全な参照](http://www.naildrivin5.com/scalatour/wiki_pages/ExplcitlyTypedSelfReferences) と共にトレイトを使う方法です:

```scala
trait ExampleController {
  this: Controller =>

  def index() = {
     ...
  }
}

object ExampleController extends Controller with ExampleController
```

<!-- and then test the trait: -->
そして、このトレイトをテストします。

```scala
object ExampleControllerSpec extends Specification {

  class TestController() extends Controller with ExampleController

  "Example Page#index" should {
    "should be valid" in {
          val controller = new TestController()          
          val result = controller.index()          
          result must not beNull
      }
    }
  }
}
```

<!-- This approach can be extended with your choice of dependency injection framework (Subcut/Spring/Guice/Cake Pattern) to set up and inject mocks into the trait. -->
このアプローチは、モックをセットアップしてトレイトに差し込むために選択した DI フレームワーク (Subcut/Spring/Guice/Cake Pattern) によって拡張することができます。

<!--
> **Next:** [[Writing functional tests | ScalaFunctionalTest]]
-->
> **次ページ:** [[機能テスト | ScalaFunctionalTest]]