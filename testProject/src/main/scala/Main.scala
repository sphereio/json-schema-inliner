object Main extends App {
  val resources = Seq(
    "test/category.schema.json",
    "test/inline/category.schema.json"
  )

  resources foreach { r =>
    val url = this.getClass.getClassLoader.getResource(r)
    println(s"url from $r: $url")
  }
}