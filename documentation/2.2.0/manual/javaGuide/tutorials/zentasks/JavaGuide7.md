<!-- translated -->
<!--
# More backend tasks
-->
# 更なるバックエンドタスク

<!--
In this stage of the tutorial we are going to refine our skills at writing backend controllers.  We won't write anymore CoffeeScript, instead we'll use a script that's already been written for us.  You can download this script from here:
-->
チュートリアルのこの段階では、バックエンドコントローラを書くスキルに磨きをかけていきます。これ以上 CoffeeScript を書くつもりはないので、代わりに作成済みのスクリプトを使います。このスクリプトは ここ からダウンロードすることができます:

<!--
Replace `app/assets/javascripts/main.coffee` with this file.
-->
このファイルで `app/assets/javascripts/main.coffee` を置き換えてください。

<!--
## View tasks in a project
-->
## プロジェクト内のタスクの閲覧

<!--
The first thing we'll do is implement the ability to view the tasks in a project.  Currently you can get an overview on the dashboard, but we want the ability to click on a project and see the tasks for that project in detail, as well as the ability to manage that project and its tasks.
-->
最初に行うのは、プロジェクト内のタスクを閲覧する機能を実装することです。今のところ、ダッシュボードで概要を得ることはできますが、プロジェクトとそのタスクを管理する機能と同じように、プロジェクトをクリックすることで、そのプロジェクトのタスクを詳細に閲覧する機能が欲しいと思います。

<!--
Let's start by writing some more templates.  We'll implement an index template for the tasks in `app/views/tasks/index.scala.html`:
-->
追加のテンプレートをいくつか書くことから始めましょう。`app/views/tasks/index.scala.html` にタスク用の index テンプレートを実装します:

```html
dd@(project: Project, tasks: List[Task])
@(project: Project, tasks: List[Task])

<header>
    <hgroup>
        <h1>@project.folder</h1>
        <h2>@project.name</h2>
    </hgroup>
</header>
<article  class="tasks" id="tasks">
    @tasks.groupBy(_.folder).map {
        case (folder, tasks) => {
            @views.html.tasks.folder(folder, tasks)
        }
    }
    <a href="#newFolder" class="new newFolder">New folder</a>
</article>
```

<!--
This template breaks the tasks up into folders within the project, and then renders them each in a folders template.  It also provides a new folder button.  Let's implement the `app/views/tasks/folder.scala.html` template:
-->
このテンプレートは、タスクをプロジェクト内のフォルダーに分割して、それぞれをフォルダーテンプレート内にレンダリングします。新規フォルダーボタンも提供します。`app/views/tasks/folder.scala.html` テンプレートを実装しましょう:

```html
@(folder: String, tasks: List[Task])

<div class="folder" data-folder-id="@folder">
    <header>
        <input type="checkbox" />
        <h3>@folder</h3>
        <span class="counter"></span>
        <dl class="options">
            <dt>Options</dt>
            <dd>
                <a class="deleteCompleteTasks" href="#">Remove complete tasks</a>
                <a class="deleteAllTasks" href="#">Remove all tasks</a>
                <a class="deleteFolder" href="#">Delete folder</a>
            </dd>
        </dl>
        <span class="loader">Loading</span>
    </header>
    <ul class="list">
        @tasks.map { task =>
            @views.html.tasks.item( task )
        }
    </ul>
</div>
```

<!--
The folders template is providing some management options for the folder.  It also renders each task, reusing the item template that we already implemented.
-->
フォルダーテンプレートは、フォルダー用の管理オプションをいくつか提供しています。また、それぞれのタスクも実装済みの item テンプレートを再利用してレンダリングします。  

<!--
So now we can render our tasks, let's write the action for serving the tasks.  We'll start with a route in `conf/routes`:
-->
これでタスクをレンダリングできるようになったので、タスクを提供するアクションを書きましょう。`conf/routes` 内のルートから始めます:

    GET     /projects/:project/tasks    controllers.Tasks.index(project: Long)

<!--
And now let's create a new controller class, `app/controllers/Task.java`:
-->
そして、ここで新しい `app/controllers/Task.java` コントローラクラスを作成しましょう:

```java
package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import views.html.tasks.*;

@Security.Authenticated(Secured.class)
public class Tasks extends Controller {
}
```

<!--
As with the projects controller, every method on this controller is protected by the `Secured` authenticator.  Now let's implement the `index` action:
-->
Projects コントローラと同じように、このコントローラのすべてのメソッドは `Secured` 認証機能によって保護されています。それでは `index` アクションを実装しましょう:

```java
public static Result index(Long project) {
    if(Secured.isMemberOf(project)) {
        return ok(
            index.render(
                Project.find.byId(project),
                Task.findByProject(project)
            )
        );
    } else {
        return forbidden();
    }
}
```

<!--
This should look familiar from our earlier work on the projects controller.  This action uses a method we haven't implemented yet on our `Task` model, `findByProject`, so let's add that to `app/models/Task.java`:
-->
以前に Projects コントローラで行った作業と似ているように見えると思います。このアクションは、まだ `Task` モデルに実装していない `findByProject` メソッドを使っているので、これを `app/models/Task.java` に追加しましょう:

```java
public static List<Task> findByProject(Long project) {
    return Task.find.where()
        .eq("project.id", project)
        .findList();
}
```

We're now almost ready, we just need to provide a link to click on to get to this action.  Our new Coffeescript file that we've downloaded uses the backbone router to handle AJAX requests to pages, so if we create a link using the format `#/some/path`, it will update the page for that.  So we can use the reverse router to render these links for us.  Let's do that now, first in the navigation drawer, in `app/views/projects/item.scala.html`:

