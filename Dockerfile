# Build phase
FROM maven:3.8.6-eclipse-temurin-17-alpine AS MAVEN_BUILD
COPY . /build/
WORKDIR /build/
RUN mvn install -DskipTests=true -DskipTypescriptGenerator=true

# Run phase
FROM eclipse-temurin:17.0.5_8-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/web/target/doc-main-service.jar /app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","doc-main-service.jar", "--spring.profiles.active=local-deployment"]
