<!-- translated -->
<!--
# Working with public assets
-->
# 公開アセットを扱う

<!--
This section covers serving your application’s static resources such as JavaScript, CSS and images.
-->
この節は JavaScript, CSS と画像などのアプリケーションの静的リソースの提供をカバーします。

<!--
Serving a public resource in Play 2.0 is the same as serving any other HTTP request. It uses the same routing as regular resources: using the controller/action path to distribute CSS, JavaScript or image files to the client.
-->
Play 2.0 での public リソースを提供することは、他の HTTP リクエストにサービスを提供することとほぼ同じです。通常のリソースと同じルーティングを使用します: クライアントに CSS, JavaScript や画像ファイルを配布するためのコントローラ/アクションのパスを使用します。

<!--
## The public/ folder
-->
## public/ フォルダ

<!--
By convention, public assets are stored in the `public` folder of your application. This folder is organized as follows:
-->
規約により public の資産は、アプリケーションの `public` フォルダに格納されています。このフォルダは次のように構成されています:

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

<!--
## How are public assets packaged?
-->
## どのように public アセットは公開されますか?

<!--
During the build process, the contents of the `public` folder are processed and added to the application classpath. When you package your application, these files are packaged into the application JAR file (under the `public/` path).
-->
ビルドプロセス中に、`public` フォルダの内容が処理され、アプリケーションのクラスパスに追加します。アプリケーションをパッケージ化するときに、(`public/` パス下にある) これらのファイルはアプリケーションのJARファイルにパッケージ化されます。

<!--
## The Assets controller
-->
## アセットコントローラ

<!--
Play 2.0 comes with a built-in controller to serve public assets. By default, this controller provides caching, ETag, gzip compression and JavaScript minification support.
-->
Play 2.0 には、公開アセットを提供する組み込みのコントローラが付属しています。デフォルトでは、このコントローラは、キャッシュ機能、ETag、gzip圧縮、JavaScript minify のサポートが提供されます。

<!--
The controller is available in the default Play JAR as `controllers.Assets`, and defines a single `at` action with two parameters:
-->
コントローラは `controllers.Assets` などのデフォルトの Play JAR で利用可能であり、2 つのパラメータを持つ `at` アクションで定義します。

```
Assets.at(folder: String, file: String)
```

<!--
The `folder` parameter must be fixed and defines the directory managed by the action. The `file` parameter is usually dynamically extracted from the request path.
-->
`folder` のパラメータは固定されており、アクションによって管理されるディレクトリを定義する必要があります。 `file` パラメータは、通常、動的にリクエストパスから抽出されます。

<!--
Here is the typical mapping of the `Assets` controller in your `conf/routes` file:
-->
`conf/routes` での `Assets` コントローラの典型的な設定を以下でお見せします:

```
GET  /assets/*file        Assets.at("public", file)
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
controllers.Assets.at("public", "javascripts/jquery.js")
```

<!--
This action will look-up the file and serve it, if it exists.
-->
このアクションは、ファイルを探し、存在する場合は提供します。

<!--
Note, if you define asset mappings outside "public," you'll need to tell
sbt about it, e.g. if you want:
-->
"public" の外部にリソース設定を定義したい場合、そのことを sbt に教えなければならないことに注意してください。例えば以下のように定義したい場合:

```
GET  /assets/*file               Assets.at("public", file)
GET  /liabilities/*file          Assets.at("foo", file)
```

<!--
you should add this to Build.scala:
-->
Build.scalaに以下を追加してください。

```
playAssetsDirectories <+= baseDirectory / "foo"
```

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
Note that we don’t specify the first `folder` parameter when we reverse the route. This is because our routes file defines a single mapping for the `Assets.at` action, where the `folder` parameter is fixed. So it doesn’t need to be specified explicitly.
-->
リバースルートするときに、 `folder` パラメータを指定しないことに注意してください。これはルートファイルが `folder` のパラメータが固定されている `Assets.at` アクションに対して1つのマッピングを定義しているためです。そのため、明示的に指定する必要はありません。

<!--
However, if you define two mappings for the `Assets.at` action, like this:
-->
しかしながら、 `Assets.at` アクションに2つのマッピングを定義している場合、このようにしてください:

```
GET  /javascripts/*file        Assets.at("public/javascripts", file)
GET  /images/*file             Assets.at("public/images", file)
```

<!--
Then you will need to specify both parameters when using the reverse router:
-->
リバースルータを使用する場合は、両方のパラメータを指定する必要があります。

```html
<script src="@routes.Assets.at("public/javascripts", "jquery.js")"></script>
<image src="@routes.Assets.at("public/images", "logo.png")">
```

<!--
## Etag support
-->
## Etag サポート

<!--
The `Assets` controller automatically manages **ETag** HTTP Headers. The ETag value is generated from the resource name and the file’s last modification date. (If the resource file is embedded into a file, the JAR file’s last modification date is used.)
-->
`Assets` コントローラーは自動的に **ETag** の HTTP ヘッダーを管理します。ETag の値は、リソース名とファイルの最終更新日時から生成されます。(リソースファイルがファイル内に埋め込まれているならば、JAR ファイルの最終更新日が使われます。)

<!--
When a web browser makes a request specifying this **Etag**, the server can respond with **304 NotModified**.
-->
Web ブラウザがこの　**ETag** を指定してリクエストを行うと、サーバは **304 NotModified** で応答することができます。

<!--
## Gzip support
-->
## Gzip サポート

<!--
If a resource with the same name but using a `.gz` suffix is found, the `Assets` controller will serve this one by adding the proper HTTP header:
-->
同じ名前を持つリソースで `.gz` という拡張子を使っているものが見つかった場合、 `Assets` のコントローラは、適切な HTTP ヘッダを追加することによって提供します。

```
Content-Encoding: gzip
```

<!--
## Additional `Cache-Control` directive
-->
## `Cache-Control` 命令の追加

<!--
Usually, using Etag is enough to have proper caching. However if you want to specify a custom `Cache-Control` header for a particular resource, you can specify it in your `application.conf` file. For example:
-->
通常、ETag を使用すると、適切なキャッシュを持つことができます。特定のリソース用のカスタム `Cache-Control` ヘッダを指定したい場合は、あなたの `application.conf` ファイルに指定することができます。例えば:

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
By default play compiles all managed assets that are kept in the ```app/assets``` folder. The compilation process will clean and recompile all managed assets regardless of the change. This is the safest strategy since tracking dependencies can be very tricky with front end technologies. 
-->
play は、デフォルトでは ```app/assets``` フォルダに保存された全てのアセットをコンパイルします。このコンパイルプロセスは、すべての管理アセットを、その変更に関わらず、クリーンして再コンパイルします。フロントエンド技術に掛かる依存性の追跡はとても厄介なので、これがもっとも安全な戦略です。

<!--
>Note if you are dealing with a lot of managed assets this strategy can be very slow. For this reason there is a way to recompile only the change file and its supposed dependencies. You can turn on this experimental feature by adding the following to your settings:
-->
>大量の管理アセットを扱う場合、この戦略は非常に遅くなり得ることに注意してください。この理由から、変更されたファイル、および依存すると想定されるファイルのみを再コンパイルする方法があります。以下を設定に追加することで、この実験的な機能を有効にすることができます:
```
incrementalAssetsCompilation := true
```

<!--
You will learn more about managed assets on the next few pages.
-->
続く数ページで管理アセットについて更に学びます。

<!--
> **Next:** [[Using CoffeeScript | AssetsCoffeeScript]]
-->
> **次:** [[CoffeeScriptを使う | AssetsCoffeeScript]]