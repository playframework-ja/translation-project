<!-- translated -->
<!--
# Set-up a front-end HTTP server
-->
# フロント・エンドとなる HTTP サーバのセットアップ

<!--
You can easily deploy your application as a stand-alone server by setting the application HTTP port to 80:
-->
HTTP ポートを 80 番に設定すれば、アプリケーションを簡単にスタンドアロンのサーバとしてデプロイすることができます。


```
$ start -Dhttp.port=80
```

<!--
> Note that you probably need root permissions to bind a process on this port.
-->
> プロセスを 80 番ポートにバインドするために root 権が必要なことがあります。

<!--
But if you plan to host several applications in the same server or load balance several instances of your application for scalability or fault tolerance, you can use a front-end HTTP server.
-->
しかし、同じサーバで複数のアプリケーションをホスティングしたり、スケーラビリティや耐障害性の観点から、アプリケーションのインスタンスを複数たちあげて、それらをロードバランシングしたいこともあるでしょう。その場合は、フロントエンド HTTP サーバを利用することができます。

<!--
Note that using a front-end HTTP server will never give you better performance than using Play server directly.
-->
フロントエンド HTTP サーバを利用する場合、Play サーバを直接利用する場合よりも優れたパフォーマンスが得られることは決してないことに注意してください。

<!--
## Set-up with lighttpd
-->
## lighttpd を使う

