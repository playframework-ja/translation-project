<!--- Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Installing Play
-->
# Play のインストール

<!--
## Prerequisites
-->
## 前提条件

<!--
To run the Play framework, you need [JDK 6 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html). 
-->
Play framework の実行には、[JDK 6以降](http://www.oracle.com/technetwork/java/javase/downloads/index.html) が必要です。

<!--
> If you are using MacOS, Java is built-in. If you are using Linux, make sure to use either the Sun JDK or OpenJDK (and not gcj, which is the default Java command on many Linux distros). If you are using Windows, just download and install the latest JDK package.
-->
> MacOSを使っているなら、Javaは導入済みです。また、Linuxを使っているならSun JDKかOpenJDKのいずれかを使えるか確認しましょう。（またgcjがあるかどうか。これは多くのLinuxのディストロで標準のJavaコマンドです。)そして、Windowsを使っているなら最新のJDKをダウンロードしてインストールしてください。

<!--
Be sure to have the `java` and `javac` commands in the current path (you can check this by typing `java -version` and `javac -version` at the shell prompt). 
-->
カレントパスで `java` と `javac` コマンドが利用できることを確認してください。(これはプロンプトで `java -version` と `javac -version` と入力することでチェックできます。)

<!--
## Install Activator
-->
## Activatorのインストール

<!--
Play is distributed through a tool called [Typesafe Activator](http://typesafe.com/activator).  Typesafe Activator provides the build tool (sbt) that Play is built on, and also provides many templates and tutorials to help get you started with writing new applications.
-->
Playは[Typesafe Activator](http://typesafe.com/activator)と呼ばれるツールを通じて提供されます。Typesafe ActivatorはPlayをビルドする際のビルドツール(sbt)を提供すると共に、あなたが新しいアプリケーションを作りはじめるにあたって助けとなる沢山のテンプレートとチュートリアルを提供します。

<!--
Download the latest [Activator distribution](https://typesafe.com/platform/getstarted) and extract the archive to a location where you have both read **and write** access. (Running `activator` writes some files to directories within the distribution, so don't install to `/opt`, `/usr/local` or anywhere else you’d need special permission to write to.)
-->
最新の[Activator distribution](https://typesafe.com/platform/getstarted)をダウンロードし、圧縮ファイルをあなたが読み**書き**できる領域に解凍します。(`activator`の実行で解凍したディレクトリの中にいくつかのファイルを書き込みます。よって`/opt`や`/usr/local`など、書き込みに特別な権限が必要な場所にインストールしないで下さい。)

<!--
## Add the activator script to your PATH
-->
## activatorにPATHを通す

<!--
For convenience, you should add the Activator installation directory to your system `PATH`. On UNIX systems, this means doing something like:
-->
利便性のため、Activatorに`PATH`を通す方が良いでしょう。UNIX系のシステムではこのように記述します。

```bash
export PATH=$PATH:/relativePath/to/activator
```

<!--
On Windows you’ll need to set it in the global environment variables. This means update the `PATH` in the environment variables and don't use a path with spaces.
-->
Windowsではシステム環境変数に設定する必要があります。これは`PATH`の環境変数を変更することを意味し、そしてパスにスペースを使用してはいけません。

<!--
> If you’re on UNIX, make sure that the `activator` script is executable.
> 
> Otherwise do a:
> ```bash
> chmod a+x activator
> ```
-->
> もし、UNIX系のシステムを使っているなら、`activator`スクリプトが実行可能かどうか、確認して下さい。
> 
> 実行権限か無い場合はこのコマンドを実行して下さい。:
> ```
> chmod a+x activator
> ```

<!--
> If you're behind a proxy make sure to define it with `set HTTP_PROXY=http://<host>:<port>` on Windows or `export  HTTP_PROXY=http://<host>:<port>` on UNIX.
-->
> もしプロキシ内のネットワークに居る場合、Windowsなら`set HTTP_PROXY=http://<host>:<port>`が設定されているか確認し、UNIX系システムなら`export  HTTP_PROXY=http://<host>:<port>`が設定されているか確認して下さい。

## Check that the activator command is available

From a shell, launch the `activator -help` command. 

```bash
$ activator -help
```

<!--
If everything is properly installed, you should see the basic help:
-->
インストールが正しく行えていれば、ヘルプが表示されるはずです。

[[images/activator.png]]

You are now ready to create a new Play application.

<!--
> **Next:** [[Creating a new application | NewApplication]]
-->
> **Next:** [[新規アプリケーションを作成する | NewApplication]]
