package io.github.thediscprog.fs2template.server.domain.healthcheck

import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.HealthCheckStatus

trait HealthCheckAlgebra[F[_]] {

  def checkHealth: F[HealthCheckStatus]
}
