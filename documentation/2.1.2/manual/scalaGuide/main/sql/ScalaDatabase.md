<!-- translated -->
<!--
# Accessing an SQL database
-->
# SQL データベースアクセス

<!--
## Configuring JDBC connection pools
-->
## JDBC コネクションプールの構成

<!--
Play 2.0 provides a plug-in for managing JDBC connection pools. You can configure as many databases as you need.
-->
Play 2.0 には JDBC コネクションプールを管理するプラグインが同梱されています。これを使って、必要なだけデータベースへの接続設定を書くことができます。

<!--
To enable the database plug-in, configure a connection pool in the `conf/application.conf` file. By convention, the default JDBC data source must be called `default` and the corresponding configuration properties are `db.default.driver` and `db.default.url`.
-->
データベースプラグインを有効にするために、`conf/application.conf` ファイルでコネクションプールの設定を行います。規約によって、デフォルトの JDBC データソースは `default` と呼ばれ、関連する設定プロパティは `db.default.driver` と `db.default.url` でなければなりません。

<!--
If something isn窶冲 properly configured you will be notified directly in your browser:
-->
もし何かが適切に設定されていなければ、ブラウザから直接気付くことになります。

[[images/dbError.png]]

<!--
> **Note:** You likely need to enclose the JDBC URL configuration value with double quotes, since ':' is a reserved character in the configuration syntax.
-->
> **注意:** 設定文法で `:` は予約された文字なので、JDBC URL 設定値をダブルクォーテーションで囲まなければならないことがあるでしょう。

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
The details of the H2 database URLs are found from [[H2 Database Engine Cheat Sheet |http://www.h2database.com/html/cheatSheet.html]].
-->
H2 データベース URL の詳細は [[H2 Database Engine Cheat Sheet |http://www.h2database.com/html/cheatSheet.html]] で見つけられます。

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

## How to see SQL Statement in the console?

```properties
db.default.logStatements=true
logger.com.jolbox=DEBUG // for EBean
```

<!--
## How to configure several data sources
-->
## 複数データソースを設定するには

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
Play 2.0 is bundled only with an [[H2 | http://www.h2database.com]] database driver. Consequently, to deploy in production you will need to add your database driver as a dependency.
-->
Play 2.0 には [[H2 | http://www.h2database.com]] データベースのドライバのみが同梱されています。このため、本番環境にデプロイするには、必要なデータベースドライバを依存性として追加する必要があるでしょう。

<!--
For example, if you use MySQL5, you need to add a [[dependency | SBTDependencies]] for the connector:
-->
例えば MySQL5 を使用する場合、コネクタのために [[依存性 | SBTDependencies]] を追加する必要があります:

```scala
val appDependencies = Seq(
  "mysql" % "mysql-connector-java" % "5.1.21"
)
```

<!--
Or if the driver can't be found from repositories you can drop the driver into your project's [[unmanaged dependencies|Anatomy]] `lib` directory.
-->
リポジトリにドライバが見つからない場合は、プロジェクトの [[依存性が管理されない|Anatomy]] `lib` ディレクトリにドライバを放り込むことができます。

<!--
## Accessing the JDBC datasource
-->
## JDBC データソースの参照

<!--
The `play.api.db` package provides access to the configured data sources:
-->
`play.api.db` パッケージには、設定したデータソースを参照する方法が用意されています。

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
JDBC コネクションを取得する方法は何種類かあります。一つめの方法が最もシンプルで、次のように書きます。

```scala
val connection = DB.getConnection()
```

<!--
But of course you need to call `close()` at some point on the opened connection to return it to the connection pool. Another way is to let Play manage closing the connection for you:
-->
しかし、この方法だと、取得したコネクションをコネクションプールに返却するために、どこかの時点で `close()` を呼び出す必要があります。そこで、コネクションのクローズを Play にまかせる方法も用意されています。

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
この方法の変形として、コネクションの auto-commit を `false` にして、ブロックを 1 トランザクションとさせる方法もあります。

```scala
DB.withTransaction { conn =>
  // do whatever you need with the connection
}
```

<!--
> **Next:** [[Using Anorm to access your database | ScalaAnorm]]
-->
> **次ページ:** [[Anorm によるデータベースアクセス | ScalaAnorm]]
