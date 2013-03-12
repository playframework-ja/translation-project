<!--
# Content negotiation
-->
# コンテントネゴシエーション

<!--  
Content negotiation is a mechanism that makes it possible to serve different representation of a same resource (URI). It is useful *e.g.* for writing Web Services supporting several output formats (XML, JSON, etc.). Server-driven negotiation is essentially performed using the `Accept*` requests headers. You can find more information on content negotiation in the [[HTTP specification|http://www.w3.org/Protocols/rfc2616/rfc2616-sec12.html]].
-->
コンテントネゴシエーションは、同じリソース (URI) から異なる表現を提供することを可能にする機構です。これは *例えば* いくつかの出力フォーマット (XML, JSON, etc.) をサポートする Web サービスを書く場合に便利です。サーバ主導のネゴシエーションは、基本的に `Accept*` リクエストヘッダを使って実行されます。[[HTTP 仕様|http://www.w3.org/Protocols/rfc2616/rfc2616-sec12.html]] で、コンテントネゴシエーションについてもっと詳しい情報を見つけることができます。

<!--
## Language
-->
## 言語

<!--
You can get the list of acceptable languages for a request using the `play.mvc.Http.RequestHeader#acceptLanguages` method that retrieves them from the `Accept-Language` header and sorts them according to their quality value. Play uses it to set the `lang` value of request’s HTTP context, so they automatically use the best possible language (if supported by your application, otherwise your application’s default language is used).
-->
あるリクエストで利用可能な言語の一覧は、`Accept-Language` ヘッダからこれらを探して、品質値に従って並び替える `play.mvc.Http.RequestHeader#acceptLanguages` を使うことで取得できます。Play はこれを使ってリクエストの HTTP コンテキストに `lang` 値を設定するので、 最適な言語が使用されます (アプリケーションがサポートしている場合で、そうでない場合はアプリケーションのデフォルト言語が使用されます) 。

<!--
## Content
-->
## コンテンツ

<!--
Similarly, the `play.mvc.Http.RequestHeader#acceptedTypes` method gives the list of acceptable result’s MIME types for a request. It retrieves them from the `Accept` request header and sorts them according to their quality factor.
-->
同様に、`play.mvc.Http.RequestHeader#acceptedTypes` メソッドは、あるリクエストに対するレスポンスにおいて利用可能な MIME タイプの一覧を提供します。このメソッドは、利用可能な MIME タイプを `Accept` リクエストヘッダから検索し、品質値に従って並び替えます。

<!--
You can test if a given MIME type is acceptable for the current request using the `play.mvc.Http.RequestHeader#accepts` method:
-->
`play.mvc.Http.RequestHeader#accepts` メソッドを使って、現在のリクエストが提供される MIME タイプを利用できるかどうかをテストすることができます:

```
public static Result list() {
  List<Item> items = Item.find.all();
  if (request().accepts("text/html")) {
    return ok(views.html.Application.list.render(items));
  } else {
    ObjectNode result = Json.newObject();
    ...
    return ok(result);
  }
}
```

<!--
> **Next:** [[Asynchronous HTTP programming | JavaAsync]]
-->
> **Next:** [[非同期 HTTP プログラミング | JavaAsync]]