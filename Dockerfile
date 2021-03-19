FROM openjdk:latest

COPY target/docker-dela-task.jar /UserService.jar

CMD ["java", "-jar", "/UserService.jar"]