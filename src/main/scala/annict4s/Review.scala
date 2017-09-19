package annict4s

case class Review(
  id                     : Long,
  title                  : String,
  body                   : String,
  rating_animation_state : Rating,
  rating_music_state     : Rating,
  rating_story_state     : Rating,
  rating_character_state : Rating,
  rating_overall_state   : Rating,
  likes_count            : Int,
  impressions_count      : Int,
  created_at             : DateTime,
  modified_at            : DateTime,
  user                   : User,
  work                   : Work
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
