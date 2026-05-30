# cloud/job-portal-config-server/Dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/job-portal-config-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]