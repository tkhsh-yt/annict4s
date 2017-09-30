package annict4s

import httpz._
import scalaz.{Inject, Free, Reader}

object Annict4s extends Annict[Command, Free[Command, ?]] {
  override protected[this] def f[A](c: Command[A]) = lift(c)
}

object Annict extends Annict[Command, Action] {

  def commands2Action[A](a: Free[Command, A]): Action[A] =
    a.foldMap(Interpreter)(httpz.ActionMonad)

  protected[this] override def f[A](c: Command[A]) =
    commands2Action(lift(c))

  private[annict4s] final val baseURL = "https://api.annict.com"
}

sealed abstract class Annict[F[_], G[_]](implicit I: Inject[Command, F]) {

  final type FreeF[A] = Free[F, A]

  final def lift[A](f: Command[A]): FreeF[A] =
    Free.liftF(I.inj(f))

  protected[this] def f[A](c: Command[A]): G[A]

  def works(
    fields              : List[String] = Nil,
    filter_ids          : List[Long] = Nil,
    filter_season       : String = "",
    filter_title        : String = "",
    page                : Int = 1,
    per_page            : Int = 25,
    sort_id             : String = "desc",
    sort_season         : String = "desc",
    sort_watchers_count : String = "desc"
  ): G[Works] =
    f(Command.Works(
        fields, filter_ids, filter_season, filter_title,
        page, per_page, sort_id, sort_season, sort_watchers_count
      ))

  def episodes(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long = 0,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_sort_number: String = "desc"
  ): G[Episodes] =
    f(Command.Episodes(
        fields, filter_ids, filter_work_id, page,
        per_page, sort_id, sort_sort_number
      ))

  def records(
    fields                    : List[String] = Nil,
    filter_ids                : List[Long] = Nil,
    filter_episode_id         : Long = 0,
    filter_has_record_comment : Boolean = true,
    page                      : Int = 1,
    per_page                  : Int = 25,
    sort_id                   : String = "desc",
    sort_likes_count          : String = "desc"
  ): G[Records] =
    f(Command.Records(
        fields, filter_ids, filter_episode_id,
        filter_has_record_comment, page, per_page,
        sort_id, sort_likes_count
      ))

  def reviews(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long = 0,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_likes_count: String = "desc"
  ): G[Reviews] =
    f(Command.Reviews(
        fields, filter_ids, filter_work_id,
        page, per_page, sort_id, sort_likes_count
      ))

  def users(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_usernames: List[String] = Nil,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc"
  ): G[Users] =
    f(Command.Users(
        fields, filter_ids, filter_usernames,
        page, per_page, sort_id
      ))

  def following(
    fields         : List[String] = Nil,
    filter_id      : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): G[Users] =
    f(Command.Following(
        fields, filter_id, filter_username,
        page, per_page, sort_id
      ))

  def followers(
    fields         : List[String] = Nil,
    filter_id      : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): G[Users] =
    f(Command.Followers(
        fields, filter_id, filter_username,
        page, per_page, sort_id
      ))

  def activities(
    fields         : List[String] = Nil,
    filter_user_id : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): G[Activities] =
    f(Command.Activities(
        fields, filter_user_id, filter_username, page, per_page, sort_id
      ))

  object Me {

    def apply(): G[User] =
      f(SelfCommand.Me())

    def statuses(
      work_id: Long,
      kind   : Status.Kind
    ): G[String] =
      f(SelfCommand.Statuses(work_id, kind))

    def postRecord(
      episode_id    : Long,
      comment       : String = "",
      rating_state  : Option[Rating] = None,
      share_twitter : Boolean = false,
      share_facebook: Boolean = false
    ): G[Record] =
      f(SelfCommand.Record(
          episode_id, comment,
          rating_state, share_twitter
        ))

    def updateRecord(
      id            : Long,
      comment       : String = "",
      rating_state  : Rating = Rating.NoSelect,
      share_twitter : Boolean,
      share_facebook: Boolean
    ): G[Record] =
      f(SelfCommand.UpdateRecord(
          id, comment, rating_state, share_twitter, share_facebook
        ))

    def deleteRecord(
      id: Long
    ): G[String] =
      f(SelfCommand.DeleteRecord(id))

    def postReview(
      work_id               : Long,
      body                  : String = "",
      rating_animation_state: Rating = Rating.NoSelect,
      rating_music_state    : Rating = Rating.NoSelect,
      rating_story_state    : Rating = Rating.NoSelect,
      rating_character_state: Rating = Rating.NoSelect,
      rating_overall_state  : Rating = Rating.NoSelect,
      share_twitter         : Boolean = false,
      share_facebook        : Boolean = false
    ): G[Review] =
      f(SelfCommand.Review(
          work_id, body, rating_animation_state, rating_music_state,
          rating_story_state, rating_character_state,
          rating_overall_state, share_twitter, share_facebook
        ))

    def updateReview(
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
    ): G[Review] =
      f(SelfCommand.UpdateReview(
          id, title, body, rating_animation_state, rating_music_state,
          rating_story_state, rating_character_state,
          rating_overall_state, share_twitter, share_facebook
        ))

    def deleteReview(
      id: Long
    ): G[String] =
      f(SelfCommand.DeleteReview(id))

    def works(
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
    ): G[Works] =
      f(SelfCommand.Works(
          fields, filter_ids, filter_season, filter_title,
          filter_status, page, per_page, sort_id,
          sort_season, sort_watchers_count
        ))

    def programs(
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
    ): G[Programs] =
      f(SelfCommand.Programs(
          fields, filter_ids, filter_channel_ids, filter_work_ids,
          filter_started_at_gt, filter_started_at_lt,
          filter_unwatched, filter_rebroadcast, page,
          per_page, sort_id, sort_started_at
        ))

    def followingActivites(
      fields        : List[String] = Nil,
      filter_actions: List[Activity.Action] = Nil,
      filter_muted  : Boolean = true,
      page          : Int = 1,
      per_page      : Int = 25,
      sort_id       : String = "desc"
    ): G[Activities] =
      f(SelfCommand.FollowingActivities(
          fields, filter_actions, filter_muted,
          page, per_page, sort_id
        ))
  }

  object OAuth {

    def authorize(
      client_id    : String,
      response_type: String = "code",
      redirect_uri : String = "urn:ietf:wg:oauth:2.0:oob",
      scope        : String = "read+write"
    ): String =
      s"https://annict.jp/oauth/authorize?client_id=${client_id}&response_type=${response_type}&redirect_uri=${redirect_uri}&scope=${scope}"

    def token(
      client_id    : String,
      client_secret: String,
      redirect_uri : String = "urn:ietf:wg:oauth:2.0:oob",
      code         : String
    ): G[AccessToken] =
      f(OAuthCommand.Token(
          client_id, client_secret, redirect_uri, code
        ))

    def info(): G[TokenInfo] =
      f(OAuthCommand.Info())

    def revoke(
      client_id    : String,
      client_secret: String,
      token        : String
    ): G[Unit] =
      f(OAuthCommand.Revoke(client_id, client_secret, token))
  }
}
