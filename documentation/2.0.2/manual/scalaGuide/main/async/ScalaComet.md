<!-- translated -->
<!--
# Comet sockets
-->
# Coment ソケット

<!--
## Using chunked responses to create Comet sockets
-->
## chunked レスポンスを利用して Comet ソケットを作成する

<!--
A good use for **Chunked responses** is to create Comet sockets. A Comet socket is just a chunked `text/html` response containing only `<script>` elements. At each chunk we write a `<script>` tag that is immediately executed by the web browser. This way we can send events live to the web browser from the server: for each message, wrap it into a `<script>` tag that calls a JavaScript callback function, and writes it to the chunked response.
-->
**Chunked レスポンス** の用途の一つは、Comet ソケットを作成することです。Comet ソケットは `<script>` 要素のみを含む `text/html` 形式の chunked レスポンスです。1 つのチャンクにつき 1 つずつ `<script>` タグを書き込むと、それが web ブラウザに即座に実行されます。この方法を用いると、サーバからのイベントを web ブラウザへリアルタイムに配信することができます。具体的には、ブラウザへ送りたいメッセージを JavaScript のコールバック関数を呼び出す `<script>` タグにラップして、それを chunked レスポンスに書きこみます。
   
<!-- 
Let’s write a first proof-of-concept: an enumerator that generates `<script>` tags that each call the browser `console.log` JavaScript function:
-->
早速、デモを作ってみましょう。まず、ブラウザの `console.log` という JavaScript 関数を呼び出す `<script>` タグを生成する enumerator を定義します。
    
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
このアクションを web ブラウザから実行すると、ブラウザのコンソールに 3 つのイベントがログ出力されます。

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
## `play.api.libs.Comet` ヘルパの利用

<!--
We provide a Comet helper to handle these Comet chunked streams that do almost the same stuff that we just wrote.
-->
これまで説明したような Comet チャンクのストリームを扱うために、 Comet ヘルパ関数が用意されています。ヘルパ関数を使うと、先ほどの例と同じようなことができます。

<!--
> **Note:** Actually it does more, like pushing an initial blank buffer data for browser compatibility, and it supports both String and JSON messages. It can also be extended via type classes to support more message types.
-->
> **Note:** 実際には、ブラウザ互換性のために最初に空白のバッファーデータを送信したり、String　と JSON メッセージの両方をサポートをしてくれる、という違いがあります。さらに、特定の type class を定義することで、他の型のメッセージをサポートするように拡張することもできます。

<!--
Let’s just rewrite the previous example to use it:
-->
先程の例を、ヘルパ関数を使って書き直してみましょう。

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
## forever iframe テクニック

<!--
The standard technique to write a Comet socket is to load an infinite chunked comet response in an HTML `iframe` and to specify a callback calling the parent frame:
-->
Comet ソケットで標準的に使われるテクニックとして、無限の chunked comet レスポンスを `iframe` 要素にロードして、親フレームで定義されたコールバック関数を呼び出す、というものがあります。

```scala
def comet = Action {
  val events = Enumerator("kiki", "foo", "bar")
  Ok.stream(events &> Comet(callback = "parent.cometMessage"))
}
```

<!--
With an HTML page like:
-->
この Comet ソケットは、例えば次のような HTML と合わせて利用します。

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