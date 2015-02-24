<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Guidelines for writing Play documentation
-->
# Play ドキュメント執筆ガイドライン

<!--
The Play documentation is written in Markdown format, with code samples extracted from compiled, run and tested source files.
-->
Play のドキュメントは、コンパイル、実行、そしてテストされたソースファイルから抽出されたサンプルコードと共に、Markdown フォーマットにて記述されています。

<!--
There are a few guidelines that must be adhered to when writing Play documentation.
-->
Play のドキュメントを書くときには忠実に従わなければならない、いくつかのガイドラインがあります。

<!--
## Markdown
-->
## Markdown

<!--
All markdown files must have unique names across the entire documentation, regardless of what folders they are in.  Play uses a wiki style of linking and rendering documentation.
-->
すべての markdown ファイルは、それらがどのフォルダにあるかに依らず、ドキュメント全体に渡ってユニークな名前でなければなりません。Play は wiki スタイルのリンク方式を使用してドキュメントをレンダリングします。

<!--
Newline characters in the middle of paragraphs are considered hard wraps, similar to GitHub flavored markdown, and are rendered as line breaks.  Paragraphs should therefore be contained on a single line.
-->
GitHub flavored markdown と同様、段落途中の改行文字は強制折り返しと見なされ、改行としてレンダリングされます。このため、段落は単一行に含める必要があります。

<!--
### Links
-->
### リンク

<!--
Links to other pages in the documentation should be created using wiki markup syntax, for example:
-->
ドキュメント内の別ページへのリンクは、例えば以下のようにして wiki のマークアップ構文を使って作成します:

    [[Optional description|ScalaRouting]]

<!--
Images should also use the above syntax.
-->
画像についても、上記の構文を使います。

<!--
> External links should not use the above syntax, but rather, should use the standard Markdown link syntax.
-->
> 外部リンクについては、上記の構文ではなく、Markdown 標準のリンク構文を使うべきです。

<!--
## Code samples
-->
## コードサンプル

<!--
All supported code samples should be imported from external compiled files.  The syntax for doing this is:
-->
サポート対象のすべてのコードサンプルは、コンパイルされた外部ファイルからインポートされるべきです。これを行う構文は以下のとおりです:

    @[some-label](code/SomeFeature.scala)

<!--
The file should then delimit the lines that need to be extracted using `#some-label`, for example:
-->
そして、この外部ファイルは、例えば以下のように `#some-label` を使って抽出される必要のある行を区切ります:

```scala
object SomeFeatureSpec extends Specification {
  "some feature" should {
    "do something" in {
      //#some-label
      val msg = Seq("Hello", "world").mkString(" ")
      //#some-label
      msg must_== "Hello world"
    }
  }
}
```

<!--
In the above case, the ``val msg = ...`` line will be extracted and rendered as code in the page.  All code samples should be checked to ensure they compile, run, and if it makes sense, ensure that it does what the documentation says it does.  It should not try to test the features themselves.
-->
上記の場合は 行 ``val msg = ...`` が抽出され、コードとしてページ内にレンダリングされます。すべてのサンプルコードは、それらがコンパイル、実行できること、そして当然のことですが、それらがドキュメントに記述されているとおりに動作することを確認する必要があります。サンプルコードは、その機能をサンプルコード自身でテストすべきではありません。

<!--
All code samples get run on the same classloader.  Consequently they must all be well namespaced, within a package that corresponds to the part of the documentation they are associated with.
-->
すべてのサンプルコードは同一のクラスローダ上で実行されます。そのため、サンプルコードは関連するドキュメントの部分に該当するパッケージ内において、適切な名前空間に存在しなければなりません。

<!--
In some cases, it may not be possible for the code that should appear in the documentation to exactly match the code that you can write given the above guidelines.  In particular, some code samples require the use of package names like `controllers`.  As a last resort if there are no other ways around this, there are a number of directives you can put in the code to instruct the code samples extractor to modify the sample.  These are:
-->
いくつかの場面では、上記のガイドラインに沿って書くことのできるコードを、ドキュメント内に表示されるコードと完全に一致させることができない場合があるかもしれません。特に、いくつかのコードサンプルは `controllers` のようなパッケージ名を使う必要があります。他に方法がない場合は最後の手段として、コードサンプル抽出機能にサンプルを変更するよう指示する、いくつかのディレクティブをコードに配置することができます。以下がそのディレクティブです:

