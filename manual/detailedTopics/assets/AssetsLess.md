<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Using LESS CSS
-->
# LESS CSS を使う

<!--
[LESS CSS](http://lesscss.org/) is a dynamic stylesheet language. It allows considerable flexibility in the way you write CSS files including support for variables, mixins and more.
-->
[LESS CSS](http://lesscss.org/) は、動的なスタイルシート言語です。変数やミックスインその他のサポートを含む、かなり柔軟な方法で CSS ファイル書くことができます。

<!--
Compilable assets in Play must be defined in the `app/assets` directory. They are handled by the build process, and LESS sources are compiled into standard CSS files. The generated CSS files are distributed as standard resources into the same `public/` folder as the unmanaged assets, meaning that there is no difference in the way you use them once compiled.
-->
Play により別の言語へコンパイルされるアセットは、`app/assets` へ入れます。LESS CSS をここへ入れておくと、ビルドの中で普通の CSS ファイルへコンパイルされます。生成された CSS は `public/` ディレクトリに配置されたかのように扱われるため、一旦コンパイルされてしまえば通常の CSS ファイルと違いはありません。

<!--
For example, a LESS source file at `app/assets/stylesheets/main.less` will be available as a standard resource at `public/stylesheets/main.css`.  Play will compile `main.less` automatically.  Other LESS files need to be included in your `build.sbt` file:
-->
例えば、`app/assets/stylesheets/main.less` にある LESS ソースファイルは、`public/stylesheets/main.css` にある標準的なリソースとして利用できるようになります。Play は `main.less` を自動的にコンパイルします。その他の LESS ファイルは `build.sbt` ファイルに含める必要があります:

```scala
includeFilter in (Assets, LessKeys.less) := "foo.less" | "bar.less"
```

<!--
LESS sources are compiled automatically during an `assets` command, or when you refresh any page in your browser while you are running in development mode. Any compilation errors will be displayed in your browser:
-->
LESS ソースファイルは `assets` コマンドの実行時や、開発モードでの動作中にブラウザでページを更新すると自動的にコンパイルされます。あらゆるコンパイルエラーはブラウザに表示されます:

[[images/lessError.png]]

<!--
## Working with partial LESS source files
-->
## LESS コードを分割する

<!--
You can split your LESS source into several libraries and use the LESS `import` feature. 
-->
LESS ソースを複数のライブラリに分割し、LESS の `import` 機能を使うことができます。

<!--
To prevent library files from being compiled individually (or imported) we need them to be skipped by the compiler. To do this partial source files can be prefixed with the underscore (`_`) character, for example: `_myLibrary.less`. The following configuration enables the compiler to ignore partials:
-->
ライブラリファイルが個別にコンパイル (またはインポート) されるのを防ぐため、コンパイラにこれらを読み飛ばしてもらう必要があります。このため、例えば `_myLibrary.less` のように、分割されたソースファイルにアンダースコア (`_`) 文字列の接頭辞を付けることができます。以下の設定により、コンパイラはパーシャルを無視します:

```scala
includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"
```


<!--
## Layout
-->
## ディレクトリ構造

<!--
Here is an example layout for using LESS in your project:
-->
以下は、LESS を使うプロジェクトのレイアウト例です:

```
app
 └ assets
    └ stylesheets
       └ main.less
       └ utils
          └ reset.less
          └ layout.less    
```

<!--
With the following `main.less` source:
-->
次のような内容の`main.less`があるとします。

```css
@import "utils/reset.less";
@import "utils/layout.less";

h1 {
    color: red;
}
```

<!--
The resulting CSS file will be compiled as `public/stylesheets/main.css` and you can use this in your template as any regular public asset.
-->
生成された CSS ファイルは `public/stylesheets/main.css` としてコンパイルされ、その他の通常の公開アセットと同じようにテンプレートで使うことができます。

```html
<link rel="stylesheet" href="@routes.Assets.at("stylesheets/main.css")">
```

<!--
## Using LESS with Bootstrap
-->
## Bootstrap と LESS を使う

<!--
[Bootstrap](http://getbootstrap.com/css/) is a very popular library used in conjunction with LESS.
-->
[Bootstrap](http://getbootstrap.com/css/) は、LESS を併せて使う、とてもポピュラーなライブラリです。

<!--
To use Bootstrap you can use its [WebJar](http://www.webjars.org/) by adding it to your library dependencies. For example, within a `build.sbt` file:
-->
ライブラリ依存性に Bootstrap の [WebJar](http://www.webjars.org/) を追加することで、Bootstrap を使うことができます。例えば、`build.sbt` ファイル内に以下のように追加します:

```scala
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.4"
```

<!--
sbt-web will automatically extract WebJars into a lib folder relative to your asset's target folder. Therefore to use Bootstrap you can import relatively e.g.:
-->
sbt-web は、アセット対象フォルダから見た lib 関連パスに WebJar を自動的に展開します。これにより、例えば以下のように Bootstrap を関連パスでインポートして使うことができます:

```less
@import "lib/bootstrap/less/bootstrap.less";

h1 {
  color: @font-size-h1;
}
```

<!--
## Enablement and Configuration
-->
## 有効化と設定

<!--
LESS compilation is enabled by simply adding the plugin to your plugins.sbt file when using the `PlayJava` or `PlayScala` plugins:
-->
`Playjava` または `PlayScala` プラグインを使っている場合、LESS のコンパイルは plugins.sbt ファイルにプラグインを追加するだけで有効にすることができます:

```scala
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.6")
```

<!--
The plugin's default configuration is normally sufficient. However please refer to the [plugin's documentation](https://github.com/sbt/sbt-less#sbt-less) for information on how it may be configured.
-->
ふつうはプラグインのデフォルト設定で十分です。とは言え、どこまで設定できるか情報を得るために、[プラグインのドキュメント](https://github.com/sbt/sbt-less#sbt-less) を参照してください。
