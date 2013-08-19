<!-- translated -->
<!--
# Scala templates common use cases
-->
# Scala テンプレートのよくある使い方

<!--
Templates, being simple functions, can be composed in any way you want. Below are examples of some common scenarios.
-->
テンプレートは単純な関数なので、いかようにも組み合わせることができます。このページでは、よくある利用シナリオを紹介していきます。

<!--
## Layout
-->
## レイアウト

<!--
Let’s declare a `views/main.scala.html` template that will act as a main layout template:
-->
メインレイアウトとして機能する `views/main.scala.html` というテンプレートを定義してみましょう。

```html
@(title: String)(content: Html)
<!DOCTYPE html>
<html>
  <head>
    <title>@title</title>
  </head>
  <body>
    <section class="content">@content</section>
  </body>
</html>

```

<!--
As you can see, this template takes two parameters: a title and an HTML content block. Now we can use it from another `views/Application/index.scala.html` template:
-->
ご覧のとおり、このテンプレートはタイトルと HTML コンテンツを含むブロックの二つの引数を取ります。このテンプレートは、例えば `views/Application/index.scala.html` のような別のテンプレートから、次のように利用することができます。

```html
@main(title = "Home") {
    
  <h1>Home page</h1>
    
}
```

<!--
> **Note:** We sometimes use named parameters(like `@main(title = "Home")`, sometimes not like `@main("Home")`. It is as you want, choose whatever is clearer in a specific context.
-->
> **Note:** `@main("Home")` の代わりに、名前付き引数を使って `@main(title = "Home")` のように書くこともあります。どちらを使っても良いのですが、時と場合に応じてテンプレートが読みやすくなる方を選ぶとよいでしょう。

<!--
Sometimes you need a second page-specific content block for a sidebar or breadcrumb trail, for example. You can do this with an additional parameter:
-->
例えば、ページのメインコンテンツ以外にサイドバーやパンくずなどに副次的な内容を埋め込みたい場合、次のようにパラメータを追加するとよいでしょう。

```html
@(title: String)(sidebar: Html)(content: Html)
<!DOCTYPE html>
<html>
  <head>
    <title>@title</title>
  </head>
  <body>
    <section class="sidebar">@sidebar</section>
    <section class="content">@content</section>
  </body>
</html>
```

<!--
Using this from our ‘index’ template, we have:
-->
これを先ほどの `index` テンプレートから、以下のように利用することができます。

```html
@main("Home") {
  <h1>Sidebar</h1>

} {
  <h1>Home page</h1>

}
```

<!--
Alternatively, we can declare the sidebar block separately:
-->
別の書き方として、サイドバー向けのブロックをレイアウトの呼び出しとは全く別に宣言することもできます。

```html
@sidebar = {
  <h1>Sidebar</h1>
}

@main("Home")(sidebar) {
  <h1>Home page</h1>

}
```


<!--
## Tags (they are just functions, right?)
-->
## タグ

<!--
Let’s write a simple `views/tags/notice.scala.html` tag that displays an HTML notice:
-->
Web ページ上に通知を表示するための単純なタグ `views/tags/notice.scala.html` を書いてみましょう。

```html
@(level: String = "error")(body: (String) => Html)
 
@level match {
    
  case "success" => {
    <p class="success">
      @body("green")
    </p>
  }

  case "warning" => {
    <p class="warning">
      @body("orange")
    </p>
  }

  case "error" => {
    <p class="error">
      @body("red")
    </p>
  }
    
}
```

<!--
And now let’s use it from another template:
-->
このタグを別のテンプレートから呼び出すには、次のように書きます。

```html
@import tags._
 
@notice("error") { color =>
  Oops, something is <span style="color:@color">wrong</span>
}
```

<!--
## Includes
-->
## インクルード

<!--
Again, there’s nothing special here. You can just call any other template you like (and in fact any other function coming from anywhere at all):
-->
これも、特別なことは何もありません。次のように、他のテンプレートを単に呼び出すだけで OK です (実際には、テンプレートに限らず、スコープ内にある関数であればなんでも呼び出せます) 。

```html
<h1>Home</h1>
 
<div id="side">
  @common.sideBar()
</div>
```

<!--
> **Next:** [[HTTP form submission and validation | ScalaForms]]
-->
> **次ページ:** [[HTTP フォーム送信とバリデーション | ScalaForms]]