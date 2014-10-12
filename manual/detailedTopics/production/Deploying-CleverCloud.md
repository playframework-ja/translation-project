<!--
# Deploying to Clever Cloud
-->
# Clever Cloud にデプロイする
<!--
[Clever Cloud](http://www.clever-cloud.com/en) is a Platform as a Service solution. You can deploy on it Scala, Java, PHP, Python and Node.js applications. Its main particularity is that it supports **automatic vertical and horizontal scaling**.
-->
[Clever Cloud](http://www.clever-cloud.com/en) は PaaS ソリューションです。Scala, Java, PHP, Python そして Node.js アプリケーションをデプロイすることができます。Clever Cloud の主な特徴は **垂直かつ水平なオートスケール** をサポートしている点です。

<!--
Clever Cloud supports Play! 2 applications natively. The present guide explains how to deploy your application on Clever Cloud.
-->
Clever Cloud は Play! 2 アプリケーションをネイティブにサポートしています。このガイドでは、Clever Cloud にアプリケーションをデプロイする方法を説明します。

<!--
## Create a new application on Clever Cloud
-->
## Clever Cloud に新しいアプリケーションを作成する

<!--
Create your Play! application on Clever Cloud [dashboard](https://console.clever-cloud.com).
-->
Clever Cloud [ダッシュボード](https://console.clever-cloud.com) 上に Play! アプリケーションを作成してください。

<!--
## Deploy your application
-->
## アプリケーションをデプロイする

<!--
To deploy your application on Clever Cloud, just use git to push your code to the application remote repository.
-->
git を使ってアプリケーションのリモートリポジトリにコードをプッシュするだけで、Clever Clound 上にアプリケーションをデプロイすることができます。


```bash
$ git remote add <your-remote-name> <your-git-deployment-url>
$ git push <your-remote-name> master
```

<!--
**Important tip: do not forget to push to the remote master branch.**
-->
**重要な tip: リモートの master ブランチにプッシュすることを忘れないでください。**

<!--
If you work in a different branch, just use: 
-->
別のブランチで作業している場合は、以下のようにするだけです:

```bash
$ git remote add <your-remote-name> <your-git-deployment-url>
$ git push <your-remote-name> <your-branch-name>:master
```

<br/>
<!--
Clever Cloud will run `sbt update stage` to prepare your application. On the first deployment, all dependencies will be downloaded, which takes a while to complete (but will be cached for future deployments).
-->
Clever Cloud はアプリケーションを準備するために `sbt update stage` を実行します。最初のデプロイではすべての依存性がダウンロードされる (以降のデプロイではキャッシュされます) ため、完了まで時間が掛かります。


<!--
## Check the deployment of your application
-->
## アプリケーションのデプロイを確認する

<!--
You can check the deployment of your application by visiting the ***logs*** section of your application in the dashboard.
-->
ダッシュボード内でアプリケーションの ***logs*** セクションでアプリケーションのデプロイを確認することができます。


<!--
## [Optional] Configure your application
-->
## [オプション] アプリケーションの設定
<!--
You can custom your application with a `clevercloud/play.json` file.
-->
`clevercloud/play.json` ファイルを使ってアプリケーションをカスタマイズすることができます。

<!--
The file must contain the following fields:
-->
このファイルには以下のフィールドが含まれていなければなりません:

```javascript
{
    "deploy": {
        "goal": <string>
    }
}
```

<!--
That field can contain additional configuration like:
-->
このフィールドは以下のような追加の設定を含むことができます:

<!--
`"-Dconfig.resource=clevercloud.conf"`, `"-Dplay.version=2.0.4"` or `"-DapplyEvolutions.default=true"`.
-->
`"-Dconfig.resource=clevercloud.conf"`, `"-Dplay.version=2.0.4"` または `"-DapplyEvolutions.default=true"` です。

<!--
## Connecting to a database
-->
## データベースに接続する

<!--
Just go to the ***Services*** section in the Clever Cloud dashboard to add the database you need: MySQL, PostgreSQL or Couchbase.
-->
Clever Cloud ダッシュボード内の ***Services*** セクションで必要なデータベースを追加してください: MySQL, PostgreSQL または Couchbase のいずれかです。

<!--
As in every Play! 2 application, the only file you have to modify is your `conf/application.conf` file.
-->
すべての Play! 2 アプリケーションがそうであるように、変更する必要があるのは `conf/application.conf` ファイルだけです。

<!--
**Example: setup MySQL database**
-->
**例: Mysqlデータベースを設定する**

```
db.default.url="jdbc:mysql://{yourcleverdbhost}/{dbname}"
db.default.driver=com.mysql.jdbc.Driver
db.default.user={yourcleveruser}
db.default.password={yourcleverpass}
```

<!--
## Further information
-->
## さらなる情報
<!--
If you need further information, just check our complete [documentation](http://doc.clever-cloud.com/java/play-framework-2/).
-->
より詳しくは、完全な [ドキュメント](http://doc.clever-cloud.com/java/play-framework-2/) を確認してください。
