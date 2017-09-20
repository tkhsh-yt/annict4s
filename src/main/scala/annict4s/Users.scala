package annict4s

case class Users(
  users      : List[User],
  total_count: Int,
  next_page  : Option[Int],
  prev_page  : Option[Int]
) extends JsonToString[Users]

object Users {

  implicit val userssCodecJson: CodecJson[Users] =
    CodecJson.casecodec4(apply, unapply)(
      "users", "total_count", "next_page", "prev_page"
    )
}
