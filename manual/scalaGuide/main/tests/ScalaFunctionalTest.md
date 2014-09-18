<!--
# Writing functional tests
-->
# 機能テストを書く

<!--
Play provides a number of classes and convenience methods that assist with functional testing.  Most of these can be found either in the [`play.api.test`](api/scala/index.html#play.api.test.package) package or in the [`Helpers`](api/scala/index.html#play.api.test.Helpers$) object.
-->
Play は機能テストを支援するいくつかのクラスとメソッドを提供しています。それらのほとんどは [`play.api.test`](api/scala/index.html#play.api.test.package) パッケージか、[`Helpers`](api/scala/index.html#play.api.test.Helpers$) オブジェクトのいずれかの中から見つけることができます。

<!--
You can add these methods and classes by importing the following:
-->
以下をインポートすることで、これらのメソッドとクラスを追加することができます:

```scala
import play.api.test._
import play.api.test.Helpers._
```

<!--
## FakeApplication
-->
## FakeApplication

<!--
Play frequently requires a running [`Application`](api/scala/index.html#play.api.Application) as context: it is usually provided from [`play.api.Play.current`](api/scala/index.html#play.api.Play$).
-->
Play はしばしば、[`play.api.Play.current`](api/scala/index.html#play.api.Play$) が提供する [`Application`](api/scala/index.html#play.api.Application) をコンテキストとして実行することを要求します: 

<!--
To provide an environment for tests, Play provides a [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) class which can be configured with a different Global object, additional configuration, or even additional plugins.
-->
テスト用の環境を提供するために、Play は 本番用とは異なる Global オブジェクトや追加設定、さらには追加プラグインにより設定することのできる [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) クラスを提供しています。

@[scalatest-fakeApplication](code/ScalaFunctionalTestSpec.scala)

<!--
## WithApplication
-->
## WithApplication

<!--
To pass in an application to an example, use [`WithApplication`](api/scala/index.html#play.api.test.WithApplication).  An explicit [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) can be passed in, but a default [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) is provided for convenience.
-->
サンプルコードにアプリケーションを渡すには、[`WithApplication`](api/scala/index.html#play.api.test.WithApplication) を使います。明示的に [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) を渡すこともできますが、利便性のためにデフォルトの [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) が提供されています。

<!--
Because [`WithApplication`](api/scala/index.html#play.api.test.WithApplication) is a built in [`Around`](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Structure.html#Around) block, you can override it to provide your own data population:
-->
[`WithApplication`](api/scala/index.html#play.api.test.WithApplication) は [`Around`](http://etorreborre.github.io/specs2/guide/org.specs2.guide.Structure.html#Around)  ブロックに組み込まれているので、自身のデータで上書きすることができます。

@[scalafunctionaltest-withdbdata](code/WithDbDataSpec.scala)

<!--
## WithServer
-->
## WithServer

<!--
Sometimes you want to test the real HTTP stack from with your test, in which case you can start a test server using [`WithServer`](api/scala/index.html#play.api.test.WithServer):
-->
ときどき、テストコードから実際の HTTP スタックをテストしたくなりますが、このような場合は [`WithServer`](api/scala/index.html#play.api.test.WithServer) を使ってテストサーバを起動することができます。

@[scalafunctionaltest-testpaymentgateway](code/ScalaFunctionalTestSpec.scala)

<!--
The `port` value contains the port number the server is running on.  By default this is 19001, however you can change this either by passing the port into the with [`WithServer`](api/scala/index.html#play.api.test.WithServer) constructor, or by setting the system property `testserver.port`.  This can be useful for integrating with continuous integration servers, so that ports can be dynamically reserved for each build.
-->
`port` の値には、サーバが起動しているポート番号が含まれています。デフォルトは 19001 番ですが、ポート番号を [`WithServer`](api/scala/index.html#play.api.test.WithServer) のコンストラクタに渡すか、`testserver.port` システムプロパティに設定することで変更することができます。これにより、ビルドごとにポート番号を動的に確保することができるので、CI サーバと統合するのに便利です。

<!--
A [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) can also be passed to the test server, which is useful for setting up custom routes and testing WS calls:
-->
カスタムルートを設定して WS 呼び出しをテストするのに便利な [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) をテストサーバに渡すこともできます:

@[scalafunctionaltest-testws](code/ScalaFunctionalTestSpec.scala)

<!--
## WithBrowser
-->
## WithBrowser

<!--
If you want to test your application using a browser, you can use [Selenium WebDriver](http://code.google.com/p/selenium/?redir=1). Play will start the WebDriver for your, and wrap it in the convenient API provided by [FluentLenium](https://github.com/FluentLenium/FluentLenium).
-->
ブラウザを使ってアプリケーションをテストしたい場合、[Selenium WebDriver](http://code.google.com/p/selenium/?redir=1) が使えます。Play は WebDriver を起動し、[FluentLenium](https://github.com/FluentLenium/FluentLenium) が提供する便利な API でラップします。

```scala
"run in a browser" in new WithBrowser {
  browser.goTo("/")
  browser.$("#title").getTexts().get(0) must equalTo("Hello Guest")

  browser.$("a").click()

  browser.url must equalTo("/")
  browser.$("#title").getTexts().get(0) must equalTo("Hello Coco")
}
```

<!--
Like [`WithServer`](api/scala/index.html#play.api.test.WithServer), you can change the port, [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication), and you can also select the web browser to use:
-->
[`WithServer`](api/scala/index.html#play.api.test.WithServer) 同様、ポート番号、 [`FakeApplication`](api/scala/index.html#play.api.test.FakeApplication) を変更することができますし、使用する web ブラウザを選択することもできます:

```scala
"run in a browser" in new WithBrowser(webDriver = FIREFOX) {
  ...
}
```

<!--
## PlaySpecification
-->
## PlaySpecification

<!--
[`PlaySpecification`](api/scala/index.html#play.api.test.PlaySpecification) excludes some of the mixins provided in the default specs2 specification that clash with Play helpers methods.  It also mixes in the Play test helpers and types for convenience.
-->
[`PlaySpecification`](api/scala/index.html#play.api.test.PlaySpecification) は、Play のヘルパーメソッドと一緒に使うとクラッシュする、デフォルトの specs2 スペックが提供するいくつかのミックスインを除外します。また、利便性のため Play テストヘルパと型をミックスインします。

@[scalatest-playspecification](code/ExamplePlaySpecificationSpec.scala)

<!--
## Testing a template
-->
## テンプレートのテスト

<!--
Since a template is a standard Scala function, you can execute it from your test, and check the result:
-->
テンプレートは標準的な Scala の関数なので、テストから実行して、結果を確認することができます:

@[scalatest-functionaltemplatespec](code/FunctionalTemplateSpec.scala)

<!--
## Testing a controller
-->
## コントローラのテスト

<!--
You can call any `Action` code by providing a [`FakeRequest`](api/scala/index.html#play.api.test.FakeRequest):
-->
[`FakeRequest`](api/scala/index.html#play.api.test.FakeRequest) を提供することで、あらゆる `Action` のコードを呼び出すことができます:

@[scalatest-functionalexamplecontrollerspec](code/FunctionalExampleControllerSpec.scala)

<!--
## Testing the router
-->
## ルータのテスト

<!--
Instead of calling the `Action` yourself, you can let the `Router` do it:
-->
自分で `Action` を呼び出す代わりに、`Router` にこれを行わせることができます:

@[scalafunctionaltest-respondtoroute](code/ScalaFunctionalTestSpec.scala)

<!--
## Testing a model
-->
## モデルのテスト

<!--
If you are using an SQL database, you can replace the database connection with an in-memory instance of an H2 database using `inMemoryDatabase`.
-->
SQL データベースを使っている場合、`inMemoryDatabase` を使ってデータベースコネクションを H2 データベースのインメモリインスタンスに置き換えることができます。

@[scalafunctionaltest-testmodel](code/ScalaFunctionalTestSpec.scala)

<!--
> **Next:** [[Advanced topics|Iteratees]]
-->
> **次ページ:** [[より高度なトピック|Iteratees]]