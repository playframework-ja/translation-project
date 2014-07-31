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
Play provides a plugin for managing JDBC connection pools. You can configure as many databases you need.
-->
Play には JDBC コネクションプールを管理するプラグインが同梱されています。これを使って、必要なだけデータベースへの接続設定を書くことができます。

<!--
To enable the database plugin add javaJdbc in your build dependencies :
-->
ビルドの依存性に javaJdbc を追加してデータベースプラグインを有効化します :

```scala
val appDependencies = Seq(
  javaJdbc
)
```

<!--
Then you must configure a connection pool in the `conf/application.conf` file. By convention the default JDBC data source must be called `default`:
-->
その後、`conf/application.conf` ファイルでコネクションプールの設定を行う必要があります。規約によって、デフォルトの JDBC データソースは `default` でなければなりません。

```properties
# Default database configuration
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
```

<!--
To configure several data sources:
-->
複数のデータソースの設定は以下のように行われます。

```properties
# Orders database
db.orders.driver=org.h2.Driver
db.orders.url="jdbc:h2:mem:orders"

# Customers database
db.customers.driver=org.h2.Driver
db.customers.url="jdbc:h2:mem:customers"
```

<!--
If something isn’t properly configured, you will be notified directly in your browser:
-->
もし何かが適切に設定されていなければ、ブラウザから直接気付くことになります。

[[images/dbError.png]]

<!--
## Accessing the JDBC datasource
-->
## JDBC データソースの参照

<!--
The `play.db` package provides access to the configured data sources:
-->
`play.db` パッケージには、設定したデータソースを参照する方法が用意されています。

```java
import play.db.*;

DataSource ds = DB.getDatasource();
```

<!--
## Obtaining a JDBC connection
-->
## JDBC コネクションの取得

<!--
You can retrieve a JDBC connection the same way:
-->
JDBC コネクションも同じように取得できます。

```
Connection connection = DB.getConnection();
```

<!--
It is important to note that resulting Connections are not automatically disposed at the end of the request cycle. In other words, you are responsible for calling their close() method somewhere in your code so that they can be immediately returned to the pool.
-->
取得された Connection がリクエストサイクルの終わりに自動的に破棄されないことに気を付けることは重要です。別の言い方をすれば、あなたには Connection が直ちにプールに返却されるよう、コードのどこかで close() メソッドを呼び出す責任があります。

<!--
## Exposing the datasource through JNDI
-->
## JNDI にデータソースを公開する

<!--
Some libraries expect to retrieve the `Datasource` reference from JNDI. You can expose any Play managed datasource via JDNI by adding this configuration in `conf/application.conf`:
-->
一部のライブラリは JNDI からデータソースを取得する事を想定しています。 Play の管理下にあるデータソースを JNDI に公開するには、以下の設定を `conf/application.conf` に追加します。

```
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
db.default.jndiName=DefaultDS
```

<!--
## Importing a Database Driver
-->
## データベースドライバをインポートする

<!--
Other than for the h2 in-memory database, useful mostly in development mode, Play does not provide any database drivers. Consequently, to deploy in production you will have to add your database driver as an application dependency.
-->
主に開発時に有効である H2 のインメモリデータベースを除いて、 Play はデータベースドライバを何も提供していません。このため、本番環境にデプロイするには、必要なデータベースドライバを依存性として追加する必要があるでしょう。

<!--
For example, if you use MySQL5, you need to add a [[dependency| SBTDependencies]] for the connector:
-->
例えば MySQL5 を使用する場合、コネクタのために [[依存性 | SBTDependencies]] を追加する必要があります:

```
val appDependencies = Seq(
     // Add your project dependencies here,
     ...
     "mysql" % "mysql-connector-java" % "5.1.18"
     ...
)
```

<!--
> **Next:** [[Using Ebean to access your database | JavaEbean]]
-->
> **次ページ:** [[Ebean によるデータベースアクセス | JavaEbean]]