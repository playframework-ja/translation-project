<!-- translated -->
<!--
# JSON Reads[T]/Writes[T]/Format[T] Combinators
-->
# JSON Reads[T]/Writes[T]/Format[T] コンビネータ

> Please note this documentation was initially published as an article by Pascal Voitot ([@mandubian](https://github.com/mandubian)) on [mandubian.com](http://mandubian.com/2012/09/08/unveiling-play-2-dot-1-json-api-part1-jspath-reads-combinators/)

<!--
## Summary of main new features added in <code>Play2.1</code>
-->
## <code>Play2.1</code> における新機能の概要

<!--
- `Reads[T]` / `Writes[T]` / `Format[T]` combinators based on JsPath and simple logic operators
-->
- `Reads[T]` / `Writes[T]` / `Format[T]` コンビネータは、JsPath とシンプルな論理演算子に基づいています

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

val customReads: Reads[(String, Float, List[String])] = 
  (JsPath \ "key1").read[String](email keepAnd minLength(5)) and 
  (JsPath \ "key2").read[Float](min(45)) and
  (JsPath \ "key3").read[List[String]] 
  tupled
```

<!--
- `Reads[T]` API now validates JSON by returning a monadic `JsResult[T]` being a `JsSuccess[T]` or a `JsError` aggregating all validation errors 
-->
- `Reads[T]` API は、`JsSuccess[T]` か、あるいはすべてのバリデーションエラーを集約した`JsError` として、モナディックな `JsResult[T]` を返すことにより JSON のバリデーションを行います

```
import play.api.libs.json.Json

val js = Json.obj(
  "key1" -> "alpha", 
  "key2" -> 123.345F, 
  "key3" -> Json.arr("alpha", "beta")
)

scala> customReads.reads(js) 
res5: JsSuccess(("alpha", 123.345F, List("alpha", "beta")))

customReads.reads(js).fold(
  invalid = { errors => ... },
  valid = { res => 
    val (s, f, l): (String, Float, List[String]) = res
    ...
  }
)
```

<!--
**Now let's go in the details ;)**
-->
**それでは詳細に踏み込んでみましょう ;)**

<br/>
<br/>
<!--
# <a name="quick-jspath">JsPath in a nutshell</a>
-->
# <a name="quick-jspath">JsPath とは</a>

<!--
You certainly know `XMLPath` in which you can access a node of an XML AST using a simple syntax based on path.  
JSON is already an AST and we can apply the same kind of syntax to it and logically we called it `JsPath`.
-->
パスに基づいたシンプルな構文を使って XML 抽象構文木のノードにアクセスすることのできる `XMLPath` についてはご存知でしょう。
JSON は抽象構文木なので、同じような構文を適用することができます。そして、これを論理的に `JsPath` と呼んでいます。

<!--
All following examples use JSON defined in previous paragraph.
-->
以下のすべての例では、ひとつ前の段落で定義した JSON を使います。

<!--
### Building JsPath
-->
### JsPath を構築する

<!--
```
import play.api.libs.json._

// Simple path
JsPath \ "key1"

// 2-levels path
JsPath \ "key3" \ "key33"
 
// indexed path
(JsPath \ "key3" \ "key32")(2) // 2nd element in a JsArray

// multiple/recursive paths
JsPath \\ "key1"
```
-->
```
import play.api.libs.json._

// シンプルなパス
JsPath \ "key1"

// 二階層のパス
JsPath \ "key3" \ "key33"

// インデックス付けされたパス
(JsPath \ "key3" \ "key32")(2) // JsArray の二番目の要素

// 複数の/再帰的なパス
JsPath \\ "key1"
```

<!--
### Alternative syntax
-->
### 代替構文

<!--
`JsPath` is quite cool but we found this syntax could be made even clearer to highlight `Reads[T]` combinators in the code.  
That's why we provide an alias for `JsPath`: `__` (2 underscores).  
_You can use it or not. This is just a visual facility because with it, you immediately find your JsPath in the code…_
-->
`JsPath` という構文もかなりかっこいいのですが、コード中において `Reads[T]` をよりクリアに目立たせることのできる構文を見つけました。
このような理由から、`JsPath` のエイリアスを提供しています: `__` (二つのアンダースコア) です。
_この構文を使っても使わなくても構いません。この構文は、コードの中から JsPath をすぐに見つけられるようにする、視覚的な利便性に過ぎません_

<!--
You can write:
-->
次のように書くことができます:

<!--
```
import play.api.libs.json._
import play.api.libs.functional.syntax._

// Simple path
__ \ "key1"

// 2-levels path
__ \ "key3" \ "key33"
 
// indexed path
(__ \ "key3" \ "key32")(2) // 2nd element in a JsArray

// multiple paths
__ \\ "key1"

// a sample on Reads[T] combinators to show the difference with both syntax
// DON'T TRY TO UNDERSTAND THIS CODE RIGHT NOW… It's explained in next paragraphs
val customReads = 
  (JsPath \ "key1").read(
    (JsPath \ "key11").read[String] and
    (JsPath \ "key11").read[String] and
    (JsPath \ "key11").read[String]
    tupled
  ) and 
  (JsPath \ "key2").read[Float](min(45)) and
  (JsPath \ "key3").read(
    (JsPath \ "key31").read[String] and
    (JsPath \ "key32").read[String] and
    (JsPath \ "key33").read[String]
    tupled
  ) 
  tupled
// with __
val customReads = 
  (__ \ "key1").read(
    (__ \ "key11").read[String] and
    (__ \ "key11").read[String] and
    (__ \ "key11").read[String]  
    tupled
  ) and 
  (__ \ "key2").read[Float](min(45)) and
  (__ \ "key3").read[List[String]] (
    (__ \ "key31").read[String] and
    (__ \ "key32").read[String] and
    (__ \ "key33").read[String]
    tupled  
  )
  tupled

// You can immediately see the structure of the JSON tree
```
-->
```
import play.api.libs.json._
import play.api.libs.functional.syntax._

// シンプルなパス
__ \ "key1"

// 二階層のパス
__ \ "key3" \ "key33"

// インデックス付けされたパス
(__ \ "key3" \ "key32")(2) // JsArray の二番目の要素

// 複数のパス
__ \\ "key1"

// 双方の構文の違いを紹介する Reads[T] コンビネータのサンプル
// 今すぐこのコードを理解しようとしないでください… これは次の段落で説明します
val customReads =
  (JsPath \ "key1").read(
    (JsPath \ "key11").read[String] and
    (JsPath \ "key11").read[String] and
    (JsPath \ "key11").read[String]
    tupled
  ) and
  (JsPath \ "key2").read[Float](min(45)) and
  (JsPath \ "key3").read(
    (JsPath \ "key31").read[String] and
    (JsPath \ "key32").read[String] and
    (JsPath \ "key33").read[String]
    tupled
  )
  tupled

// __ を使った場合
val customReads =
  (__ \ "key1").read(
    (__ \ "key11").read[String] and
    (__ \ "key11").read[String] and
    (__ \ "key11").read[String]
    tupled
  ) and
  (__ \ "key2").read[Float](min(45)) and
  (__ \ "key3").read[List[String]] (
    (__ \ "key31").read[String] and
    (__ \ "key32").read[String] and
    (__ \ "key33").read[String]
    tupled
  )
  tupled

// JSON ツリーの構造がひと目で分かりますね
```

<!--
### Accessing value of JsPath
-->
### JsPath の値へのアクセス

<!--
The important function to retrieve the value at a given JsPath in a JsValue is the following:
-->
与えられた JsPath にある JsValue から値を取り出す重要な関数は以下のとおりです:

```
sealed trait PathNode {
  def apply(json: JsValue): List[JsValue]
  …
}
```

<!--
As you can see, this function retrieves a `List[JsValue]`
-->
ご覧のとおり、この関数は `List[JsValue]` を取り出します

<!--
You can simply use it like:
-->
これは、以下のようにシンプルに使うことができます:

<!--
```
import play.api.libs.json._

// build a JsPath
scala> (__ \ "key1")(js) 
res12: List[play.api.libs.json.JsValue] = List("value1")  // actually this is JsString("value1")

// 2-levels path
scala> (__ \ "key3" \ "key33")(js)
res13: List[play.api.libs.json.JsValue] = List({"key":"value2","key34":"value34"})
 
// indexed path
scala> (__ \ "key3" \ "key32")(2)(js)
res14: List[play.api.libs.json.JsValue] = List(234.13)

// multiple paths
scala> (__ \\ "key1")(js)
res17: List[play.api.libs.json.JsValue] = List("value1", "value2")
```
-->
```
import play.api.libs.json._

// JsPath を構築する
scala> (__ \ "key1")(js)
res12: List[play.api.libs.json.JsValue] = List("value1")  // これは実際には JsString("value1") です

// 二階層のパス
scala> (__ \ "key3" \ "key33")(js)
res13: List[play.api.libs.json.JsValue] = List({"key":"value2","key34":"value34"})

// インデックス付けされたパス
scala> (__ \ "key3" \ "key32")(2)(js)
res14: List[play.api.libs.json.JsValue] = List(234.13)

// 複数のパス
scala> (__ \\ "key1")(js)
res17: List[play.api.libs.json.JsValue] = List("value1", "value2")
```

<br/>
<br/>
<!--
# <a name="reads">Reads[T] is now a validator</a>
-->
# <a name="reads">Reads[T] はバリデータに</a>

<!--
## <a name="reads-2_0">Reads in Play2.0.x</a>
-->
## <a name="reads-2_0">Play2.0.x における Reads</a>

<!--
Do you remember how you had to write a Json `Reads[T]` in `Play2.0.x` ?  
You had to override the `reads` function.  
-->
`Play2.0.x` では、どのように Json `Reads[T]` を書いたか覚えていますか?
`reads` 関数をオーバーライドする必要がありました。

<!--
```
trait Reads[A] {
  self =>
  /**
   * Convert the JsValue into a A
   */
  def reads(json: JsValue): A
}
```
-->
```
trait Reads[A] {
  self =>
  /**
   * JsValue を A に変換する
   */
  def reads(json: JsValue): A
}
```

<!--
Take the following simple case class that you want to map on JSON structure:
-->
以下のような、JSON 構造をマッピングするシンプルなケースクラスを取り上げます:

```
case class Creature(
  name: String, 
  isDead: Boolean, 
  weight: Float
)
```

<!--
In `Play2.0.x`, you would write your reader as following:
-->
`Play2.0.x` では、以下のような reader を書いていたことでしょう:

```
import play.api.libs.json._

implicit val creatureReads = new Reads[Creature] {
  def reads(js: JsValue): Creature = {
    Creature(
      (js \ "name").as[String],
      (js \ "isDead").as[Boolean],
      (js \ "weight").as[Float]
    )
  }
}

scala> val js = Json.obj( "name" -> "gremlins", "isDead" -> false, "weight" -> 1.0F)
scala> val c = js.as[Creature] 
c: Creature("gremlins", false, 1.0F)
```

<!--
Easy isn't it ?
So what's the problem if it's so easy?
-->
簡単ですよね?
こんなに簡単なら、何が問題なのでしょう?

<!--
Imagine, you pass the following JSON with a missing field:
-->
以下のような、フィールドが不足した JSON を渡すことを想像してください:

```
val js = Json.obj( "name" -> "gremlins", "weight" -> 1.0F)
```

<!--
What happens?
-->
なにが起こるでしょう?

```
java.lang.RuntimeException: Boolean expected
  at play.api.libs.json.DefaultReads$BooleanReads$.reads(Reads.scala:98)
  at play.api.libs.json.DefaultReads$BooleanReads$.reads(Reads.scala:95)
  at play.api.libs.json.JsValue$class.as(JsValue.scala:56)
  at play.api.libs.json.JsUndefined.as(JsValue.scala:70)
```

<!--
**What is this?**
Yes ugly RuntimeException (not even subtyped) but you can work around it using `JsValue.asOpt[T]` :)
-->
**これは何でしょう?**
そうです、みっともない (サブクラスですらない) RuntimeException です。しかし、`JsValue.asOpt[T]` を使ってこれに対処することができます :)

```
scala> val c: Option[Creature] = js.asOpt[Creature]
c: None
```

<!--
**Cool but you only know that the deserialization `Json => Creature` failed but not where or on which field(s)?**
-->
**クールですが、分かるのは `Json => Creature` のデシリアライズに失敗したことだけで、どこ、あるいはどのフィールドで失敗したのかは分かりません**

<br/>
<br/>
<!--
## <a name="reads-2_1">Reads in Play2.1</a>
We couldn't keep this imperfect API as is and in `Play2.1`, the `Reads[T]` API has changed into :
-->
## <a name="reads-2_1">Play2.1 における Reads</a>
この不完全な API を `Play2.1` でそのままにしておけず、 `Reads[T]` API を以下のように変更しました :

<!--
```
trait Reads[A] {
  self =>
  /**
   * Convert the JsValue into a A
   */
  def reads(json: JsValue): JsResult[A]
}
```
-->
```
trait Reads[A] {
  self =>
  /**
   * JsValue を A に変換する
   */
  def reads(json: JsValue): JsResult[A]
}
```

<!--
> Yes you have to refactor all your existing custom Reads but you'll see you'll get lots of new interesting features…
-->
> そうです、すでに存在する自作の Reads は、すべてリファクタリングしなければなりません。しかし、たくさんの興味深い新機能が手に入ることがすぐに分かると思います…

<!--
So you remark immediately `JsResult[A]` which is a very simple structure looking a bit like an Either applied to our specific problem.  
-->
すぐに、今回の問題に適用した Either にちょっと似ているように見える、とてもシンプルな構造の `JsResult[A]` に目が行ったことと思います。

<!--
**`JsResult[A]` can be of 2 types**:
-->
**`JsResult[A]` は二つのタイプになり得ます**:

<!--
- **`JsSuccess[A]` when `reads`succeeds**
-->
- **`reads` が成功した場合は `JsSuccess[A]`**

<!--
```
case class JsSuccess[A](
  value: T, // the value retrieved when deserialization JsValue => A worked
  path: JsPath = JsPath() // the root JsPath where this A was read in the JsValue (by default, it's the root of the JsValue)
) extends JsResult[T]

// To create a JsSuccess from a value, simply do:
val success = JsSuccess(Creature("gremlins", false, 1.0))

```
-->
```
case class JsSuccess[A](
  value: T, // JsValue => A のデシリアライズが動作したときに取り出される値
  path: JsPath = JsPath() // この A が JsValue に読み込まれたときのルートの JsPath (デフォルトでは JsValue のルート)
) extends JsResult[T]

// 値から JsSuccess を作るには、次のようにシンプルに行います:
val success = JsSuccess(Creature("gremlins", false, 1.0))

```

<!--
- **`JsError[A]` when `reads` fails**
-->
- **`reads` が失敗した場合は `JsError[A]`**

<!--
> Please note the greatest advantage of JsError is that it's a cumulative error which can store several errors discovered in the Json at different JsPath
-->
> JsError は累積エラーであり、異なる JsPath にある Json で検出された複数のエラーを保存することができます。この素晴らしい利点に注目してください

<!--
```
case class JsError(
  errors: Seq[(JsPath, Seq[ValidationError])]  
  // the errors is a sequence of JsPath locating the path 
  // where there was an error in the JsValue and validation 
  // errors for this path
) extends JsResult[Nothing]

// ValidationError is a simple message with arguments (which can be mapped on localized messages)
case class ValidationError(message: String, args: Any*)

// To create a JsError, there are a few helpers and for ex:
val errors1 = JsError( __ \ 'isDead, ValidationError("validate.error.missing", "isDead") )
val errors2 = JsError( __ \ 'name, ValidationError("validate.error.missing", "name") )

// Errors are cumulative which is really interesting
scala> val errors = errors1 ++ errors2
errors: JsError(List((/isDead,List(ValidationError(validate.error.missing,WrappedArray(isDead)))), (/name,List(ValidationError(validate.error.missing,WrappedArray(name))))))
```
-->
```
case class JsError(
  errors: Seq[(JsPath, Seq[ValidationError])]
  // errors は、この JsValue のどこにエラーがあったのか、
  // このパスのどこにバリデーションエラーがあったのかを
  // 指し示す JsPath のシーケンスです
) extends JsResult[Nothing]

// ValidationError は、引数を伴った (ローカライズされたメッセージにマッピングできる) メッセージです
case class ValidationError(message: String, args: Any*)

// JsError を作るために、例えば次のようないくつかのヘルパーがあります
val errors1 = JsError( __ \ 'isDead, ValidationError("validate.error.missing", "isDead") )
val errors2 = JsError( __ \ 'name, ValidationError("validate.error.missing", "name") )

// とても興味深いことに、Errors は累積することができます
scala> val errors = errors1 ++ errors2
errors: JsError(List((/isDead,List(ValidationError(validate.error.missing,WrappedArray(isDead)))), (/name,List(ValidationError(validate.error.missing,WrappedArray(name))))))
```

<!--
So what's interesting there is that `JsResult[A]` is a monadic structure and can be used with classic functions of such structures: 
-->
さて、ここで興味深いのは、`JsResult[A]` はモナディックな構造であり、この構造の馴染み深い関数と共に使うことができるということです:

- `flatMap[X](f: A => JsResult[X]): JsResult[X]`  
- `fold[X](invalid: Seq[(JsPath, Seq[ValidationError])] => X, valid: A => X)`  
- `map[X](f: A => X): JsResult[X]`  
- `filter(p: A => Boolean)`  
- `collect[B](otherwise:ValidationError)(p:PartialFunction[A,B]): JsResult[B]` 
- `get: A`

<!--
And some sugar such :
-->
次のような糖衣構文と使うこともできます :

- `asOpt`  
- `asEither`
- … 

<!--
>Please note that **`JsResult[A]` is not just Monadic but Applicative** because it cumulates errors.  
>This cumulative feature makes `JsResult[T]` makes it **not very good to be used with _for comprehension_** because you'll get only the first error and not all. 
-->
>エラーを累積するので、 **`JsResult[A]` はモナディックなだけでなくアプリカティブである** ことにも注目してください。
>この累積する機能のため、`JsResult[T]` を _for 内包表記_ で使うことはあまり良いことではありません。すべてのエラーではなく、最初のひとつだけを取り出すことになるからです。
  
<!--
### Reads[A] has become a validator
-->
### Reads[A] はバリデータに

<!--
As you may understand, using the new `Reads[A]`, you don't only deserialize a JsValue into another structure but you really **validate** the JsValue and retrieve all the validation errors.  
BTW, in `JsValue`, a new function called `validate` has appeared:
-->
ご理解頂いたとおり、新しい `Reads[A]` を使うと、ただ JsValue を別の構造にデシリアライズするだけではなく、実際にその JsValue の **バリデーションを行い** 、そしてすべてのバリデーションエラーを探し出します。
ところで、`JsValue` に `validate` と呼ばれる新しい関数が登場しました:

<!--
```
trait JsValue {
…
  def validate[T](implicit _reads: Reads[T]): JsResult[T] = _reads.reads(this)
  
  // same behavior but it throws a specific RuntimeException JsResultException now
  def as[T](implicit fjs: Reads[T]): T
  // exactly the same behavior has before
  def asOpt[T](implicit fjs: Reads[T]): Option[T]
…
}

// You can now write if you got the right implicit in your scope
val res: JsResult[Creature] = js.validate[Creature])
```
-->
```
trait JsValue {
…
  def validate[T](implicit _reads: Reads[T]): JsResult[T] = _reads.reads(this)

  // 以前と同じように振る舞いますが、具体的な実行時例外 JsResultException を投げるようになりました
  def as[T](implicit fjs: Reads[T]): T
  // 以前とまったく同じように振る舞います
  def asOpt[T](implicit fjs: Reads[T]): Option[T]
…
}

// スコープから正しい implicit を取得できる場合に、このように書けるようになります
val res: JsResult[Creature] = js.validate[Creature])
```

<br/>
<!--
### Manipulating a JsResult[A]
-->
### JsResult[A] の操作

<!--
So when manipulating a `JsResult`, you don't access the value directly and it's preferable to use `map/flatmap/fold` to modify the value.
-->
`JsResult` を操作する際は、値に直接アクセスするのではなく、`map/flatmap/fold` を使って値を変更することが推奨されています。

<!--
```
import play.api.libs.json._

val res: JsResult[Creature] = js.validate[Creature]

// managing the success/error and potentially return something
res.fold(
  valid = { c => println( c ); c.name },
  invalid = { e => println( e ); e }
)

// getting the name directly (using the get can throw a NoSuchElementException if it's a JsError)
val name: JsSuccess[String] = res.map( creature => creature.name ).get

// filtering the result
val name: JsSuccess[String] = res.filter( creature => creature.name == "gremlins" ).get

// a classic Play action
def getNameOnly = Action(parse.json) { request =>
  val json = request.body
  json.validate[Creature].fold(
    valid = ( res => Ok(res.name) ),
    invalid = ( e => BadRequest(e.toString) )
  )
}
```
-->
```
import play.api.libs.json._

val res: JsResult[Creature] = js.validate[Creature]

// 成功/失敗を管理し、何かしらの結果を返します
res.fold(
  valid = { c => println( c ); c.name },
  invalid = { e => println( e ); e }
)

// name を直接取得します (JsResult が JsError の場合、get は NoSuchElementException を投げることができます)
val name: JsSuccess[String] = res.map( creature => creature.name ).get

// 結果をフィルタリングします
val name: JsSuccess[String] = res.filter( creature => creature.name == "gremlins" ).get

// お馴染みの Play アクション
def getNameOnly = Action(parse.json) { request =>
  val json = request.body
  json.validate[Creature].fold(
    valid = ( res => Ok(res.name) ),
    invalid = ( e => BadRequest(e.toString) )
  )
}
```

<!--
### <a name="play2-syntax">Reads interesting new features</a>
-->
### <a name="play2-syntax">Reads の興味深い新機能</a>

<!--
As you know, Json API for Play2.1 was still draft and has evolved since I began writing article part 1/2.  
We have changed a few things since (nothing conceptual, just cosmetics). 
-->
ご存知のとおり、Play2.1 の Json API はまだ草案であり、この記事を書き始めたときからも進化しています。
その後、いくつかの (概念的ではなく、表面的な) 変更が加えられました。

#### `Reads[A <: JsValue] andThen Reads[B]` 
<!--
`andThen` has the classic Scala semantic of function composition : it applies `Reads[A <: JsValue]` on JSON retrieving a JsValue and then applies `Reads[B]` on this JsValue.
-->
`andThen` には、関数合成に関する馴染み深い Scala のセマンティックが含まれています : JSON に `Reads[A <: JsValue]` を適用して JsValue を取り出し、それからこの JsValue に `Reads[B]` を適用します。
<br/>

#### `Reads[A <: JsValue].map(f: A => B): Reads[B]` 
<!--
`map` is the classic and always very useful Scala map function.
-->
`map` は馴染み深く、そしていつでもとても便利な Scala の map 関数です。
<br/>

#### `Reads[A <: JsValue].flatMap(f: A => Reads[B]): Reads[B]` 
<!--
`flatMap` is the classic Scala flatMap function.
-->
`flatMap` は、馴染み深い Scala の flatMap 関数です。

<!--
### Rewriting the Reads[T] with JsResult[A]
-->
### JsResult[A] で Reads[T] を書き直す

<!--
The `Reads[A]` API returning a JsResult, you can't write your `Reads[A]` as before as you must return a JsResult gathering all found errors.  
You could imagine simply compose Reads[T] with flatMap :
-->
検出されたすべてのエラーをかき集めた JsResult を返さなければならないので、JsResult を返す `Reads[A]` API を以前の `Reads[A]` のように書くことはできません。
シンプルに flatMap で Reads[T] を組み立てるところを想像するかもしれません :

<!--
**Following code is WRONG**
-->
**以下のコードは誤りです**

<!--
```
import play.api.libs.json._

// DO NOT USE, WRONG CODE
implicit val creatureReads = new Reads[Creature] {
  def reads(js: JsValue): JsResult[Creature] = {
    (js \ "name").validate[String].flatMap{ name => 
      (js \ "isDead").validate[Boolean].flatMap { isDead =>
      (js \ "weight").validate[Float].map { weight =>
        Creature(name, isDead, weight)
      }
    }
  }
  }
}
```
-->
```
import play.api.libs.json._

// 誤ったコードです。使わないでください
implicit val creatureReads = new Reads[Creature] {
  def reads(js: JsValue): JsResult[Creature] = {
    (js \ "name").validate[String].flatMap{ name =>
      (js \ "isDead").validate[Boolean].flatMap { isDead =>
      (js \ "weight").validate[Float].map { weight =>
        Creature(name, isDead, weight)
      }
    }
  }
  }
}
```

<!--
>Remember the main purpose of `JsResult` is to gather all found errors while validating the JsValue.
-->
>`JsResult` の主な目的は、JsValue のバリデーションを行っている間に見つかったすべてのエラーをかき集めることであることを思い出してください。

<!--
`JsResult.flatMap` is pure monadic function (if you don't know what it is, don't care about it, you can understand without it) implying that the function that you pass to `flatMap()` is called only if the result is a `JsSuccess` else it just returns the `JsError`.  
This means the previous code won't aggregate all errors found during validation and will stop at first error which is exactly what we don't want.
-->
`JsResult.flatMap` は純粋にモナディックな関数 (何のことか分からなくても、理解できるので気にしないでください) なので、`flatMap()` に渡した関数は、その結果が `JsSuccess` であるときのみ呼び出され、そうでない場合はただ `JsError` を返します。
これは、上記のコードはバリデーションを行っている間に見つかったすべてのエラーをかき集めるわけではなく、最初のエラーで止まってしまうことを意味しており、これはまったく望ましくありません。

<!--
Actually, Monad pattern is not good in our case because we are not just composing Reads but we expect combining them following the schema:
-->
実際のところ、ただ Reads を組み立てるだけではなく、以下のようなスキーマに従って結合することを期待しているので、このような場合においてモナドパターンは好ましくありません:

```
Reads[String] AND Reads[Boolean] AND Reads[Float]   
  => Reads[(String, Boolean, Float)]   
     => Reads[Creature]
```

<!--
> So we need something else to be able to combine our Reads and this is the greatest new feature that `Play2.1` brings for JSON :  
> **THE READS combinators with JsPath**
-->
> このため、Reads を結合できるようにする何かが必要であり、これこそが `Play2.1` が JSON のために用意した素晴らしい新機能 : **JsPath を伴う READS コンビネータ** です。
<br/>
> If you want more theoretical aspects about the way it was implemented based on generic functional structures adapted to our needs, you can read this post ["Applicatives are too restrictive, breaking Applicatives and introducing Functional Builders"](http://sadache.tumblr.com/post/30955704987/applicatives-are-too-restrictive-breaking-applicativesfrom) written by [@sadache](https://github.com/sadache)

<!--
## Writing Reads[T] combinators
-->
## Reads[T] コンビネータを書く

<!--
### Minimal import to work with combinators
-->
### コンビネータを使うための最小インポート

<!--
```
// if you need Json structures in your scope
import play.api.libs.json._
// IMPORTANT import this to have the required tools in your scope
import play.api.libs.functional.syntax._
```
-->
```
// スコープ内に Json 構造が必要な場合
import play.api.libs.json._
// 重要。必要なツールをスコープにインポートします
import play.api.libs.functional.syntax._
```

<!--
### Rewriting the Reads[T] with combinators
-->
### コンビネータで Reads[T] を書き直す

<!--
Go directly to the example as practice is often the best :
-->
いきなりサンプルに飛び込んで実践するのが往々にして最良です :

<!--
```
// IMPORTANT import this to have the required tools in your scope
import play.api.libs.json._
import play.api.libs.functional.syntax._

implicit val creatureReads = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float]
)(Creature.apply _)  

// or in a simpler way as case class has a companion object with an apply function
implicit val creatureReads = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float]
)(Creature)  

