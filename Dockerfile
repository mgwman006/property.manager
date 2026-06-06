FROM maven:3-eclipse-temurin-17-alpine as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn clean package -DskipTests

# Use Eclipse Temurin for base image.
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
#FROM eclipse-temurin:17.0.15_6-jre-alpine
FROM openjdk:17-jdk-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/landlordtenant-*.jar /app.jar

# Expose port 8080
EXPOSE 8080

# Run the web service on container startup.
CMD ["java", "-jar", "/app.jar"]

# [END cloudrun_helloworld_dockerfile]