
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
├── Docker
├── project/
│   ├── plugins.sbt               # SBT plugins
│   └── build.properties          # SBT version
└── README.md                     # Project documentation
```

## Setup and Installation
### Prerequisites
- Scala 2.12.13
- SBT 1.8.0 or higher
- Java 11 or higher

## Steps to Run
1. Clone the Repository:
```
git clone <repository-url>
cd Exercises441
```
2. Install Dependencies: Ensure all dependencies are downloaded:
```
sbt update
```
3. Compile the Project:
```
sbt compile
```
4. Run the Application:
```
sbt run
```
5. Test the Application: Run the test suite:
```
sbt test
```

---

## Running via Docker

You can run this project using Docker. Follow the steps below:

### Build the Docker Image
```
docker build -t my-scala-server .
```

### Run the Docker Container
```
docker run -d -p 8080:8080 my-scala-server
```

### Verify the Container is Running
List running containers to get the container ID:
```
docker ps
```

### Access the Container Shell
Use the container ID from the previous step to access the shell:
```
docker exec -it <container-id> sh
```

### Test the Application
Send a request to the application using `curl`:
```
curl -X POST http://localhost:8080/process-request \
     -H "Content-Type: application/json" \
     -d '{"prompt": "Hello, can you help me?"}'
```

---

## API Endpoints
1. POST /process-request
- **Description**: Processes a user request and returns a response.
- **Request Body**:
```
{
  "prompt": "Your query here"
}
```
- **Response Body**:
```
{
  "prompt": "Your query here",
  "response": "Processed response from the system"
}
```

Example curl Command:
```
curl -X POST http://localhost:8080/process-request \
     -H "Content-Type: application/json" \
     -d '{"prompt": "Hello, can you help me?"}'
```

---
