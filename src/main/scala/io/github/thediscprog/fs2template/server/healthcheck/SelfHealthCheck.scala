package io.github.thediscprog.fs2template.server.healthcheck

import cats.Applicative
import io.github.thediscprog.fs2template.server.domain.healthcheck.HealthChecker
import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.{
  HealthCheckerResponse,
  HealthStatus
}

class SelfHealthCheck[F[_]: Applicative] extends HealthChecker[F] {

  override val name: String = "ServerSelfHealthCheck"

  override def checkHealth(): F[HealthCheckerResponse] =
    Applicative[F].pure(
      HealthCheckerResponse(name, HealthStatus.OK)
    )
}

object SelfHealthCheck {

  def apply[F[_]: Applicative] = new SelfHealthCheck()
}
