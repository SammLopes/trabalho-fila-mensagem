FROM ubuntu:latest

RUN apt-get update

RUN apt-get install -y maven  openjdk-21-jdk

RUN apt-get update

WORKDIR /app

COPY . /app

RUN mvn clean install

EXPOSE 8080

CMD ["java", "-jar", "./target/microservico1-0.0.1-SNAPSHOT.jar"]

