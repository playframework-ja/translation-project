<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# The Play cache API
-->
# Play キャッシュ API

Caching data is a typical optimization in modern applications, and so Play provides a global cache.

> An important point about the cache is that it behaves just like a cache should: the data you just stored may just go missing.

For any data stored in the cache, a regeneration strategy needs to be put in place in case the data goes missing. This philosophy is one of the fundamentals behind Play, and is different from Java EE, where the session is expected to retain values throughout its lifetime. 

<!--
The default implementation of the Cache API uses [EHCache](http://ehcache.org/).
-->
キャッシュ API のデフォルト実装は [EHCache](http://ehcache.org/) です。

<!--
## Importing the Cache API
-->
## キャッシュ API のインポート

<!--
Add `cache` into your dependencies list. For example, in `build.sbt`:
-->
依存ライブラリの一覧に `cache` を追加してください。 `build.sbt` の例です。

```scala
libraryDependencies ++= Seq(
  cache,
  ...
)
```

<!--
## Accessing the Cache API
-->
## キャッシュ API へのアクセス

<!--
The cache API is provided by the [CacheApi](api/scala/play/api/cache/CacheApi.html) object, and can be injected into your component like any other dependency.  For example:
-->
キャッシュ API は [CacheApi](api/scala/play/api/cache/CacheApi.html) オブジェクトとして提供されています。

@[inject](code/ScalaCache.scala)

<!--
> **Note:** The API is intentionally minimal to allow several implementation to be plugged in. If you need a more specific API, use the one provided by your Cache plugin.
-->
> **Note:** 様々な実装をプラグインできるように、キャッシュ API の機能は意図的に最小限に絞りこまれています。より特殊な API が必要な場合、独自のキャッシュプラグインにその API を持たせるとよいでしょう。

Using this simple API you can either store data in cache:

@[set-value](code/ScalaCache.scala)

<!--
And then retrieve it later:
-->
そして、保存したデータを後で取得するためには、次のようなコードを記述します。

@[get-value](code/ScalaCache.scala)

<!--
There is also a convenient helper to retrieve from cache or set the value in cache if it was missing:
-->
値がキャッシュに保存されていればそれを取得し、そうでなければ保存するという機能を持つ便利なヘルパ関数もあります。

@[retrieve-missing](code/ScalaCache.scala)

You can specify an expiry duration by passing a duration, by default the duration is infinite:

@[set-value-expiration](code/ScalaCache.scala)

<!--
To remove an item from the cache use the `remove` method:
-->
`remove` メソッドでデータをキャッシュから削除することができます。

@[remove-value](code/ScalaCache.scala)

## Accessing different caches

It is possible to access different caches.  The default cache is called `play`, and can be configured by creating a file called `ehcache.xml`.  Additional caches may be configured with different configurations, or even implementations.

If you want to access multiple different ehcache caches, then you'll need to tell Play to bind them in `application.conf`, like so:

    play.cache.bindCaches = ["db-cache", "user-cache", "session-cache"]

Now to access these different caches, when you inject them, use the [NamedCache](api/java/play/cache/NamedCache.html) qualifier on your dependency, for example:

@[qualified](code/ScalaCache.scala)

<!--
## Caching HTTP responses
-->
## HTTP レスポンスのキャッシュ

<!--
You can easily create smart cached actions using standard Action composition. 
-->
標準的なアクション合成の方法を使って、簡単にスマートなキャッシュ機能を備えたアクションを実装できます。

<!--
> **Note:** Play HTTP `Result` instances are safe to cache and reuse later.
-->
> **Note:** Play HTTP の `Result` インスタンスはキャッシュして後で再利用しても安全です。

The [Cached](api/scala/play/api/cache/Cached.html) class helps you build cached actions.

@[cached-action-app](code/ScalaCache.scala)

You can cache the result of an action using a fixed key like `"homePage"`.

@[cached-action](code/ScalaCache.scala)

If results vary, you can cache each result using a different key. In this example, each user has a different cached result.

@[composition-cached-action](code/ScalaCache.scala)

<!--
### Control caching
-->
### キャッシュ制御

<!--
You can easily control what you want to cache or what you want to exclude from the cache.
-->
キャッシュしたり、キャッシュから除外することを簡単に制御できます。

<!--
You may want to only cache 200 Ok results.
-->
200 Ok の結果だけをキャッシュすることもできます。

@[cached-action-control](code/ScalaCache.scala)

<!--
Or cache 404 Not Found only for a couple of minutes
-->
また数分間だけ 404 Not Found をキャッシュすることもできます。

@[cached-action-control-404](code/ScalaCache.scala)

## Custom implementations

It is possible to provide a custom implementation of the [CacheApi](api/scala/play/api/cache/CacheApi.html) that either replaces, or sits along side the default implementation.

To replace the default implementation, you'll need to disable the default implementation by setting the following in `application.conf`:

```
play.modules.disabled += "play.api.cache.EhCacheModule"
```

Then simply implement `CacheApi` and bind it in the [[DI container|ScalaDependencyInjection]].

To provide an implementation of the cache API in addition to the default implementation, you can either create a custom qualifier, or reuse the `NamedCache` qualifier to bind the implementation.
