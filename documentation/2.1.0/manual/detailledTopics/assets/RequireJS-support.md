<!-- translated -->
# RequireJS

<!--
According to [RequireJS](http://requirejs.org/)' website 
-->
[RequireJS](http://requirejs.org/) のウェブサイトによると、

<!--
> RequireJS is a JavaScript file and module loader. It is optimized for in-browser use, but it can be used in other JavaScript environments, like Rhino and Node. Using a modular script loader like RequireJS will improve the speed and quality of your code.
-->
> RequireJS は、JavaScript ファイルであり、モジュールローダーです。ブラウザでの使用に最適化されていますが、 Rhino と Node のような、他の JavaScript 環境で使用することもできます。 RequireJS のようなモジュラースクリプトローダーを使用することで、コードの速度と品質が向上します。

<!--
What this means in practice is that one can use [RequireJS](http://requirejs.org/) to modularize big javascript codebases. RequireJS achieves this by implementing a semi-standard API called [Asynchronous Module Definition](http://wiki.commonjs.org/wiki/Modules/AsynchronousDefinition) (other similar ideas include [CommonJS](http://www.commonjs.org/) ). Using AMD it's possible to resolve and load javascript modules, usually kept in separate files, at _client side_ while allowing server side _optimization_, that is, for production use, dependencies can be minified and combined. Therefore, RequireJs supports both client side and server side resolutions.
-->
このことは、モジュール化された大きな JavaScript のコードベースに対して [RequireJS](http://requirejs.org/) を使うことができることを意味します。 RequireJS は [Asynchronous Module Definition](http://wiki.commonjs.org/wiki/Modules/AsynchronousDefinition) （一般的に AMD と略称され、同様のアイデアは [CommonJS](http://www.commonjs.org/) に含まれています。）と呼ばれる準標準 API を実装することによって、実現されています。 AMD を使用することで、 javascript モジュールの依存を解決しロードすることができること、通常、個別のファイルの中で維持された、許可するサーバーサイド _最適化_ の間の _クライアントサイド_ で、すなわち、 prod モード用に、依存性は最小にされ組み合わせることができます。したがって、 RequireJs は、クライアントサイドとサーバサイドの依存解決の両方をサポートします。

<!--
RequireJs support is enabled by default, so all you need to do is to drop javascript modules into ```public/javascripts``` and then bootstrap the module using one of the preferred RequireJS bootstraping techniques.
-->
RequireJs サポートはデフォルトで有効になっていますので、ユーザーは ```public/javascripts``` に javascript モジュールをドロップし、好きな RequireJS のブートストラップ技術を使ってモジュールをブートストラップだけすればいいのです。


<!--
## Things to know about the implementation
-->
## 実装について知るべきこと

<!--
* ```require.js``` is bundled with play, so users do not need to add it manually
* in dev mode dependencies resolved client side, closure compiler - without commonJS support - is run through the scripts for sanity check but no files are modified
* ```requireJs``` setting key in your build script should contain the list of modules you want to run through the optimizer (modules should be relative to ```app/assets/javascripts```) 
* empty ```requireJs``` key indicates that no optimization should take place
*  ```stage```, ```dist``` and ```start``` commands were changed to
run [RequireJS's optimizer](http://requirejs.org/docs/optimization.html) for configured moduled in ```app/assets/javascripts```. The minified and combined assets are stored in ```app/assets/javascripts-min```
* a new template tag ```@requireJs``` can be used  to switch between dev and prod mode seamlessly 
* by default a rhino based optimizer is used, the native, node version can be configured for performance via ```requireNativePath``` setting
* right now this is enabled only for javascript but we are looking into using it for css as well
-->
* ```require.js``` は Play にバンドルされていますので、ユーザーが手動で追加する必要はありません。
* dev モードでのクライアント側での依存解決では、 commonJsがサポートされていない、 closure compiler は健全性チェックのためにスクリプトを介して実行されますが、どのファイルも変更しません。
* ビルドスクリプト内での ```requireJs``` の設定キーは、オプティマイザを介して実行したいモジュールのリストが含まれていなければなりません。（モジュールは、```app/assets/javascripts```　からの相対パスでなければなりません）
* 空の ```requireJs``` キーは、最適化が行われないことを示します。
* ```stage``` , ```dist``` と ```start``` コマンドは設定された ```app/assets/javascripts``` 内のモジュールに [RequireJS の最適化](http://requirejs.org/docs/optimization.html) を実行するために変更されました。 最小化し、合成されたアセットは ```app/assets/javascripts-min``` に格納されます。
* 新しいテンプレートタグ　```@requireJs``` を使うことで、 dev モードと prod モードをシームレスに切り替えて使用することができます。
* デフォルトでは、rhino ベースのオプティマイザが使用され、ネイティブでの、node のバージョンは ```requireNativePath``` 設定でパフォーマンス用に設定することができます。
* 今は JavaScript でのみこの機能は有効になっていますが、CSS でも同様の方法を探しています。

<!--
## Example
-->
## 例

<!--
create `app/assets/javascripts/main.js`:
-->
`app/assets/javascripts/main.js` を作る:

```js
require(["helper/lib"],function(l) {
	var s = l.sum(4, 5);
	alert(s);
});
```

<!--
create `app/assets/javascripts/helper/lib.js`:
-->
`app/assets/javascripts/helper/lib.js` を作る:

```js
define(function() {
    return {
         sum: function(a,b) {
    		return a + b;
        }
    }
});
```

<!--
create `app/views/index.scala.html`:
-->
`app/views/index.scala.html` を作る:

```html
@helper.requireJs(core = routes.Assets.at("javascripts/require.js").url, module = routes.Assets.at("javascripts/main").url)
```

<!--
In `project/Build.scala` add:
-->
`project/Build.scala` 内に以下を追加する:

```
val main = play.Project(appName, appVersion, appDependencies).settings(
    	requireJs += "main.js"
    )	
```

<!--
After rendering the page in Dev mode you should see: ```9``` popping up in an alert
-->
Dev モードでページを再レンダリングしてみましょう: alert に ```9``` が表示されるでしょう。

<!--
## When running stage, dist or start
-->
## stage, dist または start で実行する

<!--
your application's jar file should contain (```public/javascript/main.js```):
-->
アプリケーションの jar ファイルに  (```public/javascript/main.js```) が含まれていなくてはなりません:

```js
define("helper/lib",[],function(){return{sum:function(e,t){return e+t}}}),require(["helper/lib"],function(e){var t=e.sum(5,4);alert(t)}),define("main",function(){})
```
