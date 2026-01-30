# Issues Log

This document captures important technical issues encountered during development,
along with their root causes and solutions.  
The goal is to avoid repeating mistakes and help future contributors debug faster.

---

## Issue 001: CORS error when calling backend from frontend

### Problem
Frontend requests from `http://localhost:5173` were blocked with CORS errors,
even after configuring CORS using `WebMvcConfigurer`.

### Cause
Spring Security intercepts requests before Spring MVC.
When Spring Security is present, CORS configuration defined in `WebMvcConfigurer`
is ignored, causing preflight (`OPTIONS`) requests to be blocked.

### Solution
Configured CORS directly in Spring Security using a `CorsConfigurationSource`
and enabled it in the `SecurityFilterChain`.

---

## Issue 002: `NoSuchMethodError` on application startup with springdoc-openapi

### Problem
Application failed to start with the following error:

java.lang.NoSuchMethodError:
'void org.springframework.web.method.ControllerAdviceBean.<init>(java.lang.Object)'

### Cause
The version of `springdoc-openapi` used was incompatible with the Spring Framework
version bundled with Spring Boot 3.x.
The library attempted to call a constructor that no longer exists in Spring Framework 6.

### Solution
Aligned the `springdoc-openapi` dependency version with Spring Boot 3.x
by using a compatible version (`springdoc-openapi-starter-webmvc-ui:2.3.0`).

---

## 🐞 Issue-003: Swagger breaks after adding `@RestControllerAdvice`

---

## 🧩 Problem

Swagger UI was working correctly in the project.

After adding:
- `@RestControllerAdvice`
- Global exception handling
- DTO validation using `@Valid`

Swagger stopped working and failed when accessing:
- `/swagger-ui.html`
- `/v3/api-docs`

The application itself started successfully and APIs worked,  
but Swagger failed at runtime.

---

## ❌ Error Observed

java.lang.NoSuchMethodError:
'void org.springframework.web.method.ControllerAdviceBean.<init>(java.lang.Object)'


This error occurred only when Swagger tried to generate OpenAPI documentation.

---

## 🔍 Why This Happened

- The project uses **Spring Boot 3.5.x**
- Spring Boot 3.5.x uses **Spring Framework 6.2.x**
- The project was using **springdoc-openapi 2.6.0**

When `@RestControllerAdvice` was added:
- Swagger (springdoc) started scanning global exception handlers
- While scanning, springdoc internally uses `ControllerAdviceBean`
- Spring Framework 6.2.x changed the internal constructor of this class
- springdoc 2.6.0 still expects the old constructor

As a result, Swagger crashed with a `NoSuchMethodError`.

---

## 🤔 Why Swagger Worked Earlier

Swagger was working earlier because:
- `@RestControllerAdvice` was not present
- The incompatible Springdoc code path was never executed

Once global exception handling was added,  
Swagger touched the incompatible internal API and failed.

---

## ❌ What Was NOT the Cause

This issue was **not caused by**:
- Global exception handler logic
- DTO validation annotations
- Spring Security configuration
- CORS configuration
- Controller or service code

All of these are correct and valid.

---

## ✅ Solution

Upgrade `springdoc-openapi` to a version compatible with  
Spring Framework 6.2.x.

### 🔧 Updated Dependency

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

## Issue 004: `ClassCastException` during JWT generation after authentication

### Problem
User authentication was successful, but the application failed at runtime
while generating the JWT token.

Although login completed correctly, an exception occurred when trying to
typecast the authenticated principal.

---

### ❌ Error Observed

java.lang.ClassCastException:
class org.springframework.security.core.userdetails.User
cannot be cast to class com.learnease.server.model.UserAuth

This error occurred while typecasting:

(UserAuth) fullyAuthenticated.getPrincipal();

---

### 🔍 Cause

`AuthenticationManager.authenticate()` returns an `Authentication` object.

The `principal` inside this object is whatever is returned by
`loadUserByUsername()`.

Initially, `loadUserByUsername()` was returning Spring Security’s built-in
user object:

org.springframework.security.core.userdetails.User

Because of this:
- The authenticated principal was of type `User`
- The application attempted to cast it to `UserAuth`
- This resulted in a `ClassCastException`

---

### ⚠️ Additional Issue: Loss of User ID

Spring Security’s default `User` object does not contain the database user ID.

JWT generation required the user ID to add custom claims, but this information
was lost when returning Spring’s `User` from `loadUserByUsername()`.

