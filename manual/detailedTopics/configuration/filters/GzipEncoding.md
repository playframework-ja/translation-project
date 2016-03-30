<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring gzip encoding
-->
# gzip エンコーディングの設定

<!--
Play provides a gzip filter that can be used to gzip responses.
-->
Play は gzip レスポンスに使われる gzip フィルタを備えています。

<!--
## Enabling the gzip filter
-->
## gzip フィルタの有効化

<!--
To enable the gzip filter, add the Play filters project to your `libraryDependencies` in `build.sbt`:
-->
gzip フィルタを有効にするには、`build.sbt` 内の `libraryDependencies` に Play filters プロジェクトを追加してください:

@[content](code/filters.sbt)

<!--
Now add the gzip filter to your filters, which is typically done by creating a `Filters` class in the root of your project:
-->
ここで、この filters に gzip フィルタを追加しますが、通常これはプロジェクトルートに `Filters` クラスを作成することで実現します:

Scala
: @[filters](code/GzipEncoding.scala)

Java
: @[filters](code/detailedtopics/configuration/gzipencoding/Filters.java)

<!--
The `Filters` class can either be in the root package, or if it has another name or is in another package, needs to be configured using `play.http.filters` in `application.conf`:
-->
`Filters` クラスはルートパッケージに置くか、もし違う名前にする、あるいは違うパッケージに置く場合は、`application.conf` 内で `play.http.filters` を使って設定する必要があります:

```
play.http.filters = "filters.MyFilters"
```

<!--
## Configuring the gzip filter
-->
## gzip フィルタの設定

<!--
The gzip filter supports a small number of tuning configuration options, which can be configured from `application.conf`.  To see the available configuration options, see the Play filters [`reference.conf`](resources/confs/filters-helpers/reference.conf).
-->
gzip フィルタは、わずかながら `application.conf` で設定できるチューニング設定オプションをサポートしています。利用できる設定オプションは、Play フィルタの [`reference.conf`](resources/confs/filters-helpers/reference.conf) を参照してください。

<!--
## Controlling which responses are gzipped
-->
## gzip されるレスポンスの管理

<!--
To control which responses are and aren't implemented, use the `shouldGzip` parameter, which accepts a function of a request header and a response header to a boolean.
-->
レスポンスが gzip されるか否かを操作するには、リクエストヘッダとレスポンスヘッダから真偽値を返す関数を受け取る `shouldGzip` パラメータを使います。

<!--
For example, the code below only gzips HTML responses:
-->
例えば、以下のコードは HTML レスポンスのみ gzip します:

@[should-gzip](code/GzipEncoding.scala)
