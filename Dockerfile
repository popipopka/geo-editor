FROM openjdk:17-jdk-alpine3.14 AS build

ARG JAR_FILE
WORKDIR /build

COPY ${JAR_FILE} app.jar

RUN java -Djarmode=layertools -jar app.jar extract --destination extracted

FROM openjdk:17-jdk-alpine3.14

VOLUME /tmp
WORKDIR /app

COPY --from=build /build/extracted/dependencies .
COPY --from=build /build/extracted/spring-boot-loader .
COPY --from=build /build/extracted/snapshot-dependencies .
COPY --from=build /build/extracted/application .

ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}