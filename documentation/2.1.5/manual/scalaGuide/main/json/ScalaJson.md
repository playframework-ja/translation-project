<!-- translated -->
<!-- # The Play JSON library Basics -->
# Play JSON ライブラリの基礎

<!-- ## Overview -->
## 概要

<!-- The recommended way of dealing with JSON is using Play’s typeclass based JSON library, located at ```play.api.libs.json```.  -->
JSON を使いたければ、```play.api.libs.json``` にある JSON ライブラリを基にした Play の型クラスを使うことをおすすめします。

<!-- For parsing JSON strings, Play uses super-fast Java based JSON library, [Jackson](http://jackson.codehaus.org/).   -->
JSON 文字列をパースするために、Play は超高速な Java ベースの JSON ライブラリ、[Jerkson](https://github.com/codahale/jerkson/) を使います。

<!-- The benefit of this approach is that both the Java and the Scala side of Play can share the same underlying library (Jackson), while Scala users can enjoy the extra type safety and functional aspects that Play’s JSON support brings to the table. -->
このアプローチの利点として、Scala ユーザーは Play の JSON サポートがお膳立てする特上の型安全性と関数型の側面を楽しむことができる一方、Java と Scala の両方の Play は同じ基礎となるライブラリ (Jackson) を共有することができます。

<!-- ## JSON is an AST (_Abstract Syntax Tree_) -->
## JSON は AST (_Abstract Syntax Tree: 抽象構文木_)

<!-- Take JSON example: -->
JSON の例を見てください:

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

<!-- This can be seen as a tree structure using the 2 following structures: -->
これは、以下の二つの仕組みを使った木構造として見ることができます:

<!-- - **JSON object** contains a set of `name` / `value` pairs:
    - `name` is a String
    - `value` can be :
        - string
        - number
        - another JSON object
        - a JSON array
        - true/false
        - null
- **JSON array** is a sequence of values from the previously listed value types. -->
- **JSON オブジェクト** には `name` / `value` のペアのセットが含まれます:
    - `name` は文字列
    - `value` は以下のいずれかです :
        - 文字列
        - 数値
        - 他の JSON オブジェクト
        - JSON 配列
        - true/false
        - null
- **JSON 配列** は、上述した value の型からなる値の列です。

<!-- > If you want to have more info about the exact JSON standard, please go to [json.org](http://json.org/) -->
> 正確な JSON 標準について詳しく知りたい場合は、[json.org](http://json.org/) へアクセスしてください


<!-- ## Json Data Types -->
## Json データ型

<!-- `play.api.libs.json` package contains 7 JSON data types reflecting exactly the previous structure. -->
`play.api.libs.json` には、前述の構造を厳密に反映した 7 つの JSON データ型が含まれています。

### ```JsObject``` 

<!-- - This is a set of name/value pairs as described in standard.
- `{ "name" : "toto", "age" : 45 }` as a JSON example. -->
- 標準で説明されている name/value ペアのセットです。
- 例えば `{ "name" : "toto", "age" : 45 }` は JSON です。

### ```JsNull```

<!-- This represents `null` value in JSON -->
JSON の `null` を表現します

### ```JsBoolean```

<!-- This is a boolean with value `true` or `false` -->
`true` か `false` の値を取る真偽値です

### ```JsNumber```

<!-- - JSON does NOT discriminate `short`, `int`, `long`, `float`, `double`, `bigdecimal` so it is represented by `JsNumber` containing a `bigdecimal`. 
- Play JSON API brings more type precision when converting to Scala structures. -->
- JSON は `short`, `int`, `long`, `float`, `double`, `bigdecimal` を区別しないので、`bigdecimal` を含む `JsNumber` として表現されます。
- Play の JSON API は、より正確な型の Scala オブジェクトに変換します。


### ```JsArray``` 

<!-- - An array is a sequence of any Json value types (not necessarily the same type).
- `[ "alpha", "beta", true, 123.44, 334]` as a JSON example. -->
- 配列は、あらゆる Json 値の型の列です (同じ型である必要はありません)
- 例えば `[ "alpha", "beta", true, 123.44, 334]` は JSON です。

### ```JsString```

<!-- A classic String. -->
標準的な文字列です。

### ```JsUndefined```

<!-- This is not part of the JSON standard and is only used internally by the API to represent some error nodes in the AST. -->
これは JSON 標準の一部ではなく、抽象構文木においてエラーとなったノードを表現するために API が内部でのみ使用します。

### ```JsValue```

<!-- All previous types inherit from the generic JSON trait, ```JsValue```. -->
上記のすべての型は、総称的な JSON トレイトである ```JsValue``` を継承します。

<!-- ## Minimal Import to work with basic JSON API -->
## 基本的な JSON API を使うための最小 import

```scala
import play.api.libs.json.Json
```

<!-- This import give access to the most basic JSON features : -->
この import は、もっとも基本的な JSON 機能へのアクセスを提供します:

<!-- - `Json.parse` : parses a string to JsValue
- `Json.stringify` : stringifies a JsValue using compact printer (NO line feed/indentation)
- `Json.prettyPrint` : stringifies a JsValue using pretty printer (line feed + indentation)
- `Json.toJson[T](t: T)(implicit writes: Writes[T])` : tries to convert a Scala structure to a `JsValue` using the resolved implicit `Writes[T]`
- `Json.fromJson[T](json: JsValue)(implicit reads: Reads[T])` : tries to convert a `JsValue` to a Scala structure using the resolved implicit `Reads[T]`
- `Json.obj()` : simplified syntax to create a `JsObject`
- `Json.arr()` : simplified syntax to create a `JsArray` -->
- `Json.parse` : 文字列を JsValue にパースします
- `Json.stringify` : JsValue をコンパクトな文字列にします (改行、インデントなし)
- `Json.prettyPrint` : JsValue を整形された文字列にします (改行、インデントあり)
- `Json.toJson[T](t: T)(implicit writes: Writes[T])` : 解決された暗黙の `Writes[T]` を使った Scala オブジェクトから `JsValue` への変換を試みます
- `Json.fromJson[T](json: JsValue)(implicit reads: Reads[T])` : 解決された暗黙の `Reads[T]` を使った `JsValue` から Scala オブジェクトへの変換を試みます
- `Json.obj()` : `JsObject` を作成するための簡素化された文法です
- `Json.arr()` : `JsArray` を作成するための簡素化された文法です

<!-- ## Parsing a JSON String -->
## JSON 文字列のパース

<!-- You can easily parse any JSON string as a `JsValue`: -->
どのような JSON 文字列でも `JsValue` として簡単にパースすることができます:

```
import play.api.libs.json.Json

val json: JsValue = Json.parse("""
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
""")
```

<!-- This sample is used in all next samples. -->
このサンプルは、以下すべての例で使用します。

<!-- As explained previously, the parsing is performed by [Jackson](http://jackson.codehaus.org/). -->
上記にて説明した通り、パースは [Jackson](http://jackson.codehaus.org/) によって行われます。

<!-- ## Constructing JSON directly -->
## JSON ディレクトリの構築

<!-- ### Raw way -->
### 無骨な方法

<!-- The previous sample Json object can be created in other ways too. 
Here is the raw approach. -->
上記した Json オブジェクトの例は、別の方法でも作成することができます。
以下は無骨なアプローチです。

```
import play.api.libs.json._

JsObject(
  "users" -> JsArray(
    JsObject(
      "name" -> JsString("Bob") ::
      "age" -> JsNumber(31) ::
      "email" -> JsString("bob@gmail.com") ::
      Nil) ::
    JsObject(
      "name" -> JsString("Kiki") ::
      "age" -> JsNumber(25) ::
      "email" -> JsNull ::
      Nil
    ) :: Nil
  ) :: Nil
)
```

<!-- ### Preferred way -->
### 推奨する方法

<!-- Play now provides a simplified syntax to build your JSON.
The previous JsObject can be constructed as following: -->
Play は JSON を作成する簡素化された文法を提供しています。
上記の JsObject は以下のようにして構築することができます:

```
import play.api.libs.json.Json

Json.obj(
  "users" -> Json.arr(
    Json.obj(
      "name" -> "bob",
      "age" -> 31,
      "email" -> "bob@gmail.com"  	  
    ),
    Json.obj(
      "name" -> "kiki",
      "age" -> 25,
      "email" -> JsNull  	  
    )
  )
)
```


<!-- ## Serializing JSON -->
## JSON のシリアライズ

<!-- Serializing a `JsValue` to its JSON String representation is easy: -->
`JsValue` を JSON 文字列表現にシリアライズするのは簡単です:

```
import play.api.libs.json.Json

val jsonString: String = Json.stringify(jsValue)
```


<!-- ## Accessing Path in a JSON tree  -->
## JSON ツリー内パスへのアクセス

<!-- As soon as you have a `JsValue` you can navigate into the JSON tree.  
The API looks like the one provided to navigate into XML document by Scala using `NodeSeq` except you retrieve `JsValue`. -->
`JsValue` さえ手に入れば、JSON ツリーの中を探索することができます。
この API は、`JsValue` を探索することを除けば、Scala で `NodeSeq` を使って XML ドキュメントを探索するために提供されている API と似ています。

<!-- ### Simple path `\` -->
### シンプルな `\` パス

<!-- ```scala
// Here we import everything under json in case we need to manipulate different Json types
scala> import play.api.libs.json._

scala> val name: JsValue = json \ "user" \ "name"
name: play.api.libs.json.JsValue = "toto"
``` -->
```scala
// 様々な種類の Json を操作する必要がある場合に備えて、ここでは json パッケージ配下をすべてインポートします
scala> import play.api.libs.json._

scala> val name: JsValue = json \ "user" \ "name"
name: play.api.libs.json.JsValue = "toto"
```

<!-- ### Recursive path `\\` -->
### 再帰的な `\\` パス
 
<!-- ```scala
// recursively searches in the sub-tree and returns a Seq[JsValue]
// of all found JsValue
scala> val emails: Seq[String] = json \ "user" \\ "email"
emails: Seq[play.api.libs.json.JsValue] = List("toto@jmail.com", "tata@coldmail.com")
``` -->
```scala
// サブツリーを再帰的に検索し、見つかったすべての JsValue に対する
// Seq[JsValue] を返します
scala> val emails: Seq[String] = json \ "user" \\ "email"
emails: Seq[play.api.libs.json.JsValue] = List("toto@jmail.com", "tata@coldmail.com")
``` 

<!-- ## Converting JsValue to Scala Value -->
## JsValue から Scala 値への変換

<!-- While navigating JSON tree, you retrieve `JsValue` but you may want to convert the JsValue to a Scala type. 
For ex, a `JsString` to a `String` or a `JsNumber` to a `Long` (if it can be converted). -->
JSON ツリーを渡り歩いていると `JsValue` を見つけることができますが、JsValue から Scala の型へ変換したくなるかもしれません。
例えば、`JsString` を `String` に、または (もし変換できるなら) `JsNumber` を `Long` に、と言った具合です。

<!-- ### Unsafe conversion with `json.as[T]` -->
### `json.as[T]` による安全でない変換

<!-- `as[T]` is unsafe because it tries to access the path and to convert to the required type. But if the path is not found or the conversion not possible, it generates a `JsResultException` RuntimeException containing detected errors. -->
`as[T]` は、パスにアクセスして要求された型に変換しようとするので、安全ではありません。しかし、パスが見つからなかったり、変換が不可能だった場合は、検出されたエラーを含む実行時例外 `JsResultException` を生成します。

<!-- #### case OK: path found & conversion possible -->
#### 成功した場合: パスが見つかり、変換できる

<!-- ```scala
// returns the value converted to provided type (if possible and if found)
scala> val name: String = (json \ "user" \ "name").as[String]
name: String = toto
``` -->
```scala
// (変換が可能でパスが見つかった場合) 提供された型に変換された値を返します
scala> val name: String = (json \ "user" \ "name").as[String]
name: String = toto
```

<!-- #### case KO: Path not found -->
#### 失敗した場合: パスが見つからない

```scala
scala> val nameXXX: String = (json \ "user" \ "nameXXX").as[String]
play.api.libs.json.JsResultException: JsResultException(errors:List((,List(ValidationError(validate.error.expected.jsstring,WrappedArray())))))
	at play.api.libs.json.JsValue$$anonfun$4.apply(JsValue.scala:65)
	at play.api.libs.json.JsValue$$anonfun$4.apply(JsValue.scala:65)
	at play.api.libs.json.JsResult$class.fold(JsResult.scala:69)
	at play.api.libs.json.JsError.fold(JsResult.scala:10)
	at play.api.libs.json.JsValue$class.as(JsValue.scala:63)
	at play.api.libs.json.JsUndefined.as(JsValue.scala:96)
```

<!-- > Please note the error that doesn't return `path.not.found` as you may expect. This is a difference from JSON combinators presented later in the doc. 
> This is due to the fact that `(json \ "user" \ "nameXXX")` returns `JsNull` and the implicit `Reads[String]` here awaits a `JsString` which explains the detected error. -->
> このエラーは、期待しているであろう `path.not.found` を返さないことに注意してください。これは、このドキュメントの後の方で登場する JSON コンビネーターとは異なります。
> これは、`(json \ "user" \ "nameXXX")` が `JsNull` を返却し、暗黙の `Reads[String]` は検出されたエラーで説明されている通り `JsString` を待ち受けていることによるものです。


<!-- #### case KO: Conversion not possible -->
#### 失敗した場合: 変換できない

```scala
scala> val name: Long = (json \ "user" \ "name").as[Long]
play.api.libs.json.JsResultException: JsResultException(errors:List((,List(ValidationError(validate.error.expected.jsnumber,WrappedArray())))))
	at play.api.libs.json.JsValue$$anonfun$4.apply(JsValue.scala:65)
	at play.api.libs.json.JsValue$$anonfun$4.apply(JsValue.scala:65)
	at play.api.libs.json.JsResult$class.fold(JsResult.scala:69)
	at play.api.libs.json.JsError.fold(JsResult.scala:10)
	at play.api.libs.json.JsValue$class.as(JsValue.scala:63)
	at play.api.libs.json.JsString.as(JsValue.scala:111)
```

<br/>
<!-- ### Safer conversion with `Option[T]` -->
### `Option[T]` による、より安全な変換

<!-- `as[T]` is immediate but not robust so there is `asOpt[T]` which returns None in case of error of any type. -->
`as[T]` は即時性はあるものの堅牢ではないので、あらゆる型のエラーが発生した場合に None を返す `asOpt[T]` が用意されています。

<!-- #### case OK: path found & conversion possible -->
#### 成功した場合: パスが見つかり、変換できる

```scala
scala> val maybeName: Option[String] = (json \ "user" \ "name").asOpt[String]
maybeName: Option[String] = Some(toto)
```

<!-- #### case KO: Path not found -->
#### 失敗した場合: パスが見つからない

```scala
scala> val maybeNameXXX: Option[String] = (json \ "user" \ "nameXXX").asOpt[String]
maybeNameXXX: Option[String] = None
```

<!-- #### case KO: Conversion not possible -->
#### 失敗した場合: 変換できない

```scala
scala> val maybeNameLong: Option[Long] = (json \ "user" \ "name").asOpt[Long]
maybeNameLong: Option[Long] = None
```

<br/>
<!-- ### Safest conversion with `validate[T]` -->
### `validate[T]` による、もっとも安全な変換

<!-- `asOpt[T]` is better but you lose the kind of error that was detected.   -->
`asOpt[T]` は、より安全ですが、検出されたエラー等を失ってしまいます。

<!-- `validate[T]` is there to provide the safest and most robust way to convert a `JsValue` by returning a `JsResult[T]`: -->
`validate[T]` は、`JsResult[T]` を返すことで、`JsValue` をもっとも安全で堅牢な方法を提供します:

<!-- - `JsResult[T]` accumulates all detected errors (doesn't stop at 1st error),
- `JsResult[T]` is a monadic structure providing `map`/`flatMap`/`fold` operations to manipulate, compose it. -->
- `JsResult[T]` は (最初のエラーに留まらずに) 検出されたすべてのエラーを蓄積します。
- `JsResult[T]` は、これを操作、構成する `map`/`flatMap`/`fold` を提供するモナド構造です。

<!-- #### `JsResult[T]` in a very nutshell -->
#### `JsResult[T]` をひと言で

<!-- `JsResult[T]` can have 2 values: -->
`JsResult[T]` は二つの値を持つことができます:

<!-- - `JsSuccess[T](value: T, path: JsPath = JsPath())` contains: 
    - `value: T` when conversion was OK,
    - FYI, don't focus on `path` which is mainly an internal field used by the API to represent the current traversed `JsPath`. -->
- 以下を含む `JsSuccess[T](value: T, path: JsPath = JsPath())` :
    - 変換に成功した場合には `value: T`
    - ちなみに、`path` は走査された最新の `JsPath` を表現するために API が使う内部的なフィールドなので、気にしないでください。

<!-- > Please note : `JsPath` will be described later but it is just the same as `XMLPath` for JSON. 
> When you write : 
> `json \ "user" \ "name"`
> It can be written as following : 
> `(JsPath \ "user" \ "name")(json)`
> _You create a `JsPath` to search `user` then `name` and apply it to a given `json`._ -->
> 注意 : `JsPath` については後ほど説明しますが、JSON における `XMLPath` とまったく同じものです。
> `json \ "user" \ "name"`
> と書く場合、以下のように書くことができます :
> `(JsPath \ "user" \ "name")(json)`
> _`user` に続けて `name` を検索する `JsPath` を作成し、これを与えられた `json` に適用します。_

<!-- - `JsError(errors: Seq[(JsPath, Seq[ValidationError])])` :  
    - `errors` is a Sequence of pairs `(JsPath, Seq[ValidationError])`
    - pair `(JsPath, Seq[ValidationError])` locates one or more detected errors at given `JsPath` -->
- `JsError(errors: Seq[(JsPath, Seq[ValidationError])])` :  
    - `errors` は `(JsPath, Seq[ValidationError])` ペアの列です
    - `(JsPath, Seq[ValidationError])` ペアは、与えられた `JsPath` で検出したひとつ以上のエラーを示します

<!-- A few samples of usage: -->
いくつかの使用例:

<!-- ```scala
scala> import play.api.libs.json._

scala> val jsres: JsResult[String] = JsString("toto").validate[String]
jsres: JsSuccess("toto")

scala> val jsres: JsResult[String] = JsNumber(123).validate[String]
jsres: play.api.libs.json.JsResult[String] = JsError(List((,List(ValidationError(validate.error.expected.jsstring,WrappedArray())))))

jsres.map{ s: String => …}
jsres.flatMap{ s: String => JsSuccess(s) }

jsres.fold( 
  errors: Seq[(JsPath, Seq[ValidationError])] => // manage errors,
  s: String => // manage value 
)

jsres.map( s: String => // manage value )
     .recoverTotal( jserror: JsError => // manage errors and return default value)
``` -->
```scala
scala> import play.api.libs.json._

scala> val jsres: JsResult[String] = JsString("toto").validate[String]
jsres: JsSuccess("toto")

scala> val jsres: JsResult[String] = JsNumber(123).validate[String]
jsres: play.api.libs.json.JsResult[String] = JsError(List((,List(ValidationError(validate.error.expected.jsstring,WrappedArray())))))

jsres.map{ s: String => …}
jsres.flatMap{ s: String => JsSuccess(s) }

jsres.fold( 
  errors: Seq[(JsPath, Seq[ValidationError])] => // エラーを処理して,
  s: String => // 値を処理する 
)

jsres.map( s: String => // manage value )
     .recoverTotal( jserror: JsError => // エラーを処理をしてデフォルト値を返す)
```

<!-- #### case OK: path found & conversion possible -->
#### 成功した場合: パスが見つかり、変換できる

<!-- ```scala
scala> val safeName = (json \ "user" \ "name").validate[String]
safeName: play.api.libs.json.JsResult[String] = JsSuccess(toto,) // path is not precised because it's root
``` -->
```scala
scala> val safeName = (json \ "user" \ "name").validate[String]
safeName: play.api.libs.json.JsResult[String] = JsSuccess(toto,) // path は root なので厳密ではありません
```

<!-- #### case KO: Path not found -->
#### 失敗した場合: パスが見つからない

```scala
scala> val nameXXX = (json \ "user" \ "nameXXX").validate[String]
nameXXX: play.api.libs.json.JsResult[String] = 
  JsError(List((,List(ValidationError(validate.error.expected.jsstring,WrappedArray())))))
```

<!-- > Please note the error that doesn't return `path.not.found` as you may expect. This is a difference from JSON combinators presented later in the doc. 
> This is due to the fact that `(json \ "user" \ "nameXXX")` returns `JsNull` and the implicit `Reads[String]` here awaits a `JsString` which explains the detected error. -->
> このエラーは、期待しているであろう `path.not.found` を返さないことに注意してください。これは、このドキュメントの後の方で登場する JSON コンビネーターとは異なります。
> これは、`(json \ "user" \ "nameXXX")` が `JsNull` を返却し、暗黙の `Reads[String]` は検出されたエラーで説明されている通り `JsString` を待ち受けていることによるものです。

<!-- #### case KO: Conversion not possible -->
#### 失敗した場合: 変換できない

```scala
scala> val name = (json \ "user" \ "name").validate[Long]
name: play.api.libs.json.JsResult[Long] = 
  JsError(List((,List(ValidationError(validate.error.expected.jsnumber,WrappedArray())))))
```

<br/>
<!-- ### Converting Recursive path `\\` -->
### 再帰的なパス `\\` の変換

<!-- `\\` recursively searches in the sub-tree and returns a `Seq[JsValue]` of found JsValue which is then a collection with classical Scala functions. -->
`\\` はサブツリーを再帰的に検索し、標準的な Scala 関数のコレクションである、発見した JsValue の `Seq[JsValue]` を返します。

```scala
scala> val emails: Seq[String] = (json \ "user" \\ "email").map(_.as[String])
emails: Seq[String] = List(toto@jmail.com, tata@coldmail.com)
```
<br/>
<!-- ## Converting a Scala value to JsValue -->
## Scala 値から JsValue への変換

<!-- Scala to JSON conversion is performed by function `Json.toJson[T](implicit writes: Writes[T])` based on implicit typeclass `Writes[T]` which is just able to convert a `T` to a `JsValue`.  -->
Scala から JSON への変換は、ちょうど `T` から `JsValue` に変換するために利用できる、暗黙的な型クラス `Writes[T]` に基づいた `Json.toJson[T](implicit writes: Writes[T])` 関数によって行われます。

<!-- ### Create very simple JsValue -->
### とてもシンプルな JsValue を作る

```
val jsonNumber = Json.toJson(4)
jsonNumber: play.api.libs.json.JsValue = 4
```

<!-- *This conversion is possible because Play JSON API provides an implicit `Writes[Int]`* -->
*Play の JSON API が暗黙の `Writes[Int]` を提供するので、このように変換することができます*


<!-- ### Create a JSON array from a Seq[T] -->
### Seq[T] から JSON 配列を作る

```
import play.api.libs.json.Json

val jsonArray = Json.toJson(Seq(1, 2, 3, 4))
jsonArray: play.api.libs.json.JsValue = [1,2,3,4]
```

<!-- *This conversion is possible because Play JSON API provides an implicit `Writes[Seq[Int]]`* -->
*Play の JSON API が暗黙の `Writes[Seq[Int]]` を提供するので、このように変換することができます*

<!-- Here we have no problem to convert a `Seq[Int]` into a Json array. However it is more complicated if the `Seq` contains heterogeneous values: -->
ここで、`Seq[Int]` は問題なく Json 配列に変換することができました。しかし、 `Seq` が異質な値を含むとすると、より複雑です:

```
import play.api.libs.json.Json

val jsonArray = Json.toJson(Seq(1, "Bob", 3, 4))
<console>:11: error: No Json deserializer found for type Seq[Any]. Try to implement an implicit Writes or Format for this type.
       val jsonArray = Json.toJson(Seq(1, "Bob", 3, 4))
```

<!-- You get an error because there is no way to convert a `Seq[Any]` to Json (`Any` could be anything including something not supported by Json right?) -->
`Seq[Any]` を Json に変換する手段がないためエラーになります (`Any` は Json でサポートされていないものまで含むことができますよね?)

<!-- A simple solution is to handle it as a `Seq[JsValue]`: -->
シンプルな解決方法は、これを `Seq[JsValue]` として扱うことです:

```
import play.api.libs.json.Json

val jsonArray = Json.toJson(Seq(
  toJson(1), toJson("Bob"), toJson(3), toJson(4)
))
```
<!-- *This conversion is possible because Play API JSON provides an implicit `Writes[Seq[JsValue]]`* -->
*Play の JSON API が暗黙の `Writes[Seq[JsValue]]` を提供するので、このように変換することができます*

<!-- ### Create a JSON object from a Map[String, T] -->
### Map[String, T] から JSON オブジェクトを作る

```
import play.api.libs.json.Json

val jsonObject = Json.toJson(
  Map(
    "users" -> Seq(
      toJson(
        Map(
          "name" -> toJson("Bob"),
          "age" -> toJson(31),
          "email" -> toJson("bob@gmail.com")
        )
      ),
      toJson(
        Map(
          "name" -> toJson("Kiki"),
          "age" -> toJson(25),
          "email" -> JsNull
        )
      )
    )
  )
)
```

<!-- That will generate this Json result: -->
これは次の Json を生成します:

```
{
  "users":[
    {
      "name": "Bob",
      "age": 31.0,
      "email": "bob@gmail.com"
    },
    {
      "name": "Kiki",
      "age":  25.0,
      "email": null
    }
  ]
}
```

<!-- > **Next:** [[JSON Reads/Writes/Formats Combinators | ScalaJsonCombinators]] -->
> **Next:** [[Json の読み/書き/フォーマットの結合| ScalaJsonCombinators]]