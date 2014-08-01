<!-- translated -->
<!--
# Writing Plugins
-->
# プラグインを書く

<!--
Play comes with a few plugins predefined for all applications, these plugins are the following: 
-->
Play には、以下に示すあらゆるアプリケーション向けのいくつかのプラグインがあらかじめ同梱されています:

<!--
* `DBPlugin` -> providing a JDBC datasource
* `EvolutionPlugin` -> provides migration  _(only available if db was configured)_
* `EbeanPlugin` -> provides Ebean support_ (only available if db was configured)_
* `MessagesPlugin` - > provides i18n support
* `BasicCachePlugin` -> provides in-memory caching
* `GlobalPlugin` -> executes application's settings
-->
* `DBPlugin` -> JDBC データソースを提供します
* `EvolutionPlugin` -> マイグレーションを提供します  _(データベースが設定されている場合のみ有効)_
* `EbeanPlugin` -> Ebean サポートを提供します _(データベースが設定されている場合のみ有効)_
* `MessagesPlugin` - > 国際化サポートを提供します
* `BasicCachePlugin` -> インメモリキャッシュを提供します
* `GlobalPlugin` -> アプリケーションの設定を行います

<!--
However, one can easily add a new plugin to an application by following these steps:
-->
一方で、以下の手順に従ってアプリケーションに新しいプラグインを簡単に追加することができます:

<!--
* implement `play.Plugin` (see [this](https://github.com/playframework/playframework/blob/master/framework/src/play-java-ebean/src/main/java/play/db/ebean/EbeanPlugin.java) for an example)
* this plugin should be available in the application either through pulling in it from a maven repository and referencing it
as an app dependency or the plugin code can be part of a play application
* you can access it like 
-->
* `play.Plugin` を実装します (例として [こちら](https://github.com/playframework/playframework/blob/master/framework/src/play-java-ebean/src/main/java/play/db/ebean/EbeanPlugin.java) を参照してください)
* maven リポジトリからダウンロードしてアプリケーションの依存性として参照するか、またはプラグインのコードを play アプリケーションの一部とすることで、このプラグインをアプリケーション内で利用することができます
* 以下のようにしてプラグインにアクセスすることができます

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

<!--
* in your app create a file: `conf/play.plugins` and add a reference to your plugin:
-->
* アプリケーション中に `conf/play.plugins` ファイルを作成し、プラグインへの参照を追加します:

    5000:com.example.MyPlugin

<!--
The number represents the plugin loading order, by setting it to > 10000 we can make sure it's loaded after the global plugins.
-->
この数字はプラグインの読み込み順序を表しており、10000 より大きい値を設定することで、グローバルプラグインより後に読み込まれることを保証することができます。