<!-- translated -->
<!-- # A first iteration for the data model -->
# はじめてのモデル

<!-- Here we will start to write the model for our blog engine. -->
ここから、タスク管理システムのモデルを書いていきます。

<!-- ## Introduction to Ebean -->
## Ebean 概論

<!-- The model layer has a central position in a play application (and in fact in all well designed applications).  It is the demain-specific representation of the information on which the application operates.  As we want to create a task management system, the model layer will contain classes like `User`, `Project` and `Task`. -->
モデル層は、Play アプリケーション (そして、実際のところはよくデザインされたすべてのアプリケーション) において中心的な位置を占めます。モデルは、アプリケーションが操作する情報のドメインに特化した表現です。ここではタスク管理システムを作りたいので、モデル層は `User`, `Project` そして `Task` といったクラスを含むことになるでしょう。

<!-- Because most model objects need to survive between application restarts, we have to save them in a persistent datastore. A common choice is to use a relational database.  But because Java is an object oriented language, we will use an **Object Relational Mapper** to help reduce the impedance mismatch. -->
モデルオブジェクトはアプリケーションを再起動する間も存続する必要があるので、これを永続化データストアに保存しなければなりません。一般的にはリレーショナルデータベースを使うことを選択します。しかしながら Java はオブジェクト指向言語なので、インピーダンスミスマッチの減少を手助けする **オブジェクト-リレーショナルマッピングツール** を使用します。

<!-- > Though Play does come with support for relational databases out of the box, there is nothing stopping you from using Play framework with a NoSQL database, in fact, this is a very common way to implement models in Play framework. However we will use a relational database for this tutorial. -->
> Play にはリレーショナルデータベースのサポートが組み込まれていますが、Play framework を NoSQL データベースと共に使用することを妨げるものは何もありません。実際、 NoSQL を使用することは Play framework においてモデルを実装するとても一般的な方法です。しかしながら、このチュートリアルではリレーショナルデータベースを使います。

<!-- Ebean is a Java ORM library that aims to implement a very simple interface to mapping Java objects to the database.  It uses JPA annotations for mapping classes to tables, but if you have had experience with JPA before, Ebean differs in that it is sessionless.  This can greatly simplify the way you interact with the database, removing many of the surprises of things being done at odd times, such as session flushing, and errors with regards to stale or detached objects, that can occur when using JPA. -->
Ebean は Java の ORM ライブラリで、とてもシンプルなインタフェースでオブジェクトとデータベースのマッピングを行えるような実装を目指しています。Ebean はクラスをテーブルにマッピングするために JPA アノテーションを使用しますが、もし JPA を以前に使ったことがあるならば、Ebean はセッションレスであるという点で JPA とは異なることが分かるでしょう。このことは、JPA を使っているとちょっとした時に起こり得る、セッションのフラッシュや、失効または切断されたオブジェクトに関わるエラーのような多くの驚きを排除し、データベースとのやり取りをとてもシンプルにします。

<!-- ## Starting with the User class -->
## User クラス

<!-- We will start to code ZenTasks by creating the `User` class.  Create a new file called `app/models/User.java`, and declare a first implementation of the `User` class: -->
`User` クラスを作成するところから ZenTasks のコーディングを始めましょう。新しく `app/models/User.java` ファイルを作成し、`User` クラスの最初の実装を定義します:

```java
package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

@Entity
public class User extends Model {

    @Id
    public String email;
    public String name;
    public String password;
    
    public User(String email, String name, String password) {
      this.email = email;
      this.name = name;
      this.password = password;
    }

    public static Finder<String,User> find = new Finder<String,User>(
        String.class, User.class
    ); 
}
```

<!-- The `@Entity` annotation marks this class as a managed Ebean entity, and the `Model` superclass automatically provides a set of useful JPA helpers that we will discover later.  All fields of this class will be automatically persisted to the database. -->
`@Entity` アノテーションは、このクラスが管理された Ebean エンティティであることを印付けし、 `Model` スーパークラスは後述する便利な JPA ヘルパを自動的に提供します。このクラスのすべてのフィールドは、自動的にデータベースに永続化されます。

<!-- > It's not required that your model objects extend the `play.db.ebean.Model` class.  You can use plain Ebean as well. But extending this class is a good choice in most cases as it will make a lot of the Ebean stuff easier. -->
> モデルオブジェクトは `play.db.ebean.Model` クラスを継承しなければならないわけではありません。素の Ebean を使うこともできます。しかし、このクラスは Ebean 周りの多くの部分を簡易にするので、多くのケースにおいて、これを継承するのは良い選択です。

