<!-- translated -->
<!--
# Introducing Play 2.0
-->
# Play 2.0 の紹介

<!--
Since 2007, we have been working on making Java web application development easier. Play started as an internal project at [[Zenexity|http://www.zenexity.com]] and was heavily influenced by our way of doing web projects: focusing on developer productivity, respecting web architecture, and using a fresh approach to packaging conventions from the start - breaking so-called JEE best practices where it made sense.
-->
2007 年以来、私たちは Java での web アプリケーション の開発を容易なものにしようとしてきました。Play は、 [[Zenexity|http://www.zenexity.com]] における内部的なプロジェクトとしてスタートし、私たちの web プロジェクトの進め方に強く影響されてきました。つまり、開発者の生産性に焦点を当て、 web のアーキテクチャを尊重し、初めからパッケージング規約に対して斬新なやり方を採用してきたのです - そうすることが理にかなっている場合には、いわゆる JEE のベストプラクティスをも破ってきました。

<!--
In 2009, we decided to share these ideas with the community as an open source project. The immediate feedback was extremely positive and the project gained a lot of traction. Today - after two years of active development - Play has several versions, an active community of 4,000 people, with a growing number of applications running in production all over the globe.
-->
2009 年に、私たちはこれらのアイデアを、オープンソースプロジェクトとしてコミュニティと共有することを決断しました。即座に返されたフィードバックは極めてポジティブなものであり、このプロジェクトは大きな関心を引きつけました。今日 - 2 年間の活発な開発を経て - Play にはいくつかのバージョンができ、4,000 人の参加者からなる活発なコミュニティが存在し、世界中で実際に使われているアプリケーションの数は増え続けています。

<!--
Opening a project to the world certainly means more feedback, but it also means discovering and learning about new use cases, requiring features and un-earthing bugs that we were not specifically considered in the original design and its assumptions. During the two years of work on Play as an open source project we have worked to fix this kind of issues, as well as to integrate new features to support a wider range of scenarios. As the project has grown, we have learned a lot from our community and from our own experience - using Play in more and more complex and varied projects.
-->
世界中に対してプロジェクトを解放するということは、確かにより多くのフィードバックを得られるということではありますが、それはまた、新たなユースケースに出会ってそこから学ぶことや、新たな機能が必要になることや、 元々の設計や前提の下では考慮されていなかったバグが明らかになることでもあります。オープンソースプロジェクトとして Play に取り組んできた 2 年の間に、私たちはこういったすべての問題を修復し、加えて広範囲なシナリオをサポートするための新たな機能を統合してきました。 Play のプロジェクトが成長するにつれて、私たちは Play のコミュニティと、私たち自身の経験から学びました - Play は、どんどん複雑で、多様なプロジェクトで使われるようになってきたのです。

<!--
Meanwhile, technology and the web have continued to evolve. The web has become the central point of all applications. HTML, CSS and JavaScript technologies have evolved quickly - making it almost impossible for a server-side framework to keep up. The whole web architecture is fast moving towards real-time processing, and the emerging requirements of today’s project profiles mean SQL no longer works as the exclusive datastore technology. At the programming language level we’ve witnessed some monumental changes with several JVM languages, including Scala, gaining popularity.
-->
一方、技術と web の進化の歩みはとどまることを知りません。web は、あらゆるアプリケーションの中心となりました。 HTML, CSS, JavaScript の技術は、急速に発展してきましたーサーバーサイドのフレームワークがついていくことはほとんど不可能なほどです。web のアーキテクチャは、総体として、リアルタイム処理の方向へ急速に向かっており、今日のプロジェクト群に求められるようになった事項からは、データストア技術として SQL を唯一のものとするわけにはいかなくなっていることが分かります。プログラミング言語のレベルにおいては、私たちは、一般的になってきた Scala を含むいくつかの JVM 言語に関連する、後々まで記憶されるような変化の目撃者となりました。

<!--
That’s why we created Play 2.0, a new web framework for a new era.
-->
それこそが、新時代の web フレームワーク、 Play 2.0 を開発した理由です。

<!--
## Built for asynchronous programming
-->
## 非同期プログラミングの構築

<!--
Today’s web applications are integrating more concurrent real-time data, so web frameworks need to support a full asynchronous HTTP programming model. Play was initially designed to handle classic web applications with many short-lived requests. But now, the event model is the way to go for persistent connections - though Comet, long-polling and WebSockets.
-->
今日の web アプリケーションは、これまで以上にリアルタイムデータの並行処理を統合するようになってきており、 web フレームワークには完全な非同期 HTTP プログラミングモデルをサポートすることが求められます。 Play はまず、短期間に処理される大量のリクエストを処理する、クラシックな web アプリケーションを扱うように設計されました。しかし今日では、Comet、長期間のポーリング、WebSockets を通じて、接続が保持され続けるコネクションを処理するため、イベントモデルへと進むべきです。

<!--
Play 2.0 is architected from the start under the assumption that every request is potentially long-lived. But that’s not all: we also need a powerful way to schedule and run long-running tasks. The Actor-based model is unquestionably the best model today to handle highly concurrent systems, and the best implementation of that model available for both Java and Scala is Akka - so it’s going in. Play 2.0 provides native Akka support for Play applications, making it possible to write highly-distributed systems.
-->
Play 2.0 は、最初からすべてのリクエストが潜在的に長期間保持されるものと見なして設計されています。しかしそれだけではなく、私たちには、長時間にわたって処理されるタスクのスケジューリングと実行を扱う、強力な方法も必要です。 今日、並列度が非常に高いシステムを扱うモデルとしては、Actor ベースのモデルが最良であること、そして Java と Scala の双方で利用可能な Actor ベースのモデルの実装として、Akka が最良のものであることは、疑問の余地がありません - これが、Akka を使う理由です。Play 2.0 は Play アプリケーションで Akka をネイティブにサポートし、高度な分散システムを書くことができるようにします。

<!--
## Focused on type safety
-->
## 型安全性へのフォーカス

<!--
One benefit of using a statically-typed programming language for writing Play applications is that the compiler can check parts of your code. This is not only useful for detecting mistakes early in the development process, but it also makes it a lot easier to work on large projects with a lot developers involved.
-->
Play のアプリケーションを書くための言語として 静的型付け言語を使う利点の一つは、コンパイラがコードのある部分をチェックできるという点にあります。これは、開発プロセスの早期にミスを検出するのに有効であるのみならず、多くの開発者が参加する大規模なプロジェクトでの作業をとても容易にしてくれます。

<!--
Adding Scala to the mix for Play 2.0, we clearly benefit from even stronger compiler guarantees - but that’s not enough. In Play 1.x, the template system was dynamic, based on the Groovy language, and the compiler couldn’t do much for you. As a result, errors in templates could only be detected at run-time. The same goes for verification of glue code with controllers.
-->
Play 2.0 の中に Scala を追加することで、私たちは間違いなく、コンパイラによるさらに強力な保障という利点を得ることになります - しかし、それでもまだ十分ではありません。Play 1.x では、テンプレートシステムは動的なものであり、 Groovy に基づくもので、コンパイラにできることはそれほどありませんでした。その結果、テンプレートで発生するエラーは、実行時にしか検出できなかったのです。これは、コントローラとの間を取り持つコードの検証についても同じことが言えました。

<!--
In version 2.0, we really wanted to push this idea of having Play check most of your code at compilation time further. This is why we decided to use the Scala-based template engine as the default for Play applications - even for developers using Java as the main programming language. This doesn’t mean that you have to become a Scala expert to write templates in Play 2.0, just as you were not really required to know Groovy to write templates in Play 1.x.
-->
私たちは Play 2.0 において、コードのほとんどをコンパイル時にチェックさせるという考え方をさらに推し進めたいと強く考えています。そのため、私たちは Play のアプリケーションのデフォルトとして、 Scala ベースのテンプレートエンジンを使うことに決めました - これは、Java をメインのプログラミング言語として使う開発者にとっても、です。ただしだからといって、Play 1.x でテンプレートを書くために、Groovy を本当に知っていることが必要だったわけではないのと同様に、Scala のエキスパートにならなければ Play 2.0 でテンプレートを書くことができないということではありません。

<!--
In templates, Scala is mainly used to navigate your object graph in order to display relevant information, with a syntax that is very close to Java’s. However, if you want to unleash the power of Scala to write advanced templates abstractions, you will quickly discover how Scala, being expression-oriented and functional, is a perfect fit for a template engine.
-->
Scala が主に使われるのは、Java のシンタックスに極めて近いシンタックスを使って、必要な情報を表示するのにオブジェクトグラフをたどっていくためです。とはいえ、Scala の持つパワーを生かして高度に抽象化されたテンプレートを書きたいなら、式指向で関数型である Scala が、どれほどテンプレートエンジンにぴったりなのかは、すぐに理解できることでしょう。

<!--
And that’s not only true for the template engine: the routing system is also fully type-checked. Play 2.0 checks your routes’ descriptions, and verifies that everything is consistent, including the reverse routing part.
-->
そして、これはテンプレートエンジンにだけ言えることではありません。ルーティングのシステムもまた、完全に型が検査されることになります。Play 2.0 は、ルートに関するすべての記述をチェックし、リバースルートの部分も含めて、すべてにおいて整合性が保たれているかどうかを検証します。

<!--
A nice side effect of being fully compiled is that the templates and route files will be easier to package and reuse. You also get a significant performance gain on these parts at run-time.
-->
完全にコンパイルが行われることの嬉しい副作用として、テンプレートとルートファイルのパッケージ化と再利用が容易になることと、これらの部分の実行時のパフォーマンスの大幅な向上が見込めるということもあります。

<!--
## Native support for Java and Scala
-->
## Java 及び Scala のネイティブサポート

<!--
Early in the Play project’s history, we started exploring the possibility of using the Scala programming language for writing Play applications. We initially introduced this work as an external module, to be able to experiment freely without impacting the framework itself.
-->
Play 1.1 から、Play のアプリケーションを書くのにプログラミング言語 Scala を使用する可能性を、私たちは探り始めました。この作業は、まずフレームワークそのものに影響を与えることなく、自由に試せるような外部モジュールとして導入されました。

<!--
Properly integrating Scala into a Java-based framework is not trivial. Considering Scala’s compatibility with Java, one can quickly achieve a first naive integration that simply uses Scala’s syntax instead of Java’s. This, however, is certainly not the optimal way of using the language. Scala is a mix of true object orientation with functional programming. Leveraging the full power of Scala requires rethinking most of the framework’s APIs.
-->
Scala を適切に Java ベースのフレームワークに統合することは容易なことではありません。Scala が持つ Java との互換性を考慮すれば、単純に Scala のシンタックスを Java のシンタックスの代わりに使う形で、まず単純に素早く統合してしまうことは可能です。しかしこれは間違いなく、Scala を利用する上で最適な方法ではありません。 Scala は、真のオブジェクト指向と関数型プログラミングを混合したものです。Scala の本当のパワーを解放するには、Play のフレームワークの API の多くを再検討しなければなりません。

<!--
We quickly reached the limits of what we can do with Scala support as a separate module. Initial design choices we made in Play 1.x, relying heavily on Java reflection API and byte code manipulation, have made it harder to progress without completely rethinking some essential parts of Play’s internals. Meanwhile, we have created several awesome components for the Scala module, such as the new type-safe template engine and the brand new SQL access component Anorm. This is why we decided that, to fully unleash the power of Scala with Play, we would move Scala support from a separate module to the core of Play 2.0, which is designed from the beginning to natively support Scala as a programming language.
-->
今では、私たちは個別のモジュールとして Scala をサポートするやり方の限界点に到達しています。私たちが Play 1.x で行った、初期の設計における選択は、Java のリフレクションAPIとバイトコードの操作に強く依存しており、Play の内部の重要な部分のいくつかについて完全に再検討し直さなければ、これ以上の進歩は難しくなっていました。一方で、私たちは Scala モジュールのために、新たな型安全テンプレートエンジンや、まったく新しい SQL アクセスコンポーネントである Anorm といった、複数の素晴らしいコンポーネントを作成していました。そこで私たちは、 Scala の持つパワーを Play で完全に解放するために、Scala のサポートを個別のモジュールから、Play 2.0 のコアへ移すことを決断しました。この Play 2.0 のコアは、初めからプログラミング言語として Scala をネイティブにサポートするよう設計されることになります。

<!--
Java, on the other hand, is certainly not getting any less support from Play 2.0; quite the contrary. The Play 2.0 build provides us with an opportunity to enhance the development experience for Java developers. Java developers get a real Java API written with all the Java specificity in mind.
-->
一方で、Java に対するサポートが Play 2.0 から弱くなることはまったくありません。むしろ、完全にその反対なのです。 Play 2.0 のビルドは、Java の開発者に対し、開発の体験を拡張する機会を提供します。

<!--
## Powerful build system
-->
## 強力なビルドシステム

<!--
From the beginning of the Play project, we have chosen a fresh way to run, compile and deploy Play applications. It may have looked like an esoteric design at first, but it was crucial to providing an asynchronous HTTP API instead of the standard Servlet API, short feedback cycles through live compilation and reloading of source code during development, and promoting a fresh packaging approach. Consequently, it was difficult to make Play follow the standard JEE conventions.
-->
私たちは初めから、Play のアプリケーションの実行、コンパイル、デプロイについて、斬新な方法を選択してきました。当初、私たちの採った方法は、難解な設計に見えたかも知れません - しかし、標準的な Servlet API の代わりに非同期 HTTP API を提供し、ライブコンパイルと開発中のソースコードのリロードによって短いフィードバックサイクルを提供し、斬新なパッケージングのアプローチを推進することは、極めて重要なことだったのです。その結果として、Play が標準的な JEE の規約に従うことは難しくなりました。

<!--
Today, this idea of container-less deployment is increasingly accepted in the Java world. It’s a design choice that has allowed the Play framework to run natively on platforms like Heroku, which introduced a model that we consider the future of Java application deployment on elastic PaaS platforms.
-->
今日では、コンテナレスデプロイメントの概念は、Javaの世界において非常に広く受け入れられるようになってきました。この設計上の選択によって、Play framework は Heroku のようなプラットフォームにおいてネイティブに動作できるようになりました。私たちは、Heroku によって紹介されたモデルは、エラスティックな PaaS プラットフォームにおける Java アプリケーションのデプロイメントの未来だと考えています。

<!--
Existing Java build systems, however, were not flexible enough to support this new approach. Since we wanted to provide straightforward tools to run and deploy Play applications, in Play 1.x we created a collection of Python scripts to handle build and deployment tasks.
-->
一方で、既存の Java のビルドシステムは、この新しいアプローチをサポートするには、柔軟性が不足していました。私たちは、Play のアプリケーションを実行し、デプロイするための単純明快なツールを提供したいと考えていたことから、Play 1.x では ビルドとデプロイメントのタスクのすべてを処理するために、Python スクリプトの集合体を作り上げました。

<!--
Meanwhile, developers using Play for more enterprise-scale projects, which require build process customization and integration with their existing company build systems, were a bit lost. The Python scripts we provide with Play 1.x are in no way a fully-featured build system and are not easily customizable. That’s why we’ve decided to go for a more powerful build system for Play 2.0.
-->
しかし、ビルドのプロセスのカスタマイズや、企業内の既存のビルドシステムとの統合が求められる、よりエンタープライズ規模のプロジェクトで Play を使っている開発者の方々は、少々困っていました。私たちが Play 1.x で提供していた Python のスクリプト群は、完全な機能を完備したビルドシステムではまったくありませんでしたし、カスタマイズも容易ではありませんでした。これが、私たちが Play 2.0 でさらに強力なビルドシステムへ舵を切ることを決めた理由です。

<!--
Since we need a modern build tool, flexible enough to support Play original conventions and able to build Java and Scala projects, we have chosen to integrate sbt in Play 2.0. This, however, should not scare existing Play users who are happy with the simplicity of the original Play build. We are leveraging the same simple `play new`, `run`, `start` experience on top of an extensible model: Play 2.0 comes with a preconfigured build script that will just work for most users. On the other hand, if you need to change the way your application is built and deployed, the fact that a Play project is a standard sbt project gives you all the power you need to customize and adapt it.
-->
Play 独自の規約をサポートでき、Java と Scala のプロジェクトをビルドできるだけの柔軟性を持った、現代的なビルドツールが必要だったことから、私たちは SBT を Play 2.0 に統合することにしました。ただしこれによって、既存の Play のビルドのシンプルさに満足しているユーザーが脅かされることがあってはなりません。私たちは、拡張性のあるモデルの上で、これまで同様のシンプルな `play new`, `run`, `start` が体験できるようにしようとしています。 Play 2.0 は、ほとんどのユーザーにとっては、とにかく単純にうまく処理をこなしてくれる、設定済みのビルドスクリプトを持つことになります。一方で、アプリケーションのビルドやデプロイの方法を変更する必要がある場合は、 Play のプロジェクトは標準的な SBT プロジェクトになるので、カスタマイズや特殊な要求への適用に応えるだけのあらゆるパワーを活用できるのです。

<!--
This also means better integration with Maven projects out of the box, the ability to package and publish your project as a simple set of JAR files to any repository, and especially live compiling and reloading at development time of any depended project, even for standard Java or Scala library projects.
-->
これはまた、Play 2.0 はインストール直後から、Maven との統合がこれまでよりもうまくできているということでもあり、プロジェクトをシンプルな jar ファイルの集合体としてパッケージ化し、任意のリポジトリへ公開できるということでもあり、さらには依存しているいかなる標準的な Java あるいは Scala ライブラリが開発中の状態であっても、ライブコンパイルやリローディングが可能だということでもあります。

<!--
## Datastore and model integration
-->
## データストアとモデルの統合

<!--
‘Data store’ is no longer synonymous with ‘SQL database’, and probably never was. A lot of interesting data storage models are becoming popular, providing different properties for different scenarios. For this reason it has become difficult for a web framework like Play to make bold assumptions regarding the kind of data store that developers will use. A generic model concept in Play no longer makes sense, since it is almost impossible to abstract over all these kinds of technologies with a single API.
-->
データストアは、もはや「SQL データベース」の同義語ではありませんし、おそらくはこれまでもそうではありませんでした。データストアの興味深いモデルは、数多くのものが広く使われるようになり、様々なシナリオにおいて、様々な特徴が提供されてきました。そのため、Play のような web フレームワークにとっては、開発者がどのようなデータストアを利用するのか、明確な推測をすることが難しくなってきたのです。Play における汎用モデルの考え方は、単一の API でこういった技術のすべてを抽象化するのはほとんど不可能である以上、もはや意味を成さないものになってしまっています。

<!--
In Play 2.0, we wanted to make it really easy to use any data store driver, ORM, or any other database access library without any special integration with the web framework. We simply want to offer a minimal set of helpers to handle common technical issues, like managing the connection bounds. We also want, however, to maintain the full-stack aspect of Play framework by bundling default tools to access classical databases for users WHO don’t have specialized needs, and that’s why Play 2.0 comes with built-in relational database access libraries such as Ebean, JPA and Anorm.
-->
私たちは Play 2.0 で、どのようなデータストアドライバ、ORM あるいはその他のデータベースアクセスライブラリも、特にこのフレームワークに統合することなく、容易に利用できるようにしたいと考えています。私たちは単に、コネクションのバインドの管理のような、一般的な技術的課題を扱うための、最小限のヘルパーを提供するようにしたいのです。とはいえ、私たちはまた、特殊な要求を持たないユーザーがクラシックなデータベースへアクセスするためのデフォルトのツールをバンドルすることで、 Play フレームワークのフルスタックという性格も保ち続けたいと思っています。それこそが、 Play 2.0 が Ebean, JPA, Anorm といったビルドインのリレーショナルデータベースアクセスライブラリを同梱している理由です。
