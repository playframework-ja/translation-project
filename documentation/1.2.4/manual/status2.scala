#!/bin/sh
exec scala -savecompiled "$0" "$@"
!#

import java.io._
import scala.io.Source._

val total_files = new File(".").listFiles.filter(_.getName.endsWith(".textile"))
val translated_files = total_files.filter(_.isTranslated)

val total_size = total_files.map(_.length).sum / 1000
val translated_size = translated_files.map(_.length).sum / 1000
val size_percent = translated_size * 100 / total_size

println( "translated size: %dkb/%dkb %d%% (pending %dkb %d%%)" 
  format(translated_size, total_size, size_percent, total_size-translated_size, 100-size_percent)
)

val total_count = total_files.length
val translated_count = translated_files.length
val count_percent = translated_count * 100 / total_count

println( "translated files: %d/%d %d%% (pending %d %d%%)" 
  format(translated_count, total_count, count_percent, total_count-translated_count, 100-count_percent)
)

implicit def fileToDocumentationFile(file: File): DocumentationFile = new DocumentationFile(file)

class DocumentationFile(val file: File) {
  val isTranslated = (
    fromFile(file).getLines.take(1).mkString.
      indexOf("Esta página todavía no ha sido traducida al castellano") == -1
  )
}
