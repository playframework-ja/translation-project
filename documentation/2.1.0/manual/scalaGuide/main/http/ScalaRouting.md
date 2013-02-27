<!-- translated -->
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

An HTTP request is seen as an event by the MVC framework. This event contains two major pieces of information:

- the request path (e.g. `/clients/1542`, `/photos/list`), including the query string
- the HTTP method (e.g. GET, POST, …).

Routes are defined in the `conf/routes` file, which is compiled. This means that you’ll see route errors directly in your browser:
-->
ルータとは、送られてきた HTTP リクエストをそれぞれ対応するアクションの呼び出しに変換する役割を持つコンポーネントです。

HTTP リクエストは MVC フレームワークにとってはイベントであるといえます。このイベントには、次の２つの重要データが含まれています。

- クエリストリングを含むリクエストパス (例えば、 `clients/1542` 、 `/photos/list`) 
- HTTP メソッド (例えば、 GET 、 POST 、 …)

ルートは `conf/routes` ファイル内に定義され、コンパイルされます。したがって、ルート定義のエラーはブラウザ上ですぐ確認できます。

[[images/routesError.png]]

<!--
## The routes file syntax
-->
## ルートファイルの文法

<!--
`conf/routes` is the configuration file used by the router. This file lists all of the routes needed by the application. Each route consists of an HTTP method and URI pattern, both associated with a call to an `Action` generator.

Let’s see what a route definition looks like:
-->
`conf/routes` はルータによって利用される設定ファイルです。このファイルにはアプリケーションの全てのルートの一覧があります。各ルートは HTTP メソッドと URI パターン、そしてその２つが関連付けられた `Action` ジェネレータで構成されています。

ルート定義を例を見てみましょう。

```
GET   /clients/:id          controllers.Clients.show(id: Long)  
```

<!--
Each route starts with the HTTP method, followed by the URI pattern. The last element is the call definition.

You can also add comments to the route file, with the `#` character.
-->
各ルートの先頭は HTTP メソッドで、次が URI パターンです。最後の要素は呼び出し定義 (call definition) と呼ばれます。

`#` から始まるコメント行を書くこともできます。

```
# Display a client.
GET   /clients/:id          controllers.Clients.show(id: Long)  
```

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

```
GET   /clients/all              controllers.Clients.list()
```

<!--
### Dynamic parts 
-->
### 動的パート

<!--
If you want to define a route that retrieves a client by ID, you’ll need to add a dynamic part:
-->
例えば、クライアント ID を受け取るようなルートを定義する場合、次のような動的パートを使うことができます。

```
GET   /clients/:id          controllers.Clients.show(id: Long)  
```

<!--
> Note that a URI pattern may have more than one dynamic part.
-->
> 一つの URI パターンには、複数の動的パートを含められます

<!--
The default matching strategy for a dynamic part is defined by the regular expression `[^/]+`, meaning that any dynamic part defined as `:id` will match exactly one URI part.
-->
動的パートをマッチ方法は、デフォルトでは `[^/]+` という正規表現になっています。したがって、 `:id` のように定義した動的パートは、ちょうど一つの URI パートにマッチします。

<!--
### Dynamic parts spanning several /
-->
### 複数の / をまたぐ動的パート

<!--
If you want a dynamic part to capture more than one URI path segment, separated by forward slashes, you can define a dynamic part using the `*id` syntax, which uses the `.*` regular expression:
-->
`/` から始まる複数の URI パスセグメントをキャプチャする動的パートを定義したい場合、 `*id` という文法を使った動的パートが使えます。これには `.*` という正規表現が使われます。

```
GET   /files/*name          controllers.Application.download(name)  
```

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
    
```
GET   /clients/$id<[0-9]+>  controllers.Clients.show(id: Long)  
```

<!--
## Call to the Action generator method
-->
## アクションジェネレータメソッドの呼び出し

<!--
The last part of a route definition is the call. This part must define a valid call to a method returning a `play.api.mvc.Action` value, which will typically be a controller action method.

If the method does not define any parameters, just give the fully-qualified method name:
-->
ルート定義の最後の部分は、アクションの呼び出しです。この部分には `play.api.mvc.Action` 型の値を返すようなメソッドの有効な呼び出しを書くことになっています。通常は、コントローラのアクションメソッドを呼び出すことになるでしょう。

メソッドに一つもパラメータが存在しない場合、単にメソッドの完全修飾名を書きます。

```
GET   /                     controllers.Application.homePage()
```

<!--
If the action method defines some parameters, all these parameter values will be searched for in the request URI, either extracted from the URI path itself, or from the query string.
-->
アクションメソッドにパラメータが存在する場合は、全てのパラメータがリクエスト URI から検索され、 URI パス部分かクエリストリングのいずれかから抽出されることになります。

```
# Extract the page parameter from the path.
GET   /:page                controllers.Application.show(page)
```

<!--
Or:
-->
または

