---
name: code-review
description: >-
  Conducts thorough, structured code reviews for pull requests, Git diffs, staged changes, or specific files.
  Use this skill whenever the user asks for a code review, pull request (PR) inspection, diff analysis, security audit, architecture compliance check, or code quality assessment.
---

# Code Review Skill

This skill provides a systematic, multi-dimensional code review workflow tailored for Clean Architecture, robust security, Kotlin/Spring Boot standards, and high code quality.

---

## 1. Review Objectives & Guiding Principles

- **Security & Integrity First**: Identify vulnerabilities, cryptographic misconfigurations, injection risks, authorization bypasses, and data leakage.
- **Architectural Purity**: Enforce clean hexagonal boundaries between Domain, Infrastructure/Persistence, Security, and Controller layers.
- **Reliability & Correctness**: Detect logical bugs, unhandled edge cases, concurrency hazards, and null-safety violations.
- **Maintainability & Idioms**: Promote idiomatic Kotlin, clean naming conventions, single-responsibility use cases, and proper test coverage.
- **Actionable & Constructive Feedback**: Provide exact file links, severity ratings, explanation of "why", and copy-pasteable diff/code suggestions.

---

## 2. Review Process & Workflow

Follow these sequential steps when conducting a code review:

### Step 1: Identify Review Scope & Context
1. Determine the target files or changes:
   - **Working Tree / Uncommitted Changes**: Inspect `git status` and `git diff`.
   - **Branch / Commit Diff**: Compare current branch against base branch (e.g. `git diff main...HEAD`).
   - **Specific Files / Features**: Inspect requested file(s) and their immediate dependencies.
2. Read related domain models, use cases, and configuration to understand business context.

### Step 2: Automated Verification (Build & Tests)
Run automated checks where applicable to catch compilation, syntax, or test failures early:
- For Gradle projects: `./gradlew test` or `./gradlew compileKotlin`
- Check for existing linter or static analysis feedback.

### Step 3: Deep Multi-Dimensional Analysis
Evaluate changes across the 6 core dimensions:

1. **🛡️ Security & Authentication**:
   - Check API-Key / Bearer JWT enforcement on sensitive endpoints.
   - Validate token lifespan, signature verification, and secret handling.
   - Ensure passwords and sensitive data use strong hashing (e.g. BCrypt) and secure random generators (`SecureRandom`).
   - Check for IDOR (Insecure Direct Object References), permission escalation, or missing `isBlocked` account checks.
   - *Refer to [Security Audit Guide](./references/security-audit.md) for detailed security review checklist.*

2. **🏗️ Architecture & Layering (Clean / Hexagonal)**:
   - Ensure the **Domain layer** remains pure (no Spring, JPA, HTTP, or infrastructure dependencies).
   - Verify that data access logic stays within **Repository/Adapter** implementations.
   - Check that DB entities (`*Db`) are mapped to Domain entities (`User`, etc.) using dedicated mappers before reaching use cases.
   - Ensure controllers only handle HTTP deserialization, delegation to use cases, and HTTP response mapping.

3. **☕ Kotlin & Framework Idioms**:
   - Leverage Kotlin null-safety (`?`, `let`, `takeIf`, `?:`) without reckless `!!` force unwrap.
   - Use immutable data structures (`val`, `List`, data classes with `.copy()`) where appropriate.
   - Ensure correct Spring annotations (`@Service`, `@Repository`, `@Transactional`, `@RestController`).
   - Ensure proper exception handling: business domain exceptions translated into standard `ResponseStatusException` (e.g., 400, 401, 403, 404).

4. **⚡ Database & Performance**:
   - Identify potential N+1 queries, unindexed queries on large tables, or missing transaction boundaries.
   - Verify pagination parameters (`page`, `limit`) and sensible bounds.
   - Prevent connection leaks, long blocking calls, or unconstrained resource allocation.

5. **🧪 Test Quality & Edge Cases**:
   - Check for unit/integration tests covering positive, negative, and boundary scenarios.
   - Verify edge cases: null inputs, expired tokens, duplicate keys, invalid OTPs, rate-limiting conditions.

6. **📝 Documentation & API Contracts**:
   - Verify updated API request/response payloads match specs and documentation (e.g. `AGENTS.md`).
   - Ensure clear logging without leaking credentials, tokens, or PII.

*For a full itemized checklist across all dimensions, see [Review Checklist](./references/checklist.md).*

---

## 3. Review Report Format

Format the review output in a clear, consistent structure:

```markdown
## 📋 Code Review Summary

**Overall Verdict**: [ ✅ Approved / ⚠️ Approved with Comments / ❌ Changes Requested ]
**Scope**: [Summary of reviewed files, commits, or features]

### 🎯 Key Highlights & Strengths
- [Highlight good practices, clean patterns, or solid test coverage observed]

---

### 🚨 Critical / Major Issues (Must Fix)
*(Security vulnerabilities, logic bugs, architectural violations, breaking changes)*

#### 1. [Issue Title]
- **Location**: [filename.kt:L12-L18](file:///path/to/filename.kt#L12-L18)
- **Severity**: Critical / Major
- **Description**: Clear explanation of what is wrong and the potential risk/impact.
- **Recommendation**:
```diff
- problematic_line()
+ corrected_line()
```

---

### 💡 Suggestions & Improvements (Should Fix / Nice to Have)
*(Refactoring, performance optimizations, naming clarity, idiomatic Kotlin improvements)*

#### 1. [Suggestion Title]
- **Location**: [filename.kt:L45](file:///path/to/filename.kt#L45)
- **Severity**: Minor / Nitpick
- **Description**: Suggested improvement with rationale.
- **Proposed Solution**:
```kotlin
// Example clean code
```

---

### 🧪 Testing & Verification Checklist
- [ ] `./gradlew test` passes without regression.
- [ ] Manual test case / curl command: `curl -X POST ...`
```

---

## 4. Severity Definitions

- **🚨 Critical**: Vulnerabilities (auth bypass, injection, plaintext secrets), data corruption, service crash, or severe architectural violation. Blocks merge.
- **⚠️ Major**: Incorrect business logic, broken error handling, unhandled edge cases, missing auth checks, performance regressions. Requires resolution.
- **💡 Minor**: Suboptimal performance, minor code duplication, missing test case, non-idiomatic pattern. Recommended to address.
- **🎨 Nitpick**: Naming style, formatting, minor comment updates. Optional.
