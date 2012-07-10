<!--
# Play 2.0 for Scala developers
-->
# Play 2.0 for Scala ガイド

<!--
The Scala API for Play 2.0 application developers is available in the `play.api` package. 
-->
Play 2.0 アプリケーション向けの Scala API は `play.api` パッケージ以下にあります。

<!--
> The API available directly inside the `play` package (such as `play.mvc`) is reserved for Java developers. As a Scala developer, look at `play.api.mvc`.
-->
> `play` パッケージ直下に定義されている API は全ての Java 向けです。 Scala 開発者の皆さんは、 `play.api.mvc` などを利用してください。

<!--
## Main concepts
-->
## メインコンセプト

<!--
1. [[HTTP programming | ScalaActions]]
    1. [[Actions, Controllers and Results | ScalaActions]]
    1. [[HTTP routing | ScalaRouting]]
    1. [[Manipulating results | ScalaResults]]
    1. [[Session and Flash scopes | ScalaSessionFlash]]
    1. [[Body parsers | ScalaBodyParsers]]
    1. [[Actions composition | ScalaActionsComposition]]
1. [[Asynchronous HTTP programming | ScalaAsync]]
    1. [[Handling asynchronous results | ScalaAsync]]
    1. [[Streaming HTTP responses | ScalaStream]]
    1. [[Comet sockets | ScalaComet]]
    1. [[WebSockets | ScalaWebSockets]]
1. [[The template engine | ScalaTemplates]]
    1. [[Templates syntax | ScalaTemplates]]
    1. [[Common use cases | ScalaTemplateUseCases]]
1. [[HTTP form submission and validation | ScalaForms]]
    1. [[Form definitions | ScalaForms]]
    1. [[Using the form template helpers | ScalaFormHelpers]]
1. [[Working with Json | ScalaJson]]
    1. [[The Play Json library | ScalaJson]]
    1. [[Handling and serving Json requests | ScalaJsonRequests]]
    1. [[The Play Json Library with Generics | ScalaJsonGenerics]]
1. [[Working with XML | ScalaXmlRequests]]
    1. [[Handling and serving XML requests | ScalaXmlRequests]]
1. [[Handling file upload | ScalaFileUpload]]
    1. [[Direct upload and multipart/form-data | ScalaFileUpload]]
1. [[Accessing an SQL database | ScalaDatabase]]
    1. [[Configuring and using JDBC | ScalaDatabase]]
    1. [[Using Anorm to access your database | ScalaAnorm]]
    1. [[Integrating with other database access libraries | ScalaDatabaseOthers]]
1. [[Using the Cache | ScalaCache]]
    1. [[The Play cache API | ScalaCache]]
1. [[Calling WebServices | ScalaWS]]
    1. [[The Play WS API  | ScalaWS]]
    1. [[Connecting to OpenID services | ScalaOpenID]]
    1. [[Accessing resources protected by OAuth | ScalaOAuth]]
1. [[Integrating with Akka | ScalaAkka]]
    1. [[Setting up Actors and scheduling asynchronous tasks | ScalaAkka]]
1. [[Internationalization | ScalaI18N]]
    1. [[Messages externalisation and i18n | ScalaI18N]]
1. [[The application Global object | ScalaGlobal]]
    1. [[Application global settings | ScalaGlobal]]
    1. [[Intercepting requests | ScalaInterceptors]]
1. [[Testing your application | ScalaTest]]
    1. [[Writing tests | ScalaTest]]
    1. [[Writing functional tests | ScalaFunctionalTest]]
-->
1. [[HTTP プログラミング | ScalaActions]]
    1. [[アクション、コントローラ、レスポンス | ScalaActions]]
    1. [[HTTP ルーティング | ScalaRouting]]
    1. [[レスポンスの変更 | ScalaResults]]
    1. [[セッション、フラッシュスコープ | ScalaSessionFlash]]
    1. [[ボディ・パーサー | ScalaBodyParsers]]
    1. [[アクションの合成 | ScalaActionsComposition]]
1. [[非同期 HTTP プログラミング | ScalaAsync]]
    1. [[非同期レスポンス | ScalaAsync]]
    1. [[HTTP レスポンスのストリーミング | ScalaStream]]
    1. [[Comet | ScalaComet]]
    1. [[WebSocket | ScalaWebSockets]]
1. [[テンプレート・エンジン | ScalaTemplates]]
    1. [[テンプレート文法 | ScalaTemplates]]
    1. [[よくある利用例 | ScalaTemplateUseCases]]
1. [[HTTP フォーム送信とバリデーション | ScalaForms]]
    1. [[フォーム定義 | ScalaForms]]
    1. [[フォーム・テンプレート・ヘルパー | ScalaFormHelpers]]
1. [[JSON | ScalaJson]]
    1. [[Play の JSON ライブラリ | ScalaJson]]
    1. [[JSON リクエストと JSON レスポンス | ScalaJsonRequests]]
    1. [[ジェネリクスと Play Json ライブラリ | ScalaJsonGenerics]]
1. [[XML | ScalaXmlRequests]]
    1. [[XML リクエストと XML レスポンス | ScalaXmlRequests]]
1. [[ファイルアップロード | ScalaFileUpload]]
    1. [[multipart/form-data のアップロード | ScalaFileUpload]]
1. [[SQL データベースアクセス | ScalaDatabase]]
    1. [[JDBC の設定と利用 | ScalaDatabase]]
    1. [[Anorm によるデータベースアクセス | ScalaAnorm]]
    1. [[データベースアクセスライブラリの利用 | ScalaDatabaseOthers]]
1. [[キャッシュ | ScalaCache]]
    1. [[Play の Cache API | ScalaCache]]
1. [[ウェブサービスの呼び出し | ScalaWS]]
    1. [[Play の WS API  | ScalaWS]]
    1. [[OpenID 対応サービスへの接続 | ScalaOpenID]]
    1. [[OAuth によリ保護されたデータへのアクセス | ScalaOAuth]]
1. [[Akka | ScalaAkka]]
    1. [[アクターと非同期処理 | ScalaAkka]]
1. [[国際化対応 | ScalaI18N]]
    1. [[メッセージの外部ファイル化と i18n | ScalaI18N]]
1. [[Global オブジェクト | ScalaGlobal]]
    1. [[アプリケーションの全般設定 | ScalaGlobal]]
    1. [[インターセプター | ScalaInterceptors]]
1. [[テストについて | ScalaTest]]
    1. [[テストの書き方 | ScalaTest]]
    1. [[機能テストの書き方 | ScalaFunctionalTest]]
    
<!--
## Advanced topics
-->
## 上級編

<!--
1. [[Handling data streams reactively | Iteratees]]
    1. [[Iteratees | Iteratees]]
    1. [[Enumerators | Enumerators]]
    1. [[Enumeratees | Enumeratees]]
-->
1. [[反応的なストリーム処理 | Iteratees]]
    1. [[Iteratee | Iteratees]]
    1. [[Enumerator | Enumerators]]
    1. [[Enumeratee | Enumeratees]]

<!--
## Tutorials
-->
## チュートリアル

<!--
1. [[Your first application | ScalaTodoList]]
-->
1. [[TODO リストをつくる | ScalaTodoList]]
