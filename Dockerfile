FROM maven:3.6.3-jdk-11-slim
WORKDIR /app
COPY . .
EXPOSE 8081
RUN mvn install --no-transfer-progress -DskipTests=true
ENTRYPOINT ["mvn", "spring-boot:run"]