// or using the operators inspired by Scala parser combinators for those who know them
implicit val creatureReads = (
  (__ \ "name").read[String] ~
  (__ \ "isDead").read[Boolean] ~
  (__ \ "weight").read[Float]
)(Creature)  

```
-->
```
// 重要。必要なツールをスコープにインポートします
import play.api.libs.json._
import play.api.libs.functional.syntax._

implicit val creatureReads = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float]
)(Creature.apply _)

// または、apply 関数を持つコンパニオンオブジェクトと共にケースクラスとするのもシンプルな方法です
implicit val creatureReads = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float]
)(Creature)

// あるいは、Scala のパーサコンビネータを知っている方々は、これに着想を得た演算子を使います
implicit val creatureReads = (
  (__ \ "name").read[String] ~
  (__ \ "isDead").read[Boolean] ~
  (__ \ "weight").read[Float]
)(Creature)

```

<!--
So there is nothing quite complicated, isn't it?
-->
どうでしょう、難しいことは何もありませんよね?

<!--
####`(__ \ "name")` is the `JsPath` where you gonna apply `read[String]`
-->
####`(__ \ "name")` は `read[String]` を適用しようとしている `JsPath` です
<br/>
<!--
####`and` is just an operator meaning `Reads[A] and Reads[B] => Builder[Reads[A ~ B]]`
-->
####`and` は `Reads[A] and Reads[B] => Builder[Reads[A ~ B]]` を意味する演算子に過ぎません
<!--
  - `A ~ B` just means `Combine A and B` but it doesn't suppose the way it is combined (can be a tuple, an object, whatever…)   
  - `Builder` is not a real type but I introduce it just to tell that the operator `and` doesn't create directly a `Reads[A ~ B]` but an intermediate structure that is able to build a `Reads[A ~ B]` or to combine with another `Reads[C]`
-->
  - `A ~ B` は `Combine A and B` を意味しますが、どのように結合するかは前提としません (タプル、オブジェクト、その他どんなものにでもなることができます)
  - `Builder` は実際の型ではありませんが、`and` 演算子は直接 `Reads[A ~ B]` を作るのではなく、`Reads[A ~ B]` を作れたり、または別の `Reads[C]` と結合できたりする中間成果物を作ることを述べるために紹介しています
<br/>

<!--
####`(…)(Creature)` builds a `Reads[Creature]`
  - Remark that:
-->
####`(…)(Creature)` は `Reads[Creature]` を作ります
  - 以下のことに気を付けてください:
  
```
(__ \ "name").read[String] and (__ \ "isDead").read[Boolean] and (__ \ "weight").read[Float]
```
<!--
builds a
-->
これは、以下のものを作ります

```
Builder[Reads[String ~ Boolean ~ Float])]
```

<!--
but you expect a `Reads[Creature]`.  
-->
しかし、期待しているのは `Reads[Creature]` です。
  
<!--
  - So you apply the `Builder[Reads[String ~ Boolean ~ String])]` to the function `Creature.apply = (String, Boolean, Float) => Creature` constructor to finally obtain a `Reads[Creature]`
-->
  - そのため、最終的に `Reads[Creature]` を取得するために、関数 `Creature.apply = (String, Boolean, Float) => Creature` コンストラクタに `Builder[Reads[String ~ Boolean ~ String])]` を適用します

<!--
Try it:
-->
以下を試してみてください:

<!--
```
scala> val js = Json.obj( "name" -> "gremlins", "isDead" -> false, "weight" -> 1.0F)
scala> js.validate[Creature] 
res1: play.api.libs.json.JsResult[Creature] = JsSuccess(Creature(gremlins,false,1.0),) 
// nothing after last comma because the JsPath is ROOT by default

