package annict4s

case class Profile(
  id                   : Option[Long],
  username             : Option[String],
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
  created_at           : Option[DateTime],
  email                : Option[String],
  notifications_count  : Option[Int]
) extends JsonToString[Profile]

object Profile {

  implicit val profileCodecJson: CodecJson[Profile] =
    CodecJson.casecodec17(apply, unapply)(
      "id", "username", "description", "url", "avatar_url",
      "background_image_url", "records_count", "followings_count",
      "followers_count", "wanna_watch_count", "watching_count",
      "watched_count", "on_hold_count", "stop_watching_count",
      "created_at", "email", "notifications_count"
    )

}