<!--
This example shows you how to configure [[lighttpd | http://www.lighttpd.net/]] as a front-end web server. Note that you can do the same with Apache, but if you only need virtual hosting or load balancing, lighttpd is a very good choice and much easier to configure!
-->
以下は [[lighttpd | http://www.lighttpd.net/]] をフロントエンド Web サーバとして構成する例です。全く同じ用途に Apache を利用することもできますが、必要なものがバーチャルホスティングやロードバランスの機能だけであれば、 lighttpd の方が設定が簡単なのでおすすめです。

<!--
The `/etc/lighttpd/lighttpd.conf` file should define things like this:
-->
`/etc/lighttpd/lighttpd.conf` ファイルで、以下のように定義します:

```
server.modules = (
      "mod_access",
      "mod_proxy",
      "mod_accesslog" 
)
…
$HTTP["host"] =~ "www.myapp.com" {
    proxy.balance = "round-robin" proxy.server = ( "/" =>
        ( ( "host" => "127.0.0.1", "port" => 9000 ) ) )
}
 
$HTTP["host"] =~ "www.loadbalancedapp.com" {
    proxy.balance = "round-robin" proxy.server = ( "/" => ( 
          ( "host" => "127.0.0.1", "port" => 9001 ), 
          ( "host" => "127.0.0.1", "port" => 9002 ) ) 
    )
}
```

<!--
## Set-up with Apache
-->
## Apache を使う

<!--
The example below shows a simple set-up with [[Apache httpd server | http://httpd.apache.org/]] running in front of a standard Play configuration.
-->
以下は [[Apache httpd server | http://httpd.apache.org/]] を標準的な設定の Play アプリケーションの前段に配置するシンプルな設定の例です。

```
LoadModule proxy_module modules/mod_proxy.so
…
<VirtualHost *:80>
  ProxyPreserveHost On
  ServerName www.loadbalancedapp.com
  ProxyPass  /excluded !
  ProxyPass / http://127.0.0.1:9000/
  ProxyPassReverse / http://127.0.0.1:9000/
</VirtualHost>
```

<!--
## Advanced proxy settings
-->
## リバースプロキシに関する詳細設定

<!--
When using an HTTP frontal server, request addresses are seen as coming from the HTTP server. In a usual set-up, where you both have the Play app and the proxy running on the same machine, the Play app will see the requests coming from 127.0.0.1.
-->
フロントエンド HTTP サーバを利用する場合、Play アプリケーションからすると、 HTTP リクエストがその HTTP サーバから送信されているように見えます。通常の設定では、同じマシン上で Play アプリケーションとリバースプロキシを起動している場合、 Play からはリクエストが 127.0.0.1 から送信されているように見えます。

<!--
Proxy servers can add a specific header to the request to tell the proxied application where the request came from. Most web servers will add an X-Forwarded-For header with the remote client IP address as first argument. If you enable the forward support in the XForwardedSupport configuration, Play will change the request.remoteAddress from the proxy’s IP to the client’s IP. You have to list the IP addresses of your proxy servers for this to work.
-->
リバースプロキシとなってる HTTP サーバは、プロキシ先の Play アプリケーションにリクエストの送信元を知らせるために、特別なヘッダを付与することができます。ほとんどの Web サーバは X-Forwarded-For というヘッダの第一引数にリクエスト元のクライアントの IP アドレスをつけて送信します。そのような Web サーバを利用している場合、 Play アプリケーションの XForwardedSupport を設定することで、 Play が request.remoteAddress をリバースプロキシの IP アドレスからリクエスト元クライントの IP アドレスに変更してくれます。ただし、この機能を利用するためには、アプリケーションの設定内でリバースプロキシの IP アドレスを全て記述しておく必要があります。

<!--
However, the host header is untouched, it’ll remain issued by the proxy. If you use Apache 2.x, you can add a directive like:
-->
この設定を行った場合でも、 host ヘッダは変更されず、リバースプロキシが送信したままの内容になっています。そこで、例えば Apache 2.x を利用している場合は、次の設定を行うよいでしょう。

```
ProxyPreserveHost on
```

<!--
The host: header will be the original host request header issued by the client. By combining theses two techniques, your app will appear to be directly exposed.
-->
この設定を行うと、 Play アプリケーションへ送信される Host ヘッダが、クライアントから送信された元々の Host ヘッダの内容と同じになります。このように、XForwardedSupport と ProxyPreserveHost の二つの設定を組み合わせることで、アプリケーションが直接クライアントに公開されているように振る舞えるようになります。

<!--
If you don't want this play app to occupy the whole root, add an exclusion directive to the proxy config:
-->
もし、リバースプロキシへの一部のリクエストのみを Play アプリケーションに処理させたい場合は、一部のパスをプロキシ対象から除外する設定を追加してください。

```
ProxyPass /excluded !
```

<!--
## Apache as a front proxy to allow transparent upgrade of your application
-->
## アプリケーションを透過的に更新するための、リバースプロキシとしての Apache

<!--
The basic idea is to run two Play instances of your web application and let the front-end proxy load-balance them. In case one is not available, it will forward all the requests to the available one.
-->
基本的な考え方は、 Play アプリケーションのインスタンスを二つ起動して、フロントエンドのプロキシサーバにそれらをロードバランスさせます。一方のインスタンスが落ちている場合には、稼働中のもう一方のインスタンスにのみリクエストを転送するようにします。

<!--
Let’s start the same Play application two times: one on port 9999 and one on port 9998.
-->
まず、同じ Play アプリケーションを 2 回起動します。一つはポート 9999 番、もう一つはポート 9998 で起動します。

```
$ start -Dhttp.port=9998
$ start -Dhttp.port=9999
```

<!--
Now, let’s configure our Apache web server to have a load balancer.
-->
次に、 Apache によるロードバランシングの設定を行います。

<!--
In Apache, I have the following configuration:
-->
Apache の設定ファイルに、次のような設定を記述します。

```
<VirtualHost mysuperwebapp.com:80>
  ServerName mysuperwebapp.com
  <Location /balancer-manager>
    SetHandler balancer-manager
    Order Deny,Allow
    Deny from all
    Allow from .mysuperwebapp.com
  </Location>
  <Proxy balancer://mycluster>
    BalancerMember http://localhost:9999
    BalancerMember http://localhost:9998 status=+H
  </Proxy>
  <Proxy *>
    Order Allow,Deny
    Allow From All
  </Proxy>
  ProxyPreserveHost On
  ProxyPass /balancer-manager !
  ProxyPass / balancer://mycluster/
  ProxyPassReverse / http://localhost:9999/
  ProxyPassReverse / http://localhost:9998/
</VirtualHost>
```

<!--
The important part is balancer://mycluster. This declares a load balancer. The +H option means that the second Play application is on stand-by. But you can also instruct it to load-balance.
-->
特に重要なのは、 balancer://mycluster という部分です。これがロードバランサーの定義です。 +H オプションは、2 番目の Play アプリケーションが待機系であるということの宣言です。ただし、同時にこの待機系をロードバランス対象に加えることもできます。

<!--
Apache also provides a way to view the status of your cluster. Simply point your browser to /balancer-manager to view the current status of your clusters.
-->
Apache には、宣言されたクラスタのステータスを表示する機能も備わっています。現在のステータスを表示するためには、ブラウザで /balancer-manager にアクセスしましょう。

<!--
Because Play is completely stateless you don’t have to manage sessions between the 2 clusters. You can actually easily scale to more than 2 Play instances.
-->
Play は完全にステートレスなアーキテクチャになっているため、2 つのクラスタ間でのセッションの共有に頭を悩ませる必要はありません。そのおかげで、特に苦労せず 3 つ以上のインスタンスにスケールアウトさせることが可能です。
