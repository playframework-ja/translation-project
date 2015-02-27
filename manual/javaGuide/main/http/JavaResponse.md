<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
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

@[text-content-type](code/javaguide/http/JavaResponse.java)

<!--
Will automatically set the `Content-Type` header to `text/plain`, while:
-->
のように記述した場合は、`Content-Type` ヘッダに `text/plain` がセットされます。また、

@[json-content-type](code/javaguide/http/JavaResponse.java)

<!--
will set the `Content-Type` header to `application/json`.
-->
のように記述した場合は、`Content-Type` ヘッダに `application/json` がセットされます。

<!--
This is pretty useful, but sometimes you want to change it. Just use the `as(newContentType)` method on a result to create a new similar result with a different `Content-Type` header:
-->
これはかなり便利な機能ですが、`Content-Type` を任意に変更したいこともあるでしょう。そんな時は、Result の `as(newContentType)` というメソッドを呼び出して、`Content-Type` ヘッダを変更した新たなレスポンスを生成しましょう。

@[custom-content-type](code/javaguide/http/JavaResponse.java)

<!--
You can also set the content type on the HTTP response context:
-->
HTTP レスポンスのコンテキストにコンテントタイプを指定する事もできます。

@[context-content-type](code/javaguide/http/JavaResponse.java)

<!--
## Setting HTTP response headers
-->
## HTTP レスポンスヘッダの指定

@[response-headers](code/javaguide/http/JavaResponse.java)

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

@[set-cookie](code/javaguide/http/JavaResponse.java)

<!--
If you need to set more details, including the path, domain, expiry, whether it's secure, and whether the HTTP only flag should be set, you can do this with the overloaded methods:
-->
パスやドメイン、有効期限、セキュアか否か、そして HTTP only フラグがセットされるべきか否かなどを含む詳細を設定する必要がある場合は、オーバーロードされたメソッドを使うことができます:

@[detailed-set-cookie](code/javaguide/http/JavaResponse.java)

<!--
To discard a Cookie previously stored on the web browser:
-->
すでに web ブラウザに格納された Cookie を破棄する場合は、以下のようにします: 

@[discard-cookie](code/javaguide/http/JavaResponse.java)

<!--
Make sure, if you set a path or domain when setting the cookie, that you set the same path or domain when discarding the cookie, as the browser will only discard it if the name, path and domain matches.
-->
ブラウザは名前、パス、そしてドメインが一致している場合にのみ cookie を破棄するので、cookie をセットする際にパスやドメインを設定している場合は、cookie を破棄する際にパスまたはドメインを設定していることを確認してください。

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

@[charset](code/javaguide/http/JavaResponse.java)

<!--
> **Next:** [[Session and Flash scopes | JavaSessionFlash]]
-->
> **次ページ:** [[セッションとフラッシュスコープ | JavaSessionFlash]]
