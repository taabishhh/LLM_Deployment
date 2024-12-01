
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.13"

lazy val root = (project in file("."))
  .settings(
    name := "Exercises441"
  )

//enablePlugins(ScalaPB)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) =>
    xs match {
      case "MANIFEST.MF" :: sNil => MergeStrategy.discard
      case  "services" :: _     => MergeStrategy.concat
      case _                    => MergeStrategy.discard
    }
  case "reference.conf" => MergeStrategy.concat
  case x if x.endsWith(".proto") => MergeStrategy.rename
  case x if x.contains("hadoop") => MergeStrategy.first
  case _ => MergeStrategy.first
}


//resolvers += Resolver.mavenCentral

//Compile / PB.targets := Seq(
//  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
//)
PB.targets in Compile := Seq(
  scalapb.gen(grpc = true) -> (sourceManaged in Compile).value
)


//PB.targets in Compile := Seq(
//  scalapb.gen(grpc = true) -> (Compile / sourceManaged).value
//)

//enablePlugins(ScalaPB)
//resolvers ++= Seq(
//  Resolver.mavenCentral,
//  Resolver.sbtPluginRepo("releases")
//)

//PB.targets in Compile := Seq(
//  scalapb.gen(grpc = true) -> (Compile / sourceManaged).value
//)
lazy val akkaVersion = "2.6.20"
lazy val akkaHttpVersion = "10.2.10"
lazy val scalapbVersion = "0.11.13"
lazy val grpcVersion = "1.42.2"

// Protobuf settings
Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)


resolvers += Resolver.sbtPluginRepo("releases")


libraryDependencies ++= Seq(
//  "com.amazonaws" % "aws-java-sdk-bom" % "1.12.500",
//  "com.amazonaws" % "aws-java-sdk-bedrock" % "1.12.500",
//  "com.amazonaws" % "aws-lambda-java-core" % "1.2.1",
  "com.amazonaws" % "aws-java-sdk-bedrock" % "1.12.560", // AWS SDK Bedrock
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.1",    // AWS Lambda Core
  "com.amazonaws" % "aws-lambda-java-events" % "3.11.0", // AWS Lambda Events
  "org.json4s" %% "json4s-native" % "3.7.0-M5",          // JSON parsing
  "com.typesafe" % "config" % "1.4.2",

  "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.10",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "0.11.10",
//  "com.typesafe.akka" %% "akka-http" % "10.2.10",
//  "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",
//  "com.softwaremill.sttp.client3" %% "core" % "3.9.0",

  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.10",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20",      // Akka Actor
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",          // Akka Streams
  "com.typesafe.akka" %% "akka-http" % "10.2.10",           // Akka HTTP
  "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",      // JSON (Circe) support for Akka HTTP
  "io.circe" %% "circe-core" % "0.14.5",                   // Circe core
  "io.circe" %% "circe-generic" % "0.14.5",                // Circe generic
  "io.circe" %% "circe-parser" % "0.14.5",                 // Circe parser
  "com.softwaremill.sttp.client3" %% "core" % "3.9.0",     // HTTP client
  "org.scalatest" %% "scalatest" % "3.2.17" % Test,      // Testing

  "io.grpc" % "grpc-netty" % "1.56.0",
  "io.grpc" % "grpc-protobuf" % "1.56.0",
  "io.grpc" % "grpc-stub" % "1.56.0",
  "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.10",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "0.11.10",
  "io.github.scalapb-json" %% "scalapb-circe" % "0.16.0"

//  "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.2"
//)
//  "com.lightbend.akka.grpc" %% "akka-grpc-runtime" % "2.5.0", // Akka gRPC runtime
//  "com.typesafe.akka" %% "akka-http" % "10.2.10",            // Akka HTTP
//  "com.softwaremill.sttp.client3" %% "core" % "3.9.0",        // HTTP client
//  "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",        // JSON handling




  ////libraryDependencies ++= Seq(
//  // Akka
//  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
//  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
//  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  //  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
//  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
//  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
//  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
//  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
//  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
//  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
//  "com.typesafe.akka" %% "akka-pki" % akkaVersion,
//  "com.typesafe.akka" %% "akka-protobuf-v3" % akkaVersion,
////
//  // Configuration
//  "com.typesafe" % "config" % "1.4.2",
//
//  // HTTP Client
//  "org.scalaj" %% "scalaj-http" % "2.4.2",
//
//  // JSON
//  "io.spray" %%  "spray-json" % "1.3.6",
//
//  // Logging
//  "ch.qos.logback" % "logback-classic" % "1.4.7",
//  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
//
//  // Protobuf and gRPC
//  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
//  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
//
//  "io.grpc" % "grpc-netty" % grpcVersion,
//  "io.grpc" % "grpc-protobuf" % grpcVersion,
//  "io.grpc" % "grpc-stub" % grpcVersion,
//
//  // Testing
//  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
//  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
//  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
//)
//
//// Additional compiler options
//scalacOptions ++= Seq(
//  "-deprecation",
//  "-feature",
//  "-unchecked",
//  "-Xlint"
//)
//
//// Java options
//javacOptions ++= Seq(
//  "-source", "11",
//  "-target", "11"
)


