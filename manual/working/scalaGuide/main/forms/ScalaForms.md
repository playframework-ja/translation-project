<!--- Copyright (C) 2009-2019 Lightbend Inc. <https://www.lightbend.com> -->
<!--
# Handling form submission
-->
# フォームの送信

<!--
## Overview
-->
## 概要

<!--
Form handling and submission is an important part of any web application.  Play comes with features that make handling simple forms easy and complex forms possible.
-->
フォームの処理と送信は、Web アプリケーションの重要な構成要素のひとつです。 Play には、シンプルなフォーム処理を扱いやすく、そして複雑なフォーム処理を扱うことのできる機能が搭載されています。

<!--
Play's form handling approach is based around the concept of binding data.  When data comes in from a POST request, Play will look for formatted values and bind them to a [`Form`](api/scala/play/api/data/Form.html) object.  From there, Play can use the bound form to value a case class with data, call custom validations, and so on.
-->
Play のフォーム処理アプローチは、データをバインドするという概念に基づいています。POST リクエストからデータを受け取ると、Play はフォーマットされた値を探し、それらを [`Form`](api/scala/play/api/data/Form.html) オブジェクトにバインドします。そこから、Play はバインドされたフォームを使用して、データが含まれているケースクラスを評価したり、カスタムのバリデーションを呼び出すことができます。

<!--
Typically forms are used directly from a [`BaseController`](api/scala/play/api/mvc/BaseController.html) instance.  However, [`Form`](api/scala/play/api/data/Form.html) definitions do not have to match up exactly with case classes or models: they are purely for handling input and it is reasonable to use a distinct `Form` for a distinct POST.
-->
通常、フォームは [`BaseController` ](api/scala/play/api/mvc/BaseController.html)インスタンスから直接使用されます。しかし、[`Form`](api/scala/play/api/data/Form.html) 定義は、ケースクラスやモデルと正確に一致させる必要はありません。それらはただ単に入力を処理するためのものであり、異なる POST には異なる `Form` を使用するのが合理的です。

<!--
## Imports
-->
## インポート

<!--
To use forms, import the following packages into your class:
-->
フォームを使用するには、以下のパッケージをクラスにインポートします。

@[form-imports](code/ScalaForms.scala)

To make use of validation and constraints, import the following packages into your class:

@[validation-imports](code/ScalaForms.scala)

<!--
## Form Basics
-->
## フォームの基本

<!--
We'll go through the basics of form handling:
-->
フォーム処理の基本は次のとおりです。

<!--
* defining a form,
* defining constraints in the form,
* validating the form in an action,
* displaying the form in a view template,
* and finally, processing the result (or errors) of the form in a view template.
-->
* フォームを定義し、
* フォームの制約を定義し、
* アクション内でフォームを検証し、
* ビューテンプレートにフォームを表示し、
* 最後に、ビューテンプレート内でフォームの結果 (またはエラー) を処理します。

<!--
The end result will look something like this:
-->
最終的な結果は次のようになります。

[[images/lifecycle.png]]

<!--
### Defining a form
-->
### フォームの定義

<!--
First, define a case class which contains the elements you want in the form.  Here we want to capture the name and age of a user, so we create a `UserData` object:
-->
まず、ケースクラスを定義し、そこにフォームの中で必要な要素を含めます。ここでは、ユーザーの名前と年齢を取得したいので、`UserData` オブジェクトを作成します。

@[userData-define](code/ScalaForms.scala)

