#!/bin/sh
exec scala "$0" "$@"
!#

import java.io._

val docs = new File(".").listFiles
  .filter(_.getName.endsWith(".textile"))   // process only textile files
  .map(new DocumentationFile(_))

val translated = docs.filter(_.isTranslated)    // only already translated files

//Other ways to do it 
//val translatedLength = translated.foldLeft(0L)( (acum, element) => acum + element.length )
//val translatedLength = translated.foldLeft(0L)( _ + _.length )
//val translatedLength = if (translated.length == 0) 0 else translated.map(_.length).sum

val translatedLength = translated.map(_.length).sum

val docsLength = docs.map(_.length).sum

println( 
  status("translated size", translatedLength, docsLength, (length) => asKB(length) ) 
)

println( 
  status("translated files", translated.length, docs.length) 
)

def status(title: String = "status", current: Long, total: Long, format: (Long) => String = (x) => x.toString): String = {
  title + ": " + format(current) + "/" + format(total) + " " +
  (current * 100 / total) + "%" +
  " (pending " + format(total - current) + " " +
  (100-(current * 100 / total)) + "%)"
}

def asKB(length: Long) = (length / 1000) + "kb"

class DocumentationFile(val file: File) {

  val name = file.getName
  val length = file.length
  val isTranslated = (firstLine.indexOf("Esta página todavía no ha sido traducida al castellano") == -1)

  override def toString = "name: " + name + ", length: " + length + ", isTranslated: " + isTranslated

  def firstLine = new BufferedReader(new FileReader(file)).readLine

}

