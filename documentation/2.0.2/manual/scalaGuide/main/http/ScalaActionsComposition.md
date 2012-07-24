<!--
# Action composition
-->
# アクション合成

<!--
This chapter introduce several ways of defining generic action functionality.
-->
この章では、アクションの一部機能を汎用的な形で切り出して定義する方法を紹介していきます。

<!--
## Basic action composition
-->
## アクション合成の基本

<!--
Let’s start with the simple example of a logging decorator: we want to log each call to this action.
-->
アクションの呼び出しをロギングする、簡単なロギング・デコーレーターの例から始めてみましょう。

<!--
The first way is not to define our own Action, but just to provide a helper method building a standard Action:
-->
一つめの方法は、アクション自体を定義する代わりに、アクションを生成するためのヘルパーメソッドを提供することです。

```scala
def LoggingAction(f: Request[AnyContent] => Result): Action[AnyContent] = {
  Action { request =>
    Logger.info("Calling action")
    f(request)
  }
}
```

<!--
That you can use as:
-->
このヘルパーメソッドは次のように利用できます。

```scala
def index = LoggingAction { request =>
  Ok("Hello World")    
}
```

<!--
This is simple but it works only with the default `parse.anyContent` body parser as we don't have a way to specify our own body parser. We can of course define an additional helper method:
-->
この方法はとても簡単ですが、ボディパーサーを外部から指定する方法がないため、デフォルトの `parse.anyContent` ボディパーサーしか利用できません。そこで、次のようなヘルパーメソッドも定義してみます。

```scala
def LoggingAction[A](bp: BodyParser[A])(f: Request[A] => Result): Action[A] = {
  Action(bp) { request =>
    Logger.info("Calling action")
    f(request)
  }
}
```

<!--
And then:
-->
これは次のように利用できます。

```scala
def index = LoggingAction(parse.text) { request =>
  Ok("Hello World")    
}
```

<!--
## Wrapping existing actions
-->
## 既存のアクションをラップする

<!--
Another way is to define our own `LoggingAction` that would be a wrapper over another `Action`:
-->
その他に、`LoggingAction` を `Action` のラッパーとして定義する方法もあります。

```scala
case class Logging[A](action: Action[A]) extends Action[A] {
  
  def apply(request: Request[A]): Result = {
    Logger.info("Calling action")
    action(request)
  }
  
  lazy val parser = action.parser
}
```

<!--
Now you can use it to wrap any other action value:
-->
これを利用すると、他のアクション値をラップすることができます。

```scala
def index = Logging { 
  Action { 
    Ok("Hello World")
  }
}
```

<!--
Note that it will just re-use the wrapped action body parser as is, so you can of course write:
-->
この方法ではラップされたアクションのボディパーサーがそのまま再利用されるため、次のような記述が可能です。

```scala
def index = Logging { 
  Action(parse.text) { 
    Ok("Hello World")
  }
}
```

<!--
> Another way to write the same thing but without defining the `Logging` class, would be:
-->
> 次のように、`Logging` クラスを定義せずに全く同じ機能を持つラッパーを記述することもできます。
> 
> ```scala
> def Logging[A](action: Action[A]): Action[A] = {
>   Action(action.parser) { request =>
>     Logger.info("Calling action")
>     action(request)
>   }
> }
> ```

<!--
## A more complicated example
-->
## さらに複雑な例

<!--
Let’s look at the more complicated but common example of an authenticated action. The main problem is that we need to pass the authenticated user to the wrapped action and to wrap the original body parser to perform the authentication.
-->
次は、「認証を伴うアクション」という、これまでより込み入った例を見てみましょう。ここでの問題は、認証されたユーザだけをラップしたアクションへ通すこと、また認証を行うために元のボディパーサーをラップするという２点です。

```scala
def Authenticated[A](action: User => Action[A]): Action[A] = {
  
  // Let's define an helper function to retrieve a User
  def getUser(request: RequestHeader): Option[User] = {
    request.session.get("user").flatMap(u => User.find(u))
  }
  
  // Wrap the original BodyParser with authentication
  val authenticatedBodyParser = parse.using { request =>
    getUser(request).map(u => action(u).parser).getOrElse {
      parse.error(Unauthorized)
    }          
  }
  
  // Now let's define the new Action
  Action(authenticatedBodyParser) { request =>
    getUser(request).map(u => action(u)(request)).getOrElse {
      Unauthorized
    }
  }
  
}
```

