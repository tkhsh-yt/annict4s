package annict4s

case class Programs(
  programs   : List[Program],
  total_count: Int,
  next_page  : Option[Int],
  prev_page  : Option[Int]
) extends JsonToString[Programs]

object Programs {

  implicit val programsCodecJson: CodecJson[Programs] =
    CodecJson.casecodec4(apply, unapply)(
      "programs", "total_count", "next_page", "prev_page"
    )
}
