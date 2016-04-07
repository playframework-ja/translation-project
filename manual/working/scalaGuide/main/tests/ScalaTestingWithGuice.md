<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Testing with Guice
-->
# Guice を使用したテスト

<!--
If you're using Guice for [[dependency injection|ScalaDependencyInjection]] then you can directly configure how components and applications are created for tests. This includes adding extra bindings or overriding existing bindings.
-->
Guice を [[依存性注入|ScalaDependencyInjection]] に使用している場合は、コンポーネントやアプリケーションがテストのためにどのように生成されるかを直接設定することができます。これはバインディングを追加したり、既存のバインディングをオーバーライドすることも含みます。

<!--
## GuiceApplicationBuilder
-->
## GuiceApplicationBuilder

<!--
[GuiceApplicationBuilder](api/scala/play/api/inject/guice/GuiceApplicationBuilder.html) provides a builder API for configuring the dependency injection and creation of an [Application](api/scala/play/api/Application.html).
-->
[GuiceApplicationBuilder](api/scala/play/api/inject/guice/GuiceApplicationBuilder.html) は [Application](api/scala/play/api/Application.html) の生成と依存性注入の設定のためのビルダー API を提供します。

<!--
### Environment
-->
### Environment

<!--
The [Environment](api/scala/play/api/Environment.html), or parts of the environment such as the root path, mode, or class loader for an application, can be specified. The configured environment will be used for loading the application configuration, it will be used when loading modules and passed when deriving bindings from Play modules, and it will be injectable into other components.
-->
[Environment](api/scala/play/api/Environment.html) 、すなわちアプリケーションのルートパスやモード、クラスローダなどの環境のパーツを定義できます。設定された環境はアプリケーションの設定をロードするのに使用されます (アプリケーションの設定はモジュールをロードする際に使用され、Play のモジュールからバインディングを引き出す際に使用されます。また、他のコンポーネントに注入することもできます)。 

@[builder-imports](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)
@[set-environment](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)
@[set-environment-values](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)

<!--
### Configuration
-->
### 設定

<!--
Additional configuration can be added. This configuration will always be in addition to the configuration loaded automatically for the application. When existing keys are used the new configuration will be preferred.
-->
設定を追加することもできます。この設定は、アプリケーションに自動的にロードされる設定に加えてつねに有効です。既にキーが使用されている場合は新しい設定が優先されます。

@[add-configuration](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)

<!--
The automatic loading of configuration from the application environment can also be overridden. This will completely replace the application configuration. For example:
-->
設定のアプリケーションの環境からの自動ロードは、上書きされる可能性があります。これはアプリケーションの設定に完全に置き換えることができます。

@[override-configuration](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)

<!--
### Bindings and Modules
-->
### バインディングとモジュール

<!--
The bindings used for dependency injection are completely configurable. The builder methods support [[Play Modules and Bindings|ScalaDependencyInjection]] and also Guice Modules.
-->
依存性注入に使用されるバインディングは完全に設定可能です。ビルダーメソッドは [[Play のモジュールとバインディング|ScalaDependencyInjection]] あるいは Guice のモジュールをサポートしています。

<!--
#### Additional bindings
-->
#### 追加のバインディング

<!--
Additional bindings, via Play modules, Play bindings, or Guice modules, can be added:
-->
Play のモジュール、Play のバインディングあるいは Guice のモジュールから、バインディングを追加することができます。

@[bind-imports](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)
@[add-bindings](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)

<!--
#### Override bindings
-->
#### バインディングのオーバーライド

<!--
Bindings can be overridden using Play bindings, or modules that provide  bindings. For example:
-->
Play のバインディングあるいはバインディングを提供するモジュールを使って、バインディングをオーバーライドすることもできます。

@[override-bindings](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)

<!--
#### Disable modules
-->
#### モジュールを無効化する

<!--
Any loaded modules can be disabled by class name:
-->
いかなるモジュールもクラス名を指定して無効化できます。

@[disable-modules](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)

<!--
#### Loaded modules
-->
#### ロードされたモジュール

<!--
Modules are automatically loaded from the classpath based on the `play.modules.enabled` configuration. This default loading of modules can be overridden. For example:
-->
モジュールは `play.modules.enabled` 設定に従い、クラスパスから自動的にロードされます。このようなモジュールのデフォルトのロードは上書きできます。

@[load-modules](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)


<!--
## GuiceInjectorBuilder
-->
## GuiceInjectorBuilder

<!--
[GuiceInjectorBuilder](api/scala/play/api/inject/guice/GuiceInjectorBuilder.html) provides a builder API for configuring Guice dependency injection more generally. This builder does not load configuration or modules automatically from the environment like `GuiceApplicationBuilder`, but provides a completely clean state for adding configuration and bindings. The common interface for both builders can be found in [GuiceBuilder](api/scala/play/api/inject/guice/GuiceBuilder.html). A Play [Injector](api/scala/play/api/inject/Injector.html) is created. Here's an example of instantiating a component using the injector builder:
-->
[GuiceInjectorBuilder](api/scala/play/api/inject/guice/GuiceInjectorBuilder.html) は、Guice の依存性注入をより一般的に設定するビルダー API を提供します。このビルダーは `GuiceApplicationBuilder` のように自動的には環境から設定やモジュールをロードしませんが、設定やバインディングを追加するのに完全にクリーンな状態を提供します。それぞれのビルダーに共通のインターフェースは [GuiceBuilder](api/scala/play/api/inject/guice/GuiceBuilder.html) にあります。Play の [Injector](api/scala/play/api/inject/Injector.html) が生成されます。以下は injector ビルダーを使用したコンポーネントをインスタンス化する例です。

@[injector-imports](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)
@[bind-imports](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)
@[injector-builder](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)


<!--
## Overriding bindings in a functional test
-->
## 機能テストにおけるバインディングのオーバーライド

<!--
Here is a full example of replacing a component with a mock component for testing. Let's start with a component, that has a default implementation and a mock implementation for testing:
-->
これはテスト用のモックコンポーネントで置き換える例です。標準の実装を持ち、テスト用にはモック実装を持つコンポーネントを使ってみましょう。

@[component](code/tests/guice/Component.scala)

<!--
This component is loaded automatically using a module:
-->
コンポーネントはモジュールを利用して自動的にロードされます。

@[component-module](code/tests/guice/Component.scala)

<!--
And the component is used in a controller:
-->
そしてコンポーネントはコントローラに利用されます。

@[controller](code/tests/guice/controllers/Application.scala)

<!--
To build an `Application` to use in functional tests we can simply override the binding for the component:
-->
`Application` をビルドして機能テストの中で使用するためには、コンポーネントのバインディングを単純にオーバーライドすることができます。

@[builder-imports](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)
@[bind-imports](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)
@[override-bindings](code/tests/guice/ScalaGuiceApplicationBuilderSpec.scala)

<!--
The created application can be used with the functional testing helpers for [[Specs2|ScalaFunctionalTestingWithSpecs2]] and [[ScalaTest|ScalaFunctionalTestingWithScalaTest]].
-->
生成されたアプリケーションは [[Specs2|ScalaFunctionalTestingWithSpecs2]] や [[ScalaTest|ScalaFunctionalTestingWithScalaTest]] のための機能テストヘルパーとともに使用することができます。
