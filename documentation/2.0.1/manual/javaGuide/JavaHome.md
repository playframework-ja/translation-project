<!--
# Play 2.0 for Java developers
-->
# Java 開発者のための Play 2.0

<!--
The Java API for the Play 2.0 application developers is available in the `play` package. 
-->
Play 2.0 アプリケーション開発者は `play` パッケージ内の Java API を使うことができます。

<!--
> The API available in the `play.api` package (such as `play.api.mvc`) is reserved for Scala developers. As a Java developer, look at `play.mvc`.
-->
> (`play.api.mvc` のような) `play.api` パッケージ内の API は Scala 開発者に予約されています。Java 開発者は `play.mvc` を見てください。


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
    1. [[Actions composition | JavaActionsComposition]]
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
1. [[Testing your application | JavaTest]]
    1. [[Writing tests | JavaTest]]
    1. [[Writing functional tests | JavaFunctionalTest]]
-->
1. [[HTTP プログラミング | JavaActions]]
    1. [[アクション、コントローラと結果 | JavaActions]]
    1. [[HTTP ルーティング | JavaRouting]]
    1. [[HTTP レスポンスの操作 | JavaResponse]]
    1. [[Session と Flash スコープ | JavaSessionFlash]]
    1. [[ボディパーサ | JavaBodyParsers]]
    1. [[アクションの合成 | JavaActionsComposition]]
1. [[非同期 HTTP プログラミング | JavaAsync]]
    1. [[非同期処理結果のハンドリング | JavaAsync]]
    1. [[ストリーミング HTTP レスポンス | JavaStream]]
    1. [[Comet | JavaComet]]
    1. [[WebSockets | JavaWebSockets]]
1. [[テンプレートエンジン | JavaTemplates]]
    1. [[テンプレート構文 | JavaTemplates]]
    1. [[一般的なユースケース | JavaTemplateUseCases]]
1. [[HTTP フォームの投稿とバリデーション | JavaForms]]
    1. [[フォーム定義 | JavaForms]]
    1. [[フォームテンプレートヘルパを使う | JavaFormHelpers]]
1. [[Json を扱う | JavaJsonRequests]]
    1. [[Json リクエストのハンドリングと送信 | JavaJsonRequests]]
1. [[XML を扱う | JavaXmlRequests]]
    1. [[XML リクエストのハンドリングと送信 | JavaXmlRequests]]
1. [[ファイルアップロードのハンドリング | JavaFileUpload]]
    1. [[ダイレクトアップロードと multipart/form-data | JavaFileUpload]]
1. [[SQL データベースへのアクセス | JavaDatabase]]
    1. [[JDBC の設定としよう | JavaDatabase]]
    1. [[Ebean ORM を使う | JavaEbean]]
    1. [[JPA の統合 | JavaJPA]]
1. [[キャッシュを使う | JavaCache]]
    1. [[Play のキャッシュ API | JavaCache]]
1. [[Web サービスの呼び出し | JavaWS]]
    1. [[Play の Web サービス API  | JavaWS]]
    1. [[OpenID サーバへの接続 | JavaOpenID]]
1. [[Akka の統合 | JavaAkka]]
    1. [[アクタのセットアップと非同期タスクのスケジューリング | JavaAkka]]
1. [[国際化 | JavaI18N]]
    1. [[メッセージの外部化と i18n | JavaI18N]]
1. [[アプリケーショングローバルオブジェクト | JavaGlobal]]
    1. [[アプリケーショングローバル設定 | JavaGlobal]]
    1. [[リクエストのインターセプト | JavaInterceptors]]
1. [[アプリケーションのテスト | JavaTest]]
    1. [[テストを書く | JavaTest]]
    1. [[機能テストを書く | JavaFunctionalTest]]

<!--
## Tutorials
-->
## チュートリアル

<!--
1. [[Your first application | JavaTodoList]]
-->
1. [[初めてのアプリケーション | JavaTodoList]]