<!-- translated -->
<!--
# Comet sockets
-->
# Comet ソケット

<!--
## Using chunked responses to create Comet sockets  
-->
## Comet ソケットを作成するためにチャンクレスポンスを利用する

<!--
An useful usage of **Chunked responses** is to create Comet sockets. A Comet socket is just a chunked `text/html` response containing only `<script>` elements. For each chunk, we write a `<script>` tag containing JavaScript that is immediately executed by the web browser. This way we can send events live to the web browser from the server: for each message, wrap it into a `<script>` tag that calls a JavaScript callback function, and write it to the chunked response.
-->
**チャンクレスポンス** を応用すると、Comet ソケットを作成することができます。 Comet ソケットは、 `<script>` のみを含むチャンク分割された単なる `text/html` レスポンスです。それぞれのチャンクに、 web ブラウザによって実行される JavaScript を含んだ `<script>` タグを書き込みます。これを利用することで、サーバから web ブラウザへ、イベントをリアルタイムに送信することができます: それぞれのメッセージ毎に、JavaScript のコールバック関数を呼び出す `<script>` タグでイベントをラップして、それをチャンクレスポンスに書き込みます。

<!--
Let’s write a first proof-of-concept: create an enumerator generating `<script>` tags calling the browser `console.log` function:
-->
それでは、これを確かめるデモを作成してみましょう。まず、ブラウザの `console.log` 関数を呼び出す `<script>` タグを生成するような Enumerator を作成します。

@[manual](code/javaguide/async/JavaComet.java)

<!--
If you run this action from a web browser, you will see the three events logged in the browser console.
-->
このアクションを web ブラウザから実行すると、ブラウザのコンソールに３つのイベントログが出力されるでしょう。
  
<!--
## Using the `play.libs.Comet` helper
-->
## `play.libs.Comet` ヘルパーを使う

<!--
We provide a Comet helper to handle these comet chunked streams that does almost the same as what we just wrote.
-->
チャンク分割された comet ストリームを扱うために、上で書いた内容とほぼ同じことを行う Comet ヘルパーを用意しています。

<!--
> **Note:** Actually it does more, such as pushing an initial blank buffer data for browser compatibility, and supporting both String and JSON messages.
-->
> **ノート:** 実際のところ Comet ヘルパは、ブラウザの互換性のため最初に空のバッファデータを送信したり、メッセージとして String と JSON の両方をサポートするなど、上で書いた内容以上のことを行います。

<!--
Let’s just rewrite the previous example to use it:
-->
これを使って前述の例を書き直してみましょう:

@[comet](code/javaguide/async/JavaComet.java)

<!--
## The forever iframe technique  
-->
## Forever iframe テクニック

<!--
The standard technique to write a Comet socket is to load an infinite chunked comet response in an iframe and to specify a callback calling the parent frame:  
-->
Comet ソケットを書く標準的なテクニックとして、 iframe 内でチャンク分割された Comet レスポンスを無限にロードし、親フレームを呼び出すコールバック関数を特定するというものがあります:

@[forever-iframe](code/javaguide/async/JavaComet.java)

<!--
With an HTML page like:  
-->  
これを、次のような HTML ページと共に使用します:

```
<script type="text/javascript">
  var cometMessage = function(event) {
    console.log('Received event: ' + event)
  }
</script>

<iframe src="/comet"></iframe>
```

<!--
> **Next:** [[WebSockets | JavaWebSockets]]
-->
> **次ページ:** [[WebSocket | JavaWebSockets]]