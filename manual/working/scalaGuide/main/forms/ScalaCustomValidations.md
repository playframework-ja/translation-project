<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Using Custom Validations
-->
# カスタムバリデーションの使用

<!--
The [validation package](api/scala/play/api/data/validation/package.html) allows you to create ad-hoc constraints using the `verifying` method.  However, Play gives you the option of creating your own custom constraints, using the [`Constraint`](api/scala/play/api/data/validation/Constraint.html) case class.
-->
[バリデーションパッケージ](api/scala/play/api/data/validation/package.html) では、`verifying` メソッドを使ってアドホック制約を作成することができます。しかし Play では、[`Constraint`](api/scala/play/api/data/validation/Constraint.html) ケースクラスを使用して独自のカスタム制約を作成する、という選択肢が提供されています。

<!--
Here, we'll implement a simple password strength constraint that uses regular expressions to check the password is not all letters or all numbers.  A [`Constraint`](api/scala/play/api/data/validation/Constraint.html) takes a function which returns a [`ValidationResult`](api/scala/play/api/data/validation/ValidationResult.html), and we use that function to return the results of the password check:
-->
ここでは、正規表現を使用して、パスワードが「すべて文字」ではない、または「すべて数字」ではないことを確認する簡単なパスワード強度の制約を実装してみます。[`Constraint`](api/scala/play/api/data/validation/Constraint.html) は [`ValidationResult`](api/scala/play/api/data/validation/ValidationResult.html) を返す関数を取り、その関数を使ってパスワードチェックの結果を返します。

@[passwordcheck-constraint](code/CustomValidations.scala)

<!--
> **Note:** This is an intentionally trivial example.  Please consider using the [OWASP guide](https://www.owasp.org/index.php/Authentication_Cheat_Sheet#Implement_Proper_Password_Strength_Controls) for proper password security.
-->
> **メモ:** ここではあえて簡単な例にしています。適切なパスワードのセキュリティについては、[OWASP ガイド](https://www.owasp.org/index.php/Authentication_Cheat_Sheet#Implement_Proper_Password_Strength_Controls) の使用を検討してください。

<!--
We can then use this constraint together with [`Constraints.min`](api/scala/play/api/data/validation/Constraints.html) to add additional checks on the password.
-->
この制約を [`Constraints.min`](api/scala/play/api/data/validation/Constraints.html) と共に使用して、パスワードのチェックを追加できます。

@[passwordcheck-mapping](code/CustomValidations.scala)
