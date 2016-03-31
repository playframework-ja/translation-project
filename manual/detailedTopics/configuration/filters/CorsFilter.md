<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Cross-Origin Resource Sharing
-->
# Cross-Origin Resource Sharing

<!--
Play provides a filter that implements Cross-Origin Resource Sharing (CORS).
-->
Play は Cross-Origin Resource Sharing (CORS) を実装したフィルタを備えています。

<!--
CORS is a protocol that allows web applications to make requests from the browser across different domains.  A full specification can be found [here](http://www.w3.org/TR/cors/).
-->
CORS は、web アプリケーションがブラウザから異なるドメインにリクエストを送信することのできるプロトコルです。完全な仕様は [ここ](http://www.w3.org/TR/cors/) で確認することができます。

<!--
## Enabling the CORS filter
-->
## CORS フィルタの有効化

<!--
To enable the CORS filter, add the Play filters project to your `libraryDependencies` in `build.sbt`:
-->
CORS フィルタを有効にするには、`build.sbt` 内の `libraryDependencies` に Play filters プロジェクトを追加してください:

@[content](code/filters.sbt)

<!--
Now add the CORS filter to your filters, which is typically done by creating a `Filters` class in the root of your project:
-->
ここで、この filters に CORS フィルタを追加しますが、通常これはプロジェクトルートに `Filters` クラスを作成することで実現します:

Scala
: @[filters](code/CorsFilter.scala)

Java
: @[filters](code/detailedtopics/configuration/cors/Filters.java)

<!--
## Configuring the CORS filter
-->
## CORS フィルタの設定

<!--
The filter can be configured from `application.conf`.  For a full listing of configuration options, see the Play filters [`reference.conf`](resources/confs/filters-helpers/reference.conf).
-->
このフィルタは `application.conf` で設定することができます。設定オプションの全リストは、Play フィルタの [`reference.conf`](resources/confs/filters-helpers/reference.conf) を参照してください。

<!--
The available options include:
-->
利用できるオプションには以下のようなものがあります:

<!--
* `play.filters.cors.pathPrefixes` - filter paths by a whitelist of path prefixes
* `play.filters.cors.allowedOrigins` - allow only requests with origins from a whitelist (by default all origins are allowed)
* `play.filters.cors.allowedHttpMethods` - allow only HTTP methods from a whitelist for preflight requests (by default all methods are allowed)
* `play.filters.cors.allowedHttpHeaders` - allow only HTTP headers from a whitelist for preflight requests (by default all headers are allowed)
* `play.filters.cors.exposedHeaders` - set custom HTTP headers to be exposed in the response (by default no headers are exposed)
* `play.filters.cors.supportsCredentials` - disable/enable support for credentials (by default credentials support is enabled)
* `play.filters.cors.preflightMaxAge` - set how long the results of a preflight request can be cached in a preflight result cache (by default 1 hour)
-->
* `play.filters.cors.pathPrefixes` - パス接頭辞のホワイトリストによるフィルタパス
* `play.filters.cors.allowedOrigins` - リクエストを許可するオリジンのみのホワイトリスト (デフォルトはすべてのオリジンを許可)
* `play.filters.cors.allowedHttpMethods` - プリフライトリクエストに使える HTTP メソッドのみのホワイトリスト (デフォルトはすべてのメソッドを許可)
* `play.filters.cors.allowedHttpHeaders` - プリフライトリクエストに使える HTTP ヘッダのみのホワイトリスト (デフォルトはすべてのヘッダを許可)
* `play.filters.cors.exposedHeaders` - レスポンスに含めるカスタム HTTP ヘッダ (デフォルトは含まれるヘッダ無し)
* `play.filters.cors.supportsCredentials` - クレデンシャルサポートの無効/有効 (デフォルトはクレデンシャルサポート有効)
* `play.filters.cors.preflightMaxAge` - プリフライトリクエストの結果をキャッシュできる期間 (デフォルトは一時間)

<!--
For example:
-->
例:

```
play.filters.cors {
  pathPrefixes = ["/some/path", ...]
  allowedOrigins = ["http://www.example.com", ...]
  allowedHttpMethods = ["GET", "POST"]
  allowedHttpHeaders = ["Accept"]
  preflightMaxAge = 3 days
}
```
