package annict4s

case class AccessToken(
  access_token: String,
  token_type  : Option[String] = None,
  scope       : Option[String] = None,
  created_at  : Option[Long] = None
) {

  private[annict4s] val config: httpz.Config = httpz.Request.bearer(access_token)
}

object AccessToken {

  implicit val accessTokenCodecJson: CodecJson[AccessToken] =
    CodecJson.casecodec4(apply, unapply)(
      "access_token", "token_type", "scope", "created_at"
    )

}
