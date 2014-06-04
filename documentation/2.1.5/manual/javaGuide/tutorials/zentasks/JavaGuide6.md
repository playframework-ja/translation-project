<!-- translated -->
<!--
# Invoking actions from Javascript
-->
# Javascript によるアクションの実行

<!--
In the last the last chapter of the tutorial, we implemented a number of new actions that are going to be the backend for the navigation drawer.  In this chapter, we'll add the client side code necessary to complete the behavior for the navigation drawer.
-->
このチュートリアルのひとつ前の章で、ナビゲーションドローワーの舞台裏となるいくつかの新しいアクションを実装しました。この章では、このナビゲーションドローワーの振る舞いを完成するために必要なクライアント側のコードを追加します。

<!--
## Javascript routes
-->
## Javasctipt ルート

<!--
The first thing we need to do is implement a Javascript router.  While you could always just make AJAX calls using hard coded URLs, Play provides a client side router that will build these URLs and make the AJAX requests for you.  Building URLs to make AJAX calls can be quite fragile, and if you change your URL structure or parameter names at all, it can be easy to miss things when you update your Javascript code.  For this reason, Play has a Javascript router, that lets us call actions on the server, from Javascript, as if we were invoking them directly.
-->
最初に実装する必要があるのは Javascript ルーターです。常にただハードコードした URL を使って AJAX 呼び出しを行うこともできますが、Play はこれらの URL を組み立てて AJAX リクエストを送信するクライアントサイドルーターを提供しています。AJAX 呼び出しを行う URL の組み立てはとても壊れ易くなりがちで、URL の構造や引数の名前をちょっとでも変更していると、それは Javascript コードを更新する際に簡単に忘れられてしまいます。このような理由から、Play にはサーバー上のアクションを、まるで直接実行するかのように呼び出すことのできる Javascript ルーターが備わっています。

<!--
A Javascript router needs to be generated from our code, to say what actions it should include.  It can be implemented as a regular action that your client side code can download using a script tag.  Alternatively Play has support for embedding the router in a template, but for now we'll just use the action method.  Write a Javascript router action in `app/controllers/Application.java`:
-->
Javascript ルーターは、どのアクションが含まれるべきかを示すためにコードから生成される必要があります。これは、クライアント側のコードが script タグを使ってダウンロードすることのできる通常のアクションとして実装することができます。別の方法として、Play はテンプレートへのルーターの埋め込みもサポートしていますが、ここでは単純にアクションメソッドを使います。以下のように、`app/controllers/Application.java` に Javascript ルーターを書いてください:

```java
public static Result javascriptRoutes() {
    response().setContentType("text/javascript");
    return ok(
        Routes.javascriptRouter("jsRoutes",
            controllers.routes.javascript.Projects.add(),
            controllers.routes.javascript.Projects.delete(),
            controllers.routes.javascript.Projects.rename(),
            controllers.routes.javascript.Projects.addGroup()
        )
    );
}
```

<!--
We've set the response content type to be `text/javascript`, because the router will be a Javascript file.  Then we've used `Routes.javascriptRouter` to generate the routes.  The first parameter that we've passed to it is `jsRoutes`, this means the router will be bound to the global variable by that name, so in our Javascript/CoffeeScript code, we'll be able to access the router using that variable name.  Then we've passed the list of actions that we want in the router.
-->
このルーターは Javascript ファイルになるので、レスポンスのコンテントタイプを `text/javascript` に設定しました。その後、ルートを生成するために `Routes.javascriptRouter` を使いました。第一引数に `jsRoutes` を渡しているのは、ルーターがこの名前のグローバル変数に結びつくことを意味しており、そのため Javascript/CoffeeScript コードでは、この名前を使ってルーターにアクセスすることができます。続いて、ルーターで使いたいアクションのリストを渡しました。

<!--
Of course, we need to add a route for that in the `conf/routes` file:
-->
もちろん、`conf/routes` ファイルにルートを追加する必要があります:

    GET     /assets/javascripts/routes          controllers.Application.javascriptRoutes()

<!--
Now before we implement the client side code, we need to source all the javascript dependencies that we're going to need in the `app/views/main.scala.html`:
-->
クライアント側のコードを実装する前に、`app/views/main.scala.html` で必要になるすべての javascript の依存性を調達する必要があります:

