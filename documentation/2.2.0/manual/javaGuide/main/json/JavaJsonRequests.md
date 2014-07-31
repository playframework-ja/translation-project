<!-- translated -->
<!--
# Handling and serving JSON requests
-->
# JSON リクエストとレスポンス

<!--
## Handling a JSON request
-->
## JSON リクエストの処理

<!--
A JSON request is an HTTP request using a valid JSON payload as request body. Its `Content-Type` header must specify the `text/json` or `application/json` MIME type.
-->
JSON リクエストは JSON データをリクエストボディに含む HTTP リクエストです。JSON リクエストは、`Content-Type` ヘッダに `text/json` か `application/json` という MIME タイプを指定する必要があります。

<!--
By default an action uses an **any content** body parser, which you can use to retrieve the body as JSON (actually as a Jackson `JsonNode`):
-->
アクションは **any content** ボディパーサーをデフォルトで使います。これを利用して、リクエストボディを JSON (具体的には Jackson の `JsonNode`) として取得することができます。

```java
import com.fasterxml.jackson.databind.JsonNode;
...

public static Result sayHello() {
  JsonNode json = request().body().asJson();
  if(json == null) {
    return badRequest("Expecting Json data");
  } else {
    String name = json.findPath("name").getTextValue();
    if(name == null) {
      return badRequest("Missing parameter [name]");
    } else {
      return ok("Hello " + name);
    }
  }
}
```

<!--
Of course it’s way better (and simpler) to specify our own `BodyParser` to ask Play to parse the content body directly as JSON:
-->
この場合、専用の`BodyParser` を指定することで Play にコンテントボディを直接的に JSON としてパースさせると、記述がシンプル化されてなお良いでしょう。

```java
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;
...

@BodyParser.Of(BodyParser.Json.class)
public static Result sayHello() {
  JsonNode json = request().body().asJson();
  String name = json.findPath("name").getTextValue();
  if(name == null) {
    return badRequest("Missing parameter [name]");
  } else {
    return ok("Hello " + name);
  }
}
```

<!--
> **Note:** This way, a 400 HTTP response will be automatically returned for non JSON requests with Content-type set to application/json. 
-->
> **ノート:** この方法では、JSON 以外のリクエストに対しては自動的に HTTP の 400 番のレスポンスが application/json に設定された Content-type と共に返ってきます。

<!--
You can test it with **cURL** from a command line:
-->
このアクションは、コマンドラインから **cURL** を使って以下のようにテストできます。

```bash
curl 
  --header "Content-type: application/json" 
  --request POST 
  --data '{"name": "Guillaume"}' 
  http://localhost:9000/sayHello
```

<!--
It replies with:
-->
レスポンスは以下のようになります。

```http
HTTP/1.1 200 OK
Content-Type: text/plain; charset=utf-8
Content-Length: 15

Hello Guillaume
```

<!--
## Serving a JSON response
-->
## JSON レスポンスの送信

<!--
In our previous example we handled a JSON request, but replied with a `text/plain` response. Let’s change that to send back a valid JSON HTTP response:
-->
前述の例ではリクエストを JSON で受けていましたが、レスポンスは `text/plain` として送信していました。これを、正しい JSON HTTP レスポンスを送り返すように変更してみましょう。

```java
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
...

@BodyParser.Of(BodyParser.Json.class)
public static Result sayHello() {
  JsonNode json = request().body().asJson();
  ObjectNode result = Json.newObject();
  String name = json.findPath("name").getTextValue();
  if(name == null) {
    result.put("status", "KO");
    result.put("message", "Missing parameter [name]");
    return badRequest(result);
  } else {
    result.put("status", "OK");
    result.put("message", "Hello " + name);
    return ok(result);
  }
}
```

<!--
Now it replies with:
-->
レスポンスは以下のようになります。

```http
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 43

{"status":"OK","message":"Hello Guillaume"}
```

<!--
> **Next:** [[Working with XML | JavaXmlRequests]]
-->
> **次ページ:** [[XML | JavaXmlRequests]]
