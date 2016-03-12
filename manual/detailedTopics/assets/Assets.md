<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Working with public assets
-->
# 公開アセットを扱う

<!--
This section covers serving your application’s static resources such as JavaScript, CSS and images.
-->
この節は JavaScript, CSS と画像などのアプリケーションの静的リソースの提供をカバーします。

<!--
Serving a public resource in Play is the same as serving any other HTTP request. It uses the same routing as regular resources using the controller/action path to distribute CSS, JavaScript or image files to the client.
-->
Play で public リソースを提供することは、他の HTTP リクエストにサービスを提供することと同じです。コントローラ/アクションのパスが使う通常のリソースと同じルーティングを使って、クライアントに CSS, JavaScript や画像ファイルを配布します。

<!--
## The public/ folder
-->
## public/ フォルダ

<!--
By convention public assets are stored in the `public` folder of your application. This folder can be organized the way that you prefer. We recommend the following organization:
-->
規約により、公開アセットはアプリケーションの `public` フォルダに格納されます。このフォルダは好きなように構成できます。以下の構成がおすすめです:

```
public
 └ javascripts
 └ stylesheets
 └ images
```

<!--
If you follow this structure it will be simpler to get started, but nothing stops you to modifying it once you understand how it works.
-->
このフォルダ構成に従えば簡単に始められますが、どのように動作するか理解している場合は、自由に変更できます。

<!--
## WebJars
-->
## WebJars

