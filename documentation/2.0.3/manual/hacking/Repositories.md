<!-- translated -->
<!--
# Artifact repositories

## Typesafe repository

All Play artifacts are published to the Typesafe repository at [[http://repo.typesafe.com/typesafe/releases/]].

> **Note:** it's a Maven2 compatible repository.

To enable it in your sbt build, you must add a proper resolver (typically in `plugins.sbt`):
-->
# アーティファクト・レポジトリ

## Typesafe レポジトリ

全ての Play 関連のアーティファクトは [[http://repo.typesafe.com/typesafe/releases/]] にある Typesafe レポジトリにパブリッシュされています。

> **Note:** このレポジトリは Maven2 形式です。

sbt ビルドでこのレポジトリを有効化したいときは、正しいリゾルバを追加（通常は `plugins.sbt` 内に）する必要があります。


```
// The Typesafe repository
resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
```

<!--
## Accessing snapshots

Snapshots are published daily from our [[Continuous Server|CIServer]] to the Typesafe snapshots repository at [[http://repo.typesafe.com/typesafe/snapshots/]].
-->
## Snapshot を利用する

Snapshot は Play の [[継続的インテグレーションサーバ|CIServer]] から [[http://repo.typesafe.com/typesafe/snapshots/]] にある Typesafe Snapshots レポジトリへ、一日一回パブリッシュされています。

```
// The Typesafe snapshots repository
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
```

