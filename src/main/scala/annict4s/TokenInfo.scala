package annict4s

case class TokenInfo(
  resource_owner_id : Long,
  scopes            : List[String],
  expires_in_seconds: Option[Long],
  application       : TokenInfo.Application,
  created_at        : DateTime
) extends JsonToString[TokenInfo]

object TokenInfo {

  case class Application(
    uid: String
  )

  object Application {

    implicit val applicationCodecJson: CodecJson[Application] =
      CodecJson.casecodec1(apply, unapply)("uid")

  }

  implicit val tokenInfoCodecJson: CodecJson[TokenInfo] =
    CodecJson.casecodec5(apply, unapply)(
      "resource_owner", "scopes", "expires_in_seconds",
      "application", "created_at"
    )
}
