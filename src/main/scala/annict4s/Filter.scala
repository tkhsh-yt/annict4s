package annict4s

trait UserFilter
case class UserId(id: Long)           extends UserFilter
case class UserName(username: String) extends UserFilter
