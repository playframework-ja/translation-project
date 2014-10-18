<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# What's new in Play 2.1?
-->
# Play 2.1 の変更点

<!--
## Migration to Scala 2.10 
-->
## Scala 2.10 への移行

<!--
The whole runtime API for Play have been migrated to Scala 2.10 allowing your applications to take part of the new great features provided by this new language release. 
-->
Play の全てのランタイム API がScala 2.10 に移行しました。これにより、Scala 最新版に含まれる素晴らしい機能を使ってアプリケーション開発が行えるようになりました。

<!--
In the same time we have broken the dependency that was existing between the Scala version used by the _Build system_ (sbt), and the Scala version used at runtime. This way, it makes it easier to build and test Play on experimental or unstable branches of the Scala language.
-->
同時に _ビルドシステム_ (sbt) の Scala バージョンと、アプリケーション実行時に利用される Scala のバージョンを独立させました。これにより、 実験的または試験的なブランチの Scala を使ってより簡単に Play のビルドやテストを行えるようになりました。

<!--
## Migration to `scala.concurrent.Future`
-->
## `scala.concurrent.Future` への移行

<!--
One of the greatest feature provided by Scala 2.10 is the new standard `scala.concurrent.Future` library for managing asynchronous code in Scala. Play is now based itself on this API, and its great asynchronous HTTP and streaming features are now compatible directly with any other library using the same API. 
-->
Scala 2.10 で導入された目玉機能の一つに、Scala で非同期コードを扱うための `scala.concurrent.Future` というライブラリがあります。Play 自身、この API をベースに動作するようになりました。また、Play の非同期 HTTP 通信やストリーミングの機能も同 API で直接扱えるようになりました。

<!--
It makes it even simpler to use Play with Akka, or any upcoming asynchronous datastore driver that will use this new API.
-->
さらに Play と Akka 、またこれからリリースされるであろう非同期なデータストアドライバとを今まで以上に容易に組み合わせられるようになりました。

<!--
In the same time, we worked to simplify the execution context model, and by providing an easy way to choose for each part of your application, the underlying `ExecutionContext` used to run your code. 
-->
同時に、実行コンテキストのモデルがよりシンプル化されるように見直しを行った結果、アプリケーションの部分部分で非同期コードを実行する `ExecutionContext` を容易に選択できるようになりました。

<!--
## Modularization
-->
## モジュール化

<!--
The Play project has been split on many sub-projects, allowing you to select the minimal set of dependency needed for your project. 
-->
Play プロジェクトがいくつかのサブプロジェクトに分割されて、その中からユーザ自身が必要最小限のものを選択できるようになりました。

<!--
You have to select the exact set of optional dependencies your application need, in the list of:
-->
今後、アプリケーションに必要だと思われる依存プロジェクトを次のリストの中から自由に選択することができます。

<!--
- `jdbc` : The **JDBC** connection pool and the `play.api.db` API. 
- `anorm` : The **Anorm** component.
- `javaCore` : The core **Java** API.
- `javaJdbc` : The Java database API.
- `javaEbean` : The Ebean plugin for Java.
- `javaJpa` : The JPA plugin for Java.
- `filters` : A set of build-in filters for Play (such as the CSRF filter)
-->
- `jdbc` : **JDBC** コネクションプールと `play.api.db` API
- `anorm` : **Anorm**
- `javaCore` : **Java** 向けのコア API
- `javaJdbc` : Java 向けのデータベース API
- `javaEbean` : Java 向けの Ebean プラグイン
- `javaJpa` : Java 向けの JPA プラグイン
- `filters` : Play にビルトインされたフィルタ (CSRF フィルターなど)

<!--
The `play` core project has now a very limited set of external dependencies and can be used as a minimal asynchronous, high performance HTTP server without the other components.
-->
`play` コアプロジェクトには非常に限られた依存ライブラリしか含まれていません。そして、コアはそれ単体で最小構成の非同期的、かつハイパフォーマンスな HTTP サーバとして利用できます。

<!--
## Allow more modularization for your projects
-->
## アプリケーションのより柔軟なモジュール化

<!--
To allow, to compose and reuse components in your own projects further, Play 2.1 supports sub-Router composition.
-->
Play 2.1 に追加されたサブ・ルータ合成という機能のおかげで、アプリケーションの部分部分をこれまでより柔軟にモジュール化、そして再利用できるようになりました。

