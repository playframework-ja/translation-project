<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# Action composition
-->
# アクションの構成

<!--
This chapter introduces several ways of defining generic action functionality.
-->
この章では、汎用的なアクション機能を定義するいくつかの方法を紹介します。

<!--
## Custom action builders
-->
## 独自のアクションビルダー

<!--
We saw [[previously|ScalaActions]] that there are multiple ways to declare an action - with a request parameter, without a request parameter, with a body parser etc.  In fact there are more than this, as we'll see in the chapter on [[asynchronous programming|ScalaAsync]].
-->
[[以前のページ|ScalaActions]] で、リクエストパラメータを使用する、リクエストパラメータを使用しない、ボディパーサーを使用するなど アクションを宣言する複数の方法を見てきました。実際のところ、[[非同期プログラミング|ScalaAsync]] の章にもあるとおり、ほかにも方法があります。

<!--
These methods for building actions are actually all defined by a trait called [`ActionBuilder`](api/scala/play/api/mvc/ActionBuilder.html) and the [`Action`](api/scala/play/api/mvc/Action$.html) object that we use to declare our actions is just an instance of this trait.  By implementing your own `ActionBuilder`, you can declare reusable action stacks, that can then be used to build actions.
-->
アクションを構築するためのこれらのメソッドは、実際は [`ActionBuilder`](api/scala/play/api/mvc/ActionBuilder.html) と呼ばれるトレイトに全て定義されています。また、これまでに定義してきた [`Action`](api/scala/play/api/mvc/Action$.html) オブジェクトはこのトレイトの単なるインスタンスです。独自の `ActionBuilder` を実装することで、アクションを作るために使用できる、再利用可能なアクションスタックを宣言することができます。

<!--
Let’s start with the simple example of a logging decorator, we want to log each call to this action.
-->
ロギングデコレータの簡単な例から始めましょう。このアクションの各呼び出しをログに記録します。

<!--
The first way is to implement this functionality in the `invokeBlock` method, which is called for every action built by the `ActionBuilder`:
-->
最初の方法は、`invokeBlock` メソッドでこの機能を実装するやり方です。このメソッドは、`ActionBuilder` によって構築されたすべてのアクションに対して呼び出されます。

@[basic-logging](code/ScalaActionsComposition.scala)

<!--
Now we can use it the same way we use `Action`:
-->
これで `Action` と同じように使用できます。

@[basic-logging-index](code/ScalaActionsComposition.scala)
 
<!--
Since `ActionBuilder` provides all the different methods of building actions, this also works with, for example, declaring a custom body parser:
-->
`ActionBuilder` はアクションを構築するためのさまざまなメソッドをすべて提供するので、例えば、独自のボディパーサーを宣言することもできます。

@[basic-logging-parse](code/ScalaActionsComposition.scala)


<!--
### Composing actions
-->
### アクションの作成

<!--
In most applications, we will want to have multiple action builders, some that do different types of authentication, some that provide different types of generic functionality, etc.  In which case, we won't want to rewrite our logging action code for each type of action builder, we will want to define it in a reuseable way.
-->
ほとんどのアプリケーションでは、 様々な種類の認証を行いたい、様々な種類の一般的な機能を提供したいなど、複数のアクションビルダーが必要となるでしょう。 このような場合、それぞれの種類に応じたアクションビルダーに対するロギングアクションのコードを書き直すのではなく、再利用が可能な方法で定義したくなります。

<!--
Reusable action code can be implemented by wrapping actions:
-->
アクションをラッピングして実装することで、再利用が可能なアクションのコードを書くことができます。

@[actions-class-wrapping](code/ScalaActionsComposition.scala)

<!--
We can also use the `Action` action builder to build actions without defining our own action class:
-->
`Action` アクションビルダーを使って、独自のアクションクラスを定義せずにアクションを構築することもできます。

@[actions-def-wrapping](code/ScalaActionsComposition.scala)

<!--
Actions can be mixed in to action builders using the `composeAction` method:
-->
アクションは `composeAction` メソッドを使ってアクションビルダーにミックスインさせることができます。

@[actions-wrapping-builder](code/ScalaActionsComposition.scala)