```
-->
```
scala> val js = Json.obj( "name" -> "gremlins", "isDead" -> false, "weight" -> 1.0F)
scala> js.validate[Creature]
res1: play.api.libs.json.JsResult[Creature] = JsSuccess(Creature(gremlins,false,1.0),)
// この JsPath はデフォルトでルートになるので、最後のカンマの後には何もありません

```

<!--
Now what happens if you have an error now?
-->
さて、ここでエラーがあった場合は何が起こるのでしょう?

```
scala> val js = Json.obj( "name" -> "gremlins", "weight" -> 1.0F)
scala> js.validate[Creature] 
res2: play.api.libs.json.JsResult[Creature] = JsError(List((/isDead,List(ValidationError(validate.error.missing-path,WrappedArray())))))

```

<!--
Explicit, isn't it?
-->
分かり易いでしょう?

<!--
### Complexifying the case
-->
### 複雑化された場合

<!--
Ok, I see what you think : what about more complex cases where you have several constraints on a field and embedded Json in Json and recursive classes and whatever…
-->
ええ、あなたの考えていることは分かっています : もっと複雑な場合はどうでしょう。フィールドに複数の制約があって、Json の中に Json が組み込まれていて、再帰的なクラスで…

<!--
Let's imagine our creature:
-->
例に取り上げている生き物について想像してみましょう:

<!--
- is a relatively modern creature having an email and hating email addresses having less than 5 characters for a reason only known by the creature itself. 
- may have 2 favorites data: 
    - 1 String (called "string" in JSON) which shall not be "ni" (because it loves Monty Python too much to accept this) and then to skip the first 2 chars
    - 1 Int (called "number" in JSON) which can be less than 86 or more than 875 (don't ask why, this is creature with a different logic than ours)
- may have friend creatures
- may have an optional social account because many creatures are not very social so this is quite mandatory
-->
- それは email アカウントを持つ比較的モダンな生き物であり、その生き物自身だけが知っている理由によって、五文字より少ない email アドレスを嫌います
- お気に入りデータを二つ持つ場合があります:
    - (それを受け入れるにはモンティ・パイソンが好き過ぎるために) "ni" であってはならず、そして最初の二文字がスキップされる、ひとつの (JSON では "string" と呼ばれる) String
    - 86 より小さいか、または 875 より大きな (なぜかは聞かないでください。我々とは違う論理を持つ生き物なのです) ひとつの (JSON では "number" と呼ばれる) Int
- それには友達がいるかもしれません
- 多くの生き物はあまり社交的ではないので、とても必要なソーシャルアカウントをオプションで持っているかもしれません

<!--
Now the class looks like:
-->
クラスは以下のようになります:

<!--
```
case class Creature(
  name: String, 
  isDead: Boolean, 
  weight: Float,
  email: String, // email format and minLength(5)
  favorites: (String, Int), // the stupid favorites
  friends: List[Creature] = Nil, // yes by default it has no friend
  social: Option[String] = None // by default, it's not social
)
```
-->
```
case class Creature(
  name: String,
  isDead: Boolean,
  weight: Float,
  email: String, // email フォーマットかつ minLength(5)
  favorites: (String, Int), // 間抜けなお気に入りデータ
  friends: List[Creature] = Nil, // ええ、デフォルトでは友達がいません
  social: Option[String] = None // デフォルトでは社交的ではありません
)
```

<!--
`Play2.1` provide lots of generic Reads helpers:
-->
`Play2.1` は多くの総称的な Reads ヘルパーを提供しています:

<!--
- `JsPath.read[A](implicit reads:Reads[A])` can be passed a custom `Reads[A]` which is applied to the JSON content at this JsPath. So with this property, you can compose hierarchically `Reads[T]` which corresponds to JSON tree structure.
- `JsPath.readNullable` allows `Reads[Option[T]]` with missing or empty field
- `Reads.email` which validates the String has email format  
- `Reads.minLength(nb)` validates the minimum length of a String
- `Reads[A] or Reads[A] => Reads[A]` operator is a classic `OR` logic operator
- `Reads[A] keepAnd Reads[B] => Reads[A]` is an operator that tries `Reads[A]` and `Reads[B]` but only keeps the result of `Reads[A]` (For those who know Scala parser combinators `keepAnd == <~` )
- `Reads[A] andKeep Reads[B] => Reads[B]` is an operator that tries `Reads[A]` and `Reads[B]` but only keeps the result of `Reads[B]` (For those who know Scala parser combinators `andKeep == ~>` )
-->
- `JsPath.read[A](implicit reads:Reads[A])` には、この JsPath にある JSON の内容に適用される独自の `Reads[A]` を渡すことができます。この性質により、JSON ツリー構造に対応する階層的な `Reads[T]` を組み立てることができます。
- `JsPath.readNullable` は、見つからないか、空のフィールドを持つ `Reads[Option[T]]` を許容します
- `Reads.email` は、文字列が email フォーマットであることを検証します
- `Reads.minLength(nb)` は、文字列長の最小値を検証します
- `Reads[A] or Reads[A] => Reads[A]` 演算子は、馴染み深い `OR` 論理演算子です
- `Reads[A] keepAnd Reads[B] => Reads[A]` は、`Reads[A]` と `Reads[B]` の実行を試みますが、`Reads[A]` の結果のみを保持します (Scala のパーサコンビネータ `keepAnd == <~` を知っている人のためのものです)
- `Reads[A] andKeep Reads[B] => Reads[B]` は、`Reads[A]` と `Reads[B]` の実行を試みますが、`Reads[B]` の結果のみを保持します (Scala のパーサコンビネータ `andKeep == ~>` を知っている人のためのものです)

<!--
```
// import just Reads helpers in scope
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError

