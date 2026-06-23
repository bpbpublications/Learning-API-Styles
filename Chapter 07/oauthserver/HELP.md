# 🔐 Auth Service - Spring Boot (HELP.md)

## 📌 Overview
This authentication service provides secure user authentication and authorization using:
- JWT (Access Token)
- Refresh Token (stored securely in DB)
- Token rotation & reuse detection
- Logout & multi-device session management

---

## 🚀 Features
- User Registration
- User Login with JWT
- Access Token validation
- Refresh Token rotation
- Logout (single device)
- Logout from all devices
- Token blacklisting (Redis or in-memory)
- Secure password hashing (BCrypt)

---

## 🏗️ Tech Stack
- Java 21
- Spring Boot
- Spring Security
- JWT (jjwt)
- MySQL (or any RDBMS)
- - Gradle

---

## ⚙️ Configuration

### application.properties

```yml
# JWT
server:
  port: 8081

spring:
  flyway:
    enabled: false
    baseline-on-migrate: true
    baseline-version: 1


  datasource:
    url: jdbc:mysql://localhost:3306/pbp_ecomm
    username: root
    password: root
    driver-class-name : com.mysql.cj.jdbc.Driver
    database-platform : org.hibernate.dialect.MySQL8Dialect
jwt:
  secret: K+vwZZME6tA3QtVtrC3FooGViC4ohLkD45KNEDensRw=          # min 256-bit key in production
  access-token-expiry-ms: 900000  # 15 minutes
  refresh-token-expiry-ms: 604800000  # 7 days

security:
  bcrypt-strength: 12
  max-login-attempts: 5
  lockout-duration-minutes: 30

logging:
  level:
    com.ecommerce.auth: INFO
    org.springframework.security: WARN