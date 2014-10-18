<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Handling data streams reactively
-->
# 反応的なストリーム処理

<!--
Progressive Stream Processing and manipulation is an important task in modern Web Programming, starting from chunked upload/download to Live Data Streams consumption, creation, composition and publishing through different technologies including Comet and WebSockets.
-->
現代の Web プログラミングにおいて、ストリーム処理は重要なタスクです。これには、チャンク単位のデータダウンロード/アップロードや、 Comet や WebSocket など様々な技術を利用したデータストリームのリアルタイム処理、作成、合成、提供などが含まれます。

<!--
Iteratees provide a paradigm and an API allowing this manipulation, while focusing on several important aspects:
-->
Iteratee はこのようなストリーム処理を実現する考え方と API です。 Iteratee の主な特徴は次のとおりです。

<!--
* Allowing the user to create, consume and transform streams of data.
* Treating different data sources in the same manner (Files on disk, Websockets, Chunked Http, Data Upload, ...).
* Composable: using a rich set of adapters and transformers to change the shape of the source or the consumer - construct your own or start with primitives.
* Being able to stop data being sent mid-way through, and being informed when source is done sending data.
* Non blocking, reactive and allowing control over resource consumption (Thread, Memory)
-->
* データのストリームを生成、処理、変換することができる
* 様々なデータを同じような形式で扱える (例えば、ディスク上のファイル、WebSocket、Chunked HTTP、データアップロードなど)
* 合成可能: ストリームのソースやコンシューマを、別の型のソースやコンシューマに変換するためのアダプタや変換器が豊富に用意されている
* データの送信を途中で停めたり、データの送信が完了した際に通知を受けることができるようにする
* ノン・ブロッキング、リアクティブで、かつ (Thread、Memoryなどの) リソース消費をコントロールできる

<!--
## Iteratees
-->
## Iteratee

<!--
An Iteratee is a consumer - it describes the way input will be consumed to produce some value. An Iteratee is a consumer that returns a value it computes after being fed enough input.
-->
Iteratee はコンシューマです。Iteratee は入力データの処理方法および結果の生成方法が記述されます。Iteratee は十分な入力データを受け取ると、それに対して何らかの計算を行い結果値を返します。

```scala
// an iteratee that consumes String chunks and produces an Int
Iteratee[String,Int]
```

<!--
The Iteratee interface `Iteratee[E,A]` takes two type parameters: `E`, representing the type of the Input it accepts, and `A`, the type of the calculated result.
-->
Iteratee のインタフェースは `[Iteratee[E, A]]` のように二つの型パラメータを取ります。`E` は入力データの型、`A` は結果値の型です。

<!--
An iteratee has one of three states: `Cont` meaning accepting more input, `Error` to indicate an error state, and `Done` which carries the calculated result. These three states are defined by the `fold` method of an `Iteratee[E,A]` interface:
-->
Iteratee は 3 つの状態を持ちます。それぞれ、`Cont` はさらに入力データを受付可能であること、`Error` はエラーにより入力を停止したこと、`Done` は計算結果が出ていることを表します。これら 3 つのステータスは `Iteratee[E,A]` インタフェースの `fold` メソッドにより定義することができます。

```scala
def fold[B](folder: Step[E, A] => Future[B]): Future[B]
```

<!--
where the `Step` object has 3 states :
-->
`Step` オブジェクトは 3 つの状態を持ちます。

```scala
object Step {
  case class Done[+A, E](a: A, remaining: Input[E]) extends Step[E, A]
  case class Cont[E, +A](k: Input[E] => Iteratee[E, A]) extends Step[E, A]
  case class Error[E](msg: String, input: Input[E]) extends Step[E, Nothing]
}
```

<!--
The fold method defines an iteratee as one of the three mentioned states. It accepts three callback functions and will call the appropriate one depending on its state to eventually extract a required value. When calling `fold` on an iteratee you are basically saying:
-->
fold メソッドは、 Iteratee をこれら３つの状態のいずれかに定義します。fold メソッドは 3 つのコールバック関数を引数にとり、状態に応じていずれか一つを呼び出し、最終的には結果値を返します。Iteratee に対する `fold` の呼び出しは、次のような意味になります。

