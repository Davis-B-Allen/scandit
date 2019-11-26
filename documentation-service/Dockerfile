# Start with a base image containing Java runtime:
FROM openjdk:8-jdk-alpine

# Add Maintainer info:
MAINTAINER davisallen <davis.b.allen@gmail.com>

# Make port 9093 available to the world outside this container:
EXPOSE 9093

# Add a volume pointing to /tmp:
VOLUME /tmp

# The application's JAR file:
ARG JAR_FILE=target/documentation-service-0.0.1-SNAPSHOT.jar

# Add the application's JAR to the container:
ADD ${JAR_FILE} documentation-service.jar

COPY ./src/main/resources/*.properties /config/

ENV SPRING_PROFILES_ACTIVE dev

# Run the jar file:
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/documentation-service.jar"]