As a result:
- `JwtUtil.generateToken(UserAuth user)` could not be used correctly
- Custom claims such as `user_id` could not be added safely

---

### ❌ Why Returning Spring’s User Was a Problem

Returning `org.springframework.security.core.userdetails.User` caused:
- Runtime `ClassCastException` during typecasting
- Loss of domain-specific fields like `id` and `status`
- Inability to generate JWT tokens with required custom claims

---

### ✅ Solution

To resolve this issue:
- The `UserAuth` entity was updated to implement `UserDetails`
- `loadUserByUsername()` was modified to return the `UserAuth` object directly

This ensured that:
- The authenticated principal is of type `UserAuth`
- User ID and other domain fields are preserved
- JWT generation works correctly
- No typecasting issues occur

## Cautions

- #### Using Id from token to get an entity
Beware when using id from token to get an entity it is possible to mistakenly use findById
but the id inside jwt token is UserAuthId and not the Id field of that entity thus
if you use findById and pass the Id from token it will always return null

- #### UUID representation mismatch (BINARY vs String)
  Beware when using UUIDs stored as `BINARY(16)` in the database and passing them through APIs.
  MySQL represents binary UUIDs as hex values (`0x...`), but Spring expects the standard
  36-character UUID string format (`xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`).
  If you pass the `0x...` value to a controller DTO of type `UUID`, deserialization will fail.
  Always convert database values using `BIN_TO_UUID()` and use UUID strings in Swagger,
  request bodies, and JWTs.


## Issue 005: Swagger shows CORS error for multipart file upload

### Problem
When invoking the `POST /api/v1/instructor/addCourse` API from Swagger UI using
`multipart/form-data` (image and video upload) along with an `Authorization` header,
Swagger displays the following error:

Failed to fetch.
Possible Reasons:
- CORS
- Network Failure

Even though this error is shown, backend logs confirm that the request reaches
the server and JWT authentication succeeds.

---

### Cause
This issue is caused by a misleading error message from Swagger UI.

Multipart requests with `Authorization` headers trigger a CORS preflight request.
While CORS is correctly configured in Spring Security, the actual backend failure
occurs due to the default multipart upload size limit being exceeded.

Backend logs show the following exception:

MaxUploadSizeExceededException: Maximum upload size exceeded

Swagger incorrectly reports this server-side exception as a CORS or network failure.

---

### Solution
Increase multipart upload limits in `application.yml`:

spring:
servlet:
multipart:
max-file-size: 200MB
max-request-size: 200MB

Restart the application after applying the configuration.

---

### Result
- Multipart file uploads work correctly from Swagger UI
- JWT authentication remains functional
- No misleading CORS errors are displayed
- Large file uploads are handled successfully

---

### Key Takeaway
Swagger UI may display backend errors as CORS failures. Always verify server logs
to identify the actual root cause.


## Issue 006: Hibernate TransientObjectException while adding Education to UserDetails

### Problem
When attempting to add a new `Education` entry to an existing `UserDetails`
entity and saving the parent entity, the following error is returned:

org.hibernate.TransientObjectException: persistent instance references an unsaved
transient instance of 'com.learnease.server.model.Education'
(save the transient instance before flushing)

The API fails even though the `UserDetails` entity is already persisted.

---

### Cause
This issue occurs because the `Education` entity is a **new (transient) object**
that has not yet been saved, while `UserDetails` is a **persistent entity**.

Hibernate does not automatically persist child entities in a `@OneToMany`
relationship unless **cascade persistence** is explicitly configured.

As a result, Hibernate detects a persistent entity referencing an unsaved
transient entity and throws a `TransientObjectException`.

---

### Solution
Enable cascade persistence on the `@OneToMany` relationship in `UserDetails`:

```java
@OneToMany(
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
@JoinColumn(name = "user_id")
private List<Education> educations = new ArrayList<>();
```

## Issue 007: Cannot project byte[] to java.util.UUID in native query

### Problem
When executing a native query with projection in Spring Data JPA, the following error occurs:

Cannot project byte[] to java.util.UUID;  
Target type is not an interface and no matching Converter found

This error appears while mapping native query results to a projection or DTO
that uses `java.util.UUID`.

---

### Cause
In MySQL, UUIDs are commonly stored as `BINARY(16)` for performance optimization.

When using native SQL queries:
- `BINARY(16)` UUID columns are returned as `byte[]`
- Spring Data JPA projections expect `java.util.UUID`
- Spring cannot automatically convert `byte[]` to `UUID`

As a result, the projection fails with a type conversion error.

---

