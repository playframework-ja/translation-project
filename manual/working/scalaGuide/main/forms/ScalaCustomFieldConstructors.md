<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Custom Field Constructors
-->
# カスタムフィールドコンストラクタ

<!--
A field rendering is not only composed of the `<input>` tag, but it also needs a `<label>` and possibly other tags used by your CSS framework to decorate the field.
-->
フィールドレンダリングは `<input>` タグで構成するだけでなく、フィールドを修飾するためにCSSフレームワークで使用される `<label>` や他のタグも必要です。

<!--
All input helpers take an implicit [`FieldConstructor`](api/scala/views/html/helper/FieldConstructor.html) that handles this part. The [default one](api/scala/views/html/helper/defaultFieldConstructor$.html) (used if there are no other field constructors available in the scope), generates HTML like:
-->
すべての入力ヘルパーは、この役割を担う暗黙の [`FieldConstructor`](api/scala/views/html/helper/FieldConstructor.html) を使用します。[デフォルトフィールドコンストラクタ](api/scala/views/html/helper/defaultFieldConstructor$.html) (スコープ内で利用可能な他のフィールドコンストラクタがない場合に使用) は、次のような HTML を生成します。

```html
<dl class="error" id="username_field">
    <dt><label for="username">Username:</label></dt>
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
このデフォルトのフィールドコンストラクタは、入力ヘルパーの引数に渡すことができる追加オプションをサポートしています。

<!--
```
'_label -> "Custom label"
'_id -> "idForTheTopDlElement"
'_help -> "Custom help"
'_showConstraints -> false
'_error -> "Force an error"
'_showErrors -> false
```
-->
```
'_label -> "カスタムのラベル"
'_id -> "最上位のdl要素のid"
'_help -> "カスタムのヘルプ"
'_showConstraints -> false
'_error -> "強制エラー"
'_showErrors -> false
```

<!--
## Writing your own field constructor
-->
## 独自のフィールドコンストラクタの記述

<!--
Often you will need to write your own field constructor. Start by writing a template like:
-->
多くの場合、独自のフィールドコンストラクタを記述する必要があります。 まず次のようなテンプレートを作成します。

@[form-myfield](code/scalaguide/forms/scalafieldconstructor/myFieldConstructorTemplate.scala.html)

<!--
> **Note:** This is just a sample. You can make it as complicated as you need. You also have access to the original field using `@elements.field`.
-->
> **メモ:** これはほんの一例です。必要に応じて複雑にすることができます。`@elements.field` を使って元のフィールドにアクセスすることもできます。

<!--
Now create a [`FieldConstructor`](api/scala/views/html/helper/FieldConstructor.html) using this template function:
-->
このテンプレート関数を使用して [`FieldConstructor`](api/scala/views/html/helper/FieldConstructor.html) を作成します。

@[form-myfield-helper](code/ScalaFieldConstructor.scala)

<!--
And to make the form helpers use it, just import it in your templates:
-->
これをフォームヘルパーで使えるようにするには、テンプレートでインポートします。

@[import-myhelper](code/scalaguide/forms/scalafieldconstructor/userImport.scala.html)

@[form](code/scalaguide/forms/scalafieldconstructor/userImport.scala.html)

<!--
It will then use your field constructor to render the input text.
-->
次に、フィールドコンストラクターを使用して入力テキストをレンダリングします。

<!--
You can also set an implicit value for your [`FieldConstructor`](api/scala/views/html/helper/FieldConstructor.html) inline:
-->
[`FieldConstructor`](api/scala/views/html/helper/FieldConstructor.html) 用に暗黙の値をインラインで設定することもできます。

@[declare-implicit](code/scalaguide/forms/scalafieldconstructor/userDeclare.scala.html)

@[form](code/scalaguide/forms/scalafieldconstructor/userDeclare.scala.html)
