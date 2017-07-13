<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# WebSockets
-->
# WebSocket

<!--
[WebSockets](https://en.wikipedia.org/wiki/WebSocket) are sockets that can be used from a web browser based on a protocol that allows two way full duplex communication.  The client can send messages and the server can receive messages at any time, as long as there is an active WebSocket connection between the server and the client.
-->
[WebSocket](https://ja.wikipedia.org/wiki/WebSocket) は、双方向の全二重通信を可能にするプロトコルに基づいて Web ブラウザから使用できるソケットです。サーバーとクライアントの間にアクティブな Web ソケット接続が存在する限り、クライアントはいつでもメッセージを送信することができ、サーバーはいつでもメッセージを受信することができます。

<!--
Modern HTML5 compliant web browsers natively support WebSockets via a JavaScript WebSocket API.  However WebSockets are not limited in just being used by WebBrowsers, there are many WebSocket client libraries available, allowing for example servers to talk to each other, and also native mobile apps to use WebSockets.  Using WebSockets in these contexts has the advantage of being able to reuse the existing TCP port that a Play server uses.
-->
最新の HTML5 に準拠した Web ブラウザは、JavaScript WebSocket API を介して WebSocket をネイティブにサポートします。しかし、WebSocket は Web ブラウザだけで使用されているだけでなく、例えばサーバ同士で会話したり、ネイティブのモバイルアプリで WebSocket を使ったりすることのできる WebSocket クライアントライブラリがたくさんあります。このような状況において WebSocket を使用することには、Play サーバーが使用する既存の TCP ポートを再利用できるという利点があります。

<!--
## Handling WebSockets
-->
## WebSocket の処理

<!--
Until now, we were using `Action` instances to handle standard HTTP requests and send back standard HTTP responses. WebSockets are a totally different beast and can’t be handled via standard `Action`.
-->
これまでは、標準的な HTTP リクエストを処理し、標準的な HTTP レスポンスを送信するためには `Action` を使っていました。WebSocket はそれと全く異なるので、通常の `Action` では扱えません。

<!--
Play provides two different built in mechanisms for handling WebSockets.  The first is using actors, the second is using iteratees.  Both of these mechanisms can be accessed using the builders provided on [WebSocket](api/scala/play/api/mvc/WebSocket$.html).
-->
Play には、WebSocket を処理するための2つの異なる組み込みのメカニズムが用意されています。一つ目のメカニズムはアクターを使用し、二つ目はイテレートを使用します。これらの両方のメカニズムには、[WebSocket](api/scala/play/api/mvc/WebSocket$.html) で提供されるビルダーを使用してアクセスできます。

<!--
## Handling WebSockets with actors
-->
## アクターによる WebSocket の処理

<!--
To handle a WebSocket with an actor, we need to give Play a `akka.actor.Props` object that describes the actor that Play should create when it receives the WebSocket connection.  Play will give us an `akka.actor.ActorRef` to send upstream messages to, so we can use that to help create the `Props` object:
-->
アクターを使って WebSocket を処理するには、Play が WebSocket 接続を受け取ったときに作成するアクターが記述された `akka.actor.Props` オブジェクトを Play に渡す必要があります。Play はアップストリームメッセージを送信するための `akka.actor.ActorRef` を与えてくれるので、これを使って `Props` オブジェクトの作成に役立てることができます。

@[actor-accept](code/ScalaWebSockets.scala)

<!--
The actor that we're sending to here in this case looks like this:
-->
この場合、ここに送るアクターは、このように見えます。

@[example-actor](code/ScalaWebSockets.scala)

<!--
Any messages received from the client will be sent to the actor, and any messages sent to the actor supplied by Play will be sent to the client.  The actor above simply sends every message received from the client back with `I received your message: ` prepended to it.
-->
クライアントから受信したメッセージはすべてアクターに送信され、Play から提供されたアクターに送信されたメッセージはすべてクライアントに送信されます。上記のアクターは、単にクライアントから受け取ったすべてのメッセージを `I received your message: ` を先頭に付けて送るだけです。

<!--
### Detecting when a WebSocket has closed
-->
### WebSocket が閉じられたときの検出

<!--
When the WebSocket has closed, Play will automatically stop the actor.  This means you can handle this situation by implementing the actors `postStop` method, to clean up any resources the WebSocket might have consumed.  For example:
-->
WebSocket が閉じると、Playは自動的にアクターを停止します。つまり、WebSocket が消費した可能性のあるリソースをすべて片付ける処理を行うような、アクターの `postStop` メソッドを実装することで、この状況を処理することができます。例を示します。

@[actor-post-stop](code/ScalaWebSockets.scala)

<!--
### Closing a WebSocket
-->
### WebSocket を閉じる

<!--
Play will automatically close the WebSocket when your actor that handles the WebSocket terminates.  So, to close the WebSocket, send a `PoisonPill` to your own actor:
-->
WebSocket を処理するアクターが終了すると、Play は WebSocket を自動的に閉じます。よって、WebSocket を閉じるには、自分のアクターに `PoisonPill` を送ります。

@[actor-stop](code/ScalaWebSockets.scala)

<!--
### Rejecting a WebSocket
-->
### WebSocket の拒否

<!--
Sometimes you may wish to reject a WebSocket request, for example, if the user must be authenticated to connect to the WebSocket, or if the WebSocket is associated with some resource, whose id is passed in the path, but no resource with that id exists.  Play provides `tryAcceptWithActor` to address this, allowing you to return either a result (such as forbidden, or not found), or the actor to handle the WebSocket with:
-->
例えば WebSocket に接続するためにユーザーを認証する必要がある場合、または WebSocket がパスの中で ID を通過させるようなリソースに関連付けられている場合で、その ID を持つリソースが存在しない場合など、WebSocket リクエストを拒否したい場合もあるでしょう。Play はこれに対処するための `tryAcceptWithActor` を提供し、結果(禁止されている、見つからないなど)、または WebSocket を処理するアクターを返すことができます。

@[actor-try-accept](code/ScalaWebSockets.scala)

<!--
### Handling different types of messages
-->
### 異なる種類のメッセージの処理

<!--
So far we have only seen handling `String` frames.  Play also has built in handlers for `Array[Byte]` frames, and `JsValue` messages parsed from `String` frames.  You can pass these as the type parameters to the WebSocket creation method, for example:
-->
これまでのところ、`String` フレームの処理しか見ていませんでした。Play には `Array[Byte]` フレームと、`String` フレームから解析された `JsValue` メッセージ用のハンドラが組み込まれています。これらを型パラメータとして WebSocket 作成メソッドに渡すことができます。たとえば、次のようにします。

@[actor-json](code/ScalaWebSockets.scala)

<!--
You may have noticed that there are two type parameters, this allows us to handle differently typed messages coming in to messages going out.  This is typically not useful with the lower level frame types, but can be useful if you parse the messages into a higher level type.
-->
2つの型パラメータがあることに気づいたかもしれません。これにより、入力メッセージから出力メッセージへと、異なる型のメッセージを処理させることができます。これは通常、下位レベルのフレームタイプでは有用ではありませんが、上位レベルの型へメッセージを解析する場合に便利です。

<!--
For example, let's say we want to receive JSON messages, and we want to parse incoming messages as `InEvent` and format outgoing messages as `OutEvent`.  The first thing we want to do is create JSON formats for out `InEvent` and `OutEvent` types:
-->
たとえば、JSON メッセージを受け取りたいとします。受信メッセージを `InEvent`  として解析し、送信メッセージを `OutEvent` としてフォーマットするとします。まず、`InEvent` と `OutEvent` 型の JSON フォーマットを作成します。

@[actor-json-formats](code/ScalaWebSockets.scala)

<!--
Now we can create WebSocket `FrameFormatter`'s for these types:
-->
これらの型に対して WebSocket `FrameFormatter` を作成することができます。

@[actor-json-frames](code/ScalaWebSockets.scala)

<!--
And finally, we can use these in our WebSocket:
-->
最後に、WebSocket でこれらを使用することができます。

@[actor-json-in-out](code/ScalaWebSockets.scala)

<!--
Now in our actor, we will receive messages of type `InEvent`, and we can send messages of type `OutEvent`.
-->
このアクターによって、`InEvent` 型のメッセージを受け取り、`OutEvent` 型のメッセージを送ることができます。

<!--
## Handling WebSockets with iteratees
-->
## イテレートを使用した WebSocket の処理

<!--
While actors are a better abstraction for handling discrete messages, iteratees are often a better  abstraction for handling streams.
-->
アクターは、個別のメッセージを処理するためのより良い抽象化ですが、イテレートは、ストリームを処理するためのより良い抽象化であることがよくあります。

<!--
To handle a WebSocket request, use a `WebSocket` instead of an `Action`:
-->
WebSocket リクエストを処理するためには、`Action` の代わりに `WebSocket` を使います。

@[iteratee1](code/ScalaWebSockets.scala)

<!--
A `WebSocket` has access to the request headers (from the HTTP request that initiates the WebSocket connection), allowing you to retrieve standard headers and session data. However, it doesn’t have access to a request body, nor to the HTTP response.
-->
`WebSocket` からはリクエストヘッダ (WebSocket 接続を開始するための HTTP リクエストからの) を参照でき、標準的なヘッダやセッションデータを取得することが可能です。しかし、リクエストボディを参照したり、HTTP レスポンスを返すことはできません。

<!--
When constructing a `WebSocket` this way, we must return both `in` and `out` channels.
-->
`WebSocket` をこの方法で組み立てる場合、`in` と `out` の二つのチャンネルを返す必要があります。

<!--
- The `in` channel is an `Iteratee[A,Unit]` (where `A` is the message type - here we are using `String`) that will be notified for each message, and will receive `EOF` when the socket is closed on the client side.
- The `out` channel is an `Enumerator[A]` that will generate the messages to be sent to the Web client. It can close the connection on the server side by sending `EOF`.
-->
- `in` チャンネルは各メッセージについて通知される `Iteratee[A,Unit]` (`A`
 はメッセージタイプです - ここでは `String` を使用しています）であり、クライアント側でソケットが閉じられたときに `EOF` を受け取ります。
- `out` チャンネルは、Web クライアントに送信されるメッセージを生成する `Enumerator[A]` です。`EOF` を送信することで、サーバー側の接続を閉じることができます。

<!--
It this example we are creating a simple iteratee that prints each message to console. To send messages, we create a simple dummy enumerator that will send a single **Hello!** message.
-->
この例では、受信した各メッセージをコンソールに出力するだけのシンプルなイテレートを作成しています。また、メッセージを送信するため、 **Hello!** というメッセージを一回だけ送信する単純なダミーの Enumerator も作成しました。

<!--
> **Tip:** You can test WebSockets on <https://www.websocket.org/echo.html>. Just set the location to `ws://localhost:9000`.
-->
> **ヒント:** WebSocket は <http://websocket.org/echo.html> でテストすることができます。location に `ws://localhost:9000` を設定してください。

<!--
Let’s write another example that discards the input data and closes the socket just after sending the **Hello!** message:
-->
次は、入力データを全て捨てつつ、 **Hello!** メッセージを送信した後すぐにソケットを閉じる例を書いてみましょう。

@[iteratee2](code/ScalaWebSockets.scala)

<!--
Here is another example in which the input data is logged to standard out and broadcast to the client utilizing `Concurrent.broadcast`.
-->
入力データが標準出力にロギングされ、`Concurrent.broadcast` を利用してクライアントにブロードキャストされる別の例を次に示します。

@[iteratee3](code/ScalaWebSockets.scala)
