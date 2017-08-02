<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# JSON basics
-->
# JSON の基本

<!--
Modern web applications often need to parse and generate data in the JSON (JavaScript Object Notation) format. Play supports this via its [JSON library](api/scala/play/api/libs/json/package.html).
-->
最新の Web アプリケーションでは、JSON (JavaScript Object Notation) 形式のデータを解析・生成する必要があります。Play はこれを [JSON ライブラリ](api/scala/play/api/libs/json/package.html) でサポートしています。

<!--
JSON is a lightweight data-interchange format and looks like this:
-->
JSON は軽量のデータ交換フォーマットで、次のようなものです。

```json
{
  "name" : "Watership Down",
  "location" : {
    "lat" : 51.235685,
    "long" : -1.309197
  },
  "residents" : [ {
    "name" : "Fiver",
    "age" : 4,
    "role" : null
  }, {
    "name" : "Bigwig",
    "age" : 6,
    "role" : "Owsla"
  } ]
}
```

<!--
> To learn more about JSON, see [json.org](http://json.org/).
-->
> JSON の詳細については、[json.org](http://www.json.org/json-ja.html) を参照してください。

<!--
## The Play JSON library
-->
## Play JSON ライブラリ

<!--
The [`play.api.libs.json`](api/scala/play/api/libs/json/package.html) package contains data structures for representing JSON data and utilities for converting between these data structures and other data representations. Types of interest are:
-->
[`play.api.libs.json`](api/scala/play/api/libs/json/package.html) パッケージには、JSON データを表現するためのデータ構造と、これらのデータ構造と他のデータ表現との間で変換を行うためのユーティリティが含まれています。重要な型は次のとおりです。

### [`JsValue`](api/scala/play/api/libs/json/JsValue.html)

<!--
This is a trait representing any JSON value. The JSON library has a case class extending `JsValue` to represent each valid JSON type:
-->
これは JSON の値を表すトレイトです。JSON ライブラリには以下のような、`JsValue` を拡張した有効な JSON 型を表すケースクラスがあります。

- [`JsString`](api/scala/play/api/libs/json/JsString.html)
- [`JsNumber`](api/scala/play/api/libs/json/JsNumber.html)
- [`JsBoolean`](api/scala/play/api/libs/json/JsBoolean.html)
- [`JsObject`](api/scala/play/api/libs/json/JsObject.html)
- [`JsArray`](api/scala/play/api/libs/json/JsArray.html)
- [`JsNull`](api/scala/play/api/libs/json/JsNull$.html)

<!--
Using the various `JsValue` types, you can construct a representation of any JSON structure.
-->
さまざまな `JsValue` 型を使用して、任意の JSON 構造の表現を構築することができます。

### [`Json`](api/scala/play/api/libs/json/Json$.html)

<!--
The `Json` object provides utilities, primarily for conversion to and from `JsValue` structures.
-->
`Json` オブジェクトは、主に `JsValue` 構造との間の変換のためのユーティリティを提供します。

### [`JsPath`](api/scala/play/api/libs/json/JsPath.html)

<!--
Represents a path into a `JsValue` structure, analogous to XPath for XML. This is used for traversing `JsValue` structures and in patterns for implicit converters.
-->
XML に対する XPath のような、`JsValue` 構造へのパスを表します。これは、`JsValue` 構造を走査するために使用され、暗黙のコンバータのためのパターンに含まれます。

<!--
## Converting to a `JsValue`
-->
## `JsValue` への変換

<!--
### Using string parsing
-->
### 文字列解析の使用

@[convert-from-string](code/ScalaJsonSpec.scala)

<!--
### Using class construction
-->
### クラス構築の使用

@[convert-from-classes](code/ScalaJsonSpec.scala)

<!--
`Json.obj` and `Json.arr` can simplify construction a bit. Note that most values don't need to be explicitly wrapped by JsValue classes, the factory methods use implicit conversion (more on this below).
-->
`Json.obj` と `Json.arr` は構築を少しだけ簡単にすることができます。ほとんどの値は JsValue クラスで明示的にラップする必要はないことに注意してください。ファクトリメソッドは暗黙の変換を使用します (詳細は後述) 。

@[convert-from-factory](code/ScalaJsonSpec.scala)

<!--
### Using Writes converters
-->
### 書き込みコンバータの使用

<!--
Scala to `JsValue` conversion is performed by the utility method `Json.toJson[T](T)(implicit writes: Writes[T])`. This functionality depends on a converter of type [`Writes[T]`](api/scala/play/api/libs/json/Writes.html) which can convert a `T` to a `JsValue`. 
-->
Scala から `JsValue` への変換は、ユーティリティメソッド `Json.toJson[T](T)(implicit writes: Writes[T])` によって実行されます。この機能は `T` を `JsValue` に変換する [`Writes[T]`](api/scala/play/api/libs/json/Writes.html) 型のコンバータに依存します。

<!--
The Play JSON API provides implicit `Writes` for most basic types, such as `Int`, `Double`, `String`, and `Boolean`. It also supports `Writes` for collections of any type `T` that a `Writes[T]` exists. 
-->
Play JSON API は、`Int`、`Double`、`String`、`Boolean` などのほとんどの基本型に対して暗黙の `Writes` を提供します。`Writes[T]` が存在する `T` 型のコレクションのための `Writes` もサポートしています。

@[convert-from-simple](code/ScalaJsonSpec.scala)

<!--
To convert your own models to `JsValue`s, you must define implicit `Writes` converters and provide them in scope.
-->
独自のモデルを `JsValue` に変換するには、暗黙の `Writes` コンバータを定義し、それらをスコープで提供する必要があります。

@[sample-model](code/ScalaJsonSpec.scala)

@[convert-from-model](code/ScalaJsonSpec.scala)

<!--
Alternatively, you can define your `Writes` using the combinator pattern:
-->
代わりに、コンビネータパターンを使って `Writes` を定義することもできます。

<!--
> Note: The combinator pattern is covered in detail in [[JSON Reads/Writes/Formats Combinators|ScalaJsonCombinators]].
-->
> メモ: コンビネータパターンについては、[[JSON Reads/Writes/Formats コンビネータ|ScalaJsonCombinators]] で詳しく説明しています。

@[convert-from-model-prefwrites](code/ScalaJsonSpec.scala)

<!--
## Traversing a JsValue structure
-->
## JsValue 構造の走査

<!--
You can traverse a `JsValue` structure and extract specific values. The syntax and functionality is similar to Scala XML processing.
-->
`JsValue` 構造を走査して特定の値を抽出することができます。構文と機能は Scala の XML 処理と似ています。

<!--
> Note: The following examples are applied to the JsValue structure created in previous examples.
-->
> メモ: 次の例は、前の例で作成された JsValue 構造に適用されます。

<!--
### Simple path `\`
-->
### 基本的なパス `\`

<!--
Applying the `\` operator to a `JsValue` will return the property corresponding to the field argument, supposing this is a `JsObject`. 
-->
`\` 演算子を `JsValue` に適用すると、フィールド引数が `JsObject` の場合に、対応するプロパティが返されます。

@[traverse-simple-path](code/ScalaJsonSpec.scala)

<!--
### Recursive path `\\`
-->
### 再帰的パス `\\`

<!--
Applying the `\\` operator will do a lookup for the field in the current object and all descendants.
-->
`\\` 演算子を適用すると、現在のオブジェクトとすべての子孫のフィールドが参照されます。

@[traverse-recursive-path](code/ScalaJsonSpec.scala)

<!--
### Index lookup (for JsArrays)
-->
### インデックス検索 (JsArrays の場合)

<!--
You can retrieve a value in a `JsArray` using an apply operator with the index number.
-->
インデックス番号付きの apply 演算子を使用して、`JsArray` の中の値を検索することができます。

@[traverse-array-index](code/ScalaJsonSpec.scala)

<!--
## Converting from a JsValue
-->
## JsValue からの変換

<!--
### Using String utilities
-->
### 文字列ユーティリティの使用

<!--
Minified:
-->
縮小版:

@[convert-to-string](code/ScalaJsonSpec.scala)

```json
{"name":"Watership Down","location":{"lat":51.235685,"long":-1.309197},"residents":[{"name":"Fiver","age":4,"role":null},{"name":"Bigwig","age":6,"role":"Owsla"}]}
```

<!--
Readable:
-->
可読版:

@[convert-to-string-pretty](code/ScalaJsonSpec.scala)

```json
{
  "name" : "Watership Down",
  "location" : {
    "lat" : 51.235685,
    "long" : -1.309197
  },
  "residents" : [ {
    "name" : "Fiver",
    "age" : 4,
    "role" : null
  }, {
    "name" : "Bigwig",
    "age" : 6,
    "role" : "Owsla"
  } ]
}
```

<!--
### Using JsValue.as/asOpt
-->
### JsValue.as/asOpt の使用

<!--
The simplest way to convert a `JsValue` to another type is using `JsValue.as[T](implicit fjs: Reads[T]): T`. This requires an implicit converter of type [`Reads[T]`](api/scala/play/api/libs/json/Reads.html) to convert a `JsValue` to `T` (the inverse of `Writes[T]`). As with `Writes`, the JSON API provides `Reads` for basic types.
-->
`JsValue` を別の型に変換する最も簡単な方法は、`JsValue.as[T](implicit fjs: Reads[T]): T` を使用することです。これには、`JsValue` を `T` に変換するための型 [`Reads[T]`](api/scala/play/api/libs/json/Reads.html) (`Writes[T]` の逆) の暗黙のコンバータが必要です。`Writes` と同様に、JSON API は基本の型の `Reads` を提供します。

@[convert-to-type-as](code/ScalaJsonSpec.scala)

<!--
The `as` method will throw a `JsResultException` if the path is not found or the conversion is not possible. A safer method is `JsValue.asOpt[T](implicit fjs: Reads[T]): Option[T]`.
-->
パスが見つからないか、変換が不可能な場合、`as` メソッドは `JsResultException` をスローします。より安全なメソッドは、`JsValue.asOpt[T](implicit fjs: Reads[T]): Option[T]` です。

@[convert-to-type-as-opt](code/ScalaJsonSpec.scala)

<!--
Although the `asOpt` method is safer, any error information is lost.
-->
`asOpt` メソッドはより安全ですが、エラー情報は失われます。

<!--
### Using validation
-->
### バリデーションの使用

<!--
The preferred way to convert from a `JsValue` to another type is by using its `validate` method (which takes an argument of type `Reads`). This performs both validation and conversion, returning a type of [`JsResult`](api/scala/play/api/libs/json/JsResult.html). `JsResult` is implemented by two classes:
-->
`JsValue` から別の型に変換するための好ましい方法は、`validate` メソッド (`Reads` 型の引数を取る) を使うことです。これにより、バリデーションと変換の両方が実行され、[`JsResult`](api/scala/play/api/libs/json/JsResult.html) の型が返されます。`JsResult` は2つのクラスによって実装されます。

<!--
- [`JsSuccess`](api/scala/play/api/libs/json/JsSuccess.html): Represents a successful validation/conversion and wraps the result.
- [`JsError`](api/scala/play/api/libs/json/JsError.html): Represents unsuccessful validation/conversion and contains a list of validation errors.
-->
- [`JsSuccess`](api/scala/play/api/libs/json/JsSuccess.html): バリデーション/変換が成功したことを表し、結果をラップします。
- [`JsError`](api/scala/play/api/libs/json/JsError.html): バリデーション/変換が失敗したことを表し、バリデーションエラーのリストを含みます。

<!--
You can apply various patterns for handling a validation result:
-->
バリデーション結果を処理するためのさまざまなパターンを適用できます。

@[convert-to-type-validate](code/ScalaJsonSpec.scala)

<!--
### JsValue to a model
-->
### JsValue からモデルへ

<!--
To convert from JsValue to a model, you must define implicit `Reads[T]` where `T` is the type of your model.
-->
JsValue からモデルに変換するには、暗黙の `Reads[T]` を定義しなければなりません。ここで `T` はモデルの型です。

<!--
> Note: The pattern used to implement `Reads` and custom validation are covered in detail in [[JSON Reads/Writes/Formats Combinators|ScalaJsonCombinators]].
-->
> メモ: `Reads` と独自のバリデーションの実装に使われるパターンは、[[JSON Reads/Writes/Formats コンビネータ|ScalaJsonCombinators]] で詳しく説明しています。

@[sample-model](code/ScalaJsonSpec.scala)

@[convert-to-model](code/ScalaJsonSpec.scala)
