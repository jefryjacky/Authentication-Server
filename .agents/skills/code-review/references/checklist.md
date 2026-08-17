# Comprehensive Code Review Checklist

Use this checklist during code reviews to systematically inspect changed files.

---

## 1. 🏗️ Clean Architecture & Layering

- [ ] **Domain Purity**:
  - Does the `domain/` package contain zero dependencies on Spring Web, JPA annotations, or external database drivers?
  - Are domain exceptions (`UnAuthorizedException`, `IllegalArgumentException`, etc.) defined independently of HTTP status codes?
- [ ] **Port & Adapter Boundaries**:
  - Do use cases depend only on domain repository interfaces (Ports), not persistence implementations?
  - Are persistence adapters located strictly within `repository/`?
- [ ] **Data Mapping**:
  - Are JPA DB entities (`UserDB`, `EmailOtpDb`, etc.) mapped to domain models (`User`, `Role`) using dedicated mapper classes (e.g. `UserDbMapperImpl`)?
  - Is database state never leaked directly into HTTP controllers?
- [ ] **Controller Boundaries**:
  - Do controllers restrict their responsibility to extracting HTTP request data, validating raw input syntax, calling use cases, and mapping domain outcomes to HTTP status codes (`ResponseStatusException`)?

---

## 2. 🛡️ Security & Authentication

- [ ] **API Protection**:
  - Is the `API-KEY` header validated via `ApiKeyFilter` on all `/api/**` endpoints?
  - Are protected endpoints requiring Bearer JWT tokens extracting and validating the Authorization header?
- [ ] **Role-Based Access & Authorization**:
  - Are admin-only endpoints verifying that `user.role == Role.ADMIN`?
  - Are blocked users (`user.isBlocked == true`) rejected immediately before performing any operation?
- [ ] **Cryptographic Handling**:
  - Are passwords hashed using `BCryptPasswordEncoder` (never stored or logged in plain text)?
  - Are OTPs generated using secure random generators (`SecureRandomUtils`)?
  - Are token lifespans strictly verified (e.g., Access Token: 15 min, Reset Token: 10 min, OTP: 3 min)?
  - Are AES-GCM tokens using correct key sizes (128-bit key, 96-bit nonce)?

---

## 3. ☕ Kotlin & Spring Boot Idioms

- [ ] **Null Safety**:
  - Are Kotlin nullable types (`?`) handled safely using safe calls (`?.`), Elvis operator (`?:`), or `let`/`takeIf`?
  - Avoid unsafe force unwraps (`!!`) unless an invariant was previously proven.
- [ ] **Immutability & Data Classes**:
  - Are domain entities modeled as immutable `data class` with `val` properties?
  - Are state transitions performed via `.copy(...)` rather than mutable property setters?
- [ ] **Spring Annotations & Dependency Injection**:
  - Are components properly annotated with `@Service`, `@Repository`, `@RestController`, or `@Component`?
  - Is constructor injection or `@Autowired` used consistently?
  - Are transactions marked with `@Transactional` where atomic database operations occur?
- [ ] **Error Handling**:
  - Are exceptions caught cleanly and converted to meaningful HTTP status codes (`400 BAD_REQUEST`, `401 UNAUTHORIZED`, `403 FORBIDDEN`, `404 NOT_FOUND`, `409 CONFLICT`)?

---

## 4. ⚡ Database & Performance

- [ ] **Query Efficiency**:
  - Are database queries indexed on filtered/sorted columns (e.g. `email`)?
  - Are paginated endpoints utilizing `Pageable` / `page` & `limit` with sensible upper bounds?
- [ ] **Data Integrity**:
  - Are unique constraints enforced at the database level (`@Column(unique = true)`) as well as in business logic?
  - Are cascade operations and foreign keys handled properly to prevent orphaned records?

---

## 5. 🧪 Testing & Validation

- [ ] **Test Coverage**:
  - Are there unit tests for newly added or modified use cases?
  - Are edge cases (null inputs, expired tokens, duplicate email, invalid credentials) covered?
- [ ] **Integration Verification**:
  - Do existing integration tests (`./gradlew test`) pass without regression?
