package services

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OllamaClientSpec extends AnyFlatSpec with Matchers {

  "OllamaClient" should "initialize without errors" in {
    // Act
    val client = new OllamaClient()

    // Assert
    client should not be null
  }

  it should "generate a next query based on a valid response" in {
    // Arrange
    val client = new OllamaClient()
    val response = "Tell me about artificial intelligence."

    // Act
    val result = client.generateNextQuery(response)

    // Assert
    result should not be null
    result should not be empty
    println(s"Generated Query: $result") // Debug information
  }

  it should "return 'exit' when response is invalid" in {
    // Arrange
    val client = new OllamaClient()
    val invalidResponse = ""

    // Act
    val result = client.generateNextQuery(invalidResponse)

    // Assert
    result shouldBe "exit"
  }
}
