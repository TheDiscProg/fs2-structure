package simex

import cats.effect.{ExitCode, IO, IOApp}
import simex.service.AppServer
import cats.effect._
import cats.syntax.all._
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import fs2.Stream

object MainApp extends IOApp{

  override def run(args: List[String]): IO[ExitCode] = {
    val log: Stream[IO, Logger[IO]] = Stream.eval(Slf4jLogger.create[IO])

    val result: Stream[IO, Unit] = for {
      implicit0(logger: Logger[IO]) <- log
      result <- (new AppServer[IO]).start()
    }yield result

      result.compile.drain.as(ExitCode.Success)
  }
}
