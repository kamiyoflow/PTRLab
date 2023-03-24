name := "PTRLAB"

version := "0.1"

scalaVersion := "2.13.10"

val AkkaVersion = "2.7.0"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
  "io.netty" % "netty" % "3.10.6.Final"
)
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.10"
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion