package simex.server.healthcheck

import cats.Functor
import simex.guardrail.definitions.HealthResponse
import simex.guardrail.healthcheck.HealthcheckHandler
import simex.guardrail.healthcheck.HealthcheckResource.HealthcheckResponse
import simex.server.domain.healthcheck.HealthCheckAlgebra
import simex.server.domain.healthcheck.entities.HealthCheckStatus
import simex.server.domain.healthcheck.entities.HealthStatus.OK
import cats.implicits._

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
