<!-- translated -->
<!--
# Adding some AJAX actions
-->
# AJAX アクションの追加

<!--
Now that we can log in, let's start writing functionality for our application.  We'll start simple, by adding dynamic functionality to the navigation drawer, that is, the sidebar with the list of projects.
-->
ログインできるようになったので、アプリケーションの機能を書いていきましょう。ナビゲーションドローワー、つまりプロジェクトのリストを含むサイドバーに、シンプルに動的な機能を追加するところから始めましょう。

<!--
To start off in this chapter we will implement the backend, including tests.
-->
この章では、テストを含むバックエンドの処理から実装します。

<!--
## The Projects controller
-->
## Projects コントローラ

<!--
We're going to add a few new backend actions, specifically to:
-->
具体的に、以下のいくつかのバックエンド処理を追加します。

<!--
* Add a project
* Rename a project
* Delete a project
* Add a group
-->
* プロジェクトを追加する
* プロジェクトをリネームする
* プロジェクトを削除する
* グループを追加する

<!--
To start, create a controller class called `app/controllers/Projects.java`:
-->
`app/controllers/Projects.java` と呼ばれるコントローラクラスを作ることから始めます:

```java
package controllers;

import java.util.*;

import static play.data.Form.form;
import play.mvc.*;
import views.html.projects.*;
import models.*;

@Security.Authenticated(Secured.class)
public class Projects extends Controller {

}
```

<!--
An important thing to notice here is that we have annotated the entire class with our security authenticator.  With the dashboard, we just annotated the method, but these annotations can also be placed at the class level to say that every method in this class must use this action.  This can save us a lot of boiler plate code, and also saves us from accidentally forgetting to annotate a method.
-->
ここで確認しておく重要な事項は、クラス全体をセキュリティ認証機能で注釈したことです。ダッシュボードではメソッドだけを注釈しましたが、クラス内のすべてのメソッドがこのアクションを使わなければならないことを伝えるために、これらのアノテーションをクラスレベルで付与することができます。これにより大量のボイラープレートコードを削減することができ、またメソッドをうっかり注釈し忘れることも防止できます。

<!--
Now let's add a method to create a new project:
-->
それでは、新しいプロジェクトを作成するメソッドを追加しましょう:

```java
public static Result add() {
    Project newProject = Project.create(
        "New project",
        form().bindFromRequest().get("group"),
        request().username()
    );
    return ok(item.render(newProject));
}
```

We've used our existing `create()` method on our `Project` model to create the new project, owned by the currently logged in user, which is returned by `request().username()`.

Also notice that we are reusing that `item` template that we created earlier to render the new project.  Now you'll begin to see why we created our templates in the structure that we did earlier.  This method only renders a small part of the page; that's ok, we'll be using this fragment from an AJAX action.

<!--
Let's now add a method to rename a project, but before we do, let's consider the security requirements of this function. A user should only be allowed to rename a project if they are a member of that project.  Let's write a utility method in our `app/controllers/Secured.java` class that checks this:
-->
続いてプロジェクトをリネームするメソッドを追加しようと思いますが、その前にこの機能のセキュリティ要件について考えてみましょう。あるユーザーは、自身がメンバーになっているプロジェクトのリネームだけが許可されるべきです。これを確認するユーティリティメソッドを `app/controllers/Secured.java` に書いてみましょう:

```java
public static boolean isMemberOf(Long project) {
    return Project.isMember(
        project,
        Context.current().request().username()
    );
}
```

<!--
You may notice here that we've used `Context.current()` to get the `request()`.  This is a convenient way to get access to a request if you aren't in an action.  Underneath, it uses thread locals to find the current request, response, session and so on.
-->
`request()` を取得するために `Context.current()` を使ったことに気付いたかもしれません。アクションの中にいない場合でも、この便利な方法でリクエストにアクセスすることができます。内部的には、現在のリクエスト、レスポンス、セッションやその他を見つけるために thread local を使用します。

Our `isMemberOf()` method has used a new method that we haven't written on our `Project` model yet.  In fact, we are going to need a few new methods on the `Project` object, so let's open `app/models/Project.java` now to add them:

