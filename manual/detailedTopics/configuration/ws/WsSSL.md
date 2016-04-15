<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring WS SSL
-->
# WS SSL の設定

<!--
[[Play WS|ScalaWS]] allows you to set up HTTPS completely from a configuration file, without the need to write code.  It does this by layering the Java Secure Socket Extension (JSSE) with a configuration layer and with reasonable defaults.
-->
[[Play WS|ScalaWS]] では、設定ファイルで HTTPS を完璧にセットアップすることができるので、コードを書く必要はありません。これは、Java Secure Socket Extension (JSSE) を設定レイヤと妥当なデフォルト値で階層化することで実現しています。

<!--
JDK 1.8 contains an implementation of JSSE which is [significantly more advanced](https://docs.oracle.com/javase/8/docs/technotes/guides/security/enhancements-8.html) than previous versions, and should be used if security is a priority.
-->
JDK 1.8 には、以前のバージョンよりも [かなり進化した](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/enhancements-8.html) JSSE 実装が含まれており、セキュリティを重要視する場合は使用されるべきです。

<!--
## Table of Contents
-->
## 目次

<!--
- [[Quick Start to WS SSL|WSQuickStart]]
- [[Generating X.509 Certificates|CertificateGeneration]]
- [[Configuring Trust Stores and Key Stores|KeyStores]]
- [[Configuring Protocols|Protocols]]
- [[Configuring Cipher Suites|CipherSuites]]
- [[Configuring Certificate Validation|CertificateValidation]]
- [[Configuring Certificate Revocation|CertificateRevocation]]
- [[Configuring Hostname Verification|HostnameVerification]]
- [[Example Configurations|ExampleSSLConfig]]
- [[Using the Default SSLContext|DefaultContext]]
- [[Debugging SSL Connections|DebuggingSSL]]
- [[Loose Options|LooseSSL]]
- [[Testing SSL|TestingSSL]]
-->
- [[WS SSL クイックスタート|WSQuickStart]]
- [[X.509 証明書の生成|CertificateGeneration]]
- [[トラストストアとキーストアの設定|KeyStores]]
- [[プロトコルの設定|Protocols]]
- [[暗号スイートの設定|CipherSuites]]
- [[Certificate Validation の設定|CertificateValidation]]
- [[証明書失効の設定|CertificateRevocation]]
- [[ホスト名検証の設定|HostnameVerification]]
- [[設定の例|ExampleSSLConfig]]
- [[デフォルトの SSLContext の使用|DefaultContext]]
- [[SSL 接続のデバッグ|DebuggingSSL]]
- [[緩いオプション|LooseSSL]]
- [[SSL のテスト|TestingSSL]]

<!--
## Further Reading
-->
## 参考文献

<!--
JSSE is a complex product.  For convenience, the JSSE materials are provided here:
-->
JSSE は複雑な製品です。便宜のため、以下に JSSE 資材が提供されています:

<!--
JDK 1.8:
-->
JDK 1.8:

<!--
* [JSSE Reference Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html)
* [JSSE Crypto Spec](https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#SSLTLS)
* [SunJSSE Providers](https://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html#SunJSSEProvider)
* [PKI Programmer's Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html)
* [keytool](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
-->
* [JSSE リファレンスガイド](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html)
* [JSSE 暗号化仕様](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/crypto/CryptoSpec.html#SSLTLS)
* [SunJSSE プロバイダ](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/SunProviders.html#SunJSSEProvider)
* [PKI プログラマーズガイド](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html)
* [keytool](https://docs.oracle.com/javase/jp/8/docs/technotes/tools/unix/keytool.html)
