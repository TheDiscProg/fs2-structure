package simex.service.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class RabbitConfig(host: String, port: Int, user: String, password: String)

object RabbitConfig {
  implicit val rmqConfigDecoder:Decoder[RabbitConfig] = deriveDecoder
}