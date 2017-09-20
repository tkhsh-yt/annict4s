package annict4s

case class Record(
  id             : Option[Long],
  comment        : Option[String],
  rating_state   : Option[Rating],
  is_modified    : Option[Boolean],
  likes_count    : Option[Int],
  comments_count : Option[Int],
  created_at     : Option[DateTime],
  user           : Option[User],
  work           : Option[Work],
  episode        : Option[Episode]
) extends JsonToString[Record]

object Record {

  implicit val recordCodecJson: CodecJson[Record] =
    CodecJson.casecodec10(apply, unapply)(
      "id", "comment", "rating_state", "is_modified",
      "likes_count", "comments_count", "created_at",
      "user", "work", "episode"
    )

}
