<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Form template helpers
-->
# フォームテンプレートヘルパーの利用

<!--
Play provides several helpers to help you render form fields in HTML templates.
-->
Play には HTML テンプレート中にフォームやフィールドを生成するためのヘルパー関数が用意されています。

<!--
## Creating a `<form>` tag
-->
## `<form>` タグの作成
    
<!--
The first helper creates the `<form>` tag. It is a pretty simple helper that automatically sets the `action` and `method` tag parameters according to the reverse route you pass in:
-->
まず、 `<form>` タグを作成するヘルパーがあります。これは、 `action` と `method` タグパラメータを引数に渡したリバースルートに基づいて設定する、かなりシンプルなヘルパーです。

@[form](code/javaguide/forms/helpers.scala.html)

<!--
You can also pass an extra set of parameters that will be added to the generated HTML:
-->
生成される HTML に追加されるその他のパラメータを渡すこともできます。

@[form-with-id](code/javaguide/forms/helpers.scala.html)

<!--
## Rendering an `<input>` element
-->
## `<input>` 要素のレンダリング

<!--
There are several input helpers in the `views.html.helper` package. You feed them with a form field, and they display the corresponding HTML form control, with a populated value, constraints and errors:
-->
`views.html.helper` パッケージには input 要素のヘルパーがいくつか用意されています。これらヘルパーにフォームフィールドのオブジェクトを渡すと、それに対応した HTML フォームコントロールが表示されます。コントロールには値がセットされ、制約、エラー情報も付与されます。

@[full-form](code/javaguide/forms/fullform.scala.html)

<!--
As for the `form` helper, you can specify an extra set of parameters that will be added to the generated HTML:
-->
`form` ヘルパーと同様に、生成される HTML に追加されるその他のパラメータを指定することができます。

@[extra-params](code/javaguide/forms/helpers.scala.html)

<!--
> **Note:** All extra parameters will be added to the generated HTML, except for ones whose name starts with the `_` character. Arguments starting with an underscore are reserved for field constructor argument (which we will see later).
-->
> **ノート:** `_` という文字以外で始まるパラメータは全て生成される HTML に追加されます。アンダースコアで始まるパラメータは、フィールドコンストラクタの引数（後で説明します）のために予約されています。

<!--
## Handling HTML input creation yourself
-->
## input 要素を自由に生成する

<!--
There is also a more generic `input` helper that let you code the desired HTML result:
-->
生成される HTML をもっと細かくコントロールしたい時のために、より汎用的な `input` ヘルパーも用意されています。

@[generic-input](code/javaguide/forms/helpers.scala.html)

<!--
## Field constructors
-->
## フィールドコンストラクタ

<!--
A rendered field does not only consist of an `<input>` tag, but may also need a `<label>` and a bunch of other tags used by your CSS framework to decorate the field.
-->
フィールドをレンダリングする際、単に `<input>` タグを出力するだけでなく、`<label>` タグや、利用している CSS フレームワークによってはフィールドを修飾する他の HTML 要素を出力しなければならないこともあります。
    
<!--
All input helpers take an implicit `FieldConstructor` that handles this part. The default one (used if there are no other field constructors available in the scope), generates HTML like:
-->
全ての input ヘルパーはこの目的のために `FieldConstructor` という暗黙的な引数をとります。デフォルト (フィールドコンストラクタがスコープ内に存在しない場合に使われる) では、次のような HTML が生成されます。

```
<dl class="error" id="email_field">
    <dt><label for="email">Email:</label></dt>
    <dd><input type="text" name="email" id="email" value=""></dd>
    <dd class="error">This field is required!</dd>
    <dd class="error">Another error</dd>
    <dd class="info">Required</dd>
    <dd class="info">Another constraint</dd>
</dl>
```

<!--
This default field constructor supports additional options you can pass in the input helper arguments:
-->
デフォルトのフィールドコンストラクタは、input ヘルパーの引数に渡すことができる以下の追加オプションに対応しています。

```
'_label -> "Custom label"
'_id -> "idForTheTopDlElement"
'_help -> "Custom help"
'_showConstraints -> false
'_error -> "Force an error"
'_showErrors -> false
```

<!--
### Writing your own field constructor
-->
### フィールドコンストラクタを自作する

<!--
Often you will need to write your own field constructor. Start by writing a template like:
-->
独自のフィールドコンストラクタが必要になることもあります。まず以下のようなテンプレートを作成します。

@[template](code/javaguide/forms/myFieldConstructorTemplate.scala.html)

<!--
Save it in `views/` and name `myFieldConstructorTemplate.scala.html`
-->
これを `myFieldConstructorTemplate.scala.html` という名前で `views/` に保存します。

<!--
> **Note:** This is just a sample. You can make it as complicated as you need. You have also access to the original field using `@elements.field`.
-->
> **Note:** これはただのサンプルです。必要であれば、もっと複雑なテンプレートを作成することもできます。また、`@elements.field` と書くと元のフィールドを参照することができます。

<!--
Now create a `FieldConstructor` somewhere, using:
-->
次に、以下のように `FieldConstructor` を作成します。

@[field](code/javaguide/forms/withFieldConstructor.scala.html)

<!--
## Handling repeated values
-->
## 値の繰り返しを扱う

<!--
The last helper makes it easier to generate inputs for repeated values. Suppose you have this kind of form definition:
-->
最後に紹介するヘルパーは、値の繰り返しに関する input 要素を生成するものです。まず、次のようなフォームを定義したとします。

@[code](code/javaguide/forms/html/UserForm.java)

<!--
Now you have to generate as many inputs for the `emails` field as the form contains. Just use the `repeat` helper for that:
-->
次に、フォームにの `emails` フィールドのために沢山の input 要素を生成する必要があります。ここで `repeat` ヘルパーが役に立ちます。

@[repeat](code/javaguide/forms/helpers.scala.html)

<!--
Use the `min` parameter to display a minimum number of fields, even if the corresponding form data are empty.
-->
フィールドが空の場合であっても、`min` パラメータに指定した個数の input 要素が生成されます。

<!--
> **Next:** [[Protecting against CSRF|JavaCsrf]]
-->
> **Next:** [[CSRF 対策|JavaCsrf]]