```html
<script type="text/javascript" src="@routes.Assets.at("javascripts/jquery-1.7.1.js")"></script>
<script type="text/javascript" src="@routes.Assets.at("javascripts/jquery-play-1.7.1.js")"></script>
<script type="text/javascript" src="@routes.Assets.at("javascripts/underscore-min.js")"></script>
<script type="text/javascript" src="@routes.Assets.at("javascripts/backbone-min.js")"></script>
<script type="text/javascript" src="@routes.Assets.at("javascripts/main.js")"></script>
<script type="text/javascript" src="@routes.Application.javascriptRoutes()"></script>
```

<!--
## CoffeeScript
-->
## CoffeeScript

<!--
We are going to implement the client side code using CoffeeScript.  CoffeeScript is a nicer to use Javascript, it compiles to Javascript and is fully interoperable with Javascript, so we can use our Javascript router for example from it.  We could use Javascript, but since Play comes built in with a CoffeeScript compiler, we're going to use that instead.  When we added the Javascript dependencies to `main.scala.html`, we added a dependency on `main.js`.  This doesn't exist yet, it is going to be the artifact that will be produced from the CoffeeScript compilation.  Let's implement it now, open `app/assets/javascripts/main.coffee`:
-->
CoffeeScript を使ってクライアント側のコードを実装しましょう。CoffeeScript は、Javascript を便利にするものです。CoffeeScript は Javascript にコンパイルされ、Javascript と完全に互換性があるので、CoffeeScript のサンプルとして Javascript ルーターを使うことができます。Javascript を使うこともできますが、Play には CoffessScript コンパイラが組み込まれているので、Javascript の代わりに CoffessScript を使ってみましょう。`main.scala.html` に Javascript の依存性を追加した際、`main.js` への依存性を追加しました。これは CoffeeScript をコンパイルすると生成されることになる成果物なので、まだ存在しません。さっそく実装しましょう。`app/assets/javascripts/main.coffee` を開いてください:

```coffeescript
$(".options dt, .users dt").live "click", (e) ->
    e.preventDefault()
    if $(e.target).parent().hasClass("opened")
        $(e.target).parent().removeClass("opened")
    else
        $(e.target).parent().addClass("opened")
        $(document).one "click", ->
            $(e.target).parent().removeClass("opened")
    false

$.fn.editInPlace = (method, options...) ->
    this.each ->
        methods = 
            # public methods
            init: (options) ->
                valid = (e) =>
                    newValue = @input.val()
                    options.onChange.call(options.context, newValue)
                cancel = (e) =>
                    @el.show()
                    @input.hide()
                @el = $(this).dblclick(methods.edit)
                @input = $("<input type='text' />")
                    .insertBefore(@el)
                    .keyup (e) ->
                        switch(e.keyCode)
                            # Enter key
                            when 13 then $(this).blur()
                            # Escape key
                            when 27 then cancel(e)
                    .blur(valid)
                    .hide()
            edit: ->
                @input
                    .val(@el.text())
                    .show()
                    .focus()
                    .select()
                @el.hide()
            close: (newName) ->
                @el.text(newName).show()
                @input.hide()
        # jQuery approach: http://docs.jquery.com/Plugins/Authoring
        if ( methods[method] )
            return methods[ method ].apply(this, options)
        else if (typeof method == 'object')
            return methods.init.call(this, method)
        else
            $.error("Method " + method + " does not exist.")
```

<!--
Now the code that you see above may be a little overwhelming to you.  The first block of code activates all the option icons in the page, it's straight forward jquery.  The second is an extension to jquery that we'll use a bit later, that turns a span into one that can be edited in place.  These are just some utility methods that we are going to need to help with writing the rest of the logic.
-->
上記のコードに少し圧倒されたかもしれません。コードの最初の段落は、単純な jquery であり、ページ内のすべての option のアイコンを活性化します。二番目の段落は、ある領域をその場で編集できるように変更する jquery の拡張で、これは後ほど利用します。これらは、ロジックの残りの部分を書く際に必要となるユーティリティメソッドに過ぎません。

<!--
Let's start to write our Backbone views:
-->
Backbone ビューを書いていきましょう:

```coffeescript
class Drawer extends Backbone.View

$ -> 
    drawer = new Drawer el: $("#projects")
```

