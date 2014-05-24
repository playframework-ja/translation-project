<!-- translated -->
<!--
# Adding authentication
-->
# 認証機能の追加

We can now display a dashboard. But before we can go on to allow users to create, work on and assign tasks, we need a way for users to identify themselves.

<!--
## Implementing a login screen
-->
## ログインスクリーンの実装

<!--
To start off, let's implement a screen that allows a user to login.  Create a new route in `conf/routes` for the login screen:
-->
始めるに当たって、ユーザがログインできる画面を実装しましょう。`conf/routes` にログイン画面用の新しいルートを作成してください:

    GET     /login                  controllers.Application.login()

<!--
And now add the login action to `app/controllers/Application.java`:
-->
そしてここで `app/controllers/Application.java` に login アクションを追加してください:

```java
public static Result login() {
    return ok(
        login.render()
    );
}
```

In our action, we have referred to a new login template, let's write a skeleton for that template now, in `app/views/login.scala.html`:

```html
<html>
    <head>
        <title>Zentasks</title>
        <link rel="shortcut icon" type="image/png" href="@routes.Assets.at("images/favicon.png")">
        <link rel="stylesheet" type="text/css" media="screen" href="@routes.Assets.at("stylesheets/login.css")">
    </head>
    <body>
        <header>
            <a href="@routes.Application.index" id="logo"><span>Zen</span>tasks</a>
        </header>
    
    </body>
</html>
```

