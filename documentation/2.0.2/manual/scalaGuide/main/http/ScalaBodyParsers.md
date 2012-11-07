<!-- translated -->
<!--
# Body parsers
-->
# ボディパーサー

<!--
## What is a body parser?
-->
## ボディパーサーの概要

<!--
An HTTP PUT or POST request contains a body. This body can use any format, specified in the `Content-Type` request header. In Play, a **body parser** transforms this request body into a Scala value. 
-->
HTTP PUT や POST リクエストはボディを含みます。このボディは `Content-Type` リクエストヘッダで指定さえしておけば、どんなフォーマットであっても構いません。Play では **ボディパーサー** がリクエストボディを Scala の値に変換します。

<!--
However the request body for an HTTP request can be very large and a **body parser** can’t just wait and load the whole data set into memory before parsing it. A `BodyParser[A]` is basically an `Iteratee[Array[Byte],A]`, meaning that it receives chunks of bytes (as long as the web browser uploads some data) and computes a value of type `A` as result.
-->
しかし、HTTP リクエストのリクエストボディはとても大きなサイズになる可能性があり、 **ボディパーサー** が全てのデータセットがメモリにロードされるのを単純に待ってからパースを行うというのは現実的ではありません。`BodyParser[A]` は基本的に `Iteratee[Array[Byte],A]` です。これは、ボディパーサーはバイトデータの塊を（webブラウザがデータをアップロードし続ける限り）入力として受け取り、結果として `A` 型の値を計算する、ということを意味します。

<!--
Let’s consider some examples.
-->
いくつか例を見てみましょう。

<!--
- A **text** body parser could accumulate chunks of bytes into a String, and give the computed String as result (`Iteratee[Array[Byte],String]`).
- A **file** body parser could store each chunk of bytes into a local file, and give a reference to the `java.io.File` as result (`Iteratee[Array[Byte],File]`).
- A **s3** body parser could push each chunk of bytes to Amazon S3 and give a the S3 object id as result (`Iteratee[Array[Byte],S3ObjectId]`).
-->
- **text** ボディパーサーはバイトデータの塊を String に積み上げていき、計算された String を結果として返します(`Iteratee[Array[Byte],STring]`)。
- **file** ボディパーサーはバイトデータの塊をローカルファイルに保存し、`java.io.File` への参照を結果として返します(`Iteratee[Array[Byte],File]`)。
- **s3** ボディパーサーはバイトデータの塊を Amazon S3 へ保存し、S3 オブジェクトの ID を結果として返します(`Iteratee[Array[Byte],S3ObjectId]`)。

<!--
Additionally a **body parser** has access to the HTTP request headers before it starts parsing the request body, and has the opportunity to run some precondition checks. For example, a body parser can check that some HTTP headers are properly set, or that the user trying to upload a large file has the permission to do so.
-->
これらに加えて、**ボディパーサー** はリクエストボディのパースを始める前に HTTP リクエストヘッダを参照して、いくつか事前条件のチェックをすることがあります。例えば、特定の HTTP ヘッダが正しくセットされていることをチェックしたり、ユーザが大きなファイルをアップロードしようとしたときに本当にその権限を持っているのかチェックする、というようなボディーパーサーが考えられます。

<!--
> **Note**: That's why a body parser is not really an `Iteratee[Array[Byte],A]` but more precisely a `Iteratee[Array[Byte],Either[Result,A]]`, meaning that it has the opportunity to send directly an HTTP result itself (typically `400 BAD_REQUEST`, `412 PRECONDITION_FAILED` or `413 REQUEST_ENTITY_TOO_LARGE`) if it decides than it is not able to compute a correct value for the request body
-->
> **ノート**: これがボディパーサーが厳密には `Iteratee[Array[Byte],A]` ではなく `Iteratee[Array[Byte],Either[Result,A]]` であることの理由です。つまり、リクエストボディを元に適切な値を計算できないと判断した場合、ボディパーサー自身が直接的に HTTP レスポンスを送信することがあります（よくあるのは `400 BAD_REQUEST`, `412 PRECONDITION_FAILED`, `413 REQUEST_ENTITY_TOO_LARGE` です）。

