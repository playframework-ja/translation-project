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
テンプレートは Scala の標準的な関数であるため、テスト内で呼び出してその結果をチェックすればよいでしょう。

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
テストしたい `Action` に `FakeRequest` を渡すことで、コードを実行することができます。

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
`Action` を直接的に呼び出す代わりに、 `Router` に呼び出させることもできます。

```scala
"respond to the index Action" in {
  val Some(result) = route(FakeRequest(GET, "/Bob"))
  
  status(result) must equalTo(OK)
  contentType(result) must beSome("text/html")
  charset(result) must beSome("utf-8")
  contentAsString(result) must contain("Hello Bob")
}
```

<!--
## Starting a real HTTP server
-->
## テスト内で HTTP サーバを起動する

<!--
Sometimes you want to test the real HTTP stack from with your test, in which case you can start a test server:
-->
場合によっては、 HTTP スタック全体を通したテストを書きたいことがあると思います。その場合、テストサーバを起動するとよいでしょう。

```scala
"run in a server" in new WithServer {
  await(WS.url("http://localhost:" + port).get).status must equalTo(OK)
}
```

<!--
The `port` value contains the port number the server is running on, by default this is 19001, however you can change
this either by passing the port into the with `WithServer` constructor, or by setting the system property
`testserver.port`.  This can be useful for integrating with continuous integration servers, so that ports can be
dynamically reserved for each build.

A custom `FakeApplication` can also be passed to the test server, for example:
-->
`port` の値はサーバが起動するポート番号で、デフォルトは 19001 です。変更するためには `WithServer` の引数でポート番号を指定するか、システムプロパティ `testserver.port` を設定してください。この設定は、 CI サーバからテストを実行するためにビルド毎に動的にポート番号を変更したいような場合にも有用です。

```scala
"run in a server" in new WithServer(port = 3333, app = FakeApplication(additionalConfiguration = inMemoryDatabase)) {
  await(WS.url("http://localhost:3333").get).status must equalTo(OK)
}
```

<!--
## Testing from within a Web browser.
-->
## Web ブラウザを通してテストする

<!--
If you want to test your application using a browser, you can use [[Selenium WebDriver| http://code.google.com/p/selenium/?redir=1]]. Play will start the WebDriver for your, and wrap it in the convenient API provided by [[FluentLenium|https://github.com/FluentLenium/FluentLenium]].
-->
ブラウザを経由してアプリケーションのテストを行いたい場合、 [[Selenium WebDriver| http://code.google.com/p/selenium/?redir=1]] を使うとよいでしょう。 WebDriver は Play によって起動されて、 [[FluentLenium|https://github.com/FluentLenium/FluentLenium]] が提供する便利な API へ既にラップされた状態で利用できます。

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
Like `WithServer`, you can change the port, `FakeApplication`, and you can also select the web browser to use:
-->
`WithServer` と同様に、ポート番号や `FakeApplication` 、利用するブラウザなどを変更できます。

```scala
"run in a browser" in new WithBrowser(browser = FIREFOX) {
  ...
}
```

<!--
> **Next:** [[Advanced topics | Iteratees]]
-->
> **次ページ:** [[より高度なトピック | Iteratees]]