<!--
We've now bound our drawer, which has an id of `projects`, to a Backbone view called `Drawer`.  But we haven't done anything useful yet.  In the initialize function of the drawer, let's bind all the groups to a new `Group` class, and all the projects in each group to a new `Project` class:
-->
`Drawer` と呼ばれる Backbone ビューに、`projects` の id を持つ drawer を結び付けました。しかし、今のところ何も便利になっていません。drawer の初期化メソッドで、すべてのグループを新しい `Group` クラスに、そしてそれぞれのグループ内のすべてのプロジェクトを新しい `Project` クラスに結び付けましょう:

```coffeescript
class Drawer extends Backbone.View
    initialize: ->
        @el.children("li").each (i,group) ->
            new Group
                el: $(group)
            $("li",group).each (i,project) ->
                new Project
                    el: $(project)

class Group extends Backbone.View

class Project extends Backbone.View
```

<!--
Now we'll add some behavior to the groups.  Let's first add a toggle function to the group, so that we can hide and display the group:
-->
それではグループにいくつかの振る舞いを追加します。最初に、グループを隠したり表示したりできるよう、切り替え機能を追加しましょう:

```coffeescript
class Group extends Backbone.View
    events:
        "click    .toggle"          : "toggle"
    toggle: (e) ->
        e.preventDefault()
        @el.toggleClass("closed")
        false
```

<!--
Earlier when we created our groups template, we added some buttons, including a new project button.  Let's bind a click event to that:
-->
以前にグループのテンプレートを作成した際、新しいプロジェクト用のボタンを含む、いくつかのボタンを追加しました。これにクリックイベントを結び付けましょう:

```coffeescript
class Group extends Backbone.View
    events:
        "click    .toggle"          : "toggle"
        "click    .newProject"      : "newProject"
    newProject: (e) ->
            @el.removeClass("closed")
        jsRoutes.controllers.Projects.add().ajax
            context: this
            data:
                group: @el.attr("data-group")
            success: (tpl) ->
                _list = $("ul",@el)
                _view = new Project
                    el: $(tpl).appendTo(_list)
                _view.el.find(".name").editInPlace("edit")
            error: (err) ->
                $.error("Error: " + err)
```

<!--
Now you can see that are are using the `jsRoutes` Javascript router that we created before.  It almast looks like we are just making an ordinary call to the `Projects.add` action.  Invoking this actually returns an object that gives us a method for making ajax requests, as well as the ability to get the URL and method for the action.  But, this time you can see we are invoking the `ajax` method, passing in the group name as part of the `data`, and then passing `success` and `error` callbacks.  In fact, the `ajax` method just delegates straight to jQuery's `ajax` method, supplying the URL and the method along the way, so anything you can do with jQuery, you can do here.
-->
以前に作った Javascript ルーターである `jsRoutes` を使っていることが分かります。これは、ほとんど `Projects.add` アクションを通常どおり呼び出しているように見えます。これを起動すると、ajax リクエストを行うメソッドのほか、アクションの URL およびメソッドを取得する機能を提供するオブジェクトが返却されます。しかし、ここでは `data` の一部としてグループの名前を渡し、そして `success` と `error` コールバックを渡して `ajax` を実行しているのが分かります。実際のところ、`ajax` メソッドは URL とメソッドを jQuery の `ajax` メソッドの作法に従ってそのまま委譲しているだけなので、jQuery でできることはすべてここで行うことができます。

<!--
> You don't have to use jQuery with the Javascript router, it's just the default implementation that Play provides.  You could use anything, by supplying your own ajax function name to call to the Javascript router when you generate it.
-->
> Javascript ルーターを jQuery と併せて使うのは、Play が提供するデフォルト実装に過ぎず、jQuery を使わなければならないということではありません。Javascript ルーターを生成する際に、呼び出す独自の関数名を引き渡すことで、どんな ajax 関数でも使うことができます。

<!--
Now if you refresh the page, you should be able to create a new project.  However, the new projects name is "New Project", not really what we want.  Let's implement the functionality to rename it:
-->
ここでページをリフレッシュすると、新しいプロジェクトを作成できるようになっているはずです。しかし、新しいプロジェクトの名前は "New Project"  であり、望ましいものではありません。これをリネームする機能を実装しましょう:

