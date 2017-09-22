package annict4s

import argonaut.DecodeJson
import httpz._
import scalaz.{Free, Inject, NonEmptyList}
import scalaz.syntax.monoid._

/**
  * @see https://github.com/xuwei-k/ghscala/blob/master/src/main/scala/Command.scala
  */
sealed abstract class Command[A](val f: String => Request)(implicit val decoder: DecodeJson[A]) {
  final def request: httpz.Request =
    requestWithURL(Annict.baseURL)

  final def requestWithURL(baseURL: String): httpz.Request =
    f(baseURL)

  final def actionWithURL(baseURL: String): httpz.Action[A] =
    Core.json[A](requestWithURL(baseURL))(decoder)

  final def action: httpz.Action[A] =
    actionWithURL(Annict.baseURL)

  final def lift[F[_]](implicit I: Inject[Command, F]): Free[F, A] =
    Free.liftF(I.inj(this))

  final def actionEOps: ActionEOps[httpz.Error, A] =
    new ActionEOps(action)

  final def nel: ActionE[NonEmptyList[httpz.Error], A] =
    actionEOps.nel
}

object Command {

  private[annict4s] def request(url: String, opt: Config = httpz.emptyConfig): String => Request = {
    baseURL => opt(Request(url = baseURL + url))
  }

  final case class Works(
    fields             : List[String] = Nil,
    filter_ids         : List[Long] = Nil,
    filter_season      : String = "",
    filter_title       : String = "",
    page               : Int = 1,
    per_page           : Int = 25,
    sort_id            : String = "desc",
    sort_season        : String = "desc",
    sort_watchers_count: String = "desc"
  )(token: AccessToken) extends Command[annict4s.Works](
    request(
      "/v1/works",
      Request.params(
        ("fields"             , fields.mkString(",")),
        ("filter_ids"         , filter_ids.mkString(",")),
        ("filter_season"      , filter_season),
        ("filter_title"       , filter_title),
        ("page"               , page.toString),
        ("per_page"           , per_page.toString),
        ("sort_id"            , sort_id.toString),
        ("sort_season"        , sort_season),
        ("sort_watchers_count", sort_watchers_count)
      ) |+| token.config
    )
  )

  final case class Episodes(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_sort_number: String = "desc"
  )(token: AccessToken) extends Command[annict4s.Episodes](
    request(
      "/v1/episodes",
      Request.params(
        ("fields"          , fields.mkString(",")),
        ("filter_ids"      , filter_ids.mkString(",")),
        ("filter_work_id"  , filter_work_id.toString),
        ("page"            , page.toString),
        ("per_page"        , per_page.toString),
        ("sort_id"         , sort_id.toString),
        ("sort_sort_number", sort_sort_number)
      ) |+| token.config
    )
  )

  final case class Records(
    fields                   : List[String] = Nil,
    filter_ids               : List[Long] = Nil,
    filter_episode_id        : Long,
    filter_has_record_comment: Boolean = true,
    page                     : Int = 1,
    per_page                 : Int = 25,
    sort_id                  : String = "desc",
    sort_likes_count         : String = "desc"
  )(token: AccessToken) extends Command[annict4s.Records](
    request(
      "/v1/records",
      Request.params(
        ("fields"           , fields.mkString(",")),
        ("filter_ids"       , filter_ids.mkString(",")),
        ("filter_episode_id", filter_episode_id.toString),
        ("page"             , page.toString),
        ("per_page"         , per_page.toString),
        ("sort_id"          , sort_id),
        ("sort_likes_count" , sort_likes_count)
      ) |+| token.config
    )
  )

  final case class Reviews(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_likes_count: String = "desc"
  )(token: AccessToken) extends Command[annict4s.Reviews](
    request(
      "/v1/reviews",
      Request.params(
        ("fields"          , fields.mkString(",")),
        ("filter_ids"      , filter_ids.mkString(",")),
        ("filter_work_id"  , filter_work_id.toString),
        ("page"            , page.toString),
        ("per_page"        , per_page.toString),
        ("sort_id"         , sort_id),
        ("sort_likes_count", sort_likes_count)
      ) |+| token.config
    )
  )

  final case class Users(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_usernames: List[String] = Nil,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc"
  )(token: AccessToken) extends Command[annict4s.Users](
    request(
      "/v1/users",
      Request.params(
        ("fields"          , fields.mkString(",")),
        ("filter_ids"      , filter_ids.mkString(",")),
        ("filter_usernames", filter_usernames.mkString(",")),
        ("page"            , page.toString),
        ("per_page"        , per_page.toString),
        ("sort_id"         , sort_id)
      ) |+| token.config
    )
  )

  final case class Following(
    fields         : List[String] = Nil,
    filter_ids     : List[Long] = Nil,
    filter_username: String,
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  )(token: AccessToken) extends Command[annict4s.Users](
    request(
      "/v1/following",
      Request.params(
        ("fields"         , fields.mkString(",")),
        ("filter_ids"     , filter_ids.mkString(",")),
        ("filter_username", filter_username),
        ("page"           , page.toString),
        ("per_page"       , per_page.toString),
        ("sort_id"        , sort_id)
      ) |+| token.config
    )
  )

  final case class Followers(
    fields         : List[String] = Nil,
    filter_ids     : List[Long] = Nil,
    filter_username: String,
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  )(token: AccessToken) extends Command[annict4s.Users](
    request(
      "/v1/followers",
      Request.params(
        ("fields"         , fields.mkString(",")),
        ("filter_ids"     , filter_ids.mkString(",")),
        ("filter_username", filter_username),
        ("page"           , page.toString),
        ("per_page"       , per_page.toString),
        ("sort_id"        , sort_id)
      ) |+| token.config
    )
  )

  final case class Activities(
    fields     : List[String] = Nil,
    filter_user: UserFilter,
    page       : Int = 1,
    per_page   : Int = 25,
    sort_id    : String = "desc"
  )(token: AccessToken) extends Command[annict4s.Activities](
    request(
      "/v1/activities",
      Request.params(
        ("fields"         , fields.mkString(",")),
        filter_user match {
          case UserId(id)       => ("filter_user_id", id.toString)
          case UserName(username) => ("filter_username", username)
        },
        ("page"           , page.toString),
        ("per_page"       , per_page.toString),
        ("sort_id"        , sort_id)
      ) |+| token.config
    )
  )
}

object SelfCommand {

  import Command.request

  case class Me(token: AccessToken) extends Command[annict4s.User](
    request("/v1/me", token.config)
  )

  // case class Statuses(
  //   work_id: Long,
  //   kind   : Status.Kind
  // )(accessToken: AccessToken) extends Command[NoContent](
  //   post_with_oauth("/v1/me/statuses", Request.params(
  //     ("work_id", work_id.toString),
  //     ("kind"   , kind.toString)
  //   ))(accessToken)
  // )
}
