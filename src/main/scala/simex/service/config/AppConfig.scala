package simex.service.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class AppConfig(
    http: HttpConfig,
    rabbitMQ: Option[RabbitConfig],
    tokenKey: Option[String]
)

object AppConfig {
  implicit val appConfigDecoder:Decoder[AppConfig] = deriveDecoder
}
