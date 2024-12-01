package services

import lambda_service.{LambdaRequest, LambdaResponse, LambdaServiceGrpc}
import io.grpc.ManagedChannelBuilder
import scala.concurrent.{ExecutionContext, Future}

class LambdaGrpcClient(host: String, port: Int)(implicit ec: ExecutionContext) {
  private val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
  private val stub = LambdaServiceGrpc.stub(channel)

  def invokeLambda(query: String): Future[LambdaResponse] = {
    val request = LambdaRequest(query = query)
    stub.invokeLambda(request)
  }
}
