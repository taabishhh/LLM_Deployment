package services

import lambda_service.lambda_service.{LambdaRequest, LambdaResponse, LambdaServiceGrpc}
import io.grpc.ManagedChannelBuilder
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{ExecutionContext, Future}

class LambdaGrpcClient(host: String, port: Int)(implicit ec: ExecutionContext) {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  // Initialize gRPC channel and stub
  private val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
  private val stub = LambdaServiceGrpc.stub(channel)

  logger.info(s"LambdaGrpcClient initialized with host: $host, port: $port")

  /**
   * Invokes the Lambda gRPC service with the given query.
   *
   * @param query The input query string.
   * @return A Future containing the LambdaResponse.
   */
  def invokeLambda(query: String): Future[LambdaResponse] = {
    logger.debug(s"Preparing to invoke Lambda service with query: $query")

    val request = LambdaRequest(query = query)

    stub.invokeLambda(request).map { response =>
      logger.info(s"Received response from Lambda service: ${response.result}")
      response
    }.recover { case e: Exception =>
      logger.error(s"Error invoking Lambda service: ${e.getMessage}", e)
      throw e // Ensure the error is propagated if necessary
    }
  }
}
