<!--translated-->
<!--
# Form template helpers
-->
# フォームテンプレートヘルパー

<!--
Play provides several helpers to help you render form fields in HTML templates.
-->
PlayにはフォームのフィールドをHTMLテンプレート内に描画するためのヘルパがいくつか用意されています。

<!--
## Creating a `<form>` tag
-->
## `<form>`タグを作成する

<!--
The first helper creates the `<form>` tag. It is a pretty simple helper that automatically sets the `action` and `method` tag parameters according to the reverse route you pass in:
-->
まず、`<form>`タグを作成するヘルパーがあります。これは、`action`と`method`タグパラメータを引数に渡したリバースルートに基いて設定してくれる、というかなりシンプルなヘルパーです。
    
```
@helper.form(action = routes.Application.submit()) {
    
}
```

<!--
You can also pass an extra set of parameters that will be added to the generated HTML:  
-->
さらに、生成されるHTMLに追加されるその他のパラメータを渡すこともできます。

```
@helper.form(action = routes.Application.submit(), 'id -> "myForm") {
    
}
```

<!--
## Rendering an `<input>` element
-->
## `<input>`要素をレンダリングする

<!--
There are several input helpers in the `views.html.helper` package. You feed them with a form field, and they display the corresponding HTML form control, with a populated value, constraints and errors:
-->
`views.html.helper`パッケージにはinput要素のヘルパーがいくつか用意されています。これらヘルパーにフォームフィールドのオブジェクトを渡すと、それに対応したHTMLフォームコントロールが表示されます。コントロールには値がセットされ、制約、エラー情報も付与されます。

```
@(myForm: Form[User])

@helper.form(action = routes.Application.submit()) {
    
    @helper.inputText(myForm("username"))
    
    @helper.inputPassword(myForm("password"))
    
}
```

<!--
As for the `form` helper, you can specify an extra set of parameters that will be added to the generated HTML:
-->
`form`ヘルパーと同様に、生成されるHTMLに追加されるその他のパラメータを指定することができます。

```
@helper.inputText(myForm("username"), 'id -> "username", 'size -> 30)
```

<!--
> **Note:** All extra parameters will be added to the generated HTML, except for ones whose name starts with the `_` character. Arguments starting with an underscore are reserved for field constructor argument (which we will see later).
-->
> **ノート:** `_`という文字で始まるパラメータ以外は全て生成されるHTMLに追加されます。このアンダースコアで始まる引数は、「フィールドコンストラクター引数」（後で説明します）のために予約されています。

<!--
## Handling HTML input creation yourself
-->
## HTML inputの生成を自分でコントロールする

<!--
There is also a more generic `input` helper that let you code the desired HTML result:
-->
生成されるHTMLを好きなようにカスタマイズできるように、これまで紹介したものよりもっと基本的な`input`ヘルパーも用意されています。

```
@helper.input(myForm("username")) { (id, name, value, args) =>
    <input type="date" name="@name" id="@id" @toHtmlArgs(args)>
} 
```

<!--
## Field constructors
-->
## フィールドコンストラクター

<!--
A rendered field does not only consist of an `<input>` tag, but may also need a `<label>` and a bunch of other tags used by your CSS framework to decorate the field.
    
All input helpers take an implicit `FieldConstructor` that handles this part. The default one (used if there are no other field constructors available in the scope), generates HTML like:
-->
レンダリングされたフィールドには`<input>`タグ以外に`<label>`タグ、お使いのCSSフレームワークがフィールドの修飾に使うものなど、様々なタグが含まれます。

全てのinputヘルパーはこのようなタグの生成を担当する`FieldConstructor`を暗黙的な引数にとります。デフォルトのもの（スコープ内にフィールドコンストラクターが存在しない場合に使われる）は以下の様なHTMLを生成します。

```
<dl class="error" id="username_field">
    <dt><label for="username"><label>Username:</label></dt>
    <dd><input type="text" name="username" id="username" value=""></dd>
    <dd class="error">This field is required!</dd>
    <dd class="error">Another error</dd>
    <dd class="info">Required</dd>
    <dd class="info">Another constraint</dd>
</dl>
```