// defines a custom reads to be reused
// a reads that verifies your value is not equal to a give value
def notEqualReads[T](v: T)(implicit r: Reads[T]): Reads[T] = Reads.filterNot(ValidationError("validate.error.unexpected.value", v))( _ == v )

def skipReads(implicit r: Reads[String]): Reads[String] = r.map( _.substring(2) )

implicit val creatureReads: Reads[Creature] = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float] and
  (__ \ "email").read(email keepAnd minLength[String](5)) and
  (__ \ "favorites").read( 
    (__ \ "string").read[String]( notEqualReads("ni") andKeep skipReads ) and
    (__ \ "number").read[Int]( max(86) or min(875) )
    tupled
  ) and
  (__ \ "friends").lazyRead( list[Creature](creatureReads) ) and
  (__ \ "social").readNullable[String]
)(Creature)  
```
-->
```
// Reads ヘルパーをスコープにインポートします
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError

// 再利用するために独自の Reads を定義します
// この Reads は、文字列が与えられた文字列と等しくないことを検証します
def notEqualReads[T](v: T)(implicit r: Reads[T]): Reads[T] = Reads.filterNot(ValidationError("validate.error.unexpected.value", v))( _ == v )

def skipReads(implicit r: Reads[String]): Reads[String] = r.map( _.substring(2) )

implicit val creatureReads: Reads[Creature] = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float] and
  (__ \ "email").read(email keepAnd minLength[String](5)) and
  (__ \ "favorites").read(
    (__ \ "string").read[String]( notEqualReads("ni") andKeep skipReads ) and
    (__ \ "number").read[Int]( max(86) or min(875) )
    tupled
  ) and
  (__ \ "friends").lazyRead( list[Creature](creatureReads) ) and
  (__ \ "social").readNullable[String]
)(Creature)
```

<!--
Many things above can be understood very logically but let's explain a bit:
-->
上記の多くのことは論理的に理解できますが、ちょっと説明してみましょう:

#### `(__ \ "email").read(email keepAnd minLength[String](5))`
<!--
- As explained previously, `(__ \ "email").read(…)` applies the `Reads[T]` passed in parameter to function `read` at the given JsPath `(__ \ "email")`
- `email keepAnd minLength[String](5) => Reads[String]` is a Js validator that verifies JsValue:
    1. is a String : `email: Reads[String]` so no need to specify type here
    2. has email format
    3. has min length of 5 (we precise the type here because minLength is a generic `Reads[T]`)  
- Why not `email and minLength[String](5)`? because, as explained previously, it would generate a `Builder[Reads[(String, String)]]` whereas you expect a `Reads[String]`. The `keepAnd` operator (aka `<~`) does what we expect: it validates both sides but if succeeded, it keeps only the result on left side.
-->
- 以前に説明した通り、`(__ \ "email").read(…)` は `read` 関数の引数に渡された `Reads[T]` を、与えられた JsPath `(__ \ "email")` に適用します。
- `email keepAnd minLength[String](5) => Reads[String]` は JsValue を検証する Js バリデータです。
    1. 文字列であること : `email: Reads[String]` なので、ここでは型は指定していません
    2. email フォーマットであること
    3. 少なくとも 5 文字以上であること (minLength は総称的な `Reads[T]` なので、ここでは厳格に型を指定しています)
- なぜ `email and minLength[String](5)` としないのでしょうか? 以前に説明した通り、これは `Reads[String]` を期待しているところに `Builder[Reads[(String, String)]]` を生成してしまいます。(`<~` としても知られる) `keepAnd` 演算子は期待通りのことを行います: 両辺の検証を行いますが、成功した場合、左辺の結果のみを保持します。
<br/>

#### `notEqualReads("ni") andKeep skipReads`

<!--
- No need to write `notEqualReads[String]("ni")` because `String` type is inferred from `(__ \ "knight").read[String]` (the power of Scala typing engine)
- `skipReads` is a customReads that skips the first 2 chars
- `andKeep` operator (aka `~>`) is simple to undestand : it validates the left and right side and if both succeeds, only keeps the result on right side. In our case, only the result of `skipReads` is kept and not the result of `notEqualReads`.
-->
- `String` 型であることは `(__ \ "knight").read[String]` から推論されるので、`notEqualReads[String]("ni")` と書く必要はありません (Scala の型エンジンの威力です)
- `skipReads` は最初の二文字をスキップする独自の Reads です
- (`~>` としても知られる) `andKeep` 演算子は、シンプルに理解することができます : 左辺と右辺の検証を行い、両方とも成功した場合は右辺の結果のみを保持します。今回の場合、`notEqualReads` の結果ではなく、`skipReads` の結果のみを保持します。
<br/>

#### `max(86) or min(875)`

<!--
- Nothing to explain there, isn't it ? `or` is the classic `OR`logic operator, nothing else
-->
- 説明することは何もありませんよね? `or` は馴染み深い `OR` 論理演算子以外の何物でもありません
<br/>

#### `(__ \ "favorites").read(…)`

```
    (__ \ "string").read[String]( notEqualReads("ni") andKeep notEqualReads("swallow") ) and
    (__ \ "number").read[Int]( max(86) or min(875) )
    tupled
