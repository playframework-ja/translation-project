<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# The Application Secret
-->
# アプリケーションシークレット

<!--
Play uses a secret key for a number of things, including:
-->
Play は、下記を含むいくつかの目的で秘密鍵を使います:

<!--
* Signing session cookies and CSRF tokens
* Built in encryption utilities
-->
* セッション cookie と CSRF トークンに署名する
* 組み込みの暗号化ユーティリティ

<!--
It is configured in `application.conf`, with the property name `play.crypto.secret`, and defaults to `changeme`.  As the default suggests, it should be changed for production.
-->
これは `application.conf` 内にある `play.crypto.secret` という名前のプロパティで設定されていて、デフォルトは `changeme` です。このデフォルト値が示唆する通り、本番環境では変更されなければなりません。

<!--
> When started in prod mode, if Play finds that the secret is not set, or if it is set to `changeme`, Play will throw an error.
-->
> Paly は、prod モードで起動した際に秘密鍵が設定されていない、または `changeme` が設定されていることを検出すると、エラーを投げます。

<!--
## Best practices
-->
## ベストプラクティス

<!--
Anyone that can get access to the secret will be able to generate any session they please, effectively allowing them to log in to your system as any user they please.  Hence it is strongly recommended that you do not check your application secret into source control.  Rather, it should be configured on your production server.  This means that it is considered bad practice to put the production application secret in `application.conf`.
-->
この秘密鍵にアクセスできる人なら誰でも、望み通りのあらゆるセッションを生成できることになり、望み通りのあらゆるユーザとして簡単にシステムにログインすることができます。このため、アプリケーションシークレットをソース管理システムに登録しないことを強くお勧めします。その代わりに、本番サーバに設定されるべきです。これは、本番のアプリケーションシークレットを `application.conf` に設定することは悪い習慣と見なされることを意味します。

<!--
One way of configuring the application secret on a production server is to pass it as a system property to your start script.  For example:
-->
本番サーバでアプリケーションシークレットを設定する方法のひとつは、起動スクリプトにシステム設定として渡すことです。例えば:

```bash
/path/to/yourapp/bin/yourapp -Dplay.crypto.secret="QCY?tAnfk?aZ?iwrNwnxIlR6CTf:G3gf:90Latabg@5241AB`R5W:1uDFN];Ik@n"
```

<!--
This approach is very simple, and we will use this approach in the Play documentation on running your app in production mode as a reminder that the application secret needs to be set.  In some environments however, placing secrets in command line arguments is not considered good practice.  There are two ways to address this.
-->
このアプローチはとてもシンプルであり、Play の本番モードにおけるアプリケーションの起動に関するドキュメントでも、アプリケーションシークレットは設定されていなければならないことを思い出す注意として、このアプローチを使います。しかしながら、いくつかの環境ではコマンドライン引数に秘密鍵を設定するのは良い習慣と見なされません。これに対応する方法はふたつあります。

<!--
### Environment variables
-->
### 環境変数

<!--
The first is to place the application secret in an environment variable.  In this case, we recommend you place the following configuration in your `application.conf` file:
-->
ひとつ目の方法ではアプリケーションシークレットを環境変数に配置します。この場合、`application.conf` ファイルに次のように設定することをお勧めします:

    play.crypto.secret="changeme"
    play.crypto.secret=${?APPLICATION_SECRET}

<!--
The second line in that configuration sets the secret to come from an environment variable called `APPLICATION_SECRET` if such an environment variable is set, otherwise, it leaves the secret unchanged from the previous line.
-->
この設定の二行目は、`APPLICATION_SECRET` という環境変数が設定されている場合は、この変数を秘密鍵に設定しますが、そうでない場合は一行目の秘密鍵から変更しません。

<!--
This approach works particularly well for cloud based deployment scenarios, where the normal practice is to set passwords and other secrets via environment variables that can be configured through the API for that cloud provider.
-->
このアプローチは、クラウドプロバイダが提供する API を通じて設定することができる環境変数によってパスワードやその他の秘密鍵を設定することが標準的な習慣であるクラウドベースのデプロイシナリオにおいて、特に有効です。

<!--
### Production configuration file
-->
### 本番設定ファイル

<!--
Another approach is to create a `production.conf` file that lives on the server, and includes `application.conf`, but also overrides any sensitive configuration, such as the application secret and passwords.
-->
別のアプローチでは、`application.conf` をインクルードをするものの、アプリケーションシークレットやパスワードのような慎重に扱うべき設定をすべて上書きする `production.conf` ファイルをサーバ上に作成します。

<!--
For example:
-->
例えば、以下のようにします:

    include "application"

    play.crypto.secret="QCY?tAnfk?aZ?iwrNwnxIlR6CTf:G3gf:90Latabg@5241AB`R5W:1uDFN];Ik@n"

<!--
Then you can start Play with:
-->
それから、以下のように Play を起動します:

```bash
/path/to/yourapp/bin/yourapp -Dconfig.file=/path/to/production.conf
```

<!--
## Generating an application secret
-->
## アプリケーションシークレットの生成

<!--
Play provides a utility that you can use to generate a new secret.  Run `playGenerateSecret` in the Play console.  This will generate a new secret that you can use in your application.  For example:
-->
Play は新しい秘密鍵を生成するユーティリティを提供しています。Play コンソールで `playGenerateSecret` を実行してください。アプリケーションで使用できる新しい秘密鍵を生成します。例えば、以下のようにします:

```
[my-first-app] $ playGenerateSecret
[info] Generated new secret: QCYtAnfkaZiwrNwnxIlR6CTfG3gf90Latabg5241ABR5W1uDFNIkn
[success] Total time: 0 s, completed 28/03/2014 2:26:09 PM
```

<!--
## Updating the application secret in application.conf
-->
## application.conf 内のアプリケーションシークレットの更新

<!--
Play also provides a convenient utility for updating the secret in `application.conf`, should you want to have a particular secret configured for development or test servers.  This is often useful when you have encrypted data using the application secret, and you want to ensure that the same secret is used every time the application is run in dev mode.
-->
Play は、開発またはテストサーバにおいて特定の秘密鍵が設定されているべき `application.conf` 内の秘密鍵を更新する便利なユーティリティも提供しています。これは、アプリケーションシークレットを使ってデータを暗号化しており、dev モードでアプリケーションを起動しているときはいつでも秘密鍵が同じであることを保証してほしいときに便利です。

<!--
To update the secret in `application.conf`, run `playUpdateSecret` in the Play console:
-->
`application.conf` 内の秘密鍵を更新するには、Play コンソールにて `playUpdateSecret` を実行します:

```
[my-first-app] $ playUpdateSecret
[info] Generated new secret: B4FvQWnTp718vr6AHyvdGlrHBGNcvuM4y3jUeRCgXxIwBZIbt
[info] Updating application secret in /Users/jroper/tmp/my-first-app/conf/application.conf
[info] Replacing old application secret: play.crypto.secret="changeme"
[success] Total time: 0 s, completed 28/03/2014 2:36:54 PM
```
