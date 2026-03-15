# UAT Test Scenarios – Librarian Assistant

**System under test**: Librarian Assistant Backend API (`http://localhost:8081`)  
**Date**: 2026-03-14  
**Tester role**: QA / Product Owner  

---

## Prerequisites

- Application running (`./gradlew bootRun` or `docker compose up`)
- PostgreSQL accessible at port 5434
- REST client (Postman or Swagger UI at `http://localhost:8081/swagger-ui.html`)
- Default seeded accounts available (see README)

---

## FR-1: User Registration & Authentication

### UAT-001 – Successful registration

| | |
|---|---|
| **Precondition** | No user with email `newuser@test.com` exists |
| **Steps** | POST `/api/auth/register` with `{"name":"Test User","email":"newuser@test.com","password":"pass1234"}` |
| **Expected** | HTTP 200; response contains `token` (JWT string) and `email` = `newuser@test.com` |

### UAT-002 – Duplicate email rejected

| | |
|---|---|
| **Precondition** | User `newuser@test.com` exists (from UAT-001) |
| **Steps** | POST `/api/auth/register` with same email |
| **Expected** | HTTP 409; body contains `"Email already in use"` |

### UAT-003 – Successful login

| | |
|---|---|
| **Precondition** | User registered with known credentials |
| **Steps** | POST `/api/auth/login` with `{"email":"librarian@library.com","password":"password"}` |
| **Expected** | HTTP 200; `token` field is non-empty JWT |

### UAT-004 – Invalid credentials rejected

| | |
|---|---|
| **Precondition** | User exists |
| **Steps** | POST `/api/auth/login` with wrong password |
| **Expected** | HTTP 401 |

### UAT-005 – Protected endpoint requires token

| | |
|---|---|
| **Precondition** | — |
| **Steps** | GET `/api/books` without `Authorization` header |
| **Expected** | HTTP 401 |

---

## FR-2: Book Catalog CRUD & Search

### UAT-010 – Add a book (LIBRARIAN)

| | |
|---|---|
| **Precondition** | Authenticated as LIBRARIAN |
| **Steps** | POST `/api/books` with `{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","genre":"Technology","totalCopies":3}` |
| **Expected** | HTTP 201; response contains book `id` and `availableCopies` = 3 |

### UAT-011 – Add book as PATRON is forbidden

| | |
|---|---|
| **Precondition** | Authenticated as PATRON |
| **Steps** | POST `/api/books` with valid book body |
| **Expected** | HTTP 403 |

### UAT-012 – Search books

| | |
|---|---|
| **Precondition** | At least one book with title containing "Clean" exists |
| **Steps** | GET `/api/books/search?query=Clean` |
| **Expected** | HTTP 200; array contains the book; search works case-insensitively |

### UAT-013 – Update book

| | |
|---|---|
| **Precondition** | Book with `id=1` exists; authenticated as LIBRARIAN |
| **Steps** | PUT `/api/books/1` updating `totalCopies` to 5 |
| **Expected** | HTTP 200; `totalCopies` = 5 in response |

### UAT-014 – Delete book

| | |
|---|---|
| **Precondition** | Book with `id=1` exists, no active checkouts; authenticated as LIBRARIAN |
| **Steps** | DELETE `/api/books/1` |
| **Expected** | HTTP 204; subsequent GET `/api/books/1` returns 404 |

---

## FR-3: Hold Management

### UAT-020 – Place a hold on unavailable book

| | |
|---|---|
| **Precondition** | Book has 0 available copies; authenticated as PATRON |
| **Steps** | POST `/api/holds` with `{"userId":2,"bookId":1}` |
| **Expected** | HTTP 200; `status` = `PENDING`; `queuePosition` = 1 |

### UAT-021 – Duplicate hold rejected

| | |
|---|---|
| **Precondition** | Active PENDING hold exists for user+book |
| **Steps** | POST `/api/holds` with same `userId`/`bookId` |
| **Expected** | HTTP 400; `"already has an active hold"` |

### UAT-022 – Cancel a hold

| | |
|---|---|
| **Precondition** | Hold with `id=1` in PENDING status; authenticated as hold owner |
| **Steps** | DELETE `/api/holds/1` |
| **Expected** | HTTP 204 (or 200); hold `status` = `CANCELLED` |

### UAT-023 – Hold notified on book return

| | |
|---|---|
| **Precondition** | Book has PENDING hold; book is currently checked out |
| **Steps** | POST `/api/checkouts/{id}/return` for the active checkout |
| **Expected** | Hold `status` changes to `NOTIFIED` |

---

## FR-4 & FR-5: Checkout & Return

### UAT-030 – Successful checkout

| | |
|---|---|
| **Precondition** | Book has available copies ≥ 1; authenticated as LIBRARIAN |
| **Steps** | POST `/api/checkouts` with `{"userId":2,"bookId":1}` |
| **Expected** | HTTP 200; `status` = `ACTIVE`; `dueDate` = today + 14 days; book `availableCopies` decremented |

