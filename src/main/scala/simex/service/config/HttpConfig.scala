package simex.service.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class HttpConfig(port: Int)

object HttpConfig {
  implicit val httpConfigDecoder:Decoder[HttpConfig] = deriveDecoder
}
