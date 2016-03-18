<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Improving Compilation Times
-->
# コンパイル時間の改善

<!--
Compilation speed can be improved by following some guidelines that are also good engineering practice:
-->
良いエンジニアリグ習慣でもある以下のガイドラインに従うことで、コンパイル速度を改善することができます:

<!--
## Use subprojects/modularize
-->
## サブプロジェクト/モジュールを使う

<!--
This is something like bulkheads for incremental compilation in addition to the other benefits of modularization. It minimizes the size of cycles, makes inter-dependencies explicit, and allows you to work with a subset of the code when desired. It also allows sbt to compile independent modules in parallel.
-->
これは、モジュール化から得られるその他の恩恵に追加される、差分コンパイルに関する圧力壁のようなものです。これにより循環のサイズが小さくなり、依存関係が明確になり、必要な場合はコードのサブセットについて作業することができるようになります。また、sbt が独立したモジュールを並列にコンパイルすることもできるようになります。

<!--
## Annotate return types of public methods
-->
## public メソッドの戻り値を注釈する

<!--
This makes compilation faster as it reduces the need for type inference and for accuracy helps address corner cases in incremental compilation arising from inference across source file boundaries.
-->
型推論、およびソースファイルをまたがる推論から生じる差分コンパイルのコーナーケースにおける正確性を減らすことで、コンパイルを速くすることができます。

<!--
## Avoid large cycles between source files
-->
## ソースファイル間の大きな循環を避ける

<!--
Cycles tend to result in larger recompilations and/or more steps.  In sbt 0.13.0+ (Play 2.2+), this is less of a problem.
-->
循環は、巨大な再コンパイルと同じか、またはそれ以上の手順をもたらす傾向があります。sbt 0.13.0 (Play 2.2) 以上では、以前ほど問題にはなりません。

<!--
## Minimize inheritance
-->
## 継承を最小化する

<!--
A public API change in a source file typically requires recompiling all descendents.
-->
ソースファイル内の公開 API を変更すると、通常はすべての子孫クラスの再コンパイルが必要になります。