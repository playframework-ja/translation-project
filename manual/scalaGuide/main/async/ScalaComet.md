<!-- translated -->
<!--
# Comet sockets
-->
# Coment ソケット

<!--
## Using chunked responses to create Comet sockets
-->
## Comet ソケットを作成するためにチャンクレスポンスを利用する

<!--
A good use for **Chunked responses** is to create Comet sockets. A Comet socket is just a chunked `text/html` response containing only `<script>` elements. At each chunk we write a `<script>` tag that is immediately executed by the web browser. This way we can send events live to the web browser from the server: for each message, wrap it into a `<script>` tag that calls a JavaScript callback function, and writes it to the chunked response.
-->
**チャンクレスポンス** を応用すると、Comet ソケットを作成することができます。 Comet ソケットは、 `<script>` のみを含むチャンク分割された単なる `text/html` レスポンスです。それぞれのチャンクに、 web ブラウザによって実行される JavaScript を含んだ `<script>` タグを書き込みます。これを利用することで、サーバから web ブラウザへ、イベントをリアルタイムに送信することができます: それぞれのメッセージ毎に、JavaScript のコールバック関数を呼び出す `<script>` タグでイベントをラップして、それをチャンクレスポンスに書き込みます。
    
<!-- 
Let’s write a first proof-of-concept: an enumerator that generates `<script>` tags that each call the browser `console.log` JavaScript function:
-->
それでは、これを確かめるデモを作成してみましょう。まず、ブラウザの `console.log` 関数を呼び出す `<script>` タグを生成するような Enumerator を作成します。
    
```scala
def comet = Action {
  val events = Enumerator(
     """<script>console.log('kiki')</script>""",
     """<script>console.log('foo')</script>""",
     """<script>console.log('bar')</script>"""
  )
  Ok.stream(events >>> Enumerator.eof).as(HTML)
}
```

<!--
If you run this action from a web browser, you will see the three events logged in the browser console.
-->
このアクションを web ブラウザから実行すると、ブラウザのコンソールに３つのイベントログが出力されるでしょう。

<!--
> **Tip:** Writing `events >>> Enumerator.eof` is just another way of writing `events.andThen(Enumerator.eof)`
-->
> **Tip:** `events >>> Enumerator.eof` は `events.andThen(Enumerator.eof)` の別の書き方です。

<!--
We can write this in a better way by using `play.api.libs.iteratee.Enumeratee` that is just an adapter to transform an `Enumerator[A]` into another `Enumerator[B]`. Let’s use it to wrap standard messages into the `<script>` tags:
-->
`Enumerator[A]` を別の `Enumerator[B]` へ変換する `play.api.libs.iteratee.Enumeratee` を利用すると、この例はもっと簡単に書けます。試しに、標準的なメッセージを `<script>` タグでラップするのに使ってみましょう。
    
```scala
import play.api.templates.Html

// Transform a String message into an Html script tag
val toCometMessage = Enumeratee.map[String] { data => 
    Html("""<script>console.log('""" + data + """')</script>""")
}

def comet = Action {
  val events = Enumerator("kiki", "foo", "bar")
  Ok.stream(events >>> Enumerator.eof &> toCometMessage)
}
```

<!--
> **Tip:** Writing `events >>> Enumerator.eof &> toCometMessage` is just another way of writing `events.andThen(Enumerator.eof).through(toCometMessage)`
-->
> **Tip:** `events >>> Enumerator.eof &> toCometMessage` は `events.andThen(Enumerator.eof).through(toCometMessage)` の別の書き方です。

<!--
## Using the `play.api.libs.Comet` helper
-->
## `play.api.libs.Comet` ヘルパーを使う

<!--
We provide a Comet helper to handle these Comet chunked streams that do almost the same stuff that we just wrote.
-->
チャンク分割された comet ストリームを扱うために、上で書いた内容とほぼ同じことを行う Comet ヘルパーを用意しています。

<!--
> **Note:** Actually it does more, like pushing an initial blank buffer data for browser compatibility, and it supports both String and JSON messages. It can also be extended via type classes to support more message types.
-->
> **ノート:** 実際のところ Comet ヘルパは、ブラウザの互換性のため最初に空のバッファデータを送信したり、メッセージとして String と JSON の両方をサポートするなど、上で書いた内容以上のことを行います。さらに、特定の type class を定義することで、他の型のメッセージをサポートするように拡張することもできます。

<!--
Let’s just rewrite the previous example to use it:
-->
これを使って前述の例を書き直してみましょう:

```scala
def comet = Action {
  val events = Enumerator("kiki", "foo", "bar")
  Ok.stream(events &> Comet(callback = "console.log"))
}
```

<!--
> **Tip:** `Enumerator.callbackEnumerator` and `Enumerator.pushEnumerator` are two convenient ways to create reactive non-blocking enumerators in an imperative style.
-->
> **Tip:** `Enumerator.callbackEnumerator` と `Enumerator.pushEnumerator` は反応的 (reactive) かつノンブロッキングな enumerator を手続き的に記述することに使えるヘルパ関数です。

<!--
## The forever iframe technique
-->
## Forever iframe テクニック

<!--
The standard technique to write a Comet socket is to load an infinite chunked comet response in an HTML `iframe` and to specify a callback calling the parent frame:
-->
Comet ソケットを書く標準的なテクニックとして、 iframe 内でチャンク分割された Comet レスポンスを無限にロードし、親フレームを呼び出すコールバック関数を特定するというものがあります:

```scala
def comet = Action {
  val events = Enumerator("kiki", "foo", "bar")
  Ok.stream(events &> Comet(callback = "parent.cometMessage"))
}
```

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
> **Next:** [[WebSockets | ScalaWebSockets]]
-->
> **Next:** [[WebSocket | ScalaWebSockets]]
