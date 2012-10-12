<!-- translated -->
<!--
# Comet sockets
-->
# Cometソケット

<!--
## Using chunked responses to create Comet sockets  
-->
## Cometソケットを作成するためにチャンクレスポンスを利用する

<!--
An useful usage of **Chunked responses** is to create Comet sockets. A Comet socket is just a chunked `text/html` response containing only `<script>` elements. For each chunk, we write a `<script>` tag containing JavaScript that is immediately executed by the web browser. This way we can send events live to the web browser from the server: for each message, wrap it into a `<script>` tag that calls a JavaScript callback function, and write it to the chunked response.
    
Let’s write a first proof-of-concept: create an enumerator generating `<script>` tags calling the browser `console.log` function:
-->
**チャンクレスポンス**を応用すると、Cometソケットを作成することができます。Cometソケットはチャンク分割されたただの`text/html`レスポンスで、その内容は`<script>`要素のみです。一つのチャンクにつき、JavaScriptを含む`<script>`タグを一つだけ書き込み、それをWebブラウザに実行させます。これを利用することで、サーバからWebブラウザへ、イベントをリアルタイムに送信することができます。具体的には、Webブラウザへ送信したい各メッセージをJavaScriptによるコールバック関数呼び出しを行う`<script>`タグに包み、それをチャンクレスポンスに書き込みます。

それでは、これを確かめるデモを作成してみましょう。まず、ブラウザの`console.log`関数を呼び出す`<script>`タグを生成するようなEnumeratorを作成します。
    
```
public static Result index() {
  // Prepare a chunked text stream
  Chunks<String> chunks = new StringChunks() {

    // Called when the stream is ready
    public void onReady(Chunks.Out<String> out) {
      out.write("<script>console.log('kiki')</script>");
      out.write("<script>console.log('foo')</script>");
      out.write("<script>console.log('bar')</script>");
      out.close();
    }

  };

  response().setContentType("text/html");
  return ok(chunks);
}
```

<!--
If you run this action from a web browser, you will see the three events logged in the browser console.
-->
このアクションをWebブラウザから実行すると、ブラウザのコンソールに３つのイベントがロギングされます。
  
<!--
## Using the `play.libs.Comet` helper
-->
## `play.libs.Comet`ヘルパーを使う

<!--
We provide a Comet helper to handle these comet chunked streams that does almost the same as what we just wrote.

> **Note:** Actually it does more, such as pushing an initial blank buffer data for browser compatibility, and supporting both String and JSON messages.

Let’s just rewrite the previous example to use it:
-->
前述の方法でCometによるチャンク分けされたストリーミングを行うために、Cometヘルパーが用意されています。

> **ノート:** 厳密には、Cometヘルパーは前述の方法以上のことをしてくれます。例えば、ブラウザの互換性のため最初に空のバッファーデータを送信したり、メッセージの内容としてStringとJSONの両方をサポートしてくれます。

```
public static Result index() {
  Comet comet = new Comet("console.log") {
    public void onConnected() {
      sendMessage("kiki");
      sendMessage("foo");
      sendMessage("bar");
      close();
    }
  };
  
  ok(comet);
}
```

<!--
## The forever iframe technique  
-->
## Forever iframeテクニック

<!--
The standard technique to write a Comet socket is to load an infinite chunked comet response in an iframe and to specify a callback calling the parent frame:  
-->
Cometソケットで標準的に使われるテクニックとして、終わりのないチャンクCometレスポンスをiframe上でロードして、そこから親フレームのコールバック関数を呼び出すというものです。

```
public static Result index() {
  Comet comet = new Comet("parent.cometMessage") {
    public void onConnected() {
      sendMessage("kiki");
      sendMessage("foo");
      sendMessage("bar");
      close();
    }
  };
  
  ok(comet);
}
```

<!--
With an HTML page like:  
-->  
そのときのHTMLページの内容は以下のようなものになります。

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
