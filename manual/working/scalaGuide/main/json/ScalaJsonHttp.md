<!--- Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com> -->
<!--
# JSON with HTTP
-->
# JSON と HTTP

<!--
Play supports HTTP requests and responses with a content type of JSON by using the HTTP API in combination with the JSON library.
-->
Play は、JSON ライブラリと組み合わせて HTTP API を使用することによって、コンテンツタイプが JSON の HTTP リクエストとレスポンスをサポートしています。

<!--
> See  [[HTTP Programming | ScalaActions]] for details on Controllers, Actions, and routing.
-->
> コントローラ、アクション、ルーティングの詳細については、[[HTTP プログラミング | ScalaActions]] を参照してください。

<!--
We'll demonstrate the necessary concepts by designing a simple RESTful web service to GET a list of entities and accept POSTs to create new entities. The service will use a content type of JSON for all data.
-->
エンティティのリストを GET したり、新しいエンティティを作成する POST を受け入れる、簡単な RESTful Web サービスを設計することにより、必要な概念を説明します。このサービスでは、すべてのデータに JSON のコンテンツタイプを使用します。

<!--
Here's the model we'll use for our service:
-->
このサービスで使用するモデルは次のとおりです。

@[model](code/ScalaJsonHttpSpec.scala)

<!--
## Serving a list of entities in JSON
-->
## JSON によるエンティティのリストの提供

<!--
We'll start by adding the necessary imports to our controller.
-->
必要なインポートをコントローラに追加することから始めます。

@[controller](code/ScalaJsonHttpSpec.scala)

<!--
Before we write our `Action`, we'll need the plumbing for doing conversion from our model to a `JsValue` representation. This is accomplished by defining an implicit `Writes[Place]`.
-->
`Action` を記述する前に、モデルを `JsValue` 表現に変換する仕組みが必要です。これには、暗黙の `Writes[Place]` を定義することによって対応します。

@[serve-json-implicits](code/ScalaJsonHttpSpec.scala)

<!--
Next we write our `Action`:
-->
次に `Action` を記述します。

@[serve-json](code/ScalaJsonHttpSpec.scala)

<!--
The `Action` retrieves a list of `Place` objects, converts them to a `JsValue` using `Json.toJson` with our implicit `Writes[Place]`, and returns this as the body of the result. Play will recognize the result as JSON and set the appropriate `Content-Type` header and body value for the response. 
-->
`Action` は `Place` オブジェクトのリストを取得し、暗黙の `Writes[Place]` による `Json.toJson` を使って `JsValue` に変換し、これを結果のボディとして返します。Play は結果を JSON として認識し、適切な `Content-Type` ヘッダとレスポンス用のボディ値を設定します。

<!--
The last step is to add a route for our `Action` in `conf/routes`:
-->
最後のステップとして、`conf/routes` に `Action` 用のルートを追加します。

```
GET   /places               controllers.Application.listPlaces
```

