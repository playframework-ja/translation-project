<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# JSON Reads/Writes/Format Combinators
-->
# JSON Reads/Writes/Format コンビネータ

<!--
[[JSON basics|ScalaJson]] introduced [`Reads`](api/scala/play/api/libs/json/Reads.html) and [`Writes`](api/scala/play/api/libs/json/Writes.html) converters which are used to convert between [`JsValue`](api/scala/play/api/libs/json/JsValue.html) structures and other data types. This page covers in greater detail how to build these converters and how to use validation during conversion.
-->
[[JSON の基本|ScalaJson]] では、[`JsValue`](api/scala/play/api/libs/json/JsValue.html) 構造体と他のデータ型との変換に使用されるコンバータ [`Reads`](api/scala/play/api/libs/json/Reads.html) と [`Writes`](api/scala/play/api/libs/json/Writes.html) を紹介しました。このページでは、これらのコンバータを構築する方法と、変換中にバリデーションを使用する方法について詳しく説明します。

<!--
The examples on this page will use this `JsValue` structure and corresponding model:
-->
このページの例では、次のような `JsValue` 構造体と、対応するモデルを使用します。

@[sample-json](code/ScalaJsonCombinatorsSpec.scala)

@[sample-model](code/ScalaJsonCombinatorsSpec.scala)

<!--
## JsPath
-->
## JsPath

<!--
[`JsPath`](api/scala/play/api/libs/json/JsPath.html) is a core building block for creating `Reads`/`Writes`. `JsPath` represents the location of data in a `JsValue` structure. You can use the `JsPath` object (root path) to define a `JsPath` child instance by using syntax similar to traversing `JsValue`:
-->
[`JsPath`](api/scala/play/api/libs/json/JsPath.html) は、`Reads`/`Writes` を作るための中核となる構成要素です。`JsPath` は `JsValue` 構造体におけるデータの位置を表します。`JsValue` を走査するのと同様の構文を使って、`JsPath` オブジェクト (ルートパス) を使用して `JsPath` の子インスタンスを定義することができます。

@[jspath-define](code/ScalaJsonCombinatorsSpec.scala)

<!--
The [`play.api.libs.json`](api/scala/play/api/libs/json/package.html) package defines an alias for `JsPath`: `__` (double underscore). You can use this if you prefer:
-->
[`play.api.libs.json`](api/scala/play/api/libs/json/package.html) パッケージは、`JsPath` の別名として `__` (ダブルアンダースコア) を定義します。必要に応じてこれを使用することができます。

@[jspath-define-alias](code/ScalaJsonCombinatorsSpec.scala)

<!--
## Reads
-->
## Reads

<!--
[`Reads`](api/scala/play/api/libs/json/Reads.html) converters are used to convert from a `JsValue` to another type. You can combine and nest `Reads` to create more complex `Reads`.
-->
[`Reads`](api/scala/play/api/libs/json/Reads.html) コンバータは `JsValue` から別の型に変換するために使われます。`Reads` を組み合わせたりネストして、より複雑な `Reads` を作成することができます。

<!--
You will require these imports to create `Reads`:
-->
`Reads` を作るためにこれらのインポートが必要になります。

@[reads-imports](code/ScalaJsonCombinatorsSpec.scala)

<!--
### Path Reads
-->
### パス Reads

<!--
`JsPath` has methods to create special `Reads` that apply another `Reads` to a `JsValue` at a specified path:
-->
`JsPath` には、指定されたパスの `JsValue` に、別の `Reads` を適用する特殊な `Reads` メソッドを作成するメソッドがあります。

<!--
- `JsPath.read[T](implicit r: Reads[T]): Reads[T]` - Creates a `Reads[T]` that will apply the implicit argument `r` to the `JsValue` at this path.
- `JsPath.readNullable[T](implicit r: Reads[T]): Reads[Option[T]]readNullable` - Use for paths that may be missing or can contain a null value.
-->
- `JsPath.read[T](implicit r: Reads[T]): Reads[T]` - このパスの `JsValue` に暗黙の引数 `r` を適用する `Reads[T]` を作成します。
- `JsPath.readNullable[T](implicit r: Reads[T]): Reads[Option[T]]readNullable` - 存在しないか null 値を含む可能性のあるパスに使用します。

<!--
> Note: The JSON library provides implicit `Reads` for basic types such as `String`, `Int`, `Double`, etc.
-->
> メモ: JSON ライブラリは、`String`、`Int`、`Double` などの基本型に対する暗黙の `Reads` を提供します。

<!--
Defining an individual path `Reads` looks like this:
-->
単一のパスを定義する `Reads` は次のようになります。

@[reads-simple](code/ScalaJsonCombinatorsSpec.scala)

