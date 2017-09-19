package annict4s

case class Works(
  works      : List[Work],
  total_count: Int,
  next_page  : Option[Int],
  prev_page  : Option[Int]
) extends JsonToString[Works]

object Works {

  implicit val worksCodecJson: CodecJson[Works] =
    CodecJson.casecodec4(apply, unapply)(
      "works", "total_count", "next_page", "prev_page"
    )
}