<!--
Now that we have a case class, the next step is to define a [`Form`](api/scala/play/api/data/Form.html) structure. The function of a `Form is to transform form data into a bound instance of a case class, and we define it like follows:
-->
ケースクラスができたので、次のステップとして [`Form`](api/scala/play/api/data/Form.html) 構造を定義します。`Form` の関数は、フォームデータを、バインドされたケースクラスのインスタンスに変換します。それを以下のように定義します。

@[userForm-define](code/ScalaForms.scala)

<!--
The [Forms](api/scala/play/api/data/Forms$.html) object defines the [`mapping`](api/scala/play/api/data/Forms$.html#mapping%5BR%2CA1%5D\(\(String%2CMapping%5BA1%5D\)\)\(\(A1\)%E2%87%92R\)\(\(R\)%E2%87%92Option%5BA1%5D\)%3AMapping%5BR%5D) method. This method takes the names and constraints of the form, and also takes two functions: an `apply` function and an `unapply` function.  Because UserData is a case class, we can plug its `apply` and `unapply` methods directly into the mapping method.
-->
この [Forms](api/scala/play/api/data/Forms$.html) オブジェクトには、[`mapping`](api/scala/play/api/data/Forms$.html#mapping%5BR%2CA1%5D\(\(String%2CMapping%5BA1%5D\)\)\(\(A1\)%E2%87%92R\)\(\(R\)%E2%87%92Option%5BA1%5D\)%3AMapping%5BR%5D) メソッドが定義されています。このメソッドは、フォームの名前と制約を受け取り、`apply` 関数と `unapply` 関数の2つの関数も受け取ります。UserData はケースクラスなので、`apply` メソッドと `unapply` メソッドを直接マッピングメソッドにつなぐことができます。

<!--
> **Note:** Maximum number of fields for a single tuple or mapping is 22 due to the way form handling is implemented. If you have more than 22 fields in your form, you should break down your forms using lists or nested values.
-->
> **メモ:** フォーム処理実装のための、単一タプルまたはマッピングのフィールドの最大数は 22 個です。フォームに 22 個以上のフィールドがある場合は、リストやネストされた値を使用してフォームを分割する必要があります。

<!--
A form will create `UserData` instance with the bound values when given a Map:
-->
フォームは、マップが与えられた時に `UserData` インスタンスを生成し、バインドされた値を保持します。

@[userForm-generate-map](code/ScalaForms.scala)

<!--
But most of the time you'll use forms from within an Action, with data provided from the request. [`Form`](api/scala/play/api/data/Form.html) contains [`bindFromRequest`](api/scala/play/api/data/Form.html#bindFromRequest\(\)\(Request%5B_%5D\)%3AForm%5BT%5D), which will take a request as an implicit parameter.  If you define an implicit request, then `bindFromRequest` will find it.
-->
しかし、ほとんどの場合、リクエストから提供されたデータとともに、アクションの中からフォームを使用します。[`Form`](api/scala/play/api/data/Form.html) は、暗黙的なパラメータとしてリクエストを受け取る [`bindFromRequest`](api/scala/play/api/data/Form.html#bindFromRequest\(\)\(Request%5B_%5D\)%3AForm%5BT%5D) を含んでいます。暗黙的なリクエストを定義すると、`bindFromRequest` がそれを探します。

@[userForm-generate-request](code/ScalaForms.scala)

<!--
> **Note:** There is a catch to using `get` here.  If the form cannot bind to the data, then `get` will throw an exception.  We'll show a safer way of dealing with input in the next few sections.
-->
> **メモ:** ここで `get` を使うことには難点があります。フォームがデータにバインドできない場合、`get` は例外をスローします。入力を扱うより安全な方法を、次のいくつかのセクションで示します。

<!--
You are not limited to using case classes in your form mapping.  As long as the apply and unapply methods are properly mapped, you can pass in anything you like, such as tuples using the [`Forms.tuple`](api/scala/play/api/data/Forms$.html) mapping or model case classes.  However, there are several advantages to defining a case class specifically for a form:
-->
フォームマッピングでは、ケースクラス以外を使用することもできます。apply と unapply メソッドが適切にマッピングされている限り、[`Forms.tuple`](api/scala/play/api/data/Forms$.html) マッピングやモデルケースクラスを使って、タプルなど好きなものを渡すことができます。しかし、フォーム専用のケースクラスを定義することには、いくつかの利点があります。

<!--
* **Form specific case classes are convenient.**  Case classes are designed to be simple containers of data, and provide out of the box features that are a natural match with `Form` functionality.
* **Form specific case classes are powerful.**  Tuples are convenient to use, but do not allow for custom apply or unapply methods, and can only reference contained data by arity (`_1`, `_2`, etc.)
* **Form specific case classes are targeted specifically to the Form.**  Reusing model case classes can be convenient, but often models will contain additional domain logic and even persistence details that can lead to tight coupling.  In addition, if there is not a direct 1:1 mapping between the form and the model, then sensitive fields must be explicitly ignored to prevent a [parameter tampering](https://www.owasp.org/index.php/Web_Parameter_Tampering) attack.
-->
* **フォーム専用のケースクラスは便利です。** ケースクラスは、データのシンプルなコンテナになるように設計されており、`Form` 機能と自然にマッチした機能をデフォルトで提供します。
* **フォーム専用のケースクラスは強力です。** タプルを使用するのは便利ですが、カスタムの apply や unapply メソッドは許可されず、含まれるデータは、(`_1`, `_2` など) 項数によってのみ参照できます。
* **フォーム固有のケースクラスは、フォームに特化したものです。** モデルケースクラスを再利用することは便利ですが、モデルには、追加のドメインロジックや、密結合につながる明細の永続化さえ含まれることがよくあります。さらに、フォームとモデルの間に直接の1対1のマッピングがない場合、[パラメータタンパリング](https://www.owasp.org/index.php/Web_Parameter_Tampering) 攻撃を防止するために、機密フィールドを明示的に無視する必要があります。

<!--
### Defining constraints on the form
-->
### フォームに制約を定義する

<!--
The `text` constraint considers empty strings to be valid.  This means that `name` could be empty here without an error, which is not what we want.  A way to ensure that `name` has the appropriate value is to use the `nonEmptyText` constraint.
-->
`text` 制約は、空文字列が有効であるとみなします。これは `name` をエラーなしで空にできることを意味しますが、これは望ましいものではありません。`name` が適切な値を持つことを保証するには `nonEmptyText` 制約を使います。

@[userForm-constraints-2](code/ScalaForms.scala)

<!--
Using this form will result in a form with errors if the input to the form does not match the constraints:
-->
このフォームを使用すると、フォームへの入力が制約と一致しない場合にエラーが発生します。

@[userForm-constraints-2-with-errors](code/ScalaForms.scala)

<!--
The out of the box constraints are defined on the [Forms object](api/scala/play/api/data/Forms$.html):
-->
デフォルトで使える制約は [Forms object](api/scala/play/api/data/Forms$.html) に定義されています。

<!--
* [`text`](api/scala/play/api/data/Forms$.html#text%3AMapping%5BString%5D): maps to `scala.String`, optionally takes `minLength` and `maxLength`.
* [`nonEmptyText`](api/scala/play/api/data/Forms$.html#nonEmptyText%3AMapping%5BString%5D): maps to `scala.String`, optionally takes `minLength` and `maxLength`.
* [`number`](api/scala/play/api/data/Forms$.html#number%3AMapping%5BInt%5D): maps to `scala.Int`, optionally takes `min`, `max`, and `strict`.
* [`longNumber`](api/scala/play/api/data/Forms$.html#longNumber%3AMapping%5BLong%5D): maps to `scala.Long`, optionally takes `min`, `max`, and `strict`.
* [`bigDecimal`](api/scala/play/api/data/Forms$.html#bigDecimal%3AMapping%5BBigDecimal%5D): takes `precision` and `scale`.
* [`date`](api/scala/play/api/data/Forms$.html#date%3AMapping%5BDate%5D), [`sqlDate`](api/scala/play/api/data/Forms$.html#sqlDate%3AMapping%5BDate%5D): maps to `java.util.Date`, `java.sql.Date`, optionally takes `pattern` and `timeZone`.
* [`email`](api/scala/play/api/data/Forms$.html#email%3AMapping%5BString%5D): maps to `scala.String`, using an email regular expression.
* [`boolean`](api/scala/play/api/data/Forms$.html#boolean%3AMapping%5BBoolean%5D): maps to `scala.Boolean`.
* [`checked`](api/scala/play/api/data/Forms$.html#checked%3AMapping%5BBoolean%5D): maps to `scala.Boolean`.
* [`optional`](api/scala/play/api/data/Forms$.html): maps to `scala.Option`.
 -->
* [`text`](api/scala/play/api/data/Forms$.html#text%3AMapping%5BString%5D): `scala.String` にマッピングされ、`minLength` と `maxLength` をオプションに取ります。
* [`nonEmptyText`](api/scala/play/api/data/Forms$.html#nonEmptyText%3AMapping%5BString%5D): `scala.String` にマッピングされ、`minLength` と `maxLength` をオプションに取ります。
* [`number`](api/scala/play/api/data/Forms$.html#number%3AMapping%5BInt%5D): `scala.Int` にマッピングされ、`min` 、`max` 、`strict` をオプションに取ります。
* [`longNumber`](api/scala/play/api/data/Forms$.html#longNumber%3AMapping%5BLong%5D): `scala.Long` にマッピングされ、`min` 、`max` 、`strict` をオプションに取ります。
* [`bigDecimal`](api/scala/play/api/data/Forms$.html#bigDecimal%3AMapping%5BBigDecimal%5D): `precision` と `scale` をパラメータに取ります。
* [`date`](api/scala/play/api/data/Forms$.html#date%3AMapping%5BDate%5D), [`sqlDate`](api/scala/play/api/data/Forms$.html#sqlDate%3AMapping%5BDate%5D), [`jodaDate`](api/scala/play/api/data/Forms$.html#jodaDate%3AMapping%5BDateTime%5D): `java.util.Date` 、`java.sql.Date` 、`org.joda.time.DateTime` にマッピングされ、`pattern` と `timeZone` をオプションに取ります。
* [`jodaLocalDate`](api/scala/play/api/data/Forms$.html#jodaLocalDate%3AMapping%5BLocalDate%5D): `org.joda.time.LocalDate` にマッピングされ、`pattern` をオプションに取ります。
* [`email`](api/scala/play/api/data/Forms$.html#email%3AMapping%5BString%5D): `scala.String` にマッピングされ、電子メールの正規表現を使用します。
* [`boolean`](api/scala/play/api/data/Forms$.html#boolean%3AMapping%5BBoolean%5D): `scala.Boolean` にマッピングされます。
* [`checked`](api/scala/play/api/data/Forms$.html#checked%3AMapping%5BBoolean%5D): `scala.Boolean` にマッピングされます。
* [`optional`](api/scala/play/api/data/Forms$.html): `scala.Option` にマッピングされます。

<!--
### Defining ad-hoc constraints
-->
### アドホック制約の定義

<!--
You can define your own ad-hoc constraints on the case classes using the [validation package](api/scala/play/api/data/validation/index.html).
-->
[バリデーションパッケージ](api/scala/play/api/data/validation/index.html) を使用して、ケースクラスに独自のアドホック制約を定義できます。

@[userForm-constraints](code/ScalaForms.scala)

<!--
You can also define ad-hoc constraints on the case classes themselves:
-->
また、ケースクラス自体にアドホック制約を定義することもできます。

@[userForm-constraints-ad-hoc](code/ScalaForms.scala)

<!--
You also have the option of constructing your own custom validations.  Please see the [[custom validations|ScalaCustomValidations]] section for more details.
-->
独自のカスタムバリデーションを構築するオプションもあります。詳細については、[[カスタムバリデーション|ScalaCustomValidations]] セクションを参照してください。

<!--
### Validating a form in an Action
-->
### アクション内のフォームの検証

<!--
Now that we have constraints, we can validate the form inside an action, and process the form with errors.
-->
制約を使用することにより、アクション内でフォームを検証し、エラーを用いてフォームを処理することができます。

<!--
We do this using the `fold` method, which takes two functions: the first is called if the binding fails, and the second is called if the binding succeeds.
-->
これは二つの関数を持つ `fold` メソッドを使って行います。一つ目の関数はバインディングが失敗した場合に呼び出され、二つ目はバインディングが成功した場合に呼び出されます。

@[userForm-handling-failure](code/ScalaForms.scala)

<!--
In the failure case, we render the page with BadRequest, and pass in the form _with errors_ as a parameter to the page.  If we use the view helpers (discussed below), then any errors that are bound to a field will be rendered in the page next to the field.
-->
失敗の場合、BadRequest のページをレンダリングし、_with errors_ をパラメータとしてページに渡します。ビューヘルパー (後述) を使用すると、フィールドにバインドされているエラーが、ページのフィールドの隣にレンダリングされます。

<!--
In the success case, we're sending a `Redirect` with a route to `routes.Application.home` here instead of rendering a view template.  This pattern is called  [Redirect after POST](https://en.wikipedia.org/wiki/Post/Redirect/Get), and is an excellent way to prevent duplicate form submissions.
 -->
成功の場合、ビューテンプレートをレンダリングする代わりに `routes.Application.home` へのルートを持つ `Redirect` を送ります。このパターンは [Redirect after POST](https://en.wikipedia.org/wiki/Post/Redirect/Get) と呼ばれ、フォームの重複送信を防ぐ優れた方法です。

<!--
> **Note:** "Redirect after POST" is **required** when using `flashing` or other methods with [[flash scope|ScalaSessionFlash]], as new cookies will only be available after the redirected HTTP request.
-->
> **メモ:** 新しいクッキーは、リダイレクトされた HTTP リクエストの後にのみ利用可能になるので、`flashing` または [[フラッシュスコープ|ScalaSessionFlash]] を持つ他のメソッドを使用する場合は、"Redirect after POST" が **必須** です。

<!--
Alternatively, you can use the `parse.form` [[body parser|ScalaBodyParsers]] that binds the content of the request to your form.
-->
あるいは、リクエストの内容をフォームにバインドする `parse.form` [[ボディパーサー|ScalaBodyParsers]] を使うことができます。

@[form-bodyparser](code/ScalaForms.scala)

<!--
In the failure case, the default behaviour is to return an empty `BadRequest` response. You can override this behaviour with your own logic. For instance, the following code is completely equivalent to the preceding one using `bindFromRequest` and `fold`.
-->
失敗の場合、デフォルトの動作は空の `BadRequest` レスポンスを返すことです。 独自のロジックでこの動作を無効にすることができます。例えば、次のコードは `bindFromRequest` と `fold` を使った前のコードと完全に同じです。

@[form-bodyparser-errors](code/ScalaForms.scala)

<!--
### Showing forms in a view template
-->
### ビューテンプレートにフォームを表示する

<!--
Once you have a form, then you need to make it available to the template engine.  You do this by including the form as a parameter to the view template.  For `user.scala.html`, the header at the top of the page will look like this:
-->
フォームを作成したら、それをテンプレートエンジンで使用できるようにする必要があります。これを行うには、フォームをビューテンプレートにパラメータとして含める必要があります。`user.scala.html` の場合、ページ上部のヘッダーは次のようになります。

@[form-define](code/scalaguide/forms/scalaforms/views/user.scala.html)

<!--
Because `user.scala.html` needs a form passed in, you should pass the empty `userForm` initially when rendering `user.scala.html`:
-->
`user.scala.html` にはフォームが渡される必要があるので、`user.scala.html` をレンダリングするときに空の `userForm` を最初に渡すべきです。

@[form-render](code/ScalaForms.scala)

<!--
The first thing is to be able to create the [form tag](api/scala/views/html/helper/form$.html). It is a simple view helper that creates a [form tag](http://www.w3.org/TR/html5/forms.html#the-form-element) and sets the `action` and `method` tag parameters according to the reverse route you pass in:
 -->
最初に、[form タグ](api/scala/views/html/helper/form$.html) を作成することができます。これは単純なビューヘルパーで、[form タグ](http://www.w3.org/TR/html5/forms.html#the-form-element) を作成し、引数に渡したリバースルートに従って `action` と `method` タグのパラメータを設定します。

@[form-user](code/scalaguide/forms/scalaforms/views/user.scala.html)

<!--
You can find several input helpers in the [`views.html.helper`](api/scala/views/html/helper/index.html) package. You feed them with a form field, and they display the corresponding HTML input, setting the value, constraints and displaying errors when a form binding fails.
 -->
いくつかの入力ヘルパーは [`views.html.helper`](api/scala/views/html/helper/index.html) にあります。それらをフォームフィールドで送信し、対応する HTML 入力を表示し、値や制約を設定し、フォームバインディングが失敗したときにエラーを表示します。

<!--
> **Note:** You can use `@import helper._` in the template to avoid prefixing helpers with `@helper.`
-->
> **メモ:** テンプレートで `@import helper._` を使うと、ヘルパーの先頭に `@helper.` が付かないようにすることができます。

<!--
There are several input helpers, but the most helpful are:
-->
いくつかの入力ヘルパーがありますが、最も役立つものは次のとおりです。

<!--
* [`form`](api/scala/views/html/helper/form$.html): renders a [form](https://www.w3.org/TR/html/sec-forms.html#the-form-element) element.
* [`inputText`](api/scala/views/html/helper/inputText$.html): renders a [text input](https://www.w3.org/TR/html/sec-forms.html#elementdef-input) element.
* [`inputPassword`](api/scala/views/html/helper/inputPassword$.html): renders a [password input](https://www.w3.org/TR/html/sec-forms.html#element-statedef-input-password) element.
* [`inputDate`](api/scala/views/html/helper/inputDate$.html): renders a [date input](https://www.w3.org/TR/html/sec-forms.html#element-statedef-input-date) element.
* [`inputFile`](api/scala/views/html/helper/inputFile$.html): renders a [file input](https://www.w3.org/TR/html/sec-forms.html#file-upload-state-typefile) element.
* [`inputRadioGroup`](api/scala/views/html/helper/inputRadioGroup$.html): renders a [radio input](https://www.w3.org/TR/html/sec-forms.html#element-statedef-input-radio-button) element.
* [`select`](api/scala/views/html/helper/select$.html): renders a [select](https://www.w3.org/TR/html/sec-forms.html#the-select-element) element.
* [`textarea`](api/scala/views/html/helper/textarea$.html): renders a [textarea](https://www.w3.org/TR/html/sec-forms.html#the-textarea-element) element.
* [`checkbox`](api/scala/views/html/helper/checkbox$.html): renders a [checkbox](https://www.w3.org/TR/html/sec-forms.html#element-statedef-input-checkbox) element.
* [`input`](api/scala/views/html/helper/input$.html): renders a generic input element (which requires explicit arguments).
-->
* [`inputText`](api/scala/views/html/helper/inputText$.html): [text input](http://www.w3.org/TR/html-markup/input.text.html) 要素をレンダリングします。
* [`inputPassword`](api/scala/views/html/helper/inputPassword$.html): [password input](http://www.w3.org/TR/html-markup/input.password.html#input.password) 要素をレンダリングします。
* [`inputDate`](api/scala/views/html/helper/inputDate$.html): [date input](http://www.w3.org/TR/html-markup/input.date.html) 要素をレンダリングします。
* [`inputFile`](api/scala/views/html/helper/inputFile$.html): [file input](http://www.w3.org/TR/html-markup/input.file.html) 要素をレンダリングします。
* [`inputRadioGroup`](api/scala/views/html/helper/inputRadioGroup$.html): [radio input](http://www.w3.org/TR/html-markup/input.radio.html#input.radio) 要素をレンダリングします。
* [`select`](api/scala/views/html/helper/select$.html): [select](http://www.w3.org/TR/html-markup/select.html#select) 要素をレンダリングします。
* [`textarea`](api/scala/views/html/helper/textarea$.html): [textarea](http://www.w3.org/TR/html-markup/textarea.html#textarea) 要素をレンダリングします。
* [`checkbox`](api/scala/views/html/helper/checkbox$.html): [checkbox](http://www.w3.org/TR/html-markup/input.checkbox.html#input.checkbox) 要素をレンダリングします。
* [`input`](api/scala/views/html/helper/input$.html): 汎用的な入力要素 (明示的な引数が必要) をレンダリングします。


> **Note:** The source code for each of these templates is defined as Twirl templates under `views/helper` package, and so the packaged version corresponds to the generated Scala source code.  For reference, it can be useful to see the [`views/helper` ](https://github.com/playframework/playframework/tree/master/core/play/src/main/scala/views/helper) package on Github.

<!--
As with the `form` helper, you can specify an extra set of parameters that will be added to the generated Html:
-->
`form` ヘルパーと同様に、生成された Html に追加される、追加のパラメータセットを指定することができます。

@[form-user-parameters](code/scalaguide/forms/scalaforms/views/user.scala.html)

<!--
The generic `input` helper mentioned above will let you code the desired HTML result:
-->
上記の汎用的な `input` ヘルパーは、あなたが望む HTML の結果をコード化することを可能にします。

@[form-user-parameters-html](code/scalaguide/forms/scalaforms/views/user.scala.html)

<!--
> **Note:** All extra parameters will be added to the generated Html, unless they start with the **\_** character. Arguments starting with **\_** are reserved for [[field constructor arguments|ScalaCustomFieldConstructors]].
-->
> **メモ:** **\_** 文字で始まらない限り、追加のパラメータはすべて生成された HTML に追加されます。 **\_** で始まる引数は、[[フィールドコンストラクタ引数|ScalaCustomFieldConstructors]] 用に予約されています。

<!--
For complex form elements, you can also create your own custom view helpers (using scala classes in the `views` package) and [[custom field constructors|ScalaCustomFieldConstructors]].
-->
複雑なフォーム要素については、独自のカスタムビューヘルパー (`views` パッケージの scala クラスを使用) と [[カスタムフィールドコンストラクタ|ScalaCustomFieldConstructors]] を作成することもできます。

### Passing MessagesProvider to Form Helpers

The form helpers above -- [`input`](api/scala/views/html/helper/input$.html), [`checkbox`](api/scala/views/html/helper/checkbox$.html), and so on -- all take [`MessagesProvider`](api/scala/play/api/i18n/MessagesProvider.html) as an implicit parameter.  The form handlers need to take [`MessagesProvider`](api/scala/play/api/i18n/MessagesProvider.html) because they need to provide error messages mapped to the language defined in the request.  You can see more about [`Messages`](api/scala/play/api/i18n/Messages.html) in the [[Internationalization with Messages|ScalaI18N]] page.

There are two ways to pass in the [`MessagesProvider`](api/scala/play/api/i18n/MessagesProvider.html) object required.

#### Option One: Implicitly Convert Request to Messages

The first way is to make the controller extend [`play.api.i18n.I18nSupport`](api/scala/play/api/i18n/I18nSupport.html), which makes use of an injected [`MessagesApi`](api/scala/play/api/i18n/MessagesApi.html), and will implicitly convert an implicit request to an implicit [`Messages`](api/scala/play/api/i18n/Messages.html):

@[messages-controller](code/ScalaForms.scala)

This means that the following form template will be resolved:

@[form-define](code/scalaguide/forms/scalaforms/views/implicitMessages.scala.html)

#### Option Two: Use MessagesRequest

The second way is to dependency inject a [`MessagesActionBuilder`](api/scala/play/api/mvc/MessagesActionBuilder.html), which provides a [`MessagesRequest`](api/scala/play/api/mvc/MessagesRequest.html):

@[messages-request-controller](code/ScalaForms.scala)

This is useful because to use [[CSRF|ScalaCsrf]] with forms, both a `Request` (technically a `RequestHeader`) and a [`Messages`](api/scala/play/api/i18n/Messages.html) object must be available to the template.  By using a [`MessagesRequest`](api/scala/play/api/mvc/MessagesRequest.html), which is a [`WrappedRequest`](api/scala/play/api/mvc/WrappedRequest.html) that extends [`MessagesProvider`](api/scala/play/api/i18n/MessagesProvider.html), only a single implicit parameter needs to be made available to templates.

Because you typically don't need the body of the request, you can pass [`MessagesRequestHeader`](api/scala/play/api/mvc/MessagesRequestHeader.html), rather than typing `MessagesRequest[_]`:

@[form-define](code/scalaguide/forms/scalaforms/views/messages.scala.html)

Rather than inject [`MessagesActionBuilder`](api/scala/play/api/mvc/MessagesActionBuilder.html) into your controller, you can also make [`MessagesActionBuilder`](api/scala/play/api/mvc/MessagesActionBuilder.html) be the default `Action` by extending [MessagesAbstractController](api/scala/play/api/mvc/MessagesAbstractController.html) to incorporate form processing into your controllers.

@[messages-abstract-controller](code/ScalaForms.scala)

<!--
### Displaying errors in a view template
-->
### ビューテンプレートでのエラーの表示

<!--
The errors in a form take the form of `Map[String,FormError]` where [`FormError`](api/scala/play/api/data/FormError.html) has:
-->
フォームのエラーは、`Map[String,FormError]` の形式をとり、[`FormError`](api/scala/play/api/data/FormError.html) は次のような情報を持ちます。

<!--
* `key`: should be the same as the field.
* `message`: a message or a message key.
* `args`: a list of arguments to the message.
 -->
* `key`: フィールドと同じである必要があります。
* `message`: メッセージまたはメッセージキー。
* `args`: メッセージへの引数のリスト。

<!--
The form errors are accessed on the bound form instance as follows:
 -->
フォームエラーは、バインドされたフォームインスタンスにて、次のようにアクセスされます。

<!--
* `errors`: returns all errors as `Seq[FormError]`.
* `globalErrors`: returns errors without a key as `Seq[FormError]`.
* `error("name")`: returns the first error bound to key as `Option[FormError]`.
* `errors("name")`: returns all errors bound to key as `Seq[FormError]`.
 -->
* `errors`: すべてのエラーを `Seq[FormError]` として返します。
* `globalErrors`: キー無しのエラーを `Seq[FormError]` として返します。
* `error("name")`: key にバインドされた最初のエラーを `Option[FormError]` として返します。
* `errors("name")`: key にバインドされたすべてのエラーを `Seq[FormError]` として返します。

<!--
Errors attached to a field will render automatically using the form helpers, so `@helper.inputText` with errors can display as follows:
 -->
フィールドのエラーはフォームヘルパーを使って自動的にレンダリングされるので、エラーのある `@helper.inputText` は次のように表示されます。

@[form-user-generated](code/scalaguide/forms/scalaforms/views/user.scala.html)

Errors that are not attached to a field can be converted to a string with `error.format`, which takes an implicit [play.api.i18n.Messages](api/scala/play/api/i18n/Messages.html) instance.

<!--
Global errors that are not bound to a key do not have a helper and must be defined explicitly in the page:
 -->
キーにバインドされていないグローバルエラーにはヘルパーがなく、ページで明示的に定義する必要があります。

@[global-errors](code/scalaguide/forms/scalaforms/views/user.scala.html)

<!--
### Mapping with tuples
 -->
### タプルによるマッピング

<!--
You can use tuples instead of case classes in your fields:
 -->
フィールドには、ケースクラスの代わりにタプルを使用できます。

@[userForm-tuple](code/ScalaForms.scala)

<!--
Using a tuple can be more convenient than defining a case class, especially for low arity tuples:
 -->
タプルを使用すると、特に項数が少ないタプルの場合、ケースクラスを定義するよりも便利になります。

@[userForm-tuple-example](code/ScalaForms.scala)

<!--
### Mapping with single
 -->
### 単一のマッピング

<!--
Tuples are only possible when there are multiple values.  If there is only one field in the form, use `Forms.single` to map to a single value without the overhead of a case class or tuple:
 -->
タプルは値が複数の場合にのみ使用可能です。フォームにフィールドが1つしかない場合は `Forms.single` を使用して、ケースクラスまたはタプルのオーバーヘッドなしに単一の値にマップします。

@[form-single-value](code/ScalaForms.scala)

<!--
### Fill values
 -->
### 値の埋め込み

<!--
Sometimes you’ll want to populate a form with existing values, typically for editing data:
 -->
典型的にはデータを編集する場合など、フォームに既存の値を設定したくなる場合があります。

@[userForm-filled](code/ScalaForms.scala)

<!--
When you use this with a view helper, the value of the element will be filled with the value:
 -->
これをビューヘルパーで使用すると、指定した要素の値に埋め込み値をセットすることができます。

```html
@helper.inputText(filledForm("name")) @* will render value="Bob" *@
```

<!--
Fill is especially helpful for helpers that need lists or maps of values, such as the [`select`](api/scala/views/html/helper/select$.html) and [`inputRadioGroup`](api/scala/views/html/helper/inputRadioGroup$.html) helpers.  Use [`options`](api/scala/views/html/helper/options$.html) to value these helpers with lists, maps and pairs:
 -->
値の埋め込みは、[`select`](api/scala/views/html/helper/select$.html) や [`inputRadioGroup`](api/scala/views/html/helper/inputRadioGroup$.html) ヘルパーのような、リストや値のマップを必要とするヘルパーにとって特に便利です。これらのヘルパーをリスト、マップ、ペアで評価するには [`options`](api/scala/views/html/helper/options$.html) を使います。

A single valued form mapping can set the selected options in a select
dropdown:

@[addressSelectForm-constraint](code/ScalaForms.scala)

@[addressSelectForm-filled](code/ScalaForms.scala)

And when this is used in a template that sets the options to a list of pairs

@[select-form-define](code/scalaguide/forms/scalaforms/views/select.scala.html)

@[addressSelectForm-options-usage](code/scalaguide/forms/scalaforms/views/select.scala.html)

The filled value will be selected in the dropdown based on the first value of the pair.
In this case, the U.K. Office will be displayed in the select and the option's value
will be London.

<!--
### Nested values
 -->
### ネストされた値

<!--
A form mapping can define nested values by using [`Forms.mapping`](api/scala/play/api/data/Forms$.html) inside an existing mapping:
 -->
フォームマッピングでは、既存のマッピング内で [`Forms.mapping`](api/scala/play/api/data/Forms$.html) を使用してネストされた値を定義できます。

@[userData-nested](code/ScalaForms.scala)

@[userForm-nested](code/ScalaForms.scala)

<!--
> **Note:** When you are using nested data this way, the form values sent by the browser must be named like `address.street`, `address.city`, etc.
 -->
> **メモ:** この方法でネストされたデータを使用している場合、ブラウザが送信するフォームの値は、 `address.street`、`address.city` などの名前にする必要があります。

@[form-field-nested](code/scalaguide/forms/scalaforms/views/nested.scala.html)

<!--
### Repeated values
 -->
### 繰り返し値

<!--
A form mapping can define repeated values using [`Forms.list`](api/scala/play/api/data/Forms$.html) or [`Forms.seq`](api/scala/play/api/data/Forms$.html):
 -->
フォームマッピングでは、[`Forms.list`](api/scala/play/api/data/Forms$.html) か [`Forms.seq`](api/scala/play/api/data/Forms$.html) を使用して繰り返し値を定義できます。

@[userListData](code/ScalaForms.scala)

@[userForm-repeated](code/ScalaForms.scala)

<!--
When you are using repeated data like this, there are two alternatives for sending the form values in the HTTP request.  First, you can suffix the parameter with an empty bracket pair, as in "emails[]".  This parameter can then be repeated in the standard way, as in `http://foo.com/request?emails[]=a@b.com&emails[]=c@d.com`.  Alternatively, the client can explicitly name the parameters uniquely with array subscripts, as in `emails[0]`, `emails[1]`, `emails[2]`, and so on.  This approach also allows you to maintain the order of a sequence of inputs.
 -->
