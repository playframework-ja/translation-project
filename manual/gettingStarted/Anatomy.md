<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Anatomy of a Play application
-->
# Play アプリケーションの構造

## The Play application layout

The layout of a Play application is standardized to keep things as simple as possible. After a first successful compile, a Play application looks like this:

```
app                      → Application sources
 └ assets                → Compiled asset sources
    └ stylesheets        → Typically LESS CSS sources
    └ javascripts        → Typically CoffeeScript sources
 └ controllers           → Application controllers
 └ models                → Application business layer
 └ views                 → Templates
build.sbt                → Application build script
conf                     → Configurations files and other non-compiled resources (on classpath)
 └ application.conf      → Main configuration file
 └ routes                → Routes definition
dist                     → Arbitrary files to be included in your projects distribution
public                   → Public assets
 └ stylesheets           → CSS files
 └ javascripts           → Javascript files
 └ images                → Image files
project                  → sbt configuration files
 └ build.properties      → Marker for sbt project
 └ plugins.sbt           → sbt plugins including the declaration for Play itself
lib                      → Unmanaged libraries dependencies
logs                     → Logs folder
 └ application.log       → Default log file
target                   → Generated stuff
 └ resolution-cache      → Info about dependencies
 └ scala-2.10
    └ api                → Generated API docs
    └ classes            → Compiled class files
    └ routes             → Sources generated from routes
    └ twirl              → Sources generated from templates
 └ universal             → Application packaging
 └ web                   → Compiled web assets
test                     → source folder for unit or functional tests
```

<!--
## The `app/` directory
-->
## `app/` ディレクトリ

<!--
The `app` directory contains all executable artifacts: Java and Scala source code, templates and compiled assets’ sources.
-->
`app` ディレクトリには、実行可能な全てのコードが含まれます。Java や Scala のコードや、テンプレート、LESS や CoffeeScript のような、別の言語へコンパイルされるアセットのソースファイルなど、全てです。

There are three packages in the `app` directory, one for each component of the MVC architectural pattern: 

- `app/controllers`
- `app/models`
- `app/views`

<!--
You can of course add your own packages, for example an `app/utils` package.
-->
もちろん、`app/utils` のような独自のパッケージの作成しても問題ありません。

<!--
> Note that in Play, the controllers, models and views package name conventions are now just that and can be changed if needed (such as prefixing everything with `com.yourcompany`).
-->
> Play では、`controllers`、`models`、`views` というパッケージ名はゆるい規約で、必要なら変更することができます（例えば、全てのパッケージに `com.yourcompany` というプレフィックスをつけるとか）。

