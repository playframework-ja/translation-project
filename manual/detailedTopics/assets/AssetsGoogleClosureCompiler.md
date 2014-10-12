<!--
# Using Google Closure Compiler
-->
# Google Closure Compiler を使う

<!--
The [Closure Compiler](http://code.google.com/p/closure-compiler/) is a tool for making JavaScript download and run faster. It is a true compiler for JavaScript - though instead of compiling from a source language to machine code, it compiles JavaScript to better JavaScript. It parses your JavaScript, analyzes it, removes dead code and rewrites and minimizes what’s left.
-->
[Closure Compiler](http://code.google.com/p/closure-compiler/) は、JavaScript のダウンロード・実行を高速化するためのツールです。Closure Compiler は、その名の通りコンパイラです。ただし、ソースコードを機械語へコンパイルするのではなく、JavaScript を良い JavaScript へコンパイルします。JavaScript コードはパース・分析され、不要なコードは削除され、必要に応じて書き換えられて、最終的には minify されます。

<!--
Any JavaScript file present in `app/assets` will be parsed by Google Closure compiler, checked for errors and dependencies and minified if activated in the build configuration.
-->
`app/assets` ディレクトリ以下の JavaScript は全て Google Closure Compiler によりパースされて、エラーや依存性のチェックがされます。さらに、ビルド設定で有効にされている場合は minify されます。

<!--
## Check JavaScript sanity
-->
## JavaScript のサニティチェック

<!--
JavaScript code is compiled during the `compile` command, as well as automatically when modified. Error are shown in the browser just like any other compilation error.
-->
JavaScript コードは `compile` コマンドの実行時や、コードの変更時に自動的にコンパイルされます。他のコードのコンパイルエラー同様、JavaScript のコンパイルエラーもブラウザ上で確認できます。

[[images/ClosureError.png]]

<!--
## Minification
-->
## 縮小

<!--
A minified file is also generated, where `.js` is replaced by `.min.js`. In our example, it would be `test.min.js`. If you want to use the minified file instead of the regular file, you need to change the script source attribute in your HTML.
-->
`.js` が `.min.js` に置き換えられた縮小版のファイルも生成されます。今回の例の場合、縮小版は `test.min.js` となっていることでしょう。通常のファイルの替わりに縮小版を使いたい場合は、HTML ファイルにて script の source 属性を変更する必要があります。

<!--
## Entry Points
-->
## エントリポイント

By default, any JavaScript file not prepended by an underscore will be compiled. This behavior can be changed in `build.sbt` by overriding the `javascriptEntryPoints` key. This key holds a `PathFinder`.

<!--
For example, to compile only `.js` file from the `app/assets/javascripts/main` directory:
-->
例えば、 `app/assets/javascripts/main` ディレクトリの `.js` ファイルだけをコンパイルするには以下のようにします:

```
javascriptEntryPoints <<= baseDirectory(base =>
    base / "app" / "assets" / "javascripts" / "main" ** "*.js"
)
```

<!--
The default definition is:
-->
デフォルトの定義は以下のとおりです:

```
javascriptEntryPoints <<= (sourceDirectory in Compile)(base =>
   ((base / "assets" ** "*.js") --- (base / "assets" ** "_*")).get
)
```

<!--
## Options
-->
## オプション

ClosureCompiler compilation can be configured in your project’s `build.sbt` file. There are several currently supported options:

<!--
- *advancedOptimizations* Achieves extra compressions by being more aggressive in the ways that it transforms code and renames symbols. However, this more aggressive approach means that you must take greater care when you use ADVANCED_OPTIMIZATIONS to ensure that the output code works the same way as the input code.
- *checkCaja* Checks Caja control structures.
- *checkControlStructures* Checks for invalid control structures.
- *checkTypes* Checks for invalid types.
- *checkSymbols* Checks for invalid symbols.
- *ecmascript5* Sets the input- and output-language to the newer ECMAScript version 5. Might break code on older browsers.
-->
- *advancedOptimizations* コード変換とシンボルのリネームをより積極的に行うことで、更なる圧縮を達成します。一方で、より積極的なアプローチを取るということは、ADVANCED_OPTIMIZATIONS を使う際には出力されたコードが入力のコードと同じように動くことを確認するために十分な注意を払わなければならないということを意味します。
- *checkCaja* Caja コントロールの構成をチェックします。
- *checkControlStructures* 不正なコントロール構成をチェックします。
- *checkTypes* 不正な型をチェックします。
- *checkSymbols* 不正なシンボルをチェックします。
- *ecmascript5* 入力および出力言語を、より新しい ECMAScript バージョン 5 に設定します。古いブラウザでは動作しないかもしれません。

<!--
Example:
-->
例:

```
closureCompilerOptions += "ecmascript5"
```

<!--
> **Next:** [[Using require.js to manage dependencies | RequireJS-support]]
-->
> **次ページ:** [[依存性の管理に require.js を使う| RequireJS-support]]