このような繰り返しデータを使用する場合、HTTP リクエストでフォーム値を送信する方法が2つあります。まず、"emails[]" のように空の括弧でパラメータの末尾に追加することができます。このパラメータは、 `http://foo.com/request?emails[]=a@b.com&emails[]=c@d.com` のように、標準的な方法で繰り返すことができます。あるいはクライアントは、`emails[0]`、`emails[1]`、`emails[2]` などのように、配列の添字でパラメータを明示的に指定することもできます。この方法では、一連の入力の順序を維持することもできます。

<!--
If you are using Play to generate your form HTML, you can generate as many inputs for the `emails` field as the form contains, using the [`repeat`](api/scala/views/html/helper/repeat$.html) helper:
 -->
フォーム HTML を生成するために Play を使用している場合、フォームに含まれるのと同じ数の `emails` フィールドの入力を生成するには、[`repeat`](api/scala/views/html/helper/repeat$.html) ヘルパーを使用します。

@[form-field-repeat](code/scalaguide/forms/scalaforms/views/repeat.scala.html)

<!--
The `min` parameter allows you to display a minimum number of fields even if the corresponding form data are empty.
 -->
`min` パラメータは、対応するフォームデータが空であっても、最小数のフィールドを表示することを可能にします。

If you want to access the index of the fields you can use the `repeatWithIndex` helper instead:

