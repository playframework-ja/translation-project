<!-- translated -->
<!--
# Installing Play
-->
# Play のインストール

<!--
## Prerequisites
-->
## 前提条件
<!--
To run the Play framework, you need [[JDK 6 or later| http://www.oracle.com/technetwork/java/javase/downloads/index.html]]. 
-->
Play framework の実行には、[[JDK 6以降|http://www.oracle.com/technetwork/java/javase/downloads/index.html]] が必要です。

<!--
> If you are using MacOS, Java is built-in. If you are using Linux, make sure to use either the Sun JDK or OpenJDK (and not gcj, which is the default Java command on many Linux distros). If you are using Windows, just download and install the latest JDK package.

> Note, Java 7 pre update 9 on MacOS has a bug that causes problems with futures and iteratees, including making large file uploads hang.  If using Java 7 on MacOS, make sure you are using the latest version.
-->
> MacOS をお使いであれば、Java はプリインストールされています。Linux をお使いの場合は、Sun JDK か OpenJDK をインストール (多くの Linux ディストリビューションで標準になっている gcj ではなく) してください。Windows をお使いの場合、最新の JDK パッケージをダウンロード・インストールしてください。

> MacOS の Java7 pre update 9 には、futures と iteratees において大きいファイルのアップロードがハングするという問題を引き起こすバグがあります。MacOS で Java 7 を使用する場合は、最新バージョンを使用していることを確認してください。

<!--
Be sure to have the `java` and `javac` commands in the current path (you can check this by typing `java -version` and `javac -version` at the shell prompt). 
-->
カレントパスで `java` と `javac` コマンドが利用できることを確認してください。(これはプロンプトで `java -version` と `javac -version` と入力することでチェックできます。)

<!--
## Download the binary package
-->
## バイナリパッケージのダウンロード

<!--
Download the latest [[Play binary package | http://www.playframework.org/download]] (take the latest official version) and extract the archive to a location where you have both read **and write** access. (Running `play` writes some files to directories within the archive, so don't install to `/opt`, `/usr/local` or anywhere else you’d need special permission to write to.)
-->
[[Play バイナリパッケージ|http://download.playframework.org/download/]](公式の最新バージョン) をダウンロードし、書き込み・読み込み権限がある場所に展開してください。（Play の実行中ファイルがアーカイブ内のディレクトリに書き込まれるので、`/opt` にインストールするのではなく、`/usr/local` か書き込み権限のある場所にインストールしてください）

<!--
## Add the play script to your PATH
-->
## play スクリプトへパスを通す

<!--
For convenience, you should add the framework installation directory to your system PATH. On UNIX systems, this means doing something like:
-->
利便性のため、Play のインストール先ディレクトリを PATH 環境変数に追加しておきましょう。例えば、Unix 系の OS をお使いの場合は、次のように設定します:

```bash
export PATH=$PATH:/relativePath/to/play
```

<!--
On Windows you’ll need to set it in the global environment variables. This means update the PATH in the environment variables and don't use a path with spaces.
-->
Windows では、システム環境変数を設定する必要があります。これは環境変数で設定されているパスを更新することを意味し、スペースを含むパスを使わないようにしてください。

<!--
> If you’re on UNIX, make sure that the `play` script is executable (otherwise do a `chmod a+x play`).

> If you behind a proxy make sure to define it with `set HTTP_PROXY=http://<host>:<port>` on Windows or `export  HTTP_PROXY=http://<host>:<port>` on UNIX.
-->
> UNIX 系の OS をお使いのときは、 play スクリプトが実行可能になっていることを確認してください (そうなっていなければ、 `chmod a+x play` を実行してください)

> プロキシの背後にいる場合は、Windows であれば `set HTTP_PROXY=http://<host>:<port>` として、UNIX であれば `export  HTTP_PROXY=http://<host>:<port>` として、プロキシを定義していることを確認してください。

<!--
## Check that the play command is available
-->
## play コマンドの起動確認

<!--
From a shell, launch the `play help` command. 

```bash
$ play help
```
-->
シェルで `play help` コマンドを実行しましょう。

```bash
$ play help
```

<!--
If everything is properly installed, you should see the basic help:
-->
インストールが正しく行えていれば、ヘルプが表示されるはずです。

[[images/play.png]]

<!--
> **Next:** [[Creating a new application | NewApplication]]
-->
> **Next:** [[新規アプリケーションを作成する | NewApplication]]