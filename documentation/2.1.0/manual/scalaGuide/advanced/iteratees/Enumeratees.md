<!-- translated -->
<!--
# Handling data streams reactively
-->
# 反応的なストリーム処理

<!--
## The realm of Enumeratees
-->
## Enumeratee の守備範囲

<!--
‘Enumeratee’ is a very important component in the iteratees API. It provides a way to adapt and transform streams of data. An `Enumeratee` that might sound familiar is the `Enumeratee.map`.
-->
`Enumeratee` は Iteratee API の中でとても重要なコンポーネントです。`Enumeratee` の役割は、データのストリームを適合・変換することです。その役割をイメージできるという意味で、 `Enumeratee` の中でも特に `Enumeratee.map` というメソッドが親しみやすいかもしれません。

<!--
Starting with a simple problem, consider the following `Iteratee`:
-->
単純な例から始めてみましょう。まずは、次のような `Iteratee` をつくります。

```scala
val sum: Iteratee[Int,Int] = Iteratee.fold[Int,Int](0){ (s,e) => s + e }
```

<!--
This `Iteratee` takes `Int` objects as input and computes their sum. Now if we have an `Enumerator` like the following:
-->
この `Iteratee` は `Int` を入力にとり、それらの和を計算します。次は以下のような `Enumerator` をつくりましょう。

```scala
val strings: Enumerator[String] = Enumerator("1","2","3","4")
```

<!--
Then obviously we can not apply the `strings:Enumerator[String]` to an `Iteratee[Int,Int]`. What we need is transform each `String` to the corresponding `Int` so that the source and the consumer can be fit together. This means we either have to adapt the `Iteratee[Int,Int]` to be `Iteratee[String,Int]`, or adapt the `Enumerator[String]` to be rather an `Enumerator[Int]`.
An `Enumeratee` is the right tool for doing that. We can create an `Enumeratee[String,Int]` and adapt our `Iteratee[Int,Int]` using it:
-->
さて、ここで `string:Enumerator[String]` は明らかに `Iteratee[Int,Int]` に適用できません。しかし、 Enumerator が生成する `String` を何らかのルールに基づいて `Int` へ変換することができれば、ストリームの生成側と消費側がうまく噛み合いそうです。つまり、 `Iteratee[Int,Int]` を `Iteratee[String,Int]` に適合させるか、もしくは `Enumerator[String]` を `Enumerator[Int]` へ適合させるか、どちらかを行う必要があるということです。
`Enumeratee` はまさにこの用途のためにあります。 `Enumeratee[String,Int]` を利用すると、 `Iteratee[Int,Int]` を目的のインタフェースに適合させることができます。

```scala
//create am Enumeratee using the map method on Enumeratee
val toInt: Enumeratee[String,Int] = Enumeratee.map[String]{ s => s.toInt } 

val adaptedIteratee: Iteratee[String,Int] = toInt.transform(sum)

//this works!
strings |>> adaptedIteratee
```
<!--
There is a symbolic alternative to the `transform` method, `&>>` which we can use in our previous example:
-->
`transform` メソッドと全く同じ意味をもつ演算子 `&>>` も利用できます。

```scala
strings |>> toInt &>> sum 
```

<!--
The `map` method will create an 'Enumeratee' that uses a provided `From => To` function to map the input from the `From` type to the `To` type. We can also adapt the `Enumerator`:
-->
`map` メソッドは引数に渡された `From => To` という関数をつかって、`From` 型の入力データを `To` 型の値へ変換する `Enumeratee` を生成します。`Enumeratee` は `Enumerator` を変換することもできます。

```scala
val adaptedEnumerator: Enumerator[Int] = strings.through(toInt)

//this works!
adaptedEnumerator |>> sum
```

<!--
Here too, we can use a symbolic version of the `through` method:
-->
`through` メソッドについても、同じ意味の演算子が用意されています。

```scala
strings &> toInt |>> sum
```

<!--
Let’s have a look at the `transform` signature defined in the `Enumeratee` trait:
-->
`Enumeratee` トレイトに定義されている `transform` メソッドのシグネチャを見てみましょう。

```scala
trait Enumeratee[From, To] {
  def transform[A](inner: Iteratee[To, A]): Iteratee[From, A] = ...
}
```

<!--
This is a fairly simple signature, and is the same for `through` defined on an `Enumerator` :
-->
かなり簡単なシグネチャです。 `Enumerator` に定義されている `through` メソッドについても同様です。

