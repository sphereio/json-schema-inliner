
lazy val root = (project in file(".")) dependsOn jsonSchemaInliner

lazy val jsonSchemaInliner = file("..").getAbsoluteFile.toURI
//{
//  val result = ProjectRef(file(".."), "jsonSchemaInlinerPlugin")
//  println(result)
//  result
//}