<!--
* `###replace: foo` - Replace the next line with `foo`.  You may optionally terminate this command with `###`
* `###insert: foo` - Insert `foo` before the next line.  You may optionally terminate this command with `###`
* `###skip` - Skip the current line
* `###skip: n` - Skip the next n lines
-->
* `###replace: foo` - 次の行を `foo` で置き換えます。このコマンドを `###` で終端することもできます
* `###insert: foo` - 次の行の前に `foo` を挿入します。このコマンドを `###` で終端することもできます
* `###skip` - 現在の行をスキップします
* `###skip: n` - 次の n 行をスキップします

<!--
For example:
-->
例えば、以下のように使います:

```scala
//#controller
//###replace: package controllers
package foo.bar.controllers

import play.api.mvc._

object Application extends Controller {
  ...
}
//#controller
```

<!--
> These directives must only be used as a last resort, since the point of pulling code samples out into external files is that the very code that is in the documentation is also compiled and tested.  Directives break this.
-->
> 外部ファイルにコードサンプルを抽出することの要点は、ドキュメント中のすべてのコードがコンパイルされ、テストされていることなので、これらのディレクティブは最後の手段として使わなければなりません。ディレクティブはこの要点に違反します。

<!--
It's also important to be aware of the current context of the code samples, to ensure that the appropriate import statements are documented.  However it doesn't make sense to necessarily include all import statements in every code sample, so discretion must be shown here.
-->
適切な import 文が記述されていることを保証するために、コードサンプルの現在のコンテキストに注意することも重要です。一方で、あらゆるコードサンプルに必ずしも import 文をすべて含める必要はないので、ここでは慎重さが示される必要があります。

<!--
Guidelines for specific types of code samples are below.
-->
特別な種類のコードサンプルに向けたガイドラインを以下に示します。

<!--
### Scala
-->
### Scala

<!--
All scala code samples should be tested using specs, and the code sample, if possible, should be inside the spec.  Local classes and method definitions are encouraged where appropriate.  Scoping import statements within blocks are also encouraged.
-->
すべての scala コードサンプルは specs を使ってテストする必要があり、そのコードサンプルは可能であれば spec の内部にあるべきです。適切な場所におけるローカルクラスおよびメソッド定義を推奨します。ブロック内にスコープを限定した import 文も推奨します。

<!--
### Java
-->
### Java

<!--
All Java code samples should be tested using JUnit.  Simple code samples are usually simple to include inside the JUnit test, but when the code sample is a method or a class, it gets harder.  Preference should be shown to use local and inner classes, but this may not be possible, for example, a static method can only appear on a static inner class, but that means adding the static modifier to the class, which would not appear if it was an outer class.  Consequently it may be necessary in some cases to pull Java code samples out into their own files.
-->
すべての Java コードサンプルは JUnit を使ってテストする必要があります。通常、単純なコードサンプルは素直に JUnit テストの中に含めることができますが、コードサンプルがメソッドまたはクラスの場合、より難しくなります。ローカルなインナークラスを使う選択が見受けられるかもしれませんが、これが不可能な場合もあるかもしれません。例えば static メソッドは static インナークラス上にのみ登場するのですが、これは、もしそのクラスが外部クラスであったならば登場しないであろう static 修飾子をインナークラスに 追加することを意味します。このため、いくつかの場合においては、Java コードサンプルをそのファイル自身に抽出する必要があるかもしれません。

<!--
### Scala Templates
-->
### Scala テンプレート

<!--
Scala template code samples should be tested either with Specs in Scala or JUnit in Java.  Note that templates are compiled with different default imports, depending on whether they live in the Scala documentation or the Java documentation.  It is therefore also important to test them in the right context, if a template is relying on Java thread locals, they should be tested from a Java action.
-->
Scala テンプレートコードサンプルは、Scala の Specs か Java の JUnit のいずれかでテストする必要があります。テンプレートは、Scala ドキュメント または Java ドキュメントのどちらに含まれるかによって、異なるデフォルトのインポートでコンパイルされることに注意してください。このため、正しいコンテキストにおいてテンプレートをテストすることも重要です。もしテンプレートが Java のスレッドローカルに依存しているならば、それは Java アクションからテストされるべきです。

<!--
Where possible, template code samples should be consoloditated in a single file, but this may not always be possible, for example if the code sample contains a parameter declaration.
-->
可能であれば、テンプレートコードサンプルはひとつのファイルに統合されるべきですが、例えばコードサンプルが引数宣言を含む場合など、常に可能であるとは限らないでしょう。

