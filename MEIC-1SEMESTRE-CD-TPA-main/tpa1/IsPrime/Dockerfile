FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app


# Copy the jar file into the container
# Replace 'your-application.jar' with the name of your jar file
COPY target/IsPrime-1.0-jar-with-dependencies.jar /app/IsPrime-1.0-jar-with-dependencies.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/IsPrime-1.0-jar-with-dependencies.jar"]
