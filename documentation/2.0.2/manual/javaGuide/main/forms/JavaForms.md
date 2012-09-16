<!-- translated -->
<!--
# Handling form submission
-->
# フォームの送信

<!--
## Defining a form
-->
## フォームの定義

<!--
The `play.data` package contains several helpers to handle HTTP form data submission and validation. The easiest way to handle a form submission is to define a `play.data.Form` that wraps an existing class:
-->
`play.data`パッケージにはHTTPフォームデータの送信およびバリデーションを行うヘルパーがいくつか含まれています。フォーム送信を実装する最も簡単な方法は、既存のクラスをラップする`play.data.Form`オブジェクトを定義することです。

```java
public class User {
    public String email;
    public String password;
}
```

```java
Form<User> userForm = form(User.class);
```

<!--
> **Note:** The underlying binding is done using [[Spring data binder| http://static.springsource.org/spring/docs/3.0.7.RELEASE/reference/html/validation.html]].

This form can generate a `User` result value from `HashMap<String,String>` data:
-->
> **ノート:** 内部で行われる値のバインディングには[[Spring data binder| http://static.springsource.org/spring/docs/3.0.7.RELEASE/reference/html/validation.html]]が使われています。

このフォームは`HashMap<String,String>`データから`User`の結果値を生成します。

```java
Map<String,String> anyData = new HashMap();
anyData.put("email", "bob@gmail.com");
anyData.put("password", "secret");

User user = userForm.bind(anyData).get();
```

<!--
If you have a request available in the scope, you can bind directly from the request content:
-->
スコープ内にリクエストが存在する場合は、リクエストの内容から直接バインドすることができます。

```java
User user = userForm.bindFromRequest().get();
```

<!--
## Defining constraints
-->
## 制約を定義する

<!--
You can define additional constraints that will be checked during the binding phase using JSR-303 (Bean Validation) annotations:
-->
JSR-303 (Bean バリデーション)を使って、バインディング時にチェックされる制約を追加することができます。

```java
public class User {
    
    @Required
    public String email;
    public String password;
}
```

<!--
> **Tip:** The `play.data.validation.Constraints` class contains several built-in validation annotations.

You can also define an ad-hoc validation by adding a `validate` method to your top object:
-->
> **Tip:** `play.data.validation.Constraints`クラスには組み込みのバリデーションアノテーションがいくつか含まれています。

トップオブジェクトに`validate`メソッドを追加することで、その場限りのバリデーションを定義することもできます。

```java
public class User {
    
    @Required
    public String email;
    public String password;
    
    public String validate() {
        if(authenticate(email,password) == null) {
            return "Invalid email or password";
        }
        return null;
    }
}
```

<!--
## Handling binding failure
-->
## バインドエラーを処理する

<!--
Of course if you can define constraints, then you need to be able to handle the binding errors.
-->
制約を定義できるということは、バインドエラーを処理できる必要があります。

```java
if(userForm.hasErrors()) {
    return badRequest(form.render(userForm));
} else {
    User user = userForm.get();
    return ok("Got user " + user);
}
```

<!--
## Filling a form with initial default values
-->
## フォームを初期値で埋める

<!--
Sometimes you’ll want to fill a form with existing values, typically for editing:
-->
編集などのために、フォームに予め値を埋めておきたい場合は以下のようにします。

```java
userForm = userForm.fill(new User("bob@gmail.com", "secret"))
```

<!--
> **Tip:** `Form` objects are immutable - calls to methods like `bind()` and `fill()` will return a new object filled with the new data.
-->
> **Tip:** `Form`オブジェクトはイミュータブルです。つまり、`bind()`や`fill()`などのメソッドを呼び出すと、新しいデータで埋められた新しいオブジェクトが返ります。

<!--
## Register a custom DataBinder
-->
## 独自のDataBinderを登録する

<!--
In case you want to define a mapping from a custom object to a form field string and vice versa you need to register a new Formatter for this object.
For an object like JodaTime's `LocalTime` it could look like this:
-->
独自オブジェクトからフォームのフィールド、またはその逆方向のマッピングを定義したいときは、そのオブジェクトのために新たなフォーマッタを登録する必要があります。例えば、JodaTimeの`LocalTime`オブジェクトのためのフォーマッタを登録する場合は、次のようなコードになります。

```java
Formatters.register(LocalTime.class, new SimpleFormatter<LocalTime>() {

    private Pattern timePattern = Pattern.compile(
        "([012]?\\\\d)(?:[\\\\s:\\\\._\\\\-]+([0-5]\\\\d))?"
    ); 
    
    @Override
    public LocalTime parse(String input, Locale l) throws ParseException {
        Matcher m = timePattern.matcher(input);
        if (!m.find()) throw new ParseException("No valid Input",0);
        int hour = Integer.valueOf(m.group(1));
        int min = m.group(2) == null ? 0 : Integer.valueOf(m.group(2));
        return new LocalTime(hour, min);
    }
    
    @Override
    public String print(LocalTime localTime, Locale l) {
        return localTime.toString("HH:mm");
    }
  
});
```

<!--
> **Next:** [[Using the form template helpers | JavaFormHelpers]]
-->
> **次ページ:** [[フォームテンプレートヘルパーを利用する | JavaFormHelpers]]
