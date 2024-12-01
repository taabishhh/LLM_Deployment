//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.1")
//addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "2.1.7")

//addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.6")

//libraryDependencies ++= Seq(
//  "com.thesamet.scalapb" %% "compilerplugin" % "0.11.13",
//  "com.thesamet.scalapb.grpcweb" %% "scalapb-grpcweb-code-gen" % "0.6.4"
//)

//het
addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.6")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.1")


libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "compilerplugin" % "0.11.13",
)
//
//  "com.thesamet.scalapb.grpcweb" %% "scalapb-grpcweb-code-gen" % "0.6.4"
//)
