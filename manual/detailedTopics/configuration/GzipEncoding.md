<!--
# Configuring gzip encoding
-->
# gzip エンコードの設定

<!--
Play provides a gzip filter that can be used to gzip responses.  It can be added to the applications filters using the `Global` object.
-->
Play はレスポンスを gzip に変換するフィルタを提供しています。`Global` オブジェクトを使うことでアプリケーションにこのフィルタを追加することができます。

<!--
## Enabling gzip in Scala
-->
## Scala で gzip を利用可能にする

<!--
The simplest way to enable the gzip filter in a Scala project is to use the `WithFilters` helper:
-->
Scala プロジェクトの中で `WithFilters` ヘルパーを使うことが gzip を利用可能にする簡単な方法です。

@[global](code/GzipEncoding.scala)

<!--
To control which responses are and aren't implemented, use the `shouldGzip` parameter, which accepts a function of a request header and a response header to a boolean.
-->
有効にするかしないかを操作するために `shouldGzip` パラメータを使います。これはリクエストヘッダとレスポンスヘッダの関数のブール値で受け取ることができます。

<!--
For example, the code below only gzips HTML responses:
-->
例えば次のコードは HTML レスポンスを gzip するだけとなります。

@[should-gzip](code/GzipEncoding.scala)

<!--
## Enabling GZIP in Java
-->
## Java で gzip を利用可能にする

<!--
To enable gzip in Java, add it to the list of filters in the `Global` object:
-->
Java で gzip を利用可能にするには、`Global` オブジェクトにあるフィルタのリストに gzip を追加します。

@[global](code/detailedtopics/configuration/gzipencoding/Global.java)