```

<!--
- Remember that `(__ \ "string").read[String](…) and (__ \ "number").read[Int](…) => Builder[Reads[(String, Int)]]`  
- What means `tupled` ? 
`Builder[Reads[(String, Int)]]` can be used with a case class `apply` function to build the `Reads[Creature]` for ex. But it provides also `tupled` which is quite easy to understand : it _"tuplizes"_ your Builder: `Builder[Reads[(A, B)]].tupled => Reads[(A, B)]` 
- Finally `(__ \ "favorites").read(Reads[(String, Int)]` will validate a `(String, Int)` which is the expected type for field `favorites`
-->
- `(__ \ "string").read[String](…) and (__ \ "number").read[Int](…) => Builder[Reads[(String, Int)]]` を思い出してください
- `tupled` は何を意味するのでしょう?
`Builder[Reads[(String, Int)]]` は、例えばケースクラスの `Reads[Creature]` を作る `apply` 関数と共に使うことができます。しかし、まったく簡単に理解できる `tupled` も提供されています : これは Builder を _"タプル化"_ します: `Builder[Reads[(A, B)]].tupled => Reads[(A, B)]`
- 最終的に `(__ \ "favorites").read(Reads[(String, Int)]` は `favorites` フィールドに期待されている `(String, Int)` を検証します
<br/>

#### `(__ \ "friend").lazyRead( list[Creature](creatureReads) )`

<!--
This is the most complicated line in this code. But you can understand why: the `friend` field is recursive on the `Creature` class itself so it requires a special treatment.
-->
これがこのコードでもっとも複雑な行です。しかし、なぜ複雑なのかは理解できます。`friend` は `Creature` クラス自身において再帰的なフィールドであり、特別な取り扱いが必要です。

<!--
- `list[Creature](…)` creates a `Reads[List[Creature]]`  
- `list[Creature](creatureReads)` passes explicitly `creatureReads` as an argument because it's recursive and Scala requires that to resolve it. Nothing too complicated…
- `(__ \ "friend").lazyRead[A](r: => Reads[A]))` : `lazyRead` expects a `Reads[A]` value _passed by name_ to allow the type recursive construction. This is the only refinement that you must keep in mind in this very special recursive case.
-->
- `list[Creature](…)` は `Reads[List[Creature]]` を作ります
- `list[Creature](creatureReads)` は再帰的であり、Scala がこれを解決するために必要なので、明示的に `creatureReads` を引数として渡しています。難し過ぎることはありません…
- `(__ \ "friend").lazyRead[A](r: => Reads[A]))` : `lazyRead` は、再帰的な構造を受け入れるために、`Reads[A]` の値が _名前渡し_ であることを期待します。これが、このとても特別な再帰的なケースにおいて頭に入れておかなければならない唯一の工夫です。
<br/>

#### `(__ \ "social").readNullable[String]`

<!--
Nothing quite complicated to understand: we need to read an option and `readNullable` helps in doing this.
-->
理解するのに複雑なところは何もありません: option を読み込む必要があり、`readNullable` はこれを手助けしてくれます。

<!--
Now we can use this `Reads[Creature]`
-->
これで、この `Reads[Creature]` を使うことができます

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

val gizmojs = Json.obj( 
  "name" -> "gremlins", 
  "isDead" -> false, 
  "weight" -> 1.0F,
  "email" -> "gizmo@midnight.com",
  "favorites" -> Json.obj("string" -> "alpha", "number" -> 85),
  "friends" -> Json.arr(),
  "social" -> "@gizmo"
)

scala> val gizmo = gizmojs.validate[Creature] 
gizmo: play.api.libs.json.JsResult[Creature] = JsSuccess(Creature(gremlins,false,1.0,gizmo@midnight.com,(pha,85),List(),Some(@gizmo)),)

val shaunjs = Json.obj( 
  "name" -> "zombie", 
  "isDead" -> true, 
  "weight" -> 100.0F,
  "email" -> "shaun@dead.com",
  "favorites" -> Json.obj("string" -> "brain", "number" -> 2),
  "friends" -> Json.arr( gizmojs))

scala> val shaun = shaunjs.validate[Creature] 
shaun: play.api.libs.json.JsResult[Creature] = JsSuccess(Creature(zombie,true,100.0,shaun@dead.com,(ain,2),List(Creature(gremlins,false,1.0,gizmo@midnight.com,(alpha,85),List(),Some(@gizmo))),None),)

val errorjs = Json.obj( 
  "name" -> "gremlins", 
  "isDead" -> false, 
  "weight" -> 1.0F,
  "email" -> "rrhh",
  "favorites" -> Json.obj("string" -> "ni", "number" -> 500),
  "friends" -> Json.arr()
)

scala> errorjs.validate[Creature] 
res0: play.api.libs.json.JsResult[Creature] = JsError(List((/favorites/string,List(ValidationError(validate.error.unexpected.value,WrappedArray(ni)))), (/email,List(ValidationError(validate.error.email,WrappedArray()), ValidationError(validate.error.minlength,WrappedArray(5)))), (/favorites/number,List(ValidationError(validate.error.max,WrappedArray(86)), ValidationError(validate.error.min,WrappedArray(875))))))
```

<!--
## Reads[A] other features
-->
## Reads[A] のその他の機能

### `(Reads[A] and Reads[B]).tupled: Reads[(A, B)]` 
<!--
This useful to create `Reads[TupleX]`
-->
これは `Reads[TupleX]` を作るのに便利です

```
(
  (__ \ 'field1).read[String] and 
  (__ \ 'field2).read[Int]
).tupled : Reads[(String, Int)]
```

<!--
It also works with JsArray and indexes
-->
インデックスが指定された JsArray と使うこともできます

```
(
  (__(0)).read[String] and 
  (__(1)).read[Int]
).tupled : Reads[(String, Int)]
```


### `(Reads[A1 <: A] and Reads[A2 <: A]).reduce(implicit reducer: Reducer[A, B]): Reads[B]` 
<!--
Useful to read several parts of a JSON and then aggregate them.
This one requires an implicit Reducer/Monoid.
We provide the ones for `JsObject` and `JsArray`.
-->
JSON のいくつかの部分を読み込んで集約するのに便利です。
これには implicit な Reducer/Monoid が必要です。
`JsObject` と `JsArray` 向けのものが提供されています。

<!--
Here are a few examples using Json transformers presented in next paragraph:
-->
以下は、次の段落で登場する Json トランスフォーマーのいくつかの使用例です:

<!--
#### **Reduce a JsObject (copies branches and aggregates them in a JsObject)**
-->
#### **JsObject を Reduce する (ブランチをコピーして、ひとつの JsObuject に集約する)**

```
(
  (__ \ 'field1).json.pickBranch[JsString] and 
  (__ \ 'field2).json.pickBranch[JsNumber]
).reduce : Reads[JsObject]
```

<!--
#### **Reduce a JsArray (copies leaf values and aggregates them in a JsArray)**
-->
#### **JsArray を Reduce する (リーフをコピーして、ひとつの JsArray に集約する)**

```
(
  (__ \ 'field1).json.pick[JsString] and 
  (__ \ 'field2).json.pick[JsNumber]
).reduce : Reads[JsArray]
```


<br/>
<br/>
<!--
# <a name="writes">Writes[T] hasn't changed (except combinators)</a>
-->
# <a name="writes">Writes[T] は (コンビネータ以外) 変更なし</a>

<!--
## <a name="writes-2_0">Writes in Play2.0.x</a>
-->
## <a name="writes-2_0">Play2.0.x における Writes</a>

<!--
Do you remember how you had to write a Json `Writes[T]` in `Play2.0.x` ?  
You had to override the `writes` function.  
-->
`Play2.0.x` では Json `Writes[T]` をどのように書かなければならなかった覚えていますか?
`writes` 関数をオーバーライドする必要がありました。

<!--
```
trait Writes[-A] {
  self =>
  /**
   * Convert the object into a JsValue
   */
  def writes(o: A): JsValue
}
```
-->
```
trait Writes[-A] {
  self =>
  /**
   * オブジェクトを JsValue に変換する
   */
  def writes(o: A): JsValue
}
```

<!--
Take the same simple case class we used in Part 1:
-->
Part1 で使ったものと同じシンプルなケースクラスを取り上げましょう:
```
case class Creature(
  name: String, 
  isDead: Boolean, 
  weight: Float
)
```

<!--
In `Play2.0.x`, you would write your `Writes[Creature]` as following (using new Json syntax to re-show it even if it didn't exist in Play2.0.x ;) ):
-->
`Play2.0.x` では `Writes[Creature]` を以下のように書いていたことでしょう (Play2.0.x には存在していませんでしたが、もう一度紹介するために新しい Json 文法を使っています ;) ):

```
import play.api.libs.json._

implicit val creatureWrites = new Writes[Creature] {
  def writes(c: Creature): JsValue = {
    Json.obj(
      "name" -> c.name,
      "isDead" -> c.isDead,
      "weight" -> c.weight
    )
  }
}

scala> val gizmo = Creature("gremlins", false, 1.0F)
scala> val gizmojs = Json.toJson(gizmo)
gizmojs: play.api.libs.json.JsValue = {"name":"gremlins","isDead":false,"weight":1.0}

```

<!--
## <a name="writes-2_1">Writes in Play2.1.x</a>
-->
## <a name="writes-2_1">Play2.1.x における Writes</a>

<!--
No suspense to be kept: **in Play2.1, you write Writes exactly in the same way** :D
-->
不安に思うことはありません: **Play2.1 では、まったく同じ方法で Writes を書きます** :D

<!--
So what's the difference?  
As presented in Part 1, `Reads` could be combined using simple logical operators.  
Using functional Scala power, we were able to **provide combinators for `Writes[T]`**.
-->
それでは何が違うのでしょう?
Part1 で述べられた通り、`Reads` はシンプルな論理演算子を使って結合することができました。
Scala の関数型の力を使うことで、**`Writes[T]` コンビネータを提供** できるようになりました。