<!--
- If the iteratee is in the state `Done`, then I'll take the calculated result of type `A` and what is left from the last consumed chunk of input `Input[E]` and eventually produce a `B`
- If the iteratee is in the state `Cont`, then I'll take the provided continuation (which is accepting an input) `Input[E] => Iteratee[E,A]` and eventually produce a `B`. Note that this state provides the only way to push input into the iteratee, and get a new iteratee state, using the provided continuation function. 
- If the iteratee is in the state `Error`, then I'll take the error message of type `String` and the input that caused it and eventually produce a B.
-->
- Iteratee が `Done` 状態であれば、 `A` という型の計算結果と `Input[E]` という型のこれから消費される入力データの最後のチャンクを元に、 `B` という型の値を生成します。
- Iteratee が `Cont` 状態であれば、`Input[E] => Iteratee[E,A]` という型の継続 (入力を待ち受ける) を使って、最終的には `B` という型の値を生成します。この状態が Iteratee にデータを入力する唯一のタイミングであり、データを入力後は提供された継続を使って新しい状態の Iteratee を返します。
- Iteratee が `Error` 状態であれば、`String` 型のエラーメッセージと、エラーの原因となった入力データを元に、 `B` 型の値を生成します。

<!--
Depending on the state of the iteratee, `fold` will produce the appropriate `B` using the corresponding passed-in function.
-->
Iteratee の状態に依存して、`fold` は引数に渡された関数のいずれかを呼び出して、適切な `B` 型の値を生成します。

<!--
To sum up, an iteratee consists of 3 states, and `fold` provides the means to do something useful with the state of the iteratee.
-->
まとめると、 Iteratee には 3 つの状態が存在して、 `fold` メソッドは Iteratee の状態に応じて何か実処理を行う方法を提供します。

<!--
### Some important types in the `Iteratee` definition:
-->
### Iteratee の定義における重要な型

<!--
Before providing some concrete examples of iteratees, let's clarify two important types we mentioned above:
-->
Iteratee の具体例を見るために、上記で説明した二つの重要な型について詳しく見ていきます。

<!--
- `Input[E]` represents a chunk of input that can be either an `El[E]` containing some actual input, an `Empty` chunk or an `EOF` representing the end of the stream.
For example, `Input[String]` can be `El("Hello!")`, Empty, or EOF
-->
- `Input[E]` は入力データのチャンクを表し、実際の入力データを含む `El[E]` か `Empty` チャンクか、またはストリームの終端を表す EOF のいずれかになります。
例えば、`Input[String]` は `El("Hello!")` や Empty 、 EOF にいずれかになります。

<!--
- `Future[A]` represents, as its name indicates, a future value of type `A`. This means that it is initially empty and will eventually be filled in ("redeemed") with a value of type `A`, and you can schedule a callback, among other things you can do, if you are interested in that value. A Future is a very nice primitive for synchronization and composing async calls, and is explained further at the [[ScalaAsync]] section.
-->
- `Future[A]` は、その名の通り `A` 型の将来における値です。これは、はじめは空であり、最終的に ("約束された") `A` 型の値がセットされること、その値を使いたい場合はコールバックやその他の処理を計画できることを意味しています。Future は並列処理を同期させたり、非同期処理を合成する際に便利なデータ構造であり、[[ScalaAsync]] の節で詳しく説明されています。

<!--
### Some primitive iteratees:
-->
### 基本的な Iteratee

<!--
By implementing the iteratee, and more specifically its fold method, we can now create some primitive iteratees that we can use later on.
-->
Iteratee や、より具体的にはその fold メソッドを定義することで、後々に再利用できる基本的な Iteratee を作成することができます。

<!--
- An iteratee in the `Done` state producing a `1:Int` and returning `Empty` as the remaining value from the last `Input[String]`
-->
- `1:Int` を生成し、最後の `Input[String]` のあとに残る値として `Empty` を返す `Done` 状態の Iteratee

```scala
val doneIteratee = new Iteratee[String,Int] {
  def fold[B](folder: Step[String,Int] => Future[B])(implicit ec: ExecutionContext) : Future[B] = 
    folder(Step.Done(1, Input.Empty))
}
```

