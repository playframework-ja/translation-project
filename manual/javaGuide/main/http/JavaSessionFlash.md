<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
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
Cookies are signed with a secret key so the client can’t modify the cookie data (or it will be invalidated). The Play session is not intended to be used as a cache. If you need to cache some data related to a specific session, you can use the Play built-in cache mechanism and use the session to store a unique ID to associate the cached data with a specific user.
-->
クッキーの値は秘密鍵によって署名されているため、クライアントがクッキーのデータを変更することはできません (変更すると、値が無効化されます) 。Play のセッションはキャッシュとして使われることを想定して作られてはいません。もし、特定のセッションに関するデータをキャッシュしたい場合、Play に組み込まれたキャッシュ機構を利用し、そしてキャッシュされたデータを特定のユーザに対応付けるユニークな ID をセッションに格納することができます。

<!--
> There is no technical timeout for the session, which expires when the user closes the web browser. If you need a functional timeout for a specific application, just store a timestamp into the user Session and use it however your application needs (e.g. for a maximum session duration, maxmimum inactivity duration, etc.).
-->
> セッションには技術的にタイムアウトが存在しません。セッションは、ユーザが web ブラウザを終了させたときに切れます。もし、特定のアプリケーションでタイムアウトの機能が必要になった場合は、ユーザのセッションにタイムスタンプを保存して、必要なところで参照すると良いでしょう (例えば、セッションの最長期間、期限切れまでに許される無操作時間等) 。

<!--
## Storing data into the Session
-->
## セッションへの値の保存

<!--
As the Session is just a Cookie, it is also just an HTTP header, but Play provides a helper method to store a session value:
-->
セッションはただのクッキーであると同時に、ただの HTTP ヘッダでもあります。しかし、Play はセッションの値を格納するためのヘルパーメソッドを提供しています。

@[store-session](code/javaguide/http/JavaSessionFlash.java)

<!--
The same way, you can remove any value from the incoming session:
-->
同じ方法で、受け取ったセッション中の値を削除することができます。

@[remove-from-session](code/javaguide/http/JavaSessionFlash.java)

<!--
## Reading a Session value
-->
## セッション値の読み込み

<!--
You can retrieve the incoming Session from the HTTP request:
-->
次のように、送信されてきたセッションを HTTP リクエストから取り出すことができます。

@[read-session](code/javaguide/http/JavaSessionFlash.java)

<!--
## Discarding the whole session
-->
## セッションの破棄

<!--
If you want to discard the whole session, there is special operation:
-->
セッション全体を破棄する場合は、特別な操作が用意されています。

@[discard-whole-session](code/javaguide/http/JavaSessionFlash.java)

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
So for example, after saving an item, you might want to redirect the user back to the index page, and you might want to display an error on the index page saying that the save was successful.  In the save action, you would add the success message to the flash scope:
-->
例えば、アイテムを保存した後、ユーザを index ページにリダイレクトさせて、この index ページに保存処理が成功したことを示す通知を表示したいとします。この場合、save アクションで flash スコープに処理成功メッセージを追加します:

@[store-flash](code/javaguide/http/JavaSessionFlash.java)

<!--
Then in the index action, you could check if the success message existed in the flash scope, and if so, render it:
-->
そして index アクションで flash スコープにメッセージが存在することを確認し、存在した場合はこれをレンダリングします:

@[read-flash](code/javaguide/http/JavaSessionFlash.java)

<!--
> **Next:** [[Body parsers | JavaBodyParsers]]
-->
> **次ページ:** [[ボディパーサー | JavaBodyParsers]]
