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
Play comes with a powerful Scala-based template engine, whose design was inspired by ASP.NET Razor. Specifically it is:
-->
Play には強力な Scala ベースのテンプレートエンジンが同梱されています。この新しいテンプレートエンジンのデザインは、ASP.NET Razor にインスパイアされており、特徴は次のとおりです。

<!--
- **compact, expressive, and fluid**: it minimizes the number of characters and keystrokes required in a file, and enables a fast, fluid coding workflow. Unlike most template syntaxes, you do not need to interrupt your coding to explicitly denote server blocks within your HTML. The parser is smart enough to infer this from your code. This enables a really compact and expressive syntax which is clean, fast and fun to type.
- **easy to learn**: it allows you to quickly become productive, with a minimum of concepts. You use simple Scala constructs and all your existing HTML skills.
- **not a new language**: we consciously chose not to create a new language. Instead we wanted to enable Scala developers to use their existing Scala language skills, and deliver a template markup syntax that enables an awesome HTML construction workflow.
- **editable in any text editor**: it doesn’t require a specific tool and enables you to be productive in any plain old text editor.
-->
- **簡潔さ、表現力、書きやすさを重視**: このテンプレートエンジンを利用すると、1 ファイルあたりに必要な文字数やタイプ量が最小化され、サクサクとコーディングができます。よくあるテンプレートの文法とは異なり、サーバで実行されるコードブロックを HTML 中に明示する必要がありませんので、コーディングを妨げることがありません。パーサが優秀なので、コードブロックと HTML 部分を自動的に区別することができるのです。これにより、簡潔さ、表現力、書きやすさに優れた文法になっています。
- **学習コストが低い**: テンプレート文法は最小限になっているので、すぐにテンプレートを書き始めることができます。簡潔な Scala の構文を使うことができ、また HTML についての知識がそのまま応用できます。
- **新しい言語ではありません**: Play framework の開発者は、新しい言語を作ることを意識的に避けました。その代わり、Scala 版のフレームワークの利用者が、テンプレートのマークアップ言語を拡張できるようにしました。
- **エディタを選ばない**: 特別なツールがなくても、ただのテキストエディタで書くことができます。

&nbsp;

<!--
Templates are compiled, so you will see any errors in your browser:
-->
テンプレートはコンパイルされて、エラーはブラウザ上で確認することができます。

[[images/templatesError.png]]

<!--
## Overview
-->
## 概観

<!--
A Play Scala template is a simple text file that contains small blocks of Scala code. Templates can generate any text-based format, such as HTML, XML or CSV.
-->
Play Scala テンプレートは Scala のコードブロックを少し含む以外は、ただのテキストファイルです。したがって、HTML, XML, CSV などあらゆるテキストベースのフォーマットを生成するために利用できます。

<!--
The template system has been designed to feel comfortable to those used to working with HTML, allowing front-end developers to easily work with the templates.
-->
テンプレートシステムは HTML に慣れた人に向けて設計されているので、フロントエンドエンジニアでも簡単に書くことができます。

<!--
Templates are compiled as standard Scala functions, following a simple naming convention. If you create a `views/Application/index.scala.html` template file, it will generate a `views.html.Application.index` class that has an `apply()` method.
-->
テンプレートは簡単な命名規則に従って、ただの Scala の関数としてコンパイルされます。`views/Application/index.scala.html` というテンプレートファイルを作成すると、コンパイルにより `views.html.Application.index` という `apply()` メソッドを持つクラスが生成されます。

<!--
For example, here is a simple template:
-->
例えば、次のようなシンプルなテンプレートを作成します。

@[example-template](code/scalaguide/templates/views/Application/index.scala.html)

<!--
You can then call this from any Scala code as you would normally call a method on a class:
-->
このテンプレートは、いつもクラスのメソッドを呼び出しているように、あらゆる Scala コードから呼び出すことができます:

@[invoke-template](code/ScalaTemplates.scala)

<!--
## Syntax: the magic ‘@’ character
-->
## 文法: 魔法の `@`

<!--
The Scala template uses `@` as the single special character. Every time this character is encountered, it indicates the beginning of a dynamic statement. You are not required to explicitly close the code block - the end of the dynamic statement will be inferred from your code:
-->
Scala テンプレートでは `@` という文字が唯一の特殊文字です。この文字はテンプレート中に登場すると動的コードの開始として認識されます。コードブロックの終わりはコンパイラにより自動的に推論されるため、明示的に閉じる必要がありません。

```
Hello @customer.name!
       ^^^^^^^^^^^^^
       Dynamic code
```

<!--
Because the template engine automatically detects the end of your code block by analysing your code, this syntax only supports simple statements. If you want to insert a multi-token statement, explicitly mark it using brackets:
-->
テンプレートエンジンがコードを解析してコードブロックの終わりを自動的に認識するため、この文法では単純な Scala 文しかサポートされません。複数のトークンにまたがるようなコードを挿入したい場合は、その範囲を括弧で明示することができます。

```
Hello @(customer.firstName + customer.lastName)!
       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    Dynamic Code
```

<!--
You can also use curly brackets, to write a multi-statement block:
-->
同様に、複数の文を含むコードを挿入したい場合は、中括弧を利用することができます。

```
Hello @{val name = customer.firstName + customer.lastName; name}!
       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                             Dynamic Code
```

