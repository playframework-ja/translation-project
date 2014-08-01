<!-- translated -->
<!--
# Content negotiation
-->
# コンテンツネゴシエーション

<!--
Content negotiation is a mechanism that makes it possible to serve different representation of a same resource (URI). It is useful *e.g.* for writing Web Services supporting several output formats (XML, JSON, etc.). Server-driven negotiation is essentially performed using the `Accept*` requests headers. You can find more information on content negotiation in the [HTTP specification](http://www.w3.org/Protocols/rfc2616/rfc2616-sec12.html).
-->
コンテンツネゴシエーションとは、同一のリソース (URI) を複数のフォーマットで提供するためのメカニズムです。用途としては、*例えば*複数の出力フォーマット (XML, JSON など) をサポートするような Web サービスを実装する場合です。サーバ駆動のネゴシエーションは `Accept*` リクエストヘッダの内容に基いて行われます。コンテンツネゴシエーションについての詳細は [HTTP の仕様](http://www.w3.org/Protocols/rfc2616/rfc2616-sec12.html) を参照してください。

<!--
# Language
-->
# 言語

<!--
You can get the list of acceptable languages for a request using the `play.api.mvc.RequestHeader#acceptLanguages` method that retrieves them from the `Accept-Language` header and sorts them according to their quality value. Play uses it in the `play.api.mvc.Controller#lang` method that provides an implicit `play.api.i18n.Lang` value to your actions, so they automatically use the best possible language (if supported by your application, otherwise your application’s default language is used).
-->
`play.api.mvc.RequestHeader#acceptLanguages` メソッドを呼び出すと、リクエストの `Accept-Language` ヘッダから、クライアント側で受付可能な言語のリストを取り出すことができます。リストは quality 値によってソートされます。Play は `play.api.mvc.Controller#lang` メソッド内でこのメソッドを呼び出して、 implicit な `play.api.i18n.Lang` 値をアクションのスコープ内に提供しています。これによって、アクション内では常に最適な言語 (クライアントが希望している言語をあなたのアプリケーションがサポートしている場合はそれを、そうでなければアプリケーションのデフォルト言語が代わりに提供されます) を利用することができます。　

<!--
# Content
-->
# コンテンツ

<!--
Similarly, the `play.api.mvc.RequestHeader#acceptedTypes` method gives the list of acceptable result’s MIME types for a request. It retrieves them from the `Accept` request header and sorts them according to their quality factor.

Actually, the `Accept` header does not really contain MIME types but media ranges (*e.g.* a request accepting all text results may set the `text/*` range, and the `*/*` range means that all result types are acceptable). Controllers provide a higher-level `render` method to help you to handle media ranges. Consider for example the following action definition:
-->
同様に、 `play.api.mvc.RequestHeader#acceptedTypes` メソッドを呼び出すことで、リクエストの `Accept` ヘッダから、クライアント側で受付可能な MIME タイプのリストを取り出すことができます。リストは quality 値でソートされます。

実際に `Accept` ヘッダに含まれているのは MIME タイプではなく メディアレンジ (*例えば* 任意のテキストを受け入れる場合は `text/*` というレンジ、ありとあらゆる結果を受け入れる場合は `*/*` というレンジ) です。コントローラは高級な `render` メソッドを提供することで、これらメディアレンジの処理を補助してくれます。例として、次のアクション定義を見てください。

@[negotiate_accept_type](code/ScalaContentNegotiation.scala)

<!--
`Accepts.Html()` and `Accepts.Json()` are extractors testing if a given media range matches `text/html` and `application/json`, respectively. The `render` method takes a partial function from `play.api.http.MediaRange` to `play.api.mvc.Result` and tries to apply it to each media range found in the request `Accept` header, in order of preference. If none of the acceptable media ranges is supported by your function, the `NotAcceptable` result is returned.
-->
`Accepts.Html()` および `Accepts.Json()` は、それぞれメディアレンジが `text/html` や `application/json` にマッチするかをテストする抽出子です。 `render` メソッドは `play.api.http.MediaRange` を入力に `play.api.mvc.Result` を返す PartialFunction を引数にとり、リクエストの `Accept` ヘッダ内のメディアレンジに優先度順に適用します。その PartialFunction で受け入れ可能なメディアレンジが一つも存在しない場合、 `NotAcceptable` という結果が返ります。

<!--
For example, if a client makes a request with the following value for the `Accept` header: `*/*;q=0.5,application/json`, meaning that it accepts any result type but prefers JSON, the above code will return the JSON representation. If another client makes a request with the following value for the `Accept` header: `application/xml`, meaning that it only accepts XML, the above code will return `NotAcceptable`.
-->
例えば、任意の型の結果を受け入れるが、 JSON を優先する、という意味である `*/*;q=0.5,application/json` という `Accept` ヘッダを持つリクエストをクライアントが送信したとします。この場合、前述のコードは `JSON` 形式の結果を返します。一方、XML のみ受け付けるという意味である `Accept` ヘッダ `application/xml` を含むリクエストを他のクライアントが送信したとすると、 `NotAcceptable` を返します。

<!--
# Request extractors
-->
# リクエスト抽出子

<!--
See the API documentation of the `play.api.mvc.AcceptExtractors.Accepts` object for the list of the MIME types supported by Play out of the box in the `render` method. You can easily create your own extractor for a given MIME type using the `play.api.mvc.Accepting` case class, for example the following code creates an extractor checking that a media range matches the `audio/mp3` MIME type:
-->
`play.api.mvc.AcceptExtractors.Accepts` オブジェクトの API ドキュメントには、 Play が始めからから `render` メソッドでサポートしている MIME タイプの一覧が掲載されています。さらに、 `play.api.mvc.Accepting` ケースクラスを使うと、任意の MIME タイプに対応する独自の抽出子を実装することができます。例えば、メディアレンジが `audo/mp3` MIME タイプにマッチするかどうかをチェックする抽出子は、次のように定義します。

@[extract_custom_accept_type](code/ScalaContentNegotiation.scala)


<!--
> **Next:** [[Asynchronous HTTP programming | ScalaAsync]]
-->
> **次ページ:** [[非同期 HTTP プログラミング | ScalaAsync]]
