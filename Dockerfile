# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the build.gradle and settings.gradle files first
COPY build.gradle settings.gradle ./

# Copy the gradle wrapper files
COPY gradlew ./
COPY gradle ./gradle

# Fetch gradle dependencies before copying the entire project
RUN ./gradlew dependencies --no-daemon

# Copy the remaining project files, rename application.properties.back4app
COPY --from=none . .
RUN mv src/main/resources/application.properties.back4app src/main/resources/application.properties


# Build the application
RUN ./gradlew build --no-daemon

# Expose the application port (adjust as needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/your-application.jar"]
