<!-- translated -->
<!--
# Using the Ebean ORM
-->
# Ebean ORM を使う

<!--
## Configuring Ebean
-->
## Ebean の設定

<!--
Play 2.0 comes with the [[Ebean| http://www.avaje.org/]] ORM. To enable it, add the following line to `conf/application.conf`:
-->
Play 2.0 には [[Ebean| http://www.avaje.org/]] ORM が同梱されています。有効にするには以下の行を `conf/application.conf` に追加します。

```properties
ebean.default="models.*"
```

<!--
This defines a `default` Ebean server, using the `default` data source, which must be properly configured. You can actually create as many Ebean servers you need, and explicitly define the mapped class for each server.
-->
ここでは `default` データソースを使用する `default` Ebean サーバを定義していて、これは適切に設定する必要があります。 Ebean サーバは必要な分だけ作ることができ、また各サーバにマップされるクラスを明確に定義する事も出来ます。

```properties
ebean.orders="models.Order,models.OrderItem"
ebean.customers="models.Customer,models.Address"
```

<!--
In this example, we have access to two Ebean servers - each using its own database.
-->
この例では、2 つの Ebean サーバへのアクセスが設定されています。各サーバはそれぞれ異なるデータベースを使用しています。

<!--
> For more information about Ebean, see the [[Ebean documentation | http://www.avaje.org/ebean/documentation.html]].
-->
> Ebean の詳細の情報については、 [[Ebean ドキュメント | http://www.avaje.org/ebean/documentation.html]] をご覧下さい。

<!--
## Using the play.db.ebean.Model superclass
-->
## play.db.ebean.Model のスーパークラスを使う

<!--
Play 2.0 defines a convenient superclass for your Ebean model classes. Here is a typical Ebean class, mapped in Play 2.0:
-->
Play 2.0 は Ebean モデルクラス向けの便利なスーパークラスを定義しています。Play 2.0 でマップされた典型的な Ebean クラスは以下のようになります。

```java
package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity 
public class Task extends Model {

  @Id
  @Constraints.Min(10)
  public Long id;
  
  @Constraints.Required
  public String name;
  
  public boolean done;
  
  @Formats.DateTime(pattern="dd/MM/yyyy")
  public Date dueDate = new Date();
  
  public static Finder<Long,Task> find = new Finder<Long,Task>(
    Long.class, Task.class
  ); 

}
```
<!--
> Play has been designed to generate getter/setter automatically, to ensure compatibility with libraries that expect them to be available at runtime (ORM, Databinder, JSON Binder, etc). **If Play detects any user-written getter/setter in the Model, it will not generate getter/setter in order to avoid any conflict.**
-->
> 実行時に getter/setter を必要とするライブラリ (ORM、データバインダ、JSON バインダ、等) との互換性を保つため、Play はこれらを自動生成するように設計されてきました。 **もしユーザが作成した getter/setter が Model で見つかった場合、Play は競合を避けるために getter/setter を生成しません。**

<!--
As you can see, we've added a `find` static field, defining a `Finder` for an entity of type `Task` with a `Long` identifier. This helper field is then used to simplify querying our model:
-->
ご覧のように、 `find` static フィールドが追加されました。これは識別子が `Long` である `Task` 型のエンティティに対する `Finder` を定義します。このヘルパーフィールドはモデルのクエリーを簡素化するために使われます。

```
// Find all tasks
List<Task> tasks = Task.find.all();
    
// Find a task by ID
Task anyTask = Task.find.byId(34L);

// Delete a task by ID
Task.find.ref(34L).delete();

// More complex task query
List<Task> tasks = find.where()
    .ilike("name", "%coco%")
    .orderBy("dueDate asc")
    .findPagingList(25)
    .getPage(1);
```

<!--
## Transactional actions
-->
## トランザクション管理されたアクション

<!--
By default Ebean will not use transactions. However, you can use any transaction helper provided by Ebean to create a transaction. For example:
-->
デフォルトでは Ebean はトランザクションを使用しません。しかし、Ebean から提供された全てのトランザクションヘルパーを使うことができます。例えば:

<!--
:: Rob Bygrave - 
The above statement is not correct. Ebean will use implicit transactions. To demarcate transactions you have 3 options: @Transactional, TxRunnable() or a beginTransaction(), commitTransaction()
-->
:: Rob Bygrave - 
上の記述は正確ではありません。 Ebean は暗黙的なトランザクションを使用します。トランザクションの境界を引く方法は3つあります。 @Transactional、TxRunnable() または beginTransaction() と commitTransaction() です。

<!--
See http://www.avaje.org/ebean/introtrans.html for examples and an explanation.
-->
例と解説は http://www.avaje.org/ebean/introtrans.html をご覧下さい。
:: - end note

```
// run in Transactional scope...  
Ebean.execute(new TxRunnable() {  
  public void run() {  
      
    // code running in "REQUIRED" transactional scope  
    // ... as "REQUIRED" is the default TxType  
    System.out.println(Ebean.currentTransaction());  
      
    // find stuff...  
    User user = Ebean.find(User.class, 1);  
    ...  
      
    // save and delete stuff...  
    Ebean.save(user);  
    Ebean.delete(order);  
    ...  
  }  
});
```

<!--
You can also annotate your action method with `@play.db.ebean.Transactional` to compose your action method with an `Action` that will automatically manage a transaction:
-->
アクションメソッドに `@play.db.ebean.Transactional` アノテーションを付けることで、メソッドにトランザクションを自動的に管理する `Action` を組み合わせる事も出来ます。

```
@Transactional
public static Result save() {
  ...
}
```

<!--
> **Next:** [[Integrating with JPA | JavaJPA]]
-->
> **次ページ:** [[JPA の統合 | JavaJPA]]
