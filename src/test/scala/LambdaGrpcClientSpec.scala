package services

import lambda_service.lambda_service.{LambdaRequest, LambdaResponse, LambdaServiceGrpc}
import io.grpc.{Server, ServerBuilder}
import io.grpc.stub.StreamObserver
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.{ExecutionContext, Future}

class LambdaGrpcClientSpec extends AnyFlatSpec with Matchers with ScalaFutures {

  // Test gRPC Server setup
  private val testPort = 50051
  private val testHost = "localhost"

  // Start the gRPC server
  private val server: Server = ServerBuilder
    .forPort(testPort)
    .build()
    .start()

  // Ensure the server shuts down after tests
  sys.addShutdownHook {
    server.shutdown()
  }

  implicit val ec: ExecutionContext = ExecutionContext.global

  "LambdaGrpcClient" should "initialize without errors" in {
    // Act
    val client = new LambdaGrpcClient(testHost, testPort)

    // Assert
    client should not be null
  }

  it should "invoke Lambda service and get a valid response" in {
    // Arrange
    val client = new LambdaGrpcClient(testHost, testPort)
    val query = "Test query"

    // Act
    val responseFuture: Future[LambdaResponse] = client.invokeLambda(query)

    // Assert
    whenReady(responseFuture) { response =>
      response.result shouldBe s"Processed: $query"
    }
  }

  it should "handle errors gracefully when the service is unavailable" in {
    // Arrange
    val invalidPort = 12345 // Assume no server is running on this port
    val client = new LambdaGrpcClient(testHost, invalidPort)
    val query = "Test query"

    // Act
    val responseFuture: Future[LambdaResponse] = client.invokeLambda(query)

    // Assert
    whenReady(responseFuture.failed) { exception =>
      exception shouldBe a[io.grpc.StatusRuntimeException]
    }
  }

  // Clean up server after tests
  server.shutdown()
}
