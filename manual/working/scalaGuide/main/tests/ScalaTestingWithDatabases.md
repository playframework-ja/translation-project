<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Testing with databases
-->
# データベースを使用したテスト

<!--
While it is possible to write functional tests using [[ScalaTest|ScalaFunctionalTestingWithScalaTest]] or [[specs2|ScalaFunctionalTestingWithSpecs2]] that test database access code by starting up a full application including the database, starting up a full application is not often desirable, due to the complexity of having many more components started and running just to test one small part of your application.
-->
[[ScalaTest|ScalaFunctionalTestingWithScalaTest]] や [[specs2|ScalaFunctionalTestingWithSpecs2]] を使って、データベースを含むアプリケーション全体を起動してデータベースアクセスコードをテストするような機能テストを書くことも可能ではありますが、アプリケーションのたった一部分をテストするために多くのコンポーネントを立ち上げるという複雑性により、アプリケーション全体を立ち上げるのは通常好ましくありません。

<!--
Play provides a number of utilities for helping to test database access code that allow it to be tested with a database but in isolation from the rest of your app.  These utilities can easily be used with either ScalaTest or specs2, and can make your database tests much closer to lightweight and fast running unit tests than heavy weight and slow functional tests.
-->
Play はデータベースアクセスコードをテストする補助となる多くのユーティリティを提供しており、他の部分から独立してデータベースのみをテストできるようにしています。このようなユーティリティは ScalaTest や specs2 から容易に使用でき、データベースのテストを重くて遅い機能テストからより軽量で高速なものとすることができます。

<!--
## Using a database
-->
## データベースを使う

<!--
To connect to a database, at a minimum, you just need database driver name and the url of the database, using the [`Databases`](api/scala/play/api/db/Databases$.html) companion object.  For example, to connect to MySQL, you might use the following:
-->
データベースに接続するには、最低限、データベースドライバ名とデータベースの URL が必要で、 [`Databases`](api/scala/play/api/db/Databases$.html) コンパニオンオブジェクトを使用します。例えば、 MySQL に接続するには以下のようにします。

@[database](code/database/ScalaTestingWithDatabases.scala)

<!--
This will create a database connection pool for the MySQL `test` database running on `localhost`, with the name `default`.  The name of the database is only used internally by Play, for example, by other features such as evolutions, to load resources associated with that database.
-->
これによって、 `default` という名前の `localhost` で実行される  MySQL の `test` データベースへのコネクションプールが生成されます。データベースの名前は、例えば evolutions などの他の機能ではデータベースに関連づけられたリソースをロードするなど、Play の内部でのみ使用されるものです。

<!--
You may want to specify other configuration for the database, including a custom name, or configuration properties such as usernames, passwords and the various connection pool configuration items that Play supports, by supplying a custom name parameter and/or a custom config parameter:
-->
独自の name や 、 username や password、Play がサポートする様々なコネクションプール設定アイテムなどの設定プロパティなど、データベースについて他にも設定したいときは、カスタム name パラメータやカスタム config パラメータを与えることができます。

@[full-config](code/database/ScalaTestingWithDatabases.scala)

<!--
After using a database, since the database is typically backed by a connection pool that holds open connections and may also have running threads, you need to shut it down.  This is done by calling the `shutdown` method:
-->
基本的に、データベースは、開いているコネクションや、ことによると実行中かもしれないスレッドを保持するコネクションプールによって支えられているので、データベースの使用後には閉じる必要があります。これは `shutdown` メソッドで行うことができます。

@[shutdown](code/database/ScalaTestingWithDatabases.scala)

<!--
Manually creating the database and shutting it down is useful if you're using a test framework that runs startup/shutdown code around each test or suite.  Otherwise it's recommended that you let Play manage the connection pool for you.
-->
手動でデータベースを生成したり閉じたりするのは、テストフレームワークを使っていて、それぞれのテストやスイートごとに立ち上げと終了を行っている場合に便利です。そうでない場合は、Play にコネクションプールを管理させるのをお勧めします。

<!--
### Allowing Play to manage the database for you
-->
### Play にデータベースを管理させる

<!--
Play also provides a `withDatabase` helper that allows you to supply a block of code to execute with a database connection pool managed by Play.  Play will ensure that it is correctly shutdown after the block of code finishes executing:
-->
Play は、Play によって管理されたデータベースコネクションプールを使って実行するためのコードのブロックを与えられるようにする、 `withDatabase` ヘルパーも提供しています。Play はコードブロックの実行後にコネクションプールが適切に閉じられるのを保証します。

@[with-database](code/database/ScalaTestingWithDatabases.scala)

<!--
Like the `Database.apply` factory method, `withDatabase` also allows you to pass a custom `name` and `config` map if you please.
-->
`Database.apply` ファクトリメソッドと同様に、 `withDatabase` に対しても必要に応じて独自の `name` や `config` マップを渡すことができます。

<!--
Typically, using `withDatabase` directly from every test is an excessive amount of boilerplate code.  It is recommended that you create your own helper to remove this boiler plate that your test uses.  For example:
-->
特に、それぞれのテストで `withDatabase` を直接使うとボイラープレートコードがかなりの量になるので、独自のヘルパーを定義し、使用されるボイラープレートを除去するのをおすすめします。

