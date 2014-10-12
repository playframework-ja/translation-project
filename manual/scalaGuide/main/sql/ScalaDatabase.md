<!--
# Accessing an SQL database
-->
# SQL データベースアクセス

<!--
## Configuring JDBC connection pools
-->
## JDBC コネクションプールの構成

<!--
Play provides a plug-in for managing JDBC connection pools. You can configure as many databases as you need.
-->
Play には JDBC コネクションプールを管理するプラグインが同梱されています。これを使って、必要なだけデータベースへの接続設定を書くことができます。


<!--
To enable the database plug-in, add jdbc in your build dependencies :
-->
DB プラグインを有効にするために、依存ライブラリに jdbc を追加しましょう：

```scala
libraryDependencies += jdbc
```

<!--
Then you must configure a connection pool in the `conf/application.conf` file. By convention, the default JDBC data source must be called `default` and the corresponding configuration properties are `db.default.driver` and `db.default.url`.
-->
そして `conf/application.conf` でコネクションプールの設定を行う必要があります。規約により、デフォルトの JDBC データソースは `default` という名前である必要があり、これに関連する設定属性名は `db.default.driver` や `db.default.url` のようになります。

If something isn’t properly configured you will be notified directly in your browser:

[[images/dbError.png]]

<!--
> **Note:** You likely need to enclose the JDBC URL configuration value with double quotes, since ':' is a reserved character in the configuration syntax.
-->
> **注意:** 設定ファイルの文法において `:` は予約文字となっているため、JDBC の URL 属性をダブルクォーテーションで囲まなければならない場合があります。

<!--
### H2 database engine connection properties
-->
### H2 データベースエンジン接続設定

```properties
# Default database configuration using H2 database engine in an in-memory mode
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
```

```properties
# Default database configuration using H2 database engine in a persistent mode
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:/path/to/db-file"
```

<!--
The details of the H2 database URLs are found from [H2 Database Engine Cheat Sheet](http://www.h2database.com/html/cheatSheet.html).
-->
H2 データベースの URL 設定に関する詳細については [H2 Database Engine Cheat Sheet](http://www.h2database.com/html/cheatSheet.html) を見てください。

<!--
### SQLite database engine connection properties
-->
### SQLite データベースエンジン接続設定

```properties
# Default database configuration using SQLite database engine
db.default.driver=org.sqlite.JDBC
db.default.url="jdbc:sqlite:/path/to/db-file"
```

<!--
### PostgreSQL database engine connection properties
-->
### PostgreSQL データベースエンジン接続設定

```properties
# Default database configuration using PostgreSQL database engine
db.default.driver=org.postgresql.Driver
db.default.url="jdbc:postgresql://database.example.com/playdb"
```

<!--
### MySQL database engine connection properties
-->
### MySQL データベースエンジン接続設定

```properties
# Default database configuration using MySQL database engine
# Connect to playdb as playdbuser
db.default.driver=com.mysql.jdbc.Driver
db.default.url="jdbc:mysql://localhost/playdb"
db.default.user=playdbuser
db.default.pass="a strong password"
```

<!--
## How to see SQL Statement in the console?
-->
## コンソールで SQL 文を確認する方法

```properties
db.default.logStatements=true
logger.com.jolbox=DEBUG // for EBean
```

<!--
## How to configure several data sources
-->
## 複数データソースを設定する方法

```properties
# Orders database
db.orders.driver=org.h2.Driver
db.orders.url="jdbc:h2:mem:orders"

# Customers database
db.customers.driver=org.h2.Driver
db.customers.url="jdbc:h2:mem:customers"
```

<!--
## Configuring the JDBC Driver
-->
JDBC ドライバの設定

<!--
Play is bundled only with an [H2](http://www.h2database.com) database driver. Consequently, to deploy in production you will need to add your database driver as a dependency.
-->
Play には [H2](http://www.h2database.com) データベースのドライバのみが同梱されています。そのため、本番環境にデプロイする際は必要なデータベースドライバを依存ライブラリに追加する必要があるでしょう。

<!--
For example, if you use MySQL5, you need to add a [[dependency | SBTDependencies]] for the connector:
-->
例えば MySQL5 を使用する場合、接続するために以下の [[依存性 | SBTDependencies]] を追加する必要があります:

```scala
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"
```

<!--
Or if the driver can't be found from repositories you can drop the driver into your project's [[unmanaged dependencies|Anatomy]] `lib` directory.
-->
あるいは Maven/Ivy2 リポジトリに必要なドライバが見つからない場合、Play プロジェクトの [[unmanaged な依存ライブラリ|Anatomy]] を配置するための場所である `lib` ディレクトリにドライバを放り込むことができます。

<!--
## Accessing the JDBC datasource
-->
## JDBC データソースの参照

<!--
The `play.api.db` package provides access to the configured data sources:
-->
`play.api.db` パッケージ（の DB オブジェクト）は設定されたデータソースにアクセスする手段を提供します。

```scala
import play.api.db._

val ds = DB.getDataSource()
```

<!--
## Obtaining a JDBC connection
-->
## JDBC コネクションの取得

<!--
There are several ways to retrieve a JDBC connection. The simplest way is:
-->
JDBC コネクションを取得する方法は何種類かあります。これは最もシンプルなやり方：

```scala
val connection = DB.getConnection()
```

Following code show you a JDBC example very simple, working with MySQL 5.*:

```scala
package controllers
import play.api.Play.current
import play.api.mvc._
import play.api.db._

object Application extends Controller {

  def index = Action {
    var outString = "Number is "
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT 9 as testkey ")
      while (rs.next()) {
        outString += rs.getString("testkey")
      }
    } finally {
      conn.close()
    }
    Ok(outString)
  }

}
```


<!--
But of course you need to call `close()` at some point on the opened connection to return it to the connection pool. Another way is to let Play manage closing the connection for you:
-->
しかし、当然ながらこの方法では取得したコネクションをコネクションプールに返却するために、必ずどこかで `close()` を呼び出さなければなりません。あなたの代わりに Play にコネクションのクローズを管理させる別のやり方があります。

```scala
// access "default" database
DB.withConnection { conn =>
  // do whatever you need with the connection
}
```

<!--
For a database other than the default:
-->
default 以外のデータベースの場合は以下のようにします。

```scala
// access "orders" database instead of "default"
DB.withConnection("orders") { conn =>
  // do whatever you need with the connection
}
```

<!--
The connection will be automatically closed at the end of the block.
-->
このコネクションはブロックの終わりで自動的にクローズされます。

<!--
> **Tip:** Each `Statement` and `ResultSet` created with this connection will be closed as well.
-->
> **Tip:** `Statement` と `ResultSet` もコネクションと一緒にクローズされます。

<!--
A variant is to set the connection's auto-commit to `false` and to manage a transaction for the block:
-->
少し違ったやり方として、コネクションの auto-commit を `false` に設定し、ブロック内をトランザクション制御する方法もあります。

```scala
DB.withTransaction { conn =>
  // do whatever you need with the connection
}
```

<!--
> **Next:** [[Using Anorm to access your database | ScalaAnorm]]
-->
> **次ページ:** [[Anorm によるデータベースアクセス | ScalaAnorm]]