@[form-field-repeat-with-index](code/scalaguide/forms/scalaforms/views/repeat.scala.html)

<!--
### Optional values
 -->
### オプションの値

<!--
A form mapping can also define optional values using [`Forms.optional`](api/scala/play/api/data/Forms$.html):
 -->
フォームマッピングでは、[`Forms.optional`](api/scala/play/api/data/Forms$.html) を使ってオプションの値を定義することもできます。

@[userData-optional](code/ScalaForms.scala)

@[userForm-optional](code/ScalaForms.scala)

<!--
This maps to an `Option[A]` in output, which is `None` if no form value is found.
 -->
これは、出力の `Option[A]` にマッピングされ、フォーム値が見つからない場合は `None` になります。

<!--
### Default values
 -->
### デフォルト値

<!--
You can populate a form with initial values using [`Form#fill`](api/scala/play/api/data/Form.html):
 -->
[`Form＃fill`](api/scala/play/api/data/Form.html) を使用してフォームに初期値を設定できます。


@[userForm-filled](code/ScalaForms.scala)

<!--
Or you can define a default mapping on the number using [`Forms.default`](api/scala/play/api/data/Forms$.html):
 -->
あるいは、[`Forms.default`](api/scala/play/api/data/Forms$.html) を使って番号にデフォルトのマッピングを定義することもできます。@[userForm-default](code/ScalaForms.scala)



