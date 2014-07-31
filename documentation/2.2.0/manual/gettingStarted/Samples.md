<!-- translated -->
<!--
# Sample applications
-->
# サンプルアプリケーション

<!--
The Play binary package comes with a comprehensive set of sample applications written in both Java and Scala. This is a very good place to look for code snippets and examples.
-->
Play のバイナリパッケージには Java と Scala の両方で書かれたサンプルアプリケーションの包括的なセットが付属しています。これはコードスニペットと例を探すのにとても良い場所です。

<!--
> The sample applications are available in the `samples/` directory of your Play installation.
-->
> サンプルアプリケーションは Play 2.0 パッケージの `samples/` ディレクトリに含まれています。

## Hello world

[[images/helloworld.png]]

<!--
This is a very basic application that demonstrates Play fundamentals:
-->
これは Play の基本機能を紹介するための入門的なアプリケーションです。

<!--
- Writing controllers and actions.
- Routing and reverse routing.
- Linking to public assets.
- Using the template engine.
- Handling forms with validation.
-->
- コントローラとアクション
- ルーティングとリバースルーティング
- 静的コンテンツへのリンク
- テンプレートエンジンの利用
- フォームの送信とバリデーション

## Computer database

[[images/computerdatabase.png]]

<!--
This is a classic CRUD application, backed by a JDBC database. It demonstrates:
-->
これは JDBC データベースを利用した、伝統的な CRUD アプリケーションです。

<!--
- accessing a JDBC database, using Ebean in Java and Anorm in Scala
- table pagination and CRUD forms
- integrating with a CSS framework ([Twitter Bootstrap](http://twitter.github.com/bootstrap/).
-->
- JDBC データベースへのアクセス。Java 版では Ebean、Scala 版では Anorm を利用します。
- テーブルのページングと CRUD 用フォーム
- CSS フレームワーク ([Twitter Bootstrap](http://twitter.github.com/bootstrap/) の組み込み。

<!--
Twitter Bootstrap requires a different form layout to the default layout provided by the Play form helper, so this application also provides an example of integrating a custom form input constructor.
-->
Twitter Bootstrap を利用するためには、 Play 標準のフォームヘルパーが生成する input 要素のレイアウトとは異なるレイアウトが必要になります。そのため、このアプリケーションはフォームヘルパーをカスタマイズしています。

<!--
## Forms
-->
## フォーム

[[images/forms.png]]

<!--
This is a dummy application presenting several typical form usages. It demonstrates: 
-->
これはいくつかの典型的なフォームの使用方法を提示するダミーアプリケーションです。 

<!--
- writing complex forms with validation
- handling forms with dynamically repeated values.
-->
- 複雑なフォームとバリデーション
- 動的な繰り返しの値のフォームの送信

## ZenTasks

[[images/zentask.png]]

<!--
This advanced todo list demonstrates a modern Ajax-based web application. This is a work in progress, and we plan to add features in the future releases. For now you can check it out to learn how to:
-->
この高機能 TODO リストは、最近流行の Ajax ベースな web アプリのデモです。まだ未完成で、今後はさらに機能が追加される予定です。今のところ次のような機能の参考になります。

<!--
- integrate authentication and security
- use Ajax and JavaScript reverse routing
- integrate with compiled assets - LESS CSS and CoffeeScript.
-->
- セキュリティと認証
- Ajax の利用と、JavaScript によるリバースルーティング
- LESS CSS や CofeeScript など、トランスコンパイルされるアセットの利用

## CometClock

[[images/comet-clock.png]]

<!--
This a very simple Comet demonstration pushing clock events from the server to the Web browser using a the forever-frame technique. It demonstrates how to:
-->
これは forever-frame と言われる技術を使ってクロックのイベントをサーバーからウェブブラウザにプッシュ送信するとても単純な Comet のデモンストレーションです。

<!--
- create a Comet connection
- use Akka actors (in the Java version)
- write custom Enumerators (in the Scala version).
-->
- Comet コネクションの作成
- Akka の Actor の利用 (Java 版)
- Enumerators の改造 (Scala 版)

<!--
## WebSocket chat
-->
## WebSocket チャット

[[images/websocket-chat.png]]

<!--
This application is a chat room, built using WebSockets. Additionally, there is a bot used that talks in the same chat room. It demonstrates:
-->
このアプリケーションは WebSocket を使用して構築されたチャットルームです。さらに、同じチャットルームで話す bot もいます。

<!--
- WebSocket connections
- advanced Akka usage.
-->
- WebSocket コネクション
- 高度な Akka の利用

<!--
## Comet monitoring
-->
## Comet モニタリング

[[images/rps-screenshot.png]]

<!--
This mobile web application monitors Play server performance. It demonstrates:
-->
このモバイルウェブアプリは Play サーバのパフォーマンスをモニタリングします。

<!--
- advanced usage of Enumerators and Enumeratees.
-->
- Enumerator と Enumeratee の高度な利用

&nbsp;

<!--
> **Next:** 
>
> – [[Play for Scala developers | ScalaHome]]
> – [[Play for Java developers | JavaHome]]
-->
> **Next:**
>
> – [[Scala 開発者のための Play|ScalaHome]]
> – [[Java 開発者のための Play|JavaHome]]
