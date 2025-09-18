FROM eclipse-temurin:17-jdk-alpine

# Add a volume for temporary files
VOLUME /tmp

# Copy your JAR from target/
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]
