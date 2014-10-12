<!--
# Configuring HTTPS
-->
# HTTPS の設定

Play can be configured to serve HTTPS.  To enable this, simply tell Play which port to listen to using the `https.port` system property.  For example:

    ./start -Dhttps.port=9443

<!--
## SSL Certificates
-->
## SSL 証明書

By default, Play will generate itself a self-signed certificate, however typically this will not be suitable for serving a website.  Play uses Java key stores to configure SSL certificates and keys.

<!--
Signing authorities often provide instructions on how to create a Java keystore (typically with reference to Tomcat configuration).  The official Oracle documentation on how to generate keystores using the JDK keytool utility can be found [here](http://docs.oracle.com/javase/7/docs/technotes/tools/solaris/keytool.html).
-->
署名当局は、しばしば Java キーストアの作り方を (典型的には Tomcat の設定などを参照して) 説明してくれます。JDK の keytool ユーティリティを使ったキーストアの生成方法に関する Oralce の公式ドキュメントは [ここ](http://docs.oracle.com/javase/7/docs/technotes/tools/solaris/keytool.html) で見つけることができます。

<!--
Having created your keystore, the following system properties can be used to configure Play to use it:
-->
キーストアを作成したら、以下のシステムプロパティを使って Play がこれを使用するよう設定することができます:

<!--
* **https.keyStore** - The path to the keystore containing the private key and certificate, if not provided generates a keystore for you
* **https.keyStoreType** - The key store type, defaults to `JKS`
* **https.keyStorePassword** - The password, defaults to a blank password
* **https.keyStoreAlgorithm** - The key store algorithm, defaults to the platforms default algorithm
-->
* **https.keyStore** - 秘密鍵と証明書を含むキーストアへのパス。このプロパティが指定されない場合はキーストアを生成します
* **https.keyStoreType** - キーストアタイプ。デフォルトは `JKS` です
* **https.keyStorePassword** - パスワード。デフォルトは空パスワードです
* **https.keyStoreAlgorithm** - キーストアアルゴリズム。デフォルトはプラットフォームのデフォルトアルゴリズムです

<!--
## Turning HTTP off
-->
## HTTP を無効にする

<!--
To disable binding on the HTTP port, set the `http.port` system property to be `disabled`, eg:
-->
HTTP ポートへのバインドを無効にしたい場合は、以下のようにして `http.port` システムプロパティに `disabled` を設定してください:

    ./start -Dhttp.port=disabled -Dhttps.port=9443 -Dhttps.keyStore=/path/to/keystore -Dhttps.keyStorePassword=changeme

<!--
> **Next:** [[Deploying to a cloud service|DeployingCloud]]
-->
> **Next:** [[クラウドサービスへデプロイする|DeployingCloud]]
