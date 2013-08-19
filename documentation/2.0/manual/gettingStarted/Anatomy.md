<!--
# Anatomy of a Play 2.0 application
-->
# Play 2.0 アプリケーションの構造

<!--
## The standard application layout
-->
## アプリケーションの標準的なファイル構成

<!--
The layout of a Play application is standardized to keep things as simple as possible. A standard Play application looks like this:
-->
Play アプリケーションのファイル構成は、できるだけシンプルさを重視して標準化されています。Play アプリケーションの標準的なファイル構成は、以下のようになっています。

```
app                      → Application sources
 └ assets                → Compiled asset sources
    └ stylesheets        → Typically LESS CSS sources
    └ javascripts        → Typically CoffeeScript sources
 └ controllers           → Application controllers
 └ models                → Application business layer
 └ views                 → Templates
conf                     → Configurations files
 └ application.conf      → Main configuration file
 └ routes                → Routes definition
public                   → Public assets
 └ stylesheets           → CSS files
 └ javascripts           → Javascript files
 └ images                → Image files
project                  → sbt configuration files
 └ build.properties      → Marker for sbt project
 └ Build.scala           → Application build script
 └ plugins.sbt           → sbt plugins
lib                      → Unmanaged libraries dependencies
logs                     → Standard logs folder
 └ application.log       → Default log file
target                   → Generated stuff
 └ scala-2.9.1              
    └ cache              
    └ classes            → Compiled class files
    └ classes_managed    → Managed class files (templates, ...)
    └ resource_managed   → Managed resources (less, ...)
    └ src_managed        → Generated sources (templates, ...)
test                     → source folder for unit or functional tests
```

<!--
## The app/ directory
-->
## app/ ディレクトリ

<!--
The `app` directory contains all executable artifacts: Java and Scala source code, templates and compiled assets’ sources.
-->
`app` ディレクトリには、実行可能な全てのコードが含まれます。Java や Scala のコードや、テンプレート、LESS や CoffeeScript のような、別の言語へコンパイルされるアセットのソースファイルなど、全てです。

<!--
There are three standard packages in the `app` directory, one for each component of the MVC architectural pattern: 
-->
`app` ディレクトリには 3 つのパッケージがあります。それぞれ、MVC パターンに登場する 3 つのコンポーネントに対応します。

- `app/controllers`
- `app/models`
- `app/views`

<!--
You can of course add your own packages, for example an `app/utils` package.
-->
もちろん、`app/utils` のような独自のパッケージの作成しても問題ありません。

<!--
> Note that in Play 2.0, the controllers, models and views package name conventions are now just that and can be changed if needed (such as prefixing everything with `com.yourcompany`).
-->
> Play 2.0では、`controllers`、`models`、`views` というパッケージ名はゆるい規約で、必要なら変更することができます（例えば、全てのパッケージに `com.yourcompany` というプレフィックスをつけるとか）。

