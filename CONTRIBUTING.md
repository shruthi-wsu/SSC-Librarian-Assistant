# Contributing to Librarian Assistant

Thank you for contributing! This document covers branch naming, commit format, how to run the test suite, and the PR checklist.

---

## Branch Naming

| Type | Pattern | Example |
|---|---|---|
| New feature | `feature/<issue-number>-short-desc` | `feature/20-integration-tests` |
| Bug fix | `fix/<issue-number>-short-desc` | `fix/35-fine-calculation` |
| Tests | `test/<issue-number>-short-desc` | `test/20-auth-integration` |
| Documentation | `docs/<issue-number>-short-desc` | `docs/27-readme` |
| Refactor | `refactor/<issue-number>-short-desc` | `refactor/26-sonarqube` |

Always branch from `main`:
```bash
git checkout main && git pull
git checkout -b feature/<issue-number>-short-desc
```

---

## Commit Message Format

```
type(#issue): short imperative description

Optional body explaining the why, not the what.
```

**Types:** `feat` · `fix` · `test` · `docs` · `refactor` · `chore`

**Examples:**
```
feat(#14): add ISBN duplicate validation in BookService
fix(#35): correct fine calculation when book returned same day
test(#20): add FineIntegrationTest covering pay and waive flows
docs(#27): add contributing guidelines and troubleshooting section
```

Keep the subject line under **72 characters**. Use the body for context when the reason for the change is not obvious.

---

## Before Opening a PR

1. **Run the full test suite and verify coverage:**
   ```bash
   cd "Librarian Assistant"
   ./gradlew test jacocoTestReport jacocoTestCoverageVerification
   ```
   All tests must pass and overall coverage must be **≥ 80%**.

2. **Check for compilation warnings:**
   ```bash
   ./gradlew build
   ```

3. **Lint the frontend (if you touched frontend code):**
   ```bash
   cd library-assistant-frontend
   npm run lint
   ```

4. **Self-review your diff** — remove debug statements, TODOs, and commented-out code before opening the PR.

---

## Pull Request Checklist

- [ ] Branch is up to date with `main` (`git pull origin main`)
- [ ] `./gradlew test` passes locally
- [ ] `./gradlew jacocoTestCoverageVerification` passes (≥ 80% coverage)
- [ ] New public methods have Javadoc or are self-explanatory
- [ ] No hardcoded secrets or credentials introduced
- [ ] PR title follows `type(#issue): description` format
- [ ] PR description explains *what* was changed and *why*
- [ ] Linked to the GitHub issue (`Closes #<number>` in the PR body)

---

## Code Style

- **Java:** Follow standard Java conventions (camelCase fields, PascalCase classes). Lombok (`@Slf4j`, `@RequiredArgsConstructor`, `@Builder`, `@Data`) is the project standard — prefer it over boilerplate.
- **Layering:** Controllers do HTTP only. Business logic lives in services. Never call a repository from a controller.
- **Logging:** Use `@Slf4j` on every service class. Log at `INFO` for significant state changes (created, updated, deleted), `WARN` for rejected operations, `ERROR` for unexpected exceptions.
- **Tests:** Prefer `@SpringBootTest` + H2 over Mockito for service-level tests in this project (see `CLAUDE.md`). Name tests `methodUnderTest_scenario_expectedOutcome`.

---

## Running Tests

```bash
# All tests
./gradlew test

# Single test class
./gradlew test --tests BookServiceTest

# Integration tests only
./gradlew test --tests "*.integration.*"

# Coverage report (opens in browser)
./gradlew test jacocoTestReport
open "Librarian Assistant/build/reports/jacoco/test/html/index.html"
```
