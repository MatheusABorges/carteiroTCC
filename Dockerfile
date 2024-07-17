FROM openjdk:21-slim as build

RUN apt-get update && apt-get install -y wget unzip
RUN wget https://services.gradle.org/distributions/gradle-8.1-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-8.1-bin.zip && \
    ln -s /opt/gradle/gradle-8.1/bin/gradle /usr/bin/gradle

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
COPY src/ src/

RUN chmod +x gradlew

RUN ./gradlew build --no-daemon

FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /app/build/libs/carteiro.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]