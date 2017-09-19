package annict4s

import httpz._
import scalaz.{Inject, Free}

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
    field               : List[String] = Nil,
    filter_ids          : List[String] = Nil,
    filter_season       : String = "",
    filter_title        : String = "",
    page                : Int = 1,
    per_page            : Int = 25,
    sort_id             : String = "desc",
    sort_season         : String = "desc",
    sort_watchers_count : String = "desc"
  )(implicit token: String): G[Works] =
    f(Command.Works(field, filter_ids, filter_season, filter_title,
      page, per_page, sort_id, sort_season, sort_watchers_count)(token))
}
