<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# RequireJS
-->
# RequireJS

<!--
According to [RequireJS](http://requirejs.org/)' website 
-->
[RequireJS](http://requirejs.org/) のウェブサイト曰く

<!--
> RequireJS is a JavaScript file and module loader. It is optimized for in-browser use, but it can be used in other JavaScript environments... Using a modular script loader like RequireJS will improve the speed and quality of your code.
-->
> RequireJS は、JavaScript ファイルであり、モジュールローダーです。ブラウザでの使用に最適化されていますが、他の JavaScript 環境で使用することもできます。RequireJS のようなモジュラースクリプトローダーを使用することで、コードの速度と品質が向上します。

<!--
What this means in practice is that one can use [RequireJS](http://requirejs.org/) to modularize your JavaScript. RequireJS achieves this by implementing a semi-standard API called [Asynchronous Module Definition](http://wiki.commonjs.org/wiki/Modules/AsynchronousDefinition) (other similar ideas include [CommonJS](http://www.commonjs.org/) ). Using AMD makes it is possible to resolve and load javascript modules on the _client side_ while allowing server side _optimization_. For server side optimization module dependencies may be minified and combined using [UglifyJS 2](https://github.com/mishoo/UglifyJS2#uglifyjs-2).
-->
現実問題としては、[RequireJS](http://requirejs.org/) を使って JavaScript をモジュール化できるということを意味しています。RequireJS は [Asynchronous Module Definition](http://wiki.commonjs.org/wiki/Modules/AsynchronousDefinition) と呼ばれる (この他に同様のアイディアが [CommonJS](http://www.commonjs.org/) に含まれている) 準標準 API を実装することで、これを実現しています。AMD を使うと、サーバサイドで _最適化_ しつつ、 _クライアントサイド_ で javascript のモジュールを解決してロードすることができます。サーバサイドの最適化では、モジュールは [UglifyJS 2](https://github.com/mishoo/UglifyJS2#uglifyjs-2) を使って最小化され、結合されているかもしれません。

<!--
By convention RequireJS expects a main.js file to bootstrap its module loader.
-->
慣例により、RequireJS はモジュールロードの起点として main.js ファイルを要求します。

<!--
## Deployment
-->
## デプロイ

<!--
The RequireJS optimizer shouldn't generally kick-in until it is time to perform a deployment i.e. by running the `start`, `stage` or `dist` tasks.
-->
RequireJS の最適化は一般的に、例えば `start`, `stage` または `dist` タスクの実行時など、デプロイを行うときまでは実行されるべきではありません。

<!--
If you're using WebJars with your build then the RequireJS optimizer plugin will also ensure that any JavaScript resources referenced from within a WebJar are automatically referenced from the [jsdelivr](http://www.jsdelivr.com) CDN. In addition if any `.min.js` file is found then that will be used in place of `.js`. An added bonus here is that there is no change required to your html!
-->
ビルドに WebJar を使用している場合、RequireJS 最適化プラグインは WebJar 内から参照されているあらゆる JavaScript リソースが [jsdelivr](http://www.jsdelivr.com) CDN から参照されることを保証します。加えて、`.min.js` ファイルが見つかった場合、これを `.js` の代わりに使います。おまけに、html を変更する必要はありません!

<!--
## Enablement and Configuration
-->
## 有効化と設定

<!--
RequireJS optimization is enabled by simply adding the plugin to your plugins.sbt file when using the `PlayJava` or `PlayScala` plugins:
-->
`Playjava` または `PlayScala` プラグインを使っている場合、RequireJS の最適化は plugins.sbt ファイルにプラグインを追加するだけで有効にすることができます:

```scala
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")
```

<!--
To add the plugin to the asset pipeline you can declare it as follows (assuming just the one plugin for the pipeline - add others into the sequence such as digest and gzip as required):
-->
以下のように宣言して、このプラグインをアセットパイプラインに加えることができます (パイプラインにプラグインがひとつだけとした場合 - 必要に応じて 他の digest や gzip などをシーケンスに追加します):

```scala
pipelineStages := Seq(rjs)
```

<!--
A standard build profile for the RequireJS optimizer is provided and should suffice for most projects. However please refer to the [plugin's documentation](https://github.com/sbt/sbt-rjs#sbt-rjs) for information on how it may be configured.
-->
RequireJS 最適化の標準的なビルドプロファイルが提供されており、ほとんどのプロジェクトにはこれで十分です。とは言え、どこまで設定できるか情報を得るために、[プラグインのドキュメント](https://github.com/sbt/sbt-rjs#sbt-rjs) を参照してください。

<!--
Note that RequireJS performs a lot of work and while it works when executed in-JVM under Trireme, you will be best to use Node.js as the js-engine from a performance perspective. For convenience you can set the `sbt.jse.engineType` property in `SBT_OPTS`. For example on Unix:
-->
RequireJS は多くの作業を行うこと、そしてこの作業が Trireme によって JVM 内で実行されている場合、性能の観点から js-engine として Node.js を使うのがベストであることに注意してください。利便性のため、`SBT_OPTS` に `sbt.jse.engineType` プロパティを設定することができます。Unix の場合:

```bash
export SBT_OPTS="$SBT_OPTS -Dsbt.jse.engineType=Node"
```

<!--
Please refer to the [plugin's documentation](https://github.com/sbt/sbt-rjs#sbt-rjs) for information on how it may be configured.
-->
どこまで設定できるか情報を得るために、[プラグインのドキュメント](https://github.com/sbt/sbt-rjs#sbt-rjs) を参照してください。