### Solution
Convert UUID values inside the native SQL query using `BIN_TO_UUID()` so that
Spring receives UUID values instead of `byte[]`.

Example fix in native query:

BIN_TO_UUID(c.course_id) AS id  
BIN_TO_UUID(c.category_id) AS categoryId

This ensures UUID conversion happens at the database level and projection
mapping works correctly.

---

### Result
- Native query projections map correctly to `UUID`
- No runtime type conversion or casting errors
- Dashboard and read-only queries execute successfully

---

### Key Takeaway
When using native queries with UUIDs stored as `BINARY(16)` in MySQL, always
convert them using `BIN_TO_UUID()` to avoid projection and DTO mapping errors.





## 🐞 Issue-008: Protected APIs return 403 on first login (Axios API Client)

---

## 🧩 Problem

After a successful login, API requests that require authentication
(e.g. `/api/v1/bookings`) returned **403 Forbidden**.

However:
- Refreshing the page fixed the issue
- The same API worked correctly after refresh

This behavior occurred consistently on the **first login only**.

---

## ❌ Error Observed

- Backend response: **403 Forbidden**
- No token-related error in backend logs
- APIs worked correctly after browser refresh

---

## 🔍 Root Cause Analysis

The issue was caused by **how the JWT token was attached to Axios requests**.

### ❌ Incorrect Axios Configuration

```js
const apiClient = axios.create({
  baseURL: "http://localhost:8080/",
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});
````

### Why this breaks

* `axios.create()` is executed **once at import time**
* During first login:

    * Token is **not yet stored** in `localStorage`
    * Axios permanently stores `Authorization: Bearer null`
* All subsequent requests use the **stale Authorization header**
* Page refresh works because:

    * Axios is reinitialized
    * Token now exists in `localStorage`

---

## 🤔 Why the Page Refresh Fixed It

* `localStorage` persists across refresh
* On reload, Axios re-reads the token
* Authorization header is correctly attached
* API requests succeed

Refresh did not fix the bug —
it only **masked the timing issue**.

---

## ❌ What Was NOT the Cause

This issue was **NOT caused by**:

* Spring Security configuration
* Token generation logic
* Backend authorization rules
* CORS configuration
* Controller or service logic

Backend authentication was working correctly.

---

## ✅ Solution

Use an **Axios request interceptor** to attach the token
**before every request**, instead of during client creation.

---

## 🔧 Updated Axios Configuration

```js
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8080/",
  headers: {
    "Content-Type": "application/json",
  },
});

// Attach token dynamically before each request
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

export default apiClient;
```

---

## 🔁 Login Flow Requirement

The token must be **saved before navigating** to protected routes.

### ✅ Correct Flow

```js
localStorage.setItem("token", response.data.token);
navigate("/dashboard");
```

### ❌ Incorrect Flow

```js
navigate("/dashboard");
localStorage.setItem("token", response.data.token);
```

---

## 🛡 Recommended Enhancements

### Global 401 Handling (Optional but Recommended)

```js
apiClient.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);
```

This ensures:

* Automatic logout on token expiry
* No silent authorization failures

---

## 🧠 Key Takeaways

* Never read authentication tokens during Axios initialization
* Always attach tokens using request interceptors
* First-login 403 errors usually indicate **timing issues**
* Page refresh fixing auth issues is a strong sign of stale headers

---

## 📌 Final Status

✔ Issue resolved
✔ No 403 on first login
✔ Token attached correctly to every request
✔ Stable authentication flow



## DEBUGGING HELP

#### when trying to debug a type mismatch for native query use this code
it saves you some hair pulling

```java
import org.springframework.data.jpa.repository.Query;

// in repository
@Query(value = "-- your native query here", nativeQuery = true)
List<Object[]> debugNativeQuery(@Param("status") String status, @Param("sortBy") String sortBy, @Param("limit") int limit);
```
```java
// in service
    public void debugTypes() {
        // Pass dummy values to satisfy parameters
        List<Object[]> results = bookingRepository.debugNativeQuery("PUBLISHED", "REVENUE", 1);

        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            for (int i = 0; i < row.length; i++) {
                System.out.println("Column " + i + " type: " + (row[i] != null ? row[i].getClass().getName() : "NULL"));
            }
        }
    }
