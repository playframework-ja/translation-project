<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring Cipher Suites
-->
# 暗号スイートの設定

<!--
A [cipher suite](https://en.wikipedia.org/wiki/Cipher_suite) is really four different ciphers in one, describing the key exchange, bulk encryption, message authentication and random number function.  There is [no official naming convention](https://utcc.utoronto.ca/~cks/space/blog/tech/SSLCipherNames) of cipher suites, but most cipher suites are described in order -- for example, "TLS_DHE_RSA_WITH_AES_256_CBC_SHA" uses DHE for key exchange, RSA for server certificate authentication, 256-bit key AES in CBC mode for the stream cipher, and SHA for the message authentication.
-->
[暗号スイート](https://en.wikipedia.org/wiki/Cipher_suite) は、実際には鍵交換、バルク暗号化、メッセージ認証とランダム数関数の四つの異なる暗号化技術をひとつにしたものです。暗号化スイートの [公式な命名規約はありません](https://utcc.utoronto.ca/~cks/space/blog/tech/SSLCipherNames) が、ほとんどの暗号化スイートでは順番に表現されています -- 例えば、"TLS_DHE_RSA_WITH_AES_256_CBC_SHA" は鍵交換に DHE、サーバ証明書認証に RSA、ストリーム暗号に 256 ビット鍵の AES を CBC モードで、そしてメッセージ認証に SHA を使います。

<!--
## Configuring Enabled Ciphers
-->
## 利用可能な暗号の設定

<!--
The list of cipher suites has changed considerably between 1.6, 1.7 and 1.8.
-->
暗号スイートのリストは 1.6, 1.7 そして 1.8 で大幅に変更されています。

<!--
In 1.7 and 1.8, the default [out of the box](http://sim.ivi.co/2011/07/jsse-oracle-provider-preference-of-tls.html) cipher suite list is used.
-->
1.7 と 1.8 の場合、デフォルト [そのまま](http://sim.ivi.co/2011/07/jsse-oracle-provider-preference-of-tls.html) の暗号スイートリストが使われます。

<!--
In 1.6, the out of the box list is [out of order](http://op-co.de/blog/posts/android_ssl_downgrade/), with some weaker cipher suites configured in front of stronger ones, and contains a number of ciphers that are now considered weak.  As such, the default list of enabled cipher suites is as follows:
-->
1.6 の場合、デフォルトのリストは [順不同](http://op-co.de/blog/posts/android_ssl_downgrade/) で、弱い暗号が強いものよりも前に設定されていたり、現在では脆弱と見なされている暗号をいくつか含んでいます。利用可能な暗号スイートのリストは、以下のとおりです:

```
  "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
  "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
  "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
  "TLS_RSA_WITH_AES_256_CBC_SHA",
  "TLS_RSA_WITH_AES_128_CBC_SHA",
  "SSL_RSA_WITH_RC4_128_SHA",
  "SSL_RSA_WITH_RC4_128_MD5",
  "TLS_EMPTY_RENEGOTIATION_INFO_SCSV" // per RFC 5746
```

<!--
The list of cipher suites can be configured manually using the `play.ws.ssl.enabledCiphers` setting:
-->
暗号スイートのリストは `play.ws.ssl.enabledCiphers` 設定を使って手動で設定することができます:

```
play.ws.ssl.enabledCiphers = [
  "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256"
]
```

<!--
This can be useful to enable perfect forward security, for example, as only DHE and ECDHE cipher suites enable PFE.
-->
これは、例えば DHE と ECDHE のみ PFE を有効にしたい場合などに便利です。

<!--
## Recommendation: increase the DHE key size
-->
## 推奨: DHE 鍵長の伸張

<!--
Diffie Hellman has been in the news recently because it offers perfect forward secrecy.  However, in 1.6 and 1.7, the server handshake of DHE is set to 1024 at most, which is considered weak and can be compromised by attackers.
-->
ディフィー・ヘルマンは最近、PFS を提供することで話題になりました。しかし、1.6 と 1.7 では DHE のサーバハンドシェイクは最大 1024 に設定されているため、脆弱と見なされており、攻撃者により侵害される可能性があります。

<!--
If you have JDK 1.8, setting the system property `-Djdk.tls.ephemeralDHKeySize=2048` is recommended to ensure stronger keysize in the handshake.  Please see [Customizing Size of Ephemeral Diffie-Hellman Keys](http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#customizing_dh_keys).
-->
JDK 1.8 の場合、`-Djdk.tls.ephemeralDHKeySize=2048` システムプロパティを設定して、ハンドシェイクの鍵長をより強固にすることが推奨されています。[エフェメラルDiffie-Hellman鍵のサイズのカスタマイズ](http://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#customizing_dh_keys) を参照してください。

<!--
## Recommendation: Use Ciphers with Perfect Forward Secrecy
-->
## 推奨: PFS を伴う暗号の使用

<!--
As per the [Recommendations for Secure Use of TLS and DTLS](https://datatracker.ietf.org/doc/draft-ietf-uta-tls-bcp/), the following cipher suites are recommended:
-->
[セキュアな TLS および DTLS の利用のすすめ](https://datatracker.ietf.org/doc/draft-ietf-uta-tls-bcp/) の通り、以下の暗号スイートが推奨されています:

```
play.ws.ssl.enabledCiphers = [
  "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
  "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
  "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",
  "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
]
```

<!--
Some of these ciphers are only available in JDK 1.8.
-->
これらの暗号のうち、いくつかは JDK 1.8 でのみ利用できます。

<!--
## Disabling Weak Ciphers and Weak Key Sizes Globally
-->
## 弱い暗号と弱い鍵長のグローバルな無効化

<!--
The `jdk.tls.disabledAlgorithms` can be used to prevent weak ciphers, and can also be used to prevent [small key sizes](http://sim.ivi.co/2011/07/java-se-7-release-security-enhancements.html) from being used in a handshake.  This is a [useful feature](http://sim.ivi.co/2013/11/harness-ssl-and-jsse-key-size-control.html) that is only available in Oracle JDK 1.7 and later.
-->
`jdk.tls.disabledAlgorithms` は、弱い暗号およびハンドシェイクに使われる [短い鍵長](http://sim.ivi.co/2011/07/java-se-7-release-security-enhancements.html) を防止するために使うことができます。これは、Oracle JDK 1.7 以降でのみ利用できる [便利な機能](http://sim.ivi.co/2013/11/harness-ssl-and-jsse-key-size-control.html) です。

<!--
The official documentation for disabled algorithms is in the [JSSE Reference Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#DisabledAlgorithms).
-->
無効化済アルゴリズムに関する公式なドキュメントは [JSSEリファレンス・ガイド](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#DisabledAlgorithms) にあります。

<!--
For TLS, the code will match the first part of the cipher suite after the protocol, i.e. TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 has ECDHE as the relevant cipher.  The parameter names to use for the disabled algorithms are not obvious, but are listed in the [Providers documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html) and can be seen in the [source code](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8-b132/sun/security/ssl/SSLAlgorithmConstraints.java/#271).
-->
TLS の場合、コードは暗号スイートのはじめの部分のうち、プロトコルの後ろの部分、例えば TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 の場合は ECDHE を関連する暗号としてマッチします。無効化アルゴリズムに使われる引数名は明白ではありませんが、[プロバイダドキュメント](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/SunProviders.html) に一覧があり、[ソースコード](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8-b132/sun/security/ssl/SSLAlgorithmConstraints.java/#271) を確認することができます。

<!--
To enable `jdk.tls.disabledAlgorithms` or `jdk.certpath.disabledAlgorithms` (which looks at signature algorithms and weak keys in X.509 certificates) you must create a properties file:
-->
(X.509 証明書内の署名アルゴリズムと弱い鍵を検査する) `jdk.tls.disabledAlgorithms` または `jdk.certpath.disabledAlgorithms` を有効にするには、プロパティファイルを作らなければなりません:

```
# disabledAlgorithms.properties
jdk.tls.disabledAlgorithms=EC keySize < 160, RSA keySize < 2048, DSA keySize < 2048
jdk.certpath.disabledAlgorithms=MD2, MD4, MD5, EC keySize < 160, RSA keySize < 2048, DSA keySize < 2048
```

<!--
And then start up the JVM with [java.security.properties](http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7133344):
-->
その後、JVM を [java.security.properties](http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7133344) を付けて起動します:

```
java -Djava.security.properties=disabledAlgorithms.properties
```

<!--
## Debugging
-->
## デバッグ

<!--
To debug ciphers and weak keys, turn on the following debug settings:
-->
暗号と弱い鍵をデバッグする場合、以下のデバッグ設定を有効にしてください:

```
play.ws.ssl.debug = {
 ssl = true
 handshake = true
 verbose = true
 data = true
}
```

<!--
> **Next**: [[Configuring Certificate Validation|CertificateValidation]]
-->