```coffeescript
class Project extends Backbone.View
    initialize: ->
        @id = @el.attr("data-project")
        @name = $(".name", @el).editInPlace
            context: this
            onChange: @renameProject
    renameProject: (name) ->
        @loading(true)
        jsRoutes.controllers.Projects.rename(@id).ajax
            context: this
            data:
                name: name
            success: (data) ->
                @loading(false)
                @name.editInPlace("close", data)
            error: (err) ->
                @loading(false)
                $.error("Error: " + err)
    loading: (display) ->
        if (display)
            @el.children(".delete").hide()
            @el.children(".loader").show()
        else
            @el.children(".delete").show()
            @el.children(".loader").hide()
```

<!--
First we've declared the name of our project to be editable in place, using the helper function we added earlier, and passing the `renameProject` method as the callback.  In our `renameProject` method, we've again used the Javascript router, this time passing a parameter, the id of the project that we are to rename.  Try it out now to see if you can rename a project, by double clicking on the project.
-->
最初に、以前に追加したヘルパー関数を使い、かつコールバックとして `renameProject` メソッドを渡すことで、その場で編集可能なプロジェクト名を定義しました。この `renameProject` メソッドで、今度はリネームしようとするプロジェクトの id を引数として渡しながら再度 Javascript ルーターを使っています。ここで、プロジェクト上でダブルクリックして、プロジェクトをリネームできるかどうかを確認してみてください。

<!--
The last thing we want to implement for projects is the remove method, binding to the remove button.  Add the following CoffeeScript to the `Project` backbone class:
-->
プロジェクト用に最後に実装したいのは、削除ボタンに結び付いた削除メソッドです。`Project` Backbone クラスに以下の CoffessScript を追加してください:

```coffeescript
    events:
        "click      .delete"        : "deleteProject"
    deleteProject: (e) ->
        e.preventDefault()
        @loading(true)
        jsRoutes.controllers.Projects.delete(@id).ajax
            context: this
            success: ->
                @el.remove()
                @loading(false)
            error: (err) ->
                @loading(false)
                $.error("Error: " + err)
        false
```

<!--
Once again, we are using the Javascript router to invoke the delete method on the `Projects` controller.
-->
ここで再び、`Projects` コントローラ上の delete メソッドを実行するために Javascript ルーターを使っています。

<!--
As one final task that we'll do is add a new group button to the main template, and implement the logic for it.  So let's add a new group button to the `app/views/main.scala.html` template, just before the closing `</nav>` tag:
-->
最後のタスクとして行いたいのは、メインテンプレートに新規グループボタンを追加し、このロジックを実装することです。`app/views/main.scala.html` テンプレートの `</nav>` タグの直前に新規グループボタンを追加しましょう:

```html
    </ul>
    <button id="newGroup">New group</button>
</nav>
```

<!--
Now add an `addGroup` method to the `Drawer` class, and some code to the `initialize` method that binds clicking the `newGroup` button to it:
-->
それでは、`Drawer` クラスに `addGroup` メソッドを追加し、このメソッドを `newGroup` ボタンのクリックに結び付けるコードを `initialize` に追加します:

```coffeescript
class Drawer extends Backbone.View
    initialize: ->
        ...
        $("#newGroup").click @addGroup
    addGroup: ->
        jsRoutes.controllers.Projects.addGroup().ajax
            success: (data) ->
                _view = new Group
                    el: $(data).appendTo("#projects")
                _view.el.find(".groupName").editInPlace("edit")
```

<!--
Try it out, you can now create a new group.
-->
これで新規グループが作成できるようになったことを試してみてください。

<!--
## Testing our client side code
-->
## クライアントサイドコードのテスト

