FROM openjdk:8

ADD target/docker-dela-task.jar /UserService.jar

EXPOSE 8080

CMD ["java", "-jar", "/UserService.jar"]