<!--
# The Play cache API
-->
Play キャッシュ API

<!--
The default implementation of the Cache API uses [[EHCache| http://ehcache.org/]]. You can also provide your own implementation via a plug-in.
-->
キャッシュ API のデフォルト実装は [[EHCache| http://ehcache.org/]] を使用します。プラグインとして自身の実装を提供することもできます。

<!--
## Accessing the Cache API
-->
## キャッシュ API へのアクセス

<!--
The cache API is provided by the `play.api.cache.Cache` object. It requires a registered cache plug-in.
-->
キャッシュ API は `play.api.cache.Cache` オブジェクトにより提供されています。このオブジェクトは登録されたキャッシュプラグインを必要とします。

<!--
> **Note:** The API is intentionally minimal to allow several implementation to be plugged. If you need a more specific API, use the one provided by your Cache plugin.
-->
> **注意:** この API は複数の実装をプラグインできるよう、意図的に最小限に止められています。より特殊な API が必要な場合、自身のキャッシュプラグインにより提供される API を使用してください。

<!--
Using this simple API you can either store data in cache:
-->
このシンプルな API を使ってキャッシュにデータ格納することができます:

```
Cache.set("item.key", connectedUser)
```

<!--
And then retrieve it later:
-->
そして、それを後から読み出すこともできます:

```
val maybeUser: Option[User] = Cache.getAs[User]("item.key")
```

<!--
There is also a convenient helper to retrieve from cache or set the value in cache if it was missing:
-->
キャッシュから値を読み出し、もしキャッシュの中に見当たらない場合はキャッシュに値をセットする便利なヘルパもあります:

```
val user: User = Cache.getOrElseAs[User]("item.key") {
  User.findById(connectedUser)
}
```

<!--
## Caching HTTP responses
-->
## HTTP レスポンスのキャッシュ

<!--
You can easily create smart cached actions using standard Action composition. 
-->
標準的なアクションの合成を使って、簡単にスマートなアクションのキャッシュを作ることができます。

<!--
> **Note:** Play HTTP `Result` instances are safe to cache and reuse later.
-->
> **注意:** Play の HTTP `Result` インスタンスは、安全にキャッシュし、あとから再利用することができます。

<!--
Play provides a default built-in helper for standard cases:
-->
Play は一般的なな状況のためにデフォルトの組み込みヘルパを提供しています:

```
def index = Cached("homePage") {
  Action {
    Ok("Hello world")
  }
}
```

こんなこともできます:

```
def userProfile = Authenticated { user =>
  Cached(req => "profile." + user) {      
    Action { 
      Ok(views.html.profile(User.find(user)))
    }   
  }
}
```

<!--
> **Next:** [[Calling web services | ScalaWS]]
-->
> **次:** [[web サービスの呼び出し | ScalaWS]]
