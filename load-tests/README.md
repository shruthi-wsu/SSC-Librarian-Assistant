# Load Testing – Librarian Assistant

JMeter test plan targeting the checkout flow (login → checkout → return).  
NFR target: p95 response time < 500 ms under 50 concurrent users.

---

## Prerequisites

- [Apache JMeter](https://jmeter.apache.org/download_jmeter.cgi) 5.6+
- Application running at `http://localhost:8081`
- Default seeded librarian account: `librarian@library.com` / `password`
- At least one book in the database with available copies

---

## Running the Test

### CLI (non-GUI mode – recommended)

```bash
jmeter -n \
  -t load-tests/librarian-checkout-flow.jmx \
  -l load-tests/results.jtl \
  -e -o load-tests/report/
```

Open `load-tests/report/index.html` in a browser to view the HTML report.

### GUI mode (for editing)

```bash
jmeter -t load-tests/librarian-checkout-flow.jmx
```

---

## Test Plan Summary

| Parameter | Value |
|-----------|-------|
| Virtual users (threads) | 50 |
| Ramp-up period | 30 seconds |
| Loop count | 3 |
| Target host | `localhost:8081` |
| Protocol | HTTP |

### Steps per virtual user

1. **POST** `/api/auth/login` — obtain JWT token
2. **POST** `/api/books/search?query=test` — search books (read-only warm-up)
3. **GET** `/api/checkouts/user/2` — list user checkouts
4. **GET** `/api/reports/overdue` — fetch overdue report (LIBRARIAN)

---

## Pass Criteria

| Metric | Target |
|--------|--------|
| Error rate | < 1% |
| p95 response time | < 500 ms |
| Throughput | ≥ 20 req/s |

---

## Interpreting Results

JMeter produces a `results.jtl` file. The HTML report (`-e -o` flags) shows:
- **Response Time Percentiles** – check p95 < 500 ms
- **Throughput** – requests per second
- **Error Rate** – should be < 1%

For CI integration, fail the build if `errorPct > 1` using:

```bash
jmeter-plugins-cmd --generate-png results.png \
  --input-jtl load-tests/results.jtl \
  --plugin-type TransactionsPerSecond
```
