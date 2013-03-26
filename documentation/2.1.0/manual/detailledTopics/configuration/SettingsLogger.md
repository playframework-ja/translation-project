<!-- translated -->
<!--
# Configuring logging
-->
# ログの設定

<!--
Play 2.0 uses [[logback | http://logback.qos.ch/]] as its logging engine.
-->
Play 2.0 のログ機能の実装には、[[logback | http://logback.qos.ch/]] が使われています。

<!--
## Configuration logging level in application.conf
-->
# application.conf でログレベルを設定する

<!--
The easiest way to configure the logging level is to use the `logger` key in your `conf/application.conf` file.
-->
ログレベルを設定する一番簡単な方法は、`conf/application.conf` の `logger` というキーを使うことです。

<!--
Play defines a default `application` logger for your application, which is automatically used when you use the default `Logger` operations.
-->
Play にはデフォルトで `application` という名前のロガーが用意されていて、デフォルトの `Logger` を利用してログを出力する場合はこれが使われます。

```properties
# Root logger:
logger=ERROR

# Logger used by the framework:
logger.play=INFO

# Logger provided to your application:
logger.application=DEBUG
```

<!--
The root logger configuration affects all log calls, rather than requiring custom logging levels. Additionally, if you want to enable the logging level for a specific library, you can specify it here. For example to enable `TRACE` log level for Spring, you could add:
-->
一番上の階層の logger の設定は全てのログ出力に影響します。さらに、特定のライブラリに対するログレベルの指定も application.conf に記述します。例えば、Spring に対して `TRACE` のログレベルを設定する場合は、次のように記述します。

```properties
logger.org.springframework=TRACE
```

<!--
## Configuring logback
-->
## logbackの設定

<!--
The default is to define two appenders, one dispatched to the standard out stream, and the other to the `logs/application.log` file.
-->
デフォルトでは 2 つの appender が定義されています。ひとつは標準出力へのログ用、もうひとつは `logs/application.log` へ出力するログ用です。

If you want to fully customize logback, just define a `conf/application-logger.xml` configuration file. Here is the default configuration file used by Play:

```xml
<configuration>
    
  <conversionRule conversionWord="coloredLevel" converterClass="play.api.Logger$ColoredLevel" />
  
  <appender name="FILE" class="ch.qos.logback.core.FileAppender">
     <file>${application.home}/logs/application.log</file>
     <encoder>
       <pattern>%date - [%level] - from %logger in %thread %n%message%n%xException%n</pattern>
     </encoder>
   </appender>

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%coloredLevel %logger{15} - %message%n%xException{5}</pattern>
    </encoder>
  </appender>
  
  <logger name="play" level="INFO" />
  <logger name="application" level="INFO" />

  <root level="ERROR">
    <appender-ref ref="STDOUT" />
    <appender-ref ref="FILE" />
  </root>
  
</configuration>
```

<!--
## Changing the logback configuration file
-->
## logback 設定ファイルの変更

<!--
You can also specify another logback configuration file via a System property. It is particulary useful when running in production.
-->
さらに、システムプロパティによって別の logback 設定ファイルを指定することができます。
本番環境 (production モード) で実行する場合に非常に便利です。

<!--
### Using `-Dlogger.resource`
-->
### `-Dlogger.resource` を使う

Specify another logback configuration file to be loaded from the classpath:

```
$ start -Dlogger.resource=prod-logger.xml
```

<!--
### Using `-Dlogger.file`
-->
### `-Dlogger.file` を使う

Specify another logback configuration file to be loaded from the file system:

```
$ start -Dlogger.file=/opt/prod/logger.xml
```

<!--
### Using `-Dlogger.url`
-->
### `-Dlogger.url` を使う

Specify another logback configuration file to be loaded from an URL:

```
$ start -Dlogger.url=http://conf.mycompany.com/logger.xml
```
