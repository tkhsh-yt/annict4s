package annict4s

case class Reviews(
  reviews    : List[Review],
  total_count: Int,
  next_page  : Option[Int],
  prev_page  : Option[Int]
) extends JsonToString[Reviews]

object Reviews {

  implicit val reviewsCodecJson: CodecJson[Reviews] =
    CodecJson.casecodec4(apply, unapply)(
      "reviews", "total_count", "next_page", "prev_page"
    )
}
