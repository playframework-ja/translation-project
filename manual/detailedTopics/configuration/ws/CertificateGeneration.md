<!--
# Generating X.509 Certificates
-->
# X.509 証明書の生成

<!--
## X.509 Certificates
-->
## X.509 証明書

<!--
Public key certificates are a solution to the problem of identity.  Encryption alone is enough to set up a secure connection, but there's no guarantee that you are talking to the server that you think you are talking to.  Without some means to verify the identity of a remote server, an attacker could still present itself as the remote server and then forward the secure connection onto the remote server.  Public key certificates solve this problem.
-->
公開鍵証明書は本人性に関する問題を解決します。暗号化だけでもセキュアな接続をセットアップするには十分ですが、会話しているサーバが、あなたが会話していると思っているサーバである保証はありません。リモートサーバの本人性を検証する手段がなければ、攻撃者は依然としてリモートサーバとして居座り続けて、セキュアな接続をリモートサーバにフォワードすることができます。

<!--
The best way to think about public key certificates is as a passport system. Certificates are used to establish information about the bearer of that information in a way that is difficult to forge. This is why certificate verification is so important: accepting **any** certificate means that even an attacker's certificate will be blindly accepted.
-->
公開鍵証明書はパスポートシステムとして考えるのがもっとも良い方法です。証明書は、偽造することが困難な方法で情報の運搬に関する情報を確立するために使われます。これが、証明書の検証がとても重要である理由です: **すべての** 証明書を受け入れることは、攻撃者の証明書さえ盲目的に受け入れてしまうことを意味します。

<!--
## Using Keytool
-->
## キーツールを使う

<!--
keytool comes in several versions:
-->
キーツールにはいくつかのバージョンがあります:

