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
There’s nothing to add to `build.sbt` to use unmanaged dependencies, although you could change a configuration key if you’d like to use a directory different to `lib`.
-->
この場合、`build.sbt` へ依存性を記述する必要はありませんが、`lib` 以外の場所に JAR ファイルを入れたい場合は、設定キーを変更することができます。

<!--
## Managed dependencies
-->
## 管理された依存性

<!--
Play uses Apache Ivy (via sbt) to implement managed dependencies, so if you’re familiar with Maven or Ivy, you won’t have much trouble.
-->
Play は「管理された依存性」を実現するために、Apache Ivy を利用しています。もし、Maven や Ivy に詳しければ、特に困ることはないでしょう。

Most of the time you can simply list your dependencies in the `build.sbt` file. 

<!--
Declaring a dependency looks like this (defining `group`, `artifact` and `revision`):
-->
依存性の定義は、次のように書きます。(`group`、`artifact`、`revision` を定義します。)

```scala
libraryDependencies += "org.apache.derby" % "derby" % "10.4.1.3"
```

<!--
or like this, with an optional `configuration`:
-->
または、任意で指定できる `configuration` を付け足すことができます。

```scala
libraryDependencies += "org.apache.derby" % "derby" % "10.4.1.3" % "test"
```

<!--
Multiple dependencies can be added either by multiple declarations like the above, or you can provide a Scala sequence:
-->
複数の依存性は上記のように複数の定義によって追加することもできますし、Scala のシーケンスを提供することもできます:

```scala
libraryDependencies ++= Seq(
  "org.apache.derby" % "derby" % "10.4.1.3",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
)
```

<!--
Of course, sbt (via Ivy) has to know where to download the module. If your module is in one of the default repositories sbt comes with then this will just work.
-->
sbt (内部で Ivy を使っています) は、モジュールのダウンロード元を知っている必要があります。モジュールが sbt に組み込まれたデフォルトのレポジトリのいずれかに含まれていれば、特に何もしなくても動きます。

<!--
### Getting the right Scala version with `%%`
-->
### `%%` を使って適切な Scala バージョンを選択する

<!--
If you use `groupID %% artifactID % revision` instead of `groupID % artifactID % revision` (the difference is the double `%%` after the `groupID`), sbt will add your project’s Scala version to the artifact name. This is just a shortcut. 
-->
`groupID % artifactID % revision` のかわりに、`groupID %% artifactID % revision` というというように、groupID の後に `%` を二つ重ねて記述することで、sbt はプロジェクトに設定された Scala バージョンに対応したアーティファクトをダウンロードしてくれます。

<!--
You could write this without the `%%`:
-->
これは単なるショートカットであり、`%%` を使わずに書くこともできます。

```scala
libraryDependencies += "org.scala-tools" % "scala-stm_2.9.1" % "0.3"
```

<!--
Assuming the `scalaVersion` for your build is `2.9.1`, the following is identical:
-->
例えば、ビルドファイルで `scalaVersion` に `2.9.1` を指定している場合、次の定義も全く同じ意味になります。

```scala
libraryDependencies += "org.scala-tools" %% "scala-stm" % "0.3"
```

<!--
### Resolvers
-->
### リゾルバ

<!--
sbt uses the standard Maven2 repository and the Typesafe Releases (<http://repo.typesafe.com/typesafe/releases>) repositories by default. If your dependency isn’t on one of the default repositories, you’ll have to add a resolver to help Ivy find it.
-->
sbt は、標準 Maven2 レポジトリと、Typesafe Releases レポジトリ (<http://repo.typesafe.com/typesafe/releases>) をデフォルトとして利用します。定義した依存モジュールがデフォルトのレポジトリに無い場合は、Ivy がモジュールを探せるように、リゾルバを自分で追加してやる必要があります。

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
    "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"
)
```


<!--
> **Next:** [[Working with sub-projects | SBTSubProjects]]
-->
> **Next:** [[サブプロジェクト | SBTSubProjects]]
