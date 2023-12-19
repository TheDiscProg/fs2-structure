package simex.service.config

import cats.effect.kernel.Sync
import fs2.Stream
import pureconfig._
import pureconfig.generic.auto._



object Configuration {

  def load[F[_]: Sync]: Stream[F, AppConfig] = Stream.eval(loadSync())

  private def loadSync[F[_]: Sync](): F[AppConfig] =
    Sync[F].delay(ConfigSource.default.at("server").loadOrThrow[AppConfig])

}

