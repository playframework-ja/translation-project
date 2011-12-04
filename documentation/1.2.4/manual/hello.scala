import java.io._

println("Hello, world, from a script!")

val dir = new File(".")

val files = dir.listFiles()

val file = files(0)

val documentationFile = new DocumentationFile(file)

println(documentationFile)

val docFiles = dir.listFiles.map(new DocumentationFile(_))

docFiles.map(println)

class DocumentationFile(val file: File) {

  val name: String = file.getName
  
  val lenght: Long = file.length

  val isTranslated: Boolean = calculateIsTranslated

  override def toString = "name: " + name + ", lenght: " + lenght.toString + ", isTranslated: " + isTranslated.toString

  private def calculateIsTranslated: Boolean = {
    firstLine.indexOf("Esta página todavía no ha sido traducida al castellano") == -1
  }

  def firstLine: String = {
    new BufferedReader(new FileReader(file)).readLine
  }

}

