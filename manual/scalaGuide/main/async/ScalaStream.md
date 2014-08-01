<!-- translated -->
<!--
# Streaming HTTP responses
-->
# HTTP レスポンスのストリーミング

<!--
## Standard responses and Content-Length header
-->
## 標準的なレスポンスと Content-Length ヘッダ

<!--
Since HTTP 1.1, to keep a single connection open to serve several HTTP requests and responses, the server must send the appropriate `Content-Length` HTTP header along with the response. 
-->
HTTP 1.1 以降、複数の HTTP リクエストとレスポンスにまたがって単一のコネクションを使いまわすためには、サーバはレスポンスと一緒に適切な `Content-Length` HTTP ヘッダを送信する必要があります。

<!--
By default, you are not specifying a `Content-Length` header when you send back a simple result, such as:
-->
デフォルトでは、SimpleResult を返却する際、次のように `Content-Length` ヘッダを省略することができます。

```scala
def index = Action {
  Ok("Hello World")
}
```

<!--
Of course, because the content you are sending is well-known, Play is able to compute the content size for you and to generate the appropriate header.
-->
この場合、送信しようとしているコンテンツが明らかなため、Play はコンテンツサイズを自動的に計算して、適切なヘッダを生成することができます。

<!--
> **Note** that for text-based content it is not as simple as it looks, since the `Content-Length` header must be computed according the character encoding used to translate characters to bytes.
-->
> 文字列をバイト列に変換するために使われたエンコーディングにしたがって `Content-Length` をヘッダを計算しなければならないので、このようなテキストベースのコンテンツを返すことは見た目ほど単純なことではないことに **注意** してください。

<!--
Actually, we previously saw that the response body is specified using a `play.api.libs.iteratee.Enumerator`:
-->
ところで、以前、レスポンスボディは `play.api.libs.iteratee.Enumerator` を用いて指定されていることを説明しました。

```scala
def index = Action {
  SimpleResult(
    header = ResponseHeader(200),
    body = Enumerator("Hello World")
  )
}
```

<!--
This means that to compute the `Content-Length` header properly, Play must consume the whole enumerator and load its content into memory. 
-->
これは、`Content-Length` ヘッダを適切に計算するために、 Play は enumerator 全体を消費し、そのコンテンツを全てメモリにロードしなければならないことを意味します。

<!--
## Sending large amounts of data
-->
## 大量のデータ送信

<!--
If it’s not a problem to load the whole content into memory for simple Enumerators, what about large data sets? Let’s say we want to return a large file to the web client.
-->
シンプルな enumerator であればメモリに全て読み込んで処理しても問題ありませんが、データセットが巨大な場合はどうでしょうか？例えば、大きなファイルを web クライアントへ送り返すような場合です。

<!--
Let’s first see how to create an `Enumerator[Array[Byte]]` enumerating the file content:
-->
まず、ファイルのコンテンツを読む `Enumerator[Array[Byte]]` を定義してみましょう。

```scala
val file = new java.io.File("/tmp/fileToServe.pdf")
val fileContent: Enumerator[Array[Byte]] = Enumerator.fromFile(file)
```

<!--
Now it looks simple right? Let’s just use this enumerator to specify the response body:
-->
シンプルですね? 次に、この enumerator を使って、レスポンスボディを指定します。

```scala
def index = Action {

  val file = new java.io.File("/tmp/fileToServe.pdf")
  val fileContent: Enumerator[Array[Byte]] = Enumerator.fromFile(file)    
    
  SimpleResult(
    header = ResponseHeader(200),
    body = fileContent
  )
}
```

<!--
Actually we have a problem here. As we don’t specify the `Content-Length` header, Play will have to compute it itself, and the only way to do this is to consume the whole enumerator content and load it into memory, and then compute the response size.
-->
問題はここです。`Content-Length` ヘッダを指定していないので、 Play 自身がこれを計算しなければならず、これには enumerator のコンテンツを全て消費してメモリにロードし、レスポンスサイズを計算するしか方法がありません。

