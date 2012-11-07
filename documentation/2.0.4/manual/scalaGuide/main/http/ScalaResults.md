<!-- translated -->
<!--
# Manipulating Results
-->
# レスポンスの変更

<!--
## Changing the default Content-Type
-->
## デフォルトの Content-Type の変更

<!--
The result content type is automatically inferred from the Scala value that you specify as the response body.
-->
レスポンスのコンテンツタイプは、レスポンスボディとして指定された Scala の値から自動的に推論されます。

<!--
For example:
-->
例えば、

```scala
val textResult = Ok("Hello World!")
```

<!--
Will automatically set the `Content-Type` header to `text/plain`, while:
-->
このように記述した場合は、`Content-Type` ヘッダに `text/plain` がセットされます。また、

```scala
val xmlResult = Ok(<message>Hello World!</message>)
```

<!--
will set the Content-Type header to `text/xml`.
-->
このように記述した場合は、`Content-Type` ヘッダに `text/xml` がセットされます。

<!--
> **Tip:** this is done via the `play.api.http.ContentTypeOf` type class.
-->
> **Tip:** この `Content-Type` の推論は、`play.api.http.ContentTypeOf` という型クラスによって実現されています。

<!--
This is pretty useful, but sometimes you want to change it. Just use the `as(newContentType)` method on a result to create a new similar result with a different `Content-Type` header:
-->
これはかなり便利な機能ですが、`Content-Type` を任意に変更したいこともあるでしょう。そんな時は、Result の `as(newContentType)` というメソッドを呼び出して、`Content-Type` ヘッダを変更した新たなレスポンスを生成しましょう。

```scala
val htmlResult = Ok(<h1>Hello World!</h1>).as("text/html")
```

<!--
or even better, using:
-->
次のように、もう少し良い書き方もできます。

```scala
val htmlResult = Ok(<h1>Hello World!</h1>).as(HTML)
```

<!--
> **Note:** The benefit of using `HTML` instead of the `"text/html"` is that the charset will be automatically handled for you and the actual Content-Type header will be set to `text/html; charset=utf-8`. We will see that in a bit.
-->
> **Note:** `"text/html"` の代わりに `HTML` を利用するメリットは、フレームワークが charset を自動的に決定してくれるため、実際の `Content-Type` には `text/html; charset=utf-8` がセットされるということです。この機能については、すぐ後で説明します。

<!--
## Manipulating HTTP headers
-->
## HTTP ヘッダの変更

<!--
You can also add (or update) any HTTP header to the result:
-->
任意の HTTP ヘッダを追加または更新することもできます。

```scala
Ok("Hello World!").withHeaders(
  CACHE_CONTROL -> "max-age=3600", 
  ETAG -> "xx"
)
```

<!--
Note that setting an HTTP header will automatically discard the previous value if it was existing in the original result.
-->
既に値が存在する HTTP ヘッダに何か値をセットすると、上書きにより以前の値は自動的に破棄されてしまうことを覚えておいてください。

<!--
## Setting and discarding cookies
-->
## cookie の設定と破棄

<!--
Cookies are just a special form of HTTP headers but we provide a set of helpers to make it easier.
-->
Cookie は HTTP ヘッダの特殊形でしかありませんが、扱いを楽にするためいくつかのヘルパーが用意されています。

<!--
You can easily add a Cookie to the HTTP response using:
-->
次のように、HTTP レスポンスへ Cookie を簡単に追加することができます。

```scala
Ok("Hello world").withCookies(
  Cookie("theme", "blue")
)
```

<!--
Also, to discard a Cookie previously stored on the Web browser:
-->
また、既に Web ブラウザに保存されている Cookie を破棄させるには、次のように書きます。

```scala
Ok("Hello world").discardingCookies("theme")
```

<!--
## Changing the charset for text based HTTP responses.
-->
## テキストベースの HTTP レスポンスの charset を変更する

<!--
For text based HTTP response it is very important to handle the charset correctly. Play handles that for you and uses `utf-8` by default.
-->
テキストベースの HTTP レスポンスについては、charset を適切に処理することがとても重要です。Play は `utf-8` をデフォルトの charset として処理を行います。

<!--
The charset is used to both convert the text response to the corresponding bytes to send over the network socket, and to alterate the `Content-Type` header with the proper `;charset=xxx` extension.
-->
charset はテキストベースのレスポンスをバイトデータに変換してネットワークソケット経由で送信できるようにしたり、`Content-Type` ヘッダに適切な `;charset=xxx` を付与するために利用されます。

<!--
The charset is handled automatically via the `play.api.mvc.Codec` type class. Just import an implicit instance of `play.api.mvc.Codec` in the current scope to change the charset that will be used by all operations:
-->
charset は `paly.api.mvc.Codec` という型クラスにより自動的に決定されます。全操作で利用される charset を変更するためには、`play.api.mvc.Codec` のインスタンスを implicit val として現在のスコープ内に定義してください。

```scala
object Application extends Controller {
    
  implicit val myCustomCharset = Codec.javaSupported("iso-8859-1")
    
  def index = Action {
    Ok(<h1>Hello World!</h1>).as(HTML)
  }
    
}
```

<!--
Here, because there is an implicit charset value in the scope, it will be used by both the `Ok(...)` method to convert the XML message into `ISO-8859-1` encoded bytes and to generate the `text/html; charset=iso-8859-1` Content-Type header.
-->
上記の例では、implicit な charset 値がスコープ内に定義されているため、`Ok(...)` メソッドにおいて XML メッセージを `ISO-8859-1` エンコードされたバイトデータへ変換すること、また `text/html; charset=iso-8859-1` という Content-Type ヘッダを生成することの二つの目的で利用されます。

<!--
Now if you are wondering how the `HTML` method works, here it is how it is defined:
-->
ここで、`HTML` メソッドが一体何をしているのか疑問に思った方のために、定義をご紹介しておきます。

```scala
def HTML(implicit codec: Codec) = {
  "text/html; charset=" + codec.charset
}
```

<!--
You can do the same in your API if you need to handle the charset in a generic way.
-->
もし charset を直接扱う必要がある場合は、これと同じようなコードをご自身で記述すれば OK です。

<!--
> **Next:** [[Session and Flash scopes | ScalaSessionFlash]]
-->
> **次ページ:** [[セッションと Flash スコープ | ScalaSessionFlash]]