<!--
For example, a sub project can define its own router component, using its own namespace, such as:
-->
具体的には、サブプロジェクトとして利用されるアプリケーションには、それ独自の名前空間で、それ専用のルータを定義できます。

<!--
In `conf/my.subproject.routes`
-->
例えば、 `conf/my.subproject.routes` に次のようなルータを定義しておくとします。

```
GET   /                   my.subproject.controllers.Application.index
```

<!--
And then, you can integrate this component into your main application, by wiring the Router, such as:
-->
このルータは、メインとなるアプリケーションへ次のように組み込むことができます。

<!--
```
# The home page
GET   /                   controllers.Application.index

# Include a sub-project
->    /my-subproject      my.subproject.Routes

# The static assets
GET   /public/*file       controllers.Assets.at(file)
```
-->
```
# トップページ
GET   /                   controllers.Application.index

<!--
In the configuration, at runtime a call to the `/my-subproject` URL will eventually invoke the `my.subproject.controllers.Application.index` Action.
-->
この設定によって、アプリケーション実行時に `/my-subproject` 以下の URL へ行われたアクセスは、全て `my.subproject.controllers.Application.index` というアクションの呼び出しに変換されます。

<!--
> Note: in order to avoid name collision issues with the main application, always make sure that you define a subpackage within your controller classes that belong to a sub project (i.e. `my.subproject` in this particular example). You'll also need to make sure that the subproject's Assets controller is defined in the same name space.
-->
> Note: メインとなるアプリケーション内の名前と衝突を起こさないように、サブプロジェクトとなるアプリケーションのコントローラは専用のサブパッケージ (上記の例では `my.subproject` がそうです) 以下に定義しましょう。同様に、サブプロジェクトとなるアプリケーションの Assets コントローラも同じ名前空間に定義したほうがよいでしょう。

<!--
More information about this feature can be found at [[Working with sub-projects|SBTSubProjects]].
-->
この機能に関する詳細については [[サブプロジェクト関連のドキュメント|SBTSubProjects]] を参照してください。


<!--
## `Http.Context` propagation in the Java API
-->
## Java API 向けの `Http.Context` プロパゲーション機能

<!--
In Play 2.0, the HTTP context was lost during the asynchronous callback, since these code fragment are run on different thread than the original one handling the HTTP request.
-->
Play 2.0 では、HTTP コンテキストが非同期コールバックの過程で失われてしまっていました。これは、非同期コールバックの過程で、コードを実行スレッドが、最初に HTTP リクエストを処理し始めたスレッドとは別のスレッドへの変わってしまうからです。

<!--
<!--
Consider:
-->
次の例を考えてみましょう。
-->
次の例を考えてみましょう。

```
public static Result index() {
  return async(
    aServiceSomewhere.getData().map(new Function<String,Result>(data) {
      // Ouch! You try to access the request data in an asynchronous callback
      String user = session().get("user"); 
      return ok("Here is the result " + user + ": " + data);
    })
  );
}
```

<!--
This code wasn't working this way. For really good reason, if you think about the underlying asynchronous architecture, but yet it was really surprising for Java developers.
-->
このコードは意図したとおりには動いていませんでした。その理由は、Play の非同期処理のアーキテクチャをよく知っていればわかるといえばわかるのですが、 Java 開発者にとってはあまり馴染みのない挙動でした。

<!--
We eventually found a way to solve this problem and to propagate the `Http.Context` over a stack spanning several threads, so this code is now working this way.
-->
この問題を解決する方法、つまり `Http.Context` を複数スレッドにまたがるスタックへ伝搬していく方法が発見されたため、このコードは Play 2.1 からは意図したとおりに動くようになりました。

<!--
## Better threading model for the Java API
-->
## Java API 向けスレッドモデルの改善

<!--
While running asynchronous code over mutable data structures, chances are right that you hit some race conditions if you don't synchronize properly your code. Because Play promotes highly asynchronous and non-blocking code, and because Java data structure are mostly mutable and not thread-safe, it is the responsibility of your code to deal with the synchronization issues.
-->
排他制御を行わずに非同期コードをミュータブルなデータ構造に対して実行すると、何らかのレースコンディションに陥いる可能性が高いと思われます。Play が高度に非同期化されたノンブロッキングなコードを宣伝していることに加えて、 Java のデータ構造が大抵はミュータブルで非スレッドセーフであることを考えると、これまではアプリケーション側で排他制御の問題を解決する必要がありました。

Consider:

```
public static Result index() {

  final HashMap<String,Integer> result = new HashMap<String,Integer>();

  aService.doSomethingAsync().map(new Function<String,String>(key) {
    Integer i = result.get(key);
    if(i != null) {
      result.set(key, i++);
    }
    return key;
  });

  aService.doSomethingElse().map(new Function<String,String>(key) {
    result.remove(key);
    return null;
  });

  ...
}
```

<!--
In this code, chances are really high to hit a race condition if the 2 callbacks are run in the same time on 2 different threads, while accessing each to the share `result` HashMap. And the consequence will be probably some pseudo-deadlock in your application because of the implementation of the underlying Java HashMap.
-->
上記のコードでは、二つのコールバックが `result` という HashMap を共有しています。したがって、これらコールバックが別々のスレッドで同時に実行された場合、レースコーンディションに陥る可能性が高いと思われます。そして、これが Java の HashMap 実装と相まると、結果的にアプリケーションが擬似的なデッドロック状態に陥ってしまうことが考えられます。

<!--
To avoid any of these problems, we decided to manage the callback execution synchronization at the framework level. Play 2.1 will never run concurrently 2 callbacks for the same `Http.Context`. In this context, all callback execution will be run sequentially (while there is no guarantees that they will run on the same thread).
-->
このような問題を回避するため、コールバックの実行に必要な排他制御がフレームワークレベルで行われるようになりました。 Play 2.1 では、同じ `Http.Context` に対してふたつ以上のコールバックが同時に実行されることはもうありません。同じコンテキスト内では、全てのコールバックは順番に（ただし、同じスレッドで実行されるという保証はありません）実行されます。

<!--
## Managed Controller classes instantiation
-->
## コントローラクラスのインスタンス生成処理がカスタマイズ可能になった

<!--
By default Play binds URLs to controller methods statically, that is, no Controller instances are created by the framework and the appropriate static method is invoked depending on the given URL. In certain situations, however, you may want to manage controller creation and that’s when the new routing syntax comes handy.
-->
初期状態では、 Play は URL をコントローラのメソッドに静的にバインドします。したがって、フレームワークによってコントローラのインスタンスが生成されることはなく、単に URL に応じた適切な static メソッドが呼び出されるだけです。しかし、場合によってはコントローラの生成処理をカスタマイズしたいこともあります。ルーティングの新しいシンタックスを使うとそれが可能です。

<!--
Route definitions starting with @ will be managed by the `Global::getControllerInstance` method, so given the following route definition:
-->
@ から始まるルート定義は `Global::getControllerInstance` メソッドによって処理されます。例えば、次のようなルートを定義します。

```
GET     /                  @controllers.Application.index()
```

<!--
Play will invoke the `getControllerInstance` method which in return will provide an instance of `controllers.Application` (by default this is happening via the default constructor). Therefore, if you want to manage controller class instantiation either via a dependency injection framework or manually you can do so by overriding getControllerInstance in your application’s Global class.
-->
この場合、 Play によって呼び出された `getControllerInstance` メソッドが `controllers.Application` のインスタンス(初期状態ではデフォルトコンストラクタによって生成されます)を返します。したがって、 DI フレームワークや独自のロジックによってコントローラクラスのインスタンス生成を行いたい場合、アプリケーションの Global クラスにおいて getControllerInstance メソッドをオーバーライドするとよいでしょう。

<!--
As this example [demonstrates it](https://github.com/guillaumebort/play20-spring-demo), it allows to wire any dependency injection framework such as __Spring__ into your Play application.
-->
この [デモアプリケーション](https://github.com/guillaumebort/play20-spring-demo) のように、 __Spring__ を始めとするあらゆる DI フレームワークを Play アプリケーションに組み込むことができます。

<!--
## New Scala JSON API
-->
## Scala 向けの新しい JSON API

<!--
The new Scala JSON API provide great features such as transformation and validation of JSON tree. Check the new documentation at the [[Scala Json combinators document|ScalaJsonCombinators]].
-->
Scala 向けの新しい JSON API によって、JSON ツリーの変換やバリデーションなどの便利な機能が実現できました。詳細については [[Scala JSON コンビネータのドキュメント|ScalaJsonCombinators]] を参照してください。

<!--
## New Filter API and CSRF protection
-->
## 新たなフィルタ API と CSRF プロテクション機能

<!--
Play 2.1 provides a new and really powerful filter API allowing to intercept each part of the HTTP request or response, in a fully non-blocking way.
-->
Play 2.1 では強力なフィルタ API が追加され、完全にノンブロッキングな方法で HTTP リクエストや HTTP レスポンスの部分をインターセプトできるようになりました。

<!--
For that, we introduced a new new simpler type replacing the old `Action[A]` type, called `EssentialAction` which is defined as:
-->
この API を実現するため、既存の `Action[A]` という型の代替えとして、 `EssentialAction` という次のようなシンプルな型が追加されました。

```
RequestHeader => Iteratee[Array[Byte], Result]
```

<!--
As a result, a filter is simply defined as: 
-->
これを利用すると、フィルタは次のように定義できます。

```
EssentialAction => EssentialAction
```

<!--
> __Note__: The old `Action[A]` type is still available for compatibility.
-->
> __Note__: 既存の `Action[A]` という型は互換性のために残されています。

<!--
The `filters` project that is part of the standard Play distribution contain a set of standard filter, such as the `CSRF` providing automatic token management against the CSRF security issue. 
-->
Play のディストリビューションに含まれる　`filters` プロジェクトには、標準的なフィルタがいくつか用意されています。例えば、 `CSRF` フィルタには CSRF 対策のために自動的にトークン管理を行う機能があります。

## RequireJS

<!--
In play 2.0 the default behavior for Javascript was to use google closure's commonJS module support. In 2.1 this was changed to use [requireJS](http://requirejs.org/) instead.
-->
Play 2.0 では、JavaScript は Google Closure の CommonJS モジュールサポートによって処理されていました。Play 2.1 からは、代わりに [RequireJS](http://requirejs.org/) が使われるようになりました。

<!--
What this means in practice is that by default Play will only minify and combine files in stage, dist, start modes only. In dev mode Play will resolve dependencies client side.
-->
実利用においてこれが意味するのは、特に何か設定しない限り、 Play は stage 、 dist 、 start 時のみ JavaScript を minify するということです。開発モード時は、クライアント側で JavaScript モジュール間の依存性が解決されます。

<!--
If you wish to use this feature, you will need to add your modules to the settings block of your build file:
-->
この機能を利用する場合は、ビルドファイル内の設定に RequireJS で管理したいモジュールを追加する必要があります。

```
requireJs := "main.js"
```

<!--
More information about this feature can be found on the [[RequireJS documentation page|RequireJS-support]].
-->
この機能に関する詳細については、 [[RequireJS 関連のドキュメント|RequireJS-support]] を参照してください。

<!--
## Content negotiation
-->
## コンテンツネゴシエーション

<!--
The support of content negotiation has been improved, for example now controllers automatically choose the most appropriate lang according to the quality values set in the `Accept-Language` request header value.
-->
コンテンツネゴシエーションのサポートが改善され、コントローラにおいてレスポンスに最適な言語が `Accent-Language` リクエストヘッダ値の quality 値に基いて自動的に選択されるようになりました。

<!--
It is also easier to write Web Services supporting several representations of a same resource and automatically choosing the best according to the `Accept` request header value:
-->
加えて、同じリソースについて複数のレスポンス形式をサポートするような Web サービスを開発することが容易になりました。例えば、次のように記述すると、`Accept` リクエストヘッダ値に基いて適切なレスポンスの形式を選択することができます。

```
val list = Action { implicit request =>
  val items = Item.findAll
  render {
    case Accepts.Html() => Ok(views.html.list(items))
    case Accepts.Json() => Ok(Json.toJson(items))
  }
}
```

<!--
More information can be found on the content negotiation documentation pages for [[Scala|ScalaContentNegotiation]] and [[Java|JavaContentNegotiation]].
-->
この機能についての詳細はコンテンツネゴシエーションに関するドキュメントの [[Scala 版|ScalaContentNegotiation]] および [[Java 版|JavaContentNegotiation]] を参照してください。
