<!-- translated -->
<!--
# HTTP routing
-->
# HTTPルーティング

<!--
## The built-in HTTP router
-->
## 組み込み HTTP ルータ

<!--
The router is the component in charge of translating each incoming HTTP request to an Action.
-->
「ルータ」はクライアントから受け取った HTTP リクエストをアクションへ変換するコンポーネントです。

<!--
An HTTP request is seen as an event by the MVC framework. This event contains two major pieces of information:
-->
HTTP リクエストは MVC フレームワークにとって「イベント」であるといえます。このイベントには大きく分けて次の二つの情報が含まれています。

<!--
- the request path (e.g. `/clients/1542`, `/photos/list`), including the query string
- the HTTP method (e.g. GET, POST, …).
-->
- クエリストリングを含むリクエストパス (例えば、`/clients/1542` や `/photos/list`)
- HTTP メソッド (GET、POST など)

<!--
Routes are defined in the `conf/routes` file, which is compiled. This means that you’ll see route errors directly in your browser:
-->
ルートは `conf/routes` ファイルに定義しておくことでコンパイルされます。これは、ルート定義に関するエラーもブラウザで直接的に確認できるということです。

[[images/routesError.png]]

<!--
## The routes file syntax
-->
## routesファイルの文法

<!--
`conf/routes` is the configuration file used by the router. This file lists all of the routes needed by the application. Each route consists of an HTTP method and URI pattern, both associated with a call to an `Action` generator.
-->
`conf/routes` はルータによって読み込まれる設定ファイルです。このファイルには、アプリケーションが必要とする全てのルートをリストアップします。それぞれのルートは、HTTP メソッドと URI パターン、そしてその二つに割り当てられたアクションジェネレータの呼び出しで表します。

<!--
Let’s see what a route definition looks like:
-->
実際のルート定義を見てみましょう。

```
GET   /clients/:id          controllers.Clients.show(id: Long)  
```

<!--
Each route starts with the HTTP method, followed by the URI pattern. The last element is the call definition.
-->
それぞれのルートでは、先頭に HTTP メソッド、その後に URI パターンが続きます。最後がアクション呼び出しの定義です。

<!--
You can also add comments to the route file, with the `#` character.
-->
`#` の文字を使って、routes ファイルにコメントを残すこともできます。

```
# Display a client.
GET   /clients/:id          controllers.Clients.show(id: Long)  
```

<!--
## The HTTP method
-->
## HTTPメソッド

<!--
The HTTP method can be any of the valid methods supported by HTTP (`GET`, `POST`, `PUT`, `DELETE`, `HEAD`).
-->
HTTP メソッドには、HTTP がサポートするあらゆるメソッド(`GET`、`POST`、`PUT`、`DELETE`、`HEAD`)が指定できます。

<!--
## The URI pattern
-->
## URIパターン

<!--
The URI pattern defines the route’s request path. Parts of the request path can be dynamic.
-->
URI パターンはルートのリクエストパスの定義です。リクエストパスの一部を動的にすることができます。

<!--
### Static path
-->
### 静的パス

<!--
For example, to exactly match incoming `GET /clients/all` requests, you can define this route:
-->
例えば、リクエストを `GET /clients/all` に完全一致させたいときは、次のように定義できます。

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
しかし、URL からクライアント ID を取得するような場合には、動的パートを追加する必要があります。

```
GET   /clients/:id          controllers.Clients.show(id: Long)  
```

<!--
> Note that a URI pattern may have more than one dynamic part.
-->
> 1 つの URI パターンには、2 つ以上の動的パートを含められます

<!--
The default matching strategy for a dynamic part is defined by the regular expression `[^/]+`, meaning that any dynamic part defined as `:id` will match exactly one URI part.
-->
動的パートのデフォルトのマッチ規則は正規表現でいうと `[^/]+` です。したがって、`:id` という動的パートはちょうど一つの URI パートにマッチします。


<!--
### Dynamic parts spanning several /
-->
### 複数の`/`をまたぐ動的パート

<!--
If you want a dynamic part to capture more than one URI path segment, separated by forward slashes, you can define a dynamic part using the `*id` syntax, which uses the `.*` regular expression:
-->
動的パートを使って、URI パスの `/` で分割された複数のセグメントをまとめてキャプチャしたいときは、`.*` という正規表現に対応する `*id` という文法が使えます。

```
GET   /files/*name          controllers.Application.download(name)  
```

<!--
Here for a request like `GET /files/images/logo.png`, the `name` dynamic part will capture the `images/logo.png` value.
-->
これで、`GET /files/images/logo.png` というリクエストに対して、動的パート `name` に `images/logo.png` という値をキャプチャさせることができます。

<!--
### Dynamic parts with custom regular expressions
-->
### 動的パートで独自の正規表現を使う

<!--
You can also define your own regular expression for the dynamic part, using the `$id<regex>` syntax:
-->
動的パートに独自の正規表現を使わせたい場合は、`$id<regex>` という文法が利用します。
    
```
GET   /clients/$id<[0-9]+>  controllers.Clients.show(id: Long)  
```

