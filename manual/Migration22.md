<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Play 2.2 Migration Guide
-->
# Play 2.2 移行ガイド

<!--
This guide is for migrating to Play 2.2 from Play 2.1.  To migrate to Play 2.1, first follow the [[Play 2.1 Migration Guide|Migration21]].
-->
これは Play 2.1 から Play 2.2 へ移行するためのガイドです。 Play 2.1 へ移行する場合は、まず [[Play 2.1 移行ガイド|Migration21]] に従ってください。

<!--
## Build tasks
-->
## ビルドタスク

<!--
### Update the Play organization and version
-->
### Play 組織とバージョンの更新

<!--
Play is now published under a different organisation id.  This is so that eventually we can deploy Play to Maven Central.  The old organisation id was `play`, the new one is `com.typesafe.play`.
-->
Play はこれまでと異なる organization id の元に公開されるようになりました。これにより、ようやく Play を Maven Central に配置することができます。古い organization id は `play` で、新しい id は `com.typesafe.play` です。

<!--
The version also must be updated to 2.2.0.
-->
バージョンも 2.2.0 にアップデートする必要があります。

<!--
In `project/plugins.sbt`, update the Play plugin to use the new organisation id:
-->
新しい organization id を使って `project/plugins.sbt` 中の Play プラグインを更新してください:

```scala
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.0")
```

<!--
In addition, if you have any other dependencies on Play artifacts, and you are not using the helpers to depend on them, you may have to update the organisation and version numbers there.
-->
さらに、もし何か Play の artifact に依存するものがあり、ヘルパーを使わずに依存している場合、それらの organization id とバージョン番号を更新する必要があるかもしれません。

<!--
### Update SBT version
-->
### SBT バージョンの更新

<!--
`project/build.properties` is required to be updated to use sbt 0.13.0.
-->
sbt 0.13.0 を使うように `project/build.properties` を更新する必要があります。

<!--
### Update root project
-->
### root プロジェクトの更新

<!--
If you're using a multi-project build, and none of the projects has a root directory of the current directory, the root project is now determined by overriding rootProject instead of alphabetically:
-->
sbt のマルチプロジェクトビルドを使っていて、このプロジェクトの root ディレクトリを指定した project が存在しない場合、root プロジェクトはアルファベット順で決められる代わりに、rootProject で上書きして設定することができるようになりました:

```scala
override def rootProject = Some(myProject) 
```

<!--
### Update Scala version
-->
### Scala バージョンの更新

<!--
If you have set the scalaVersion (e.g. because you have a multi-project build that uses Project in addition to play.Project), you should update it to 2.10.2.
-->
(例えば play.Project に加えて、Project を使うマルチプロジェクトビルドがある場合など) 複数の scalaVersion がある場合、2.10.2 に更新してください。

<!--
### Play cache module
-->
### Play キャッシュモジュール

<!--
Play cache is now split out into its own module.  If you are using the Play cache, you will need to add this as a dependency.  For example, in `Build.scala`:
-->
Play キャッシュは独自モジュールに切り出されました。Play キャッシュを使っている場合は、依存性として追加する必要があります。例えば、次のように `Build.scala` に追加します:

```scala
val addDependencies = Seq(
  jdbc,
  cache,
  ...
)
```

<!--
Note that if you depend on plugins that depend on versions of Play prior to 2.2 then there will be a conflict within caching due to multiple caches being loaded. Update to a later plugin version or ensure that older Play versions are excluded if you see this issue.
-->
Play 2.2 以前のバージョンに依存するプラグインに依存している場合、複数のキャッシュ機能がロードされることになるため、衝突が発生することに注意してください。この問題が発生した場合は、プラグインを最新化するか、古いバージョンの Play が確実に除外されるようにしてください。

<!--
### sbt namespace no longer extended
-->
### sbt 名前空間を継承しない

<!--
The `sbt` namespace was previously extended by Play e.g. `sbt.PlayCommands.intellijCommandSettings`. This is considered bad practice and so
Play now uses its own namespace for sbt related things e.g. `play.PlayProject.intellijCommandSettings`.
-->
これまで `sbt` 名前空間は、例えば `sbt.PlayCommands.intellijCommandSettings` のように Play によって継承されていました。これはバッドプラクティスと考えられているので、
今後 Play は sbt に関するものに、例えば `play.PlayProject.intellijCommandSettings` のような独自の名前空間を使います。

<!--
## New results structure in Scala
-->
## Scala における新しい構造の Result

