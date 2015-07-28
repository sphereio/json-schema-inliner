package io.sphere

import java.io.File
import java.nio.charset.Charset

import org.json4s._
import org.json4s.jackson.{parseJson, prettyJson}
import sbt.Keys._
import sbt._


class JsonSchemaInliner(source: File, streams: TaskStreams) {

  private val baseDir = source.getParentFile

  def inline(target: File): Option[File] = {
    val schemas = toInline
    val moreRecent = schemas.values.map(_._1.lastModified()).max
    if (target.exists() && target.lastModified() == moreRecent) {
      // no need to update
      streams.log.debug( s""""$target" already up to dated""")
      None
    } else {
      streams.log.debug( s"""inline "$source" to "$target"""")
      val json = parseJson(FileInput(source))
      val newContent = inlineSchema(json, schemas.mapValues(_._2))
      IO.write(target, prettyJson(newContent).getBytes(Charset.forName("UTF-8")))
      target.setLastModified(moreRecent)
      Some(target)
    }
  }

  private def toInline: Map[String, (File, JValue)] = {
    def step(schema: String, curr: Map[String, (File, JValue)]): Map[String, (File, JValue)] = {
      val file = new File(baseDir, schema)
      val json = parseJson(FileInput(file))
      val updated = curr + (schema → (file, json))
      references(json).foldLeft(updated) { (acc, ref) ⇒
        if (acc.contains(ref)) acc else acc ++ step(ref, acc)
      }
    }
    step(source.getName, Map.empty)
  }

  private def inlineSchema(json: JValue, raj: Map[String, JValue]): JValue = {
    json.transform {
      case JObject(JField("$ref", JString(s: String)) :: List()) => raj.getOrElse(s, JObject(List(JField("$ref", JString(s)))))
      case JObject(JField("$ref", JString(s: String)) :: otherFields) =>
        raj.get(s).map {
          case JObject(fields) => JObject(fields ::: otherFields)
          case o => o
        }.getOrElse(JObject(List(JField("$ref", JString(s)))))
    }
  }

  private def references(json: JValue): Set[String] =
    (json filterField {
      case JField("$ref", _) => true
      case _ => false
    } flatMap {
      case JField(_, JString(s: String)) => if (!s.startsWith("#")) List(s) else List()
      case _ => List()
    }).toSet

  private def removeSchema(p: JValue): JValue =
    p.removeField {
      case JField("$schema", _) => true
      case _ => false
    }
}

object JsonSchemaInliner {
  def inline(sources: Seq[(File, File)], destinationF: File ⇒ File, streams: TaskStreams): Seq[File] = {
    sources flatMap { case (base, src) =>
      val inliner = new JsonSchemaInliner(src, streams)
      val relative = IO.relativizeFile(base, src).getOrElse(src)
      val target = destinationF(relative)
      streams.log.debug(
        s"""base: $base
           |src: $src
           |relative: $relative
           |target: $target
         """.stripMargin)
      inliner.inline(target)
    }
  }
}