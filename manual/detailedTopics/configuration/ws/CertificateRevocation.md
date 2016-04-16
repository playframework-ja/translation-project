<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring Certificate Revocation
-->
# 証明書失効の設定

<!--
Certificate Revocation in JSSE can be done through two means: certificate revocation lists (CRLs) and OCSP.
-->
JSSE における証明書の失効は、ふたつの方法: 証明書失効リスト (CRL) と OCSP によって行うことができます。

<!--
Certificate Revocation can be very useful in situations where a server's private keys are compromised, as in the case of [Heartbleed](http://heartbleed.com).
-->
証明書の失効は、[Heartbleed](http://heartbleed.com) などのようにサーバの秘密鍵が危険にさらされている場合に便利です。

<!--
Certificate Revocation is disabled by default in JSSE.  It is defined in two places:
-->
JSSE ではデフォルトで証明書失効が無効化されています。これは二箇所で定義されています:

<!--
* [PKI Programmer's Guide, Appendix C](https://docs.oracle.com/javase/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html#AppC)
* [Enable OCSP Checking](https://blogs.oracle.com/xuelei/entry/enable_ocsp_checking)
-->
* [Java PKI APIプログラマーズ・ガイド](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html#AppC)
* [Enable OCSP Checking](https://blogs.oracle.com/xuelei/entry/enable_ocsp_checking)

<!--
To enable OCSP, you must set the following system properties on the command line:
-->
OCSP を有効にするには、コマンドラインで以下のシステムプロパティを設定しなければなりません:

```
java -Dcom.sun.security.enableCRLDP=true -Dcom.sun.net.ssl.checkRevocation=true
```

<!--
After doing the above, you can enable certificate revocation in the client:
-->
上記を実行後、クライアントで証明書失効を有効化することができます:

```
play.ws.ssl.checkRevocation = true
```

<!--
Setting `checkRevocation` will set the internal `ocsp.enable` security property automatically:
-->
`checkRevocation` を設定すると、内部で自動的に `ocsp.enable` セキュリティプロパティが設定されます:

```
java.security.Security.setProperty("ocsp.enable", "true")
```

<!--
And this will set OCSP checking when making HTTPS requests.
-->
こうすると、HTTPS リクエストの送信時に OCSP による確認が行われるようになります。

<!--
> NOTE: Enabling OCSP requires a round trip to the OCSP responder.  This adds a notable overhead on HTTPS calls, and can make calls up to [33% slower](https://blog.cloudflare.com/ocsp-stapling-how-cloudflare-just-made-ssl-30).  The mitigation technique, OCSP stapling, is not supported in JSSE.
-->
> 注意: OCSP を有効にするには、OCSP レスポンダへの往復が必要です。これは HTTPS 呼び出しに顕著なオーバーヘッドを加え、呼び出しを最大で [33% 遅く](https://blog.cloudflare.com/ocsp-stapling-how-cloudflare-just-made-ssl-30) する可能性があります。これを緩和するテクニックである OCSP ステープリングは JSSE でサポートされていません。

<!--
Or, if you wish to use a static CRL list, you can define a list of URLs:
-->
または、静的な CRL リストを使いたい場合はリストの URL を定義することができます:

```
play.ws.ssl.revocationLists = [ "http://example.com/crl" ]
```

<!--
## Debugging
-->
## デバッグ

<!--
To test certificate revocation is enabled, set the following options:
-->
証明書失効が有効になっていることを確認するには、以下のオプションを設定します:

```
play.ws.ssl.debug = {
 certpath = true
 ocsp = true
}
```

<!--
And you should see something like the following output:
-->
こうすると、以下のような出力が確認できるはずです:

```
certpath: -Using checker7 ... [sun.security.provider.certpath.RevocationChecker]
certpath: connecting to OCSP service at: http://gtssl2-ocsp.geotrust.com
certpath: OCSP response status: SUCCESSFUL
certpath: OCSP response type: basic
certpath: Responder's name: CN=GeoTrust SSL CA - G2 OCSP Responder, O=GeoTrust Inc., C=US
certpath: OCSP response produced at: Wed Mar 19 13:57:32 PDT 2014
certpath: OCSP number of SingleResponses: 1
certpath: OCSP response cert #1: CN=GeoTrust SSL CA - G2 OCSP Responder, O=GeoTrust Inc., C=US
certpath: Status of certificate (with serial number 159761413677206476752317239691621661939) is: GOOD
certpath: Responder's certificate includes the extension id-pkix-ocsp-nocheck.
certpath: OCSP response is signed by an Authorized Responder
certpath: Verified signature of OCSP Response
certpath: Response's validity interval is from Wed Mar 19 13:57:32 PDT 2014 until Wed Mar 26 13:57:32 PDT 2014
certpath: -checker7 validation succeeded
```

<!--
## Further Reading
-->
## 参考文献

<!--
* [Fixing Certificate Revocation](https://tersesystems.com/2014/03/22/fixing-certificate-revocation/)
-->
* [Fixing Certificate Revocation](https://tersesystems.com/2014/03/22/fixing-certificate-revocation/)