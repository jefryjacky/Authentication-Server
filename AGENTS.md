# Authentication-Server — Project Context & Architecture Guide

## 1. Executive Summary & Overview
**Authentication-Server** is a production-grade authentication and user identity management microservice built with **Kotlin**, **Spring Boot 2**, **Spring Security**, and **PostgreSQL**. It provides standard OAuth2 token generation (JWT), user registration, credential authentication, Google Sign-In verification, email verification flows (both via tokenized links and 6-digit OTPs), self-service password reset/update flows, and administrative user management with role-based access control.

---

## 2. Technical Stack & Dependencies

| Component | Technology / Library | Version / Details |
|---|---|---|
| **Language** | Kotlin | `1.3.72` (JVM Target `1.8`) |
| **Framework** | Spring Boot | `2.3.3.RELEASE` |
| **Dependency Mgmt** | Spring Dependency Management | `1.0.10.RELEASE` |
| **Security** | Spring Security (Core, Web, Config, Crypto) | `5.3.2.RELEASE` |
| **Database & ORM** | PostgreSQL Driver, Spring Data JPA / Hibernate | PostgreSQL dialect with sequence generators |
| **JSON Serialization**| Jackson Kotlin Module (`2.x`), Google Gson (`2.8.6`) | Custom payload deserialization |
| **Email & Templating**| Spring Boot Starter Mail (`2.2.5.RELEASE`), Thymeleaf (`2.3.3.RELEASE`)| HTML email templates |
| **OAuth2 / Google** | Google API Client (`com.google.api-client:google-api-client:1.31.5`) | Google ID Token verification |
| **Build Tool** | Gradle Kotlin DSL (`build.gradle.kts`) | Gradle Wrapper (`gradlew`) |
| **Containerization**| Docker | Alpine JRE 14 base image (`adoptopenjdk/openjdk14`) |

---

## 3. Architecture & Design Patterns

The codebase adheres strictly to **Clean Architecture / Hexagonal Architecture** principles, maintaining a clean boundary between domain logic, data persistence, security configurations, and transport/HTTP controllers:

```
src/main/kotlin/com/authentication/app/
├── AuthenticationServerApplication.kt   # Main Spring Boot application entry point
├── config/
│   └── apisecurity/                    # API Key filter & Spring Security Web configuration
├── controller/                         # Spring REST Controllers (HTTP boundary)
│   ├── oauth/                          # Token acquisition & Google login endpoints
│   ├── password/                       # Password reset, update & OTP endpoints
│   └── user/                           # Registration, verification, profile & admin endpoints
├── domain/                             # Core Business Domain (Framework-agnostic logic)
│   ├── Constant.kt                     # Token lifespan & timing constants
│   ├── entity/                         # Domain models (User, Role, Payloads, Tokens)
│   ├── repository/                     # Domain repository interfaces (Ports)
│   ├── usecase/                        # Single-responsibility business use case services
│   └── utils/                          # Domain utility interfaces (Crypto, JWT, Mail, etc.)
├── repository/                         # Infrastructure / Persistence Layer (Adapters)
│   ├── changepassword/                 # Change password OTP repository implementation
│   ├── emailotp/                       # Email verification OTP repository implementation
│   ├── entity/                         # JPA Database Entities (UserDB, EmailOtpDb, etc.)
│   ├── mapper/                         # Mappers between JPA DB entities and Domain models
│   └── user/                           # User repository implementation & JPA repository
└── utils/                              # Infrastructure Implementations for Utilities
    ├── json/                           # Gson payload mapping implementations
    └── jwt/                            # HMAC-SHA256 JWT encoder/decoder implementation
```

---

## 4. Security & Authentication Model

### 4.1. Two-Tier Protection Architecture
1. **API Key Filter (`ApiKeyFilter`)**:
   - Every incoming request to `/api/**` requires the `API-KEY` HTTP header.
   - Verified against the environment variable `API_KEY`.
   - Rejects unauthenticated requests with `403 Forbidden`.
