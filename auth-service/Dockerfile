# Start with a base image containing Java runtime:
FROM openjdk:8-jdk-alpine

# Add Maintainer info:
MAINTAINER davisallen <davis.b.allen@gmail.com>

# Make port 8082 available to the world outside this container:
EXPOSE 8082

# Add a volume pointing to /tmp:
VOLUME /tmp

# The application's JAR file:
ARG JAR_FILE=target/auth-service-0.0.1-SNAPSHOT.jar

# Add the application's JAR to the container:
ADD ${JAR_FILE} auth-service.jar

COPY ./src/main/resources/*.properties /config/

COPY ./src/main/resources/db/migration /config/migration

ENV SPRING_PROFILES_ACTIVE dev

RUN ls /config/migration

# Run the jar file:
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/auth-service.jar"]