<!-- If you have used JPA before, you know that every JPA entity must provide an `@Id` property.  In this case, we are choosing `email` to be the id field. -->
もし以前に JPA を使ったことがあるなら、すべての JPA エンティティは `@Id` プロパティを提供しなければならないことを知っているでしょう。ここでは、`email` を id フィールドに選択しています。

<!-- The `find` field will be used to programatically make queries, which we will see later. -->
後で紹介する `find` フィールドは、プログラムでクエリを組み上げるために使用されます。

<!-- Now if you're a Java developer with any experience at all, warning sirens are probably clanging like mad at the sight of a public variable.  In Java (as in other object-oriented languages), best practice says to make all fields private and provide accessors and mutators.  This is to promote encapsulation, a concept critical to object oriented design.  In fact, play takes care of that for you and automatically generates getters and setters while preserving encapsulation; we will see how it works later in this tutorial. -->
どのような経験があろうと、Java 開発者であれば public 変数を目にした途端に警告ベルがじゃんじゃん鳴り出すかもしれません。Java においては (他のオブジェクト指向言語と同様に) すべてのフィールドを private にして、アクセサとミューテータを提供するのを最も良い習慣としています。これは、オブジェクト指向デザインにおいて重要な概念であるカプセル化を促進するためのものです。実際のところ、Play はこれに対応しており、getter と setter を自動生成してカプセル化を保護します; これがどのようにして動作するのかについては、このチュートリアルの後半で紹介します。

<!-- You can now refresh the application homepage.  This time you should see something different: -->
これでアプリケーションのホームページをリフレッシュすることができます。今回は、これまでとは違うものを目にするでしょう:

[[images/evolution.png]]

<!-- Play has automatically detected that you've added a new model, and has generated a new **evolution** for it.  An evolution is an SQL script that migrates the database schema from one state to the next in your application.  In our case, our database state is empty, and Play has generated scripts that create the tables.  For now during the early stages of development, we will let Play to continue to generate these scripts for us.  Later on in the project lifecycle, you will switch to writing them yourself.  Each time you see this message, you can safe click the apply button. -->
Play は新しいモデルが追加されたことを自動的に検知し、そのための **evolution** を生成しました。evolution は、アプリケーションにおけるデータベーススキーマをある状態から次の状態へ移行する SQL スクリプトです。今回の例では、データベースは空の状態であり、Play はテーブルを作成するスクリプトを生成します。これから開発の初期段階の間は、Play にこれらのスクリプトを生成し続けてもらうことになるでしょう。開発プロジェクトの後半には、これらのスクリプトを Play に代わって自分で書いていくことになります。そのどの場合でもこのメッセージが表示されるので、安全に apply ボタンを押すことができます。

<!-- > If you don't want to have to worry about applying evolutions each time you restart play, you can disable this prompting by adding the argument `-DapplyEvolutions.default=true` when you run the `play` command. -->
> play を再起動するするたびに evolution を適用する心配に気を揉みたくない場合は、`play` コマンド実行時に `-DapplyEvolutions.default=true` 引数を追加することで、このプロンプトを無効にすることができます。

<!-- ## Writing the first test -->
## はじめてのテストの作成

<!-- A good way to test the newly created `User` class is to write a JUnit test case.  It will allow you to incrementally complete the application model and ensure that all is fine. -->
新規に作成した User クラスをテストする良い方法は、JUnit テストケースを書くことです。これによりアプリケーションをくり返し仕上げ、すべてがばっちりであることを確信できるようになります。

<!-- Create a new file called `test/models/ModelsTest.java`.  We will start off by setting up the application, with an in memory database, ready to write and run our tests: -->
`test/models/ModelsTest.java` という新しいファイルを作成してください。テストを作成し、実行する準備を整えるため、インメモリデータベースを使用するようアプリケーションを設定することから始めます:

```java
package models;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }
}
```

<!-- We have extended the `WithApplication` class.  This is optional, it provides the `start()` method that allows us to easily start a fake application, and it automatically cleans it up after each test has run.  You could manage these yourself, but we are going to let Play manage it for us.  -->
`WithApplication` クラスを継承しました。これは必須ではありませんが、このクラスは、簡単にフェイクアプリケーションを起動し、さらにテストを実行するたびにアプリケーションを自動的にクリアする、`start()` メソッドを提供します。

