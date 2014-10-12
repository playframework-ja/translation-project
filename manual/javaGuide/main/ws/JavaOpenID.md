<!--
# OpenID Support in Play
-->
# Play の OpenID 対応

<!--
OpenID is a protocol for users to access several services with a single account. As a web developer, you can use OpenID to offer users a way to login with an account they already have (their [Google account](https://developers.google.com/accounts/docs/OpenID) for example). In the enterprise, you can use OpenID to connect to a company's SSO server if it supports it.
-->
OpenID はユーザが単一のアカウントで複数のサービスにアクセスできるようにするためのプロトコルです。 Web 開発者としては、 OpenID を使うことで、ユーザが別のサービスで既に作成してあるアカウント (例えば [Google アカウント](http://code.google.com/apis/accounts/docs/OpenID.html)) であなたの Web アプリケーションにログインできるようになります。エンタープライズ向けには、企業の SSO サーバに接続するために OpenID を使うといったことも考えられます。

<!--
## The OpenID flow in a nutshell
-->
## OpenID のフロー

<!--
1. The user gives you his OpenID (a URL)
2. Your server inspect the content behind the URL to produce a URL where you need to redirect the user
3. The user validates the authorization on his OpenID provider, and gets redirected back to your server
4. Your server receives information from that redirect, and check with the provider that the information is correct
-->
1. ユーザが OpenID (ある URL) を提供します。
2. アプリケーションサーバが URL の示すコンテンツを検証し、ユーザのリダイレクト先 URL を生成します。
3. ユーザが OpenID プロバイダのサイトにて認可情報を確認し、アプリケーションサーバに再度リダイレクトされます。
4. アプリケーションサーバがリダイレクトから認可情報を取得して、その情報が正しいことをプロバイダに確認します。

<!--
The step 1. may be omitted if all your users are using the same OpenID provider (for example if you decide to rely completely on Google accounts).
-->
すべてのユーザが同じ OpenID プロバイダを使う場合 (例えば Google アカウントにのみ依存すると決断した場合) 、ステップ 1 を省略することができます。

<!--
## OpenID in Play Framework
-->
## Play Framework における OpenID

<!--
The OpenID API has two important functions:
-->
OpenID API には特に重要な関数が二つあります。

<!--
* `OpenID.redirectURL` calculates the URL where you should redirect the user. It involves fetching the user's OpenID page, this is why it returns a `Promise<String>` rather than a `String`. If the OpenID is invalid, an exception will be thrown.
* `OpenID.verifiedId` inspects the current request to establish the user information, including his verified OpenID. It will do a call to the OpenID server to check the authenticity of the information, this is why it returns a `Promise<UserInfo>` rather than just `UserInfo`. If the information is not correct or if the server check is false (for example if the redirect URL has been forged), the returned `Promise` will be a `Thrown`.
-->
* `OpenID.redirectURL` はユーザのリダイレクト先 URL を計算する関数です。この関数は内部でユーザの OpenID ページを取得しにいくため、 `String` ではなく `Promise<String>` を返します。OpenID が正しくない場合、例外が発生します。
* `OpenID.verifiedId` は現在のリクエストの内容を見て、検証済みの OpenID などのユーザ情報を確立します。この関数はユーザ情報を認証するために内部で OpenID サーバへ通信を行うため、 `UserInfo` ではなく `Promise<UserInfo>` を返します。ユーザ情報が正しくないか、またはサーバによるチェックが失敗（例えば、リダイレクトURLが捏造されていたなどの理由によって）した場合は返却される `Promise` は `Thrown` になります。

<!--
In any case, you should catch exceptions and if one is thrown redirect back the user to the login page with relevant information.
-->
いずれの場合でも、常に例外をキャッチしておき、もし例外が投げられた場合はユーザをログインページへリダイレクトさせつつ、関連情報を表示するべきでしょう。

<!--
## Extended Attributes
-->
## 拡張属性

<!--
The OpenID of a user gives you his identity. The protocol also support getting [extended attributes](http://openid.net/specs/openid-attribute-exchange-1_0.html) such as the email address, the first name, the last name...
-->
OpenID はユーザの同一性を確かめるために利用することができます。それ以外にメールアドレスや名前、苗字などの取得のために [拡張属性](http://openid.net/specs/openid-attribute-exchange-1_0.html) というものもサポートされています。

<!--
You may request from the OpenID server *optional* attributes and/or *required* attributes. Asking for required attributes means the user can not login to your service if he doesn't provides them.
-->
OpenID サーバに対しては、*任意* および *必須* の属性のどちらか一方または両方をリクエストすることができます。必須の属性を要求するということは、ユーザがその情報を提供しないかぎり、あなたのサービスへログインできないことを意味します。

<!--
Extended attributes are requested in the redirect URL:
-->
拡張属性はリダイレクト URL の中でリクエストされます。

```
Map<String, String> attributes = new HashMap<String, String>();
attributes.put("email", "http://schema.openid.net/contact/email");
OpenID.redirectURL(
  openid, 
  routes.Application.openIDCallback.absoluteURL(request()), 
  attributes
);
```

<!--
Attributes will then be available in the `UserInfo` provided by the OpenID server.
-->
リクエストした属性は OpenID サーバから返却された `UserInfo` より取得することができます。
