<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# The Play cache API
-->
# Play キャッシュ API

<!--
The default implementation of the Cache API uses [EHCache](http://ehcache.org/). You can also provide your own implementation via a plug-in.
-->
キャッシュ API のデフォルト実装は [EHCache](http://ehcache.org/) です。その他の実装はプラグイン経由で利用することができます。

## Importing the Cache API

Add `cache` into your dependencies list. For example, in `build.sbt`:

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
The cache API is provided by the `play.api.cache.Cache` object. It requires a registered cache plug-in.
-->
キャッシュ API は `play.api.cache.Cache` オブジェクトとして提供されています。このオブジェクトを利用するためには、キャッシュプラグインが既に登録されている必要があります。

<!--
> **Note:** The API is intentionally minimal to allow several implementation to be plugged. If you need a more specific API, use the one provided by your Cache plugin.

Using this simple API you can either store data in cache:
-->
> **Note:** 様々な実装をプラグインできるように、キャッシュ API の機能は意図的に最小限に絞りこまれています。より特殊な API が必要な場合、独自のキャッシュプラグインにその API を持たせるとよいでしょう。

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


<!--
To remove an item from the cache use the `remove` method:
-->
`remove` メソッドでデータをキャッシュから削除することができます。

@[remove-value](code/ScalaCache.scala)


<!--
## Caching HTTP responses
-->
## HTTP レスポンスのキャッシュ

<!--
You can easily create smart cached actions using standard Action composition. 

> **Note:** Play HTTP `Result` instances are safe to cache and reuse later.

Play provides a default built-in helper for standard cases:
-->
標準的なアクション合成の方法を使って、簡単にスマートなキャッシュ機能を備えたアクションを実装できます。

@[cached-action](code/ScalaCache.scala)


Or even:

@[composition-cached-action](code/ScalaCache.scala)

### Control caching

You can easily control what you want to cache or what you want to exclude from the cache.

You may want to only cache 200 Ok results.

@[cached-action-control](code/ScalaCache.scala)

Or cache 404 Not Found only for a couple of minutes

@[cached-action-control-404](code/ScalaCache.scala)

<!--
> **Next:** [[Calling web services | ScalaWS]]
-->
> **次ページ:** [[Web サービスの呼び出し | ScalaWS]]
