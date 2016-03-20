<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Play Modules
-->
# Playモジュール

<!--
At its core, Play is a very lightweight HTTP server, providing mechanisms for serving HTTP requests, but not much else. Additional functionality in Play is provided through the use of Play modules.
-->
Play のコアはとても軽量な HTTP サーバで HTTP リクエストを扱える機能を提供していますが、それだけではありません。Play では追加の機能を Play モジュールを利用することで提供できます。


<!--
## What is a module?
-->
## モジュールとは?
<!--
There is no strict definition in Play of what a module is or isn't - a module could be just a library that provides some helper methods to help you do something, or it could be a full framework providing complex functionality such as user management. Some modules are built in to Play, others are written and maintained by members of the Play community.
-->
Play では何がモジュールで何がモジュールでないかという厳格な定義はありません。モジュールは何かの手助けになるいくつかのヘルパーメソッドを提供するだけのただのライブラリになりますし、ユーザ管理のような複雑な機能を提供する重厚なフレームワークにもなります。いくつかのモジュールは Play に組み込まれており、その他は Play のコミュニティメンバーにより作成、メンテナンスされています。

<!--
Some modules provide components - objects that represent resources, for example a database connection.  These objects may have a lifecycle and need to be started and stopped when the application starts and stops, and they may hold some state such as a cache. Play provides a variety of mechanisms for accessing and using these components. Components are not only provided by modules, they may be provided by the application themselves.
-->
いくつかのモジュールはコンポーネントを提供しています。例えばデータベース接続などのオブジェクトはリソースを表しています。これらのオブジェクトはライフサイクルをもっているかもしれないし、アプリケーションが開始されたときや停止したときに開始や停止をする必要があるかもしれないですし、キャッシュのような状態を保持するかもしれません。Play はこれらのコンポーネントにアクセスしたり利用したりする様々な機構を提供しています。

<!--
## Accessing modules
-->
## モジュールにアクセスする

<!--
One of the earliest decisions that you need to make when starting a new Play project is how you will access the components provided by modules. Components are accessed through the use of a dependency injection mechanism, where rather than having your components look up other components in the system, your components declare what other components they need, and the system injects those components into your components.
-->
新しい Play のプロジェクトを始めたときの初期段階に決定する必要があるのはモジュールによって提供されたコンポーネントにどのようにアクセスするかです。コンポーネントは依存性の注入の機構を利用してアクセスされます。これはあなたのコンポーネントからシステムのコンポーネントを参照するよりも、どのコンポーネントを必要としているのか宣言しておけば、システムがそれらのコンポーネントをあなたのコンポーネントに注入します。


<!--
At its core, Play is agnostic to any particular form of dependency injection, however out of the box Play provides and we recommend that you use [Guice](https://github.com/google/guice). The remainder of this documentation will assume that this is the decision that you have made, however there will be examples of how to integrate with other dependency injection mechanisms.
-->
Play のコアはいかなる特定の依存性注入方式にも依存しませんが、展開したての Play は [Guice](https://github.com/google/guice) を提供しており、また、これを利用することを推奨します。以降のこのドキュメントでは Guice を選択したと想定しますが、他の依存性注入機構の例もあります。

<!--
You can read more about dependency injection in [[Scala|ScalaDependencyInjection]] or [[Java|JavaDependencyInjection]].
-->
依存性の注入については [[Scala|ScalaDependencyInjection]] もしくは [[Java|JavaDependencyInjection]] でより詳しく読むことができます。
