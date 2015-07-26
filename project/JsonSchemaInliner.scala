import sbt._

object JsonSchemaInliner {
  def inline(sources: Seq[File], destination: File): Seq[File] = {
    println(sources.mkString(", "))
    println(destination)
    val targets = sources.map(s => new File(destination, s.getName))
    IO.copy(sources.zip(targets))
    targets
  }
}