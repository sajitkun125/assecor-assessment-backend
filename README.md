# Assecor Assessment Backend - REST API Implementation

This project implements a RESTful web interface for managing persons and their favorite colors, reading data from CSV files with support for multiple data sources.

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Maven 3.6** or higher

### Build & Run
```bash
# Build the project
mvn clean install

# Run the application (CSV mode - default)
mvn spring-boot:run

# Or use the run script
./run.sh
```

The application will start on `http://localhost:8080`

### Quick Test
```bash
# Get all persons
curl http://localhost:8080/persons

# Get person by ID
curl http://localhost:8080/persons/1

# Get persons by color
curl http://localhost:8080/persons/color/blau
```

## 📋 Implementation Overview

### Technology Stack
- **Framework:** Spring Boot 3.2.1
- **Language:** Java 17
- **Build Tool:** Maven
- **CSV Parsing:** Apache Commons CSV
- **Database:** H2 (optional, in-memory)
- **ORM:** JPA/Hibernate (for database mode)
- **Testing:** JUnit 5, MockMvc
- **Validation:** Bean Validation (Jakarta)

### Project Structure
```
src/main/java/com/assecor/assessment/
├── AssecorAssessmentApplication.java    # Main application
├── controller/
│   └── PersonController.java            # REST endpoints
├── service/
│   └── PersonService.java               # Business logic
├── repository/
│   ├── PersonRepository.java            # Repository interface
│   ├── CsvPersonRepository.java         # CSV implementation
│   └── JpaPersonRepository.java         # Database implementation
├── model/
│   └── Person.java                      # Person entity
├── exception/
│   ├── GlobalExceptionHandler.java      # Centralized error handling
│   ├── PersonNotFoundException.java     # 404 exception
│   ├── InvalidRequestException.java     # 400 exception
│   └── ErrorResponse.java               # Error response DTO
└── config/
    ├── ColorMapper.java                 # Color ID mapping
    └── DatabaseInitializer.java         # DB initialization

src/main/resources/
├── application.properties               # CSV mode config (default)
└── application-database.properties      # Database mode config

src/test/java/
└── PersonControllerTest.java            # REST endpoint tests (10 tests)
```

## 🎯 API Endpoints

All endpoints return `application/json`

### 1. GET /persons
Get all persons
```bash
curl http://localhost:8080/persons
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Hans",
    "lastname": "Müller",
    "zipcode": "67742",
    "city": "Lauterecken",
    "color": "blau"
  }
]
```

### 2. GET /persons/{id}
Get person by ID (CSV line number)
```bash
curl http://localhost:8080/persons/1
```

**Response:** `200 OK` or `404 Not Found`

### 3. GET /persons/color/{color}
Get all persons with specific color
```bash
curl http://localhost:8080/persons/color/blau
```

**Response:** `200 OK`

### 4. POST /persons (Bonus Feature)
Create a new person
```bash
curl -X POST http://localhost:8080/persons \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Max",
    "lastname": "Mustermann",
    "zipcode": "12345",
    "city": "Berlin",
    "color": "rot"
  }'
```

**Response:** `201 Created`

## 🎨 Color Mapping

| ID | Color (Farbe) |
|----|---------------|
| 1  | blau          |
| 2  | grün          |
| 3  | violett       |
| 4  | rot           |
| 5  | gelb          |
| 6  | türkis        |
| 7  | weiß          |

## 🏗️ Architecture & Design

### Layered Architecture
```
┌─────────────────────┐
│  PersonController   │  ← REST Layer
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│  PersonService      │  ← Business Logic
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│ PersonRepository    │  ← Data Access Interface
└──────────┬──────────┘
           │
      ┌────┴────┐
      │         │
┌─────▼───┐  ┌─▼────────┐
│   CSV   │  │    H2    │  ← Data Sources
└─────────┘  └──────────┘
```

### Key Design Patterns
- **Repository Pattern:** Abstracted data access
- **Dependency Injection:** Spring's IoT container
- **Strategy Pattern:** Switchable data sources via `@ConditionalOnProperty`
- **Builder Pattern:** Clean object construction (Lombok)

### Exception Handling
Centralized error handling with `@RestControllerAdvice`:
- **404 Not Found:** Person not found
- **400 Bad Request:** Validation errors, invalid data types
- **500 Internal Server Error:** Unexpected errors

All errors return consistent JSON format:
```json
{
  "timestamp": "2026-01-30T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Person not found with ID: 999",
  "path": "/persons/999"
}
```

## 📊 Data Sources

