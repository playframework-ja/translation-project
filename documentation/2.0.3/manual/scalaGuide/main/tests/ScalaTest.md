<!-- translated -->
<!--
# Testing your application
-->
# アプリケーションのテスト

<!--
Test source files must be placed in your application’s `test` folder. You can run them from the Play console using the `test` and `test-only` tasks.
-->
テストのソースファイルは `test` フォルダに配置します。 Play コンソールで `test` や `test-only` タスクを実行すると、テストを実行することができます。

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

<!--
If the code you want to test depends of a running application, you can easily create a `FakeApplication` on the fly:
-->
起動中のアプリケーションの依存するコードをテストする場合は、簡単に `FakeApplication` を利用することができます。

```scala
"Computer model" should {

  "be retrieved by id" in {
    running(FakeApplication()) {
  
      val Some(macintosh) = Computer.findById(21)

      macintosh.name must equalTo("Macintosh")
      macintosh.introduced must beSome.which(dateIs(_, "1984-01-24"))  
  
    }
  }
}
```

<!--
You can also pass (or override) additional configuration to the fake application, or mock any plug-in. For example to create a `FakeApplication` using a `default` in memory database:
-->
このフェイクアプリケーションに対して設定値を追加 (または上書き) したり、プラグインをモックすることも可能です。例えば、 `default` という名前の インメモリデータベースに接続された `FakeApplication` を起動する場合は、次のように書きます。

```scala
FakeApplication(additionalConfiguration = inMemoryDatabase())
```

<!--
> **Next:** [[Writing functional tests | ScalaFunctionalTest]]
-->
> **次ページ:** [[機能テスト | ScalaFunctionalTest]]