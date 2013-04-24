<!-- translated -->
<!--
# Deploying to CloudFoundry / AppFog
-->
# CloudFoundry / AppFog にデプロイする

<!--
## CloudFoundry vs. AppFog
-->
## CloudFoundry vs. AppFog

<!--
Deploying to [AppFog](https://www.appfog.com/) can be accomplished by following the [Cloud Foundry](http://cloudfoundry.com) instructions below except run `af` instead of `vmc`. Also, with AppFog you need to follow an [extra step of adding an extra jar to the deployment zip file](https://docs.appfog.com/languages/java/play).
-->
[AppFog](https://www.appfog.com/) へのデプロイは、下記の [Cloud Foundry](http://cloudfoundry.com) のインストラクションで `vmc` の代わりに `af` を実行する事で実現できます。また AppFog では [デプロイ用 zip ファイルに jar を追加するステップ](https://docs.appfog.com/languages/java/play) に従う必要があります。

<!--
## Prerequisites
-->
## 必要条件

<!--
Sign up for a free [Cloud Foundry](http://cloudfoundry.com) account and install or update the Cloud Foundry command line tool, VMC, to the latest version (0.3.18 or higher) by using the following command:
-->
無料の [Cloud Foundry](http://cloudfoundry.com) アカウントにサインアップし、下記のコマンドで Cloud Foundry コマンドラインツールおよび VMC の最新版 (0.3.18 以上) をインストールもしくはアップグレードします。

```bash
gem install vmc
```

<!--
## Build your Application
-->
## アプリケーションをビルドする

<!--
Package your app by typing the `dist` command in the play prompt.
-->
Play プロンプトで `dist` コマンドを打つ事でアプリケーションをパッケージングします。

<!--
## Deploy your Application
-->
## アプリケーションをデプロイする

<!--
Deploy the created zip file to Cloud Foundry with the VMC push command.  If you choose to create a database service, Cloud Foundry will automatically apply your database evolutions on application start.
-->
作成した zip ファイルを VMC push コマンドで Cloud Foundry にデプロイします。もしデータベースサービスを作成すると選択した場合、 Cloud Foundry はアプリケーションの起動時に自動的にデータベースのエボリューションを適用します。

```bash
yourapp$ vmc push --path=dist/yourapp-1.0.zip
Application Name: yourapp
Detected a Play Framework Application, is this correct? [Yn]:
Application Deployed URL [yourapp.cloudfoundry.com]:
Memory reservation (128M, 256M, 512M, 1G, 2G) [256M]:
How many instances? [1]:
Create services to bind to 'yourapp'? [yN]: y
1: mongodb
2: mysql
3: postgresql
4: rabbitmq
5: redis
What kind of service?: 3
Specify the name of the service [postgresql-38199]: your-db
Create another? [yN]:
Would you like to save this configuration? [yN]: y
Manifest written to manifest.yml.
Creating Application: OK
Creating Service [your-db]: OK
Binding Service [your-db]: OK
Uploading Application:
  Checking for available resources: OK
  Processing resources: OK
  Packing application: OK
  Uploading (186K): OK
Push Status: OK
Staging Application 'yourapp': OK
Starting Application 'yourapp': OK
```

<!--
## Working With Services
-->
## サービスとのやり取りをする

<!--
### Auto-Reconfiguration
-->
### Auto-Reconfiguration
<!--
Cloud Foundry uses a mechanism called auto-reconfiguration to automatically connect your Play application to a relational database service. If a single database configuration is found in the Play configuration (for example, `default`) and a single database service instance is bound to the application, Cloud Foundry will automatically override the connection properties in the configuration to point to the PostgreSQL or MySQL service bound to the application.
-->
Cloud Foundry は auto-reconfiguration というメカニズムを使用して Play アプリケーションをリレーショナルデータベースサービスに自動接続します。もし Play の設定に単一のデータベース設定 (例えば `default`) が見つかり、単一のデータベースサービスインスタンスがアプリケーションに紐付けられている場合、 Cloud Foundry は設定されている接続プロパティを自動的にオーバーライドして、アプリケーションに紐付けられた PostgreSQL および MySQL のサービスを指定するようにします。

<!--
This is a great way to get simple apps up and running quickly. However, it is quite possible that your application will contain SQL that is specific to the type of database you are using.  In these cases, or if your app needs to bind to multiple services, you may choose to avoid auto-reconfiguration and explicitly specify the service connection properties.
-->
この方法はシンプルなアプリケーションを素早く立ち上げて動作させるすばらしい方法です。しかし、アプリケーションには使用しているデータベースに特有の SQL が含まれている事がよくあります。このような場合や、アプリケーションが複数のサービスと紐付けたい場合には、 auto-reconfiguration を回避して明示的にサービス接続プロパティを指定することができます。

<!--
### Connecting to Cloud Foundry Services
-->
### Cloud Foundry サービスに接続する
<!--
As always, Cloud Foundry provides all of your service connection information to your application in JSON format through the VCAP_SERVICES environment variable. However, connection information is also available as series of properties you can use in your Play configuration. Here is an example of connecting to a PostgreSQL service named `tasks-db` from within an application.conf file:
-->
通常通り、 Cloud Foundry はアプリケーションに接続された全てのサービス接続の情報を VCAP_SERVICES 環境変数を通して JSON フォーマットで提供します。しかし接続情報は Play の設定で利用可能なプロパティとして取得する事も出来ます。ここでは `tasks-db` という PostgreSQL のサービスに接続する application.conf ファイルの例を示します。

```bash
db.default.driver=${?cloud.services.tasks-db.connection.driver}
db.default.url=${?cloud.services.tasks-db.connection.url}
db.default.password=${?cloud.services.tasks-db.connection.password}
db.default.user=${?cloud.services.tasks-db.connection.username}
```

<!--
This information is available for all types of services, including NoSQL and messaging services. Also, if there is only a single service of a type (e.g. postgresql), you can refer to that service only by type instead of specifically by name, as exemplified below:
-->
この情報は NoSQL やメッセージングサービスを含む全てのタイプのサービスに適用可能です。また、一つのサービス (postgresql 等) しか無い場合、名前を指定する代わりに以下のようにそのサービスを指定することができます。

```bash
db.default.driver=${?cloud.services.postgresql.connection.driver}
db.default.url=${?cloud.services.postgresql.connection.url}
db.default.password=${?cloud.services.postgresql.connection.password}
db.default.user=${?cloud.services.postgresql.connection.username}
```
<!--
We recommend keeping these properties in a separate file (for example `cloud.conf`) and then including them only when building a distribution for Cloud Foundry. You can specify an alternative config file to `play dist` by using `-Dconfig.file`.
-->
これらのプロパティを (`cloud.conf` 等の) 別ファイルにし、 Cloud Foundry へのディストリビューションをビルドする時のみに含める事が推奨されます。 `play dist` に `-Dconfig.file` を使う事で別のファイルを指定する事が出来ます。

<!--
### Opting out of Auto-Reconfiguration
-->
### Auto-reconfiguration を無効にする
<!--
If you use the properties referenced above, you will automatically be opted-out. To explicitly opt out, include a file named “cloudfoundry.properties” in your application’s conf directory, and add the entry `autoconfig=false`
-->
上記のプロパティを使用した場合、 auto-reconfiguration が無効になります。明示的に無効にするには "cloudfoundry.properties" ファイルをアプリケーションの conf ディレクトリに作成し、それから `autoconfig=false` を追加します。
