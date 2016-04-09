<!--
# Quick Start to WS SSL
-->
# WS SSL クイックスタート

<!--
This section is for people who need to connect to a remote web service over HTTPS, and don't want to read through the entire manual.  If you need to set up a web service or configure client authentication, please proceed to the [[next section|CertificateGeneration]].
-->
この章は、HTTPS を使ってリモートの web サービスに接続する必要があるものの、手順書をすべて読み通したくはない人に向けたものです。wewb サービスをセットアップしたり、クライアント認証を設定する必要がある場合は、[[次の章|CertificateGeneration]] に進んでください。

<!--
## Connecting to a Remote Server over HTTPS
-->
## HTTPS によるリモートサーバへの接続

<!--
If the remote server is using a certificate that is signed by a well known certificate authority, then WS should work out of the box without any additional configuration.  You're done!
-->
リモートサーバがよく知られた認証局によって署名された証明書を使っている場合、WS は追加の設定をすることなく、そのままの状態で動作します。やったね!

<!--
If the web service is not using a well known certificate authority, then it is using either a private CA or a self-signed certificate.  You can determine this easily by using curl:
-->
もし web サービスがよく知られた認証局を使っていない場合、プライベート CA か、または自己署名証明書のいずれかを使っています。これは curl を使って簡単に確かめることができます:

```
curl https://financialcryptography.com # uses cacert.org as a CA
```

<!--
If you receive the following error:
-->
以下のようなエラーが発生した場合は:

```
curl: (60) SSL certificate problem: Invalid certificate chain
More details here: http://curl.haxx.se/docs/sslcerts.html

curl performs SSL certificate verification by default, using a "bundle"
 of Certificate Authority (CA) public keys (CA certs). If the default
 bundle file isn't adequate, you can specify an alternate file
 using the --cacert option.
If this HTTPS server uses a certificate signed by a CA represented in
 the bundle, the certificate verification probably failed due to a
 problem with the certificate (it might be expired, or the name might
 not match the domain name in the URL).
If you'd like to turn off curl's verification of the certificate, use
 the -k (or --insecure) option.
```

<!--
Then you have to obtain the CA's certificate, and add it to the trust store.
-->
この CA の証明書を取得し、トラストストアに追加する必要があります。

<!--
## Obtain the Root CA Certificate
-->
## ルート CA 証明書の取得

<!--
Ideally this should be done out of band: the owner of the web service should provide you with the root CA certificate directly, in a way that can't be faked, preferably in person.
-->
理想的には、これは帯域外で行われるべきです: web サービスの所有者は、ルート CA 証明書を偽装できない方法で、できれば人が直接提供すべきです。

<!--
In the case where there is no communication (and this is **not recommended**), you can sometimes get the root CA certificate directly from the certificate chain, using [`keytool from JDK 1.8`](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html):
-->
コミュニケーション手段がない場合 (これは **非推奨** です)、[`JDK 1.8 のキーツール`](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html) を使って、証明書チェーンからルート CA 証明書を取得できることがあります:

```
keytool -printcert -sslserver playframework.com
```

<!--
which returns #2 as the root certificate:
-->
これはルート証明書として #2 を返します:

```
Certificate #2
====================================
Owner: CN=GlobalSign Root CA, OU=Root CA, O=GlobalSign nv-sa, C=BE
Issuer: CN=GlobalSign Root CA, OU=Root CA, O=GlobalSign nv-sa, C=BE
```

<!--
To get the certificate chain in an exportable format, use the -rfc option:
-->
エクスポートできるフォーマットとして証明書チェーンを取得するには、-rfc オプションを使います:

```
keytool -printcert -sslserver playframework.com -rfc
```

<!--
which will return a series of certificates in PEM format:
-->
これは一連の証明書を PEM フォーマットで返すでしょう:

```
-----BEGIN CERTIFICATE-----
...
-----END CERTIFICATE-----
```

<!--
which can be copied and pasted into a file.  The very last certificate in the chain will be the root CA certificate.
-->
これはコピーしてファイルに貼り付けることができます。チェーンの最後の最後にある証明書がルート CA 証明書のはずです。

<!--
> **NOTE**: Not all websites will include the root CA certificate.  You should decode the certificate with keytool or with [certificate decoder](https://www.sslshopper.com/certificate-decoder.html) to ensure you have the right certificate.
-->
> **注意**: すべての web サイトがルート CA 証明書を含むわけではありません。キーツールか [certificate decoder](https://www.sslshopper.com/certificate-decoder.html) を使って証明書をデコードし、正しい証明書であることを確認すべきです。

<!--
## Point the trust manager at the PEM file
-->
## トラストマネージャを PEM ファイルに向ける

<!--
Add the following into `conf/application.conf`, specifying `PEM` format specifically:
-->
`PEM` フォーマットであることをはっきりと指定しながら、以下の内容を `conf/application.conf` に追加します:

```
ws.ssl {
  trustManager = {
    stores = [
      { type = "PEM", path = "/path/to/cert/globalsign.crt" }
    ]
  }
}
```

<!--
This will tell the trust manager to ignore the default `cacerts` store of certificates, and only use your custom CA certificate.
-->
これは、トラストマネージャに証明書のデフォルト `cacerts` ストアを無視し、カスタムの CA 証明書のみを使うよう指示します。

<!--
After that, WS will be configured, and you can test that your connection works with:
-->
これで WS が設定されたので、次のようにして接続が動作していることを確認することができます:

```
WS.url("https://example.com").get()
```

<!--
You can see more examples on the [[example configurations|ExampleSSLConfig]] page.
-->
[[example configurations|ExampleSSLConfig]] ページでより多くの例を見ることができます。
