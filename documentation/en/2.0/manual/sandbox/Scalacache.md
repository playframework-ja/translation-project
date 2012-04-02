# Caching

An in-memory cache with expiry support is provided by Play.

> **Note:** By default, this implementation is also used for Play’s internal caching.

You can find usage instructions in the [play.api.cache.Cache API documentation](https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/cache/Cache.scala).

# Plugging in your own

To implement a different caching solution, use the following steps.

1. In ```application.conf```, set ```ehcacheplugin=disabled```.
2. Implement the ```play.api.CacheAPI``` interface.
3. Implement ```play.api.Plugin```.

> **Next:** [[Calling WebServices | ScalaWS]]