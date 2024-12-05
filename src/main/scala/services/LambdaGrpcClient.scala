package services

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.Materializer
import spray.json._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{ExecutionContext, Future}

// Request payload structure for the Lambda function
case class LambdaRequest(prompt: String)

// Structure of response details from the Lambda function
case class ResponseDetails(generation: String)

// Full response structure from the Lambda function
case class LambdaResponse(prompt: String, response: ResponseDetails)

// JSON format definitions for serialization/deserialization
object LambdaJsonProtocol extends DefaultJsonProtocol {
  implicit val lambdaRequestFormat: RootJsonFormat[LambdaRequest] = jsonFormat1(LambdaRequest)
  implicit val responseDetailsFormat: RootJsonFormat[ResponseDetails] = jsonFormat1(ResponseDetails)
  implicit val lambdaResponseFormat: RootJsonFormat[LambdaResponse] = jsonFormat2(LambdaResponse)
}

// Client for invoking the Lambda function via API Gateway
class LambdaGrpcClient(apiGatewayUrl: String)
                      (implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext) {

  import LambdaJsonProtocol._

  private val logger: Logger = LoggerFactory.getLogger(getClass)
  private val http = Http(system)

  /**
   * Invokes the Lambda function and processes the response.
   *
   * @param prompt The input prompt for the Lambda function.
   * @return Future containing the LambdaResponse or an error.
   */
  def invokeLambda(prompt: String): Future[LambdaResponse] = {
    logger.info(s"Invoking Lambda function at $apiGatewayUrl")

    // Create the request payload as JSON
    val payload = LambdaRequest(prompt).toJson.toString()

    // Build the HTTP POST request
    val httpRequest = HttpRequest(
      method = HttpMethods.POST,
      uri = apiGatewayUrl,
      entity = HttpEntity(ContentTypes.`application/json`, payload)
    )

    // Send the request and handle the response
    http.singleRequest(httpRequest).flatMap { response =>
      response.status match {
        case StatusCodes.OK =>
          logger.info("Successful response from Lambda")
          Unmarshal(response.entity).to[LambdaResponse]
        case _ =>
          val errorMsg = s"Failed to invoke Lambda. Status: ${response.status}"
          logger.error(errorMsg)
          Future.failed(new RuntimeException(errorMsg))
      }
    }
  }
}
