<!-- translated -->
<!--
# Actions, Controllers and Results
-->
# アクション、コントローラ、レスポンス

<!--
## What is an Action?
-->
## アクションとは?

<!--
Most of the requests received by a Play application are handled by an `Action`. 
-->
Play アプリケーションが受け取ったリクエストのほとんどは、`Action` によって処理されます。

<!--
A `play.api.mvc.Action` is basically a `(play.api.mvc.Request => play.api.mvc.Result)` function that handles a request and generates a result to be sent to the client.
-->
基本的に `play.api.mvc.Action` は、リクエストを処理してクライアントへ送るレスポンスを生成する `(play.api.mvc.Request => play.api.mvc.Result)` 型の関数です。

```scala
val echo = Action { request =>
  Ok("Got request [" + request + "]")
}
```

<!--
An action returns a `play.api.mvc.Result` value, representing the HTTP response to send to the web client. In this example `Ok` constructs a **200 OK** response containing a **text/plain** response body.
-->
アクションは `play.api.mvc.Result` 型の値を返し、これはクライアントへ送信される HTTP レスポンスを表しています。上記の例では、`Ok` は コンテントタイプ **text/plain** のレスポンスボディを含む、 ステータス **200 OK** のレスポンスを生成します。

<!--
## Building an Action
-->
## アクションの定義

<!--
The `play.api.mvc.Action` companion object offers several helper methods to construct an Action value. 
-->
コンパニオンオブジェクト `play.api.mvc.Action` には、アクションの生成に役立ついくつかのヘルパーメソッドが用意されています。

<!--
The first simplest one just takes as argument an expression block returning a `Result`:
-->
まず、最もシンプルなヘルパーメソッドは `Result` 型の値を返すブロック式をひとつ引数に取ります。

```scala
Action {
  Ok("Hello world")
}
```

<!--
This is the simplest way to create an Action, but we don't get a reference to the incoming request. It is often useful to access the HTTP request calling this Action. 
-->
これはアクションを作成する最も簡単な方法ですが、受け取ったリクエストへの参照が得られていません。多くの場合、このアクションを呼び出した HTTP リクエストにアクセスできると便利です。

<!--
So there is another Action builder that takes as an argument a function `Request => Result`:
-->
そのため、`Request => Result` 型の関数を引数に取る別のアクションビルダが用意されています。

```scala
Action { request =>
  Ok("Got request [" + request + "]")
}
```

<!--
It is often useful to mark the `request` parameter as `implicit` so it can be implicitely used by other APIs that need it:
-->
パラメータ `request` に `implicit` を指定して、リクエストを暗黙的な引数として他の API に渡すテクニックは覚えておくと役立つことがあります。

```scala
Action { implicit request =>
  Ok("Got request [" + request + "]")
}
```

<!--
The last way of creating an Action value is to specify an additional `BodyParser` argument:
-->
アクションを生成する最後の方法は、他の引数に加えて `BodyParser` を指定するというものです。

```scala
Action(parse.json) { implicit request =>
  Ok("Got request [" + request + "]")
}
```

<!--
Body parsers will be covered later in this manual.  For now you just need to know that the other methods of creating Action values use a default **Any content body parser**.
-->
Body parser については後ほど説明します。今は、これまで説明してきたアクション生成用のヘルパーメソッドが、デフォルトで **Any content body parser** を使用することを知っていれば充分です。

<!--
## Controllers are action generators
-->
## コントローラとアクションジェネレータ

<!--
A `Controller` is nothing more than a singleton object that generates `Action` values. 
-->
`Controller` は `Action` を生成する、ただのシングルトンオブジェクトです。

<!--
The simplest use case for defining an action generator is a method with no parameters that returns an `Action` value	:
-->
アクションジェネレータを定義する最もシンプルな方法は、`Action` 型の値を返す引数なしのメソッドを定義するというものです。

```scala
package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("It works!")
  }
    
}
```

<!--
Of course, the action generator method can have parameters, and these parameters can be captured by the `Action` closure:
-->
もちろん、アクションジェネレータは引数を取ることができますし、その引数は `Action` のクロージャが捕捉することができます。

```scala
def hello(name: String) = Action {
  Ok("Hello " + name)
}
```

<!--
## Simple results
-->
## SimpleResult

<!--
For now we are just interested in simple results: An HTTP result with a status code, a set of HTTP headers and a body to be sent to the web client.
-->
次は、SimpleResult に注目してみましょう。 SimpleResult とは、web クライアントへ送信される HTTP レスポンスを表すオブジェクトで、ステータスコードとレスポンスヘッダ一式、メッセージボディをまとめたものです。

<!--
These results are defined by `play.api.mvc.SimpleResult`:
-->
SimpleResult は `play.api.mvc.SimpleResult` クラスとして定義されています。

```scala
def index = Action {
  SimpleResult(
    header = ResponseHeader(200, Map(CONTENT_TYPE -> "text/plain")), 
    body = Enumerator("Hello world!")
  )
}
```

<!--
Of course there are several helpers available to create common results such as the `Ok` result in the sample above:
-->
もちろん、これまでの例でお見せした `Ok` のように、よく使われるレスポンスを生成するヘルパーが用意されています。

```scala
def index = Action {
  Ok("Hello world!")
}
```

<!--
This produces exactly the same result as before.
-->
このコードは、一つ前のサンプルと同じ SimpleResult を生成します。

<!--
Here are several examples to create various results:
-->
SimpleResult を生成する例をいくつかご紹介します。

```scala
val ok = Ok("Hello world!")
val notFound = NotFound
val pageNotFound = NotFound(<h1>Page not found</h1>)
val badRequest = BadRequest(views.html.form(formWithErrors))
val oops = InternalServerError("Oops")
val anyStatus = Status(488)("Strange response type")
```

<!--
All of these helpers can be found in the `play.api.mvc.Results` trait and companion object.
-->
これらのヘルパーは全て `play.api.mvc.Results` トレイトまたはコンパニオンオブジェクトで定義されています。

<!--
## Redirects are simple results too
-->
## リダイレクトも SimpleResult

<!--
Redirecting the browser to a new URL is just another kind of simple result. However, these result types don't take a response body.
-->
ブラウザを新しい URL へリダイレクトさせることも SimpleResult の一種です。ただし、リダイレクトの SimpleResult はレスポンスボディを取りません。

<!--
There are several helpers available to create redirect results:
-->
リダイレクトを生成するヘルパーもいくつか用意されています。

```scala
def index = Action {
  Redirect("/user/home")
}
```

<!--
The default is to use a `303 SEE_OTHER` response type, but you can also set a more specific status code if you need one:
-->
デフォルトでは、`303 SEE_OTHER` のステータスコードが使われますが、必要に応じて別のステータスコードを指定することもできます。

```scala
def index = Action {
  Redirect("/user/home", status = MOVED_PERMANENTLY)
}
```

<!--
## "TODO" dummy page
-->
## "TODO" ダミーページ

<!--
You can use an empty `Action` implementation defined as `TODO`: the result is a standard ‘Not implemented yet’ result page:
-->
`TODO` という、空の `Action` 実装を利用することができます。このアクションのレスポンスは、Play 標準の 'Not implemented yet' ページです。

```scala
def index(name:String) = TODO
```

<!--
> **Next:** [[HTTP Routing | ScalaRouting]]
-->
> **次ページ:** [[HTTP ルーティング | ScalaRouting]]
