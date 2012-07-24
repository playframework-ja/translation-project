# Setting-up your preferred IDE

Working with Play is easy. You don’t even need a sophisticated IDE, because Play compiles and refreshes the modifications you make to your source files automatically, so you can easily work using a simple text editor.

However, using a modern Java or Scala IDE provides cool productivity features like auto-completion, on-the-fly compilation, assisted refactoring and debugging.

## Eclipse

### Generate configuration

Play provides a command to simplify Eclipse configuration. To transform a Play application into a working Eclipse project, use the `eclipsify` command:

```
[My first application] $ eclipsify
```

You then need to import the application into your Workspace with the **File/Import/General/Existing project…** menu (compile your project first).

[[images/eclipse.png]] 

You can also start your application with `play debug run` and then you can use the Connect JPDA launcher using **Debug As** to start a debugging session at any time. Stopping the debugging session will not stop the server.

> Workaround Scala Template with Play 2.0 RC2 and Scala IDE : run your application using `~ run` to enable direct compilation on file change. This way scala templates files are auto discovered when you create new template in `view` and auto compiled when file change. If you use normal `run` then you have to hit `Refresh` on you browser each time. Play team will check this issue with Scala IDE team.  (source : http://bit.ly/wCyR5j)

If you make any important changes to your application, such as changing the classpath, use `eclipsify` again to regenerate the configuration files.

> Tip: Do not commit Eclipse configuration files when you work in a team!

The generated configuration files contain absolute references to your framework installation. These are specific to your own installation. When you work in a team, each developer must keep his Eclipse configuration files private.

## IntelliJ

### Generate configuration

Play provides a command to simplify Intellij IDEA configuration. To transform a Play application into a working IDEA module, use the idea command:

```
[My first application] $ idea
```

You then need to import the application into your project (File->New Module->Import existing Module)

[[images/idea.png]] 

> Tip: There is an [Intellij IDEA issue](http://devnet.jetbrains.net/thread/433870) regarding building Java based Play2 apps while having the scala plugin installed. Until it's fixed, the recommended workaround is to disable scala plugin.

To debug, first add a debug configuration

- Open Run/Debug Configurations dialog, then click Run -> Edit Configurations
- Add a Remote configuration, then select `Remote`
- Configure it:
    - Set a name
    - Transport: Socket
    - Debugger mode: Attach
    - Host: localhost
    - Port: 9999
    - Select module you imported
- Close dialog - click Apply

Start play in debug mode:

```
$ play debug
```

which should print: 

```
Listening for transport dt_socket at address: 9999
```

Set some breakpoints. Run the web app by executing the task `play` (again I had to do this in same terminal I ran `play debug`). Finally, browse `http://localhost:9000`. IntelliJ should stop at your breakpoint.

If you make any important changes to your application, such as changing the classpath, use `idea` again to regenerate the configuration files.


## Netbeans

### Generate Configuration

Play does not have native Netbeans project generation support at this time.  For now you can generate a Netbeans Scala project with the [Netbeans SBT plugin](https://github.com/remeniuk/sbt-netbeans-plugin).

First edit the plugins.sbt file
```
// Comment to get more information during initialization
logLevel := Level.Warn

resolvers ++= Seq( 
    DefaultMavenRepository, 
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/", 
    "remeniuk repo" at "http://remeniuk.github.com/maven" 
)

addSbtPlugin("play" % "sbt-plugin" % "2.0-RC2") 

libraryDependencies += "org.netbeans" %% "sbt-netbeans-plugin" % "0.1.4"
```

Now run

```
$ play netbeans
```

> **Next:** 
>
> - Explore the [[sample applications | Samples]]
>
> - Read the Play 2.0 manual:
>     - [[Play 2.0 for Scala developers | ScalaHome]]
>     - [[Play 2.0 for Java developers | JavaHome]]