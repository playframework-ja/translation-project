<!-- translated -->
<!--
# Handling form submission
-->
# フォーム送信を扱う

<!--
## Defining a form
-->
## フォームの定義

<!--
The `play.api.data` package contains several helpers to handle HTTP form data submission and validation. The easiest way to handle a form submission is to define a `play.api.data.Form` structure:
-->
`play.api.data` パッケージには HTTP フォームデータ送信とバリデーションに関するヘルパ関数がいくつか用意されています。フォーム送信を処理する最も簡単な方法は、`play.api.data.Form` を利用することです。

```scala
import play.api.data._
import play.api.data.Forms._

val loginForm = Form(
  tuple(
    "email" -> text,
    "password" -> text
  )
)
```

<!--
This form can generate a `(String, String)` result value from `Map[String,String]` data:
-->
このフォームは `Map[String,String]` 型のデータから `(String, String)` の値を生成することができます。

```scala
val anyData = Map("email" -> "bob@gmail.com", "password" -> "secret")
val (user, password) = loginForm.bind(anyData).get
```

<!--
If you have a request available in the scope, you can bind directly to it from the request content:
-->
このとき、スコープにリクエストが存在すれば、フォームをリクエストのボディから直接バインドすることができます。

```scala
val (user, password) = loginForm.bindFromRequest.get
```

<!--
## Constructing complex objects
-->
## 複雑なオブジェクトの構築

<!--
A form can use functions to construct and deconstruct the value. So you can, for example, define a form that wraps an existing case class:
-->
フォームはある値を構築したり、分解する関数を引数に取ることができます。そのため、例えば次のように case class をラップするフォームを定義することができます。

```scala
import play.api.data._
import play.api.data.Forms._

case class User(name: String, age: Int)

val userForm = Form(
  mapping(
    "name" -> text,
    "age" -> number
  )(User.apply)(User.unapply)
)

val anyData = Map("name" -> "bob", "age" -> "18")
val user: User = userForm.bind(anyData).get
```

<!--
> **Note:** The difference between using `tuple` and `mapping` is that when you are using `tuple` the construction and deconstruction functions don’t need to be specified (we know how to construct and deconstruct a tuple, right?). 
>
> The `mapping` method just let you define your custom functions. When you want to construct and deconstruct a case class, you can just use its default `apply` and `unapply` functions, as they do exactly that!
-->
> **Note:** `tuple` と `mapping` の違いは、`tuple` については構築および分解に使われる関数を指定する必要がないということです (タプルを構築、または分解する方法は明らかですよね) 。
>
> `mapping` メソッドには好きな関数を渡すことができます。ケースクラスを構築または分解する場合、デフォルトの `apply` と `unapply` 関数がまさしくケースクラスの構築・分解を行う関数なので、それらを渡しておけば問題ありません。

<!--
Of course often the `Form` signature doesn’t match the case class exactly. Let’s use the example a form that contains an additional checkbox field, used to accept terms of service. We don’t need to add this data to our `User` value. It’s just a dummy field that is used for form validation but which doesn’t carry any useful information once validated.
-->
`Form` のシグネチャがケースクラスと一致しないこともあると思います。例えば、利用規約の同意を尋ねるためのチェックボックスを追加したフォームで考えてみましょう。このチェックボックスの値は `User` に追加したくありません。なぜかというと、このダミーのフィールドはフォームをバリデーションするために利用しますが、バリデーションが終わった後は用済みだからです。

<!--
As we can define our own construction and deconstruction functions, it is easy to handle it:
-->
構築・分解用の関数を自前で定義して利用すると、このようなフォームも簡単に実現できます。

```scala
val userForm = Form(
  mapping(
    "name" -> text,
    "age" -> number,
    "accept" -> checked("Please accept the terms and conditions")
  )((name, age, _) => User(name, age))
   ((user: User) => Some((user.name, user.age, false))
)
```

<!--
> **Note:** The deconstruction function is used when we fill a form with an existing `User` value. This is useful if we want the load a user from the database and prepare a form to update it.
-->
> **Note:** 分解用の関数は `User` の値をフォームに設定するときに利用されます。これは、例えばユーザ情報をデータベースから読み込んで、更新のために予めフォームに埋め込んでおくような場合に便利です。

<!--
## Defining constraints
-->
## 制約の定義

<!--
For each mapping, you can also define additional validation constraints that will be checked during the binding phase:
-->
各々のマッピングについて、フォームのバインド時に実行されるバリデーションを追加することができます。

```scala
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

case class User(name: String, age: Int)

val userForm = Form(
  mapping(
    "name" -> text.verifying(required),
    "age" -> number.verifying(min(0), max(100))
  )(User.apply)(User.unapply)
)
```

