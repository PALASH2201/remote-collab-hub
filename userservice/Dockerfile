# Dockerfile (Java 21 base image)
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/userService.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
