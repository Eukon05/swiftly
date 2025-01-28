FROM amazoncorretto:23

COPY Interns_2025_SWIFT_CODES.xlsx appdata/file.xlsx
COPY target/*.jar app.jar

EXPOSE 8080:8080
ENTRYPOINT ["java", "-jar", "/app.jar"]