<!--
# Deploying to Cloudbees
-->
# Cloudbees へのデプロイ

<!--
CloudBees support Play dists natively - with Jenkins and continuous deployment - you can read more about this [here](https://developer.cloudbees.com/bin/view/RUN/Playframework) - yes, you can run it for free.
-->
CloudBees は Play の配布をネイティブでサポートしています - Jenkins および継続的デプロイ付きで - より詳しい情報は[ここ](https://developer.cloudbees.com/bin/view/RUN/Playframework) で読めます - 無料で実行する事も出来ます。

<!--
How to use: 
-->
使うためには:

<!--
1. [Signup for CloudBees](https://www.cloudbees.com/signup) (if you don't already have an account) 
2. [Run the ClickStart to setup a working Play app, with repo and Jenkins build service](https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/CloudBees-community/play2-clickstart/master/clickstart.json)
3. Clone the repo created above - push your changes and then glory is yours !
-->
1. [CloudBees にサインアップする](https://www.cloudbees.com/signup) (アカウントを持っていない場合) 
2. [ClickStart を実行してリポジトリと Jenkins ビルドサービス付きで Play app をセットアップする](https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/CloudBees-community/play2-clickstart/master/clickstart.json)
3. 上で作成したリポジトリをクローンする - 変更をプッシュすれば栄光が得られます!

<!--
Alternatively, you may want to just deploy from your Play command shell directly (using SBT), the plugin for this can be found [here](https://github.com/CloudBees-community/sbt-cloudbees-play-plugin).
-->
上記の代わりに Play コマンドシェルから (SBT を使って) 直接デプロイしたい場合は、そのためのプラグインが [ここ](https://github.com/CloudBees-community/sbt-cloudbees-play-plugin) にあります。