Keep in mind that default values are used only when:

1. Populating the `Form` from data, for example, from the request
2. And there is no corresponding data for the field.

The default value is not used when creating the form.

<!--
### Ignored values
 -->
### 無視される値

<!--
If you want a form to have a static value for a field, use [`Forms.ignored`](api/scala/play/api/data/Forms$.html):
 -->
フォームにフィールドの静的な値を持たせたい場合は、[`Forms.ignored`](api/scala/play/api/data/Forms$.html) を使います。

@[userForm-static-value](code/ScalaForms.scala)

### Custom binders for form mappings

Each form mapping uses an implicitly provided [`Formatter[T]`](api/scala/play/api/data/format/Formatter.html) binder object that performs the conversion of incoming `String` form data to/from the target data type.

@[userData-custom-datatype](code/ScalaForms.scala)

To bind to a custom type like java.net.URL in the example above, define a form mapping like this:

@[userForm-custom-datatype](code/ScalaForms.scala)

For this to work you will need to make an implicit `Formatter[java.net.URL]` available to perform the data binding/unbinding.

@[userForm-custom-formatter](code/ScalaForms.scala)

Note the [`Formats.parsing`](api/scala/play/api/data/format/Formats$.html) function is used to capture any exceptions thrown in the act of converting a `String` to target type `T` and registers a [`FormError`](api/scala/play/api/data/FormError.html) on the form field binding.

