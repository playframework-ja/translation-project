<!-- translated -->
<!--
# Managing database evolutions
-->
# データベース・エボリューション

<!--
When you use a relational database, you need a way to track and organize your database schema evolutions. Typically there are several situation where you need a more sophisticated way to track your database schema changes:
-->
リレーショナル・データベースを使う場合、スキーマ・エボリューションを追跡・整理する方法が必要になります。特に、次のような場合には、データベース・スキーマへの変更を追跡するために高度なツールを利用したくなると思います。

<!--
- When you work within a team of developers, each person needs to know about any schema change.
- When you deploy on a production server, you need to have a robust way to upgrade your database schema.
- If you work on several machines, you need to keep all database schemas synchronized.
-->
- チームで開発を行なっていて、各メンバーがスキーマへのすべての変更を知っておく必要がある
- アプリケーションを本番サーバにデプロイするにあたって、データベース・スキーマを安全にアップグレードしたい
- 複数台のマシンで開発を行なっていて、全マシンでデータベース・スキーマを同期させたい

<!--
## Evolutions scripts
-->
## エボリューション・スクリプト

<!--
Play tracks your database evolutions using several evolutions script. These scripts are written in plain old SQL and should be located in the `conf/evolutions/{database name}` directory of your application. If the evolutions apply to your default database, this path is `conf/evolutions/default`.
-->
Play はエボリューション・スクリプトによってデータベース・エボリューションを追跡します。このスクリプトはただの SQL で記述し、アプリケーションの `conf/evolutions/{データベース名}` というディレクトリに保存することになっています。デフォルト・データベースにエボリューションを適用したい場合は、スクリプトの保存先は、 `conf/evolutions/default` になります。

<!--
The first script is named `1.sql`, the second script `2.sql`, and so on…
-->
最初のスクリプトは `1.sql`、2番目のスクリプトは `2.sql`、…というように名前をつけます。

<!--
Each script contains two parts:
-->
各スクリプトはそれぞれ二つのパートで構成されています。

<!--
- The **Ups** part the describe the required transformations.
- The **Downs** part that describe how to revert them.
-->
- **Ups** パートは、必要なスキーマの変換方法の記述
- **Downs** パートは、上記の変換をもとに戻す方法の記述

<!--
For example, take a look at this first evolution script that bootstrap a basic application:
-->
試しに、基本的なアプリケーションを始めるための最初のエボリューション・スクリプトを作成してみましょう。

```
# Users スキーマ
 
# --- !Ups
 
CREATE TABLE User (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    fullname varchar(255) NOT NULL,
    isAdmin boolean NOT NULL,
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE User;
```

<!--
As you see you have to delimitate the both Ups and Downs section by using comments in your SQL script.
-->
ご覧のとおり、Ups パートと Downs パートはコメントで区切る必要があります。

<!--
Evolutions are automatically activated if a database is configured in `application.conf` and evolution scripts are present. You can disable them by setting `evolutionplugin=disabled`. For example when tests set up their own database you can disable evolutions for the test environment.
-->
データベースが `application.conf` で設定されていて、エボリューション・スクリプトが存在する場合は、エボリューションが自動的に有効化されます。エボリューションを無効化したい場合は、 `evolutionplugin=disabled` という設定を行なってください。例えば、自前でデータベースをセットアップするような自動化テストを書くような場合は、テスト環境においてエボリューションを無効化する設定を行いましょう。

<!--
When evolutions are activated, Play will check your database schema state before each request in DEV mode, or before starting the application in PROD mode. In DEV mode, if your database schema is not up to date, an error page will suggest that you synchronise your database schema by running the appropriate SQL script.
-->
エボリューションが有効化されていると、Play は開発モードの場合リクエスト毎に、本番モードの場合アプリケーションの起動前にスキーマの状態をチェックします。開発モードでは、データベース・スキーマが最新でない場合、適切な SQL スクリプトを実行してデータベース・スキーマを同期させるように促すエラーページが表示されます。

[[images/evolutions.png]]

<!--
If you agree with the SQL script, you can apply it directly by clicking on the ‘Apply evolutions’ button.
-->
SQL スクリプトの内容に問題がなければ、この `Apply evolutions` ボタンをクリックすることで即座にスクリプトを実行することができます。

<!--
## Synchronizing concurrent changes
-->
## 同時に行われた変更を同期させる

<!--
Now let’s imagine that we have two developers working on this project. Developer A will work on a feature that requires a new database table. So he will create the following `2.sql` evolution script:
-->
仮に、プロジェクトに二人の開発者がいるとします。開発者 A は新しいテーブルを必要とする新機能を担当しています。そこで、開発者 A は次のような `2.sql` を作成します。

```
# Post 追加
 
# --- !Ups
CREATE TABLE Post (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content text NOT NULL,
    postedAt date NOT NULL,
    author_id bigint(20) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES User(id),
    PRIMARY KEY (id)
);
 
# --- !Downs
DROP TABLE Post;
```

<!--
Play will apply this evolution script to Developer A’s database.
-->
Play はこのエボリューション・スクリプトを開発者 A のデータベースで実行します。

<!--
On the other hand, developer B will work on a feature that requires altering the User table. So he will also create the following `2.sql` evolution script:
-->
一方、開発者 B は User テーブルのスキーマ変更を要する新機能を担当しています。そこで、開発者 B は次のような `2.sql` を作成します。

