<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Messages and internationalization
-->
# メッセージ API と多言語対応

<!--
## Specifying languages supported by your application
-->
## アプリケーションの対応言語を指定する

<!--
A valid language code is specified by a valid **ISO 639-2 language code**, optionally followed by a valid **ISO 3166-1 alpha-2 country code**, such as `fr` or `en-US`.
-->
アプリケーションの対応言語は、`fr` や `en-US` のように、**ISO 639-2 言語コード** の後に省略可能な **ISO 3166-1 alpha-2 国コード** を続けて指定します。

<!--
To start you need to specify the languages supported by your application in the `conf/application.conf` file:
-->
初めに、 `conf/application/conf.` ファイルであなたのアプリケーションの対応言語を指定しましょう。

```
application.langs="en,en-US,fr"
```

<!--
## Externalizing messages
-->
## メッセージの外部ファイル化

<!--
You can externalize messages in the `conf/messages.xxx` files.
-->
メッセージは `conf/messages.xxx` のようなファイルに外部化することができます。

<!--
The default `conf/messages` file matches all languages. Additionally you can specify language-specific message files such as `conf/messages.fr` or `conf/messages.en-US`.
-->
デフォルトの `conf/messages` というファイルは、全ての言語にマッチします。このファイルに加えて `conf/messages.fr` や `conf/messages.en-US` のように言語ごとのメッセージファイルを指定することができます。

<!--
You can then retrieve messages using the `play.api.i18n.Messages` object:
-->
メッセージは、`play.api.libs.i18n.Messages` オブジェクトから取得することができます。

```scala
val title = Messages("home.title")
```

<!--
All internationalization API calls take an implicit `play.api.i18.Lang` argument retrieved from the current scope. You can also specify it explicitly:
-->
全ての国際化対応に関する API は、現在のスコープ内の implicit な `play.api.libs.i18n.Lang` を implicit parameter として受け取ります。implicit なしで、次のように明示することも可能です。

```scala
val title = Messages("home.title")(Lang("fr"))
```

<!--
> **Note:** If you have an implicit `Request` in the scope, it will provide an implicit `Lang` value corresponding to the preferred language extracted from the `Accept-Language` header and matching one of the application supported languages. You should add a `Lang` implicit parameter to your template like this: `@()(implicit lang: Lang)`.
-->
> **Note:** implicit な `Request` がスコープ内に存在する場合は、その `Accept-Language` ヘッダとアプリケーションの対応言語を考慮した上で適切な言語が決定されて、 implicit な `Lang` として提供されます。 テンプレートに `@()(implicit lang: Lang)` のように、 implicit パラメータ `LANG` を追加する必要があります。 

<!--
## Messages format
-->
## メッセージの書式

Messages are formatted using the `java.text.MessageFormat` library. For example, assuming you have message defined like:

```
files.summary=The disk {1} contains {0} file(s).
```

<!--
You can then specify parameters as:
-->
このメッセージのパラメータは次のように指定できます。

```scala
Messages("files.summary", d.files.length, d.name)
```

## Notes on apostrophes

Since Messages uses `java.text.MessageFormat`, please be aware that single quotes are used as a meta-character for escaping parameter substitutions.

For example, if you have the following messages defined:

@[apostrophe-messages](code/scalaguide/i18n/messages)
@[parameter-escaping](code/scalaguide/i18n/messages)

you should expect the following results:

@[apostrophe-messages](code/ScalaI18N.scala)
@[parameter-escaping](code/ScalaI18N.scala)

<!--
## Retrieving supported language from an HTTP request
-->
## HTTP リクエストから対応言語を取得する

<!--
You can retrieve the languages supported by a specific HTTP request:
-->
HTTP リクエストから対応言語を取得することができます。

```scala
def index = Action { request =>
  Ok("Languages: " + request.acceptLanguages.map(_.code).mkString(", "))
}
```

<!--
> **Next:** [[The application Global object | ScalaGlobal]]
-->
> **次ページ:** [[Global オブジェクト | ScalaGlobal]]