> If you want more theoretical aspects about the way it was implemented based on generic functional structures adapted to our needs, you can read this post ["Applicatives are too restrictive, breaking Applicatives and introducing Functional Builders"](http://sadache.tumblr.com/post/30955704987/applicatives-are-too-restrictive-breaking-applicativesfrom) written by [@sadache](https://github.com/sadache)

<!--
## <a name="writes-combined">Writes main change: *combinators*</a>
-->
## <a name="writes-combined">Writes の主な変更点: *コンビネータ*</a>

<!--
Once again, code first: re-writing previous `Writes[T]` using combinators.
-->
再びコードから始めましょう: 以前の `Writes[T]` をコンビネータを使って書き直します。

<!--
```
// IMPORTANT import this to have the required tools in your scope
import play.api.libs.json._
// imports required functional generic structures
import play.api.libs.functional.syntax._

implicit val creatureWrites = (
  (__ \ "name").write[String] and
  (__ \ "isDead").write[Boolean] and
  (__ \ "weight").write[Float]
)(unlift(Creature.unapply))

// or using the operators inspired by Scala parser combinators for those who know them
implicit val creatureWrites = (
  (__ \ "name").write[String] ~
  (__ \ "isDead").write[Boolean] ~
  (__ \ "weight").write[Float]
)(unlift(Creature.unapply))

scala> val c = Creature("gremlins", false, 1.0F)
scala> val js = Json.toJson(c)
js: play.api.libs.json.JsValue = {"name":"gremlins","isDead":false,"weight":1.0}

```
-->
```
// 重要。必要なツールをスコープにインポートします
import play.api.libs.json._
// 必要な関数型の総称的な構造をインポートします
import play.api.libs.functional.syntax._

implicit val creatureWrites = (
  (__ \ "name").write[String] and
  (__ \ "isDead").write[Boolean] and
  (__ \ "weight").write[Float]
)(unlift(Creature.unapply))

// あるいは、Scala のパーサコンビネータを知っている人は、これに着想を得た演算子を使います
implicit val creatureWrites = (
  (__ \ "name").write[String] ~
  (__ \ "isDead").write[Boolean] ~
  (__ \ "weight").write[Float]
)(unlift(Creature.unapply))

scala> val c = Creature("gremlins", false, 1.0F)
scala> val js = Json.toJson(c)
js: play.api.libs.json.JsValue = {"name":"gremlins","isDead":false,"weight":1.0}

```

<!--
It looks exactly like `Reads[T]` except a few things, isn't it?  
Let's explain a bit (by copying Reads article changing just a few things… I'm lazy ;)):
-->
いくつかの点を除いて、`Reads[T]` にとてもよく似ているでしょう?
ちょっと説明してみましょう (Reads の記事をコピーしてちょっとだけ変更して… 面倒くさがりなんです ;)):

####`import play.api.libs.json.Writes._` 
<!--
It imports only the required stuff for `Writes[T]` without interfering with other imports.
-->
その他のインポートに影響するものを除いて、`Writes[T]` に必要なものだけをインポートします。
<br/>

####`(__ \ "name").write[String]`
<!--
You apply `write[String]` on this JsPath (exactly the same as `Reads`)
-->
この JsPath に `write[String]` を適用します (`Reads` とまったく同じです)
<br/>

<!--
####`and` is just an operator meaning `Writes[A] and Writes[B] => Builder[Writes[A ~ B]]`
-->
####`and` は `Writes[A] and Writes[B] => Builder[Writes[A ~ B]]` を意味する演算子に過ぎません
<!--
  - `A ~ B` just means `Combine A and B` but it doesn't suppose the way it is combined (can be a tuple, an object, whatever…)
  - `Builder` is not a real type but I introduce it just to tell that the operator `and` doesn't create directly a `Writes[A ~ B]` but an intermediate structure that is able to build a `Writes[A ~ B]` or to combine with another `Writes[C]`
-->
  - `A ~ B` は `Combine A and B` を意味しますが、どのように結合するかは前提としません (タプル、オブジェクト、その他どんなものにでもなることができます)
  - `Builder` は実際の型ではありませんが、`and` 演算子は直接 `Writes[A ~ B]` を作るのではなく、`Writes[A ~ B]` を作れたり、または別の `Writes[C]` と結合できたりする中間成果物を作ることを述べるために紹介しています
<br/>
   
<!--
####`(…)(unlift(Creature.unapply))` builds a `Writes[Creature]`
  - Remark that:
-->
####`(…)(unlift(Creature.unapply))` は `Writes[Creature]` を作ります
  - 以下のことに気を付けてください:
```
(__ \ "name").write[String] and (__ \ "isDead").write[Boolean] and (__ \ "weight").write[Float]` 
```

builds a `Builder[Writes[String ~ Boolean ~ Float])]` whereas you want a `Writes[Creature]`. 
  - So you apply the `Builder[Writes[String ~ Boolean ~ String])]` to a function `Creature => (String, Boolean, Float)` to finally obtain a `Writes[Creature]`.  
Please note that it may seem a bit strange to provide `Creature => (String, Boolean, Float)` to obtain a `Writes[Creature]` from a `Builder[Writes[String ~ Boolean ~ String])]` but it's due to the contravariant nature of `Writes[-T]`.
  - We have `Creature.unapply` but its signature is `Creature => Option[(String, Boolean, Float)]` so we `unlift` it to obtain `Creature => (String, Boolean, Float)`.
<br/>

<!--
>The only thing you have to keep in mind is this `unlift` call which might not be natural at first sight!
-->
>気に留めておかなければならない唯一のことは、この `unlift` 呼び出しが自然でないように見えるのは最初だけということです!
  
<!--
As you can deduce by yourself, the `Writes[T]` is far easier than the `Reads[T]` case because when writing, it doesn't try to validate so there is no error management at all.  
-->
推測されている通り、書き込みの際にはバリデーションを行わず、エラー処理がまったく無いため、`Writes[T]` は `Reads[T]` よりずっと簡単です。

<!--
Moreover, due to this, you have to keep in mind that operators provided for `Writes[T]` are not as rich as for `Reads[T]`. Do you remind `keepAnd` and `andKeep` operators?  They don't have any meaning for `Writes[T]`. When writing `A~B`, you write `A and B` but not `only A or only B`. So `and` is the only operators provided for `Writes[T]`.
-->
さらに、このため `Writes[T]` に提供されている演算子は `Reads[T]` ほど豪華でないことを気に留めておかなければなりません。`keepAnd` と `andKeep` を覚えていますか? これらは `Writes[T]` において何の意味もありません。`A~B` を書き込むときは、`A and B` を書くのであって、`only A or only B` ではありません。このため、`and` がただ一つ `Writes[T]` に提供されている演算子です。
 

<!--
### Complexifying the case
-->
### 複雑化された場合

<!--
Let's go back to our more complex sample used in end of Part1.
Remember that we had imagined that our creature was modelled as following:
-->
Part1 で使った、もっと複雑な例に立ち返ってみましょう。
以下のようにモデリングした生き物を想像したことを思い出してください:

<!--
```
case class Creature(
  name: String, 
  isDead: Boolean, 
  weight: Float,
  email: String, // email format and minLength(5)
  favorites: (String, Int), // the stupid favorites
  friends: List[Creature] = Nil, // yes by default it has no friend
  social: Option[String] = None // by default, it's not social
)
```
-->
```
case class Creature(
  name: String,
  isDead: Boolean,
  weight: Float,
  email: String, // email フォーマットかつ minLength(5)
  favorites: (String, Int), // 間抜けなお気に入りデータ
  friends: List[Creature] = Nil, // ええ、デフォルトでは友達がいません
  social: Option[String] = None // デフォルトでは社交的ではありません
)
```

<!--
Let's write corresponding `Writes[Creature]`
-->
これに対応する `Writes[Creature]` を書いてみましょう。

<!--
```
// IMPORTANT import this to have the required tools in your scope
import play.api.libs.json._
// imports required functional generic structures
import play.api.libs.json.functional.syntax._

implicit val creatureWrites: Writes[Creature] = (
  (__ \ "name").write[String] and
  (__ \ "isDead").write[Boolean] and
  (__ \ "weight").write[Float] and
  (__ \ "email").write[String] and
  (__ \ "favorites").write( 
    (__ \ "string").write[String] and
    (__ \ "number").write[Int]
    tupled
  ) and
  (__ \ "friends").lazyWrite(Writes.traversableWrites[Creature](creatureWrites)) and
  (__ \ "social").write[Option[String]]
)(unlift(Creature.unapply))

val gizmo = Creature("gremlins", false, 1.0F, "gizmo@midnight.com", ("alpha", 85), List(), Some("@gizmo"))
val gizmojs = Json.toJson(gizmo)
gizmojs: play.api.libs.json.JsValue = {"name":"gremlins","isDead":false,"weight":1.0,"email":"gizmo@midnight.com","favorites":{"string":"alpha","number":85},"friends":[],"social":"@gizmo"}

