<!-- translated -->
<!--
# Writing functional tests
-->
# 機能テストを書く

<!--
## Testing a template
-->
## テンプレートのテスト

<!--
Since a template is a standard Scala function, you can execute it from your test, and check the result:
-->
テンプレートは単なる Scala の関数なので、テストから呼び出して結果をチェックすることができます。

```scala
"render index template" in {
  val html = views.html.index("Coco")
  
  contentType(html) must equalTo("text/html")
  contentAsString(html) must contain("Hello Coco")
}
```

<!--
## Testing your controllers
-->
## コントローラのテスト

<!--
You can call any `Action` code by providing a `FakeRequest`:
-->
`FakeRequest` を渡せば、どんな `Action` コードでも実行することができます。

```scala
"respond to the index Action" in {
  val result = controllers.Application.index("Bob")(FakeRequest())
  
  status(result) must equalTo(OK)
  contentType(result) must beSome("text/html")
  charset(result) must beSome("utf-8")
  contentAsString(result) must contain("Hello Bob")
}
```

<!--
## Testing the router
-->
## ルータのテスト

<!--
Instead of calling the `Action` yourself, you can let the `Router` do it:
-->
`Action` を自分で呼び出す代わりに、 `Router` に任せることもできます。

```scala
"respond to the index Action" in {
  val Some(result) = routeAndCall(FakeRequest(GET, "/Bob"))
  
  status(result) must equalTo(OK)
  contentType(result) must beSome("text/html")
  charset(result) must beSome("utf-8")
  contentAsString(result) must contain("Hello Bob")
}
```

<!--
## Starting a real HTTP server
-->
## 実際に HTTP サーバを起動する

<!--
Sometimes you want to test the real HTTP stack from with your test, in which case you can start a test server:
-->
実際の HTTP スタックを通したテストを実施したい場合、テストサーバを起動することができます。

```scala
"run in a server" in {
  running(TestServer(3333)) {
  
    await(WS.url("http://localhost:3333").get).status must equalTo(OK)
  
  }
}
```

<!--
## Testing from within a Web browser.
-->
## Web ブラウザからテストする

<!--
If you want to test your application using a browser, you can use [[Selenium WebDriver| http://code.google.com/p/selenium/?redir=1]]. Play will start the WebDriver for your, and wrap it in the convenient API provided by [[FluentLenium|https://github.com/FluentLenium/FluentLenium]].
-->
ブラウザを通してアプリケーションをテストしたい場合、 [[Selenium WebDriver| http://code.google.com/p/selenium/?redir=1]] を使うことができます。Play は WebDriver を起動した上で、 [[FluentLenium|https://github.com/FluentLenium/FluentLenium]] が提供する便利な API にラップします。

```scala
"run in a browser" in {
  running(TestServer(3333), HTMLUNIT) { browser =>
    
    browser.goTo("http://localhost:3333")
    browser.$("#title").getTexts().get(0) must equalTo("Hello Guest")
    
    browser.$("a").click()
    
    browser.url must equalTo("http://localhost:3333/Coco")
    browser.$("#title").getTexts().get(0) must equalTo("Hello Coco")

  }
}
```

<!--
> **Next:** [[Advanced topics | Iteratees]]
-->
> **次ページ:** [[上級者向けのトピック | Iteratees]]