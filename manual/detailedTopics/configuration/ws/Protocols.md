<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring Protocols
-->
# プロトコルの設定

<!--
By default, WS SSL will use the most secure version of the TLS protocol available in the JVM.
-->
WS SSL はデフォルトで、JVM が利用できるもののうち、もっともセキュアな TLS プロトコルを使います。

<!--
* On JDK 1.7 and later, the default protocol is "TLSv1.2".
* On JDK 1.6, the default protocol is "TLSv1".
-->
* JDK 1.7 以降の場合、デフォルトプロトコルは "TLSv1.2" です。
* JDK 1.6 の場合、デフォルトプロトコルは "TLSv1" です。

<!--
The full protocol list in JSSE is available in the [Standard Algorithm Name Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#jssenames).
-->
JSSE で利用できるプロトコルの全リストは [標準アルゴリズムのドキュメント](https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#jssenames) にあります。

<!--
## Defining the default protocol
-->
## デフォルトプロトコルの定義

<!--
If you want to define a different [default protocol](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLContext.html#getInstance\(java.lang.String\)), you can set it specifically in the client:
-->
異なる [デフォルトプロトコル](https://docs.oracle.com/javase/jp/8/docs/api/javax/net/ssl/SSLContext.html#getInstance\(java.lang.String\)) を定義したい場合は、クライアントで具体的に指定します:

```
# Passed into SSLContext.getInstance()
play.ws.ssl.protocol = "TLSv1.2"
```

<!--
If you want to define the list of enabled protocols, you can do so explicitly:
-->
利用可能なプロトコルのリストを定義したい場合、明確に定義することができます:

```
# passed into sslContext.getDefaultParameters().setEnabledProtocols()
play.ws.ssl.enabledProtocols = [
  "TLSv1.2",
  "TLSv1.1",
  "TLSv1"
]
```

<!--
If you are on JDK 1.8, you can also set the `jdk.tls.client.protocols` system property to enable client protocols globally.
-->
JDK 1.8 を使っている場合、`jdk.tls.client.protocols` システムプロパティを設定して、クライアントプロトコルをグローバルに利用することもできます。

<!--
WS recognizes "SSLv3", "SSLv2" and "SSLv2Hello" as weak protocols with a number of [security issues](https://www.schneier.com/paper-ssl.pdf), and will throw an exception if they are in the `play.ws.ssl.enabledProtocols` list.  Virtually all servers support `TLSv1`, so there is no advantage in using these older protocols.
-->
WS は "SSLv3", "SSLv2" そして "SSLv2Hello" を多数の [セキュリティ上の問題](https://www.schneier.com/paper-ssl.pdf) を抱えた弱いプロトコルと認識しており、これらが `play.ws.ssl.enabledProtocols` リストに含まれている場合は例外を投げます。事実上、すべてのサーバは `TLSv1` をサポートしているので、これらの古いプロトコルを使う利点はありません。

<!--
## Debugging
-->
## デバッグ

<!--
The debug options for configuring protocol are:
-->
プロトコル設定のデバッグオプションは以下の通りです:

```
play.ws.ssl.debug = {
  ssl = true
  sslctx = true
  handshake = true
  verbose = true
  data = true
}
```
