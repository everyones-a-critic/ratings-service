# syntax=docker/dockerfile:1
# Issue caused by M1 architecture not matching AWS linux:
# https://stackoverflow.com/questions/67361936/exec-user-process-caused-exec-format-error-in-aws-fargate-service
# ./mvnw clean package
# docker buildx build --platform=linux/amd64 -t eac-ratings-service-prod .

FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]