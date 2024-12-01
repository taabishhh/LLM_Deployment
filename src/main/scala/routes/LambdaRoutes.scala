package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import services.LambdaGrpcClient
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

// Import LambdaResponse from the generated ScalaPB classes
import lambda_service.LambdaResponse

// Define a custom Spray JSON marshaller for LambdaResponse
object LambdaResponseJsonProtocol extends DefaultJsonProtocol {
  implicit object LambdaResponseFormat extends RootJsonFormat[LambdaResponse] {
    def write(response: LambdaResponse): JsValue = JsObject(
      "result" -> JsString(response.result)
    )

    def read(json: JsValue): LambdaResponse = {
      json.asJsObject.getFields("result") match {
        case Seq(JsString(result)) => LambdaResponse(result)
        case _ => throw new DeserializationException("LambdaResponse expected")
      }
    }
  }
}

class LambdaRoutes(grpcClient: LambdaGrpcClient)(implicit ec: ExecutionContext) {
  import LambdaResponseJsonProtocol._

  val route: Route =
    path("process-request") {
      post {
        entity(as[String]) { requestBody =>
          onComplete(grpcClient.invokeLambda(requestBody)) {
            case Success(response) =>
              complete((StatusCodes.OK, response)) // Spray JSON uses the custom marshaller
            case Failure(exception) =>
              complete((StatusCodes.InternalServerError, s"Error invoking Lambda: ${exception.getMessage}"))
          }
        }
      }
    }
}
