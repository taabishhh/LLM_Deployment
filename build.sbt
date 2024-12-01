// Project settings
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.13"

lazy val root = (project in file("."))
  .settings(
    name := "Exercises441"
  )

// Dependency versions
lazy val akkaVersion = "2.6.20"
lazy val akkaHttpVersion = "10.2.10"
lazy val scalapbVersion = "0.11.13"
lazy val grpcVersion = "1.56.0"

// Assembly merge strategy for creating fat JARs
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) =>
    xs match {
      case "MANIFEST.MF" :: Nil => MergeStrategy.discard
      case "services" :: _      => MergeStrategy.concat
      case _                    => MergeStrategy.discard
    }
  case "reference.conf"         => MergeStrategy.concat
  case x if x.endsWith(".proto") => MergeStrategy.rename
  case x if x.contains("hadoop") => MergeStrategy.first
  case _                         => MergeStrategy.first
}

// Protobuf settings for generating ScalaPB and gRPC code
Compile / PB.targets := Seq(
  scalapb.gen(grpc = true) -> (Compile / sourceManaged).value / "scalapb"
)

// Resolvers
resolvers += Resolver.sbtPluginRepo("releases")

// Library dependencies
libraryDependencies ++= Seq(
  // AWS SDKs
  "com.amazonaws" % "aws-java-sdk-bedrock" % "1.12.560",     // AWS SDK Bedrock
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.1",        // AWS Lambda Core
  "com.amazonaws" % "aws-lambda-java-events" % "3.11.0",     // AWS Lambda Events

  // Akka dependencies
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,   // Akka Actor
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,        // Akka Streams
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,      // Akka HTTP
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion, // Akka JSON support
  "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",       // Circe support for Akka HTTP

  // JSON libraries
  "org.json4s" %% "json4s-native" % "3.7.0-M5",             // JSON parsing
  "io.circe" %% "circe-core" % "0.14.5",                   // Circe core
  "io.circe" %% "circe-generic" % "0.14.5",                // Circe generic
  "io.circe" %% "circe-parser" % "0.14.5",                 // Circe parser

  // HTTP client
  "com.softwaremill.sttp.client3" %% "core" % "3.9.0",      // HTTP client

  // Protobuf and gRPC
  "io.grpc" % "grpc-netty" % grpcVersion,                  // gRPC transport
  "io.grpc" % "grpc-protobuf" % grpcVersion,               // gRPC protobuf
  "io.grpc" % "grpc-stub" % grpcVersion,                   // gRPC stub
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf", // ScalaPB runtime
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,         // ScalaPB gRPC runtime
  "io.github.scalapb-json" %% "scalapb-circe" % "0.16.0",  // ScalaPB Circe support

  // Logging
  "org.slf4j" % "slf4j-api" % "2.0.9",                     // SLF4J API
  "com.typesafe" % "config" % "1.4.2",                     // Configuration library

  // Miscellaneous
  "io.github.ollama4j" % "ollama4j" % "1.0.79",            // Ollama4j library

  // Testing
  "org.scalatest" %% "scalatest" % "3.2.17" % Test         // ScalaTest for unit testing
)
