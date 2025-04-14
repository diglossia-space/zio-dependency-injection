val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-dependency-injection",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.17",
      "org.scalameta" %% "munit" % "1.0.0" % Test
  )
  )