```

## Issue 009: OTP attempt count not incrementing on invalid OTP

### Problem
While validating OTPs, the `attempt_count` column in the `otp_verification` table
was not incrementing when an incorrect OTP was entered, even though the
increment logic and repository save operation were executed.

---

### Cause
OTP validation was executed inside an **outer transactional method**
(`resetPasswordWithOtp`).  
Although `@Transactional(noRollbackFor = OtpException.class)` was applied on the
OTP validation method, the thrown `OtpException` propagated to the outer
transaction, causing the **entire transaction to roll back**.

As a result, the OTP attempt count update was undone and never committed to the
database.

---

### Solution
OTP validation was executed in a **separate transaction** using
`Propagation.REQUIRES_NEW`, ensuring that OTP attempt count updates are committed
independently of the outer business transaction.

```java
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    noRollbackFor = OtpException.class
)
@Override
public void validateOtp(UUID userId, OtpPurpose purpose, String otp) {

    OtpVerification otpEntity = otpRepository
        .findByUserIdAndPurposeAndConsumedAtIsNullAndExpiresAtAfter(
            userId, purpose, LocalDateTime.now()
        )
        .orElseThrow(() ->
            new OtpException(OtpErrorCode.OTP_NOT_FOUND)
        );

    if (otpEntity.getAttemptCount() >= MAX_ATTEMPTS) {
        throw new OtpException(OtpErrorCode.OTP_ATTEMPTS_EXCEEDED);
    }

    if (!passwordEncoder.matches(otp, otpEntity.getOtpHash())) {
        otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
        otpRepository.save(otpEntity);
        throw new OtpException(OtpErrorCode.OTP_INVALID);
    }
}
```


-----------------------------
# Issue 010: Database Updates Failing in `TransactionalEventListener` (AFTER_COMMIT)

## Problem
Database updates performed inside a listener annotated with:

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
```

do not persist, even though the listener executes successfully and no errors are logged.

---

## Root Cause

This behavior is caused by a **transaction lifecycle mismatch** in Spring:

- **AFTER_COMMIT timing**: The listener runs *after* the original transaction has been committed.
- **No active transaction**: At this point, the thread no longer has a writable transactional context.
- **Silent non-persistence**: `repository.save()` may trigger a `SELECT`, but no `UPDATE` is flushed because there is no transaction to commit.

As a result, database writes appear to succeed but are never persisted.

---

## Solutions

### Solution 1: Synchronous Execution (Same Thread)

If the listener is **not async**, it executes on the request thread.  
A **new transaction must be explicitly started**.

**Fix:** Use `REQUIRES_NEW` propagation.

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handleBookingPaid(BookingPaidEvent event) {
    Booking booking = bookingRepository.findById(event.bookingId()).orElseThrow();
    booking.setInvoiceStatus(InvoiceStatus.GENERATED);
    bookingRepository.save(booking);
}
```

**When to use**
- Lightweight logic
- No external I/O
- Acceptable to block the request thread

---

### Solution 2: Asynchronous Execution (Recommended)

Best suited for **heavy, non-critical post-payment work**, such as:
- Invoice PDF generation
- File uploads (S3, GCS)
- Email notifications

This approach guarantees **payment safety**, **low response latency**, and **failure isolation**.

---

## Prerequisite: Enable Async Support

```java
@EnableAsync
@Configuration
public class AsyncConfig {
}
```

---

## Async Event Listener Implementation

When `@Async` is used:
- The listener runs on a background thread
- The original transaction context is **not inherited**
- A new transaction **must be started manually**

```java
@Async
@Transactional
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handleBookingPaid(BookingPaidEvent event) {
    // Background invoice logic
}
```

---

## Why `@Transactional` Is Required

- `AFTER_COMMIT` runs after the main transaction completes
- No transaction is active by default
- Any database write (e.g. `invoice_url`, `invoice_status`) requires a new transaction

Adding `@Transactional` ensures:
- A fresh transaction is created
- Writes are properly flushed and committed
- Failures do not impact the payment flow

> If running synchronously, prefer  
> `@Transactional(propagation = Propagation.REQUIRES_NEW)`

---

## Outcome

**Data Integrity**
- Payment and booking state commit first
- Invoice updates persist independently

**Performance**
- Client receives *Payment Successful* immediately
- Heavy work runs in the background

**Reliability**
- Invoice failures never affect payments
- Listener logic is isolated and retryable

---

## Key Takeaway

- `AFTER_COMMIT` is effectively **read-only** unless a new transaction is started
- To safely write data:
    - Use `@Transactional`
    - Prefer `@Async` for non-blocking execution
    - Use `REQUIRES_NEW` only for synchronous listeners

This is the **recommended production pattern** for payments, invoicing, and post-transaction workflows.
