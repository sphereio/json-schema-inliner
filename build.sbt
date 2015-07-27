name := "json-schema-inliner"
organization := "io.sphere"

sbtPlugin := true

scalaVersion := "2.10.5"

libraryDependencies ++=
  "org.json4s" %% "json4s-jackson" % "3.2.11" ::
  Nil

libraryDependencies ++=
  "org.scalatest" % "scalatest_2.11" % "2.2.4" ::
  Nil map (_ % Test)

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

releaseSettings
bintraySettings