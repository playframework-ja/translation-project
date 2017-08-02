resolvers += "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"

lazy val root = (project in file(".")).enablePlugins(PlayDocsPlugin)

PlayDocsKeys.docsVersion := "2.4.11"

//PlayDocsKeys.manualPath := new File("C:/Users/suwabe/Documents/translation-project")
