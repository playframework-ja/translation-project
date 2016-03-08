<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Working with public assets
-->
# 公開アセットを扱う

<!--
This section covers serving your application’s static resources such as JavaScript, CSS and images.
-->
この節は JavaScript, CSS と画像などのアプリケーションの静的リソースの提供をカバーします。

Serving a public resource in Play is the same as serving any other HTTP request. It uses the same routing as regular resources using the controller/action path to distribute CSS, JavaScript or image files to the client.

<!--
## The public/ folder
-->
## public/ フォルダ

By convention public assets are stored in the `public` folder of your application. This folder can be organized the way that you prefer. We recommend the following organization:

```
public
 └ javascripts
 └ stylesheets
 └ images
```

<!--
If you follow this structure it will be simpler to get started, but nothing stops you to modifying it once you understand how it works.
-->
このフォルダ構成に従っていれば、開始するのは簡単ですが、動作の仕組みを理解していれば、変更してもかまいません。

## WebJars

[WebJars](http://www.webjars.org/) provide a convenient and conventional packaging mechanism that is a part of Activator and sbt. For example you can declare that you will be using the popular [Bootstrap library](http://getbootstrap.com/) simply by adding the following dependency in your build file:

```scala
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.4"
```

WebJars are automatically extracted into a `lib` folder relative to your public assets for convenience. For example, if you declared a dependency on [RequireJs](http://requirejs.org/) then you can reference it from a view using a line like:

```html
<script data-main="@routes.Assets.at("javascripts/main.js")" type="text/javascript" src="@routes.Assets.at("lib/requirejs/require.js")"></script>
```

Note the `lib/requirejs/require.js` path. The `lib` folder denotes the extract WebJar assets, the `requirejs` folder corresponds to the WebJar artifactId, and the `require.js` refers to the required asset at the root of the WebJar.

<!--
## How are public assets packaged?
-->
## どのように public アセットは公開されますか?

During the build process, the contents of the `public` folder are processed and added to the application classpath.

When you package your application, all assets for the application, including all sub projects, are aggregated into a single jar, in `target/my-first-app-1.0.0-assets.jar`.  This jar is included in the distribution so that your Play application can serve them.  This jar can also be used to deploy the assets to a CDN or reverse proxy.

<!--
## The Assets controller
-->
## アセットコントローラ

Play comes with a built-in controller to serve public assets. By default, this controller provides caching, ETag, gzip and compression support.

The controller is available in the default Play JAR as `controllers.Assets` and defines a single `at` action with two parameters:

```
Assets.at(path: String, file: String)
```

<!--
The `path` parameter must be fixed and defines the directory managed by the action. The `file` parameter is usually dynamically extracted from the request path.
-->
`path` のパラメータは固定されており、アクションによって管理されるディレクトリを定義する必要があります。 `file` パラメータは、通常、動的にリクエストパスから抽出されます。

<!--
Here is the typical mapping of the `Assets` controller in your `conf/routes` file:
-->
`conf/routes` での `Assets` コントローラの典型的な設定を以下でお見せします:

```
GET  /assets/*file        controllers.Assets.at(path="/public", file)
```

<!--
Note that we define the `*file` dynamic part that will match the `.*` regular expression. So for example, if you send this request to the server:
-->
正規表現 `.*` にマッチする動的な部分 `*file` を定義したことに注意してください。このため、例えば次のようなリクエストをサーバに送信した場合:

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

This action will look-up and serve the file and if it exists.

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

Note that we don’t specify the first `folder` parameter when we reverse the route. This is because our routes file defines a single mapping for the `Assets.at` action, where the `folder` parameter is fixed. So it doesn't need to be specified.

<!--
However, if you define two mappings for the `Assets.at` action, like this:
-->
しかしながら、 `Assets.at` アクションに2つのマッピングを定義している場合、このようにしてください:

```
GET  /javascripts/*file        controllers.Assets.at(path="/public/javascripts", file)
GET  /images/*file             controllers.Assets.at(path="/public/images", file)
```

You will then need to specify both parameters when using the reverse router:

```html
<script src="@routes.Assets.at("/public/javascripts", "jquery.js")"></script>
<img src="@routes.Assets.at("/public/images", "logo.png")" />
```

## Reverse routing and fingerprinting for public assets

[sbt-web](https://github.com/sbt/sbt-web) brings the notion of a highly configurable asset pipeline to Play e.g. in your build file:

```scala
pipelineStages := Seq(rjs, digest, gzip)
```

The above will order the RequireJs optimizer (`sbt-rjs`), the digester (`sbt-digest`) and then compression (`sbt-gzip`). Unlike many sbt tasks, these tasks will execute in the order declared, one after the other.

In essence asset fingerprinting permits your static assets to be served with aggressive caching instructions to a browser. This will result in an improved experience for your users given that subsequent visits to your site will result in less assets requiring to be downloaded. Rails also describes the benefits of [asset fingerprinting](http://guides.rubyonrails.org/asset_pipeline.html#what-is-fingerprinting-and-why-should-i-care-questionmark). 

The above declaration of `pipelineStages` and the requisite `addSbtPlugin` declarations in your `plugins.sbt` for the plugins you require are your start point. You must then declare to Play what assets are to be versioned. The following routes file entry declares that all assets are to be versioned:

```scala
GET  /assets/*file  controllers.Assets.versioned(path="/public", file: Asset)
```

> Make sure you indicate that `file` is an asset by writing `file: Asset`.

You then use the reverse router, for example within a `scala.html` view:

```html
<link rel="stylesheet" href="@routes.Assets.versioned("assets/css/app.css")">
```

We highly encourage the use of asset fingerprinting.

<!--
## Etag support
-->
## Etag サポート

The `Assets` controller automatically manages **ETag** HTTP Headers. The ETag value is generated from the digest (if `sbt-digest` is being used in the asset pipeline) or otherwise the resource name and the file’s last modification date. If the resource file is embedded into a file, the JAR file’s last modification date is used.

When a web browser makes a request specifying this **Etag** then the server can respond with **304 NotModified**.

<!--
## Gzip support
-->
## Gzip サポート

If a resource with the same name but using a `.gz` suffix is found then the `Assets` controller will also serve the latter and add the following HTTP header:

```
Content-Encoding: gzip
```

Including the `sbt-gzip` plugin in your build and declaring its position in the `pipelineStages` is all that is required to generate gzip files.

<!--
## Additional `Cache-Control` directive
-->
## `Cache-Control` 命令の追加

Using Etag is usually enough for the purposes of caching. However if you want to specify a custom `Cache-Control` header for a particular resource, you can specify it in your `application.conf` file. For example:

```
# Assets configuration
# ~~~~~
"assets.cache./public/stylesheets/bootstrap.min.css"="max-age=3600"
```

<!--
## Managed assets
-->
## 管理アセット

Starting with Play 2.3 managed assets are processed by [sbt-web](https://github.com/sbt/sbt-web#sbt-web) based plugins. Prior to 2.3 Play bundled managed asset processing in the form of CoffeeScript, LESS, JavaScript linting (ClosureCompiler) and RequireJS optimization. The following sections describe sbt-web and how the equivalent 2.2 functionality can be achieved. Note though that Play is not limited to this asset processing technology as many plugins should become available to sbt-web over time. Please check-in with the [sbt-web](https://github.com/sbt/sbt-web#sbt-web) project to learn more about what plugins are available.

Many plugins use sbt-web's [js-engine plugin](https://github.com/sbt/sbt-js-engine). js-engine is able to execute plugins written to the Node API either within the JVM via the excellent [Trireme](https://github.com/apigee/trireme#trireme) project, or directly on [Node.js](https://nodejs.org/) for superior performance. Note that these tools are used during the development cycle only and have no involvement during the runtime execution of your Play application. If you have Node.js installed then you are encouraged to declare the following environment variable. For Unix, if `SBT_OPTS` has been defined elsewhere then you can:

```bash
export SBT_OPTS="$SBT_OPTS -Dsbt.jse.engineType=Node"
```

The above declaration ensures that Node.js is used when executing any sbt-web plugin.
