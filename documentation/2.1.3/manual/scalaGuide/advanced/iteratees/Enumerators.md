<!-- translated -->
<!--
# Handling data streams reactively
-->
# 反応的なストリーム処理

<!--
## Enumerators
-->
## Enumerator

<!--
If an iteratee represents the consumer, or sink, of input, an `Enumerator` is the source that pushes input into a given iteratee. As the name suggests, it enumerates some input into the iteratee and eventually returns the new state of that iteratee. This can be easily seen looking at the `Enumerator`’s signature:
-->
Iteratee がストリームの消費者やシンク、入力だとすると、 `Enumerator` は入力データを特定の Iteratee へ渡す送信元であるといえます。その名前が示すとおり、`Enumerator` は入力データを列挙 (Enumerate) して、 Iteratee に渡していきます。そして、最終的に新しい状態の Iteratee を返します。この挙動は、 `Enumerator` のシグネチャを見ると想像しやすいでしょう。

```scala
trait Enumerator[E] {

  /**
   * Apply this Enumerator to an Iteratee
   */
  def apply[A](i: Iteratee[E, A]): Promise[Iteratee[E, A]]

}
```

<!--
An `Enumerator[E]` takes an `Iteratee[E,A]` which is any iteratee that consumes `Input[E]` and returns a `Promise[Iteratee[E,A]]` which eventually gives the new state of the iteratee.
-->
`Enumerator[E]` は `Iteratee[E,A]` を引数に取ります。この Iterate は `Input[E]` を消費して、 `Promise[Iteratee[E,A]]` を返します。最終的には、この Promise から次の状態の Iteratee を取り出すことができます。

<!--
We can go ahead and manually implement `Enumerator` instances by consequently calling the iteratee’s fold method, or use one of the provided `Enumerator` creation methods. For instance we can create an `Enumerator[String]` that pushes a list of strings into an iteratee, like the following:
-->
このまま単に Iteratee.fold メソッドを呼び出すことで `Enumerator` を実装してもよいのですが、 `Enumerator` 作成用のヘルパーを使うこともできます。例えば、 文字列のリストを Iteratee へ送る `Enumerator[String]` は次のように作成することができます。

```scala
val enumerateUsers: Enumerator[String] = {
  Enumerator("Guillaume", "Sadek", "Peter", "Erwan")
}
```

<!--
Now we can apply it to the consume iteratee we created before:
-->
この Enumerator を使って、先程作成した Iteratee にデータを消費させるには次のように書きます。

```scala
val consume = Iteratee.consume[String]()
val newIteratee: Future[Iteratee[String,String]] = enumerateUsers(consume) 
```

<!--
To terminate the iteratee and extract the computed result we pass `Input.EOF`. An `Iteratee` carries a `run` method that does just this. It pushes an `Input.EOF` and returns a `Promise[A]`, ignoring left input if any.
-->
Iteratee を終了させて、計算結果を取り出すためには、 `Input.EOF` を渡します。 `Iteratee` にはそのための `run` メソッドが用意されています。このメソッドを呼び出すと、 `Input.EOF` が送信されて、 `Promise[A]` が帰ります。残りの入力データは無視されます。

```scala
// We use flatMap since newIteratee is a promise, 
// and run itself return a promise
val eventuallyResult: Future[String] = newIteratee.flatMap(i => i.run)

//Eventually print the result
eventuallyResult.onSuccess { case x => println(x) }

// Prints "GuillaumeSadekPeterErwan"
```

<!--
You might notice here that an `Iteratee` will eventually produce a result (returning a promise when calling fold and passing appropriate calbacks), and a `Promise` eventually produces a result. Then a `Promise[Iteratee[E,A]]` can be viewed as `Iteratee[E,A]`. Indeed this is what `Iteratee.flatten` does, Let’s apply it to the previous example:
-->
もしかしたら、 `Iteratee` が最終的に結果を生成し (fold に適切なコールバック関数を渡した場合は Promise が返ります) 、一方で `Promise` も最終的に結果を生成することに気づかれた方がいるかもしれません。このとき、 `Promise[Iteratee[E,A]]` は `Iteratee[E,A]` と見なすことができます。 `Iteratee.flatten` はまさしくこの Promise と Iteratee の変換を行うヘルパーです。先程の例でこのヘルパーを使ってみましょう。

```scala
//Apply the enumerator and flatten then run the resulting iteratee
val newIteratee = Iteratee.flatten(enumerateUsers(consume))

val eventuallyResult: Future[String] = newIteratee.run
   
//Eventually print the result 
eventuallyResult.onSuccess { case x => println(x) }

// Prints "GuillaumeSadekPeterErwan"
```