```java
public static boolean isMember(Long project, String user) {
    return find.where()
        .eq("members.email", user)
        .eq("id", project)
        .findRowCount() > 0;
}

public static String rename(Long projectId, String newName) {
    Project project = find.ref(projectId);
    project.name = newName;
    project.update();
    return newName;
}
```

Having added a `rename()` method to our `Project` model, we are now ready to implement our action in `app/controllers/Projects.java`:

```java
public static Result rename(Long project) {
    if (Secured.isMemberOf(project)) {
        return ok(
            Project.rename(
                project,
                form().bindFromRequest().get("name")
            )
        );
    } else {
        return forbidden();
    }
}
```

This is the first time we've implemented an action that accepts a parameter, in this case it's a `Long` for the project.  This parameter is going to come from the path to our action, which you'll see later when we add a route for this action to the routes file.

<!--
You can see that first we check that the current user is a member of the project, and if they aren't, we return them a `forbidden` response.  Also notice our use of the `form()` method.  We've seen this before, when we were populating and validating our login form.  However this time, we haven't passed in a bean to decode the form into and to validate it with.  Rather, we've used what's called a dynamic form.  A dynamic form just parses a form submission into a map of string keys to string values, and is very convenient for simple form submissions with only one or two values where you don't want to do any validation. 
-->
最初に、現在のユーザーがプロジェクトのメンバーであることを確認し、そうでない場合は `forbidden` レスポンスを返却しているのが分かります。`form` メソッドを使っていることにも気付くでしょう。これは、以前にログインフォームを追加し、バリデーションを行ったときにも目にしてきました。しかし、今回はフォームの内容を展開し、バリデーションを行うための bean を渡していません。その代わりに、ダイナミックフォームと呼ばれるものを使っています。ダイナミックフォームは、投稿されたフォームを文字列のキーと値を持つマップに展開するだけのもので、とくにバリデーションを行いたくない、ひとつまたはふたつの値を投稿するシンプルなフォームを扱うときにとても便利です。

<!--
Let's move on to our method to delete a project:
-->
プロジェクトを削除するメソッドに移りましょう:

```java
public static Result delete(Long project) {
    if(Secured.isMemberOf(project)) {
        Project.find.ref(project).delete();
        return ok();
    } else {
        return forbidden();
    }
}
```

<!--
And finally, let's add a method to create a group:
-->
そして最後に、グループを作成するメソッドを追加しましょう:

```java
public static Result addGroup() {
    return ok(
        group.render("New group", new ArrayList<Project>())
    );
}
```

<!--
Now that we have our controller methods implemented, let's add routes to these controllers in `conf/routes`:
-->
これでコントローラのメソッドを実装し終えたので、これらコントローラへのルートを `conf/routes` に追加しましょう:

    POST    /projects                   controllers.Projects.add()
    POST    /projects/groups            controllers.Projects.addGroup()
    DELETE  /projects/:project          controllers.Projects.delete(project: Long)
    PUT     /projects/:project          controllers.Projects.rename(project: Long)

<!--
For the delete and rename projects, you can see that we've declared where it's parameter should come from.  In the path, we've used a colon to specify that this part of the path is dynamic, and that it should be passed to the action as the first parameter.  So when a PUT request is made to `/projects/123`, `Projects.rename(123)` will be invoked.
-->
プロジェクトの削除とリネームに、引数が渡されるべき場所を宣言しているのが分かります。パス中において動的な箇所を指定するためにコロンを使っており、この値はアクションの第一引数として引き渡されます。このため、`/projects/123` に対して PUT リクエストが送信された場合、`Projects.rename(123)` が実行されます。

<!--
Now do a quick refresh of the application in the browser, to make sure there are no compile errors.
-->
それでは、ブラウザからアプリケーションをさっとリフレッシュし、コンパイルエラーが発生していないことを確認してください。

<!--
## Testing your actions
-->
## アクションのテスト

