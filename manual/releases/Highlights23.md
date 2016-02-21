<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# What's new in Play 2.3
-->
# Play 2.3 の変更点

<!--
This page highlights the new features of Play 2.3. If you want learn about the changes you need to make to migrate to Play 2.3, check out the [[Play 2.3 Migration Guide|Migration23]].
-->
このページでは、Play 2.3 の新機能に着目します。Play 2.3 に移行するために必要な変更点について知りたい場合は、[[Play 2.3 移行ガイド|Migration23]] を確認してください。

<!--
## Activator
-->
## Activator

<!--
The first thing you'll notice about Play 2.3 is that the `play` command has become the `activator` command. Play has been updated to use [Activator](https://typesafe.com/activator) so that we can:
-->
まず最初に、Play 2.3 において `play` コマンドが `activator` コマンドに変わったことに気付くでしょう。Play は [Activator](https://typesafe.com/activator) を使うようになったので、以下のことができるようになりました:

<!--
* Extend the range of templates we provide for getting started with Play projects. Activator supports a much [richer library](https://typesafe.com/activator/templates) of project templates. Templates can also include tutorials and other resources for getting started. The Play community can [contribute templates](https://typesafe.com/activator/template/contribute) too.
* Provide a nice web UI for getting started with Play, especially for newcomers who are unfamiliar with command line interfaces. Users can write code and run tests through the web UI. For experienced users, the command line interface is available just like before.
* Make Play's high productivity development approach available to other projects. Activator isn't just for Play. Other projects can use Activator too.
-->
* Play プロジェクトの入門用に用意されたテンプレートを拡張することができます。Activator は、プロジェクトテンプレートのとても [豊富なライブラリ](https://typesafe.com/activator/templates) をサポートしています。テンプレートには入門用のチュートリアルや、その他のリソースを含めることができます。Play コミュニティは [テンプレートに貢献する](https://typesafe.com/activator/template/contribute) こともできます。
* コマンドラインインタフェースに馴染みのない初心者向けに、Play 入門用の web UI を提供することができます。ユーザはこの web UI を使ってコードを書き、テストすることができます。熟練したユーザは、これまでどおりコマンドラインインタフェースを使うことができます。
* Play の高い生産性を誇るアプローチを、他のプロジェクトでも利用することができます。Activator は Play だけのものではありません。他のプロジェクトでも Activator を使うことができます。

<!--
In the future Activator will get even more features, and these features will automatically benefit Play and other projects that use Activator. [Activator is open source](https://github.com/typesafehub/activator), so the community can contribute to its evolution.
-->
将来的に Activator はより多くの機能を手に入れ、これらの機能は自動的に Activator を使う Play や、その他のプロジェクトの利益となるでしょう。[Activator はオープンソース](https://github.com/typesafehub/activator) なので、コミュニティはこの進化に貢献することができます。

<!--
### Activator command
-->
### Activator コマンド

<!--
All the features that were available with the `play` command are still available with the `activator` command.
-->
`play` コマンドで利用できた機能は、すべて `activator` コマンドでも使えます。

<!--
* `activator new` to create a new project. See [[Creating a new application|NewApplication]].
* `activator` to run the console. See [[Using the Play console|PlayConsole]].
* `activator ui` is a new command that launches a web user interface.
-->
* `activator new` で新しいプロジェクトを作ります。[[新しいアプリケーションを作る|NewApplication]] を参照してください。
* `activator` でコンソールを起動します。[[Play コンソールを使う|PlayConsole]] を参照してください。
* `activator ui` は web ユーザインタフェースを起動する新しいコマンドです。

<!--
> The new `activator` command and the old `play` command are both wrappers around [sbt](http://www.scala-sbt.org/). If you prefer, you can use the `sbt` command directly. However, if you use sbt you will miss out on several Activator features, such as templates (`activator new`) and the web user interface (`activator ui`). Both sbt and Activator support all the usual console commands such as `test` and `run`.
-->
> 新しい `activator` コマンドと、古い `play` コマンドはいずれも [sbt](http://www.scala-sbt.org/) のラッパーです。お望みであれば `sbt` コマンドを直接使うことができます。しかし、sbt を使うと、例えばテンプレート (`activator new`) や web ユーザインタフェース (`activator ui`) など、Activator の機能をいくつか失うことになります。sbt と Activator はいずれも `test` や `run` のような一般的なコンソールコマンドをすべてサポートしています。

<!--
### Activator distribution
-->
### Activator ディストリビューション

<!--
Play is distributed as an Activator distribution that contains all Play's dependencies. You can download this distribution from the [Play download](https://www.playframework.com/download) page.
-->
Play は、すべての依存性を含む Activator ディストリビューションとして配布されています。このディストリビューションは [Play ダウンロード](https://www.playframework.com/download) ページからダウンロードすることができます。

<!--
If you prefer, you can also download a minimal (1MB) version of Activator from the [Activator site](https://typesafe.com/activator). Look for the "mini" distribution on the download page. The minimal version of Activator will only download dependencies when they're needed.
-->
お望みであれば、[Activator のサイト](https://typesafe.com/activator) から最小 (1MB) バージョンの Activator をダウンロードすることもできます。ダウンロードページで "mini" ディストリビューションを探してみてください。最小バーションの Activator は、必要になった時のみ依存性をダウンロードします。

<!--
Since Activator is a wrapper around sbt, you can also download and use [sbt](http://www.scala-sbt.org/) directly, if you prefer.
-->
Activator は sbt のラッパーなので、お望みであれば [sbt](http://www.scala-sbt.org/) をダウンロードして直接使うこともできます。

<!--
## Build improvements
-->
## Build の改善

<!--
### sbt-web
-->
### sbt-web

<!--
The largest new feature for Play 2.3 is the introduction of [sbt-web](https://github.com/sbt/sbt-web#sbt-web). In summary sbt-web allows HTML, CSS and JavaScript functionality to be factored out of Play's core into a family of pure sbt plugins. There are two major advantages to you:
-->
Play 2.3 の最大の新機能は [sbt-web](https://github.com/sbt/sbt-web#sbt-web) の紹介です。手短に言うと、sbt-web により HTML, CSS そして JavaScript の機能を、Play コアから純粋な sbt プラグイン群に移動することができました。これには主に二つの利点があります:

<!--
* Play is less opinionated on the HTML, CSS and JavaScript; and
* sbt-web can have its own community and thrive in parallel to Play's.
-->
* Play は HTML, CSS そして JavaScript に固執しなくて済むようになります; そして
* sbt-web は、専門のコミュニティを持ち、Play のコミュニティと並行して成長することができます

<!--
### Auto Plugins
-->
### Auto プラグイン

<!--
Play now uses sbt 0.13.5. This version brings a new feature named "auto plugins" which, in essence permits a large reduction in settings-oriented code for your build files.
-->
これからの Play は sbt 0.13.5 を使います。このバージョンには "auto プラグイン" という、要するにビルドファイルから設定向けのコードを大幅に削減することができる機能が付いています。

<!--
### Asset Pipeline and Fingerprinting
-->
### アセットパイプラインとフィンガープリント

<!--
sbt-web brings the notion of a highly configurable asset pipeline to Play e.g.:
-->
sbt-web は、例えば次のように高度に設定できるアセットパイプラインを Play に持たらします:

```scala
pipelineStages := Seq(rjs, digest, gzip)
```

<!--
The above will order the RequireJs optimizer (sbt-rjs), the digester (sbt-digest) and then compression (sbt-gzip). Unlike many sbt tasks, these tasks will execute in the order declared, one after the other.
-->
上記の設定は、RequireJs オプティマイザ (sbt-rjs), ダイジェスト化 (sbt-digest), そしてその後に圧縮 (sbt-gzip) を並べています。多くの sbt タスクとは違い、これらのタスクは宣言された順番通り、ひとつずつ実行されます。

<!--
One new capability for Play 2.3 is the support for asset fingerprinting, similar in principle to [Rails asset fingerprinting](http://guides.rubyonrails.org/asset_pipeline.html#what-is-fingerprinting-and-why-should-i-care-questionmark). A consequence of asset fingerprinting is that we now use far-future cache expiries when they are served. The net result of this is that your user's will experience faster downloads when they visit your site given the aggressive caching strategy that a browser is now able to employ.
-->
Play 2.3 の新しい機能のひとつに、[Rails アセットフィンガープリント](http://railsguides.jp/asset_pipeline.html#%E3%83%95%E3%82%A3%E3%83%B3%E3%82%AC%E3%83%BC%E3%83%97%E3%83%AA%E3%83%B3%E3%83%88%E3%81%A8%E6%B3%A8%E6%84%8F%E7%82%B9) と似た原理のアセットフィンガープリントのサポートがあります。アセットフィンガープリントの重要性は、いまや我々はアセットが提供される際に遥か遠い未来の有効期限を設定する点にあります。最終的な結論として、ブラウザが採用できるようになった積極的なキャッシュ戦略を与えられたサイトを訪れた際、ユーザは高速なダウンロードを体験することになるでしょう。

<!--
### Default ivy cache and local repository
-->
### デフォルトの ivy キャッシュとローカルリポジトリ

<!--
Play now uses the default ivy cache and repository, in the `.ivy2` folder in the users home directory.
-->
Play はユーザのホームディレクトリにある `.ivy2` フォルダにあるデフォルトの ivy キャッシュとリポジトリを使います。

<!--
This means Play will now integrate better with other sbt builds, not requiring artifacts to be cached multiple times, and allowing the sharing of locally published artifacts.
-->
これは Play が他の sbt ビルドとよりよく統合され、キャッシュされるアーティファクトを何度も要求せず、そしてローカルに発行されたアーティファクトを共有することを意味します。

<!--
## Java improvements
-->
## Java の改善

<!--
### Java 8
-->
### Java 8

<!--
Play 2.3 has been tested with Java 8. Your project will work just fine with Java 8; there is nothing special to do other than ensuring that your Java environment is configured for Java 8. There is a new Activator sample available for Java 8:
-->
Play 2.3 は Java 8 でテストされています。あなたのプロジェクトは Java 8 で問題なく動作するでしょう; Java 環境が Java 8 向けに設定されていることを確認すれば、他に特にすることはありません。以下に、Java 8 で利用できる新しい Activator サンプルがあります:

http://typesafe.com/activator/template/reactive-stocks-java8

<!--
Our documentation has been improved with Java examples in general and, where applicable, Java 8 examples. Check out some [[examples of asynchronous programming with Java 8|JavaAsync]].
-->
一般的な Java サンプルと、そして適用できる場所には Java 8 のサンプルを使ってドキュメントを改善しました。[[Java 8 による非同期プログラミングのサンプル|JavaAsync]] を確認してください。

<!--
For a complete overview of going Reactive with Java 8 and Play check out this blog: http://typesafe.com/blog/go-reactive-with-java-8 -->
Java 8 と Play によるリアクティブに関する全体的な概要は、次のブログを参照してください: http://typesafe.com/blog/go-reactive-with-java-8

<!--
### Java performance
-->
### Java パフォーマンス

<!--
We've worked on Java performance. Compared to Play 2.2, throughput of simple Java actions has increased by 40-90%. Here are the main optimizations:
-->
Java パフォーマンスについて取り組みました。Play 2.2 と比較すると、単純な Java アクションのスループットは 40〜90% 改善しています。主な最適化は以下の通りです:

<!--
* Reducing thread switches for Java actions and body parsers.
* Caching more route information and using per-route caching rather than a shared Map.
* Reducing body parsing overhead for GET requests.
* Using a unicast enumerator for returning chunked responses.
-->
* Java アクションとボディパーサのスレッド切り替えを削減しました。
* より多くのルーティング情報をキャッシュして、共有 Map の代わりにルートごとのキャッシュを使うようにしました。
* GET リクエストにおけるボディ解析のオーバーヘッドを削減しました。
* ユニキャストな enumerator を使ってチャンクされたレスポンスを返すようにしました。

<!--
Some of these changes also improved Scala performance, but Java had the biggest performance gains and was the main focus of our work.
-->
これらの変更のうち、いくつかは Scala のパフォーマンスも改善しますが、Java のパフォーマンスは大幅に改善されており、これこそがこの取り組みの主な目的でした。

<!--
Thankyou to [YourKit](https://www.yourkit.com/) for supplying the Play team with licenses to make this work possible.
-->
Play チームにライセンスを提供し、この作業を可能にしてくれた [YourKit](https://www.yourkit.com/) に感謝します。

<!--
## Scala 2.11
-->
## Scala 2.11

<!--
Play 2.3 is the first release of Play to have been cross built against multiple versions of Scala, both 2.10 and 2.11.
-->
Play 2.3 は複数の Scala バージョン、2.10 と 2.11 でクロスビルドできる最初のリリースです。

<!--
You can select which version of Scala you would like to use by setting the `scalaVersion` setting in your `build.sbt` or `Build.scala` file.
-->
使用する Scala のバージョンは、`build.sbt` または `Build.scala` ファイル内の `scalaVersion` 設定で選択することができます。

<!--
For Scala 2.11:
-->
Scala 2.11 の場合:

```scala
scalaVersion := "2.11.1"
```

<!--
For Scala 2.10:
-->
Scala 2.10 の場合:

```scala
scalaVersion := "2.10.4"
```

<!--
## Play WS
-->
## Play WS

<!--
### Separate library
-->
### 分離されたライブラリ

<!--
The WS client library has been refactored into its own library which can be used outside of Play. You can now have multiple `WSClient` objects, rather than only using the `WS` singleton.
-->
WS クライアントライブラリは、Play 以外でも使えるよう、独自のライブラリにリファクタリングされました。今後は、`WS` シングルトンだけではなく、複数の `WSClient` オブジェクトを使うことができます。

[[Java|JavaWS]]

```java
WSClient client = new NingWSClient(config);
Promise<WSResponse> response = client.url("http://example.com").get();
```

[[Scala|ScalaWS]]

```scala
val client: WSClient = new NingWSClient(config)
val response = client.url("http://example.com").get()
```

<!--
Each WS client can be configured with its own options. This allows different Web Services to have different settings for timeouts, redirects and security options.
-->
いずれの WS クライアントも独自のオプションを設定することができます。これにより、異なる Web サービスにタイムアウト、リダイレクト、そしてセキュリティに関する異なるオプションを設定できるようになります。

<!--
The underlying `AsyncHttpClient` object can also now be accessed, which means that multi-part form and streaming body uploads are supported.
-->
根本となる `AsyncHttpClient` オブジェクトにもアクセスすることができるので、multi-part フォーム、そしてボディのストリームアップロードをサポートすることができます。

<!--
### WS Security
-->
### WS セキュリティ

<!--
WS clients have [[settings|WsSSL]] for comprehensive SSL/TLS configuration. WS client configuration is now more secure by default.
-->
WS クライアントには、包括的な SSL/TSL [[設定|WsSSL]]があります。デフォルトの WS クライアント設定は、よりセキュアなものになりました。

<!--
## Actor WebSockets
-->
## Actor WebSocket

<!--
A method to use actors for handling websocket interactions has been incorporated for both Java and Scala e.g. using Scala:
-->
actor を使って websocket とのやり取りをハンドリングするメソッドが Java と Scala の両方に組み込まれました。例 Scala を使う場合:

[[Java|JavaWebSockets]]

```java
public static WebSocket<String> socket() {
    return WebSocket.withActor(MyWebSocketActor::props);
}
```

[[Scala|ScalaWebSockets]]

```scala
def webSocket = WebSocket.acceptWithActor[JsValue, JsValue] { req => out =>
  MyWebSocketActor.props(out)
```

<!--
## Results restructuring completed
-->
## Results 再構築の完了

<!--
In Play 2.2, a number of new result types were introduced and old results types deprecated. Play 2.3 finishes this restructuring. See *Results restructure* in the [[Migration Guide|Migration23]] for more information.
-->
Play 2.2 で新しい型の Result がいくつか追加され、古い型の Result は非推奨になりました。この再構築作業を Play 2.3 で完了します。詳細は [[Play 2.3 移行ガイド]] の **Result 再構築** を参照してください。

<!--
## Anorm
-->
## Anorm

<!--
There are various fixes included in Play 2.3's Anorm (type safety, option parsing, error handling, ...) and new interesting features.
-->
Play 2.3 の Anorm には、様々な修正 (型安全、オプションのパース、エラーハンドリング、...) と、興味深い新機能が含まれています。

<!--
- String interpolation is available to write SQL statements more easily, with less verbosity (passing arguments) and performance improvements (up to x7 faster processing parameters). e.g. `SQL"SELECT * FROM table WHERE id = $id"`
- Multi-value (sequence/list) can be passed as parameter. e.g. `SQL"""SELECT * FROM Test WHERE cat IN (${Seq("a", "b", "c")})"""`
- It's now possible to parse column by position. e.g. `val parser = long(1) ~ str(2) map { case l ~ s => ??? }`
- Query results include not only data, but execution context (with SQL warning).
- More types are supported as parameter and as column: `java.util.UUID`, numeric types (Java/Scala big decimal and integer, more column conversions between numerics), temporal types (`java.sql.Timestamp`), character types.
-->
- 文字列の補間で SQL 文字列をより簡単、かつ (引数のパースを) 少ない冗長さで書くことができるようになり、また性能も (パラメータの処理は 7 倍にまで) 改善しました。例 `SQL"SELECT * FROM table WHERE id = $id"`
- 複数の値 (シーケンス/リスト) をパラメータとして渡すことができます。例 `SQL"""SELECT * FROM Test WHERE cat IN (${Seq("a", "b", "c")})"""`
- 列を位置でパースできるようになりました。例 `val parser = long(1) ~ str(2) map { case l ~ s => ??? }`
- クエリ結果にはデータだけなく、(SQL の警告を含む) 実行コンテキストが含まれています。
- より多くの型が、引数、および列としてサポートされました: `java.util.UUID`, 数値型 (Java/Scala の big decimal と integer, 数値型間の列変換), 時制型 (`java.sql.Timestamp`), 文字列型。

<!--
## Custom SSLEngine for HTTPS
-->
## HTTPS 用のカスタム SSL エンジン

<!--
The Play server can now [[use a custom `SSLEngine`|ConfiguringHttps]]. This is also useful in cases where customization is required, such as in the case of client authentication.
-->
Play サーバは [[カスタム `SSLEngine` を使う|ConfiguringHttps]] ことができるようになりました。これは、クライアント認証する場合などのカスタマイズに便利です。