### UAT-031 – Checkout unavailable book rejected

| | |
|---|---|
| **Precondition** | Book has 0 available copies |
| **Steps** | POST `/api/checkouts` for that book |
| **Expected** | HTTP 400; `"No available copies"` |

### UAT-032 – Renew checkout

| | |
|---|---|
| **Precondition** | Active checkout with `renewalCount` < 2 |
| **Steps** | POST `/api/checkouts/{id}/renew` |
| **Expected** | HTTP 200; `dueDate` extended by 14 days; `renewalCount` incremented |

### UAT-033 – Max renewals enforced

| | |
|---|---|
| **Precondition** | Active checkout with `renewalCount` = 2 |
| **Steps** | POST `/api/checkouts/{id}/renew` |
| **Expected** | HTTP 400; `"Maximum renewals"` |

### UAT-034 – Return on time (no fine)

| | |
|---|---|
| **Precondition** | Active checkout with `dueDate` in the future |
| **Steps** | POST `/api/checkouts/{id}/return` |
| **Expected** | HTTP 200; `status` = `RETURNED`; `fineAmount` is null or 0 |

### UAT-035 – Overdue return generates fine

| | |
|---|---|
| **Precondition** | Active checkout with `dueDate` in the past |
| **Steps** | POST `/api/checkouts/{id}/return` |
| **Expected** | HTTP 200; `fineAmount` = `daysOverdue × 0.25`; fine record created for user |

---

## FR-5 (continued): Fines

### UAT-040 – Pay fine

| | |
|---|---|
| **Precondition** | Fine with `id=1` in UNPAID status |
| **Steps** | POST `/api/fines/1/pay` |
| **Expected** | HTTP 200; `status` = `PAID`; `paidDate` = today |

### UAT-041 – Waive fine (LIBRARIAN only)

| | |
|---|---|
| **Precondition** | Fine with `id=2` in UNPAID status; authenticated as LIBRARIAN |
| **Steps** | POST `/api/fines/2/waive` |
| **Expected** | HTTP 200; `status` = `WAIVED` |

### UAT-042 – Waive fine as PATRON forbidden

| | |
|---|---|
| **Precondition** | Authenticated as PATRON |
| **Steps** | POST `/api/fines/2/waive` |
| **Expected** | HTTP 403 |

---

## FR-6: Reporting & Analytics

### UAT-050 – Circulation report (LIBRARIAN)

| | |
|---|---|
| **Precondition** | Authenticated as LIBRARIAN; at least one checkout exists |
| **Steps** | GET `/api/reports/circulation?from=2026-01-01&to=2026-12-31` |
| **Expected** | HTTP 200; response contains `totalCheckouts`, `activeCheckouts`, `overdueCheckouts`, `totalReturns` |

### UAT-051 – Circulation report as PATRON forbidden

| | |
|---|---|
| **Precondition** | Authenticated as PATRON |
| **Steps** | GET `/api/reports/circulation?from=2026-01-01&to=2026-12-31` |
| **Expected** | HTTP 403 |

### UAT-052 – Overdue items report

| | |
|---|---|
| **Precondition** | At least one overdue checkout exists; authenticated as LIBRARIAN |
| **Steps** | GET `/api/reports/overdue` |
| **Expected** | HTTP 200; array contains items with `daysOverdue` > 0 and `accruedFine` = `daysOverdue × 0.25` |

### UAT-053 – Popular books report

| | |
|---|---|
| **Precondition** | Multiple books with different checkout counts; authenticated as LIBRARIAN |
| **Steps** | GET `/api/reports/popular-books?limit=5` |
| **Expected** | HTTP 200; array of ≤ 5 books ordered by `checkoutCount` descending |

---

## NFR Tests

### UAT-060 – Response time

| | |
|---|---|
| **Precondition** | Application running normally |
| **Steps** | Measure response time for `GET /api/books` 10 times |
| **Expected** | All responses < 2 seconds; p95 < 500 ms |

### UAT-061 – Swagger UI accessible

| | |
|---|---|
| **Precondition** | Application running |
| **Steps** | Open `http://localhost:8081/swagger-ui.html` in browser |
| **Expected** | Swagger UI loads; all endpoint groups visible |

---

## Test Execution Checklist

- [ ] UAT-001 through UAT-005 (Auth)
- [ ] UAT-010 through UAT-014 (Books)
- [ ] UAT-020 through UAT-023 (Holds)
- [ ] UAT-030 through UAT-035 (Checkout/Return)
- [ ] UAT-040 through UAT-042 (Fines)
- [ ] UAT-050 through UAT-053 (Reports)
- [ ] UAT-060 through UAT-061 (NFR)

**Sign-off**: All scenarios passed before marking milestone complete.
