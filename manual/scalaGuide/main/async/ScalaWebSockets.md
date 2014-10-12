<!--
# WebSockets
-->
# WebSocket

<!--
## Using WebSockets instead of Comet sockets
-->
## Comet ソケットの代わりに WebSocket を使う

<!--
A Comet socket is a kind of hack for sending live events to the web browser. Also, it only supports one-way communication from the server to the client. To push events to the server, the web browser has to send Ajax requests.
-->
Comet ソケットは Web ブラウザへイベントをリアルタイムに送信するためのいわばハックです。また、Comet はサーバからクライアントへの一方向の通信しかサポートしません。サーバへイベントを PUSH 送信する場合は、AJAX リクエストを使います。

<!--
> **Note:** It is also possible to achieve the same kind of live communication the other way around by using an infinite HTTP request handled by a custom `BodyParser` that receives chunks of input data, but that is far more complicated.
-->
> **Note:** クライアントからサーバへリアルタイムに通信を行う別の方法として、入力データのチャンクを受信するカスタムの `BodyParser` に無限の HTTP リクエストを処理させることも考えられますが、Ajax リクエストよりさらに複雑になってしまいます。

<!--
Modern web browsers natively support two-way live communication via WebSockets.
-->
現代的な Web ブラウザは WebSocket による双方向のリアルタイム通信をネイティブにサポートしています。

<!--
>WebSocket is a web technology that provides bi-directional, full-duplex communication channels, over a single Transmission Control Protocol (TCP) socket. The WebSocket API is being standardized by the W3C, and the WebSocket protocol has been standardized by the IETF as RFC 6455.
>
>WebSocket is designed to be implemented in web browsers and web servers, but it can be used by any client or server application. Because ordinary TCP connections to port numbers other than 80 are frequently blocked by administrators outside of home environments, it can be used as a way to circumvent these restrictions and provide similar functionality with some additional protocol overhead while multiplexing several WebSocket services over a single TCP port.
>
>WebSocket is also useful for web applications that require real-time bi-directional communication. Before the implementation of WebSocket, such bi-directional communication was only possible using Comet channels; however, Comet is not trivial to implement reliably, and due to the TCP handshaking and HTTP header overhead, it may be inefficient for small messages. The WebSocket protocol aims to solve these problems without compromising the web’s security assumptions.
>
> <http://en.wikipedia.org/wiki/WebSocket>
-->
> WebSocket は双方向かつ全多重の通信チャンネルを、単一の Transmission Control Protocol (TCP) ソケット上で実現する Web テクノロジです。WebSocket の API は W3C により、一方 WebSocket のプロトコルは IETF により RFC 6455 として、標準化が進められています。
>
> WebSocket は元々、Web ブラウザおよび Web サーバにおいて実装されることを想定して設計されていますが、実際はどんな種類のクライアントやサーバでも利用できます。80 番ポート以外への TCP 接続は家庭内ネットワーク以外では管理者によってブロックされていることがよくありますが、WebSocket を使うとこの制限を迂回することができます。つまり、プロトコルのオーバーヘッドと引き換えにはなりますが、通常の TCP 接続と同じような機能を実現することができ、単一の TCP ポート上で複数の WebSocket サービスを多重化させることもできます。
>
> WebSocket は、リアルタイムに双方向通信を行うような web アプリケーションにとっても有用です。WebSocket が実現するまでも、このような双方向通信は Comet チャンネルを使えば実現することができました。しかし、Comet は安定して動作させるのが難しい、また TCP ハンドシェイクや HTTP ヘッダのオーバーヘッドがあるため小さいメッセージを送受信する場合に非効率的であるという欠点がありました。WebSocket は web のセキュリティ要件を落とさずにこのような双方向通信を実現するために開発されました。
>
> <http://en.wikipedia.org/wiki/WebSocket>

<!--
## Handling WebSockets
-->
## WebSocket を扱う

