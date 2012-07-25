<!-- translated -->
<!--
# Handling file upload
-->
# ファイルアップロード

<!--
## Uploading files in a form using multipart/form-data
-->
## フォームから multipart/form-data 形式でファイルをアップロードする

<!--
The standard way to upload files in a web application is to use a form with a special `multipart/form-data` encoding, which lets you mix standard form data with file attachment data.
-->
web アプリケーションにおけるファイルアップロードの標準的な方法は、`multipart/form-data` エンコーディングのフォームを使うことです。`multipart/form-data` を使うと、標準的なフォームデータに加えて、ファイルを添付データとして一緒に送信することができます。

<!--
Start by writing an HTML form:
-->
まず、 HTML フォームを書きます。

```
@form(action = routes.Application.upload, 'enctype -> "multipart/form-data") {
    
    <input type="file" name="picture">
    
    <p>
        <input type="submit">
    </p>
    
}
```

<!--
Now define the `upload` action using a `multipartFormData` body parser:
-->
次に、 `multipartFormData` ボディパーサーを利用して、 `upload` アクションを定義します。

```scala
def upload = Action(parse.multipartFormData) { request =>
  request.body.file("picture").map { picture =>
    import java.io.File
    val filename = picture.filename 
    val contentType = picture.contentType
    picture.ref.moveTo(new File("/tmp/picture"))
    Ok("File uploaded")
  }.getOrElse {
    Redirect(routes.Application.index).flashing(
      "error" -> "Missing file"
    )
  }
}
```

<!--
The `ref` attribute give you a reference to a `TemporaryFile`. This is the default way the `mutipartFormData` parser handles file upload.
-->
`ref` 属性は `TemporaryFile` への参照です。これは、`multipartFormData`　パーサーがファイルアップロードを処理するデフォルトの方法です。

<!--
> **Note:** As always, you can also use the `anyContent` body parser and retrieve it as `request.asMultipartFormData`.
-->
> **Note:** これまでのように `anyContent` ボディパーサーを使って、`request.asMultipartFormData` によりフォームデータを参照することもできます。

<!--
## Direct file upload
-->
## ダイレクトファイルアップロード

<!--
Another way to send files to the server is to use Ajax to upload the file asynchronously in a form. In this case the request body will not have been encoded as `multipart/form-data`, but will just contain the plain file content.
-->
サーバへファイルを送信する別の方法は、フォームでファイルの非同期的にアップロードするために Ajax を活用することです。このケースでは、リクエストは `multipart/form-data` としてエンコードされるのではなく、単純にファイルの内容を含むだけになります。

<!--
In this case we can just use a body parser to store the request body content in a file. For this example, let’s use the `temporaryFile` body parser:
-->
この場合も、リクエストボディの内容をファイルへ保存するためのボディパーサーを使うだけです。この例では、`temporaryFile` ボディパーサーを使ってみましょう。

```scala
def upload = Action(parse.temporaryFile) { request =>
  request.body.moveTo(new File("/tmp/picture"))
  Ok("File uploaded")
}
```

<!--
## Writing your own body parser
-->
## ボディパーサーを自作する

<!--
If you want to handle the file upload directly without buffering it in a temporary file, you can just write your own `BodyParser`. In this case, you will receive chunks of data that you are free to push anywhere you want.
-->
一時ファイルへのバッファリングなしでファイルアップロードを直接的に処理したい場合、`BodyParser` を自作する手があります。その場合、チャンクに分割されたデータを受信して、それを好きなところに PUSH 送信することになります。

<!--
If you want to use `multipart/form-data` encoding, you can still use the default `mutipartFormData` parser by providing your own `PartHandler[FilePart[A]]`. You receive the part headers, and you have to provide an `Iteratee[Array[Byte], FilePart[A]]` that will produce the right `FilePart`.
-->
`multipart/form-data` エンコーディングを利用したい場合は、`PartHandler[FilePart[A]]` を指定して、デフォルトの `multipartFormData` パーサーを利用することができます。その場合、パートヘッダーを元に、正しい `FilePart` を生成するような `Iteratee[Array[Byte]]`、`FilePart[A]]` を生成する必要があります。

<!--
> **Next:** [[Accessing an SQL database | ScalaDatabase]]
-->
> **次ページ:** [[SQL データベースへのアクセス | ScalaDatabase]]