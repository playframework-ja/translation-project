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
HTTP 1.1 から、1 コネクションで複数の HTTP リクエストとレスポンスをやり取りするために、サーバは適切な `Content-Length` HTTP ヘッダをレスポンスと共に返すことになっています。

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
> **ノート** テキストベースなコンテンツの場合、文字コードに応じて `Content-Length` ヘッダの値を計算しなければならないため、実は見た目ほど話は簡単ではないのです。

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
これが何を意味するかというと、`Content-Length` ヘッダを適切に計算するために、 Play はまず enumerator とそのコンテンツを全てメモリにロードしなければならないということです。

<!--
## Sending large amounts of data
-->
## 大量のデータ送信

<!--
If it’s not a problem to load the whole content into memory for simple Enumerators, what about large data sets? Let’s say we want to return a large file to the web client.
-->
メモリにロードする Enumerator のデータ量が少ない場合は問題ないとして、データ量が多い場合はどうでしょうか？仮に、大きなファイルを web クライアントに返したいとします。

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
問題はここです。私達は `Content-Length` ヘッダを指定していないので、 Play が代わりに計算しなければならなくなります。Play はまず enumerator のコンテンツを全てメモリにロードします。そうして初めて、レスポンスサイズを計算することができます。

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
このヘルパーはファイル名から `Content-Type` ヘッダを計算し、web ブラウザがレスポンスをどのように処理するか指定する `Content-Disposition` ヘッダも追加してくれます。デフォルトでは、`Content-Disposition: attachment; filename=fileToServe.pdf` のようなヘッダを HTTP レスポンスに追加して、web ブラウザがユーザにファイルをダウンロードするかどうか尋ねさせます。

<!--
You also provide your own file name:
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
## Chunked レスポンス

<!--
For now, it works well with streaming file content since we are able to compute the content length before streaming it. But what about dynamically computed content, with no content size available?
-->
これまで説明した通り、ファイルを送信する場合、content length を事前に計算することができるので、ストリーミングが機能します。しかし、コンテンツサイズが存在しない、動的に生成されるコンテンツの場合はどうでしょうか？

<!--
For this kind of response we have to use **Chunked transfer encoding**. 
-->
この種類のレスポンスのために、 **Chunked transfer encoding** を使う必要があります。

<!--
> **Chunked transfer encoding** is a data transfer mechanism in version 1.1 of the Hypertext Transfer Protocol (HTTP) in which a web server serves content in a series of chunks. It uses the `Transfer-Encoding` HTTP response header instead of the `Content-Length` header, which the protocol would otherwise require. Because the `Content-Length` header is not used, the server does not need to know the length of the content before it starts transmitting a response to the client (usually a web browser). Web servers can begin transmitting responses with dynamically-generated content before knowing the total size of that content.
> 
> The size of each chunk is sent right before the chunk itself, so that a client can tell when it has finished receiving data for that chunk. Data transfer is terminated by a final chunk of length zero.
>
> [[http://en.wikipedia.org/wiki/Chunked_transfer_encoding]]
-->
> **Chunked transfer encoding** は HTTP 1.1 のデータ転送メカニズムで、web サーバがコンテンツを一連の chunk に分割して送信する、というものです。**Chunked transfer encoding** では、`Content-Length` ヘッダの代わりに、`Transfer-Encoding` ヘッダを指定します。`Content-Length` ヘッダが指定されないので、サーバは レスポンスをクライアントへ送信する際に content length を前もって知る必要がありません。Web サーバは動的に生成されるコンテンツの合計サイズを知ることなく、レスポンスを送信し始めることができます。
>
> 各々の chunk のサイズは、chunk の内容の直前に送信されます。そのため、クライアントは chunk の受信を完了したことをサーバに通知することができます。最終的に長さ 0 のチャンクを受信すると、データ転送が終了します。
>
> [[http://en.wikipedia.org/wiki/Chunked_transfer_encoding]]

<!--
The advantage is that we can serve the data **live**, meaning that we send chunks of data as soon as they are available. The drawback is that since the web browser doesn’t know the content size, it is not able to display a proper download progress bar.
-->
この方法の利点は、データを **ライブ** 配信ーつまり、データのチャンクを生成され次第送信ーできるということです。欠点は、web ブラウザがコンテンツサイズを知らないため、ダウンロードのプログレスバーを正しく表示できないということです。

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
  
  Ok.stream(dataContent)
}
```

<!--
Of course, we can use any `Enumerator` to specify the chunked data:
-->
次のように、 チャンクに分割されたデータを返す際にも、あらゆる `Enumerator` が利用できます。

```scala
def index = Action {
  Ok.stream(
    Enumerator("kiki", "foo", "bar").andThen(Enumerator.eof)
  )
}
```

<!--
> **Tip:** `Enumerator.callbackEnumerator` and `Enumerator.pushEnumerator` are two convenient way of creating reactive non-blocking enumerators in an imperative style.
-->
> **Tip:** `Enumerator.callbackEnumerator` と `Enumerator.pushEnumerator` という二つのヘルパは、reactive (反応的) かつノン・ブロッキングな enumerator を手続き型のスタイルで定義するのに役立ちます。

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