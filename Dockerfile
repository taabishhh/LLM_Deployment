# Stage 1: Build the application
FROM openjdk:11-jdk-slim as builder

# Install sbt and required dependencies
RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    apt-transport-https && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99e82a75642ac823" | apt-key add - && \
    apt-get update && apt-get install -y sbt && \
    curl -fsSL https://ollama.com/install.sh | sh && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . /app

  # Build the application using sbt
RUN sbt assembly

 # Stage 2: Create a lightweight runtime image
FROM openjdk:11-jre-slim

WORKDIR /app
COPY --from=builder /app/target/scala-2.12/Exercises441-assembly-0.1.0-SNAPSHOT.jar app.jar

  # Expose the port your server listens on
EXPOSE 8080

  # Set the command to run the application
CMD ["java", "-jar", "app.jar"]
