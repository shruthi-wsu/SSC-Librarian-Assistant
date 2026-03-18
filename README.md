# Librarian Assistant

A full-stack library management system built for WSU's Software Quality course. Librarians can manage the book catalog, register patrons, handle checkouts and returns, place holds, track fines, and view reports — all through a web interface backed by a REST API.

**Team:** Shruthi Mallesh · Sanjeev Sreekumar Krishnan · Chenhua Fan

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [System Requirements](#system-requirements)
- [Project Structure](#project-structure)
- [Quick Start (Docker)](#quick-start-docker)
- [Local Development Setup](#local-development-setup)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Default Accounts](#default-accounts)
- [API Documentation](#api-documentation)
- [Running Tests](#running-tests)
- [Architecture Overview](#architecture-overview)

---

## Project Documentation

This is a **Software Quality course project**. The full planning document — covering
functional requirements, non-functional requirements, quality goals (ISO/IEC 25010),
testing strategies, cost of quality estimation, and project timeline — is in:

- [`docs/deliverable-1-project-plan.md`](docs/deliverable-1-project-plan.md) — Deliverable I: Requirements, Architecture & QA Strategy

---

## Features

| Feature | Description |
|---|---|
| Authentication | JWT-based login with role-based access (Librarian / Patron) |
| Book Catalog | Add, edit, search, and browse books |
| Checkouts & Returns | Issue and return books with due date tracking |
| Holds | Place and manage holds on unavailable books |
| Fines | Automatic fine calculation for overdue items |
| User Management | Register and manage patron accounts |
| Reports | Dashboard with circulation statistics |
| API Docs | Interactive Swagger UI at `/swagger-ui.html` |

---

## Tech Stack

**Backend**
- Java 21 · Spring Boot 4.0.1
- Spring Security (JWT) · Spring Data JPA · Hibernate
- PostgreSQL 16 (production) · H2 (tests)
- Gradle · JaCoCo · SpringDoc/OpenAPI

**Frontend**
- React 19 · TypeScript · Vite 7
- React Router v7 · TanStack Query · Axios
- React Hook Form · Zod · Tailwind CSS

---

## System Requirements

| Requirement | Minimum Version |
|---|---|
| Java | 21 |
| Node.js | 18 |
| npm | 9 |
| Docker + Docker Compose | 24 (for the Docker path) |
| PostgreSQL | 16 (only needed for local non-Docker setup) |

---

## Project Structure

```
.
├── Librarian Assistant/        # Spring Boot backend
│   ├── src/
│   │   ├── main/java/com/example/librarianassistant/
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Spring Data JPA
│   │   │   ├── model/          # JPA entities
│   │   │   ├── dto/            # Request/Response objects
│   │   │   ├── security/       # JWT filter, config
│   │   │   ├── exception/      # Global error handling
│   │   │   └── config/         # DataSeeder, OpenAPI config
│   │   └── test/               # JUnit 5 integration tests (H2)
│   ├── docs/                   # Postman collection
│   ├── docker-compose.yml      # PostgreSQL + app containers
│   └── Dockerfile
├── library-assistant-frontend/ # React frontend
│   ├── src/
│   │   ├── features/           # Feature modules (auth, books, checkouts, etc.)
│   │   ├── components/         # Shared UI components
│   │   ├── context/            # Auth state (AuthContext)
│   │   ├── api/                # Axios client
│   │   └── App.tsx             # Routes and protected route wrappers
│   └── .env.example
└── docs/
    └── qa-plan.md              # Quality Assurance Plan (Deliverable I)
```

---

## Quick Start (Docker)

The easiest way to run the full system. This starts PostgreSQL and the Spring Boot backend together.

```bash
# 1. Clone the repository
git clone <repo-url>
cd "Librarian Assistant"

# 2. Start the backend + database
docker-compose up --build

# Backend is now running at http://localhost:8081
```

Then start the frontend separately (Docker does not include it):

```bash
cd ../library-assistant-frontend
cp .env.example .env
npm install
npm run dev

# Frontend is now running at http://localhost:5173
```

Open [http://localhost:5173](http://localhost:5173) and log in with one of the [default accounts](#default-accounts).

---

## Local Development Setup

### Backend

**Prerequisites:** Java 21, PostgreSQL 16 running locally on port 5434

```bash
cd "Librarian Assistant"
```

**Option A — Use Docker just for the database:**

```bash
docker-compose up db     # starts only PostgreSQL, not the app container
./gradlew bootRun        # starts Spring Boot on port 8081
```

**Option B — Provide your own PostgreSQL:**

Edit `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:postgresql://localhost:<your-port>/librarydb
spring.datasource.username=<your-user>
spring.datasource.password=<your-password>
```

Then run:

```bash
./gradlew bootRun
```

The database schema is created automatically on first startup (`spring.jpa.hibernate.ddl-auto=create`). Sample books and users are seeded automatically on first run by `DataSeeder`.

### Frontend

**Prerequisites:** Node.js 18+

```bash
cd library-assistant-frontend

# Copy env file and point it at the backend
cp .env.example .env
# Default .env content: VITE_API_URL=http://localhost:8081
# Change this if your backend is on a different port.

npm install
npm run dev
```

The dev server starts at [http://localhost:5173](http://localhost:5173) and proxies all `/api` requests to `VITE_API_URL`.

---

## Default Accounts

These accounts are seeded automatically on first startup.

| Role | Email | Password |
|---|---|---|
| Librarian | `admin@library.com` | `admin123` |
| Patron | `jane@library.com` | `patron123` |

**Librarians** can access the full admin interface: book management, user management, checkouts, holds, fines, and reports.

**Patrons** have a limited view: browse the catalog, view their own checkouts and holds.

---

## API Documentation

With the backend running, open:

- **Swagger UI:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

A Postman collection is also available at `Librarian Assistant/docs/LibrarianAssistant.postman_collection.json`.

**Authentication:** All endpoints (except `/api/auth/**`) require a Bearer token. Get one by calling `POST /api/auth/login` with valid credentials, then include the token as `Authorization: Bearer <token>` in subsequent requests.

---

## Running Tests

Backend tests use JUnit 5 with an H2 in-memory database — no running PostgreSQL needed.

```bash
cd "Librarian Assistant"

# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests BookServiceTest

# Run tests and generate HTML coverage report
./gradlew test jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html

# Verify the 80% coverage threshold is met
./gradlew jacocoTestCoverageVerification
```

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         Browser                                  │
│              React 19 SPA (Vite · Tailwind CSS)                 │
│   AuthContext → TanStack Query → Axios → /api/* (proxied)       │
└──────────────────────────┬──────────────────────────────────────┘
                           │ HTTP/JSON  (JWT in Authorization header)
┌──────────────────────────▼──────────────────────────────────────┐
│                    Spring Boot 4.0.1 (port 8081)                │
│                                                                  │
│  JwtAuthFilter → SecurityConfig                                  │
│                                                                  │
│  Controllers  (AuthController, BookController, UserController,  │
│                CheckoutController, HoldController,               │
│                FineController, ReportController, …)              │
│       ↓                                                          │
│  Services     (BookService, UserService, CirculationService,    │
│                HoldService, FineService, ReportService)          │
│       ↓                                                          │
│  Repositories (Spring Data JPA)                                  │
│       ↓                                                          │
│  PostgreSQL 16  (port 5434)                                     │
└─────────────────────────────────────────────────────────────────┘
```

**Key design decisions:**
- **Layered architecture** — Controllers only handle HTTP; Services hold all business logic; Repositories handle data access. This keeps each layer testable in isolation.
- **JWT stateless auth** — Tokens are issued at login and validated on every request by `JwtAuthFilter`. No server-side session state.
- **Role-based access** — `LIBRARIAN` role gets full admin access; `PATRON` role is scoped to self-service operations. Enforced in `SecurityConfig` and individual controller methods.
- **H2 for tests** — Integration tests run against an in-memory H2 database using `@SpringBootTest`, so no external database setup is needed for CI or local testing.
- **DataSeeder** — Seeds demo users and books on first startup so the system is usable immediately after installation without any manual setup.
