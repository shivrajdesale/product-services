#### Build Application

FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /build
COPY gradle /build/gradle
COPY gradlew build.gradle settings.gradle /build/
RUN ./gradlew dependencies --no-daemon
COPY src /build/src
RUN ./gradlew bootJar --no-daemon

### Start container.
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]