<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Adding support for a custom format to the template engine
-->
# テンプレートエンジンに対するカスタムフォーマットのサポートの追加

<!--
The built-in template engine supports common template formats (HTML, XML, etc.) but you can easily add support for your own formats, if needed. This page summarizes the steps to follow to support a custom format.
-->
組み込みのテンプレートエンジンは一般的なテンプレートフォーマット (HTML、XML、等) をサポートしていますが、必要であれば独自のフォーマットのサポートを簡単に追加する事ができます。このページではカスタムフォーマットをサポートする為に必要なステップをまとめています。

## Overview of the templating process

<!--
The template engine builds its result by appending static and dynamic content parts of a template. Consider for instance the following template:
-->
テンプレートエンジンはテンプレートの静的および動的な部品を結合する事でビルドします。例えば以下のテンプレートを考えてみて下さい:

```
foo @bar baz
```

<!--
It consists in two static parts (`foo ` and ` baz`) around one dynamic part (`bar`). The template engine concatenates these parts together to build its result. Actually, in order to prevent cross-site scripting attacks, the value of `bar` can be escaped before being concatenated to the rest of the result. This escaping process is specific to each format: e.g. in the case of HTML you want to transform “<” into “&amp;lt;”.
-->
上記のテンプレートは一つの動的な部品 (`bar`) およびその周囲の二つの静的な部品 (`foo ` および ` baz`) から構成されています。テンプレートエンジンはこれらの部品を結合する事で結果をビルドします。実際には、クロスサイトスクリプティング攻撃を防ぐ為、他の部品と結合される前に `bar` の値をエスケープすることができます。このエスケーププロセスはフォーマット毎に特有となります: 例えば、 HTML の場合は「<」を「&amp;lt;」に変換したくなるでしょう。

<!--
How does the template engine know which format correspond to a template file? It looks at its extension: e.g. if it ends with `.scala.html` it associates the HTML format to the file.
-->
テンプレートエンジンはどのようにしてテンプレートファイルに対応するフォーマットを知る事が出来るのでしょうか? 拡張子を見ています: 例えば、 `.scala.html` で終わる場合は、HTML フォーマットとファイルを紐付けます。

<!--
Finally, you usually want your template files to be used as the body of your HTTP responses, so you have to define how to make a Play result from a template rendering result.
-->
最終的に、テンプレートファイルを HTTP レスポンスのボディとして使用したい事が普通だと思うので、テンプレートのレンダリング結果から Play の result をどうやって作成するかを定義しなければいけません。

<!--
In summary, to support your own template format you need to perform the following steps:
-->
まとめると、独自のテンプレートフォーマットをサポートするには、以下のステップを行う必要があります:

<!--
* Implement the text integration process for the format ;
* Associate a file extension to the format ;
* Eventually tell Play how to send the result of a template rendering as an HTTP response body.
-->
* フォーマットに対するテキスト統合のプロセスを実装する ;
* ファイル拡張子をフォーマットと関連づける ;
* 最終的に、Play にどうやってテンプレートのレンダリング結果を HTTP のレスポンスボディに送るかを指定する。

<!--
## Implement a format
-->
## フォーマットを実装する

Implement the `play.twirl.api.Format[A]` trait that has the methods `raw(text: String): A` and `escape(text: String): A` that will be used to integrate static and dynamic template parts, respectively.

The type parameter `A` of the format defines the result type of the template rendering, e.g. `Html` for a HTML template. This type must be a subtype of the `play.twirl.api.Appendable[A]` trait that defines how to concatenates parts together.

For convenience, Play provides a `play.twirl.api.BufferedContent[A]` abstract class that implements `play.twirl.api.Appendable[A]` using a `StringBuilder` to build its result and that implements the `play.twirl.api.Content` trait so Play knows how to serialize it as an HTTP response body (see the last section of this page for details).

In short, you need to write to classes: one defining the result (implementing `play.twirl.api.Appendable[A]`) and one defining the text integration process (implementing `play.twirl.api.Format[A]`). For instance, here is how the HTML format is defined:

<!--
```scala
// The `Html` result type. We extend `BufferedContent[Html]` rather than just `Appendable[Html]` so
// Play knows how to make an HTTP result from a `Html` value
class Html(buffer: StringBuilder) extends BufferedContent[Html](buffer) {
  val contentType = MimeTypes.HTML
}

object HtmlFormat extends Format[Html] {
  def raw(text: String): Html = …
  def escape(text: String): Html = …
}
```
-->
```scala
// `Html` の結果の型です。 Play に `Html` の値から HTTP の結果を生成方法を知らせるために、
// `Appendable[Html]` ではなく `BufferedContent[Html]` を継承しています。
class Html(buffer: StringBuilder) extends BufferedContent[Html](buffer) {
  val contentType = MimeTypes.HTML
}

<!--
## Associate a file extension to the format
-->
## ファイル拡張子をフォーマットと関連づける

The templates are compiled into a `.scala` files by the build process just before compiling the whole application sources. The `TwirlKeys.templateFormats` key is a sbt setting of type `Map[String, String]` defining the mapping between file extensions and template formats. For instance, if HTML was not supported out of the box by Play, you would have to write the following in your build file to associate the `.scala.html` files to the `play.twirl.api.HtmlFormat` format:

```scala
TwirlKeys.templateFormats += ("html" -> "my.HtmlFormat.instance")
```

Note that the right side of the arrow contains the fully qualified name of a value of type `play.twirl.api.Format[_]`.

<!--
## Tell Play how to make an HTTP result from a template result type
-->
## テンプレート結果の型から HTTP の結果を生成する方法を Play に教える

<!--
Play can write an HTTP response body for any value of type `A` for which it exists an implicit `play.api.http.Writeable[A]` value. So all you need is to define such a value for your template result type. For instance, here is how to define such a value for HTTP:
-->
暗黙の `play.api.http.Writeable[A]` の値がある場合、 Play は任意の `A` の値に対して HTTP レスポンスを書く事が出来ます。従って、あなたがやらなければいけない事はテンプレート結果の型に対してそのような値を定義する事です。例えば、以下は HTTP に対してそのような値を定義しています:

```scala
implicit def writableHttp(implicit codec: Codec): Writeable[Http] =
  Writeable[Http](result => codec.encode(result.body), Some(ContentTypes.HTTP))
```

> **Note:** if your template result type extends `play.twirl.api.BufferedContent` you only need to define an
> implicit `play.api.http.ContentTypeOf` value:
> ```scala
> implicit def contentTypeHttp(implicit codec: Codec): ContentTypeOf[Http] =
>   ContentTypeOf[Http](Some(ContentTypes.HTTP))
> ```

<!--
> **Next:** [[HTTP form submission and validation | ScalaForms]]
-->
> **次:** [[HTTP フォーム送信とバリデーション | ScalaForms]]