@[custom-with-database](code/database/ScalaTestingWithDatabases.scala)

<!--
Then it can be easily used in each test with minimal boilerplate:
-->
すると、それぞれのテストで最小限のボイラープレートで簡単に使用できます。

@[custom-with-database-use](code/database/ScalaTestingWithDatabases.scala)

<!--
> **Tip:** You can use this to externalise your test database configuration, using environment variables or system properties to configure what database to use and how to connect to it.  This allows for maximum flexibility for developers to have their own environments set up the way they please, as well as for CI systems that provide particular environments that may differ to development.
-->
> **Tip:** これを使用してテストデータベースの設定を外部化できます (環境変数やシステムプロパティをどのデータベースを使用しどのように接続するかの設定に使用します)。これにより、開発者に対しては好みの方法でそれぞれの環境を構築できるようにする最大の柔軟性を、そして同様に CI システムに対しても 開発環境とは異なりうる特定の環境を提供することができます。

<!--
### Using an in-memory database
-->
### インメモリデータベースを使用する

<!--
Some people prefer not to require infrastructure such as databases to be installed in order to run tests.  Play provides simple helpers to create an H2 in-memory database for these purposes:
-->
テストを実行するためにデータベースのようなインフラストラクチャを求めたくない人もいます。Play はこれらの目的で H2 インメモリデータベースを作成するための単純なヘルパーを提供しています。

@[in-memory](code/database/ScalaTestingWithDatabases.scala)

<!--
The in-memory database can be configured, by supplying a custom name, custom URL arguments, and custom connection pool configuration.  The following shows supplying the `MODE` argument to tell H2 to emulate `MySQL`, as well as configuring the connection pool to log all statements:
-->
インメモリデータベースは、カスタム name やカスタム URL 引数、カスタムコネクションプール設定を提供するとこで設定可能です。以下では、H2 に `MySQL` をエミュレートさせるために `MODE` 引数を与えつつ、すべてのステートメントをログに出力するためにコネクションプールを設定しています。

@[in-memory-full-config](code/database/ScalaTestingWithDatabases.scala)

<!--
As with the generic database factory, ensure you always shut the in-memory database connection pool down:
-->
一般的なデータベースファクトリと同様、インメモリデータベースのコネクションプールを確実に閉じます。

@[in-memory-shutdown](code/database/ScalaTestingWithDatabases.scala)

<!--
If you're not using a test frameworks before/after capabilities, you may want Play to manage the in-memory database lifecycle for you, this is straightforward using `withInMemory`:
-->
テストフレームワークの before/after 機能を使用していない場合に、Playにインメモリデータベースのライフサイクルを管理させたいことがあるかもしれません。その場合は直接 `withInMemory` を使用してください。

@[with-in-memory](code/database/ScalaTestingWithDatabases.scala)

<!--
Like `withDatabase`, it is recommended that to reduce boilerplate code, you create your own method that wraps the `withInMemory` call:
-->
`withDatabase` と同様、ボイラープレートコードを削減することをお勧めしますが、そうする場合は、 `withInMemory` 呼び出しをラップする独自のメソッドを作成します。

@[with-in-memory-custom](code/database/ScalaTestingWithDatabases.scala)

<!--
## Applying evolutions
-->
## evolutions の適用

<!--
When running tests, you will typically want your database schema managed for your database.  If you're already using evolutions, it will often make sense to reuse the same evolutions that you use in development and production in your tests.  You may also want to create custom evolutions just for testing.  Play provides some convenient helpers to apply and manage evolutions without having to run a whole Play application.
-->
テストを実行する際には、よくデータベーススキーマをデータベースのために管理したいことがあります。evolutions をすでに使用している場合は、テストでも開発環境や本番環境と同じ evolutions を使用することにはしばしば意味があります。テストのためだけに独自の evolutions を作りたくなるかもしれません。Play は、Play アプリケーション全体を起動することなく evolutions を適用、管理する便利なヘルパーをいくつか提供しています。

<!--
To apply evolutions, you can use `applyEvolutions` from the [`Evolutions`](api/scala/play/api/db/evolutions/Evolutions$.html) companion object:
-->
evolutions を適用するには、 [`Evolutions`](api/scala/play/api/db/evolutions/Evolutions$.html) コンパニオンオブジェクトから `applyEvolutions` を使用できます。

@[apply-evolutions](code/database/ScalaTestingWithDatabases.scala)

<!--
This will load the evolutions from the classpath in the `evolutions/<databasename>` directory, and apply them.
-->
これは `evolutions/<databasename>` ディレクトリ内のクラスパスから evolutions をロードし、適用します。

<!--
After a test has run, you may want to reset the database to its original state.  If you have implemented your evolutions down scripts in such a way that they will drop all the database tables, you can do this simply by calling the `cleanupEvolutions` method:
-->
テストが実行されたら、データベースを元の状態にリセットしたいこともあるでしょう。すべてのデータベーステーブルを削除するように evolutions の down スクリプトを実装してあれば、 単純に`cleanupEvolutions` メソッドを呼ぶことでこれを実現できます。

