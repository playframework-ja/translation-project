<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Debugging SSL Connections
-->
# SSL 接続のデバッグ

<!--
In the event that an HTTPS connection does not go through, debugging JSSE can be a hassle.
-->
HTTPS 接続がうまくいかない場合、JSSE のデバッグは面倒になりがちです。

<!--
WS SSL provides configuration options that will turn on JSSE debug options defined in the [Debugging Utilities](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#Debug) and  [Troubleshooting Security](https://docs.oracle.com/javase/8/docs/technotes/guides/security/troubleshooting-security.html) pages.
-->
WS SSL は [デバッグ・ユーティリティ](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#Debug) と [セキュリティのトラブルシューティング](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/troubleshooting-security.html) で定義された JSSE デバッグオプションを有効にする設定オプションを提供しています。

<!--
To configure, set the `play.ws.ssl.debug` property in `application.conf`:
-->
設定するには、`application.conf` に `play.ws.ssl.debug` プロパティをセットします:

```
play.ws.ssl.debug = {
    # Turn on all debugging
    all = false
    # Turn on ssl debugging
    ssl = false
    # Turn certpath debugging on
    certpath = false
    # Turn ocsp debugging on
    ocsp = false
    # Enable per-record tracing
    record = false
    # hex dump of record plaintext, requires record to be true
    plaintext = false
    # print raw SSL/TLS packets, requires record to be true
    packet = false
    # Print each handshake message
    handshake = false
    # Print hex dump of each handshake message, requires handshake to be true
    data = false
    # Enable verbose handshake message printing, requires handshake to be true
    verbose = false
    # Print key generation data
    keygen = false
    # Print session activity
    session = false
    # Print default SSL initialization
    defaultctx = false
    # Print SSLContext tracing
    sslctx = false
    # Print session cache tracing
    sessioncache = false
    # Print key manager tracing
    keymanager = false
    # Print trust manager tracing
    trustmanager = false
    # Turn pluggability debugging on
    pluggability = false
}
```

<!--
> NOTE: This feature changes the setting of the `java.net.debug` system property which is global on the JVM.  In addition, this feature [changes static properties at runtime](https://tersesystems.com/2014/03/02/monkeypatching-java-classes/), and is only intended for use in development environments.
-->
> 注意: この機能は JVM 上においてグローバルな `java.net.debug` システムプロパティの設定を変更します。加えて、この機能は開発環境で使用されることのみを想定しており、[ランタイムの静的なプロパティを変更](https://tersesystems.com/2014/03/02/monkeypatching-java-classes/) します。

<!--
## Verbose Debugging
-->
## 冗長なデバッグ

<!--
To see the behavior of WS, you can configuring the SLF4J logger for debug output:
-->
SLF4J をデバッグ出力するよう設定して WS の振る舞いを確認することができます:

```
logger.play.api.libs.ws.ssl=DEBUG
```

<!--
## Dynamic Debugging
-->
## 動的なデバッグ

<!--
If you are working with WSClient instances created dynamically, you can use the `SSLDebugConfig` class to set up debugging using a builder pattern:
-->
動的に生成された WSClient のインスタンスを使って作業している場合、ビルダーパターンを使う `SSLDebugConfig` クラスを使ってデバッグ設定を行うことができます:

```
val debugConfig = SSLDebugConfig().withKeyManager().withHandshake(data = true, verbose = true)
```

<!--
## Further reading
-->
## 参考文献

<!--
Oracle has a number of sections on debugging JSSE issues:
-->
Oracle には JSSE のデバッグ問題に関するドキュメントが数多くあります:

<!--
* [Debugging SSL/TLS connections](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/ReadDebug.html)
* [JSSE Debug Logging With Timestamp](https://blogs.oracle.com/xuelei/entry/jsse_debug_logging_with_timestamp)
* [How to Analyze Java SSL Errors](http://www.smartjava.org/content/how-analyze-java-ssl-errors)
-->
* [SSL/TLS接続のデバッグ](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/ReadDebug.html)
* [JSSE Debug Logging With Timestamp](https://blogs.oracle.com/xuelei/entry/jsse_debug_logging_with_timestamp)
* [How to Analyze Java SSL Errors](http://www.smartjava.org/content/how-analyze-java-ssl-errors)