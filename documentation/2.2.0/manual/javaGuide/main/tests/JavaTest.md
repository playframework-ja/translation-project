<!-- translated -->
<!--
# Testing your application
-->
# アプリケーションのテスト

<!--
Test source files must be placed in your application’s `test` folder. You can run tests from the Play console using the `test` (run all tests) and `test-only` (run one test class: `test-only my.namespace.MySpec`) tasks.
-->
テストのソースファイルは `test` フォルダに配置します。 Play コンソールで `test` (すべてのテストを実行) や `test-only` (ひとつのテストクラスを実行: `test-only my.namespace.MySpec`) タスクを実行すると、テストを実行することができます。

<!--
## Using JUnit
-->
## JUnit を使う

The default way to test a Play application is with [JUnit](http://www.junit.org/).

```
package test;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class SimpleTest {

    @Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

}
```

> **Note:** A new process is forked each time `test` or `test-only` is run.  The new process uses default JVM settings.  Custom settings can be added to `play.Project.settings` in `Build.scala`.  For example:  
> ```
> javaOptions in (Test) += "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
> javaOptions in (Test) += "-Xms512M",
> javaOptions in (Test) += "-Xmx1536M",
> javaOptions in (Test) += "-Xss1M",
> javaOptions in (Test) += "-XX:MaxPermSize=384M"
> ```

<!--
## Running in a fake application
-->
## フェイクアプリケーション上で実行する

<!--
If the code you want to test depends on a running application, you can easily create a `FakeApplication` on the fly:
-->
起動中のアプリケーションの依存するコードをテストする場合は、簡単に `FakeApplication` を利用することができます。

```
@Test
public void findById() {
  running(fakeApplication(), new Runnable() {
    public void run() {
      Computer macintosh = Computer.find.byId(21l);
      assertThat(macintosh.name).isEqualTo("Macintosh");
      assertThat(formatted(macintosh.introduced)).isEqualTo("1984-01-24");
    }
  });
}
```

<!--
You can also pass (or override) additional application configuration, or mock any plugin. For example to create a `FakeApplication` using a `default` in-memory database:
-->
このフェイクアプリケーションに対して設定値を追加 (または上書き) したり、プラグインをモックすることも可能です。例えば、 `default` という名前の インメモリデータベースに接続された `FakeApplication` を起動する場合は、次のように書きます。

```
fakeApplication(inMemoryDatabase())
```

> **Note:** Applications using Ebean ORM may be written to rely on Play's automatic getter/setter generation.  Play also rewrites field accesses to use the generated getters/setters.  Ebean relies on calls to the setters to do dirty checking.  In order to use these patterns in JUnit tests, you will need to enable Play's field access rewriting in test by adding the following to `play.Project.settings` in `Build.scala`:
> ```
> compile in Test <<= PostCompile(Test)
> ```  

<!--
> **Next:** [[Writing functional tests | JavaFunctionalTest]]
-->
> **次ページ:** [[機能テスト | JavaFunctionalTest]]