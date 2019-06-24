package org.zella.tuapse.smart.commands

import java.nio.file.{Path, Paths}

import ai.x.play.json.Jsonx
import better.files.File
import org.zella.tuapse.smart.utils.Utils
import play.api.libs.json.{Format, Json}

import scala.collection.JavaConverters._

sealed trait WordLike {
  def parameter: String
}

object WordLike {
  implicit lazy val jsonFormat: Format[WordLike] = Jsonx.formatSealed[WordLike]
}

case class Word(parameter: String, variants: Set[Variant] = Set.empty) extends WordLike

object Word {
  implicit val format: Format[Word] = Json.format[Word]
}

case class Any(parameter: String) extends WordLike

object Any {
  implicit val format: Format[Any] = Json.format[Any]
}

case class Variant(value: String, synonyms: Set[String])

object Variant {
  implicit val format: Format[Variant] = Json.format[Variant]
}

case class CommandDef(sentence: List[WordLike]) {

  private lazy val regexp = {

    def acc(r: String, sentence: List[WordLike]): String = {
      sentence match {
        case x :: xs =>
          x match {
            case Word(p, variants) => acc(r + "(" + variants.flatMap(_.synonyms.map(s => "\\s?" + s + "\\s?")).mkString("|") + ")", xs)
            case Any(p) => acc(r + "(.*)", xs)
          }
        case Nil => r
      }
    }

    (acc("^", sentence) + "$").r(sentence.map(_.parameter): _*)
  }

  def matches(words: Seq[String]): Option[Map[String, String]] = {

    val text = words.mkString(" ")

    if (text.matches(regexp.regex)) {
      val r = regexp
        .findFirstMatchIn(text)
        .map(m => m.groupNames.map(g => g -> Option(m.group(g)))
          .collect { case (key, Some(value)) => (key, value) }.toMap
          .mapValues(v =>
            sentence.flatMap {
              case Word(_, variants) => variants.find(vr => vr.synonyms.contains(v.trim)).map(_.value)
              case Any(_) => Some(v)
            }.headOption
          ).collect { case (key, Some(value)) => (key, value) })
      r
    }

    else None
  }

}


object CommandDef {

  case class MediaName(text: String)

  implicit val format: Format[CommandDef] = Json.format[CommandDef]

  private val path: Path = Paths.get(sys.env.getOrElse("COMMANDS_JSON", "commands.json"))

  val commands: Set[CommandDef] = Json.parse(File(path).contentAsString).as[Set[CommandDef]]

  //support "find media subject"
  def extractMediaSubject(text: String): Option[MediaName] = {
    commands
      .flatMap(c => c.matches(Utils.wordsLowerCase(text).asScala)).headOption
      .flatMap(params => {
        if (params.keySet == Set("find", "media", "subject"))
          Some(MediaName(params("subject")))
        else None
      })
  }

}