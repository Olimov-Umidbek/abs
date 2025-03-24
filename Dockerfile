#Prepare dependencies
FROM maven:3.8.5-openjdk-17 AS DEPENDENCIES

WORKDIR /app
COPY commons/pom.xml commons/pom.xml
COPY abs-backend/pom.xml abs-backend/pom.xml
COPY pom.xml .

RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.8.1:go-offline

# Build stage
FROM maven:3.8.5-openjdk-17 AS BUILDER
WORKDIR /app
COPY --from=DEPENDENCIES /root/.m2 /root/.m2
COPY --from=DEPENDENCIES /app/ /app
COPY commons/src /app/commons/src
COPY abs-backend/src /app/abs-backend/src

RUN mvn -B -e clean install -DskipTests

FROM openjdk:17-slim

WORKDIR app
COPY --from=BUILDER /app/abs-backend/target/*.jar /app/app.jar
ENTRYPOINT java -jar -Dspring.profiles.active=prod /app/app.jar
EXPOSE 8080
