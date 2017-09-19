package annict4s

case class Work(
  id                : Long,
  title             : String,
  title_kana        : String,
  media             : String,
  media_text        : String,
  season_name       : String,
  season_name_text  : String,
  released_on       : String,
  released_on_about : String,
  official_site_url : String,
  wikipedia_url     : String,
  twitter_username  : String,
  twitter_hashtag   : String,
  images            : Option[Work.Images],
  episodes_count    : Int,
  watchers_count    : Int,
  reviews_count     : Int,
  no_episodes       : Boolean,
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
      mini_avatar_url   : String,
      normal_avatar_url : String,
      bigger_avatar_url : String,
      original_url      : String
    ) extends JsonToString[Twitter]

    case class Facebook(
      og_image_url: String
    )
    
    object Twitter {

      implicit val twitterCodecJson: CodecJson[Twitter] =
        CodecJson.casecodec4(apply, unapply)(
          "mini_avatar_url", "normal_avatar_url",
          "bigger_avatar_url", "original_url"
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
      "twitter_username", "twitter_hashtag", "iamges",
      "episodes_count", "watchers_count", "reviews_count",
      "no_episodes", "status"
    )
}
