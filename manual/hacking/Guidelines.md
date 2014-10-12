<!--
# Developer & Contributor Guidelines
-->
# 開発者 & コントリビュータ向けガイドライン

<!--
## General Workflow
-->
## 一般的なワークフロー

<!--
This is the process for committing code into master. There are of course exceptions to these rules, for example minor changes to comments and documentation, fixing a broken build etc.
-->
これは master にコミットする手順です。もちろん、例えばコメントやドキュメントのちょっとした変更や、壊れたビルドの修正、その他など、これらのルールに対する例外もあります。

<!--
1. Make sure you have signed the [Typesafe CLA](http://www.typesafe.com/contribute/cla), if not, sign it online.
2. Before starting to work on a feature or a fix, you have to make sure that:
    1. There is a ticket for your work in the project's issue tracker. If not, create it first.
    2. The ticket has been scheduled for the current milestone.
    3. The ticket is estimated by the team.
    4. The ticket have been discussed and prioritized by the team.
3. You should always perform your work in a Git feature branch. The branch should be given a descriptive name that explains its intent.
4. We use [scalariform](https://github.com/mdr/scalariform) to ensure that code is formatted consistently. Please ensure that your code complies by running the build scalariform-format task.
4. When the feature or fix is completed you should open a [Pull Request](https://help.github.com/articles/using-pull-requests) on GitHub. Pull Requests that do not pass their tests or have scalariform compliant formatting will be automatically failed by our CI.
5. The Pull Request should be reviewed by other maintainers (as many as feasible/practical). Note that the maintainers can consist of outside contributors, both within and outside the Play team. Outside contributors are encouraged to participate in the review process, it is not a closed process.
6. After the review you should fix the issues as needed (pushing a new commit for new review etc.), iterating until the reviewers give their thumbs up.
7. Once the code has passed review the Pull Request can be merged into the master branch. 
-->
1. [Typesafe CLA](http://www.typesafe.com/contribute/cla) にサインしていることを確認して、まだサインしていない場合は、オンラインでサインしてください。
2. 機能や修正について作業を始める前に、以下について確認しなければなりません:
    1. プロジェクトの issue とラッカーにその作業用のチケットがあること。もしチケットが無い場合は、まずチケットを作成してください。
    2. そのチケットが現在のマイルストーン内に計画されていること。
    3. そのチケットがチームによって検討されていること。
    4. そのチケットがチームによって議論され、優先順位づけされていること。
3. 常に Git の機能ブランチで作業を行うべきです。ブランチは、その意図を説明する、分かり易い名前を与えられるべきです。
4. 常にコードがフォーマットされていることを保証するために [scalariform](https://github.com/mdr/scalariform) を使っています。scalariform-format タスクのビルドを実行して、コードがコンパイルできることを確認してください。
4. 機能や修正が完了したら、GitHub 上で [プルリクエスト](https://help.github.com/articles/using-pull-requests) を実行してください。テストにパスしない、または scalariform のフォーマットに適合しないプルリクエストは、CI によって自動的に失敗します。
5. プルリクエストは、(実現性があり/現実的な限り多くの) 他のメンテナーによってレビューされるべきです。メンテナーは、Play チーム内外いずれも含めて、外部のコントリビュータによって構成され得ることに留意してください。レビュープロセスは閉じたものではなく、外部のコントリビュータはレビューに参加することが奨励されています。
6. レビュー後、必要に応じて (新しいレビューその他について新しいコミットを push しながら) 課題を修正し、レビュア―が「いいね！」と言うまでくり返します。
7. レビューを通過したら、そのプルリクエストは master ブランチにマージされます。

<!--
## Developer group & discussions
-->
## 開発者グループと議論

<!--
To discuss features, proposal and pull-requests, use the dedicated group at https://groups.google.com/forum/#!forum/play-framework-dev.
-->
機能、提案、そしてプルリクエストの議論には、専用の https://groups.google.com/forum/#!forum/play-framework-dev グループを使います。

<!--
## Pull Request Requirements
-->
## プルリクエスト要件

<!--
For a Pull Request to be considered at all it has to meet these requirements:
-->
プルリクエストがきちんと検討されるためには、これらの要件を満たす必要があります:

<!--
1. Live up to the current code standard:
   - Not violate [DRY](http://programmer.97things.oreilly.com/wiki/index.php/Don%27t_Repeat_Yourself).
   - [Boy Scout Rule](http://programmer.97things.oreilly.com/wiki/index.php/The_Boy_Scout_Rule) needs to have been applied.
2. Regardless if the code introduces new features or fixes bugs or regressions, it must have comprehensive tests.
3. The code must be well documented in the Play standard documentation format (see the ‘Documentation’ section below). Each API change must have the corresponding documentation change.
4. Implementation-wise, the following things should be avoided as much as possible:
   * Global state
   * Public mutable state
   * Implicit conversions
   * ThreadLocal
   * Locks
   * Casting
   * Introducing new, heavy external dependencies
5. The Play API design rules are the following:
   * Play is a Java and Scala framework, make sure your changes are working for both API-s
   * Java APIs should go to ```framework/play/src/main/java```, package structure is ```play.myapipackage.xxxx```
   * Scala APIs should go to ```framework/play/src/main/scala```, where the package structure is ```play.api.myapipackage```
   * Java and Scala APIs should be implemented the following way:
     * implement the core API in scala (```play.api.xxx```)
     * if your component requires life cycle management or needs to be swappable, create a plugin, otherwise skip this step
     * wrap core API for scala users ([example](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala#L69))
     * wrap scala API for java users ([example](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/java/play/cache/Cache.java))
   * Features are forever, always think about whether a new feature really belongs to the core framework or it should be implemented as a plugin
-->
1. 現在のコーディング標準に沿っていること:
   - [DRY](http://programmer.97things.oreilly.com/wiki/index.php/Don%27t_Repeat_Yourself) に違反しない
   - [Boy Scout Rule](http://programmer.97things.oreilly.com/wiki/index.php/The_Boy_Scout_Rule) を適用する
2. そのコードが新機能を導入するものか、あるいはバグやリグレッションを修正するものかに関わらず、包括的なテストが必要です
3. コードは Play 標準のドキュメントフォーマット (下記の 'ドキュメント' の段落を参照してください) に従って充分に説明されていなければいけません。あらゆる API の変更には関連するドキュメントの変更も必要です。
4. 実装面では、以下に示すことを可能な限り避けるべきです:
     * 大域的な状態
     * 外部から変更可能な状態
     * 暗黙的変換
     * ThreadLocal
     * ロック
     * 型キャスト
     * 巨大なモジュール依存性の新規追加
5. Play の API 設計ルールは以下の通りです:
   * Play は Java、Scala 向けのフレームワークです。変更された API が Java、Scala どちらでも動作するようにしましょう
   * Java API は ```framework/play/src/main/java``` ディレクトリに配置し、パッケージ構成は ```play.myapipackage.xxxx``` にしましょう
   * Scala API は ```framework/play/src/main/scala``` ディレクトリに配置し、パッケージ構成は ```play.api.myapipackage``` にしましょう
   * Java および Scala API は次のように実装しましょう:
     * コア API を Scala で実装します（```play.api.xxx```）
     * API の内部コンポーネントがライフサイクル管理が必要であったり、置き換え可能である必要がある場合は、プラグインとして作成しましょう。該当しない場合は、このステップは飛ばしてください
     * コア API を Scala ユーザ向けにラップします ([サンプル](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/scala/play/api/cache/Cache.scala#L69))
     * Scala API を Java ユーザ向けにラップします ([サンプル](https://github.com/playframework/playframework/blob/master/framework/src/play-cache/src/main/java/play/cache/Cache.java))
   * 機能追加には限りがありません。その新機能はフレームワークのコアに必要か、プラグインとして実装すべきか、ということを常に考えましょう

<!--
If these requirements are not met then the code should **not** be merged into master, or even reviewed - regardless of how good or important it is. No exceptions.
-->
これらの要件が満たされない場合は、そのコードがどれだけ優れていても、またはどれだけ重要であっても、master にマージされることが **あり得ない** どころか、レビューすらされません。例外はありません。

<!--
## Documentation
-->
## ドキュメント

<!--
The documentation live as markdown pages into the `documentation/manual` directory. Each Play branch has it own documentation version.
-->
markdown 形式のドキュメントが `documentation/manual` ディレクトリにあります。Play のブランチごとに、各バージョンのドキュメントがあります。

<!--
## Work In Progress
-->
## 進行中の作業

It is ok to work on a public feature branch in the GitHub repository. Something that can sometimes be useful for early feedback etc. If so then it is preferable to name the branch accordingly. This can be done by either prefix the name with ``wip-`` as in ‘Work In Progress’, or use hierarchical names like ``wip/..``, ``feature/..`` or ``topic/..``. Either way is fine as long as it is clear that it is work in progress and not ready for merge. This work can temporarily have a lower standard. However, to be merged into master it will have to go through the regular process outlined above, with Pull Request, review etc.. 

<!--
Also, to facilitate both well-formed commits and working together, the ``wip`` and ``feature``/``topic`` identifiers also have special meaning.   Any branch labelled with ``wip`` is considered “git-unstable” and may be rebased and have its history rewritten.   Any branch with ``feature``/``topic`` in the name is considered “stable” enough for others to depend on when a group is working on a feature.
-->
実際の作業ときれいに整形されたコミットメッセージの組み合わせを促進するために、``wip`` や ``feature``/``topic`` の識別子にもまた、特別な意味を持たせます。``wip`` というラベルの付いたブランチは、いずれも "git-unstable”と見なされ、リベースして履歴を変更される場合があります。``feature``/``topic`` という名前の付いたブランチは、ある機能についてグループで作業している場合、依存するのに十分なほど "stable”であると見なされます。

<!--
## Creating Commits And Writing Commit Messages
-->
## コミットとコミットメッセージ

<!--
Follow these guidelines when creating public commits and writing commit messages.
-->
公開されたコミットを行い、コミットメッセージを書く場合は、以下のガイドラインに従ってください。

<!--
1. If your work spans multiple local commits (for example; if you do safe point commits while working in a feature branch or work in a branch for long time doing merges/rebases etc.) then please do not commit it all but rewrite the history by squashing the commits into a single big commit which you write a good commit message for (like discussed in the following sections). For more info read this article: [Git Workflow](https://sandofsky.com/blog/git-workflow.html). Every commit should be able to be used in isolation, cherry picked etc.
2. First line should be a descriptive sentence what the commit is doing. It should be possible to fully understand what the commit does by just reading this single line. It is **not ok** to only list the ticket number, type "minor fix" or similar. Include reference to ticket number, prefixed with #, at the end of the first line. If the commit is a small fix, then you are done. If not, go to 3.
3. Following the single line description should be a blank line followed by an enumerated list with the details of the commit.
4. Add keywords for your commit (depending on the degree of automation we reach, the list may change over time):
    * ``Review by @gituser`` - if you want to notify someone on the team. The others can, and are encouraged to participate.
    * ``Fix/Fixing/Fixes/Close/Closing/Refs #ticket`` - if you want to mark the ticket as fixed in the issue tracker (Assembla understands this).
    * ``backport to _branch name_`` - if the fix needs to be cherry-picked to another branch (like 2.9.x, 2.10.x, etc)
-->
1. 複数のローカルコミットにまたがって作業している (例えば、フィーチャーブランチでの作業中に安全な区切りとして何度かコミットしたり、またはあるブランチにおいて何度かマージ/リベースを行いながら長い間作業している) 場合は、これらを全てコミットするのではなく、ひとつの大きなコミットに squash して履歴を書き換えた上で、このコミットにふさわしい (以下の段落で説明されているような) コミットメッセージを書いてコミットしてください。より詳しくは、この記事: [Git Workflow](https://sandofsky.com/blog/git-workflow.html) を読んでください。すべてのコミットは独立して cherry-pick やその他に利用できるべきです。
2. 最初の行は、そのコミットが何をしているかわかりやすい文章であるべきです。この一行を読むだけで、そのコミットが何をするのかを完全に理解できなければなりません。チケット番号のリストや、"小規模な修正" 、またはこれに似たメッセージをタイプするだけでは **十分ではありません** 。
最初の行の末尾に、＃ で始まるチケット番号への参照を含めてください。そのコミットが小規模な修正であれば、これで完了です。そうでない場合は、3 に進みます。
3. 一行目の説明の後は、その後にコミットの詳細一覧が列挙される空行であるべきです。
4. コミットに関するキーワードを追加します (自動化されている度合に応じて、このリストは都度変更される場合があります):
    * ``Review by @gituser`` - チームの誰かに通知したい場合。指名したひと以外もレビューできますし、参加することが推奨されます。
    * ``Fix/Fixing/Fixes/Close/Closing/Refs #ticket`` - issue トラッカー内のチケットを修正済みにしたい場合 (Assembla がこれを理解する場合) 。
    * ``backport to _branch name_`` - この修正が (2.9.x, 2.10.x, その他のような) 別のブランチに cherry-pick される必要がある場合。

<!--
Example:
-->
例:

<!--
    Adding monadic API to Future. Fixes #2731

      * Details 1
      * Details 2
      * Details 3
-->
    機能に API をひとつ追加。Fixes #2731

