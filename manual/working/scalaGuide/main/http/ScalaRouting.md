<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# HTTP routing
-->
# HTTP ルーティング

<!--
## The built-in HTTP router
-->
## ビルトイン HTTP ルータ

<!--
The router is the component in charge of translating each incoming HTTP request to an Action.
-->
ルータとは、送られてきた HTTP リクエストをそれぞれ対応するアクションの呼び出しに変換する役割を持つコンポーネントです。

<!--
An HTTP request is seen as an event by the MVC framework. This event contains two major pieces of information:
-->
HTTP リクエストは MVC フレームワークにとってはイベントであるといえます。このイベントには、次の 2 つの重要データが含まれています。

<!--
- the request path (e.g. `/clients/1542`, `/photos/list`), including the query string
- the HTTP method (e.g. `GET`, `POST`, …).
-->
- クエリストリングを含むリクエストパス (例えば、 `clients/1542` 、 `/photos/list`)
- HTTP メソッド (例えば、 `GET` 、 `POST` 、 …)

<!--
Routes are defined in the `conf/routes` file, which is compiled. This means that you’ll see route errors directly in your browser:
-->
ルートは `conf/routes` ファイル内に定義され、コンパイルされます。したがって、ルート定義のエラーはブラウザ上ですぐ確認できます。

[[images/routesError.png]]

<!--
## Dependency Injection
-->
## 依存性の注入

<!--
Play supports generating two types of routers, one is a dependency injected router, the other is a static router.  The default is the static router, but if you created a new Play application using the Play seed Activator templates, your project will include the following configuration in `build.sbt` telling it to use the injected router:
-->
Play は 2 種類のルータ生成をサポートしていて、ひとつは依存性注入のルータ、もうひとつは静的なルータです。デフォルトは静的なルータですが、Activator のテンプレートを使用して新しい Play アプリケーションを作成した場合は、プロジェクトには依存性注入のルータが使用されるように `build.sbt` に次の設定が含まれます。

```scala
routesGenerator := InjectedRoutesGenerator
```

<!--
The code samples in Play's documentation assumes that you are using the injected routes generator.  If you are not using this, you can trivially adapt the code samples for the static routes generator, either by prefixing the controller invocation part of the route with an `@` symbol, or by declaring each of your controllers as an `object` rather than a `class`.
-->
Play のドキュメントのコードサンプルは依存性注入のルートジェネレータを使用することを前提としています。これを使用しない場合は、ルートのコントローラの呼び出し部分の前に `@` を付けるか、各コントローラを `class` ではなく `object` として宣言することで、静的なルートジェネレータ用のコードサンプルに作り変えることができます。

<!--
## The routes file syntax
-->
## ルートファイルの文法

<!--
`conf/routes` is the configuration file used by the router. This file lists all of the routes needed by the application. Each route consists of an HTTP method and URI pattern, both associated with a call to an `Action` generator.
-->
`conf/routes` はルータによって利用される設定ファイルです。このファイルにはアプリケーションの全てのルートの一覧があります。各ルートは HTTP メソッドと URI パターン、そしてその 2 つが関連付けられた `Action` ジェネレータで構成されています。

<!--
Let’s see what a route definition looks like:
-->
ルート定義を例を見てみましょう。

@[clients-show](code/scalaguide.http.routing.routes)

<!--
Each route starts with the HTTP method, followed by the URI pattern. The last element is the call definition.
-->
各ルートの先頭は HTTP メソッドで、次が URI パターンです。最後の要素は呼び出し定義 (call definition) と呼ばれます。

<!--
You can also add comments to the route file, with the `#` character.
-->
`#` から始まるコメント行を書くこともできます。

@[clients-show-comment](code/scalaguide.http.routing.routes)

<!--
## The HTTP method
-->
## HTTP メソッド

<!--
The HTTP method can be any of the valid methods supported by HTTP (`GET`, `POST`, `PUT`, `DELETE`, `HEAD`).
-->
HTTP メソッドには HTTP がサポートする有効なメソッド (`GET`, `POST`, `PUT`, `DELETE`, `HEAD`) が全て使えます。

