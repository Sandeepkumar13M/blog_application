FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -Dskiptests
FROM openjdk:21-jdk-slim
COPY --from=build /target/blog-application-0.0.1-SNAPSHOT.jar blog-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "blog-app.jar"]