<!--
Once the body parser finishes its job and gives back a value of type `A`, the corresponding `Action` function is executed and the computed body value is passed into the request.
-->
ボディパーサーは処理を終えると即座に `A` 型の値を返却し、その後に対応する `Action` 関数が実行され、計算されたボディの値がリクエストに渡されます。

<!--
## More about Actions
-->
## アクションについての詳細

<!--
Previously we said that an `Action` was a `Request => Result` function. This is not entirely true. Let’s have a more precise look at the `Action` trait:
-->
以前、`Action` は `Request => Result` 型の関数だと説明しました。しかし、これは厳密には正しくありません。`Action` トレイトをより正確に見てみましょう。

```scala
trait Action[A] extends (Request[A] => Result) {
  def parser: BodyParser[A]
}
```

<!--
First we see that there is a generic type `A`, and then that an action must define a `BodyParser[A]`. With `Request[A]` being defined as:
-->
まず、ジェネリック型 `A` が存在すること、そしてアクションは `BodyParser[A]` を定義しなければならないことがわかります。`Request[A]` は次のように定義されています。

```scala
trait Request[+A] extends RequestHeader {
  def body: A
}
```

<!--
The `A` type is the type of the request body. We can use any Scala type as the request body, for example `String`, `NodeSeq`, `Array[Byte]`, `JsonValue`, or `java.io.File`, as long as we have a body parser able to process it.
-->
`A` はリクエストボディの型です。例えば、`String`, `NodeSeq`, `Array[Byte]`, `JsonValue`, `java.io.File` など、その型を処理できるボディパーサーが定義されていれてさえいれば、あらゆる Scala の型をリクエストボディの型として指定できます。

<!--
To summarize, an `Action[A]` uses a `BodyParser[A]` to retrieve a value of type `A` from the HTTP request, and to build a `Request[A]` object that is passed to the action code. 
-->
まとめると、`Action[A]` は `BodyParser[A]` であり、HTTP リクエストから `A` 型の値を受け取り、アクションのコードに渡される `Request[A]` 型のオブジェクトを組み立てます。

<!--
## Default body parser: AnyContent
-->
## デフォルトのボディパーサー： AnyContent

<!--
In our previous examples we never specified a body parser. So how can it work? If you don’t specify your own body parser, Play will use the default, which processes the body as an instance of `play.api.mvc.AnyContent`.
-->
前の例では、ボディパーサーを明示的に指定していませんでした。一体なぜ動いたのでしょうか? 実は、ボディパーサーを自分で指定しなかった場合、Play はボディを `play.api.mvc.AnyContent` として処理するデフォルトのボディパーサーを使います。

<!--
This body parser checks the `Content-Type` header and decides what kind of body to process:
-->
このボディパーサーは `Content-Type` ヘッダの内容に応じて、ボディを何として処理すべきかを決定します。

<!--
- **text/plain**: `String`
- **application/json**: `JsValue`
- **text/xml**: `NodeSeq`
- **application/form-url-encoded**: `Map[String, Seq[String]]`
- **multipart/form-data**: `MultipartFormData[TemporaryFile]`
- any other content type: `RawBuffer`
-->
- **text/plain**: `String`
- **application/json**: `JsValue`
- **text/xml**: `NodeSeq`
- **application/form-url-encoded**: `Map[String, Seq[String]]`
- **multipart/form-data**: `MultipartFormData[TemporaryFile]`
- その他の Content-Type: `RawBuffer`

<!--
For example:
-->
例えば、以下のように利用します。

```scala
def save = Action { request =>
  val body: AnyContent = request.body
  val textBody: Option[String] = body.asText 
  
  // Expecting text body
  textBody.map { text =>
    Ok("Got: " + text)
  }.getOrElse {
    BadRequest("Expecting text/plain request body")  
  }
}
```

<!--
## Specifying a body parser
-->
## ボディパーサーの指定

<!--
The body parsers available in Play are defined in `play.api.mvc.BodyParsers.parse`.
-->
Play で利用できるボディパーサーは `play.api.mvc.BodyParsers.parse` に定義されています。

<!--
So for example, to define an action expecting a text body (as in the previous example):
-->
例えば、(前の例と同様に) テキストのボディを受け取るようなアクションは次のように定義します。

```scala
def save = Action(parse.text) { request => 
   Ok("Got: " + request.body) 
} 
```

