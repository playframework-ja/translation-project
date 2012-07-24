# About SBT Settings

## About sbt settings

The sbt build script defines settings for your project. You can also define you own custom settings for your project, as described in the [[sbt documentation | https://github.com/harrah/xsbt/wiki]].

To set a basic setting, use the `:=` operator:

```scala
val main = PlayProject(appName, appVersion, appDependencies).settings(
  confDirectory := "myConfFolder"     
)
```

## Default settings for Java applications

Play 2.0 defines a default set of settings suitable for Java-based applications. To enable them add the `defaultJavaSettings` set of settings to your application definition:

```scala
val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA)
```

These default settings mostly define the default imports for generated templates. For example, it imports `java.lang.*`, so types like `Long` are the Java ones by default instead of the Scala ones. It also imports `java.util.*` so the default collection library will be the Java one.

## Default settings for Scala applications

Play 2.0 defines a default set of settings suitable for Scala-based applications. To enable them add the `defaultScalaSettings` set of settings to your application definition:

```scala
val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA)
```

These default settings define the default imports for generated templates (such as internationalized messages, and core APIs).

## Play project settings with their default value

When you define your sbt project using `PlayProject` instead of `Project`, you will get a default set of settings. Here is the default configuration:

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