<!-- We have also implemented a `@Before` annotated method.  This annotation means that this method will be run before each test.  In our case we are starting a new `FakeApplication`, and configuring this application to use a new in memory database.  Because we are using an in memory database, we don't need to worry about clearing the database before each test, since a new clean database is created for us before each test. -->
また、`@Before` で注釈したメソッドも実装しました。このアノテーションは、このメソッドがそれぞれのテストの前に実行されることを意味します。今回の例の場合、新しい `FakeApplication` を起動して、アプリケーションが新しいインメモリデータベースを使用するよう設定しています。インメモリデータベースを使用するので、それぞれのテストの前に新しいまっさらのデータベースが作成されますから、テスト毎にデータベースをクリアすることに気を揉む必要はありません。

<!-- Now we will write our first test, which is just going to check that we can insert a row, and retrieve it again: -->
いよいよ、行を追加すること、そしてそれを再び検索できることを確認するだけの最初のテストを書いていきます:

```java
    @Test
    public void createAndRetrieveUser() {
        new User("bob@gmail.com", "Bob", "secret").save();
        User bob = User.find.where().eq("email", "bob@gmail.com").findUnique();
        assertNotNull(bob);
        assertEquals("Bob", bob.name);
    }
```

<!-- You can see that we have programatically created a query using the `User.find` finder, to find a unqiue instance where `email` is equal to Bob's email address. -->
`email` が Bob のメールアドレスと一致する唯一のインスタンスを見つけるために、`User.find` ファインダを使ってプログラムでクエリを組み立てているのが分かります。

<!-- To run this test case, make sure that you have stopped the running application by pressing Ctrl+D in the play console, and then run `test`.  The test should pass. -->
このテストケースを実行するには、play コンソールで Ctrl+D を押して実行中のアプリケーションを終了したことを確認してから、`test` を実行します。テストは成功するはずです。

<!-- Although we could use the find object from anywhere in our code to create queries for users, it's not good practice to spread that code all through our application.  One such query that we need is a query that will authenticate users.  In `User.java`, add the `authenticate()` method: -->
ユーザを問い合わせるこのファインダは、コードのどこででも使うことができますが、アプリケーション中にコードを散らかすのは良いプラクティスではありません。必要なのはユーザを認証するクエリです。`User.java` に `authenticate()` メソッドを追加します:

```java
    public static User authenticate(String email, String password) {
        return find.where().eq("email", email)
            .eq("password", password).findUnique();
    }
```

<!-- And now the test case: -->
これでテストケースは以下のようになります:

```java
    @Test
    public void tryAuthenticateUser() {
        new User("bob@gmail.com", "Bob", "secret").save();
        
        assertNotNull(User.authenticate("bob@gmail.com", "secret"));
        assertNull(User.authenticate("bob@gmail.com", "badpassword"));
        assertNull(User.authenticate("tom@gmail.com", "secret"));
    }
```

<!-- Each time you make a modification you can run all the tests from the play test runner to make sure you didn't break anything. -->
変更を行うたびに play テストランナーですべてのテストを実行し、なにも壊れていないことを確認することができます。

<!-- > The above authentication code stores the password in cleartext.  This is considered very bad practice, you should hash the password before storing it, and then hash it before running the query, but that is beyond the scope of this tutorial. -->
> 上記の認証コードはパスワードを平文で保存しています。これはとても悪いプラクティスで、パスワードは保存される前にハッシュ化するべきですし、問い合わせの前にもハッシュ化するべきですが、これはこのチュートリアルの範囲外です。

<!-- ## The Project class -->
## Project クラス

<!-- The `Project` class will represent projects that tasks can be a part of.  A project also has a list of members that can be assigned to tasks in the project.  Let's do a first implementation: -->
`Project` クラスは、複数のタスクがその一部となり得るプロジェクトを表現します。ひとつのプロジェクトには、そのプロジェクト内のタスクにアサインすることのできるメンバーのリストも存在します。さっそく実装してみましょう:

```java
package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Project extends Model {

    @Id
    public Long id;
    public String name;
    public String folder;
    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<User> members = new ArrayList<User>();

    public Project(String name, String folder, User owner) {
        this.name = name;
        this.folder = folder;
        this.members.add(owner);
    }

    public static Model.Finder<Long,Project> find = new Model.Finder(Long.class, Project.class);

    public static Project create(String name, String folder, String owner) {
        Project project = new Project(name, folder, User.find.ref(owner));
        project.save();
        project.saveManyToManyAssociations("members");
        return project;
    }

    public static List<Project> findInvolving(String user) {
        return find.where()
            .eq("members.email", user)
            .findList();
    }
}
```