<!--
You can use it like this:
-->
このヘルパーメソッドは次のように利用することができます。

```scala
def index = Authenticated { user =>
  Action { request =>
    Ok("Hello " + user.name)      
  }
}
```

<!--
> **Note:** There is already an `Authenticated` action in `play.api.mvc.Security.Authenticated` with a better implementation than this example.
-->
> **注:** `play.api.mvc.Security.Authenticated` には ここで説明した例よりもっと良い `Authenticated` アクションの実装が用意されています。

<!--
## Another way to create the Authenticated action
-->
## 認証されたアクションの別の実装方法

<!--
Let’s see how to write the previous example without wrapping the whole action and without authenticating the body parser:
-->
次は、先ほどの例を、アクションを丸ごとラップせずに、かつボディパーサーの認証なしで記述してみましょう。

```scala
def Authenticated(f: (User, Request[AnyContent]) => Result) = {
  Action { request =>
    request.session.get("user").flatMap(u => User.find(u)).map { user =>
      f(user, request)
    }.getOrElse(Unauthorized)      
  }
}
```

<!--
To use this:
-->
これは次のように利用します。

```scala
def index = Authenticated { (user, request) =>
   Ok("Hello " + user.name)    
}
```

<!--
A problem here is that you can't mark the `request` parameter as `implicit` anymore. You can solve that using currying:
-->
ここでの問題は、もう `request` という引数を `implicit` 指定することはできない、ということです。これはカリー化を使うと解決できます。

```scala
def Authenticated(f: User => Request[AnyContent] => Result) = {
  Action { request =>
    request.session.get("user").flatMap(u => User.find(u)).map { user =>
      f(user)(request)
    }.getOrElse(Unauthorized)     
  }
}
```

<!--
Then you can do this:
-->
これは次のように利用することができます。

```scala
def index = Authenticated { user => implicit request =>
   Ok("Hello " + user.name)    
}
```

<!--
Another (probably simpler) way is to define our own subclass of `Request` as `AuthenticatedRequest` (so we are merging both parameters into a single parameter):
-->
別の (たぶんより簡単な) 方法は、`Request` のサブクラス `AuthenticatedRequest` を定義することです。二つの引数をひとつにまとめる、とも言い換えられます。

```scala
case class AuthenticatedRequest(
  val user: User, request: Request[AnyContent]
) extends WrappedRequest(request)

def Authenticated(f: AuthenticatedRequest => Result) = {
  Action { request =>
    request.session.get("user").flatMap(u => User.find(u)).map { user =>
      f(AuthenticatedRequest(user, request))
    }.getOrElse(Unauthorized)            
  }
}
```

<!--
And then:
-->
これを利用すると、次のような記述ができます。

```scala
def index = Authenticated { implicit request =>
   Ok("Hello " + request.user.name)    
}
```

<!--
We can of course extend this last example and make it more generic by making it possible to specify a body parser:
-->
ボディパーサーを指定できるようにすると、この実装はもっと一般的な形に拡張することができます。

```scala
case class AuthenticatedRequest[A](
  val user: User, request: Request[A]
) extends WrappedRequest(request)

def Authenticated[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result) = {
  Action(p) { request =>
    request.session.get("user").flatMap(u => User.find(u)).map { user =>
      f(AuthenticatedRequest(user, request))
    }.getOrElse(Unauthorized)      
  }
}

// Overloaded method to use the default body parser
import play.api.mvc.BodyParsers._
def Authenticated(f: AuthenticatedRequest[AnyContent] => Result): Action[AnyContent]  = {
  Authenticated(parse.anyContent)(f)
}
```

<!--
> **Next:** [[Asynchronous HTTP programming | ScalaAsync]]
-->
> **次ページ:** [[非同期 HTTP プログラミング | ScalaAsync]]