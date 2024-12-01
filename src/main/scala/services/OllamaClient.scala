package services

import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.models.OllamaResult
import io.github.ollama4j.utils.Options
import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}

class OllamaClient {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  // Load configuration
  private val config = ConfigFactory.load().getConfig("ollama")
  private val host = config.getString("host")
  private val requestTimeout = config.getInt("request-timeout-seconds")
  private val model = config.getString("model")

  // Initialize Ollama API
  private val ollamaAPI = new OllamaAPI(host)
  ollamaAPI.setRequestTimeoutSeconds(requestTimeout)

  logger.info(s"OllamaClient initialized with host: $host, model: $model, request timeout: $requestTimeout seconds")

  /**
   * Generates the next query based on the given response.
   *
   * @param response The response text to base the next query on.
   * @return The next query or "exit" if an error occurs.
   */
  def generateNextQuery(response: String): String = {
    val prompt = s"Generate the next query based on this response: $response"
    logger.debug(s"Generating next query with prompt: $prompt")

    try {
      val result: OllamaResult = ollamaAPI.generate(model, prompt, false, new Options(new java.util.HashMap[String, Object]()))
      val generatedResponse = result.getResponse
      logger.info(s"Successfully generated next query: $generatedResponse")
      generatedResponse
    } catch {
      case e: Exception =>
        logger.error(s"Failed to generate next query: ${e.getMessage}", e)
        "exit"
    }
  }
}