<!--
We can test the action by making a request with a browser or HTTP tool. This example uses the unix command line tool [cURL](http://curl.haxx.se/).
-->
ブラウザや HTTP ツールを使ってリクエストを行うことでアクションをテストできます。この例では unix コマンドラインツール [cURL](http://curl.haxx.se/) を使用しています。

```
curl --include http://localhost:9000/places
```

<!--
Response:
-->
処理結果は以下のようになります。

```
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 141

[{"name":"Sandleford","location":{"lat":51.377797,"long":-1.318965}},{"name":"Watership Down","location":{"lat":51.235685,"long":-1.309197}}]
```

<!--
## Creating a new entity instance in JSON
-->
## JSON による新しいエンティティインスタンスの作成

<!--
For this `Action` we'll need to define an implicit `Reads[Place]` to convert a `JsValue` to our model.
-->
この `Action` では、`JsValue` をモデルに変換するために暗黙の `Reads[Place]` を定義する必要があります。

@[handle-json-implicits](code/ScalaJsonHttpSpec.scala)

<!--
Next we'll define the `Action`.
-->
次に `Action` を定義します。

@[handle-json-bodyparser](code/ScalaJsonHttpSpec.scala)

<!--
This `Action` is more complicated than our list case. Some things to note:
-->
この `Action` は、リストの場合より複雑です。 注意すべき事項を挙げます。

<!--
- This `Action` expects a request with a `Content-Type` header of `text/json` or `application/json` and a body containing a JSON representation of the entity to create.
- It uses a JSON specific `BodyParser` which will parse the request and provide `request.body` as a `JsValue`. 
- We used the `validate` method for conversion which will rely on our implicit `Reads[Place]`.
- To process the validation result, we used a `fold` with error and success flows. This pattern may be familiar as it is also used for [[form submission|ScalaForms]].
- The `Action` also sends JSON responses.
-->
- この `Action` は、`Content-Type` ヘッダが `text-json` や `application/json` となっているリクエストと、作成するエンティティの JSON 表現を含むボディを期待します。
- リクエストを解析したり、`request.body` を `JsValue` として提供する、JSON に特化した `BodyParser` を使用します。
- 暗黙の `Reads[Place]` に依存する変換のために `validate` メソッドを使いました。
- バリデーションの結果を処理するために、エラーと成功のフローを持つ `fold` を使用しました。このパターンは、[[フォームの送信|ScalaForms]] でも使用されているので、よく知られているかもしれません。
- `Action` は JSON のレスポンスも送信します。

<!--
Finally we'll add a route binding in `conf/routes`:
-->
最後に `conf/routes` にルートバインディングを追加します。

```
POST  /places               controllers.Application.savePlace
```

<!--
We'll test this action with valid and invalid requests to verify our success and error flows. 
-->
成功とエラーのフローを検証するため、このアクションを有効なリクエストと無効なリクエストでテストします。

<!--
Testing the action with a valid data:
-->
有効なデータによるアクションのテスト:

```
curl --include
  --request POST
  --header "Content-type: application/json" 
  --data '{"name":"Nuthanger Farm","location":{"lat" : 51.244031,"long" : -1.263224}}' 
  http://localhost:9000/places
```

<!--
Response:
-->
処理結果:

```
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 57

{"status":"OK","message":"Place 'Nuthanger Farm' saved."}
```

<!--
Testing the action with a invalid data, missing "name" field:
-->
"name" フィールドが存在しない、無効なデータによるアクションのテスト:

```
curl --include
  --request POST
  --header "Content-type: application/json"
  --data '{"location":{"lat" : 51.244031,"long" : -1.263224}}' 
  http://localhost:9000/places
```
<!--
Response:
-->
処理結果:

```
HTTP/1.1 400 Bad Request
Content-Type: application/json; charset=utf-8
Content-Length: 79

{"status":"KO","message":{"obj.name":[{"msg":"error.path.missing","args":[]}]}}
```
<!--
Testing the action with a invalid data, wrong data type for "lat":
-->
"lat" のデータ型が正しくない、無効なデータによるアクションのテスト:

```
curl --include
  --request POST
  --header "Content-type: application/json" 
  --data '{"name":"Nuthanger Farm","location":{"lat" : "xxx","long" : -1.263224}}' 
  http://localhost:9000/places
```
<!--
Response:
-->
処理結果:

```
HTTP/1.1 400 Bad Request
Content-Type: application/json; charset=utf-8
Content-Length: 92

{"status":"KO","message":{"obj.location.lat":[{"msg":"error.expected.jsnumber","args":[]}]}}
```

<!--
## Summary
-->
## まとめ

<!--
Play is designed to support REST with JSON and developing these services should hopefully be straightforward. The bulk of the work is in writing `Reads` and `Writes` for your model, which is covered in detail in the next section. 
-->
Play は JSON を用いた REST をサポートするように設計されており、これらのサービスの開発作業はすんなりと進むことでしょう。作業の大部分は、モデルの `Reads` と `Writes` を記述することになります。詳細については次の章で説明します。
