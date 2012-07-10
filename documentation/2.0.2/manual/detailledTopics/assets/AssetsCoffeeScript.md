<!--
# Using CoffeeScript
-->
# CoffeeScript を使う

<!--
[[CoffeeScript | http://jashkenas.github.com/coffee-script/]] is a small and elegant language that compiles into JavaScript. It provides a nicer syntax for writing JavaScript code.
-->
[[CoffeeScript | http://jashkenas.github.com/coffee-script/]] は、小さくかつエレガントな言語で、JavaScript へコンパイルされます。CoffeeScript を利用すると、JavaScript コードをより良い構文で書くことができます。

<!--
Compiled assets in Play 2.0 must be defined in the `app/assets` directory. They are handled by the build process, and CoffeeScript sources are compiled into standard JavaScript files. The generated JavaScript files are distributed as standard resources into the same `public/` folder as other unmanaged assets, meaning that there is no difference in the way you use them once compiled.
-->
Play 2.0 では、CoffeeScript など、別の言語へコンパイルされるようなアセットは全て `app/assets` へ入れることになっています。ここへ入れられたファイルはビルドの中で自動的にコンパイルされます。CoffeeScript の場合は、このとき普通の JavaScript へコンパイルされます。生成された JavaScript ファイルは `public/` ディレクトリに配置されたかのように扱われるため、一旦コンパイルされてしまえば通常の JavaScript ファイルと違いはありません。

<!--
> Note that managed resources are not copied directly into your application’s `public` folder, but maintained in a separate folder in `target/scala-2.9.1/resources_managed`.
-->
> 補足：生成された JavaScript ファイルなどの Play が管理しているリソース自体は `public/` ディレクトリへ直接コピーされるのではなく、 `target/scala-2.9.1/resources_managed` という別のディレクトリに保持されます。

<!--
For example a CoffeeScript source file `app/assets/javascripts/main.coffee` will be available as a standard JavaScript resource, at `public/javascripts/main.js`.
-->
例えば、 `app/assets/javascripts/main.coffee` は `public/javascripts/main.js` において通常の JavaScript リソースとして利用できるようになります。

<!--
CoffeeScript sources are compiled automatically during a `compile` command, or when you refresh any page in your browser while you are running in development mode. Any compilation errors will be displayed in your browser:
-->
CoffeeScript ソースファイルは `compile` コマンドの実行時や、開発モードでアプリケーションを起動中にブラウザでページが再読み込みされた際に自動的にコンパイルされます。後者のタイミングでコンパイルされた場合、コンパイルエラーはブラウザ上に表示されます。

[[images/coffeeError.png]]

<!--
## Layout
-->
## ディレクトリ構造

<!--
Here is an example layout for using CoffeeScript in your projects:
-->
CoffeeScript を使うプロジェクトの基本的なデイレクトリ構造は次のようになります。

```
app
 └ assets
    └ javascripts
       └ main.coffee   
```

<!--
Two JavaScript files will be compiled: `public/javascripts/main.js` and `public/javascripts/main.min.js`. The first one is a readable file useful in development, and the second one a minified file that you can use in production. You can use either one in your template:
-->
二つの JavaScript ファイル: `public/javascripts/main.js` と `public/javascripts/main.min.js` がコンパイルされます。ひとつ目のファイルは開発時に便利な読めるコードで、二つ目は本番環境で利用できる圧縮ファイルです。いずれもテンプレートにて使用することができます:

```html
<script src="@routes.Assets.at("javascripts/main.js")">
```

```html
<script src="@routes.Assets.at("javascripts/main.min.js")">
```

<!--
## Options
-->
## オプション

<!--
CoffeeScript compilation can be configured in your project’s `Build.scala` file (in the settings part of the `PlayProject`). The only option currently supported is *bare* mode.
-->
CoffeeScript のコンパイルは (`PlayProject` の設定部分である) `Build.scala` ファイルで制御することができます。現在サポートされている唯一のオプションは *bare* モードです。

```
coffeescriptOptions := Seq("bare")
```
<!--
> Note there is a new experimental option which lets you use the native coffee script compiler. The benefit is that it's way faster, the disadvantage is that it's an external dependency. If you want to try this, add this to your settings:
-->
> ネイティブの CoffeeScript コンパイルを使う実験的なオプションがあることを覚えておいてください。この方法の利点は速さであり、欠点は外部への依存です。この方法を試したい場合は、設定に以下を追加してください:

```
coffeescriptOptions := Seq("native", "/usr/local/bin/coffe -p")
```

<!--
By default, the JavaScript code is generated inside a top-level function safety wrapper, preventing it from polluting the global scope. The `bare` option removes this function wrapper.
-->
グローバルスコープの汚染を防ぐため、 JavaScript コードはデフォルトでトップレベルの安全な関数ラッパ内に生成されます。 `bare` オプションはこの関数ラッパを取り除きます。

<!--
> **Next:** [[Using LESS CSS | AssetsLess]]
-->
> **次:** [[LESS CSS を使う | AssetsLess]]