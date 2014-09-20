<!--
# Dependency Injection
-->
# 依存性の注入

<!--
Play does not use any dependency injection framework under the hood, and leaves the choice of DI framework (if any) in your hands.  Models, Services and Configuration objects can all be handled transparently, and do not need explicit configuration to work in Play.
-->
Play は依存性の注入のフレームワークを見えないところでは使っていません。そして、 （もし必要なら） DI  フレームワークの選択権をあなたの手に委ねています。
モデル層、サービス層、設定ファイルなど全て率直に扱え、明示的な設定をするような労力は Play を使う上では必要ありません。

<!--
There is one case which requires explicit configuration, which involves how controllers (which are singleton objects by default) interact with routes.
-->
routes と（標準でシングルトンの）コントローラーがどう影響しあうのかに関しての明示的な設定を必要とするケースがあります。

<!--
## Controller Injection
-->
## コントローラーの注入

<!--
In Play, routes which start with `@` are managed by [play.api.GlobalSettings#getControllerInstance](api/scala/index.html#play.api.GlobalSettings),
-->
Play で `@`で始まる routes は、[play.api.GlobalSettings#getControllerInstance](api/scala/index.html#play.api.GlobalSettings)  で制御されています。

<!--
Given the following route definition:
-->
以下のように route の定義が与えられます。

@[di-routes](code/scalaguide.advanced.dependencyinjection.routes)

<!--
Then you can manage controller class instantiation using a DI framework by overriding `getControllerInstance` in your application's `Global` class:
-->
アプリケーションの`Global` クラス で、 `getControllerInstance` オーバーライドした DI フレームワークを使ったコントローラークラスのインスタンスを扱えます。

@[di-global](code/ControllerInjection.scala)

<!--
## Example Projects
-->
## プロジェクト例

<!--
The pace of development and the myriad of options even within a single DI framework means that full documentation is beyond the scope of this documentation.  However, there are a number of sample projects that demonstrate how to best leverage DI in your project.
-->
一つの DI フレームワークにしても、開発の早さと無数のオプションがあるため、 ここだけのドキュメントでは足りず、フルのドキュメントが必要であることを意味します。
しかしながら、世にあふれるサンプルプロジェクトは DI をどう使ったら最も効果的なのかを見せてくれます。


### Activator

<!--
[Typesafe Activator](http://www.typesafe.com/activator) is a local web & command-line tool that helps developers get started with the Typesafe Platform.  Using Activator is highly recommended, as you can download a number of sample projects at once and walk through tutorials presented through the Activator UI.
-->
[Typesafe Activator](http://www.typesafe.com/activator) は、開発者が Typesafe のプラットフォームでスタートする手助けをするローカルウェブとコマンドラインのツールです。
この Activator を使うことを、強く薦めます。なぜなら、たくさんのサンプルプロジェクトを一度にダウンロードでき、 Activator の UI で表現されたチュートリアルを進めることが出来るからです。


### Spring

<!--
[Spring](http://www.springsource.org/) is a popular application development for Java that has a dependency injection framework at its core.  There is also an additional project [Spring Scala](https://github.com/SpringSource/spring-scala), which provides additional integration options using Scala and Spring.
-->
[Spring](http://www.springsource.org/) は Java において人気のある開発ツールで、依存性の注入のフレームワークをコアとして持っています。
[Spring Scala](https://github.com/SpringSource/spring-scala) は、Scala と Spring を使った付加的なオプションを提供しているプロジェクトです。

<!--
There is an [Activator](http://www.typesafe.com/activator) project available for Spring.  You can download it from Activator [directly](http://typesafe.com/activator/template/play-spring-data-jpa), or clone it from [https://github.com/typesafehub/play-spring-data-jpa](https://github.com/typesafehub/play-spring-data-jpa).
-->
[Activator](http://www.typesafe.com/activator) プロジェクトは、 Spring で使えます。
[directly](http://typesafe.com/activator/template/play-spring-data-jpa) からダウンロードできます。
[https://github.com/typesafehub/play-spring-data-jpa](https://github.com/typesafehub/play-spring-data-jpa) からも clone することも出来ます。


### Subcut

<!--
[Subcut](https://github.com/dickwall/subcut/blob/master/GettingStarted.md) is a lightweight dependency injection framework written for Scala that uses implicits to pass configuration through injectable classes.
-->
[Subcut](https://github.com/dickwall/subcut/blob/master/GettingStarted.md) は、軽量の Scala で書かれた依存性注入のフレームワークで、依存できるクラスを設定なしに暗黙的に使えます。

<!--
There is a Github project by the Subcut team that shows how to integrate Subcut with Play.  You can clone it from [https://github.com/dickwall/play-subcut](https://github.com/dickwall/play-subcut) and it is also an Activator project.
-->
Play の中でどのように Subcut を使うのかを示している Subcut チームによる Github プロジェクトがあります。
[https://github.com/dickwall/play-subcut](https://github.com/dickwall/play-subcut) から clone することができ、これは Activator のプロジェクトでもあります。

### Macwire

<!--
[Macwire](https://github.com/adamw/macwire) is a lightweight dependency injection framework that uses Scala macros.
-->
[Macwire](https://github.com/adamw/macwire) は、 Scala マクロで書かれた軽量の依存性注入のフレームワークです。

<!--
There is an [Activator](http://www.typesafe.com/activator) project available for Macwire.  You can download it from Activator [directly](http://typesafe.com/activator/template/macwire-activator), or clone it from [https://github.com/adamw/macwire-activator](https://github.com/adamw/macwire-activator).
-->
[Activator](http://www.typesafe.com/activator) プロジェクトで Macwire は利用出来ます。
[directly](http://typesafe.com/activator/template/macwire-activator) からダウンロード出来ます。
また、 [https://github.com/adamw/macwire-activator](https://github.com/adamw/macwire-activator) からも clone 出来ます。

### Guice

<!--
[Guice](https://code.google.com/p/google-guice/) is a lightweight dependency injection framework designed for Java.
-->
[Guice](https://code.google.com/p/google-guice/) は、Java用に設計された軽量の依存性注入のフレームワークです。

<!--
There is an [Activator](http://www.typesafe.com/activator) project available for Guice.  You can download it from Activator [directly](http://typesafe.com/activator/template/play-guice), or clone it from [https://github.com/typesafehub/play-guice](https://github.com/typesafehub/play-guice).
-->
Guice は、 [Activator](http://www.typesafe.com/activator) プロジェクトから利用出来ます。
また、 [directly](http://typesafe.com/activator/template/play-guice) からダウンロード出来ますし、
[https://github.com/typesafehub/play-guice](https://github.com/typesafehub/play-guice) から clone も出来ます。
