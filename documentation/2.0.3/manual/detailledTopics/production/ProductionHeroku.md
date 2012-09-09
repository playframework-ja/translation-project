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

<!--
The web process is up.  Review the logs for more information:
-->
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
If you don't see the web process, and you see `Error H14 (No web processes running)` in your logs, you probably need to add a web dyno:
-->
もし Web プロセスが起動しておらず、 `Error H14 (No web processes running)` というエラーがログに出力されているようであれば、 web dynamo を追加する必要があるでしょう。

    heroku ps:scale web=1
    heroku restart

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

<!--
## Finetuning
-->
## 微調整

<!--
Even for simple apps, the heroku slug size will soon exceed 100MB, which is the allowed range.
The reason is the .ivy cache that is included in the slug. You can easily overcome that using your own custom heroku "build pack". A build pack is a bunch of scripts stored in a remote git repository. The are fetched and executed at compile time. The original build pack is available here: https://github.com/heroku/heroku-buildpack-scala.
-->
シンプルなアプリケーショであっても、容量が Heroku の slug size の最大である 100MB をすぐ超えてしまいます。
理由は、 .ivy キャッシュが slug に含まれてしまっているからです。カスタムの heroku "build pack" を利用すると、この問題は簡単に解決できます。build pack とはリモート git レポジトリに保存される多数のスクリプトをセットにしたもののことです。スクリプトは、 git で取得されて、コンパイル時に実行されます。標準の build pack は [[こちら|https://github.com/heroku/heroku-buildpack-scala.]] で公開されています。

<!--
You simply fork it on github and change the ```bin/compile``` script towards its end:
-->
具体的には、 GitHub で build pack のレポジトリを fork して、 ```bin/compile``` スクリプトの後半を次のように変更します。

```
...
mkdir -p $CACHE_DIR
for DIR in $CACHED_DIRS ; do
  rm -rf $CACHE_DIR/$DIR
  mkdir -p $CACHE_DIR/$DIR
  cp -r $DIR/.  $CACHE_DIR/$DIR
echo "-----> Dropping ivy cache from the slug: $DIR"
rm -rf $SBT_USER_HOME/.ivy2
done
```

<!--
Using the same tool, you can overcome another problem: On heroku, the scala template compilation needs the UTF8 encoding to be explicitly set. Simply change that in your build pack like that:
-->
同じ方法で、別の問題ー Heroku で Scala テンプレートをコンパイルする際に文字コードとして UTF8 を明示しなければならないーにも対応できます。これも、 build pack を次のように修正します。

```
...
echo "-----> Running: sbt $SBT_TASKS"
test -e "$SBT_BINDIR"/sbt.boot.properties && PROPS_OPTION="-Dsbt.boot.properties=$SBT_BINDIR/sbt.boot.properties"
HOME="$SBT_USER_HOME_ABSOLUTE" java -Xmx512M -Dfile.encoding=UTF8 -Duser.home="$SBT_USER_HOME_ABSOLUTE" -Divy.default.ivy.user.dir="$SBT_USER_HOME_ABSOLUTE/.ivy2" $PROPS_OPTION -jar "$SBT_BINDIR"/$SBT_JAR $SBT_TASKS 2>&1 | sed -u 's/^/       /'
if [ "${PIPESTATUS[*]}" != "0 0" ]; then
  echo " !     Failed to build app with SBT $SBT_VERSION"
  exit 1
fi
...
```

<!--
Here you can find an example of a working build pack: https://github.com/joergviola/heroku-buildpack-scala.
-->
実際に動作する build pack は [[コチラ|https://github.com/joergviola/heroku-buildpack-scala.]] にあります。

<!--
Last step: Add the build pack address as a config var to heroku:
-->
最終ステップ: build pack のアドレスを Heroku の config var に設定します。

```
heroku config:add BUILDPACK_URL='git@github.com:joergviola/heroku-buildpack-scala.git'
```