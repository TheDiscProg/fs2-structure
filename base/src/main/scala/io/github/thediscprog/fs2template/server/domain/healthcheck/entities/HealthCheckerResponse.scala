package io.github.thediscprog.fs2template.server.domain.healthcheck.entities

case class HealthCheckerResponse(
    name: String,
    status: HealthStatus
)
