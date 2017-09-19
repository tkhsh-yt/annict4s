package annict4s

case class Status(
  kind: Status.Kind
)

object Status {

  trait Kind
  case object WannaWatch extends Kind {
    override def toString = "wanna_watch"
  }
  case object Watching extends Kind {
    override def toString = "watching"
  }
  case object Watched extends Kind {
    override def toString = "watched"
  }
  case object OnHold extends Kind {
    override def toString = "on_hold"
  }
  case object StopWatching extends Kind {
    override def toString = "stop_watching"
  }
  case object NoSelect extends Kind {
    override def toString = "no_select"
  }

  object Kind {

    import argonaut.{EncodeJson, DecodeJson}
    import scalaz.syntax.std.option._

    implicit val kindCodecJson: CodecJson[Kind] =
      CodecJson.derived(
        EncodeJson.jencode1(_.toString()),
        DecodeJson.optionDecoder({
          _.string.flatMap {
            case "wanna_watch"   => WannaWatch.some
            case "watching"      => Watching.some
            case "watched"       => Watched.some
            case "on_hold"       => OnHold.some
            case "stop_watching" => StopWatching.some
            case "no_select"     => NoSelect.some
            case _               => None
          }
        }, "Kind")
      )
  }

  implicit val statusCodecJson: CodecJson[Status] =
    CodecJson.casecodec1(apply, unapply)("kind")
}
