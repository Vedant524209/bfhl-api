# 🚀 BFHL API

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)
![Deployment](https://img.shields.io/badge/Deployment-Render-purple.svg)

A production-ready REST API built with **Spring Boot 3** and **Java 21**. This service is designed to process and analyze mixed data arrays containing numbers, alphabets, and special characters, offering comprehensive analytics like alphabet frequency, vowel counts, sorting, and duplicate detection.

---

## ✨ Features

- **Data Categorization**: Automatically separates inputs into numbers, alphabets, and special characters.
- **Alphanumeric Processing**: Extracts distinct letters and digits from combined alphanumeric strings.
- **Advanced Mathematics**: Calculates odd/even split, sum, min, max, and sorted order using high-precision `BigDecimal`.
- **String Analytics**: Tracks vowel count, alphabet frequency, and identifies longest/shortest values.
- **Duplicate Handling**: Detects and removes duplicates before processing to ensure clean data metrics.
- **Robust Validation**: Implements Jakarta Bean Validation with structured error responses.
- **Observability**: Includes custom `X-Request-Id` header tracking and processing time measurements.
- **Health Checks**: Built-in `/health` endpoint for uptime monitoring.
- **API Documentation**: Interactive Swagger UI and OpenAPI JSON specifications.

---

## 🏗️ Architecture

The application follows a clean, layered architectural pattern:

```text
com.bajaj.bfhlapi
├── controller/          → REST endpoints (BfhlController, HealthController)
├── service/             → Business logic interfaces
│   └── impl/            → O(n) Time Complexity Service implementations
├── dto/                 → Request/Response data transfer objects
├── exception/           → @ControllerAdvice Global exception handling
├── config/              → Swagger OpenAPI & CORS configuration
└── util/                → Helper utilities
```

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Core** | Java 21 |
| **Framework** | Spring Boot 3.3.0 |
| **Build Tool** | Maven |
| **Validation** | Jakarta Bean Validation |
| **Documentation**| SpringDoc OpenAPI 2.5.0 |
| **Testing** | JUnit 5, Mockito |
| **DevOps** | Docker (Multi-stage build), Render |

---

## 🔌 API Endpoints

### 1. Process Data
**`POST /bfhl`**

Processes a mixed data array and returns comprehensive categorized analytics.

**Headers:**
- `Content-Type: application/json`
- `X-Request-Id: <custom-id>` *(Optional: auto-generated if missing)*

**Sample Request:**
```json
{
  "data": ["A", "1", "$", "B", "25.5", "-10", "A1B2", "Orange", "A"]
}
```

**Sample Response:**
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
  "vowel_count": 5,
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
  "processing_time_ms": 3,
  "summary": {
    "total_elements_received": 9,
    "valid_elements_processed": 8,
    "invalid_elements_ignored": 1
  }
}
```

### 2. System Health
**`GET /health`**

Used by Render and other deployment platforms to check service uptime.

**Sample Response:**
```json
{
  "status": "UP",
  "service": "bfhl-api",
  "timestamp": "2026-06-17T16:50:43"
}
```

---

## 📚 Swagger Documentation

When the application is running locally or deployed, you can access the interactive API documentation:

- **Swagger UI**: `/swagger-ui.html` (e.g., `http://localhost:8080/swagger-ui.html`)
- **OpenAPI JSON**: `/v3/api-docs` (e.g., `http://localhost:8080/v3/api-docs`)

---

## 💻 How to Run Locally

### Prerequisites
- Java 21
- Maven 3.9+

### Quick Start
```bash
# Clone the repository
git clone https://github.com/Vedant524209/bfhl-api.git
cd bfhl-api

# Run with Maven Wrapper
./mvnw spring-boot:run
```

The application will start at `http://localhost:8080`

### Running Tests
The project includes a robust test suite with 29 test cases ensuring high code coverage.
```bash
./mvnw test
```

---

## 🐳 Docker Instructions

Build an optimized, multi-stage Docker image:
```bash
docker build -t bfhl-api .
```

Run the container:
```bash
docker run -p 8080:8080 bfhl-api
```

---

## 🚀 Deployment Instructions (Render)

This project is pre-configured for automated deployment on **Render** using Docker.

1. Create an account on [Render](https://render.com)
2. Go to your Dashboard and click **New → Web Service**
3. Connect this GitHub repository (`Vedant524209/bfhl-api`)
4. Configure the service:
   - **Name**: `bfhl-api`
   - **Root Directory**: `bfhl-api`
   - **Runtime**: `Docker`
   - **Instance Type**: `Free`
5. Click **Deploy Web Service**

Render will automatically read the `Dockerfile` and `render.yaml`, set up the `PORT` environment variable, and expose your public API URL (e.g., `https://bfhl-api-xxxx.onrender.com/bfhl`).
