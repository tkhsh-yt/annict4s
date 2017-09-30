package annict4s

import argonaut.DecodeJson
import httpz._
import scalaz._
import Scalaz._

/**
  * @see https://github.com/xuwei-k/ghscala/blob/master/src/main/scala/Command.scala
  */
sealed abstract class Command[A](val f: String => Request)(implicit val decoder: httpz.Request => httpz.Action[A]) {
  final def request: httpz.Request =
    requestWithURL(Annict.baseURL)

  final def requestWithURL(baseURL: String): httpz.Request =
    f(baseURL)

  final def actionWithURL(baseURL: String): httpz.Action[A] =
    decoder(requestWithURL(baseURL))

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

  implicit private[annict4s] def CoreJson[A: DecodeJson]: httpz.Request => httpz.Action[A] =
    Core.json[A]

  private[annict4s] def request(method: String, url: String, opt: Config = httpz.emptyConfig): String => Request = {
    baseURL => opt(Request(url = baseURL + url, method = method))
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
  ) extends Command[annict4s.Works](
    request(
      "GET",
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
      )
    )
  )

  final case class Episodes(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long = 0,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_sort_number: String = "desc"
  ) extends Command[annict4s.Episodes](
    request(
      "GET",
      "/v1/episodes",
      Request.params(
        ("fields"          , fields.mkString(",")),
        ("filter_ids"      , filter_ids.mkString(",")),
        ("filter_work_id"  , (filter_work_id > 0) ?? filter_work_id.toString),
        ("page"            , page.toString),
        ("per_page"        , per_page.toString),
        ("sort_id"         , sort_id.toString),
        ("sort_sort_number", sort_sort_number)
      )
    )
  )

  final case class Records(
    fields                   : List[String] = Nil,
    filter_ids               : List[Long] = Nil,
    filter_episode_id        : Long = 0,
    filter_has_record_comment: Boolean = true,
    page                     : Int = 1,
    per_page                 : Int = 25,
    sort_id                  : String = "desc",
    sort_likes_count         : String = "desc"
  ) extends Command[annict4s.Records](
    request(
      "GET",
      "/v1/records",
      Request.params(
        ("fields"           , fields.mkString(",")),
        ("filter_ids"       , filter_ids.mkString(",")),
        ("filter_episode_id", (filter_episode_id > 0) ?? filter_episode_id.toString),
        ("page"             , page.toString),
        ("per_page"         , per_page.toString),
        ("sort_id"          , sort_id),
        ("sort_likes_count" , sort_likes_count)
      )
    )
  )

  final case class Reviews(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long = 0,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_likes_count: String = "desc"
  ) extends Command[annict4s.Reviews](
    request(
      "GET",
      "/v1/reviews",
      Request.params(
        ("fields"          , fields.mkString(",")),
        ("filter_ids"      , filter_ids.mkString(",")),
        ("filter_work_id"  , (filter_work_id > 0) ?? filter_work_id.toString),
        ("page"            , page.toString),
        ("per_page"        , per_page.toString),
        ("sort_id"         , sort_id),
        ("sort_likes_count", sort_likes_count)
      )
    )
  )

  final case class Users(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_usernames: List[String] = Nil,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc"
  ) extends Command[annict4s.Users](
    request(
      "GET",
      "/v1/users",
      Request.params(
        ("fields"          , fields.mkString(",")),
        ("filter_ids"      , filter_ids.mkString(",")),
        ("filter_usernames", filter_usernames.mkString(",")),
        ("page"            , page.toString),
        ("per_page"        , per_page.toString),
        ("sort_id"         , sort_id)
      )
    )
  )

  final case class Following(
    fields         : List[String] = Nil,
    filter_user_id : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ) extends Command[annict4s.Users](
    request(
      "GET",
      "/v1/following",
      Request.params(
        ("fields"         , fields.mkString(",")),
        ("filter_user_id" , (filter_user_id > 0) ?? filter_user_id.toString),
        ("filter_username", filter_username),
        ("page"           , page.toString),
        ("per_page"       , per_page.toString),
        ("sort_id"        , sort_id)
      )
    )
  )

  final case class Followers(
    fields         : List[String] = Nil,
    filter_user_id : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ) extends Command[annict4s.Users](
    request(
      "GET",
      "/v1/followers",
      Request.params(
        ("fields"         , fields.mkString(",")),
        ("filter_user_id" , (filter_user_id > 0) ?? filter_user_id.toString),
        ("filter_username", filter_username),
        ("page"           , page.toString),
        ("per_page"       , per_page.toString),
        ("sort_id"        , sort_id)
      )
    )
  )

  final case class Activities(
    fields         : List[String] = Nil,
    filter_user_id : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ) extends Command[annict4s.Activities](
    request(
      "GET",
      "/v1/activities",
      Request.params(
        ("fields"         , fields.mkString(",")),
        ("filter_user_id" , (filter_user_id > 0) ?? filter_user_id.toString),
        ("filter_username", filter_username),
        ("page"           , page.toString),
        ("per_page"       , per_page.toString),
        ("sort_id"        , sort_id)
      )
    )
  )
}

