<!-- translated -->
<!--
# About SBT Settings
-->
# SBT セッティングについて

<!--
## About sbt settings
-->
## sbt セッティングについて

<!-- The sbt build script defines settings for your project. You can also define you own custom settings for your project, as described in the [[sbt documentation | https://github.com/harrah/xsbt/wiki]]. -->
sbt　ビルドスクリプトはプロジェクトの設定を定義します。[[sbt ドキュメント | https://github.com/harrah/xsbt/wiki]] に記述されているように、プロジェクト独自のカスタム設定を定義することも可能です。

<!--
To set a basic setting, use the `:=` operator:
-->
基本的な設定を行うには、 `:=` 演算子を使います:

```scala
val main = PlayProject(appName, appVersion, appDependencies).settings(
  confDirectory := "myConfFolder"     
)
```

<!--
## Default settings for Java applications
-->
## Java アプリケーション向けのデフォルトセッティング

<!--
Play 2.0 defines a default set of settings suitable for Java-based applications. To enable them add the `defaultJavaSettings` set of settings to your application definition:
-->
Play 2.0 は Java ベースのアプリケーションに適したデフォルト設定のセットを定義しています。これらを有効にするには、アプリケーション定義に設定の `defaultJavaSettings` セットを追加してください:

```scala
val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA)
```

<!--
These default settings mostly define the default imports for generated templates. For example, it imports `java.lang.*`, so types like `Long` are the Java ones by default instead of the Scala ones. It also imports `java.util.*` so the default collection library will be the Java one.
-->
これらのデフォルト設定は、生成されるテンプレート向けに大部分のデフォルト import 文を定義します。例えば `java.lang.*` をインポートするので、`Long` のような型はデフォルトで Scala のものではなく Java のものになります。`java.util.*` もインポートするので、デフォルトのコレクションライブラリは Java のものになります。


<!--
## Default settings for Scala applications
-->
## Scala アプリケーション向けのデフォルトセッティング

<!--
Play 2.0 defines a default set of settings suitable for Scala-based applications. To enable them add the `defaultScalaSettings` set of settings to your application definition:
-->
Play 2.0 は Scala ベースのアプリケーションに適したデフォルト設定のセットを定義しています。これらを有効にするには、アプリケーション定義に設定の `defaultScalaSettings` セットを追加してください:

```scala
val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA)
```

<!--
These default settings define the default imports for generated templates (such as internationalized messages, and core APIs).
-->
これらのデフォルト設定は、生成されるテンプレート向けにデフォルト import 文 (国際化メッセージや、主要な API) を定義します。

<!--
## Play project settings with their default value
-->
## デフォルト値の Play プロジェクトセッティング   

<!--
When you define your sbt project using `PlayProject` instead of `Project`, you will get a default set of settings. Here is the default configuration:
-->
`Project` の代わりに `PlayProject` を使って sbt プロジェクトを定義した場合、設定はデフォルト値のセットになります。以下がデフォルト設定です:

```scala
resolvers ++= Seq(
  "Maven Repository" at "http://repo1.maven.org/maven2/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
),

target <<= baseDirectory / "target",

sourceDirectory in Compile <<= baseDirectory / "app",

confDirectory <<= baseDirectory / "conf",

scalaSource in Compile <<= baseDirectory / "app",

javaSource in Compile <<= baseDirectory / "app",

distDirectory <<= baseDirectory / "dist",

libraryDependencies += "play" %% "play" % play.core.PlayVersion.current,

sourceGenerators in Compile <+= (confDirectory, sourceManaged in Compile) map RouteFiles,

sourceGenerators in Compile <+= (sourceDirectory in Compile, sourceManaged in Compile, templatesTypes, templatesImport) map ScalaTemplates,

commands ++= Seq(
  playCommand, playRunCommand, playStartCommand, playHelpCommand, h2Command, classpathCommand, licenseCommand, computeDependenciesCommand
),

shellPrompt := playPrompt,

copyResources in Compile <<= (copyResources in Compile, playCopyResources) map { (r, pr) => r ++ pr },

mainClass in (Compile, run) := Some(classOf[play.core.server.NettyServer].getName),

compile in (Compile) <<= PostCompile,

dist <<= distTask,

computeDependencies <<= computeDependenciesTask,

playCopyResources <<= playCopyResourcesTask,

playCompileEverything <<= playCompileEverythingTask,

playPackageEverything <<= playPackageEverythingTask,

playReload <<= playReloadTask,

playStage <<= playStageTask,

cleanFiles <+= distDirectory.identity,

resourceGenerators in Compile <+= LessCompiler,

resourceGenerators in Compile <+= CoffeescriptCompiler,

resourceGenerators in Compile <+= JavascriptCompiler,

playResourceDirectories := Seq.empty[File],

playResourceDirectories <+= baseDirectory / "conf",

playResourceDirectories <+= baseDirectory / "public",

templatesImport := Seq("play.api.templates._", "play.api.templates.PlayMagic._"),

templatesTypes := {	
  case "html" => ("play.api.templates.Html", "play.api.templates.HtmlFormat")
  case "txt" => ("play.api.templates.Txt", "play.api.templates.TxtFormat")
  case "xml" => ("play.api.templates.Xml", "play.api.templates.XmlFormat")
}

```