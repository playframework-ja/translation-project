<!-- translated -->
<!--
# Handling data streams reactively
-->
# 反応的なストリーム処理

<!--
Progressive Stream Processing and manipulation is an important task in modern Web Programming, starting from chunked upload/download to Live Data Streams consumption, creation, composition and publishing through different technologies including Comet and WebSockets.
-->
現代の Web プログラミングにおいて、ストリーム処理は重要なタスクです。これには、チャンク単位のデータダウンロード/アップロードや、 Comet や WebSocket など様々な技術を利用したデータストリームのリアルタイム処理、作成、合成、提供などが含まれます。

<!--
Iteratees provide a paradigm and an api allowing this manipulation while focusing on several important aspects:
-->
Iteratee はこのようなストリーム処理を実現する考え方と API です。 Iteratee の主な特徴は次のとおりです。

<!--
* Allowing user to create, consume and transform streams of data.
* Treating different data sources in the same manner (Files on disk, Websockets, Chunked Http, Data Upload, ...).
* Composable: use a rich set of adapters and transformers to change the shape of the source or the consumer; construct your own or start with primitives.
* Having control over when it is enough data sent and be informed when source is done sending data.
* Non blocking, reactive and allowing control over resource consumption (Thread, Memory)
-->
* データのストリームを生成、処理、変換することができる
* 様々なデータを同じような形式で扱える (例えば、ディスク上のファイル、WebSocket、Chunked HTTP、データアップロードなど)
* 合成可能: ストリームのソースやコンシューマを、別の型のソースやコンシューマに変換するためのアダプタや変換器が豊富に用意されている
* 受信完了とするために必要なデータの定義を細かくコントロールできる。また、ソースがデータ送信を完了した際に通知を受けることができる
* ノン・ブロッキング、リアクティブで、かつ (Thread、Memoryなどの) リソース消費をコントロールできる

<!--
## Iteratees
-->
## Iteratee

<!--
An Iteratee is a consumer, it describes the way input will be consumed to produce some value. Iteratee is a consumer that returns a value it computes after being fed enough input.
-->
Iteratee はデータのコンシューマーつまり、消費者ーであるといえます。Iteratee は入力データの処理方法および結果の生成方法が記述されます。Iteratee は十分な入力データを受け取ると、それに対して何らかの計算を行い結果値を返します。

```scala
// an iteratee that consumes chunkes of String and produces an Int
Iteratee[String,Int] 
```

<!--
The Iteratee interface `Iteratee[E,A]` takes two type parameters, `E` representing the type of the Input it accepts and `A` the type of the calculated result.
-->
Iteratee のインタフェースは `[Iteratee[E, A]]` のように二つの型パラメータを取ります。`E` は入力データの型、`A` は結果値の型です。

<!--
An iteratee has one of three states, `Cont` meaning accepting more input, `Error` to indicate an error state and `Done` which carries the calculated result. These three states are defined by the `fold` method of an `Iteratee[E,A]` interface:
-->
Iteratee は 3 つの状態を持ちます。それぞれ、`Cont` はさらに入力データを受付可能であること、`Error` はエラーにより入力を停止したこと、`Done` は計算結果が出ていることを表します。これら 3 つのステータスは `Iteratee[E,A]` インタフェースの `fold` メソッドにより定義することができます。

```scala
def fold[B](
  done: (A, Input[E]) => Promise[B],
  cont: (Input[E] => Iteratee[E, A]) => Promise[B],
  error: (String, Input[E]) => Promise[B]
): Promise[B]
```

<!--
The fold method defines an iteratee as one of the three mentioned states. It accepts three callback functions and will call the appropriate one depending on its state to eventually extract a required value. When calling `fold` on an iteratee you are basically saying:
-->
fold メソッドは、 Iteratee をこれら３つの状態のいずれかに定義します。fold メソッドは 3 つのコールバック関数を引数にとり、状態に応じていずれか一つを呼び出し、最終的には結果値を返します。Iteratee に対する `fold` の呼び出しは、次のような意味になります。

<!--
- If the iteratee is the state `Done`, then I'd take the calculated result of type `A` and what is left from the last consumed chunk of input `Input[E]` and eventually produce a `B`
- If the iteratee is the state `Cont`, then I'd take the provided continuation (which is accepting an input) `Input[E] => Iteratee[E,A]` and eventually produce a `B`. Note that this state provides the only way to push input into the iteratee, and get a new iteratee state, using the provided continuation function. 
- If the iteratee is the state `Error`, then I'd take the error message of type `String` and the input that caused it and eventually produce a B.
-->
- Iteratee が `Done` 状態であれば、 `A` という型の計算結果と `Input[E]` という型のこれから消費される入力データの最後のチャンク  を元に、 `B` という型の値を生成します。
- Iteratee が `Cont` 状態であれば、`Input[E] => Iteratee[E,A]` という型の継続 (入力を待ち受ける) を使って、最終的には `B` という型の値を生成します。この状態が Iteratee にデータを入力する唯一のタイミングであり、データを入力後は提供された継続を使って新しい状態の Iteratee を返します。
- Iteratee が `Error` 状態であれば、`String` 型のエラーメッセージと、エラーの原因となった入力データを元に、 `B` 型の値を生成します。

