<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Deploying to Heroku
-->
# Heroku へのデプロイ

<!--
[Heroku](https://www.heroku.com/) is a cloud application platform – a way of building and deploying web apps.
-->
[Heroku](https://www.heroku.com/) はクラウド・アプリケーション・プラットフォーム - つまり web アプリケーションをビルドし、デプロイするひとつの方法です。

<!--
To get started:
-->
まず始めに以下の作業を行います。

<!--
1. [Install the Heroku Toolbelt](https://toolbelt.heroku.com)
2. [Sign up for a Heroku account](https://id.heroku.com/signup)
-->
1. [Heroku Toolbelt をインストールする](https://toolbelt.heroku.com)
2. [Heroku アカウントにサインアップする](https://id.heroku.com/signup)


<!--
## Store your application in git
-->
## アプリケーションを git で保存する

```bash
$ git init
$ git add .
$ git commit -m "init"
```


<!--
## Create a new application on Heroku
-->
## Heroku 上で新規アプリケーションを作成する

```bash
$ heroku create
Creating warm-frost-1289... done, stack is cedar
http://warm-1289.herokuapp.com/ | git@heroku.com:warm-1289.git
Git remote heroku added
```

<!--
This provisions a new application with an HTTP (and HTTPS) endpoint and Git endpoint for your application.  The Git endpoint is set as a new remote named `heroku` in your Git repository's configuration.
-->
これで、新しいアプリケーションが、 HTTP (と HTTPS) のエンドポイントおよび Git のエンドポイントと一緒に提供されます。 Git エンドポイントは Git リポジトリの設定に `heroku` というリモートリポジトリとして登録されています。


<!--
## Deploy your application
-->
## アプリケーションをデプロイする

<!--
To deploy your application on Heroku, just use git to push it into the `heroku` remote repository:
-->
Heroku にアプリケーションをデプロイするため、ローカルレポジトリを `heroku` という名前のリモートレポジトリへ git push してください。

```bash
$ git push heroku master
Counting objects: 34, done.
Delta compression using up to 8 threads.
Compressing objects: 100% (20/20), done.
Writing objects: 100% (34/34), 35.45 KiB, done.
Total 34 (delta 0), reused 0 (delta 0)

-----> Heroku receiving push
-----> Scala app detected
-----> Building app with sbt v0.11.0
-----> Running: sbt clean compile stage
       ...
-----> Discovering process types
       Procfile declares types -> web
-----> Compiled slug size is 46.3MB
-----> Launching... done, v5
       http://8044.herokuapp.com deployed to Heroku

To git@heroku.com:floating-lightning-8044.git
* [new branch]      master -> master
```

<!--
Heroku will run `sbt clean stage` to prepare your application. On the first deployment, all dependencies will be downloaded, which takes a while to complete (but will be cached for future deployments).
-->
git push が完了すると、 Heroku は `sbt clean stage` を実行して、アプリケーションをビルドします。初回のデプロイ時には、全ての依存モジュールがダウンロードされるため、多少の時間がかかります (2 回目以降のデプロイ時はキャッシュが使われます) 。


<!--
## Check that your application has been deployed
-->
## アプリケーションがデプロイされたことを確認する

<!--
Now, let’s check the state of the application’s processes:
-->
では、デプロイしたアプリケーションの状態を確認してみましょう。

```bash
$ heroku ps
Process       State               Command
------------  ------------------  ----------------------
web.1         up for 10s          target/universal/stage/bin/myapp 
```

<!--
The web process is up.  Review the logs for more information:
-->
Web プロセスが起動しています。次は、詳細を確認するため、ログを見てみましょう:

```bash
$ heroku logs
2011-08-18T00:13:41+00:00 heroku[web.1]: Starting process with command `target/universal/stage/bin/myapp`
2011-08-18T00:14:18+00:00 app[web.1]: Starting on port:28328
2011-08-18T00:14:18+00:00 app[web.1]: Started.
2011-08-18T00:14:19+00:00 heroku[web.1]: State changed from starting to up
...
```

<!--
We can also tail the logs in the same manner as we could do at a regular command line.  This is useful for debugging:
-->
通常のコマンドラインで行うのと同じやり方でログを追いかけることもできます。これはデバッグに便利です:
```bash
$ heroku logs -t --app floating-lightning-8044
2011-08-18T00:13:41+00:00 heroku[web.1]: Starting process with command `target/start`
2011-08-18T00:14:18+00:00 app[web.1]: Starting on port:28328
2011-08-18T00:14:18+00:00 app[web.1]: Started.
2011-08-18T00:14:19+00:00 heroku[web.1]: State changed from starting to up
...
```

<!--
Looks good. We can now visit the app by running:
-->
問題なしのようです。さらに以下のコマンドを実行すると、ブラウザが起動してデプロイした Web アプリにアクセスすることができます:

```bash
$ heroku open
```


<!--
## Connecting to a database
-->
## データベースに接続する

<!--
Heroku provides a number of relational and NoSQL databases through [Heroku Add-ons](https://addons.heroku.com).  Play applications on Heroku are automatically provisioned a [Heroku Postgres](https://addons.heroku.com/heroku-postgresql) database.  To configure your Play application to use the Heroku Postgres database, first add the PostgreSQL JDBC driver to your application dependencies (`build.sbt`):
-->
Heroku は [Heroku Add-ons](https://addons.heroku.com) によって多くのリレーショナルデータベースと NoSQL データベースを提供しています。Heroku 上の Play アプリケーションには、自動的に [Heroku Postgres](https://addons.heroku.com/heroku-postgresql) が提供されています。この Postgres データベースを Play アプリケーションが使うように設定するには、まず PostgreSQL JDBC ドライバをアプリケーション依存性 (`build.sbt`) に追加します:

```scala
libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
```

<!--
Then create a new file in your project's root directory named `Procfile` (with a capital "P") that contains the following (substituting the `myapp` with your project's name):
-->
そして、プロジェクトのルートディレクトリに `Procfile` ("P" は大文字) というファイルを以下の内容で作成します (`myapp` は自身のプロジェクト名に置き換えてください):

```txt
web: target/universal/stage/bin/retailos -Dhttp.port=${PORT} -DapplyEvolutions.default=true -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url=${DATABASE_URL}
```

<!--
This instructs Heroku that for the process named `web` it will run Play and override the `applyEvolutions.default`, `db.default.driver`, and `db.default.url` configuration parameters.  Note that the `Procfile` command can be maximum 255 characters long.  Alternatively, use the `-Dconfig.resource=` or `-Dconfig.file=` mentioned in [[production configuration|ProductionConfiguration]] page.
-->
この設定で、 `web` というプロセスでは Play を起動し `applyEvolutions.default` 、 `db.default.driver` それと `db.default.url` 設定をオーバーライドするように Heroku に指示します。 `Procfile` コマンドは最大で 255 文字である事に注意して下さい。代替手段として [[本番向けの設定|ProductionConfiguration]] ページに書かれている `-Dconfig.resource=` および `-Dconfig.file=` を使用します。
<!--
Note that the creation of a Procfile is not actually required by Heroku, as Heroku will look in your play application's conf directory for an application.conf file in order to determine that it is a play application.
-->
Heroku はデプロイされたアプリケーションが play アプリケーションであると断定するために conf ディレクトリの中にある application.conf ファイルを探すので、実際のところ Procfile の作成は必須でないことを覚えておいてください。

<!--
## Further learning resources
-->
## 更に進んだ学習リソース

<!--
* [Play Tutorial for Java](https://github.com/jamesward/play2torial/blob/master/JAVA.md)
* [Getting Started with Play, Scala, and Squeryl](http://www.artima.com/articles/play2_scala_squeryl.html)
* [Edge Caching With Play, Heroku, and CloudFront](http://www.jamesward.com/2012/08/08/edge-caching-with-play2-heroku-cloudfront)
* [Optimizing Play for Database-Driven Apps](http://www.jamesward.com/2012/06/25/optimizing-play-2-for-database-driven-apps)
* [Play Scala Console on Heroku](http://www.jamesward.com/2012/06/11/play-2-scala-console-on-heroku)
* [Play App with a Scheduled Job on Heroku](https://github.com/jamesward/play2-scheduled-job-demo)
* [Using Amazon S3 for File Uploads with Java and Play](https://devcenter.heroku.com/articles/using-amazon-s3-for-file-uploads-with-java-and-play-2)
-->
* [Java 用の Play チュートリアル](https://github.com/jamesward/play2torial/blob/master/JAVA.md)
* [Play, Scala, Squeryl による入門](http://www.artima.com/articles/play2_scala_squeryl.html)
* [Play, Heroku, CloudFront によるエッジキャッシング](http://www.jamesward.com/2012/08/08/edge-caching-with-play2-heroku-cloudfront)
* [データベース指向アプリケーションのための Play 最適化](http://www.jamesward.com/2012/06/25/optimizing-play-2-for-database-driven-apps)
* [Heroku での Play Scala コンソール](http://www.jamesward.com/2012/06/11/play-2-scala-console-on-heroku)
* [Heroku 上でスケジューリングされたジョブを持つ Play アプリケーション](https://github.com/jamesward/play2-scheduled-job-demo)
* [Java と Play でファイルアップロードに Amazon S3 を使う](https://devcenter.heroku.com/articles/using-amazon-s3-for-file-uploads-with-java-and-play-2)