```
# Extract the page parameter from the query string.
GET   /                     controllers.Application.show(page)
```

<!--
Here is the corresponding, `show` method definition in the `controllers.Application` controller:
-->
上記のルートに対応する `controllers.Application` コントローラの `show` メソッドは次のとおりです。

```scala
def show(page: String) = Action {
    loadContentFromDatabase(page).map { htmlContent =>
        Ok(htmlContent).as("text/html")
    }.getOrElse(NotFound)
}
```

<!--
### Parameter types
-->
### パラメータの型

<!--
For parameters of type `String`, typing the parameter is optional. If you want Play to transform the incoming parameter into a specific Scala type, you can explicitly type the parameter:
-->
`String` 型のパラメータについては、型は指定しなくても構いません。しかし、送られてきたパラメータを Play に他の型へ変換させたい場合は、パラメータの型を明記する必要があります。

```
GET   /client/:id           controllers.Clients.show(id: Long)
```

<!--
And do the same on the corresponding `show` method definition in the `controllers.Clients` controller:
-->
対応する `controllers.Clients` コントローラの `show` メソッドにも同様の型を定義します。

```scala
def show(id: Long) = Action {
    Client.findById(id).map { client =>
        Ok(views.html.Clients.display(client))
    }.getOrElse(NotFound)
}
```

<!--
### Parameters with fixed values
-->
### パラメータに固定値を渡す

<!--
Sometimes you’ll want to use a fixed value for a parameter:
-->
パラメータに固定値を渡したい場合は、次のようにします。

```
# Extract the page parameter from the path, or fix the value for /
GET   /                     controllers.Application.show(page = "home")
GET   /:page                controllers.Application.show(page)
```

<!--
### Parameters with default values
-->
### パラメータにデフォルト値を設定する

<!--
You can also provide a default value that will be used if no value is found in the incoming request:
-->
送信されてきたリクエストに値が存在しない場合にデフォルト値を使うためには、次のようにします。

```
# Pagination links, like /clients?page=3
GET   /clients              controllers.Clients.list(page: Int ?= 1)
```

<!--
### Optional parameters
-->
### 省略可能なパラメータ

<!--
You can also specify an optional parameter that does not need to be present in all requests:
-->
必ずしもリクエストに含まれていなくてもよい省略可能なパラメータを定義するためには、次のようにします。

```
# The version parameter is optional. E.g. /api/list-all?version=3.0
GET   /api/list-all         controllers.Api.list(Option[version])
```

<!--
## Routing priority  
-->
## ルートの優先度

<!--
Many routes can match the same request. If there is a conflict, the first route (in declaration order) is used.
-->
同じリクエストに複数のルートがマッチする可能性もあります。そのようなルートの競合が怒った場合、 (定義した順番が) 最初のルートが利用されます。

<!--
## Reverse routing
-->
## リバースルーティング

<!--
The router can also be used to generate a URL from within a Scala call. This makes it possible to centralize all your URI patterns in a single configuration file, so you can be more confident when refactoring your application.

For each controller used in the routes file, the router will generate a ‘reverse controller’ in the `routes` package, having the same action methods, with the same signature, but returning a `play.api.mvc.Call` instead of a `play.api.mvc.Action`. 

The `play.api.mvc.Call` defines an HTTP call, and provides both the HTTP method and the URI.

For example, if you create a controller like:  
-->
ルータはScalaから呼び出してURLを生成させるという使い方もできます。これにより、全ての URI パターンを一つの設定ファイルに集約することができ、アプリケーションのリファクタリングをより安全に行うことができるようになります。

ルータはルートファイルで使われるそれぞれのコントローラについて `routes` パッケージ以下に「リバースコントローラ」を生成します。リバースコントローラにはコントローラと同じシグネチャを持つ、同名のアクションメソッド定義されていますが、それぞれ `play.api.mvc.Action` の代わりに `play.api.mvc.Call` を返すようになっています。

`play.api.mvc.Call` は HTTP 呼び出しを表していて、 HTTP メソッドと URI が両方とも含まれてます。

例えば、次のようなコントローラを作成した場合を考えてみます。

```scala
package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
    
  def hello(name: String) = Action {
      Ok("Hello " + name + "!")
  }
    
}
```

<!--
And if you map it in the `conf/routes` file:
-->
そして、 `conf/routes` ファイルで次のようなマッピングを行ったとします。

```
# Hello action
GET   /hello/:name          controllers.Application.hello(name)
```

<!--
You can then reverse the URL to the `hello` action method, by using the `controllers.routes.Application` reverse controller:
-->
すると、 `controllers.routes.Application` リバースコントローラを利用することで、 `hello` アクションメソッドの URI を逆引きすることができます。

```scala
// Redirect to /hello/Bob
def helloBob = Action {
    Redirect(routes.Application.hello("Bob"))    
}
```

<!--
> **Next:** [[Manipulating results | ScalaResults]]
-->
> **次ページ:** [[レスポンスの操作 | ScalaResults]]


