<!--
# Play enhancer
-->
# Play エンハンサ

<!--
The [Play enhancer](https://github.com/playframework/play-enhancer) is an sbt plugin that generates getters and setters for Java beans, and rewrites the code that accesses those fields to use the getters and setters.
-->
[Play エンハンサ](https://github.com/playframework/play-enhancer) は、Java Bean に getter と setter を生成し、これらのフィールドにアクセスするコードを getter と setter を使うよう書き換える sbt プラグインです。

<!--
## Motivation
-->
## 動機

<!--
One common criticism of Java is that simple things require a lot of boilerplate code.  One of the biggest examples of this is encapsulating fields - it is considered good practice to encapsulate the access and mutation of fields in methods, as this allows future changes such as validation and generation of the data. In Java, this means making all your fields private, and then writing getters and setters for each field, a typical overhead of 6 lines of code per field.
-->
Java に対するよくある批判のうちのひとつは、単純なことにも大量のボイラープレートコードを要求されることです。もっとも顕著な例はフィールドのカプセル化です - メソッド内のフィールドアクセスと状態変更をカプセル化することは、将来的にデータのバリデーションや生成を可能とするため、よいプラクティスと見做されます。これは、Java では全てのフィールドを private として、フィールドごとに getter と setter を書くことを意味するので、典型的なフィールドごとに 6 行のオーバーヘッドとなります。

<!--
Furthermore, many libraries, particularly libraries that use reflection to access properties of objects such as ORMs, require classes to be implemented in this way, with getters and setters for each field.
-->
それだけではなく、例えば ORM のようにオブジェクトのプロパティにリフレクションを使ってアクセスする多くのライブラリが、クラスのフィールドごとに getter と setter が実装されていることを要求します。

<!--
The Play enhancer provides a convenient alternative to manually implementing getters and setters. It implements some post processing on the compiled byte code for your classes, this is commonly referred to as byte code enhancement. For every public field in your classes, Play will automatically generate a getter and setter, and then will rewrite the code that uses these fields to use the getters and setters instead.
-->
Play エンハンサは、getter と setter を手作業で実装する代わりとなる機能を提供します。コンパイルされたクラスのバイトコードに対するいくつかの後処理を実装しており、これは一般的にバイトコード拡張と呼ばれています。Play は、クラス内のすべての public フィールドに対する getter と setter を生成し、これらのフィールドを使うコードを生成した getter と setter を使うように書き換えます。

<!--
### Drawbacks
-->
### 欠点

<!--
Using byte code enhancement to generating getters and setters is not without its drawbacks however.  Here are a few:
-->
しかしながら、getter と setter を生成するバイトコード拡張にも欠点はあります。以下に数点、示します:

<!--
* Byte code enhancement is opaque, you can't see the getters and setters that are generated, so when things go wrong, it can be hard to debug and understand what is happening. Byte code enhancement is ofter described as being "magic" for this reason.
* Byte code enhancement can interfere with the operation of some tooling, such as IDEs, as they will be unaware of the eventual byte code that gets used. This can cause problems such as tests failing when run in an IDE because they depend on byte code enhancement, but the IDE isn't running the byte code enhancer when it compiles your source files.
* Existing Java developers that are new to your codebase will not expect getters and setters to be generated, this can cause confusion.
-->
* バイトコード拡張は不明瞭であり、生成された getter と setter を目にすることはないので、なにかがおかしくなったときに何が起こっているのか理解し、デバッグするのが難しくなります。このような理由から、バイトコード拡張ははしばしば "魔法" と言われています。
* IDE のようなツール類は、最終的に利用されるバイトコードを認識していないので、バイトコード拡張はこれらツールの操作を妨げてしまいます。このことは、バイトコード拡張に依存しているソースコードのテストを IDE 内にて実行する場合に、IDE がコンパイル時にバイトコードエンハンサを実行しないためにテストが失敗するなどのような問題を引き起こします。
* コードベースに不慣れな従来の Java 開発者は getter と setter が生成されることを予期しておらず、混乱を引き起こすでしょう。

<!--
Whether you use the Play enhancer or not in your projects is up to you, if you do decide to use it the most important thing is that you understand what the enhancer does, and what the drawbacks may be.
-->
Play エンハンサを使うか否かはプロジェクト次第ですが、もし使うと決断した場合は、エンハンサが何を行い、どのような欠点がありえるのかを理解することが、もっとも重要です。

<!--
## Setting up
-->
## セットアップ

<!--
To enable the byte code enhancer, simply add the following line to your `project/plugins.sbt` file:
-->
バイトコードエンハンサは、以下の行を `project/plugins.sbt` ファイルに追記するだけで有効になります:

@[plugins.sbt](code/enhancer.sbt)

<!--
The Play enhancer should be enabled for all your projects.  If you want to disable the Play enhancer for a particular project, you can do that like so in your `build.sbt` file:
-->
Play エンハンサは、すべてのプロジェクトで有効になります。特定のプロジェクトで Play エンハンサを無効にしたい場合は、`build.sbt` ファイルで以下のように設定します:

@[disable-project](code/enhancer.sbt)

<!--
In some situations, it may not be possible to disable the enhancer plugin, an example of this is using Play's ebean plugin, which requires the enhancer to ensure that getters and setters are generated before it does its byte code enhancement.  If you don't want to generate getters and setters in that case, you can use the `playEnhancerEnabled` setting:
-->
例えば、事前にバイトコード拡張による getter と setter の生成が保証されていることを要求する Play の ebean プラグインを使っているときのように、いくつかの状況ではエンハンサプラグインを無効にすることができないかもしれません。このような状況で getter と setter を生成したくない場合は、`playEnhancerEnabled` 設定を使うことができます:

@[disable-enhancement](code/enhancer.sbt)

<!--
## Operation
-->
## 操作

<!--
The enhancer looks for all fields on Java classes that:
-->
エンハンサは、以下に該当する Java クラスの全フィールドを探します:

<!--
* are public
* are non static
* are non final
-->
* public で
* static ではなく
* final ではない

<!--
For each of those fields, it will generate a getter and a setter if they don't already exist.  If you wish to provide a custom getter or setter for a field, this can be done by just writing it, the Play enhancer will simply skip the generation of the getter or setter if it already exists.
-->
これらのフィールドについて、もし getter と setter が既に存在していなければ、エンハンサが生成します。フィールドに対して特別な getter または setter を提供したい場合、ただ実装しておけば、Play エンハンサは既に存在している getter または setter の生成を単にスキップします。

<!--
## Configuration
-->
## 設定

<!--
If you want to control exactly which files get byte code enhanced, this can be done by configuring the `sources` task scoped to the `playEnhancerGenerateAccessors` and `playEnhancerRewriteAccessors` tasks.  For example, to only enhance the java sources in the models package, you might do this:
-->
バイトコード拡張されるファイルを正確にコントロールしたい場合は、`playEnhancerGenerateAccessors` と `playEnhancerRewriteAccessors` タスクに `souces` タスクスコープを設定することができます。例えば models パッケージ内の java ソースだけ拡張する場合は、以下のようになるでしょう:

@[select-generate](code/enhancer.sbt)
