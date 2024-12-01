import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}
import routes.LambdaRoutes
import services.{LambdaGrpcClient, OllamaClient}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object AppMain {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private val config = ConfigFactory.load()
  private val endpoint: String = config.getString("lambda.endpoint")
  private val port: Int = config.getInt("lambda.port")

  def main(args: Array[String]): Unit = {
    logger.info("Initializing Akka HTTP Server...")

    implicit val system: ActorSystem = ActorSystem("AkkaHttpServer")
    implicit val materializer: Materializer = Materializer(system)
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    logger.info(s"System initialized: ActorSystem = ${system.name}")

    // Initialize gRPC client
    val grpcClient = new LambdaGrpcClient(endpoint, port)
    logger.info(s"gRPC Client initialized with endpoint: $endpoint and port: $port")

    // Initialize Ollama client
    val ollamaClient = new OllamaClient
    logger.info("Ollama Client initialized")

    // Set up routes
    val lambdaRoutes = new LambdaRoutes(grpcClient, ollamaClient)
    logger.info("Routes initialized")

    // Start HTTP server
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(lambdaRoutes.route)
    logger.info("Server binding initiated on http://localhost:8080/")

    println("Server is running at http://localhost:8080/")
    println("Press RETURN to stop...")

    // Await user input to stop the server
    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete {
        case scala.util.Success(_) =>
          logger.info("Server unbound successfully. Terminating the system...")
          system.terminate()
        case scala.util.Failure(ex) =>
          logger.error("Error while unbinding the server:", ex)
          system.terminate()
      }
  }
}
