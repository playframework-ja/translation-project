<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Using CoffeeScript
-->
# CoffeeScript を使う

<!--
[CoffeeScript](http://coffeescript.org/) is a small and elegant language that compiles into JavaScript. It provides a nice syntax for writing JavaScript code.
-->
[CoffeeScript](http://coffeescript.org/) は、小さくかつエレガントな言語で、JavaScript へコンパイルされます。CoffeeScript は、JavaScript コードを書くためのより良い構文を提供しています。

<!--
Compiled assets in Play must be defined in the `app/assets` directory. They are handled by the build process and CoffeeScript sources are compiled into standard JavaScript files. The generated JavaScript files are distributed as standard resources into the same `public/` folder as other unmanaged assets, meaning that there is no difference in the way you use them once compiled.
-->
Play では、コンパイルされるアセットは `app/assets` ディレクトリに定義しなければなりません。このアセットはビルドプロセスによって操作され、CoffeeScript ソースは通常の JavaScript ファイルにコンパイルされます。生成された JavaScript ファイルは標準的なリソースとして、他の管理されないアセットと同じように `public/` フォルダに配布されるので、一度コンパイルされればこれらのリソースの使い方に違いはありません。

<!--
For example a CoffeeScript source file `app/assets/javascripts/main.coffee` will be available as a standard JavaScript resource, at `public/javascripts/main.js`.
-->
例えば、 `app/assets/javascripts/main.coffee` という CoffeeScript ソースファイルは `public/javascripts/main.js` にある通常の JavaScript リソースとして利用できるようになります。

<!--
CoffeeScript sources are compiled automatically during an `assets` command, or when you refresh any page in your browser while you are running in development mode. Any compilation errors will be displayed in your browser:
-->
CoffeeScript ソースファイルは `assets` コマンドの実行時や、開発モードでの動作中にブラウザでページを更新すると自動的にコンパイルされます。あらゆるコンパイルエラーはブラウザに表示されます:

[[images/coffeeError.png]]

<!--
## Layout
-->
## ディレクトリ構造

<!--
Here is an example layout for using CoffeeScript in your projects:
-->
以下は、CoffeeScript を使うプロジェクトのレイアウト例です:

```
app
 └ assets
    └ javascripts
       └ main.coffee   
```

<!--
You can use the following syntax to use the compiled JavaScript file in your template:
-->
以下の構文で、コンパイルされた JavaScript ファイルをテンプレートから使うことができます:

```html
<script src="@routes.Assets.at("javascripts/main.js")">
```

<!--
## Enablement and Configuration
-->
## 有効化と設定

<!--
CoffeeScript compilation is enabled by simply adding the plugin to your plugins.sbt file when using the `PlayJava` or `PlayScala` plugins:
-->
`Playjava` または `PlayScala` プラグインを使っている場合、CoffeeScript のコンパイルは plugins.sbt ファイルにプラグインを追加するだけで有効にすることができます:

```scala
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")
```

<!--
The plugin's default configuration is normally sufficient. However please refer to the [plugin's documentation](https://github.com/sbt/sbt-coffeescript#sbt-coffeescript) for information on how it may be configured.
-->
ふつうはプラグインのデフォルト設定で十分です。とは言え、どこまで設定できるか情報を得るために、[プラグインのドキュメント](https://github.com/sbt/sbt-coffeescript#sbt-coffeescript) を参照してください。