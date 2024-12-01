# Exercises441-Homework3

It is a Scala-based project built to demonstrate the use of gRPC, Akka HTTP, and JSON handling libraries for implementing a conversational system. The project integrates AWS SDKs, ScalaPB, and external APIs to process and handle requests efficiently.

## Features

- **gRPC Integration**: Implements a gRPC client and server using ScalaPB and Protobuf.
- **Akka HTTP**: Provides REST API endpoints for interacting with the system.
- **JSON Handling**: Supports JSON parsing and serialization using Circe and Spray JSON.
- **AWS SDKs**: Integrates with AWS Lambda and other AWS services.
- **Logging**: Uses SLF4J for logging, ensuring structured and detailed logs.
- **Testing**: Includes unit tests using ScalaTest.

---

## Project Structure

```plaintext
Exercises441/
├── src/
│   ├── main/
│   │   ├── protobuf/             # Protobuf definitions for gRPC
│   │   │   └── lambda_service.proto
│   │   ├── resources/
│   │   │   └── application.conf  # Configuration file
│   │   ├── scala/
│   │       ├── routes/           # Akka HTTP routes
│   │       │   └── LambdaRoutes.scala
│   │       ├── services/         # gRPC clients and additional services
│   │       │   ├── LambdaGrpcClient.scala
│   │       │   └── OllamaClient.scala
│   │       ├── AppMain.scala     # Main application entry point
│   ├── test/
│       ├── scala/
│       │   ├── LambdaGrpcClientSpec.scala # Tests for LambdaGrpcClient
│       │   ├── OllamaClientSpec.scala     # Tests for OllamaClient
├── build.sbt                     # SBT build definition
├── project/
│   ├── plugins.sbt               # SBT plugins
│   └── build.properties          # SBT version
└── README.md                     # Project documentation