<!--
## The URI pattern
-->
## URI パターン

<!--
The URI pattern defines the route’s request path. Parts of the request path can be dynamic.
-->
URI パターンはルートのリクエストパスを定義します。リクエストパスの各パートはそれぞれ動的にマッチさせることができます。

<!--
### Static path
-->
### 静的パス

<!--
For example, to exactly match incoming `GET /clients/all` requests, you can define this route:
-->
例えば、 `GET /clients/all` へのリクエストだけをマッチさせたい場合、次のようなルートになります。

@[static-path](code/scalaguide.http.routing.routes)

<!--
### Dynamic parts 
-->
### 動的パート

<!--
If you want to define a route that retrieves a client by ID, you’ll need to add a dynamic part:
-->
例えば、クライアント ID を受け取るようなルートを定義する場合、次のような動的パートを使うことができます。

@[clients-show](code/scalaguide.http.routing.routes)

<!--
> Note that a URI pattern may have more than one dynamic part.
-->
> 一つの URI パターンには、複数の動的パートを含められます

<!--
The default matching strategy for a dynamic part is defined by the regular expression `[^/]+`, meaning that any dynamic part defined as `:id` will match exactly one URI part.
-->
動的パートをマッチングするデフォルトの方法は、`[^/]+` という正規表現によって定義されています。これはすなわち、 `:id` のように定義した動的パートは、ちょうど一つの URI パートにマッチすることを意味します。

<!--
### Dynamic parts spanning several `/`
-->
### 複数の `/` をまたぐ動的パート

<!--
If you want a dynamic part to capture more than one URI path segment, separated by forward slashes, you can define a dynamic part using the `*id` syntax, which uses the `.+` regular expression:
-->
`/` で区切られる URI パスセグメントを複数キャプチャする動的パートを定義したい場合、`*id` という文法を使って定義することができます。この文法は、正規表現 `.+` を使います。

@[spanning-path](code/scalaguide.http.routing.routes)

<!--
Here for a request like `GET /files/images/logo.png`, the `name` dynamic part will capture the `images/logo.png` value.
-->
`GET /files/images/logo.png` のようなリクエストについて、 `name` という動的パートは `images/logo.png` という値をキャプチャします。

<!--
### Dynamic parts with custom regular expressions
-->
### 動的パーツに使う正規表現のカスタマイズ

<!--
You can also define your own regular expression for the dynamic part, using the `$id<regex>` syntax:
-->
動的パートに独自の正規表現を使うためには、`$id<regex>`という構文を使って次のような定義をします。
    
@[regex-path](code/scalaguide.http.routing.routes)

<!--
## Call to the Action generator method
-->
## アクションジェネレータメソッドの呼び出し

<!--
The last part of a route definition is the call. This part must define a valid call to a method returning a `play.api.mvc.Action` value, which will typically be a controller action method.
-->
ルート定義の最後の部分は、アクションの呼び出しです。この部分には `play.api.mvc.Action` 型の値を返すようなメソッドの有効な呼び出しを書くことになっています。通常は、コントローラのアクションメソッドを呼び出すことになるでしょう。

<!--
If the method does not define any parameters, just give the fully-qualified method name:
-->
メソッドに一つもパラメータが存在しない場合、単にメソッドの完全修飾名を書きます。

@[home-page](code/scalaguide.http.routing.routes)

<!--
If the action method defines some parameters, all these parameter values will be searched for in the request URI, either extracted from the URI path itself, or from the query string.
-->
アクションメソッドにパラメータが存在する場合は、全てのパラメータがリクエスト URI から検索され、 URI パス部分かクエリストリングのいずれかから抽出されることになります。

@[page](code/scalaguide.http.routing.routes)

<!--
Or:
-->
または

@[page](code/scalaguide.http.routing.query.routes)

<!--
Here is the corresponding, `show` method definition in the `controllers.Application` controller:
-->
上記のルートに対応する `controllers.Application` コントローラの `show` メソッドは次のとおりです。

