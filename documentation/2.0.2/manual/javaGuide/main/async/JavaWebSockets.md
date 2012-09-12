<!-- translated -->
<!--
# WebSockets
-->
# WebSocket

<!--
## Using WebSockets instead of Comet sockets
-->
## Cometソケットの代わりにWebSocketを使う

<!--
A Comet socket is a kind of hack for sending live events to the web browser. Also, Comet only supports one-way communication from the server to the client. To push events to the server, the web browser can make Ajax requests.

Modern web browsers natively support two-way live communication via WebSockets.  
-->
CometソケットはWebブラウザへイベントをリアルタイムに送信するためのいわばハックです。また、Cometはサーバからクライアントへの一方向の通信しかサポートしません。サーバへイベントをPUSH送信する場合は、AJAXリクエストを使います。

現代的なWebブラウザはWebSocketにより双方向のリアルタイム通信をネイティブにサポートしています。

<!--
>WebSocket is a web technology providing for bi-directional, full-duplex communications channels, over a single Transmission Control Protocol (TCP) socket. The WebSocket API is being standardized by the W3C, and the WebSocket protocol has been standardized by the IETF as RFC 6455.
>
>WebSocket is designed to be implemented in web browsers and web servers, but it can be used by any client or server application. Because ordinary TCP connections to port numbers other than 80 are frequently blocked by administrators outside of home environments, it can be used as a way to circumvent these restrictions and provide similar functionality with some additional protocol overhead while multiplexing several WebSocket services over a single TCP port. Additionally, it serves a purpose for web applications that require real-time bi-directional communication. Before the implementation of WebSocket, such bi-directional communication was only possible using comet channels; however, a comet is not trivial to implement reliably, and due to the TCP Handshake and HTTP header overhead, it may be inefficient for small messages. The WebSocket protocol aims to solve these problems without compromising security assumptions of the web.
>
> [[http://en.wikipedia.org/wiki/WebSocket]]
-->
> WebSocketは双方向かつ全多重の通信チャンネルを、単一のTransmission Control Protocol (TCP)ソケット上で実現するWebテクノロジです。WebSocketのAPIはW3Cにより、一方WebSocketのプロトコルはIETFによりRFC 6455として、標準化が進められています。
>
> WebSocketは元々、WebブラウザおよびWebサーバにおいて実装されることを想定して設計されていますが、実際はどんな種類のクライアントやサーバでも利用できます。80番ポート以外へのTCP接続は家庭内ネットワーク以外では管理者によってブロックされていることがよくありますが、WebSocketを使うとこの制限を迂回することができます。つまり、プロトコルのオーバーヘッドと引き換えにはなりますが、通常のTCP接続と同じような機能を実現することができ、単一のTCPポート上で複数のWebSocketサービスを多重化させることもできます。加えて、WebSocketの主な用途は、リアルタイムかつ双方向の通信を要するようなWebアプリケーションです。WebSocketが実現するまでは、このような双方向通信を実現しようとするとCometチャンネルを利用するほかありませんでした。しかしながら、Cometで双方向通信を実現するのはそれほど自明なことではありません。また、TCPハンドシェイクとHTTPヘッダによるオーバーヘッドがあるため、メッセージが小さな場合は非効率です。WebSocketプロトコルはこのような問題を、Webのセキュリティ損なわずに解決することを狙っています。
> [[http://en.wikipedia.org/wiki/WebSocket]]

<!--
## Handling WebSockets
-->
## WebSocketを使う

<!--
Until now we were using a simple action method to handle standard HTTP requests and send back standard HTTP results. WebSockets are a totally different beast, and can’t be handled via standard actions.

To handle a WebSocket your method must return a `WebSocket` instead of a `Result`:
-->
これまでは標準的なHTTPリクエストを受け取って標準的なHTTPレスポンスを返すような単純なアクションメソッドを使いました。WebSocketはこれとは全く異なる猛獣なので、このような単純なアクションでは扱えません。

WebSocketを扱うためには、`Result`の代わりに`WebSocket`を返す必要があります。

<!--
```
public static WebSocket<String> index() {
  return new WebSocket<String>() {
      
    // Called when the Websocket Handshake is done.
    public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
      
      // For each event received on the socket,
      in.onMessage(new Callback<String>() {
         public void invoke(String event) {
             
           // Log events to the console
           println(event);  
             
         } 
      });
      
      // When the socket is closed.
      in.onClose(new Callback0() {
         public void invoke() {
             
           println("Disconnected")
             
         }
      });
      
      // Send a single 'Hello!' message
      out.write("Hello!");
      
    }
    
  }
}
```
-->
```
public static WebSocket<String> index() {
  return new WebSocket<String>() {
      
    // WebSocketのハンドシェイクが完了すると呼ばれます。
    public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
      
      // ソケットで受け取ったイベント毎に
      in.onMessage(new Callback<String>() {
         public void invoke(String event) {
             
           // Log events to the console
           println(event);  
             
         } 
      });
      
      // ソケットが閉じた時
      in.onClose(new Callback0() {
         public void invoke() {
             
           println("Disconnected")
             
         }
      });
      
      // 単一の`Hello!`というメッセージを送る
      out.write("Hello!");
      
    }
    
  }
}
```

<!--
A WebSocket has access to the request headers (from the HTTP request that initiates the WebSocket connection) allowing you to retrieve standard headers and session data. But it doesn't have access to any request body, nor to the HTTP response.

When the `WebSocket` is ready, you get both `in` and `out` channels.

It this example, we print each message to console and we send a single **Hello!** message.
-->
WebSocketはリクエストヘッダ(WebSocket接続を初期化するためのHTTPリクエストに付加されていたもの)を参照することができるため、標準的なヘッダの内容やセッションデータを受け取ることができます。しかし、リクエストボディやHTTPレスポンスへは一切アクセスできません。

`WebSocket`の準備が終わると、`in`と`out`という二つのチャンネルが得られます。

この例では、受け取ったメッセージをコンソールへ出力しつつ、単一の**Hello!**というメッセージを送信しています。

<!--
> **Tip:** You can test your WebSocket controller on [[http://websocket.org/echo.html]]. Just set the location to `ws://localhost:9000`.

Let’s write another example that totally discards the input data and closes the socket just after sending the **Hello!** message:
-->
> **Tip:** WebSocketコントローラは[[http://websocket.org/echo.html]]でテストすることができます。テストを行うためには、locationとして`ws://localhost:9000`を指定してください。

次は、入力データを全て捨てつつ、**Hello!**メッセージを送信した後すぐにソケットを閉じる例を書いてみましょう。

```
public static WebSocket<String> index() {
  return new WebSocket<String>() {
      
    public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
      out.write("Hello!");
      out.close()
    }
    
  }
}
```

<!--
> **Next:** [[The template engine | JavaTemplates]]
-->
> **次ページ:** [[テンプレートエンジン | JavaTemplates]]
