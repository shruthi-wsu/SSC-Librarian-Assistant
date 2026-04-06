# UAT User Guide — Librarian Assistant

This guide is for **non-technical testers** (product owners, stakeholders) performing user acceptance testing on the Librarian Assistant web application. No coding is required.

---

## Before You Start

### 1. Access the application

| Interface | URL | Use for |
|---|---|---|
| Web UI (frontend) | http://localhost:5173 | All user-facing flows |
| Swagger UI (API docs) | http://localhost:8081/swagger-ui.html | API-level verification |

> If you are testing a deployed environment, substitute `localhost` with the provided server address.

### 2. Default test accounts

The application seeds two accounts on startup:

| Role | Email | Password |
|---|---|---|
| Librarian | `librarian@library.com` | `password` |
| Patron | `patron@library.com` | `password` |

Use the **Librarian** account for admin operations (add books, view reports, manage users). Use the **Patron** account to simulate a regular library member.

### 3. How to report a defect

If a scenario does not match the expected result:

1. Note the scenario ID (e.g., `UG-003`)
2. Capture a screenshot or copy the error message
3. Open a new issue at the project repository with label `bug` and title: `[UAT] UG-003 – <short description>`

---

## Scenario Checklist

Mark each scenario **Pass / Fail / Blocked** in the final column.

---

### Module 1 — Authentication

#### UG-001 — Log in as Librarian

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Open http://localhost:5173 | Login page is displayed | |
| 2 | Enter email `librarian@library.com` and password `password`, click **Log In** | Dashboard page loads; sidebar shows Librarian menu items (Books, Users, Reports) | |
| 3 | Refresh the page | User remains logged in (token persisted) | |

#### UG-002 — Log in as Patron

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Open http://localhost:5173 | Login page is displayed | |
| 2 | Enter `patron@library.com` / `password`, click **Log In** | Dashboard loads; sidebar shows Patron menu items (My Checkouts, My Holds, My Fines) | |

#### UG-003 — Register a new patron account

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | On the login page, click **Register** | Registration form is displayed | |
| 2 | Fill in name, a new email address, and a password of at least 6 characters | Form validation passes (no inline errors) | |
| 3 | Click **Register** | User is logged in and redirected to the Patron dashboard | |

#### UG-004 — Wrong password rejected

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Enter a valid email with an incorrect password, click **Log In** | Error message is shown (e.g., "Invalid credentials"); user stays on login page | |

---

### Module 2 — Book Catalog

#### UG-005 — Browse all books

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Patron | Dashboard visible | |
| 2 | Click **Catalog** in the sidebar | A grid of book cards is displayed | |
| 3 | Observe a book card | Shows title, author, and availability status | |

#### UG-006 — Search for a book

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | On the Catalog page, type a partial title (e.g., `Lord`) in the search box | Results filter to matching books in real time | |
| 2 | Clear the search box | All books are shown again | |
| 3 | Search for a term with no matches (e.g., `zzz9999`) | "No results found" message is displayed | |

#### UG-007 — Add a new book (Librarian only)

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Librarian | Dashboard visible | |
| 2 | Click **Books** → **Add Book** | Book creation form is displayed | |
| 3 | Fill in all required fields (ISBN, Title, Author, Copies) and click **Save** | New book appears in the catalog; success notification shown | |
| 4 | Log in as Patron and try to access the Add Book page directly | Access denied / page not found | |

#### UG-008 — Edit a book (Librarian only)

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Librarian, navigate to the Catalog | Book list visible | |
| 2 | Click **Edit** on any book | Edit form pre-filled with existing values | |
| 3 | Change the title and click **Save** | Updated title appears in the catalog | |

---

### Module 3 — Checkout & Returns

#### UG-009 — Check out a book

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Librarian, navigate to **Checkout** | Checkout form visible | |
| 2 | Enter a patron's user ID and a book ID with available copies, click **Check Out** | Checkout created; book availability decremented by 1 | |
| 3 | Navigate to the book's detail page | Available copies count has decreased | |

#### UG-010 — View my checkouts (Patron)

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Patron | Dashboard visible | |
| 2 | Click **My Checkouts** | List of active checkouts with due dates shown | |

#### UG-011 — Return a book

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Librarian, navigate to **Returns** | Returns page visible | |
| 2 | Enter an active checkout ID and click **Return** | Checkout status changes to RETURNED; book availability restored | |

#### UG-012 — Renew a checkout

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Patron, navigate to **My Checkouts** | Active checkout listed | |
| 2 | Click **Renew** on a checkout that has not reached the renewal limit | Due date extended by 14 days; renewal count incremented | |
| 3 | Attempt to renew the same item after reaching the 2-renewal limit | Error message: renewal limit reached | |

---

### Module 4 — Holds

#### UG-013 — Place a hold on an unavailable book

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Patron | Dashboard visible | |
| 2 | Browse to a book with 0 available copies | Book detail shows **Place Hold** button | |
| 3 | Click **Place Hold** | Confirmation shown; hold appears in **My Holds** with queue position | |

#### UG-014 — Cancel a hold

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Patron, navigate to **My Holds** | Active hold listed | |
| 2 | Click **Cancel Hold** | Hold removed from the list | |

---

### Module 5 — Fines

#### UG-015 — View my fines (Patron)

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Patron, click **My Fines** | List of fines shown (may be empty for a new account) | |

#### UG-016 — Pay a fine (Patron)

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Navigate to **My Fines** with at least one UNPAID fine | Fine listed with amount and status UNPAID | |
| 2 | Click **Pay** on the fine | Fine status changes to PAID; total unpaid amount updates | |

#### UG-017 — Waive a fine (Librarian only)

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Librarian, navigate to the patron's fine list | Fine visible | |
| 2 | Click **Waive** | Fine status changes to WAIVED | |
| 3 | Log in as Patron and attempt to waive a fine | Waive button not present or action is forbidden | |

---

### Module 6 — Reports (Librarian only)

#### UG-018 — Circulation report

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Librarian, click **Reports** | Report dashboard visible | |
| 2 | Set a date range covering today and click **Run** | Report shows total checkouts, active checkouts, overdue count, and total returns | |

#### UG-019 — Overdue items report

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | On the Reports page, view the Overdue Items section | Table listing patron name, book title, due date, days overdue, and accrued fine | |

#### UG-020 — Popular books report

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | On the Reports page, view Popular Books | Top books ranked by checkout count | |

---

### Module 7 — User Management (Librarian only)

#### UG-021 — View all patrons

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | Log in as Librarian, click **Users** | List of all registered patrons | |

#### UG-022 — Suspend a patron account

| Step | Action | Expected Result | Result |
|---|---|---|---|
| 1 | On the Users page, click **Suspend** next to a patron | Patron status changes to SUSPENDED | |
| 2 | Attempt to log in as that patron | Login fails with an appropriate error message | |

---

## Sign-Off

| UAT Cycle | Date | Tester | Outcome |
|---|---|---|---|
| Cycle 1 | | | |
| Cycle 2 | | | |

**All scenarios must pass before production release.**
