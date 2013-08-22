<!-- translated -->
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
Previously, we said that an action is a Java method that returns a `play.mvc.Result` value. Actually, Play manages internally actions as functions. Because Java doesn't support first class functions, an action provided by the Java API is an instance of `play.mvc.Action`:
-->
前のページで、アクションとは `play.mvc.Result` 値を返す Java のメソッドだということを説明しました。より正確には、Play はアクションを内部的には関数として管理しています。しかし、Java は第一級オブジェクトとしての関数をサポートしていないため、Java API では `play.mvc.Action` のインスタンスでアクションを定義します。

```
public abstract class Action {
    
  public abstract Result call(Http.Context ctx);    
    
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
You can compose the code provided by the action method with another `play.mvc.Action`, using the `@With` annotation:
-->
`@With` アノテーションを利用すると、あるアクションメソッドのコードを他の `play.mvc.Action` と組み合わせることができます。

```
@With(VerboseAction.class)
public static Result index() {
  return ok("It works!");
}
```

<!--
Here is the definition of the `VerboseAction`:
-->
`VerboseAction` の定義は以下の通りです。

```
public class VerboseAction extends Action.Simple {

  public Result call(Http.Context ctx) throws Throwable {
    Logger.info("Calling action for " + ctx);
    return delegate.call(ctx);
  }
}
```

<!--
At one point you need to delegate to the wrapped action using `delegate.call(...)`.
-->
どこかのタイミングで `delegate.call(...)` を呼び出して、ラップされているアクションに処理を委譲する必要があることに注意してください。

<!--
You also mix with several actions:
-->
複数のアクションを合成することもできます:

```
@With(Authenticated.class, Cached.class)
public static Result index() {
  return ok("It works!");
}
```

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

```
@With(VerboseAction.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Verbose {
   boolean value() default true;
}
```

<!--
You can then use your new annotation with an action method:
-->
この新しいアノテーションをアクションメソッドで利用するには、以下のようにします。

```
@Verbose(false)
public static Result index() {
  return ok("It works!");
}
```

<!--
Your `Action` definition retrieves the annotation as configuration:
-->
`Action` 側ではアノテーションを `configuration` として受け取ることができます。

```
public class VerboseAction extends Action<Verbose> {

  public Result call(Http.Context ctx) {
    if(configuration.value) {
      Logger.info("Calling action for " + ctx);  
    }
    return delegate.call(ctx);
  }
}
```

<!--
## Annotating controllers
-->
## コントローラにアノテーションを付与する

<!--
You can also put any action composition annotation directly on the `Controller` class. In this case it will be applied to all action methods defined by this controller.
-->
アクションを合成するアノテーションは何でも `Controller` クラスに直接付与することができます。その場合、アノテーションはコントローラ内に定義された全てのアクションメソッドに適用されます。

```
@Authenticated
public Admin extends Controller {
    
  …
    
}
```

<!--
> **Next:** [[Asynchronous HTTP programming | JavaAsync]]
-->
> **次ページ:** [[非同期 HTTP プログラミング | JavaAsync]]