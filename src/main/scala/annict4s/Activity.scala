package annict4s

case class Activity(
  id             : Option[Long],
  user           : Option[User],
  work           : Option[Work],
  action         : Option[Activity.Action],
  created_at     : Option[DateTime],
  episode        : Option[Episode],                       // for create_record
  record         : Option[Record],                        // for create_record
  review         : Option[Review],                        // for create_revie
  multiple_record: Option[List[Activity.MultipleRecord]], // for create_multiple_records
  status         : Option[Status]                         // for create_status
) extends JsonToString[Activity]

object Activity {

  case class MultipleRecord(
    episode: Episode,
    record: Record
  )

  object MultipleRecord {

    implicit val multiRecordCodecJson: CodecJson[MultipleRecord] =
      CodecJson.casecodec2(apply, unapply)("episode", "record")

  }

  trait Action
  case object CreateRecord extends Action {
    override def toString = "create_record"
  }
  case object CreateMultipleRecords extends Action {
    override def toString = "create_multiple_records"
  }
  case object CreateStatus extends Action {
    override def toString = "create_status"
  }
  case object CreateReview extends Action {
    override def toString = "create_review"
  }

  object Action {

    import argonaut.{EncodeJson, DecodeJson}
    import scalaz.syntax.std.option._

    implicit val actionCodecJson: CodecJson[Action] =
      CodecJson.derived(
        EncodeJson.jencode1(_.toString()),
        DecodeJson.optionDecoder({
          _.string.flatMap {
            case "create_record"           => CreateRecord.some
            case "create_multiple_records" => CreateMultipleRecords.some
            case "create_status"           => CreateStatus.some
            case "create_review"           => CreateReview.some
            case _                         => None
          }
        }, "Action")
      )
  }

  implicit val activityCodecJson: CodecJson[Activity] =
    CodecJson.casecodec10(apply, unapply)(
      "id", "user", "work", "action", "created_at",
      "episode", "record", "review", "multiple_record",
      "status"
    )
}


