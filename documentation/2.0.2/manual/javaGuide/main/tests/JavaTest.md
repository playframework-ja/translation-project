<!-- translated -->
<!--
# Testing your application
-->
# アプリケーションのテスト

<!--
Test source files must be placed in your application’s `test` folder. You can run tests from the Play console using the `test` and `test-only` tasks.
-->
テストのソースファイルは `test` フォルダに配置します。テストを実行するためには、 `test` または `test-only` タスクを実行してください。

<!--
## Using JUnit
-->
## JUnit を使う

<!--
The default way to test a Play 2 application is with [[JUnit| http://www.junit.org/]].
-->
Play 2 アプリケーションの標準的なテスト方法は、 [[JUnit| http://www.junit.org/]] を使うことです。

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
## フェイク・アプリケーション上で実行する

<!--
If the code you want to test depends on a running application, you can easily create a `FakeApplication` on the fly:
-->
実行しているアプリケーションやその設定に依存するようなコードをテストするときは、テスト内で `FakeApplication` を起動するとよいでしょう。

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
フェイク・アプリケーションを起動する際に、パラメータの上書き・追加、プラグインを持っくすることができます。例えば、 `default` という名前のインメモリデータベースが構成された `FakeApplication` を起動するには次のようにします。

```
fakeApplication(inMemoryDatabase())
```

<!--
> **Next:** [[Writing functional tests | JavaFunctionalTest]]
-->
> **次ページ:** [[機能テストを書く | JavaFunctionalTest]]