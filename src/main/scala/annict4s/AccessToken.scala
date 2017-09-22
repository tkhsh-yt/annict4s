package annict4s

case class AccessToken(token: String) {

  private[annict4s] val config: httpz.Config = httpz.Request.bearer(token)
}
