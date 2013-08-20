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
Play 2.0 provides a plug-in for managing JDBC connection pools. You can configure as many databases you need.
-->
Play 2.0 には JDBC コネクションプールを管理するプラグインが同梱されています。これを使って、必要なだけデータベースへの接続設定を書くことができます。

<!-- To enable the database plug-in, configure a connection pool in the `conf/application.conf` file. By convention, the default JDBC data source must be called `default`: -->
データベースプラグインを有効にするために、`conf/application.conf` ファイルでコネクションプールの設定を行います。規約によって、デフォルトの JDBC データソースは `default` と呼ばれます:

```properties
# Default database configuration
db.default.driver=org.h2.Driver
db.default.url=jdbc:h2:mem:play
```

<!-- To configure several data sources: -->
複数のデータソースを設定する場合は、以下のようにします:

```properties
# Orders database
db.orders.driver=org.h2.Driver
db.orders.url=jdbc:h2:mem:orders

# Customers database
db.customers.driver=org.h2.Driver
db.customers.url=jdbc:h2:mem:customers
```

<!--
If something isn’t properly configured you will be notified directly in your browser:
-->
もし何かが適切に設定されていなければ、ブラウザから直接気付くことになります。

[[images/dbError.png]]

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

val ds = DB.getDatasource()
```

<!--
## Obtaining a JDBC connection
-->
## JDBC コネクションの取得

<!--
There is several ways to retrieve a JDBC connection. The first is the most simple:
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
DB.withConnection { conn =>
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
A variant is to set the connection auto-commit to `false` automatically and to manage a transaction for the block:
-->
この方法の変形として、コネクションの auto-commit を自動的に `false` にして、ブロックを 1 トランザクションとさせる方法もあります。

```scala
DB.withTransaction { conn =>
  // do whatever you need with the connection
}
```

<!-- ## Importing a Database Driver -->
## データベースドライバのインポート

<!-- Other than h2 for in-memory database, useful mostly in development mode, Play 2.0 does not provide any database driver. Consequently, to deploy in production you will need to add your database driver as a dependency. -->
Play 2.0 は、開発モードにおいてとても便利な h2 インメモリデータベース以外のデータベースドライバを提供していません。このため、本番環境にデプロイする際には、依存性にデータベースドライバを追加する必要が出てくるでしょう。

<!-- For example, if you use MySQL5, you need to add a [[dependency | SBTDependencies]] for the connector: -->
例えば MySQL5 を使う場合、このコネクタの [[依存性 | SBTDependencies]] を追加する必要があります:

```scala
val appDependencies = Seq(
     // Add your project dependencies here,
     ...
     "mysql" % "mysql-connector-java" % "5.1.18"
     ...
)
```

<!--
> **Next:** [[Using Anorm to access your database | ScalaAnorm]]
-->
> **次ページ:** [[Anorm によるデータベースアクセス | ScalaAnorm]]