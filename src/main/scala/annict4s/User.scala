package annict4s

case class User(
  id                   : Option[Long],
  username             : Option[String],
  name                 : Option[String],
  description          : Option[String],
  url                  : Option[String],
  avatar_url           : Option[String],
  background_image_url : Option[String],
  records_count        : Option[Int],
  followings_count     : Option[Int],
  followers_count      : Option[Int],
  wanna_watch_count    : Option[Int],
  watching_count       : Option[Int],
  watched_count        : Option[Int],
  on_hold_count        : Option[Int],
  stop_watching_count  : Option[Int],
  created_at           : Option[DateTime]
) extends JsonToString[User]

object User {

  implicit val userCodecJson: CodecJson[User] =
    CodecJson.casecodec16(apply, unapply)(
      "id", "username", "name", "description",
      "url", "avatar_url", "background_image_url",
      "records_count", "followings_count",
      "followers_count", "wanna_watch_count",
      "watching_count", "watched_count",
      "on_hold_count", "stop_watching_count",
      "created_at"
    )
}
