<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# H2 database
-->
# H2 データベース

<!--
The H2 in memory database is very convenient for development because your evolutions are run from scratch when play is restarted.  If you are using anorm you probably need it to closely mimic your planned production database.  To tell h2 that you want to mimic a particular database you add a parameter to the database url in your application.conf file, for example:
-->
H2 インメモリーデータベースは、Play が再起動した時に、evolution を 1 から実行できるため、 開発時に非常に便利です。 anorm を使用している場合は、計画している本番データベースに非常に似ている必要があります。
例えば、 application.conf ファイルでデータベース URL パラメータを追加することで、特定のデータベースを真似るように H2 に伝えることができます。

```
db.default.url="jdbc:h2:mem:play;MODE=MYSQL"
```

<!--
## Target databases
-->
## 対象となるデータベース

<table>
<tr>
<tr><td>MySql</td><td>MODE=MYSQL</td>
<td><ul><li>H2 does not have a uuid() function. You can use random_uuid() instead.  Or insert the following line into your 1.sql file: <pre><code>CREATE ALIAS UUID FOR 
"org.h2.value.ValueUuid.getNewRandom";</code></pre></li>  

<li>Text comparison in MySQL is case insensitive by default, while in H2 it is case sensitive (as in most other databases). H2 does support case insensitive text comparison, but it needs to be set separately, using SET IGNORECASE TRUE. This affects comparison using =, LIKE, REGEXP.</li></td></tr>
<tr><td>DB2</td><td>
MODE=DB2</td><td></td></tr>
<tr><td>Derby</td><td>
MODE=DERBY</td><td></td></tr>
<tr><td>HSQLDB</td><td>
MODE=HSQLDB</td><td></td></tr>
<tr><td>MS SQL</td><td>
MODE=MSSQLServer</td><td></td></tr>
<tr><td>Oracle</td><td>
MODE=Oracle</td><td></td></tr>
<tr><td>PostgreSQL</td><td>
MODE=PostgreSQL</td><td></td></tr>
</table>

<!--
## Prevent in memory DB reset
-->
## インメモリ DB のリセットを防ぐ

<!--
H2 drops your database if there no connections.  You probably don't want this to happen.  To prevent this add `DB_CLOSE_DELAY=-1` to the url (use a semicolon as a separator) eg: `jdbc:h2:mem:play;MODE=MYSQL;DB_CLOSE_DELAY=-1`
-->
H2 は接続が切れるとデータベースをドロップします。おそらくこうなることは望まないでしょう。このことを防ぐためには、 (区切り文字としてセミコロンを使用して)、 URL に `DB_CLOSE_DELAY=-1` を追加してください。 例: `jdbc:h2:mem:play;MODE=MYSQL;DB_CLOSE_DELAY=-1`

<!--
## H2 Browser
-->
## H2 ブラウザ

<!--
You can browse the contents of your database by typing `h2-browser` at the play console.  An SQL browser will run in your web browser.
-->
play コンソールで `h2-browser` とタイプすることで、データベースの内容を見ることができます。SQL ブラウザは web ブラウザで実行されます。

<!--
## H2 Documentation
-->
## H2 ドキュメント

<!--
More H2 documentation is available [from their web site](http://www.h2database.com/html/features.html)
-->
より詳細な H2 のドキュメントは、 [Web サイト](http://www.h2database.com/html/features.html) から入手できます。
