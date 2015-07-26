name := "json-schema-inliner-plugin"
organization := "io.sphere"

sbtPlugin := true

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++=
  "org.json4s" %% "json4s-jackson" % "3.2.11" ::
  Nil

libraryDependencies ++=
  "org.scalatest" % "scalatest_2.11" % "2.2.4" ::
  Nil map (_ % Test)