2. **Stateless Bearer JWT Authentication**:
   - Protected user & admin endpoints require a signed JWT access token in the `Authorization` header.
   - Role verification (`USER` vs `ADMIN`) is validated on a per-use-case basis.
   - Blocked accounts (`isBlocked = true`) are rejected immediately across all operations.

### 4.2. Token & Cryptographic Specifications
- **Access Token**:
  - Custom JWT encoded with `HS256` (HMAC-SHA256) using `JWT_SECRET`.
  - Lifespan: **15 minutes** (`900,000 ms`).
  - Payload contains: `userId`, `issueDate`, `expireDate`, `type = "ACCESS"`.
- **Refresh Token**:
  - JWT encoded with `HS256`.
  - Lifespan: **~84 days / 12 weeks** (`7,257,600,000 ms`).
  - Payload contains: `userId`, `issueDate`, `expireDate`, `type = "REFRESH"`.
- **Email Verification & Password Reset Tokens**:
  - Encrypted via **AES-GCM (128-bit)** using `AES_SECRET_KEY` and `AES_NOUNCE_KEY`.
  - Payload contains `userId` and timestamp expiration (e.g., 10 minutes for password reset).
- **Password Hashing**:
  - Spring Security `BCryptPasswordEncoder`.
- **One-Time Passwords (OTP)**:
  - 6-digit cryptographically secure numeric OTPs (`SecureRandomUtils`).
  - Lifespan: **3 minutes** expiration window.

---

## 5. API Endpoints Catalog

All endpoints operate under the base path `/api` and require `API-KEY: <key>` header.

### 5.1. Authentication & OAuth (`/api/oauth`)
| Method | Endpoint | Content-Type | Parameters / Body | Description |
|---|---|---|---|---|
| `POST` | `/api/oauth/token` | `x-www-form-urlencoded` | `email`, `password` **OR** `grant_type=refresh_token`, `refresh_token` | Issue access & refresh tokens |
| `POST` | `/api/oauth/google/token` | `x-www-form-urlencoded` | `token` (Google ID Token) | Verify Google ID token, auto-register/login user |

### 5.2. User Management & Verification (`/api/user`)
| Method | Endpoint | Auth | Parameters / Body | Description |
|---|---|---|---|---|
| `POST` | `/api/user/register` | Public (API Key) | `email`, `password` | Register new user or update unverified account |
| `POST` | `/api/user/requestemailverification` | Public (API Key) | `email` | Send verification link email |
| `POST` | `/api/user/emailverification` | Public (API Key) | `token` (encrypted) | Verify email and return tokens |
| `POST` | `/api/user/requestemailverification/otp`| Public (API Key) | `email` | Generate & email 6-digit OTP |
| `POST` | `/api/user/verify/email/otp` | Public (API Key) | `email`, `otp` | Verify 6-digit OTP and return tokens |
| `GET` | `/api/user/get` | Bearer JWT | `userId` (optional, Admin only) | Fetch current user or target user profile |
| `GET` | `/api/user/get/users` | Bearer JWT (Admin) | `email` (opt), `page`, `limit` | Paginated user search/listing |
| `PUT` | `/api/user/block/{userId}` | Bearer JWT (Admin) | Path `userId` | Block a user from accessing the system |
| `PUT` | `/api/user/unblock/{userId}` | Bearer JWT (Admin) | Path `userId` | Unblock a user account |
| `PUT` | `/api/user/update` | Bearer JWT | JSON `UpdateUserRequest` (`displayName`) | Update user profile data |

### 5.3. Password Management (`/api/password`)
| Method | Endpoint | Auth | Parameters / Body | Description |
|---|---|---|---|---|
| `POST` | `/api/password/reset` | Public (API Key) | `email` | Send password reset link to user's email |
| `POST` | `/api/password/update` | Bearer JWT | `password`, `new_password` | Change password with existing password |
| `POST` | `/api/password/update/token` | Public (API Key) | `token` (encrypted reset token), `new_password` | Reset password using link token |
| `POST` | `/api/password/requestchangepassword/otp`| Public (API Key) | `email` | Generate & send 6-digit password change OTP |
| `POST` | `/api/password/update/otp` | Public (API Key) | `email`, `password` (new), `otp` | Reset password using 6-digit OTP |

