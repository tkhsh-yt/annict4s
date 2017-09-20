package annict4s

case class Episodes(
  episodes   : List[Episode],
  total_count: Int,
  next_page  : Option[Int],
  prev_page  : Option[Int]
) extends JsonToString[Episodes]

object Episodes {

  implicit val episodesCodecJson: CodecJson[Episodes] =
    CodecJson.casecodec4(apply, unapply)(
      "episodes", "total_count", "next_page", "prev_page"
    )
}