```html
...
<li data-project="@project.id">
    <a class="name" href="#@routes.Tasks.index(project.id)">@project.name</a>
    <button class="delete" href="@routes.Projects.delete(project.id)">Delete</button>
...
```

<!--
And now in the dashboard, in `app/views/index.scala.html`:
-->
そして、つぎに `app/views/index.scala.html` 内のダッシュボードです:

```html
...
    <div class="folder" data-folder-id="@project.id">
        <header>
            <h3><a href="#@routes.Tasks.index(project.id)">@project.name</a></h3>
        </header>
...
```

<!--
Now reload the screen, and you should be able to click the projects in the navigation drawer as well as in the dashboard, and you'll see our new tasks index page.
-->
ここで画面をリロードすると、ダッシュボードと同じように、ナビゲーションドローワーでプロジェクトをクリックすることができるようになっており、そして新しいタスク用の index ページを目にすることでしょう。

<!--
## Adding a task
-->
## タスクの追加

Let's now implement the ability to add a task.  We'll start off again with the template, we'll need to modify the `app/views/tasks/folder.scala.html` template we added before to include a form for adding tasks.  Place the form after the list of tasks in the folder, and before the folders closing `<div>`:

```html
...
    </ul>
    <form class="addTask">
        <input type="hidden" name="folder" value="@folder" />
        <input type="text" name="taskBody" placeholder="New task..." />
        <input type="text" name="dueDate" class="dueDate" placeholder="Due date: mm/dd/yy" />
        <div class="assignedTo">
            <input type="text" name="assignedTo" placeholder="Assign to..." />
        </div>
        <input type="submit" />
    </form>
</div>
```

<!--
Now the CoffeeScript code we added before already knows how to handle this form, we just need to implement an endpoint for it to use.  Let's start off by writing the route:
-->
先ほど追加した CoffeeScript は、このフォームをどのように取り扱うか既に知っているので、必要なのはスクリプトが使用するエンドポイントを実装することだけです。ルートを書くことから始めましょう:

    POST    /projects/:project/tasks    controllers.Tasks.add(project: Long, folder: String)

<!--
Notice here that this time, not only do we have the `project` path parameter, but we also have a `folder` parameter, and it isn't specified in the path.  This is the mechanism used to specify praameters that come from the query String.  So this route is saying that it expects POST requests that look something like this `/projects/123/tasks?folder=foo`.
-->
ここで、今回は `project` パスパラメータだけではなく、パスに指定されていない `folder` パラメータもあることに気を付けてください。これには、クエリ文字列からパラメータを取得する機能が使われます。そのため、このルートは `/projects/123/tasks?folder=foo` のような POST リクエストを期待することを告げています。

<!--
Now let's implement the `add` action in our `app/controllers/Tasks.java` controller:
-->
それでは、`app/controllers/Tasks.java` コントローラに `add` アクションを実装しましょう:

```java
public static Result add(Long project, String folder) {
    if(Secured.isMemberOf(project)) {
        Form<Task> taskForm = form(Task.class).bindFromRequest();
        if(taskForm.hasErrors()) {
            return badRequest();
        } else {
            return ok(
                item.render(Task.create(taskForm.get(), project, folder))
            );
        }
    } else {
        return forbidden();
    }
}
```

<!--
This action binds the request to our `Task` model object, treating it as a form.  It also validates it, but since we haven't defined any validation for the `Task` model, this will always pass.
-->
このアクションは、リクエストをフォームとして取り扱い、`Task` モデルオブジェクトに結び付けます。バリデーションも行いますが、`Task` モデルには何もバリデーションを定義していないので、これは常にパスします。

<!--
Since the CoffeeScript that invokes this action is going to do it using the Javascript reverse router, we'll need to add our new route to that.  Add it in `app/controllers/Application.java`:
-->
このアクションを起動する CoffeeScript は Javascript リバースルーターを使ってこれを行うので、このアクションに対する新しいルートを追加する必要があります。`app/controllers/Application.java` に追加してください:

```java
...
        controllers.routes.javascript.Projects.addGroup(),
        controllers.routes.javascript.Tasks.add()
    )
...
```

<!--
Now refresh the page, enter an event name and a valid email address, and hit enter, and you should have created a new task.
-->
これで、ページをリフレッシュして、イベント名と適切な email アドレスを入力し、enter を押すと、新しいタスクを作ることができるはずです。

<!--
### Validation and formatting
-->
### バリデーションとフォーマット

<!--
In the above action, we validated our task form, but we don't yet have any constraints defined for the task model.  Let's define them now.  Play supports annotation based validation.  Let's make the title field compulsary, add the required annotation to `app/models/Task.java`:
-->
上記のアクションでタスクフォームのバリデーションを行いましたが、タスクモデルにはまだ何の制約も定義していません。ここで定義しましょう。Play はアノテーションに基づいたバリデーションをサポートしています。`app/models/Task.java` に Required アノテーションを追加して title フィールドを必須にしましょう:

```java
import play.data.validation.*;

...
    @Constraints.Required
    public String title;
```

<!--
Additionally, when binding the form, we want to specify the format that the due date should be in.  You can do that using a format annotation:
-->
加えて、フォームを結び付ける際、期日が従っているべきフォーマットを指定したいと思います。Formats アノテーションを使うことができます:

```java
import play.data.format.*;

...
    @Formats.DateTime(pattern="MM/dd/yy")
    public Date dueDate;
```


