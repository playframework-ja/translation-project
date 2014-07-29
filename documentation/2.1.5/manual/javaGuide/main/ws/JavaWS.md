<!-- translated -->
<!--
# The Play WS API
-->
# Play WS API

<!--
Sometimes you want to call other HTTP services from within a Play application. Play supports this via its `play.libs.WS` library, which provides a way to make asynchronous HTTP calls.
-->
ときどき、Play アプリケーションから他の HTTP サービスを呼び出したくなることがあります。そんなときは、 Play が提供している、非同期　HTTP 呼び出しを行うためのライブラリ `play.libs.WS` を使いましょう。

<!--
A call made by `play.libs.WS` should return a `Promise<Ws.Response>`, which you can handle later with Play’s asynchronous mechanisms.
-->
`play.libs.WS` による HTTP 呼び出しは、 `Promise<Ws.Response>` を返します。これは、後に Play の非同期メカニズムにより処理されます。

<!--
## Making HTTP calls
-->
## HTTP 呼び出しを作成する

<!--
To make an HTTP request, you start with `WS.url()` to specify the URL. Then you get a builder that you can use to specify HTTP options, such as setting headers. You end by calling a method corresponding to the HTTP method you want to use:
-->
HTTP リクエストを作成するためには、まず `WS.url()` で URL を指定します。その結果、例えばヘッダをセットする、といったような 各種 HTTP オプションを指定するためのビルダが返ってきます。オプションの指定が終わったら、最後に利用したい HTTP メソッドに対応するメソッドを呼び出します。例を挙げると、

```
Promise<WS.Response> homePage = WS.url("http://mysite.com").get();
```

<!--
Alternatively:
-->
または、以下のように記述します。

```
Promise<WS.Response> result = WS.url("http://localhost:9001").post("content");
```

<!--
## Retrieving the HTTP response result
-->
## HTTP レスポンスを取得する

<!--
The call is made asynchronously and you need to manipulate it as a `Promise<WS.Response>` to get the actual content. You can compose several promises and end up with a `Promise<Result>` that can be handled directly by the Play server:
-->
HTTP 呼び出しは非同期で行われ、実際のコンテンツを取得するためには `Promise<WS.Response>` を操作する必要があります。また、複数の Promise を合成して、最終的に Play サーバが直接的に処理できるように `Promise<Result>` を返す、という方法も使えます。

```
import play.libs.F.Function;
import play.libs.WS;
import play.mvc.*;

public class Controller extends Controller {

  public static Result feedTitle(String feedUrl) {
    return async(
      WS.url(feedUrl).get().map(
        new Function<WS.Response, Result>() {
          public Result apply(WS.Response response) {
            return ok("Feed title:" + response.asJson().findPath("title"));
          }
        }
      )
    );
  }
  
}
```

<!--
## Composing results
-->
## 結果を合成する

<!--
If you want to make multiple calls in sequence, this can be achieved using `flatMap`:
-->
複数の HTTP 呼び出しを順番に行いたい場合は、 `flatMap` を使うとよいでしょう。

```
  public static Result feedComments(String feedUrl) {
    return async(
      WS.url(feedUrl).get().flatMap(
        new Function<WS.Response, Promise<Result>>() {
          public Promise<Result> apply(WS.Response response) {
            return WS.url(response.asJson().findPath("commentsUrl").get().map(
              new Function<WS.Response, Result>() {
                public Result apply(WS.Response response) {
                  return ok("Number of comments: " + response.asJson().findPath("count"));
                }
              }
            );
          }
        }
      )
    );
  }
```

<!--
## Configuring the HTTP client
-->
## HTTP クライアントを設定する

<!--
The HTTP client can be configured globally in `application.conf` via a few properties:
-->
Play アプリケーション全体で使われる HTTP クライアントの設定は、 `application.conf` にほんの少しプロパティを設定するだけで行えます。

```
# Follow redirects (default true)
ws.followRedirects=true
# Connection timeout in ms (default 120000)
ws.timeout=120000
# Whether to use http.proxy* JVM system properties (default true)
ws.useProxyProperties=true
# A user agent string to set on each request (default none)
ws.useragent="My Play Application"
```

<!--
> **Next:** [[Integrating with Akka | JavaAkka]]
-->
> **次ページ:** [[Akka との統合 | JavaAkka]]