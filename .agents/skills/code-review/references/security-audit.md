# Security Audit & Vulnerability Guide

This reference document outlines specific security review guidelines for authentication and user management services.

---

## 1. Authentication & Session Security

### 1.1 Token Verification
- **JWT Signature**: Ensure HMAC-SHA256 (`HS256`) signature is validated against `JWT_SECRET` with constant-time verification.
- **Expiration Check**: Verify `expireDate` against the current timestamp `System.currentTimeMillis()`. Tokens past their expiration must be rejected immediately.
- **Token Type Isolation**:
  - `ACCESS` tokens must never be accepted at refresh token endpoints.
  - `REFRESH` tokens must never be accepted at protected resource endpoints.
- **Revocation / Block Check**: Always verify `user.isBlocked` before executing operations for a valid token payload.

### 1.2 OTP (One-Time Password) Lifecycle
- **Entropy**: Generated using cryptographically secure PRNG (`SecureRandom`).
- **Lifespan**: Strictly limited to 3 minutes (`180,000 ms`).
- **Single Use & Invalidation**: Once verified or upon issuing a new OTP, the existing OTP record must be cleared or updated to prevent replay attacks.
- **Rate Limiting**: Protect OTP generation and verification endpoints against brute-force attacks.

---

## 2. Cryptographic Best Practices

- **Password Storage**:
  - Passwords must always be hashed with `BCryptPasswordEncoder` before persistence.
  - Never log plain text passwords or tokens in application logs.
- **AES-GCM Encryption (Email & Reset Tokens)**:
  - Key size: 128-bit (`AES_SECRET_KEY`).
  - Nonce / IV: 96-bit (12 bytes, `AES_NOUNCE_KEY`).
  - GCM authentication tag validation must be enforced (prevent ciphertext tampering).

---

## 3. Authorization & Access Control (Broken Object-Level Authorization)

- **User Isolation**: Normal users (`Role.USER`) must only access or modify their own profile data.
- **Admin Endpoints**: Endpoints that list users, block/unblock accounts, or modify roles must explicitly check `caller.role == Role.ADMIN`.
- **Blocked State**: When `isBlocked == true`, block all actions including login, token refresh, profile retrieval, and updates.

---

## 4. Input Validation & Injection Defense

- **Email Validation**: Validate email format on registration and password reset.
- **SQL / JPA Injection**: Always use parameterized queries or Spring Data JPA repository methods. Avoid raw SQL string concatenation.
- **XSS & Content Security**: Sanitize user inputs (e.g. `displayName`) when rendered in templates (Thymeleaf email templates).
