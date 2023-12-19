package simex.service

import cats.effect._
import cats.effect.kernel.Clock
import fs2.Stream
import org.typelevel.log4cats.Logger
import simex.service.config.Configuration

class AppServer[F[_]: Clock: Async: Spawn: Sync: Logger]() {

  def start(): Stream[F, Unit] =
    for {
      config <- Configuration.load
    }yield ()
}
