<!-- translated -->
<!--
# The template engine
-->
# テンプレートエンジン

<!--
## A type safe template engine based on Scala
-->
## Scala ベースのタイプセーフなテンプレートエンジン

<!--
Play 2.0 comes with a new and really powerful Scala-based template engine. This new template engine’s design was inspired by ASP.NET Razor. Specifically it is:
-->
Play 2.0 には強力な Scala ベースのテンプレートエンジンが新たに同梱されています。この新しいテンプレートエンジンのデザインは、ASP.NET Razor にインスパイアされており、特徴は次のとおりです。

<!--
- **compact, expressive, and fluid**: it minimizes the number of characters and keystrokes required in a file, and enables a fast, fluid coding workflow. Unlike most template syntaxes, you do not need to interrupt your coding to explicitly denote server blocks within your HTML. The parser is smart enough to infer this from your code. This enables a really compact and expressive syntax which is clean, fast and fun to type.
- **easy to learn**: it enables you to quickly be productive with a minimum of concepts. You use all your existing Scala language and HTML skills.
- **not a new language**: we consciously chose not to create a new language. Instead we wanted to enable developers to use their existing Scala language skills, and deliver a template markup syntax that enables an awesome HTML construction workflow with your language of choice.
- **editable in any text editor**: it doesn’t require a specific tool and enables you to be productive in any plain old text editor.
-->
- **簡潔さ、表現力、書きやすさを重視**: このテンプレートエンジンを利用すると、1 ファイルあたりに必要な文字数やタイプ量が最小化され、サクサクとコーディングができます。よくあるテンプレートの文法とは異なり、サーバで実行されるコードブロックを HTML 中に明示する必要がありませんので、コーディングを妨げることがありません。パーサが優秀なので、コードブロックと HTML 部分を自動的に区別することができるのです。これにより、簡潔さ、表現力、書きやすさに優れた文法になっています。
- **学習コストが低い**: テンプレート文法は最小限になっているので、すぐにテンプレートを書き始めることができます。Scala と HTML についての知識がそのまま応用できます。
- **新しい言語ではありません**: Play framework の開発者は、新しい言語を作ることを意識的に避けました。その代わり、フレームワークの利用者がこれまでの Scala の知識をそのまま使って、テンプレートのマークアップ言語を拡張できるようにしました。
- **エディタを選ばない**: 特別なツールがなくても、ただのテキストエディタで書くことができます。

<!--
Templates are compiled, so you will see any errors right in your browser:
-->
テンプレートはコンパイルされて、エラーはブラウザ上で即座に確認することができます。

[[images/templatesError.png]]

<!--
## Overview
-->
## 概観

<!--
A Play Scala template is a simple text file, that contains small blocks of Scala code. They can generate any text-based format, such as HTML, XML or CSV.
-->
Play Scala テンプレートは Scala のコードブロックを少し含む以外は、ただのテキストファイルです。したがって、HTML, XML, CSV などあらゆるテキストベースのフォーマットを生成するために利用できます。

<!--
The template system has been designed to feel comfortable to those used to dealing with HTML, allowing web designers to easily work with the templates.
-->
テンプレートシステムは HTML に慣れた人に向けて設計されているので、web デザイナでも簡単に書くことができます。

<!--
Templates are compiled as standard Scala functions, following a simple naming convention: If you create a `views/Application/index.scala.html` template file, it will generate a `views.html.Application.index` function.
-->
テンプレートは簡単な命名規則に従って、ただの Scala の関数としてコンパイルされます。`views/Application/index.scala.html` というテンプレートファイルを作成すると、コンパイルにより `views.html.Application.index` という関数が生成されます。

<!--
For example, here is a simple template:
-->
例えば、次のようなシンプルなテンプレートを作成します。

```html
@(customer: Customer, orders: Seq[Order])
 
<h1>Welcome @customer.name!</h1>

<ul> 
@orders.map { order =>
  <li>@order.title</li>
} 
</ul>
```

