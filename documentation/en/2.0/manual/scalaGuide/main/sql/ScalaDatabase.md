# Accessing an SQL database

## Configuring JDBC connection pools

Play 2.0 provides a plug-in for managing JDBC connection pools. You can configure as many databases you need.

To enable the database plug-in, configure a connection pool in the `conf/application.conf` file. By convention, the default JDBC data source must be called `default`:

```properties
# Default database configuration
db.default.driver=org.h2.Driver
db.default.url=jdbc:h2:mem:play
```

To configure several data sources:

```properties
# Orders database
db.orders.driver=org.h2.Driver
db.orders.url=jdbc:h2:mem:orders

# Customers database
db.customers.driver=org.h2.Driver
db.customers.url=jdbc:h2:mem:customers
```

If something isn’t properly configured you will be notified directly in your browser:

[[images/dbError.png]]

## Accessing the JDBC datasource

The `play.api.db` package provides access to the configured data sources:

```scala
import play.api.db._

val ds = DB.getDatasource()
```

## Obtaining a JDBC connection

There is several ways to retrieve a JDBC connection. The first is the most simple:

```scala
val connection = DB.getConnection()
```

But of course you need to call `close()` at some point on the opened connection to return it to the connection pool. Another way is to let Play manage closing the connection for you:

```scala
DB.withConnection { conn =>
  // do whatever you need with the connection
}
```

The connection will be automatically closed at the end of the block.

> **Tip:** Each `Statement` and `ResultSet` created with this connection will be closed as well.

A variant is to set the connection auto-commit to `false` automatically and to manage a transaction for the block:

```scala
DB.withTransaction { conn =>
  // do whatever you need with the connection
}
```

## Importing a Database Driver
Other than h2 for in-memory database, useful mostly in development mode, Play 2.0 does not provide any database driver. Consequently, to deploy in production you will need to add your database driver as a dependency.

For example, if you use MySQL5, you need to add a [[dependency | SBTDependencies]] for the connector:
```scala
val appDependencies = Seq(
     // Add your project dependencies here,
     ...
     "mysql" % "mysql-connector-java" % "5.1.18"
     ...
)
```

> **Next:** [[Using Anorm to access your database | ScalaAnorm]]