<!--
## Putting it all together
 -->
## すべてを一緒に入れる

<!--
Here's an example of what a model and controller would look like for managing an entity.
 -->
次に、エンティティを管理するためのモデルとコントローラの例を示します。

<!--
Given the case class `Contact`:
 -->
まず、以下のようなケースクラス `Contact` を与えます。

@[contact-define](code/ScalaForms.scala)

<!--
Note that `Contact` contains a `Seq` with `ContactInformation` elements and a `List` of `String`.  In this case, we can combine the nested mapping with repeated mappings (defined with `Forms.seq` and `Forms.list`, respectively).
 -->
`Contact` は、`ContactInformation` 要素を持つ `Seq` と、`String` の `List` を含んでいます。この場合、ネストされたマッピングと繰り返しのマッピング (それぞれ `Forms.seq` と `Forms.list` で定義されます) を組み合わせることができます。

@[contact-form](code/ScalaForms.scala)

<!--
And this code shows how an existing contact is displayed in the form using filled data:
 -->
そしてこのコードは、埋め込んだデータを使用して既存の連絡先をフォームに表示する方法を示しています。

@[contact-edit](code/ScalaForms.scala)

<!--
Finally, this is what a form submission handler would look like:
 -->
最後に、フォーム送信ハンドラは次のようになります。

@[contact-save](code/ScalaForms.scala)