object SelfCommand {

  import Command.{CoreJson, request}

  private[annict4s] val CoreString: Request => httpz.Action[String] = {
    Core.string(_).leftMap(Error.http)
  }

  final case class Me() extends Command[annict4s.User](
    request("GET", "/v1/me")
  )

  final case class Statuses(
    work_id: Long,
    kind   : Status.Kind
  ) extends Command[String](
    request(
      "POST",
      "/v1/me/statuses",
      Request.params(
        ("work_id", work_id.toString),
        ("kind"   , kind.toString)
      ))
  )(CoreString)

  final case class Record(
    episode_id    : Long,
    comment       : String = "",
    rating_state  : Option[Rating] = None,
    share_twitter : Boolean = false,
    share_facebook: Boolean = false
  ) extends Command[annict4s.Record](
    request(
      "POST",
      "/v1/me/records",
      Request.params(
        ("episode_id"    , episode_id.toString),
        ("comment"       , comment),
        ("rating_state"  , rating_state.map(_.toString).orZero),
        ("share_twitter" , share_twitter.toString),
        ("share_facebook", share_facebook.toString)
      )
    )
  )

  final case class UpdateRecord(
    id            : Long,
    comment       : String = "",
    rating_state  : Rating = Rating.NoSelect,
    share_twitter : Boolean,
    share_facebook: Boolean
  ) extends Command[annict4s.Record](
    request(
      "PATCH",
      s"/v1/me/records/${id}",
      Request.params(
        ("commnet"       , comment),
        ("rating_state"  , rating_state.toString),
        ("share_twitter" , share_twitter.toString),
        ("share_facebook", share_facebook.toString)
      )
    )
  )

  final case class DeleteRecord(
    id: Long
  ) extends Command[String](
    request(
      "DELETE",
      s"/v1/me/records/${id}"
    )
  )(CoreString)

  final case class Review(
    work_id               : Long,
    body                  : String = "",
    rating_animation_state: Rating = Rating.NoSelect,
    rating_music_state    : Rating = Rating.NoSelect,
    rating_story_state    : Rating = Rating.NoSelect,
    rating_character_state: Rating = Rating.NoSelect,
    rating_overall_state  : Rating = Rating.NoSelect,
    share_twitter         : Boolean = false,
    share_facebook        : Boolean = false
  ) extends Command[annict4s.Review](
    request(
      "POST",
      "/v1/me/reviews",
      Request.params(
        ("work_id"               , work_id.toString),
        ("body"                  , body),
        ("rating_animation_state", rating_animation_state.toString),
        ("rating_story_state"    , rating_story_state.toString),
        ("rating_character_state", rating_character_state.toString),
        ("raging_overall_state"  , rating_overall_state.toString),
        ("share_twitter"         , share_twitter.toString),
        ("share_facebook"        , share_facebook.toString)
      )
    )
  )

  final case class UpdateReview(
    id                    : Long,
    title                 : String,
    body                  : String,
    rating_animation_state: Rating = Rating.NoSelect,
    rating_music_state    : Rating = Rating.NoSelect,
    rating_story_state    : Rating = Rating.NoSelect,
    rating_character_state: Rating = Rating.NoSelect,
    rating_overall_state  : Rating = Rating.NoSelect,
    share_twitter         : Boolean = false,
    share_facebook        : Boolean = false
  ) extends Command[annict4s.Review](
    request(
      "PATCH",
      s"/v1/me/reviews/${id}",
      Request.params(
        ("title"                 , title),
        ("body"                  , body),
        ("rating_animation_state", rating_animation_state.toString),
        ("rating_music_state"    , rating_music_state.toString),
        ("rating_story_state"    , rating_story_state.toString),
        ("rating_character_state", rating_character_state.toString),
        ("rating_overall_state"  , rating_overall_state.toString),
        ("share_twitter"         , share_twitter.toString),
        ("share_facebook"        , share_facebook.toString)
      )
    )
  )

