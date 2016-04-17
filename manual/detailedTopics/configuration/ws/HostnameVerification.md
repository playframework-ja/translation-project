<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring Hostname Verification
-->
# ホスト名検証の設定

<!--
Hostname verification is a little known part of HTTPS that involves a [server identity check](https://tools.ietf.org/search/rfc2818#section-3.1) to ensure that the client is talking to the correct server and has not been redirected by a man in the middle attack.
-->
ホスト名検証は、クライアントが正しいサーバと会話しており、また中間者攻撃によってリダイレクトされていないことを保証する [サーバの身元確認](https://www.ipa.go.jp/security/rfc/RFC2818JA.html#31) に含まれた、HTTPS でもあまり知られていない部分です。

<!--
The check involves looking at the certificate sent by the server, and verifying that the `dnsName` in the `subjectAltName` field of the certificate matches the host portion of the URL used to make the request.
-->
この確認はサーバから送られた証明書を確認し、証明書の `subjectAltName` フィールドにある `dnsName` がリクエストした URL のホスト部分と一致することを検証します。

<!--
WS SSL does hostname verification automatically, using the [DefaultHostnameVerifier](api/scala/play/api/libs/ws/ssl/DefaultHostnameVerifier.html) to implement the [hostname verifier](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#HostnameVerifier) fallback interface.
-->
WS SSL は、[ホスト名検証方式](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#HostnameVerifier) フォールバックインタフェースを実装する [DefaultHostnameVerifier](api/scala/play/api/libs/ws/ssl/DefaultHostnameVerifier.html) を使って、自動的にホスト名検証を行います。

<!--
## Modifying the Hostname Verifier
-->
## ホスト名検証方式の変更

<!--
If you need to specify a different hostname verifier, you can configure `application.conf` to provide your own custom [`HostnameVerifier`](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html):
-->
別のホスト名検証方式を指定する必要がある場合は、`application.conf` に独自の [`HostnameVerifier`](https://docs.oracle.com/javase/jp/8/docs/api/javax/net/ssl/HostnameVerifier.html) を提供するよう設定することができます。

```
play.ws.ssl.hostnameVerifierClass=org.example.MyHostnameVerifier
```

<!--
## Debugging
-->
## デバッグ

<!--
Hostname Verification can be tested using `dnschef`.  A complete guide is out of scope of documentation, but an example can be found in [Testing Hostname Verification](https://tersesystems.com/2014/03/31/testing-hostname-verification/).
-->
ホスト名検証は `dnschef` を使ってテストすることができます。完全な解説はこのドキュメントのスコープ外ですが、[Testing Hostname Verification](https://tersesystems.com/2014/03/31/testing-hostname-verification/) で使用例が見つかります。

<!--
## Further Reading
-->
## 参考文献

<!--
* [Fixing Hostname Verification](https://tersesystems.com/2014/03/23/fixing-hostname-verification/)
-->
* [Fixing Hostname Verification](https://tersesystems.com/2014/03/23/fixing-hostname-verification/)