<!--
## Call to the Action generator method
-->
## アクションジェネレータメソッドの呼び出し

<!--
The last part of a route definition is the call. This part must define a valid call to a method returning a `play.api.mvc.Action` value, which will typically be a controller action method.
-->
ルート定義の最後のパートは、アクションの呼び出しです。このパートでは、`play.api.mvc.Action` の値を返すメソッドへの呼び出しを定義する必要があります。通常は、コントローラのアクションメソッドの呼び出しになります。

<!--
If the method does not define any parameters, just give the fully-qualified method name:
-->
メソッドが一つも引数を取らない場合、単にメソッドの完全修飾名を指定します。

```
GET   /                     controllers.Application.homePage()
```

<!--
If the action method defines some parameters, all these parameter values will be searched for in the request URI, either extracted from the URI path itself, or from the query string.
-->
アクションメソッドが引数を取る場合、全ての引数はリクエスト URI のパスまたはクエリストリングから抽出されます。

パスから抽出するためには、

```
# Extract the page parameter from the path.
GET   /:page                controllers.Application.show(page)
```

<!--
Or:
-->
また、クエリストリングから抽出するためには、

```
# Extract the page parameter from the query string.
GET   /                     controllers.Application.show(page)
```
のように定義します。

<!--
Here is the corresponding, `show` method definition in the `controllers.Application` controller:
-->
このルートに対応する `controllers.Application` コントローラの `show` メソッドの定義は次のようになります。

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
### 引数の型
<!--
For parameters of type `String`, typing the parameter is optional. If you want Play to transform the incoming parameter into a specific Scala type, you can explicitly type the parameter:
-->
`String` 型の引数の場合、型は記述しても、しなくても OK です。リクエストパラメータを特定の型に変換したいときは、型を明示することができます。

```
GET   /client/:id           controllers.Clients.show(id: Long)
```

<!--
And do the same on the corresponding `show` method definition in the `controllers.Clients` controller:
-->
このルートに対応する `controllers.Clients` コントローラの `show` メソッドの定義は次のようになります。

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
### 引数に定数を渡す

<!--
Sometimes you’ll want to use a fixed value for a parameter:
-->
アクションメソッドの引数に定数を渡したいときは、次のように記述します。

```
# Extract the page parameter from the path, or fix the value for /
GET   /                     controllers.Application.show(page = "home")
GET   /:page                controllers.Application.show(page)
```

<!--
### Parameters with default values
-->
### デフォルト引数

<!--
You can also provide a default value that will be used if no value is found in the incoming request:
-->
受け取ったリクエストに値がないとき、代わりにデフォルト値を渡すこともできます。

```
# Pagination links, like /clients?page=3
GET   /clients              controllers.Clients.list(page: Int ?= 1)
```

<!--
## Routing priority
-->
## ルートの優先度

<!--
Many routes can match the same request. If there is a conflict, the first route (in declaration order) is used.
-->
リクエストに複数のルートがマッチしてしまうことがあります。そのような競合が発生した場合は、先に宣言された方のルートが優先されます。

<!--
## Reverse routing
-->
## リバースルーティング

<!--
The router can also be used to generate a URL from within a Scala call. This makes it possible to centralize all your URI patterns in a single configuration file, so you can be more confident when refactoring your application.
-->
Scala コード中で URL を生成するためにルータを使うこともできます。これにより、URI パターンの定義をただ一つの設定ファイルに集約することができ、アプリケーションをリファクタリングする際の間違いを減らすことができます。

<!--
For each controller used in the routes file, the router will generate a ‘reverse controller’ in the `routes` package, having the same action methods, with the same signature, but returning a `play.api.mvc.Call` instead of a `play.api.mvc.Action`. 
-->
ルータは、routes ファイルから利用された全てのコントローラについて、`routes` パッケージ以下に「リバースコントローラ」を生成します。リバースコントローラは元になったコントローラと同じシグネチャで、`play.api.mvc.Result` の代わりに `play.api.mvc.Call` を返すようなメソッドを持っています。

<!--
The `play.api.mvc.Call` defines an HTTP call, and provides both the HTTP method and the URI.
-->
`play.api.mvc.Call` は HTTP 呼び出しを表していて、HTTP メソッドと URL の両方の情報を持っています。

<!--
For example, if you create a controller like:
-->
例えば、次のようなコントローラを作成したとします。

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
そして、このコントローラを `conf/routes` で次のようにマッピングしたとします。

```
# Hello action
GET   /hello/:name          controllers.Application.hello(name)
```

<!--
You can then reverse the URL to the `hello` action method, by using the `controllers.routes.Application` reverse controller:
-->
このとき、`controllers.routes.Application` というリバースコントローラを利用することで、`hello` というアクションメソッドの URL を逆引きすることができます。

```scala
// Redirect to /hello/Bob
def helloBob = Action {
    Redirect(routes.Application.hello("Bob"))    
}
```

<!--
> **Next:** [[Manipulating results | ScalaResults]]
-->
> **次ページ:** [[レスポンスの変更 | ScalaResults]]