<!--
As shown above, this is easily done by calling the appropriate `apply` function, in our case that of `Done`, with the necessary information.
-->
上記のとおり、このような Iteratee は、この例における `Done` のような適切な `apply` 関数に必要な引数を渡すだけで実装することができます。

<!--
To use this iteratee we will make use of the `Future` that holds a promised value.
-->
この Iteratee を利用するためには、約束された値を持つ `Future` を使います。

```scala
def folder(step: Step[String,Int]):Future[Option[Int]] = step match {
  case Step.Done(a, e) => future(Some(a))
  case Step.Cont(k) => future(None)
  case Step.Error(msg,e) => future(None)
} 

val eventuallyMaybeResult: Future[Option[Int]] = doneIteratee.fold(folder)

eventuallyMaybeResult.onComplete(i => println(i))
```

<!--
of course to see what is inside the `Future` when it is redeemed we use `onComplete`
-->
redeem されたときに `Future` の中身を取得するためには、 `onComplete` を使います。

```scala
// will eventually print 1
eventuallyMaybeResult.onComplete(i => println(i))
```

<!--
There is already a built-in way allowing us to create an iteratee in the `Done` state by providing a result and input, generalizing what is implemented above:
-->
上記の実装をもっと汎用的にしてみましょう。Play には結果と入力値から `Done` 状態の Iteratee を作るヘルパーが用意されています。

```scala
val doneIteratee = Done[String,Int](1, Input.Empty)
```

<!--
Creating a `Done` iteratee is simple, and sometimes useful, but it does not consume any input. Let's create an iteratee that consumes one chunk and eventually returns it as the computed result:
-->
`Done` Iteratee はこの通りとても簡単に作成でき、役に立つケースも無くはないのですが、入力データをなにも消費しません。次は、入力データのチャンクをひとつ消費して、最終的にそのチャンクを結果値として返すような Iteratee を作ってみましょう。

```scala
val consumeOneInputAndEventuallyReturnIt = new Iteratee[String,Int] {
    
def fold[B](folder: Step[String,Int] => Future[B])(implicit ec: ExecutionContext): Future[B] = {
     folder(Step.Cont {
       case Input.EOF => Done(0, Input.EOF) //Assuming 0 for default value
       case Input.Empty => this
       case Input.El(e) => Done(e.toInt,Input.EOF) 
     })
  }
}

def folder(step: Step[String,Int]):Future[Int] = step match {
  case Step.Done(a, _) => future(a)
  case Step.Cont(k) => k(Input.EOF).fold({
    case Step.Done(a1, _) => Future.successful(a1)
    case _ => throw new Exception("Erroneous or diverging iteratee")
  })
  case _ => throw new Exception("Erroneous iteratee")
} 

```

<!--
As for `Done`, there is a built-in way to define an iteratee in the `Cont` state by providing a function that takes `Input[E]` and returns a state of `Iteratee[E,A]` :
-->
`Done` の場合と同様に、Play には `Cont` 状態の Iteratee を作るためのヘルパーも用意されています。このヘルパーは、 `Input[E]` の値を引数にとって、 `Iteratee[E,A]` を返します。

```scala
val consumeOneInputAndEventuallyReturnIt = {
  Cont[String,Int](in => Done(100,Input.Empty))
}
```

<!--
In the same manner there is a built-in way to create an iteratee in the `Error` state by providing an error message and an `Input[E]`
-->
さらに、`Error` 状態についても、`Input[E]` とエラーメッセージを渡すことで `Error` 状態の Iteratee を作成できるヘルパーが用意されています。

<!--
Back to the `consumeOneInputAndEventuallyReturnIt`, it is possible to create a two-step simple iteratee manually, but it becomes harder and cumbersome to create any real-world iteratee capable of consuming a lot of chunks before, possibly conditionally, it eventually returns a result. Luckily there are some built-in methods to create common iteratee shapes in the `Iteratee` object.
-->
`consumeONeINputAndEventuallyReturnIt` の例に立ち戻ると、Cont と Done の 2 ステップのみの単純な Iteratee をベタに実装することは出来そうです。しかし、実際のアプリケーションで使うような、大量の入力データのチャンクを、場合によっては条件付きで消費して、最終的に結果を返すような複雑な Iteratee　をするのはなかなかに厄介です。そこで、Play には典型的な Iterateee を作成するためのヘルパーが用意されています。