<!-- A project has a name, a folder that it belongs to, and members.  This time you can see that we again have the `@Entity` annotation on the class, extending `Model`, `@Id` on our `id` field and `find` for running queries.  We have also declared a relation with the `User` class, declaring it as `@ManyToMany`.  This means that each user can be member of many projects, and each project can have many users. -->
あるプロジェクトには、ひとつの名前、格納されるひとつのフォルダ、そして複数のメンバーが存在します。今回もまた、クラスに `@Entity` アノテーションがあること、`Model` を継承していること、`id` フィールドに `@Id` アノテーションがあること、クエリを実行する `find` があることが分かります。`User` クラスを `@ManyToMany` として定義することで、関連も定義しました。これは、それぞれのユーザは複数のプロジェクトのメンバーになれること、そしてそれぞれのプロジェクトは複数のメンバーを保持できることを意味します。

<!-- We have also implemented a create method.  Note that the many to many `members` association has to be saved explicitly. Note also that we never actually assign the `id` property.  This is because we are going to let the database generate an id for us. -->
create メソッドも実装しました。`members` の多対多の関連は明示的に保存する必要があることに注意してください。`id` プロパティを実質的に設定していないことにも注目してください。これは、データベースに id を生成させているからです。

<!-- Finally we have implemented another query method, one that finds all projects involving a particular user.  You can see how the dot notation has been used to refer to the `email` property of `User` in the `members` list. -->
最後に、特定のユーザを含むすべてのプロジェクトを探すもうひとつのクエリメソッドを実装しました。ドット表記を使って、`members` リスト中の `User` の `email` プロパティを参照していることが分かるでしょう。

<!-- Now we will write a new test in our `ModelsTest` class to test our `Project` class and the query with it: -->
ここで、`Project` クラスとそのクエリをテストする新しいテストを `ModelsTest` クラスに書きましょう:

```java
    @Test
    public void findProjectsInvolving() {
        new User("bob@gmail.com", "Bob", "secret").save();
        new User("jane@gmail.com", "Jane", "secret").save();

        Project.create("Play 2", "play", "bob@gmail.com");
        Project.create("Play 1", "play", "jane@gmail.com");

        List<Project> results = Project.findInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Play 2", results.get(0).name);
    }
```

<!-- > **Don't forget** to import **java.util.List** or you will get a compilation error. -->
> コンパイルエラーが発生しないよう **java.util.List** のインポートを **忘れないでください** 。

<!-- ## Finish with Task -->
## 最後の Task

<!-- The last thing that we need for our model draft, and most important thing, is tasks. -->
モデル設計に必要な、最後の、そしてもっとも重要なこと、それは task です。

```java
package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Task extends Model {

    @Id
    public Long id;
    public String title;
    public boolean done = false;
    public Date dueDate;
    @ManyToOne
    public User assignedTo;
    public String folder;
    @ManyToOne
    public Project project;

    public static Model.Finder<Long,Task> find = new Model.Finder(Long.class, Task.class);

    public static List<Task> findTodoInvolving(String user) {
       return find.fetch("project").where()
                .eq("done", false)
                .eq("project.members.email", user)
           .findList();
    }

    public static Task create(Task task, Long project, String folder) {
        task.project = Project.find.ref(project);
        task.folder = folder;
        task.save();
        return task;
    }
}
```

<!-- Each task has a generated id, a title, a flag to say whether it is done or not, a date that it must be completed by, a user it is assigned to, a folder and a project.  The `assignedTo` and `project` relationships are mapped using `@ManyToOne`.  This means a task may have one user, and one project, while each user may have many tasks assigned to them, and each project may have many tasks. -->
それぞれのタスクには、生成された id とタイトル、そのタスクが完了されたか否かを示すフラグ、そのタスクが完了されるべき日付、もしアサインされていればユーザー、そしてフォルダとプロジェクトが存在します。`assignedTo` と `project` の関連は `@ManyToOne` を使ってマッピングされています。これは、ユーザはそれぞれアサインされた複数のタスクを持ち、プロジェクトはそれぞれ複数のタスクを持つ一方で、あるタスクは一人のユーザとひとつのプロジェクトを持つことを意味します。

<!-- We also have a simple query, this time finding all the todo tasks, that is, those tasks that aren't done, assigned to a particular user, and a create method. -->
すべての todo タスク、すなわちまだ完了されておらず、特定のユーザーがアサインされたタスクを探すシンプルなクエリと、create メソッドも持っています。

<!-- Let's write a test for this class as well. -->
このクラスのテストも同じように書いてみましょう。

