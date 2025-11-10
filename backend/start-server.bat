@echo off
REM Batch script to start the backend server
SET JAVA_HOME=C:\Program Files\Java\jdk-22

echo Starting Bookstore Backend Server...
echo JAVA_HOME: %JAVA_HOME%
echo Server will run on http://localhost:8080
echo.

REM Start the Spring Boot application
mvnw.cmd spring-boot:run
