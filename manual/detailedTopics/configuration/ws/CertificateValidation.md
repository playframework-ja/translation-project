 <!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
 <!--
# Configuring Certificate Validation
-->
# 証明書検証の設定

<!--
In an SSL connection, the identity of the remote server is verified using an X.509 certificate which has been signed by a certificate authority.
-->
SSL 接続において、リモートサーバのアイデンティティは、認証局によって署名された X.509 証明書を使って検証されます。

<!--
The JSSE implementation of X.509 certificates is defined in the [PKI Programmer's Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html).
-->
JSSE の X.509 証明書実装は [PKIプログラマーズ・ガイド](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html) に定義されています。

<!--
Some X.509 certificates that are used by servers are old, and are using signatures that can be forged by an attacker.  Because of this, it may not be possible to verify the identity of the server if that signature algorithm is being used.  Fortunately, this is rare -- over 95% of trusted leaf certificates and 95% of trusted signing certificates use [NIST recommended key sizes](http://csrc.nist.gov/publications/nistpubs/800-131A/sp800-131A.pdf).
-->
いくつかのサーバで使われている X.509 証明書は古く、攻撃者に侵害される可能性のある署名を使っています。このため、これらの署名アルゴリズムが使われているサーバのアイデンティティは検証できない可能性があります。幸い、これは稀です -- 95% 以上の信頼されたリーフ証明書と署名証明書が [NIST 推奨の鍵長](http://csrc.nist.gov/publications/nistpubs/800-131A/sp800-131A.pdf) を使っています。

<!--
WS automatically disables weak signature algorithms and weak keys for you, according to the [current standards](http://sim.ivi.co/2012/04/nist-security-strength-time-frames.html).
-->
WS は [現在の標準](http://sim.ivi.co/2012/04/nist-security-strength-time-frames.html) に従って、弱い署名アルゴリズムと弱い鍵を自動的に利用できないようにします。

<!--
This feature is similar to [jdk.certpath.disabledAlgorithms](http://sim.ivi.co/2013/11/harness-ssl-and-jsse-key-size-control.html), but is specific to the WS client and can be set dynamically, whereas jdk.certpath.disabledAlgorithms is global across the JVM, must be set via a security property, and is only available in JDK 1.7 and later.
-->
この機能は [jdk.certpath.disabledAlgorithms](http://sim.ivi.co/2013/11/harness-ssl-and-jsse-key-size-control.html) に似ていますが、jdk.certpath.disabledAlgorithms は JVM をまたがってグローバルで、セキュリティプロパティによって設定されなければならず、かつ JDK 1.7 以降でのみ利用できる一方、WS クライアントに特化されており、動的に設定することができます。

<!--
You can override this to your tastes, but it is recommended to be at least as strict as the defaults.  The appropriate signature names can be looked up in the [Providers Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html).
-->
これはお好みで上書きできますが、最低限デフォルトと同じくらい厳格であることが推奨されています。適切な署名は [プロバイダに関するドキュメント](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/SunProviders.html) で探すことができます。

<!--
## Disabling Certificates with Weak Signature Algorithms
-->
## 弱い署名アルゴリズムによる証明書の無効化

<!--
The default list of disabled signature algorithms is defined below:
-->
無効化された署名アルゴリズムの一覧は以下に定義されています:

```
play.ws.ssl.disabledSignatureAlgorithms = "MD2, MD4, MD5"
```

<!--
MD5 is disabled, based on the proven [collision attack](https://www.win.tue.nl/hashclash/rogue-ca/) and the Mozilla recommendations:
-->
MD5 は [衝突攻撃](https://www.win.tue.nl/hashclash/rogue-ca/) の証明と Mozilla の推奨に基づいて無効化されています:

<!--
> MD5 certificates may be compromised when attackers can create a fake cert that hashes to the same value as one with a legitimate signature, and is hence trusted. Mozilla can mitigate this potential vulnerability by turning off support for MD5-based signatures. The MD5 root certificates don't necessarily need to be removed from NSS, because the signatures of root certificates are not validated (roots are self-signed). Disabling MD5 will impact intermediate and end entity certificates, where the signatures are validated.
>
> The relevant CAs have confirmed that they stopped issuing MD5 certificates. However, there are still many end entity certificates that would be impacted if support for MD5-based signatures was turned off in 2010. Therefore, we are hoping to give the affected CAs time to react, and are proposing the date of June 30, 2011 for turning off support for MD5-based signatures. The relevant CAs are aware that Mozilla will turn off MD5 support earlier if needed.
-->
> MD5 証明書は、正当なシグネチャを持ち、かつ信頼されている証明書と同じ値のハッシュを持つ偽の証明書を攻撃者が生成できた場合、侵害される可能性があります。Mozilla は MD5 ベース の署名をサポートしないことで、この潜在的な脆弱性を軽減することができます。 MD5 ルート証明書の署名は (自己署名されており) 検証されないため、NSS から削除する必要はありません。MD5 の無効化は、署名が検証される中間および末端の証明書に影響を与えます。
> 
> 関連のある認証局は MD5 証明書の発行を停止することを発表しました。しかしながら、MD5 ベースの署名が 2010 年にサポートされなくなると影響を受ける末端の証明書は未だ多く存在します。このため、認証局には反映までの時間を希望し、かつ MD5 ベース署名のサポートを 2011 年 6 月 30 日に打ち切るよう提案しています。関連する認証局は、Mozilla が必要に応じて MD5 のサポートをより早く打ち切ることを認識しています。

<!--
SHA-1 is considered weak, and new certificates should use digest algorithms from the [SHA-2 family](https://en.wikipedia.org/wiki/SHA-2).  However, old certificates should still be considered valid.
-->
SHA-1 は脆弱であると見なされており、新しい証明書は [SHA-2 ファミリ](https://en.wikipedia.org/wiki/SHA-2) にあるダイジェストアルゴリズムを使うべきです。とは言え、古い証明書も引き続き妥当であると見なされています。

<!--
## Disabling Certificates With Weak Key Sizes
-->
## 弱い鍵長による証明書の無効化

<!--
WS defines the default list of weak key sizes as follows:
-->
WS は弱い鍵長のデフォルトリストを以下のように定めています:

```
play.ws.ssl.disabledKeyAlgorithms = "DHE keySize < 2048, ECDH keySize < 2048, ECDHE keySize < 2048, RSA keySize < 2048, DSA keySize < 2048, EC keySize < 224"
```

<!--
These settings are based in part on [keylength.com](https://keylength.com), and in part on the Mozilla recommendations:
-->
これらの設定は [keylength.com](https://keylength.com) と Mozilla 推奨の一部に基づいています:

<!--
> The NIST recommendation is to discontinue 1024-bit RSA certificates by December 31, 2010. Therefore, CAs have been advised that they should not sign any more certificates under their 1024-bit roots by the end of this year.
>
> The date for disabling/removing 1024-bit root certificates will be dependent on the state of the art in public key cryptography, but under no circumstances should any party expect continued support for this modulus size past December 31, 2013. As mentioned above, this date could get moved up substantially if new attacks are discovered. We recommend all parties involved in secure transactions on the web move away from 1024-bit moduli as soon as possible.
-->
> NIST は 2010 年 12 月 31 日までに 1024-bit RSA 証明書を中止するよう勧告しています。そのため、認証局はこの年末までどのような証明書にも 1024 ビットで署名すべきではないと忠告されています。
> 
> 1024 ビットのルート証明書を削除/無効にするための日付は公開鍵暗号方式における最先端の技術に依存することになりますが、どのような場合でも、あらゆる組織は 2013 年 12 月 31 日以降、このモジュールサイズの継続的なサポートを期待すべきではありません。上述したとおり、新しい攻撃が発見された場合、この日付は実質的に早まることがあります。web 上のセキュアなトランザクションに関与するすべての組織が、できるだけ早く 1024 ビットのモジュールから離れることをお勧めします。

<!--
**NOTE:** because weak key sizes also apply to root certificates (which is not included in the certificate chain available to the PKIX certpath checker included in JSSE), setting this option will check the accepted issuers in any configured trustmanagers and keymanagers, including the default.
-->
**注意:** 弱い鍵長は (JSSE に含まれる PKIX 証明書パスチェッカ で利用できる証明書チェーンには含まれない) ルート証明書にも適用されるので、このオプションを設定すると、受け取った発行者がデフォルトを含め、設定されたトラストマネージャおよびキーマネージャに含まれているか確認します。

<!--
Over 95% of trusted leaf certificates and 95% of trusted signing certificates use [NIST recommended key sizes](http://csrc.nist.gov/publications/nistpubs/800-131A/sp800-131A.pdf), so this is considered a safe default.
-->
95% 以上の信頼されたリーフ証明書と署名証明書が [NIST 推奨の鍵長](http://csrc.nist.gov/publications/nistpubs/800-131A/sp800-131A.pdf) を使っているので、これは安全なデフォルトと見なすことができます。

<!--
## Disabling Weak Certificates Globally
-->
## 弱い証明書のグローバルな無効化

<!--
To disable signature algorithms and weak key sizes globally across the JVM, use the `jdk.certpath.disabledAlgorithms` [security property](http://sim.ivi.co/2011/07/java-se-7-release-security-enhancements.html).  Setting security properties is covered in more depth in [[Configuring Cipher Suites|CipherSuites]] section.
-->
JVM を超えてグローバルに署名アルゴリズムと鍵長を無効化する場合、`jdk.certpath.disabledAlgorithms` [セキュリティプロパティ](http://sim.ivi.co/2011/07/java-se-7-release-security-enhancements.html) を使ってください。セキュリティプロパティは [[暗号スイートの設定|CipherSuites]] 章でより詳しくカバーされています。

<!--
> **NOTE** if configured, the `jdk.certpath.disabledAlgorithms` property should contain the settings from both `disabledKeyAlgorithms` and `disabledSignatureAlgorithms`.
-->
> **注意** jdk.certpath.disabledAlgorithms` プロパティを設定する場合、disabledKeyAlgorithms` および `disabledSignatureAlgorithms` 両方の設定を含んでいなければなりません。

<!--
## Debugging Certificate Validation
-->
## 証明書検証のデバッグ

<!--
To see more details on certificate validation, set the following debug configuration:
-->
証明書検証をより詳しく確認する場合、以下のデバッグ設定をセットしてください:

```
play.ws.ssl.debug.certpath = true
```

<!--
The undocumented setting `-Djava.security.debug=x509` may also be helpful.
-->
ドキュメントにない `-Djava.security.debug=x509` 設定も同様に便利かもしれません。

<!--
## Further Reading
-->
## 参考文献

<!--
* [Dates for Phasing out MD5-based signatures and 1024-bit moduli](https://wiki.mozilla.org/CA:MD5and1024)
* [Fixing X.509 Certificates](https://tersesystems.com/2014/03/20/fixing-x509-certificates/)
-->
* [Dates for Phasing out MD5-based signatures and 1024-bit moduli](https://wiki.mozilla.org/CA:MD5and1024)
* [Fixing X.509 Certificates](https://tersesystems.com/2014/03/20/fixing-x509-certificates/)
