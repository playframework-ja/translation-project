<!--
# Writing Plugins
-->
# プラグインを書く
<!--
Play comes with a few plugins predefined for all applications, these plugins are the following:
-->

Play フレームワークには、すべてのアプリケーションに対して、以下のようにあらかじめ定義されたプラグインがあります。

<!--
* `DBPlugin` -> providing a JDBC datasource
* `EvolutionPlugin` -> provides migration  _(only available if db was configured)_
* `EbeanPlugin` -> provides Ebean support _(only available if db was configured)_
* `MessagesPlugin` - > provides i18n support
* `BasicCachePlugin` -> provides in-memory caching
* `GlobalPlugin` -> executes application's settings
-->

* `DBPlugin` -> JDBC データソースを提供します。
* `EvolutionPlugin` -> マイグレーション機能を提供します。 _(データベースが設定されている場合のみ)_
* `EbeanPlugin` -> Ebean をサポートします。 _(データベースが設定されている場合のみ)_
* `MessagesPlugin` - > i18n サポートします。
* `BasicCachePlugin` -> インメモリキャッシュを提供します。
* `GlobalPlugin` -> アプリケーションの設定を実行します。

<!--
However, one can easily add a new plugin to an application.
-->

ですが、アプリケーションに新しいプラグインを追加するのも簡単です。
<!--
1. first step is to implement play.api.Plugin trait which has three methods: onStart, onStop and enabled - [for example](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala))
2. this plugin should be available in the application either through pulling in it from a maven repository and referencing it
as an app dependency or the plugin code can be part of a play application
3. you can use it directly like `app.plugin[MyPlugin].map(_.api.mymethod).getOrElse(throwMyerror)` (where `app` is  a reference to the current application which can be obtain by importing play.api.Play.current) however, it's recommended to wrap it for convenience (for example, see [this](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala))
4. in your app create a file: `conf/play.plugins` and add a reference to your plugin:

    5000:com.example.MyPlugin

The number represents the plugin loading order, by setting it to > 10000 we can make sure it's loaded after the global plugins.
-->

1. はじめに、onStart, onStop and enabled の三つのメソッドを持つ play.api.Plugin トレイトを実装します。- [参考](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala))
2. このプラグインはMavenリポジトリから引き込まれるようにするか、アプリケーションの依存性として参照するか、あるいはPlayアプリケーションの一部でなければなりません。
3. プラグインは次のように直接使うことができます。`app.plugin[MyPlugin].map(_.api.mymethod).getOrElse(throwMyerror)`  ( `app`の場所はplay.api.Play.current をインポートすることで得られる 現在のアプリケーションから参照できるものになります) しかしながら、利便性のためにラップして使用することを奨めます。 (参考 [こちら](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala))
4. アプリケーション内に `conf/play.plugins` というファイルを作成し、作ったプラグインを次のように付け加えてください:

	5000:com.example.MyPlugin

冒頭の数字はプラグインは呼び出される順番を表しており、10000 以上に設定することでグローバルプラグインの後に確実に呼ばれるようになります。 

<!--
_Tip: If you are a scala developer but you want to share your plugin with java developers, you will need make sure your API is wrapped for Java users (see [this](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala) and [this](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/java/play/cache/Cache.java) for an example)_
-->

_Tip: もしあなたがscala開発者で自分のプラグインをJava開発者を共有したくない場合はJavaユーザーのためにラップされたAPIが必要になります。(参考 [this](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala) and [this](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/java/play/cache/Cache.java) for an example)_

