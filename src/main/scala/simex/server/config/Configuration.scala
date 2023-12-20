package simex.server.config

import cats.effect.kernel.Sync
import fs2.Stream
import pureconfig._
import pureconfig.generic.auto._
import shareprice.config.ServerConfiguration

object Configuration {

  def load[F[_]: Sync]: Stream[F, ServerConfiguration] = Stream.eval(loadSync())

  private def loadSync[F[_]: Sync](): F[ServerConfiguration] =
    Sync[F].delay(ConfigSource.default.at("server").loadOrThrow[ServerConfiguration])

}
