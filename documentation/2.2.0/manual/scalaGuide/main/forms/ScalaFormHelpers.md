<!-- translated -->
<!--
# Using the form template helpers
-->
# フォームテンプレートヘルパーの利用

<!--
Play provides several helpers for rendering form fields in HTML templates.
-->
Play には HTML テンプレート中にフォームやフィールドを生成するためのヘルパー関数が用意されています。

<!--
## Create a `<form>` tag
-->
## `<form>` タグの作成
    
<!--
The first thing is to be able to create the `<form>` tag. It is a pretty simple helper that has no more value than automatically setting the `action` and `method` tag parameters according to the reverse route you pass in:
-->
まずは `<form>` タグの作り方です。リバースルートから `action`, `method` を取り出して、フォームに自動的にセットするだけの単純なヘルパー関数の使い方は、次のとおりです。

@[form-tag](code/scalaguide/forms/scalaformhelper/views/login.scala.html)

<!--
You can also pass an extra set of parameters that will be added to the generated Html:
-->
生成される HTML に追加されるその他のパラメータを渡すこともできます。

@[form-pass-parameters](code/scalaguide/forms/scalaformhelper/views/login.scala.html)

<!--
## Rendering an `<input>` element
-->
## `<input>` 要素のレンダリング

<!--
You can find several input helpers in the `views.html.helper` package. You feed them with a form field, and they display the corresponding HTML input, setting the value, constraints and errors:
-->
`views.html.helper` パッケージには input 要素のヘルパーがいくつか用意されています。これらヘルパーにフォームフィールドのオブジェクトを渡すと、それに対応した HTML フォームコントロールが表示されます。コントロールには値がセットされ、制約、エラー情報も付与されます。

@[form-define](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

@[form-user](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
As for the `form` helper, you can specify an extra set of parameters that will be added to the generated Html:
-->
`form` ヘルパーと同様に、生成される HTML に追加されるその他のパラメータを指定することができます。

@[form-user-parameters](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
> **Note:** All extra parameters will be added to the generated Html, unless they start with the **\_** character. Arguments starting with **\_** are reserved for field constructor arguments (we will see that shortly).
-->
> **Note:** **\_** 以外の文字で始まるパラメータは全て生成される HTML に追加されます。 **\_** で始まるパラメータは、フィールドコンストラクタの引数（後で説明します）のために予約されています。

<!--
## Handling HTML input creation yourself
-->
## input 要素を自由に生成する

<!--
There is also a more generic `input` helper that lets you code the desired HTML result:
-->
生成される HTML をもっと細かくコントロールしたい時のために、より汎用的な `input` ヘルパーも用意されています。

@[form-user-parameters-html](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
## Field constructors
-->
## フィールドコンストラクタ

<!--
A field rendering is not only composed of the `<input>` tag, but it also needs a `<label>` and possibly other tags used by your CSS framework to decorate the field.
-->
フィールドをレンダリングする際、単に `<input>` タグを出力するだけでなく、`<label>` タグや、利用している CSS フレームワークによってはフィールドを修飾する他の HTML 要素を出力しなければならないこともあります。
    
<!--
All input helpers take an implicit `FieldConstructor` that handles this part. The default one (used if there are no other field constructors available in the scope), generates HTML like:
-->
全ての input ヘルパーはこの目的のために `FieldConstructor` という暗黙的な引数をとります。デフォルト (フィールドコンストラクタがスコープ内に存在しない場合に使われる) では、次のような HTML が生成されます。

@[form-user-generated](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
This default field constructor supports additional options you can pass in the input helper arguments:
-->
デフォルトのフィールドコンストラクタは、input ヘルパーの引数に渡すことができる以下の追加オプションに対応しています。

@[form-field-options](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
## Twitter bootstrap field constructor
-->
## Twitter bootstrap フィールドコンストラクタ

There is also another built-in field constructor that can be used with [TwitterBootstrap](http://twitter.github.com/bootstrap/) 1.4.  (Note: 2.0.2 will support LESS 1.3 and Bootstrap 2.0.)

<!--
To use it, just import it in the current scope:
-->
これを利用するためには、単にスコープ内にインポートしてください。

@[import-twitterbootstrap](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
It generates Html like:
-->
このフィールドコンストラクタは、次のような HTML を生成します。

@[form-bootstrap-generated](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
It supports the same set of options as the default field constructor (see below).
-->
また、デフォルトのフィールドコンストラクタと同様のオプションにも対応しています (以下を参照) 。

<!--
## Writing your own field constructor
-->
## フィールドコンストラクタを自作する

<!--
Often you will need to write your own field constructor. Start by writing a template like:
-->
独自のフィールドコンストラクタが必要になることもあります。まず以下のようなテンプレートを作成します。

@[form-myfield](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
> **Note:** This is just a sample. You can make it as complicated as you need. You also have access to the original field using `@elements.field`.
-->
> **Note:** これはただのサンプルです。必要であれば、もっと複雑なテンプレートを作成することもできます。また、`@elements.field` と書くと元のフィールドを参照することができます。

<!--
Now create a `FieldConstructor` using this template function:
-->
次に、このテンプレートを利用して `FieldConstructor` を作成します。

@[form-myfield-helper](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
And to make the form helpers use it, just import it in your templates:
-->
フォームヘルパーにこのフィールドコンストラクタを使わせるため、テンプレートにインポートします。

@[import-myhelper](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

@[form-myfield-helper](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
It will then use your field constructor to render the input text.
-->
これで、作成したフィールドコンストラクタを使って、テキスト用の input 要素がレンダリングされます。

<!--
> **Note:** You can also set an implicit value for your `FieldConstructor` inline in your template this way:
-->
> **Note:** テンプレート内で `FieldConstructor` を implicit value として定義することもできます。
>
>@[import-myhelper-implicit](code/scalaguide/forms/scalaformhelper/views/user.scala.html)
>
>@[form-myfield-helper](code/scalaguide/forms/scalaformhelper/views/user.scala.html)

<!--
## Handling repeated values
-->
## 値の繰り返しを扱う

<!--
The last helper makes it easier to generate inputs for repeated values. Let’s say you have this kind of form definition:
-->
最後に紹介するヘルパーは、値の繰り返しに関する input 要素を生成するものです。まず、次のようなフォームを定義したとします。

@[userForm-repeated](code/ScalaForms.scala)

<!--
Now you have to generate as many inputs for the `emails` field as the form contains. Just use the `repeat` helper for that:
-->
次に、フォームにの `emails` フィールドのために沢山の input 要素を生成する必要があります。ここで `repeat` ヘルパーが役に立ちます。

@[form-field-repeat](code/scalaguide/forms/scalaformhelper/views/register.scala.html)

<!--
The `min` parameter allows you to display a minimum number of fields even if the corresponding form data are empty.
-->
フィールドが空の場合であっても、`min` パラメータに指定した個数の input 要素が生成されます。

> **Next:** [[Protecting against CSRF|ScalaCsrf]]



