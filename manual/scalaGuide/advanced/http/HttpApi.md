<!-- translated -->
<!--
# Introduction to Play 2 HTTP API
-->
# Play 2 HTTP API 概論

<!--
## What is EssentialAction?
-->
## EssentialAction とは?

<!--
The EssentialAction is the new simpler type replacing the old Action[A]. To understand EssentialAction we need to understand the Play 2 architecture.
-->
EssentialAction は古い Action[A] に変わる、よりシンプルな新しい型です。 EssentialAction を理解するには Play 2 のアーキテクチャを理解する必要があります。

<!--
The core of Play2 is really small, surrounded by a fair amount of useful APIs, services and structure to make Web Programming tasks easier.
-->
Play 2 は Web プログラミング作業を簡単にするための大量の有用な API やサービス、構造がありますが、それらが囲んでいるコアは本当に小さくなっています。

<!--
Basically, Play2 is an API that abstractly have the following type:
-->
基本的には、 Play 2 は概念的に以下の型を持つ API です。

```scala
RequestHeader -> Array[Byte] -> Result 
```

<!--
The above [computation](http://www.haskell.org/arrows/) takes the request header `RequestHeader`, then takes the request body as `Array[Byte]` and produces a `Result`.
-->
上の [計算](http://www.haskell.org/arrows/) は `RequestHeader` 型のリクエストヘッダを受け取り、さらにリクエストボディを `Array[Byte]` として受け取り `Result` を生成します。

<!--
Now this type presumes putting request body entirely into memory (or disk), even if you only want to compute a value out of it, or better forward it to a storage service like Amazon S3.
-->
この型は、単に入力から計算をしたい場合でも、もしくは Amazon S3 のようなストレージサービスに転送したい場合でも、リクエストボディ全体をメモリ (もしくはディスク) に置く事が前提となってしまいます。

<!--
We rather want to receive request body chunks as a stream and be able to process them progressively if necessary.
-->
むしろ、リクエストボディの断片をストリームとして受け取り、必要であれば段階的に処理できるようにする事が望まれます。

<!--
What we need to change is the second arrow to make it receive its input in chunks and eventually produce a result. There is a type that does exactly this, it is called `Iteratee` and takes two type parameters.
-->
二つ目の矢印で入力を断片として受け取り、結果を生成ようにする事が必要です。まさにこれをするクラスがあります。それは `Iteratee` と呼ばれ、二つの型パラメータを持ちます。

<!--
`Iteratee[E,R]` is a type of [arrow](http://www.haskell.org/arrows/) that will take its input in chunks of type `E` and eventually return `R`. For our API we need an Iteratee that takes chunks of `Array[Byte]` and eventually return a `Result`. So we slightly modify the type to be:
-->
`Iteratee[E,R]` は `E` 型の断片を入力として受け取り、最終的に `R` を返す [矢印](http://www.haskell.org/arrows/) 型です。Play の API では、断片化された `Array[Byte]` を受け取り、最終的に `Result`を返す Iteratee が必要です。そのため、以下のように型を少しだけ変えます:

```scala
RequestHeader -> Iteratee[Array[Byte],Result]
```

<!--
For the first arrow, we are simply using the Function[From,To] which could be type aliased with `=>`:
-->
最初の矢印では `=>` の別名を持つ Function[From,To] をそのまま使います。

```scala
RequestHeader => Iteratee[Array[Byte],Result]
```

<!--
Now if I define an infix type alias for `Iteratee[E,R]`:
-->
ここでもし `Iteratee[E,R]` の別名の中置記法での型を定義した場合、

<!--
`type ==>[E,R] = Iteratee[E,R]` then I can write the type in a funnier way:
-->
`type ==>[E,R] = Iteratee[E,R]` となり、さらに面白い方法で型を書くことができます:

```scala
RequestHeader => Array[Byte] ==> Result
```

<!--
And this should read as: Take the request headers, take chunks of `Array[Byte]` which represent the request body and eventually return a `Result`. This exactly how the `EssentialAction` type is defined:
-->
そしてこれは以下のように読めます: リクエストヘッダを受け取り、リクエストボディを表す `Array[Byte]` を断片として受け取り、最終的に `Result` を返す、と。これがまさに `EssentialAction` 型の定義です:

```scala
trait EssentialAction extends (RequestHeader => Iteratee[Array[Byte], Result])
```

<!--
The `Result` type, on the other hand, can be abstractly thought of as the response headers and the body of the response:
-->
一方 `Result` 型は概念的にはレスポンスヘッダとレスポンスボディとして考えることができます:

```scala
case class Result(headers: ResponseHeader, body:Array[Byte])
```

<!--
But, what if we want to send the response body progressively to the client without filling it entirely into memory. We need to improve our type. We need to replace the body type from an `Array[Byte]` to something that produces chunks of `Array[Byte]`. 
-->
しかし、もしレスポンスボディの全てをメモリに格納せずに、段階的にクライアントに送りたい場合はどうすれば良いでしょうか。型を改善する必要があります。ボディの型を `Array[Byte]` ではなく、断片化された `Array[Byte]` を生成する何かに置き換える必要があります。

<!--
We already have a type for this and is called `Enumerator[E]` which means that it is capable of producing chunks of `E`, in our case `Enumerator[Array[Byte]]`: 
-->
このための `Enumerator[E]` と呼ばれる型があり、断片化された `E` を生成する事が出来ます。 Play の場合は `Enumerator[Array[Byte]]` になります。

```scala
case class Result(headers:ResponseHeaders, body:Enumerator[Array[Byte]])
```

<!--
If we don't have to send the response progressively we still can send the entire body as a single chunk.
-->
もしレスポンスを段階的に送る必要がない場合は、ボディ全体を一つの断片として送ることができます。

<!--
We can stream and write any type of data to socket as long as it is convertible to an `Array[Byte]`, that is what `Writeable[E]` insures for a given type 'E':
-->
`Array[Byte]` に変換できれば、言い換えると `E` に対してそれを補償する `Writeable[E]` があれば、あらゆるタイプのデータをソケットに書き出すことができます。

```scala
case class Result[E](headers:ResponseHeaders, body:Enumerator[E])(implicit writeable:Writeable[E])
```

<!--
## Bottom Line
-->
## 要点

<!--
The essential Play2 HTTP API is quite simple:
-->
Play 2 の原始的な HTTP API はとてもシンプルです。

```scala
RequestHeader -> Iteratee[Array[Byte],Result]
```
<!--
or the funnier
-->
さらに面白く書くと、

```scala
RequestHeader => Array[Byte] ==> Result
```

<!--
Which reads as the following: Take the `RequestHeader` then take chunks of `Array[Byte]` and return a response. A response consists of `ResponseHeaders` and a body which is chunks of values convertible to `Array[Byte]` to be written to the socket represented in the `Enumerator[E]` type.
-->
以下のように読みます: `RequestHeader` を受け取り、 `Array[Byte]` の断片を受け取り、そしてレスポンスを返す。レスポンスは `ResponseHeaders` とボディからなり、ボディはソケットに書き出される `Array[Byte]` へと変換出来る値の断片であり、 `Enumerator[E]` として表現されます。

<!-- > **Next:** [[HTTP Filters | ScalaHttpFilters]] -->
> **次ページ:** [[HTTP フィルター | ScalaHttpFilters]]