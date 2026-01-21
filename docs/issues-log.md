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
