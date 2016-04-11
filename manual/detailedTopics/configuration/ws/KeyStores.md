<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring Trust Stores and Key Stores
-->
# トラストストアとキーストアの設定

<!--
Trust stores and key stores contain X.509 certificates.  Those certificates contain public (or private) keys, and are organized and managed under either a TrustManager or a KeyManager, respectively.
-->
トラストストアとキーストアは X.509 証明書を含んでいます。これらの証明書は公開 (または秘密) 鍵を含んでおり、TrustManager または KeyManager の配下でそれぞれ組織され、管理されています。

<!--
If you need to generate X.509 certificates, please see [[Certificate Generation|CertificateGeneration]] for more information.
-->
X.509 証明書を生成する必要がある場合、より詳細は [[証明書の生成|CertificateGeneration]] を参照してください。

<!--
## Configuring a Trust Manager
-->
## トラストマネージャの設定

<!--
A [trust manager](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#TrustManager) is used to keep trust anchors: these are root certificates which have been issued by certificate authorities.   It determines whether the remote authentication credentials (and thus the connection) should be trusted.  As an HTTPS client, the vast majority of requests will use only a trust manager.  
-->
[トラストマネージャ](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#TrustManager) は、認証局より発行されたルート証明書であるトラストアンカーを維持するために使われます: トラストマネージャは、リモート認証クリデンシャル (ひいては、その接続) を信頼すべきかどうかを決定します。HTTPS クライアントの場合、リクエストの大半はトラストマネージャを使います。

If you do not configure it at all, WS uses the default trust manager, which points to the `cacerts` key store in `${java.home}/lib/security/cacerts`.  If you configure a trust manager explicitly, it will override the default settings and the `cacerts` store will not be included.
まったく設定していない場合、WS は `cacerts` キーストアを `${java.home}/lib/security/cacerts` 内で指し示すデフォルトのトラストマネージャを使います。トラストマネージャを明示的に指定した場合、デフォルトの設定は上書きされ、`cacerts` ストアは取り込まれません。

If you wish to use the default trust store and add another store containing certificates, you can define multiple stores in your trust manager.  The [CompositeX509TrustManager](api/scala/play/api/libs/ws/ssl/CompositeX509TrustManager.html) will try each in order until it finds a match:
デフォルトのトラストストアを使いつつ、証明書を含む別のストアを追加したい場合、トラストマネージャに複数のストアを定義することができます。[CompositeX509TrustManager](api/scala/play/api/libs/ws/ssl/CompositeX509TrustManager.html) は、マッチするストアが見つかるまで、それぞれのストアを順番に試します。

```
play.ws.ssl {
  trustManager = {
      stores = [
        { path: ${store.directory}/truststore.jks, type: "JKS" }  # Added trust store
        { path: ${java.home}/lib/security/cacerts, password = "changeit" } # Default trust store
      ]
  }
}
```


> **NOTE**: Trust stores should only contain CA certificates with public keys, usually JKS or PEM.  PKCS12 format is supported, but PKCS12 should not contain private keys in a trust store, as noted in the [reference guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#SunJSSE).
> **注意**: トラストストアは CA 証明書と公開鍵だけを、通常は JKS または PEM にて含むべきです。PKCS12 フォーマットもサポートされていますが、PKCS12 は [reference guide](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#SunJSSE) に記されている通り、トラストストアに秘密鍵を保持すべきではありません。

<!--
## Configuring a Key Manager
-->
## キーマネージャの設定

A [key manager](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#KeyManager) is used to present keys for a remote host.  Key managers are typically only used for client authentication (also known as mutual TLS).
[キーマネージャ](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#KeyManager) リモートホストの鍵を提示するために使われます。キーマネージャは一般的に (相互 TLS として知られている) クライアント認証のためだけに使われます。

The [CompositeX509KeyManager](api/scala/play/api/libs/ws/ssl/CompositeX509KeyManager.html) may contain multiple stores in the same manner as the trust manager.

```
play.ws.ssl {
    keyManager = {
      stores = [
        {
          type: "pkcs12",
          path: "keystore.p12",
          password: "password1"
        },
      ]
    }
}
```

> **NOTE**: A key store that holds private keys should use PKCS12 format, as indicated in the [reference guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#SunJSSE).

<!--
## Configuring a Store
-->
## ストアの設定

A store corresponds to a [KeyStore](https://docs.oracle.com/javase/8/docs/api/java/security/KeyStore.html) object, which is used for both trust stores and key stores.  Stores may have a [type](https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyStore) -- `PKCS12`, [`JKS`](https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#KeystoreImplementation) or `PEM` (aka Base64 encoded DER certificate) -- and may have an associated password.

Stores must have either a `path` or a `data` attribute.  The `path` attribute must be a file system path.

```
{ type: "PKCS12", path: "/private/keystore.p12" }
```

The `data` attribute must contain a string of PEM encoded certificates.

```
{
  type: "PEM", data = """
-----BEGIN CERTIFICATE-----
...certificate data
-----END CERTIFICATE-----
-----BEGIN CERTIFICATE-----
...certificate data
-----END CERTIFICATE-----
-----BEGIN CERTIFICATE-----
...certificate data
-----END CERTIFICATE-----
"""
}
```

<!--
## Debugging
-->
## デバッグ

To debug the key manager / trust manager, set the following flags:

```
play.ws.ssl.debug = {
  ssl = true
  trustmanager = true
  keymanager = true
}
```

<!--
## Further Reading
-->
## 併せて読みたい

In most cases, you will not need to do extensive configuration once the certificates are installed.  If you are having difficulty with configuration, the following blog posts may be useful:

* [Key Management](https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#KeyManagement)
* [Java 2-way TLS/SSL (Client Certificates) and PKCS12 vs JKS KeyStores](http://blog.palominolabs.com/2011/10/18/java-2-way-tlsssl-client-certificates-and-pkcs12-vs-jks-keystores/)
* [HTTPS with Client Certificates on Android](http://chariotsolutions.com/blog/post/https-with-client-certificates-on/)