<!--
Now the builder can be used in the same way as before:
-->
これで、ビルダーを先ほどと同じ方法で使用することができます。

@[actions-wrapping-index](code/ScalaActionsComposition.scala)

<!--
We can also mix in wrapping actions without the action builder:
-->
ラッピングしたアクションはアクションビルダーなしでミックスインさせることができます。

@[actions-wrapping-direct](code/ScalaActionsComposition.scala)

<!--
### More complicated actions
-->
### より複雑なアクション

<!--
So far we've only shown actions that don't impact the request at all.  Of course, we can also read and modify the incoming request object:
-->
これまでは、リクエストに全く影響しないアクションしか示していませんでした。もちろん、入力されてくるリクエストオブジェクトに対して、読み込みと変更も可能です。

@[modify-request](code/ScalaActionsComposition.scala)

<!--
> **Note:** Play already has built in support for `X-Forwarded-For` headers.
-->
> **メモ:** Play は 'X-Forwarded-For' ヘッダを組み込みですでにサポートしています。

<!--
We could block the request:
-->
リクエストをブロックすることができます。

@[block-request](code/ScalaActionsComposition.scala)

<!--
And finally we can also modify the returned result:
-->
そして、返された結果を変更することもできます。

@[modify-result](code/ScalaActionsComposition.scala)

<!--
## Different request types
-->
## 様々なリクエストタイプ

<!--
While action composition allows you to perform additional processing at the HTTP request and response level, often you want to build pipelines of data transformations that add context to or perform validation on the request itself.  `ActionFunction` can be thought of as a function on the request, parameterized over both the input request type and the output type passed on to the next layer.  Each action function may represent modular processing such as authentication, database lookups for objects, permission checks, or other operations that you wish to compose and reuse across actions.
-->
アクションの構成では、HTTPリクエストとレスポンス・レベルで追加の処理を実行できますが、リクエスト自体に対してコンテキストを追加したりバリデーションを実行したりするデータ変換のパイプラインを構築することがよくあります。`ActionFunction` は、入力リクエストタイプと次のレイヤーに渡される出力タイプの両方でパラメータ化された、リクエストの関数と考えることができます。各アクション関数は、認証、オブジェクトのデータベース検索、権限チェックなどのような、アクション全体で作成および再利用したいモジュール的な処理を表すことができます。

<!--
There are a few pre-defined traits implementing `ActionFunction` that are useful for different types of processing:
-->
さまざまなタイプの処理に役立つ `ActionFunction` を実装する、いくつかの事前定義されたトレイトがあります。

<!--
* [`ActionTransformer`](api/scala/play/api/mvc/ActionTransformer.html) can change the request, for example by adding additional information.
* [`ActionFilter`](api/scala/play/api/mvc/ActionFilter.html) can selectively intercept requests, for example to produce errors, without changing the request value.
* [`ActionRefiner`](api/scala/play/api/mvc/ActionRefiner.html) is the general case of both of the above.
* [`ActionBuilder`](api/scala/play/api/mvc/ActionBuilder.html) is the special case of functions that take `Request` as input, and thus can build actions.
-->
* [`ActionTransformer`](api/scala/play/api/mvc/ActionTransformer.html) は、追加情報を加えるなど、リクエストを変更することができます。
* [`ActionFilter`](api/scala/play/api/mvc/ActionFilter.html) は、リクエストの値を変更することなくエラーを生成するなど、リクエストを選択的に受け取ることができます。
* [`ActionRefiner`](api/scala/play/api/mvc/ActionRefiner.html) は、上記の両方の一般的なケースです。
* [`ActionBuilder`](api/scala/play/api/mvc/ActionBuilder.html) は、`Request` を入力として受け取り、アクションを構築する関数の特殊なケースです。

<!--
You can also define your own arbitrary `ActionFunction` by implementing the `invokeBlock` method.  Often it is convenient to make the input and output types instances of `Request` (using `WrappedRequest`), but this is not strictly necessary.
-->
`invokeBlock` メソッドを実装することによって、任意の `ActionFunction` を定義することもできます。多くの場合、(`WrappedRequest` を使って) `Request` の入力と出力タイプのインスタンスを作るのが便利ですが、これは厳密には必要ではありません。

