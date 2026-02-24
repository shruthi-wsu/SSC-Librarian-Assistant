#!/usr/bin/env python3
"""
Generate Deliverable II PDF – Librarian Assistant (CptS 583)
Design Modeling & Implementation Document
"""

import base64
import os
import sys
import subprocess

def install_package(package):
    subprocess.check_call([sys.executable, "-m", "pip", "install", "-q", package])

def image_to_base64(path):
    if not os.path.exists(path):
        print(f"  WARNING: image not found: {path}")
        return None
    with open(path, "rb") as f:
        return base64.b64encode(f.read()).decode("utf-8")

def img_tag(path, alt="", width="100%"):
    data = image_to_base64(path)
    if data is None:
        return f'<p style="color:red;">[Image not found: {path}]</p>'
    ext = os.path.splitext(path)[1].lower().lstrip(".")
    mime = {"png": "image/png", "jpg": "image/jpeg", "jpeg": "image/jpeg"}.get(ext, "image/png")
    return f'<img src="data:{mime};base64,{data}" alt="{alt}" style="width:{width}; max-width:100%; display:block; margin:12px auto;" />'

def build_html(diagrams_dir):
    arch   = img_tag(os.path.join(diagrams_dir, "architecture-overview.png"),         "Architecture Overview Diagram")
    comp1  = img_tag(os.path.join(diagrams_dir, "Component_user_management.drawio.png"), "UserManagement Component Diagram")
    comp2  = img_tag(os.path.join(diagrams_dir, "Book_catalog.drawio.png"),            "BookCatalog Component Diagram")
    comp3  = img_tag(os.path.join(diagrams_dir, "Circulation.drawio.png"),             "CirculationManagement Component Diagram")
    comp4  = img_tag(os.path.join(diagrams_dir, "holdManagement.drawio.png"),          "HoldManagement Component Diagram")
    comp5  = img_tag(os.path.join(diagrams_dir, "frontend.drawio.png"),               "Frontend UI Component Diagram")

    html = f"""<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"/>
<title>Librarian Assistant – Deliverable II</title>
<style>
  @page {{
    size: letter;
    margin: 1in;
    @bottom-right {{
      content: "Page " counter(page) " of " counter(pages);
      font-size: 9pt;
      color: #666;
    }}
  }}
  body {{
    font-family: 'Georgia', 'Times New Roman', serif;
    font-size: 11pt;
    line-height: 1.65;
    color: #222;
  }}
  /* ── Title page ─────────────────────────────────── */
  .title-page {{
    text-align: center;
    padding-top: 160px;
    page-break-after: always;
  }}
  .title-page h1 {{
    font-size: 28pt;
    color: #1a237e;
    border: none;
    margin-bottom: 6px;
  }}
  .title-page .subtitle {{
    font-size: 16pt;
    color: #3949ab;
    margin: 8px 0 4px;
  }}
  .title-page .deliverable {{
    font-size: 14pt;
    color: #555;
    margin-bottom: 50px;
  }}
  .title-page .meta {{
    font-size: 12pt;
    color: #444;
    line-height: 2;
  }}
  .title-page .meta strong {{
    color: #1a237e;
  }}
  /* ── Section headings ────────────────────────────── */
  h1 {{
    font-size: 20pt;
    color: #1a237e;
    border-bottom: 3px solid #3949ab;
    padding-bottom: 6px;
    margin-top: 36px;
    page-break-after: avoid;
  }}
  h2 {{
    font-size: 15pt;
    color: #283593;
    border-bottom: 1px solid #c5cae9;
    padding-bottom: 4px;
    margin-top: 26px;
    page-break-after: avoid;
  }}
  h3 {{
    font-size: 12pt;
    color: #1a237e;
    margin-top: 18px;
    page-break-after: avoid;
  }}
  /* ── Body text ───────────────────────────────────── */
  p {{
    margin: 8px 0;
    text-align: justify;
  }}
  ul, ol {{
    margin: 8px 0;
    padding-left: 28px;
  }}
  li {{
    margin: 4px 0;
  }}
  /* ── Tables ──────────────────────────────────────── */
  table {{
    border-collapse: collapse;
    width: 100%;
    margin: 14px 0;
    font-size: 10pt;
    page-break-inside: avoid;
  }}
  th {{
    background-color: #1a237e;
    color: #fff;
    padding: 7px 10px;
    text-align: left;
    font-weight: bold;
  }}
  td {{
    border: 1px solid #c5cae9;
    padding: 6px 10px;
  }}
  tr:nth-child(even) td {{
    background-color: #e8eaf6;
  }}
  /* ── Diagram caption ─────────────────────────────── */
  .caption {{
    text-align: center;
    font-style: italic;
    font-size: 10pt;
    color: #555;
    margin: 4px 0 20px;
  }}
  /* ── Component box ───────────────────────────────── */
  .component-box {{
    border: 1px solid #c5cae9;
    border-left: 5px solid #3949ab;
    background: #f8f9ff;
    padding: 12px 16px;
    margin: 14px 0;
    page-break-inside: avoid;
  }}
  .component-box h3 {{
    margin-top: 0;
    color: #1a237e;
  }}
  /* ── Code / inline ───────────────────────────────── */
  code {{
    font-family: 'Courier New', monospace;
    font-size: 9.5pt;
    background: #e8eaf6;
    padding: 1px 5px;
    border-radius: 3px;
  }}
  /* ── Page break ──────────────────────────────────── */
  .pagebreak {{ page-break-before: always; }}
  hr {{
    border: none;
    border-top: 1px solid #c5cae9;
    margin: 20px 0;
  }}
  .badge {{
    display: inline-block;
    background: #3949ab;
    color: #fff;
    border-radius: 10px;
    font-size: 9pt;
    padding: 2px 8px;
    margin: 0 2px;
  }}
</style>
</head>
<body>

<!-- ══════════════════════════════════════════════════════════════════
     TITLE PAGE
     ══════════════════════════════════════════════════════════════════ -->
<div class="title-page">
  <h1>Librarian Assistant</h1>
  <p class="subtitle">Design Modeling &amp; Implementation</p>
  <p class="deliverable">Project Deliverable II – Part 1</p>
  <div class="meta">
    <p><strong>Team Members</strong></p>
    <p>Shruthi Mallesh &nbsp;|&nbsp; Sanjeev Sreekumar Krishnan &nbsp;|&nbsp; Chenhua Fan</p>
    <br/>
    <p><strong>Course:</strong> CptS 583 – Software Quality</p>
    <p><strong>Institution:</strong> Washington State University</p>
    <p><strong>Semester:</strong> Spring 2026</p>
    <p><strong>Instructor:</strong> Parteek Kumar</p>
    <br/>
    <p><strong>Backend Repository:</strong> github.com/shruthi-wsu/SSC-Librarian-Assistant</p>
    <p><strong>Frontend Repository:</strong> github.com/sanjeevkrishnan02/library-assistant-frontend</p>
    <p><strong>Date:</strong> February 2026</p>
  </div>
</div>


<!-- ══════════════════════════════════════════════════════════════════
     SECTION 1 – ARCHITECTURAL DESIGN
     ══════════════════════════════════════════════════════════════════ -->
<h1>1. Architectural Design</h1>

<h2>1.1 Architectural Pattern</h2>

<p>
  The Librarian Assistant system employs a <strong>Client-Server + Layered Architecture (n-tier) with
  Model-View-Controller (MVC) within the backend tier</strong>. This combined pattern was selected because
  it directly maps to the full-stack nature of the project: a React Single-Page Application (SPA)
  acting as the client communicates over REST/JSON with a Spring Boot server that internally enforces
  strict layer separation.
</p>

<table>
  <tr><th>Pattern Element</th><th>Realization in This System</th></tr>
  <tr><td><strong>Client-Server</strong></td><td>React SPA (browser) ↔ REST/JSON ↔ Spring Boot backend on port 8081</td></tr>
  <tr><td><strong>Layered / n-Tier</strong></td><td>Presentation → Business Logic → Data Access → Database (4 distinct layers)</td></tr>
  <tr><td><strong>MVC within Backend</strong></td><td>REST Controllers (View/Presentation) → Service classes (Controller/Business) → JPA Repositories (Model/Data)</td></tr>
</table>

<h2>1.2 High-Level Architecture Diagram</h2>

{arch}
<p class="caption">Figure 1 – High-Level Architecture: React SPA ↔ Spring Boot Layers ↔ PostgreSQL</p>

<h2>1.3 Pattern Justification</h2>

<p>
  <strong>Why Client-Server?</strong> Separating the React frontend from the Spring Boot backend
  allows each tier to be developed, tested, and deployed independently. The frontend team (Sanjeev)
  can work against mock API responses while the backend (Shruthi) exposes a stable REST contract.
  This separation directly supports <em>QG-6: Maintainability</em> and <em>QG-7: Compatibility</em>
  by enabling the UI to evolve without touching business logic.
</p>

<p>
  <strong>Why Layered Architecture within the Backend?</strong> The layered model enforces a
  strict one-way dependency flow: Controllers depend on Services; Services depend on Repositories;
  Repositories depend only on JPA/DB. No layer can skip another. This produces high cohesion
  within each layer and controlled coupling between them — the primary design goals cited in the
  deliverable specification. It also enables focused unit testing at each layer, supporting
  <em>QG-6: Maintainability</em> (test coverage ≥ 80% target).
</p>

<p>
  <strong>Why not Microservices?</strong> The team consists of three members with a fixed
  academic deadline. Microservices would introduce cross-service communication overhead, distributed
  transactions, and infrastructure complexity (service registry, API gateway) that would consume
  time better spent on feature quality. A well-structured monolith with clear module boundaries
  achieves the same separation of concerns at this scale.
</p>

<p>
  <strong>Why not Server-Side Rendering (e.g., Thymeleaf)?</strong> A React SPA delivers a
  richer, more responsive user experience that satisfies <em>QG-3: Usability</em> (SUS ≥ 75,
  checkout task &lt; 30 seconds). Server-side rendering would tightly couple the UI to the backend
  and complicate future mobile-client support.
</p>

<h2>1.4 Separation of Concerns</h2>

<table>
  <tr><th>Tier / Layer</th><th>Responsibility</th><th>Technologies</th></tr>
  <tr><td>Client (Frontend)</td><td>User interface, routing, form validation, JWT storage</td><td>React 18, TypeScript, Vite, Axios</td></tr>
  <tr><td>Presentation (Backend)</td><td>HTTP request/response mapping, input validation, authentication filter</td><td>Spring MVC @RestController, Jakarta Validation</td></tr>
  <tr><td>Business Logic</td><td>Domain rules: loan periods, fine calculation, hold queue management, RBAC</td><td>Spring @Service, Spring Security</td></tr>
  <tr><td>Data Access</td><td>CRUD and query abstraction; no SQL in service layer</td><td>Spring Data JPA, @Repository interfaces</td></tr>
  <tr><td>Database</td><td>Persistent storage, schema definition, referential integrity</td><td>PostgreSQL 16 (Docker container)</td></tr>
</table>


<!-- ══════════════════════════════════════════════════════════════════
     SECTION 2 – COMPONENT-LEVEL DESIGN
     ══════════════════════════════════════════════════════════════════ -->
<div class="pagebreak"></div>
<h1>2. Component-Level Design</h1>

<p>
  Six key components are elaborated below. Each diagram follows UML component notation created
  in Draw.io, showing the component name, provided interface, key attributes and operations,
  and inter-component dependencies.
</p>

<!-- ─── Component 1 ─────────────────────────────────────────── -->
<h2>2.1 UserManagement Component</h2>

<div class="component-box">
  <h3>Component: UserManagement</h3>
  <p><strong>Provided Interface:</strong> <code>IUserService</code></p>
  <p><strong>Key Attributes:</strong> userId, name, email, passwordHash, role (LIBRARIAN | PATRON), status (ACTIVE | SUSPENDED), registrationDate</p>
  <p><strong>Operations:</strong> registerUser(), loginUser(), getUserById(), getAllUsers(), updateUserStatus(), loadUserByUsername()</p>
  <p><strong>Dependencies:</strong> UserRepository, Spring Security (SecurityContext, PasswordEncoder), JwtUtil</p>
  <p><strong>Traces to:</strong> <span class="badge">FR-1</span></p>
</div>

{comp1}
<p class="caption">Figure 2 – UserManagement Component Diagram</p>

<p>
  The UserManagement component handles all identity and access management. It provides the
  <code>IUserService</code> interface consumed by the <code>AuthController</code> and
  <code>UserController</code>. Registration and login operations delegate to
  <code>JwtUtil</code> for token generation and to Spring Security's
  <code>PasswordEncoder</code> (BCrypt) for credential hashing. Role-based access control
  (LIBRARIAN vs. PATRON) is enforced at the controller layer using
  <code>@PreAuthorize</code> annotations backed by this component.
</p>

<!-- ─── Component 2 ─────────────────────────────────────────── -->
<div class="pagebreak"></div>
<h2>2.2 BookCatalog Component</h2>

<div class="component-box">
  <h3>Component: BookCatalog</h3>
  <p><strong>Provided Interface:</strong> <code>IBookService</code></p>
  <p><strong>Key Attributes:</strong> bookId, isbn (unique), title, author, genre, publishYear, totalCopies, availableCopies, location, status (AVAILABLE | CHECKED_OUT | LOST | DAMAGED)</p>
  <p><strong>Operations:</strong> addBook(), updateBook(), deleteBook(), getBookById(), getAllBooks(), searchBooks(), getAvailableBooks()</p>
  <p><strong>Dependencies:</strong> BookRepository</p>
  <p><strong>Traces to:</strong> <span class="badge">FR-2</span></p>
</div>

{comp2}
<p class="caption">Figure 3 – BookCatalog Component Diagram</p>

<p>
  The BookCatalog component manages the complete book inventory lifecycle. Write operations
  (add, update, delete) are restricted to LIBRARIAN role via method-level security. The
  <code>searchBooks()</code> operation uses a JPQL query performing case-insensitive matching
  across title, author, and ISBN fields simultaneously, supporting the patron search use case
  without exposing raw SQL. The <code>availableCopies</code> counter is atomically decremented/
  incremented by the CirculationManagement component during checkout and return.
</p>

<!-- ─── Component 3 ─────────────────────────────────────────── -->
<div class="pagebreak"></div>
<h2>2.3 CirculationManagement Component</h2>

<div class="component-box">
  <h3>Component: CirculationManagement</h3>
  <p><strong>Provided Interface:</strong> <code>ICirculationService</code></p>
  <p><strong>Key Attributes:</strong> checkoutId, userId, bookId, checkoutDate, dueDate (14-day loan), returnDate, renewalCount (max 2), fineAmount ($0.25/day), status (ACTIVE | RETURNED | OVERDUE)</p>
  <p><strong>Operations:</strong> checkoutBook(), returnBook(), renewCheckout(), getUserCheckouts(), getOverdueCheckouts()</p>
  <p><strong>Dependencies:</strong> CheckoutRepository, BookRepository, UserRepository, FineRepository</p>
  <p><strong>Traces to:</strong> <span class="badge">FR-4</span> <span class="badge">FR-5</span></p>
</div>

{comp3}
<p class="caption">Figure 4 – CirculationManagement Component Diagram</p>

<p>
  The CirculationManagement component orchestrates the full checkout/return lifecycle within
  a single <code>@Transactional</code> boundary, ensuring atomicity: a failed book status
  update will roll back the checkout record and vice versa. Fine calculation on return is
  automatic — if <code>returnDate &gt; dueDate</code>, a <code>Fine</code> entity is persisted
  for future payment tracking. Renewal is bounded by <code>MAX_RENEWALS = 2</code>, enforced
  at the service layer, ensuring fair access to popular titles.
</p>

<!-- ─── Component 4 ─────────────────────────────────────────── -->
<div class="pagebreak"></div>
<h2>2.4 HoldManagement Component</h2>

<div class="component-box">
  <h3>Component: HoldManagement</h3>
  <p><strong>Provided Interface:</strong> <code>IHoldService</code></p>
  <p><strong>Key Attributes:</strong> holdId, userId, bookId, holdDate, expirationDate, queuePosition, status (PENDING | NOTIFIED | FULFILLED | CANCELLED | EXPIRED)</p>
  <p><strong>Operations:</strong> placeHold(), cancelHold(), getUserHolds(), getQueuePosition(), getNextInQueue(), processExpiredHolds()</p>
  <p><strong>Dependencies:</strong> HoldRepository, BookRepository, UserRepository</p>
  <p><strong>Traces to:</strong> <span class="badge">FR-3</span></p>
</div>

{comp4}
<p class="caption">Figure 5 – HoldManagement Component Diagram</p>

<p>
  The HoldManagement component implements a FIFO hold queue keyed by book and hold date.
  When a patron places a hold on a checked-out book, a <code>Hold</code> record is created
  with <code>PENDING</code> status and the queue position is determined by the
  <code>holdDate</code> ordering. The repository method
  <code>findTopByBookIdAndStatusOrderByHoldDateAsc()</code> efficiently identifies the
  next eligible patron when a book is returned. Holds prevent unlimited queuing via duplicate
  detection (<code>findByUserIdAndBookIdAndStatus()</code>).
</p>

<!-- ─── Component 5 ─────────────────────────────────────────── -->
<div class="pagebreak"></div>
<h2>2.5 Frontend UI Component</h2>

<div class="component-box">
  <h3>Component: Frontend UI (React SPA)</h3>
  <p><strong>Provided Interface:</strong> Browser-rendered UI; consumes REST API from Spring Boot backend</p>
  <p><strong>Key Attributes:</strong> authToken (JWT in localStorage), currentUser (AuthContext), searchQuery, bookList, userCheckouts</p>
  <p><strong>Pages / Operations:</strong>
    LoginPage (login, register),
    BookSearchPage (searchBooks, browseAll),
    BookDetailPage (viewBook, placeHold, checkoutBook),
    DashboardPage (viewMyCheckouts, returnBook)
  </p>
  <p><strong>Shared Components:</strong> Navbar, BookCard, SearchBar</p>
  <p><strong>Services:</strong> authService.ts, bookService.ts, checkoutService.ts (Axios wrappers)</p>
  <p><strong>Dependencies:</strong> Spring Boot REST API (HTTP/JSON), React Router (navigation), Axios (HTTP client)</p>
  <p><strong>Traces to:</strong> <span class="badge">FR-1</span> <span class="badge">FR-2</span> <span class="badge">FR-3</span> <span class="badge">FR-4</span> <span class="badge">FR-5</span> <span class="badge">FR-6</span></p>
</div>

{comp5}
<p class="caption">Figure 6 – Frontend UI Component Diagram (React SPA)</p>

<p>
  The Frontend UI component is the sole entry point for end users. It is architecturally
  isolated from the backend — all communication passes through typed Axios service wrappers
  that construct REST requests and parse JSON responses. The <code>AuthContext</code> React
  context stores the JWT token and current user role, making authentication state globally
  accessible. React Router enforces route-level authorization (patron routes vs. librarian
  routes) on the client side, complementing the server-side <code>@PreAuthorize</code>
  guards. Vite's dev-proxy configuration routes <code>/api/*</code> calls to
  <code>http://localhost:8081</code>, eliminating CORS issues during development.
</p>

<h2>2.6 Requirement Traceability Table</h2>

<table>
  <tr>
    <th>Functional Req.</th>
    <th>Backend Component</th>
    <th>Frontend Page</th>
    <th>Interface</th>
    <th>Quality Goals</th>
  </tr>
  <tr>
    <td>FR-1: User Management</td>
    <td>UserManagement</td>
    <td>LoginPage, RegisterPage</td>
    <td>IUserService / <code>/api/auth/**</code></td>
    <td>QG-1, QG-5</td>
  </tr>
  <tr>
    <td>FR-2: Book Catalog</td>
    <td>BookCatalog</td>
    <td>BookSearchPage, BookDetailPage</td>
    <td>IBookService / <code>/api/books/**</code></td>
    <td>QG-1, QG-3</td>
  </tr>
  <tr>
    <td>FR-3: Hold Management</td>
    <td>HoldManagement</td>
    <td>BookDetailPage</td>
    <td>IHoldService / <code>/api/holds/**</code></td>
    <td>QG-1, QG-3</td>
  </tr>
  <tr>
    <td>FR-4: Checkout</td>
    <td>CirculationManagement</td>
    <td>BookDetailPage</td>
    <td>ICirculationService / <code>/api/checkouts</code></td>
    <td>QG-1, QG-2</td>
  </tr>
  <tr>
    <td>FR-5: Return &amp; Renewal</td>
    <td>CirculationManagement</td>
    <td>DashboardPage</td>
    <td>ICirculationService / <code>/api/checkouts/{'{id}'}/return</code></td>
    <td>QG-1, QG-2</td>
  </tr>
  <tr>
    <td>FR-6: Reporting</td>
    <td>ReportingAnalytics (planned)</td>
    <td>DashboardPage</td>
    <td>IReportService / <code>/api/reports/**</code></td>
    <td>QG-1, QG-4</td>
  </tr>
</table>


<!-- ══════════════════════════════════════════════════════════════════
     SECTION 3 – DESIGN REASONING & TRADE-OFFS
     ══════════════════════════════════════════════════════════════════ -->
<div class="pagebreak"></div>
<h1>3. Design Reasoning &amp; Trade-offs</h1>

<h2>3.1 Architectural Pattern Decision</h2>

<p>
  The selection of <strong>Client-Server + Layered + MVC</strong> was driven by three
  primary forces:
</p>
<ol>
  <li>
    <strong>Team Structure:</strong> Three developers with distinct frontend, backend, and
    infrastructure responsibilities. The client-server split provides a natural work boundary
    with the REST API as the contract.
  </li>
  <li>
    <strong>Quality Goals:</strong> QG-5 (Security) requires all business rules and data
    validation to live in a server-side layer unreachable by client-side manipulation. The
    layered backend enforces this: the Service layer validates all domain rules before any
    data reaches the Repository.
  </li>
  <li>
    <strong>Testability:</strong> Layered architecture enables isolated unit tests for each
    layer with mocked dependencies — a direct prerequisite for achieving the QG-6
    Maintainability target of ≥ 80% test coverage (JaCoCo).
  </li>
</ol>

<h2>3.2 Alternatives Considered</h2>

<table>
  <tr>
    <th>Alternative</th>
    <th>Reason Rejected</th>
  </tr>
  <tr>
    <td><strong>Microservices</strong> (separate services per domain)</td>
    <td>Prohibitive operational complexity for a 3-person academic team; distributed transactions across checkout/fine/hold services would introduce consistency challenges that are out of scope for this course.</td>
  </tr>
  <tr>
    <td><strong>Monolith with Server-Side Rendering</strong> (Thymeleaf/JSP)</td>
    <td>Tightly couples UI design to backend release cycles; limits future mobile app development; does not satisfy QG-3 Usability requirements for a rich, responsive patron experience.</td>
  </tr>
  <tr>
    <td><strong>Event-Driven Architecture</strong> (Kafka/RabbitMQ)</td>
    <td>Unnecessary overhead for a single-instance library system; adds infrastructure dependencies that would reduce portability (QG-8) and increase deployment complexity beyond the scope of this project.</td>
  </tr>
  <tr>
    <td><strong>Flat / Anemic Architecture</strong> (no service layer)</td>
    <td>Business rules (fine calculation, renewal limits, hold queue ordering) would bleed into controllers, making the code untestable and violating the separation-of-concerns requirement in the grading rubric.</td>
  </tr>
</table>

<h2>3.3 Key Design Trade-offs</h2>

<table>
  <tr>
    <th>Decision</th>
    <th>Benefit</th>
    <th>Trade-off Accepted</th>
  </tr>
  <tr>
    <td>Stateless JWT authentication</td>
    <td>Scales horizontally; no server-side session storage; supports the React SPA cleanly</td>
    <td>Token revocation requires additional infrastructure (token blacklist) — deferred to future sprint</td>
  </tr>
  <tr>
    <td>PostgreSQL via Docker</td>
    <td>Reproducible environment; no system-level installation conflicts; consistent behavior across developer machines</td>
    <td>Requires Docker to be running; H2 used for unit tests to avoid container dependency in CI</td>
  </tr>
  <tr>
    <td>Spring Data JPA (repository pattern)</td>
    <td>Eliminates boilerplate SQL; Spring generates JPQL from method names; type-safe queries</td>
    <td>Less control over query optimization for complex reporting queries — custom <code>@Query</code> used selectively for overdue lookups</td>
  </tr>
  <tr>
    <td><code>ddl-auto=create</code> during development</td>
    <td>Schema auto-updated on each restart; no migration scripts needed at this stage</td>
    <td>Data is lost on restart — DataSeeder repopulates test data; production will switch to <code>validate</code> + Flyway</td>
  </tr>
  <tr>
    <td>@Transactional boundaries at service layer</td>
    <td>Atomic checkout/return operations: book availability and checkout status change together or not at all</td>
    <td>Slightly larger transaction scope than strictly necessary, but acceptable at this scale</td>
  </tr>
</table>

<h2>3.4 Requirement Revisions Triggered by Design</h2>

<p>
  During design modeling, one implicit requirement was made explicit:
</p>
<ul>
  <li>
    <strong>Security layer requirement (implicit → explicit):</strong> The original FR-1
    described "user login" without specifying token mechanism. Design analysis revealed that
    a stateless REST API serving a SPA client requires explicit JWT issuance, refresh handling,
    and a <code>JwtAuthFilter</code> for every protected endpoint. This was formally
    documented as a security sub-requirement and implemented in the <code>SecurityConfig</code>
    and <code>JwtAuthFilter</code> classes.
  </li>
  <li>
    <strong>Fine tracking (clarified):</strong> FR-5 (Return) implied fine calculation but
    did not explicitly require a persistent <code>Fine</code> entity. The component design
    identified that reporting (FR-6) and patron payment workflows require persisted fine
    records — hence the <code>Fine</code> JPA entity and <code>FineRepository</code> were
    added as part of the data schema.
  </li>
</ul>


<!-- ══════════════════════════════════════════════════════════════════
     SECTION 4 – QUALITY ALIGNMENT SUMMARY
     ══════════════════════════════════════════════════════════════════ -->
<div class="pagebreak"></div>
<h1>4. Quality Alignment Summary</h1>

<p>
  The following table maps each quality goal (defined in Deliverable I) to the specific
  architectural and design decisions that support it.
</p>

<table>
  <tr>
    <th>Quality Goal</th>
    <th>Priority</th>
    <th>Supporting Design Decision</th>
    <th>Metric / Evidence</th>
  </tr>
  <tr>
    <td><strong>QG-1: Functional Correctness</strong></td>
    <td>Critical</td>
    <td>Service layer owns all domain rules (fine = $0.25/day, max renewals = 2, FIFO hold queue). Rules are centralized, testable, and cannot be bypassed by the controller layer.</td>
    <td>Unit tests per service class; target: zero critical defects in production</td>
  </tr>
  <tr>
    <td><strong>QG-2: Reliability</strong></td>
    <td>Critical</td>
    <td>@Transactional boundaries ensure atomic checkout/return operations. PostgreSQL ACID guarantees prevent partial updates. Docker container with restart policy ensures DB availability.</td>
    <td>MTBF &gt; 720 hours; zero data loss target</td>
  </tr>
  <tr>
    <td><strong>QG-3: Usability</strong></td>
    <td>High</td>
    <td>React SPA delivers a responsive, client-side-rendered UI with instant feedback. Separate pages per workflow (LoginPage, BookSearchPage, DashboardPage) minimize cognitive load.</td>
    <td>SUS score ≥ 75; checkout task &lt; 30 seconds</td>
  </tr>
  <tr>
    <td><strong>QG-4: Performance Efficiency</strong></td>
    <td>High</td>
    <td>JPQL search query with index-friendly LIKE patterns. HikariCP connection pool. Stateless JWT eliminates session-store round-trips. H2 in-memory DB used in tests for speed.</td>
    <td>95th pct response ≤ 2 s; DB search ≤ 1 s</td>
  </tr>
  <tr>
    <td><strong>QG-5: Security</strong></td>
    <td>High</td>
    <td>BCrypt password hashing. HMAC-SHA256 JWT tokens (jjwt 0.12.6). Stateless session (STATELESS policy). @PreAuthorize RBAC on all write endpoints. CORS restricted to frontend origin.</td>
    <td>Zero unauthorized access; 100% PII encrypted in transit (HTTPS in production)</td>
  </tr>
  <tr>
    <td><strong>QG-6: Maintainability</strong></td>
    <td>Medium</td>
    <td>Layered architecture enables layer-isolated testing with Mockito. Lombok reduces boilerplate. Conventional Commits enforce readable history. Feature-branch GitHub workflow enables code review.</td>
    <td>Test coverage ≥ 80% (JaCoCo); cyclomatic complexity &lt; 10</td>
  </tr>
  <tr>
    <td><strong>QG-7: Compatibility</strong></td>
    <td>Medium</td>
    <td>React SPA tested on Chrome/Firefox/Safari. Standard REST/JSON API is client-agnostic. PostgreSQL Docker container works on Windows, macOS, and Linux.</td>
    <td>Runs on latest 2 versions of major browsers</td>
  </tr>
  <tr>
    <td><strong>QG-8: Portability</strong></td>
    <td>Low</td>
    <td>Docker Compose file bundles PostgreSQL for one-command setup. Gradle wrapper eliminates JDK version ambiguity. GitHub Actions CI validates builds on every push.</td>
    <td>Installation time &lt; 2 hours; 3+ environment deployment verified</td>
  </tr>
</table>

<h2>4.1 Workflow Quality Practices</h2>

<table>
  <tr><th>Practice</th><th>Implementation</th><th>Supports</th></tr>
  <tr>
    <td>Feature Branch Workflow</td>
    <td>Every issue has a <code>feature/&lt;issue-id&gt;-&lt;description&gt;</code> branch; merges via PR to <code>development</code></td>
    <td>QG-6 Maintainability, Engineering Discipline</td>
  </tr>
  <tr>
    <td>Conventional Commits</td>
    <td><code>feat(#13): ...</code>, <code>fix(#11): ...</code> — machine-readable history</td>
    <td>QG-6, Process Discipline</td>
  </tr>
  <tr>
    <td>GitHub Projects Kanban</td>
    <td>Issues move: Backlog → In Progress → Done; sprint-tagged, priority-labeled</td>
    <td>Engineering Discipline, Visibility</td>
  </tr>
  <tr>
    <td>CI Pipeline (GitHub Actions)</td>
    <td><code>./gradlew build &amp;&amp; ./gradlew test</code> on every push and PR to main/development</td>
    <td>QG-1 Correctness, QG-6 Maintainability</td>
  </tr>
  <tr>
    <td>H2 In-Memory Test DB</td>
    <td><code>src/test/resources/application.properties</code> overrides PostgreSQL with H2 for fast, dependency-free CI tests</td>
    <td>QG-6, QG-4 Performance in pipeline</td>
  </tr>
</table>

<h2>4.2 Next Steps Toward Full Quality Assurance</h2>

<ul>
  <li><strong>Sprint 2:</strong> Add JaCoCo Gradle plugin; generate coverage report; target ≥ 80%</li>
  <li><strong>Sprint 3:</strong> Integrate SonarQube for static analysis; resolve all critical code smells</li>
  <li><strong>Sprint 3:</strong> Complete integration test suite (Spring Boot test slices + Testcontainers for PostgreSQL)</li>
  <li><strong>Sprint 4:</strong> JMeter load testing for NFR-1 (10 concurrent sessions, ≤ 2 s p95 response)</li>
  <li><strong>Sprint 4:</strong> Switch <code>ddl-auto</code> to <code>validate</code> and add Flyway migrations for production safety</li>
  <li><strong>Sprint 4:</strong> Docker Compose production profile with HTTPS (Let's Encrypt / self-signed)</li>
</ul>

</body>
</html>"""
    return html


