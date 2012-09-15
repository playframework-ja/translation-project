<!-- translated -->
<!--
# Integrating with JPA
-->
# JPA の統合

<!--
## Exposing the datasource through JNDI
-->
## JNDI にデータソースを公開する

<!--
JPA requires the datasource to be accessible via JNDI. You can expose any Play-managed datasource via JDNI by adding this configuration in `conf/application.conf`:
-->
JPA を使用するには JNDI 経由でデータソースをアクセス可能にする必要があります。Play で管理しているデータソースは `conf/application.conf` に以下の設定を追加する事で JNDI に公開することができます。

```
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play"
db.default.jndiName=DefaultDS
```

<!--
## Adding a JPA implementation to your project
-->
## JPA の実装をプロジェクトに追加する

<!--
There is no built-in JPA implementation in Play 2.0; you can choose any available implementation. For example, to use Hibernate, just add the dependency to your project:
-->
Play 2.0 には JPA の実装は同梱されていません。ユーザは任意の実装を選択することができます。例えば、Hibernate を使う場合はプロジェクトに依存関係を追加します。

```
val appDependencies = Seq(
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
)
```

<!--
## Creating a persistence unit
-->
## 永続性ユニットを作成する

<!--
Next you have to create a proper `persistence.xml` JPA configuration file. Put it into the `conf/META-INF` directory, so it will be properly added to your classpath.
-->
次に、適切な `persistence.xml` JPA 設定ファイルを作成する必要があります。設定ファイルを `conf/META-INF` ディレクトリに配置すると、クラスパスに適切に追加されます。

<!--
Here is a sample configuration file to use with Hibernate:
-->
以下は Hibernate を使うための設定ファイルの見本です。

```
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
             version="2.0">
             
    <persistence-unit name="defaultPersistenceUnit" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.ejb.HibernatePersistence</provider>
        <non-jta-data-source>DefaultDS</non-jta-data-source>
        <properties>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
        </properties>
    </persistence-unit>
    
</persistence>
```

<!--
## Annotating JPA actions with `@Transactional`
-->
## JPA のアクションに `@Transactional` アノテーションを付ける

<!--
Every JPA call must be done in a transaction so, to enable JPA for a particular action, annotate it with `@play.db.jpa.Transactional`. This will compose your action method with a JPA `Action` that manages the transaction for you:
-->
各 JPA 呼び出しはトランザクションの中で行われる必要があるため、JPA を特定のアクションで有効にするには、 `@play.db.jpa.Transactional` アノテーションを付けます。これにより、アクションメソッドにトランザクション管理をする JPA `Action` が組み合わされます。

```
@Transactional
public static Result index() {
  ...
}
```

<!--
If your action perfoms only queries, you can set the `readOnly` attribute to `true`:
-->
アクションがクエリーしか実行しない場合は、 `readOnly` 属性を `true` にすることができます。

```
@Transactional(readOnly=true)
public static Result index() {
  ...
}
```

<!--
## Using the `play.db.jpa.JPA` helper
-->
## `play.db.jpa.JPA` ヘルパーを使う

<!--
At any time you can retrieve the current entity manager from the `play.db.jpa.JPA` helper class:
-->
`play.db.jpa.JPA` ヘルパークラスを使う事で、任意の時に現在のエンティティ・マネージャを取得することができます。

```
public static Company findById(Long id) {
  return JPA.em().find(Company.class, id);
}
```

<!--
> **Next:** [[Using the cache | JavaCache]]
-->
> **次ページ:** [[キャッシュを使う | JavaCache]]

