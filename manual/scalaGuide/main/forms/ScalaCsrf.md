<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Protecting against Cross Site Request Forgery
-->
# クロスサイトリクエストフォージェリ対策

<!--
Cross Site Request Forgery (CSRF) is a security exploit where an attacker tricks a victims browser into making a request using the victims session.  Since the session token is sent with every request, if an attacker can coerce the victims browser to make a request on their behalf, the attacker can make requests on the users behalf.
-->
クロスサイトリクエストフォージェリ (CSRF) は、攻撃者が被害者のブラウザに被害者のセッションを使ったリクエストを行わせるように仕向ける、セキュリティ上の脆弱性です。セッショントークンはすべてのリクエストにおいて送信されるので、攻撃者が被害者のブラウザに代わってリクエストを強制できる場合、攻撃者は被害者に代わってリクエストを行えるということになります。

It is recommended that you familiarise yourself with CSRF, what the attack vectors are, and what the attack vectors are not.  We recommend starting with [this information from OWASP](https://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29).

<!--
Simply put, an attacker can coerce a victims browser to make the following types of requests:
-->
端的に言えば、攻撃者は被害者のブラウザに次の種類のリクエストを行うよう強制することができます:

<!--
* All `GET` requests
* `POST` requests with bodies of type `application/x-www-form-urlencoded`, `multipart/form-data` and `text/plain`
-->
* あらゆる `GET` リクエスト
* ボディタイプが `application/x-www-form-urlencoded`, `multipart/form-data` そして `text/plain` の `POST` リクエスト

<!--
An attacker can not:
-->
攻撃者は次のことは行えません:

<!--
* Coerce the browser to use other request methods such as `PUT` and `DELETE`
* Coerce the browser to post other content types, such as `application/json`
* Coerce the browser to send new cookies, other than those that the server has already set
* Coerce the browser to set arbitrary headers, other than the normal headers the browser adds to requests
-->
* この他の、`PUT` や `DELETE` のようなリクエストを使うようブラウザに強制する
* この他の、`application/json` のようなコンテントタイプを送信するようブラウザに強制する
* サーバが既に設定したものとは異なる、新しいクッキーを送信するようブラウザに強制する
* ブラウザがリクエストに追加する通常のヘッダとは異なる、任意のヘッダを設定するようブラウザに強制する

<!--
Since `GET` requests are not meant to be mutative, there is no danger to an application that follows this best practice.  So the only requests that need CSRF protection are `POST` requests with the above mentioned content types.
-->
`GET` リクエストは状態を変えないことを意図しているので、このベストプラクティスに従っているアプリケーションに危険はありません。このため、CSRF 対策が必要なのは、上記で言及されたコンテントタイプを持つ `POST` リクエストだけです。

<!--
### Play's CSRF protection
-->
### Play の CSRF 対策

<!--
Play supports multiple methods for verifying that a request is not a CSRF request.  The primary mechanism is a CSRF token.  This token gets placed either in the query string or body of every form submitted, and also gets placed in the users session.  Play then verifies that both tokens are present and match.
-->
Play はリクエストが CSRF リクエストでないことを検証する複数のメソッドを提供しています。その主要なメカニズムは CSRF トークンです。このトークンは、クエリ文字列、または投稿されたすべてのフォームのボディ部分に配置され、同様にユーザーのセッションにも配置されます。そして、Play はこの双方のトークンが存在し、一致することを検証します。

<!--
To allow simple protection for non browser requests, such as requests made through AJAX, Play also supports the following:
-->
例えば AJAX を通じて行われるような、ブラウザ以外からのリクエストについて簡易に対策できるよう、Play は以下も提供しています:

<!--
* If an `X-Requested-With` header is present, Play will consider the request safe.  `X-Requested-With` is added to requests by many popular Javascript libraries, such as jQuery.
* If a `Csrf-Token` header with value `nocheck` is present, or with a valid CSRF token, Play will consider the request safe.
-->
* `X-Requested-With` ヘッダが存在する場合、Play はそのリクエストが安全であると見なします。`X-Requested-With` は jQuery のようないくつかのポピュラーな Javascript ライブラリがリクエストに追加します。
* 値が `nocheck` の `Csrf-Token` ヘッダが存在する場合、または適切な CSRF トークンを含む場合、Play はそのリクエストが安全であると見なします。

<!--
## Applying a global CSRF filter
-->
## グローバル CSRF フィルタの適用

Play provides a global CSRF filter that can be applied to all requests.  This is the simplest way to add CSRF protection to an application.  To enable the global filter, add the Play filters helpers dependency to your project in `build.sbt`:

```scala
libraryDependencies += filters
```

<!--
Now add the filter to your `Global` object:
-->
ここで `Global` オブジェクトにフィルタを追加します:

@[global](code/ScalaCsrf.scala)

<!--
### Getting the current token
-->
### 現在のトークンを取得する

<!--
The current CSRF token can be accessed using the `getToken` method.  It takes an implicit `RequestHeader`, so ensure that one is in scope.
-->
現在の CSRF トークンには `getToken` メソッドを使ってアクセスすることができます。このメソッドは implicit な `RequestHeader` を取るので、これがスコープに存在することを確認してください。

@[get-token](code/ScalaCsrf.scala)

<!--
To help in adding CSRF tokens to forms, Play provides some template helpers.  The first one adds it to the query string of the action URL:
-->
フォームへの CSRF トークンの追加を支援するために、Play はいくつかのテンプレートヘルパを提供しています。最初のひとつはトークンをアクション URL のクエリ文字列に追加します:

@[csrf-call](code/scalaguide/forms/csrf.scala.html)

<!--
<!--
This might render a form that looks like this:
-->
これはフォームを以下のようにレンダリングするでしょう:
-->
これはフォームを以下のようにレンダリングするでしょう:

```html
<form method="POST" action="/items?csrfToken=1234567890abcdef">
   ...
</form>
```

<!--
If it is undesirable to have the token in the query string, Play also provides a helper for adding the CSRF token as hidden field in the form:
-->
クエリ文字列にトークンを持つことが望しくない場合もあるので、Play は CSRF トークンをフォーム内の hidden フィールドとして追加するヘルパも提供しています:

@[csrf-input](code/scalaguide/forms/csrf.scala.html)

This might render a form that looks like this:

```html
<form method="POST" action="/items">
   <input type="hidden" name="csrfToken" value="1234567890abcdef"/>
   ...
</form>
```

<!--
The form helper methods all require an implicit token or request to be available in scope.  This will typically be provided by adding an implicit `RequestHeader` parameter to your template, if it doesn't have one already.
-->
これらのフォームヘルパーメソッドは、すべてスコープ上で利用できる implicit なトークンまたはリクエストを必要とします。これらは通常、implicit な `RequestHeader` パラメータがまだテンプレートに存在しない場合、テンプレートに追加することで提供されます。

<!--
### Adding a CSRF token to the session
-->
### CSRF トークンをセッションに追加する

<!--
To ensure that a CSRF token is available to be rendered in forms, and sent back to the client, the global filter will generate a new token for all GET requests that accept HTML, if a token isn't already available in the incoming request.
-->
CSRF トークンがフォーム内にレンダリングされ、そしてクライアントに送り返されることを保証するために、グローバルフィルタは HTML を受け取るすべての GET リクエストにおいて、そのリクエストで既にトークンが利用可能でない場合は、新しいトークンを生成します。

<!--
## Applying CSRF filtering on a per action basis
-->
## アクションごとに CSRF フィルタリングを適用する

<!--
Sometimes global CSRF filtering may not be appropriate, for example in situations where an application might want to allow some cross origin form posts.  Some non session based standards, such as OpenID 2.0, require the use of cross site form posting, or use form submission in server to server RPC communications.
-->
例えば、アプリケーションがいくつかのサイトをまたがったフォーム送信を許可するようにしたい場合など、グローバル CSRF フィルタが適切でない場合もあるかもしれません。Open ID 2.0 のようなセッションを前提としない標準は、サイトをまたがったフォームの送信、または複数サーバ間の RPC コミュニケーションによるフォーム送信を要求します。

<!--
In these cases, Play provides two actions that can be composed with your applications actions.
-->
このような状況のために、Play はアプリケーションのアクションに合成できる二つのアクションを提供しています。

<!--
The first action is the `CSRFCheck` action, and it performs the check.  It should be added to all actions that accept session authenticated POST form submissions:
-->
最初のひとつは、検査を行う `CSRFCheck` アクションです。これは、セッションで認証される POST フォームの投稿を受け取るすべてのアクションに追加する必要があります:

@[csrf-check](code/ScalaCsrf.scala)

<!--
The second action is the `CSRFAddToken` action, it generates a CSRF token if not already present on the incoming request.  It should be added to all actions that render forms:
-->
二つ目は、入力されたリクエストに既に存在しなければ CSRF トークンを生成する `CSRFAddToken` アクションです。これは、フォームをレンダリングするすべてのアクションに追加する必要があります:

@[csrf-add-token](code/ScalaCsrf.scala)

<!--
A more convenient way to apply these actions is to use them in combination with Play's `ActionBuilder`:
-->
これらのアクションをもっと便利に適用する方法は、これらのアクションを Play の `ActionBuilder` と組み合わせて使う方法です:

@[csrf-action-builder](code/ScalaCsrf.scala)

<!--
Then you can minimise the boiler plate code necessary to write actions:
-->
これでアクションを書くために必要なボイラープレートコードを最小化することができます:

@[csrf-actions](code/ScalaCsrf.scala)

## CSRF configuration options

The following options can be configured in `application.conf`:

* `csrf.token.name` - The name of the token to use both in the session and in the request body/query string. Defaults to `csrfToken`.
* `csrf.cookie.name` - If configured, Play will store the CSRF token in a cookie with the given name, instead of in the session.
* `csrf.cookie.secure` - If `csrf.cookie.name` is set, whether the CSRF cookie should have the secure flag set.  Defaults to the same value as `session.secure`.
* `csrf.body.bufferSize` - In order to read tokens out of the body, Play must first buffer the body and potentially parse it.  This sets the maximum buffer size that will be used to buffer the body.  Defaults to 100k.
* `csrf.sign.tokens` - Whether Play should use signed CSRF tokens.  Signed CSRF tokens ensure that the token value is randomised per request, thus defeating BREACH style attacks.

> **Next:** [[Custom Validations|ScalaCustomValidations]]