<!--
> **Note:** That can be also written:
-->
> **Note:** 上記のコードは次のように書くこともできます。
>
> ```scala
> mapping(
>   "name" -> nonEmptyText,
>   "age" -> number(min=0, max=100)
> )
> ```
>
<!--
> This constructs the same mappings, with additional constraints.
-->
> このコードは、先程の例と同様に、バリデーションが追加された全く同じマッピングを表しています。

<!--
You can also define ad-hoc constraints on the fields:
-->
フィールドにアドホックなバリデーションを追加することもできます。

```scala
val loginForm = Form(
  tuple(
    "email" -> nonEmptyText,
    "password" -> text
  ) verifying("Invalid user name or password", fields => fields match { 
      case (e, p) => User.authenticate(e,p).isDefined 
  })
)
```

<!--
## Handling binding failure
-->
## バインドエラーの処理

<!--
If you can define constraints, then you need to be able to handle the binding errors. You can use the `fold` operation for this:
-->
バリデーションを定義するということは、一方でバインドエラーを処理しなければならないということです。そのためには、`fold` 操作を利用することができます。

```scala
loginForm.bindFromRequest.fold(
  formWithErrors => // binding failure, you retrieve the form containing errors,
  value => // binding success, you get the actual value 
)
```

<!--
## Fill a form with initial default values
-->
## フォームに初期値を設定する

<!--
Sometimes you’ll want to populate a form with existing values, typically for editing data:
-->
よくあるケースとして、データ編集用のフォームを実現したいような場合に、フォームに予め値を設定しておくことができます。

```scala
val filledForm = userForm.fill(User("Bob", 18))
```

<!--
## Nested values
-->
## ネストした値

<!--
A form mapping can define nested values:
-->
フォームはネストした値を扱うこともできます。

```scala
case class User(name: String, address: Address)
case class Address(street: String, city: String)

val userForm = Form(
  mapping(
    "name" -> text,
    "address" -> mapping(
        "street" -> text,
        "city" -> text
    )(Address.apply)(Address.unapply)
  )(User.apply, User.unapply)
)
```

<!--
When you are using nested data this way, the form values sent by the browser must be named like `address.street`, `address.city`, etc.
-->
このような方法でネストしたデータを扱う場合、ブラウザから送信されたフォーム値の名前は `address.street` や `address.city` のような形式になっている必要があります。

<!--
## Repeated values
-->
## 値の繰り返し

<!--
A form mapping can also define repeated values:
-->
フォームは値の繰り返しを扱うこともできます。

```scala
case class User(name: String, emails: List[String])

val userForm = Form(
  mapping(
    "name" -> text,
    "emails" -> list(text)
  )(User.apply, User.unapply)
)
```

<!--
When you are using repeated data like this, the form values sent by the browser must be named `emails[0]`, `emails[1]`, `emails[2]`, etc.
-->
このようなデータの繰り返しを処理する場合には、ブラウザから送信されるフォーム値の名前は `emails[0]`, `emails[1]`, `examils[2]` のような形式になっている必要があります。

<!--
## Optional values
-->
## 省略可能な値

<!--
A form mapping can also define optional values:
-->
フォームは省略可能な値を扱うこともできます。

```scala
case class User(name: String, email: Option[String])

val userForm = Form(
  mapping(
    "name" -> text,
    "email" -> optional(text)
  )(User.apply, User.unapply)
)
```

<!--
> **Note:** The email field will be ignored and set to `None` if the field `email` is missing in the request payload or if it contains a blank value.
-->
> **Note:** リクエストパラメータに `email` フィールドが含まれていないか、値が空であるような場合には、`email` フィールドは無視されて、デフォルトとして `None` がセットされます。

<!--
## Ignored values
-->
## 無視された値

<!--
If you want a form to have a static value for a field:
-->
フィールド用の静的な値を持つフォームが必要な場合は:

```scala
case class User(id: Long, name: String, email: Option[String])

val userForm = Form(
  mapping(
    "id" -> ignored(1234),
    "name" -> text,
    "email" -> optional(text)
  )(User.apply, User.unapply)
)
```

<!--
Now you can mix optional, nested and repeated mappings any way you want to create complex forms.
-->
これまで説明した省略可能な値、値のネストや繰り返しに関するマッピングを組み合わせて、より複雑なフォームを定義することができます。

<!--
> **Next:** [[Using the form template helpers | ScalaFormHelpers]]
-->
> **次ページ:** [[フォームテンプレートヘルパーの利用 | ScalaFormHelpers]]




