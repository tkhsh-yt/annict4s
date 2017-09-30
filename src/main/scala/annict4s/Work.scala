package annict4s

case class Work(
  id                : Option[Long],
  title             : Option[String],
  title_kana        : Option[String],
  media             : Option[String],
  media_text        : Option[String],
  season_name       : Option[String],
  season_name_text  : Option[String],
  released_on       : Option[String],
  released_on_about : Option[String],
  official_site_url : Option[String],
  wikipedia_url     : Option[String],
  twitter_username  : Option[String],
  twitter_hashtag   : Option[String],
  images            : Option[Work.Images],
  episodes_count    : Option[Int],
  watchers_count    : Option[Int],
  reviews_count     : Option[Int],
  no_episodes       : Option[Boolean],
  status            : Option[Status]
) extends JsonToString[Work]

object Work {

  case class Images(
    recommended_url : String,
    twitter         : Images.Twitter,
    facebook        : Images.Facebook
  )

  object Images {

    implicit val imagesCodecJson: CodecJson[Images] =
      CodecJson.casecodec3(apply, unapply)(
        "recommended_url",
        "twitter",
        "facebook"
      )

    case class Twitter(
      image_url          : String,
      mini_avatar_url    : String,
      normal_avatar_url  : String,
      bigger_avatar_url  : String,
      original_avatar_url: String
    ) extends JsonToString[Twitter]

    case class Facebook(
      og_image_url: String
    )

    object Twitter {

      implicit val twitterCodecJson: CodecJson[Twitter] =
        CodecJson.casecodec5(apply, unapply)(
          "image_url", "mini_avatar_url", "normal_avatar_url",
          "bigger_avatar_url", "original_avatar_url"
        )

    }

    object Facebook {

      implicit val facebookCodecJson: CodecJson[Facebook] =
        CodecJson.casecodec1(apply, unapply)(
          "og_image_url"
        )

    }
  }

  implicit val workCodecJson: CodecJson[Work] =
    CodecJson.casecodec19(apply, unapply)(
      "id", "title", "title_kana", "media", "media_text",
      "season_name", "season_name_text", "released_on",
      "released_on_about", "official_site_url", "wikipedia_url",
      "twitter_username", "twitter_hashtag", "images",
      "episodes_count", "watchers_count", "reviews_count",
      "no_episodes", "status"
    )
}
