<!-- translated -->
<!-- # <a name="json-to-json">JSON transformers</a> -->
# <a name="json-to-json">JSON トランスフォーマー</a>

<!-- > Please note this documentation was initially published as an article by Pascal Voitot ([@mandubian](http://www.github.com/mandubian)) on [mandubian.com](http://mandubian.com/2012/10/29/unveiling-play-2-dot-1-json-api-part3-json-transformers/)  -->
> このドキュメントの初出は Pascal Voitot ([@mandubian](http://www.github.com/mandubian)) の記事 [mandubian.com](http://mandubian.com/2012/10/29/unveiling-play-2-dot-1-json-api-part3-json-transformers/) であることにご注意ください

<!-- Now you should know how to validate JSON and convert into any structure you can write in Scala and back to JSON. But as soon as I've begun to use those combinators to write web applications, I almost immediately encountered a case : read JSON from network, validate it and convert it into… JSON.  -->
ここまでで、JSON のバリデーションを行い、Scala で記述できるあらゆる構造に変換し、そして JSON に書き戻す方法は分かったはずです。しかし、これらのコンビネータを使って web アプリケーションを書き始めたとき、ほとんどすぐに私はあるケースに遭遇しました : ネットワークから JSON を読み込み、バリデーションを行い、そしてそれを変換するのです… JSON に。


<!-- ## <a name="json-to-json">Introducing JSON _coast-to-coast_ design</a> -->
## <a name="json-to-json">_端から端への_ JSON 設計の紹介</a>

<!-- ### <a name="doomed-to-OO">Are we doomed to convert JSON to OO?</a> -->
### <a name="doomed-to-OO">JSON からオブジェクト指向への変換は絶望的?</a>

<!-- For a few years now, in almost all web frameworks (except recent JS serverside stuff maybe in which JSON is the default data structure), we have been used to get JSON from network and **convert JSON (or even POST/GET data) into OO structures** such as classes (or case classes in Scala). Why?   -->
ここ数年、(JSON をデフォルトのデータ構造とする最近のサーバサイド JS フレームワークを除く) ほとんどすべてのフレームワークにおいて、ネットワークから JSON を取得し、**そして JSON を (あるいは POST/GET データさえも) クラス (または Scala ではケースクラス) のようなオブジェクト指向構造に変換** してきました。なぜでしょう?

<!-- - for a good reason : **OO structures are "language-native"** and allows **manipulating data with respect to your business logic** in a seamless way while ensuring isolation of business logic from web layers.
- for a more questionable reason : **ORM frameworks talk to DB only with OO structures** and we have (kind of) convinced ourselves that it was impossible to do else… with the well-known good & bad features of ORMs… (not here to criticize those stuff) -->
- 妥当な理由 : **オブジェクト指向構造は "プログラミング言語ネイティブ"** であり、ビジネスロジックを web 層から隔離しつつ、自然な方法で **ビジネスロジックに対してデータを操作する** ことができるため
- より疑わしい理由 : **ORM フレームワークはオブジェクト指向構造を通してしか DB とやり取りできない**、他のやり方なんて不可能だ …ORM のよく知られた良い機能や悪い機能と付き合いながらね…と自分たちを (ある程度) 納得させているため (ここで ORM を批判しているわけではありません)


<!-- ### <a name="is-default-case">Is OO conversion really the default usecase?</a> -->
### <a name="is-default-case">オブジェクト指向への変換は本当にデフォルトのユースケースか?</a>

<!-- **In many cases, you don't really need to perform any real business logic with data but validating/transforming before storing or after extracting.**   -->
**ほとんどの場合、データと共に本物のビジネスロジックを実行する必要は無く、データを格納する前や展開した後で、バリデーション/変換を行う必要があるだけです。**

<!-- Let's take the CRUD case:  -->
CRUD の場合を取り上げてみましょう:

<!-- - You just get the data from the network, validate them a bit and insert/update into DB. 
- In the other way, you just retrieve data from DB and send them outside.   -->
- ネットワークからデータをただ取得し、ちょろっとバリデーションして DB にinsert/update する
- 逆に、DB からデータをただ検索し、それらを外部に送信する

<!-- So, generally, for CRUD ops, you convert JSON into a OO structure just because the frameworks are only able to speak OO. -->
このように、一般的に CRUD 操作において JSON をオブジェクト指向構造に変換するのは、フレームワークがオブジェクト指向的にしか会話しないためです。

<!-- >**I don't say or pretend you shouldn't use JSON to OO conversion but maybe this is not the most common case and we should keep conversion to OO only when we have real business logic to fulfill.** -->
>**JSON からオブジェクト指向構造への変換を使うべきではないと言ったり、取り繕ったりするわけではありませんが、多くの場合においてオブジェクト指向構造への変換は必要はなく、満たすべき本物のビジネスロジックが存在するときのみに止めるべきです。**


<!-- ### <a name="new-players">New tech players change the way of manipulating JSON</a> -->
### <a name="new-players">新たな技術者達が JSON の操作方法を変える</a>

<!-- Besides this fact, we have some new DB types such as Mongo (or CouchDB) accepting document structured data looking almost like JSON trees (_isn't BSON, Binary JSON?_).  
With these DB types, we also have new great tools such as [ReactiveMongo](http://www.reactivemongo.org) which provides reactive environment to stream data to and from Mongo in a very natural way.  
I've been working with Stephane Godbillon to integrate ReactiveMongo with Play2.1 while writing the [Play2-ReactiveMongo module](https://github.com/zenexity/Play-ReactiveMongo). Besides Mongo facilities for Play2.1, this module provides _Json To/From BSON conversion typeclasses_.   -->
事実、Mongo (または CouchDB) のような、ほとんど JSON (_BSON, つまり Binary JSON ですよね?_) ツリーのように見えるドキュメント構造のデータを受け入れる新しいタイプの DB があります。
このようなタイプの DB　のために、とても自然なやり方で Mongo とストリームデータをやり取りすることのできる、リアクティブな環境を提供する [ReactiveMongo](http://www.reactivemongo.org) のような、新しくて素晴らしいツールもあります。
私は [Play2-ReactiveMongo module](https://github.com/zenexity/Play-ReactiveMongo) を書きながら、Play2.1 と共に ReactiveMongo を実装するために Stephane Godbillon と共に働きました。Play2.1 における Mongo の容易さに加えて、このモジュールは _Json から BSON に、または BSON から Json に変換する型クラス_ を提供します。


<!-- > **So it means you can manipulate JSON flows to and from DB directly without even converting into OO.** -->
> **これは、DB に対する JSON データフローのやり取りを、オブジェクト指向構造に変換することなく直接操作できることを意味しています。**

<!-- ### <a name="new-players">JSON _coast-to-coast_ design</a> -->
### <a name="new-players">_端から端への_ JSON 設計</a>

<!-- Taking this into account, we can easily imagine the following:  -->
JSON のことを考慮すると、次のようなことをぱっと思い付きます:

<!-- - receive JSON,
- validate JSON,
- transform JSON to fit expected DB document structure,
- directly send JSON to DB (or somewhere else) -->
- JSON を受け取り、
- JSON のバリデーションを行い、
- DB のドキュメント構造に合うように JSON を変換して、
- JSON を直接 DB (またはその他のどこか) に送る

<!-- This is exactly the same case when serving data from DB: -->
これは DB からデータを提供するときとまったく同じです:

<!-- - extract some data from DB as JSON directly,
- filter/transform this JSON to send only mandatory data in the format expected by the client (for ex, you don't want some secure info to go out),
- directly send JSON to the client -->
- DB からいくつかのデータを JSON として直接抽出して、
- クライアントが期待するフォーマット (例えば、セキュリティに関する情報は外部に送信したくありません) にて要求されたデータのみを送信するために JSON をフィルタリング/変換して、
- クライアントに JSON を直接送信する

<!-- In this context, we can easily imagine **manipulating a flow of JSON data** from client to DB and back without any (explicit) transformation in anything else than JSON.  
Naturally, when you plug this transformation flow on **reactive infrastructure provided by Play2.1**, it suddenly opens new horizons. -->
この文脈において、JSON ではない何かへのあらゆる (明示的な) 変換を伴わずに、クライアントから DB へ、そしてその逆方向へ **JSON データフローを操作** できたらと容易に想像します。
この変換フローを **Play2.1 が提供するリアクティブなインフラ** にごく自然に当てはめたとき、新たな地平線が突然開けます。

<!-- > This is the so-called (by me) **JSON coast-to-coast design**: 
> 
> - Don't consider JSON data chunk by chunk but as a **continuous flow of data from client to DB (or else) through server**,
> - Treat the **JSON flow like a pipe that you connect to others pipes** while applying modifications, transformations alongside,
> - Treat the flow in a **fully asynchronous/non-blocking** way.
>
> This is also one of the reason of being of Play2.1 reactive architecture…  
> I believe **considering your app through the prism of flows of data changes drastically the way you design** your web apps in general. It may also open new functional scopes that fit today's webapps requirements quite better than classic architecture. Anyway, this is not the subject here ;) -->
> このため、これは **端から端への JSON 設計** と (私によって) 呼ばれています:
>
> - JSON データは、その都度ごとのチャンクと捉えるのではなく、**サーバーを経由したクライアントから DB (またはその他) への継続的なデータフロー** と考えてください
> - 変更や変換を適用している間、並行して **JSON フローを、その他のパイプにつなぐパイプ** として取り扱ってください
> - フローは **完全に非同期/ノンブロッキング** な方法で取り扱ってください
>
> これもまた、Play2.1 がリアクティブなアーキテクチャである理由のひとつです…
> **データフローのプリズムを通じてアプリケーションを考慮することは、概して web アプリケーションの設計方法を劇的に変化させる** と信じています。これまでのアーキテクチャよりもずっと良く今日の web アプリケーションの要件にフィットする機能的なスコープを切り開くかもしれません。まあこれはここで話すべきことではありません ;)

<!-- So, as you have deduced by yourself, to be able to manipulate Json flows based on validation and transformation directly, we needed some new tools. JSON combinators were good candidates but they are a bit too generic.  
That's why we have created some specialized combinators and API called **JSON transformers** to do that. -->
このため、あなたがご自身で推論したように、バリデーションおよび変換に基づいた Json フローを直接操作できるようにするために、新しいツールが必要です。JSON コンビネータは良い候補でしたが、少し総称的過ぎます。これが、これを行う **JSON トランスフォーマー** と呼ばれる特別なコンビネータと API を作った理由です。

<br/>
<!-- # <a name="json-transf-are-reads">JSON transformers are `Reads[T <: JsValue]`</a> -->
# <a name="json-transf-are-reads">JSON トランスフォーマーは `Reads[T <: JsValue]`</a>

<!-- You may tell JSON transformers are just `f:JSON => JSON`.  
So a JSON transformer could be simply a `Writes[A <: JsValue]`.  
But, a JSON transformer is not only a function: as we said, we also want to validate JSON while transforming it.  
As a consequence, a JSON transformer is a `Reads[A <: Jsvalue]`. -->
JSON トランスフォーマーは単なる `f:JSON => JSON` であると言うかもしれません。
このため、JSON トランスフォーマーは `Writes[A <: JsValue]` を簡単にすることができるだろうと。
しかし、JSON トランスフォーマーは関数だけではありません: すでに述べたように、JSON を変換している間にバリデーションも行いたいと考えています。
結論として、JSON トランスフォーマーは `Reads[A <: Jsvalue]` なのです。

<!-- > **Keep in mind that a Reads[A <: JsValue] is able to transform and not only to read/validate** -->
> **Reads[A <: JsValue] は、読み込み/変換するだけではなく、変換もできることを忘れないでください**

<!-- ## <a name="step-pick">Use `JsValue.transform` instead of `JsValue.validate`</a> -->
## <a name="step-pick">`JsValue.validate` の代わりに `JsValue.transform` を使う</a>

<!-- We have provided a function helper in `JsValue` to help people consider a `Reads[T]` is a transformer and not only a validator: -->
人々が `Reads[T]` を単なるバリデータではなく、トランスフォーマーと見なしやすくするために、`JsValue` にヘルパー関数を提供しました:

`JsValue.transform[A <: JsValue](reads: Reads[A]): JsResult[A]`

<!-- This is exactly the same `JsValue.validate(reads)` -->
これは `JsValue.validate(reads)` とまったく同じです

<!-- ## <a name="step-pick">Case 1: Pick JSON value in JsPath</a> -->
## <a name="step-pick">ケース 1: JsPath にある JSON をつまみ出す</a>

<!-- ### <a name="step-pick-1">Pick value as JsValue</a> -->
### <a name="step-pick-1">JsValue として値をつまみ出す</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key2 \ 'key23).json.pick

scala> json.transform(jsonTransformer)
res9: play.api.libs.json.JsResult[play.api.libs.json.JsValue] = 
	JsSuccess(
	  ["alpha","beta","gamma"],
	  /key2/key23
	)	
```

####`(__ \ 'key2 \ 'key23).json...` 
<!--   - All JSON transformers are in `JsPath.json.` -->
  - すべての JSON トランスフォーマーは `JsPath.json.` に存在します。

####`(__ \ 'key2 \ 'key23).json.pick` 
<!--   - `pick` is a `Reads[JsValue]` which picks the value **IN** the given JsPath. Here `["alpha","beta","gamma"]` -->
  - `pick` は、与えられた JsPath に **含まれる** 値をつまみ出す `Reads[JsValue]` です。この場合は `["alpha","beta","gamma"]` です。
  
####`JsSuccess(["alpha","beta","gamma"],/key2/key23)`
<!--   - This is a simply successful `JsResult`
  - For info, `/key2/key23` represents the JsPath where data were read but don't care about it, it's mainly used by Play API to compose JsResult(s))
  - `["alpha","beta","gamma"]` is just due to the fact that we have overriden `toString` -->
  - `JsResult` を簡易化します
  - ちなみに、`/key2/key23` はデータが読みだされた JsPath を表現しますが、これは主に Play API が JsResult を組み上げるために利用するものなので、気にしないでください。
  - 単に `toString` をオーバーライドしたために `["alpha","beta","gamma"]` となっています

<br/>
<!-- > **Reminder** 
> `jsPath.json.pick` gets ONLY the value inside the JsPath -->
> **リマインダー**
> `jsPath.json.pick` は JsPath に含まれる値 **だけ** を取得します
<br/>


<!-- ### <a name="step-pick-2">Pick value as Type</a> -->
### <a name="step-pick-2">値を型としてつまみ出す</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key2 \ 'key23).json.pick[JsArray]

scala> json.transform(jsonTransformer)
res10: play.api.libs.json.JsResult[play.api.libs.json.JsArray] = 
	JsSuccess(
	  ["alpha","beta","gamma"],
	  /key2/key23
	)
```

####`(__ \ 'key2 \ 'key23).json.pick[JsArray]` 
<!--   - `pick[T]` is a `Reads[T <: JsValue]` which picks the value (as a `JsArray` in our case) **IN** the given JsPath -->
  - `pick[T]` は、与えられた JsPath に **含まれる** 値を (この場合は `JsArray` として) つまみ出す `Reads[T <: JsValue]` です。

<br/>
<!-- > **Reminder**
> `jsPath.json.pick[T <: JsValue]` extracts ONLY the typed value inside the JsPath -->
> **リマインダー**
> `jsPath.json.pick[T <: JsValue]` は JsPath に含まれる型付けされた値 **だけ** を取得します
<br/>

<!-- ## <a name="step-pickbranch">Case 2: Pick branch following JsPath</a> -->
## <a name="step-pickbranch">ケース 2: JsPath に従ってブランチをつまみ出す</a>

<!-- ### <a name="step-pickbranch-1">Pick branch as JsValue</a> -->
### <a name="step-pickbranch-1">ブランチを JsValue としてつまみ出す</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key2 \ 'key24 \ 'key241).json.pickBranch

scala> json.transform(jsonTransformer)
res11: play.api.libs.json.JsResult[play.api.libs.json.JsObject] = 
  JsSuccess(
	{
	  "key2": {
	    "key24":{
	      "key241":234.123
	    }
	  }
	},
	/key2/key24/key241
  )

```

####`(__ \ 'key2 \ 'key23).json.pickBranch` 
<!--   - `pickBranch` is a `Reads[JsValue]` which picks the branch from root to given JsPath -->
  - `pickBranch` は、ルートから与えらえた JsPath までのブランチをつまみ出す `Reads[JsValue]` です
  
####`{"key2":{"key24":{"key242":"value242"}}}`
<!--   - The result is the branch from root to given JsPath including the JsValue in JsPath -->
  - この結果は、JsPath 内の JsValue を含む、ルートから与えられた JsPath までのブランチです
  
<br/>
<!-- > **Reminder:**
> `jsPath.json.pickBranch` extracts the single branch down to JsPath + the value inside JsPath -->
> **リマインダー**
> `jsPath.json.pickBranch` は、JsPath まで掘り下げていくひとつのブランチと、JsPath に含まれる値を展開します
<br/>


<!-- ## <a name="step-copyfrom">Case 3: Copy a value from input JsPath into a new JsPath</a> -->
## <a name="step-copyfrom">ケース 3: 入力の JsPath から新しい JsPath に値をコピーする</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key25 \ 'key251).json.copyFrom( (__ \ 'key2 \ 'key21).json.pick )

scala> json.transform(jsonTransformer)
res12: play.api.libs.json.JsResult[play.api.libs.json.JsObject] 
  JsSuccess( 
    {
      "key25":{
        "key251":123
      }
    },
    /key2/key21
  )

```

####`(__ \ 'key25 \ 'key251).json.copyFrom( reads: Reads[A <: JsValue] )` 
<!--   - `copyFrom` is a `Reads[JsValue]`
  - `copyFrom` reads the JsValue from input JSON using provided Reads[A]
  - `copyFrom` copies this extracted JsValue as the leaf of a new branch corresponding to given JsPath -->
  - `copyFrom` は `Reads[JsValue]` です
  - `copyFrom` は、提供されている Reads[A] を使って入力された JSON から JsValue を読み込みます
  - `copyFrom` は、抽出された JsValue を与えられた JsPath に対応する新しいブランチのリーフとしてコピーします
  
####`{"key25":{"key251":123}}`
<!--   - `copyFrom` reads value `123` 
  - `copyFrom` copies this value into new branch `(__ \ 'key25 \ 'key251)` -->
  - `copyFrom` は、値 `123` を読み込みます
  - `copyFrom` は、この値を新しいブランチ `(__ \ 'key25 \ 'key251)` にコピーします

<!-- > **Reminder:**
> `jsPath.json.copyFrom(Reads[A <: JsValue])` reads value from input JSON and creates a new branch with result as leaf -->
> **リマインダー**
> `jsPath.json.copyFrom(Reads[A <: JsValue])` は、入力された JSON から値を読み出し、この結果をリーフとして新しいブランチを作ります

<br/>
<!-- ## <a name="step-update">Case 4: Copy full input Json & update a branch</a> -->
## <a name="step-update">ケース 4: 入力された JSON 全体のコピーとブランチの更新</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key2 \ 'key24).json.update( 
	__.read[JsObject].map{ o => o ++ Json.obj( "field243" -> "coucou" ) }
)

scala> json.transform(jsonTransformer)
res13: play.api.libs.json.JsResult[play.api.libs.json.JsObject] = 
  JsSuccess(
    {
      "key1":"value1",
      "key2":{
        "key21":123,
        "key22":true,
        "key23":["alpha","beta","gamma"],
        "key24":{
          "key241":234.123,
          "key242":"value242",
          "field243":"coucou"
        }
      },
      "key3":234
    },
  )

```

####`(__ \ 'key2).json.update(reads: Reads[A < JsValue])` 
<!-- - is a `Reads[JsObject]` -->
- これは `Reads[JsObject]` です

<!-- ####`(__ \ 'key2 \ 'key24).json.update(reads)` does 3 things: 
- extracts value from input JSON at JsPath `(__ \ 'key2 \ 'key24)`
- applies `reads` on this relative value and re-creates a branch `(__ \ 'key2 \ 'key24)` adding result of `reads` as leaf
- merges this branch with full input JSON replacing existing branch (so it works only with input JsObject and not other type of JsValue) -->
####`(__ \ 'key2 \ 'key24).json.update(reads)` は三つのことを行います:
- 入力された JSON の JsPath `(__ \ 'key2 \ 'key24)` にある値を抽出します
- この関連した値に `reads` を適用し、この `reads` の結果を再作成した新しいブランチ `(__ \ 'key2 \ 'key24)` にリーフとして追加します
- 既存のブランチを置き換えながら、入力された JSON 全体にこの新しいブランチをマージします (このため、これは JsObject でのみ動作し、その他の種類の JsValue では動作しません)

####`JsSuccess({…},)`
<!-- - Just for info, there is no JsPath as 2nd parameter there because the JSON manipulation was done from Root JsPath -->
- 参考までに、ここでの JSON 操作はルートの JsPath で行われるため、第二引数としての JsPath は存在しません

<br/>
<!-- > **Reminder:**
> `jsPath.json.update(Reads[A <: JsValue])` only works for JsObject, copies full input `JsObject` and updates jsPath with provided `Reads[A <: JsValue]` -->
> **リマインダー**
> `jsPath.json.update(Reads[A <: JsValue])` は JsObject に対してのみ動作し、提供されている `Reads[A <: JsValue]` で入力された `JsObject` 全体をコピーして、JsPath を更新します

<br/>
<!-- ## <a name="step-put">Case 5: Put a given value in a new branch</a> -->
## <a name="step-put">ケース 5: 新しいブランチに与えられた値を設定する</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key24 \ 'key241).json.put(JsNumber(456))

scala> json.transform(jsonTransformer)
res14: play.api.libs.json.JsResult[play.api.libs.json.JsObject] = 
  JsSuccess(
    {
      "key24":{
        "key241":456
      }
    },
  )

```

####`(__ \ 'key24 \ 'key241).json.put( a: => JsValue )` 
<!-- - is a Reads[JsObject] -->
- これは Reads[JsObject] です

####`(__ \ 'key24 \ 'key241).json.put( a: => JsValue )`
<!-- - creates a new branch `(__ \ 'key24 \ 'key241)`
- puts `a` as leaf of this branch. -->
- 新しいブランチ `(__ \ 'key24 \ 'key241)` を作成します
- `a` をこのブランチのリーフとして追加します

####`jsPath.json.put( a: => JsValue )`
<!-- - takes a JsValue argument passed by name allowing to pass even a closure to it. -->
- 名前で渡された JsValue 引数を取り、JsValue に対するクロージャでさえも渡すことができます。

####`jsPath.json.put` 
<!-- - doesn't care at all about input JSON
- simply replace input JSON by given value -->
- 入力された JSON についてまったく気にしません
- 与えられた値で入力された JSON をシンプルに置き換えます

<br/>
<!-- > **Reminder: **
> `jsPath.json.put( a: => Jsvalue )` creates a new branch with a given value without taking into account input JSON -->
> **リマインダー**
> `jsPath.json.put( a: => Jsvalue )` は、入力の JSON について考慮することなしに、与えられた値で新しいブランチを作成します

<br/>
<!-- ## <a name="step-prune">Case 6: Prune a branch from input JSON</a> -->
## <a name="step-prune">ケース 6: 入力 された JSON からブランチを刈り取る</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key2 \ 'key22).json.prune

scala> json.transform(jsonTransformer)
res15: play.api.libs.json.JsResult[play.api.libs.json.JsObject] = 
  JsSuccess(
    {
      "key1":"value1",
      "key3":234,
      "key2":{
        "key21":123,
        "key23":["alpha","beta","gamma"],
        "key24":{
          "key241":234.123,
          "key242":"value242"
        }
      }
    },
    /key2/key22/key22
  )

```

####`(__ \ 'key2 \ 'key22).json.prune` 
<!-- - is a `Reads[JsObject]` that works only with JsObject -->
- これは JsObject に対してのみ動作する `Reads[JsObject]` です

####`(__ \ 'key2 \ 'key22).json.prune`
<!-- - removes given JsPath from input JSON (`key22` has disappeared under `key2`) -->
- 入力された JSON から与えられた JsPath を削除します (`key2` から `key22` が消えています)

<!-- Please note the resulting JsObject hasn't same keys order as input JsObject. This is due to the implementation of JsObject and to the merge mechanism. But this is not important since we have overriden `JsObject.equals` method to take this into account. -->
結果の JsObject のキーの並びが入力された JsObject のものと同じでないことに注意してください。これは JsObject の実装とマージ機構によるものです。しかし、これを考慮に入れて `JsObject.equals` をオーバーライドしているので、このことは重要ではありません。

<!-- > **Reminder:**
> `jsPath.json.prune` only works with JsObject and removes given JsPath form input JSON)  
> 
> Please note that:
> - `prune` doesn't work for recursive JsPath for the time being
> - if `prune` doesn't find any branch to delete, it doesn't generate any error and returns unchanged JSON. -->
> **リマインダー**
> `jsPath.json.prune`  は JsObject に対してのみ動作し、入力された JSON から与えられた JsPath を削除します。
> 
> 以下に注意してください:
> - `prune` は今のところ JsPath に対して再帰的には動作しません
> - `prune` が削除するブランチを見つけなかった場合、一切のエラーを生成せず、変更されていない JSON を返します。

<!-- # <a name="more-complicated">More complicated cases</a> -->
# <a name="more-complicated">より複雑なケース</a>

<!-- ## <a name="more-complicated-pick-update">Case 7: Pick a branch and update its content in 2 places</a> -->
## <a name="more-complicated-pick-update">ケース 7: 二箇所にあるブランチの内容の取得と更新</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key2).json.pickBranch(
  (__ \ 'key21).json.update( 
    of[JsNumber].map{ case JsNumber(nb) => JsNumber(nb + 10) }
  ) andThen 
  (__ \ 'key23).json.update( 
    of[JsArray].map{ case JsArray(arr) => JsArray(arr :+ JsString("delta")) }
  )
)

scala> json.transform(jsonTransformer)
res16: play.api.libs.json.JsResult[play.api.libs.json.JsObject] = 
  JsSuccess(
    {
      "key2":{
        "key21":133,
        "key22":true,
        "key23":["alpha","beta","gamma","delta"],
        "key24":{
          "key241":234.123,
          "key242":"value242"
        }
      }
    },
    /key2
  )

```

####`(__ \ 'key2).json.pickBranch(reads: Reads[A <: JsValue])` 
<!-- - extracts branch `__ \ 'key2` from input JSON and applies `reads` to the relative leaf of this branch (only to the content) -->
- 入力された JSON から `__ \ 'key2` ブランチを抽出し、このブランチの関連するリーフに `reads` (の内容のみ) を適用します

####`(__ \ 'key21).json.update(reads: Reads[A <: JsValue])` 
<!-- - updates `(__ \ 'key21)` branch -->
- `(__ \ 'key21)` ブランチを更新します

####`of[JsNumber]` 
<!-- - is just a `Reads[JsNumber]` 
- extracts a JsNumber from `(__ \ 'key21)` -->
- これは、ただの `Reads[JsNumber]` です
- `(__ \ 'key21)` から JsNumber を抽出します

####`of[JsNumber].map{ case JsNumber(nb) => JsNumber(nb + 10) }` 
<!-- - reads a JsNumber (_value 123_ in `__ \ 'key21`)
- uses `Reads[A].map` to increase it by _10_ (in immutable way naturally) -->
- JsNumber (`__ \ 'key21` にある _値 123_) を読み込みます
- 値を _10_ だけ (自然にイミュータブルな方法で) 増やすために `Reads[A].map` を使います

####`andThen` 
<!-- - is just the composition of 2 `Reads[A]`
- first reads is applied and then result is piped to second reads -->
- これは、単に二つの `Reads[A]` を合成します
- ひとつめの reads が適用され、それからその結果が二つ目の reads にパイプされます

####`of[JsArray].map{ case JsArray(arr) => JsArray(arr :+ JsString("delta")` 
<!-- - reads a JsArray (_value [alpha, beta, gamma] in `__ \ 'key23`_)
- uses `Reads[A].map` to append `JsString("delta")` to it -->
- JsArray (_`__ \ 'key23` にある値 [alpha, beta, gamma]_) を読み込みます
- これに `JsString("delta")` を追加するために `Reads[A].map` を使います

<!-- >Please note the result is just the `__ \ 'key2` branch since we picked only this branch -->
>`__ \ 'key2` ブランチだけをつまみ出したので、結果もこのブランチだけになることに注意してください

<br/>
<!-- ## <a name="more-complicated-pick-prune">Case 8: Pick a branch and prune a sub-branch</a> -->
## <a name="more-complicated-pick-prune">ケース 8: ブランチをつまみ出してサブブランチを刈り取る</a>

```
import play.api.libs.json._

val jsonTransformer = (__ \ 'key2).json.pickBranch(
  (__ \ 'key23).json.prune
)

scala> json.transform(jsonTransformer)
res18: play.api.libs.json.JsResult[play.api.libs.json.JsObject] = 
  JsSuccess(
    {
      "key2":{
        "key21":123,
        "key22":true,
        "key24":{
          "key241":234.123,
          "key242":"value242"
        }
      }
    },
    /key2/key23
  )

```

####`(__ \ 'key2).json.pickBranch(reads: Reads[A <: JsValue])` 
<!-- - extracts branch `__ \ 'key2` from input JSON and applies `reads` to the relative leaf of this branch (only to the content) -->
- 入力された JSON から `__ \ 'key2` ブランチを抽出し、このブランチの関連するリーフに `reads` (の内容のみ) を適用します

####`(__ \ 'key23).json.prune` 
<!-- - removes branch `__ \ 'key23` from relative JSON -->
- 関連する JSON から `__ \ 'key23` ブランチを削除します

<!-- >Please remark the result is just the `__ \ 'key2` branch without `key23` field. -->
>この結果が、単に `key23` フィールドのない `__ \ 'key2` ブランチであることに注目してください


<!-- # <a name="combinators">What about combinators?</a> -->
# <a name="combinators">コンビネータは?</a>

<!-- I stop there before it becomes boring (if not yet)… -->
退屈する前にやめておきます (もしまだ退屈していないなら、ですけど) …

<!-- Just keep in mind that you have now a huge toolkit to create generic JSON transformers.  
You can compose, map, flatmap transformers together into other transformers. So possibilities are almost infinite. -->
今や汎用的な JSON トランスフォーマーを作り出すたくさんのツールキットを手に入れたことを覚えておいてください。
トランスフォーマーを他のトランスフォーマーに合成し、はめ込み、展開することができます。そう、可能性はほとんど無限です。

<!-- But there is a final point to treat: mixing those great new JSON transformers with previously presented Reads combinators.
This is quite trivial as JSON transformers are just `Reads[A <: JsValue]` -->
ただし、最後に気を付けなければならない点が一点あります: これらの新しくてすごい JSON トランスフォーマーと、以前に存在していた Reads コンビネータを混ぜる場合です。
JSON トランスフォーマーは単なる `Reads[A <: JsValue]` なので、これはまったく些細なことです。

<!-- Let's demonstrate by writing a __Gizmo to Gremlin__ JSON transformer. -->
__ギズモからグレムリン__ JSON トランスフォーマーを書いて実証しましょう。

<!-- Here is Gizmo: -->
これがギズモです:

```
val gizmo = Json.obj(
  "name" -> "gizmo",
  "description" -> Json.obj(
    "features" -> Json.arr( "hairy", "cute", "gentle"),
    "size" -> 10,
    "sex" -> "undefined",
    "life_expectancy" -> "very old",
    "danger" -> Json.obj(
      "wet" -> "multiplies",
      "feed after midnight" -> "becomes gremlin"
    )
  ),
  "loves" -> "all"
)
```

<!-- Here is Gremlin: -->
これがグレムリンです:

```
val gremlin = Json.obj(
  "name" -> "gremlin",
  "description" -> Json.obj(
    "features" -> Json.arr("skinny", "ugly", "evil"),
    "size" -> 30,
    "sex" -> "undefined",
    "life_expectancy" -> "very old",
    "danger" -> "always"
  ),
  "hates" -> "all"
)
```

<!-- Ok let's write a JSON transformer to do this transformation -->
それでは、この変換を行う JSON トランスフォーマーを書いてみましょう

```
import play.api.libs.json._
import play.api.libs.functional.syntax._

val gizmo2gremlin = (
	(__ \ 'name).json.put(JsString("gremlin")) and
	(__ \ 'description).json.pickBranch(
		(__ \ 'size).json.update( of[JsNumber].map{ case JsNumber(size) => JsNumber(size * 3) } ) and
		(__ \ 'features).json.put( Json.arr("skinny", "ugly", "evil") ) and
		(__ \ 'danger).json.put(JsString("always"))
		reduce
	) and
	(__ \ 'hates).json.copyFrom( (__ \ 'loves).json.pick )
) reduce

scala> gizmo.transform(gizmo2gremlin)
res22: play.api.libs.json.JsResult[play.api.libs.json.JsObject] = 
  JsSuccess(
    {
      "name":"gremlin",
      "description":{
        "features":["skinny","ugly","evil"],
        "size":30,
        "sex":"undefined",
        "life_expectancy":
        "very old","danger":"always"
      },
      "hates":"all"
    },
  )
```

<!-- Here we are ;)  
I'm not going to explain all of this because you should be able to understand now.  
Just remark: -->
はい、できた ;)
もう理解できるはずなので、これについてはまったく説明しないことにします。
以下に気を付けてください:

<!-- ####`(__ \ 'features).json.put(…)` is after `(__ \ 'size).json.update` so that it overwrites original `(__ \ 'features)` -->
####`(__ \ 'features).json.put(…)` は `(__ \ 'size).json.update` の後にあるので、オリジナルの `(__ \ 'features)` を上書きします
<br/>
####`(Reads[JsObject] and Reads[JsObject]) reduce` 
<!-- - It merges results of both `Reads[JsObject]` (JsObject ++ JsObject)
- It also applies the same JSON to both `Reads[JsObject]` unlike `andThen` which injects the result of the first reads into second one. -->
- 両方の `Reads[JsObject]` (JsObject ++ JsObject) の結果をマージします
- ひとつ目の reads をふたつ目の reads に挿入する `andThen` とは違い、両方の `Reads[JsObject]` に同じ JSON を適用します

<!-- > **Next:** [[JSON Macro Inception | ScalaJsonInception]] -->
> **Next:** [[Json マクロ入門 | ScalaJsonInception]]