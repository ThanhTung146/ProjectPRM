# PowerShell script to start the backend server
# Set JAVA_HOME environment variable
$env:JAVA_HOME = "C:\Program Files\Java\jdk-22"

Write-Host "Starting Bookstore Backend Server..." -ForegroundColor Green
Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Yellow
Write-Host "Server will run on http://localhost:8080" -ForegroundColor Yellow
Write-Host ""

# Start the Spring Boot application
.\mvnw.cmd spring-boot:run