<!--
In order to simplify action composition and filtering, the Play results structure has been simplified.  There is now only one type of result, `SimpleResult`, where before there were `SimpleResult`, `ChunkedResult` and `AsyncResult`, plus the interfaces `Result` and `PlainResult`.  All except `SimpleResult` have been deprecated.  `Status`, a subclass of `SimpleResult`, still exists as a convenience class for building results.  In most cases, actions can still use the deprecated types, but they will get deprecation warnings.  Actions doing composition and filters however will have to switch to using `SimpleResult`.
-->
アクション合成とフィルターを簡素化するために、Play の Result 構造はシンプルになりました。以前は `SimpleResult`, `ChunkedResult` そして `AsyncResult` に加えて `Result` および `PlainResult` インタフェースがありましたが、Result の型は `SimpleResult` ただひとつになります。`SimpleResult` を除いて、すべて非推奨になりました。`SimpleResult` のサブクラスである `Status` は、Result を組み立てるために便利であるため、まだ残っています。ほとんどの場合において、アクションは非推奨となった型をまだ使うことができますが、非推奨の警告が表示されるでしょう。合成またはフィルターを行うアクションはどうにかして `SimpleResult` を使うように切り替える必要があります。

<!--
### Async actions
-->
### 非同期アクション

<!--
Previously, where you might have the following code:
-->
以前は次のように書く必要のあったところで:

```scala
def asyncAction = Action {
  Async {
    Future(someExpensiveComputation)
  }
}
```