<!--
That’s a problem for large files that we don’t want to load completely into memory. So to avoid that, we just have to specify the `Content-Length` header ourself.
-->
これは、メモリにロードさせたくないくらい大きなファイルの場合に問題があります。これを避けるためには、`Content-Length` ヘッダを自分で指定する必要があります。

```scala
def index = Action {

  val file = new java.io.File("/tmp/fileToServe.pdf")
  val fileContent: Enumerator[Array[Byte]] = Enumerator.fromFile(file)    
    
  SimpleResult(
    header = ResponseHeader(200, Map(CONTENT_LENGTH -> file.length.toString)),
    body = fileContent
  )
}
```

<!--
This way Play will consume the body enumerator in a lazy way, copying each chunk of data to the HTTP response as soon as it is available.
-->
こうすると、Play は body enumerator の内容をチャンクに分割して少しずつ読み込み、読み込んだチャンクを随侍 HTTP レスポンスにコピーします。

<!--
## Serving files
-->
## ファイルの送信

<!--
Of course, Play provides easy-to-use helpers for common task of serving a local file:
-->
もちろん、Play には、ローカルファイルを送信するヘルパが用意されています。

```scala
def index = Action {
  Ok.sendFile(new java.io.File("/tmp/fileToServe.pdf"))
}
```

<!--
This helper will also compute the `Content-Type` header from the file name, and add the `Content-Disposition` header to specify how the web browser should handle this response. The default is to ask the web browser to download this file by adding the header `Content-Disposition: attachment; filename=fileToServe.pdf` to the HTTP response.
-->
このヘルパはファイル名から `Content-Type` ヘッダを計算してくれます。さらに、 web ブラウザがどのようにこのレスポンスを取り扱うべきかを指定する `Content-Disposition` ヘッダも追加してくれます。デフォルトでは、`Content-Disposition: attahment; filename=fileToServe.pdf` という指定により、 web ブラウザはこのファイルのダウンロードをするかどうかをユーザに確認します。

<!--
You can also provide your own file name:
-->
次のようにすると、ファイル名を指定することもできます。

```scala
def index = Action {
  Ok.sendFile(
    content = new java.io.File("/tmp/fileToServe.pdf"),
    fileName = _ => "termsOfService.pdf"
  )
}
```

<!--
If you want to serve this file `inline`:
-->
ファイルを `inline` で送信したい場合は、次のようにします。

```scala
def index = Action {
  Ok.sendFile(
    content = new java.io.File("/tmp/fileToServe.pdf"),
    inline = true
  )
}
```

<!--
Now you don't have to specify a file name since the web browser will not try to download it, but will just display the file content in the web browser window. This is useful for content types supported natively by the web browser, such as text, HTML or images.
-->
`inline` の場合、 web ブラウザはファイルをダウンロードするのではなく、単にファイルの内容を web ブラウザのウインドウ内に表示するたけなので、ファイル名を指定する必要はありません。これは、テキストや HTML、画像など、Web ブラウザがネイティブ対応しているコンテンツ・タイプのファイルを送信する場合に便利です。

<!--
## Chunked responses
-->
## チャンクレスポンス

<!--
For now, it works well with streaming file content since we are able to compute the content length before streaming it. But what about dynamically computed content, with no content size available?
-->
今のところ、ストリーミングを始める前にコンテンツの長さを計算することができるため、うまくファイルの内容をストリーミングすることができていました。しかし、コンテンツのサイズが事前にわからないような動的に生成されるコンテンツをストリーミングする場合はどうでしょうか？

<!--
For this kind of response we have to use **Chunked transfer encoding**. 
-->
このような種類のレスポンスを返すためには、 **チャンク転送エンコーディング** を利用します。

