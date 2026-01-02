FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy the POM file first to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

## Set up protobuf and gRPC
#RUN mvn protobuf:compile protobuf:compile-custom -DskipTests

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR file
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 4000

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]