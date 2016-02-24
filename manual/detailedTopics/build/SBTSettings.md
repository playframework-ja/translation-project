<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# About SBT Settings
-->
# SBT 設定について

<!--
## About sbt settings
-->
## sbt 設定について

<!--
The `build.sbt` file defines settings for your project. You can also define your own custom settings for your project, as described in the [sbt documentation](http://www.scala-sbt.org).  In particular, it helps to be familiar with the [settings](http://www.scala-sbt.org/release/docs/Getting-Started/More-About-Settings) in sbt.
-->
`build.sbt` ファイルはプロジェクトの設定を定義します。[sbt ドキュメント](http://www.scala-sbt.org) に記述されているように、プロジェクト独自のカスタム設定を定義することも可能です。特に sbt の [セッティング](http://www.scala-sbt.org/release/docs/Getting-Started/More-About-Settings) について慣れる事は有効です。

<!--
To set a basic setting, use the `:=` operator:
-->
基本的な設定を行うには、 `:=` 演算子を使います:

```scala
confDirectory := "myConfFolder"     
```

<!--
## Default settings for Java applications
-->
## Java アプリケーション向けのデフォルト設定

<!--
Play defines a default set of settings suitable for Java-based applications. To enable them add the `PlayJava` plugin via your project's enablePlugins method. These settings mostly define the default imports for generated templates e.g. importing `java.lang.*` so types like `Long` are the Java ones by default instead of the Scala ones. `play.Project.playJavaSettings` also imports `java.util.*` so that the default collection library will be the Java one.
-->
Play は Java ベースのアプリケーションに適したデフォルト設定のセットを定義しています。これらを有効にするには、プロジェクトの enablePlugins メソッドで `PlayJava` プラグインを追加してください。これらの設定は、例えば `java.lang.*` のようなデフォルトのインポートを生成されたテンプレート用に定義するので、Long のような型はデフォルトで Scala のものではなく、Java のものになります。`play.Project.playJavaSettings` は `java.util.*` もインポートするので、デフォルトのコレクションライブラリは Java のものになります。

<!--
## Default settings for Scala applications
-->
## Scala アプリケーション向けのデフォルト設定

<!--
Play defines a default set of settings suitable for Scala-based applications. To enable them add the `PlayScala` plugin via your project's enablePlugins method. These default settings define the default imports for generated templates (such as internationalized messages, and core APIs).
-->
Play は Scala ベースのアプリケーションに適したデフォルト設定のセットを定義しています。これらを有効にするには、プロジェクトの enablePlugins メソッドで `PlayScala` プラグインを追加してください。これらの設定は、(例えば国際化されたメッセージやコア API のような) デフォルトのインポートを、生成されたテンプレート用に定義します。