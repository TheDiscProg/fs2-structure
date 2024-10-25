lazy val scala3 = "3.5.1"

ThisBuild / organization := "simex"

ThisBuild / version := "2.0.0"

ThisBuild / versionScheme := Some("early-semver")

lazy val commonSettings = Seq(
  scalaVersion := scala3,
  libraryDependencies ++= Dependencies.all,
  resolvers += Resolver.githubPackages("TheDiscProg"),
  githubOwner := "TheDiscProg",
  githubRepository := "simex-webservice-fs2-template",
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
)

lazy val base = (project in file("base"))
  .settings(
    commonSettings,
    name := "simex-webservice-fs2-template-base",
    scalacOptions ++= Scalac.options,
    coverageExcludedPackages := Seq(
      "<empty>",
      ".*.entities.*"
    ).mkString(";"),
    publish / skip := true
  )

lazy val guardrail = (project in file("guardrail"))
  .settings(
    commonSettings,
    name := "simex-webservice-fs2-template-guardrail",
    Compile / guardrailTasks := List(
      ScalaServer(
        file("swagger.yaml"),
        pkg = "io.github.thediscprog.fs2template.guardrail",
        framework = "http4s",
        tracing = false,
        imports = List(
          "eu.timepit.refined.types.string.NonEmptyString"
        )
      )
    ),
    publish / skip := true,
    coverageExcludedPackages := Seq(
      "<empty>",
      ".*guardrail.*"
    ).mkString(";")
  )
  .dependsOn(base % "test->test; compile->compile")

lazy val root = (project in file("."))
  .enablePlugins(
    ScalafmtPlugin,
    JavaAppPackaging,
    UniversalPlugin,
    DockerPlugin
  )
  .settings(
    commonSettings,
    name := "simex-webservice-fs2-template", // change to your repo
    Compile / doc / sources := Seq.empty,
    scalacOptions ++= Scalac.options,
    coverageExcludedPackages := Seq(
      "<empty>"
    ).mkString(";"),
    coverageExcludedFiles := Seq(
      "<empty>",
      ".*MainApp.*",
      ".*AppServer.*"
    ).mkString(";"),
    coverageFailOnMinimum := true,
    coverageMinimumStmtTotal := 79,
    coverageMinimumBranchTotal := 95,
    Compile / mainClass := Some("simex.MainApp"),
    Docker / packageName := "simex-webservice-fs2-template", // Change to your repo
    Docker / dockerUsername := Some("ramindur"),
    Docker / defaultLinuxInstallLocation := "/opt/simex-template", // Change to your repo
    dockerBaseImage := "eclipse-temurin:21-jdk",
    dockerExposedPorts ++= Seq(8000), // Change to unique port defined in CONF
    dockerExposedVolumes := Seq("/opt/docker/.logs", "/opt/docker/.keys")
  )
  .aggregate(base, guardrail)
  .dependsOn(base % "test->test; compile->compile")
  .dependsOn(guardrail % "test->test; compile->compile")

// Put here as database repository tests may hang but remove for none db applications
parallelExecution := false

addCommandAlias("cleanTest", ";clean;scalafmt;test:scalafmt;test;")
addCommandAlias("cleanCoverage", ";clean;scalafmt;test:scalafmt;coverage;test;coverageReport;")