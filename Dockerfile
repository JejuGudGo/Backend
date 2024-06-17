# Base image
FROM openjdk:17

# Work directory
WORKDIR /app

# Copy the application JAR
COPY build/libs/jeju-0.0.1-SNAPSHOT.jar app.jar

# Copy the .env file
COPY .env .env

# Set environment variables from .env file
RUN export $(grep -v '^#' .env | xargs)

# Run the application
CMD ["sh", "-c", "java -jar app.jar"]
