<!-- translated -->
<!--
# Handling file upload
-->
# ファイルアップロード

<!--
## Uploading files in a form using `multipart/form-data`
-->
## フォームから `multipart/form-data` 形式でファイルをアップロードする

<!--
The standard way to upload files in a web application is to use a form with a special `multipart/form-data` encoding, which allows to mix standard form data with file attachments. Please note: the HTTP method for the form have to be POST (not GET). 
-->
web アプリケーションにおけるファイルアップロードの標準的な方法は、`multipart/form-data` エンコーディングのフォームを使うことです。`multipart/form-data` を使うと、標準的なフォームデータに加えて、ファイルを添付データとして一緒に送信することができます。このフォームが使用する HTTP メソッドは (GET ではなく) POST でなければならないことに注意してください。

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
Now let’s define the `upload` action:
-->
次に、 `upload` アクションを定義します。

```
public static Result upload() {
  MultipartFormData body = request().body().asMultipartFormData();
  FilePart picture = body.getFile("picture");
  if (picture != null) {
    String fileName = picture.getFilename();
    String contentType = picture.getContentType(); 
    File file = picture.getFile();
    return ok("File uploaded");
  } else {
    flash("error", "Missing file");
    return redirect(routes.Application.index());    
  }
}
```

<!--
## Direct file upload
-->
## ダイレクトファイルアップロード

<!--
Another way to send files to the server is to use Ajax to upload files asynchronously from a form. In this case, the request body will not be encoded as `multipart/form-data`, but will just contain the plain file contents.
-->
サーバへファイルを送信する別の方法は、Ajax を活用してフォームからファイルを非同期的にアップロードするというものです。この方法では、リクエストボディは `multipart/form-data` としてエンコードされず、単にファイルの内容を含むだけになります。

```
public static Result upload() {
  File file = request().body().asRaw().asFile();
  return ok("File uploaded");
}
```

<!--
> **Next:** [[Accessing an SQL database | JavaDatabase]]
-->
> **次ページ:** [[SQL データベースへのアクセス | JavaDatabase]]