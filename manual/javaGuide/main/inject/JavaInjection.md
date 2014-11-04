<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
By default Play binds URLs to controller methods _statically_, that is, Controller instances are created by the framework and then the appropriate static method is invoked depending on the given URL. In certain situations, however, you may want to manage controller creation and that's when the new routing syntax comes handy.
-->
デフォルトでは Play は URL をコントローラメソッドに_静的に_バインドします — つまり、コントローラインスタンスはフレームワークによって生成され、適切なスタティックメソッドが指定の URL に従って呼び出されます。しかし場合によってはコントローラの作成を管理したくなるでしょう。そしてその時に新しいルーティングの構文が便利になります。

<!--
Route definitions starting with ```@``` will be managed by ```play.GlobalSettings#getControllerInstance``` method, so given the following route definition:
-->
 ```@``` から始まるルーティング定義は ```play.GlobalSettings#getControllerInstance``` メソッドで管理され、以下のようなルーティング定義が与えられた場合、

    GET     /                  @controllers.Application.index()

<!--
Play will invoke ```play.GlobalSettings#getControllerInstance``` which in return will provide an instance of ```controllers.Application``` (by default this is happening via ```controllers.Application```'s default constructor). Therefore, if you want to manage controller class instantiation either via a dependency injection framework or manually you can do so by overriding ```getControllerInstance``` in your application's ```Global``` class.
-->
Play は ```play.GlobalSettings#getControllerInstance``` を呼び出し ```controllers.Application``` のインスタンスを提供します(デフォルトでは ```controllers.Application``` のデフォルトコンストラクタを呼び出します)。従って、もしあなたがコントローラクラスの生成を DI フレームワークもしくは手動で管理したい場合、アプリケーションの ```Global``` クラスの ```getControllerInstance``` をオーバーライドする事で実現できます。

<!--
Here's an example using Guice:
-->
Guice を使った例です:

```java
    import play.GlobalSettings;

    import com.google.inject.Guice;
    import com.google.inject.Injector;

    public class Global extends GlobalSettings {

      private static final Injector INJECTOR = createInjector();

      @Override
      public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        return INJECTOR.getInstance(controllerClass);
      }

      private static Injector createInjector() {
        return Guice.createInjector();
      }

    }
```

<!--
another example using Spring:
-->
Spring を使った別の例です:

https://github.com/guillaumebort/play20-spring-demo
