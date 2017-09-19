package annict4s

case class Program(
  id             : Long,
  started_at     : DateTime,
  is_rebroadcast : Boolean,
  channel        : String,
  work           : Work,
  episode        : Episode
) extends JsonToString[Program]

object Program {

  implicit val programCodecJson: CodecJson[Program] =
    CodecJson.casecodec6(apply, unapply)(
      "id", "started_at", "is_rebroadcast",
      "channel", "work", "episode"
    )

}