---

## 6. Database Schema & Persistence

Managed with Hibernate JPA (`spring.jpa.hibernate.ddl-auto=update`) against PostgreSQL:

### `user_table`
- `userId` (`Long`, Primary Key, sequence `user_sequence`)
- `email` (`VARCHAR`, Unique, Not Null)
- `display_name` (`VARCHAR`, Nullable)
- `hashPassword` (`VARCHAR`, Not Null)
- `emailVerified` (`BOOLEAN`, default `false`)
- `role` (`VARCHAR`, `USER` or `ADMIN`)
- `isBlocked` (`BOOLEAN`, default `false`)

### `email_otp_table`
- `email` (`VARCHAR`, Primary Key)
- `otp` (`VARCHAR`, 6 digits)
- `createdDate` (`TIMESTAMP`)

### `change_password_otp_table`
- `email` (`VARCHAR`, Primary Key)
- `otp` (`VARCHAR`, 6 digits)
- `createdDate` (`TIMESTAMP`)

---

## 7. Environment Variables & Configuration

The application is configured via [application.properties](file:///C:/Users/jefry/project/Authentication-Server/src/main/resources/application.properties) utilizing environment variables:

| Variable | Description | Example / Format |
|---|---|---|
| `DATABASE_SERVER_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/auth_db` |
| `DATABASE_USERNAME` | PostgreSQL username | `postgres` |
| `DATABASE_PASSWORD` | PostgreSQL password | `secret_password` |
| `JWT_SECRET` | Secret key for HMAC-SHA256 JWT tokens | `your-32-char-min-secret-key...` |
| `AES_SECRET_KEY` | 128-bit key for AES-GCM encryption | 16-character string or base64 |
| `AES_NOUNCE_KEY` | 12-byte / 96-bit nonce for AES-GCM | 12-character string |
| `API_KEY` | Required header key for all `/api/**` requests | `secret-api-client-key` |
| `ALLOW_ORIGIN` | Allowed CORS origin | `http://localhost:3000` |
| `MAIL_HOST` | SMTP server host | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP server port | `587` |
| `MAIL_USERNAME` | SMTP account email/username | `noreply@domain.com` |
| `MAIL_PASSWORD` | SMTP account password / app token | `mail_app_password` |
| `MAIL_FROM` | Sender display email | `noreply@domain.com` |
| `REGARDED_BY` | Signature/brand name in email templates | `Authentication Support Team` |
| `EMAIL_VERIFICATION_HOST`| Base URL for email verification link | `https://app.domain.com/verify-email` |
| `FORGOT_PASSWORD_HOST` | Base URL for reset password link | `https://app.domain.com/reset-password` |
| `GOOGLE_AUTH_CLIENT_ID`| Google OAuth2 Client ID | `xxxx.apps.googleusercontent.com` |

---

## 8. Build, Run & Deployment

### Local Development
```bash
# Build project
./gradlew build

# Run unit & integration tests
./gradlew test

# Run application locally
./gradlew bootRun
```
The server will start on port `8082` (configured by `server.port=8082`).

### Docker Build & Deployment
```bash
# Build jar
./gradlew bootJar

# Build container image
docker build -t authentication-server:latest .

# Run container
docker run -p 8082:8082 --env-file .env authentication-server:latest
```

---

## 9. Development Conventions & Guidelines
- **Dependency Injection**: Use Spring `@Service` / `@Repository` components injected via field or constructor `@Autowired`.
- **Entity Immutability**: Kotlin `data class` with copy constructors are preferred for domain models.
- **Exceptions & HTTP Responses**: Business layer throws domain exceptions (`UnAuthorizedException`, `IllegalArgumentException`, `IllegalAccessException`), which controllers catch and translate into appropriate `ResponseStatusException` (e.g. `400 BAD_REQUEST`, `401 UNAUTHORIZED`, `403 FORBIDDEN`, `404 NOT_FOUND`).
- **Mapper Pattern**: Always use dedicated mappers (`UserDbMapperImpl`, `EmailOtpDbMapperImpl`, etc.) to translate between JPA database models and domain entities.
