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

<!--
The default way to test a Play 2 application is with [[JUnit| http://www.junit.org/]].
-->
Play 2 アプリケーションのテストは、デフォルトで [[JUnit| http://www.junit.org/]] を使います。

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

<!--
> **Next:** [[Writing functional tests | JavaFunctionalTest]]
-->
> **次ページ:** [[機能テスト | JavaFunctionalTest]]