<!--
This default field constructor supports additional options you can pass in the input helper arguments:
-->
デフォルトのフィールドコンストラクターには、追加オプションを受け取って、それをinputヘルパに渡すという機能もあります。

```
'_label -> "Custom label"
'_id -> "idForTheTopDlElement"
'_help -> "Custom help"
'_showConstraints -> false
'_error -> "Force an error"
'_showErrors -> false
```

<!--
## Twitter bootstrap field constructor
-->
## Twitter bootstrapフィールドコンストラクター

<!--
There is another built-in field constructor that can be used with [[Twitter Bootstrap | http://twitter.github.com/bootstrap/]].

To use it, just import it in the current scope:
-->
[[Twitter Bootstrap | http://twitter.github.com/bootstrap/]]向けのフィールドコンストラクターも組み込みで用意されています。

これを利用するためには、単にスコープ内にインポートしてください。

```
@import helper.twitterBootstrap._
```

<!--
This field constructor generates HTML like the following:
-->
このフィールドコンストラクターは以下のようなHTMLを生成します。

```
<div class="clearfix error" id="username_field">
  <label for="username">Username:</label>
  <div class="input">
    <input type="text" name="username" id="username" value="">
    <span class="help-inline">This field is required!, Another error</span>
    <span class="help-block">Required, Another constraint</d</span> 
  </div>
</div>
```

<!--
It supports the same set of options as the default field constructor (see above).
-->
先ほど説明したデフォルトのフィールドコンストラクターと同様に、追加オプションにも対応しています。

<!--
## Writing you own field constructor
-->
## 独自のフィールドコンストラクターを書く

<!--
Often you will need to write your own field constructor. Start by writing a template like:
-->
独自のフィールドコンストラクターが必要になることもあります。フィールドコンストラクターを定義するには、まず以下ようなテンプレートを作成しましょう。

```
@(elements: helper.FieldElements)

<div class="@if(elements.hasErrors) {error}">
    <label for="@elements.id">@elements.label</label>
    <div class="input">
        @elements.input
        <span class="errors">@elements.errors.mkString(", ")</span>
        <span class="help">@elements.infos.mkString(", ")</span> 
    </div>
</div>
```

<!--
> **Note:** This is just a sample. You can make it as complicated as you need. You have also access to the original field using `@elements.field`.

Now create a `FieldConstructor` somewhere, using:
-->
> **ノート:** これは単なるサンプルです。実際にはどんなに複雑なフィールドコンストラクターでも自由に作成することができます。`@elements.field`から元のフィールドにアクセスすることさえできます。

テンプレートを作成したら、以下のように`FieldConstructor`を作成しましょう。

```
@implicitField = @{ FieldConstructor(myFieldConstructorTemplate.f) }

@inputText(myForm("username"))
```

<!--
## Handling repeated values
-->
## 値の繰り返しを扱う

<!--
The last helper makes it easier to generate inputs for repeated values. Suppose you have this kind of form definition:
-->
最後のヘルパーは、繰り返された値ごとにinput要素を生成するというタスクを簡単にするものです。例えば、以下の様なフォーム定義があるとします。

```
val myForm = Form(
  tuple(
    "name" -> text,
    "emails" -> list(email)
  )
)
```

<!--
Now you have to generate as many inputs for the `emails` field as the form contains. Just use the `repeat` helper for that:
-->
フォームに含まれている`emails`フィールドのために多くの`input`要素を作成しなければならないとします。このような場合は、`repeat`ヘルパーが使えます。

```
@inputText(myForm("name"))

@repeat(myForm("emails"), min = 1) { emailField =>
    
    @inputText(emailField)
    
}
```

<!--
Use the `min` parameter to display a minimum number of fields, even if the corresponding form data are empty.
-->
`min`パラメータにはフィールドの最小個数を指定します。仮に対応するフォームデータが空の場合でも、この個数分はフィールドが表示されます。

<!--
> **Next:** [[Working with JSON| JavaJsonRequests]]
-->
> **次ページ:** [[JSONの利用|JavaJsonRequests]]
