<!-- translated -->
<!--
# Contributor Guidelines
-->
# コントリビュータ向けガイドライン

<!--
Implementation-wise, the following things should be avoided as much as possible:
-->
実装において、次のものは極力利用しないようにしましょう。

<!--
* public mutable state
* global state
* implicit conversions
* threadLocal
* locks
* casting
* introducing new, heavy external dependencies* 
-->
* 外部から変更可能な状態
* 大域的な状態
* 暗黙的変換
* ThreadLocal
* ロック
* 型キャスト
* 巨大なモジュール依存性の新規追加

<!--
## API design
-->
## API デザイン

<!--
* Play is a Java and Scala framework, make sure your changes are working for both API-s
* Java APIs should go to ```framework/play/src/main/java```, package structure is ```play.myapipackage.xxxx```
* Scala APIs should go to ```framework/play/src/main/scala```, where the package structure is ```play.api.myapipackage```
* Java and Scala APIs should be implemented the following way:
  * implement the core API in scala (```play.api.xxx```)
  * if your component requires life cycle management or needs to be swappable, create a plugin, otherwise skip this step
  * wrap core API for scala users ([example]  (https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/cache/Cache.scala#L69))
  * wrap scala API for java users ([example](https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/java/play/cache/Cache.java))
* features are forever, always think about whether a new feature really belongs to the core framework or it should be implemented as a plugin
* if you are in doubt, ask on the mailing list
-->
* Play は Java、Scala 向けのフレームワークです。変更された API が Java、Scala どちらでも動作するようにしましょう
* Java API は ```framework/play/src/main/java``` ディレクトリに配置し、パッケージ構成は ```play.myapipackage.xxxx``` にしましょう
* Scala API は ```framework/play/src/main/scala``` ディレクトリに配置し、パッケージ構成は ```play.api.myapipackage``` にしましょう
* Java および Scala API は次のように実装しましょう
  * コア API を Scala で実装します（```play.api.xxx```）
  * API の内部コンポーネントがライフサイクル管理が必要であったり、置き換え可能である必要がある場合は、プラグインとして作成しましょう。該当しない場合は、このステップは飛ばしてください。
  * コア API を Scala ユーザ向けにラップします ([サンプル](https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/cache/Cache.scala#L69))
  * Scala API を　Java ユーザ向けにラップします ([サンプル](https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/java/play/cache/Cache.java))
* 機能追加には限りがありません。その新機能はフレームワークのコアに必要か、プラグインとして実装すべきか、ということを常に考えましょう
* 疑問点があれば、メーリングリストで聞いてみましょう

<!--
## Testing and documentation
-->
## テストとドキュメント

<!--
* each and every public facing method and class need to have a corresponding scaladoc or javadoc with examples, description etc.
* each feature and bug fix requires either a functional test (```framework/integrationtest```) or a spec (```/play/src/test```)
* run Play's integration test suite ```framework/runtests``` before pushing. If a test fails, fix it, do not ignore it.
-->
* 全ての公開メソッド、公開クラスには、使用例や詳細な説明などを含む scaladoc や javadoc を記述しましょう。
* 全ての機能やバグフィックスについて、ファンクショナル・テスト（```framework/integrationtest```）や spec（```/play/src/test```）を作成しましょう。
* push 前に Play の結合テスト・スイート ```framework/runtests``` を実行しましょう。テストが失敗する場合、無視せずに、問題を修正しましょう。

<!--
## source format
-->
## コードスタイル

<!--
* run scalariform-format  before commit* * 
-->
* コミット前に scalariform-format を実行しましょう。

<!--
## git commits
-->
## git コミット

<!--
* prefer rebase
* bigger changesets
-->
* 積極的に rebase しましょう
* 同じトピックに関する変更はひとつのコミットにまとましょう