  final case class DeleteReview(
    id: Long
  ) extends Command[String](
    request(
      "DELETE",
      s"/v1/me/reviews/${id}"
    )
  )(CoreString)

  final case class Works(
    fields             : List[String] = Nil,
    filter_ids         : List[Long] = Nil,
    filter_season      : String = "",
    filter_title       : String = "",
    filter_status      : Status.Kind = Status.NoSelect,
    page               : Int = 1,
    per_page           : Int = 25,
    sort_id            : String = "desc",
    sort_season        : String = "desc",
    sort_watchers_count: String = "desc"
  ) extends Command[annict4s.Works](
    request(
      "GET",
      "/v1/me/works",
      Request.params(
        ("fields"             , fields.mkString(",")),
        ("filter_ids"         , filter_ids.mkString(",")),
        ("filter_season"      , filter_season),
        ("filter_title"       , filter_title),
        ("page"               , page.toString),
        ("per_page"           , per_page.toString),
        ("sort_id"            , sort_id),
        ("sort_season"        , sort_season),
        ("sort_watchers_count", sort_watchers_count)
      ) |+| (filter_status == Status.NoSelect) ?? Request.params(
        ("filter_status"      , filter_status.toString)
      )
    )
  )

  final case class Programs(
    fields              : List[String] = Nil,
    filter_ids          : List[Long] = Nil,
    filter_channel_ids  : List[Long] = Nil,
    filter_work_ids     : List[Long] = Nil,
    filter_started_at_gt: String = "",
    filter_started_at_lt: String = "",
    filter_unwatched    : Boolean = true,
    filter_rebroadcast  : Boolean = true,
    page                : Int = 1,
    per_page            : Int = 25,
    sort_id             : String = "desc",
    sort_started_at     : String = "desc"
  ) extends Command[annict4s.Programs](
    request(
      "GET",
      "/v1/me/programs",
      Request.params(
        ("fields"              , fields.mkString(",")),
        ("filter_ids"          , filter_ids.mkString(",")),
        ("filter_channel_ids"  , filter_channel_ids.mkString(",")),
        ("filter_work_ids"     , filter_work_ids.mkString(",")),
        ("filter_started_at_gt", filter_started_at_gt),
        ("filter_started_at_lt", filter_started_at_lt),
        ("filter_unwatched"    , filter_unwatched.toString),
        ("filter_rebroadcast"  , filter_rebroadcast.toString),
        ("page"                , page.toString),
        ("per_page"            , per_page.toString),
        ("sort_id"             , sort_id),
        ("sort_started_at"     , sort_started_at)
      )
    )
  )

  final case class FollowingActivities(
    fields        : List[String] = Nil,
    filter_actions: List[Activity.Action] = Nil,
    filter_muted  : Boolean = true,
    page          : Int = 1,
    per_page      : Int = 25,
    sort_id       : String = "desc"
  ) extends Command[annict4s.Activities](
    request(
      "GET",
      "/v1/me/following_activities",
      Request.params(
        ("fields"        , fields.mkString(",")),
        ("filter_actions", fields.mkString(",")),
        ("filter_muted"  , filter_muted.toString),
        ("page"          , page.toString),
        ("per_page"      , per_page.toString),
        ("sort_id"       , sort_id)
      )
    )
  )
}

object OAuthCommand {

  import Command.{request, CoreJson}

  case class Token(
    client_id    : String,
    client_secret: String,
    redirect_uri : String,
    code         : String
  ) extends Command[annict4s.AccessToken](
    request(
      "POST",
      "/oauth/token",
      Request.params(
        ("client_id"    , client_id),
        ("client_secret", client_secret),
        ("grant_type"   , "authorization_code"),
        ("redirect_uri" , redirect_uri),
        ("code"         , code)
      )
    )
  )

  case class Info() extends Command[annict4s.TokenInfo](
    request(
      "GET",
      "/oauth/token/info"
    )
  )

  case class Revoke(
    client_id    : String,
    client_secret: String,
    token        : String
  ) extends Command[Unit](
    request(
      "POST",
      "/oauth/revoke",
      Request.params(
        ("client_id"    , client_id.toString),
        ("client_secret", client_secret.toString),
        ("token"        , token)
      ) 
    )
  )
}