<!--
* [1.8](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
* [1.7](http://docs.oracle.com/javase/7/docs/technotes/tools/solaris/keytool.html)
* [1.6](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html)
-->
* [1.8](http://docs.oracle.com/javase/jp/8/docs/technotes/tools/unix/keytool.html)
* [1.7](http://docs.oracle.com/javase/jp/7/docs/technotes/tools/solaris/keytool.html)
* [1.6](http://docs.oracle.com/javase/jp/6/docs/technotes/tools/solaris/keytool.html)

<!--
The examples below use keytool 1.7, as 1.6 does not support the minimum required certificate extensions needed for marking a certificate for CA usage or for a hostname.
-->
キーツール 1.6 は、証明書を CA 用途またはホスト名用として印付けるために必要な認証拡張の最小要件をサポートしていないので、以下の例では 1.7 を使います。

<!--
## Generating a random password
-->
## ランダムなパスワードの生成

<!--
Create a random password using pwgen (`brew install pwgen` if you're on a Mac):
-->
pwgem を使ってランダムなパスワードを作ります (Mac の場合は `brew install pwgen`):

@[context](code/genpassword.sh)

<!--
## Server Configuration
-->
## サーバの設定

<!--
You will need a server with a DNS hostname assigned, for hostname verification.  In this example, we assume the hostname is `example.com`.
-->
ホスト名検証のために、ホスト名が登録された DNS サーバが必要になります。この例では、ホスト名を `example.com` と仮定します。

<!--
### Generating a server CA
-->
### サーバ証明書の生成

<!--
The first step is to create a certificate authority that will sign the example.com certificate.  The root CA certificate has a couple of additional attributes (ca:true, keyCertSign) that mark it explicitly as a CA certificate, and will be kept in a trust store.
-->
はじめの一歩は、example.com 証明書に署名する認証局を作成することです。ルート CA 証明書は、それが CA 証明書であることを示すいくつかの追加属性 (ca:true や keyCertSign) を持っており、トラストストアに保持されます。

@[context](code/genca.sh)

<!--
### Generating example.com certificates
-->
### example.com 証明書の生成

<!--
The example.com certificate is presented by the `example.com` server in the handshake.
-->
example.com 証明書は、以下のハンドシェイクで `example.com` サーバより提示されます。

@[context](code/genserver.sh)

<!--
You should see:
-->
以下のように表示されるはずです:

```
Alias name: example.com
Creation date: ...
Entry type: PrivateKeyEntry
Certificate chain length: 2
Certificate[1]:
Owner: CN=example.com, OU=Example Org, O=Example Company, L=San Francisco, ST=California, C=US
Issuer: CN=exampleCA, OU=Example Org, O=Example Company, L=San Francisco, ST=California, C=US
```

<!--
> **NOTE**: Also see the [[Configuring HTTPS|ConfiguringHttps]] section for more information.
-->
> **注意**: より詳しくは [[Configuring HTTPS|ConfiguringHttps]] の章を参照してください。

<!--
### Configuring example.com certificates in Nginx
-->
### example.com 証明書を Nginx に設定する

<!--
If example.com does not use Java as a TLS termination point, and you are using nginx, you may need to export the certificates in PEM format.
-->
example.com が TLS 終端に Java を使用しておらず、nginx を使っている場合は、証明書を PEM フォーマットで出力する必要があります。

<!--
Unfortunately, keytool does not export private key information, so openssl must be installed to pull private keys.
-->
残念ながらキーツールは秘密鍵の情報を出力しないので、これを引っ張り出すために openssl をインストールする必要があります。

@[context](code/genserverexp.sh)

<!--
Now that you have both `example.com.crt` (the public key certificate) and `example.com.key` (the private key), you can set up an HTTPS server.
-->
これで `example.com.crt` (公開鍵証明書) と `example.com.key` (秘密鍵) の両方が手に入ったので、HTTPS サーバをセットアップすることができます。

<!--
For example, to use the keys in nginx, you would set the following in `nginx.conf`:
-->
例えば、この鍵を nginx で使う場合は `nginx.conf` を以下のように設定します:

```
ssl_certificate      /etc/nginx/certs/example.com.crt;
ssl_certificate_key  /etc/nginx/certs/example.com.key;
```

<!--
If you are using client authentication (covered in **Client Configuration** below), you will also need to add:
-->
(次の **クライアントの設定** で触れる) クライアント認証を使う場合、以下も追加する必要があります:

```
ssl_client_certificate /etc/nginx/certs/clientca.crt;
ssl_verify_client on;
```

<!--
You can check the certificate is what you expect by checking the server:
-->
サーバを確認することで、期待している証明書であることを確認できます:

```
keytool -printcert -sslserver example.com
```

<!--
> **NOTE**: Also see the [[Setting up a front end HTTP server|HTTPServer]] section for more information.
-->
> **注意**: より詳しくは [[フロントエンド HTTP サーバの設定|HTTPServer]] の章を参照してください。

<!--
## Client Configuration
-->
## クライアントの設定

<!--
There are two parts to setting up a client -- configuring a trust store, and configuring client authentication.
-->
クライアントの設定は二箇所あります -- トラストストアの設定と、クライアント認証の設定です。

<!--
### Configuring a Trust Store
-->
### トラストストアの設定

<!--
Any clients need to see that the server's example.com certificate is trusted, but don't need to see the private key.  Generate a trust store which contains only the certificate and hand that out to clients.  Many java clients prefer to have the trust store in JKS format.
-->
どのようなクライアントでも、example.com サーバの証明書が信頼できることを確認する必要がありますが、秘密鍵を確認する必要はありません。この証明書だけを含み、そしてこれをクライアントに手渡すトラストストアを生成しましょう。多くの Java クライアントは JKS フォーマットのトラストストアを好んでいます。

@[context](code/gentruststore.sh)

<!--
You should see a `trustedCertEntry` for exampleca:
-->
exampleca の `trustedCertEntry` が確認できるはずです:

```
Alias name: exampleca
Creation date: ...
Entry type: trustedCertEntry

Owner: CN=exampleCA, OU=Example Org, O=Example Company, L=San Francisco, ST=California, C=US
Issuer: CN=exampleCA, OU=Example Org, O=Example Company, L=San Francisco, ST=California, C=US
```

<!--
The `exampletrust.jks` store will be used in the TrustManager.
-->
`exampletrust.jks` ストアはトラストマネージャで使われます。

```
ws.ssl {
  trustManager = {
    stores = [
      { path = "/Users/wsargent/work/ssltest/conf/exampletrust.jks" }
    ]
  }
}
```

<!--
> **NOTE**: Also see the [[Configuring Key Stores and Trust Stores|KeyStores]] section for more information.
-->
> **注意**: より詳しくは [[トラストストアとキーストアの設定|KeyStores]] の章を参照してください。

<!--
### Configure Client Authentication
-->
### クライアント認証の設定

<!--
Client authentication can be obscure and poorly documented, but it relies on the following steps:
-->
クライアント認証はドキュメントが貧弱で曖昧になりがちですが、以下の手順によって成り立っています。

<!--
1. The server asks for a client certificate, presenting a CA that it expects a client certificate to be signed with.  In this case, `CN=clientCA` (see the [debug example](http://docs.oracle.com/javase/7/docs/technotes/guides/security/jsse/ReadDebug.html)).
2. The client looks in the KeyManager for a certificate which is signed by `clientCA`, using `chooseClientAlias` and `certRequest.getAuthorities`.
3. The KeyManager will return the `client` certificate to the server.
4. The server will do an additional ClientKeyExchange in the handshake.
-->
1. サーバは、署名されたクライアント証明書を期待する CA を提示することで、クライアント証明書を要求します。この場合は、`CN=clientCA` ([デバッグ例](http://docs.oracle.com/javase/jp/7/docs/technotes/guides/security/jsse/ReadDebug.html) を参照してください) がこれに当たります。
2. クライアントは `chooseClientAlias` と `certRequest.getAuthorities` を使って、キーマネージャの中から `clientCA` によって署名された証明書を探します。
3. キーマネージャは `client` 証明書をサーバに返します。
4. サーバはこのハンドシェイクにおいて、さらに ClientKeyExchange を行います。

<!--
The steps to create a client CA and a signed client certificate are broadly similiar to the server certificate generation, but for convenience are presented in a single script:
-->
クライアント CA と署名したクライアント証明書を作成する手順は、サーバー証明書の生成と大部分で似ていますが、利便性のために単一のスクリプトを提供しています:

@[context](code/genclient.sh)

<!--
There should be one alias `client`, looking like the following:
-->
以下のような `client` というエイリアスがあるはずです:

```
Your keystore contains 1 entry

Alias name: client
Creation date: ...
Entry type: PrivateKeyEntry
Certificate chain length: 2
Certificate[1]:
Owner: CN=client, OU=Example Org, O=Example Company, L=San Francisco, ST=California, C=US
Issuer: CN=clientCA, OU=Example Org, O=Example Company, L=San Francisco, ST=California, C=US
```

<!--
And put `client.jks` in the key manager:
-->
そして、`client.jks` をキーマネージャに配置します:

```
ws.ssl {
  keyManager = {
    stores = [
      { type = "JKS", path = "conf/client.jks", password = $PW }
    ]
  }
}
```

<!--
> **NOTE**: Also see the [[Configuring Key Stores and Trust Stores|KeyStores]] section for more information.
-->
> **注意**: より詳しくは [[トラストストアとキーストアの設定|KeyStores]] の章を参照してください。

<!--
## Certificate Management Tools
-->
## 証明書管理ツール

<!--
If you want to examine certificates in a graphical tool than a command line tool, you can use [Keystore Explorer](http://keystore-explorer.sourceforge.net/) or [xca](http://sourceforge.net/projects/xca/).  [Keystore Explorer](http://keystore-explorer.sourceforge.net/) is especially convenient as it recognizes JKS format.  It works better as a manual installation, and requires some tweaking to the export policy.
-->
コマンドラインツールよりも、グラフィカルなツールで証明書を検査したい場合は [Keystore Explorer](http://keystore-explorer.sourceforge.net/) や [xca](http://sourceforge.net/projects/xca/) を使うことができます。[Keystore Explorer](http://keystore-explorer.sourceforge.net/) は JKS フォーマットを認識するので特に便利です。このツールは、手動インストールするとよく良く動作し、出力ポリシーにちょっとした調整が必要です。

<!--
If you want to use a command line tool with more flexibility than keytool, try [java-keyutil](https://code.google.com/p/java-keyutil/), which understands multi-part PEM formatted certificates and JKS.
-->
キーツールよりも柔軟なコマンドラインツールを使いたい場合は、マルチパート PEM フォーマット証明書と JKS を理解する [java-keyutil](https://code.google.com/p/java-keyutil/) を試してみてください。

<!--
## Certificate Settings
-->
## 証明書の設定

<!--
### Secure
-->
### セキュリティ

<!--
If you want the best security, consider using [ECDSA](http://blog.cloudflare.com/ecdsa-the-digital-signature-algorithm-of-a-better-internet) as the signature algorithm (in keytool, this would be `-sigalg EC`). ECDSA is also known as "ECC SSL Certificate".
-->
最高のセキュリティを求める場合は、(キーツールでは `-sigalg EC` であろう) 署名アルゴリズムに [ECDSA](https://blog.cloudflare.com/ecdsa-the-digital-signature-algorithm-of-a-better-internet) を使うことを検討してください。ECDSA は "ECC SSL Certificate" としても知られています。

<!--
### Compatible
-->
### 互換性

<!--
For compatibility with older systems, use RSA with 2048 bit keys and SHA256 as the signature algorithm.  If you are creating your own CA certificate, use 4096 bits for the root.
-->
古いシステムとの互換性には、署名アルゴリズムに 2048 bit 鍵の RSA と SHA256 を使ってください。独自の CA 証明書を作成している場合は、4096 bit のルートを使用してください。

<!--
## Further Reading
-->
## 併せて読みたい

<!--
* [JSSE Reference Guide To Creating KeyStores](http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#CreateKeystore)
* [Java PKI Programmer's Guide](http://docs.oracle.com/javase/7/docs/technotes/guides/security/certpath/CertPathProgGuide.html)
* [Fixing X.509 Certificates](http://tersesystems.com/2014/03/20/fixing-x509-certificates/)
-->
* [JSSEで使用するキーストアの作成](http://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#CreateKeystore)
* [Java PKIプログラマーズ・ガイド](http://docs.oracle.com/javase/jp/7/docs/technotes/guides/security/certpath/CertPathProgGuide.html)
* [Fixing X.509 Certificates](http://tersesystems.com/2014/03/20/fixing-x509-certificates/)

<!--
> **Next:** [[Configuring Trust Stores and Key Stores|KeyStores]]
-->
> **Next:** [[トラストストアとキーストアの設定|KeyStores]]