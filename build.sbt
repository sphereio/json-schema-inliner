name := "json-schema-inliner"
organization := "io.sphere"

sbtPlugin := true

scalaVersion := "2.10.6"

libraryDependencies ++=
  "org.json4s" %% "json4s-jackson" % "3.3.0" ::
  Nil

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")
javacOptions ++= Seq("-deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7")

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

releaseSettings

