<!-- translated -->
<!--
# Deploying to Heroku
-->
# Heroku へのデプロイ

<!--
[[Heroku | http://www.heroku.com/]] is a cloud application platform – a new way of building and deploying web apps.
-->
[[Heroku | http://www.heroku.com/]] は Web アプリケーションの構築とデプロイを効率化する、クラウド・アプリケーション・プラットフォームです。

<!--
## Add the Procfile　
-->
## Procfile の作成

<!--
Heroku requires a special file in the application root called `Procfile`. Create a simple text file with the following content:
-->
Heroku を利用するにあたって、アプリケーションのルートディレクトリに `Procfile` という特別なファイルを作成する必要があります。次のような内容のテキストファイルを作成してください。

```txt
web: target/start -Dhttp.port=${PORT} ${JAVA_OPTS}
```
<!--
## Store your application in git
-->
## アプリケーションを git で保存する

<!--
Just create a git repository for your application:
-->
開発中アプリケーションの git レポジトリを作成してください。

```bash
$ git init
$ git add .
$ git commit -m "init"
```

<!--
## Create a new application on Heroku
-->
## Heroku 上で新規アプリケーションを作成する

<!--
> Note that you need an Heroku account, and to install the heroku gem.
-->
> 事前に Heroku のアカウント登録、 heroku gem のインストールが必要です。

```bash
$ heroku create --stack cedar
Creating warm-frost-1289... done, stack is cedar
http://warm-1289.herokuapp.com/ | git@heroku.com:warm-1289.git
Git remote heroku added
```

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
Heroku will run `sbt clean compile stage` to prepare your application. On the first deployment, all dependencies will be downloaded, which takes a while to complete (but will be cached for future deployments).
-->
git push が完了すると、 Heroku は `sbt clean compile stage` を実行して、アプリケーションをビルドします。初回のデプロイ時には、全ての依存モジュールがダウンロードされるため、多少の時間がかかります。（２回目以降のデプロイ時はキャッシュが使われます。）

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
web.1         up for 10s          target/start 
```

<!-- The web process is up. Review the logs for more information: -->
Web プロセスが起動しています。次は、詳細を確認するため、ログを見てみましょう。

```bash
$ heroku logs
2011-08-18T00:13:41+00:00 heroku[web.1]: Starting process with command `target/start`
2011-08-18T00:14:18+00:00 app[web.1]: Starting on port:28328
2011-08-18T00:14:18+00:00 app[web.1]: Started.
2011-08-18T00:14:19+00:00 heroku[web.1]: State changed from starting to up
...
```

<!--
Looks good. We can now visit the app with `heroku open`.
-->
問題なしのようです。さらに `heroku open` コマンドを実行すると、ブラウザが起動してデプロイした Web アプリにアクセスすることができます。

<!--
## Running play commands remotely
-->
## サーバ上で play コマンドを実行する

<!--
Cedar allows you to launch a REPL process attached to your local terminal for experimenting in your application’s environment:
-->
Cedar では、サーバ上で起動した REPL プロセスにローカルの端末を接続して、アプリケーションの実行環境をテストすることができます。

```bash
$ heroku run sbt play
Running sbt play attached to terminal... up, run.2
[info] Loading global plugins from /app/.sbt_home/.sbt/plugins
[info] Updating {file:/app/.sbt_home/.sbt/plugins/}default-0f55ac...
[info] Set current project to My first application (in build file:/app/)
       _            _ 
 _ __ | | __ _ _  _| |
| '_ \| |/ _' | || |_|
|  __/|_|\____|\__ (_)
|_|            |__/ 
             
play! 2.0-beta, http://www.playframework.org

> Type "help" or "license" for more information.
> Type "exit" or use Ctrl+D to leave this console.

[My first application] $
```