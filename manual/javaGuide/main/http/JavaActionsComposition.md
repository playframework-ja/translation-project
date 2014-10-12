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

Previously, we said that an action is a Java method that returns a `play.mvc.Result` value. Actually, Play manages internally actions as functions. Because Java doesn't yet support first class functions, an action provided by the Java API is an instance of [`play.mvc.Action`](api/java/play/mvc/Action.html):

```java
public abstract class Action {
  public abstract Promise<SimpleResult> call(Context ctx) throws Throwable;
}
```

<!--
Play builds a root action for you that just calls the proper action method. This allows for more complicated action composition.
-->
Play は、適切なアクションメソッドを呼び出すルートアクションを生成します。これにより、もっと込み入ったアクションの合成を行うことができます。

Notice that the `call` method returns `Promise<SimpleResult>`, this was introduced in [[version 2.2|Highlights22]] to improve handling different result type such as chunked, plain or async. 

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
You can then use your new annotation with an action method:
-->
この新しいアノテーションをアクションメソッドで利用するには、以下のようにします。

@[verbose-annotation-index](code/javaguide/http/JavaActionsComposition.java)

<!--
Your `Action` definition retrieves the annotation as configuration:
-->
`Action` 側ではアノテーションを `configuration` として受け取ることができます。

@[verbose-annotation-action](code/javaguide/http/JavaActionsComposition.java)

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
public Admin extends Controller {
    
  …
    
}
```

<!--
## Passing objects from action to controller
-->
## アクションからコントローラへオブジェクトを引き渡す

<!--
You can pass an object from an action to a controller by utilizing the context args map.
-->
コンテキスト引数マップを使ってアクションからコントローラへオブジェクトを引き渡すことができます。

@[security-action](code/javaguide/http/JavaActionsComposition.java)

<!--
> **Next:** [[Content negotiation | JavaContentNegotiation]]
-->
> **次ページ:** [[コンテントネゴシエーション | JavaContentNegotiation]]
