FROM maven:3.8.3-openjdk-17-slim

ARG JAR_FILE

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]