<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Writing functional tests with specs2
-->
# specs2 による機能テストの記述

<!--
Play provides a number of classes and convenience methods that assist with functional testing.  Most of these can be found either in the [`play.api.test`](api/scala/play/api/test/package.html) package or in the [`Helpers`](api/scala/play/api/test/Helpers$.html) object.
-->
Playには、機能テストを支援する、多くのクラスや便利なメソッドが用意されています。これらの大半は、[`play.api.test`](api/scala/play/api/test/package.html) パッケージか [`Helpers`](api/scala/play/api/test/Helpers$.html) オブジェクトに含まれています。

<!--
You can add these methods and classes by importing the following:
-->
これらのメソッドやクラスは次のインポートによって追加できます。

```scala
import play.api.test._
import play.api.test.Helpers._
```

<!--
## FakeApplication
-->
## FakeApplication

<!--
Play frequently requires a running [`Application`](api/scala/play/api/Application.html) as context: it is usually provided from [`play.api.Play.current`](api/scala/play/api/Play$.html).
-->
Play では頻繁に実行中の [`Application`](api/scala/play/api/Application.html) をコンテキストとして実行する必要がありますが、これはたいてい [`play.api.Play.current`](api/scala/play/api/Play$.html) によって提供されます。

<!--
To provide an environment for tests, Play provides a [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) class which can be configured with a different Global object, additional configuration, or even additional plugins.
-->
テスト用の環境を提供するために、Play は [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) クラスを提供します。このクラスは、異なる Global オブジェクト、追加の設定、追加のプラグインによって構成されています。

@[scalafunctionaltest-fakeApplication](code/specs2/ScalaFunctionalTestSpec.scala)

<!--
## WithApplication
-->
## WithApplication

<!--
To pass in an application to an example, use [`WithApplication`](api/scala/play/api/test/WithApplication.html).  An explicit [`Application`](api/scala/play/api/Application.html) can be passed in, but a default [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) is provided for convenience.
-->
アプリケーションをサンプルに渡すには [`WithApplication`](api/scala/play/api/test/WithApplication.html) を使用します。明示的な [`Application`](api/scala/play/api/Application.html) を渡すことができますが、便宜上デフォルトの [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) が提供されます。