<!--
Although we've tested manually that things are working, Javascript apps can be quite fragile, and easy to break in future.  Play provides a very simple mechanism for testing client side code using [FluentLenium](https://github.com/FluentLenium/FluentLenium).  FluentLenium provides a simple way to represent your pages and the components on them in a way that is reusable, and let's you interact with them and make assertions on them.
-->
万事うまく動作することを手動でテストしてきましたが、Javascript アプリケーションはとてももろくなりがちで、そして将来的には簡単に壊れてしまいます。Play は、[FluentLenium](https://github.com/FluentLenium/FluentLenium) を使ってクライアントサイドコードをテストするとてもシンプルな機構を提供しています。FluentLenium は、ページとコンポーネントを再利用できるやり方でシンプルに表現する方法を提供し、ページと相互にやり取りして、その妥当性を検証させてくれます。

<!--
### Implementing page objects
-->
### ページオブジェクトの実装

<!--
Let's start by creating a page that represents our login page.  Open `test/pages/Login.java`:
-->
ログインページを表現するページを作成するところから始めましょう。`test/pages/Login.java` を開いてください:

```java
package pages;

import org.fluentlenium.core.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import components.*;
import controllers.*;

public class Login extends FluentPage {
    public String getUrl() {
        return routes.Application.login().url();
    }

    public void isAt() {
        assertThat(find("h1", withText("Sign in"))).hasSize(1);
    }

    public void login(String email, String password) {
        fill("input").with(email, password);
        click("button");
    }
}
```

<!--
You can ee three methods here.  Firstly, we've declared the URL of our page, conveniently using the reverse router to get this.  Then we've implemented an `isAt` method, this runs some assertions on the page to make sure that we are at this page.  FluentLenium will use this when we go to the page to make sure everything is as expected.  We've written a simple assertion here to ensure that the heading is the login page heading.  Finally, we've implemented an action on the page, which fills the login form with the users email and password, and then clicks the login button.
-->
メソッドが三つあるのが分かります。まず、利便性のためにリバースルーターを使ってページの URL を取得して、これを宣言しました。その後、このページにいることを確認するために、このページ上でいくつかのアサーションを実行する `isAt` メソッドを実装しました。FluentLenium は、すべてが期待どおりであることを確認するためにこのページを訪れたとき、このメソッドを使用します。ここでは、見出しがログインページのものであることを確認するシンプルなアサーションを書きました。最後に、ログインフォームにユーザの email とパスワードを入力し、その後でログインボタンをクリックする、このページのアクションを実装しました。

<!--
> You can read more about FluentLenium and the APIs it provides [here](https://github.com/FluentLenium/FluentLenium). We won't go into any more details in this tutorial.
-->
> FluentLenium の詳細と提供されている API は [ここ](https://github.com/FluentLenium/FluentLenium) で読むことができます。このチュートリアルでは、これ以上詳しく触れません。

<!--
Now that we can log in, let's create a page that represents the dashboard in `test/pages/Dashboard.java`:
-->
これでログインできるようになりましたので、`test/pages/Dashboard.java` にダッシュボードを表現するページを作りましょう:

```java
package pages;

import org.fluentlenium.core.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import components.*;

public class Dashboard extends FluentPage {
    public String getUrl() {
        return "/";
    }

    public void isAt() {
        assertThat(find("h1", withText("Dashboard"))).hasSize(1);
    }

    public Drawer drawer() {
        return Drawer.from(this);
    }
}
```

<!--
It is similarly simple, like the login page.  Eventually we will add more functionality to this page, but for now since we're only testing the drawer, we just provide a method to get the drawer.  Let's see how implementation of the drawer, in `test/components/Drawer.java`:
-->
ログインページと同様にシンプルです。最終的にはこのページにより多くの機能を追加しますが、今は drawer をテストしたいだけなので、drawer を取得するメソッドだけを提供します。どのようにして `test/components/Drawer.java` に drawer を実装するのか見ていきましょう:

```java
ackage components;

import java.util.*;

import org.fluentlenium.core.*;
import org.fluentlenium.core.domain.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class Drawer {

    private final FluentWebElement element;

    public Drawer(FluentWebElement element) {
        this.element = element;
    }

    public static Drawer from(Fluent fluent) {
        return new Drawer(fluent.findFirst("nav"));
    }

    public DrawerGroup group(String name) {
        return new DrawerGroup(element.findFirst("#projects > li[data-group=" + name + "]"));
    }
}
```

<!--
Since our drawer is not a page, but rather is a component of a page, we haven't extended `FluentPage` this time, rather we are simply wrapping a `FluentWebElement`, this is the `<nav>` element that the drawer lives in.  We've provided a method to look up a group by name, let's see the implementation of the group now, open `test/components/DrawerGroup.java`:
-->
drawer はページではなく、ページのコンポーネントであるため、今回は `FluentPage` を拡張せず、drawer が存在する `<nav>` 要素をである `FluentWebElement` をシンプルにラップしました。グループを名前で検索するメソッドを提供したので、ここで `test/components/DrawerGroup.java` を開いて、グループをどのように実装するのか見てみましょう:

```java
package components;

import org.fluentlenium.core.*;
import org.fluentlenium.core.domain.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import java.util.*;
import com.google.common.base.Predicate;

public class DrawerGroup {
    private final FluentWebElement element;

    public DrawerGroup(FluentWebElement element) {
        this.element = element;
    }

    public List<DrawerProject> projects() {
        List<DrawerProject> projects = new ArrayList<DrawerProject>();
        for (FluentWebElement e: (Iterable<FluentWebElement>) element.find("ul > li")) {
            projects.add(new DrawerProject(e));
        }
        return projects;
    }

    public DrawerProject project(String name) {
        for (DrawerProject p: projects()) {
            if (p.name().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Predicate hasProject(final String name) {
        return new Predicate() {
            public boolean apply(Object o) {
                return project(name) != null;
            }
        };
    }

    public void newProject() {
        element.findFirst(".newProject").click();
    }
}
```

<!--
Like with `Drawer`, we have a method for looking up a project by name.  We've also provided a method for checking if a project with a particular name exists, we've used `Predicate` to capture this, which will make it easy for us later when we tell FluentLenium to wait until certain conditions are true.
-->
`Drawer` のように、プロジェクトを名前で検索するメソッドがあります。また、特定の名前のプロジェクトが存在するか確認するメソッドも提供しました。後ほど、特定の条件が true になるまで待つように FluentLenium に告げる際、簡単にこれを捕捉する `Predicate` を使っています。

<!--
Finally, the last component of our model that we'll build out is `test/componenst/DrawerProject.java`:
-->
最後に作成するモデルのコンポーネントは `test/componenst/DrawerProject.java` です:

```java
package components;

import org.fluentlenium.core.*;
import org.fluentlenium.core.domain.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.openqa.selenium.Keys;
import com.google.common.base.Predicate;

public class DrawerProject {
    private final FluentWebElement element;

    public DrawerProject(FluentWebElement element) {
        this.element = element;
    }

    public String name() {
        FluentWebElement a = element.findFirst("a.name");
        if (a.isDisplayed()) {
            return a.getText().trim();
        } else {
            return element.findFirst("input").getValue().trim();
        }
    }

    public void rename(String newName) {
        element.findFirst(".name").doubleClick();
        element.findFirst("input").text(newName);
        element.click();
    }

    public Predicate isInEdit() {
        return new Predicate() {
            public boolean apply(Object o) {
                return element.findFirst("input") != null && element.findFirst("input").isDisplayed();
            }
        };
    }
```

<!--
The `DrawerProject` allows us to lookup the name of the project, rename the project, and has a predicate for checking if the project name is in edit mode.  So, it's been a bit of work to get this far with our selenium tests, and we haven't written any tests yet!  The great thing is though, all these components and pages are going to be reusable from all of our selenium tests, so when something about our markup changes, we can just update these components, and all the tests will still work.
-->
`DrawerProject` は、プロジェクトの名前を探したり、プロジェクトをリネームすることができますし、プロジェクト名が編集中であることを確認するプレディケートも用意されています。さて、ここまで作業するためにずいぶんと selenium テストから遠ざかってしまいましたし、まだテストを何も書いていません! すばらしいことに、これらのコンポーネントとページはすべての selenium テストから再利用することができるので、マークアップを何か変更しても、これらのコンポーネントを更新するだけで、すべてのテストが動き続けます。

<!--
### Implementing the tests
-->
### テストの実装

<!--
Open `test/views/DrawerTest.java`, and add the following setup code:
-->
`test/views/DrawerTest.java` を開いて、以下のセットアップコードを追加してください:

```java
package views;

import org.junit.*;

import play.test.*;
import static play.test.Helpers.*;

import org.fluentlenium.core.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import com.google.common.base.*;

import pages.*;
import components.*;

public class DrawerTest extends WithBrowser {

    public Drawer drawer;

    @Before
    public void setUp() {
        start();
        Login login = browser.createPage(Login.class);
        login.go();
        login.login("bob@example.com", "secret");
        drawer = browser.createPage(Dashboard.class).drawer();
    }
}
```

<!--
This time we've made our test case extend `WithBrowser`, which gives us a mock web browser to work with.  The default browser is HtmlUnit, a Java based headless browser, but you can also use other browmsers, such as Firefox and Chrome. In our `setUp` method we've called `start()`, which starts both the browser and the server.  We've then created a login page, navigated to it, and logged in, and finally, we've created a dashboard page and retrieved the drawer from it. We're now ready to write our first test case:
-->
今回は、テストケースと共に動作する web ブラウザのモックを提供する `WithBrowser` を継承してテストケースを作成しました。デフォルトのブラウザは Java ベースのヘッドレスブラウザである HtmlUnit ですが、Firefox や Chrome など、その他のブラウザを使うこともできます。`setUp` メソッドで、ブラウザとサーバの両方を起動する `start()` を呼び出しました。その後、ログインページを作成し、ログインページに遷移して、ログインし、そして最後にダッシュボードページを作成して、ここから drawer を検索しました。これで最初のテストケースを書く準備が整いました:

```java
    @Test
    public void newProject() throws Exception {
        drawer.group("Personal").newProject();
        dashboard.await().until(drawer.group("Personal").hasProject("New project"));
        dashboard.await().until(drawer.group("Personal").project("New project").isInEdit());
    }
```

<!--
Here we're testing new project creation.  We get the `Personal` group, using the methods we've already defined on drawer, and you can see we're using those predicates we wrote for testing if a group has a project, and if it's in edit mode (as it should be after we've created it).  Let's write another test, this time testing the rename functionality:
-->
ここではプロジェクトの新規作成をテストしています。drawer に定義済みのメソッドを使って `Personal` グループを取得します。グループがプロジェクトを持っていること、またプロジェクトが編集中であることを (プロジェクトを作成した後に) テストするために書いたプレディケートを使っているのが分かります。別のテストも書いてみましょう。今度はリネーム機能をテストします:

```java
    @Test
    public void renameProject() throws Exception {
        drawer.group("Personal").project("Private").rename("Confidential");
        dashboard.await().until(Predicates.not(drawer.group("Personal").hasProject("Private")));
        dashboard.await().until(drawer.group("Personal").hasProject("Confidential"));
        dashboard.await().until(Predicates.not(drawer.group("Personal").project("Confidential").isInEdit()));
    }
```

<!--
We rename a project, and then wait for it to disappear, wait for the new one to appear, and then ensure that the new one is not in edit mode.  In this tutorial we're going to leave the client side tests there, but you can try now to implement your own tests for deleting projects and adding new groups.
-->
プロジェクトをリネームして、これが消えるのを待ち、新しいプロジェクト名が表示されるのを待ち、そして新しいプロジェクト名が編集中でないことを確認しています。このチュートリアルでは、ここでクライアントサイドのテストから手を引きますが、あなたはここで、プロジェクトを削除し、新しいグループを作成するテストを実装してみることができます。

<!--
So now we have a working and tested navigation drawer.  We've seen how to implement a few more actions, how the Javascript router works, and to use CoffeeScript in our Play application, and how to use the Javascript router from our CoffeeScript code.  We've also seen how we can use the page object pattern to write tests for a client side code running in a headless browser.  There are a few functions we haven't implemented yet, these include renaming a group and deleting a group.  You could try implementing them on your own, or check the code in the ZenTasks sample app to see how it's done.
-->
ここまでで、ナビゲーションドローワーは動作し、テストされました。いくつかのアクションをどのように実装するか、Javascript ルーターがどのように動作するか、どのようにして CoffeeScript を Play アプリケーションで使用するか、そしてどのようにして CoffeeScript コードから Javascript ルーターを使用するかについて見てきました。また、ヘッドレスブラウザで実行されるクライアントサイドコードをテストするために、ページオブジェクトパターンを使う方法も見てきました。グループのリネームと、グループの削除を含むいくつかの機能はまだ実装していません。あなた自身でこれらを実装してみるか、どのように実装するのか ZenTasks サンプルアプリを確認することができます。

<!--
Commit your work to git.
-->
作業内容を git にコミットしてください。

<!--
> Go to the [next part](JavaGuide6)
-->
> [次章](JavaGuide7) に進みましょう