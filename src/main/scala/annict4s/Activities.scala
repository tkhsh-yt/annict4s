package annict4s

case class Activities(
  activities : List[Activity],
  total_count: Int,
  next_page  : Option[Int],
  prev_page  : Option[Int]
) extends JsonToString[Activities]

object Activities {

  implicit val activitiesCodecJson: CodecJson[Activities] =
    CodecJson.casecodec4(apply, unapply)(
      "activities", "total_count", "next_page", "prev_page"
    )
}
