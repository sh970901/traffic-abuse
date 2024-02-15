# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /build

COPY . .

RUN mvn -U clean package

# Final stage
FROM openjdk:17-jdk-slim

ARG DEFAULT_PORT=8080

WORKDIR /totoro

COPY --from=build /build/target/totoro-antiabuse.jar ./totoro-antiabuse.jar

# Set environment variables with default values
#ENV JAVA_HOME /etc/alternatives/java

ENV PORT $DEFAULT_PORT

# Expose the required port
EXPOSE $DEFAULT_PORT

# Run the application with environment variables
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar totoro-antiabuse.jar"]
#ENTRYPOINT ["java", "$JAVA_OPTS", "-Dspring.profiles.active=$SPRING_PROFILES_ACTIVE", "-jar", "dalgona.message-api.jar"]

# Default CMD (can be overridden by 'docker run' command)
CMD ["-Xms512m", "-Xmx512m"]


#docker run -e SPRING_PROFILES_ACTIVE=prod my-image
#docker build --build-arg DEFAULT_PORT=80 -t my-image .
#https://docs.docker.com/engine/reference/builder/#arg