<!--
Because `@` is a special character, you’ll sometimes need to escape it. Do this by using `@@`:
-->
`@` は特殊文字ですが、これをエスケープしたいときは `@@` と書きます。

@[escape-at](code/scalaguide/templates/snippets.scala.html)

<!--
## Template parameters
-->
## テンプレート引数

<!--
A template is like a function, so it needs parameters, which must be declared at the top of the template file:
-->
テンプレートは関数のように引数をとります。引数はテンプレートの先頭行で宣言します。

@[simple-parameters](code/scalaguide/templates/simpleParameters.scala.html)

<!--
You can also use default values for parameters:
-->
引数のデフォルト値を設定することもできます。

@[default-parameters](code/scalaguide/templates/defaultParameters.scala.html)

<!--
Or even several parameter groups:
-->
複数の引数グループを作成することもできます。

@[curried-parameters](code/scalaguide/templates/curriedParameters.scala.html)

<!--
## Iterating
-->
## 反復

<!--
You can use the `for` keyword, in a pretty standard way:
-->
`for` キーワードをよくある形で使うことができます。

@[for-loop](code/scalaguide/templates/snippets.scala.html)

<!--
> **Note:** Make sure that `{` is on the same line with `for` to indicate that the expression continues to next line.  
-->
> **ノート:** 次の行に式が続いていることを示すために、`for` と同じ行に `{` を置いていることを確認してください。


<!--
## If-blocks
-->
## If ブロック

<!--
If-blocks are nothing special. Simply use Scala’s standard `if` statement:
-->
If ブロックについても特別なことはなにもありません。Scala の `if` 式がそのまま使えます。

@[conditional](code/scalaguide/templates/snippets.scala.html)

<!--
## Declaring reusable blocks
-->
## 再利用可能なブロックの宣言

<!--
You can create reusable code blocks:
-->
次のように、再利用可能なコードブロックを作成することができます。

@[reusable](code/scalaguide/templates/snippets.scala.html)

<!--
Note that you can also declare reusable pure code blocks:
-->
再利用可能な Scala のコードブロックを宣言することもできます。

@[pure-reusable](code/scalaguide/templates/snippets.scala.html)

<!--
> **Note:** Declaring code block this way in a template can be sometime useful but keep in mind that a template is not the best place to write complex logic. It is often better to externalize these kind of code in a Java class (that you can store under the `views/` package as well if your want).
-->
> **ノート:** このようにコードブロックをテンプレートで宣言すると便利なこともありますが、テンプレートは込み入ったロジックを置く場所としては適切でないことを覚えておいてください。このようなコードは 通常の Java クラスに切り出すと良いことが多いです。 (必要であれば、 `views/` パッケージ以下に通常の Java クラスを置くこともできます。)

<!--
By convention a reusable block defined with a name starting with **implicit** will be marked as `implicit`:
-->
慣習として、名前が **implicit** で始まる再利用可能なブロックは、`implicit` として扱われます。

@[implicits](code/scalaguide/templates/snippets.scala.html)

<!--
## Declaring reusable values
-->
## 再利用可能な値の宣言

<!--
You can define scoped values using the `defining` helper:
-->
`defining` ヘルパ関数を使って、特定のスコープ内でのみ有効な値を定義することができます。

@[defining](code/scalaguide/templates/snippets.scala.html)

<!--
## Import statements
-->
## import 文

<!--
You can import whatever you want at the beginning of your template (or sub-template):
-->
テンプレート (またはサブテンプレート) の冒頭で、任意のパッケージやオブジェクトなどを import することができます。

@[import](code/scalaguide/templates/importStatement.scala.html)

<!--
To make an absolute resolution, use **_root_** prefix in the import statement.
-->
絶対パスでパッケージを指定をしたい場合は、import 文で先頭に **\_root\_** を指定します。

@[absolute](code/scalaguide/templates/importStatement.scala.html)

<!--
If you have common imports, which you need in all templates, you can declare in `build.sbt`
-->
全てのテンプレートで必要な共通の import 文がある場合は、 `build.sbt` で指定することができます。

```scala
templatesImport += "com.abc.backend._"
```

<!--
## Comments
-->
## コメント

<!--
You can write server side block comments in templates using `@* *@`:
-->
`@* *@` を利用して、サーバーサイドのブロックコメントを書くことができます。

@[comment](code/scalaguide/templates/snippets.scala.html)

<!--
You can put a comment on the first line to document your template into the Scala API doc:
-->
テンプレートの先頭行にコメントを記述すると、Scala API doc にドキュメントが出力されます。

@[comment](code/scalaguide/templates/firstLineComment.scala.html)

<!--
## Escaping
-->
## エスケープ

<!--
By default, dynamic content parts are escaped according to the template type’s (e.g. HTML or XML) rules. If you want to output a raw content fragment, wrap it in the template content type. 
-->
デフォルトでは、動的なコンテンツ部分はテンプレートの型 (HTML や XML など) に応じてエスケープされます。エスケープなしでコンテンツを出力したい場合、コンテンツをテンプレートのコンテンツタイプでラップしてください。

<!--
For example to output raw HTML:
-->
例えば、生の HTML を出力する場合は次のように記述します。

@[raw-html](code/scalaguide/templates/snippets.scala.html)

<!--
> **Next:** [[Common use cases | ScalaTemplateUseCases]]
-->
> **Next:** [[よくある使い方 | ScalaTemplateUseCases]]