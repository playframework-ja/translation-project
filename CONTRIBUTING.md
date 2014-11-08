# 翻訳ガイドライン

翻訳にご協力頂く際は、以下のガイドラインを参考にしてください。

軽微な修正や改善などの場合は、必ずしも以下のガイドラインを遵守する必要はありません。お気軽にご協力ください。


## 作業の流れ

翻訳作業全体の流れは以下の通りです。

1. 作業環境を準備する
1. issue と担当を確認する
2. 翻訳する
3. コミットする
4. pull request を送る


## 作業環境を準備する

作業には GitHub アカウントが必要です。まだアカウントを持っていない場合は [サインアップ](https://github.com/join) してください。

軽微な修正や改善など、一時的にご協力頂く場合は、[GitHub のオンラインエディタで編集する](https://help.github.com/articles/editing-files-in-another-user-s-repository/) と fork と pull request が自動的に行われるので便利です。

継続的にご協力頂ける場合は [リポジトリを fork](https://help.github.com/articles/fork-a-repo/) してください。


## issue と担当を確認する

他の翻訳者と作業が衝突しないよう、作業に取り掛かる前に [issues](https://github.com/playframework-ja/translation-project/issues) を確認してください。作業対象ファイル名で [issues を検索する](https://help.github.com/articles/searching-issues/) と効率的です。

- issue が登録されていない場合
  - 新しく issue を登録してください
    - タイトルまたは本文に対象ドキュメントへのパスを `2.3.x/manual/Home.md` のように記入してください
    - Label や Milestones, assignee を設定する必要はありません
- issue は登録されているが、誰も着手していない場合
  - コメント欄にて作業に着手する旨を宣言してください。内容はどんなものでも構いません
  - playframework-ja のメンバーは、自分を assignee に設定してください
- issue が登録されていて、誰か着手している場合
  - コメント欄にて担当者とコミュニケーションを取り、作業を分担するか、残作業を引き継いでください


## 翻訳する

以下のルールに従って翻訳してください。


### 原文のコメントアウト

以下のように、原文をコメントアウトして翻訳文を追記します。

```
<!--
# Installing Play
-->
# Play のインストール
```

翻訳後の diff を確認し易くするために、コメントの前後は改行してください。以下は悪い例です。

```
<!-- # Installing Play -->
# Play のインストール
```

原文と翻訳文の対応を明確にするために、翻訳文はコメントアウトの直後に空行を含めないで追記してください。以下は悪い例です。

```
<!--
# Installing Play
-->

# Play のインストール

<!--
## Prerequisites
-->

## 前提条件
```

複数行をまとめてコメントアウトすると翻訳漏れが発生し易くなるので、空行を含めない単位でコメントアウトしてください。以下は悪い例です。

```
<!--
# Installing Play

## Prerequisites
-->
# Play のインストール

## 前提条件
```

空行を含まない場合は、以下のように複数行まとめてコメントアウトして構いません (markdown 記法として成立しなくなるため、まとめてコメントアウトするしかない場合がほとんどです) 。

```
<!--
1. [[Play for Scala developers|ScalaHome]]
1. [[Play for Java developers|JavaHome]]
-->
1. [[Scala 開発者のための Play|ScalaHome]]
1. [[Java 開発者のための Play|JavaHome]]
```

不要なレイアウト崩れを防ぐために、翻訳文が長くなった場合でも、原文が一行であれば改行しないでください。以下は悪い例です。

```
<!--
Since 2007, we have been working on making Java web application development easier. Play started as an internal project at [Zenexity](http://www.zenexity.com) and was heavily influenced by our way of doing web projects: focusing on developer productivity, respecting web architecture, and using a fresh approach to packaging conventions from the start - breaking so-called JEE best practices where it made sense.
-->
2007 年以来、私たちは Java での web アプリケーション の開発を容易なものにしようとしてきました。
Play は、 [Zenexity](http://www.zenexity.com) における内部的なプロジェクトとしてスタートし、私たちの web プロジェクトの進め方に強く影響されてきました。
つまり、開発者の生産性に焦点を当て、 web のアーキテクチャを尊重し、初めからパッケージング規約に対して斬新なやり方を採用してきたのです - そうすることが理にかなっている場合には、いわゆる JEE のベストプラクティスをも破ってきました。
```


### 全角文字と半角文字

- 英数字と記号は原則として半角で記述してください
- カタカナは全角で記述してください
- 全角文字と半角文字の間に半角スペースをひとつ挿入してください。


### 語調の統一

- 語尾は「〜です。」「〜ます。」等の、いわゆる「ですます調」で結んでください。「〜だ。」「〜である。」の等の、いわゆる「である調」にしないでください。


### 訳語の統一

- 固有名詞や専門的な用語を無理に翻訳する必要はありません
- 表記揺れで迷った場合は、Google 検索結果数などを目安にしてください
  - 例: repository
    - リポジトリ: 約 2,030,000 件
    - レポジトリ: 約 363,000 件


### 原文の誤りについて

原文の誤りを発見した場合は、以下の手順に従って対応してください。

1. [英語版ドキュメントの最新版](https://github.com/playframework/playframework/tree/master/documentation/manual) を確認して、発見した誤りが修正されていないことを確認する
2. 英語版ドキュメントを修正して pull request を送る
3. pull request がマージされたことを確認する
4. 翻訳文に修正を反映する

状況に応じて上記の手順が多少前後しても構いませんが、翻訳文だけが改善されて、英語版ドキュメントにフィードバックされない状況は避けてください。


### 意訳、訳註について

原則として、原文に存在しない情報は付け加えないでください。

英語版ドキュメントを改善することで翻訳文が読み易くなる場合は、原文の誤りを発見した場合と同様の手順で英語版ドキュメントを改善し、それから翻訳文に反映してください。

英語版ドキュメントに改善の余地はなく、意訳または訳註を行うことで翻訳版の可読性が著しく向上する場合にのみ、意訳または訳註を行い、コミットコメントにその旨を追記してください。


## コミット

### コミット単位

レビューを行い易くするために、論理的にまとまりのある単位でコミットしてください。例えば、typo の修正と翻訳文の改善を行う場合は、それぞれごとにコミットしてください。

コミットコメントがひとつの文で簡潔に記述できる単位を目安にするとよいでしょう。


### コミットコメント

`#999` のようにして、作業対象の issue 番号をコミットコメントに含めてください。

その他、作業内容を日本語または英語で簡潔に記述してください。[絵文字](http://www.emoji-cheat-sheet.com/) も積極的に使ってください。


## Pull Request

作業が完了したら [pull request](https://help.github.com/articles/creating-a-pull-request/) を送ってください。マージを行い易くするため、基本的に 1 つの issue に対して pull request を 1 つ作成してください。

翻訳に自信のない箇所など、特にレビューや議論が必要な場合は、[pull request の diff にコメント](https://help.github.com/articles/commenting-on-the-diff-of-a-pull-request/) してください。
