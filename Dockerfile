FROM openjdk:17

WORKDIR /app

COPY .env .env

COPY build/libs/jeju-0.0.1-SNAPSHOT.jar app.jar

RUN set -a && . ./.env && set +a

CMD ["sh", "-c", "java -jar app.jar"]
