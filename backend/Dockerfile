# Build stage
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime stage WITH Maven
FROM eclipse-temurin:21-jdk AS runtime
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
