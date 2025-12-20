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