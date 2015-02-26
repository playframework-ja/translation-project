<!--- Copyright (C) 2009-2014 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# OpenID Support in Play
-->
# Play の OpenID 対応

<!--
OpenID is a protocol for users to access several services with a single account. As a web developer, you can use OpenID to offer users a way to log in using an account they already have, such as their [Google account](https://developers.google.com/accounts/docs/OpenID). In the enterprise, you may be able to use OpenID to connect to a company’s SSO server.
-->
OpenID はユーザが単一のアカウントで複数のサービスにアクセスできるようにするためのプロトコルです。Web 開発者としては、OpenID を使うことで、例えば [Google アカウント](https://developers.google.com/accounts/docs/OpenID) のような、ユーザがすでに所有しているアカウントを使ってログインする方法を提供できます。エンタープライズにおいては、企業の SSO サーバに接続するために OpenID を使うといったことも考えられます。

<!--
## The OpenID flow in a nutshell
-->
## OpenID のフロー概要

<!--
1. The user gives you his OpenID (a URL).
2. Your server inspects the content behind the URL to produce a URL where you need to redirect the user.
3. The user confirms the authorization on his OpenID provider, and gets redirected back to your server.
4. Your server receives information from that redirect, and checks with the provider that the information is correct.
-->
1. ユーザが OpenID (ある URL) を提供します。
2. アプリケーションサーバが URL の示すコンテンツを検証し、ユーザのリダイレクト先 URL を生成します。
3. ユーザが OpenID プロバイダのサイトにて認可情報を確認し、アプリケーションサーバに再度リダイレクトされます。
4. アプリケーションサーバがリダイレクトから認可情報を取得して、その情報が正しいことをプロバイダに確認します。

<!--
Step 1 may be omitted if all your users are using the same OpenID provider (for example if you decide to rely completely on Google accounts).
-->
すべてのユーザが同じ OpenID プロバイダを使う場合 (例えば Google アカウントにのみ依存すると決断した場合) 、ステップ 1 を省略することができます。

<!--
## Usage
-->
## 使い方

<!--
To use OpenId, first add `javaWs`  to your `build.sbt` file:
-->
OpenId を使うには、まず `build.sbt` ファイルに `javaWs` を追加します:

```scala
libraryDependencies ++= Seq(
  javaWs
)
```

<!--
## OpenID in Play
-->
## Play における OpenID

<!--
The OpenID API has two important functions:
-->
OpenID API には特に重要な関数が二つあります。

<!--
* `OpenID.redirectURL` calculates the URL where you should redirect the user. It involves fetching the user's OpenID page asynchronously, this is why it returns a `Promise<String>`. If the OpenID is invalid, the returned `Promise` will be a `Thrown`.
* `OpenID.verifiedId` inspects the current request to establish the user information, including his verified OpenID. It will do a call to the OpenID server asynchronously to check the authenticity of the information, this is why it returns a `Promise<UserInfo>`. If the information is not correct or if the server check is false (for example if the redirect URL has been forged), the returned `Promise` will be a `Thrown`.
-->
* `OpenID.redirectURL` は、ユーザのリダイレクト先 URL を計算する関数です。この関数は、非同期でユーザの OpenID ページを検索するため、`Promise<String>` を返します。OpenID が無効な場合、返却される `Promise` は失敗します。
* `OpenID.verifiedId` は、現在のリクエストを調べて、検証済みの OpenID を始めとするユーザの認可情報を組み立てます。この関数は、非同期に認可元の情報を確認するために OpenID サーバと通信を行うので、 `Promise<UserInfo>` を返します。認可情報が不正か、サーバが単に (リダイレクト URL が書き換えられるなどの理由で) 確認に失敗した場合、返却される `Promise` は `Thrown` になります。

<!--
If the `Promise` fails, you can define a fallback, which redirects back the user to the login page or return a `BadRequest`.
-->
`Promise` が失敗するかもしれないので、ユーザーをログインページにリダイレクトするか、または `BadRequest` を返すフォールバックを定義することができます。

<!--
### Example
-->
### 例

`conf/routes`:

@[ws-openid-routes](code/javaguide.ws.routes)

controller:

Java
: @[ws-openid-controller](code/javaguide/ws/controllers/OpenIDController.java)

Java 8
: @[ws-openid-controller](java8code/java8guide/ws/controllers/OpenIDController.java)


<!--
## Extended Attributes
-->
## 拡張属性

<!--
The OpenID of a user gives you his identity. The protocol also supports getting [extended attributes](http://openid.net/specs/openid-attribute-exchange-1_0.html) such as the e-mail address, the first name, or the last name.
-->
OpenID はユーザの同一性を提供します。メールアドレスや名前、苗字などを取得する [拡張属性](http://openid.net/specs/openid-attribute-exchange-1_0.html) もサポートしています。

<!--
You may request *optional* attributes and/or *required* attributes from the OpenID server. Asking for required attributes means the user cannot login to your service if he doesn’t provides them.
-->
OpenID サーバに対しては、*任意* および *必須* の属性のどちらか一方または両方をリクエストすることができます。必須の属性を要求するということは、ユーザがその情報を提供しないかぎり、あなたのサービスへログインできないことを意味します。

<!--
Extended attributes are requested in the redirect URL:
-->
拡張属性はリダイレクト URL の中でリクエストされます。

@[ws-openid-extended-attributes](code/javaguide/ws/controllers/OpenIDController.java)

<!--
Attributes will then be available in the `UserInfo` provided by the OpenID server.
-->
リクエストした属性は OpenID サーバから返却された `UserInfo` より取得することができます。

> **Next:** [[Accessing resources protected by OAuth|JavaOAuth]]
