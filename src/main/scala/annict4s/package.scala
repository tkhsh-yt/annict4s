import argonaut.{EncodeJson, DecodeJson}
import java.text.SimpleDateFormat

package object annict4s {

  type DateTime = org.joda.time.DateTime
  private[annict4s] type JsonToString[A <: httpz.JsonToString[A]] =
    httpz.JsonToString[A]

  type CodecJson[A] = argonaut.CodecJson[A]
  val CodecJson = argonaut.CodecJson

  implicit val datetimeCodecJson: CodecJson[DateTime] =
    CodecJson.derived(
      EncodeJson.jencode1(_.toString()),
      DecodeJson.optionDecoder({
        _.string.map { str =>
          new DateTime((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).parse(str))
        }
      }, "DateTime")
    )
}