<!--
There is also an optional directory called `app/assets` for compiled assets such as [[LESS sources | http://lesscss.org/]] and [[CoffeeScript sources | http://jashkenas.github.com/coffee-script/]].
-->
この 3 つ以外にも、必須ではありませんが `app/assets` というディレクトリがあります。ここには、[[LESS| http://lesscss.org/]] や [[CoffeeScript| http://jashkenas.github.com/coffee-script/]] のような、別の言語へコンパイルされるアセットのソースファイルを置きます。

<!--
## The public/ directory
-->
## public/ ディレクトリ

<!--
Resources stored in the `public` directory are static assets that are served directly by the Web server.
-->
`public/` ディレクトリに保存されたリソースはいわゆる静的コンテンツとなり、Web ブラウザへそのまま送信されます。

<!--
This directory is split into three standard sub-directories for images, CSS stylesheets and JavaScript files. You should organize your static assets like this to keep all Play applications consistent.
-->
このディレクトリには、デフォルトで 3 つのサブディレクトリがあります。それぞれ、画像ファイル、CSS ファイル、JavaScript ファイルを入れます。全ての Play アプリケーションが似たような構成になるように、静的コンテンツはこれらのディレクトリに入れておくと良いでしょう。

<!--
> In a newly-created application, the `/public` directory is mapped to the `/assets` URL path, but you can easily change that, or even use several directories for your static assets.
-->
> ちなみに、アプリケーションを新規作成した状態では、`public/` ディレクトリが `/assets` という URL パスに対応付けされています。これはいつでも変更できます。また、静的コンテンツを複数のディレクトリに分散させることもできます。

<!--
## The conf/ directory
-->
## conf/ ディレクトリ

<!--
The `conf` directory contains the application’s configuration files. There are two main configuration files:
-->
`conf` ディレクトリにはアプリケーションの設定ファイルを入れます。主な設定ファイルは次の二つです。

<!--
- `application.conf`, the main configuration file for the application, which contains standard configuration parameters
- `routes`, the routes definition file.
-->
- `application.conf` という、アプリケーションのメイン設定ファイル。標準的な設定項目を全て含んでいます。
- `routes` という、ルート定義ファイル。

<!--
If you need to add configuration options that are specific to your application, it’s a good idea to add more options to the `application.conf` file.
-->
もし、アプリケーション独自の設定を追加したい場合は、`application.conf` に設定項目を追加すると良いでしょう。

<!--
If a library needs a specific configuration file, try to file it under the `conf` directory.
-->
また、ライブラリ独自の設定ファイルは、`conf` ディレクトリに配置すると良いでしょう。

<!--
## The lib/ directory
-->
## lib/ ディレクトリ

<!--
The `lib` directory is optional and contains unmanaged library dependencies, ie. all JAR files you want to manually manage outside the build system. Just drop any JAR files here and they will be added to your application classpath.
-->
`lib` ディレクトリは必須ではありませんが、ビルドシステムの管理外におきたい jar ファイルなど、全ての管理されないライブラリ依存性を含みます。jar ファイルこのディレクトリに配置しておくだけで、アプリケーションのクラスパスに追加されます。

<!--
## The project/ directory
-->
## project/ ディレクトリ

<!--
The `project` directory contains the sbt build definitions:
-->
`project` ディレクトリは、sbt のビルド定義を含みます。

<!--
- `plugins.sbt` defines sbt plugins used by this project
- `Build.scala` defines your application build script.
-->
- `plugins.sbt` は、このプロジェクトで使う sbt プラグインの定義ファイルです。
- `Build.scala` は、このアプリケーションのビルドスクリプトです。

<!--
## The target/ directory
-->
## target/ ディレクトリ

<!--
The `target` directory contains everything generated by the build system. It can be useful to know what is generated here.
-->
`target/` ディレクトリには、Play のビルドシステムによって生成された全てのファイルが入ります。何が生成されるのか覚えておくと良いでしょう。

<!--
- `classes/` contains all compiled classes (from both Java and Scala sources).
- `classes_managed/` contains only the classes that are managed by the framework (such as the classes generated by the router or the template system). It can be useful to add this class folder as an external class folder in your IDE project.
- `resource_managed/` contains generated resources, typically compiled assets such as LESS CSS and CoffeeScript compilation results.
- `src_managed/` contains generated sources, such as the Scala sources generated by the template system.
-->
- `classes/` には、Java や Scala のコードからコンパイルされたクラスファイルが入ります。
- `classes_managed/` には、Play が管理しているクラス (例えば、ルータやテンプレートシステムによって生成されたクラスなど) が入ります。IDE をお使いの場合は、このディレクトリへクラスパスを通しておくと便利でしょう。
- `resource_managed/` には、Play によって生成されたリソースが含まれます。例えば、LESS CSS や CoffeeScript からトランスコンパイルされた CSS ファイル や JavaScript ファイルは、このディレクトリに配置されます。
- `src_managed/` には、テンプレートシステムが生成した Scala コードなど、Play によって生成されたソースが含まれます。

<!--
## Typical .gitignore file
-->
## よく使う .gitignore ファイル

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
```

<!--
> **Next:** [[Using the Play 2.0 console | PlayConsole ]]
-->
> **Next:** [[Play 2.0 コンソールを使う | PlayConsole]]