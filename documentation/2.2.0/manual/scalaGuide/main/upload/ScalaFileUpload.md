<!-- translated -->
<!--
# Handling file upload
-->
# ファイルアップロード

<!--
## Uploading files in a form using multipart/form-data
-->
## multipart/form-data を利用したフォームからのファイルアップロード

<!--
The standard way to upload files in a web application is to use a form with a special `multipart/form-data` encoding, which lets you mix standard form data with file attachment data. Please note: the HTTP method for the form have to be POST (not GET). 
-->
Web アプリケーションにおけるファイルアップロードの標準的な方法は、`multipart/form-data` エンコーディングのフォームを使うことです。`multipart/form-data` を使うと、標準的なフォームデータに加えて、ファイルを添付データとして一緒に送信することができます。なお、フォームの HTTP メソッドは GET ではなく POST である必要があることに留意してください。

<!--
Start by writing an HTML form:
-->
まず、 HTML フォームを書きます。

@[file-upload-form](code/scalaguide/templates/views/uploadForm.scala.html)

<!--
Now define the `upload` action using a `multipartFormData` body parser:
-->
次に、 `multipartFormData` ボディパーサーを利用して、 `upload` アクションを定義します。

@[upload-file-action](code/ScalaFileUpload.scala)

<!--
The `ref` attribute give you a reference to a `TemporaryFile`. This is the default way the `mutipartFormData` parser handles file upload.
-->
`ref` 属性は `TemporaryFile` への参照です。これは、`multipartFormData`　パーサーがファイルアップロードを処理するデフォルトの方法です。

<!--
> **Note:** As always, you can also use the `anyContent` body parser and retrieve it as `request.asMultipartFormData`.
-->
> **Note:** これまでのように `anyContent` ボディパーサーを使って、`request.asMultipartFormData` によりフォームデータを参照することもできます。

At last, add a POST router

@[application-upload-routes](code/scalaguide.upload.fileupload.routes)

<!--
## Direct file upload
-->
## 直接的なファイルアップロード

<!--
Another way to send files to the server is to use Ajax to upload the file asynchronously in a form. In this case the request body will not have been encoded as `multipart/form-data`, but will just contain the plain file content.
-->
サーバへファイルを送信する別の方法は、Ajax を活用してフォームからファイルを非同期的にアップロードするというものです。この方法では、リクエストボディは `multipart/form-data` としてエンコードされず、単にファイルの内容を含むだけになります。

<!--
In this case we can just use a body parser to store the request body content in a file. For this example, let’s use the `temporaryFile` body parser:
-->
この場合も、リクエストボディの内容をファイルへ保存するためのボディパーサーを使うだけです。この例では、`temporaryFile` ボディパーサーを使ってみましょう。

@[upload-file-directly-action](code/ScalaFileUpload.scala)

<!--
## Writing your own body parser
-->
## ボディパーサーを自作する

<!--
If you want to handle the file upload directly without buffering it in a temporary file, you can just write your own `BodyParser`. In this case, you will receive chunks of data that you are free to push anywhere you want.
-->
一時ファイルへのバッファリングなしでファイルアップロードを直接的に処理したい場合、`BodyParser` を自作するという方法があります。その場合、チャンクに分割されたデータを受信して、それを好きなところに PUSH 送信することになります。

<!--
If you want to use `multipart/form-data` encoding, you can still use the default `mutipartFormData` parser by providing your own `PartHandler[FilePart[A]]`. You receive the part headers, and you have to provide an `Iteratee[Array[Byte], FilePart[A]]` that will produce the right `FilePart`.
-->
`multipart/form-data` エンコーディングを利用したい場合は、`PartHandler[FilePart[A]]` を指定して、デフォルトの `multipartFormData` パーサーを利用することができます。その場合、パートヘッダーを元に、正しい `FilePart` を生成するような `Iteratee[Array[Byte]]`、`FilePart[A]]` を生成する必要があります。

<!--
> **Next:** [[Accessing an SQL database | ScalaDatabase]]
-->
> **次ページ:** [[SQL データベースへのアクセス | ScalaDatabase]]