@[show-page-action](code/ScalaRouting.scala)

<!--
### Parameter types
-->
### パラメータの型

<!--
For parameters of type `String`, typing the parameter is optional. If you want Play to transform the incoming parameter into a specific Scala type, you can explicitly type the parameter:
-->
`String` 型のパラメータについては、型は指定しなくても構いません。しかし、送られてきたパラメータを Play に他の型へ変換させたい場合は、パラメータの型を明記する必要があります。

@[clients-show](code/scalaguide.http.routing.routes)

<!--
And do the same on the corresponding `show` method definition in the `controllers.Clients` controller:
-->
対応する `controllers.Clients` コントローラの `show` メソッドにも同様の型を定義します。

@[show-client-action](code/ScalaRouting.scala)

<!--
### Parameters with fixed values
-->
### パラメータに固定値を渡す

<!--
Sometimes you’ll want to use a fixed value for a parameter:
-->
パラメータに固定値を渡したい場合は、次のようにします。

@[page](code/scalaguide.http.routing.fixed.routes)

<!--
### Parameters with default values
-->
### パラメータにデフォルト値を設定する

<!--
You can also provide a default value that will be used if no value is found in the incoming request:
-->
送信されてきたリクエストに値が存在しない場合にデフォルト値を使うためには、次のようにします。

@[clients](code/scalaguide.http.routing.defaultvalue.routes)

<!--
### Optional parameters
-->
### 省略可能なパラメータ

<!--
You can also specify an optional parameter that does not need to be present in all requests:
-->
必ずしもリクエストに含まれていなくてもよい省略可能なパラメータを定義するためには、次のようにします。

@[optional](code/scalaguide.http.routing.routes)

<!--
## Routing priority
-->
## ルートの優先度

<!--
Many routes can match the same request. If there is a conflict, the first route (in declaration order) is used.
-->
同じリクエストに複数のルートがマッチする可能性もあります。そのようなルートの競合が起こった場合、 (定義した順番が) 最初のルートが利用されます。

<!--
## Reverse routing
-->
## リバースルーティング

<!--
The router can also be used to generate a URL from within a Scala call. This makes it possible to centralize all your URI patterns in a single configuration file, so you can be more confident when refactoring your application.
-->
ルータは Scala から呼び出して URL を生成させるという使い方もできます。これにより、全ての URI パターンを一つの設定ファイルに集約することができ、アプリケーションのリファクタリングをより安全に行うことができるようになります。

<!--
For each controller used in the routes file, the router will generate a ‘reverse controller’ in the `routes` package, having the same action methods, with the same signature, but returning a `play.api.mvc.Call` instead of a `play.api.mvc.Action`. 
-->
ルータはルートファイルで使われるそれぞれのコントローラについて `routes` パッケージ以下に「リバースコントローラ」を生成します。リバースコントローラにはコントローラと同じシグネチャを持つ、同名のアクションメソッド定義されていますが、それぞれ `play.api.mvc.Action` の代わりに `play.api.mvc.Call` を返すようになっています。

<!--
The `play.api.mvc.Call` defines an HTTP call, and provides both the HTTP method and the URI.
-->
`play.api.mvc.Call` は HTTP 呼び出しを表していて、 HTTP メソッドと URI が両方とも含まれてます。

<!--
For example, if you create a controller like:
-->
例えば、次のようなコントローラを作成した場合を考えてみます。

@[reverse-controller](code/ScalaRouting.scala)

<!--
And if you map it in the `conf/routes` file:
-->
そして、 `conf/routes` ファイルで次のようなマッピングを行ったとします。

@[route](code/scalaguide.http.routing.reverse.routes)

<!--
You can then reverse the URL to the `hello` action method, by using the `controllers.routes.Application` reverse controller:
-->
すると、 `controllers.routes.Application` リバースコントローラを利用することで、 `hello` アクションメソッドの URI を逆引きすることができます。

@[reverse-router](code/ScalaRouting.scala)
