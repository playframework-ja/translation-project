<!-- translated -->
<!--
# JSON Macro Inception
-->
# マクロによる JSON インセプション

<!--
> Please note this documentation was initially published as an article by Pascal Voitot ([@mandubian](http://www.github.com/mandubian)) on [mandubian.com](http://mandubian.com/2012/11/11/JSON-inception/) 
-->
> このドキュメントの初出は Pascal Voitot ([@mandubian](http://www.github.com/mandubian)) の記事 [mandubian.com](http://mandubian.com/2012/11/11/JSON-inception/) であることにご注意ください

<!--
> **This feature is still experimental even if working.**
-->
> **正しく動いているとしても、この機能はあくまで試験的なものです**

<!--
## <a name="wtf-inception-boring">Writing a default case class Reads/Writes/Format is so boring!</a>
-->
## <a name="wtf-inception-boring">ケースクラスのためにデフォルトの Reads/Writes/Format を書くのは面倒くさい!</a>

<!--
Remember how you write a `Reads[T]` for a case class.
-->
ケースクラスのための `Reads[T]` を書く方法を思い出してみましょう。


```
import play.api.libs.json._
import play.api.libs.functional.syntax._
_
case class Person(name: String, age: Int, lovesChocolate: Boolean)

implicit val personReads = (
	(__ \ 'name).read[String] and
	(__ \ 'age).read[Int] and
	(__ \ 'lovesChocolate).read[Boolean]
)(Person)
```

<!--
So you write 4 lines for this case class.  
You know what?  
We have had a few complaints from some people who think it's not cool to write a `Reads[TheirClass]` because usually Java JSON frameworks like Jackson or Gson do it behind the curtain without writing anything.  
We argued that Play2.1 JSON serializers/deserializers are:
-->
ひとつのケースクラスのために、4 行のコードを書きました。
あなたはどう思いますか?
人によっては<!--訳注：直訳すると「ある人々は〜と考えています」ですが、日本語でそういう表現はしないと思いますので、「人によっては〜と考えるようです」という表現をします--> `Reads[TheirClass]` を書くというのは格好良くない、と考えるようです。その理由は、Jackson や Gson のような Java の JSON フレームワークが通常そのようなコーディングを全く必要とせずに、カーテンの裏側<!--訳注：「behind the curtain/カーテンの裏側」という比喩表現に対応する日本語が思いつかなかったので、直訳しています-->でよしなに計らってくれるから、というものです。実際、そのように考えている人々からの改善要望も聞いていました。
そして、私たちは議論の結果、 Play 2.1 の JSON シリアライザ/デシリアライザを以下のようなものにしました。

<!--
- completely typesafe, 
- fully compiled,
- nothing was performed using introspection/reflection at runtime.  
-->
- 完全に型安全
- 全てがコンパイルされる
- 実行時にはイントロスペクション/リフレクションを利用した処理が一切行われない

しかし、一部の人にとっては、これら利点はケースクラスそれぞれについてのコード量増加を正当化するほどではありませんでした。
<!--
But for some, this didn’t justify the extra lines of code for case classes.
-->

一方で私達はこのアプローチ自体は正しいと信じているので、追加で次のものを提案しました。
<!--
We believe this is a really good approach so we persisted and proposed:
-->

<!--
- JSON simplified syntax
- JSON combinators
- JSON transformers
-->
- JSON 簡易文法
- JSON コンビネータ
- JSON 変換子

<!--
Added power, but nothing changed for the additional 4 lines.
-->
これらの追加によって、コード量の増加を抑えつつ、機能的には先ほどの「4 行の追加コード」と同じものを実現します。<!--Added powerを直訳して「力を追加して」と書くと日本語としてわけわからんので、意訳します-->

<!--
## <a name="wtf-inception-minimalist">Let's be minimalist</a>
-->
## <a name="wtf-inception-minimalist">ミニマリストになろう</a>
<!--
As we are perfectionist, now we propose a new way of writing the same code:
-->
私達は完璧主義者として、先ほどのコードの新しい記述方法を提案します。

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Person(name: String, age: Int, lovesChocolate: Boolean)

implicit val personReads = Json.reads[Person]
```

<!--
1 line only.  
Questions you may ask immediately:
-->
たったの 1 行です。
今、こんな疑問が浮かんだのではないかと思います。

<!--
> Does it use runtime bytecode enhancement? -> NO

> Does it use runtime introspection? -> NO

> Does it break type-safety? -> NO
-->
> 実行時のバイトコード書き換えをしている? -> いいえ

> 実行時イントロスペクションを使っている? -> いいえ

> 型安全性を壊している? -> いいえ

<!--
**So what?**
-->
**どういうこと?**

<!--
> After creating buzzword **JSON coast-to-coast design**, let's call it **JSON INCEPTION**.
-->
> 勝手に作った **端から端への JSON 設計** というバズワードに倣って、これを **JSON インセプション** と呼ぶことにしましょう。

<br/>
<br/>
<!--
# <a name="json-incept">JSON Inception</a>
-->
# <a name="json-incept">JSON インセプション</a>

<!--
## <a name="json-incept-eq">Code Equivalence</a>
-->
## <a name="json-incept-eq">等価なコードについて</a>

<!--
As explained just before:
-->
先ほどご説明したとおり、以下のコードと等価です。

```
implicit val personReads = Json.reads[Person]

// IS STRICTLY EQUIVALENT TO writing

implicit val personReads = (
	(__ \ 'name).read[String] and
	(__ \ 'age).read[Int] and
	(__ \ 'lovesChocolate).read[Boolean]
)(Person)
```	

<!--
## <a name="json-incept">Inception equation</a>
-->
## <a name="json-incept">インセプションの方程式</a>

<!--
Here is the equation describing the windy _Inception_ concept:
-->
これが颯爽と現れた _インセプション_ の概念を説明するための方程式です。

```
(Case Class INSPECTION) + (Code INJECTION) + (COMPILE Time) = INCEPTION
```

<br/>
<!--
####Case Class Inspection
-->
####ケースクラス・インスペクション
<!--
As you may deduce by yourself, in order to ensure preceding code equivalence, we need :

- to inspect `Person` case class, 
- to extract the 3 fields `name`, `age`, `lovesChocolate` and their types,
- to resolve typeclasses implicits,
- to find `Person.apply`.
-->
想像がつくかもしれませんが、先ほどのコード等価性を確保するためには、次のものが必要です。

- `Person` ケースクラスのインスペクション
- `name`, `age`, `lovesChocolate` という 3 つのフィールドとそれらの型
- 型クラスの implicit 値の解決
- `Person.apply` を見つける

<br/>
<!--
####INJECTION?
-->
####インジェクションとは?
<!--
No I stop you immediately…  
-->
先に断っておきますが…

<!--
>**Code injection is not dependency injection…**  
>No Spring behind inception… No IOC, No DI… No No No ;)  
-->
> **コードインジェクションとは、 DI のことではありません…**
> インセプションの裏に Spring はいません。IOC や DI はいるか、って?…いやいやいや ;)

<!--
I used this term on purpose because I know that injection is now linked immediately to IOC and Spring. But I'd like to re-establish this word with its real meaning.  
Here code injection just means that **we inject code at compile-time into the compiled scala AST** (Abstract Syntax Tree).
-->
私は、一般に「インジェクション」という言葉からは IOC や Spring がすぐ連想される、ということを理解しています。しかし、この用語の本来の意味を改めて確立しなおしたいと考えて、あえてこの用語を使います。ここでのコードインジェクションの意味は、**「コンパイル時に、コンパイル結果としての Scala の AST (Abstract Syntax Tree/抽象構文木) の中に、コードをインジェクトする」**です。


<!--
So `Json.reads[Person]` is compiled and replaced in the compile AST by:
-->
このインジェクションの結果、繰り返しになりますが、 `Json.reads[Person]` はコンパイルされ、コンパイル結果の AST の中で以下のコードに置換されます。

```
(
	(__ \ 'name).read[String] and
	(__ \ 'age).read[Int] and
	(__ \ 'lovesChocolate).read[Boolean]
)(Person)
```

<!--
Nothing less, nothing more…
-->
これ以上でも、これ以下でもありません…

<br/>
<!--
####COMPILE-TIME
Yes everything is performed at compile-time.  
No runtime bytecode enhancement.  
No runtime introspection.  
-->
####コンパイル時
はい、すべてはコンパイル時に行われます。
実行時のバイトコードエンハンスメントはありません。
実行時のイントロスペクションもありません。

<!--
> As everything is resolved at compile-time, you will have a compile error if you did not import the required implicits for all the types of the fields.
-->
全てがコンパイル時に解決されるため、全フィールドの全ての型に要求される implicit 値が import されていない場合、コンパイルエラーが発生します。

<br/>
<!--
# <a name="scala-macros">Json inception is Scala 2.10 Macros</a>
-->
# <a name="scala-macros">JSON インセプションは Scala 2.10 のマクロです</a>

<!--
We needed a Scala feature enabling:

- compile-time code enhancement
- compile-time class/implicits inspection
- compile-time code injection
-->
私達には以下の 3 つを実現できる Scala の機能が必要でした。

- コンパイル時のコードエンハンスメント
- コンパイル時の、クラス/implicit 値のイントロスペクション
- コンパイル時のコードインジェクション

<!--
This is enabled by a new experimental feature introduced in Scala 2.10: [Scala Macros](http://scalamacros.org/)  
-->
結果的には、JSON インセプションは Scala 2.10 で新たに導入された試験的な機能 [Scala マクロ](http://scalamacros.org/) によって実装されました。<!--冒頭の This は JSON インセプションのことを指していると解釈しました。JSON インセプション = コンパイル時のコードエンハンスメント/クラス等のイントロスペクション/コードインジェクション であり、Scalaの機能 = Scala マクロ がこれを enable する、という説明がなされているからです。Thisらしく「これは」と訳してもいいのですが、これが何を指しているのか日本語として明確でないと考えたため、「JSON インセプション」と明示的に書きます。-->

<!--
Scala macros is a new feature (still experimental) with a huge potential. You can :

- introspect code at compile-time based on Scala reflection API, 
- access all imports, implicits in the current compile context
- create new code expressions, generate compiling errors and inject them into compile chain.
-->
Scala マクロは大きなポテンシャルを持った (まだ試験的な) 新機能です。これにより以下のことができます。

- Scala リフレクション API によるコンパイル時のコードイントロスペクション
- 現在のコンパイル文脈<!--訳注：compile contextに対応する日本語の慣用句が思いつかないので、contextを文脈と直訳しました。意訳するなら「コンパイル時にスコープに存在するimport、implicit値を〜」で良いかもしれません。というか、「コンパイル文脈にあるimport、implicits」って意味わかんないですよねｗ日本語で説明するときにそんな表現しない。しかし、一方で Context は Scala リフレクション APIの用語でもあるので、Scala リフレクション API を知る人なら「文脈」って表現でわかるのかもしれません。Hmm。-->内で import されたものや implicit 値の参照
- 新たなコードやコンパイルエラーを生成して、それをコンパイルチェインへ差し込む

<!--
Please note that:

- **We use Scala Macros because it corresponds exactly to our requirements.**
- **We use Scala macros as an enabler, not as an end in itself.**
- **The macro is a helper that generates the code you could write by yourself.**
- **It doesn't add, hide unexpected code behind the curtain.**
- **We follow the *no-surprise* principle**
-->
また、補足として以下のことを知っておいてください。

- **Scala マクロを採用した理由は、それが要件にぴったり一致したからです**
- **要件を満たすためにとりあえず Scala マクロを使っただけで、これが最終形というわけではありません**
- **マクロは手でベタにかけるコードを生成するためのヘルパーです**
- **JSON インセプションは未知のコードをカーテン裏にこっそり追加するようなことはしません**
- **私たちは *驚き最小の原則* に従います**

<!--
As you may discover, writing a macro is not a trivial process since your macro code executes in the compiler runtime (or universe).  

    So you write macro code 
      that is compiled and executed 
      in a runtime that manipulates your code 
         to be compiled and executed 
         in a future runtime…           
**That's also certainly why I called it *Inception* ;)**
-->
お気づきかもしれませんが、マクロを書くというのは並大抵の仕事ではありません。マクロのコードがコンパイラーのランタイムに (または、 universe の中で) 実行されるからです。

    言い換えると、あなたの書くマクロコードは、
      それ自体がコンパイルされ、コンパイラの実行時に呼び出されて、
      他のコードを操作します。
        操作結果のコードはコンパイルされ、
        プログラムの実行時に呼び出されます…
**これは、一連の処理が*インセプション*と呼ばれる所以でもあります ;)**

<!--
So it requires some mental exercises to follow exactly what you do. The API is also quite complex and not fully documented yet. Therefore, you must persevere when you begin using macros.
-->
したがって、この仕事をやり遂げるには少し頭の体操が必要です。また、 API はかなり複雑で、ドキュメントも完全ではありません。そのため、マクロを使いはじめるにあたっては相応の努力をすることになります。

<!--
I'll certainly write other articles about Scala macros because there are lots of things to say.  
This article is also meant **to begin the reflection about the right way to use Scala Macros**.  
Great power means greater responsability so it's better to discuss all together and establish a few good manners…
-->
Scala マクロについてはまだまだ説明したいことが沢山あるので、きっと別の記事も書くと思います。
この記事には、**Scalaマクロの正しい使い方について熟考するきっかけ**になって欲しい、という想いも込められています。
強力な力にはより大きな責任が伴いますから、これから一緒に話し合いを重ねて、良い作法を少しずつ確立していければ何よりです。

<br/>
<!--
# <a name="writes-format">Writes[T] & Format[T]</a>
-->
# <a name="writes-format">Writes[T] と Format[T]</a>

<!--
>Please remark that JSON inception just works for case class having `unapply/apply` functions.

Naturally, you can also _incept_ `Writes[T]`and `Format[T]`.
-->
>JSON インセプションは `unapply/apply` 関数を持つケースクラスに対してのみ機能する、ということに気をつけてください

もちろん、`Writes[T]` や `Format[T]` を _インセプト_ することもできます。

## <a name="writes">Writes[T]</a>

```
import play.api.libs.json._
import play.api.libs.functional.syntax._
 
implicit val personWrites = Json.writes[Person]
```

## <a name="format">Format[T]</a>

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

implicit val personWrites = Json.format[Person]
```

<!--
> **Next:** [[Handling and serving JSON requests | ScalaJsonRequests]]
-->
> **次ページ:** [[JSON リクエストとレスポンス | ScalaJsonRequests]]