<!--
### Routes files
-->
### Routes ファイル

<!--
Routes files should be tested either with Specs in Scala or JUnit in Java.  Routes files should be named with the full package name, for example, `scalaguide.http.routing.routes`, to ensure that they are isolated from other routes code samples.
-->
Routes ファイルは Scala の Specs か Java の JUnit のいずれかでテストする必要があります。Routes ファイルは、その他の routes コードサンプルと区別できることを保証するため、例えば `scalaguide.http.routing.routes` のように完全なパッケージ名を含めた名前にしなければなりません。

<!--
The routes compiler used by the documentation runs in a special mode that generates the reverse router inside the namespace declared by that file.  This means that although a routes code sample may appear to use absolute references to classes, it is actually relative to the namespace of the router.  Thus in the above routes file, if you have a route called `controllers.Application`, it will actually refer to a controller called `scalaguide.http.routing.controllers.Application`.
-->
ドキュメントから利用される routes コンパイラは、そのドキュメントに宣言された名前空間内でリバースルータを生成する特別なモードで動作します。これは、routes コードサンプルは絶対パスを使ってクラスを参照しているように見えるにも関わらず、実際にはルーターの名前空間に対する相対パスを使うということを意味します。そのため、上記の routes ファイル内に `controllers.Application` というルートがある場合、これは実際には `scalaguide.http.routing.controllers.Application` というコントローラへの参照になります。

<!--
### SBT code
-->
### SBT コード

<!--
At current, SBT code samples cannot be pulled out of the documentation, since compiling and running them will require a very custom SBT setup involving using completely different classloaders and classpaths.
-->
SBT コードサンプルをコンパイルして実行するには、まったく異なるクラスローダとクラスパスを含む、とても作り込んだ SBT をセットアップする必要があるため、今のところ SBT コードサンプルをドキュメントの外に抽出することはできません。

<!--
### Other code
-->
### その他のコード

<!--
Other code may or may not be testable.  It may make sense to test Javascript code by running an integration test using HTMLUnit.  It may make sense to test configuration by loading it.  Common sense should be used here.
-->
その他のコードはテストできないかもしれません。HTMLUnit を使った結合テストを実行することで Javascript コードをテストすることは理にかなっているかもしれません。実際にロードすることで設定ファイルをテストすることも意味があるでしょう。ここでは常識が用いられるべきです。

<!--
## Testing the docs
-->
## ドキュメントをテストする

<!--
To ensure that the docs render correctly, run `./build run` from within the documentation directory.  This will start a small Play server that does nothing but serve the documentation.
-->
ドキュメントが正しくレンダリングされることを確認するには、ドキュメントディレクトリ内で `./build run` を実行します。これは、ドキュメントの提供以外は何もしない小さな Play サーバを開始します。

<!--
To ensure that the code samples compile, run and tests pass, run `./build test`.
-->
コードサンプルがコンパイル、実行され、そしてテストにパスすることを確認するには、`./build test` を実行します。

<!--
To validate that the documentation is structurely sound, run `./build validate-docs`.  This checks that there are no broken wiki links, code references or resource links, ensures that all documentation markdown filenames are unique, and ensures that there are no orphaned pages.
-->
ドキュメントの構造が妥当であることを確認するには、`./build validate-docs` を実行します。このコマンドは、壊れた wiki リンクやコード参照、またはリソースリンクが存在しないことを確認し、ドキュメントの markdown ファイル名がユニークであることを保証し、そして孤立したページが存在しないことを保証します。

## Code samples from external Play modules

To avoid circular dependencies, any documentation that documents a Play module that is not a core part of Play can't include its code samples along with the rest of the Play documentation.  To address this, the documentation for that module can place an entry into the `externalPlayModules` map in `project/Build.scala`, including all the extra settings (namely library dependencies) required to build the code snippets for that module.  For example:

```scala
val externalPlayModules: Map[String, Seq[Setting[_]]] = Map(
  "some-module" -> Seq(
    libraryDependencies += "com.example" %% "some-play-module" % "1.2.3" % "test"
  ),
  ...
)
```

Now place all code snippets that use that module in `code-some-module`.  Now to run any SBT commands, ensuring that that module is included, run `./build -Dexternal.modules=some-module test`, or to run the tests for all modules, run `./build -Dexternal-modules=all test`.