<!--
An `Enumerator` has some symbolic methods that can act as operators, which can be useful in some contexts for saving some parentheses. For example, the `|>>` method works exactly like apply:
-->
`Enumerator` には演算子のように振る舞う記号的なメソッドがいくつか用意されています。いずれも、文脈によっては括弧の節約という意味で役に立つことがあるかもしれません。例えば、 `|>>` メソッドは apply メソッドと全く同じ結果になります。

```scala
val eventuallyResult: Future[String] = {
  Iteratee.flatten(enumerateUsers |>> consume).run
}
```

<!--
Since an `Enumerator` pushes some input into an iteratee and eventually return a new state of the iteratee, we can go on pushing more input into the returned iteratee using another `Enumerator`. This can be done either by using the `flatMap` function on `Promise`s or more simply by combining `Enumerator` instancess using the `andThen` method, as follows:
-->
`Enumerator` は入力データを Iteratee へ送信して、最終的には新しい状態の Iteratee を返します。この新しい Iteratee に、別の `Enumerator` を使ってさらに入力データを渡すことができます。これは、 `Promise` に `flatMap` を適用するか、もしくは `Enumerator` のインスタンスを `andThen` メソッドによって組み合わせることで実現できます。

```scala
val colors = Enumerator("Red","Blue","Green")

val moreColors = Enumerator("Grey","Orange","Yellow")

val combinedEnumerator = colors.andThen(moreColors)

val eventuallyIteratee = combinedEnumerator(consume)
```

<!--
As for apply, there is a symbolic version of the `andThen` called `>>>` that can be used to save some parentheses when appropriate:
-->
apply メソッドと同様に、 `andThen` にも `>>>` という演算子版が容易されています。これも、状況によっては括弧の節約に役立つでしょう。

```scala
val eventuallyIteratee = {
  Enumerator("Red","Blue","Green") >>>
  Enumerator("Grey","Orange","Yellow") |>>
  consume    
}
```

<!--
We can also create `Enumerator`s for enumerating files contents:
-->
ファイルの内容を列挙するための `Enumerator` を作成することもできます。

```scala
val fileEnumerator: Enumerator[Array[Byte]] = {
  Enumerator.fromFile(new File("path/to/some/file"))
}
```

<!--
Or more generally enumerating a `java.io.InputStream` using `Enumerator.fromStream`. It is important to note that input won't be read until the iteratee this `Enumerator` is applied on is ready to take more input.
-->
より汎用的には、 `Enumerator.fromStream` を利用して `java.io.InputStream` 内のデータを列挙することができます。この場合、 `Enumerator` に割り当てられている Iteratee が次の入力データを読み込めるような状態になるまで、Enumerator 側でも新しいデータが読み込まれないことに注意してください。

<!--
Actually both methods are based on the more generic `Enumerator.fromCallback` that has the following signature:
-->
内部的には、これらのメソッドは両方とも `Enumerator.fromCallback` という関数に依存しています。

```scala
def fromCallback[E](
  retriever: () => Promise[Option[E]],
  onComplete: () => Unit = () => (),
  onError: (String, Input[E]) => Unit = (_: String, _: Input[E]) => ()
): Enumerator[E] = {
  ... 
}
```

<!--
This method defined on the `Enumerator` object is one of the most important methods for creating `Enumerator`s from imperative logic. Looking closely at the signature, this method takes a callback function `retriever: () => Promise[Option[E]]` that will be called each time the iteratee this `Enumerator` is applied to is ready to take some input. 
-->
`Enumerator` オブジェクトに定義されているこのメソッドは、手続き的な処理を行う `Enumerator` をつくる場合に大変重要なメソッドです。シグネチャをよくみると、このメソッドは `retriever: () => Promise[Option[E]]` というコールバック関数を引数をとることがわかります。このコールバック関数は、 `Enumerator` に割り当てられている Iteratee が次の入力データを読込可能な状態になった際に呼び出されます。

<!--
It can be easily used to create an `Enumerator` that represents a stream of time values every 100 millisecond using the opportunity that we can return a promise, like the following:
-->
例えば、このメソッドを利用すると、 Promise を返すタイミングにおいて、100 ミリ秒おきに日時データを生成するストリームを生成することができます。

```scala
Enumerator.fromCallback { () =>
  Promise.timeout(Some(new Date), 100 milliseconds)
}
```

