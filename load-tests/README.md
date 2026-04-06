# Load Testing – Librarian Assistant

Two JMeter test plans are provided:

| Plan | Scenario | Users | Target |
|---|---|---|---|
| `librarian-checkout-flow.jmx` | Checkout-only flow (login → checkout → return) | 50 | p95 < 500 ms |
| `librarian-full-scenario.jmx` | Mixed workload covering all transaction types | 10 | p95 < 2 s (NFR-1) |

Use **`librarian-full-scenario.jmx`** for NFR-1 compliance verification.  
Use **`librarian-checkout-flow.jmx`** for targeted checkout-path profiling.

---

## Prerequisites

- [Apache JMeter](https://jmeter.apache.org/download_jmeter.cgi) 5.6+
- Application running at `http://localhost:8081`
- Default seeded accounts available:
  - Patron: `patron@library.com` / `password`
  - Librarian: `librarian@library.com` / `password`
- At least one book in the database with `id=1` and available copies (seeded by DataSeeder on startup)

---

## Running the Tests

### NFR-1 full mixed-scenario plan (recommended)

```bash
# 2-minute smoke run
jmeter -n \
  -t load-tests/librarian-full-scenario.jmx \
  -l load-tests/results/full-scenario-results.jtl \
  -e -o load-tests/results/full-report/

# Full 2-hour endurance run — edit the plan's ThreadGroup.duration from 120 to 7200
jmeter -n \
  -Jduration=7200 \
  -t load-tests/librarian-full-scenario.jmx \
  -l load-tests/results/endurance-results.jtl \
  -e -o load-tests/results/endurance-report/
```

### Checkout-flow plan

```bash
jmeter -n \
  -t load-tests/librarian-checkout-flow.jmx \
  -l load-tests/results/checkout-results.jtl \
  -e -o load-tests/results/checkout-report/
```

### GUI mode (for editing plans)

```bash
jmeter -t load-tests/librarian-full-scenario.jmx
```

Open the HTML report: `load-tests/results/<report-dir>/index.html`

---

## Mixed Scenario — Workload Distribution

The `librarian-full-scenario.jmx` models realistic library usage:

| Transaction | Weight | Endpoint |
|---|---|---|
| Search Books | 40% | `GET /api/books/search?query=<keyword>` |
| Get Book Detail | 15% | `GET /api/books/{id}` |
| Checkout Book | 25% | `POST /api/checkouts` |
| Return Book | 15% | `POST /api/checkouts/{id}/return` |
| View My Checkouts | 5% | `GET /api/checkouts/user/{userId}` |

A Gaussian think-time of 500–1500 ms is injected between requests to simulate real user pacing.

---

## NFR-1 Acceptance Criteria

| Metric | Target | How to verify |
|---|---|---|
| p95 response time | ≤ 2 000 ms | Aggregate Report → 95th Percentile column |
| p99 response time | ≤ 5 000 ms | Aggregate Report → 99th Percentile column |
| Error rate | < 1% | Summary Report → Error % column |
| Throughput | ≥ 10 req/s sustained | Summary Report → Throughput column |
| Endurance stability | No degradation over 2 hours | Compare p95 at t=10min vs t=120min |

---

## Interpreting HTML Reports

After running with `-e -o <output-dir>`, open `<output-dir>/index.html`. Key sections:

- **Statistics** — p50, p90, p95, p99 per endpoint
- **Response Time Percentiles Over Time** — detect degradation during long runs
- **Active Threads Over Time** — verify ramp-up behaviour
- **Errors** — inspect any non-2xx responses

---

## Results Directory

Results files are ignored by git (see `.gitignore`). Create the directory before running:

```bash
mkdir -p load-tests/results
```
