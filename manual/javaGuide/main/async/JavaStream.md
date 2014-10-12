<!--
# Streaming HTTP responses
-->
# HTTP レスポンスのストリーミング

<!--
## Standard responses and Content-Length header
-->
## 標準的なレスポンスと Content-length ヘッダ

Since HTTP 1.1, to keep a single connection open to serve several HTTP requests and responses, the server must send the appropriate `Content-Length` HTTP header along with the response. 

<!--
By default, when you send a simple result, such as:
-->
デフォルトでは、例えば次のような単純な結果を送る場合を考えてみましょう。

@[by-default](code/javaguide/async/JavaStream.java)

<!--
You are not specifying a `Content-Length` header. Of course, because the content you are sending is well known, Play is able to compute the content size for you and to generate the appropriate header.
-->
この例では、`Content-Length` を指定していません。送信しようとしているコンテンツが明らかなので、Play が自動的にコンテンツサイズを計算して、適切なヘッダを生成することができます。

<!--
> **Note** that for text-based content this is not as simple as it looks, since the `Content-Length` header must be computed according the encoding used to translate characters to bytes.
-->
> 文字列をバイト列に変換するために使われたエンコーディングにしたがって `Content-Length` をヘッダを計算しなければならないので、このようなテキストベースのコンテンツを返すことは見た目ほど単純なことではないことに **注意** してください。

To be able to compute the `Content-Length` header properly, Play must consume the whole response data and load its content into memory. 

<!--
## Serving files
-->
## ファイルを送信する

<!--
If it’s not a problem to load the whole content into memory for simple content what about a large data set? Let’s say we want to send back a large file to the web client.
-->
シンプルなコンテンツであればメモリに全て読み込んで処理しても問題ありませんが、データセットが巨大な場合はどうでしょうか？例えば、大きなファイルを web クライアントへ送り返すような場合です。

<!--
Play provides easy to use helpers to this common task of serving a local file:
-->
Play には、ローカルファイルを送信するというよくあるタスクを簡単にするためのヘルパが用意されています。

@[serve-file](code/javaguide/async/JavaStream.java)

<!--
Additionally this helper will also compute the `Content-Type` header from the file name. And it will also add the `Content-Disposition` header to specify how the web browser should handle this response. The default is to ask the web browser to download this file by using `Content-Disposition: attachment; filename=fileToServe.pdf`.
-->
このヘルパはファイル名から `Content-Type` ヘッダを計算してくれます。さらに、 web ブラウザがどのようにこのレスポンスを取り扱うべきかを指定する `Content-Disposition` ヘッダも追加してくれます。デフォルトでは、`Content-Disposition: attahment; filename=fileToServe.pdf` という指定により、 web ブラウザはこのファイルのダウンロードをするかどうかをユーザに確認します。

<!--
## Chunked responses
-->
## チャンクレスポンス

<!--
For now, this works well with streaming file content, since we are able to compute the content length before streaming it. But what about dynamically-computed content with no content size available?
-->
今のところ、ストリーミングを始める前にコンテンツの長さを計算することができるため、うまくファイルの内容をストリーミングすることができていました。しかし、コンテンツのサイズが事前にわからないような動的に生成されるコンテンツをストリーミングする場合はどうでしょうか？

For this kind of response we have to use **Chunked transfer encoding**. 

<!--
> **Chunked transfer encoding** is a data transfer mechanism in version HTTP 1.1 in which a web server serves content in a series of chunks. This uses the `Transfer-Encoding` HTTP response header instead of the `Content-Length` header, which the protocol would otherwise require. Because the `Content-Length` header is not used, the server does not need to know the length of the content before it starts transmitting a response to the client (usually a web browser). Web servers can begin transmitting responses with dynamically-generated content before knowing the total size of that content.
> 
> The size of each chunk is sent right before the chunk itself so that a client can tell when it has finished receiving data for that chunk. The data transfer is terminated by a final chunk of length zero.
>
> <http://en.wikipedia.org/wiki/Chunked_transfer_encoding>
-->
> **チャンク転送エンコーディング** は HTTP 1.1 で定義されているデータ転送メカニズムの一つで、 web サーバがコンテンツをいくつかのチャンクに分けて送信する、というものです。このレスポンスを送信するためには `Content-Length` ヘッダの代わりに `Transfer-Encoding` HTTP レスポンスヘッダを使います。 `Content-Length` ヘッダがないので、サーバはレスポンスをクライアント（通常は web ブラウザ）へ送信し始める前にコンテンツの長さを知る必要はありません。つまり、 web サーバは動的に生成されるコンテンツの最終的な長さを知ることなく、レスポンスを送り始めることができます。
>
> 各チャンクの長さはチャンクの内容の直前に送信されます。これによって、クライアントはチャンクの受信が終わったことを認識できます。最後に、長さがゼロのチャンクを送信すると、データ転送は完了です。
> <http://en.wikipedia.org/wiki/Chunked_transfer_encoding>

<!--
The advantage is that we can serve data **live**, meaning that we send chunks of data as soon as they are available. The drawback is that since the web browser doesn’t know the content size, it is not able to display a proper download progress bar.
-->
この方式の利点は、データを **ライブ** に提供できる、つまり利用できるようになったデータを即座にチャンクとして送信できることです。欠点は、 web ブラウザがコンテンツサイズを知らないため、ダウンロードプログレスバーを正しく表示できないということです。

<!--
Let’s say that we have a service somewhere that provides a dynamic `InputStream` that computes some data. We can ask Play to stream this content directly using a chunked response:
-->
例として、あるサービスが何かのデータを計算して動的な `InputStream` を提供しているケースを考えます。チャンクレスポンスを利用して、 Play にコンテンツを直接的にストリーミングさせるには、次のようにします。

@[input-stream](code/javaguide/async/JavaStream.java)

<!--
You can also set up your own chunked response builder. The Play Java API supports both text and binary chunked streams (via `String` and `byte[]`):
-->
自分でチャンクレスポンスのビルダをつくることもできます。 Play Java API は（それぞれ `String` と `byte[]` によって）テキスト形式とバイナリ両方のチャンクストリームをサポートしています。

@[chunked](code/javaguide/async/JavaStream.java)

<!--
The `onReady` method is called when it is safe to write to this stream. It gives you a `Chunks.Out` channel you can write to.
-->
`onReady` メソッドはストリームへの書き込み準備が整ったときに呼び出されます。引数には書き込み先のチャンネルである `Chunks.Out` が渡されます。

<!--
Let’s say we have an asynchronous process (like an `Actor`) somewhere pushing to this stream:
-->
さらに（ `アクター` のような）非同期プロセスがこのストリームにデータを書き込むとします。

@[register-out-channel](code/javaguide/async/JavaStream.java)

<!--
We can inspect the HTTP response sent by the server:
-->
サーバから送信される HTTP レスポンスは次のようになります。

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
We get three chunks and one final empty chunk that closes the response.
-->
3 つのチャンクと、最後にレスポンスを完了するための空のチャンクが送信されています。

<!--
> **Next:** [[Comet sockets | JavaComet]]
-->
> **次ページ:** [[Cometソケット | JavaComet]]