Now visit [http://localhost:9000/login] (http://localhost:9000/login) in your browser to check that our route is working.  Apart from the title, the page should be blank.

<!--
### Adding a form
-->
### フォームの追加

<!--
Our login page needs to contain a form, which will of course, hold an email address (username) and password.
-->
もちろん、ログインページには email アドレス (ユーザー名) とパスワードを保持するフォームが必要です。

Play provides a forms API for handling the rendering, decoding, and validation of forms.  Let's start off by implementing our form as a Java object.  Open the `app/controllers/Application.java` class, and declare a static inner class called `Login` at the end of it:

```java
public static class Login {

    public String email;
    public String password;

}
```

Now we need to pass this form into our template to render.  Modify the `login` method in `app/controllers/Application.java` to pass this form to the template:

```java
    public static Result login() {
        return ok(
            login.render(form(Login.class))
        );
    }
```

And now declare the form as a parameter for the login template to accept in `app/views/login.scala.html`:

```html
@(form: Form[Application.Login])

<html>
...
```

<!--
Now we need to render our form.  Add the form to the login template:
-->
フォームをレンダリングする必要があります。ログインテンプレートにフォームを追加しましょう:

```html
@helper.form(routes.Application.authenticate) {
   <h1>Sign in</h1>

   <p>
       <input type="email" name="email" placeholder="Email" value="@form("email").value">
   </p>
   <p>
       <input type="password" name="password" placeholder="Password">
   </p>
   <p>
       <button type="submit">Login</button>
   </p>
}
```

<!--
The important thing to notice here is the `@helper.form` call.  We have passed to it a route, `routes.Application.authenticate`.  This tells Play where this form should be submitted to.  Notice that there are no hard coded URLs here?  This means we can change our URL structure without silently breaking our application.
-->
ここで確認しておくべき重要なことは、`@helper.form` の呼び出しです。`routes.Application.authenticate` というルートを引き渡しました。これは、Play にこのフォームがどこに投稿されるべきかを伝えます。ここで URL が一切ハードコーディングされていないことに気付きましたか? これは、アプリケーションが突然の死を迎えること無しに URL の構造を変更できることを意味しています。

<!--
> Play actually offers a much richer set of form tags, but they are overkill for our login form. We will look at those later in the tutorial.
-->
> 実際には Play はもっとリッチなフォームタグのセットを提供していますが、このログインフォームには行き過ぎたものです。これらはこのチュートリアルの後半で目にすることになるでしょう。

<!--
Of course, the `authenticate` route hasn't been implemented yet, so let's implement it now.  First of all, add the route to `conf/routes`:
-->
もちろん、この `authenticate` ルートはまだ実装されていないので、ここで実装していきましょう。まず最初に、`conf/routes` にルートを追加します:

    POST    /login                      controllers.Application.authenticate()

<!--
Now implement the method in `app/controllers/Application.java`:
-->
そして `app/controllers/Application.java` にメソッドを実装します:

```java
public static Result authenticate() {
    Form<Login> loginForm = form(Login.class).bindFromRequest();
    return ok();
}
```

> Make sure you add the import statements `import play.data.*;` and `import static play.data.Form.*;` to `Application.java`

<!--
### Validating a form
-->
### フォームのバリデーション

Currently, our `authenticate` action is doing nothing but reading our form.  The next thing we want to do is validate the form. There is only one thing we are concerned with in the validation - is the username and password correct?  To implement this validation, we are going to write a `validate()` method on the `Login` class in `app/controllers/Application.java`.

```java
public String validate() {
    if (User.authenticate(email, password) == null) {
      return "Invalid user or password";
    }
    return null;
}
```

As you can see, this method is able to do any arbitrary validation, in our case, using the `User.authenticate()` method that we've already implemented. If validation fails, it returns a `String` with the error message; otherwise, `null` if validation passes.

<!--
We can now use this validation by using the `hasErrors()` method on our `Form` object in the `authenticate` action:
-->
これで `authenticate` アクションの `Form` オブジェクトで `hasErrors()` メソッドを使ってバリデーションを行うことができます:

```java
public static Result authenticate() {
    Form<Login> loginForm = form(Login.class).bindFromRequest();
    if (loginForm.hasErrors()) {
        return badRequest(login.render(loginForm));
    } else {
        session().clear();
        session("email", loginForm.get().email);
        return redirect(
            routes.Application.index()
        );
    }
}
```

<!--
This code introduces a number of new concepts.  Firstly, if validation fails, we return a status of `400 Bad Request`, rendering the login page with our form that had the failed validation.  By passing this form back, we can extract any validation errors from the form, and the values the user entered, and render them back to the user.
-->
このコードはいくつかの新しい概念を紹介しています。まず、バリデーションに失敗した場合は、バリデーションに失敗したフォームと共にログインページをレンダリングしつつ、`400 Bad Request` ステータスを返却します。フォームを返却する際、フォームからあらゆるバリデーションエラー、そしてユーザが入力した値を抽出し、それらをユーザに書き戻すことができます。

<!--
If the validation was successful, then we put an attribute into the session.  We call this attribute `email`, and it's value is the email address of the user that just successfully logged in.  We will use this session attribute later to find out the currently logged in user.
-->
バリデーションに成功した場合は、セッションに属性を追加します。この属性は `email` と呼ぶことにして、その値は今まさに正常にログインしたユーザの email アドレスです。このセッション属性は、後で現在ログインしているユーザを検出するために使用します。

<!--
After setting the user in the session, we issue an HTTP redirect to the dashboard.  You can see that we've used the reverse router, in the same way that we include assets in templates, to refer to the dashboard action.
-->
ユーザをセッションに格納した後、ダッシュボードへの HTTP リダイレクトを発行します。テンプレートにアセットを取り込むときと同じように、ダッシュボードアクションを参照するためにリバースルーターを使っていることが分かるでしょう。

We are almost finished with validation.  The one thing left to do is to display the error message when validation fails. You saw before that we passed the invalid form back to the template - we will use this to get the error message.  Place the following code in `app/views/login.scala.html`, just below the Sign In heading:

```html
@if(form.hasGlobalErrors) {
    <p class="error">
        @form.globalError.message
    </p>
}
```

<!--
Now try and log in with an invalid password.  You should see something like this:
-->
ここで不正なパスワードでログインしてみましょう。こんな感じに見えるはずです:

[[images/loginfail.png]]

<!--
Now reenter the valid password (`secret`), and login.  You should be taken to the dashboard.
-->
今度は正しいパスワード (`secret`) を入力し直してログインします。ダッシュボードに案内されるはずです。

<!--
## Testing your action
-->
## アクションのテスト

Now is a good time for us to start writing tests for our actions.  We've written an action that provides the ability to log in - let's check that it works.  Start by creating a skeleton test class called `test/controllers/LoginTest.java`:

```java
package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class LoginTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

}
```

> Notice that this time we've passed a `fakeGlobal()` to the fake application when we set it up.  In fact, since creating our "real" `Global.java`, the `ModelsTest` we wrote earlier has been broken because it is loading the initial data when the test starts.  So, it too should be updated to use `fakeGlobal()`.

<!--
Now let's write a test that tests what happens when we authenticate successfully:
-->
それでは、正常に認証した場合に何が起こるか確認するテストを書きましょう:

```java
@Test
public void authenticateSuccess() {
    Result result = callAction(
        controllers.routes.ref.Application.authenticate(),
        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
            "email", "bob@example.com",
            "password", "secret"))
    );
    assertEquals(303, status(result));
    assertEquals("bob@example.com", session(result).get("email"));
}
```

There are a few new concepts introduced here.  The first is the use of Play's "ref" reverse router.  This allows us to get a reference to an action, which we then pass to `callAction()` to invoke.  In our case, we've got a reference to the `Application.authenticate` action.

<!--
We are also creating a fake request.  We are giving this a form body with the email and password to authenticate with.
-->
フェイクリクエストも作成しています。認証に使用する email と password を保持するフォームボディを与えています。

Finally, we are using the `status()` and `session()` helper methods to get the status and the session of the result.  We ensure that the successful login occurred with Bob's email address being added to the session.  There are other helper methods available to get access to other parts of the result, such as the headers and the body.  You might wonder why we can't just directly get the result.  The reason for this is that the result may, for example, be asynchronous, and so Play needs to unwrap it if necessary in order to access it.

Run the test to make sure it passes.  Now let's write another test, this time to ensure that if an invalid email and password are supplied, that we don't get logged in:

```java
@Test
public void authenticateFailure() {
    Result result = callAction(
        controllers.routes.ref.Application.authenticate(),
        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
            "email", "bob@example.com",
            "password", "badpassword"))
    );
    assertEquals(400, status(result));
    assertNull(session(result).get("email"));
}
```

<!--
Run this test to ensure it passes.
-->
このテストを実行して、成功することを確認してください。

<!--
## Implementing authenticators
-->
## 認証機能の実装

<!--
Now that we are able to login, we can start protecting actions with authentication.  Play allows us to do this using action composition.  Action composition is the ability to compose multiple actions together in a chain.  Each action can do something to the request before delegating to the next action, and can also modify the result.  An action can also decide not to pass the request onto the next action, and instead generate the result itself.
-->
これでログインできるようになったので、アクションを認証機能で保護していきましょう。Play のアクション合成を使うことでこれを実現することができます。アクション合成は、複数のアクションを連鎖するようにまとめて組み立てる機能です。それぞれのアクションは、次のアクションにリクエストを委譲する前に何らかの処理を行うことができますし、処理結果を変更することもできます。あるアクションは、次のアクションにリクエストを引き渡さずに、自分自身で処理結果を生成することを決断することもできます。

Play already comes with a built in authenticator action, which we will extend to add our logic.  We will call this authenticator `Secured`.  Open `app/controllers/Secured.java`, and implement this class:

```java
package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
}
```

We have implemented two methods here.  `getUsername()` is used to get the username of the current logged in user.  In our case this is the email address, that we set in the `email` attribute in the session when the user logged in.  If this method returns a value, then the authenticator considers the user to be logged in, and lets the request proceed.  If, however, the method returns `null`, then the authenticator will block the request, and instead invoke `onUnathorized()`, which we have implemented to redirect to our login screen.

<!--
Now let's use this authenticator on the dashboard.  In `app/controllers/Application.java`, add the `@Security.Authenticated` annotation with our authenticator to the `index` method:
-->
ダッシュボードでこの認証機能を使ってみましょう。`app/controllers/Application.java` の `index` メソッドに、作成した認証機能を含めた `@Security.Authenticated` アノテーション追加してください:

```java
@Security.Authenticated(Secured.class)
public static Result index() {
    ...
```

<!--
### Testing the authenticator
-->
### 認証機能のテスト

<!--
Let's write a test for the authenticator now, to make sure it works, in `test/controllers/LoginTest.java`:
-->
この認証機能が動作することを確認するために、`test/controllers/LoginTest.java` にテストを書いていきましょう:

```java
@Test
public void authenticated() {
    Result result = callAction(
        controllers.routes.ref.Application.index(),
        fakeRequest().withSession("email", "bob@example.com")
    );
    assertEquals(200, status(result));
}    
```

<!--
Of course, the more important thing to test is that the request is blocked when you are not authenticated, so let's check that:
-->
もちろん、より重要なテストは認証されなかった場合にリクエストがブロックされることなので、これを確認しましょう:

```java
@Test
public void notAuthenticated() {
    Result result = callAction(
        controllers.routes.ref.Application.index(),
        fakeRequest()
    );
    assertEquals(303, status(result));
    assertEquals("/login", header("Location", result));
}
```

<!--
Run the tests to make sure that the authenticator works.
-->
テストを実行して認証機能が動作していることを確認してください。

<!--
## Logging out
-->
## ログアウト

Now try and visit the dashboard in a web browser.  If you logged in successfully before, you're probably now on the dashboard. The authenticator hasn't blocked you because you were already logged in.  You could close your browser and reopen it to log out, but now is as good a time as any for us to implement a log out action.  As always, start with the route:

    GET     /logout                     controllers.Application.logout()

<!--
And then implement the action in `app/controllers/Application.java`:
-->
そして、`app/controllers/Application.java` に実装します:

```java
public static Result logout() {
    session().clear();
    flash("success", "You've been logged out");
    return redirect(
        routes.Application.login()
    );
}
```

<!--
You can see we have cleared the session, this will log the user out.  There is one new concept here.  After clearing the session, we added an attribute to the flash scope, a success message.  The flash scope is similar to the session, except that the flash scope lasts only until the next request comes in.  This will allow us to render the success message in the login template when the redirected request comes in.  Let's add the message to `app/views/login.scala.html`, just as we did for the error message:
-->
セッションをクリアすることでユーザーをログアウトしているのが分かると思います。ここで新しい概念がひとつ登場します。セッションをクリアしたあと、flash スコープに属性として成功メッセージを追加しました。flash スコープは、次のリクエストがやってくるまでしか存続しないことを除いて、セッションと似ています。これを使うことで、リダイレクトリクエストがやってきたときにログインテンプレートに成功メッセージを表示することができます。エラーメッセージのときと同じように、`app/views/login.scala.html` にメッセージを追加しましょう:

```html
@if(flash.contains("success")) {
    <p class="success">
        @flash.get("success")
    </p>
}
```

<!--
Finally lets add a logout link to the main template, `app/views/main.scala.html`, inside the header section:
-->
最後に、メインテンプレート `app/views/main.scala.html` の見出し段落の中にログアウトリンクを追加しましょう:

```html
<header>
    <a href="@routes.Application.index" id="logo"><span>Zen</span>tasks</a>
    <dl id="user">
        <dt>User</dt>
        <dd>
            <a href="@routes.Application.logout()">Logout</a>
        </dd>
    </dl>
</header>
```

Now go to the dashboard in your browser, try logging out, and then visiting the dashboard again.  You should be unable to view the dashboard; it will redirect to the login screen.  Login, and you should be able to see the dashboard again.

<!--
## Using the current user
-->
## 現在のユーザーを使用する

<!--
There is one last thing that we want to do.  We can currently block access to an action based on whether we are logged in, but how can we access the currently logged in user?  The answer is through the `request.username()` method.  This will give us the email address of the current user.
-->
最後にやっておきたいことがもうひとつあります。今ではユーザーがログインしているかどうかによってアクセスをブロックすることができるようになりましたが、どうすれば現在ログインしているユーザーにアクセスするのでしょう? その答えは `request.username()` メソッドを通じて得られます。このメソッドは現在のユーザーの email アドレスを提供します。

Let's put the name of the user in the main template next to the logout link.  To get the name, we'll actually have to load the whole user from the database.  Let's also limit the projects to the one that the user is a member of, and the tasks to the ones that the user is assigned to, using the methods that we've already implemented on our models.

<!--
Start by loading the user in the `index` method in `app/controllers/Application.java`:
-->
`app/controllers/Application.java` の `index` メソッドでユーザーを読み込むところから始めます:

```java
@Security.Authenticated(Secured.class)
public static Result index() {
    return ok(index.render(
        Project.findInvolving(request().username()), 
        Task.findTodoInvolving(request().username()),
        User.find.byId(request().username())
    )); 
}
```

<!--
We've passed an additional parameter to the index template, so let's declare that parameter in `app/views/index.scala.html`, and pass it to the main template:
-->
index テンプレートに渡す引数を追加したので、`app/views/index.scala.html` に引数を宣言して、これをメインテンプレートに引き渡しましょう:

```html
@(projects: List[Project], todoTasks: List[Task], user: User)

@main(projects, user) {
    ...
```

<!--
And of course, we'll have to add it to our `app/views/main.scala.html` parameter declaration:
-->
もちろん `app/views/main.scala.html` にも引数の宣言を追加しなければなりません:

```html
@(projects: List[Project], user: User)(body: Html)
```

<!--
Now we can use it in the header, add it before the logout link we just added:
-->
これで見出しにユーザー情報を使用できるので、追加したばかりのログアウトリンクの前にユーザー情報を追加します:

```html
<dl id="user">
    <dt>@user.name <span>(@user.email)</span></dt>
    <dd>
        <a href="@routes.Application.logout()">Logout</a>
    </dd>
</dl>
```

<!--
Now visit the dashboard again, ensuring you are logged in.
-->
ログインしていることを確認したら、再度ダッシュボードにアクセスしてください。

[[images/dashboardloggedin.png]]

We can now see the currently logged in user, only the projects that the user has access to, and the tasks they have assigned to them.

<!--
As always, commit your work to git.
-->
いつも通り、git に作業内容をコミットしてください。

> Go to the [[next part|JavaGuide5]]