<!--
There is also an optional directory called `app/assets` for compiled assets such as [LESS sources](http://lesscss.org/) and [CoffeeScript sources](http://coffeescript.org/).
-->
この 3 つ以外にも、必須ではありませんが `app/assets` というディレクトリがあります。ここには、[LESS](http://lesscss.org/) や [CoffeeScript sources](http://coffeescript.org/) のような、別の言語へコンパイルされるアセットのソースファイルを置きます。

<!--
## The `public/` directory
-->
## `public/` ディレクトリ

<!--
Resources stored in the `public` directory are static assets that are served directly by the Web server.
-->
`public/` ディレクトリに保存されたリソースはいわゆる静的コンテンツとなり、Web ブラウザへそのまま送信されます。

This directory is split into three sub-directories for images, CSS stylesheets and JavaScript files. You should organize your static assets like this to keep all Play applications consistent.

<!--
> In a newly-created application, the `/public` directory is mapped to the `/assets` URL path, but you can easily change that, or even use several directories for your static assets.
-->
> ちなみに、アプリケーションを新規作成した状態では、`public/` ディレクトリが `/assets` という URL パスに対応付けされています。これはいつでも変更できます。また、静的コンテンツを複数のディレクトリに分散させることもできます。

<!--
## The `conf/` directory
-->
## `conf/` ディレクトリ

<!--
The `conf` directory contains the application’s configuration files. There are two main configuration files:
-->
`conf` ディレクトリにはアプリケーションの設定ファイルを入れます。主な設定ファイルは次の二つです。

- `application.conf`, the main configuration file for the application, which contains configuration parameters
- `routes`, the routes definition file.

<!--
If you need to add configuration options that are specific to your application, it’s a good idea to add more options to the `application.conf` file.
-->
もし、アプリケーション独自の設定を追加したい場合は、`application.conf` に設定項目を追加すると良いでしょう。

<!--
If a library needs a specific configuration file, try to file it under the `conf` directory.
-->
また、ライブラリ独自の設定ファイルは、`conf` ディレクトリに配置すると良いでしょう。

<!--
## The `lib/` directory
-->
## `lib/` ディレクトリ

<!--
The `lib` directory is optional and contains unmanaged library dependencies, ie. all JAR files you want to manually manage outside the build system. Just drop any JAR files here and they will be added to your application classpath.
-->
`lib` ディレクトリは必須ではありませんが、ビルドシステムの管理外におきたい jar ファイルなど、全ての管理されないライブラリ依存性を含みます。jar ファイルこのディレクトリに配置しておくだけで、アプリケーションのクラスパスに追加されます。

<!--
## The `build.sbt` file
-->
## `build.sbt` ファイル

<!--
Your project's main build declarations are generally found in `build.sbt` at the root of the project. `.scala` files in the `project/` directory can also be used to declare your project's build.
-->
通常、プロジェクトの主要なビルド定義はプロジェクトルートにある `build.sbt` に書かれます。`project/` ディレクトリにある `.scala` も、プロジェクトのビルドを定義するために使うことができます。

<!--
## The `project/` directory
-->
## `project/` ディレクトリ

<!--
The `project` directory contains the sbt build definitions:
-->
`project` ディレクトリは、sbt のビルド定義を含みます。

<!--
- `plugins.sbt` defines sbt plugins used by this project
- `build.properties` contains the sbt version to use to build your app.
-->
- `plugins.sbt` は、このプロジェクトで使う sbt プラグインの定義ファイルです。
- `build.properties` には、このアプリケーションをビルドするために使用する sbt のバージョンが含まれています。

<!--
## The `target/` directory
-->
## `target/` ディレクトリ

<!--
The `target` directory contains everything generated by the build system. It can be useful to know what is generated here.
-->
`target/` ディレクトリには、Play のビルドシステムによって生成された全てのファイルが入ります。何が生成されるのか覚えておくと良いでしょう。

<!--
- `classes/` contains all compiled classes (from both Java and Scala sources).
- `classes_managed/` contains only the classes that are managed by the framework (such as the classes generated by the router or the template system). It can be useful to add this class folder as an external class folder in your IDE project.
- `resource_managed/` contains generated resources, typically compiled assets such as LESS CSS and CoffeeScript compilation results.
- `src_managed/` contains generated sources, such as the Scala sources generated by the template system.
- `web/` contains assets processed by [sbt-web](https://github.com/sbt/sbt-web#sbt-web) such as those from the `app/assets` and `public` folders.
-->

- `classes/` には、Java や Scala のコードからコンパイルされたクラスファイルが入ります。
- `classes_managed/` には、Play が管理しているクラス (例えば、ルータやテンプレートシステムによって生成されたクラスなど) が入ります。IDE をお使いの場合は、このディレクトリへクラスパスを通しておくと便利でしょう。
- `resource_managed/` には、Play によって生成されたリソースが含まれます。例えば、LESS CSS や CoffeeScript からトランスコンパイルされた CSS ファイル や JavaScript ファイルは、このディレクトリに配置されます。
- `src_managed/` には、テンプレートシステムが生成した Scala コードなど、Play によって生成されたソースが含まれます。
- `web/` には、`app/assets` と `public` とフォルダから [sbt-web](https://github.com/sbt/sbt-web#sbt-web) によって生成されたアセットが含まれます。

<!--
## Typical `.gitignore` file
-->
## よく使う `.gitignore` ファイル

<!--
Generated folders should be ignored by your version control system. Here is the typical `.gitignore` file for a Play application:
-->
Play によって生成されたディレクトリは、バージョン管理システムに無視させるべきでしょう。Play アプリケーションで典型的な `.gitignore` ファイルの内容を以下に示します。

```txt
logs
project/project
project/target
target
tmp
dist
.cache
```

## Default SBT layout

You also have the option of using the default layout used by SBT and Maven. Please note that this layout is experimental and may have issues. In order to use this layout, use `disablePlugins(PlayLayoutPlugin)`. This will stop Play from overriding the default SBT layout, which looks like this:

```
build.sbt                  → Application build script
src                        → Application sources
 └ main                    → Compiled asset sources
    └ java                 → Java sources
       └ controllers       → Java controllers
       └ models            → Java business layer
    └ scala                → Scala sources
       └ controllers       → Scala controllers
       └ models            → Scala business layer
    └ resources            → Configurations files and other non-compiled resources (on classpath)
       └ application.conf  → Main configuration file
       └ routes            → Routes definition
    └ twirl                → Templates
    └ assets               → Compiled asset sources
       └ css               → Typically LESS CSS sources
       └ js                → Typically CoffeeScript sources
    └ public               → Public assets
       └ css               → CSS files
       └ js                → Javascript files
       └ images            → Image files
 └ test                    → Unit or functional tests
    └ java                 → Java source folder for unit or functional tests
    └ scala                → Scala source folder for unit or functional tests
    └ resources            → Resource folder for unit or functional tests
 └ universal               → Arbitrary files to be included in your projects distribution
project                    → sbt configuration files
 └ build.properties        → Marker for sbt project
 └ plugins.sbt             → sbt plugins including the declaration for Play itself
lib                        → Unmanaged libraries dependencies
logs                       → Logs folder
 └ application.log         → Default log file
target                     → Generated stuff
 └ scala-2.10.0            
    └ cache              
    └ classes              → Compiled class files
    └ classes_managed      → Managed class files (templates, ...)
    └ resource_managed     → Managed resources (less, ...)
    └ src_managed          → Generated sources (templates, ...)
```
