FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . /app

RUN apt update && apt install -y maven

RUN mvn clean package

CMD ["java", "-jar, "target/my-java-app-1.0-SNAPSHOT.jar"]

