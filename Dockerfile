FROM alpine:3.17.3
FROM adoptopenjdk/openjdk14:x86_64-alpine-jre-14.0.2_12
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]