<!--
### Complex Reads
-->
### 複雑な Reads

<!--
You can combine individual path `Reads` to form more complex `Reads` which can be used to convert to complex models.
-->
複雑なモデルに変換するために使用できる、より複雑な `Reads` を形成するために、単一のパス `Reads` を組み合わせることができます。

<!--
For easier understanding, we'll break down the combine functionality into two statements. First combine `Reads` objects using the `and` combinator:
-->
理解をより簡単にするために、組み合わせた機能を 2 つのステートメントに分解します。最初に、`and` コンビネータを使用して `Reads` オブジェクトを結合します。

@[reads-complex-builder](code/ScalaJsonCombinatorsSpec.scala)

<!--
This will yield a type of `FunctionalBuilder[Reads]#CanBuild2[Double, Double]`. This is an intermediary object and you don't need to worry too much about it, just know that it's used to create a complex `Reads`. 
-->
これは `FunctionalBuilder[Reads]#CanBuild2[Double, Double]` の型を生成します。これは中間のオブジェクトであり、複雑な `Reads` を作成するために使用されていることだけを知っていれば、あまり心配する必要はありません。

<!--
Second call the `apply` method of `CanBuildX` with a function to translate individual values to your model, this will return your complex `Reads`. If you have a case class with a matching constructor signature, you can just use its `apply` method:
-->
個々の値をモデルに変換する関数を持つ `CanBuildX` の 2 番目の `apply` メソッドを呼び出すと、複雑な `Reads` が返されます。コンストラクタのシグネチャが一致するケースクラスがある場合は、単にその `apply` メソッドを使うことができます。

@[reads-complex-buildertoreads](code/ScalaJsonCombinatorsSpec.scala)

<!--
Here's the same code in a single statement:
-->
同じコードを単一のステートメントで書くと以下のようになります。

@[reads-complex-statement](code/ScalaJsonCombinatorsSpec.scala)

<!--
### Validation with Reads
-->
### Reads のバリデーション

<!--
The `JsValue.validate` method was introduced in [[JSON basics|ScalaJson]] as the preferred way to perform validation and conversion from a `JsValue` to another type. Here's the basic pattern:
-->
`JsValue.validate` メソッドは、`JsValue` から別の型へのバリデーションと変換を行うための良い方法として [[JSON の基本|ScalaJson]] で紹介しました。基本的なパターンは次のとおりです。

@[reads-validation-simple](code/ScalaJsonCombinatorsSpec.scala)

<!--
Default validation for `Reads` is minimal, such as checking for type conversion errors. You can define custom validation rules by using `Reads` validation helpers. Here are some that are commonly used: 
-->
`Reads` のデフォルトのバリデーションは、型変換エラーをチェックするなどの最小限のものです。`Reads` バリデーションヘルパーを使って独自のバリデーションルールを定義することができます。よく使われるものを次に示します。

<!--
- `Reads.email` - Validates a String has email format.  
- `Reads.minLength(nb)` - Validates the minimum length of a String.
- `Reads.min` - Validates a minimum numeric value.
- `Reads.max` - Validates a maximum numeric value.
- `Reads[A] keepAnd Reads[B] => Reads[A]` - Operator that tries `Reads[A]` and `Reads[B]` but only keeps the result of `Reads[A]` (For those who know Scala parser combinators `keepAnd == <~` ).
- `Reads[A] andKeep Reads[B] => Reads[B]` - Operator that tries `Reads[A]` and `Reads[B]` but only keeps the result of `Reads[B]` (For those who know Scala parser combinators `andKeep == ~>` ).
- `Reads[A] or Reads[B] => Reads` - Operator that performs a logical OR and keeps the result of the last `Reads` checked.
-->
- `Reads.email` - E メール形式の文字列を検証します。
- `Reads.minLength(nb)` - 文字列の最小長を検証します。
- `Reads.min` - 数値の最小を検証します。
- `Reads.max` - 数値の最大を検証します。
- `Reads[A] keepAnd Reads[B] => Reads[A]` - `Reads[A]` と `Reads[B]` を試みるが、`Reads[A]` の結果だけを保持する演算子 (Scala パーサーコンビネータ `keepAnd == <~` を知っている人向け)。
- `Reads[A] andKeep Reads[B] => Reads[B]` - `Reads[A]` と `Reads[B]` を試みるが、`Reads[B]` の結果だけを保持する演算子 (Scala パーサーコンビネータ `andKeep == ~>` を知っている人向け)。
- `Reads[A] or Reads[B] => Reads` - 論理 OR を実行し、最後の `Reads` の結果をチェックし続ける演算子。

