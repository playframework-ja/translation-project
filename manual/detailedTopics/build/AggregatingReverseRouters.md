<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Aggregating reverse routers
-->
# リバースルートの統合

<!--
In some situations you want to share reverse routers between sub projects that are not dependent on each other.
-->
いくつかの状況において、互いに依存していないサブプロジェクト間でリバースルートを共有したくなることがあります。

<!--
For example, you might have a `web` sub project, and an `api` sub project.  These sub projects may have no dependence on each other, except that the `web` project wants to render links to the `api` project (for making AJAX calls), while the `api` project wants to render links to the `web` (rendering the web link for a resource in JSON).  In this situation, it would be convenient to use the reverse router, but since these projects don't depend on each other, you can't.
-->
例えば、`web` サブプロジェクトと `api` サブプロジェクト があるとします。`web` プロジェクトは `api` プロジェクトへのリンクを (AJAX 呼び出しのために) レンダリングする一方で、`api` プロジェクトは `web` へのリンク (JSON 内のリソースに対する web リンク) をレンダリングすることを除いては、これらのサブプロジェクトはお互いに依存関係を持ちません。このような場合、リバースルータを使うと便利ですが、これらのプロジェクトはお互いに依存していないため、使えません。

<!--
Play's routes compiler offers a feature that allows a common dependency to generate the reverse routers for projects that depend on it so that the reverse routers can be shared between those projects.  This is configured using the `aggregateReverseRoutes` sbt configuration item, like this:
-->
Play のルートコンパイラには、依存するプロジェクト用にリバースルートを生成する共通の依存性を提供する機能があるので、これらのプロジェクト間でリバースルートを共有することができます。以下のように、`aggregateReverseRoutes` sbt 設定項目を使って設定することができます:

@[content](code/aggregate.sbt)

<!--
In this setup, the reverse routers for `api` and `web` will be generated as part of the `common` project.  Meanwhile, the forwards routers for `api` and `web` will still generate forwards routers, but not reverse routers, because their reverse routers have already been generated in the `common` project which they depend on, so they don't need to generate them.
-->
上記のセットアップでは、`api` と `web` に対するリバースルートは `common` プロジェクトの一部として生成されます。`api` と `web` に対するリバースルータは依存する `common` プロジェクトにて既に生成されているため、生成する必要はなく、`api` と `web` 用のリバースルータではなくフォワードルータが生成されます。

<!--
> Note that the `common` project has a type of `Project` explicitly declared.  This is because there is a recursive reference between it and the `api` and `web` projects, through the `dependsOn` method and `aggregateReverseRoutes` setting, so the Scala type checker needs an explicit type somewhere in the chain of recursion.
-->
> `common` プロジェクトは明示的に `Project` 型と宣言されていることに注意してください。`dependes` メソッドと `aggregateReverseRoutes` 設定によって、`api` および `web` プロジェクト間には再帰的な依存関係があるため、Scala の型チェッカは再帰チェーン内にどこかにおいて、明示的な型を必要とします。