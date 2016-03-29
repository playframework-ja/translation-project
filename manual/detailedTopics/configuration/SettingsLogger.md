<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Configuring logging
-->
# ログの設定

<!--
Play uses [Logback](http://logback.qos.ch/) as its logging engine, see the [Logback documentation](http://logback.qos.ch/manual/configuration.html) for details on configuration.
-->
Play がログ出力に使っている [Logback](http://logback.qos.ch/) の詳細な設定は、[Logback のドキュメント](http://logback.qos.ch/manual/configuration.html) を参照してください。

<!--
## Default configuration
-->
## デフォルト設定

<!--
Play uses the following default configuration in production:
-->
Play は本番環境で以下のデフォルト設定を使用します:

@[](/confs/play/logback-play-default.xml)

<!--
A few things to note about this configuration:
-->
この設定には注意すべき点がいくつかあります:

<!--
* This specifies a file appender that writes to `logs/application.log`.
* The file logger logs full exception stack traces, while the console logger only logs 10 lines of an exception stack trace.
* Play uses ANSI color codes by default in level messages.
* Play puts both the console and the file logger behind the logback [AsyncAppender](http://logback.qos.ch/manual/appenders.html#AsyncAppender).  For details on the performance implications on this, see this [blog post](https://blog.takipi.com/how-to-instantly-improve-your-java-logging-with-7-logback-tweaks/).
-->
* `logs/application.log` に出力するファイルアペンダを指定しています。
* コンロールロガーは例外スタックトレースを 10 行しか出力しませんが、ファイルロガーは例外スタックトレースをすべて記録します。
* Play はデフォルトでレベルメッセージに ANSI カラーコードを使います。
* Play はコンソールおよびファイルロガーを logback の [AsyncAppender](http://logback.qos.ch/manual/appenders.html#AsyncAppender) の裏側に配置します。これによる性能面の影響については、この [ブログ記事](https://blog.takipi.com/how-to-instantly-improve-your-java-logging-with-7-logback-tweaks/) を参照してください。

<!--
## Custom configuration
-->
## カスタム設定

<!--
For any custom configuration, you will need to specify your own Logback configuration file.
-->
どのような設定でも、カスタマイズする場合は独自の Logback 設定ファイルを指定する必要があります。

<!--
### Using a configuration file from project source
-->
### プロジェクトソースにある設定ファイルを使う

<!--
You can provide a default logging configuration by providing a file `conf/logback.xml`.
-->
`conf/logback.xml` ファイルを用意することで、デフォルトのログ設定を定めることができます。

<!--
### Using an external configuration file
-->
### 外部設定ファイルを使う

<!--
You can also specify a configuration file via a System property.  This is particularly useful for production environments where the configuration file may be managed outside of your application source.
-->
システムプロパティで設定ファイルを指定することもできます。これは、設定ファイルがアプリケーションソースの外部で管理されるであろう本番環境において特に便利です。

<!--
> Note: The logging system gives top preference to configuration files specified by system properties, secondly to files in the `conf` directory, and lastly to the default. This allows you to customize your application's logging configuration and still override it for specific environments or developer setups.
-->
> 注意: ログシステムは、システムプロパティで指定された設定ファイルに最高の優先度、次に `conf` ディレクトリにあるファイル、最後にデフォルトと優先度を与えます。これにより、アプリケーションのログ設定をカスタマイズしつつ、特定の環境や開発者の設定によって上書くことができます。

<!--
#### Using `-Dlogger.resource`
-->
#### `-Dlogger.resource` を使う

<!--
Specify a configuration file to be loaded from the classpath:
-->
クラスパスから設定ファイルを読み込むように指定します:

```
$ start -Dlogger.resource=prod-logger.xml
```

<!--
#### Using `-Dlogger.file`
-->
#### `-Dlogger.file` を使う

<!--
Specify a configuration file to be loaded from the file system:
-->
ファイルシステムから設定ファイルを読み込むように指定します:

```
$ start -Dlogger.file=/opt/prod/logger.xml
```

<!--
### Examples
-->
#### 例

<!--
Here's an example of configuration that uses a rolling file appender, as well as a seperate appender for outputting an access log:
-->
以下は、ローリングファイルアペンダだけでなく、アクセスログを出力する別のアペンダも使う設定例です:

```xml
<configuration>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${user.dir}/web/logs/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- Daily rollover with compression -->
            <fileNamePattern>application-log-%d{yyyy-MM-dd}.gz</fileNamePattern>
            <!-- keep 30 days worth of history -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%date{yyyy-MM-dd HH:mm:ss ZZZZ} [%level] from %logger in %thread - %message%n%xException</pattern>
        </encoder>
    </appender>
    
    <appender name="ACCESS_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${user.dir}/web/logs/access.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- daily rollover with compression -->
            <fileNamePattern>access-log-%d{yyyy-MM-dd}.gz</fileNamePattern>
            <!-- keep 1 week worth of history -->
            <maxHistory>7</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%date{yyyy-MM-dd HH:mm:ss ZZZZ} %message%n</pattern>
            <!-- this quadruples logging throughput -->
            <immediateFlush>false</immediateFlush>
        </encoder>
    </appender>

    <!-- additivity=false ensures access log data only goes to the access log -->
    <logger name="access" level="INFO" additivity="false">
        <appender-ref ref="ACCESS_FILE" />
    </logger>
    
    <root level="INFO">
        <appender-ref ref="FILE"/>
    </root>

</configuration>

```

<!--
This demonstrates a few useful features:
- It uses `RollingFileAppender` which can help manage growing log files.
- It writes log files to a directory external to the application so they aren't affected by upgrades, etc.
- The `FILE` appender uses an expanded message format that can be parsed by third party log analytics providers such as Sumo Logic.
- The `access` logger is routed to a separate log file using the `ACCESS_FILE_APPENDER`.
- All loggers are set to a threshold of `INFO` which is a common choice for production logging.  
-->
これは、いくつかの便利な機能を実演しています:
- 肥大化するログファイルの管理を支援する `RollingFileAppender` を使います。
- アプリケーション外部のディレクトリにログファイルを書き出すので、アップグレードやその他の影響を受けません。
- `FILE` アペンダは、Sumo Logic のようなサードパーティのログ解析プロバイダがパースできるように、メッセージフォーマットを拡張します。
- `access` ロガーは、`ACCESS_FILE_APPENDER` によって別のログファイルに転送されます。
- すべてのロガーの閾値は、本番ログ出力では一般的に選択される `INFO` に設定されています。

<!--
## Akka logging configuration
-->
## Akka ログ設定

<!--
Akka has its own logging system which may or may not use Play's underlying logging engine depending on how it is configured.
-->
設定によって Play 内部のログ機能を使うか使わないか分からないものの、Akka には独自のログシステムがあります。

<!--
By default, Akka will ignore Play's logging configuration and print log messages to STDOUT using its own format. You can configure the log level in `application.conf`:
-->
デフォルトで Akka は Play のログ設定を無視し、独自のフォーマットを使ってログメッセージを STDOUT に出力します。このログレベルは `application.conf` で設定することができます:

```properties
akka {
  loglevel="INFO"
}
```

<!--
To direct Akka to use Play's logging engine, you'll need to do some careful configuration. First add the following config in `application.conf`:
-->
Akka に Play のログ機能を使うよう指示する場合、いくつかの注意深い設定が必要です。まず、以下の設定を `application.conf` に追加します:

```properties
akka {
  loggers = ["akka.event.slf4j.Slf4jLogger"]
  loglevel="DEBUG"
}
```

<!--
A couple things to note:
-->
いくつかの点に注意してください:

<!--
- Setting `akka.loggers` to `["akka.event.slf4j.Slf4jLogger"]` will cause Akka to use Play's underlying logging engine.
- The `akka.loglevel` property sets the threshold at which Akka will forward log requests to the logging engine but does not control logging output. Once the log requests are forwarded, the Logback configuration controls log levels and appenders as normal. You should set `akka.loglevel` to the lowest threshold that you will use in Logback configuration for your Akka components.
-->
- `akka.loggers` を `["akka.event.slf4j.Slf4jLogger"]` に設定することで、Akka が Play 内部のログ機能を使うようになります。
- `akka.loglevel` プロパティは、Akka がログリクエストをログ機能に転送する閾値を設定しますが、ログ出力は管理しません。ログリクエストが転送されると、Logback 設定が通常どおりログレベルとアペンダを管理します。`akka.loglevel` には、Logback 設定で Akka コンポーネントに指定する、もっとも低い閾値を設定すべきです。
 
<!--
Next, refine your Akka logging settings in your Logback configuration:
-->
つづいて、Logback 設定にて Akka のログ設定を改善します:

```xml
<!-- Set logging for all Akka library classes to INFO -->
<logger name="akka" level="INFO" />
<!-- Set a specific actor to DEBUG -->
<logger name="actors.MyActor" level="DEBUG" />
```

<!--
You may also wish to configure an appender for the Akka loggers that includes useful properties such as thread and actor address.
-->
スレッドやアクターアドレスなどを含む便利な Akka ロガーのアペンダを設定したい場合もあることでしょう。

<!--
For more information about configuring Akka's logging, including details on Logback and Slf4j integration, see the [Akka documentation](http://doc.akka.io/docs/akka/current/scala/logging.html).
-->
Logback と Slf4j の統合に関する詳細を含む Akka のログ設定詳細については、[Akka のドキュメント](http://doc.akka.io/docs/akka/current/scala/logging.html) を参照してください。