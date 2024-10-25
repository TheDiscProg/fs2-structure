package io.github.thediscprog.fs2template.server.domain.healthcheck.entities

import cats.data.NonEmptyList

case class HealthCheckStatus(
    status: HealthStatus,
    details: NonEmptyList[HealthCheckerResponse]
)
