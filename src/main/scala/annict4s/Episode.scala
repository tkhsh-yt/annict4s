package annict4s

case class Episode(
  id                    : Option[Long],
  number                : Option[Int],
  number_text           : Option[String],
  sort_number           : Option[Int],
  title                 : Option[String],
  records_count         : Option[Int],
  record_comments_count : Option[Int],
  work                  : Option[Work],
  prev_episode          : Option[Episode],
  next_episode          : Option[Episode]
) extends JsonToString[Episode]

object Episode {

  import argonaut._
  import Argonaut._

  implicit def EpisodeEncodeJson: EncodeJson[Episode] =
    EncodeJson( e =>
            ("id"                    := e.id)
        ->: ("number"                := e.number)
        ->: ("number_text"           := e.number_text)
        ->: ("sort_number"           := e.sort_number)
        ->: ("title"                 := e.title)
        ->: ("records_count"         := e.records_count)
        ->: ("record_comments_count" := e.record_comments_count)
        ->: ("work"                  := e.work)
        ->: ("prev_episode"          := e.prev_episode.map(EpisodeEncodeJson.encode))
        ->: ("next_episode"          := e.next_episode.map(EpisodeEncodeJson.encode))
        ->: jEmptyObject
    )

  implicit def EpisodeDecodeJson: DecodeJson[Episode] =
    DecodeJson( e => for {
      id                    <- (e --\ "id").as[Option[Long]]
      number                <- (e --\ "number").as[Option[Int]]
      number_text           <- (e --\ "number_text").as[Option[String]]
      sort_number           <- (e --\ "sort_number").as[Option[Int]]
      title                 <- (e --\ "title").as[Option[String]]
      records_count         <- (e --\ "records_count").as[Option[Int]]
      record_comments_count <- (e --\ "record_comments_count").as[Option[Int]]
      work                  <- (e --\ "work").as[Option[Work]]
      prev_episode          <- (e --\ "prev_episode").as[Option[Episode]]
      next_episode          <- (e --\ "next_episode").as[Option[Episode]]
    } yield
        Episode(id, number, number_text, sort_number, title, records_count,
          record_comments_count, work, prev_episode, next_episode))

}
