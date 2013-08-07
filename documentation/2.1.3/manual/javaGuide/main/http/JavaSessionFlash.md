<!-- translated -->
<!--
# Session and Flash scopes
-->
# セッションとフラッシュスコープ

<!--
## How it is different in Play
-->
## Play におけるセッションとフラッシュスコープ

<!--
If you have to keep data across multiple HTTP requests, you can save them in the Session or the Flash scope. Data stored in the Session are available during the whole user session, and data stored in the flash scope are only available to the next request.
-->
複数のリクエストにまたがってデータを保持したい場合、セッションまたはフラッシュスコープにデータを保存することができます。一旦セッションに保存されたデータは、ユーザの 1 セッション中ずっと保持され、フラッシュスコープに保存されたデータは次のリクエストまで保持されます。

<!--
It’s important to understand that Session and Flash data are not stored in the server but are added to each subsequent HTTP Request, using Cookies. This means that the data size is very limited (up to 4 KB) and that you can only store string values.
-->
セッションやフラッシュのデータはサーバに保存されるのではなく、クッキーを利用して後続のリクエストに追加されるということはとても重要です。このことは、データサイズがかなり制限される (4KB まで) と同時に、文字列型の値しか保存できないということを意味します。

<!--
Cookies are signed with a secret key so the client can’t modify the cookie data (or it will be invalidated). The Play session is not intended to be used as a cache. If you need to cache some data related to a specific session, you can use the Play built-in cache mechanism and use store a unique ID in the user session to associate the cached data with a specific user.
-->
クッキーの値は秘密鍵によって署名されているため、クライアントがクッキーのデータを変更することはできません (変更すると、値が無効化されます) 。Play のセッションはキャッシュとして使われることを想定して作られてはいません。もし、特定のセッションに関するデータをキャッシュしたい場合、Play に組み込まれたキャッシュ機構を利用すると同時に、ユーザのセッションにユニーク ID を保存して、キャッシュを特定のユーザに対応づけることができます。

<!--
> There is no technical timeout for the session, which expires when the user closes the web browser. If you need a functional timeout for a specific application, just store a timestamp into the user Session and use it however your application needs (e.g. for a maximum session duration, maxmimum inactivity duration, etc.).
-->
> セッションには技術的にタイムアウトが存在しません。セッションは、ユーザが web ブラウザを終了させたときに切れます。もし、特定のアプリケーションでタイムアウトの機能が必要になった場合は、ユーザのセッションにタイムスタンプを保存して、必要なところで参照すると良いでしょう (例えば、セッションの最長期間、期限切れまでに許される無操作時間等) 。

<!--
## Reading a Session value
-->
## セッション値の読み込み

<!--
You can retrieve the incoming Session from the HTTP request:
-->
次のように、送信されてきたセッションを HTTP リクエストから取り出すことができます。

```java
public static Result index() {
  String user = session("connected");
  if(user != null) {
    return ok("Hello " + user);
  } else {
    return unauthorized("Oops, you are not connected");
  }
}
```

<!--
## Storing data into the Session
-->
## セッションへの値の保存

<!--
As the Session is just a Cookie, it is also just an HTTP header, but Play provides a helper method to store a session value:
-->
セッションはただのクッキーであると同時に、ただの HTTP ヘッダでもあります。しかし、Play はセッションの値を格納するためのヘルパーメソッドを提供しています。

```java
public static Result index() {
  session("connected", "user@gmail.com");
  return ok("Welcome!");
}
```

<!--
The same way, you can remove any value from the incoming session:
-->
同じ方法で、受け取ったセッション中の値を削除することができます。

```java
public static Result index() {
  session().remove("connected");
  return ok("Bye");
}
```

<!--
## Discarding the whole session
-->
## セッションの破棄

<!--
If you want to discard the whole session, there is special operation:
-->
セッション全体を破棄する場合は、特別な操作が用意されています。

```java
public static Result index() {
  session().clear();
  return ok("Bye");
}
```

<!--
## Flash scope
-->
## フラッシュスコープ

<!--
The Flash scope works exactly like the Session, but with two differences:
-->
フラッシュスコープは次の 2 点の違いを除いて、セッションと全く同じように動作します。

<!--
- data are kept for only one request
- the Flash cookie is not signed, making it possible for the user to modify it.
-->
- データは 1 リクエストまで保持されます
- フラッシュ向けのクッキーは署名されていないため、ユーザによって変更される可能性があります

<!--
> **Important:** The flash scope should only be used to transport success/error messages on simple non-Ajax applications. As the data are just kept for the next request and because there are no guarantees to ensure the request order in a complex Web application, the Flash scope is subject to race conditions.
-->
> **重要:** フラッシュスコープはシンプルかつ非 Ajax なアプリケーションにおいて、成功/失敗メッセージをやり取りするためだけに利用すべきです。その理由は、データが次のリクエストまでしか保持されない、また複雑な Web アプリケーションにおいてはリクエストの順序が保証できないためにフラッシュスコープが競合状態に陥る可能性があるからです。

<!--
Here are a few examples using the Flash scope:
-->
次にフラッシュスコープの利用例をいくつか示します。

```java
public static Result index() {
  String message = flash("success");
  if(message == null) {
    message = "Welcome!";
  }
  return ok(message);
}

public static Result save() {
  flash("success", "The item has been created");
  return redirect("/home");
}
```

&nbsp;

<!--
> **Next:** [[Body parsers | JavaBodyParsers]]
-->
> **次ページ:** [[ボディパーサー | JavaBodyParsers]]