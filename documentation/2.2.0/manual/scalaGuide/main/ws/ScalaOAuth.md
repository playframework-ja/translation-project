<!-- translated -->
# OAuth

<!--
[OAuth](http://oauth.net/) is a simple way to publish and interact with protected data. It's also a safer and more secure way for people to give you access. For example, it can be used to access your users' data on [Twitter](https://dev.twitter.com/docs/auth/using-oauth).
-->
[OAuth](http://oauth.net/) はあなたのサービスのデータを他のサービスへ安全に提供したり、逆にあなたのサービスから他のサービスのデータへ安全にアクセスするための技術です。また、利用者側にとっては、データをあなたのサービスへ安全に提供する方法でもあります。例えば、OAuth を利用すると、あなたのサービス利用者が [Twitter](https://dev.twitter.com/docs/auth/using-oauth) に保存したデータを利用することができます。

<!--
There are 2 very different versions of OAuth: [OAuth 1.0](http://tools.ietf.org/html/rfc5849) and [OAuth 2.0](http://oauth.net/2/). Version 2 is simple enough to be implemented easily without library or helpers, so Play only provides support for OAuth 1.0.
-->
OAuth にはそれぞれ大きく異なる２つのバージョン、[OAuth 1.0](http://tools.ietf.org/html/rfc5849) と [OAuth 2.0](http://oauth.net/2/) が存在します。バージョン 2 はライブラリやヘルパーが必要ないほどシンプルなプロトコルなので、Play は OAuth 1.0 のみサポートしています。

<!--
## Required Information
-->
## 必須情報

<!--
OAuth requires you to register your application to the service provider. Make sure to check the callback URL that you provide, because the service provider may reject your calls if they don't match. When working locally, you can use `/etc/hosts` to fake a domain on your local machine.
-->
OAuth を利用するためには、まずアプリケーションをサービス・プロバイダに登録する必要があります。このとき、コールバック URL を間違えないように注意してください。これは、コールバック URL が一致しない場合、サービス・プロバイダによっては OAuth の通信を拒否することがあるからです。ローカル環境で試す場合は、`/etc/hosts` を利用してあなたのマシンのドメインを偽装するとよいでしょう。

<!--
The service provider will give you:
-->
登録が完了すると、サービス・プロバイダから以下の情報が提供されます。

<!--
* Application ID
* Secret key
* Request Token URL
* Access Token URL
* Authorize URL
-->
* アプリケーション ID
* 秘密鍵
* リクエスト・トークン URL
* アクセス・トークン URL
* 認可 URL

<!--
## Authentication Flow
-->
## 認証フロー

<!--
Most of the flow will be done by the Play library.
-->
OAuth の認証フローのほとんどは Play のライブラリが処理してくれます。

<!--
1. Get a request token from the server (in a server-to-server call)
2. Redirect the user to the service provider, where he will grant your application rights to use his data
3. The service provider will redirect the user back, giving you a /verifier/
4. With that verifier, exchange the /request token/ for an /access token/ (server-to-server call)
-->
1. サーバからリクエスト・トークンを取得します (サーバ間通信を利用) 。
2. ユーザをサービス・プロバイダへリダイレクトします。リダイレクト先で、ユーザがあなたのアプリケーションにデータのアクセス権を与えます。
3. サービス・プロバイダがユーザをあなたのサーバへリダイレクトします。このとき、/verifier/ が与えられます。
4. verifier を使って、/リクエスト・トークン/ を /アクセス・トークン/ に交換します (サーバ間通信を利用) 。

<!--
Now the /access token/ can be passed to any call to access protected data.
-->
以後、/アクセス・トークン/ を使って、保護されたデータへアクセスできるようになります。

<!--
## Example
-->
## 例

```scala
object Twitter extends Controller {

  val KEY = ConsumerKey("xxxxx", "xxxxx")

  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    true)

  def authenticate = Action { request =>
    request.getQueryString("oauth_verifier").map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => {
          // We received the authorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.Application.index).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      }
    }.getOrElse(
      TWITTER.retrieveRequestToken("http://localhost:9000/auth") match {
        case Right(t) => {
          // We received the unauthorized tokens in the OAuth object - store it before we proceed
          Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      })
  }

  def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }
}
```

```scala
object Application extends Controller {

  def timeline = Action { implicit request =>
    Twitter.sessionTokenPair match {
      case Some(credentials) =>
        Async {
          WS.url("https://api.twitter.com/1.1/statuses/home_timeline.json")
            .sign(OAuthCalculator(Twitter.KEY, credentials))
            .get
            .map(result => Ok(result.json))
        }
      
      case _ => Redirect(routes.Twitter.authenticate)
    }
  }
}
```

<!--
> **Next:** [[Integrating with Akka| ScalaAkka]]
-->
> **次ページ:** [[Akka の統合| ScalaAkka]]
