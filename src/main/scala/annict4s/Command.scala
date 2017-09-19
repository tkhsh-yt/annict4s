package annict4s

import argonaut.DecodeJson
import httpz._
import scalaz.{Free, Inject, NonEmptyList}
import scalaz.syntax.monoid._

/**
  * @see https://github.com/xuwei-k/ghscala/blob/master/src/main/scala/Command.scala
  */
sealed abstract class Command[A](val f: String => Request)(implicit val decoder: DecodeJson[A]){
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

  private[annict4s] def get(url: String, opt: Config = httpz.emptyConfig): String => Request = {
    baseURL => opt(Request(url = baseURL + url))
  }

  private[annict4s] def post(url: String, opt: Config = httpz.emptyConfig): String => Request = {
    baseURL => opt(Request(url = baseURL + url, method = "POST"))
  }

  final case class Works(
    field               : List[String] = Nil,
    filter_ids          : List[String] = Nil,
    filter_season       : String = "",
    filter_title        : String = "",
    page                : Int = 1,
    per_page            : Int = 25,
    sort_id             : String = "desc",
    sort_season         : String = "desc",
    sort_watchers_count : String = "desc"
  )(implicit token: String) extends Command[annict4s.Works](
    get("/v1/works", Request.params(
      ("field"              , field.mkString(",")),
      ("filter_ids"         , filter_ids.mkString(",")),
      ("filter_season"      , filter_season),
      ("filter_title"       , filter_title),
      ("page"               , page.toString),
      ("per_page"           , per_page.toString),
      ("sort_id"            , sort_id.toString),
      ("sort_season"        , sort_season),
      ("sort_watchers_count", sort_watchers_count)
    ) |+| Request.bearer(token))
  )
}
