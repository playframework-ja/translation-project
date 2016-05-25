<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
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
HTTP PUT や POST リクエストはボディを含みます。このボディは `Content-Type` リクエストヘッダで指定さえしておけば、どんなフォーマットであっても構いません。 **ボディパーサー** はリクエストボディを Java の値に変換する役割を持ちます。

<!--
> **Note:** You can't write `BodyParser` implementation directly using Java. Because a Play `BodyParser` must handle the body content incrementally using an `Iteratee[Array[Byte], A]` it must be implemented in Scala.
>
> However Play provides default `BodyParser`s that should fit most use cases (parsing Json, Xml, Text, uploading files). And you can reuse these default parsers to create your own directly in Java; for example you can provide an RDF parsers based on the Text one.
-->
> **ノート:** Java を使って `BodyParser` を直接実装する事はできません。Play の `BodyParser` はリクエストボディの内容を `Iteratee[Array[Byte], A]` を使ってインクリメンタルに処理する必要があるため、 Scala で実装しなければなりません。
>
> しかし Play が提供するデフォルトの `BodyParser` はほとんどのケースで十分機能します (Json、Xml、テキストの解析やファイルアップロードなど)。そしてこれらのデフォルトパーサーを再利用して独自のボディパーサーを作ることができます。例えば、テキストのパーサーから RDF のパーサーを提供することができます。

<!--
## The `BodyParser` Java API
-->
## `BodyParser` API

<!--
When working with request bodies, ensure that have the following imports in your controller:
-->
リクエストボディを取り扱う場合は、コントローラに以下をインポートしていることを確認してください:

@[imports](code/javaguide/http/JavaBodyParsers.java)

<!--
In the Java API, all body parsers must generate a `play.mvc.Http.RequestBody` value. This value computed by the body parser can then be retrieved via `request().body()`:
-->
Java では全てのボディパーサーは `play.mvc.Http.RequestBody` を生成します。ボディパーサーから算出されたこの値は `request().body()` から取得できます。

@[request-body](code/javaguide/http/JavaBodyParsers.java)

<!--
You can specify the `BodyParser` to use for a particular action using the `@BodyParser.Of` annotation:
-->
アクションで使用する `BodyParser` を指定したい場合は、`@BodyParser.Of` アノテーションを使用します。

@[particular-body-parser](code/javaguide/http/JavaBodyParsers.java)

<!--
## The `Http.RequestBody` API
-->
## `Http.RequestBody` API

<!--
As we just said all body parsers in the Java API will give you a `play.mvc.Http.RequestBody` value. From this body object you can retrieve the request body content in the most appropriate Java type.
-->
先ほど Java の API では全てのボディパーサーは `play.mvc.Http.RequestBody` を提供すると述べました。このボディオブジェクトからはリクエストボディの内容を Java における適切な型で取得できます。

<!--
> **Note:** The `RequestBody` methods like `asText()` or `asJson()` will return `null` if the parser used to compute this request body doesn't support this content type. For example in an action method annotated with `@BodyParser.Of(BodyParser.Json.class)`, calling `asXml()` on the generated body will return `null`.
-->
> **ノート:** `asText()` や `asJson()` 等の `RequestBody` のメソッドは、パーサーがこのコンテントタイプをサポートしていない場合は `null` を返します。例えば、`@BodyParser.Of(BodyParser.Json.class)` アノテーションが付いたアクションメソッド内では、リクエストボディの `asXml()` を呼び出すと `null` を返します。

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
- **application/xml**, **text/xml** or **application/XXX+xml**: `org.w3c.Document`, accessible via `asXml()`
- **application/form-url-encoded**: `Map<String, String[]>`, accessible via `asFormUrlEncoded()`
- **multipart/form-data**: `Http.MultipartFormData`, accessible via `asMultipartFormData()`
- Any other content type: `Http.RawBuffer`, accessible via `asRaw()`
-->
- **text/plain**: `String`。`asText()` でアクセスできます
- **application/json**: `JsonNode`。`asJson()` でアクセスできます
- **application/xml**, **text/xml** または **application/XXX+xml**: `org.w3c.Document`。`asXml()` でアクセスできます
- **application/form-url-encoded**: `Map<String, String[]>`。`asFormUrlEncoded()` でアクセスできます
- **multipart/form-data**: `Http.MultipartFormData`。`asMultipartFormData()` でアクセスできます
- その他の Content-Type: `Http.RawBuffer`。`asRaw()`でアクセスできます

<!--
For example:
-->
例えば、以下のように使用します。

@[default-parser](code/javaguide/http/JavaBodyParsers.java)

<!--
## Max content length
-->
## 最大 content length

<!--
Text based body parsers (such as **text**, **json**, **xml** or **formUrlEncoded**) use a max content length because they have to load all the content into memory.  By default, the maximum content length that they will parse is 100KB.  It can be overridden by specifying the `play.http.parser.maxMemoryBuffer` property in `application.conf`:
-->
テキストベースのボディパーサー (**text**, **json**, **xml**, **formUrlEncoded** 等) は全てのコンテンツを一旦メモリにロードする必要があるため、最大 content length が設定されています。これらがパースするデフォルトの最大コンテント長は 100KB です。これは `application.conf` 内で `play.http.parser.maxMemoryBuffer` を指定して上書くことができます:

    play.http.parser.maxMemoryBuffer=128K

<!--
For parsers that buffer content on disk, such as the raw parser or `multipart/form-data`, the maximum content length is specified using the `play.http.parser.maxDiskBuffer` property, it defaults to 10MB.  The `multipart/form-data` parser also enforces the text max length property for the aggregate of the data fields.
-->
raw データや `multipart/form-data` などの、ディスクにコンテンツをバッファリングするパーサの最大コンテント長は `play.http.parser.maxDiskBuffer` プロパティを使って指定され、デフォルトは 10MB です。`multipart/form-data` パーサは、データフィールドを集約するためにテキストの最大コンテンツ長も適用します。

<!--
You can also override the default maximum content length for a given action via the `@BodyParser.Of` annotation:
-->
`@BodyParser.Of` アノテーションを使って、特定のアクションで最大コンテント長のデフォルト値を上書くこともできます:

@[max-length](code/javaguide/http/JavaBodyParsers.java)
