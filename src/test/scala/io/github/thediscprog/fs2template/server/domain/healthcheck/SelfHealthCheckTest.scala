package io.github.thediscprog.fs2template.server.domain.healthcheck

import cats.Id
import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.HealthCheckerResponse
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.HealthStatus.OK
import io.github.thediscprog.fs2template.server.healthcheck.SelfHealthCheck

class SelfHealthCheckTest extends AnyFlatSpec with Matchers {

  val sut = new SelfHealthCheck[Id]

  it should "return OK health status" in {
    val status: Id[HealthCheckerResponse] = sut.checkHealth()

    status.status shouldBe OK
  }

}
