<!-- translated -->
<!--
# Play for Scala developers
-->
# Scala 開発者のための Play

<!--
The Scala API for Play application developers is available in the `play.api` package. 
-->
Play アプリケーションの Scala 向け API は `play.api` パッケージ配下にあります。

<!--
> The API available directly inside the `play` package (such as `play.mvc`) is reserved for Java developers. As a Scala developer, look at `play.api.mvc`.
-->
> (`play.mvc` のような) `play` パッケージ直下の API は Java 開発者のために予約されています。Scala 開発者は `play.api.mvc` を参照してください。

<!--
## Main concepts
-->
## 主要なコンセプト

<!--
1. [[HTTP programming | ScalaActions]]
    1. [[Actions, Controllers and Results | ScalaActions]]
    1. [[HTTP routing | ScalaRouting]]
    1. [[Manipulating results | ScalaResults]]
    1. [[Session and Flash scopes | ScalaSessionFlash]]
    1. [[Body parsers | ScalaBodyParsers]]
    1. [[Actions composition | ScalaActionsComposition]]
    1. [[Content negotiation | ScalaContentNegotiation]]
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
    1. [[Play Json Basics | ScalaJson]]
    1. [[Json Reads/Writes/Format Combinators | ScalaJsonCombinators]]
    1. [[Json Transformers | ScalaJsonTransformers]]
    1. [[Json Macro Inception | ScalaJsonInception]]
    1. [[Handling and serving Json requests | ScalaJsonRequests]]
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
    1. [[レスポンスの操作 | ScalaResults]]
    1. [[セッションとフラッシュスコープ | ScalaSessionFlash]]
    1. [[ボディパーサー | ScalaBodyParsers]]
    1. [[アクションの合成 | ScalaActionsComposition]]
    1. [[コンテントネゴシエーション | ScalaContentNegotiation]]
1. [[非同期 HTTP プログラミング | ScalaAsync]]
    1. [[非同期レスポンスの処理 | ScalaAsync]]
    1. [[HTTP レスポンスのストリーミング | ScalaStream]]
    1. [[Comet | ScalaComet]]
    1. [[WebSocket | ScalaWebSockets]]
1. [[テンプレート・エンジン | ScalaTemplates]]
    1. [[テンプレートの文法 | ScalaTemplates]]
    1. [[よくある使い方 | ScalaTemplateUseCases]]
1. [[HTTP フォーム送信とバリデーション | ScalaForms]]
    1. [[フォームの定義 | ScalaForms]]
    1. [[フォームテンプレートヘルパの利用 | ScalaFormHelpers]]
1. [[JSON を使う | ScalaJson]]
    1. [[Play での Json の基本 | ScalaJson]]
    1. [[Json の読み/書き/フォーマットの結合 | ScalaJsonCombinators]]
    1. [[Json の変換 | ScalaJsonTransformers]]
    1. [[Json マクロ入門 | ScalaJsonInception]]
    1. [[Json リクエストの処理と供給 | ScalaJsonRequests]]
1. [[XML を使う | ScalaXmlRequests]]
    1. [[XML リクエストの処理と送信 | ScalaXmlRequests]]
1. [[ファイルアップロード処理 | ScalaFileUpload]]
    1. [[multipart/form-data のアップロード | ScalaFileUpload]]
1. [[SQL データベースアクセス | ScalaDatabase]]
    1. [[JDBC の設定と利用 | ScalaDatabase]]
    1. [[Anorm によるデータベースアクセス | ScalaAnorm]]
    1. [[データベースアクセスライブラリの利用 | ScalaDatabaseOthers]]
1. [[キャッシュを使う | ScalaCache]]
    1. [[Play の Cache API | ScalaCache]]
1. [[Web サービスの呼び出し | ScalaWS]]
    1. [[Play WS API | ScalaWS]]
    1. [[OpenID サーバへの接続 | ScalaOpenID]]
    1. [[OAuth によリ保護されたデータへのアクセス | ScalaOAuth]]
1. [[Akka との統合 | ScalaAkka]]
    1. [[アクターの設定と非同期タスクのスケジューリング | ScalaAkka]]
1. [[国際化 | ScalaI18N]]
    1. [[メッセージの外部化と i18n | ScalaI18N]]
1. [[アプリケーション Global オブジェクト | ScalaGlobal]]
    1. [[アプリケーションの全般設定 | ScalaGlobal]]
    1. [[リクエストのインターセプト | ScalaInterceptors]]
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
1. [[HTTP Architecture | HttpApi]]
    1. [[HTTP API | HttpApi]]
    1. [[HTTP Filters | ScalaHttpFilters]]
1. [[Dependency Injection | ScalaDependencyInjection]]
    1. [[Controller Injection | ScalaDependencyInjection]]
    1. [[Example Projects | ScalaDependencyInjection]]
1. [[Reverse routing | ScalaJavascriptRouting]]
    1. [[Javascript Routing | ScalaJavascriptRouting]]
1. [[Extending Play|ScalaPlugins]]
    1. [[Writing Plugins|ScalaPlugins]]
-->
1. [[反応的なストリーム処理 | Iteratees]]
    1. [[Iteratee | Iteratees]]
    1. [[Enumerator | Enumerators]]
    1. [[Enumeratee | Enumeratees]]
1. [[HTTP アーキテクチャ | HttpApi]]
    1. [[HTTP API | HttpApi]]
    1. [[HTTP フィルター | ScalaHttpFilters]]
1. [[依存性の注入 | ScalaDependencyInjection]]
    1. [[コントローラの注入 | ScalaDependencyInjection]]
    1. [[プロジェクトの例 | ScalaDependencyInjection]]
1. リバースルーティング
    1. [[Javascriptのルーティング | ScalaJavascriptRouting]]
1. [[Play を拡張する|ScalaPlugins]]
    1. [[プラグインの書き方|ScalaPlugins]]

<!--
## Tutorials
-->
## チュートリアル

<!--
1. [[Your first application | ScalaTodoList]]
-->
1. [[はじめてのアプリケーション | ScalaTodoList]]
