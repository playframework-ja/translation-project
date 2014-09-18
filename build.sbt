resolvers += "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"

lazy val root = (project in file(".")).enablePlugins(PlayDocsPlugin)

PlayDocsKeys.docsVersion := "2.2.4"
