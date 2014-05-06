<!-- translated -->
<!--
# The Play JSON library Basics
-->
# Play JSON ライブラリの基礎

<!--
## Overview
-->
## 概要

<!--
The recommended way of dealing with JSON is using Play’s typeclass based JSON library, located at ```play.api.libs.json```. 
-->
JSON を使いたければ、```play.api.libs.json``` にある JSON ライブラリを基にした Play の型クラスを使うことをおすすめします。

For parsing JSON strings, Play uses the super-fast Java based JSON library, [Jackson](http://jackson.codehaus.org/).

<!--
The benefit of this approach is that both the Java and the Scala side of Play can share the same underlying library (Jackson), while Scala users can enjoy the extra type safety and functional aspects that Play’s JSON support brings to the table.
-->
このアプローチの利点として、Scala ユーザーは Play の JSON サポートがお膳立てする特上の型安全性と関数型の側面を楽しむことができる一方、Java と Scala の両方の Play は同じ基礎となるライブラリ (Jackson) を共有することができます。

<!--
## JSON is an AST (_Abstract Syntax Tree_)
-->
## JSON は AST (_Abstract Syntax Tree: 抽象構文木_)

Take this JSON for example:

```json
{ 
  "user": {
    "name" : "toto",
    "age" : 25,
    "email" : "toto@jmail.com",
    "isAlive" : true,
    "friend" : {
  	  "name" : "tata",
  	  "age" : 20,
  	  "email" : "tata@coldmail.com"
    }
  } 
}
```

This can be seen as a tree structure using the two following structures:

- **JSON object** contains a set of `name` / `value` pairs:
    - `name` is a String
    - `value` can be :
        - string
        - number
        - another JSON object
        - a JSON array
        - true/false
        - null
- **JSON array** is a sequence of values from the previously listed value types.

<!--
> If you want to have more info about the exact JSON standard, please go to [json.org](http://json.org/)
-->
> 正確な JSON 標準について詳しく知りたい場合は、[json.org](http://json.org/) へアクセスしてください


<!--
## Json Data Types
-->
## Json データ型

The [`play.api.libs.json`](api/scala/index.html#play.api.libs.json.package) package contains the seven JSON data types, reflecting this structure.

### [`JsObject`](api/scala/index.html#play.api.libs.json.JsObject)

This is a set of name/value pairs as described in the standard, for example:

```json
{ "name" : "toto", "age" : 45 }
```

### [`JsNull`](api/scala/index.html#play.api.libs.json.JsNull)

This represents a `null` value in JSON.

### [`JsBoolean`](api/scala/index.html#play.api.libs.json.JsBoolean)

This is a boolean with a value of `true` or `false`.

### [`JsNumber`](api/scala/index.html#play.api.libs.json.JsNumber)

JSON does NOT discriminate `short`, `int`, `long`, `float`, `double` and `BigDecimal`, so it is represented by a `JsNumber` containing a `BigDecimal`.  The Play JSON API brings more type precision when converting to Scala structures.

### [`JsArray`](api/scala/index.html#play.api.libs.json.JsArray)

An array is a sequence of any Json value types (not necessarily the same type), for example:

```
[ "alpha", "beta", true, 123.44, 334]
```

### [`JsString`](api/scala/index.html#play.api.libs.json.JsString)

<!--
A classic String.
-->
標準的な文字列です。

## Other data types

### [`JsUndefined`](api/scala/index.html#play.api.libs.json.JsUndefined)

<!--
This is not part of the JSON standard and is only used internally by the API to represent some error nodes in the AST.
-->
これは JSON 標準の一部ではなく、抽象構文木においてエラーとなったノードを表現するために API が内部でのみ使用します。

### [`JsValue`](api/scala/index.html#play.api.libs.json.JsValue)

This is the super type of all the other types.

## Working with JSON

The entry point into Play's JSON API is [`play.api.libs.json.Json`](api/scala/index.html#play.api.libs.json.Json$).  It provides the following methods:

<!--
- `Json.parse` : parses a string to JsValue
- `Json.stringify` : stringifies a JsValue using compact printer (NO line feed/indentation)
- `Json.prettyPrint` : stringifies a JsValue using pretty printer (line feed + indentation)
- `Json.toJson[T](t: T)(implicit writes: Writes[T])` : tries to convert a Scala structure to a `JsValue` using the resolved implicit `Writes[T]`
- `Json.fromJson[T](json: JsValue)(implicit reads: Reads[T])` : tries to convert a `JsValue` to a Scala structure using the resolved implicit `Reads[T]`
- `Json.obj()` : simplified syntax to create a `JsObject`
- `Json.arr()` : simplified syntax to create a `JsArray`
-->
- `Json.parse` : 文字列を JsValue にパースします
- `Json.stringify` : JsValue をコンパクトな文字列にします (改行、インデントなし)
- `Json.prettyPrint` : JsValue を整形された文字列にします (改行、インデントあり)
- `Json.toJson[T](t: T)(implicit writes: Writes[T])` : 解決された暗黙の `Writes[T]` を使った Scala オブジェクトから `JsValue` への変換を試みます
- `Json.fromJson[T](json: JsValue)(implicit reads: Reads[T])` : 解決された暗黙の `Reads[T]` を使った `JsValue` から Scala オブジェクトへの変換を試みます
- `Json.obj()` : `JsObject` を作成するための簡素化された文法です
- `Json.arr()` : `JsArray` を作成するための簡素化された文法です

<!--
## Parsing a JSON String
-->
## JSON 文字列のパース

<!--
You can easily parse any JSON string as a `JsValue`:
-->
どのような JSON 文字列でも `JsValue` として簡単にパースすることができます:

@[parse-json](code/ScalaJson.scala)

The `json` value that the result was assigned to is used in subsequent code samples below.

<!--
## Constructing JSON directly
-->
## JSON ディレクトリの構築

<!--
### Raw way
-->
### 無骨な方法

The previous sample Json object can be created in other ways too.  Here is the raw approach:

@[construct-json-case-class](code/ScalaJson.scala)

<!--
### Preferred way
-->
### 推奨する方法

Play also provides a simplified syntax to build your JSON.  The previous JsObject can be constructed using the following:

@[construct-json-dsl](code/ScalaJson.scala)

<!--
## Serializing JSON
-->
## JSON のシリアライズ

<!--
Serializing a `JsValue` to its JSON String representation is easy:
-->
`JsValue` を JSON 文字列表現にシリアライズするのは簡単です:

@[serialise-json](code/ScalaJson.scala)

<!--
## Accessing Path in a JSON tree 
-->
## JSON ツリー内パスへのアクセス

As soon as you have a `JsValue` you can navigate into the JSON tree.  The API looks like the one provided to navigate into XML document by Scala using `NodeSeq` except you retrieve `JsValue`.

<!--
### Simple path `\`
-->
### シンプルな `\` パス

You can navigate through properties in an object using the `\` method:

@[traverse-path](code/ScalaJson.scala)

<!--
### Recursive path `\\`
-->
### 再帰的な `\\` パス

@[recursive-traverse-path](code/ScalaJson.scala)

<!--
## Converting JsValue to Scala Value
-->
## JsValue から Scala 値への変換

While navigating JSON tree, you retrieve `JsValue`, however you may want to convert the JsValue to a Scala type.  For example, you may want a `JsString` to be converted to a `String`, or a `JsNumber` to be converted to a `Long`.

<!--
### Unsafe conversion with `json.as[T]`
-->
### `json.as[T]` による安全でない変換

The simplest way to convert it to a value is to use the `as[T]` method, like so:

@[as-method](code/ScalaJson.scala)

This method however is unsafe, if the path is not found, or the conversion is not possible, a `JsResultException` is thrown, containing the error.

<!--
> Please note the error that doesn't return `path.not.found` as you may expect. This is a difference from JSON combinators presented later in the doc. 
> This is due to the fact that `(json \ "user" \ "nameXXX")` returns `JsNull` and the implicit `Reads[String]` here awaits a `JsString` which explains the detected error.
-->
> このエラーは、期待しているであろう `path.not.found` を返さないことに注意してください。これは、このドキュメントの後の方で登場する JSON コンビネーターとは異なります。
> これは、`(json \ "user" \ "nameXXX")` が `JsNull` を返却し、暗黙の `Reads[String]` は検出されたエラーで説明されている通り `JsString` を待ち受けていることによるものです。

<!--
### Safer conversion with `Option[T]`
-->
### `Option[T]` による、より安全な変換

The `asOpt[T]` method is like `as[T]`, however it will return `None` instead of throwing an exception if the path isn't found, or the conversion isn't possible:

@[as-opt](code/ScalaJson.scala)

<!--
### Safest conversion with `validate[T]`
-->
### `validate[T]` による、もっとも安全な変換

<!--
`asOpt[T]` is better but you lose the kind of error that was detected.  
-->
`asOpt[T]` は、より安全ですが、検出されたエラー等を失ってしまいます。

<!--
`validate[T]` is there to provide the safest and most robust way to convert a `JsValue` by returning a `JsResult[T]`:
-->
`validate[T]` は、`JsResult[T]` を返すことで、`JsValue` をもっとも安全で堅牢な方法を提供します:

<!--
- `JsResult[T]` accumulates all detected errors (doesn't stop at 1st error),
- `JsResult[T]` is a monadic structure providing `map`/`flatMap`/`fold` operations to manipulate, compose it.
-->
- `JsResult[T]` は (最初のエラーに留まらずに) 検出されたすべてのエラーを蓄積します。
- `JsResult[T]` は、これを操作、構成する `map`/`flatMap`/`fold` を提供するモナド構造です。.

#### `JsResult[T]` in a nutshell

<!--
`JsResult[T]` can have 2 values:
-->
`JsResult[T]` は二つの値を持つことができます:

<!--
- `JsSuccess[T](value: T, path: JsPath = JsPath())` contains: 
    - `value: T` when conversion was OK,
    - FYI, don't focus on `path` which is mainly an internal field used by the API to represent the current traversed `JsPath`.
  -->
- 以下を含む `JsSuccess[T](value: T, path: JsPath = JsPath())` :
    - 変換に成功した場合には `value: T`
    - ちなみに、`path` は走査された最新の `JsPath` を表現するために API が使う内部的なフィールドなので、気にしないでください。

<!--
> Please note : `JsPath` will be described later but it is just the same as `XMLPath` for JSON. 
> When you write : 
> `json \ "user" \ "name"`
> It can be written as following : 
> `(JsPath \ "user" \ "name")(json)`
> _You create a `JsPath` to search `user` then `name` and apply it to a given `json`._
-->
> 注意 : `JsPath` については後ほど説明しますが、JSON における `XMLPath` とまったく同じものです。
> `json \ "user" \ "name"`
> と書く場合、以下のように書くことができます :
> `(JsPath \ "user" \ "name")(json)`
> _`user` に続けて `name` を検索する `JsPath` を作成し、これを与えられた `json` に適用します。_

<!--
- `JsError(errors: Seq[(JsPath, Seq[ValidationError])])` :  
    - `errors` is a Sequence of pairs `(JsPath, Seq[ValidationError])`
    - pair `(JsPath, Seq[ValidationError])` locates one or more detected errors at given `JsPath`
  -->
- `JsError(errors: Seq[(JsPath, Seq[ValidationError])])` :  
    - `errors` は `(JsPath, Seq[ValidationError])` ペアの列です
    - `(JsPath, Seq[ValidationError])` ペアは、与えられた `JsPath` で検出したひとつ以上のエラーを示します

So a successful conversion might look like this:

@[validate-success](code/ScalaJson.scala)

If however the path wasn't found, we get this:

@[validate-failure](code/ScalaJson.scala)

Since `map` and `flatMap` are provided, for comprehensions can easily be used to extract values out:

@[validate-compose](code/ScalaJson.scala)

<!--
### Converting Recursive path `\\`
-->
### 再帰的なパス `\\` の変換

<!--
`\\` recursively searches in the sub-tree and returns a `Seq[JsValue]` of found JsValue which is then a collection with classical Scala functions.
-->
`\\` はサブツリーを再帰的に検索し、標準的な Scala 関数のコレクションである、発見した JsValue の `Seq[JsValue]` を返します。

@[recursive-as](code/ScalaJson.scala)

<!--
## Converting a Scala value to JsValue
-->
## Scala 値から JsValue への変換

<!--
Scala to JSON conversion is performed by function `Json.toJson[T](implicit writes: Writes[T])` based on implicit typeclass `Writes[T]` which is just able to convert a `T` to a `JsValue`. 
-->
Scala から JSON への変換は、ちょうど `T` から `JsValue` に変換するために利用できる、暗黙的な型クラス `Writes[T]` に基づいた `Json.toJson[T](implicit writes: Writes[T])` 関数によって行われます。

<!--
### Create very simple JsValue
-->
### とてもシンプルな JsValue を作る

@[convert-simple-type](code/ScalaJson.scala)

<!--
*This conversion is possible because Play JSON API provides an implicit `Writes[Int]`*
-->
*Play の JSON API が暗黙の `Writes[Int]` を提供するので、このように変換することができます*

### Create a JSON array from a `Seq[T]`

@[convert-seq](code/ScalaJson.scala)

<!--
*This conversion is possible because Play JSON API provides an implicit `Writes[Seq[Int]]`*
-->
*Play の JSON API が暗黙の `Writes[Seq[Int]]` を提供するので、このように変換することができます*

<!--
Here we have no problem to convert a `Seq[Int]` into a Json array. However it is more complicated if the `Seq` contains heterogeneous values:
-->
ここで、`Seq[Int]` は問題なく Json 配列に変換することができました。しかし、 `Seq` が異質な値を含むとすると、より複雑です:

```scala
import play.api.libs.json._

val jsonArray = Json.toJson(Seq(1, "Bob", 3, 4))
```

When we try to compile this, we get the following error:

    No Json deserializer found for type Seq[Any]. Try to implement an implicit Writes or Format for this type.

This is because there is no way to convert a `Seq[Any]` to Json (`Any` could be anything including something not supported by Json right?)

<!--
A simple solution is to handle it as a `Seq[JsValue]`:
-->
シンプルな解決方法は、これを `Seq[JsValue]` として扱うことです:

@[convert-hetro-seq](code/ScalaJson.scala)

<!--
*This conversion is possible because Play API JSON provides an implicit `Writes[Seq[JsValue]]`*
-->
*Play の JSON API が暗黙の `Writes[Seq[JsValue]]` を提供するので、このように変換することができます*

<!--
> **Next:** [[JSON Reads/Writes/Formats Combinators | ScalaJsonCombinators]]
-->
> **Next:** [[Json の読み/書き/フォーマットの結合| ScalaJsonCombinators]]