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
The router is the component that translates each incoming HTTP request to an action call (a static, public method in a controller class).
-->
ルータはクライアントから受け取った HTTP リクエストをアクション (コントローラクラス内の static かつ public なメソッド) の呼び出しへ変換するコンポーネントです。

<!--
An HTTP request is seen as an event by the MVC framework. This event contains two major pieces of information:
-->
HTTP リクエストは MVC フレームワークにとってイベントであるといえます。このイベントには大きく分けて次の二つの情報が含まれています。

<!--
- the request path (such as `/clients/1542`, `/photos/list`), including the query string.
- the HTTP method (GET, POST, ...).
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
`conf/routes` is the configuration file used by the router. This file lists all of the routes needed by the application. Each route consists of an HTTP method and URI pattern associated with a call to an action method.
-->
`conf/routes` はルータによって読み込まれる設定ファイルです。このファイルには、アプリケーションが必要とする全てのルートをリストアップします。それぞれのルートは、HTTP メソッドと URI パターン、そしてそれらに割り当てられたアクションメソッドの呼び出しで表します。

<!--
Let’s see what a route definition looks like:
-->
実際のルート定義を見てみましょう。

```
GET   /clients/:id          controllers.Clients.show(id: Long)  
```

<!--
> Note that in the action call, the parameter type comes after the parameter name, like in Scala.
-->
> アクション呼び出しでは、Scala のように引数名の後に型を指定する事に注意して下さい。

<!--
Each route starts with the HTTP method, followed by the URI pattern. The last element of a route is the call definition.
-->
それぞれのルートでは、先頭に HTTP メソッド、その後に URI パターンが続きます。最後がアクション呼び出しの定義です。

<!--
You can also add comments to the route file, with the `#` character:
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
The URI pattern defines the route’s request path. Some parts of the request path can be dynamic.
-->
URI パターンはルートのリクエストパスの定義です。リクエストパスの一部を動的にすることができます。

<!--
### Static path
-->
### 静的パス

<!--
For example, to exactly match `GET /clients/all` incoming requests, you can define this route:
-->
例えば、リクエストを `GET /clients/all` に完全一致させたいときは、次のように定義できます。

```
GET   /clients              controllers.Clients.list()
```

<!--
### Dynamic parts 
-->
### 動的パート

<!--
If you want to define a route that, say, retrieves a client by id, you need to add a dynamic part:
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
The default matching strategy for a dynamic part is defined by the regular expression `[^/]+`, meaning that any dynamic part defined as `:id` will match exactly one URI path segment.
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
Here, for a request like `GET /files/images/logo.png`, the `name` dynamic part will capture the `images/logo.png` value.
-->
これで、`GET /files/images/logo.png` というリクエストに対して、動的パート `name` に `images/logo.png` という値をキャプチャさせることができます。

<!--
### Dynamic parts with custom regular expressions
-->
### 動的パートで独自の正規表現を使う

<!--
You can also define your own regular expression for a dynamic part, using the `$id<regex>` syntax:
-->
動的パートに独自の正規表現を使わせたい場合は、`$id<regex>` という文法を利用します。
    
```
GET   /clients/$id<[0-9]+>  controllers.Clients.show(id: Long)  
```

<!--
## Call to action generator method
-->
## アクションジェネレータメソッドの呼び出し

<!--
The last part of a route definition is the call. This part must define a valid call to an action method.
-->
ルート定義の最後のパートは、アクションの呼び出しです。このパートでは、アクションメソッド呼び出しを定義する必要があります。

<!--
If the method does not define any parameters, just give the fully-qualified method name:
-->
メソッドが一つも引数を取らない場合、単にメソッドの完全修飾名を指定します。

```
GET   /                     controllers.Application.homePage()
```

<!--
If the action method defines parameters, the corresponding parameter values will be searched for in the request URI, either extracted from the URI path itself, or from the query string.
-->
アクションメソッドが引数を取る場合、対応する引数はリクエスト URI のパスまたはクエリストリングから抽出されます。

パスから抽出するためには、

```
# Extract the page parameter from the path.
# i.e. http://myserver.com/index
GET   /:page                controllers.Application.show(page)
```

<!--
Or:
-->
また、クエリストリングから抽出するためには、

```
# Extract the page parameter from the query string.
# i.e. http://myserver.com/?page=index
GET   /                     controllers.Application.show(page)
```
のように定義します。

<!--
Here is the corresponding `show` method definition in the `controllers.Application` controller:
-->
このルートに対応する `controllers.Application` コントローラの `show` メソッドの定義は次のようになります。

```java
public static Result show(String page) {
  String content = Page.getContentOf(page);
  response().setContentType("text/html");
  return ok(content);
}
```

<!--
### Parameter types
-->
### 引数の型

<!--
For parameters of type `String`, the parameter type is optional. If you want Play to transform the incoming parameter into a specific Scala type, you can add an explicit type:
-->
`String` 型の引数の場合、型の記述はオプションです。リクエストパラメータを特定の Scala 型に変換したいときは、明示的に型を追記することができます。

```
GET   /client/:id           controllers.Clients.show(id: Long)
```

<!--
Then use the same type for the corresponding action method parameter in the controller:
-->
このルートに対応するコントローラのアクションメソッドの引数には同じ型を使用します。

```java
public static Result show(Long id) {
  Client client = Client.findById(id);
  return ok(views.html.Client.show(client));
}
```

<!--
> **Note:** The parameter types are specified using a suffix syntax. Also The generic types are specified using the `[]` symbols instead of `<>`, as in Java. For example, `List[String]` is the same type as the Java `List<String>`.
-->
> **ノート:** 引数の型は後置形式で指定されます。またジェネリックスの型は Java の `<>` 構文の代わりに `[]` で指定されます。例えば、 Java での `List<String>` は `List[String]` になります。

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
GET   /clients              controllers.Clients.list(page: Integer ?= 1)
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
The router can be used to generate a URL from within a Java call. This makes it possible to centralize all your URI patterns in a single configuration file, so you can be more confident when refactoring your application.
-->
Java コード中で URL を生成するためにルータを使うことができます。これにより、URI パターンの定義をただ一つの設定ファイルに集約することができ、アプリケーションをリファクタリングする際の間違いを減らすことができます。

<!--
For each controller used in the routes file, the router will generate a ‘reverse controller’ in the `routes` package, having the same action methods, with the same signature, but returning a `play.mvc.Call` instead of a `play.mvc.Result`. 
-->
ルータは、routes ファイルから利用された全てのコントローラについて、`routes` パッケージ以下に `リバースコントローラ` を生成します。リバースコントローラは元になったコントローラと同じシグネチャで、`play.mvc.Result` の代わりに `play.mvc.Call` を返すようなメソッドを持っています。

<!--
The `play.mvc.Call` defines an HTTP call, and provides both the HTTP method and the URI.
-->
`play.mvc.Call` は HTTP 呼び出しを表していて、HTTP メソッドと URL の両方の情報を持っています。

<!--
For example, if you create a controller like:
-->
例えば、次のようなコントローラを作成したとします。

```java
package controllers;

import play.*;
import play.mvc.*;

public class Application extends Controller {
    
  public static Result hello(String name) {
      return ok("Hello " + name + "!");
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

```java
// Redirect to /hello/Bob
public static Result index() {
    return redirect(controllers.routes.Application.hello("Bob")); 
}
```

<!--
> **Next:** [[Manipulating the response | JavaResponse]]
-->
> **次ページ:** [[レスポンスの変更 | JavaResponse]]