```scala
trait Enumerator[E] {
  def through[To](enumeratee: Enumeratee[E, To]): Enumerator[To] 
}
```

<!--
The `transform` and `through` methods on an `Enumeratee` and `Enumerator`, respectively, both use the `apply` method on `Enumeratee`, which has a slightly more sophisticated signature:
-->
`Enumeratee` と `Enumerator` における `transform` と `through` はどちらも `Enumeratee` の `apply` メソッドを利用しています。こちらのシグネチャはもう少し複雑です。

```scala
trait Enumeratee[From, To] {
  def apply[A](inner: Iteratee[To, A]): Iteratee[From, Iteratee[To, A]] = ...
}
```

<!--
Indeed, an `Enumeratee` is more powerful than just transforming an `Iteratee` type. It really acts like an adapter in that you can get back your original `Iteratee` after pushing some different input through an `Enumeratee`. So in the previous example, we can get back the original `Iteratee[Int,Int]` to continue pushing some `Int` objects in:
-->
`Enumeratee` が出来るのは、単に `Iteratee` の方を変換することだけではありません。 `Enumeratee` は取り外し可能なアダプターのようなものなので、 `Enumeratee` を通して異なる種類の入力データを送信し終わった後は、本来の `Iteratee` に戻すことができます。前述の例でいえば、 本来の `Iteratee[Int,Int]` に戻してから、今度は `Int` の入力データを送ることができます。

```scala
val sum:Iteratee[Int,Int] = Iteratee.fold[Int,Int](0){ (s,e) => s + e }

//create am Enumeratee using the map method on Enumeratee
val toInt: Enumeratee[String,Int] = Enumeratee.map[String]{ s => s.toInt } 

val adaptedIteratee: Iteratee[String,Iteratee[Int,Int]] = toInt(sum)

// pushing some strings
val afterPushingStrings: Future[Iteratee[String,Iteratee[Int,Int]]] = {
   Enumerator("1","2","3","4") |>> adaptedIteratee
}

val flattenAndRun:Future[Iteratee[Int,Int]] = Iteratee.flatten(afterPushingStrings).run

val originalIteratee = Iteratee.flatten(flattenAndRun)

val moreInts: Future[Iteratee[Int,Int]] = Enumerator(5,6,7) |>> originalIteratee

val sumFuture:Future[Int] = Iteratee.flatten(moreInts).run

sumFuture onSuccess {
  case s => println(s)// eventually prints 28 
} 
```

<!--
That’s why we call the adapted (original) `Iteratee` ‘inner’ and the resulting `Iteratee` ‘outer’.
-->
このようなことが可能であるため、変換前の元々の `Iteratee` を「内側」、変換後の `Iteratee` を「外側」と呼びます。

<!--
Now that the `Enumeratee` picture is clear, it is important to know that `transform` drops the left input of the inner `Iteratee` when it is `Done`. This means that if we use `Enumeratee.map` to transform input, if the inner `Iteratee` is `Done` with some left transformed input, the `transform` method will just ignore it.
-->
`Enumeratee` の全体像が見えてきた所で、少し重要な話をします。実は、 `transform` は内側の `Iteratee` が `Done` 状態になったときにに与えられる最後の入力データを取りこぼしてしまいます。つまり、 `Enumeratee.map` を使って入力データを変換すると、内側の `Iteratee` が入力データの最後のチャンクとともに `Done` 状態になった際、 `transform` メソッドがそれを無視してしまいます。

<!--
That might have seemed like a bit too much detail, but it is useful for grasping the model.
-->
この場で説明するには少し詳細に入りすぎていると思われるかもしれませんが、モデルを把握する役には立ちます。

<!--
Back to our example on `Enumeratee.map`, there is a more general method `Enumeratee.mapInput` which, for example, gives the opportunity to return an `EOF` on some signal:
-->
`Enumeratee.map` の例に立ち戻って考えてみると、 実はそれより汎用的な `Enumeratee.mapInput` というメソッドがあります。これを使うと、任意のタイミングで `EOF` を返すことができます。

```scala
val toIntOrEnd: Enumeratee[String,Int ] = Enumeratee.mapInput[String] {
  case Input.El("end") => Input.EOF
  case other => other.map(e => e.toInt)
}
```

`Enumeratee.map` and `Enumeratee.mapImput` are pretty straight forward, they operate on a per chunk basis and they convert them. Another useful `Enumeratee` is the `Enumeratee.filter` :

```scala
def filter[E](predicate: E => Boolean): Enumeratee[E, E]
```

