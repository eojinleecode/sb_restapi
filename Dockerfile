FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/spring_crud_activity-0.0.1-SNAPSHOT.jar"]
