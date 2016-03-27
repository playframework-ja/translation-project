<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring the JDBC pool.
-->
# JDBC コネクションプールの設定

<!--
The Play JDBC datasource is managed by [HikariCP](https://brettwooldridge.github.io/HikariCP/). 
-->
Play の JDBC データソースの管理には、[HikariCP](https://brettwooldridge.github.io/HikariCP/) が使われています。

<!--
## Special URLs
-->
## 特別な URL

<!--
Play supports special url format for both **MySQL** and **PostgreSQL**:
-->
Play は **MySQL** と **PostgreSQL** 用に特別な URL 形式をサポートしています。

<!--
```
# To configure MySQL
db.default.url="mysql://user:password@localhost/database"

# To configure PostgreSQL
db.default.url="postgres://user:password@localhost/database"
```
-->
```
# MySQL を設定する
db.default.url="mysql://user:password@localhost/database"

# PostgreSQL を設定する
db.default.url="postgres://user:password@localhost/database"
```

<!--
A non-standard port of the database service can be specified:
-->
標準外のデータベースサービスポートを指定することもできます:

<!--
```
# To configure MySQL running in Docker
db.default.url="mysql://user:password@localhost:port/database"

# To configure PostgreSQL running in Docker
db.default.url="postgres://user:password@localhost:port/database"
```
-->
```
# Docker で実行する MySQL を設定する
db.default.url="mysql://user:password@localhost:port/database"

# Docker で実行する PostgreSQL を設定する
db.default.url="postgres://user:password@localhost:port/database"
```


<!--
## Reference
-->
## リファレンス

<!--
In addition to the classical `driver`, `url`, `username`, `password` configuration properties, it also supports additional tuning parameters if you need them.  The `play.db.prototype` configuration from the Play JDBC `reference.conf` is used as the prototype for the configuration for all database connections.  The defaults for all the available configuration options can be seen here:
-->
伝統的な `driver`, `url`, `username`, `password` 設定プロパティに加え、必要な場合は追加のチューニングパラメータも用意されています。Play JDBC の `reference.conf` にある `play.db.prototype` 設定は、すべてのデータベース接続設定のプロトタイプとして使用されます。利用できる全設定オプションのデフォルト値を以下に示します:

@[](/confs/play-jdbc/reference.conf)