<!--
### Folding input:
-->
### 入力データの畳込み

<!--
One common task when using iteratees is maintaining some state and altering it each time input is pushed. This type of iteratee can be easily created using the `Iteratee.fold` which has the signature:
-->
Iteratee でよくあるタスクとして、特定の状態を保持して、入力データを受け取るたびにその状態を更新していくような処理があります。この手の Iteratee は `Iteratee.fold` で作成することができます。

```scala
def fold[E, A](state: A)(f: (A, E) => A): Iteratee[E, A]
```

<!--
Reading the signature one can realize that this fold takes an initial state `A`, a function that takes the state and an input chunk `(A, E) => A` and returns an `Iteratee[E,A]` capable of consuming `E`s and eventually returning an `A`. The created iteratee will return `Done` with the computed `A` when an input `EOF` is pushed.
-->
シグネチャの通り、この畳込みを行うヘルパーは初期状態 `A`、状態と入力データのチャンクを引数にとる `(A,E) => A` という関数、入力完了後に `E` を消費して `A` を返すような `Iteratee[E, A]` を引数にとります。作成された Iteratee は `EOF` が入力されたタイミングで、 `A` 型の結果値を含む `Done` 状態を返します。

<!--
One example would be creating an iteratee that counts the number of bytes pushed in:
-->
試しに、入力データのバイト数を数えるような Iteratee を作ってみましょう。

```scala
val inputLength: Iteratee[Array[Byte],Int] = {
  Iteratee.fold[Array[Byte],Int](0) { (length, bytes) => length + bytes.size }
}
```
<!--
Another would be consuming all input and eventually returning it:
-->
さらに別の例として、全ての入力データを結合して、最後にそれを返す、という Iteratee を作ってみましょう。

```scala
val consume: Iteratee[String,String] = {
  Iteratee.fold[String,String]("") { (result, chunk) => result ++ chunk }
}
```

<!--
There is actually already a method in the `Iteratee` object that does exactly this for any scala `TraversableLike`, called `consume`, so our example becomes:
-->
`Iteratee` オブジェクトには、このような Iteratee を任意の `TraversableLike` オブジェクトから生成するための `consume` というヘルパーが用意されています。

```scala
val consume = Iteratee.consume[String]()
```

<!--
One common case is to create an iteratee that does some imperative operation for each chunk of input:
-->
このヘルパーの利用例として、入力データのチャンクそれぞれについて、何らかの手続き的な処理を実行する Iteratee を作ってみましょう。

```scala
val printlnIteratee = Iteratee.foreach[String](s => println(s))
```

<!--
More interesting methods exist like `repeat`, `ignore`, and `fold1` - which is different from the preceding `fold` in that it gives one the opportunity to treat input chunks asychronously.
-->
この他にも `repeat`、 `ignore`、 `fold1` などのヘルパーが用意されています。特に `fold1` は、前述の `fold` と違い、入力データのチャンクを非同期で処理する機能があります。

<!--
Of course one should be worried now about how hard it would be to manually push input into an iteratee by folding over iteratee states over and over again. Indeed each time one has to push input into an iteratee, one has to use the `fold` function to check on its state, if it is a `Cont` then push the input and get the new state, or otherwise return the computed result. That's when `Enumerator`s come in handy.
-->
さて、これまでの説明を読んだ方は、入力データのチャンクを受け取って、それを Iteratee に畳み込む、という手順を何度も何度も行うのはかなり面倒なのではないかと心配されているかもしれません。確かに、 Iteratee を使ってストリーム処理を行うためには、入力データを受け取るたびにそれを Iteratee に渡して、 `fold` 関数により状態をチェックして、状態が `Cont` であれば次のデータを渡して状態を更新し、そうでなければ結果値を返す、という手順を追う必要があります。ご安心ください。次で説明する `Enumerator` は、まさにこのために存在しています。

<!--
> **Next:** [[Enumerators | Enumerators]]
-->
> **次ページ:** [[Enumerators | Enumerators]]
