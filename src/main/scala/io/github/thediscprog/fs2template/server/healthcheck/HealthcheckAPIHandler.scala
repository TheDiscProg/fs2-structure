package io.github.thediscprog.fs2template.server.healthcheck

import cats.Functor
import io.github.thediscprog.fs2template.guardrail.definitions.HealthResponse
import io.github.thediscprog.fs2template.guardrail.healthcheck.HealthcheckHandler
import io.github.thediscprog.fs2template.guardrail.healthcheck.HealthcheckResource.HealthcheckResponse
import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.HealthStatus.OK
import cats.implicits._
import io.github.thediscprog.fs2template.server.domain.healthcheck.HealthCheckAlgebra
import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.HealthCheckStatus

class HealthcheckAPIHandler[F[_]: Functor](checker: HealthCheckAlgebra[F])
    extends HealthcheckHandler[F] {
  override def healthcheck(
      respond: HealthcheckResponse.type
  )(): F[HealthcheckResponse] =
    for {
      health <- checker.checkHealth
      response = health match {
        case HealthCheckStatus(OK, _) =>
          HealthcheckResponse.Ok(HealthResponse(HealthResponse.ServiceStatus.Ok))
        case _ => HealthcheckResponse.Ok(HealthResponse(HealthResponse.ServiceStatus.Broken))
      }
    } yield response
}