<!--
Obviously, depending on the state of the iteratee, `fold` will produce the appropriate `B` using the corresponding passed function.
-->
Iteratee の状態に依存して、`fold` は引数に渡された関数のいずれかを呼び出して、適切な `B` 型の値を生成します。

<!--
To sum up, iteratee consists of 3 states, and `fold` provides the means to do something useful with the state of the iteratee.
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
- `Promise[A]` represents, as its name tells, a promise of value of type `A`. This means that it will eventually be redeemed with a value of type `A` and you can schedule a callback, among other things you can do, if you are interested in that value. A promise is a very nice primitive for synchronization and composing async calls, and is explained further at the [[PromiseScala | ScalaAsync]] section.
-->
- `Promise[A]` は、その名の通り、 `A` 型の値の Promise です。`Promise[A]` は、将来的に `A` 型の値が与えられた時に redeem（訳注：約束を果たす、という意味）されて、事前に登録されたコールバック関数やその他の処理を呼び出します。Promise は並列処理を同期させたり、非同期処理を合成する際に便利なデータ構造です。Promise についての詳細は [[非同期処理 | ScalaAsync]] を参照してください。

<!--
### Some primitive iteratees:
-->
### 基本的な Iteratee

<!--
By implementing the iteratee, and more specifically its fold method, we can now create some primitive iteratees that we can use later on.
-->
Iteratee や、より具体的にはその fold メソッドを定義することで、後々に再利用できる基本的な Iteratee を作成することができます。

<!--
- An iteratee in the `Done` state producing an `1:Int` and returning `Empty` as left from last `Input[String]`
-->
- `Input[String]` を読み飛ばした上で `1:Int` を生成して `Empty` を返すような `Done` 状態の Iteratee

```scala
val doneIteratee = new Iteratee[String,Int] {
  def fold[B](
    done: (A, Input[E]) => Promise[B],
    cont: (Input[E] => Iteratee[E, A]) => Promise[B],
    error: (String, Input[E]) => Promise[B]): Promise[B] = done(1,Input.Empty)
}
```

<!--
As shown above, this is easily done by calling the appropriate callback function, in our case `done`, with the necessary information.
-->
上記のとおり、このような Iteratee は、この例における `done` のような適切なコールバック関数に必要な引数を渡すだけで実装することができます。

<!--
To use this iteratee we will make use of the `Promise.pure` that is a promise already in the Redeemed state.
-->
この Iteratee を利用するためには、`Promise.pure` という生成した時点で Redeemed 状態になる Promise の一種を使います。

```scala
val eventuallyMaybeResult: Promise[Option[Int]] = {
  doneIteratee.fold(
  
    // if done return the computed result
    (a,in) => Promise.pure(Some(a)),

    //if continue return None
    k => Promise.pure(None),

    //on error return None
    (msg,in) => Promise.pure(None) 
  ) 
}
```

<!--
of course to see what is inside the `Promise` when it is redeemed we use `onRedeem`
-->
redeem されたときに `Promise` の中身を取得するためには、 `onRedeem` を使います。

```scala
// will eventually print 1
eventuallyMaybeResult.onRedeem(i => println(i)) 
```

<!--
There is already a built-in way allowing us to create an iteratee in the `Done` state by providing a result and input, generalizing what is implemented above:
-->
上記の実装をもっと汎用的にしてみましょう。Play には結果と入力値から `Done` 状態の Iteratee を作るヘルパーが用意されています。

```scala
val doneIteratee = Done[Int,String](1, Input.Empty)
```

<!--
Creating a `Done` iteratee is simple, and sometimes useful, but it obviously does not consume any input. Let's create an iteratee that consumes one chunk and eventually returns it as the computed result:
-->
`Done` Iteratee はこの通りとても簡単に作成でき、役に立つケースも無くはないのですが、ご覧のとおり入力データを一切消費してくれません。次は、入力データのチャンクをひとつ消費して、最終的にそのチャンクを結果値として返すような Iteratee を作ってみましょう。

```scala
val consumeOneInputAndEventuallyReturnIt = new Iteratee[String,Int] {
    
  def fold[B](
    done: (Int, Input[String]) => Promise[B],
    cont: (Input[String] => Iteratee[String, Int]) => Promise[B],
    error: (String, Input[String]) => Promise[B]
  ): Promise[B] = {
        
    cont(in => Done(in, Input.Empty))
      
  }
  
}
```

