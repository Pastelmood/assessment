FROM gcr.io/distroless/java17-debian12:latest
WORKDIR /app
COPY /build/libs/posttest-0.0.1-SNAPSHOT.jar posttest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "posttest-0.0.1-SNAPSHOT.jar"]