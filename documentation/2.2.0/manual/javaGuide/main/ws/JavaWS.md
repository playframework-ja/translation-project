<!-- translated -->
<!--
# The Play WS API
-->
# Play WS API

<!--
Sometimes you want to call other HTTP services from within a Play application. Play supports this via its `play.libs.WS` library, which provides a way to make asynchronous HTTP calls.
-->
ときどき、Play アプリケーションから他の HTTP サービスを呼び出したくなることがあります。そんなときは、 Play が提供している、非同期　HTTP 呼び出しを行うためのライブラリ `play.libs.WS` を使いましょう。

A call made by `play.libs.WS` should return a `Promise<WS.Response>`, which you can handle later with Play’s asynchronous mechanisms.

## Imports

To use WS, first import the following packages:

@[ws-imports](code/javaguide/ws/JavaWS.java)

<!--
## Making HTTP calls
-->
## HTTP 呼び出しを作成する

<!--
To make an HTTP request, you start with `WS.url()` to specify the URL. Then you get a builder that you can use to specify HTTP options, such as setting headers. You end by calling a method corresponding to the HTTP method you want to use:
-->
HTTP リクエストを作成するためには、まず `WS.url()` で URL を指定します。その結果、例えばヘッダをセットする、といったような 各種 HTTP オプションを指定するためのビルダが返ってきます。オプションの指定が終わったら、最後に利用したい HTTP メソッドに対応するメソッドを呼び出します。例を挙げると、

@[get-call](code/javaguide/ws/JavaWS.java)

<!--
Alternatively:
-->
または、以下のように記述します。

@[post-call](code/javaguide/ws/JavaWS.java)

## Recovery

If you want to recover from an error in the call transparently, you can use `recover` to substitute a response:

@[get-call-and-recover](code/javaguide/ws/JavaWS.java)

## Retrieving the HTTP response result

The call is made asynchronously and you need to manipulate it as a `Promise<WS.Response>` to get the actual content. You can compose several promises and end up with a `Promise<Result>` that can be handled directly by the Play server:

@[simple-call](code/javaguide/ws/JavaWS.java)


<!--
## Composing results
-->
## 結果を合成する

<!--
If you want to make multiple calls in sequence, this can be achieved using `flatMap`:
-->
複数の HTTP 呼び出しを順番に行いたい場合は、 `flatMap` を使うとよいでしょう。

@[composed-call](code/javaguide/ws/JavaWS.java)

<!--
## Configuring the HTTP client
-->
## HTTP クライアントを設定する

<!--
The HTTP client can be configured globally in `application.conf` via a few properties:
-->
Play アプリケーション全体で使われる HTTP クライアントの設定は、 `application.conf` にほんの少しプロパティを設定するたけで行えます。

@[application](code/javaguide/ws/application.conf)

<!--
> **Next:** [[Integrating with Akka | JavaAkka]]
-->
> **次ページ:** [[Akka との統合 | JavaAkka]]