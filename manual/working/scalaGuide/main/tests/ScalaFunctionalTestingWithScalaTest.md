<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Writing functional tests with ScalaTest
-->
# ScalaTest による機能テストの記述

<!--
Play provides a number of classes and convenience methods that assist with functional testing.  Most of these can be found either in the [`play.api.test`](api/scala/play/api/test/package.html) package or in the [`Helpers`](api/scala/play/api/test/Helpers$.html) object. The [_ScalaTest + Play_](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.package) integration library builds on this testing support for ScalaTest.
-->
Playには、機能テストを支援する、多くのクラスや便利なメソッドが用意されています。これらの大半は、[`play.api.test`](api/scala/play/api/test/package.html) パッケージか [`Helpers`](api/scala/play/api/test/Helpers$.html) オブジェクトに含まれています。[_ScalaTest + Play_](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.package) 統合ライブラリは、このテストに基づいていて、ScalaTest に対応しています。

<!--
You can access all of Play's built-in test support and _ScalaTest + Play_ with the following imports:
-->
すべての Play 組み込みのテスト支援と _ScalaTest + Play_ は、次のようなインポートによって利用できます。

```scala
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
```

<!--
## FakeApplication
-->
## FakeApplication

<!--
Play frequently requires a running [`Application`](api/scala/play/api/Application.html) as context: it is usually provided from [`play.api.Play.current`](api/scala/play/api/Play$.html).
-->
Play では頻繁に [`Application`](api/scala/play/api/Application.html) をコンテキストとして実行する必要がありますが、これはたいてい [`play.api.Play.current`](api/scala/play/api/Play$.html) によって提供されます。

<!--
To provide an environment for tests, Play provides a [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) class which can be configured with a different `Global` object, additional configuration, or even additional plugins.
-->
テスト用の環境を提供するために、Play は [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) クラスを提供します。このクラスは、異なる `Global` オブジェクト、追加の設定、追加のプラグインによって構成されています。

@[scalafunctionaltest-fakeApplication](code-scalatestplus-play/ScalaFunctionalTestSpec.scala)

<!--
If all or most tests in your test class need a `FakeApplication`, and they can all share the same `FakeApplication`, mix in trait [`OneAppPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneAppPerSuite). You can access the `FakeApplication` from the `app` field. If you need to customize the `FakeApplication`, override `app` as shown in this example:
-->
テストクラスの中の、すべてあるいはほとんどのテストが `FakeApplication` を必要とし、すべて同一の `FakeApplication` を共用可能な場合は、[`OneAppPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneAppPerSuite) トレイトをミックスインします。`app` フィールドから `FakeApplication` にアクセスできます。`FakeApplication` のカスタマイズが必要になったら、この例に示すように `app` をオーバーライドして下さい。

@[scalafunctionaltest-oneapppersuite](code-scalatestplus-play/oneapppersuite/ExampleSpec.scala)

<!--
If you need each test to get its own `FakeApplication`, instead of sharing the same one, use `OneAppPerTest` instead:
-->
同一の `FakeApplication` を共有するのではなく、それぞれのテストで独自の `FakeApplication` を取得する必要がある場合は、代わりに `OneAppPerTest` を使用します。

@[scalafunctionaltest-oneapppertest](code-scalatestplus-play/oneapppertest/ExampleSpec.scala)

