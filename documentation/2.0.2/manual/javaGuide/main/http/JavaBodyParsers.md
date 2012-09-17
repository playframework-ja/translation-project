<!-- translated -->
<!--
# Body parsers
-->
# ボディパーサー

<!--
## What is a body parser?
-->
## ボディパーサーの概要

<!--
An HTTP request (at least for those using the POST and PUT operations) contains a body. This body can be formatted with any format specified in the Content-Type header. A **body parser** transforms this request body into a Java value. 
-->
HTTP PUT や POST リクエストはボディを含みます。このボディには `Content-Type` リクエストヘッダで指定さえしておけば、どんなフォーマットであっても構いません。**ボディパーサー** はリクエストボディを Java の値に変換する役割を持ちます。

<!--
> **Note:** You can't write `BodyParser` implementation directly using Java. Because a Play `BodyParser` must handle the body content incrementaly using an `Iteratee[Array[Byte], A]` it must be implemented in Scala.
>
> However Play provides default `BodyParser`s that should fit most use cases (parsing Json, Xml, Text, uploading files). And you can reuse these default parsers to create your own directly in Java; for example you can provide an RDF parsers based on the Text one.
-->
> **ノート:** `BodyParser` は Java を使って直接実装する事は出来ません。Play の `BodyParser` はリクエストボディの内容を `Iteratee[Array[Byte], A]` を使ってインクリメンタルに処理する必要があるため、 Scala で実装しなければなりません。
>
> しかし Play が提供するデフォルトの `BodyParser` はほとんどのケースで十分機能します(Json、Xml、テキストの解析やファイルアップロードなど)。そしてこれらのデフォルトパーサーを再利用して独自のボディパーサーを作ることができます。例えば、テキストのパーサーから RDF のパーサーを提供することができます。

<!--
## The `BodyParser` Java API
-->
## `BodyParser` API

<!--
In the Java API, all body parsers must generate a `play.mvc.Http.RequestBody` value. This value computed by the body parser can then be retrieved via `request().body()`:
-->
Java では全てのボディパーサーは `play.mvc.Http.RequestBody` を生成します。ボディパーサーから算出されたこの値は `request().body()` から取得できます。

```
public static Result index() {
  RequestBody body = request().body();
  return ok("Got body: " + body);
}
```

<!--
You can specify the `BodyParser` to use for a particular action using the `@BodyParser.Of` annotation:
-->
アクションで使用する `BodyParser` を指定したい場合は、`@BodyParser.Of` アノテーションを使用します。

```
@BodyParser.Of(BodyParser.Json.class)
public static Result index() {
  RequestBody body = request().body();
  return ok("Got json: " + body.asJson());
}
```

<!--
## The `Http.RequestBody` API
-->
## `Http.RequestBody` API

<!--
As we just said all body parsers in the Java API will give you a `play.mvc.Http.RequestBody` value. From this body object you can retrieve the request body content in the most appropriate Java type.
-->
先ほど Java の API では全てのボディパーサーは `play.mvc.Http.RequestBody` を提供すると述べました。このボディオブジェクトからはリクエストボディの内容を Java における適切な型で取得できます。

<!--
> **Note:** The `RequestBody` methods like `asText()` or `asJson()` will return null if the parser used to compute this request body doesn't support this content type. For example in an action method annotated with `@BodyParser.Of(BodyParser.Json.class)`, calling `asXml()` on the generated body will retun null.
-->
> **ノート:** `asText()` や `asJson()` 等の `RequestBody` のメソッドは、パーサーがこのコンテントタイプをサポートしていない場合は null を返します。例えば、`@BodyParser.Of(BodyParser.Json.class)` アノテーションが付いたアクションメソッド内では、リクエストボディの `asXml()` を呼び出すと null を返します。

<!--
Some parsers can provide a most specific type than `Http.RequestBody` (ie. a subclass of `Http.RequestBody`). You can automatically cast the request body into another type using the `as(...)` helper method:
-->
いくつかのパーサーは `Http.RequestBody` よりも明確な型 (つまり `Http.RequestBody` のサブクラス) を提供する事があります。リクエストボディを他の型に自動的にキャストするには `as(...)` ヘルパーメソッドを使用します。

```
@BodyParser.Of(BodyLengthParser.class)
pulic static Result index() {
  BodyLength body = request().body().as(BodyLength.class);
  ok("Request body length: " + body.getLength());
}
```

<!--
## Default body parser: AnyContent
-->
## デフォルトのボディパーサー： AnyContent

<!--
If you don't specify your own body parser, Play will use the default one guessing the most appropriate content type from the `Content-Type` header:
-->
ボディパーサーを指定しない場合、Play はデフォルトのボディパーサーを使用します。このパーサーは `Content-Type` ヘッダから最も適切なコンテントタイプを推測してくれます。

<!--
- **text/plain**: `String`, accessible via `asText()`
- **application/json**: `JsonNode`, accessible via `asJson()`
- **text/xml**: `org.w3c.Document`, accessible via `asXml()`
- **application/form-url-encoded**: `Map<String, String[]>`, accessible via `asFormUrlEncoded()`
- **multipart/form-data**: `Http.MultipartFormData`, accessible via `asMultipartFormData()`
- Any other content type: `Http.RawBuffer`, accessible via `asRaw()`
-->
- **text/plain**: `String`。`asText()` でアクセスできます。
- **application/json**: `JsonNode`。`asJson()` でアクセスできます。
- **text/xml**: `org.w3c.Document`。`asXml()` でアクセスできます。
- **application/form-url-encoded**: `Map<String, String[]>`。`asFormUrlEncoded()` でアクセスできます。
- **multipart/form-data**: `Http.MultipartFormData`。`asMultipartFormData()` でアクセスできます。
- その他の Content-Type: `Http.RawBuffer`。`asRaw()`でアクセスできます。

<!--
Example:
-->
例えば、以下のように使用します。

```
pulic static Result save() {
  RequestBody body = request().body();
  String textBody = body.asText();
  
  if(textBody != null) {
    ok("Got: " + text);
  } else {
    badRequest("Expecting text/plain request body");
  }
}
```

<!--
## Max content length
-->
## 最大 Content Length

<!--
Text based body parsers (such as **text**, **json**, **xml** or **formUrlEncoded**) use a max content length because they have to load all the content into memory. 
-->
テキストベースのボディパーサー (**text**, **json**, **xml**, **formUrlEncoded** 等) は全てのコンテンツを一旦メモリにロードする必要があるため、最大 Content Lengthが設定されています。

<!--
There is a default content length (the default is 100KB). 
-->
デフォルトでは Content Length は 100KB です。

<!--
> **Tip:** The default content size can be defined in `application.conf`:
> 
> `parsers.text.maxLength=128K`
-->
> **Tip:** デフォルトのコンテンツサイズは `application.conf` から次のように定義できます。
>
> `parsers.text.maxLength=128K`


<!--
You can also specify a maximum content length via the `@BodyParser.Of` annotation:
-->
最大のコンテンツサイズは `@BodyParser.Of` アノテーションで指定することができます。

```
// Accept only 10KB of data.
@BodyParser.Of(value = BodyParser.Text.class, maxLength = 10 * 1024)
pulic static Result index() {
  if(request().body().isMaxSizeExceeded()) {
    return badRequest("Too much data!");
  } else {
    ok("Got body: " + request().body().asText()); 
  }
}
```

<!--
> **Next:** [[Actions composition | JavaActionsComposition]]
-->
> **次ページ:** [[アクションの合成 | JavaActionsComposition]]
