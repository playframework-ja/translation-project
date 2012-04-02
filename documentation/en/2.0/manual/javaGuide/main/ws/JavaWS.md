# The Play WS API

Sometimes you want to call other HTTP services from within a Play application. Play supports this via its `play.libs.WS` library, which provides a way to make asynchronous HTTP calls.

A call made by `play.libs.WS` should return a `Promise<Ws.Response>`, which you can handle later with Play’s asynchronous mechanisms.

## Making HTTP calls

To make an HTTP request, you start with `WS.url()` to specify the URL. Then you get a builder that you can use to specify HTTP options, such as setting headers. You end by calling a method corresponding to the HTTP method you want to use:

```
Promise<WS.Response> homePage = WS.url("http://mysite.com").get();
```

Alternatively:

```
Promise<WS.Response> result = WS.url("http://localhost:9001").post("content");
```

## Retrieving the HTTP response result

The call is made asynchronously and you need to manipulate it as a `Promise<WS.Response>` to get the actual content. You can compose several promises and end up with a `Promise<Result>` that can be handled directly by the Play server:

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

> **Next:** [[Integrating with Akka | JavaAkka]]