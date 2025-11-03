FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/WorkerResize-1.0-shaded.jar /app/WorkerResize.jar

# config volume docker
VOLUME ["/app/data/images"]
ENTRYPOINT ["java", "-jar", "/app/WorkerResize.jar"]