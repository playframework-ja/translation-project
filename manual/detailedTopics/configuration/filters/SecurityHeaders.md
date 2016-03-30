<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring Security Headers
-->
# セキュリティヘッダの設定

<!--
Play provides a security headers filter that can be used to configure some default headers in the HTTP response to mitigate security issues and provide an extra level of defense for new applications.
-->
Play には、HTTP レスポンス内のデフォルトヘッダを設定することで、セキュリティの問題を軽減し、新規アプリケーションの防御レベルを増強することのできるセキュリティヘッダフィルタが備わっています。

<!--
## Enabling the security headers filter
-->
## セキュリティヘッダフィルタの有効化

<!--
To enable the security headers filter, add the Play filters project to your `libraryDependencies` in `build.sbt`:
-->
セキュリティフィルタを有効にするには、`build.sbt` 内の `libraryDependencies` に Play filters プロジェクトを追加してください:

@[content](code/filters.sbt)

<!--
Now add the security headers filter to your filters, which is typically done by creating a `Filters` class in the root of your project:
-->
ここで、この filters にセキュリティフィルタを追加しますが、通常これはプロジェクトルートに `Filters` クラスを作成することで実現します:

Scala
: @[filters](code/SecurityHeaders.scala)

Java
: @[filters](code/detailedtopics/configuration/headers/Filters.java)

<!--
The `Filters` class can either be in the root package, or if it has another name or is in another package, needs to be configured using `play.http.filters` in `application.conf`:
-->
`Filters` クラスはルートパッケージに置くか、もし違う名前にする、あるいは違うパッケージに置く場合は、`application.conf` 内で `play.http.filters` を使って設定する必要があります:

```
play.http.filters = "filters.MyFilters"
```

<!--
## Configuring the security headers
-->
## セキュリティヘッダの設定

<!--
Scaladoc is available in the [play.filters.headers](api/scala/play/filters/headers/package.html) package.
-->
Scaladoc は [play.filters.headers](api/scala/play/filters/headers/package.html) パッケージにあります。

<!--
The filter will set headers in the HTTP response automatically.  The settings can can be configured through the following settings in `application.conf`
-->
このフィルタは HTTP レスポンス内のヘッダを自動的に設定します。これは、以下に示す `application.conf` 内の設定で制御することができます。

<!--
* `play.filters.headers.frameOptions` - sets [X-Frame-Options](https://developer.mozilla.org/en-US/docs/HTTP/X-Frame-Options), "DENY" by default.
* `play.filters.headers.xssProtection` - sets [X-XSS-Protection](http://blogs.msdn.com/b/ie/archive/2008/07/02/ie8-security-part-iv-the-xss-filter.aspx), "1; mode=block" by default.
* `play.filters.headers.contentTypeOptions` - sets [X-Content-Type-Options](http://blogs.msdn.com/b/ie/archive/2008/09/02/ie8-security-part-vi-beta-2-update.aspx), "nosniff" by default.
* `play.filters.headers.permittedCrossDomainPolicies` - sets [X-Permitted-Cross-Domain-Policies](https://www.adobe.com/devnet/articles/crossdomain_policy_file_spec.html), "master-only" by default.
* `play.filters.headers.contentSecurityPolicy` - sets [Content-Security-Policy](http://www.html5rocks.com/en/tutorials/security/content-security-policy/), "default-src 'self'" by default.
-->
* `play.filters.headers.frameOptions` -  [X-Frame-Options](https://developer.mozilla.org/en-US/docs/HTTP/X-Frame-Options) を設定し、デフォルトは "DENY" です。
* `play.filters.headers.xssProtection` -  [X-XSS-Protection](http://blogs.msdn.com/b/ie/archive/2008/07/02/ie8-security-part-iv-the-xss-filter.aspx) を設定し、デフォルトは "1; mode=block" です。
* `play.filters.headers.contentTypeOptions` -  [X-Content-Type-Options](http://blogs.msdn.com/b/ie/archive/2008/09/02/ie8-security-part-vi-beta-2-update.aspx) を設定し、デフォルトは "nosniff" です。
* `play.filters.headers.permittedCrossDomainPolicies` -  [X-Permitted-Cross-Domain-Policies](https://www.adobe.com/devnet/articles/crossdomain_policy_file_spec.html) を設定し、デフォルトは "master-only" です。
* `play.filters.headers.contentSecurityPolicy` -  [Content-Security-Policy](http://www.html5rocks.com/en/tutorials/security/content-security-policy/) を設定し、デフォルトは "default-src 'self'" です。

<!--
Any of the headers can be disabled by setting a configuration value of `null`, for example:
-->
あらゆるヘッダは、例えば以下のように値に `null` を設定して無効化することができます:

    play.filters.headers.frameOptions = null

<!--
For a full listing of configuration options, see the Play filters [`reference.conf`](resources/confs/filters-helpers/reference.conf).
-->
設定オプションの全一覧は Play filters の [`reference.conf`](resources/confs/filters-helpers/reference.conf) を参照してください。