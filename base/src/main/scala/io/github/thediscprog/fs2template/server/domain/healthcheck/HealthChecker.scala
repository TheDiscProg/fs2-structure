package io.github.thediscprog.fs2template.server.domain.healthcheck

import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.HealthCheckerResponse

trait HealthChecker[F[_]] {

  val name: String

  def checkHealth(): F[HealthCheckerResponse]

}
