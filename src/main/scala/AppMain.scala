import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.stream.SystemMaterializer
import routes.LambdaRoutes
import services.LambdaGrpcClient

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object AppMain {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "AkkaHttpServer")
    implicit val materializer = SystemMaterializer(system).materializer
    implicit val ec: ExecutionContextExecutor = system.executionContext

    val grpcClient = new LambdaGrpcClient("lambda-grpc-endpoint", 8080)
    val routes = new LambdaRoutes(grpcClient)

    val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(routes.route)

    println("Akka HTTP server running on port 8080. Press RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
