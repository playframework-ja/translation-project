<!-- translated -->
<!--
# Externalising messages and internationalization
-->
# メッセージの外部ファイル化と多言語化

<!--
## Specifying languages supported by your application
-->
## アプリケーションの対応言語を指定する

<!--
The specify your application’s languages, you need a valid language code, specified by a valid **ISO Language Code**, optionally followed by a valid **ISO Country Code**. For example, `fr` or `en-US`.

To start, you need to specify the languages that your application supports in its `conf/application.conf` file:
-->
アプリケーションの対応言語を指定するためには、まず正しい言語コードが必要です。言語コードの形式は、**ISO 言語コード**の後に省略可能な**ISO 国コード*を続ける、というものです。例えば、 `fr` や `en-US` は正しい言語コードです。

次に、その言語コードを使って、 `conf/application.conf` ファイル内でアプリケーションの対応言語を指定しましょう。

```
application.langs=en,en-US,fr
```

<!--
## Externalizing messages
-->
## メッセージを外部ファイル化する

<!--
You can externalize messages in the `conf/messages.xxx` files. 

The default `conf/messages` file matches all languages. You can specify additional language messages files, such as `conf/messages.fr` or `conf/messages.en-US`.

You can retrieve messages for the current language using the `play.i18n.Messages` object:
-->
メッセージは `conf/messages.xxx` というファイルに外部化することができます。

`conf/messages` ファイルは全ての言語で使われるデフォルトのメッセージです。それに加えて、`conf/messages.fr` や `conf/messages.en-US` のように対応言語ごとにメッセージファイルを指定することができます。

現在の言語向けのメッセージを取得するためには、 `play.i18n.Messages` オブジェクトを利用します。(訳注: 「現在の言語」とは、HTTPリクエストのAccept-Languageヘッダで指定された言語のこと)


```
String title = Messages.get("home.title")
```

<!--
You can also specify the language explicitly:
-->
言語を明示的に指定することもできます。


```
String title = Messages.get(new Lang("fr"), "home.title")
```

<!--
> **Note:** If you have a `Request` in the scope, it will provide a default `Lang` value corresponding to the preferred language extracted from the `Accept-Language` header and matching one the application’s supported languages.
-->
> **ノート:** スコープ内に `Request` がある場合、そこからデフォルトの `Lang` 値が提供される仕組みになっています。デフォルトの `Lang` は、リクエストの `Accept-Language` ヘッダで優先的に指定されていて、かつアプリケーションが対応している言語です。

<!--
## Formatting messages
-->
## メッセージをフォーマットする

<!--
Messages can be formatted using the `java.text.MessageFormat` library. For example, if you have defined a message like this:
-->
メッセージは `java.text.MessageFormat` ライブラリを使ってフォーマットすることができます。例えば、次のようなメッセージを定義したとします。

```
files.summary=The disk {1} contains {0} file(s).
```

<!--
You can then specify parameters as:
-->
このメッセージのパラメータは次のように指定できます。

```
Messages.get("files.summary", d.files.length, d.name)
```

<!--
## Retrieving supported languages from an HTTP request
-->
## HTTP リクエストから対応言語を取得する

<!--
You can retrieve a specific HTTP request’s supported languages:
-->
HTTP リクエストで指定されている、クライアント側の対応言語を取得することもできます。

```
public static Result index() {
  return ok(request().acceptLanguages());
}
```

<!--
> **Next:** [[The application Global object | JavaGlobal]]
-->
> **次ページ:** [[アプリケーションのグローバルオブジェクト | JavaGlobal]]