<!--
You can then call this from any Scala code as you would call a function:
-->
このテンプレートは次のような関数呼び出しにより、Scala コード中のどこからでも呼び出すことができます。

```scala
val html = views.html.Application.index(customer, orders)
```

<!--
## Syntax: the magic ‘@’ character
-->
## 文法: 魔法の `@`

<!--
The Scala template uses `@` as the single special character. Every time this character is encountered, it indicates the begining of a Scala statement. It does not require you to explicitly close the code-block - this will be inferred from your code:
-->
Scala テンプレートでは `@` という文字が唯一の特殊文字です。この文字はテンプレート中に登場すると Scala コードの開始として認識されます。コードブロックの終わりはコンパイラにより自動的に推論されるため、明示的に閉じる必要がありません。

```
Hello @customer.name!
       ^^^^^^^^^^^^^
        Scala code
```

<!--
Because the template engine automatically detects the end of your code block by analysing your code, this syntax only supports simple statements. If you want to insert a multi-token statement, explicitly mark it using brackets:
-->
テンプレートエンジンがコードを解析してコードブロックの終わりを自動的に認識するため、この文法では単純な Scala 文しかサポートされません。複数のトークンにまたがるようなコードを挿入したい場合は、その範囲を括弧で明示することができます。

```
Hello @(customer.firstName + customer.lastName)!
       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 
                    Scala Code
```

<!--
You can also use curly brackets, as in plain Scala code, to write a multi-statements block:
-->
同様に、複数の文を含むコードを挿入したい場合は、通常の Scala コードと同様、中括弧を利用することができます。

```
Hello @{val name = customer.firstName + customer.lastName; name}!
       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                             Scala Code
```

<!--
Because `@` is a special character, you’ll sometimes need to escape it. Do this by using `@@`:
-->
`@` は特殊文字ですが、これをエスケープしたいときは `@@` と書きます。

```
My email is bob@@example.com
```

<!--
## Template parameters
-->
## テンプレート引数

<!--
A template is simply a function, so it needs parameters, which must be declared on the first line of the template file:
-->
テンプレートはただの関数なので、引数をとります。引数はテンプレートの先頭行で宣言します。

```scala
@(customer: models.Customer, orders: Seq[models.Order])
```

<!--
You can also use default values for parameters:
-->
引数のデフォルト値を設定することもできます。

```scala
@(title: String = "Home")
```

<!--
Or even several parameter groups:
-->
複数の引数グループを作成することもできます。

```scala
@(title:String)(body: Html)
```

<!--
And even implicit parameters:
-->
さらに、implicit parameter を利用することもできます。

```scala
@(title: String)(body: Html)(implicit request: RequestHeader)
```

<!--
## Iterating
-->
## 反復

<!--
You can use the Scala for-comprehension, in a pretty standard way. But note that the template compiler will add a `yield` keyword before your block:
-->
通常の Scala コード内とほぼ同じように、for 式を利用することができます。通常の for 式との違いとして、テンプレートコンパイラが `yield` キーワードをブロックの直前に挿入することに注意してください。

```html
<ul>
@for(p <- products) {
  <li>@p.name ($@p.price)</li>
} 
</ul>
```

<!--
As you probably know, this for-comprehension is just syntactic sugar for a classic map:
-->
ご存知かもしれませんが、for 式は map のシンタックスシュガーですので、先程の例は次のように map を使って書くのと同じ意味になります。

```html
<ul>
@products.map { p =>
  <li>@p.name ($@p.price)</li>
} 
</ul>
```

<!--
## If-blocks
-->
## If ブロック

<!--
If-blocks are nothing special. Simply use Scala’s standard `if` statement:
-->
If ブロックについても特別なことはなにもありません。Scala の `if` 式がそのまま使えます。

```html
@if(items.isEmpty) {
  <h1>Nothing to display</h1>
} else {
  <h1>@items.size items!</h1>
}
```

<!--
## Pattern matching
-->
## パターンマッチング