<!--
The reason _ScalaTest + Play_ provides both `OneAppPerSuite` and `OneAppPerTest` is to allow you to select the sharing strategy that makes your tests run fastest. If you want application state maintained between successive tests, you'll need to use `OneAppPerSuite`. If each test needs a clean slate, however, you could either use `OneAppPerTest` or use `OneAppPerSuite`, but clear any state at the end of each test. Furthermore, if your test suite will run fastest if multiple test classes share the same application, you can define a master suite that mixes in [`OneAppPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneAppPerSuite) and nested suites that mix in [`ConfiguredApp`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredApp), as shown in the example in the [documentation for `ConfiguredApp`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredApp). You can use whichever strategy makes your test suite run the fastest.
-->
_ScalaTest + Play_ が `OneAppPerSuite` と `OneAppPerTest` の両方を提供する理由は、テストの実行を最も速くさせる方法を選択できるようにするためです。もし、連続したテストの中でアプリケーションの状態を維持したい場合は `OneAppPerSuite` を使う必要があります。しかし、各テストでクリーンな状態を望む場合は `OneAppPerTest` を用いるか、あるいは `OneAppPerSuite` を使用して各テストの最後で状態をクリアするかのいずれかになります。さらに、テストスイートが最も速く実行され、複数のテストクラスが同一アプリケーションを共有する場合、[`OneAppPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneAppPerSuite) をミックスインした親のスイートと、[`ConfiguredApp`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredApp) をミックスインした入れ子のスイートを定義できます。この例については [`ConfiguredApp` のドキュメント](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredApp) で参照できます。どちらの方法でも、テストスイートを最も速く実行できるほうを使用できます。

<!--
## Testing with a server
-->
## サーバーでのテスト

<!--
Sometimes you want to test with the real HTTP stack. If all tests in your test class can reuse the same server instance, you can mix in [`OneServerPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerSuite) (which will also provide a new `FakeApplication` for the suite):
-->
時々、実際の HTTP スタックでテストをしたい時があります。もし、テストクラスのすべてのテストが同一サーバーインスタンスを再利用可能な場合、[`OneServerPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerSuite) をミックスインできます(スイート用の新規の `FakeApplication` も提供されます)。

@[scalafunctionaltest-oneserverpersuite](code-scalatestplus-play/oneserverpersuite/ExampleSpec.scala)

<!--
If all tests in your test class require separate server instance, use [`OneServerPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerTest) instead (which will also provide a new `FakeApplication` for the suite):
-->
もし、テストクラスのすべてのテストが独立したサーバーインスタンスを要求する場合は、代わりに [`OneServerPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerTest) を使用します(スイート用の新規の `FakeApplication` も提供されます)。

@[scalafunctionaltest-oneserverpertest](code-scalatestplus-play/oneserverpertest/ExampleSpec.scala)

<!--
The `OneServerPerSuite` and `OneServerPerTest` traits provide the port number on which the server is running as the `port` field.  By default this is 19001, however you can change this either overriding `port` or by setting the system property `testserver.port`.  This can be useful for integrating with continuous integration servers, so that ports can be dynamically reserved for each build.
-->
`OneServerPerSuite` および `OneServerPerTest` トレイトは、サーバーが実行しているポート番号を `port` フィールドとして提供します。デフォルトは 19001 ですが、`port` を上書きするか、システムプロパティ `testserver.port` を設定することにより変更できます。これは、各ビルドごとにポートを動的に予約できるので、継続的な統合サーバーとの統合に役立ちます。

<!--
You can also customize the [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) by overriding `app`, as demonstrated in the previous examples.
-->
前の例で示したように、`app` を上書きすることで [`FakeApplication`](api/scala/play/api/test/FakeApplication.html) をカスタマイズすることもできます。

<!--
Lastly, if allowing multiple test classes to share the same server will give you better performance than either the `OneServerPerSuite` or `OneServerPerTest` approaches, you can define a master suite that mixes in [`OneServerPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerSuite) and nested suites that mix in [`ConfiguredServer`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredServer), as shown in the example in the [documentation for `ConfiguredServer`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredServer).
-->
最後に、複数のテストクラスが同一サーバーを共用できるようにすると、`OneServerPerSuite` や `OneServerPerTest` のアプローチよりも良いパフォーマンスが得られ、[`OneServerPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerSuite) をミックスインするマスタースイートと [`ConfiguredServer`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredServer) をミックインする入れ子のスイートを定義できます。[`ConfiguredServer` のドキュメント](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredServer) で例で示しています。

<!--
## Testing with a web browser
-->
## Web ブラウザでのテスト

<!--
The _ScalaTest + Play_ library builds on ScalaTest's [Selenium DSL](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.selenium.WebBrowser) to make it easy to test your Play applications from web browsers.
-->
_ScalaTest + Play_ ライブラリは ScalaTest の [Selenium DSL](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.selenium.WebBrowser) を利用して、Web ブラウザでの Play アプリケーションのテストを簡単にします。

<!--
To run all tests in your test class using a same browser instance, mix [`OneBrowserPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneBrowserPerSuite) into your test class. You'll also need to mix in a [`BrowserFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.BrowserFactory) trait that will provide a Selenium web driver: one of [`ChromeFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ChromeFactory), [`FirefoxFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.FirefoxFactory), [`HtmlUnitFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.HtmlUnitFactory), [`InternetExplorerFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.InternetExplorerFactory), [`SafariFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.SafariFactory).
-->
テストクラスのすべてのテストを同一ブラウザインスタンスを使用して実行するには、テストクラスに [`OneBrowserPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneBrowserPerSuite) をミックスインします。また、[`ChromeFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ChromeFactory) 、 [`FirefoxFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.FirefoxFactory) 、 [`HtmlUnitFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.HtmlUnitFactory) 、 [`InternetExplorerFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.InternetExplorerFactory) 、 [`SafariFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.SafariFactory) のいずれかを Selenium Web ドライバとして提供する [`BrowserFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.BrowserFactory) トレイトをミックスインする必要もあります。

<!--
In addition to mixing in a [`BrowserFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.BrowserFactory), you will need to mix in a [`ServerProvider`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ServerProvider) trait that provides a `TestServer`: one of [`OneServerPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerSuite), [`OneServerPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerTest), or [`ConfiguredServer`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredServer).
-->
[`BrowserFactory`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.BrowserFactory) のミックスインに加え、[`OneServerPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerSuite) 、
 [`OneServerPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneServerPerTest) 、 [`ConfiguredServer`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredServer) のいずれかを `TestServer` として提供する [`ServerProvider`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ServerProvider) トレイトをミックスインする必要もあります。

