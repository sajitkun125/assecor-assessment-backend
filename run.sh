#!/bin/bash

# Quick Start Script for Assecor Assessment Backend

echo "============================================"
echo "Assecor Assessment Backend - Quick Start"
echo "============================================"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven first."
    exit 1
fi

# Check Java version
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 17 or higher."
    exit 1
fi

echo "Java version:"
java -version
echo ""

# Build the project
echo "Building the project..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo ""
echo "Build successful!"
echo ""

# Ask user which mode to run
echo "Choose data source:"
echo "1) CSV (default)"
echo "2) H2 Database"
read -p "Enter choice (1 or 2): " choice

echo ""
echo "Starting application..."
echo ""

case $choice in
    2)
        echo "Running with H2 Database..."
        echo "H2 Console will be available at: http://localhost:8080/h2-console"
        java -jar target/assecor-assessment-backend-1.0.0.jar --spring.profiles.active=database
        ;;
    *)
        echo "Running with CSV data source..."
        java -jar target/assecor-assessment-backend-1.0.0.jar
        ;;
esac
