package io.github.thediscprog.fs2template.server.domain.healthcheck

import cats.data.NonEmptyList
import cats.effect.IO
import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.{
  HealthCheckStatus,
  HealthCheckerResponse,
  HealthStatus
}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.typelevel.log4cats.slf4j.Slf4jLogger
import io.github.thediscprog.fs2template.server.domain.healthcheck.entities.HealthStatus.{
  BROKEN,
  OK
}
import io.github.thediscprog.fs2template.server.healthcheck.HealthCheckService
import org.typelevel.log4cats.SelfAwareStructuredLogger

class HealthCheckServiceTest extends AnyFlatSpec with Matchers with MockitoSugar with ScalaFutures {

  import cats.effect.unsafe.implicits.global
  private implicit def unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  val healthy = new HealthChecker[IO]() {
    override val name: String = "healthy"

    override def checkHealth(): IO[HealthCheckerResponse] =
      IO {
        HealthCheckerResponse(name, OK)
      }
  }

  val faulty = new HealthChecker[IO] {
    override val name: String = "faulty"

    override def checkHealth(): IO[HealthCheckerResponse] =
      IO {
        HealthCheckerResponse(name, BROKEN)
      }
  }

  it should "return healthy status" in {
    val sut = new HealthCheckService[IO](NonEmptyList.of[HealthChecker[IO]](healthy))

    val result = sut.checkHealth.unsafeToFuture()

    whenReady(result) { s =>
      s.status shouldBe HealthStatus.OK
    }
  }

  it should "return un-healthy status" in {
    val sut = new HealthCheckService[IO](NonEmptyList.of[HealthChecker[IO]](healthy, faulty))

    val result = sut.checkHealth.unsafeToFuture()

    whenReady(result) { s =>
      s.status shouldBe HealthStatus.BROKEN
    }
  }

}
