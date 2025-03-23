# 🚧 Build Stage
FROM maven:3.8.7-openjdk-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package

# 🚀 Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/my-java-app-1.0-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/my-java-app-1.0-SNAPSHOT.jar"]
