
lazy val root = (project in file(".")) dependsOn jsonSchemaInliner

lazy val jsonSchemaInliner = file("..").getAbsoluteFile.toURI