def main():
    print("\n" + "="*65)
    print("  Generating Deliverable II PDF – Librarian Assistant")
    print("="*65 + "\n")

    try:
        install_package("weasyprint")
    except Exception:
        pass  # may already be installed via system package manager

    try:
        from weasyprint import HTML
    except ImportError:
        print("ERROR: weasyprint not available. Install with:")
        print("  pip3 install --break-system-packages weasyprint")
        sys.exit(1)

    script_dir = os.path.dirname(os.path.abspath(__file__))
    diagrams_dir = os.path.join(script_dir, "diagrams")

    print("Building HTML with embedded diagrams...")
    html_content = build_html(diagrams_dir)

    html_file = os.path.join(script_dir, "Deliverable_II_Design_Document.html")
    with open(html_file, "w", encoding="utf-8") as f:
        f.write(html_content)
    print(f"  HTML written: {html_file}")

    output_pdf = os.path.join(script_dir, "Deliverable_II_Librarian_Assistant_Design.pdf")
    print("Converting to PDF (this may take ~30 seconds)...")
    HTML(string=html_content).write_pdf(output_pdf)

    size_mb = os.path.getsize(output_pdf) / 1024 / 1024
    print(f"\n✓ PDF created: {output_pdf}")
    print(f"  Size: {size_mb:.2f} MB")
    print("\n" + "="*65 + "\n")


if __name__ == "__main__":
    main()
