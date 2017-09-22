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
      f(Command.Works(fields, filter_ids, filter_season, filter_title,
        page, per_page, sort_id, sort_season, sort_watchers_count)(token))
    }

  def episodes(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_sort_number: String = "desc"
  ): Authorized[G[Episodes]] =
    Reader {token =>
      f(Command.Episodes(fields, filter_ids, filter_work_id, page,
        per_page, sort_id, sort_sort_number)(token))
    }

  def records(
    fields                    : List[String] = Nil,
    filter_ids                : List[Long] = Nil,
    filter_episode_id         : Long,
    filter_has_record_comment : Boolean = true,
    page                      : Int = 1,
    per_page                  : Int = 25,
    sort_id                   : String = "desc",
    sort_likes_count          : String = "desc"
  ): Authorized[G[Records]] =
    Reader { token => 
      f(Command.Records(fields, filter_ids, filter_episode_id,
        filter_has_record_comment, page, per_page, sort_id, sort_likes_count)(token))
    }

  def reviews(
    fields          : List[String] = Nil,
    filter_ids      : List[Long] = Nil,
    filter_work_id  : Long,
    page            : Int = 1,
    per_page        : Int = 25,
    sort_id         : String = "desc",
    sort_likes_count: String = "desc"
  ): Authorized[G[Reviews]] =
    Reader { token => 
      f(Command.Reviews(fields, filter_ids, filter_work_id,
        page, per_page, sort_id, sort_likes_count)(token))
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
      f(Command.Users(fields, filter_ids, filter_usernames,
        page, per_page, sort_id)(token))
    }

  def following(
    fields         : List[String] = Nil,
    filter_ids     : List[Long] = Nil,
    filter_username: String,
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): Authorized[G[Users]] =
    Reader { token =>
      f(Command.Following(fields, filter_ids, filter_username,
        page, per_page, sort_id)(token))
    }

  def followers(
    fields         : List[String] = Nil,
    filter_ids     : List[Long] = Nil,
    filter_username: String,
    page           : Int = 1,
    per_page       : Int = 25,
    sort_id        : String = "desc"
  ): Authorized[G[Users]] =
    Reader { token =>
      f(Command.Followers(fields, filter_ids, filter_username,
        page, per_page, sort_id)(token))
    }

  def activities(
    fields     : List[String] = Nil,
    filter_user: UserFilter,
    page       : Int = 1,
    per_page   : Int = 25,
    sort_id    : String = "desc"
  ): Authorized[G[Activities]] =
    Reader { token =>
      f(Command.Activities(fields, filter_user,
        page, per_page, sort_id)(token))
    }

  def me(): Authorized[G[User]] =
    Reader { token =>
      f(SelfCommand.Me(token))
    }

  // def meStatuses(
  //   work_id: Long,
  //   kind   : Status.Kind
  // ): Authorized[G[NoContent]] =
  //   Reader { token =>
  //     f(SelfCommand.Statuses(work_id, kind)(token))
  //   }
}
