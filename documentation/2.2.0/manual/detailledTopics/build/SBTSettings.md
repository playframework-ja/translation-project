<!-- translated -->
<!--
# About SBT Settings
-->
# SBT セッティングについて

<!--
## About sbt settings
-->
## sbt セッティングについて

<!-- The `build.sbt` file defines settings for your project. You can also define your own custom settings for your project, as described in the [sbt documentation](http://www.scala-sbt.org).  In particular, it helps to be familiar with the [settings](http://www.scala-sbt.org/release/docs/Getting-Started/More-About-Settings) in sbt. -->
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
## Java アプリケーション向けのデフォルトセッティング

<!-- Play defines a default set of settings suitable for Java-based applications. To enable them add the `playJavaSettings` set of settings to your `build.sbt` file. These settings mostly define the default imports for generated templates e.g. importing `java.lang.*` so types like `Long` are the Java ones by default instead of the Scala ones. `playJavaSettings` also imports `java.util.*` so that the default collection library will be the Java one. -->
Play は Java ベースのアプリケーションに適したデフォルト設定のセットを定義しています。これらを有効にするには、`build.sbt` ファイルに設定の `playJavaSettings` セットを追加してください。これらの設定は、生成されるテンプレートのために例えば `java.lang.*` のようなデフォルトのインポートを定義するので、`Long` のようにタイプするとデフォルトで Scala の Long ではなく Java のものになります。`playJavaSettings` は `java.util.*` もインポートするので、デフォルトのコレクションライブラリは Java のものになります。

<!--
## Default settings for Scala applications
-->
## Scala アプリケーション向けのデフォルトセッティング

<!-- Play defines a default set of settings suitable for Scala-based applications. To enable them add the `playScalaSettings` set of settings to your `build.sbt` file. These default settings define the default imports for generated templates (such as internationalized messages, and core APIs). -->
Play は Scala ベースのアプリケーションに適したデフォルト設定のセットを定義しています。これらを有効にするには、`build.sbt` ファイルに設定の `playScalaSettings` セットを追加してください。これらの設定は、生成されるテンプレートのために (国際化されたメッセージやコア API などの) デフォルトのインポートを定義します。

<!--
## Play project settings with their default value
-->
## デフォルト値の Play プロジェクトセッティング

<!-- When you define your sbt project using the default settings explained above, use sbt's `settings` command via the play console for your project (the console is obtained by invoking the `play` command from the command line). You can then further show the value of a setting by using the sbt `show` command e.g. `show name` will output the project's name. -->
プロジェクトの sbt セッティングを上記で説明したデフォルトの設定を使って定義する場合は、play コンソールで sbt の `settings` コマンドを使用してください (コンソールはコマンドラインから `play` コマンドを起動することで得られます) 。その後、sbt の `show` コマンドを使用して設定の値まで見ることができます。例えば、`show name` はプロジェクトの名前を出力します。

<!--
> **Next:** [[Managing library dependencies | SBTDependencies]]
-->
> **Next:** [[ライブラリの依存性管理 | SBTDependencies]]