### CSV Mode (Default)
- Reads from `sample-input.csv` on startup
- In-memory storage (ConcurrentHashMap)
- Thread-safe
- Fast access
- Data not persisted between restarts

**Configuration:** `data.source=csv` (default in `application.properties`)

### Database Mode (Bonus Feature)
- H2 in-memory database
- JPA/Hibernate
- Data persists during runtime
- H2 Console available at `/h2-console`

**To activate:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=database
```

Or edit `application.properties`:
```properties
data.source=database
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
- **10 unit tests** for REST endpoints
- **100% controller coverage**
- All success and error scenarios covered

### Test Cases
- Get all persons
- Get person by ID (found)
- Get person by ID (not found - 404)
- Get persons by color
- Get persons by color (empty result)
- Create person (success - 201)
- Create person (validation failure - 400)
- Invalid ID type (400)
- POST with multiple color results
- Integration test for application context

**Test Results:** All tests passing ✅

## 🔧 Configuration

### Application Properties

**CSV Mode (default):**
```properties
data.source=csv
csv.file.path=sample-input.csv
```

**Database Mode:**
```properties
data.source=database
spring.datasource.url=jdbc:h2:mem:assecor
spring.jpa.hibernate.ddl-auto=create-drop
```

### Logging
- **Level:** INFO (root), DEBUG (application)
- **Pattern:** `%d{yyyy-MM-dd HH:mm:ss} - %msg%n`

## 📦 Build & Deployment

### Build JAR
```bash
mvn clean package
```

Creates: `target/assecor-assessment-backend-1.0.0.jar`

### Run JAR
```bash
# CSV mode
java -jar target/assecor-assessment-backend-1.0.0.jar

# Database mode
java -jar target/assecor-assessment-backend-1.0.0.jar --spring.profiles.active=database
```

## 🔍 How It Works

### CSV Data Loading
1. Application starts
2. `CsvPersonRepository` bean is created (via `@ConditionalOnProperty`)
3. `@PostConstruct` method loads CSV file
4. Each line parsed: `Lastname, Firstname, Zipcode City, ColorID`
5. Color ID mapped to name via `ColorMapper`
6. Data stored in `ConcurrentHashMap` (thread-safe)
7. Ready for API requests

### Request Flow
```
HTTP Request → PersonController
              ↓
          PersonService (validation, business logic)
              ↓
          PersonRepository (interface)
              ↓
          CsvPersonRepository or JpaPersonRepository
              ↓
          Data (in-memory or database)
```

## 🚨 Error Handling Examples

### Person Not Found
```bash
curl http://localhost:8080/persons/999
```
Response: `404 Not Found`
```json
{
  "timestamp": "2026-01-30T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Person not found with ID: 999",
  "path": "/persons/999"
}
```

### Invalid Data Type
```bash
curl http://localhost:8080/persons/invalid
```
Response: `400 Bad Request`

### Validation Error
```bash
curl -X POST http://localhost:8080/persons \
  -H "Content-Type: application/json" \
  -d '{"zipcode":"12345"}'
```
Response: `400 Bad Request` with validation details


## 🔐 Data Persistence

**CSV Mode:**
- ⚠️ In-memory only
- Changes lost on restart
- POST adds to memory, not file

**Database Mode:**
- ✅ Persists during runtime
- ⚠️ In-memory H2 (data lost on restart)
- Can be configured for persistent DB

## 📝 Development Notes

### Why CSV is Default
- `@ConditionalOnProperty(name = "data.source", havingValue = "csv", matchIfMissing = true)`
- `matchIfMissing = true` ensures CSV is used when property is not set
- Matches assessment requirements

### Special Character Handling
- ✅ German umlauts (ü, ö, ä)
- ✅ Special symbols (☀)
- ✅ Multi-word cities
- UTF-8 encoding throughout

---

# Original Assessment Requirements

# Assecor Assessment Test (DE)

## Zielsetzung

