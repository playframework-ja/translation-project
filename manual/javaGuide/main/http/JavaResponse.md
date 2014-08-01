<!-- translated -->
<!--
# Manipulating the response
-->
# レスポンスの変更

<!--
## Changing the default Content-Type
-->
## デフォルトの Content-Type の変更

<!--
The result content type is automatically inferred from the Java value you specify as body.
-->
レスポンスのコンテンツタイプは、レスポンスボディとして指定された Java の値から自動的に推論されます。

<!--
For example:
-->
例えば、

```
Result textResult = ok("Hello World!");
```

<!--
Will automatically set the `Content-Type` header to `text/plain`, while:
-->
のように記述した場合は、`Content-Type` ヘッダに `text/plain` がセットされます。また、

```
Result jsonResult = ok(jerksonObject);
```

<!--
will set the `Content-Type` header to `application/json`.
-->
のように記述した場合は、`Content-Type` ヘッダに `application/json` がセットされます。

<!--
This is pretty useful, but sometimes you want to change it. Just use the `as(newContentType)` method on a result to create a new similiar result with a different `Content-Type` header:
-->
これはかなり便利な機能ですが、`Content-Type` を任意に変更したいこともあるでしょう。そんな時は、Result の `as(newContentType)` というメソッドを呼び出して、`Content-Type` ヘッダを変更した新たなレスポンスを生成しましょう。

```
Result htmlResult = ok("<h1>Hello World!</h1>").as("text/html");
```

<!--
You can also set the content type on the HTTP response:
-->
HTTP レスポンスにコンテントタイプを指定する事も出来ます。

```
public static Result index() {
  response().setContentType("text/html");
  return ok("<h1>Hello World!</h1>");
}
```

<!--
## Setting HTTP response headers
-->
## HTTP レスポンスヘッダの指定

<!--
You can add (or update) any HTTP response header:
-->
任意の HTTP ヘッダを追加または更新することができます。

```
public static Result index() {
  response().setContentType("text/html");
  response().setHeader(CACHE_CONTROL, "max-age=3600");
  response().setHeader(ETAG, "xxx");
  return ok("<h1>Hello World!</h1>");
}
```

<!--
Note that setting an HTTP header will automatically discard any previous value.
-->
既に値が存在する HTTP ヘッダに何か値をセットすると、以前の値は自動的に破棄されてしまうことを覚えておいてください。

<!--
## Setting and discarding cookies
-->
## cookie の設定と破棄

<!--
Cookies are just a special form of HTTP headers, but Play provides a set of helpers to make it easier.
-->
Cookie は HTTP ヘッダの特殊形でしかありませんが、扱いを楽にするためいくつかのヘルパーが用意されています。

<!--
You can easily add a Cookie to the HTTP response:
-->
次のように、HTTP レスポンスへ Cookie を簡単に追加することができます。

```
response().setCookie("theme", "blue");
```

<!--
Also, to discard a Cookie previously stored on the Web browser:
-->
また、既に Web ブラウザに保存されている Cookie を破棄させるには、次のように書きます。

```
response().discardCookies("theme");
```

<!--
## Specifying the character encoding for text results
-->
## テキストのレスポンスの文字コードを指定する

<!--
For a text-based HTTP response it is very important to handle the character encoding correctly. Play handles that for you and uses `utf-8` by default.
-->
テキストベースの HTTP レスポンスについては、文字エンコーディングを適切に処理することがとても重要です。Play はデフォルトで `utf-8` を使い、この処理を行います。

<!--
The encoding is used to both convert the text response to the corresponding bytes to send over the network socket, and to add the proper `;charset=xxx` extension to the `Content-Type` header.
-->
文字エンコーディングはテキストベースのレスポンスをバイトデータに変換してネットワークソケット経由で送信できるようにしたり、`Content-Type` ヘッダに適切な `;charset=xxx` を付与するために利用されます。

<!--
The encoding can be specified when you are generating the `Result` value:
-->
文字エンコーディングは `Result` の値を生成する時に指定できます。

```
public static Result index() {
  response().setContentType("text/html; charset=iso-8859-1");
  return ok("<h1>Hello World!</h1>", "iso-8859-1");
}
```

<!--
> **Next:** [[Session and Flash scopes | JavaSessionFlash]]
-->
> **次ページ:** [[セッションとフラッシュスコープ | JavaSessionFlash]]