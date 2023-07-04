FROM eclipse-temurin:17.0.5_8-jre-alpine
WORKDIR /app
COPY ./web/target/doc-main-service.jar /app/doc-main-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "doc-main-service.jar", "--spring.profiles.active=prod"]
