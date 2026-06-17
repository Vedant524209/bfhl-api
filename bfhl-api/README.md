# BFHL API

A production-ready REST API built with Spring Boot 3 and Java 21 for processing and analyzing mixed data arrays containing numbers, alphabets, and special characters.

---

## Architecture

```
com.bajaj.bfhlapi
├── controller/          → REST endpoints
├── service/             → Business logic interfaces
│   └── impl/            → Service implementations
├── dto/                 → Request/Response objects
├── exception/           → Global exception handling
├── config/              → Swagger & CORS configuration
└── util/                → Helper utilities
```

---

## Features

- Categorizes input into numbers, alphabets, and special characters
- Extracts letters and digits from alphanumeric strings
- Handles negative and decimal numbers using BigDecimal
- Detects and removes duplicates before processing
- Calculates odd/even split, sum, min, max, sorted order
- Tracks vowel count, alphabet frequency, longest/shortest values
- Structured error responses with Bean Validation
- Request tracing via X-Request-Id header
- Processing time measurement per request
- Swagger UI for API exploration

---

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.0 |
| Build | Maven |
| Validation | Jakarta Bean Validation |
| Documentation | SpringDoc OpenAPI 2.5.0 |
| Testing | JUnit 5, Mockito |
| Containerization | Docker (multi-stage) |

---

## API Endpoint

### POST /bfhl

Processes a mixed data array and returns categorized results.

**Headers:**

| Header | Required | Description |
|--------|----------|-------------|
| Content-Type | Yes | application/json |
| X-Request-Id | No | Custom request ID (auto-generated if missing) |

---

## Sample Request

```json
{
  "data": ["A", "1", "$", "B", "25.5", "-10", "A1B2", "Orange", "A"]
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -H "X-Request-Id: req-001" \
  -d '{"data": ["A", "1", "$", "B", "25.5", "-10", "A1B2", "Orange", "A"]}'
```

---

## Sample Response

```json
{
  "is_success": true,
  "request_id": "req-001",
  "odd_numbers": [1],
  "even_numbers": [-10, 12],
  "alphabets": ["A", "B", "Orange", "AB"],
  "special_characters": ["$"],
  "sum": 28.5,
  "largest_number": 25.5,
  "smallest_number": -10,
  "alphabet_count": 4,
  "number_count": 4,
  "special_character_count": 1,
  "contains_duplicates": true,
  "unique_element_count": 8,
  "sorted_numbers": [-10, 1, 12, 25.5],
  "vowel_count": 3,
  "alphabet_frequency": {
    "A": 3,
    "B": 2,
    "O": 1,
    "R": 1,
    "N": 1,
    "G": 1,
    "E": 1
  },
  "longest_alphabetic_value": "Orange",
  "shortest_alphabetic_value": "A",
  "processing_time_ms": 2,
  "summary": {
    "total_elements_received": 9,
    "valid_elements_processed": 8,
    "invalid_elements_ignored": 1
  }
}
```

**Error Response:**

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": ["data: must not be null"],
  "timestamp": "2026-06-17T16:00:00"
}
```

---

## How to Run

### Prerequisites

- Java 21
- Maven 3.9+

### Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Using JAR

```bash
mvn clean package -DskipTests
java -jar target/bfhl-api-1.0.0.jar
```

The application starts at `http://localhost:8080`

---

## Docker Instructions

### Build

```bash
docker build -t bfhl-api .
```

### Run

```bash
docker run -p 8080:8080 bfhl-api
```

### Run with custom port

```bash
docker run -p 9090:8080 -e SERVER_PORT=8080 bfhl-api
```

---

## Deployment Instructions

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| SERVER_PORT | 8080 | Application port |

### Deploy to Railway / Render

1. Push code to GitHub
2. Connect repository to Railway or Render
3. Set build command: `mvn clean package -DskipTests`
4. Set start command: `java -jar target/bfhl-api-1.0.0.jar`
5. Deploy

### Deploy with Docker

```bash
docker build -t bfhl-api .
docker tag bfhl-api your-registry/bfhl-api:latest
docker push your-registry/bfhl-api:latest
```

---

## Swagger Documentation

Once the application is running:

| Resource | URL |
|----------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

---

## Project Structure

```
bfhl-api/
├── pom.xml
├── Dockerfile
├── .dockerignore
├── mvnw
├── .mvn/wrapper/maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/bajaj/bfhlapi/
│   │   │   ├── BfhlApiApplication.java
│   │   │   ├── controller/BfhlController.java
│   │   │   ├── service/BfhlService.java
│   │   │   ├── service/impl/BfhlServiceImpl.java
│   │   │   ├── dto/
│   │   │   │   ├── RequestDto.java
│   │   │   │   ├── ResponseDto.java
│   │   │   │   ├── SummaryDto.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── InvalidInputException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── config/
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── CorsConfig.java
│   │   │   └── util/
│   │   │       ├── DataProcessor.java
│   │   │       └── FileValidator.java
│   │   └── resources/application.yml
│   └── test/java/com/bajaj/bfhlapi/
│       └── service/impl/BfhlServiceImplTest.java
```

---

## Running Tests

```bash
mvn test
```

27 test cases covering:
- Negative and decimal numbers
- Duplicate detection
- Null and blank value handling
- Alphabet frequency calculation
- Vowel counting
- Longest/shortest alphabetic values
- Odd/even number splitting
- Alphanumeric string extraction
- Sum and sorting verification
