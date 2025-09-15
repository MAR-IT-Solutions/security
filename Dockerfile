FROM eclipse-temurin:23-jre-alpine
ARG JAR_FILE=target/security-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