```
# User 変更
 
# --- !Ups
ALTER TABLE User ADD age INT;
 
# --- !Downs
ALTER TABLE User DROP age;
```

<!--
Developer B finishes his feature and commits (let’s say they are using Git). Now developer A has to merge the his colleague’s work before continuing, so he runs git pull, and the merge has a conflict, like:
-->
開発者 B は担当している機能を実装し終わったあと、それをコミットします (彼らが Git を使っていることにしましょう)。すると、開発者 A は作業を続行するにあたって、チームメイトが行った作業をマージする必要があります。彼は git pull を実行しますが、マージにはコンフリクトがあります。

```
Auto-merging db/evolutions/2.sql
CONFLICT (add/add): Merge conflict in db/evolutions/2.sql
Automatic merge failed; fix conflicts and then commit the result.
```

<!--
Each developer has created a `2.sql` evolution script. So developer A needs to merge the contents of this file:
-->
二人の開発者がそれぞれ `2.sql` を作成したため、開発者 A はこのファイルの内容をマージしなければなりません。

```
<<<<<<< HEAD
# Post 追加
 
# --- !Ups
CREATE TABLE Post (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content text NOT NULL,
    postedAt date NOT NULL,
    author_id bigint(20) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES User(id),
    PRIMARY KEY (id)
);
 
# --- !Downs
DROP TABLE Post;
=======
# User 変更
 
# --- !Ups
ALTER TABLE User ADD age INT;
 
# --- !Downs
ALTER TABLE User DROP age;
>>>>>>> devB
```

<!--
The merge is really easy to do:
-->
このマージは簡単ですね。

```
# Post 追加と User 変更
 
# --- !Ups
ALTER TABLE User ADD age INT;
 
CREATE TABLE Post (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content text NOT NULL,
    postedAt date NOT NULL,
    author_id bigint(20) NOT NULL,
    FOREIGN KEY (author_id) REFERENCES User(id),
    PRIMARY KEY (id)
);
 
# --- !Downs
ALTER TABLE User DROP age;
 
DROP TABLE Post;
```

<!--
This evolution script represents the new revision 2 of the database, that is different of the previous revision 2 that developer A has already applied.
-->
このエボリューション・スクリプトが表すデータベースのリビジョン 2 は、最初に開発者 A が適用したリビジョン 2 とは内容が異なります。

<!--
So Play will detect it and ask developer A to synchronize his database by first reverting the old revision 2 already applied, and by applying the new revision 2 script:
-->
このとき、 Play はこの二つのリビジョンの内容の違いを検出して、まずは現在適用されている古い方のリビジョン 2 への変更を取り消し、それから新しい方のリビジョン 2 のスクリプトを適用します。

<!--
## Inconsistent states
-->
## 不整合状態

<!--
Sometimes you will make a mistake in your evolution scripts, and they will fail. In this case, Play will mark your database schema as being in an inconsistent state and will ask you to manually resolve the problem before continuing.
-->
たまにエボリューション・スクリプトに間違いがあって、適用に失敗してしまうこともあるでしょう。その場合、 Play はデータベース・スキーマを不整合状態としてマークします。そして、続行する前に問題を手動で解決するようにあなたに促します。

<!--
For example, the Ups script of this evolution has an error:
-->
例として、Ups スクリプトにエラーを含む次のようなエボリューションを作成してみましょう。

```
# User にカラムを追加
  
# --- !Ups
ALTER TABLE Userxxx ADD company varchar(255);
 
# --- !Downs
ALTER TABLE User DROP company;
```

<!--
So trying to apply this evolution will fail, and Play will mark your database schema as inconsistent:
-->
このエボリューションの適用は失敗し、 Play はデータベース・スキーマを不整合状態としてマークします。

[[images/evolutionsError.png]]

<!--
Now before continuing you have to fix this inconsistency. So you run the fixed SQL command:
-->
続行するためには、まずこの不整合を解決する必要があります。今回は、正しい SQL コマンドを実行します。

```
ALTER TABLE User ADD company varchar(255);
```

<!--
… and then mark this problem as manually resolved by clicking on the button.
-->
次に、ボタンをクリックして、不整合を手動で解決したことを Play に知らせます。

<!--
But because your evolution script has errors, you probably want to fix it. So you modify the `3.sql` script:
-->
しかし、エボリューション・スクリプトはまだ間違ったままなので、修正したいところです。そこで、以下のように `3.sql` を編集します。

```
# User にカラムを追加
  
# --- !Ups
ALTER TABLE User ADD company varchar(255);
 
# --- !Downs
ALTER TABLE User DROP company;
```

<!--
Play detects this new evolution that replaces the previous 3 one, and will run the appropriate script. Now everything is fixed, and you can continue to work.
-->
Play はこの新しいエボリューションを検知して、以前のリビジョン 3 と置き換え、適切なスクリプトを実行します。これで、全ての間違いが修正されて、本来の作業に戻ることができます。

<!--
> In developement mode however it is often simpler to simply trash your developement database and reapply all evolutions from the beginning.
-->
> ただし、開発モードでは単に開発用のデータベースを破棄して、全てのエボリューションを最初から適用しなおす方が簡単なことも多々あります。
