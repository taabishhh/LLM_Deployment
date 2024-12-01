package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import services.{LambdaGrpcClient, OllamaClient}
import spray.json.DefaultJsonProtocol._
import spray.json._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.slf4j.{Logger, LoggerFactory}

// Request model
case class UserRequest(prompt: String)

object UserRequest extends DefaultJsonProtocol {
  implicit val format = jsonFormat1(UserRequest.apply)
}

// Response model
case class AgentResponse(prompt: String, response: String)

object AgentResponse extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(AgentResponse.apply)
}

class LambdaRoutes(grpcClient: LambdaGrpcClient, ollamaClient: OllamaClient)(implicit ec: ExecutionContext) {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  val route: Route =
    path("process-request") {
      post {
        entity(as[UserRequest]) { userRequest =>
          logger.info(s"Received request with prompt: ${userRequest.prompt}")
          handleConversation(userRequest.prompt)
        }
      }
    }

  private def handleConversation(initialPrompt: String): Route = {
    logger.debug(s"Starting conversation loop with initial prompt: $initialPrompt")

    // Generate conversational loop
    def conversationLoop(prompt: String, iteration: Int): Route = {
      logger.debug(s"Iteration $iteration: Processing prompt: $prompt")

      onComplete(grpcClient.invokeLambda(prompt)) {
        case Success(lambdaResponse) =>
          val lambdaOutput = lambdaResponse.result
          logger.info(s"Iteration $iteration: Received Lambda output: $lambdaOutput")

          if (iteration > 10 || lambdaOutput.contains("exit")) {
            logger.info(s"Iteration $iteration: Terminating conversation.")
            complete(AgentResponse(prompt, "Conversation terminated.").toJson.toString)
          } else {
            val nextPrompt = ollamaClient.generateNextQuery(lambdaOutput)
            logger.debug(s"Iteration $iteration: Generated next query: $nextPrompt")
            conversationLoop(nextPrompt, iteration + 1)
          }
        case Failure(exception) =>
          logger.error(s"Iteration $iteration: Error processing request: ${exception.getMessage}", exception)
          complete((500, s"Error: ${exception.getMessage}"))
      }
    }

    conversationLoop(initialPrompt, iteration = 1)
  }
}