<!--
You can also use pattern matching in your templates:
-->
テンプレートにおいてパターンマッチングを利用することもできます。

```html
@connected match {
    
  case models.Admin(name) => {
    <span class="admin">Connected as admin (@name)</span>
  }

  case models.User(name) => {
    <span>Connected as @name</span>
  }
    
}
```

<!--
## Declaring reusable blocks
-->
## 再利用可能なブロックの宣言

<!--
You can create reusable code blocks:
-->
次のように、再利用可能なコードブロックを作成することができます。

```html
@display(product: models.Product) = {
  @product.name ($@product.price)
}
 
<ul>
@products.map { p =>
  @display(product = p)
} 
</ul>
```

<!--
Note that you can also declare reusable pure Scala blocks:
-->
再利用可能な Scala のコードブロックを宣言することもできます。

```html
@title(text: String) = @{
  text.split(' ').map(_.capitalize).mkString(" ")
}
 
<h1>@title("hello world")</h1>
```

<!--
> **Note:** Declaring Scala block this way in a template can be sometimes useful but keep in mind that a template is not the best place to write complex logic. It is often better to externalize these kind of code in a pure scala source file (that you can store under the `views/` package as well if you want).
-->
> **ノート:** このように Scala ブロックをテンプレートで宣言すると便利なこともありますが、テンプレートは込み入ったロジックを置く場所としては適切でないことを覚えておいてください。このようなコードは 通常の Scala ソースファイルに切り出すと良いことが多いです。 (必要であれば、 `views/` パッケージ以下に通常の Scala ソースファイルを置くこともできます。)

<!--
By convention, a reusable block defined with a name starting with **implicit** will be marked as `implicit`:
-->
慣習として、名前が **implicit** で始まる再利用可能なブロックは、`implicit` として扱われます。

```
@implicitFieldConstructor = @{ MyFieldConstructor() }
```

<!--
## Declaring reusable values
-->
## 再利用可能な値の宣言

<!--
You can define scoped values using the `defining` helper:
-->
`defining` ヘルパ関数を使って、特定のスコープ内でのみ有効な値を定義することができます。

```html
@defining(user.firstName + " " + user.lastName) { fullName =>
  <div>Hello @fullName</div>
}
```

<!--
## Import statements
-->
## import 文

<!--
You can import whatever you want at the beginning of your template (or sub-template):
-->
テンプレート (またはサブテンプレート) の冒頭で、任意のパッケージやオブジェクトなどを import することができます。

```scala
@(customer: models.Customer, orders: Seq[models.Order])
 
@import utils._
 
...
```

<!--
## Comments
-->
## コメント

<!--
You can write server side block comments in templates using `@* *@`:
-->
`@* *@` を利用して、サーバーサイドのブロックコメントを書くことができます。

```
@*********************
 * This is a comment *
 *********************@   
```

<!--
You can put a comment on the first line to document your template into the Scala API doc:
-->
テンプレートの先頭行にコメントを記述すると、Scala API doc にドキュメントが出力されます。

```
@*************************************
 * Home page.                        *
 *                                   *
 * @param msg The message to display *
 *************************************@
@(msg: String)

<h1>@msg</h1>
```

<!--
## Escaping
-->
## エスケープ

<!--
By default, the dynamic content parts are escaped according the template type (e.g. HTML or XML) rules. If you want to output a raw content fragment, wrap it in the template content type. 
-->
デフォルトでは、動的なコンテンツ部分はテンプレートの型 (HTML や XML など) に応じてエスケープされます。エスケープなしでコンテンツを出力したい場合、コンテンツをテンプレートのコンテンツタイプでラップしてください。

<!--
For example to output raw HTML:
-->
例えば、生の HTML を出力する場合は次のように記述します。

```html
<p>
  @Html(article.content)    
</p>
```

<!--
> **Next:** [[Common use cases | ScalaTemplateUseCases]]
-->
> **Next:** [[よくある利用例 | ScalaTemplateUseCases]]
