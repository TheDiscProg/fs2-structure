package io.github.thediscprog.fs2template.server.config

import cats.effect.kernel.Sync
import fs2.Stream
import io.circe.config.parser
import io.github.thediscprog.shareprice.config.ServerConfiguration

object Configuration {

  def load[F[_]: Sync]: Stream[F, ServerConfiguration] = Stream.eval(loadSync())

  private def loadSync[F[_]: Sync](): F[ServerConfiguration] =
    parser.decodePathF[F, ServerConfiguration](path = "server")

}