<!--
For example, the following test class mixes in `OneServerPerSuite` and `HtmUnitFactory`:
-->
例として、`OneServerPerSuite` と `HtmUnitFactory` をミックスインした次のようなテストクラスがあります。

@[scalafunctionaltest-onebrowserpersuite](code-scalatestplus-play/onebrowserpersuite/ExampleSpec.scala)

<!--
If each of your tests requires a new browser instance, use [`OneBrowserPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneBrowserPerSuite) instead. As with `OneBrowserPerSuite`, you'll need to also mix in a `ServerProvider` and `BrowserFactory`:
-->
もし、それぞれのテストが新しいブラウザインスタンスを必要とする場合、代わりに [`OneBrowserPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneBrowserPerSuite) を使用します。`OneBrowserPerSuite` と同様に、`ServerProvider` と `BrowserFactory` をミックスインする必要があります。

@[scalafunctionaltest-onebrowserpertest](code-scalatestplus-play/onebrowserpertest/ExampleSpec.scala)

<!--
If you need multiple test classes to share the same browser instance, mix [`OneBrowserPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneBrowserPerSuite) into a master suite and [`ConfiguredBrowser`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredBrowser) into multiple nested suites. The nested suites will all share the same web browser. For an example, see the [documentation for trait `ConfiguredBrowser`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredBrowser).
-->
もし、同一のブラウザインスタンスを共有する複数のテストクラスが必要な場合、[`OneBrowserPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.OneBrowserPerSuite) を親のスイートにミックスインし、[`ConfiguredBrowser`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredBrowser) を複数の入れ子スイートにミックスインします。この入れ子スイートはすべて同一の Web ブラウザを共有します。例については [`ConfiguredBrowser` トレイトのドキュメント](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.ConfiguredBrowser) を参照してください。

<!--
## Running the same tests in multiple browsers
-->
## 複数ブラウザでの同一テストの実行

<!--
If you want to run tests in multiple web browsers, to ensure your application works correctly in all the browsers you support, you can use traits [`AllBrowsersPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerSuite) or [`AllBrowsersPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerTest). Both of these traits declare a `browsers` field of type `IndexedSeq[BrowserInfo]` and an abstract `sharedTests` method that takes a `BrowserInfo`. The `browsers` field indicates which browsers you want your tests to run in. The default is Chrome, Firefox, Internet Explorer, `HtmlUnit`, and Safari. You can override `browsers` if the default doesn't fit your needs. You place tests you want run in multiple browsers in the `sharedTests` method, placing the name of the browser at the end of each test name. (The browser name is available from the `BrowserInfo` passed into `sharedTests`.) Here's an example that uses `AllBrowsersPerSuite`:
-->
もし、複数の Web ブラウザでテストを行いたい場合は、対応させたいすべてのブラウザでアプリケーションが正しく動作するように、[`AllBrowsersPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerSuite) または [`AllBrowsersPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerTest) トレイトを使用できます。これらのトレイトはいずれも `IndexedSeq[BrowserInfo]` 型の `browsers` フィールドと、`BrowserInfo` を引数に取る abstract メソッド `sharedTests` を宣言します。`browsers` フィールドはテストを実行したいブラウザを示しています。デフォルトは、Chrome、Firefox、Internet Explorer、 `HtmlUnit` 、Safari です。希望するブラウザとデフォルトが異なる場合は `browsers` を上書きできます。複数のブラウザで実行させたいテストを `sharedTests` メソッドに配置し、それぞれのテスト名の末尾にブラウザの名前を記述します。(ブラウザの名前は `sharedTests` に渡される `BrowserInfo` から取得できます) `AllBrowsersPerSuite` を使用した例はこちらです。

