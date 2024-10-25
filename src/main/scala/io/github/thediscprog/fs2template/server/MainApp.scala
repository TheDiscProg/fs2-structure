package io.github.thediscprog.fs2template.server

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object MainApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    given loggerStream: Stream[IO, Logger[IO]] = Stream.eval(Slf4jLogger.create[IO])

    val streams = for {
      given Logger[IO] <- loggerStream
      appService <- (new AppServer[IO]).start()
    } yield appService

    streams.compile.drain.as(ExitCode.Success)
  }
}
