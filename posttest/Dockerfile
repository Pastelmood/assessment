# State-1 Build artifact
FROM amazoncorretto:17-alpine3.19 as builder
WORKDIR /app
ADD . .
RUN ["./gradlew","bootJar"]

# State-2 Running image
FROM gcr.io/distroless/java17-debian12:latest
WORKDIR /app
COPY --from=builder /app/build/libs/posttest-0.0.1-SNAPSHOT.jar posttest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "posttest-0.0.1-SNAPSHOT.jar"]