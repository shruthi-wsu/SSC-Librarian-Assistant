# Testing Strategy — Librarian Assistant

This document describes the test pyramid for the project, coverage goals, how to run each test layer, and how they integrate with CI/CD.

---

## Test Pyramid

```
          ┌─────────┐
          │   UAT   │  ← Manual / Postman (docs/UAT_TestScenarios.md)
         ╱           ╲
        ╱  Load Tests  ╲  ← JMeter (load-tests/)
       ╱                ╲
      ╱  Integration Tests ╲  ← @SpringBootTest + H2 (5 suites)
     ╱                      ╲
    ╱     Unit Tests          ╲  ← @SpringBootTest + H2 (5 service suites)
   └────────────────────────────┘
```

---

## Layer 1 — Unit Tests (Service Layer)

**Location:** `src/test/java/com/example/librarianassistant/service/`

**Technology:** JUnit 5 + Spring Boot Test + H2 (real database, not mocks)

**Test files:**

| File | Covers |
|---|---|
| `BookServiceTest` | CRUD, ISBN uniqueness, search |
| `CirculationServiceTest` | Checkout, return, renewal, fine generation |
| `FineServiceTest` | Pay, waive, unpaid totals |
| `HoldServiceTest` | Hold placement, cancellation, queue advancement |
| `UserServiceTest` | Registration, login, status updates |

**Run:**
```bash
./gradlew test --tests "*.service.*"
```

**Coverage goal:** ≥ 90% line coverage for all service classes.

---

## Layer 2 — Integration Tests (API Layer)

**Location:** `src/test/java/com/example/librarianassistant/integration/`

**Technology:** `@SpringBootTest(webEnvironment = MOCK)` + MockMvc + Spring Security + H2

**Pattern used:**
- `@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)` resets the H2 database before each test
- JWT tokens obtained by calling `UserService.register()` directly in `@BeforeEach`
- All HTTP calls go through MockMvc with `SecurityMockMvcConfigurers.springSecurity()` applied
- Tests exercise the full stack: Controller → Service → Repository → H2

**Test files:**

| File | Covers |
|---|---|
| `AuthIntegrationTest` | Registration, login, duplicate email |
| `BookIntegrationTest` | CRUD, search, role-based access (403 for PATRONs) |
| `CheckoutIntegrationTest` | Checkout, return, renewal, overdue retrieval |
| `FineIntegrationTest` | Fine generation on overdue return, pay, waive |
| `HoldIntegrationTest` | Hold placement, cancellation, queue advancement |
| `ReportIntegrationTest` | Circulation, overdue, popular-books (librarian-only) |
| `UserIntegrationTest` | User listing, profile, status update |

**Run:**
```bash
./gradlew test --tests "*.integration.*"
```

**Coverage goal:** ≥ 80% overall (enforced by JaCoCo).

---

## Layer 3 — Load Tests

**Location:** `load-tests/`

**Technology:** Apache JMeter 5.6+

**Test plans:**

| File | Scenario |
|---|---|
| `librarian-checkout-flow.jmx` | Login → checkout → return (50 concurrent users, p95 < 500 ms) |

**NFR-1 acceptance criteria:**
- 95th percentile response time ≤ 2 seconds
- 99th percentile response time ≤ 5 seconds
- System stable under 10 concurrent users for 2+ hours
- No memory leaks during endurance testing

**Run (CLI mode):**
```bash
jmeter -n \
  -t load-tests/librarian-checkout-flow.jmx \
  -l load-tests/results/results.jtl \
  -e -o load-tests/results/report/
```

See `load-tests/README.md` for full instructions.

---

## Layer 4 — User Acceptance Testing (UAT)

**Location:** `docs/UAT_TestScenarios.md`

**Technology:** Manual testing via Postman or Swagger UI

**Covers all functional requirements:**
- FR-1: Authentication (register, login)
- FR-2: Book catalog (search, CRUD)
- FR-3: Holds (place, cancel, queue)
- FR-4: Checkout and renewal
- FR-5: Return and fine calculation
- FR-6: Reports (circulation, overdue, popular books)

**Prerequisites:**
- App running (`docker-compose up` or `./gradlew bootRun`)
- Seeded accounts available (see README default accounts section)

---

## Coverage Configuration

JaCoCo is configured in `build.gradle`:

```gradle
jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = 0.80   // 80% line coverage required
            }
        }
    }
}
```

**Exclusions** (not counted toward coverage):
- `**/model/**` — JPA entities
- `**/dto/**` — data transfer objects
- `LibrarianAssistantApplication.class` — bootstrap only

**Run coverage check:**
```bash
./gradlew test jacocoTestReport jacocoTestCoverageVerification
# HTML report: Librarian Assistant/build/reports/jacoco/test/html/index.html
# XML report:  Librarian Assistant/build/reports/jacoco/test/jacocoTestReport.xml
```

---

## CI/CD Integration

Tests run automatically on every push via GitHub Actions (`.github/workflows/`). The pipeline:

1. Compiles the project (`./gradlew build`)
2. Runs all tests (`./gradlew test`)
3. Generates JaCoCo XML report
4. Verifies 80% coverage threshold (`./gradlew jacocoTestCoverageVerification`)
5. Runs SonarQube analysis (`./gradlew sonarqube`) — requires `SONAR_TOKEN` secret

A PR cannot be merged if any step fails.
