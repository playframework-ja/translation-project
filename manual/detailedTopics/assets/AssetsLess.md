<!-- translated -->
<!--
# Using LESS CSS
-->
# LESS CSS を使う

<!--
[[LESS CSS | http://lesscss.org/]] is a dynamic stylesheet language. It allows greater flexibility in the way you write CSS files: including support for variables, mixins and more.
-->
[[LESS CSS | http://lesscss.org/]] は、動的なスタイルシートを記述するための言語です。LESS CSS を使うと、変数やミックスインなどを活用した非常に柔軟な CSS を記述することができます。

<!--
Compilable assets in Play 2.0 must be defined in the `app/assets` directory. They are handled by the build process, and LESS sources are compiled into standard CSS files. The generated CSS files are distributed as standard resources into the same `public/` folder as the unmanaged assets, meaning that there is no difference in the way you use them once compiled.
-->
Play により別の言語へコンパイルされるアセットは、`app/assets` へ入れます。LESS CSS をここへ入れておくと、ビルドの中で普通の CSS ファイルへコンパイルされます。生成された CSS は `public/` ディレクトリに配置されたかのように扱われるため、一旦コンパイルされてしまえば通常の CSS ファイルと違いはありません。

<!--
> Note that managed resources are not copied directly into your application `public` folder, but maintained in a separate folder in `target/scala-2.9.1/resources_managed`.
-->
> 補足：生成された CSS ファイルなどの Play が管理しているリソース自体は `public/` ディレクトリへ直接コピーされるのではなく、 `target/scala-2.9.1/resources_managed` という別のディレクトリに保持されます。

<!--
For example a LESS source file at `app/assets/stylesheets/main.less` will be available as a standard resource at `public/stylesheets/main.css`.
-->
例えば、`app/assets/stylesheets/main.less` にある LESS ソースファイルは、最終的に `public/stylesheets/main.css` になります。

<!--
LESS sources are compiled automatically during a `compile` command, or when you refresh any page in your browser while you are running in development mode. Any compilation errors will be displayed in your browser:
-->
LESS ソースファイルは `compile` コマンド実行時や、開発モードにおいてブラウザ上でページをリロードしたときに、自動的にコンパイルされます。コンパイルエラーはブラウザへ出力されます。

[[images/lessError.png]]

<!--
## Working with partial LESS source files
-->
## LESS コードを分割する

<!--
You can split your LESS source into several libraries, and use the LESS `import` feature. 
-->
LESS の `import` 機能を利用すると、LESS コードを複数のライブラリへ分割することができます。

<!--
To prevent library files from being compiled individually (or imported) we need them to be skipped by the compiler. To do this, partial source files must be prefixed with the underscore (`_`) character, for example: `_myLibrary.less`. To configure this behavior, see the _Configuration_ section at the end of this page.
-->
ライブラリが個別にコンパイルや import されてしまうことがないように、分割された LESS ファイルはコンパイラに処理させないようにする必要があります。そのために、分割された LESS ファイル名にはアンダースコア (`_`) を前置することになっています。例えば、`_mylibrary.less` というようなファイル名にします。 この振る舞いを制御するには、このページの最後にある _設定_ の段落を参照してください。

<!--
## Layout
-->
## ディレクトリ構造

<!--
Here is an example layout for using LESS in your project:
-->
LESS を利用するときのディレクトリ構成は次のようになります。

```
app
 └ assets
    └ stylesheets
       └ main.less
       └ utils
          └ _reset.less
          └ _layout.less    
```

<!--
With the following `main.less` source:
-->
次のような内容の`main.less`があるとします。

```css
@import "utils/_reset.less";
@import "utils/_layout.less";

h1 {
    color: red;
}
```

<!--
The resulting CSS file will be compiled as `public/stylesheets/main.css`, and you can use this in your template as any regular public asset. A minified version will also be generated.
-->
この LESS は `public/stylesheets/main.css` という CSS ファイルにコンパイルされ、普通の公開アセットと同様にテンプレートから呼び出すことができます。縮小版も同じように生成されます。

```html
<link rel="stylesheet" href="@routes.Assets.at("stylesheets/main.css")">
```

```html
<link rel="stylesheet" href="@routes.Assets.at("stylesheets/main.min.css")">
```

<!--
## Configuration
-->
## 設定

<!--
The default behavior of compiling every file that is not prepended by an underscore may not fit every project; for example if you include a library that has not been designed that way.
-->
アンダースコアが付加されていないすべてのファイルをコンパイルするデフォルトの動作は、例えば、そのように設計されていないライブラリが含まれている場合などのように、すべてのプロジェクトには適合しないかもしれません。

<!--
This can be configured in `project/Build.scala` by overriding the `lessEntryPoints` key. This key holds a `PathFinder`.
-->
この振る舞いは、 `project/Build.scala` で `lessEntryPoints` キーを上書きすることによって制御することができます。このキーは `PathFinder` を保持しています。

<!--
For example, to compile `app/assets/stylesheets/main.less` and nothing else:
-->
例えば、 `app/assets/stylesheets/main.less` 以外は何もコンパイルしない場合、次のようにします。

```
 val main = PlayProject(appName, appVersion, mainLang = SCALA).settings(
   lessEntryPoints <<= baseDirectory(_ / "app" / "assets" / "stylesheets" ** "main.less")
 )
```

<!--
> **Next:** [[Using Google Closure Compiler | AssetsGoogleClosureCompiler]]
-->
> **次ページ:** [[Google Closure Compiler を使う | AssetsGoogleClosureCompiler]]