package io.github.thediscprog.fs2template.server

import cats.data.NonEmptyList
import cats.effect.*
import cats.effect.kernel.Clock
import cats.{Monad, MonadError, Parallel}
import fs2.Stream
import io.github.thediscprog.fs2template.server.config.Configuration
import io.github.thediscprog.fs2template.server.domain.healthcheck.HealthChecker
import io.github.thediscprog.fs2template.server.entities.AppService
import io.github.thediscprog.fs2template.server.healthcheck.{
  HealthCheckService,
  HealthcheckAPIHandler,
  SelfHealthCheck
}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.typelevel.log4cats.Logger as Log4CatsLogger
import io.github.thediscprog.fs2template.guardrail.healthcheck.HealthcheckResource

class AppServer[F[_]: Monad: Clock: Async: Spawn: Sync: Parallel: Log4CatsLogger](implicit
    F: MonadError[F, Throwable]
) {

  def start(): Stream[F, Unit] =
    for {
      config <- Configuration.load

      // Health checkers
      checkers = NonEmptyList.of[HealthChecker[F]](SelfHealthCheck[F])
      healthCheckers = HealthCheckService(checkers)
      healthRoutes = new HealthcheckResource().routes(
        new HealthcheckAPIHandler[F](healthCheckers)
      )

      // Routes and HTTP App
      allRoutes = healthRoutes.orNotFound
      httpApp = Logger.httpApp(logHeaders = true, logBody = true)(allRoutes)

      // Build server
      _ <- BlazeServerBuilder[F]
        .bindHttp(config.http.port, config.http.hostAddress)
        .withHttpApp(httpApp)
        .serve

    } yield new AppService() {}
}
