<!-- translated -->
<!--
# Managing library dependencies
-->
# ライブラリ依存性の管理

<!--
## Unmanaged dependencies
-->
## 管理されない依存性

<!--
Most people end up using managed dependencies - which allows for fine-grained control, but unmanaged dependencies can be simpler when starting out.
-->
ほとんどの開発者は最終的には「管理された依存性」を利用することになりますが、最初は「管理されない依存性」から始めた方がわかりやすいこともあります。

<!--
Unmanaged dependencies work like this: create a `lib/` directory in the root of your project and then add jar files to that directory. They will automatically be added to the application classpath. There’s not much else to it!
-->
「管理されない依存性」は次のように利用します: プロジェクトルートに `lib/` ディレクトリを作成し、このディレクトリに jar ファイルを追加します。これらは自動的にアプリケーションのクラスパスに追加されます。これだけです!

<!--
There’s nothing to add to `project/Build.scala` to use unmanaged dependencies, though you could change a configuration key if you’d like to use a directory different to `lib`.
-->
この場合、`project/Build.scala` へ依存性を記述する必要もありません。ただし、`lib/` 以外の場所に JAR ファイルを入れたい場合は、`project/Build.scala` にそのような設定を入れてもよいでしょう。

<!--
## Managed dependencies
-->
## 管理された依存性

<!--
Play 2.0 uses Apache Ivy (via sbt) to implement managed dependencies, so if you’re familiar with Maven or Ivy, you won’t have much trouble.
-->
Play 2.0 は「管理された依存性」を実現するために、Apache Ivy を利用しています。もし、Maven や Ivy に詳しければ、特に困ることはないでしょう。

<!--
Most of the time, you can simply list your dependencies in the `project/Build.scala` file. It’s also possible to write a Maven POM file or Ivy configuration file to externally configure your dependencies, and have sbt use those external configuration files.
-->
たいていの場合は、依存モジュールを `project/Build.scala` へリストアップするだけでよいでしょう。必要であれば、Maven の POM ファイルや Ivy の設定ファイルに依存モジュールを書いて、それを sbt から読ませることもできます。

<!--
Declaring a dependency looks like this (defining `group`, `artifact` and `revision`):
-->
依存性の定義は、次のように書きます。(`group`、`artifact`、`revision` を定義します。)

```scala
val appDependencies = Seq(
  "org.apache.derby" % "derby" % "10.4.1.3"
)
```

<!--
or like this, with an optional `configuration`:
-->
または、任意で指定できる `configuration` を付け足すことができます。

```scala
val appDependencies = Seq(
  "org.apache.derby" % "derby" % "10.4.1.3" % "test"
)
```

<!--
Of course, sbt (via Ivy) has to know where to download the module. If your module is in one of the default repositories sbt comes with, this will just work.
-->
sbt (内部で Ivy を使っています) は、モジュールのダウンロード元を知っている必要があります。モジュールが sbt に組み込まれたデフォルトのレポジトリのいずれかに含まれていれば、特に何もしなくても動きます。

<!--
### Getting the right Scala version with `%%`
-->
## `%%` を使って適切な Scala バージョンを選択する

<!--
If you use `groupID %% artifactID % revision` instead of `groupID % artifactID % revision` (the difference is the double `%%` after the `groupID`), sbt will add your project’s Scala version to the artifact name. This is just a shortcut. 
-->
`groupID % artifactID % revision` のかわりに、`groupID %% artifactID % revision` というというように、groupID の後に `%` を二つ重ねて記述することで、sbt はプロジェクトに設定された Scala バージョンに対応したアーティファクトをダウンロードしてくれます。

<!--
You could write this without the `%%`:
-->
これは単なるショートカットであり、`%%` を使わずに書くこともできます。

```scala
val appDependencies = Seq(
  "org.scala-tools" % "scala-stm_2.9.1" % "0.3"
)
```

<!--
Assuming the `scalaVersion` for your build is `2.9.1`, the following is identical:
-->
例えば、ビルドファイルで `scalaVersion` に `2.9.1` を指定している場合、次の定義も全く同じ意味になります。

```scala
val appDependencies = Seq(
  "org.scala-tools" %% "scala-stm" % "0.3"
)
```
<!--
### Resolvers
-->
### リゾルバ

<!--
Not all packages live on the same server; sbt uses the standard Maven2 repository and the Scala Tools Releases ([[http://scala-tools.org/repo-releases]]) repositories by default. If your dependency isn’t on one of the default repositories, you’ll have to add a resolver to help Ivy find it.
-->
全てのパッケージが同じサーバにあるわけではありません。sbt は、標準 Maven2 レポジトリと、Scala Tools Releases レポジトリ ([[http://scala-tools.org/repo-releases]]) をデフォルトとして利用します。定義した依存モジュールがデフォルトのレポジトリに無い場合は、Ivy がモジュールを探せるように、リゾルバを自分で追加してやる必要があります。

<!--
Use the `resolvers` setting key to add your own resolver.
-->
`resolvers` という設定キーを使って独自のリゾルバを追加します。

```scala
resolvers += name at location
```

<!--
For example:
-->
例えば、

```scala
resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
```

<!--
sbt can search your local Maven repository if you add it as a repository:
-->
ローカルに Maven レポジトリを作成しておき、sbt に検索させることも可能です。

```scala
resolvers += (
    "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
)
```

<!--
### Final example
-->
### まとめ

<!--
Here is a final example, for a project defining several managed dependencies, with custom resolvers:
-->
標準外のリゾルバと、いくつかの管理された依存性を含むプロジェクトの定義例を示します。

```scala
import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "My first application"
    val appVersion      = "1.0"

    val appDependencies = Seq(
        
      "org.scala-tools" %% "scala-stm" % "0.3",
      "org.apache.derby" % "derby" % "10.4.1.3" % "test"
      
    )

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      
      resolvers += "JBoss repository" at "https://repository.jboss.org/nexus/content/repositories/",
      resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
            
    )

}

```