<!--
As we did with the authentication actions, we're going to write tests for the actions we've just now written.  Let's start off with a `ProjectsTest` in `test/controllers/ProjectsTest.java`:
-->
認証アクションと同様に、いま書いたばかりのアクション用にテストを書いていきます。`test/controllers/ProjectsTest.java` の `ProjectsTest` から始めましょう:

```java
package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import models.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class ProjectsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

}
```

<!--
And now let's write a test for the new project action:
-->
新しいプロジェクトを作成するアクションのテストを書きましょう:

```java
@Test
public void newProject() {
    Result result = callAction(
        controllers.routes.ref.Projects.add(),
        fakeRequest().withSession("email", "bob@example.com")
            .withFormUrlEncodedBody(ImmutableMap.of("group", "Some Group"))
    );
    assertEquals(200, status(result));
    Project project = Project.find.where()
        .eq("folder", "Some Group").findUnique();
    assertNotNull(project);
    assertEquals("New project", project.name);
    assertEquals(1, project.members.size());
    assertEquals("bob@example.com", project.members.get(0).email);
}
```

<!--
You can see we've logged Bob in using `withSession`, and this time after invoking the request and making sure it was successful, we've queried the database to make sure that what we expected to happen would happen.
-->
`withSession` を使って Bob をログインさせたことが分かります。そしてその後、リクエストを実行し、これが成功したことを確認してから、期待したことが起こっていることを確認するためにデータベースに問い合わせました。

<!--
> It's always a good idea to check the status of the request before checking the side effects of the request.  The reason for this is that the asynchronous nature of Play framework means that even the test actions may run in a different thread.  By checking the status of the request, you are ensuring that Play has finished processing the request.
-->
> リクエスト結果の副作用について確認する前にリクエストのステータスを確認するのは、常に良い考えです。その理由は、Play framework が非同期であるという特性は、テストアクションですらも異なるスレッドで実行されるかもしれないことを意味するからです。リクエストのステータスをチェックすることで、Play がそのリクエストの処理を完了したことを保証したことになります。

<!--
Now let's write a test for the rename project action:
-->
プロジェクトをリネームするアクションのテストを書きましょう:

```java
@Test
public void renameProject() {
    long id = Project.find.where()
        .eq("members.email", "bob@example.com")
        .eq("name", "Private").findUnique().id;
    Result result = callAction(
        controllers.routes.ref.Projects.rename(id),
        fakeRequest().withSession("email", "bob@example.com")
            .withFormUrlEncodedBody(ImmutableMap.of("name", "New name"))
    );
    assertEquals(200, status(result));
    assertEquals("New name", Project.find.byId(id).name);
}
```

And also importantly, let's check that our authorization is working - making sure that someone who is not a member of a project can not change the name of that project:

```java
@Test
public void renameProjectForbidden() {
    long id = Project.find.where()
        .eq("members.email", "bob@example.com")
        .eq("name", "Private").findUnique().id;
    Result result = callAction(
        controllers.routes.ref.Projects.rename(id),
        fakeRequest().withSession("email", "jeff@example.com")
            .withFormUrlEncodedBody(ImmutableMap.of("name", "New name"))
    );
    assertEquals(403, status(result));
    assertEquals("Private", Project.find.byId(id).name);
}
```

<!--
Run these tests to make sure they work.  We've now seen a little bit more of how to implement actions, as well as how to test them.  We could write some more tests now, but for the purposes of this tutorial, we'll leave it there.  For practice, you can write a few more tests, testing the delete project and new group methods as well.
-->
これらのテストが動作することを確認するために、テストを実行してください。これまでで、アクションをどのように実装するか、そしてそれらをどのようにテストするのかについて、少しだけ見てきました。ここで更にテストを書くこともできますが、このチュートリアルの目的のために、それらはここに置いていきましょう。練習するために、プロジェクトを削除するメソッド、そして新しいプロエジェクトを作成するメソッドを確認する、さらにいくつかのテストを書くことができます。

<!--
Commit your work to git.
-->
作業内容を git にコミットしてください。

> Go to the [[next part|JavaGuide6]]