<!--
Until now, we were using `Action` instances to handle standard HTTP requests and send back standard HTTP responses. WebSockets are a totally different beast and can’t be handled via standard `Action`.
-->
これまでは、標準的な HTTP リクエストを処理し、標準的な HTTP レスポンスを送信するためには `Action` を使っていました。WebSocket はそれと全く異なるので、通常の `Action` では扱えません。

<!--
To handle a WebSocket request, use a `WebSocket` instead of an `Action`:
-->
WebSocket リクエストを処理するためには、`Action` の代わりに `WebSocket` を使います。

```scala
def index = WebSocket.using[String] { request => 
  
  // Log events to the console
  val in = Iteratee.foreach[String](println).map { _ =>
    println("Disconnected")
  }
  
  // Send a single 'Hello!' message
  val out = Enumerator("Hello!")
  
  (in, out)
}
```

<!--
A `WebSocket` has access to the request headers (from the HTTP request that initiates the WebSocket connection), allowing you to retrieve standard headers and session data. However, it doesn’t have access to a request body, nor to the HTTP response.
-->
`WebSocket` からはリクエストヘッダ (WebSocket 接続を開始するための HTTP リクエストからの。) を参照でき、標準的なヘッダやセッションデータを取得することが可能です。しかし、リクエストボディを参照したり、HTTP レスポンスを返すことはできません。

<!--
When constructing a `WebSocket` this way, we must return both `in` and `out` channels.
-->
`WebSocket` をこの方法で組み上げる場合、`in` と `out` の二つのチャンネルを返す必要があります。

- The `in` channel is an `Iteratee[A,Unit]` (where `A` is the message type - here we are using `String`) that will be notified for each message, and will receive `EOF` when the socket is closed on the client side.
- The `out` channel is an `Enumerator[A]` that will generate the messages to be sent to the Web client. It can close the connection on the server side by sending `EOF`.

<!--
It this example we are creating a simple iteratee that prints each message to console. To send messages, we create a simple dummy enumerator that will send a single **Hello!** message.
-->
この例では、受信した各メッセージを console に出力するだけのシンプルな Iteratee を作成しています。また、メッセージを送信するため、**Hello!** というメッセージを一回だけ送信する単純なダミーの Enumerator も作成しました。

<!--
> **Tip:** You can test WebSockets on <http://websocket.org/echo.html>. Just set the location to `ws://localhost:9000`.
-->
> **Tip:** WebSocket は <http://websocket.org/echo.html> でテストすることができます。location に `ws://localhost:9000` を設定してください。

<!--
Let’s write another example that discards the input data and closes the socket just after sending the **Hello!** message:
-->
次は、入力データを全て捨てつつ、**Hello!** メッセージを送信した後すぐにソケットを閉じる例を書いてみましょう。

```scala
def index = WebSocket.using[String] { request => 
  
  // Just consume and ignore the input
  val in = Iteratee.consume[String]()
  
  // Send a single 'Hello!' message and close
  val out = Enumerator("Hello!").andThen(Enumerator.eof)
  
  (in, out)
}
```

Here is another example in which the input data is logged to standard out and broadcast by to the client utilizing 'Concurrent.broadcast'.

```scala
//This shows an updated websocket example for play 2.2.0 utilizing Concurrent.broadcast vs Enumerator.imperative, which is now deprecated.

 def index =  WebSocket.using[String] { request =>
 
   //Concurrent.broadcast returns (Enumerator, Concurrent.Channel)
    val (out,channel) = Concurrent.broadcast[String]
 
    //log the message to stdout and send response back to client
    val in = Iteratee.foreach[String] {
      msg => println(msg)
             //the Enumerator returned by Concurrent.broadcast subscribes to the channel and will 
             //receive the pushed messages
             channel push("RESPONSE: " + msg)
    }
    (in,out)
}
```

<!--
> **Next:** [[The template engine | ScalaTemplates]]
-->
> **次ページ:** [[テンプレートエンジン | ScalaTemplates]]
