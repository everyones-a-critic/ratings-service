# syntax=docker/dockerfile:1
# Issue caused by M1 architecture not matching AWS linux:
# https://stackoverflow.com/questions/67361936/exec-user-process-caused-exec-format-error-in-aws-fargate-service
# ./mvnw clean package
# aws ecr get-login-password --region us-west-1 | docker login --username AWS --password-stdin 081924037451.dkr.ecr.us-west-1.amazonaws.com
# docker buildx build --platform=linux/amd64 -t eac-ratings-service .
# docker tag eac-ratings-service:latest 081924037451.dkr.ecr.us-west-1.amazonaws.com/eac-ratings-service:latest
# docker push 081924037451.dkr.ecr.us-west-1.amazonaws.com/eac-ratings-service:latest

FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]