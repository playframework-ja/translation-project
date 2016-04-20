<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Example Configurations
-->
# 設定例

<!--
TLS can be very confusing.  Here are some settings that can help.
-->
TLS は混乱しがちです。ここでは手助けとなる設定例をいくつか紹介します。

<!--
## Connecting to an internal web service
-->
## 内部 web サービスへの接続

<!--
If you are using WS to communicate with a single internal web service which is configured with an up to date TLS implementation, then you have no need to use an external CA.  Internal certificates will work fine, and are arguably [more secure](http://www.thoughtcrime.org/blog/authenticity-is-broken-in-ssl-but-your-app-ha/) than the CA system.
-->
最新の TLS 実装で設定された単一の内部 web サービスに WS を使って通信する場合、外部 CA を使う必要はありません。内部証明書は正常に動作し、間違いなく CA システム [より安全](http://www.thoughtcrime.org/blog/authenticity-is-broken-in-ssl-but-your-app-ha/) です。

<!--
Generate a self signed certificate from the [[generating certificates|CertificateGeneration]] section, and tell the client to trust the CA's public certificate.
-->
[[証明書の生成|CertificateGeneration]] の章を読んで自己署名証明書を生成し、クライアントに CA の公開証明書を信頼するよう伝えてください。

```
play.ws.ssl {
  trustManager = {
    stores = [
      { type = "JKS", path = "exampletrust.jks" }
    ]
  }
}
```

<!--
## Connecting to an internal web service with client authentication
-->
## クライアント認証を伴う内部 web サービスへの接続

<!--
If you are using client authentication, then you need to include a keyStore to the key manager that contains a PrivateKeyEntry, which consists of a private key and the X.509 certificate containing the corresponding public key.  See the "Configure Client Authentication" section in [[generating certificates|CertificateGeneration]].
-->
クライアント認証を使う場合、秘密鍵と、対応する公開鍵を含む X.509 証明書から構成された PrivateKeyEntry を含むキーマネージャに、キーストアを含める必要があります。[[証明書の生成|CertificateGeneration]] の章の "クライアント認証の設定" を参照してください。

```
play.ws.ssl {
  keyManager = {
    stores = [
      { type = "JKS", path = "client.jks", password = "changeit1" }
    ]
  }
  trustManager = {
    stores = [
      { type = "JKS", path = "exampletrust.jks" }
    ]
  }
}
```

<!--
## Connecting to several external web services
-->
## 複数の外部 web サービスへの接続

<!--
If you are communicating with several external web services, then you may find it more convenient to configure one client with several stores:
-->
複数の web サービスと通信する場合、ひとつのクライアントに複数のストアを設定するとより便利かもしれません:

```
play.ws.ssl {
  trustManager = {
    stores = [
      { type = "PEM", path = "service1.pem" }
      { path = "service2.jks" }
      { path = "service3.jks" }
    ]
  }
}
```

<!--
If client authentication is required, then you can also set up the key manager with several stores:
-->
クライアント認証が必要な場合は、複数のストアにキーマネージャを設定します:

```
play.ws.ssl {
    keyManager = {
    stores = [
      { type: "PKCS12", path: "keys/service1-client.p12", password: "changeit1" },
      { type: "PKCS12", path: "keys/service2-client.p12", password: "changeit2" },
      { type: "PKCS12", path: "keys/service3-client.p12", password: "changeit3" }
    ]
  }
}
```

<!--
## Both Private and Public Servers
-->
## 内部および外部サーバ

<!--
If you are using WS to access both private and public servers on the same profile, then you will want to include the default JSSE trust store as well:
-->
同じプロファイルの WS を使って内部と外部の両方のサーバにアクセスする場合、同様に デフォルトの JSSE トラストストアを含めることになるでしょう:

```
play.ws.ssl {
  trustManager = {
    stores = [
      { path: exampletrust.jks }     # Added trust store
      { path: ${java.home}/lib/security/cacerts } # Fallback to default JSSE trust store
    ]
  }
}
```


