name := "CheckMeOutBot"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= {

  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.5.18",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test,

    "com.typesafe.play" %% "play-ws-standalone-json" % "1.1.8",

    "com.typesafe.akka" %% "akka-http" % "10.1.5",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,

    "com.typesafe.akka" %% "akka-slf4j" % "2.5.18",

    "com.typesafe.akka" %% "akka-stream" % "2.5.18",
    "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.18" % Test,

    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",


    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "de.heikoseeberger" %% "akka-http-play-json" % "1.17.0",

    "com.47deg" %% "github4s" % "0.19.0",
     "org.typelevel" %% "cats-core" % "0.9.0",

  "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test


  )
}