<!-- translated -->
<!--
# Handling form submission
-->
# フォームの送信

<!--
## Defining a form
-->
## フォームの定義

<!--
The `play.data` package contains several helpers to handle HTTP form data submission and validation. The easiest way to handle a form submission is to define a `play.data.Form` that wraps an existing class:
-->
`play.data` パッケージには HTTP フォームデータの送信とバリデーションを行うヘルパがいくつか含まれています。フォーム送信を処理する最も簡単な方法は、既存のクラスをラップする `play.data.Form` オブジェクトを定義することです。

@[user](code/javaguide/forms/u1/User.java)

@[create](code/javaguide/forms/JavaForms.java)

> **Note:** The underlying binding is done using [Spring data binder](http://static.springsource.org/spring/docs/3.0.x/reference/validation.html).

<!--
This form can generate a `User` result value from `HashMap<String,String>` data:
-->
このフォームは `HashMap<String,String>` 型のデータから `User` の値を生成することができます。

@[bind](code/javaguide/forms/JavaForms.java)

<!--
If you have a request available in the scope, you can bind directly from the request content:
-->
スコープ内にリクエストが存在する場合は、リクエストの内容から直接バインドすることができます。

@[bind-from-request](code/javaguide/forms/JavaForms.java)

<!--
## Defining constraints
-->
## 制約の定義

<!--
You can define additional constraints that will be checked during the binding phase using JSR-303 (Bean Validation) annotations:
-->
JSR-303 (Bean バリデーション) アノテーションを使って、バインディング時にチェックされる制約を追加することができます。

@[user](code/javaguide/forms/u2/User.java)

<!--
> **Tip:** The `play.data.validation.Constraints` class contains several built-in validation annotations.
-->
> **Tip:** `play.data.validation.Constraints` クラスには組み込みのバリデーションアノテーションがいくつか含まれています。

<!--
You can also define an ad-hoc validation by adding a `validate` method to your top object:
-->
トップオブジェクトに `validate` メソッドを追加することで、アドホックなバリデーションを定義することもできます。

@[user](code/javaguide/forms/u3/User.java)

The message returned in the above example will become a global error.

The `validate`-method can return the following types: `String`, `List<ValidationError>` or `Map<String,List<ValidationError>>`

`validate` method is called after checking annotation-based constraints and only if they pass.  If validation passes you must return `null` . Returning any not-`null` value (empty string or empty list) is treated as failed validation.

`List<ValidationError>` may be useful when you have additional validations for fields. For example:

@[list-validate](code/javaguide/forms/JavaForms.java)

Using `Map<String,List<ValidationError>>` is similar to `List<ValidationError>` where map's keys are error codes similar to `email` in the example above.

<!--
## Handling binding failure
-->
## バインドエラーの処理

<!--
Of course if you can define constraints, then you need to be able to handle the binding errors.
-->
バリデーションを定義するということは、一方でバインドエラーを処理しなければならないということです。

@[handle-errors](code/javaguide/forms/JavaForms.java)

Typically, as shown above, the form simply gets passed to a template.  Global errors can be rendered in the following way:

@[global-errors](code/javaguide/forms/view.scala.html)

Errors for a particular field can be rendered in the following manner:

@[field-errors](code/javaguide/forms/view.scala.html)


<!--
## Filling a form with initial default values
-->
## フォームに初期値を設定する

<!--
Sometimes you’ll want to fill a form with existing values, typically for editing:
-->
よくあるケースとして、編集などのためにフォームに予め値を設定したい場合は、以下のようにします。

@[fill](code/javaguide/forms/JavaForms.java)

<!--
> **Tip:** `Form` objects are immutable - calls to methods like `bind()` and `fill()` will return a new object filled with the new data.
-->
> **Tip:** `Form` オブジェクトはイミュータブルです。つまり、`bind()` や `fill()` などのメソッドを呼び出すと、新しいデータで埋められた新しいオブジェクトが返ります。

<!--
## Handling a form that is not related to a Model
-->
## モデルと関連しないフォームの処理

<!--
You can use a `DynamicForm` if you need to retrieve data from an html form that is not related to a `Model` :
-->
`Model` と関連しない html フォームからデータを取得する必要がある場合、`DynamicForm` を使うことができます:

@[dynamic](code/javaguide/forms/JavaForms.java)

<!--
## Register a custom DataBinder
-->
## 独自の DataBinder を登録する

<!--
In case you want to define a mapping from a custom object to a form field string and vice versa you need to register a new Formatter for this object.
For an object like JodaTime's `LocalTime` it could look like this:
-->
独自オブジェクトからフォームのフィールド、またはその逆方向のマッピングを定義したいときは、そのオブジェクトのために新たなフォーマッタを登録する必要があります。例えば、JodaTime の `LocalTime` オブジェクトのためのフォーマッタを登録する場合は、次のようなコードになります。

@[register-formatter](code/javaguide/forms/JavaForms.java)

When the binding fail an array of errors keys is created, the first one defined in the messages file will be used. This array will generally contain :

    ["error.invalid.<fieldName>", "error.invalid.<type>", "error.invalid"]

The errors keys are created by [Spring DefaultMessageCodesResolver](http://static.springsource.org/spring/docs/3.0.7.RELEASE/javadoc-api/org/springframework/validation/DefaultMessageCodesResolver.html), the root "typeMismatch" is replaced by "error.invalid".

<!--
> **Next:** [[Using the form template helpers | JavaFormHelpers]]
-->
> **次ページ:** [[フォームテンプレートヘルパーを利用する | JavaFormHelpers]]