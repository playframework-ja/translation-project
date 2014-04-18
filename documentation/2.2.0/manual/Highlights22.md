<!-- translated -->
<!--
# What's new in Play 2.2
-->
# Play 2.2 の変更点

<!--
## New results structure for Java and Scala
-->
## Java と Scala の新しい構造の Result

<!--
Previously, results could be either plain or async, chunked or simple.  Having to deal with all these different types made action composition and filters hard to implement, since often there was functionality that needed to be applied to all types of results, but code had to implemented to recursively unwrap asynchronous results and to apply the same logic to chunked and simple results.
-->
これまで Result は、同期または非同期、全体またはチャンクのいずれかの形を取っていました。これら異なる種類のすべてを取り扱うことは、アクション合成およびフィルタの実装を難しくしていました。多くの場合、ある機能はすべての種類の Result に適用しなければならないのですが、非同期 Result を再帰的に同期 Result に戻すよう実装し、また Result 全体およびチャンクされた Result に同じロジックを適用する必要があるためです。

<!--
It also created an artificial distinction between asynchronous and synchronous actions in Play, which caused confusion, leading people to think that Play could operate in a synchronous and asynchronous modes.  In fact, Play is 100% asynchronous, the only thing that differentiates whether a result is returned asynchronously or not is whether other asynchronous actions, such as IO, need to be done during action processing.
-->
Play における非同期アクションと同期アクションの間に、Play が同期または非同期モードで動作できるかのような気にさせて、混乱を招く、不自然な区別も作ってしまいました。実際のところ、Play は 100% 非同期であり、例えば IO のような他の非同期アクションが、アクションの処理中に完了する必要があるかによって、Result を非同期に返却するか、そうしないかを区別します。

<!--
So we've simplified the structure for results in Java and Scala.  There is now only one result type, `SimpleResult`.  The `Result` superclass still works in many places but is deprecated.
-->
そこで、Java と Scala における Result の構造を単純化しました。いまはただひとつ、`SimpleResult` 型の Result があるのみです。依然として `Result` スーパークラスは様々な場所で動作しますが、非推奨とします。

<!--
In Java applications, this means actions can now just return `Promise<SimpleResult>` if they wish to do asynchronous processing during a request, while Scala applications can use the `async` action builder, like this:
-->
この単純化は、リクエストの処理中に非同期処理を行いたい場合、Scala アプリケーションでは以下のように `async` アクションビルダーを使うことができる一方で、Java アプリケーションでは単純に `Promise<SimpleResult>` を返すことができるようになることを意味しています:

```scala
def index = Action.async {
  val foo: Future[Foo] = getFoo()
  foo.map(f => Ok(foo))
}
```

<!--
## Better control over buffering and keep alive
-->
## バッファリングおよび keep alive 制御の改善