<!--
To add validation, apply helpers as arguments to the `JsPath.read` method:
-->
バリデーションを追加するには、`JsPath.read` メソッドの引数としてヘルパーを適用します。

@[reads-validation-custom](code/ScalaJsonCombinatorsSpec.scala)

<!--
### Putting it all together
-->
### すべてをひとまとめにする

<!--
By using complex `Reads` and custom validation we can define a set of effective `Reads` for our example model and apply them:
-->
複雑な `Reads` と独自のバリデーションを使うことで、サンプルモデルに対して有効な `Reads` のセットを定義して適用することができます。

@[reads-model](code/ScalaJsonCombinatorsSpec.scala)

<!--
Note that complex `Reads` can be nested. In this case, `placeReads` uses the previously defined implicit `locationReads` and `residentReads` at specific paths in the structure.
-->
複雑な `Reads` はネストすることができます。この場合、`placeReads` は、あらかじめ定義された暗黙的な `locationReads` と `residentReads` を構造体内の特定のパスで使います。

<!--
## Writes
-->
## Writes

<!--
[`Writes`](api/scala/play/api/libs/json/Writes.html) converters are used to convert from some type to a `JsValue`.
-->
[`Writes`](api/scala/play/api/libs/json/Writes.html) コンバータは、ある型を `JsValue` に変換するために使われます。

<!--
You can build complex `Writes` using `JsPath` and combinators very similar to `Reads`. Here's the `Writes` for our example model:
-->
`JsPath` と、`Reads` に非常によく似たコンビネータを使って複雑な `Writes` を構築することができます。

@[writes-model](code/ScalaJsonCombinatorsSpec.scala)

<!--
There are a few differences between complex `Writes` and `Reads`:
-->
複雑な `Writes` と `Reads` にはいくつかの違いがあります。

<!--
- The individual path `Writes` are created using the `JsPath.write` method.
- There is no validation on conversion to `JsValue` which makes the structure simpler and you won't need any validation helpers.
- The intermediary `FunctionalBuilder#CanBuildX` (created by `and` combinators) takes a function that translates a complex type `T` to a tuple matching the individual path `Writes`. Although this is symmetrical to the `Reads` case, the `unapply` method of a case class returns an `Option` of a tuple of properties and must be used with `unlift` to extract the tuple.
-->
- 個々のパス `Writes` は、`JsPath.write` メソッドを使用して作成されます。
- `JsValue` への変換においてバリデーションは存在しないため、構造が簡単になり、バリデーションヘルパーは必要ありません。
- 中間の `FunctionalBuilder＃CanBuildX` (`and` コンビネータによって生成される) は、複合型 `T` を個々のパス `Writes` に一致するタプルに変換する関数を取ります。これは `Reads` の場合と対称的ですが、ケースクラスの `unapply` メソッドは、プロパティのタプルの `Option` を返し、タプルを抽出するには `unlift` と一緒に使わなければなりません。

<!--
## Recursive Types
-->
## 再帰型

<!--
One special case that our example model doesn't demonstrate is how to handle `Reads` and `Writes` for recursive types. `JsPath` provides `lazyRead` and `lazyWrite` methods that take call-by-name parameters to handle this:
-->
例題モデルが示していない特殊なケースの 1 つは、再帰型に対して `Reads` と `Writes` をどのように扱うかです。`JsPath` は、これを処理するための名前による引数を取る `lazyRead` メソッドと `lazyWrite` メソッドを提供します。

@[reads-writes-recursive](code/ScalaJsonCombinatorsSpec.scala)

<!--
## Format
-->
## Format

<!--
[`Format[T]`](api/scala/play/api/libs/json/Format.html) is just a mix of the `Reads` and `Writes` traits and can be used for implicit conversion in place of its components.
-->
[`Format[T]`](api/scala/play/api/libs/json/Format.html) は `Reads` と `Writes` の特徴をミックスさせただけのものであり、これらのコンポーネントの代わりに暗黙の変換用に使うことができます。

<!--
### Creating Format from Reads and Writes
-->
### Reads と Writes からの Format の作成

<!--
You can define a `Format` by constructing it from `Reads` and `Writes` of the same type:
-->
`Format` は、同じ型の `Reads` と `Writes` から構築することで定義できます。

@[format-components](code/ScalaJsonCombinatorsSpec.scala)

<!--
### Creating Format using combinators
-->
### コンビネータを使用した Format の作成

<!--
In the case where your `Reads` and `Writes` are symmetrical (which may not be the case in real applications), you can define a `Format` directly from combinators:
-->
`Reads` と `Writes` が対称の場合 (実際のアプリケーションではそうでないかもしれません)、コンビネータから直接 `Format` を定義することができます。

@[format-combinators](code/ScalaJsonCombinatorsSpec.scala)