<!--
### Authentication
-->
### 認証

<!--
One of the most common use cases for action functions is authentication.  We can easily implement our own authentication action transformer that determines the user from the original request and adds it to a new `UserRequest`.  Note that this is also an `ActionBuilder` because it takes a simple `Request` as input:
-->
アクション関数の最も一般的な使用例のひとつが認証です。元のリクエストからユーザーを判断し、それを新しい `UserRequest` に追加する独自の認証アクションを簡単に実装することができます。シンプルな `Request` を入力として受け取るので、これも` ActionBuilder`です。

@[authenticated-action-builder](code/ScalaActionsComposition.scala)

<!--
Play also provides a built in authentication action builder.  Information on this and how to use it can be found [here](api/scala/play/api/mvc/Security$$AuthenticatedBuilder$.html).
-->
Play はまた、組み込みの認証アクションビルダーを提供しています。 詳細と使用方法については　[こちら](api/scala/play/api/mvc/Security$$AuthenticatedBuilder$.html) を参照してください。

<!--
> **Note:** The built in authentication action builder is just a convenience helper to minimise the code necessary to implement authentication for simple cases, its implementation is very similar to the example above.
>
> If you have more complex requirements than can be met by the built in authentication action, then implementing your own is not only simple, it is recommended.
-->
> **メモ:** 組み込みの認証アクションビルダーは、シンプルなケースの認証を実装するために必要なコードを最小限に抑える便利なヘルパーであり、その実装は上記の例に非常に似ています。
> 組み込みの認証アクションでは満たされない、より複雑な要件がある場合の独自の実装は、シンプルであるだけではなく、推奨もされています。

<!--
### Adding information to requests
-->
### リクエストへの情報の追加

<!--
Now let's consider a REST API that works with objects of type `Item`.  There may be many routes under the `/item/:itemId` path, and each of these need to look up the item.  In this case, it may be useful to put this logic into an action function.
-->
`Item` 型というオブジェクトについて動作する REST API について考えてみましょう。 `/item/:itemId` というパスの下に多くのルートがあり、それらのルートでは　Item を見つける必要があるとします。この場合、このロジックをアクション関数に入れると便利です。

<!--
First of all, we'll create a request object that adds an `Item` to our `UserRequest`:
-->
まず、`Item` を `UserRequest` に追加するリクエストオブジェクトを作成します。

@[request-with-item](code/ScalaActionsComposition.scala)

<!--
Now we'll create an action refiner that looks up that item and returns `Either` an error (`Left`) or a new `ItemRequest` (`Right`).  Note that this action refiner is defined inside a method that takes the id of the item:
-->
次に、そのアイテムを調べ、エラー (`Left`) または新しい `ItemRequest` (`Right`) を返すアクションリファイナーを作成します。このアクションリファイナーは、アイテムの ID を取得するメソッド内で定義されています。

@[item-action-builder](code/ScalaActionsComposition.scala)

<!--
### Validating requests
-->
### リクエストの検証

<!--
Finally, we may want an action function that validates whether a request should continue.  For example, perhaps we want to check whether the user from `UserAction` has permission to access the item from `ItemAction`, and if not return an error:
-->
最後に、リクエストを続行するかどうかを検証するアクション関数が必要な場合があります。例えば、`UserAction` のユーザが `ItemAction` からアイテムにアクセスする権限を持っているかどうかをチェックし、そうでない場合はエラーを返します。

@[permission-check-action](code/ScalaActionsComposition.scala)

<!--
### Putting it all together
-->
### ひとまとめにする

<!--
Now we can chain these action functions together (starting with an `ActionBuilder`) using `andThen` to create an action:
-->
`andThen` を使ってこれらのアクション関数 (`ActionBuilder` で始まる) をつなぎ、１つのアクションを作成することができます。

@[item-action-use](code/ScalaActionsComposition.scala)


<!--
Play also provides a [[global filter API | ScalaHttpFilters]], which is useful for global cross cutting concerns.
-->
Play は全体的な横断的関心事に便利な [[グローバルなフィルター API | ScalaHttpFilters]] も提供しています。
