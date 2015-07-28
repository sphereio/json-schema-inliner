package io.sphere

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object JsonSchemaInlinerPlugin extends AutoPlugin {

  object autoImport {
    lazy val jsonInliner = taskKey[Seq[File]]("inline json schemas")

    lazy val inlineDestinationF = taskKey[File ⇒ File]("calculate path of inlined schema based on relative path from source")
  }

  import autoImport._


  // if not set, the JvmPlugin overwrites the resourceGenerators
  override def requires: Plugins = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    includeFilter in jsonInliner := GlobFilter("*.schema.json"),

    inlineDestinationF in Compile := {
      val newBase = (resourceManaged in Compile).value
      println(newBase)
      (f: File) ⇒ new File(newBase, new File(f.getParentFile, "inline/" + f.getName).getPath)
    },

    jsonInliner in Compile := {
      val sources = (unmanagedResourceDirectories in Compile).value flatMap { s ⇒
        s.descendantsExcept((includeFilter in jsonInliner).value, (excludeFilter in jsonInliner).value).get.map(f ⇒ (s, f))
      }
      JsonSchemaInliner.inline(sources, (inlineDestinationF in Compile).value, streams.value)
    },

    resourceGenerators in Compile <+= (jsonInliner in Compile)

  )
}