<!--
[WebJars](http://www.webjars.org/) provide a convenient and conventional packaging mechanism that is a part of Activator and sbt. For example you can declare that you will be using the popular [Bootstrap library](http://getbootstrap.com/) simply by adding the following dependency in your build file:
-->
[WebJars](http://www.webjars.org/) は、Activator や sbt の一部となっている、便利で規約化されたパッケージング機構を提供します。例えば、ビルドファイルに以下のように依存性を追加するだけで、ポピュラーな [Bootstrap ライブラリ](http://getbootstrap.com/) を使うことができます。

```scala
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.4"
```

<!--
WebJars are automatically extracted into a `lib` folder relative to your public assets for convenience. For example, if you declared a dependency on [RequireJs](http://requirejs.org/) then you can reference it from a view using a line like:
-->
利便性のため、WebJar は依存性を公開アセットフォルダから見た `lib` 関連パスに自動的に展開します。例えば、[RequireJs](http://requirejs.org/) の依存性を宣言している場合、以下のようにしてビューから参照することができます:

```html
<script data-main="@routes.Assets.at("javascripts/main.js")" type="text/javascript" src="@routes.Assets.at("lib/requirejs/require.js")"></script>
```

<!--
Note the `lib/requirejs/require.js` path. The `lib` folder denotes the extract WebJar assets, the `requirejs` folder corresponds to the WebJar artifactId, and the `require.js` refers to the required asset at the root of the WebJar.
-->
`lib/requirejs/require.js` パスに注目してください。この `lib` フォルダは展開された WebJar アセットを示し、`requirejs` フォルダは WebJar アーティファクト ID に対応していて、そして `require.js` が、この WebJar のルートにある要求されたアセットを参照しています。

<!--
## How are public assets packaged?
-->
## 公開アセットはどのようにパッケージングされる?

<!--
During the build process, the contents of the `public` folder are processed and added to the application classpath.
-->
ビルドプロセス中に `public` フォルダの内容が処理され、アプリケーションのクラスパスに追加されます。

<!--
When you package your application, all assets for the application, including all sub projects, are aggregated into a single jar, in `target/my-first-app-1.0.0-assets.jar`.  This jar is included in the distribution so that your Play application can serve them.  This jar can also be used to deploy the assets to a CDN or reverse proxy.
-->
アプリケーションをパッケージングすると、サブプロジェクトを含むすべてのアプリケーションのアセットが単一の `target/my-first-app-1.0.0-assets.jar` に集約されます。この jar はディストリビューションに含まれるので、Play アプリケーションはこれらのアセットを提供することができます。この jar はアセットを CDN やリバースプロキシにデプロイすることもできます。

<!--
## The Assets controller
-->
## アセットコントローラ

<!--
Play comes with a built-in controller to serve public assets. By default, this controller provides caching, ETag, gzip and compression support.
-->
Play には、公開アセットを提供する組み込みのコントローラが付属しています。このコントローラはデフォルトでキャッシュ機能、ETag, gzip, そして圧縮をサポートします。

<!--
The controller is available in the default Play JAR as `controllers.Assets` and defines a single `at` action with two parameters:
-->
このコントローラはデフォルトの Play JAR において `controllers.Assets` として利用可能であり、引数を二つ持つ `at` アクションを定義しています:

```
Assets.at(path: String, file: String)
```

<!--
The `path` parameter must be fixed and defines the directory managed by the action. The `file` parameter is usually dynamically extracted from the request path.
-->
`path` のパラメータは固定されており、アクションによって管理されるディレクトリを定義する必要があります。 通常、`file` パラメータはリクエストパスから動的に抽出されます。

<!--
Here is the typical mapping of the `Assets` controller in your `conf/routes` file:
-->
以下は、`conf/routes` ファイルにおける `Assets` コントローラの典型的なマッピングです:

```
GET  /assets/*file        controllers.Assets.at(path="/public", file)
```

<!--
Note that we define the `*file` dynamic part that will match the `.*` regular expression. So for example, if you send this request to the server:
-->
`*file` を正規表現 `.*` にマッチする動的な部分として定義したことに注目してください。このため、例えば次のようなリクエストをサーバに送信した場合:

```
GET /assets/javascripts/jquery.js
```

<!--
The router will invoke the `Assets.at` action with the following parameters:
-->
ルータは次のパラメータを使用して `Assets.at` アクションを起動します:

```
controllers.Assets.at("/public", "javascripts/jquery.js")
```

<!--
This action will look-up and serve the file and if it exists.
-->
このアクションは、リクエストされたファイルを探し、そのファイルが存在する場合は提供します。

<!--
## Reverse routing for public assets
-->
## 公開リソースのリバースルーティング

<!--
As for any controller mapped in the routes file, a reverse controller is created in `controllers.routes.Assets`. You use this to reverse the URL needed to fetch a public resource. For example, from a template:
-->
routes ファイルにマッピングされた任意のコントローラと同様に、リバースコントローラが `controllers.routes.Assets` に作成されます。公開リソースを取得するために必要な URL をリバースする際に使用します。テンプレートでの例は以下のようになります:

```html
<script src="@routes.Assets.at("javascripts/jquery.js")"></script>
```

<!--
This will produce the following result:
-->
以下の結果を生成します。

```html
<script src="/assets/javascripts/jquery.js"></script>
```

<!--
Note that we don’t specify the first `folder` parameter when we reverse the route. This is because our routes file defines a single mapping for the `Assets.at` action, where the `folder` parameter is fixed. So it doesn't need to be specified.
-->
ルートをリバースする際は `folder` パラメータを指定しないことに注意してください。これは、`folder` 引数が固定されている `Assets.at` アクションのマッピングをひとつ、ルートファイルに定義しているためです。そのため、指定する必要はありません。

<!--
However, if you define two mappings for the `Assets.at` action, like this:
-->
しかし、 `Assets.at` アクションにマッピングをふたつ定義している場合、次のようにしてください:

```
GET  /javascripts/*file        controllers.Assets.at(path="/public/javascripts", file)
GET  /images/*file             controllers.Assets.at(path="/public/images", file)
```

<!--
You will then need to specify both parameters when using the reverse router:
-->
リバースルータを使用する場合は、パラメータを両方指定する必要があります:

```html
<script src="@routes.Assets.at("/public/javascripts", "jquery.js")"></script>
<img src="@routes.Assets.at("/public/images", "logo.png")" />
```

<!--
## Reverse routing and fingerprinting for public assets
-->
## リバースルーティングと公開アセットへのフィンガープリント

<!--
[sbt-web](https://github.com/sbt/sbt-web) brings the notion of a highly configurable asset pipeline to Play e.g. in your build file:
-->
[sbt-web](https://github.com/sbt/sbt-web) は、例えば以下のビルドファイルのように、高度に設定可能なアセットパイプラインの概念を Play に持ち込みました:

```scala
pipelineStages := Seq(rjs, digest, gzip)
```

<!--
The above will order the RequireJs optimizer (`sbt-rjs`), the digester (`sbt-digest`) and then compression (`sbt-gzip`). Unlike many sbt tasks, these tasks will execute in the order declared, one after the other.
-->
上記の場合、RequireJs オプティマイザ (`sbt-rjs`), ダイジェスト (`sbt-digest`) と、続けて圧縮 (`sbt-gzip`) が順序付けられます。多くの sbt タスクとは異なり、これらのタスクは宣言された順序で、ひとつずつ実行されます。

<!--
In essence asset fingerprinting permits your static assets to be served with aggressive caching instructions to a browser. This will result in an improved experience for your users given that subsequent visits to your site will result in less assets requiring to be downloaded. Rails also describes the benefits of [asset fingerprinting](http://guides.rubyonrails.org/asset_pipeline.html#what-is-fingerprinting-and-why-should-i-care-questionmark). 
-->
アセットのフィンガープリントにより、ブラウザに対して提供する静的なアセットのキャッシュを積極的に指示することができます。結果として、次にサイトを訪れるユーザはより少ないアセットのダウンロードを要求することになり、ユーザ体験が向上します。Rails も [アセットのフィンガープリント](http://railsguides.jp/asset_pipeline.html#%E3%83%95%E3%82%A3%E3%83%B3%E3%82%AC%E3%83%BC%E3%83%97%E3%83%AA%E3%83%B3%E3%83%88%E3%81%A8%E6%B3%A8%E6%84%8F%E7%82%B9) の利点を述べています。

<!--
The above declaration of `pipelineStages` and the requisite `addSbtPlugin` declarations in your `plugins.sbt` for the plugins you require are your start point. You must then declare to Play what assets are to be versioned. The following routes file entry declares that all assets are to be versioned:
-->
はじめに、上記した `pipelineStages` と、必要なプラグインを `plugins.sbt` に `addSbtPlugin` で宣言します。続いて、バージョン管理するアセットを Play に宣言する必要があります。以下の route ファイルでは、すべてのアセットをバージョン管理するよう宣言しています:

```scala
GET  /assets/*file  controllers.Assets.versioned(path="/public", file: Asset)
```

<!--
> Make sure you indicate that `file` is an asset by writing `file: Asset`.
-->
> `file: Asset` と書くことで `file` がアセットであると示していることを確認してください。

<!--
You then use the reverse router, for example within a `scala.html` view:
-->
それから、例えば `scala.html` ビューでリバースルーターを使います:

```html
<link rel="stylesheet" href="@routes.Assets.versioned("assets/css/app.css")">
```

<!--
We highly encourage the use of asset fingerprinting.
-->
アセットフィンガープリントの利用を強く推奨します。

<!--
## Etag support
-->
## Etag サポート

<!--
The `Assets` controller automatically manages **ETag** HTTP Headers. The ETag value is generated from the digest (if `sbt-digest` is being used in the asset pipeline) or otherwise the resource name and the file’s last modification date. If the resource file is embedded into a file, the JAR file’s last modification date is used.
-->
`Assets` コントローラは、自動的に **ETag** HTTP ヘッダを管理します。ETag の値 (`sbt-digest` がアセットパイプラインで使われている場合) は、ダイジェストまたはリソース名および最終更新日付から生成されます。リソースが JAR ファイルに組み込まれている場合は、その JAR ファイルの最終更新日付が使用されます。

<!--
When a web browser makes a request specifying this **Etag** then the server can respond with **304 NotModified**.
-->
Web ブラウザがこの **ETag** を指定してリクエストを行った場合、サーバは **304 NotModified** で応答することができます。

<!--
## Gzip support
-->
## Gzip サポート

<!--
If a resource with the same name but using a `.gz` suffix is found then the `Assets` controller will also serve the latter and add the following HTTP header:
-->
名前が同じで `gz` 拡張子を持つリソースが見つかった場合、`Assets` コントローラは後者のリソース、そして以下の HTTP ヘッダを提供します:

```
Content-Encoding: gzip
```

<!--
Including the `sbt-gzip` plugin in your build and declaring its position in the `pipelineStages` is all that is required to generate gzip files.
-->
gzip ファイルを生成するには、ビルドに `sbt-gzip` プラグインを取り込み、`pipelineStages` において適切に宣言している必要があります。

<!--
## Additional `Cache-Control` directive
-->
## `Cache-Control` 命令の追加

<!--
Using Etag is usually enough for the purposes of caching. However if you want to specify a custom `Cache-Control` header for a particular resource, you can specify it in your `application.conf` file. For example:
-->
通常のキャッシュ目的であれば、ETag で十分です。特定のリソース用に独自の `Cache-Control` ヘッダを指定したい場合は、`application.conf` ファイルに指定することができます。例えば:

```
# Assets configuration
# ~~~~~
"assets.cache./public/stylesheets/bootstrap.min.css"="max-age=3600"
```

<!--
## Managed assets
-->
## 管理アセット

<!--
Starting with Play 2.3 managed assets are processed by [sbt-web](https://github.com/sbt/sbt-web#sbt-web) based plugins. Prior to 2.3 Play bundled managed asset processing in the form of CoffeeScript, LESS, JavaScript linting (ClosureCompiler) and RequireJS optimization. The following sections describe sbt-web and how the equivalent 2.2 functionality can be achieved. Note though that Play is not limited to this asset processing technology as many plugins should become available to sbt-web over time. Please check-in with the [sbt-web](https://github.com/sbt/sbt-web#sbt-web) project to learn more about what plugins are available.
-->
Play 2.3 から、管理アセットは [sbt-web](https://github.com/sbt/sbt-web#sbt-web) ベースのプラグインで処理されるようになりました。2.3 より前の Play には、管理アセットの処理として CoffeeScript, LESS, JavaScript Lint (ClosureCompiler) そして RequireJS による最適化がバンドルされていました。以降の節では、sbt-web と 2.2 同等の機能を達成する方法について記述します。とは言え、徐々に sbt-web で多くのプラグインが使えるようになっていくため、Play はこのアセット処理技術に制限されていないことに注意してください。[sbt-web](https://github.com/sbt/sbt-web#sbt-web) プロジェクトを確認して、利用できるより多くのプラグインについて学んでください。

<!--
Many plugins use sbt-web's [js-engine plugin](https://github.com/sbt/sbt-js-engine). js-engine is able to execute plugins written to the Node API either within the JVM via the excellent [Trireme](https://github.com/apigee/trireme#trireme) project, or directly on [Node.js](https://nodejs.org/) for superior performance. Note that these tools are used during the development cycle only and have no involvement during the runtime execution of your Play application. If you have Node.js installed then you are encouraged to declare the following environment variable. For Unix, if `SBT_OPTS` has been defined elsewhere then you can:
-->
多くのプラグインが sbt-web の [js-engine plugin](https://github.com/sbt/sbt-js-engine) を使います。js-engine は、素晴らしい [Trireme](https://github.com/apigee/trireme#trireme) プロジェクトによって JVM 上でも、またはより良いパフォーマンスのために直接 [Node.js](https://nodejs.org/) 上でも Node API で書かれたプラグインを実行することができます。これらのツールは開発サイクルにおいてのみ使用され、Play アプリケーションの実行時には関与しないことに注目してください。Node.js がインストール済みであれば、以下の環境変数を宣言することを推奨します。Unix で `SBT_OPTS` をどこかに定義している場合、以下のように指定することができます: 

```bash
export SBT_OPTS="$SBT_OPTS -Dsbt.jse.engineType=Node"
```

<!--
The above declaration ensures that Node.js is used when executing any sbt-web plugin.
-->
上記の宣言は、あらゆる sbt-web プラグインの実行時に Node.js が使われることを保証します。
