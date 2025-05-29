FROM openjdk:21
COPY target/my-app.jar /app.jar
ENTRYPOINT ["java", "-jar", "/webhook_processor.jar"]