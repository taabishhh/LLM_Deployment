import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}
import routes.LambdaRoutes
import services.{LambdaGrpcClient, OllamaClient}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.io.StdIn
import scala.util.{Failure, Success}

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
    val grpcClient = new LambdaGrpcClient(endpoint)
    logger.info(s"gRPC Client initialized with endpoint: $endpoint and port: $port")

    // Initialize Ollama client
    val ollamaClient = new OllamaClient
    logger.info("Ollama Client initialized")

    // Set up routes
    val lambdaRoutes = new LambdaRoutes(grpcClient, ollamaClient)
    logger.info("Routes initialized")

    // Start HTTP server
    val bindingFuture = Http().newServerAt("localhost", port).bind(lambdaRoutes.route)
    logger.info("Server binding initiated on http://localhost:8080/")

    // Await user input to stop the server
    StdIn.readLine()

    bindingFuture.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        logger.info(s"Server is running at http://${address.getHostString}:${address.getPort}/")
        logger.info("Press CTRL+C to terminate...")
      case Failure(ex) =>
        logger.error("Failed to bind to 0.0.0.0:8080", ex)
        system.terminate()
    }

    // Block the main thread and keep the application running
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
