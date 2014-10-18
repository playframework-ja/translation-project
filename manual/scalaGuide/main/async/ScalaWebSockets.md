<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# WebSockets
-->
# WebSocket

[WebSockets](http://en.wikipedia.org/wiki/WebSocket) are sockets that can be used from a web browser based on a protocol that allows two way full duplex communication.  The client can send messages and the server can receive messages at any time, as long as there is an active WebSocket connection between the server and the client.

Modern HTML5 compliant web browsers natively support WebSockets via a JavaScript WebSocket API.  However WebSockets are not limited in just being used by WebBrowsers, there are many WebSocket client libraries available, allowing for example servers to talk to each other, and also native mobile apps to use WebSockets.  Using WebSockets in these contexts has the advantage of being able to reuse the existing TCP port that a Play server uses.

<!--
## Handling WebSockets
-->
## WebSocket を扱う

<!--
Until now, we were using `Action` instances to handle standard HTTP requests and send back standard HTTP responses. WebSockets are a totally different beast and can’t be handled via standard `Action`.
-->
これまでは、標準的な HTTP リクエストを処理し、標準的な HTTP レスポンスを送信するためには `Action` を使っていました。WebSocket はそれと全く異なるので、通常の `Action` では扱えません。

Play provides two different built in mechanisms for handling WebSockets.  The first is using actors, the second is using iteratees.  Both of these mechanisms can be accessed using the builders provided on [WebSocket](api/scala/index.html#play.api.mvc.WebSocket$).

## Handling WebSockets with actors

To handle a WebSocket with an actor, we need to give Play a `akka.actor.Props` object that describes the actor that Play should create when it receives the WebSocket connection.  Play will give us an `akka.actor.ActorRef` to send upstream messages to, so we can use that to help create the `Props` object:

@[actor-accept](code/ScalaWebSockets.scala)

The actor that we're sending to here in this case looks like this:

@[example-actor](code/ScalaWebSockets.scala)

Any messages received from the client will be sent to the actor, and any messages sent to the actor supplied by Play will be sent to the client.  The actor above simply sends every message received from the client back with `I received your message: ` prepended to it.

### Detecting when a WebSocket has closed

When the WebSocket has closed, Play will automatically stop the actor.  This means you can handle this situation by implementing the actors `postStop` method, to clean up any resources the WebSocket might have consumed.  For example:

@[actor-post-stop](code/ScalaWebSockets.scala)

### Closing a WebSocket

Play will automatically close the WebSocket when your actor that handles the WebSocket terminates.  So, to close the WebSocket, send a `PoisonPill` to your own actor:

@[actor-stop](code/ScalaWebSockets.scala)

### Rejecting a WebSocket

Sometimes you may wish to reject a WebSocket request, for example, if the user must be authenticated to connect to the WebSocket, or if the WebSocket is associated with some resource, whose id is passed in the path, but no resource with that id exists.  Play provides `tryAcceptWithActor` to address this, allowing you to return either a result (such as forbidden, or not found), or the actor to handle the WebSocket with:

@[actor-try-accept](code/ScalaWebSockets.scala)

### Handling different types of messages

So far we have only seen handling `String` frames.  Play also has built in handlers for `Array[Byte]` frames, and `JsValue` messages parsed from `String` frames.  You can pass these as the type parameters to the WebSocket creation method, for example:

@[actor-json](code/ScalaWebSockets.scala)

You may have noticed that there are two type parameters, this allows us to handle differently typed messages coming in to messages going out.  This is typically not useful with the lower level frame types, but can be useful if you parse the messages into a higher level type.

For example, let's say we want to receive JSON messages, and we want to parse incoming messages as `InEvent` and format outgoing messages as `OutEvent`.  The first thing we want to do is create JSON formats for out `InEvent` and `OutEvent` types:

@[actor-json-formats](code/ScalaWebSockets.scala)

Now we can create WebSocket `FrameFormatter`'s for these types:

@[actor-json-frames](code/ScalaWebSockets.scala)

And finally, we can use these in our WebSocket:

@[actor-json-in-out](code/ScalaWebSockets.scala)

Now in our actor, we will receive messages of type `InEvent`, and we can send messages of type `OutEvent`.

## Handling WebSockets with iteratees

While actors are a better abstraction for handling discreet messages, iteratees are often a better  abstraction for handling streams.

<!--
To handle a WebSocket request, use a `WebSocket` instead of an `Action`:
-->
WebSocket リクエストを処理するためには、`Action` の代わりに `WebSocket` を使います。

@[iteratee1](code/ScalaWebSockets.scala)

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

@[iteratee2](code/ScalaWebSockets.scala)

Here is another example in which the input data is logged to standard out and broadcast by to the client utilizing 'Concurrent.broadcast'.

@[iteratee3](code/ScalaWebSockets.scala)

<!--
> **Next:** [[The template engine | ScalaTemplates]]
-->
> **次ページ:** [[テンプレートエンジン | ScalaTemplates]]
