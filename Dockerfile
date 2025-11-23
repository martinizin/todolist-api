
FROM eclipse-temurin:21-jdk-alpine AS deps

WORKDIR /app


COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .


RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon


FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app


COPY --from=deps /root/.gradle /root/.gradle
COPY --from=deps /app/gradlew .
COPY --from=deps /app/gradle gradle
COPY --from=deps /app/build.gradle .
COPY --from=deps /app/settings.gradle .


COPY src src


RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test --no-daemon


FROM eclipse-temurin:21-jre-alpine


RUN apk add --no-cache curl


RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app


COPY --from=builder /app/build/libs/*.jar app.jar


RUN chown spring:spring app.jar


USER spring:spring


ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SERVER_PORT=8080


EXPOSE ${SERVER_PORT}


HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1


ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
