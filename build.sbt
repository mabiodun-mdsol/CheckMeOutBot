name := "CheckMeOutBot"

version := "0.1"

scalaVersion := "2.12.7"

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)


val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)



libraryDependencies ++= {

  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.5.18",
    "com.typesafe.akka" %% "akka-http" % "10.1.5",
    "com.typesafe.akka" %% "akka-slf4j" % "2.5.18",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",

    "com.typesafe.play" %% "play-ws-standalone-json" % "1.1.8",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "de.heikoseeberger" %% "akka-http-play-json" % "1.17.0",
    "com.47deg" %% "github4s" % "0.19.1-bolaji-SNAPSHOT-3",
    "org.typelevel" %% "cats-core" % "0.9.0",
    "com.github.slack-scala-client" %% "slack-scala-client" % "0.2.5",

//    "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,
    "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test,

    "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    "org.mockito" %% "mockito-scala" % "1.0.5" % Test

  //    "org.scalamock" %% "scalamock" % "4.1.0" % Test
  )
}