@[cleanup-evolutions](code/database/ScalaTestingWithDatabases.scala)

<!--
### Custom evolutions
-->
### カスタム evolutions

<!--
In some situations you may want to run some custom evolutions in your tests.  Custom evolutions can be used by using a custom [`EvolutionsReader`](api/scala/play/api/db/evolutions/EvolutionsReader.html).  The simplest of these is the [`SimpleEvolutionsReader`](api/scala/play/api/db/evolutions/SimpleEvolutionsReader.html), which is an evolutions reader that takes a preconfigured map of database names to sequences of [`Evolution`](api/scala/play/api/db/evolutions/Evolution.html) scripts, and can be constructed using the convenient methods on the [`SimpleEvolutionsReader`](api/scala/play/api/db/evolutions/SimpleEvolutionsReader$.html) companion object.  For example:
-->
テストでカスタム evolutions を実行したい場合があるかもしれません。カスタム evolutions はカスタム [`EvolutionsReader`](api/scala/play/api/db/evolutions/EvolutionsReader.html) を使用することで行うことができます。最も簡単なのは [`SimpleEvolutionsReader`](api/scala/play/api/db/evolutions/SimpleEvolutionsReader.html) です。これは evolutions のリーダーであり、データベース名を [`Evolution`](api/scala/play/api/db/evolutions/Evolution.html) スクリプトのシーケンスに対して事前に設定したマップをとり、 [`SimpleEvolutionsReader`](api/scala/play/api/db/evolutions/SimpleEvolutionsReader$.html) コンパニオンオブジェクトに定義された便利なメソッドを使用して生成されます。例：

@[apply-evolutions-simple](code/database/ScalaTestingWithDatabases.scala)

<!--
Cleaning up custom evolutions is done in the same way as cleaning up regular evolutions, using the `cleanupEvolutions` method:
-->
カスタム evolutions のクリーンアップは、 `cleanupEvolutions` メソッドを使用することで、通常の evolutions のクリーンアップと同じ方法で行うことができます。

@[cleanup-evolutions-simple](code/database/ScalaTestingWithDatabases.scala)

<!--
Note though that you don't need to pass the custom evolutions reader here, this is because the state of the evolutions is stored in the database, including the down scripts which will be used to tear down the database.
-->
しかし、ここではカスタム evolutions リーダーを渡す必要がないことに注意してください。というのも、(データベースを解体するのに使われる down スクリプトを含む) evolutions の状態はデータベースに保持されるからです。

<!--
Sometimes it will be impractical to put your custom evolution scripts in code.  If this is the case, you can put them in the test resources directory, under a custom path using the [`ClassLoaderEvolutionsReader`](api/scala/play/api/db/evolutions/ClassLoaderEvolutionsReader.html).  For example:
-->
カスタム evolutions スクリプトをコードに埋め込みたいという特殊な事例があります。その場合は、それらをテストリソースディレクトリに配置し、 [`ClassLoaderEvolutionsReader`](api/scala/play/api/db/evolutions/ClassLoaderEvolutionsReader.html) を使用したカスタムパスに配置します。例：

@[apply-evolutions-custom-path](code/database/ScalaTestingWithDatabases.scala)

<!--
This will load evolutions, in the same structure and format as is done for development and production, from `testdatabase/evolutions/<databasename>/<n>.sql`.
-->
これは、`testdatabase/evolutions/<databasename>/<n>.sql` から、開発環境や本番環境で行われるのと同様の構造・フォーマットで evolutions をロードします。

<!--
### Allowing Play to manage evolutions
-->
### Play に evolutions の管理をさせる

<!--
The `applyEvolutions` and `cleanupEvolutions` methods are useful if you're using a test framework to manage running the evolutions before and after a test.  Play also provides a convenient `withEvolutions` method to manage it for you, if this lighter weight approach is desired:
-->
`applyEvolutions` や `cleanupEvolutions` メソッドは、テストフレームワークを使用してテストの前後に evolutions を実行するのを管理している場合に便利です。Play は `withEvolutions` という、もっと簡易なアプローチが望ましい場合に便利なメソッドも用意しています。

@[with-evolutions](code/database/ScalaTestingWithDatabases.scala)

<!--
Naturally, `withEvolutions` can be combined with `withDatabase` or `withInMemory` to reduce boilerplate code, allowing you to define a function that both instantiates the database and runs evolutions for you:
-->
もちろん、 `withEvolutions` はボイラープレートを減らすために `withDatabase` や `withInMemory` と自然に結合できるので、データベースを初期化し evolutions を実行することができる関数を定義することができます。

@[with-evolutions-custom](code/database/ScalaTestingWithDatabases.scala)

<!--
Having defined the custom database management method for our tests, we can now use them in a straight forward manner:
-->
テスト用のカスタムデータベース管理メソッドを定義したので、これらを以下の様な簡単な形で使用できます。

@[with-evolutions-custom-use](code/database/ScalaTestingWithDatabases.scala)
