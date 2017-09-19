package annict4s

trait Rating
case object Bad      extends Rating {
  override def toString = "bad"
}
case object Average extends Rating {
  override def toString = "average"
}
case object Good     extends Rating {
  override def toString = "rating"
}
case object Great    extends Rating {
  override def toString = "great"
}

object Rating {

  import argonaut.{EncodeJson, DecodeJson}
  import scalaz.syntax.std.option._

  implicit val ratingCodecJson: CodecJson[Rating] =
    CodecJson.derived(
      EncodeJson.jencode1(_.toString()),
      DecodeJson.optionDecoder({
        _.string.flatMap {
          case "bad"     => Bad.some
          case "average" => Average.some
          case "good"    => Good.some
          case "great"   => Great.some
          case _         => None
        }
      }, "Rating")
    )
}
