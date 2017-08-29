<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
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
アプリケーションの対応言語は、`fr` や `en-US` のように、 **ISO 639-2 言語コード** の後に省略可能な **ISO 3166-1 alpha-2 国コード** を続けて指定します。

<!--
To start you need to specify the languages supported by your application in the `conf/application.conf` file:
-->
初めに、 `conf/application/conf.` ファイルであなたのアプリケーションの対応言語を指定しましょう。

```
play.i18n.langs = [ "en", "en-US", "fr" ]
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
メッセージは、`play.api.i18n.Messages` オブジェクトから取得することができます。

```scala
val title = Messages("home.title")
```

<!--
All internationalization API calls take an implicit `play.api.i18n.Messages` argument retrieved from the current scope. This implicit value contains both the language to use and (essentially) the internationalized messages.
-->
全ての国際化対応に関する API 呼び出しは、現在のスコープから暗黙の引数 `play.api.i18n.Messages` を受け取ります。この暗黙の値は、使用言語と (原則的に) 国際化されたメッセージの両方を含んでいます。

<!--
The simplest way to get such an implicit value is to use the `I18nSupport` trait. For instance you can use it as follows in your controllers:
-->
このような暗黙の値を取得する最も簡単な方法は、`I18nSupport` トレイトを使用することです。例えばコントローラーの中で次のように使用することができます。

@[i18n-support](code/ScalaI18N.scala)

<!--
The `I18nSupport` trait gives you an implicit `Messages` value as long as there is a `Lang` or a `RequestHeader` in the implicit scope.
-->
暗黙のスコープ内に `Lang` もしくは `RequestHeader` が存在する限り、`I18nSupport` トレイトは暗黙の `Messages` 値を与えます。

<!--
> **Note:** If you have a `RequestHeader` in the implicit scope, it will use the preferred language extracted from the `Accept-Language` header and matching one of the `MessagesApi` supported languages. You should add a `Messages` implicit parameter to your template like this: `@()(implicit messages: Messages)`.
-->
> **メモ:** `RequestHeader` が暗黙のスコープ内に存在する場合は、その `Accept-Language` ヘッダと `MessagesApi` の対応言語を考慮した上で適切な言語が決定され、使用されます。 テンプレートに `@()(implicit messages: Messages)` のように、暗黙のパラメータ `Messages` を追加する必要があります。

<!--
> **Note:** Also, Play “knows” out of the box how to inject a `MessagesApi` value (that uses the `DefaultMessagesApi` implementation), so you can just annotate your controller with the `@javax.inject.Inject` annotation and let Play automatically wire the components for you.
-->
> **メモ:** Play はなにも設定しなくても (`DefaultMessagesApi` 実装を使う) `MessagesApi` をインジェクションする方法も "知って" いるので、 `@javax.inject.Inject` アノテーションでコントローラを注釈するだけで、Play は自動的にこのコンポーネントを結びつけることができます。

<!--
## Messages format
-->
## メッセージの書式

<!--
Messages are formatted using the `java.text.MessageFormat` library. For example, assuming you have message defined like:
-->
メッセージは `java.text.MessageFormat` ライブラリを使ってフォーマットされます。例えば、次のようなメッセージが定義されているとしましょう。

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

<!--
## Notes on apostrophes
-->
## アポストロフィに対する注意

<!--
Since Messages uses `java.text.MessageFormat`, please be aware that single quotes are used as a meta-character for escaping parameter substitutions.
-->
メッセージには `java.text.MessageFormat` を使うので、シングルクォーテーションは引数代入をエスケープするためのメタキャラクタとして使われることに注意してください。

<!--
For example, if you have the following messages defined:
-->
例えば、以下のようなメッセージが定義されているとします:

@[apostrophe-messages](code/scalaguide/i18n/messages)
@[parameter-escaping](code/scalaguide/i18n/messages)

<!--
you should expect the following results:
-->
結果は以下のようになるでしょう:

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
