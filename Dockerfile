FROM openjdk:17-jdk-slim

WORKDIR /app

COPY src /app/src

RUN mkdir /app/bin && javac /app/src/main/java/com/example/App.java -d /app/bin

CMD ["java", "-cp", "/app/bin", "com.example.App"]

