#!/bin/sh
exec scala -savecompiled "$0" "$@"
!#

import java.io._

val docs = new File(".").listFiles
  .filter(_.getName.endsWith(".textile"))   // process only textile files
  .map(new DocumentationFile(_))

val translated = docs.filter(_.isTranslated)    // only already translated files

val translatedLength = translated.map(_.length).sum
val docsLength = docs.map(_.length).sum

println( 
  status("translated size", translatedLength, docsLength, (length) => asKB(length) ) 
)

println( 
  status("translated files", translated.length, docs.length) 
)

def status(
  title: String = "status", 
  current: Long, total: Long, 
  format: (Long) => String = (x) => x.toString): String = {

  val percent = current * 100 / total

  title + ": " + format(current) + "/" + format(total) + " " +
  percent + "%" +
  " (pending " + format(total - current) + " " +
  (100-percent) + "%)"
}

def asKB(length: Long) = (length / 1000) + "kb"

class DocumentationFile(val file: File) {

  val name = file.getName
  val length = file.length
  val isTranslated = (firstLine.indexOf("Esta página todavía no ha sido traducida al castellano") == -1)

  override def toString = "name: " + name + ", length: " + length + ", isTranslated: " + isTranslated

  def firstLine = new BufferedReader(new FileReader(file)).readLine

}

