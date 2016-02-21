<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Installing Play
-->
# Play のインストール

<!--
## Prerequisites
-->
## 前提条件

<!--
You need to have a JDK 1.8 (or later) installed on your machine (see [General Installation Tasks](#JDK-installation)).
-->
自身のマシンに JDK1.8 (もしくはそれ以降)をインストールしておく必要があります。(参考：[一般的なインストール手順](#JDKインストール設定))

<!--
## Quick Start
-->
## クイックスタート

<!--
1. **Download** the latest [Typesafe Activator](https://typesafe.com/get-started).
2. **Extract** the archive on a location where you have write access.
3. **Change** dir with cmd `cd activator*` (or with the file-manager)
4. **Start** it with cmd `activator ui` (or with the file-manager)
5. **Access** it at [http://localhost:8888](http://localhost:8888)
-->
1. 最新の [Typesafe Activator](https://typesafe.com/get-started) を **ダウンロードする** 。
2. 書き込みアクセス権を持つ場所にアーカイブを **展開する** 。
3. `cd activator*` コマンド(もしくはファイルマネージャー)でディレクトリを **変更する** 。
4. `activator ui` コマンド(もしくはファイルマネージャー)でActivatorを **開始する** 。
5. [http://localhost:8888](http://localhost:8888)に **アクセスする** 。

<!--
You'll find documentation and a list of application samples which get you going immediately. For a simple start, try the **play-java** sample.
-->
アクセスすると、即座に動かせるサンプルとドキュメントがあります。まずは、 **play-java** サンプルからためしてみましょう。

<!--
### Command Line
-->
### コマンドライン

<!--
To use play from any location on your file-system, add the **activator** directory to your path (see [General Installation Tasks](#Add-Executables-to-Path)).
-->
Play をファイルシステムのどの場所からでも使えるようにするため、 **Activator** のディレクトリをパスに追加します。(参考：[一般的なインストール手順](#パスを通す))

<!--
Creating `my-first-app` based on the `play-java` template is as simple as:
-->
`play-java`  テンプレートをベースに `my-first-app` を作成するのは次のように簡単です。

```bash
activator new my-first-app play-java
cd my-first-app
activator run
```

<!--
[http://localhost:9000](http://localhost:9000) - access your application here.
-->
[http://localhost:9000](http://localhost:9000) - アプリケーションのアクセス先はここです。

<!--
You are now ready to work with Play!
-->
Play で開発をするための準備が整いました！

<!--
## General Installation Tasks
-->
## 一般的なインストール手順

<!--
You may need to deal with those general tasks in order to install Play! on your system.
-->
Play をインストールするためには、これらの一般的なタスクに対処する必要があるかもしれません。

<!--
### JDK installation
-->
### JDKインストール設定

<!--
Verify if you have a JDK (Java Development Kit) Version 1.8 or later on your machine. Simply use those commands to verify:
-->
マシンにバージョン1.8 もしくはそれ以降の JDK (Java Development Kit) がインストールされていることを確認してください。確認するためのシンプルなコマンドは次の通りです。

```bash
java -version
javac -version
```

<!--
If you don't have the JDK, you have to install it:
-->
もし JDK がない場合、インストールする必要があります：

<!--
1. **MacOS**, Java is built-in, but you may have to [Update to the latest](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. **Linux**, use either the latest Oracle JDK or OpenJDK (do not use not gcj).
3. **Windows** just download and install the [latest JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) package.
-->
1. **MacOS** の場合、Java は導入済みですが、[最新版にアップデート](http://www.oracle.com/technetwork/java/javase/downloads/index.html) する必要があるかもしれません。
2. **Linux** の場合、最新の Oracle JDK か OpenJDK を使用してください。( gcj は使用しないでください。)
3. **Windows** の場合、[最新のJDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) を単にダウンロードしてインストールしてください。

<!--
### Add Executables to Path
-->
### パスを通す

<!--
For convenience, you should add the Activator installation directory to your system `PATH`.
-->
利便性のため、Activator に `PATH` を通す方が良いでしょう。

<!--
On **Unix**, use `export PATH=/path/to/activator:$PATH`
-->
**Unix** 上では、 `export PATH=/path/to/activator:$PATH` を使用してください。

<!--
On **Windows**, add `;C:\path\to\activator` to your `PATH` environment variable. Do not use a path with spaces.
-->
**Windows** 上では、 `PATH` 環境変数に `;C:\path\to\activator` を追加してください。パスにスペースを含めてはいけません。

<!--
### File Permissions
-->
### ファイルパーミッション

#### Unix

<!--
Running `activator` writes some files to directories within the distribution, so don't install to `/opt`, `/usr/local` or anywhere else you’d need special permission to write to.
-->
実行中の `activator` はディストリビューションのディレクトリのいくつかのファイルに書き込むので、 `/opt` 、 `/usr/local`  などの特別な書き込み許可が必要な場所にインストールしてはいけません。

<!--
Make sure that the `activator` script is executable. If it's not, do a `chmod u+x /path/to/activator`.
-->
`activator` が実行可能かどうか確認してください。
実行権限がない場合は、 `chmod u+x /path/to/activator` を実行してください。

<!--
### Proxy Setup
-->
### プロキシ設定

<!--
If you're behind a proxy make sure to define it with `set HTTP_PROXY=http://<host>:<port>` on Windows or `export  HTTP_PROXY=http://<host>:<port>` on UNIX.
-->
もしプロキシ内のネットワークにいる場合、 Windows なら `set HTTP_PROXY=http://<host>:<port>` が設定されているか確認し、 UNIX 系システムなら `export  HTTP_PROXY=http://<host>:<port>` が設定されているか確認して下さい。