@[scalafunctionaltest-allbrowserspersuite](code-scalatestplus-play/allbrowserspersuite/ExampleSpec.scala)

<!--
All tests declared by `sharedTests` will be run with all browsers mentioned in the `browsers` field, so long as they are available on the host system. Tests for any browser that is not available on the host system will be canceled automatically. Note that you need to append the `browser.name` manually to the test name to ensure each test in the suite has a unique name (which is required by ScalaTest). If you leave that off, you'll get a duplicate-test-name error when you run your tests.
-->
`sharedTests` によって宣言されたすべてのテストは、`browsers` フィールドで定義されている、ホストシステムで利用可能なすべてのブラウザで実行されます。ホストシステムで利用できない、いずれのブラウザのテストも自動的にキャンセルされます。スイートの各テストが (ScalaTest によって要求された) 一意的な名前を持つように、テスト名に `browser.name` を手動で追加する必要があることに注意して下さい。もしこれを行わなかった場合、テストの実行時にテスト名の重複エラーが発生します。

<!--
[`AllBrowsersPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerSuite) will create a single instance of each type of browser and use that for all the tests declared in `sharedTests`. If you want each test to have its own, brand new browser instance, use [`AllBrowsersPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerTest) instead:
-->
[`AllBrowsersPerSuite`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerSuite) はブラウザのタイプごとに 1 つのインスタンスを生成し、`sharedTests` で宣言したすべてのテストに対して使用します。もし、各テストで独自の新しいブラウザインスタンスが必要な場合は、代わりに [`AllBrowsersPerTest`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.AllBrowsersPerTest) を使用してくだい。

@[scalafunctionaltest-allbrowserspertest](code-scalatestplus-play/allbrowserspertest/ExampleSpec.scala)

<!--
Although both `AllBrowsersPerSuite` and `AllBrowsersPerTest` will cancel tests for unavailable browser types, the tests will show up as canceled in the output.  To can clean up the output, you can exclude web browsers that will never be available by overriding `browsers`, as shown in this example:
-->
`AllBrowsersPerSuite` と `AllBrowsersPerTest` は共に、利用できないブラウザタイプのテストはキャンセルしますが、テストがキャンセルされたこと自体は出力で明示されます。出力を綺麗にするために、次の例のように `browsers` を上書きすることにより、利用できない Web ブラウザを除外できます。

@[scalafunctionaltest-allbrowserspersuite](code-scalatestplus-play/allbrowserspersuite/ExampleOverrideBrowsersSpec.scala)

<!--
The previous test class will only attempt to run the shared tests with Firefox and Chrome (and cancel tests automatically if a browser is not available).
-->
前述のテストクラスは、Firefox と Chrome で共有されたテストの実行を試みるだけです (そしてブラウザが利用できない場合は自動的にテストはキャンセルされます)。

<!--
## PlaySpec
-->
## PlaySpec

<!--
`PlaySpec` provides a convenience "super Suite" ScalaTest base class for Play tests, You get `WordSpec`, `MustMatchers`, `OptionValues`, and `WsScalaTestClient` automatically by extending `PlaySpec`:
-->
`PlaySpec` は Play のテストでの便利な「スーパースイート」 ScalaTest ベースクラスを提供し、`PlaySpec` の継承によって、 `WordSpec` 、 `MustMatchers` 、 `OptionValues` 、 `WsScalaTestClient` を自動的に利用できます。

@[scalafunctionaltest-playspec](code-scalatestplus-play/playspec/ExampleSpec.scala)

<!--
You can mix any of the previously mentioned traits into `PlaySpec`.
-->
前述のいずれのトレイトも `PlaySpec` にミックスインできます。

<!--
## When different tests need different fixtures
-->
## 異なるテストが異なるフィクスチャーを必要とする場合

