<!--
# OpenID Support in Play
-->
# Play の OpenID サポート

<!--
OpenID is a protocol for users to access several services with a single account. As a web developer, you can use OpenID to offer users a way to log in using an account they already have, such as their [[Google account | http://code.google.com/apis/accounts/docs/OpenID.html]]. In the enterprise, you may be able to use OpenID to connect to a company’s SSO server.
-->
OpenID は ユーザに単一アカウントで複数のサービスを利用させるためのプロトコルです。web 開発者から見ると、OpenID は利用者に対して [[Google アカウント | http://code.google.com/apis/accounts/docs/OpenID.html]] などの既存のアカウントでログインするための方法を提供するために使えます。企業向けサービスの場合は、企業の SSO サーバへ接続するために OpenID を利用したりします。

<!--
## The OpenID flow in a nutshell
-->
## OpenID の概要

<!--
1. The user gives you his OpenID (a URL).
2. Your server inspects the content behind the URL to produce a URL where you need to redirect the user.
3. The user confirms the authorization on his OpenID provider, and gets redirected back to your server.
4. Your server receives information from that redirect, and checks with the provider that the information is correct.
-->
1. 利用者が web サービスに対して OpenID (とある URL) を渡します。
2. サーバが URL が指示するコンテンツを検証して、ユーザをある URL へリダイレクトさせます。
3. 利用者が OpenID Provider のサイト上で認可を行います。認可後、利用者はあなたのサーバへリダイレクトされて戻ってきます。
4. サーバがリダイレクト時のパラメータから認可情報を取得します。サーバは認可情報が正しいかどうかをチェックします。

<!--
Step 1 may be omitted if all your users are using the same OpenID provider (for example if you decide to rely completely on Google accounts).
-->
全ての利用者が同じ OpenID プロバイダーを使うような場合(例えば、あなたのサイトは Google アカウントの OpenID にだけ対応する、というように決め打ちした場合)、ステップ 1 を省略することができます。

<!--
## OpenID in Play
-->
## Play における OpenID

<!--
The OpenID API has two important functions:
-->
Play の OpenID API には重要な関数が二つあります。

<!--
* `OpenID.redirectURL` calculates the URL where you should redirect the user. It involves fetching the user's OpenID page, this is why it returns a `Promise[String]` rather than a `String`. If the OpenID is invalid, the returned `Promise` will be a `Thrown`.
* `OpenID.verifiedId` needs an implicit `Request`, and inspects it to establish the user information, including his verified OpenID. It will do a call to the OpenID server to check the authenticity of the information, this is why it returns a `Promise[UserInfo]` rather than just `UserInfo`. If the information is not correct or if the server check is false (for example if the redirect URL has been forged), the returned `Promise` will be a `Thrown`.
-->
* `OpenID.redirectURL` は、ユーザのリダイレクト先 URL を計算する関数です。この関数は、非同期でユーザの OpenID ページを検索するため、`String` ではなく `Promise[String]` を返します。OpenID が無効な場合、 `Promise` の中身は `Thrown` になります。
* `OPenID.verifiedId` は、暗黙的な `Request` を引数にとり、それを元に検証済みの OpenID を始めとするユーザの認可情報を組み立てます。この関数は、認可元の情報を確認するため、 OpenID サーバと通信を行います。この関数が `UserInfo` ではなく `Promise[UserInfo]` を返すのはそのためです。認可情報が不正か、サーバが単に (リダイレクト URL が書き換えられるなどの理由で) 確認に失敗した場合、`Promise` の中身は `Thrown` になります。 

<!--
In any case, when the `Promise` you get is a `Thrown`, you should look at the `Throwable` and redirect back the user to the login page with relevant information.
-->
とにかく、`Promise` の中身が　`Thrown` であった場合、実際にどんな `Throwable` が投げられたのかを調べて、エラー情報などと共にユーザを再度ログインページへリダイレクトさせるべきです。

<!--
Here is an example of usage (from a controller):
-->
次に、とあるコントローラから抽出した例を示します。

```scala
def login = Action {
  Ok(views.html.login())
}

def loginPost = Action { implicit request =>
  Form(single(
    "openid" -> nonEmptyText
  )).bindFromRequest.fold(
    error => {
      Logger.info("bad request " + error.toString)
      BadRequest(error.toString)
    },
    {
      case (openid) => AsyncResult(OpenID.redirectURL(openid, routes.Application.openIDCallback.absoluteURL())
          .extend( _.value match {
              case Redeemed(url) => Redirect(url)
              case Thrown(t) => Redirect(routes.Application.login)
          }))
    }
  )
}

def openIDCallback = Action { implicit request =>
  AsyncResult(
    OpenID.verifiedId.extend( _.value match {
      case Redeemed(info) => Ok(info.id + "\n" + info.attributes)
      case Thrown(t) => {
        // Here you should look at the error, and give feedback to the user
        Redirect(routes.Application.login)
      }
    })
  )
}
```

<!--
## Extended Attributes
-->
## 拡張属性

<!--
The OpenID of a user gives you his identity. The protocol also supports getting [[extended attributes | http://openid.net/specs/openid-attribute-exchange-1_0.html]] such as the e-mail address, the first name, or the last name.
-->
OpenID は利用者を一意に識別する ID になります。その他に、 OpenID プロトコルでは ID 以外の情報、例えばメールアドレスや氏名のような [[拡張属性 | http://openid.net/specs/openid-attribute-exchange-1_0.html]] を取得する方法も規定されています。

<!--
You may request *optional* attributes and/or *required* attributes from the OpenID server. Asking for required attributes means the user cannot login to your service if he doesn’t provides them.
-->
サーバは任意の *optional* 属性や、必須の *required* 属性を OpenID サーバに要求することができます。必須な属性を指定するということは、ユーザはその属性を提供しない限りあなたのサービスにログインできない、ということになります。

<!--
Extended attributes are requested in the redirect URL:
-->
拡張属性はリダイレクト URL を介して要求できます。

```scala
OpenID.redirectURL(
    openid,
    routes.Application.openIDCallback.absoluteURL(),
    Seq("email" -> "http://schema.openid.net/contact/email")
)
```

<!--
Attributes will then be available in the `UserInfo` provided by the OpenID server.
-->
すると、拡張属性は OpenID サーバが提供した `UserInfo` から取得できるようになります。

<!--
> **Next:** [[OAuth | ScalaOAuth]]
-->
> **次ページ:** [[OAuth | ScalaOAuth]]