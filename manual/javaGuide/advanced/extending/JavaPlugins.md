<!--
# Writing Plugins
-->
# プラグインを書く

<!--
Play comes with a few plugins predefined for all applications, these plugins are the following: 
-->
Play には、以下に示すあらゆるアプリケーション向けのいくつかのプラグインがあらかじめ同梱されています:

* `DBPlugin` -> providing a JDBC datasource
* `EvolutionPlugin` -> provides migration  _(only available if db was configured)_
* `EbeanPlugin` -> provides Ebean support _(only available if db was configured)_
* `MessagesPlugin` - > provides i18n support
* `BasicCachePlugin` -> provides in-memory caching
* `GlobalPlugin` -> executes application's settings

<!--
However, one can easily add a new plugin to an application by following these steps:
-->
一方で、以下の手順に従ってアプリケーションに新しいプラグインを簡単に追加することができます:

* Implement `play.Plugin` (see [this](https://github.com/playframework/playframework/blob/master/framework/src/play-java-ebean/src/main/java/play/db/ebean/EbeanPlugin.java) for an example).
* This plugin should be available in the application either through pulling in it from a maven repository and referencing it
as an app dependency or the plugin code can be part of a play application.
* You can access it like:

```java
import static play.api.Play.*;
import static play.libs.Scala.*;

public Myplugin plugin() {
   return orNull(unsafeApplication().plugin(MyPlugin.class)).api();
}
``` 

<!--
which will return an instance or subclass of `MyPlugin` fully initialized or `null`.
-->
このコードは、`MyPlugin` またはサブクラスの初期化されたインスタンスか、`null` を返します。

* In your app create a file: `conf/play.plugins` and add a reference to your plugin:

```
5000:com.example.MyPlugin
```

The number represents the plugin loading order.  By setting it to > 10000 we can make sure it's loaded after the global plugins.
