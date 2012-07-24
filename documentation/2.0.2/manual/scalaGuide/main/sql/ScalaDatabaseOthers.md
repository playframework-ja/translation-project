<!--
# Integrating with other database libraries
-->
# データベースライブラリの利用

<!--
You can use any **SQL** database access library you like with Play, as can easily retrieve either a `Connection` or a `Datasource` from the `play.api.db.DB` helper.
-->
Play の `play.api.db.DB` ヘルパーを使って `Connection` や `Datasource` を取得すると、あらゆる **SQL** データベースアクセスライブラリを利用することができます。

<!--
## Integrating with ScalaQuery
-->
## ScalaQuery の利用

<!--
From here you can integrate any JDBC access layer that needs a JDBC data source. For example, to integrate with [[ScalaQuery | https://github.com/szeiger/scala-query]]:
-->
JDBC データソースを必要とする JDBC アクセスレイヤーを利用する方法を説明します。例えば、[[ScalaQuery | https://github.com/szeiger/scala-query]] を利用する場合は次のように書きます。

```scala
import play.api.db._
import play.api.Play.current

import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.extended.{ExtendedTable => Table}

import org.scalaquery.ql.extended.H2Driver.Implicit._ 

import org.scalaquery.session._

object Task extends Table[(Long, String, Date, Boolean)]("tasks") {
    
  lazy val database = Database.forDataSource(DB.getDataSource())
  
  def id = column[Long]("id", O PrimaryKey, O AutoInc)
  def name = column[String]("name", O NotNull)
  def dueDate = column[Date]("due_date")
  def done = column[Boolean]("done")
  def * = id ~ name ~ dueDate ~ done
  
  def findAll = database.withSession { implicit db:Session =>
      (for(t <- this) yield t.id ~ t.name).list
  }
  
}
```

<!--
## Exposing the datasource through JNDI
-->
## JNDI を経由してデータソースを公開する

<!--
Some libraries expect to retrieve the `Datasource` reference from JNDI. You can expose any Play managed datasource via JNDI by adding this configuration in `conf/application.conf`:
-->
いくつかのライブラリは `Datasource` を JNDI 経由で取得します。そのような場合、`conf/application.conf` に設定を追記して、 Play が管理しているデータソースを JNDI 経由で公開するとよいでしょう。

```
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
db.default.jndiName=DefaultDS
```

<!--
> **Next:** [[Using the Cache | ScalaCache]]
-->
> **次ページ:** [[キャッシュ | ScalaCache]]