Das Ziel ist es ein REST – Interface zu implementieren. Bei den möglichen Frameworks stehen .NET(C#), Java oder Go zur Auswahl. Dabei sind die folgenden Anforderungen zu erfüllen:

* Es soll möglich sein, Personen und ihre Lieblingsfarbe über das Interface zu verwalten
* Die Daten sollen aus einer CSV Datei lesbar sein, ohne dass die CSV angepasst werden muss
* Alle Personen mit exakten Lieblingsfarben können über das Interface identifiziert werden

Einige Beispieldatensätze finden sich in `sample-input.csv`. Die Zahlen der ersten Spalte sollen den folgenden Farben entsprechen:

| ID | Farbe |
| --- | --- |
| 1 | blau |
| 2 | grün |
| 3 | violett |
| 4 | rot |
| 5 | gelb |
| 6 | türkis |
| 7 | weiß |

Das Ausgabeformat der Daten ist als `application/json` festgelegt. Die Schnittstelle soll folgende Endpunkte anbieten:

**GET** /persons
```json
[{
"id" : 1,
"name" : "Hans",
"lastname": "Müller",
"zipcode" : "67742",
"city" : "Lauterecken",
"color" : "blau"
},{
"id" : 2,
...
}]
```

**GET** /persons/{id}

*Hinweis*: als **ID** kann hier die Zeilennummer verwendet werden.
```json
{
"id" : 1,
"name" : "Hans",
"lastname": "Müller",
"zipcode" : "67742",
"city" : "Lauterecken",
"color" : "blau"
}
```

**GET** /persons/color/{color}
```json
[{
"id" : 1,
"name" : "Hans",
"lastname": "Müller",
"zipcode" : "67742",
"city" : "Lauterecken",
"color" : "blau"
},{
"id" : 2,
...
}]
```

## Akzeptanzkriterien

1. Die CSV Datei wurde eingelesen, und wird programmintern durch eine dem Schema entsprechende Modellklasse repräsentiert.
2. Der Zugriff auf die Datensätze so abstrahiert, dass eine andere Datenquelle angebunden werden kann, ohne den Aufruf anpassen zu müssen.
3. Die oben beschriebene REST-Schnittstelle wurde implementiert und liefert die korrekten Antworten.
4. Der Zugriff auf die Datensätze, bzw. auf die zugreifende Klasse wird über Dependency Injection gehandhabt.
5.  Die REST-Schnittstelle ist mit Unit-Tests getestet. 
6.  Die `sample-input.csv` wurde nicht verändert 

## Bonuspunkte
* Implementierung als MSBuild Projekt für kontinuierliche Integration auf TFS (C#/.NET) oder als Maven/Gradle Projekt (Java)
* Implementieren Sie eine zusätzliche Methode POST/ Personen, die eine zusätzliche Aufzeichnung zur Datenquelle hinzufügen
* Anbindung einer zweiten Datenquelle (z.B. Datenbank via Entity Framework)

Denk an deine zukünftigen Kollegen, und mach es ihnen nicht zu einfach, indem du deine Lösung öffentlich zur Schau stellst. Danke!

# Assecor Assessment Test (EN)

## goal

You are to implement a RESTful web interface. The choice of framework and stack is yours between .NET (C#), Java or Go. It has to fulfill the following criteria:

* You should be able to manage persons and their favourite colour using the interface
* The application should be able to read the date from the CSV source, without modifying the source file
* You can identify people with a common favourite colour using the interface

A set of sample data is contained within `sample-input.csv`. The number in the first column represents one of the following colours:

| ID | Farbe |
|---|---|
| 1 | blau |
| 2 | grün |
| 3 | violett |
| 4 | rot |
| 5 | gelb |
| 6 | türkis |
| 7 | weiß |

the return content type is `application/json`. The interface should offer the following endpoints:

**GET** /persons
```json
[{
"id" : 1,
"name" : "Hans",
"lastname": "Müller",
"zipcode" : "67742",
"city" : "Lauterecken",
"color" : "blau"
},{
"id" : 2,
...
}]
```

**GET** /persons/{id}

*HINT*: use the csv line number as your **ID**.
```json
{
"id" : 1,
"name" : "Hans",
"lastname": "Müller",
"zipcode" : "67742",
"city" : "Lauterecken",
"color" : "blau"
}
```

**GET** /persons/color/{color}
```json
[{
"id" : 1,
"name" : "Hans",
"lastname": "Müller",
"zipcode" : "67742",
"city" : "Lauterecken",
"color" : "blau"
},{
"id" : 2,
...
}]
```

## acceptance criteria

1. The csv file is read and represented internally by a suitable model class.
2. File access is done with an interface, so the implementation can be easily replaced for other data sources.
3. The REST interface is implemented according to the above specifications.
4. Data access is done using a dependency injection mechanism
5. Unit tests for the REST interface are available.
6. `sample-input.csv` has not been changed.

## bonus points are awarded for the following
* implement the project with MSBuild in mind for CI using TFS/DevOps when using .NET, or as a Maven/Gradle project in Java
* Implement an additional **POST** /persons to add new people to the dataset
* Add a secondary data source (e.g. database via EF or JPA)

Think about your potential future colleagues, and do not make it too easy for them by posting your solution publicly. Thank you!
