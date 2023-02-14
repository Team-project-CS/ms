FROM maven:3.8.3-openjdk-17-slim
WORKDIR /app
COPY . .
EXPOSE 8081
RUN mvn install --no-transfer-progress -DskipTests=true
ENTRYPOINT ["mvn", "spring-boot:run"]