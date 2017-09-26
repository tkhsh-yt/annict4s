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
  ): Authorized[G[Works]] =
    Reader { token =>
      f(Command.Works(
          fields, filter_ids, filter_season, filter_title,
          page, per_page, sort_id, sort_season, sort_watchers_count
        )(token))
    }

  def episodes(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long = 0,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_sort_number: String = "desc"
  ): Authorized[G[Episodes]] =
    Reader {token =>
      f(Command.Episodes(
          fields, filter_ids, filter_work_id, page,
          per_page, sort_id, sort_sort_number
        )(token))
    }

  def records(
    fields                    : List[String] = Nil,
    filter_ids                : List[Long] = Nil,
    filter_episode_id         : Long = 0,
    filter_has_record_comment : Boolean = true,
    page                      : Int = 1,
    per_page                  : Int = 25,
    sort_id                   : String = "desc",
    sort_likes_count          : String = "desc"
  ): Authorized[G[Records]] =
    Reader { token => 
      f(Command.Records(
          fields, filter_ids, filter_episode_id,
          filter_has_record_comment, page, per_page,
          sort_id, sort_likes_count
        )(token))
    }

  def reviews(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long = 0,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_likes_count: String = "desc"
  ): Authorized[G[Reviews]] =
    Reader { token =>
      f(Command.Reviews(
          fields, filter_ids, filter_work_id,
          page, per_page, sort_id, sort_likes_count
        )(token))
    }

  def users(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_usernames: List[String] = Nil,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc"
  ): Authorized[G[Users]] =
    Reader { token =>
      f(Command.Users(
          fields, filter_ids, filter_usernames,
          page, per_page, sort_id
        )(token))
    }

  def following(
    fields         : List[String] = Nil,
    filter_id      : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): Authorized[G[Users]] =
    Reader { token =>
      f(Command.Following(
          fields, filter_id, filter_username,
          page, per_page, sort_id
        )(token))
    }

  def followers(
    fields         : List[String] = Nil,
    filter_id      : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): Authorized[G[Users]] =
    Reader { token =>
      f(Command.Followers(
          fields, filter_id, filter_username,
          page, per_page, sort_id
        )(token))
    }

  def activities(
    fields         : List[String] = Nil,
    filter_user_id : Long = 0,
    filter_username: String = "",
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): Authorized[G[Activities]] =
    Reader { token =>
      f(Command.Activities(
          fields, filter_user_id, filter_username, page, per_page, sort_id
        )(token))
    }

  object Me {
    def profile(): Authorized[G[User]] =
      Reader { token =>
        f(SelfCommand.Me(token))
      }

    def status(
      work_id: Long,
      kind   : Status.Kind
    ): Authorized[G[String]] =
      Reader { token =>
        f(SelfCommand.Statuses(work_id, kind)(token))
      }

    def postRecord(
      episode_id    : Long,
      comment       : String = "",
      rating_state  : Option[Rating] = None,
      share_twitter : Boolean = false,
      share_facebook: Boolean = false
    ): Authorized[G[Record]] =
      Reader { token =>
        f(SelfCommand.Record(
            episode_id, comment,
            rating_state, share_twitter
          )(token))
      }

    def updateRecord(

      id            : Long,
      comment       : String = "",
      rating_state  : Rating = Rating.NoSelect,
      share_twitter : Boolean,
      share_facebook: Boolean
    ): Authorized[G[Record]] =
      Reader { token =>
        f(SelfCommand.UpdateRecord(
            id, comment, rating_state, share_twitter, share_facebook
          )(token))
      }

    def deleteRecord(
      id: Long
    ): Authorized[G[String]] =
      Reader { token =>
        f(SelfCommand.DeleteRecord(id)(token))
      }

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
    ): Authorized[G[Review]] =
      Reader { token =>
        f(SelfCommand.Review(
            work_id, body, rating_animation_state, rating_music_state,
            rating_story_state, rating_character_state,
            rating_overall_state, share_twitter, share_facebook
          )(token))
      }

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
    ): Authorized[G[Review]] =
      Reader { token =>
        f(SelfCommand.UpdateReview(
            id, title, body, rating_animation_state, rating_music_state,
            rating_story_state, rating_character_state,
            rating_overall_state, share_twitter, share_facebook
          )(token))
      }

    def deleteReview(
      id: Long
    ): Authorized[G[String]] =
      Reader { token =>
        f(SelfCommand.DeleteReview(id)(token))
      }

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
    ): Authorized[G[Works]] =
      Reader { token =>
        f(SelfCommand.Works(
            fields, filter_ids, filter_season, filter_title,
            filter_status, page, per_page, sort_id,
            sort_season, sort_watchers_count
          )(token))
      }

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
    ): Authorized[G[Programs]] =
      Reader { token =>
        f(SelfCommand.Programs(
            fields, filter_ids, filter_channel_ids, filter_work_ids,
            filter_started_at_gt, filter_started_at_lt,
            filter_unwatched, filter_rebroadcast, page,
            per_page, sort_id, sort_started_at
          )(token))
      }

    def followingActivites(
      fields        : List[String] = Nil,
      filter_actions: List[Activity.Action] = Nil,
      filter_muted  : Boolean = true,
      page          : Int = 1,
      per_page      : Int = 25,
      sort_id       : String = "desc"
    ): Authorized[G[Activities]] =
      Reader { token =>
        f(SelfCommand.FollowingActivities(
            fields, filter_actions, filter_muted,
            page, per_page, sort_id
          )(token))
      }
  }

  object OAuth {

    def token(
      client_id    : String,
      client_secret: String,
      redirect_url : String,
      code         : String
    ): G[AccessToken] =
      f(OAuthCommand.Token(
          client_id, client_secret, redirect_url, code
        ))

    def info(): Authorized[G[TokenInfo]] =
      Reader { token =>  f(OAuthCommand.Info()(token)) }

    def revoke(
      client_id    : String,
      client_secret: String,
      token        : String
    ): Authorized[G[Unit]] =
      Reader { accessToken =>
        f(OAuthCommand.Revoke(
            client_id, client_secret, token
          )(accessToken))
      }
  }
}