<!--
As for `Done` there is a built-in way to define an iteratee in the `Cont` state by providing a function that takes `Input[E]` and returns a state of `Iteratee[E,A]` :
-->
`Done` の場合と同様に、Play には `Cont` 状態の Iteratee を作るためのヘルパーも用意されています。このヘルパーは、 `Input[E]` の値を引数にとって、 `Iteratee[E,A]` を返します。

```scala
val consumeOneInputAndEventuallyReturnIt = {
  Cont[String,Int](in => Done(in,Input.Empty))
}
```

<!--
In the same manner there is a built-in way to create an iteratee in the `Error` state by providing and error message and an `Input[E]`
-->
さらに、`Error` 状態についても、`Input[E]` とエラーメッセージを渡すことで `Error` 状態の Iteratee を作成できるヘルパーが用意されています。

<!--
Back to the `consumeOneInputAndEventuallyReturnIt`, it is possible to create a two step simple iteratee manually but it becomes harder and cumbersome to create any real world iteratee capable of consuming a lot of chunks before, possibly conditionally, it eventually returns a result. Luckily there are some built-in methods to create common iteratee shapes in the `Iteratee` object.
-->
`consumeOneInputAndEventuallyReturnIt` の例に立ち戻ると、Cont と Done の 2 ステップのみの単純な Iteratee をベタに実装することは出来そうです。しかし、実際のアプリケーションで使うような、大量の入力データのチャンクを、場合によっては条件付きで消費して、最終的に結果を返すような複雑な Iteratee　をするのはなかなかに厄介です。そこで、Play には典型的な Iterateee を作成するためのヘルパーが用意されています。

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
Reading the signature one can realize that this fold takes an initial state `A`, a function that takes the state and an input chunk `(A, E) => A` and returning an `Iteratee[E,A]` capable of consuming `E`s and eventually returning an `A`. The created iteratee will return `Done` with the computed `A` when an input `EOF` is pushed.
-->
シグネチャの通り、この畳込みを行うヘルパーは初期状態 `A`、状態と入力データのチャンクを引数にとる `(A,E) => A` という関数、入力完了後に `E` を消費して `A` を返すような `Iteratee[E, A]` を引数にとります。作成された Iteratee は `EOF` が入力されたタイミングで、 `A` 型の結果値を含む `Done` 状態を返します。

<!--
One example can be creating an iteratee that counts the number of bytes pushed in:
-->
試しに、入力データのバイト数を数えるような Iteratee を作ってみましょう。

```scala
val inputLength: Iteratee[Array[Byte],A] = {
  Iteratee.fold[Array[Byte],Int](0) { (length, bytes) => length + bytes.size }
}
```
<!--
Another could be consuming all input and eventually returning it:
-->
さらに別の例として、全ての入力データを結合して、最後にそれを返す、という Iteratee を作ってみましょう。

```scala
val consume: Iteratee[String,String] = {
  Iteratee.fold[String,String]("") { (result, chunk) => result ++ chunk }
}
```

<!--
There is actually already a method in `Iteratee` object that does exactly this for any scala `TraversableLike` called `consume`, so our example becomes:
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
More interesting methods exist like `repeat`, `ignore` and `fold1` which is different from the preceeding `fold` in offering the opportunity to be asynchronous in treating input chunks.
-->
この他にも `repeat`、 `ignore`、 `fold1` などのヘルパーが用意されています。特に `fold1` は、前述の `fold` と違い、入力データのチャンクを非同期で処理する機能があります。

<!--
Of course one should be worried now about how hard would it be to manually push input into an iteratee by folding over iteratee states over and over again. Indeed each time one has to push input into an iteratee, one has to use the `fold` function to check on its state, if it is a `Cont` then push the input and get the new state or otherwise return the computed result. That's when `Enumerator`s come in handy.
-->
さて、これまでの説明を読んだ方は、入力データのチャンクを受け取って、それを Iteratee に畳み込む、という手順を何度も何度も行うのはかなり面倒なのではないかと心配されているかもしれません。確かに、 Iteratee を使ってストリーム処理を行うためには、入力データを受け取るたびにそれを Iteratee に渡して、 `fold` 関数により状態をチェックして、状態が `Cont` であれば次のデータを渡して状態を更新し、そうでなければ結果値を返す、という手順を追う必要があります。ご安心ください。次で説明する `Enumerator` は、まさにこのために存在しています。

<!--
> **Next:** [[Enumerators | Enumerators]]
-->
> **次ページ:** [[Enumerators | Enumerators]]
