package annict4s

case class Record(
  id             : Long,
  commnet        : String,
  rating_state   : Rating,
  is_modified    : Boolean,
  likes_count    : Int,
  comments_count : Int,
  created_at     : DateTime,
  user           : User,
  work           : Work,
  episode        : Episode
) extends JsonToString[Record]

object Record {

  implicit val recordCodecJson: CodecJson[Record] =
    CodecJson.casecodec10(apply, unapply)(
      "id", "comment", "rating_state", "is_modified",
      "likes_count", "comments_count", "created_at",
      "user", "work", "episode"
    )

}