<!--
> **Chunked transfer encoding** is a data transfer mechanism in version 1.1 of the Hypertext Transfer Protocol (HTTP) in which a web server serves content in a series of chunks. It uses the `Transfer-Encoding` HTTP response header instead of the `Content-Length` header, which the protocol would otherwise require. Because the `Content-Length` header is not used, the server does not need to know the length of the content before it starts transmitting a response to the client (usually a web browser). Web servers can begin transmitting responses with dynamically-generated content before knowing the total size of that content.
> 
> The size of each chunk is sent right before the chunk itself, so that a client can tell when it has finished receiving data for that chunk. Data transfer is terminated by a final chunk of length zero.
>
> <http://en.wikipedia.org/wiki/Chunked_transfer_encoding>
-->
> **チャンク転送エンコーディング** は HTTP 1.1 で定義されているデータ転送メカニズムの一つで、 web サーバがコンテンツをいくつかのチャンクに分けて送信する、というものです。このレスポンスを送信するためには `Content-Length` ヘッダの代わりに `Transfer-Encoding` HTTP レスポンスヘッダを使います。 `Content-Length` ヘッダがないので、サーバはレスポンスをクライアント（通常は web ブラウザ）へ送信し始める前にコンテンツの長さを知る必要はありません。つまり、 web サーバは動的に生成されるコンテンツの最終的な長さを知ることなく、レスポンスを送り始めることができます。
>
> 各チャンクの長さはチャンクの内容の直前に送信されます。これによって、クライアントはチャンクの受信が終わったことを認識できます。最後に、長さがゼロのチャンクを送信すると、データ転送は完了です。
> <http://en.wikipedia.org/wiki/Chunked_transfer_encoding>

<!--
The advantage is that we can serve the data **live**, meaning that we send chunks of data as soon as they are available. The drawback is that since the web browser doesn’t know the content size, it is not able to display a proper download progress bar.
-->
この方式の利点は、データを **ライブ** に提供できる、つまり利用できるようになったデータを即座にチャンクとして送信できることです。欠点は、 web ブラウザがコンテンツサイズを知らないため、ダウンロードプログレスバーを正しく表示できないということです。

<!--
Let’s say that we have a service somewhere that provides a dynamic `InputStream` computing some data. First we have to create an `Enumerator` for this stream:
-->
仮に、あるデータを動的に生成する `InputStream` を提供するサービスがあるとします。まず、その `InputStream` をラップする `Enumerator` を作る必要があります。

```scala
val data = getDataStream
val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(data)
```

<!--
We can now stream these data using a `ChunkedResult`:
-->
ここでは、`ChunkedResult` を使ってこれらのデータをストリームすることができます。

```scala
def index = Action {

  val data = getDataStream
  val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(data)
  
  ChunkedResult(
    header = ResponseHeader(200),
    chunks = dataContent
  )
}
```

<!--
As always, there are helpers available to do this:
-->
いつも通り、これをもっと簡単に記述するヘルパが用意されています。

```scala
def index = Action {

  val data = getDataStream
  val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(data)
  
  Ok.chunked(dataContent)
}
```

<!--
Of course, we can use any `Enumerator` to specify the chunked data:
-->
次のように、 チャンクに分割されたデータを返す際にも、あらゆる `Enumerator` が利用できます。

```scala
def index = Action {
  Ok.chunked(
    Enumerator("kiki", "foo", "bar").andThen(Enumerator.eof)
  )
}
```

<!--
We can inspect the HTTP response sent by the server:
-->
サーバによって送信されたレスポンスを確認してみると、次のようになります。

```
HTTP/1.1 200 OK
Content-Type: text/plain; charset=utf-8
Transfer-Encoding: chunked

4
kiki
3
foo
3
bar
0

```

<!--
We get three chunks followed by one final empty chunk that closes the response.
-->
3 つのチャンクと、レスポンスを閉じる最後の空のチャンクを受信することができました。

<!--
> **Next:** [[Comet sockets | ScalaComet]]
-->
> **次ページ:** [[Comet ソケット | ScalaComet]]
