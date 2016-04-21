<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Using the Default SSLContext
-->
# デフォルト SSLContext を使う

<!--
If you don't want to use the SSLContext that WS provides for you, and want to use `SSLContext.getDefault`, please set:
-->
WS が提供する SSLContext は使いたくないものの、`SSLContext.getDefault` を使いたい場合は、以下のように設定してください:

```
play.ws.ssl.default = true
```

<!--
## Debugging
-->
## デバッグ

<!--
If you want to debug the default context,
-->
デフォルトコンテキストをデバッグする場合、以下のように設定してください。

```
play.ws.ssl.debug {
  ssl = true
  sslctx = true
  defaultctx = true
}
```

<!--
If you are using the default SSLContext, then the only way to change JSSE behavior is through manipulating the [JSSE system properties](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#Customization).
-->
デフォルトの SSLContext を使っている場合、JSSE の振る舞いは [JSSE システムプロパティ](https://docs.oracle.com/javase/jp/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#Customization) の操作を通じてのみ変更することができます。
