<!--
# Dependency Injection
-->
# 依存性の注入

<!--
Play does not use any dependency injection framework under the hood, and leaves the choice of DI framework (if any) in your hands.  Models, Services and Configuration objects can all be handled transparently, and do not need explicit configuration to work in Play.
-->
Play はフレームワーク自身の内部では一切の依存性の注入を使用せず、 (必要な) DI フレームワークを使用するかどうかの選択を使用者に委ねています。モデル、サービス、コンフィグオブジェクトは全て透過的に操作可能で、Play で作業する上で明示的なコンフィグは不要です。

<!--
There is one case which requires explicit configuration, which involves how controllers (which are singleton objects by default) interact with routes.
-->
明示的なコンフィグが必須となるのは、コントローラーがどのようにルートと相互作用するかを設定する場合だけです。

<!--
## Controller Injection
-->
## コントローラーの注入

<!--
In Play, routes which start with `@` are managed by [play.api.GlobalSettings#getControllerInstance](api/scala/index.html#play.api.GlobalSettings),
-->
Play では、 `@` から始まるルートは [play.api.GlobalSettings#getControllerInstance](api/scala/index.html#play.api.GlobalSettings) で管理されます。

<!--
Given the following route definition:
-->
下記のルート定義がされている場合 : 

@[di-routes](code/scalaguide.advanced.dependencyinjection.routes)

<!--
Then you can manage controller class instantiation using a DI framework by overriding `getControllerInstance` in your application's `Global` class:
-->
DI フレームワークを使用したコントローラークラスのインスタンス化は、アプリケーションの `Global` クラスの `getControllerInstance` をオーバーライドすることによって管理できます。

@[di-global](code/ControllerInjection.scala)

<!--
## Example Projects
-->
## プロジェクト設定例

<!--
The pace of development and the myriad of options even within a single DI framework means that full documentation is beyond the scope of this documentation.  However, there are a number of sample projects that demonstrate how to best leverage DI in your project.
-->
DI フレームワークはその開発速度の早さや多岐に渡る使い道を考慮すると、たとえ一つのフレームワークを対象としたとしてもこのドキュメントでは全部を網羅できません。ですが、いくつかの DI を最大限に活用するためのサンプルがあります。

### Activator

<!--
[Typesafe Activator](http://www.typesafe.com/activator) is a local web & command-line tool that helps developers get started with the Typesafe Platform.  Using Activator is highly recommended, as you can download a number of sample projects at once and walk through tutorials presented through the Activator UI.
-->
[Typesafe Activator](http://www.typesafe.com/activator) は開発者が Typesafe のプラットフォームを使い始めるのを支援する、ローカル web またはコマンドライン用ツールです。一度に複数のサンプルがダウンロードでき、 Activator のUI上でのチュートリアルも用意されているため、 Activator を使用することを強く推奨しています。

<!--
### Spring
-->
### Spring

<!--
[Spring](http://www.springsource.org/) is a popular application development for Java that has a dependency injection framework at its core.  There is also an additional project [Spring Scala](https://github.com/SpringSource/spring-scala), which provides additional integration options using Scala and Spring.
-->
[Spring](http://www.springsource.org/) はそのコアに依存性注入フレームワークを持つ Java の有名なフレームワークです。また、 [Spring Scala](https://github.com/SpringSource/spring-scala) という Scala と Spring の統合を提供するオプションもあります。

<!--
There is an [Activator](http://www.typesafe.com/activator) project available for Spring.  You can download it from Activator [directly](http://typesafe.com/activator/template/play-spring-data-jpa), or clone it from [https://github.com/typesafehub/play-spring-data-jpa](https://github.com/typesafehub/play-spring-data-jpa).
-->
Spring は [Activator](http://www.typesafe.com/activator) プロジェクトが利用可能です。 Activator から [ダウンロード](http://typesafe.com/activator/template/play-spring-data-jpa) 、または [https://github.com/typesafehub/play-spring-data-jpa](https://github.com/typesafehub/play-spring-data-jpa) から clone してください。

<!--
### Subcut
-->
### Subcut

<!--
[Subcut](https://github.com/dickwall/subcut/blob/master/GettingStarted.md) is a lightweight dependency injection framework written for Scala that uses implicits to pass configuration through injectable classes.
-->
[Subcut](https://github.com/dickwall/subcut/blob/master/GettingStarted.md) は Scala 用に書かれた軽量依存性注入フレームワークで、注入可能なクラスのパス設定を暗黙的に行うために使用されます。

<!--
There is a Github project by the Subcut team that shows how to integrate Subcut with Play.  You can clone it from [https://github.com/dickwall/play-subcut](https://github.com/dickwall/play-subcut) and it is also an Activator project.
-->
Github 上では Subcut チームが、どのように Subcat と Play を統合するかを例示しています。 [https://github.com/dickwall/play-subcut](https://github.com/dickwall/play-subcut) から clone 可能です。また、 Activator プロジェクトも存在しています。

<!--
### Macwire
-->
### Macwire

<!--
[Macwire](https://github.com/adamw/macwire) is a lightweight dependency injection framework that uses Scala macros.
-->
[Macwire](https://github.com/adamw/macwire) は Scala のマクロを使用した軽量依存性注入フレームワークです。


<!--
There is an [Activator](http://www.typesafe.com/activator) project available for Macwire.  You can download it from Activator [directly](http://typesafe.com/activator/template/macwire-activator), or clone it from [https://github.com/adamw/macwire-activator](https://github.com/adamw/macwire-activator).
-->
Macwire は [Activator](http://www.typesafe.com/activator) プロジェクトが利用可能です。Activator から [ダウンロード](http://typesafe.com/activator/template/macwire-activator) 、または [https://github.com/adamw/macwire-activator](https://github.com/adamw/macwire-activator) から clone してください。

<!--
### Guice
-->
### Guice

<!--
[Guice](https://code.google.com/p/google-guice/) is a lightweight dependency injection framework designed for Java.
-->
[Guice](https://code.google.com/p/google-guice/) は Java 向けにデザインされた軽量依存性注入フレームワークです。

<!--
There is an [Activator](http://www.typesafe.com/activator) project available for Guice.  You can download it from Activator [directly](http://typesafe.com/activator/template/play-guice), or clone it from [https://github.com/typesafehub/play-guice](https://github.com/typesafehub/play-guice).
-->
Guice は [Activator](http://www.typesafe.com/activator) プロジェクトが利用可能です。Activator から [ダウンロード](http://typesafe.com/activator/template/play-guice) 、または [https://github.com/typesafehub/play-guice](https://github.com/typesafehub/play-guice) から clone してください。
