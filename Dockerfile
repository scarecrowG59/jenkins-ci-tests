# 🚧 Build Stage
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package

# 🚀 Runtime Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /build/target/my-java-app-1.0-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/my-java-app-1.0-SNAPSHOT.jar"]
