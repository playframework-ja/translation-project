<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Using JSHint
-->
# JSHint を使う

<!--
From its [website documentation](http://www.jshint.com/about/):
-->
[web サイトのドキュメント](http://www.jshint.com/about/) より:

<!--
> JSHint is a community-driven tool to detect errors and potential problems in JavaScript code and to enforce your team's coding conventions. It is very flexible so you can easily adjust it to your particular coding guidelines and the environment you expect your code to execute in.
-->
> JSHint は、JavaScript コードのエラーや潜在的な問題を検出し、チームのコーディング規約を執行する、コミュニティ駆動のツールです。とても柔軟で、特別なコーディングガイドラインや、期待するコード実行環境に容易に適用することができます。

<!--
Any JavaScript file present in `app/assets` will be processed by JSHint and checked for errors.
-->
`app/assets` にあるあらゆる JavaScript ファイルは JSHint に処理され、エラーをチェックされます。

<!--
## Check JavaScript sanity
-->
## JavaScript の健全性チェック

<!--
JavaScript code is compiled during the `assets` command as well as when the browser is refreshed during development mode. Errors are shown in the browser just like any other compilation error.
-->
LESS ソースファイルは、`assets` コマンドの実行時も開発モードでの動作中にブラウザでページを更新したときと同様に、自動的にコンパイルされます。エラーは、あらゆるコンパイルエラーと同じようにブラウザに表示されます。

<!--
## Enablement and Configuration
-->
## 有効化と設定

<!--
JSHint processing is enabled by simply adding the plugin to your plugins.sbt file when using the `PlayJava` or `PlayScala` plugins:
-->
`Playjava` または `PlayScala` プラグインを使っている場合、JSHint による処理は plugins.sbt ファイルにプラグインを追加するだけで有効にすることができます:

```scala
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.3")
```

<!--
The plugin's default configuration is normally sufficient. However please refer to the [plugin's documentation](https://github.com/sbt/sbt-jshint#sbt-jshint) for information on how it may be configured.
-->
ふつうはプラグインのデフォルト設定で十分です。とは言え、どこまで設定できるか情報を得るために、[プラグインのドキュメント](https://github.com/sbt/sbt-jshint#sbt-jshint) を参照してください。
