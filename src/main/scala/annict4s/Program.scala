package annict4s

case class Program(
  id             : Option[Long],
  started_at     : Option[DateTime],
  is_rebroadcast : Option[Boolean],
  channel        : Option[Program.Channel],
  work           : Option[Work],
  episode        : Option[Episode]
) extends JsonToString[Program]

object Program {

  case class Channel(
    id  : Long,
    name: String
  )

  object Channel {

    implicit val channelCodecJson: CodecJson[Channel] =
      CodecJson.casecodec2(apply, unapply)(
        "id", "name"
      )

  }

  implicit val programCodecJson: CodecJson[Program] =
    CodecJson.casecodec6(apply, unapply)(
      "id", "started_at", "is_rebroadcast",
      "channel", "work", "episode"
    )

}
