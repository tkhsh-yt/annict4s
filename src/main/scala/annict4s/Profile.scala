package annict4s

case class Profile(
  id                   : Long,
  username             : String,
  description          : String,
  url                  : String,
  avatar_url           : String,
  background_image_url : String,
  records_count        : Int,
  followings_count     : Int,
  followers_count      : Int,
  wanna_watch_count    : Int,
  watching_count       : Int,
  watched_count        : Int,
  on_hold_count        : Int,
  stop_watching_count  : Int,
  created_at           : DateTime,
  email                : String,
  notifications_count  : Int
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