val zombie = Creature("zombie", true, 100.0F, "shaun@dead.com", ("ain", 2), List(gizmo), None)
val zombiejs = Json.toJson(zombie)
zombiejs: play.api.libs.json.JsValue = {"name":"zombie","isDead":true,"weight":100.0,"email":"shaun@dead.com","favorites":{"string":"ain","number":2},"friends":[{"name":"gremlins","isDead":false,"weight":1.0,"email":"gizmo@midnight.com","favorites":{"string":"alpha","number":85},"friends":[],"social":"@gizmo"}],"social":null

```
-->
```
// 重要。必要なツールをスコープにインポートします
import play.api.libs.json._
// 必要な関数型の総称的な構造をインポートします
import play.api.libs.json.functional.syntax._

implicit val creatureWrites: Writes[Creature] = (
  (__ \ "name").write[String] and
  (__ \ "isDead").write[Boolean] and
  (__ \ "weight").write[Float] and
  (__ \ "email").write[String] and
  (__ \ "favorites").write(
    (__ \ "string").write[String] and
    (__ \ "number").write[Int]
    tupled
  ) and
  (__ \ "friends").lazyWrite(Writes.traversableWrites[Creature](creatureWrites)) and
  (__ \ "social").write[Option[String]]
)(unlift(Creature.unapply))

val gizmo = Creature("gremlins", false, 1.0F, "gizmo@midnight.com", ("alpha", 85), List(), Some("@gizmo"))
val gizmojs = Json.toJson(gizmo)
gizmojs: play.api.libs.json.JsValue = {"name":"gremlins","isDead":false,"weight":1.0,"email":"gizmo@midnight.com","favorites":{"string":"alpha","number":85},"friends":[],"social":"@gizmo"}

val zombie = Creature("zombie", true, 100.0F, "shaun@dead.com", ("ain", 2), List(gizmo), None)
val zombiejs = Json.toJson(zombie)
zombiejs: play.api.libs.json.JsValue = {"name":"zombie","isDead":true,"weight":100.0,"email":"shaun@dead.com","favorites":{"string":"ain","number":2},"friends":[{"name":"gremlins","isDead":false,"weight":1.0,"email":"gizmo@midnight.com","favorites":{"string":"alpha","number":85},"friends":[],"social":"@gizmo"}],"social":null

```

<!--
You can see that it's quite straightforward. it's far easier than `Reads[T]` as there are no special operator.
Here are the few things to explain:
-->
とても単純であることが分かると思います。特別な演算子が無いので、`Reads[T]` よりはるかに簡単です。
いくつか説明します:

##### `(__ \ "favorites").write(…)`

```
  (__ \ "string").write[String] and
  (__ \ "number").write[Int]
  tupled
```
<!--
- Remember that `(__ \ "string").write[String](…) and (__ \ "number").write[Int](…) => Builder[Writes[String ~ Int]]`  
- What means `tupled` ? as for `Reads[T]`, it _"tuplizes"_ your Builder: `Builder[Writes[A ~ B]].tupled => Writes[(A, B)]`
-->
- `(__ \ "string").write[String](…) and (__ \ "number").write[Int](…) => Builder[Writes[String ~ Int]]` を思い出してください
- `tupled` は何を意味するのでしょう? `Reads[T]` のときと同様に、これは Builder を _"タプル化"_ します: `Builder[Writes[A ~ B]].tupled => Writes[(A, B)]`

<br/>
##### `(__ \ "friend").lazyWrite(Writes.traversableWrites[Creature](creatureWrites))`

<!--
It's the symmetric code for `lazyRead` to treat recursive field on `Creature` class itself:
-->
これは `Creature` クラス自身の再帰的なフィールドを取り扱うための、`lazyRead` の対称となるコードです。

<!--
- `Writes.traversableWrites[Creature](creatureWrites)` creates a `Writes[Traversable[Creature]]` passing the `Writes[Creature]` itself for recursion (please note that a `list[Creature]`should appear very soon ;))
- `(__ \ "friends").lazyWrite[A](r: => Writes[A]))` : `lazyWrite` expects a `Writes[A]` value _passed by name_ to allow the type recursive construction. This is the only refinement that you must keep in mind in this very special recursive case.
-->
- `Writes.traversableWrites[Creature](creatureWrites)` は、再帰のために `Writes[Creature]` 自身を渡して `Writes[Traversable[Creature]]` を作ります (間もなく `list[Creature]` が登場するので、覚えておいてください ;))
- `(__ \ "friends").lazyWrite[A](r: => Writes[A]))` : `lazyWrite` は、再帰的な構造を受け入れるために、`Writes[A]` の値が _名前渡し_ であることを期待します。これが、このとても特別な再帰的なケースにおいて頭に入れておかなければならない唯一の工夫です。

<!--
> FYI, you may wonder why `Writes.traversableWrites[Creature]: Writes[Traversable[Creature]]` can replace `Writes[List[Creature]]`?  
> This is because `Writes[-T]` is contravariant meaning: if you can write a `Traversable[Creature]`, you can write a `List[Creature]` as `List` inherits `Traversable` (relation of inheritance is reverted by contravariance).
-->
> ちなみに、`Writes.traversableWrites[Creature]: Writes[Traversable[Creature]]` を `Writes[List[Creature]]` に置き換えられることを不思議に思うかもしれませんね?
> これは、`Writes[-T]` が反変的な意味をもつためです。`Traversable[Creature]` と書けるのであれば、`Traversable` を継承する `List` として `List[Creature]` を書くことができます (継承の関連は、反変性によって取り消されます) 。

<!--
## Writes[A] other features
-->
## Writes[A] その他の機能

### `Writes[A].contramap( B => A ): Writes[B]` 

<!--
`Writes[A]` is a contravariant functor that can be _contramapped_.
So you must give a function `B => A` to transform into a `Writes[B]`.
-->
`Writes[A]` は _contramap_ できる反変的な要素です。
このため、`Writes[B]` に変換するための関数 `B => A` を与えてやらなければなりません。

<!--
For example:
-->
例:

```
scala> case class Person(name: String)
defined class Person