<!--
How and when Play buffers results is now better expressed in the Scala API, [`SimpleResult`](api/scala/index.html#play.api.mvc.SimpleResult) has a new property called `connection`, which is of type [`HttpConnection`](api/scala/index.html#play.api.mvc.HttpConnection$).
-->
Play がどのように、そしていつ Result をバッファリングするかは、[`HttpConnection`](api/scala/index.html#play.api.mvc.HttpConnection$) 型の `connection` という新しいプロパティを持つ [`SimpleResult`](api/scala/index.html#play.api.mvc.SimpleResult) という Scala API でより良く表現されるようになりました。

<!--
If set to `Close`, the response will be closed once the body is sent, and no buffering will be attempted.  If set to `KeepAlive`, Play will make a best effort attempt to keep the connection alive, in accordance to the HTTP spec, buffering the response if only no transfer encoding or content length is specified.
-->
これに `Close` を設定した場合、ボディを一度送信するとレスポンスはクローズされ、バッファリングされません。`KeepAlive` を設定すると、Play は HTTP 仕様に従い、転送エンコーディングまたはコンテント長が指定されていない場合に限り、コネクションをキープし、レスポンスをバッファリングしようと試みます。

<!--
## New action composition and action builder methods
-->
## 新しいアクション合成とアクションビルダーメソッド

<!--
We now provide an [`ActionBuilder`](api/scala/index.html#play.api.mvc.ActionBuilder) trait for Scala applications that allows more powerful building of action stacks.  For example:
-->
アクションのスタックをより強力に作ることのできる、Scala アプリケーション用の [`ActionBuilder`](api/scala/index.html#play.api.mvc.ActionBuilder) トレイトを提供しています:

<!--
```scala
object MyAction extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[SimpleResult]) = {
    // Authenticate the action and wrap the request in an authenticated request
    getUserFromRequest(request).map { user =>
      block(new AuthenticatedRequest(user, request))
    } getOrElse Future.successful(Forbidden)
  }

  // Compose the action with a logging action, a CSRF checking action, and an action that only allows HTTPS
  def composeAction[A](action: Action[A]) =
    LoggingAction(CheckCSRF(OnlyHttpsAction(action)))
}
```
-->
```scala
object MyAction extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[SimpleResult]) = {
    // アクションを認証し、リクエストを認証済みのものにラップする
    getUserFromRequest(request).map { user =>
      block(new AuthenticatedRequest(user, request))
    } getOrElse Future.successful(Forbidden)
  }

  // ロギングアクション、CSRF チェックアクション、そして HTTPS のみ許容するアクションでアクションを組み立てる
  def composeAction[A](action: Action[A]) =
    LoggingAction(CheckCSRF(OnlyHttpsAction(action)))
}
```

<!--
The resulting action builder can be used just like the built in `Action` object, with optional parser and request parameters, and async variants.  The type of the request parameter passed to the action will be the type specified by the builder, in the above case, `AuthenticatedRequest`:
-->
アクションビルダーの処理結果は、オプションのパーサとリクエストパラメータ、そして非同期なバリアントを伴った、組み込みの `Action` のように扱うことができます。アクションに渡されるリクエストパラメータの型は、ビルダーで指定された型、上記例の場合は `AuthenticatedRequest` になります:

```scala
def save(id: String) MyAction(parse.formUrlEncoded) = { request =>
  Ok("User " + request.user + " saved " + request.body)
}
```

<!--
## Improved Java promise API
-->
## Java promise API の改良

<!--
The Java Promise class has been improved in order to bring its functionality closer to Scala's Future and Promise. In particular execution contexts can now be passed into a Promise's methods.
-->
Java Promise クラスは、その機能を Scala の Future および Promise により近づけることを目的として改良されました。特に、Promise のメソッドに実行コンテキストを渡せるようになりました。

<!--
## Iteratee library execution context passing
-->
## コンテキストを渡した Iteratee ライブラリの実行

<!--
Execution contexts are now required when calling on methods of Iteratee, Enumeratee and Enumerator. Having execution contexts exposed for the Iteratee library provides finer-grained control over where execution occurs and can lead to performance improvements in some cases.
-->
Iteratee, Enumeratee そして Enumarator のメソッドを呼ぶ際には、実行コンテキストが必要になります。Iteratee ライブラリに実行コンテキストを見せることは、実行箇所におけるよりきめ細やかな制御を提供し、またいくつかの場面においてはパフォーマンスの向上につながります。

<!--
Execution contexts can be supplied implicitly which means that there is little impact on the code that uses Iteratees.
-->
実行コンテキストは暗黙的に渡すことができるので、Iteratee を使うコードへの影響はほとんどありません。

<!--
## sbt 0.13 support
-->
## sbt 0.13 サポート

<!--
There have been various usability and performance improvements.
-->
いくつかのユーザビリティ、そしてパフォーマンスが改善されます。

<!--
One usability improvement is that we now support `build.sbt` files for building Play projects e.g. `samples/java/helloworld/build.sbt`:
-->
ユーザビリティの改善のひとつとして、例えば `samples/java/helloworld/build.sbt` のような Play プロジェクトをビルドする `build.sbt` ファイルをサポートするようになりました:

```scala
import play.Project._

name := "helloworld"

version := "1.0"

playJavaSettings
```

<!--
The `playJavaSettings` now declares all that is required for a Java project. Similarly `playScalaSettings` exists for Play Scala projects. Check out the sample projects for examples of this new build configuration. Note that the previous method of using build.scala along with `play.Project` is still supported.
-->
Java プロジェクトには `playJavaSettings` の宣言が必要になります。同様に、Play の Scala プロジェクトには `playScalaSettings` が存在しています。この新しいビルド設定のサンプルについては、サンプルプロジェクトを確認してください。`play.Project` を使った build.scala を使用している以前のメソッドも、引き続きサポートされます。

<!--
For more information on what has changed for sbt 0.13 please refer to its [release notes](http://www.scala-sbt.org/0.13.0/docs/Community/ChangeSummary_0.13.0.html)
-->
sbt 0.13 における変更点のさらなる詳細については、sbt の [リリースノート](http://www.scala-sbt.org/0.13.0/docs/Community/ChangeSummary_0.13.0.html) を参照してください。

<!--
## New stage and dist tasks
-->
## 新しい stage と dist タスク

<!--
The _stage_ and _dist_ tasks have been completely overhauled in order to use the [Native Packager Plugin](https://github.com/sbt/sbt-native-packager).
-->
_stage_ と _dist_ タスク は、[ネイティブパッケージプラグイン](https://github.com/sbt/sbt-native-packager) を使うために完全に見直されました。

<!--
The benefit in using the Native Packager is that many types of archive can now be supported in addition to regular zip files e.g. tar.gz, RPM, OS X disk images, Microsoft Installers (MSI) and more. In addition a Windows batch script is now provided for Play as well as a Unix one.
-->
ネイティブパッケージを使う利点は、いつもの zip ファイルに加えて、例えば tar.gz, RPM, OS X ディスクイメージ, Microsoft インストーラ (MSI) など多くの種類のアーカイブがサポートされていることです。さらに、Play 用の Windows バッチスクリプトが Unix のものと同様に提供されるようになりました。

<!--
More information can be found in the [[Creating a standalone version of your application|ProductionDist]] document.
-->
より詳しい情報は、ドキュメント [[スタンドアローン版のアプリケーションを作成する|ProductionDist]] で読むことができます。

<!--
## Built in gzip support
-->
## 組み込みの gzip サポート

<!--
Play now has built in support for gzipping all responses.  For information on how to enable this, see [[Configuring gzip encoding|GzipEncoding]].
-->
Play は、すべてのレスポンスの gzip 圧縮を組み込みでサポートするようになりました。これを有効にする方法については、[[gzip エンコードの設定|GzipEncoding]] を参照してください。