<!--
In the same manner we can construct an `Enumerator` that would fetch a url every some time using the `WS` api which returns, not suprisingly a `Promise`
-->
同じような考え方で、`WS` API を使って特定の URL の内容を一定時間おきに取得して、 `Promise` を返す `Enumerator` も次のようにつくることができます。

<!--
Combining this, callback Enumerator, with an imperative `Iteratee.foreach` we can println a stream of time values periodically:
-->
このコールバック Enumerator と手続き的な `Iteratee.foreach` を組み合わせることで、一定時間おきに Stream から取得した日時データを println することができます。

```scala
val timeStream = Enumerator.fromCallback { () => 
  Promise.timeout(Some(new Date), 100 milliseconds)
}

val printlnSink = Iteratee.foreach[Date](date => println(date))

timeStream |>> printlnSink
```

<!--
Another, more imperative, way of creating an `Enumerator` is by using `Enumerator.pushee` which once it is ready will give a `Pushee` interface on which defined methods `push` and `close`:
-->
`Enumerator.pushee` を利用すると、 `Enumerator` をさらに手続き的に記述することができます。このメソッドからは、 `push` と `close` というメソッドが定義された `Pushee` へのインタフェースが提供されます。

```scala
val channel = Enumerator.pushee[String] { onStart = pushee =>
  pushee.push("Hello")
  pushee.push("World")
}

channel |>> Iteratee.foreach(println)
```

<!--
The `onStart` function will be called each time the `Enumerator` is applied to an `Iteratee`. In some applications, a chatroom for instance, it makes sense to assign the pushee to a synchronized global value (using STMs for example) that will contain a list of listeners. `Enumerator.pushee` accepts two other functions, `onComplete` and `onError`.
-->
この `onStart` 関数は、`Enumerator` が `Iteratee` へ適用されるたびに呼び出されます。例えば、チャットルームなどのアプリケーションでは、 Pushee を　STM などで同期制御されたグローバル変数と関連付けておき、その変数にチャットルームの参加者のリストを持たせておく、といった利用方法が考えられます。 `Enumerator.pushee` にはその他に `onComplete` 、 `onError` という関数も用意されています。

<!--
One more interesting method is the `interleave` or `>-` method which as the name says, itrerleaves two Enumerators. For reactive `Enumerator`s Input will be passed as it happens from any of the interleaved `Enumerator`s
-->
最後に一つ興味深いメソッドを紹介します。 `interleave` または演算子の `>-` というメソッドです。これはその名の通り、二つの `Enumerator` を並べて、それぞれから入力データが与えられたら、それに反応して即座に Iteratee へ渡すという、新しい `Enumerator` を生成します。

<!--
## Enumerators à la carte
-->
## Enumerator アラカルト

<!--
Now that we have several interesting ways of creating `Enumerator`s, we can use these together with composition methods `andThen` / `>>>` and `interleave` / `>-` to compose `Enumerator`s on demand.
-->
さて、これまでの説明で様々な `Enumerator` の作り方を知ることができました。これらの `Enumerator` を `andThen` / `>>>` や `interleave` / `>-` で組み合わせて、任意の `Enumerator` を合成することができます。

<!--
Indeed one interesting way of organizing a streamful application is by creating primitive `Enumerator`s and then composing a collection of them. Let’s imagine doing an application for monitoring systems:
-->
もうお気づきかもしれませんが、多数のストリームを要するアプリケーションをうまく構築するためには、基本となる `Enumerator` を生成して、それらを組み合わせるとよいでしょう。例えば、監視システムを作る場合、次のようなコードになるでしょう。

```scala
object AvailableStreams {

  val cpu: Enumerator[JsValue] = Enumerator.fromCallback(/* code here */)

  val memory: Enumerator[JsValue] = Enumerator.fromCallback(/* code here */)

  val threads: Enumerator[JsValue] = Enumerator.fromCallback(/* code here */)

  val heap: Enumerator[JsValue] = Enumerator.fromCallback(/* code here */)

}

val physicalMachine = AvailableStreams.cpu >- AvailableStreams.memory
val jvm = AvailableStreams.threads >- AvailableStreams.heap

def usersWidgetsComposition(prefs: Preferences) = {
  // do the composition dynamically
}
```

<!--
Now, it is time to adapt and transform `Enumerator`s and `Iteratee`s using ... `Enumeratee`s!
-->
次ページでは、 `Enumerator` や `Iteratee` を変換したり、アダプターをかませる方法... `Enumeratee` について説明します!

<!--
> **Next:** [[Enumeratees | Enumeratees]]
-->
> **次ページ:** [[Enumeratee | Enumeratees]]