<!--
Do you see how the code is simpler? This is because the `parse.text` body parser already sent a `400 BAD_REQUEST` response if something went wrong. We don’t have to check again in our action code, and we can safely assume that `request.body` contains the valid `String` body.
-->
コードがどれくらいシンプルになったかお分かりでしょうか? この理由は、`parse.text` ボディパーサーが何か問題を見つけた時に `400 BAD_REQUEST` レスポンスを返してくれるからです。自分のコードで再度チェックする必要がなく、`request.body` が間違いなく `String` 型のボディであることも保証されます。

<!--
Alternatively we can use:
-->
また、以下のように記述することもできます。

```scala
def save = Action(parse.tolerantText) { request =>
  Ok("Got: " + request.body)
}
```

<!--
This one doesn't check the `Content-Type` header and always loads the request body as a `String`.
-->
この方法では、`Content-Type` ヘッダの内容に関わらず、リクエストボディは常に `String` としてロードされます。

<!--
> **Tip:** There is a `tolerant` fashion provided for all body parsers included in Play.
-->
> **Tip:** Play に含まれるすべてのボディパーサーに、同じような `tolerant` 版が用意されています。

<!--
Here is another example, which will store the request body in a file:
-->
次の例では、リクエストボディをファイルとして保存します。

```scala
def save = Action(parse.file(to = new File("/tmp/upload"))) { request =>
  Ok("Saved the request content to " + request.body)
}
```

<!--
## Combining body parsers
-->
## ボディーパーサーの合成

<!--
In the previous example, all request bodies are stored in the same file. This is a bit problematic isn’t it? Let’s write another custom body parser that extract the user name from the request Session, to give a unique file for each user:
-->
前の例では、全てのリクエストボディは同じファイルに保存されます。これは問題だと思いませんか? そこで、リクエストのセッションからユーザ名を抽出して、ユーザ毎にユニークなファイルを使うようなボディパーサーを自作してみましょう。

```scala
val storeInUserFile = parse.using { request =>
  request.session.get("username").map { user =>
    file(to = new File("/tmp/" + user + ".upload"))
  }.getOrElse {
    error(Unauthorized("You don't have the right to upload here"))
  }
}

def save = Action(storeInUserFile) { request =>
  Ok("Saved the request content to " + request.body)  
}
```

<!--
> **Note:** Here we are not really writing our own BodyParser, but just combining existing ones. This is often enough and should cover most use cases. Writing a `BodyParser` from scratch is covered in the advanced topics section.
-->
> **Note:** ここでは全く新しい BodyParser を定義することはせずに、既存のものを組み合わせました。大抵はこの方法で必要十分であり、ほとんどのユースケースをカバーできるはずです。`BodyParser` をフルスクラッチで記述する方法については、本ドキュメントの上級者向けの節でご説明します。

<!--
## Max content length
-->
## 最大 content length

<!--
Text based body parsers (such as **text**, **json**, **xml** or **formUrlEncoded**) use a maximum content length because they have to load all of the content into memory. 
-->
テキストベースのボディパーサー (**text**, **json**, **xml**, **formUrlEncoded** のような。) は全てのコンテンツを一旦メモリにロードする必要があるため、最大 content length が設定されています。

<!--
There is a default content length (the default is 100KB), but you can also specify it inline:
-->
デフォルトでは content length は 100KB ですが、コード中で指定することもできます。

```scala
// Accept only 10KB of data.
def save = Action(parse.text(maxLength = 1024 * 10)) { request =>
  Ok("Got: " + text)
}
```

<!--
> **Tip:** The default content size can be defined in `application.conf`:
> 
> `parsers.text.maxLength=128K`
-->
> **Tip:** デフォルトの content length は `application.conf` から次のように定義できます。
>
> `parsers.text.maxLength=128K`

<!--
You can also wrap any body parser with `maxLength`:
-->
`maxLength` であらゆるボディパーサーをラップすることもできます。

```scala
// Accept only 10KB of data.
def save = Action(maxLength(1024 * 10, parser = storeInUserFile)) { request =>
  Ok("Saved the request content to " + request.body)  
}
```

<!--
> **Next:** [[Action composition | ScalaActionsComposition]]
-->
> **Next:** [[アクションの合成 | ScalaActionsComposition]]
