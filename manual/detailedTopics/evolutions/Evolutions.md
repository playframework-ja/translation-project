<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
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
# Users schema
 
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
As you see you have to delimit the both Ups and Downs section by using comments in your SQL script.
-->
ご覧のとおり、Ups パートと Downs パートはコメントで区切る必要があります。

<!--
> Play splits your `.sql` files into a series of semicolon-delimited statements before executing them one-by-one against the database. So if you need to use a semicolon *within* a statement, escape it by entering `;;` instead of `;`. For example, `INSERT INTO punctuation(name, character) VALUES ('semicolon', ';;');`.
-->
> Play は `.sql` ファイルをセミコロンで区切ってから、それらをひとつひとつデータベースに対して実行します。そのため、文の中でセミコロンを使用する必要がある場合は `;` の代わりに `;;` を入力する事でエスケープします。例えば `INSERT INTO punctuation(name, character) VALUES ('semicolon', ';;');` となります。

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
# Add Post
 
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
# Update User
 
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
# Add Post
 
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
# Update User
 
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
# Add Post and update User
 
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
# Add another column to User
  
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
# Add another column to User
  
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
> In development mode however it is often simpler to simply trash your development database and reapply all evolutions from the beginning.
-->
> ただし、開発モードでは単に開発用のデータベースを破棄して、全てのエボリューションを最初から適用しなおす方が簡単なことも多々あります。

### Transactional DDL

By default, each statement of each evolution script will be executed immediately. If your database supports [Transactional DDL](https://wiki.postgresql.org/wiki/Transactional_DDL_in_PostgreSQL:_A_Competitive_Analysis) you can set `evolutions.autocommit=false` in application.conf to change this behaviour, causing **all** statements to be executed in **one transaction** only. Now, when an evolution script fails to apply with autocommit disabled, the whole transaction gets rolled back and no changes will be applied at all. So your database stays "clean" and will not become inconsistent. This allows you to easily fix any DDL issues in the evolution scripts without having to modify the database by hand like described above.

<!--
### Evolution storage and limitations
-->
### エボリューションのストレージと制限

<!--
Evolutions are stored in your database in a table called PLAY_EVOLUTIONS.  A Text column stores the actual evolution script.  Your database probably has a 64kb size limit on a text column.  To work around the 64kb limitation you could: manually alter the play_evolutions table structure changing the column type or (prefered) create multiple evolutions scripts less than 64kb in size.
-->
エボリューションはデータベースの PLAY_EVOLUTIONS というテーブルに保存されます。 text 型のカラムに実際のエボリューション・スクリプトが保存されています。データベースによっては 64kb のサイズ制限が text 型のカラムに付くことがあります。64kb の制限に対処する場合 — 手動でカラムの型を変えることで play_evolutions のテーブル構造を変更するか、(より好ましい方法として) 64kb よりも小さいサイズの複数のスクリプトに分割することができます。

<!--
## Running Evolutions in Production
-->
## 本番でエボリューションを実行する

<!--
The appropriate up and down scripts are run in dev mode when you click 'Apply Evolutions' in the play console. To use evolutions in PROD mode there are two things to consider.
-->
dev モードでは play コンソールで 'Apply Evolutions' をクリックする事で適切な up と down スクリプトが実行されます。 PROD モードでエボリューションを使用する場合は、考慮しなければならない事が2つあります。

<!--
If you want to apply UP evolutions automatically, you should set the system property `-DapplyEvolutions.<database>=true` or set `applyEvolutions.<database>=true` in application.conf.
-->
UP エボリューションを自動で適用したい場合、 `-DapplyEvolutions.<database>=true` をシステムプロパティに設定するか、 `applyEvolutions.<database>=true` を application.conf に設定します。
<!--
If the evolution script calculated by Play only contains UP evolutions and this property is set, then Play will apply them and start the server.
-->
もし Play によって算出されたエボリューション・スクリプトが UP エボリューションしか含んでいなく、このプロパティがセットされていた場合 Play はこれらを適用してサーバーを起動します。

<!--
If you want to run UP and DOWN evolutions automatically,  you should set the system property `-DapplyDownEvolutions.<database>=true`. It is not recommended to have this setting in your application.conf.
-->
UP と DOWN エボリューションを自動で実行したい場合 `-DapplyDownEvolutions.<database>=true` システムプロパティを設定します。この設定を application.conf に持つ事は推奨されません。
<!--
If the evolution script calculated by Play only contains DOWN evolutions and this property is NOT set, Play will NOT apply them and will NOT start the server.
-->
もし Play によって算出されたエボリューション・スクリプトが DOWN エボリューションしか持っていなく、このプロパティが設定されていない場合 Play はこれを実行せず、サーバーも起動しません。

<!--
### Evolutions and multiple hosts using Postgres or Oracle
-->
### エボリューションと Postgres もしくは Oracle を使用している複数ホスト

<!--
If your application is running on several hosts, you must set the config property evolutions.use.locks=true. If this property is set, database locks are used to ensure that only
one host applies any Evolutions. Play will create a table called PLAY_EVOLUTIONS_LOCKS which will be used with SELECT FOR UPDATE NOWAIT to perform locking.
-->
アプリケーションが複数ホストで動作している場合は evolutions.use.locks=true プロパティを設定します。このプロパティが設定されている場合、データベースロックを使って一つのホストがエボリューションを適用する事を保証します。Play はロックを実行するために SELECT FOR UPDATE NOWAIT に使われる PLAY_EVOLUTIONS_LOCKS というテーブルを作成します。

<!--
### Evolutions and multiple hosts NOT using Postgres or Oracle
-->
### エボリューションと Postgres もしくは Oracle を使用していない複数ホスト

<!--
If your application is running on several hosts, evolutions should be switched off. Multiple hosts may try to apply the evolutions scripts concurrently, with a risk of one of them failing and leaving the database in an inconsistent state.
-->
アプリケーションが複数ホストで動作している場合は、エボリューションはオフにするべきです。複数ホストがエボリューション・スクリプトを同時に実行しようとする可能性があり、片方が失敗しデータベースが不整合の状態のままになってしまう危険を伴います。