```java
    @Test
    public void findTodoTasksInvolving() {
        User bob = new User("bob@gmail.com", "Bob", "secret");
        bob.save();

        Project project = Project.create("Play 2", "play", "bob@gmail.com");
        Task t1 = new Task();
        t1.title = "Write tutorial";
        t1.assignedTo = bob;
        t1.done = true;
        t1.save();

        Task t2 = new Task();
        t2.title = "Release next version";
        t2.project = project;
        t2.save();

        List<Task> results = Task.findTodoInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Release next version", results.get(0).title);
    }
```

<!-- ## Using Fixtures to write more complicated tests -->
## Fixture を使ったより複雑なテスト

<!-- When you start to write more complex tests, you often need a set of data to test on.  Creating and saving instances of Java classes can be quite cumbersome, for this reason, Play makes it easy to use YAML files to define Java objects, which you can then easily declare your data.  When declaring data, be sure to use the YAML `!!` type operator to specify the model class of the data that you are declaring. -->
より複雑なテストを書き始める場合、しばしばテストに使うデータセットが必要になります。Java クラスを作成して保存することはとても面倒になりがちなので、Play では Java オブジェクトを定義するために、データを簡単に宣言することのできる YAML ファイルが簡単に使えます。データを宣言するときは、定義しているデータのモデルクラスを指定するために、`!!` 型演算子 を使うことを忘れないでください。

<!-- Edit the `conf/test-data.yml` file and start to describe a User: -->
`conf/test-data.yml` ファイルを編集して User を定義してみましょう:

```yaml
- !!models.User
    email:      bob@gmail.com
    name:       Bob
    password:   secret
...
```

<!-- Notice that this object is defined as part of a root object that is a list.  We can now define more objects to be a part of that, however, our dataset is a little large, so you can download a full dataset [here](javaGuide/tutorials/zentasks/files/test-data.yml). -->
このオブジェクトが、リストであるルートオブジェクトの一部として定義されていることに注意してください。これで更に多くのオブジェクトを定義することができますが、データセットは少々大きいので、[ここ](javaGuide/tutorials/zentasks/files/test-data.yml) から完全なデータセットをダウンロードすることができます。

<!-- Now we create a test case that loads this data and runs some assertions over it: -->
それでは、このデータをロードしていくつかのアサーションを実行するテストケースを作成します。

```java
    @Test
    public void fullTest() {
        Ebean.save((List) Yaml.load("test-data.yml"));

        // Count things
        assertEquals(3, User.find.findRowCount());
        assertEquals(7, Project.find.findRowCount());
        assertEquals(5, Task.find.findRowCount());

        // Try to authenticate as users
        assertNotNull(User.authenticate("bob@example.com", "secret"));
        assertNotNull(User.authenticate("jane@example.com", "secret"));
        assertNull(User.authenticate("jeff@example.com", "badpassword"));
        assertNull(User.authenticate("tom@example.com", "secret"));

        // Find all Bob's projects
        List<Project> bobsProjects = Project.findInvolving("bob@example.com");
        assertEquals(5, bobsProjects.size());

        // Find all Bob's todo tasks
        List<Task> bobsTasks = Task.findTodoInvolving("bob@example.com");
        assertEquals(4, bobsTasks.size());
    }
```

<!-- > You may find it more convenient to load the test data in your `@Before` method, so that the test data is available for every test. -->
> テストデータが全てのテストから利用できるように、 `@Before` メソッドでテストデータをロードする方がより便利だと気づくかもしれません。

<!-- ## Save your work -->
## 作業内容の保存

<!-- We have now finished a huge part of the task management system.  Now that we have created and tested all these things, we can start to develop the web application itself. -->
ここまででタスク管理システムの大きな部分をやり終えました。これらを作成し、すべてテストしており、web アプリケーションそれ自身の開発を始めることができます。

<!-- But before continuing, it's time to save your work in git.  Open a command line and type `git status` to see the modifications made since the latest commit: -->
しかし、開発を続ける前に作業内容を git を使って保存しましょう。コマンドラインを開いて `git status` とタイプして、最後のコミットから変更された内容を確認しました:

    git status

<!-- As you can see, some new files are not under version control.  Add all the files, and commit your project. -->
ご覧の通り、いくつかの新しいファイルがバージョン管理されていません。すべてのファイルを追加してプロジェクトをコミットしてください。

    git add .
    git commit -m "The model layer is ready"

<!-- > Go to the [next part](JavaGuide3). -->
> [次章](JavaGuide3) に進みましょう
