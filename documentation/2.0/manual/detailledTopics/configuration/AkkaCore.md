<!-- translated -->
<!--
# Configuring the internal Akka system
-->
# 内部の Akka システムの設定

<!--
Play 2.0 uses an internal Akka Actor system to handle request processing. You can configure it in your application `application.conf` configuration file.
-->
Play 2.0 はリクエスト処理のために内部的に Akka Actor システムを利用しています。Actor システムの設定は、 `application.conf` ファイルで行うことができます。

<!--
## Action invoker actors
-->
## Action invoker アクター

<!-- The action invoker Actors are used to execute the `Action` code. To be able to execute several Action concurrently we are using several of these Actors managed by a Round Robin router. These actors are stateless. -->
Action invoker アクターは `Action` のコードを実行するために使われます。複数の Action を並列に実行するために、複数のアクターを起動し、それらを Round Robin ルータで制御しています。また、全てのアクターはステートレスです。

<!--
These action invoker Actors are also used to retrieve the **body parser** needed to parse the request body. Because this part waits for a reply (the `BodyParser` object to use), it will fail after a configurable timeout.
-->
Action invoker アクターは、リクエストボディをパースするために使われる **ボディパーサ** を取得するためにも使われます。この取得処理はアクターからの返信（パースに使われる BodyParser オブジェクト）を待機するという実装になっていて、設定されたタイムアウト時間を経過すると失敗する仕様です。

<!--
Action invoker actors are run by the `actions-dispatcher` dispatcher.
-->
Action invoker アクターは `actions-dispatcher` ディスパッチャによって実行されます。

<!--
## Promise invoker actors
-->
## Promise invoker アクター

<!--
The promise invoker Actors are used to execute all asynchronous callback needed by `Promise`. Several Actors must be available to execute several Promise callbacks concurrently. These actors are stateless.
-->
Promise invoker アクターは `Promise` による非同期的なコールバックを行うためのアクターです。並列に Promise のコールバックを実行するためには、それだけ多くの Promise invoker アクターが利用可能でなければなりません。また、全ての Promise invoker アクターはステートレスです。

<!--
Promise invoker actors are run by the `promises-dispatcher` dispatcher.
-->
Promise invoker アクターは `promises-dispatcher` ディスパッチャによって実行されます。

<!--
## WebSockets agent actors
-->
## WebSockets agent アクター

<!--
Each WebSocket connection state is managed by an Agent actor. A new actor is created for each WebSocket, and is killed when the socket is closed. These actors are statefull.
-->
各 WebSocket 接続の状態は Agent アクターによって管理されます。WebSocket 接続のたびに新しい Actor が生成され、ソケットがクローズされた時に Actor が破棄されます。このアクターはステートフルです。

<!--
WebSockets agent actors are run by the `websockets-dispatcher` dispatcher.
-->
WebSocket agent アクターは `websockets-dispatcher` ディスパッチャによって実行されます。

<!--
## Default configuration
-->
## デフォルト設定

<!--
Here is the reference configuration used by Play 2.0 if you don't override it. Adapt it according your application needs.
-->
Play 2.0 が利用するデフォルト設定を以下に示します。アプリケーション側で上書きしていない場合は、このリファレンス設定が使われます。必要に応じてカスタマイズしてください。

```
play {
    
    akka {
        event-handlers = ["akka.event.slf4j.Slf4jEventHandler"]
        loglevel = WARNING
        
        actor {
            
            deployment {

                /actions {
                    router = round-robin
                    nr-of-instances = 24
                }

                /promises {
                    router = round-robin
                    nr-of-instances = 24
                }

            }
            
            retrieveBodyParserTimeout = 1 second
            
            actions-dispatcher = {
                fork-join-executor {
                    parallelism-factor = 1.0
                    parallelism-max = 24
                }
            }

            promises-dispatcher = {
                fork-join-executor {
                    parallelism-factor = 1.0
                    parallelism-max = 24
                }
            }

            websockets-dispatcher = {
                fork-join-executor {
                    parallelism-factor = 1.0
                    parallelism-max = 24
                }
            }

            default-dispatcher = {
                fork-join-executor {
                    parallelism-factor = 1.0
                    parallelism-max = 24
                }
            }
            
        }
        
    }
    
}
```