scala> __.write[String].contramap( (p: Person) => p.name )
res5: play.api.libs.json.OWrites[Person] = play.api.libs.json.OWrites$$anon$2@61df9fa8
```

### `(Writes[A] and Writes[B]).tupled: Writes[(A, B)]` 
<!--
This useful to create `Writes[TupleX]`
-->
`Writes[TupleX]` を作るのに便利です。

```
(
  (__ \ 'field1).write[String] and 
  (__ \ 'field2).write[Int]
).tupled : Writes[(String, Int)]
```

<!--
**Known limitation** please note that the following doesn't work: it compiles but it will break at runtime as Writes combinators only know how to generate JsObject but not JsArray.
-->
**既知の制限** 以下は動作しないことに気を付けてください: Write コンビネータ は JsObject の生成方法しか知らず、JsArray の生成方法は知らないため、以下はコンパイルされますが、実行時に落ちます。

<!--
```
// BE CAREFUL: IT COMPILES BUT BREAKS AT RUNTIME
(
  (__(0)).write[String] and 
  (__(1)).write[Int]
).tupled : Writes[(String, Int)]
```
-->
```
// 注意: コンパイルされますが、実行時に落ちます
(
  (__(0)).write[String] and
  (__(1)).write[Int]
).tupled : Writes[(String, Int)]
```

### `(Writes[A1 <: A] and Writes[A2 <: A]).join: Writes[A]` 
<!--
Useful to write the same value in several branches.
-->
複数のブランチに同じ値を書き込むのに便利です。

<!--
For example:
-->
例えば:

<!--
```
// Please note you must give the type of resulting Writes
scala> val jsWrites: Writes[JsString] = (
     |   (__ \ 'field1).write[JsString] and 
     |   (__ \ 'field2).write[JsString]
     | ).join
jsWrites: play.api.libs.json.Writes[play.api.libs.json.JsString] = play.api.libs.json.OWrites$$anon$2@732db69a

scala> jsWrites.writes(JsString("toto"))
res3: play.api.libs.json.JsObject = {"field1":"toto","field2":"toto"}
```
-->
```
// Write の結果の型を与えなければならないことに注意してください
scala> val jsWrites: Writes[JsString] = (
     |   (__ \ 'field1).write[JsString] and
     |   (__ \ 'field2).write[JsString]
     | ).join
jsWrites: play.api.libs.json.Writes[play.api.libs.json.JsString] = play.api.libs.json.OWrites$$anon$2@732db69a

scala> jsWrites.writes(JsString("toto"))
res3: play.api.libs.json.JsObject = {"field1":"toto","field2":"toto"}
```

<!--
## <a name="format">What about combinators for Format?</a>
-->
## <a name="format">Format 用のコンビネータについて</a>

<!--
Remember in Play2.1, there was a feature called `Format[T] extends Reads[T] with Writes[T]`.  
It mixed `Reads[T]` and `Writes[T]` together to provide serialization/deserialization at the same place.
-->
Play2.1 には、`Format[T] extends Reads[T] with Writes[T]` と呼ばれる機能がありました。
これは、同じ場所にシリアライズ/デシリアライズを提供するために、`Reads[T]` と `Writes[T]` をまとめてミックスしました。

<!--
Play2.1 provide combinators for `Reads[T]` and `Writes[T]`. What about combinators for `Format[T]` ?
-->
Play2.1 は `Reads[T]` と `Writes[T]` のコンビネータを提供します。`Format[T]` のコンビネータについてはどうでしょう?

<!--
Let's go back to our very simple sample:
-->
とてもシンプルな例に立ち返りましょう:

```
case class Creature(
  name: String, 
  isDead: Boolean, 
  weight: Float
)
```

<!--
Here is how you write the `Reads[Creature]`:
-->
`Reads[Creature]` は、以下のように書きます:

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

val creatureReads = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float]
)(Creature)  
```

<!--
>Please remark that I didn't use `implicit` so that there is no implicit `Reads[Creature]` in the context when I'll define `Format[T]`
-->
>`implicit` を使っていないので、`Format[T]` を定義する際はコンテクスト中に暗黙の `Reads[Creature]` が存在しないことに気を付けてください

<!--
Here is how you write the `Writes[Creature]`:
-->
`Writes[Creature]` は、以下のように書きます:

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

val creatureWrites = (
  (__ \ "name").write[String] and
  (__ \ "isDead").write[Boolean] and
  (__ \ "weight").write[Float]
)(unlift(Creature.unapply))  
```

<!--
###How to gather both Reads/Writes to create a `Format[Creature]`?
-->
###`Format[Creature]` を作るために Reads/Writes 両方をかき集めるには?

<!--
#### <a name="format-1">1st way = create from existing reads/writes</a>
-->
#### <a name="format-1">ひとつ目の方法 = 既存の reads/writes から作る</a>

<!--
You can reuse existing `Reads[T]` and `Writes[T]` to create a `Format[T]` as following:
-->
以下のようにして、既存の `Reads[T]` と `Writes[T]` を再利用して `Format[T]` を作ることができます:

```
implicit val creatureFormat = Format(creatureReads, creatureWrites)

val gizmojs = Json.obj( 
  "name" -> "gremlins", 
  "isDead" -> false, 
  "weight" -> 1.0F
)

val gizmo = Creature("gremlins", false, 1.0F)

assert(Json.fromJson[Creature](gizmojs).get == gizmo)
assert(Json.toJson(gizmo) == gizmojs)

```

<!--
#### <a name="format-2">2nd way = create using combinators</a>
-->
#### <a name="format-2">ふたつ目の方法 = コンビネータを使って作る</a>

<!--
We have Reads and Writes combinators, isn't it?  
Play2.1 also provides **Format Combinators** due to the magic of functional programming (actually it's not magic, it's just pure functional programming;) )
-->
Reads と Writes のコンビネータを手に入れましたよね?
Play2.1 は関数型プログラミングの魔法による **Format コンビネータ** も提供します (本当は魔法ではなく、純粋な関数型プログラミングです ;))

<!--
As usual, code 1st:
-->
いつもどおり、コードから始めます:

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

implicit val creatureFormat = (
  (__ \ "name").format[String] and
  (__ \ "isDead").format[Boolean] and
  (__ \ "weight").format[Float]
)(Creature.apply, unlift(Creature.unapply))  

val gizmojs = Json.obj( 
  "name" -> "gremlins", 
  "isDead" -> false, 
  "weight" -> 1.0F
)

val gizmo = Creature("gremlins", false, 1.0F)

assert(Json.fromJson[Creature](gizmojs).get == gizmo)
assert(Json.toJson(gizmo) == gizmojs)
```

<!--
Nothing too strange…
-->
とくに奇妙なところはありません…

#####`(__ \ "name").format[String]`
<!--
It creates a format[String] reading/writing at the given `JsPath`
-->
これは、与えられた `JsPath` で読み/書きを行う format[String] を作ります
<br/>

#####`( )(Creature.apply, unlift(Creature.unapply))`
<!--
To map to a Scala structure:
-->
Scala の構造にマッピングするために:

<!--
- `Reads[Creature]` requires a function `(String, Boolean, Float) => Creature`
- `Writes[Creature]` requires a function `Creature => (String, Boolean, Float)`
-->
- `Reads[Creature]` には、関数 `(String, Boolean, Float) => Creature` が必要です
- `Writes[Creature]` には、関数 `Creature => (String, Boolean, Float)` が必要です

<!--
So as `Format[Creature] extends Reads[Creature] with Writes[Creature]` we provide `Creature.apply` and `unlift(Creature.unapply)` and that's all folks...
-->
このため、`Format[Creature] extends Reads[Creature] with Writes[Creature]` のように、`Creature.apply` と `unlift(Creature.unapply)` を用意しました。これでおしまいです...

<!--
## <a name="format">More complex case</a>
-->
## <a name="format">もっと複雑な場合</a>

<!--
The previous sample is a bit dumb because the structure is really simple and because reading/writing is symmetric. We have:
-->
先の例は、構造がとてもシンプルだし、読み/書きが対照的だったため、すこし間が抜けています。以下の例を考えます:

```
Json.fromJson[Creature](Json.toJson(creature)) == creature
```

<!--
In this case, you read what you write and vis versa. So you can use the very simple `JsPath.format[T]` functions which build both `Reads[T]` and `Writes[T]` together.  
-->
この場合、書き出したいものを読み込みます。逆もまた同様です。このため、`Reads[T]` と `Writes[T]` の両方をまとめて作る、とてもシンプルな `JsPath.format[T]` 関数を使うことができます。

<!--
But if we take our usual more complicated case class, how to write the `Format[T]`?
-->
しかし、よくあるもっと複雑なケースクラスを取り扱う場合は、どのように `Format[T]` を書くのでしょうか?

<!--
Remind the code:
-->
以下のコードを思い出してください:

<!--
```
import play.api.libs.json._
import play.api.libs.functional.syntax._

// The case class
case class Creature(
  name: String, 
  isDead: Boolean, 
  weight: Float,
  email: String, // email format and minLength(5)
  favorites: (String, Int), // the stupid favorites
  friends: List[Creature] = Nil, // yes by default it has no friend
  social: Option[String] = None // by default, it's not social
)

import play.api.data.validation.ValidationError
import play.api.libs.json.Reads._

// defines a custom reads to be reused
// a reads that verifies your value is not equal to a give value
def notEqualReads[T](v: T)(implicit r: Reads[T]): Reads[T] = Reads.filterNot(ValidationError("validate.error.unexpected.value", v))( _ == v )

def skipReads(implicit r: Reads[String]): Reads[String] = r.map( _.substring(2) )

val creatureReads: Reads[Creature] = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float] and
  (__ \ "email").read(email keepAnd minLength[String](5)) and
  (__ \ "favorites").read( 
    (__ \ "string").read[String]( notEqualReads("ni") andKeep skipReads ) and
    (__ \ "number").read[Int]( max(86) or min(875) )
    tupled
  ) and
  (__ \ "friends").lazyRead( list[Creature](creatureReads) ) and
  (__ \ "social").read(optional[String])
)(Creature)  

import play.api.libs.json.Writes._

val creatureWrites: Writes[Creature] = (
  (__ \ "name").write[String] and
  (__ \ "isDead").write[Boolean] and
  (__ \ "weight").write[Float] and
  (__ \ "email").write[String] and
  (__ \ "favorites").write( 
    (__ \ "string").write[String] and
    (__ \ "number").write[Int]
    tupled
  ) and
  (__ \ "friends").lazyWrite(Writes.traversableWrites[Creature](creatureWrites)) and
  (__ \ "social").write[Option[String]]
)(unlift(Creature.unapply))

```
-->
```
import play.api.libs.json._
import play.api.libs.functional.syntax._

// ケースクラス
case class Creature(
  name: String,
  isDead: Boolean,
  weight: Float,
  email: String, // email フォーマットかつ minLength(5)
  favorites: (String, Int), // 間抜けなお気に入りデータ
  friends: List[Creature] = Nil, // ええ、デフォルトでは友達がいません
  social: Option[String] = None // デフォルトでは社交的ではありません
)

import play.api.data.validation.ValidationError
import play.api.libs.json.Reads._

// 再利用するために独自の Reads を定義します
// この Reads は、文字列が与えられた文字列と等しくないことを検証します
def notEqualReads[T](v: T)(implicit r: Reads[T]): Reads[T] = Reads.filterNot(ValidationError("validate.error.unexpected.value", v))( _ == v )

def skipReads(implicit r: Reads[String]): Reads[String] = r.map( _.substring(2) )

val creatureReads: Reads[Creature] = (
  (__ \ "name").read[String] and
  (__ \ "isDead").read[Boolean] and
  (__ \ "weight").read[Float] and
  (__ \ "email").read(email keepAnd minLength[String](5)) and
  (__ \ "favorites").read(
    (__ \ "string").read[String]( notEqualReads("ni") andKeep skipReads ) and
    (__ \ "number").read[Int]( max(86) or min(875) )
    tupled
  ) and
  (__ \ "friends").lazyRead( list[Creature](creatureReads) ) and
  (__ \ "social").read(optional[String])
)(Creature)

import play.api.libs.json.Writes._

val creatureWrites: Writes[Creature] = (
  (__ \ "name").write[String] and
  (__ \ "isDead").write[Boolean] and
  (__ \ "weight").write[Float] and
  (__ \ "email").write[String] and
  (__ \ "favorites").write(
    (__ \ "string").write[String] and
    (__ \ "number").write[Int]
    tupled
  ) and
  (__ \ "friends").lazyWrite(Writes.traversableWrites[Creature](creatureWrites)) and
  (__ \ "social").write[Option[String]]
)(unlift(Creature.unapply))

```

<!--
As you can see, `creatureReads` and `creatureWrites` are not exactly symmetric and couldn't be merged in one single `Format[Creature]` as done previously.
-->
見ての通り、`creatureReads` と `creatureWrites` は完全に対称的ではないので、以前に行ったようにひとつの `Format[Creature]` にまとめることはできません。

```
Json.fromJson[Creature](Json.toJson(creature)) != creature
```

<!--
Hopefully, as done previously, we can build a `Format[T]` from a `Reads[T]` and a `Writes[T]`.
-->
願わくば、先に行ったように `Reads[T]` と `Writes[T]` から `Format[T]` を作りたいものです。

<!--
```
import play.api.libs.json._
import play.api.libs.functional.syntax._

implicit val creatureFormat: Format[Creature] = Format(creatureReads, creatureWrites)

// Testing Serialization of Creature to Json
val gizmo = Creature("gremlins", false, 1.0F, "gizmo@midnight.com", ("alpha", 85), List(), Some("@gizmo"))
val zombie = Creature("zombie", true, 100.0F, "shaun@dead.com", ("ain", 2), List(gizmo), None)

val zombiejs = Json.obj(
  "name" -> "zombie",
  "isDead" -> true,
  "weight" -> 100.0,
  "email" -> "shaun@dead.com",
  "favorites" -> Json.obj(
    "string" -> "ain",
    "number" -> 2
  ),
  "friends" -> Json.arr(
    Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0,
      "email" -> "gizmo@midnight.com",
      "favorites" -> Json.obj(
        "string" -> "alpha",
        "number" -> 85
      ),
      "friends" -> Json.arr(),
      "social" -> "@gizmo"
    )
  ),
  "social" -> JsNull
)

assert(Json.toJson(zombie) == zombiejs)

// Testing Deserialization of JSON to Creature (note the dissymetric reading)
val gizmo2 = Creature("gremlins", false, 1.0F, "gizmo@midnight.com", ("pha", 85), List(), Some("@gizmo"))
val zombie2 = Creature("zombie", true, 100.0F, "shaun@dead.com", ("n", 2), List(gizmo2), None)

assert(Json.fromJson[Creature](zombiejs).get == zombie2)

```
-->
```
import play.api.libs.json._
import play.api.libs.functional.syntax._

implicit val creatureFormat: Format[Creature] = Format(creatureReads, creatureWrites)

// Creature から Json へのシリアライズをテストする
val gizmo = Creature("gremlins", false, 1.0F, "gizmo@midnight.com", ("alpha", 85), List(), Some("@gizmo"))
val zombie = Creature("zombie", true, 100.0F, "shaun@dead.com", ("ain", 2), List(gizmo), None)

val zombiejs = Json.obj(
  "name" -> "zombie",
  "isDead" -> true,
  "weight" -> 100.0,
  "email" -> "shaun@dead.com",
  "favorites" -> Json.obj(
    "string" -> "ain",
    "number" -> 2
  ),
  "friends" -> Json.arr(
    Json.obj(
      "name" -> "gremlins",
      "isDead" -> false,
      "weight" -> 1.0,
      "email" -> "gizmo@midnight.com",
      "favorites" -> Json.obj(
        "string" -> "alpha",
        "number" -> 85
      ),
      "friends" -> Json.arr(),
      "social" -> "@gizmo"
    )
  ),
  "social" -> JsNull
)

assert(Json.toJson(zombie) == zombiejs)

// JSON から Creature へのデシリアライズをテストする (非対称的な読み込みであることに注意してください)
val gizmo2 = Creature("gremlins", false, 1.0F, "gizmo@midnight.com", ("pha", 85), List(), Some("@gizmo"))
val zombie2 = Creature("zombie", true, 100.0F, "shaun@dead.com", ("n", 2), List(gizmo2), None)

assert(Json.fromJson[Creature](zombiejs).get == zombie2)

```

<!--
## Format[A] other features
-->
## Format[A] のその他の機能

### `Format[A].inmap( A => B, B => A ): Format[B]` 

<!--
`Format[A]` is both covariant and contravariant (invariant) functor.
So you must give both functions `A => B` and `B => A` to transform into a `Format[B]`.
-->
`Format[A]` は共変でもあり、反変 (不変) でもある要素です。
このため、`Format[B]` に変換するための関数 `A => B` と関数 `B => A` の両方を与えてやらなければなりません。

<!--
For example:
-->
例えば:

```
scala> case class Person(name: String)
defined class Person

scala> __.format[String].inmap( (name: String) => Person(name), (p: Person) => p.name )
res6: play.api.libs.json.OFormat[Person] = play.api.libs.json.OFormat$$anon$1@2dc083c1
```

<!--
> **Next:** [[JSON Transformers | ScalaJsonTransformers]]
-->
> **Next:** [[JSON トランスフォーマー | ScalaJsonTransformers]]