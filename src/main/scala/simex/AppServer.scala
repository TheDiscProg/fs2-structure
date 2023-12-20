package simex

import cats.data.NonEmptyList
import cats.effect._
import cats.effect.kernel.Clock
import cats.{Monad, MonadError, Parallel}
import fs2.Stream
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.typelevel.log4cats.{Logger => Log4CatsLogger}
import simex.guardrail.healthcheck.HealthcheckResource
import simex.server.config.Configuration
import simex.server.domain.healthcheck.HealthChecker
import simex.server.entities.AppService
import simex.server.healthcheck.{HealthCheckService, HealthcheckAPIHandler, SelfHealthCheck}

class AppServer[
    F[_]: Monad: Clock: Async: Spawn: Sync: Parallel: Log4CatsLogger: MonadError[*[_], Throwable]
]() {

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

    } yield AppService()
}
