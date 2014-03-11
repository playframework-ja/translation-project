<!-- translated -->
<!--
# Configuration file syntax and features
-->
# 設定ファイルのシンタックスと機能

<!--
> The configuration file used by Play is based on the [Typesafe config library](https://github.com/typesafehub/config).
-->
> Play における設定ファイルは [Typesafe config library](https://github.com/typesafehub/config) をベースにしています。

<!--
The default configuration file of a Play application must be defined in `conf/application.conf`. It uses the HOCON format ( "Human-Optimized Config Object Notation").
-->
Play アプリケーションのデフォルトの設定ファイルは、  `conf/application.conf` に保存されている必要があります。また、この設定ファイルは、 HOCON フォーマット ( "Human-Optimized Config Object Notation" ) で記述されています。

<!--
## Specifying an alternative configuration file
-->
## 代替設定ファイルの指定

<!--
System properties can be used to force a different config source:
-->
システムプロパティを使うことで、異なる設定ソースの利用を強制することができます。

<!--
* `config.resource` specifies a resource name - not a basename, i.e. application and not application.conf
* `config.file` specifies a filesystem path, again it should include the extension, not be a basename
* `config.url` specifies a URL
-->
* `config.resource` はリソースファイルの指定です - application のようなベースネームの指定はできません。また、 application.conf 以外を指定します。
* `config.file` はファイルシステム上のパスの指定です。パスには拡張子を含みます。ベースネームの指定はできません。
* `config.url` は URL の指定です。

<!--
These system properties specify a replacement for `application.conf`, not an addition. In the replacement config file, you can use include "application" to include the original default config file; after the include statement you could go on to override certain settings.
-->
これらのシステムプロパティは `application.conf` の代替えとなる設定ファイルの指定に使います。代替えであって、追加ではないことに注意してください。ただし、代替えになる設定ファイルに  include "application" と記述することで、デフォルトの設定ファイルを include することができます。さらに、 include 文の後で、任意の設定を上書きすることができます。

<!--
## Using with Akka
-->
## Akka と一緒に使う

<!--
Akka 2.0 will use the same configuration file as the one defined for your Play application. Meaning that you can configure anything in Akka in the `application.conf` directory.
-->
Akka 2.0 は Play アプリケーションと同じ設定ファイルを読み込みます。つまり、 Akka に関するあらゆる設定は `application.conf` で行うことができます。

<!--
## HOCON Syntax
-->
## HOCON の文法

<!--
Much of this is defined with reference to JSON; you can find the JSON spec at http://json.org/ of course.
-->
HOCON の文法は、ほとんど JSON にしたがって定義されています。JSON の仕様は http://json.org/ にあります。

<!--
### Unchanged from JSON
-->
### JSON と同じ部分

<!--
 - files must be valid UTF-8
 - quoted strings are in the same format as JSON strings
 - values have possible types: string, number, object, array, boolean, null
 - allowed number formats matches JSON; as in JSON, some possible
   floating-point values are not represented, such as `NaN`
-->
 - ファイルは UTF-8 形式でなければならない
 - 引用された文字列は JSON の文字列と同じフォーマットである
 - 利用可能な値の型は string、number、object、array、boolean、null
 - JSON と同様の数値形式が利用可能。 JSON 同様、`NaN` のように表現不可能な浮動小数点数がある。

<!--
### Comments
-->
### コメント

<!--
Anything between `//` or `#` and the next newline is considered a comment and ignored, unless the `//` or `#` is inside a quoted string.
-->
`//` や `#` から次の行まではコメントとして無視されます。ただし、`//` や `#` が引用文字列のなかにある場合は含みません。

<!--
### Omit root braces
-->
### root の括弧の省略

<!--
JSON documents must have an array or object at the root. Empty files are invalid documents, as are files containing only a non-array non-object value such as a string.
-->
JSON ドキュメントは root として array や object を一つ持つ必要があります。空ファイルだったり、もしくは string などの array や object  以外しか含まないファイルは JSON ドキュメントとして正しくありません。

<!--
In HOCON, if the file does not begin with a square bracket or curly brace, it is parsed as if it were enclosed with `{}` curly braces.
-->
HOCON では、ファイルが中括弧や大括弧で始まっていない場合、中括弧 `{}` で囲まれているものとしてパースします。

<!--
A HOCON file is invalid if it omits the opening `{` but still has a closing `}`; the curly braces must be balanced.
-->
しかし、始め中括弧 `{` を省略したにも関わらず 閉じ中括弧 `}` が存在するようなファイルは HOCON フォーマットとして正しくありません。中括弧はバランスされている必要があります。

<!--
### Key-value separator
-->
### Key-value の区切り文字

<!--
The `=` character can be used anywhere JSON allows `:`, i.e. to separate keys from values.
-->
JSON でキーと値を区切るために `:` が期待されるような箇所には、`=` を使うこともできます。

<!--
If a key is followed by `{`, the `:` or `=` may be omitted. So `"foo" {}` means `"foo" : {}"`
-->
キーの直後に `{` が現れた場合、`:` や `=` は省略可能です。つまり、 `"foo" {}` は `"foo" : {}` と同じ意味です。

<!--
### Commas
-->
### カンマ

<!--
Values in arrays, and fields in objects, need not have a comma between them as long as they have at least one ASCII newline (`\n`, decimal value 10) between them.
-->
配列内の値や、オブジェクトのフィールドを区切るためのカンマは、1つ以上の改行（`\n`、10進数のASCIIコードでいえば 10）があれば省略可能です。

<!--
The last element in an array or last field in an object may be followed by a single comma. This extra comma is ignored.
-->
配列の最後の要素や、オブジェクトの最後のフィールドの後に、一つだけカンマを記述することができます。この余分なカンマは無視されます。

<!--
 - `[1,2,3,]` and `[1,2,3]` are the same array.
 - `[1\n2\n3]` and `[1,2,3]` are the same array.
 - `[1,2,3,,]` is invalid because it has two trailing commas.
 - `[,1,2,3]` is invalid because it has an initial comma.
 - `[1,,2,3]` is invalid because it has two commas in a row.
 - these same comma rules apply to fields in objects.
-->
 - `[1,2,3,]` と `[1,2,3]` は同じ配列です。
 - `[1\n2\n3]` と `[1,2,3]` は同じ配列です。
 - `[1,2,3,,]` は最後にカンマが二つ連続しているため、正しくありません。
 - `[,1,2,3]` は最初にカンマが記述されているため、正しくありません。
 - `[1,,2,3]` は途中でカンマが二つ連続しているため、正しくありません。
 - オブジェクトのフィールドを区切るカンマについても、同様のルールが適用されます。

<!--
### Duplicate keys
-->
### 重複したキー

<!--
The JSON spec does not clarify how duplicate keys in the same object should be handled. In HOCON, duplicate keys that appear later override those that appear earlier, unless both values are objects. If both values are objects, then the objects are merged.
-->
JSON の仕様では、同じオブジェクトにおける重複したキーの取り扱いは定義されていません。 HOCON では、キーが重複していて、値の一方がオブジェクト以外の場合は、後に出現している方が先に出現しているものを上書きします。値が両方ともオブジェクトの場合は、二つのオブジェクトがマージされます。

<!--
Note: this would make HOCON a non-superset of JSON if you assume that JSON requires duplicate keys to have a behavior. The assumption here is that duplicate keys are invalid JSON.
-->
Note: もしあなたが JSON においてキーが重複した場合に何らかの挙動を示すことを期待するのであれば、 HOCON は JSON の上位集合ではないといえます。ここでは、重複キーを含む JSON は正しくない、という仮定をしています。

<!--
To merge objects:

 - add fields present in only one of the two objects to the merged object.
 - for non-object-valued fields present in both objects, the field found in the second object must be used.
 - for object-valued fields present in both objects, the object values should be recursively merged according to these same rules.
-->
オブジェクトのマージは次のように行われます。

 - 二つのオブジェクトの一方にしか無いフィールドを、マージにより生成される新しいオブジェクトに追加します。
 - 両方のオブジェクトに存在する全ての「値がオブジェクトでないフィールド」について、後者のオブジェクトの方のフィールドを採用する。
 - 両方のオブジェクトに存在する全ての「値がオブジェクトであるフィールド」について、これら3つのルールに基づき再帰的にマージする。

<!--
Object merge can be prevented by setting the key to another value first. This is because merging is always done two values at a time; if you set a key to an object, a non-object, then an object, first the non-object falls back to the object (non-object always wins), and then the object falls back to the non-object (no merging, object is the new value). So the two objects never see each other.
-->
キーに一旦オブジェクト以外の値をセットすることで、二つのオブジェクトをマージさせないことも可能です。これは、マージが常に二つの値に対して行われるからです。例えば、キーにオブジェクト、オブジェクト以外、オブジェクトの順に値をセットすると、まず非オブジェクトがオブジェクトを上書きします（常に非オブジェクトが優先されます）。次に、オブジェクトが非オブジェクトを上書きします（オブジェクトがそのキーに対する新しい値となります。マージはされません。）。したがって、1番目と3番目のオブジェクトがマージされることはありません。

<!--
These two are equivalent:
-->
以下の二つは同じです。

    {
        "foo" : { "a" : 42 },
        "foo" : { "b" : 43 }
    }

    {
        "foo" : { "a" : 42, "b" : 43 }
    }

<!--
And these two are equivalent:
-->
以下の二つも同じです。

    {
        "foo" : { "a" : 42 },
        "foo" : null,
        "foo" : { "b" : 43 }
    }

    {
        "foo" : { "b" : 43 }
    }

<!--
The intermediate setting of `"foo"` to `null` prevents the object merge.
-->
ご覧のとおり、途中で `"foo"` に `null` をセットすることで、オブジェクトのマージを行わないということが可能です。

<!--
### Paths as keys
-->
### キーとしてのパス

<!--
If a key is a path expression with multiple elements, it is expanded to create an object for each path element other than the last. The last path element, combined with the value, becomes a field in the most-nested object.
-->
もしキーが複数要素からなるパス式である場合、パスの最後以外の全ての要素が展開されて、オブジェクトが生成されます。パスの最後の要素は、値と組み合わされて、最も内側のオブジェクトのフィールドとなります。

<!--
In other words:
-->
言い換えると、

    foo.bar : 42

<!--
is equivalent to:
-->
は、

    foo { bar : 42 }

<!--
and:
-->
と等しく、また、

    foo.bar.baz : 42

<!--
is equivalent to:
-->
は、

    foo { bar { baz : 42 } }

と等しくなります。

<!--
and so on. These values are merged in the usual way; which implies that:
-->
また、これらの値は前述の方法でマージされます。つまり、

    a.x : 42, a.y : 43

<!--
is equivalent to:
-->
は、

    a { x : 42, y : 43 }

と等しくなります。

<!--
Because path expressions work like value concatenations, you can have whitespace in keys:
-->
パス式は値の結合と同じように扱われるため、キー中に空白を含めることができます。

    a b c : 42

<!--
is equivalent to:
-->
は、

    "a b c" : 42

と等しくなります。

<!--
Because path expressions are always converted to strings, even single values that would normally have another type become strings.
-->
パス式は常に文字列に変換されるため、通常は文字列以外の型となるような単一の値さえも文字列になります。

<!--
   - `true : 42` is `"true" : 42`
   - `3.14 : 42` is `"3.14" : 42`
-->
   - `true : 42` は `"true" : 42`
   - `3.14 : 42` は `"3.14" : 42`

<!--
As a special rule, the unquoted string `include` may not begin a path expression in a key, because it has a special interpretation (see below).
-->
特例として、引用符を含まない `include` は特別扱いされるため、キーにパス式を含んではいけません。

<!--
## Substitutions
-->
## 置換

<!--
Substitutions are a way of referring to other parts of the configuration tree.
-->
置換は、設定ツリーの別のパートを参照する機能です。

<!--
The syntax is `${pathexpression}` or `${?pathexpression}` where the `pathexpression` is a path expression as described above. This path expression has the same syntax that you could use for an object key.
-->
置換の文法は、 `${pathexpression}` または `$(?pathexpression}` です。`pathexpression` は前述のパス式です。このパス式は、オブジェクトのキーに利用できるものと同じ文法です。

<!--
The `?` in `${?pathexpression}` must not have whitespace before it; the three characters `${?` must be exactly like that, grouped together.
-->
`${?pathexpression}` 中の `?` はその直前に空白文字を含んではいけません。 `${?` という3つの文字は、全くこのとおりにまとめて記述されている必要があります。

<!--
For substitutions which are not found in the configuration tree, implementations may try to resolve them by looking at system environment variables or other external sources of configuration. (More detail on environment variables in a later section.)
-->
設定ツリー内に見つからない置換を行おうとした場合、設定ファイルの実装がシステム環境変数や他の外部ソースにある設定を参照して解決を試みます。 (環境変数に関する詳細は後のセクションでご説明します。)

<!--
Substitutions are not parsed inside quoted strings. To get a string containing a substitution, you must use value concatenation with the substitution in the unquoted portion:
-->
引用文字列中では、置換はパースされません。置換を含むような文字列を取得したい場合、引用符で囲まれていない部分に置換を記述して、値の結合を行うとよいでしょう。

    key : ${animal.favorite} is my favorite animal

<!--
Or you could quote the non-substitution portion:
-->
もしくは、置換以外の部分を引用符で囲ってもよいでしょう。

    key : ${animal.favorite}" is my favorite animal"

<!--
Substitutions are resolved by looking up the path in the configuration. The path begins with the root configuration object, i.e. it is "absolute" rather than "relative."
-->
置換は設定ファイル中のパスを検索して解決されます。このとき、パスは root オブジェクトから始まります。言い換えれば、置換で指定するパスは `相対的` というより `絶対的` です。

<!--
Substitution processing is performed as the last parsing step, so a substitution can look forward in the configuration. If a configuration consists of multiple files, it may even end up retrieving a value from another file. If a key has been specified more than once, the substitution will always evaluate to its latest-assigned value (the merged object or the last non-object value that was set).
-->
置換処理は設定ファイルのパースにおける最終ステップで実行されます。したがって、置換は設定ファイルの前方で定義されたパスを参照することができます。もし、設定が複数のファイルから構成されているのであれば、置換が記述されているファイルとは別のファイルから値を取得することになるかもしれません。キーが複数回指定されている場合、一番最後にセットされた値（そのキーについてマージされたオブジェクトか、もしくは最後にセットされたオブジェクト以外の値）が置換により挿入されることになります。

<!--
If a configuration sets a value to `null` then it should not be looked up in the external source. Unfortunately there is no way to "undo" this in a later configuration file; if you have `{ "HOME" : null }` in a root object, then `${HOME}` will never look at the environment variable. There is no equivalent to JavaScript's `delete` operation in other words.
-->
設定によりある値に明示的に `null` がセットされた場合、その値を外部ソースから参照することはできなくなります。残念ながら、これを後の設定ファイルで "undo" する方法はありません。例えば、 root オブジェクトに `{ "HOME" : null }` というフィールドを定義してしまうと、 `${HOME}` という置換が外部ソースである環境変数を使って解決されることはありません。言い換えれば、 JavaScript の `delete` に相当するような操作は用意されていないということです。

<!--
If a substitution does not match any value present in the configuration and is not resolved by an external source, then it is undefined. An undefined substitution with the `${foo}` syntax is invalid and should generate an error.
-->
もし、置換しようとしている値が設定や外部ソースに存在しない場合、 それは未定義として扱われます。`${foo}` という文法では未定義の置換は許されていないので、エラーとなります。

<!--
If a substitution with the `${?foo}` syntax is undefined:
-->
一方で、 `${?foo}` という文法による置換が未定義となった場合、

<!--
 - if it is the value of an object field then the field should not
   be created. If the field would have overridden a previously-set
   value for the same field, then the previous value remains.
 - if it is an array element then the element should not be added.
 - if it is part of a value concatenation then it should become an
   empty string.
 - `foo : ${?bar}` would avoid creating field `foo` if `bar` is
   undefined, but `foo : ${?bar} ${?baz}` would be a value
   concatenation so if `bar` or `baz` are not defined, the result
   is an empty string.
-->
 - もしオブジェクトのフィールドの値であったなら、フィールドは作成されない。既にフィールドが存在して、以前の値を上書きしようとしていた場合、未定義で上書きはされず以前の値が残る。
 - もし配列の要素であったなら、要素は追加されない。
 - もし値の結合の一部分であったなら、その部分は空文字列に変換される。
 - `foo: ${?bar}` は `bar` が未定義の場合は フィールド `foo` を作成しない。しかし、 `foo : ${?bar} ${?baz}` は値の結合であるため、 `bar` か `baz` が未定義の場合、結果は空文字列となる。

<!--
Substitutions are only allowed in object field values and array elements (value concatenations), they are not allowed in keys or nested inside other substitutions (path expressions).
-->
置換はオブジェクトのフィールドの値や配列の要素（値の結合）においてのみ有効で、キーやその他の置換（パス式）内では利用できない。

<!--
A substitution is replaced with any value type (number, object, string, array, true, false, null). If the substitution is the only part of a value, then the type is preserved. Otherwise, it is value-concatenated to form a string.
-->
置換は任意の値型（number、object、string、array、true、false、null）と置き換えることができます。置換が値の一部分でしかない場合は、型はそのまま維持されます。そうでなければ、値が結合されて string となります。

<!--
Circular substitutions are invalid and should generate an error.
-->
お互いに循環するような置換は不正であり、エラーとなります。

<!--
Implementations must take care, however, to allow objects to refer to paths within themselves. For example, this must work:
-->
しかしながら、設定のパーサの実装において、オブジェクトが自身のパスを参照できることに十分注意しなければなりません。例えば、以下の設定は有効です。

    bar : { foo : 42,
            baz : ${bar.foo}
          }

<!--
Here, if an implementation resolved all substitutions in `bar` as part of resolving the substitution `${bar.foo}`, there would be a cycle. The implementation must only resolve the `foo` field in `bar`, rather than recursing the entire `bar` object.
-->
この設定における置換 `${bar.foo}` を解決するにあたって `bar ` の全ての置換を解決するようなパーサーを実装してしまったとすると、循環が発生します。実際には、このときに `bar` に再帰するのではなく、`bar` の `foo` フィールドだけを解決するようなパーサーを実装しなければなりません。

<!--
## Includes
-->
## インクルード

<!--
### Include syntax
-->
### インクルードの文法

<!--
An _include statement_ consists of the unquoted string `include` and a single quoted string immediately following it. An include statement can appear in place of an object field.
-->
_include_ 命令は、二重引用符に囲まれていない `include` という文字列と、その直後にある引用符に囲まれた文字列の二つの要素から構成されます。incude 命令はオブジェクトのフィールドの箇所に記述することができます。

<!--
If the unquoted string `include` appears at the start of a path expression where an object key would be expected, then it is not interpreted as a path expression or a key.
-->
引用符に囲まれていない `include` がパス式の始まり、つまりオブジェクトのキーが期待されるところに現れた場合、パス式やキーとしては解釈されません。

<!--
Instead, the next value must be a _quoted_ string. The quoted string is interpreted as a filename or resource name to be included.
-->
その代わりに、次の値は引用符で囲まれた文字列でなければなりません。その文字列は、インクルードされるファイル名かリソース名として解釈されます。

<!--
Together, the unquoted `include` and the quoted string substitute for an object field syntactically, and are separated from the following object fields or includes by the usual comma (and as usual the comma may be omitted if there's a newline).
-->
まとめると、引用符で囲まれていない `include` と引用符で囲まれた文字列は、文法的にはオブジェクトのフィールドに置換されます。また、その後に続けて他のフィールドや、さらなるインクルードをカンマ（前述のとおり、改行がある場合は省略可能）で区切って記述することができます。

<!--
If an unquoted `include` at the start of a key is followed by anything other than a single quoted string, it is invalid and an error should be generated.
-->
引用符で囲まれていない `include` で始まるキーの後に、引用符で囲まれた文字列以外のものを続けて記述するのは間違いで、エラーとなります。

<!--
There can be any amount of whitespace, including newlines, between the unquoted `include` and the quoted string.
-->
引用符で囲まれていない `include` と、引用符で囲まれた文字列の間は、任意の個数の空白文字または改行を含めることができます。

<!--
Value concatenation is NOT performed on the "argument" to `include`. The argument must be a single quoted string. No substitutions are allowed, and the argument may not be an unquoted string or any other kind of value.
-->
`incude` の引数に対しては、値の結合が機能しません。引数は必ず引用符で囲まれた文字列でなければなりません。置換は効かず、引数は引用符で囲まれていない文字列やその他の値も使うことはできません。

<!--
Unquoted `include` has no special meaning if it is not the start of a key's path expression.
-->
引用符で囲まれていない `include` は、パス式の始め以外の箇所に出現した場合は特別な意味を持ちません。

<!--
It may appear later in the key:
-->
キーの後半に出現したり、

    # this is valid
    { foo include : 42 }
    # equivalent to
    { "foo include" : 42 }

<!--
It may appear as an object or array value:
-->
オブジェクトや配列の値として出現することもあるでしょう。

    { foo : include } # value is the string "include"
    [ include ]       # array of one string "include"

<!--
You can quote `"include"` if you want a key that starts with the word `"include"`, only unquoted `include` is special:
-->
`"include"` という単語で始まるキーが必要な場合は、 `"include"` のように引用符をつけることができます。`include` が特別な意味を持つのは、引用符で囲まれていない時だけです。

    { "include" : 42 }

<!--
### Include semantics: merging
-->
### インクルードの意味論：　マージ

<!--
An _including file_ contains the include statement and an _included file_ is the one specified in the include statement. (They need not be regular files on a filesystem, but assume they are for the moment.)
-->
_インクルードした側のファイル_ は include 命令と include 命令で指定された _インクルードされたファイル_ の2要素から構成されています。（_インクルードされたファイル_ はファイルシステム上のファイルである必要はないのですが、今のところはそう思っておいてください）

<!--
An included file must contain an object, not an array. This is significant because both JSON and HOCON allow arrays as root values in a document.
-->
インクルードされたファイル は配列ではなくオブジェクトを含んでいる必要があります。通常、JSON や HOCON がドキュメントの root として配列を許していることを考えると、 インクルードされたファイル の root にオブジェクトしか許されないのは特徴的です。

<!--
If an included file contains an array as the root value, it is invalid and an error should be generated.
-->
root として配列を持っているような include file は間違いで、エラーとなります。

<!--
The included file should be parsed, producing a root object. The keys from the root object are conceptually substituted for the include statement in the including file.
-->
インクルードされたファイル がパースされると、 root オブジェクトが生成されます。概念的には、root オブジェクトのキーが インクルードした側のファイル の include 命令と置換される、と考えてください。

<!--
 - If a key in the included object occurred prior to the include
   statement in the including object, the included key's value
   overrides or merges with the earlier value, exactly as with
   duplicate keys found in a single file.
 - If the including file repeats a key from an earlier-included
   object, the including file's value would override or merge
   with the one from the included file.
-->
 - included object に含まれるキーが include 命令より前に出現していた場合、インクルードされたキーの値は以前の値を上書き、または以前の値とマージされます。上書きとマージの方法は、単一ファイルに同じキーが複数回出現した場合と全く同じです。
 - 以前に included object から読み込まれたキーを インクルードした側のファイル で再度指定した場合、インクルードした側のファイルの値がインクルードされたファイルの値を上書きまたはマージします。

<!--
### Include semantics: substitution
-->
### インクルードの意味論：　置換

<!--
Substitutions in included files are looked up at two different paths; first, relative to the root of the included file; second, relative to the root of the including configuration.
-->
インクルードされたファイル 内の置換は２ステップかけて、それぞれパスが異なる解釈をなされて解決されます。最初のステップでは、パスは インクルードされたファイル の root から相対的なものとして解釈されます。次のステップでは、パスは インクルードした側のファイル の root から相対的なものとして解釈されます。

<!--
Recall that substitution happens as a final step, _after_ parsing. It should be done for the entire app's configuration, not for single files in isolation.
-->
前述のとおり、置換はパースの_後_、最終ステップで実行されます。置換は単一ファイルのファイルそれぞれについてではなく、アプリケーションの設定全体について行われます。

<!--
Therefore, if an included file contains substitutions, they must be "fixed up" to be relative to the app's configuration root.
-->
したがって、included file が置換を含む場合、パスはアプリケーションの設定の root から相対的なものとして「修正」されなければなりません。

<!--
Say for example that the root configuration is this:
-->
例えば、次のような root 設定があるとします。

    { a : { include "foo.conf" } }

<!--
And "foo.conf" might look like this:
-->
そして、次のような "foo.conf" があるとします。

    { x : 10, y : ${x} }

<!--
If you parsed "foo.conf" in isolation, then `${x}` would evaluate to 10, the value at the path `x`. If you include "foo.conf" in an object at key `a`, however, then it must be fixed up to be `${a.x}` rather than `${x}`.
-->
"foo.conf" を単体でパースしたとしたら、 `${x}` はパス `x` の値 10 と評価されるはずです。しかし、 "foo.conf" を任意のオブジェクトのキー `a` にインクルードした場合、このパスは `${x}` ではなく `${a.x}` のように修正されなければなりません。

<!--
Say that the root configuration redefines `a.x`, like this:
-->
さらに、 root 設定で `a.x` が次のように再定義されているケースを考えてみます。

    {
        a : { include "foo.conf" }
        a : { x : 42 }
    }

<!--
Then the `${x}` in "foo.conf", which has been fixed up to `${a.x}`, would evaluate to `42` rather than to `10`. Substitution happens _after_ parsing the whole configuration.
-->
このとき、 "foo.conf" の `${x}` は `${a.x}` に修正されて、 `10` ではなく `42` と評価されます。置換は全ての設定のパース `後` に処理されるからです。

<!--
However, there are plenty of cases where the included file might intend to refer to the application's root config. For example, to get a value from a system property or from the reference configuration. So it's not enough to only look up the "fixed up" path, it's necessary to look up the original path as well.
-->
一方で、 インクルードされたファイル から意図的にアプリケーションの root　設定を参照したいケースはよくあります。例えば、システムプロパティやリファレンス設定から値を取得するような場合です。前述のように「修正」されたパスを参照できるだけでは不十分で、今述べたように元々のパスも参照できる必要があるのです。

<!--
### Include semantics: missing files
-->
### インクルードの意味論：　存在しないファイル

<!--
If an included file does not exist, the include statement should be silently ignored (as if the included file contained only an empty object).
-->
インクルードされたファイル が存在しない場合、 include 命令は特にエラーもなく無視されます（インクルードされたファイル に空オブジェクトしか定義されていなかった場合と同じ挙動です）。

<!--
### Include semantics: locating resources
-->
### インクルードの意味論： リソースの特定

<!--
Conceptually speaking, the quoted string in an include statement identifies a file or other resource "adjacent to" the one being parsed and of the same type as the one being parsed. The meaning of "adjacent to", and the string itself, has to be specified separately for each kind of resource.
-->
概念的には、include 命令の引数となる引用符で囲まれた文字列は、その時パースされているファイルやその他のリソースと「隣接」していて、かつ同じ種類のリソースを識別するために使われます。この文字列や「隣接」の意味は、リソースの種類によって異なります。

<!--
Implementations may vary in the kinds of resources they support including.
-->
設定のパーサーの実装によって、サポートしているリソースの種類に違いがある可能性があります。

<!--
On the Java Virtual Machine, if an include statement does not identify anything "adjacent to" the including resource, implementations may wish to fall back to a classpath resource. This allows configurations found in files or URLs to access classpath resources.
-->
Java Virtual Machine においては、 include 命令がインクルードされるものに「隣接」するリソースというものを見つけられなかった場合、パーサーの実装はクラスパス上に存在するリソースにフォールバックしてもよいことになっています。このルールのおかげで、ファイルシステムや URL 上の設定ファイルからクラスパス上のリソースを参照することができます。

<!--
For resources located on the Java classpath:
-->
Java のクラスパスにあるリソースについては、

<!--
 - included resources are looked up by calling `getResource()` on
   the same class loader used to look up the including resource.
 - if the included resource name is absolute (starts with '/')
   then it should be passed to `getResource()` with the '/'
   removed.
 - if the included resource name does not start with '/' then it
   should have the "directory" of the including resource.
   prepended to it, before passing it to `getResource()`.  If the
   including resource is not absolute (no '/') and has no "parent
   directory" (is just a single path element), then the included
   relative resource name should be left as-is.
 - it would be wrong to use `getResource()` to get a URL and then
   locate the included name relative to that URL, because a class
   loader is not required to have a one-to-one mapping between
   paths in its URLs and the paths it handles in `getResource()`.
   In other words, the "adjacent to" computation should be done
   on the resource name not on the resource's URL.
-->
 - インクルードされたリソースは、インクルードした側のリソースを検索するのに使われたものと同じクラスローダーの `getResource()` メソッドを呼び出して検索されます。
 - インクルードされたリソースの名前が絶対パス（'/' で始まる）である場合は、 '/' を削除した上で `getResource()` に渡されます。
 - インクルードされたリソースの名前が '/' で始まっていない場合は、インクルードした側のリソースのディレクトリを先頭に繋げてから、 `getResource()` に渡します。インクルードした側のリソース名が絶対パスでない（'/' で始まらない）、かつ「親ディレクトリ」（単なるパスの要素）もない場合は、インクルードされた相対的なリソース名がそのまま使われます。
 - `getResource()` を URL の取得や、または そのURL からインクルードされたリソースの 相対 URL を取得するために使うのは間違いです。これは、クラスローダーの URL におけるパスと、 `getResource()` におけるパスの一対一のマッピングをするとは限らないからです。言い換えれば、前述の「隣接」の計算は、リソースの URL ではなく 名前を使って行うべきだということです。

<!--
For plain files on the filesystem:
-->
ファイルシステム上のファイルについては、

<!--
 - if the included file is an absolute path then it should be kept
   absolute and loaded as such.
 - if the included file is a relative path, then it should be
   located relative to the directory containing the including
   file.  The current working directory of the process parsing a
   file must NOT be used when interpreting included paths.
 - if the file is not found, fall back to the classpath resource.
   The classpath resource should not have any package name added
   in front, it should be relative to the "root"; which means any
   leading "/" should just be removed (absolute is the same as
   relative since it's root-relative). The "/" is handled for
   consistency with including resources from inside other
   classpath resources, where the resource name may not be
   root-relative and "/" allows specifying relative to root.
-->
 - インクルードされたファイルが絶対パスの場合は、絶対パスのまま扱われて、ロードされます。
 - インクルードされたファイルが相対パスの場合は、インクルードする側のファイルからの相対パスとして解釈されます。インクルードされるパスを解釈するときに、ファイルをパースしているプロセスのカレントディレクトリを使ってはなりません。
 - ファイルが見つからない場合、クラスパス上のリソースにフォールバックします。そのとき、クラスパス上のリソース名の先頭にパッケージ名を追加することはせず、 "root" からの相対パスになります。これは、先頭の "/" が単に削除される（今回は root からの相対パスなので、このルールにより絶対パスも相対パスも同じリソースを指すことになります）ことを意味します。"/" をこのように扱う理由は、他のクラスアパスリソース内からリソースをインクルードするケースとの一貫性のためです。そのケースでは、リソース名は root からの相対パスではなく、 "/" をつけることで root からの相対パスとなります。

<!--
URLs:
-->
URL については、

<!--
 - for both filesystem files and Java resources, if the
   included name is a URL (begins with a protocol), it would
   be reasonable behavior to try to load the URL rather than
   treating the name as a filename or resource name.
 - for files loaded from a URL, "adjacent to" should be based
   on parsing the URL's path component, replacing the last
   path element with the included name.
 - file: URLs should behave in exactly the same way as a plain
   filename
-->
 - ファイルシステム上のファイルと Java のリソースの両方について、インクルードされた名前が URL（プロトコル名で始まる）の場合、名前はファイル名やリソース名ではなく URL として解釈・ロードされるでしょう。
 - URL からロードされたファイルについては、「隣接」は URL のパス部分をパースして最終要素をインクルードされた名前に置き換えることで計算されます。
 - file: プロトコルの URL が指定された場合は、単なるファイル名が指定された場合と全く同じ振る舞いになります。

<!--
## Duration format
-->
## 期間指定用フォーマット

<!--
The supported unit strings for duration are case sensitive and must be lowercase. Exactly these strings are supported:
-->
期間指定に利用する単位時間を表す文字列は、大文字・小文字の区別あり、かつ全ての小文字で記述されなければなりません。以下の文字列がサポートされています。

 - `ns`, `nanosecond`, `nanoseconds`
 - `us`, `microsecond`, `microseconds`
 - `ms`, `millisecond`, `milliseconds`
 - `s`, `second`, `seconds`
 - `m`, `minute`, `minutes`
 - `h`, `hour`, `hours`
 - `d`, `day`, `days`

<!--
## Size in bytes format
-->
## サイズのバイト指定用フォーマット

<!--
For single bytes, exactly these strings are supported:
-->
バイトについては、以下の文字列がサポートされています。

 - `B`, `b`, `byte`, `bytes`

<!--
For powers of ten, exactly these strings are supported:
-->
10の累乗については、以下の文字列がサポートされています。

 - `kB`, `kilobyte`, `kilobytes`
 - `MB`, `megabyte`, `megabytes`
 - `GB`, `gigabyte`, `gigabytes`
 - `TB`, `terabyte`, `terabytes`
 - `PB`, `petabyte`, `petabytes`
 - `EB`, `exabyte`, `exabytes`
 - `ZB`, `zettabyte`, `zettabytes`
 - `YB`, `yottabyte`, `yottabytes`

<!--
For powers of two, exactly these strings are supported:
-->
2の累乗については、以下の文字列がサポートされています。

 - `K`, `k`, `Ki`, `KiB`, `kibibyte`, `kibibytes`
 - `M`, `m`, `Mi`, `MiB`, `mebibyte`, `mebibytes`
 - `G`, `g`, `Gi`, `GiB`, `gibibyte`, `gibibytes`
 - `T`, `t`, `Ti`, `TiB`, `tebibyte`, `tebibytes`
 - `P`, `p`, `Pi`, `PiB`, `pebibyte`, `pebibytes`
 - `E`, `e`, `Ei`, `EiB`, `exbibyte`, `exbibytes`
 - `Z`, `z`, `Zi`, `ZiB`, `zebibyte`, `zebibytes`
 - `Y`, `y`, `Yi`, `YiB`, `yobibyte`, `yobibytes`

<!--
## Conventional override by system properties
-->
## 慣習としてのシステムプロパティによる上書き

<!--
For an application's config, Java system properties _override_ settings found in the configuration file. This supports specifying config options on the command line.
-->
アプリケーションの設定において、Java のシステムプロパティは設定ファイルに記述された設定を _上書き_ します。これにより、設定オプションをコマンドラインから指定することができます。
