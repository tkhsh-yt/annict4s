package annict4s

case class Review(
  id                     : Option[Long],
  title                  : Option[String],
  body                   : Option[String],
  rating_animation_state : Option[Rating],
  rating_music_state     : Option[Rating],
  rating_story_state     : Option[Rating],
  rating_character_state : Option[Rating],
  rating_overall_state   : Option[Rating],
  likes_count            : Option[Int],
  impressions_count      : Option[Int],
  created_at             : Option[DateTime],
  modified_at            : Option[DateTime],
  user                   : Option[User],
  work                   : Option[Work]
) extends JsonToString[Review]

object Review {

  implicit val reviewCodecJson: CodecJson[Review] =
    CodecJson.casecodec14(apply, unapply)(
      "id", "title", "body", "rating_animation_state",
      "rating_music_state", "rating_story_state",
      "rating_character_state", "rating_overall_state",
      "likes_count", "impressions_count", "created_at",
      "modified_at", "user", "work"
    )

}
