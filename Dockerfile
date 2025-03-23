FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . /app

RUN ./mvnw clean package

CMD ["java", "-jar, "target/my-java-app-1.0-SNAPSHOT.jar"]

