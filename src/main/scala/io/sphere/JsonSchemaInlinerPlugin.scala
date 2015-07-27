package io.sphere

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object JsonSchemaInlinerPlugin extends AutoPlugin {

  object autoImport {
    lazy val jsonInliner = taskKey[Seq[File]]("inline json schemas")

    lazy val inlineDestination = taskKey[File]("destination of inline json schemas")
  }

  import autoImport._


  // if not set, the JvmPlugin overwrites the resourceGenerators
  override def requires: Plugins = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    includeFilter := GlobFilter("*.schema.json"),
    inlineDestination in Compile <<= (resourceManaged in Compile) map (f => new File(f, "inline")),

    jsonInliner in Compile := {
      val sources = (unmanagedResourceDirectories in Compile).value flatMap { s ⇒
        s.descendantsExcept(includeFilter.value, excludeFilter.value).get.map(f ⇒ (s, f))
      }
      JsonSchemaInliner.inline(sources, (inlineDestination in Compile).value, streams.value)
    },

    resourceGenerators in Compile <+= (jsonInliner in Compile)

  )
}
