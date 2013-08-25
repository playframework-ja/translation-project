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
As a template is a standard Scala function, you can execute it from a test and check the result:
-->
テンプレートは単なる Scala の関数なので、テストから呼び出して結果をチェックすることができます。

```
@Test
public void renderTemplate() {
    Content html = views.html.index.render("Coco");
    assertThat(contentType(html)).isEqualTo("text/html");
    assertThat(contentAsString(html)).contains("Coco");
}
```

<!--
## Testing your controllers
-->
## コントローラのテスト

<!--
You can also retrieve an action reference from the reverse router, such as `controllers.routes.ref.Application.index`. You can then invoke it:
-->
`controllers.routes.ref.Application.index` というようにリバースルーターからアクションへの参照を受け取って、呼び出すこともできます。

```
@Test
public void callIndex() {
    Result result = callAction(controllers.routes.ref.Application.index("Kiki"));   
    assertThat(status(result)).isEqualTo(OK);
    assertThat(contentType(result)).isEqualTo("text/html");
    assertThat(charset(result)).isEqualTo("utf-8");
    assertThat(contentAsString(result)).contains("Hello Kiki");
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

```
@Test
public void badRoute() {
    Result result = routeAndCall(fakeRequest(GET, "/xx/Kiki"));
    assertThat(result).isNull();
}
```

<!--
## Starting a real HTTP server
-->
## 実際に HTTP サーバを起動する

<!--
Sometimes you want to test the real HTTP stack from with your test. You can do this by starting a test server:
-->
実際の HTTP スタックを通したテストを記述したい場合もあります。その場合は、次のようにテストサーバを起動することができます。

```
@Test
public void testInServer() {
  running(testServer(3333), new Callback0() {
      public void invoke() {
         assertThat(
           WS.url("http://localhost:3333").get().get().status
         ).isEqualTo(OK);
      }
  });
}
```

<!--
If you want to test your application from with a Web browser, you can use [[Selenium WebDriver| http://code.google.com/p/selenium/?redir=1]]. Play will start the WebDriver for your, and wrap it in the convenient API provided by [[FluentLenium|https://github.com/FluentLenium/FluentLenium]].
-->
Web ブラウザを通してアプリケーションをテストしたい場合、[[Selenium WebDriver| http://code.google.com/p/selenium/?redir=1]] を使うことができます。Play は WebDriver を初期化した上で、 [[FluentLenium|https://github.com/FluentLenium/FluentLenium]] が提供する便利な API にラップします。

```
@Test
public void runInBrowser() {
    running(testServer(3333), HTMLUNIT, new Callback<TestBrowser>() {
        public void invoke(TestBrowser browser) {
           browser.goTo("http://localhost:3333"); 
           assertThat(browser.$("#title").getTexts().get(0)).isEqualTo("Hello Guest");
           browser.$("a").click();
           assertThat(browser.url()).isEqualTo("http://localhost:3333/Coco");
           assertThat(browser.$("#title", 0).getText()).isEqualTo("Hello Coco");
        }
    });
}
```
