package annict4s

case class Episode(
  id                    : Long,
  number                : Int,
  number_text           : String,
  sort_number           : Int,
  title                 : String,
  records_count         : Int,
  record_comments_count : Int,
  work                  : Option[Work],
  prev_episode          : Option[Episode],
  next_episode          : Option[Episode]
) extends JsonToString[Episode]

object Episode {

  implicit val episodeCodecJson: CodecJson[Episode] =
    CodecJson.casecodec10(apply, unapply)(
      "id", "number", "number_text", "sort_number",
      "title", "records_count", "record_comments_count",
      "work", "prev_episode", "next_episode"
    )

}
