<!--
# Play for Java developers
-->
# Java 開発者のための Play

<!--
The Java API for the Play application developers is available in the `play` package. 
-->
Play アプリケーションは、`play` パッケージ内にある Java API を使って開発することができます。

<!--
> The API available in the `play.api` package (such as `play.api.mvc`) is reserved for Scala developers. As a Java developer, look at `play.mvc`.
-->
> (`play.api.mvc` のような) `play.api` パッケージ内の API は Scala 開発者に予約されています。Java 開発者は `play.mvc` などを参照してください。

<!--
## Main concepts
-->
## 主要なコンセプト

<!--
1. [[HTTP programming | JavaActions]]
    1. [[Actions, Controllers and Results | JavaActions]]
    1. [[HTTP routing | JavaRouting]]
    1. [[Manipulating the HTTP response | JavaResponse]]
    1. [[Session and Flash scopes | JavaSessionFlash]]
    1. [[Body parsers | JavaBodyParsers]]
    1. [[Action composition | JavaActionsComposition]]
    1. [[Content negotiation | JavaContentNegotiation]]
1. [[Asynchronous HTTP programming | JavaAsync]]
    1. [[Handling asynchronous results | JavaAsync]]
    1. [[Streaming HTTP responses | JavaStream]]
    1. [[Comet sockets | JavaComet]]
    1. [[WebSockets | JavaWebSockets]]
1. [[The template engine | JavaTemplates]]
    1. [[Templates syntax | JavaTemplates]]
    1. [[Common use cases | JavaTemplateUseCases]]
1. [[HTTP form submission and validation | JavaForms]]
    1. [[Form definitions | JavaForms]]
    1. [[Using the form template helpers | JavaFormHelpers]]
1. [[Working with Json | JavaJsonRequests]]
    1. [[Handling and serving Json requests | JavaJsonRequests]]
1. [[Working with XML | JavaXmlRequests]]
    1. [[Handling and serving XML requests | JavaXmlRequests]]
1. [[Handling file upload | JavaFileUpload]]
    1. [[Direct upload and multipart/form-data | JavaFileUpload]]
1. [[Accessing an SQL database | JavaDatabase]]
    1. [[Configuring and using JDBC | JavaDatabase]]
    1. [[Using Ebean ORM | JavaEbean]]
    1. [[Integrating with JPA | JavaJPA]]
1. [[Using the Cache | JavaCache]]
    1. [[The Play cache API | JavaCache]]
1. [[Calling WebServices | JavaWS]]
    1. [[The Play WS API  | JavaWS]]
    1. [[Connect to OpenID servers | JavaOpenID]]
1. [[Integrating with Akka | JavaAkka]]
    1. [[Setting up Actors and scheduling asynchronous tasks | JavaAkka]]
1. [[Internationalization | JavaI18N]]
    1. [[Messages externalization and i18n | JavaI18N]]
1. [[The application Global object | JavaGlobal]]
    1. [[Application global settings | JavaGlobal]]
    1. [[Intercepting requests | JavaInterceptors]]
    1. [[Managing Controller Class Instantiation | JavaInjection]]
1. [[Testing your application | JavaTest]]
    1. [[Writing tests | JavaTest]]
    1. [[Writing functional tests | JavaFunctionalTest]]
-->
1. [[HTTP プログラミング | JavaActions]]
    1. [[アクション、コントローラ、レスポンス | JavaActions]]
    1. [[HTTP ルーティング | JavaRouting]]
    1. [[HTTP レスポンスの操作 | JavaResponse]]
    1. [[セッションとフラッシュスコープ | JavaSessionFlash]]
    1. [[ボディパーサー | JavaBodyParsers]]
    1. [[アクションの合成 | JavaActionsComposition]]
    1. [[コンテントネゴシエーション | JavaContentNegotiation]]
1. [[非同期 HTTP プログラミング | JavaAsync]]
    1. [[非同期レスポンスの処理 | JavaAsync]]
    1. [[HTTP レスポンスのストリーミング | JavaStream]]
    1. [[Comet | JavaComet]]
    1. [[WebSockets | JavaWebSockets]]
1. [[テンプレートエンジン | JavaTemplates]]
    1. [[テンプレートの文法 | JavaTemplates]]
    1. [[よくある使い方 | JavaTemplateUseCases]]
1. [[HTTP フォームの投稿とバリデーション | JavaForms]]
    1. [[フォームの定義 | JavaForms]]
    1. [[フォームテンプレートヘルパの利用 | JavaFormHelpers]]
1. [[Json を使う | JavaJsonRequests]]
    1. [[Json リクエストの処理と送信 | JavaJsonRequests]]
1. [[XML を使う | JavaXmlRequests]]
    1. [[XML リクエストの処理と送信 | JavaXmlRequests]]
1. [[ファイルアップロード処理 | JavaFileUpload]]
    1. [[multipart/form-data のアップロード | JavaFileUpload]]
1. [[SQL データベースへのアクセス | JavaDatabase]]
    1. [[JDBC の設定と使用 | JavaDatabase]]
    1. [[Ebean ORM の利用 | JavaEbean]]
    1. [[JPA との統合 | JavaJPA]]
1. [[キャッシュを使う | JavaCache]]
    1. [[Play Cashe API | JavaCache]]
1. [[Web サービスの呼び出し | JavaWS]]
    1. [[Play WS API  | JavaWS]]
    1. [[OpenID サーバへの接続 | JavaOpenID]]
1. [[Akka の統合 | JavaAkka]]
    1. [[アクターの設定と非同期タスクのスケジューリング | JavaAkka]]
1. [[国際化 | JavaI18N]]
    1. [[メッセージの外部化と i18n | JavaI18N]]
1. [[アプリケーション Global オブジェクト | JavaGlobal]]
    1. [[アプリケーションの全般設定 | JavaGlobal]]
    1. [[リクエストのインターセプト | JavaInterceptors]]
1. [[アプリケーションのテスト | JavaTest]]
    1. [[テストの書き方 | JavaTest]]
    1. [[機能テストの書き方 | JavaFunctionalTest]]

<!--
## Tutorials
-->
## チュートリアル

<!--
1. [[Your first application | JavaTodoList]]
1. [[Zentask | JavaGuide1]]
-->
1. [[はじめてのアプリケーション | JavaTodoList]]
1. [[Zentask | JavaGuide1]]