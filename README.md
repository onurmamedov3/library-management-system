# Library Management System

A RESTful API for managing a library system built with **Spring Boot 4**, **Spring Data JPA**, and **MySQL**. The system handles books, authors, members, and borrowing operations with a proper layered architecture.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4.0.7 | Application framework |
| Spring Data JPA | Data access layer |
| MySQL 8 | Relational database |
| Hibernate 7 | ORM |
| MapStruct 1.6 | DTO ↔ Entity mapping |
| Lombok | Boilerplate reduction |
| SpringDoc OpenAPI 3 | Swagger API documentation |
| JUnit 5 + Mockito | Testing |
| H2 Database | In-memory test database |
| Gradle | Build tool |

## Architecture

The project follows a **layered architecture** pattern:

```
Controller → Service → Repository → Database
    ↕            ↕
   DTOs        Entities
    ↕
  Mapper
```

| Layer | Responsibility |
|---|---|
| **Controller** | Handles HTTP requests, validation, and response formatting |
| **Service** | Business logic and orchestration |
| **Repository** | Database access via Spring Data JPA |
| **DTO** | Request/Response objects (decoupled from entities) |
| **Mapper** | Converts between DTOs and Entities (MapStruct) |
| **Entity** | JPA database models |

## Domain Model

```
┌──────────┐     ManyToMany     ┌──────────┐
│   Book   │◄──────────────────►│  Author  │
└──────────┘                    └──────────┘
     ▲
     │ ManyToOne
┌──────────┐
│  Borrow  │
└──────────┘
     │ ManyToOne
     ▼
┌──────────┐
│  Member  │
└──────────┘
```

## Prerequisites

- **Java 21** or higher
- **MySQL 8** running on `localhost:3306`
- **Gradle** (wrapper included)

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd library-management-system
```

### 2. Create the database

```sql
CREATE DATABASE library_management_system;
```

### 3. Configure environment variables

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

Edit `.env` with your MySQL credentials:

```properties
DB_URL=jdbc:mysql://localhost:3306/library_management_system
DB_USERNAME=root
DB_PASSWORD=your_password
```

### 4. Run the application

```bash
./gradlew bootRun
```

The application starts on **http://localhost:8080**

### 5. Access Swagger UI

Open **http://localhost:8080/swagger-ui/index.html** to explore and test all API endpoints.

## API Endpoints

### Books (`/api/v1/books`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/books` | Get all books (paginated) |
| `GET` | `/api/v1/books/{id}` | Get a book by UUID |
| `POST` | `/api/v1/books` | Create a new book |
| `PATCH` | `/api/v1/books/{isbn}` | Update a book by ISBN |
| `DELETE` | `/api/v1/books/{isbn}` | Soft-delete a book by ISBN |

### Authors (`/api/v1/authors`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/authors` | Get all authors (paginated) |
| `GET` | `/api/v1/authors/{id}` | Get an author by UUID |
| `POST` | `/api/v1/authors` | Create a new author |
| `PATCH` | `/api/v1/authors/{email}` | Update an author by email |
| `DELETE` | `/api/v1/authors/{email}` | Soft-delete an author by email |

### Members (`/api/v1/members`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/members` | Get all members (paginated) |
| `GET` | `/api/v1/members/{id}` | Get a member by UUID |
| `POST` | `/api/v1/members` | Register a new member |
| `PATCH` | `/api/v1/members/{email}` | Update a member by email |
| `DELETE` | `/api/v1/members/{email}` | Soft-delete a member by email |

### Borrows (`/api/v1/borrows`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/borrows` | Get all borrow records (paginated) |
| `GET` | `/api/v1/borrows/{id}` | Get a borrow record by UUID |
| `POST` | `/api/v1/borrows` | Borrow a book |
| `PATCH` | `/api/v1/borrows/{id}/return` | Return a borrowed book |

## Pagination & Sorting

All list endpoints support pagination and sorting via query parameters:

```
GET /api/v1/books?page=0&size=10&sort=title,asc
GET /api/v1/members?page=0&size=5&sort=lastName,desc
GET /api/v1/borrows?sort=borrowDate,desc
```

| Parameter | Default | Description |
|---|---|---|
| `page` | `0` | Page number (zero-based) |
| `size` | `20` | Number of records per page |
| `sort` | — | Field and direction (e.g., `title,asc`) |

## Request/Response Examples

### Create a Book

**Request:**
```bash
POST /api/v1/books
Content-Type: application/json

{
  "title": "Clean Code",
  "isbn": "978-0132350884",
  "description": "A handbook of agile software craftsmanship",
  "publicationDate": "2008-08-01",
  "authorIds": []
}
```

**Response (201):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Clean Code",
  "isbn": "978-0132350884"
}
```

### Borrow a Book

**Request:**
```bash
POST /api/v1/borrows
Content-Type: application/json

{
  "memberId": "550e8400-e29b-41d4-a716-446655440001",
  "bookId": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response (201):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "memberId": "550e8400-e29b-41d4-a716-446655440001",
  "memberName": "John Doe",
  "bookId": "550e8400-e29b-41d4-a716-446655440000",
  "bookTitle": "Clean Code",
  "borrowDate": "2026-07-22T15:30:00",
  "returnDate": null,
  "status": "ACTIVE"
}
```

## Running Tests

```bash
./gradlew test
```

Tests include:
- **Unit Tests** — Service layer tested in isolation with mocked dependencies (Mockito)
- **Integration Tests** — Full Spring context with H2 in-memory database

## Project Structure

```
src/
├── main/java/library_management_system/
│   ├── controller/          # REST controllers
│   ├── dto/
│   │   ├── request/         # Request DTOs with validation
│   │   └── response/        # Response DTOs
│   ├── entity/              # JPA entities
│   ├── mapper/              # MapStruct mappers
│   ├── repository/          # Spring Data JPA repositories
│   └── service/
│       └── impl/            # Service implementations
├── main/resources/
│   └── application.yaml     # Application configuration
├── test/java/               # Unit & integration tests
└── test/resources/
    └── application-test.yaml # Test configuration (H2)
```
