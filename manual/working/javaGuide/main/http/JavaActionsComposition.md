<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Action composition
-->
# アクションの合成

<!--
This chapter introduces several ways to define generic action functionality.
-->
この章では汎用的なアクションを定義するための方法をいくつか紹介します。

<!--
## Reminder about actions
-->
## アクションのおさらい

<!--
Previously, we said that an action is a Java method that returns a `play.mvc.Result` value. Actually, Play manages internally actions as functions. Because Java doesn't yet support first class functions, an action provided by the Java API is an instance of [`play.mvc.Action`](api/java/play/mvc/Action.html):
-->
前のページで、アクションとは `play.mvc.Result` 値を返す Java のメソッドだということを説明しました。より正確には、Play はアクションを内部的には関数として管理しています。しかし、Java は第一級オブジェクトとしての関数をサポートしていないため、Java API では [`play.mvc.Action`](api/java/play/mvc/Action.html) のインスタンスでアクションを定義します :

```java
public abstract class Action {
  public abstract Promise<Result> call(Context ctx) throws Throwable;
}
```

<!--
Play builds a root action for you that just calls the proper action method. This allows for more complicated action composition.
-->
Play は、適切なアクションメソッドを呼び出すルートアクションを生成します。これにより、もっと込み入ったアクションの合成を行うことができます。

<!--
## Composing actions
-->
## アクションを組み合わせる

<!--
Here is the definition of the `VerboseAction`:
-->
`VerboseAction` の定義は以下の通りです。

@[verbose-action](code/javaguide/http/JavaActionsComposition.java)

<!--
You can compose the code provided by the action method with another `play.mvc.Action`, using the `@With` annotation:
-->
`@With` アノテーションを利用すると、あるアクションメソッドのコードを他の `play.mvc.Action` と組み合わせることができます。

@[verbose-index](code/javaguide/http/JavaActionsComposition.java)

<!--
At one point you need to delegate to the wrapped action using `delegate.call(...)`.
-->
どこかのタイミングで `delegate.call(...)` を呼び出して、ラップされているアクションに処理を委譲する必要があることに注意してください。

<!--
You also mix several actions by using custom action annotations:
-->
カスタムアクションアノテーションを使って複数のアクションを合成することもできます:

@[authenticated-cached-index](code/javaguide/http/JavaActionsComposition.java)

<!--
> **Note:**  ```play.mvc.Security.Authenticated``` and ```play.cache.Cached``` annotations and the corresponding predefined Actions are shipped with Play. See the relevant API documentation for more information.
-->
> **ノート:** ```play.mvc.Security.Authenticated``` と ```play.cache.Cached``` アノテーションやそれに対応するアクションは Play に同梱されています。詳しくは、関連する API ドキュメントを参照してください。

> **Note:**  Every request must be served by a distinct instance of your `play.mvc.Action`. If a singleton pattern is used, requests will be routed incorrectly during multiple request scenarios. For example, if you are using Spring as a DI container for Play, you need to make sure that your action beans are prototype scoped.

<!--
## Defining custom action annotations
-->
## 独自のアクションアノテーションを定義する

<!--
You can also mark action composition with your own annotation, which must itself be annotated using `@With`:
-->
アクションの合成を独自のアノテーションで行えるようにマークすることもできます。そのためには、アノテーション自身に `@With` アノテーションを付与してください。

@[verbose-annotation](code/javaguide/http/JavaActionsComposition.java)

<!--
Your `Action` definition retrieves the annotation as configuration:
-->
`Action` 側ではアノテーションを `configuration` として受け取ることができます。

@[verbose-annotation-action](code/javaguide/http/JavaActionsComposition.java)

<!--
You can then use your new annotation with an action method:
-->
この新しいアノテーションをアクションメソッドで利用するには、以下のようにします。

@[verbose-annotation-index](code/javaguide/http/JavaActionsComposition.java)

<!--
## Annotating controllers
-->
## コントローラにアノテーションを付与する

<!--
You can also put any action composition annotation directly on the `Controller` class. In this case it will be applied to all action methods defined by this controller.
-->
アクションを合成するアノテーションは何でも `Controller` クラスに直接付与することができます。その場合、アノテーションはコントローラ内に定義された全てのアクションメソッドに適用されます。

```java
@Authenticated
public class Admin extends Controller {
    
  …
    
}
```

> **Note:** If you want the action composition annotation(s) put on a ```Controller``` class to be executed before the one(s) put on action methods set ```play.http.actionComposition.controllerAnnotationsFirst = true``` in ```application.conf```. However, be aware that if you use a third party module in your project it may rely on a certain execution order of its annotations.

<!--
## Passing objects from action to controller
-->
## アクションからコントローラへオブジェクトを引き渡す

<!--
You can pass an object from an action to a controller by utilizing the context args map.
-->
コンテキスト引数マップを使ってアクションからコントローラへオブジェクトを引き渡すことができます。

@[pass-arg-action](code/javaguide/http/JavaActionsComposition.java)

Then in an action you can get the arg like this:

@[pass-arg-action-index](code/javaguide/http/JavaActionsComposition.java)