<!--
Because [`WithApplication`](api/scala/play/api/test/WithApplication.html) is a built in [`Around`](https://etorreborre.github.io/specs2/guide/SPECS2-3.4/org.specs2.guide.Contexts.html#aroundeach) block, you can override it to provide your own data population:
-->
[`WithApplication`](api/scala/play/api/test/WithApplication.html) は組み込みの [`Around`](https://etorreborre.github.io/specs2/guide/SPECS2-3.4/org.specs2.guide.Contexts.html#aroundeach) ブロックなので、独自のデータ集合を提供するために上書きできます。

@[scalafunctionaltest-withdbdata](code/specs2/WithDbDataSpec.scala)

<!--
## WithServer
-->
## WithServer

<!--
Sometimes you want to test the real HTTP stack from within your test, in which case you can start a test server using [`WithServer`](api/scala/play/api/test/WithServer.html):
-->
時々、テストから実際の HTTP スタックをテストをしたい時があります。その場合、[`WithServer`](api/scala/play/api/test/WithServer.html) を用いてテストサーバーを開始することができます。

@[scalafunctionaltest-testpaymentgateway](code/specs2/ScalaFunctionalTestSpec.scala)

<!--
The `port` value contains the port number the server is running on.  By default this is 19001, however you can change this either by passing the port into [`WithServer`](api/scala/play/api/test/WithServer.html), or by setting the system property `testserver.port`.  This can be useful for integrating with continuous integration servers, so that ports can be dynamically reserved for each build.
-->
`port` 値は、サーバーが実行しているポート番号を含んでいます。デフォルトは 19001 ですが、ポートを [`WithServer`](api/scala/play/api/test/WithServer.html) に渡すか、システムプロパティ `testserver.port` を設定することにより変更できます。継続的統合サーバーに組み入れる際に便利なので、各ビルドごとにポートを動的に予約できます。

<!--
A [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) can also be passed to the test server, which is useful for setting up custom routes and testing WS calls:
-->
[`FakeApplication`](api/scala/play/api/test/FakeApplication.html) もまた、テストサーバーに渡すことができ、これは、カスタムルートの設定や WS 呼び出しのテストに便利です。

@[scalafunctionaltest-testws](code/specs2/ScalaFunctionalTestSpec.scala)

<!--
## WithBrowser
-->
## WithBrowser

<!--
If you want to test your application using a browser, you can use [Selenium WebDriver](https://github.com/seleniumhq/selenium). Play will start the WebDriver for you, and wrap it in the convenient API provided by [FluentLenium](https://github.com/FluentLenium/FluentLenium) using [`WithBrowser`](api/scala/play/api/test/WithBrowser.html).  Like [`WithServer`](api/scala/play/api/test/WithServer.html), you can change the port, [`Application`](api/scala/play/api/Application.html), and you can also select the web browser to use:
-->
もし、ブラウザを使用したアプリケーションのテストを行いたい場合は、[Selenium WebDriver](https://github.com/seleniumhq/selenium) を使用できます。Play は WebDriver を開始し、[`WithBrowser`](api/scala/play/api/test/WithBrowser.html) を用いた [FluentLenium](https://github.com/FluentLenium/FluentLenium) から提供された便利な API によってラップします。[`WithServer`](api/scala/play/api/test/WithServer.html) のように、ポートを変更でき、使用する web ブラウザを選択することもできます。

@[scalafunctionaltest-testwithbrowser](code/specs2/ScalaFunctionalTestSpec.scala)

<!--
## PlaySpecification
-->
## PlaySpecification

<!--
[`PlaySpecification`](api/scala/play/api/test/PlaySpecification.html) is an extension of [`Specification`](https://etorreborre.github.io/specs2/api/SPECS2-3.4/index.html#org.specs2.mutable.Specification) that excludes some of the mixins provided in the default specs2 specification that clash with Play helpers methods.  It also mixes in the Play test helpers and types for convenience.
-->
[`PlaySpecification`](api/scala/play/api/test/PlaySpecification.html) は [`Specification`](https://etorreborre.github.io/specs2/api/SPECS2-3.4/index.html#org.specs2.mutable.Specification) の拡張で、デフォルトの spec2 で提供された、Play のヘルパーメソッドと競合するいくつかのミックスインは含まれていません。Play のテストヘルパーと型は便宜上ミックスインしています。

@[scalafunctionaltest-playspecification](code/specs2/ExamplePlaySpecificationSpec.scala)

<!--
## Testing a view template
-->
## ビューテンプレートのテスト

<!--
Since a template is a standard Scala function, you can execute it from your test, and check the result:
-->
テンプレートは標準的な Scala の機能なので、テストから実行でき、そして結果を確認できます。

@[scalafunctionaltest-testview](code/specs2/ScalaFunctionalTestSpec.scala)

<!--
## Testing a controller
-->
## コントローラーのテスト

<!--
You can call any `Action` code by providing a [`FakeRequest`](api/scala/play/api/test/FakeRequest.html):
-->
[`FakeRequest`](api/scala/play/api/test/FakeRequest.html) の提供による、いずれの `Action` コードも呼び出すことができます。

@[scalafunctionaltest-functionalexamplecontrollerspec](code/specs2/FunctionalExampleControllerSpec.scala)

<!--
Technically, you don't need [`WithApplication`](api/scala/play/api/test/WithApplication.html) here, although it wouldn't hurt anything to have it.
-->
技術的に、ここで [`WithApplication`](api/scala/play/api/test/WithApplication.html) は不要ですが、それにより何かを損なうことはありません。

<!--
## Testing the router
-->
## ルーターのテスト

<!--
Instead of calling the `Action` yourself, you can let the `Router` do it:
-->
自分で `Action` を呼び出す代わりに、`Router` にそれをさせることができます。

@[scalafunctionaltest-respondtoroute](code/specs2/ScalaFunctionalTestSpec.scala)

<!--
## Testing a model
-->
## モデルのテスト

<!--
If you are using an SQL database, you can replace the database connection with an in-memory instance of an H2 database using `inMemoryDatabase`.
-->
SQL データベースを使用する時、`inMemoryDatabase` を使用して H2 データベースのインメモリインスタンスでデータベース接続を置き換えることができます。

@[scalafunctionaltest-testmodel](code/specs2/ScalaFunctionalTestSpec.scala)
