# Start with a base image containing Gradle and Java 21
FROM gradle:8.8-jdk21

# Set the working directory
WORKDIR /app

# Copy the build.gradle and settings.gradle files first
COPY build.gradle settings.gradle ./

# Copy the gradle wrapper files
COPY gradlew ./
COPY gradle ./gradle

# Copy the remaining project files,
COPY . .
RUN mv src/main/resources/application.properties.back4app src/main/resources/application.properties

# Give executable permission to the gradlew file
RUN chmod +x gradlew

# Fetch gradle dependencies before copying the entire project
RUN gradle dependencies

# Build the application
RUN gradle build --no-daemon

# Expose the application port (adjust as needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/apiMvola-0.0.1-SNAPSHOT.jar"]