<!--
The signature is pretty obvious, `Enumeratee.filter` creates an `Enumeratee[E,E]` and it will test each chunk of input using the provided `predicate: E => Boolean` and it passes it along to the inner (adapted) iteratee if it statisfies the predicate:
-->
シグネチャからも明らかかもしれませんが、 `Enumeraee.filter` は `Enumeratee[E,E]` を生成します。その `Enumeratee` は入力データのチャックを `predicate: E => Boolean` で一つ一つテストして、predicate が true を返したチャンクだけを内側の Iteratee へ送信します。

```scala
val numbers = Enumerator(1,2,3,4,5,6,7,8,9,10)

val onlyOdds = Enumeratee.filter[Int](i => i % 2 != 0)

numbers.through(onlyOdds) |>> sum
```

<!--
There are methods, such as `Enumeratee.collect`, `Enumeratee.drop`, `Enumeratee.dropWhile`, `Enumeratee.take`, `Enumeratee.takeWhile`, which work on the same principle.
Let try to use the `Enumeratee.take` on an Input of chunks of bytes:
-->
その他にも、 `Enumeratee.collect` や `Enumeratee.drop`、 `Enumeratee.dropWhile`、 `Enumeratee.take`、 `Enumeratee.takeWhile` など、似たような原理の Enumeratee が用意されています。
試しに、バイトデータのチャンクに対して `Enumeratee.take` を適用してみましょう。

```scala
// computes the size in bytes
val fillInMemory: Iteratee[Array[Byte],Array[Byte]] = {
  Iteratee.consume[Array[Byte]]()
}

val limitTo100: Enumeratee[Array[Byte],Array[Byte]] = {
  Enumeratee.take[Array[Byte]](100)
}

val limitedFillInMemory: Iteratee[Array[Byte],Array[Byte]] = {
  limitTo100 &>> fillInMemory
}
```

<!--
It looks good, but how many bytes are we taking? What would ideally limit the size, in bytes, of loaded input. What we do above is to limit the number of chunks instead, whatever the size of each chunk is. It seems that the `Enumeratee.take` is not enough here since it has no information about the type of input (in our case an `Array[Byte]`) and this is why it can’t count what’s inside.
-->
一見問題なさそうにみえますが、実際のところ合計で何バイトのデータが残っているのでしょうか？どうすれば、入力データの最大サイズをうまく制限できるのでしょうか。実は、上の例は入力データのチャンク数を制限しただけで、それぞれのチャンクの大きさは制限できていません。どうやら、`Enumeratee.take` は入力データの型（ここでは `Array[Byte]`）について何の情報も参照できないので、入力のデータの大きさを測ることもできないようです。

<!--
Luckily there is a `Traversable` object that offers a set of methods for creating `Enumeratee` instances for Input types that are `TraversableLike`. An `Array[Byte]` is `TraversableLike` and so we can use`Traversable.take`:
-->
しかし、問題ありません。 `Array[Byte]` のような `TraversableLike` 型の入力データ向けの `Enumeratee` を作成するヘルパーが `Traversable` オブジェクトにひと通り用意されています。上記の例に戻ると、 `TraversableLike.take` を使うとうまくいきます。

```scala
val fillInMemory: Iteratee[Array[Byte],Array[Byte]] = {
  Iteratee.consume[Array[Byte]]()
}

val limitTo100: Enumeratee[Array[Byte],Array[Byte]] = {
  Traversable.take[Array[Byte]](100)
}

// We are sure not to get more than 100 bytes loaded into memory
val limitedFillInMemory: Iteratee[Array[Byte],Array[Byte]] = {
  limitTo100 &>> fillInMemory
}
```

<!--
Other `Traversable` methods exist including `Traversable.takeUpTo`, `Traversable.drop`.
-->
`Traversable` その他のメソッドとしては、 `Traversable.takeUpTo` や `Traversable.drop` などがあります。

<!--
Finally, you can compose different `Enumeratee` instances using the `compose` method, which has the symbolic equivalent `><>`. Note that any left input on the `Done` of the composed `Enumeratee` instances will be dropped. However, if you use `composeConcat` aliased `>+>`, any left input will be concatenated. 
-->
最後になりましたが、`Enumeratee` のインスタンスは `compose` メソッドまたは `><>` 演算子により合成することができます。注意しなければならないこととして、合成された `Enumeratee` のインスタンスは必ず `Done` と共に与えられた最後の入力データのチャンクを無視します。しかしながら、 `composeConcat` や `>+>` 演算子を使うと、最後のチャンクについてもちゃんと読み込まれます。