<!--
In all the test classes shown in previous examples, all or most tests in the test class required the same fixtures. While this is common, it is not always the case. If different tests in the same test class need different fixtures, mix in trait [`MixedFixtures`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures). Then give each individual test the fixture it needs using one of these no-arg functions: [App](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$App), [Server](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Server), [Chrome](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Chrome), [Firefox](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Firefox), [HtmlUnit](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$HtmlUnit), [InternetExplorer](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$InternetExplorer), or [Safari](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Safari).
-->
前述の例で示したすべてのテストクラスにおいて、テストクラスの中のすべてもしくはほとんどのテストは同一のフィクスチャーを必要とします。これは一般的ですが、必ずしもそうではありません。同一テストクラスの中の異なるテストが異なるフィクスチャーを必要とする場合、[`MixedFixtures`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures) トレイトをミックスインします。そして、次のような引数なし関数のいずれかを使用して、必要なフィクスチャーを個々のテストに与えます。 [App](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$App) 、 [Server](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Server) 、 [Chrome](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Chrome) 、 [Firefox](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Firefox) 、 [HtmlUnit](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$HtmlUnit) 、 [InternetExplorer](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$InternetExplorer) 、 [Safari](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures$Safari) 。

<!--
You cannot mix [`MixedFixtures`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures) into [`PlaySpec`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.PlaySpec) because `MixedFixtures` requires a ScalaTest [`fixture.Suite`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.fixture.Suite) and `PlaySpec` is just a regular [`Suite`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.Suite). If you want a convenient base class for mixed fixtures, extend [`MixedPlaySpec`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedPlaySpec) instead. Here's an example:
-->
`MixedFixtures` は ScalaTest の [`fixture.Suite`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.fixture.Suite) を必要とし、`PlaySpec` は まさに標準の [`Suite`](http://doc.scalatest.org/2.1.5/index.html#org.scalatest.Suite) であるため、[`MixedFixtures`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedFixtures) を [`PlaySpec`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.PlaySpec) にミックスインできません。ミックスインされたフィクスチャー向けの便利なベースクラスが欲しい場合は、代わりに [`MixedPlaySpec`](http://doc.scalatest.org/plus-play/1.0.0/index.html#org.scalatestplus.play.MixedPlaySpec) を継承します。例はこちらです。

@[scalafunctionaltest-mixedfixtures](code-scalatestplus-play/mixedfixtures/ExampleSpec.scala)

<!--
## Testing a template
-->
テンプレートのテスト

<!--
Since a template is a standard Scala function, you can execute it from your test, and check the result:
-->
テンプレートは標準的な Scala の機能なので、テストから実行でき、そして結果を確認できます。

@[scalafunctionaltest-testview](code-scalatestplus-play/ScalaFunctionalTestSpec.scala)

<!--
## Testing a controller
-->
## コントローラーのテスト

<!--
You can call any `Action` code by providing a [`FakeRequest`](api/scala/play/api/test/FakeRequest.html):
-->
[`FakeRequest`](api/scala/play/api/test/FakeRequest.html) の提供による、いずれの `Action` コードも呼び出すことができます。

@[scalatest-examplecontrollerspec](code-scalatestplus-play/ExampleControllerSpec.scala)

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

@[scalafunctionaltest-respondtoroute](code-scalatestplus-play/ScalaFunctionalTestSpec.scala)

<!--
## Testing a model
-->
## モデルのテスト

<!--
If you are using an SQL database, you can replace the database connection with an in-memory instance of an H2 database using `inMemoryDatabase`.
-->
SQL データベースを使用する時、`inMemoryDatabase` を使用して H2 データベースのインメモリインスタンスでデータベース接続を置き換えることができます。

@[scalafunctionaltest-testmodel](code-scalatestplus-play/ScalaFunctionalTestSpec.scala)

<!--
## Testing WS calls
-->
## WS 呼び出しのテスト

<!--
If you are calling a web service, you can use [`WSTestClient`](api/scala/play/api/test/WsTestClient.html).  There are two calls available, `wsCall` and `wsUrl` that will take a Call or a string, respectively.  Note that they expect to be called in the context of `WithApplication`.
-->
Web サービスの呼び出しを行う時、[`WSTestClient`](api/scala/play/api/test/WsTestClient.html) を使用することができます。`wsCall` と `wsUrl` の 2 つの呼び出しが可能で、それぞれ Call および文字列を引数に取ります。`WithApplication` のコンテキスト内での呼び出しが期待されていることに注意してください。

```
wsCall(controllers.routes.Application.index()).get()
```

```
wsUrl("http://localhost:9000").get()
```