<!--
You can now use the [`Action.async`](api/scala/index.html#play.api.mvc.ActionBuilder) builder:
-->
[`Action.async`](api/scala/index.html#play.api.mvc.ActionBuilder) ビルダを使えるようになります:

```scala
def asyncAction = Action.async {
  Future(someExpensiveComputation)
}
```

<!--
### Working with chunked results
-->
### チャンクした Result を使う

<!--
Previously the `stream` method on `Status` was used to produce chunked results.  This has been deprecated, replaced with a [`chunked`](api/scala/index.html#play.api.mvc.Results$Status) method, that makes it clear that the result is going to be chunked.  For example:
-->
これまで `Status` クラスの `stream` メソッドはチャンクされた Result を生成するために使われました。これは非推奨となり、Result がチャンクされることを明確にする [`chunked`](api/scala/index.html#play.api.mvc.Results$Status) メソッドで置き換えられます。例えば:

```scala
def cometAction = Action {
  Ok.chunked(Enumerator("a", "b", "c") &> Comet(callback = "parent.cometMessage"))
}
```

<!--
Advanced uses that created or used `ChunkedResult` directly should be replaced with code that manually sets/checks the `TransferEncoding: chunked` header, and uses the new `Results.chunk` and `Results.dechunk` enumeratees.
-->
直接 `ChunkedResult` を作ったり使ったりする高度な使い方は、手動で `TransferEncoding: chunked` ヘッダーを設定/チェックし、そして新しい `Results.chunk` と `Results.dechunk` enumeratee を使うコードで置き換えられるべきです。

<!--
### Action composition
-->
### アクション合成

<!--
We are now recommending that action composition be done using [`ActionBuilder`](api/scala/index.html#play.api.mvc.ActionBuilder) implementations for building actions.
-->
アクションを合成する際は、アクションを組み上げるために実装された [`ActionBuilder`](api/scala/index.html#play.api.mvc.ActionBuilder) を使うことをお勧めします。

<!--
Details on how to do these can be found [[here|ScalaActionsComposition]].
-->
やり方の詳細は [[ここ|ScalaActionsComposition]] で読むことができます。

<!--
### Filters
-->
### フィルタ

<!--
The iteratee produced by `EssentialAction` now produces `SimpleResult` instead of `Result`.  This means filters that needed to work with the result no longer have to unwrap `AsyncResult` into a `PlainResult`, arguably making all filters much simpler and easier to write.  Code that previously did the unwrapping can generally be replaced with a single iteratee `map` call.
-->
`EssentialAction` により生成される iteratee は、`Result` の代わりに `SimpleResult` を生成します。これは Result と動作する必要のあるフィルターは、もう `AsyncResult` を `PlainResult` にアンラップする必要がないことを意味し、間違いなくすべてのフィルターをシンプルに、そして簡単に書けるようにします。これまでアンラップを行っていたコードは、大抵の場合、`map` を呼び出すひとつの iteratee で置き換えることができます。

<!--
### play.api.http.Writeable application
-->
### play.api.http.Writeable の適用

<!--
Previously the constructor to `SimpleResult` took a `Writeable` for the type of the `Enumerator` passed to it.  Now that enumerator must be an `Array[Byte]`, and `Writeable` is only used for the `Status` convenience methods.
-->
これまで `SimpleResult` のコンストラクタは `Enumerator` 型に渡される `Writeable` を受け取りました。これからは enumerator は `Array[Byte]` でなければならず、`Writeable` は便利な `Status` メソッドのためだけに使われます。

<!--
### Tests
-->
### テスト

<!--
Previously `Helpers.route()` and similar methods returned a `Result`, which would always be an `AsyncResult`, and other methods on `Helpers` such as `status`, `header` and `contentAsString` took `Result` as a parameter.  Now `Future[SimpleResult]` is returned by `Helpers.route()`, and accepted by the extraction methods.  For many common use cases, where type inference is used to determine the types, no changes should be necessary to test code.
-->
これまで `Helpers.route()` や、その実体は常に `AsyncResult` である `Result` を返す同様のメソッド、そして `status`, `header` および `contentAsString` などの `Helpers` のメソッドは、引数に `Result` を受け取りました。これからは `Helpers.route()` から `Future[SimpleResult]` が返されて、抽出メソッドに受け入れられます。一般的なユースケースとして、型を決定するために型インタフェースが使われている箇所については、テストコードを変更する必要はありません。

<!--
## New results structure in Java
-->
## Java における新しい構造の Result

<!--
In order to simply action composition, the Java structure of results has been changed.  `AsyncResult` has been deprecated, and `SimpleResult` has been introduced, to distinguish normal results from the `AsyncResult` type.
-->
アクションの合成をシンプルにするために、Java の Result 構造 は変更されました。`AsyncResult` は非推奨になり、通常の Result を `AsyncResult` 型とはっきり区別するために `SimpleResult` が導入されました。

<!--
### Async actions
-->
### 非同期アクション

<!--
Previously, futures in async actions had to be wrapped in the `async` call.  Now actions may return either `Result` or `Promise<Result>`.  For example:
-->
これまで、非同期アクション内の future は `async` 呼び出しのなかにラップしなければなりませんでした。これからは、アクションは `Result` または `Promise<Result>` のいずれかを返します。例えば:

```java
public static Promise<Result> myAsyncAction() {
  Promise<Integer> promiseOfInt = Promise.promise(
    new Function0<Integer>() {
      public Integer apply() {
        return intensiveComputation();
      }
    }
  );
  return promiseOfInt.map(
    new Function<Integer, Result>() {
      public Result apply(Integer i) {
        return ok("Got result: " + i);
      }
    }
  );
}
```

<!--
### Action composition
-->
### アクション合成

<!--
The signature of the `call` method in `play.mvc.Action` has changed to now return `Promise<SimpleResult>`.  If nothing is done with the result, then typically the only change necessary will be to update the type signatures.
-->
`play.mvc.Action` にある `call` メソッドのシグネチャは `Promise<SimpleResult>` を返すように変更されました。Result に対してなにもしていない場合に必要となる変更は、通常は型のシグネチャを更新することだけです。

<!--
## Iteratees execution contexts
-->
## コンテキストを実行する Iteratee

<!--
Iteratees, enumeratees and enumerators that execute application supplied code now require an implicit execution context.  For example:
-->
アプリケーションが提供するコードを実行する iteratee, enumeratee そして enumerator は、暗黙のコンテキストを要求するようになります。例えば:

```scala
import play.api.libs.concurrent.Execution.Implicits._

Iteratee.foreach[String] { msg =>
  println(msg)
}
```

<!--
## Concurrent F.Promise execution
-->
## F.Promise の並列実行

<!--
The way that the [`F.Promise`](api/java/play/libs/F.Promise.html) class executes user-supplied code has changed in Play 2.2.
-->
ユーザより提供されたコードを [`F.Promise`](api/java/play/libs/F.Promise.html) クラスが実行する方法は、Play 2.2 で変更されました。

<!--
In Play 2.1, the `F.Promise` class restricted how user code was executed. Promise operations for a given HTTP request would execute in the order that they were submitted, essentially running sequentially.
-->
Play 2.1 の `F.Promise` は、ユーザーコードの実行に制限を加えていました。与えられた HTTP リクエストに対する Promise の操作は、登録された順番に、本質的にはシーケンシャルに実行されるものでした。

<!--
With Play 2.2, this restriction on ordering has been removed so that promise operations can execute concurrently. Work executed by the `F.Promise` class now uses [[Play's default thread pool|ThreadPools]] without placing any additional restrictions on execution.
-->
Play 2.2 において、この順序性の制限は取り除かれ、promise の操作は平行して実行できます。`F.Promise` クラスによって実行される作業は、実行に一切の制限を加えること無しに [[Play のデフォルトスレッドプール|ThreadPools]] を使うようになります。

<!--
However, for those who still want it, Play 2.1's legacy behavior has been captured in the `OrderedExecutionContext` class. The legacy behavior of Play 2.1 can be easily recreated by supplying an `OrderedExecutionContext` as an argument to any of `F.Promise`'s methods.
-->
一方、いまだに Play 2.1 の古めかしい振る舞いは、それを望むひとのために `OrderedExecutionContext` クラスに保持されています。Play 2.1 の古めかしい振る舞いは、`F.Promise` のあらゆるメソッド引数に `OrderedExecutionContext` を渡すことで、簡単に再現することができます。

<!--
The following code shows how to recreate Play 2.1's behaviour in Play 2.2. Note that this example uses the same settings as Play 2.1: a pool of 64 actors running within Play's default `ActorSystem`.
-->
以下に示すのは、Play 2.2 で Play 2.1 の振る舞いを再現するコードです。この例は Play 2.1 と同様の設定を使うことに注意してください。64 個のアクターのプールは Play デフォルトの `ActorSystem` 内で実行されます。

````java
import play.core.j.OrderedExecutionContext;
import play.libs.Akka;
import play.libs.F.*;
import scala.concurrent.ExecutionContext;

ExecutionContext orderedExecutionContext = new OrderedExecutionContext(Akka.system(), 64);
Promise<Double> pi = Promise.promise(new Function0<Double>() {
  Double apply() {
    return Math.PI;
  }
}, orderedExecutionContext);
Promise<Double> mappedPi = pi.map(new Function<Double, Double>() {
  Double apply(x Double) {
    return 2 * x;
  }
}, orderedExecutionContext);
````

<!--
## Jackson Json
-->
## Jackson Json
<!--
We have upgraded Jackson to version 2 which means that the package name is now `com.fasterxml.jackson.core` instead of `org.codehaus.jackson`.
-->
Jackson のバージョンを 2 にアップグレードしました。これは、パッケージ名が `org.codehaus.jackson` ではなく `com.fasterxml.jackson.core` になることを意味します。

<!--
## Preparing a distribution
-->
## 配布物の準備

<!--
The _stage_ and _dist_ tasks have been completely re-written in Play 2.2 so that they use the [Native Packager Plugin](https://github.com/sbt/sbt-native-packager). 
-->
_stage_ および _dist_ タスクは Play 2.2 で [Native Packager Plugin](https://github.com/sbt/sbt-native-packager) を使うように完全に書き換えられました。

<!--
Play distributions are no longer created in the project's `dist` folder. Instead, they are created in the project's `target` folder. 
-->
今後、Play の成果物はプロジェクトの `dist` フォルダには出力されません。その代わりに、プロジェクトの `target` フォルダに作成されます。

<!--
Another thing that has changed is the location of the Unix script that starts a Play application. Prior to 2.2 the Unix script was named `start` and it resided in the root level folder of the distribution. With 2.2 the `start` script is named as per the project's name and it resides in the distribution's `bin` folder. In addition there is now a `.bat` script available to start the Play application on Windows.
-->
その他に、Play アプリケーションを起動する Unix スクリプトの場所が変更されています。2.2 以前、この Unix スクリプトは `start` という名前で配布物フォルダの最上位層に置かれていました。2.2 では、このスクリプトはプロジェクトごとの名前を持ち、配布物の `bin` フォルダに置かれます。さらに、Windows 上で Play アプリケーションを起動することができる `.bat` スクリプトもそこに置かれます。

<!--
> Please note that the format of the arguments passed to the `start` script has changed. Please issue a `-h` on the `start` script to see the arguments now accepted.
-->
> `start` スクリプトに渡していた引数のフォーマットが変更されていることに注意してください。`start` スクリプトに `-h` を渡して、受け付けられる引数を確認してください。

<!--
Please consult the [["Starting your application in production mode"|Production]] documentation for more information on the new `stage` and `dist` tasks.
-->
新しい `stage` および `dist` タスクのより詳しい情報については、ドキュメント [["本番モードでアプリケーションを起動する"|Production]] を参考にしてください。

<!--
## Upgrade from Akka 2.1 to 2.2
-->
## Akka を 2.1 から 2.2 へアップグレード

<!--
The migration guide for upgrading from Akka 2.1 to 2.2 can be found [here](http://doc.akka.io/docs/akka/2.2.0/project/migration-guide-2.1.x-2.2.x.html).
-->
Akka を 2.1 から 2.2 へアップグレードする移行ガイドは、[ここ](http://doc.akka.io/docs/akka/2.2.0/project/migration-guide-2.1.x-2.2.x.html) にあります。
