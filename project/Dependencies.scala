import sbt.*

object Dependencies {

  lazy val all = Seq(
    "Shareprice" %% "shareprice-config" % "0.9.4",
    "co.fs2" %% "fs2-core" % "3.7.0",
    "co.fs2" %% "fs2-io" % "3.7.0",
    "com.github.pureconfig" %% "pureconfig" % "0.17.4",

    "ch.qos.logback" % "logback-classic" % "1.4.11",
    "org.typelevel" %% "log4cats-core" % "2.6.0",
    "org.typelevel" %% "log4cats-slf4j" % "2.6.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test,
    "org.scalatest" %% "scalatest" % "3.2.15" % Test,
    "org.scalatestplus" %% "mockito-4-6" % "3.2.15.0" % Test
  )
}
