<!-- translated -->
<!--
# Play 2.0 documentation
-->
# Play 2.0 ドキュメント

<!--
> Play 2.0 is a high-productivity Java and Scala web application framework that integrates the components and APIs you need for modern web application development. 
>
> Play is based on a lightweight, stateless, web-friendly architecture and features predictable and minimal resource consumption (CPU, memory, threads) for highly-scalable applications thanks to its reactive model, based on Iteratee IO.> 
-->
>Play 2.0 は、現代の web アプリケーション開発に必要なコンポーネント及び API を統合した生産性の高い Java と Scala の web アプリケーションフレームワークです。
>
> Play の特徴は、ライトウェイト、ステートレス、web フレンドリーなアーキテクチャであること、機能予測のしやすさです。また、Iteratee IO をベースにしたリアクティブモデルのおかげで、スケーラブルなアプリケーションでも CPU、メモリ、スレッドなどのリソース消費が最小限になっています。

<!--
## About
-->
## Play2.0について

<!--
1. [[Play 2.0 a Web framework for a new era | Philosophy]]
-->
1. [[新時代の Web フレームワーク Play 2.0 | Philosophy]]

<!--
## Getting started
-->
## はじめに

<!--
1. [[Installing Play 2.0 | Installing]]
1. [[Creating a new application | NewApplication]]
1. [[Anatomy of a Play 2.0 application | Anatomy]]
1. [[Using the Play 2.0 console | PlayConsole ]]
1. [[Setting-up your preferred IDE | IDE]]
1. [[Sample applications | Samples]]
-->
1. [[Play 2.0 のインストール | Installing]]
1. [[新規アプリケーションを作成する | NewApplication]]
1. [[Play 2.0 アプリケーションの構造 | Anatomy]]
1. [[Play 2.0 コンソールを使う | PlayConsole ]]
1. [[好きな IDE で開発する | IDE]]
1. [[サンプルアプリケーション | Samples]]

<!--
## Working with Play 2.0
-->
## Play 2.0 で開発する

<!--
1. [[Play 2.0 for Scala developers | ScalaHome]]
1. [[Play 2.0 for Java developers | JavaHome]]
-->
1. [[Scala 開発者のための Play 2.0 | ScalaHome]]
1. [[Java 開発者のための Play 2.0 | JavaHome]]

<!--
## Detailed topics
-->
## 詳細なトピック

<!--
1. [[The Build system | Build]]
    1. [[About sbt settings | SBTSettings]]
    1. [[Manage application dependencies | SBTDependencies]]
    1. [[Working with sub-projects | SBTSubProjects]]
1. [[Working with public assets | Assets]]
    1. [[Using CoffeeScript | AssetsCoffeeScript]]
    1. [[Using LESS CSS | AssetsLess]]
    1. [[Using Google Closure Compiler | AssetsGoogleClosureCompiler]]
1. [[Managing database evolutions | Evolutions]]
1. [[Configuration file syntax and features | Configuration]]
    1. [[Configuring the JDBC connection pool | SettingsJDBC]]
    1. [[Configuring the internal Akka system | AkkaCore]]
    1. [[Configuring logging | SettingsLogger]]
1. [[Deploying your application | Production]]
    1. [[Creating a standalone version of your application | ProductionDist]]
    1. [[Additional configuration | ProductionConfiguration]]
    1. [[Deploying to Heroku | ProductionHeroku]]
    1. [[Set-up a front-end HTTP server | HTTPServer]]
-->
1. [[ビルドシステム | Build]]
    1. [[sbt 設定について | SBTSettings]]
    1. [[ライブラリ依存性の管理 | SBTDependencies]]
    1. [[サブプロジェクトで作業する | SBTSubProjects]]
1. [[公開アセットを使う | Assets]]
    1. [[CoffeeScript を使う | AssetsCoffeeScript]]
    1. [[LESS CSS を使う | AssetsLess]]
    1. [[Google Closure Compiler を使う | AssetsGoogleClosureCompiler]]
1. [[Evolutions でデータベースを管理する | Evolutions]]
1. [[設定ファイルのシンタックスと機能 | Configuration]]
    1. [[JDBC コネクションプールの設定 | SettingsJDBC]]
    1. [[内部 Akka システムの設定 | AkkaCore]]
    1. [[ログの設定 | SettingsLogger]]
1. [[アプリケーションのデプロイ | Production]]
    1. [[スタンドアローンアプリケーションを作成する | ProductionDist]]
    1. [[追加設定 | ProductionConfiguration]]
    1. [[Heroku へデプロイする | ProductionHeroku]]
    1. [[フロントエンド HTTP サーバの設定 | HTTPServer]]

<!--
## Additional documentation
-->
## その他のドキュメント

<!--
1. [[Scala|http://docs.scala-lang.org/]]
1. [[Akka|http://akka.io/docs/]]
1. [[sbt|http://www.scala-sbt.org/learn.html]]
1. [[Configuration|https://github.com/typesafehub/config]]
1. [[Logback|http://logback.qos.ch/documentation.html]]
-->
1. [[Scala|http://docs.scala-lang.org/]]
1. [[Akka|http://akka.io/docs/]]
1. [[sbt|http://www.scala-sbt.org/learn.html]]
1. [[Configuration|https://github.com/typesafehub/config]]
1. [[Logback|http://logback.qos.ch/documentation.html]]

<!--
## Hacking Play 2.0
-->
## Play 2.0 を Hack する

<!--
1. [[Building Play 2.0 from source | BuildingFromSource]]
1. [[CI server at Cloudbees | CIServer]]
1. [[Repositories | Repositories]]
1. [[Issue tracker | Issues]]
1. [[Pull requests | PullRequests]]
1. [[Contributor guidelines | Guidelines]]
-->
1. [[ソースから Play 2.0 をビルドする | BuildingFromSource]]
1. [[Cloudbees における CI サーバ | CIServer]]
1. [[リポジトリ | Repositories]]
1. [[課題トラッカ | Issues]]
1. [[プルリクエスト | PullRequests]]
1. [[このプロジェクトへの貢献用ガイドライン | Guidelines]]

<!--
## Modules and plugins
-->
## モジュールとプラグイン

<!--
1. [[Temporary modules directory | Modules]]
-->
1. [[一時的なモジュールディレクトリ | Modules]]

<!--
## Contributing to this documentation
-->
## このドキュメントへの貢献

<!--
> The documentation is freely editable on [[https://github.com/playframework/Play20/wiki]]. Feel free to fix mistakes directly in the pages. 
>
> However if you want to provide additional documentation, please create new pages and reference them from here. A member of the core team will take care of integrating it in the most appropriate section.
-->
> このドキュメントは [[https://github.com/playframework-ja/Play20/wiki]] 上において自由に編集することができます。お気軽に wiki 上のページ内から間違いを直接修正してください。
> 
> 追加のドキュメントを提供したい場合は、新しいページとそれらへの参照を wiki 上に作成してください。コアチームがもっとも適当なセクションへの統合を検討します。
