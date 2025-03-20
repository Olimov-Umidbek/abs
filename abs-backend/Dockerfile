FROM openjdk:17
USER 1001
WORKDIR /app
COPY target/abs-backend.jar /app/abs-backend.jar
ENTRYPOINT java -jar -Dspring.profiles.active=prod /app/abs-backend.jar
EXPOSE 8080
