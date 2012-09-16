<!--translated-->
<!--
# The Play cache API
-->
# キャッシュAPI

<!--
Caching data is a typical optimization in modern applications, and so Play provides a global cache. An important point about the cache is that it behaves just like a cache should: the data you just stored may just go missing.
-->
データのキャッシュは現代的なアプリケーションではよく使われるパフォーマンスの最適化手法です。Playは、アプリケーション全体で利用できるグローバルなキャッシュを提供しています。キャッシュの重要な点は、通常のキャッシュとして期待されている通りに振る舞うということです。つまり、たった今保存したデータが、いつの間にか消えているということがありえます。

<!--
For any data stored in the cache, a regeneration strategy needs to be put in place in case the data goes missing. This philosophy is one of the fundamentals behind Play, and is different from Java EE, where the session is expected to retain values throughout its lifetime. 
-->
キャッシュに保存されているデータは、いつでも消去される可能性があります。その時に備えて、データを再生成する方法は常に用意しておく必要があります。この哲学はPlayの基盤の一つであり、セッションがその有効期間の間ずっと値を保持するというJava EEの哲学とは異なります。

<!--
The default implementation of the cache API uses [[EHCache| http://ehcache.org/]]. You can also provide your own implementation via a plugin.
-->
キャッシュAPIのデフォルト実装は[[EHCache| http://ehcache.org/]]を利用しています。キャッシュAPIの別の実装をプラグインとして提供することもできます。

<!--
## Accessing the Cache API
-->
## キャッシュAPIにアクセスする

<!--
The cache API is provided by the `play.cache.Cache` object. This requires a cache plugin to be registered.
-->
キャッシュAPIは`play.cache.Cache`オブジェクトを介して利用できます。このオブジェクトを利用するためには、何らかのキャッシュプラグインが登録されている必要があります。

<!--
> **Note:** The API is intentionally minimal to allow various implementations to be plugged in. If you need a more specific API, use the one provided by your Cache plugin.
-->
> **ノート:** あらゆるキャッシュAPIの実装をプラグイン可能にするために、APIは意図的に最小限に設計されています。もし、キャッシュAPIにない特別なAPIが必要な場合は、利用しているキャッシュプラグインが提供している独自APIを利用してください。

<!--
Using this simple API you can store data in the cache:
-->
次のシンプルなAPIを呼び出すことで、データをキャシュに保存することができます。

```
Cache.set("item.key", frontPageNews);
```

<!--
You can retrieve the data later:
-->
このデータを取得するためには次のようにします。

```
News news = Cache.get("item.key");
```

<!--
How to remove the key is as follows.
-->
キーを削除するためには次のようにします。

```
// 2.0 final
Cache.set("item.key", null, 0)
// later
Cache.remove("item.key")

```

<!--
## Caching HTTP responses
-->
## HTTPレスポンスのキャッシュ

<!--
You can easily create a smart cached action using standard `Action` composition. 

> **Note:** Play HTTP `Result` instances are safe to cache and reuse later.

Play provides a default built-in helper for the standard case:
-->
標準的な、`Action`の合成機能を利用することで、レスポンスをキャッシュするアクションを簡単に作成することができます。

> **ノート:** PlayのHTTPレスポンスを表す`Result`のインスタンスは、安全にキャッシュしたり後で再利用することができます。

Playは標準的に利用できるデフォルトの組み込みヘルパを提供しています。


```
@Cached("homePage")
public static Result index() {
  return ok("Hello world");
}
```

<!--
## Caching in templates  
-->
## テンプレートにおけるキャッシュ

<!--
You may also access the cache from a view template.
-->
ビューテンプレートからキャッシュにアクセスすることもできます。

```
@cache.Cache.getOrElse("cached-content", 3600) {
     <div>I’m cached for an hour</div>
}
```

<!--
## Session cache
-->
## セッションキャッシュ

<!--
Play provides a global cache, whose data are visible to anybody. How would one restrict visibility to a given user? For instance you may want to cache metrics that only apply to a given user.
-->
Playはアプリケーション内であればどこからでもデータを参照することができるグローバルなキャッシュを提供しています。データを特定のユーザのみ参照できるように制限をかけるにはどうしたらいいでしょうか？例えば、特定のユーザにのみ適用される数値指標をキャッシュしたい場合は次のようにします。

```
// Generate a unique ID
String uuid=session("uuid");
if(uuid==null) {
	uuid=java.util.UUID.randomUUID().toString();
	session("uuid", uuid);
}

// Access the cache
News userNews = Cache.get(uuid+"item.key");
if(userNews==null) {
	userNews = generateNews(uuid);
	Cache.set(uuid+"item.key", userNews );
}
```

<!--
> **Next:** [[Calling web services | JavaWS]]
-->
> **次ページ:** [[Webサービスの呼び出し | JavaWS]]
