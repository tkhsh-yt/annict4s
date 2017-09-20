package annict4s

case class Records(
  records   : List[Record],
  total_count: Int,
  next_page  : Option[Int],
  prev_page  : Option[Int]
) extends JsonToString[Records]

object Records {

  implicit val episodesCodecJson: CodecJson[Records] =
    CodecJson.casecodec4(apply, unapply)(
      "records", "total_count", "next_page", "prev_page"
    )
}
