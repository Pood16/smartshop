# Spring Security & JWT Authentication - Complete Guide

## Table of Contents
1. [Spring Security Fundamentals](#1-spring-security-fundamentals)
2. [Authentication vs Authorization](#2-authentication-vs-authorization)
3. [Spring Security Architecture](#3-spring-security-architecture)
4. [JWT (JSON Web Tokens)](#4-jwt-json-web-tokens)
5. [Securing Our SmartShop Application](#5-securing-our-smartshop-application)
6. [Implementation Walkthrough](#6-implementation-walkthrough)

---

## 1. Spring Security Fundamentals

### What is Spring Security?
Spring Security is a powerful framework that provides authentication, authorization, and protection against common security vulnerabilities. Think of it as a security guard for your application.

### Core Concepts You Must Know

#### 1.1 Principal
- **What**: The currently logged-in user
- **Analogy**: Your ID card that proves who you are
- **In Code**: Represented by `UserDetails` interface

#### 1.2 Credentials
- **What**: Password or proof of identity
- **Analogy**: The PIN you enter with your ID card
- **In Code**: Usually a password that gets verified

#### 1.3 Authentication
- **What**: The process of verifying identity
- **Analogy**: Airport security checking your passport
- **In Code**: `Authentication` object containing principal and credentials

#### 1.4 Authorization
- **What**: Determining what an authenticated user can do
- **Analogy**: Your boarding pass determines which lounge you can access
- **In Code**: Roles and authorities (ADMIN, CLIENT, etc.)

#### 1.5 SecurityContext
- **What**: Holds security information for the current thread
- **Analogy**: A secure briefcase that follows you everywhere in the application
- **In Code**: `SecurityContextHolder.getContext()`

---

## 2. Authentication vs Authorization

### Authentication: "Who are you?"
```
User: "I'm John Doe"
System: "Prove it" (checks username/password)
User: Provides credentials
System: "Verified! You are John Doe"
```

### Authorization: "What can you do?"
```
John: "I want to delete a product"
System: "Are you an ADMIN?" (checks roles)
John: "Yes, I'm an ADMIN"
System: "Okay, you can delete products"
```

### In Our SmartShop Application:
- **Authentication**: User logs in with username/password → Gets JWT token
- **Authorization**: 
  - ADMIN can: Create/Update/Delete products, manage orders, manage clients
  - CLIENT can: View products, view own profile, view own orders

---

## 3. Spring Security Architecture

### 3.1 The Filter Chain
Spring Security works through a chain of filters. Every HTTP request passes through these filters.

```
HTTP Request
    ↓
[Filter 1: CORS Filter]
    ↓
[Filter 2: JWT Authentication Filter] ← We'll create this
    ↓
[Filter 3: Authorization Filter]
    ↓
[Filter 4: Exception Translation Filter]
    ↓
Your Controller
```

### 3.2 Key Components

#### A. SecurityFilterChain
- **Purpose**: Defines which URLs are protected and how
- **Configuration**: We configure this in `SecurityConfig` class

```java
// Conceptual example
SecurityFilterChain:
  - /api/auth/login → Allow everyone (public)
  - /api/products (GET) → Require authentication
  - /api/products (POST) → Require ADMIN role
  - /api/clients/** → Require ADMIN role
```

#### B. AuthenticationManager
- **Purpose**: Coordinates authentication process
- **Role**: Delegates to AuthenticationProvider to verify credentials

```
User submits credentials
    ↓
AuthenticationManager receives them
    ↓
Asks AuthenticationProvider: "Are these valid?"
    ↓
Provider checks with UserDetailsService
    ↓
Returns Authentication object (success) or throws exception (failure)
```

#### C. UserDetailsService
- **Purpose**: Loads user data from your database
- **Method**: `loadUserByUsername(String username)`
- **Returns**: `UserDetails` object with user info

```java
// What it does conceptually
loadUserByUsername("john_doe") {
    1. Query database for user with username "john_doe"
    2. If found, convert to UserDetails object
    3. Return UserDetails (contains username, password, roles)
    4. If not found, throw UsernameNotFoundException
}
```

#### D. UserDetails
- **Purpose**: Represents a user in Spring Security's world
- **Contains**: Username, password, authorities (roles), account status

```java
// What UserDetails provides
interface UserDetails {
    String getUsername();           // "john_doe"
    String getPassword();           // "$2a$10$encrypted..."
    Collection<GrantedAuthority> getAuthorities(); // [ROLE_ADMIN]
    boolean isAccountNonExpired();  // true
    boolean isAccountNonLocked();   // true
    boolean isCredentialsNonExpired(); // true
    boolean isEnabled();            // true
}
```

#### E. PasswordEncoder
- **Purpose**: Encrypts and verifies passwords
- **Why**: Never store plain text passwords!
- **Common**: BCryptPasswordEncoder

```java
// How it works
String plainPassword = "myPassword123";
String encoded = passwordEncoder.encode(plainPassword);
// Result: "$2a$10$N9qo8uLOickgx2ZMRZoMye..."

// Later, during login:
boolean matches = passwordEncoder.matches("myPassword123", encoded);
// Result: true
```

---

## 4. JWT (JSON Web Tokens)

### 4.1 What is JWT?

JWT is a compact, self-contained token that carries information about a user. Think of it as a **digital passport**.

### 4.2 Why JWT for REST APIs?

#### Traditional Session-Based Auth (What we removed):
```
1. User logs in
2. Server creates session, stores in memory/database
3. Server sends session ID to client (cookie)
4. Client sends session ID with each request
5. Server looks up session in storage

Problems:
- Server must store sessions (memory/database)
- Doesn't scale well (multiple servers need shared session storage)
- Not ideal for mobile apps
```

#### JWT-Based Auth (What we're implementing):
```
1. User logs in
2. Server creates JWT token (no storage needed)
3. Server sends JWT to client
4. Client sends JWT with each request
5. Server verifies JWT signature (no database lookup)

Benefits:
- Stateless (server doesn't store anything)
- Scalable (any server can verify the token)
- Works great for mobile apps and SPAs
```

### 4.3 JWT Structure

A JWT has three parts separated by dots:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTYxNjIzOTAyMn0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

[HEADER].[PAYLOAD].[SIGNATURE]
```

#### Part 1: Header
```json
{
  "alg": "HS256",      // Algorithm used
  "typ": "JWT"         // Token type
}
```

#### Part 2: Payload (Claims)
```json
{
  "sub": "john_doe",        // Subject (username)
  "role": "ADMIN",          // Custom claim
  "iat": 1616239022,        // Issued at
  "exp": 1616242622         // Expiration time
}
```

#### Part 3: Signature
```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret_key
)
```

**Important**: The signature ensures the token hasn't been tampered with!

### 4.4 JWT Security

#### What JWT Protects Against:
- ✅ Token tampering (signature verification)
- ✅ Unauthorized token creation (requires secret key)

#### What JWT Doesn't Protect Against:
- ❌ Token theft (if someone steals your token, they can use it)
- ❌ Token exposure (payload is base64 encoded, NOT encrypted)

**Best Practices**:
- Always use HTTPS
- Store tokens securely (not in localStorage if possible)
- Use short expiration times
- Don't put sensitive data in payload

### 4.5 JWT Workflow in Our Application

```
┌─────────┐                                    ┌─────────┐
│ Client  │                                    │ Server  │
└────┬────┘                                    └────┬────┘
     │                                              │
     │  POST /api/auth/login                       │
     │  { username, password }                     │
     ├────────────────────────────────────────────>│
     │                                              │
     │                                              │ 1. Validate credentials
     │                                              │ 2. Generate JWT token
     │                                              │
     │  { token: "eyJhbG...", userId, role }       │
     │<────────────────────────────────────────────┤
     │                                              │
     │  Store token in memory/secure storage       │
     │                                              │
     │                                              │
     │  GET /api/products                          │
     │  Header: Authorization: Bearer eyJhbG...    │
     ├────────────────────────────────────────────>│
     │                                              │
     │                                              │ 1. Extract token
     │                                              │ 2. Verify signature
     │                                              │ 3. Extract username
     │                                              │ 4. Load user details
     │                                              │ 5. Set SecurityContext
     │                                              │ 6. Check authorization
     │                                              │
     │  { products: [...] }                        │
     │<────────────────────────────────────────────┤
     │                                              │
```

---

## 5. Securing Our SmartShop Application

### 5.1 Current State (After Session Removal)
- ❌ No authentication
- ❌ No authorization
- ❌ All endpoints are public
- ❌ Anyone can access anything

### 5.2 Desired State (After JWT Implementation)
- ✅ JWT-based authentication
- ✅ Role-based authorization (ADMIN, CLIENT)
- ✅ Protected endpoints
- ✅ Secure password storage

### 5.3 Security Requirements

#### Public Endpoints (No Authentication Required):
```
POST /api/auth/login          - Anyone can login
POST /api/clients             - Anyone can register
```

#### Authenticated Endpoints (Valid JWT Required):
```
GET /api/products             - Any authenticated user
GET /api/products/{id}        - Any authenticated user
```

#### ADMIN Only Endpoints:
```
POST   /api/products          - Create product
PUT    /api/products/{id}     - Update product
DELETE /api/products/{id}     - Delete product

GET    /api/clients           - List all clients
GET    /api/clients/{id}      - Get client details
PUT    /api/clients/{id}      - Update client
DELETE /api/clients/{id}      - Delete client

POST   /api/orders            - Create order
GET    /api/orders            - List all orders
GET    /api/orders/{id}       - Get order details
DELETE /api/orders/{id}       - Delete order
PATCH  /api/orders/{id}/confirm - Confirm order
PATCH  /api/orders/{id}/cancel  - Cancel order

POST   /api/payments/order/{id}     - Create payment
PATCH  /api/payments/{id}/collect   - Collect payment
PATCH  /api/payments/{id}/reject    - Reject payment
GET    /api/payments/order/{orderId} - Get payments
```

#### CLIENT Endpoints (Own Resources):
```
GET /api/clients/profile/{id}           - View own profile
GET /api/clients/{clientId}/orders-history - View own orders
```

---

## 6. Implementation Walkthrough

### 6.1 Components We'll Create

```
src/main/java/com/ouirghane/smartshop/
│
├── config/
│   ├── JwtProperties.java              ← JWT configuration
│   └── SecurityConfig.java             ← Main security configuration
│
├── security/
│   ├── JwtService.java                 ← JWT generation & validation
│   ├── JwtAuthenticationFilter.java    ← Intercepts requests, validates JWT
│   ├── JwtAuthenticationEntryPoint.java ← Handles auth errors
│   ├── CustomUserDetails.java          ← Our User → UserDetails adapter
│   ├── CustomUserDetailsService.java   ← Loads users from database
│   └── SecurityContextHelper.java      ← Utility to get current user
│
└── dto/response/
    └── LoginResponseDto.java           ← Modified to include JWT token
```

### 6.2 How Components Work Together

```
┌─────────────────────────────────────────────────────────────────┐
│                    HTTP Request Arrives                          │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
         ┌───────────────────────────────┐
         │  JwtAuthenticationFilter      │
         │  (OncePerRequestFilter)       │
         └───────────┬───────────────────┘
                     │
                     │ 1. Extract JWT from Authorization header
                     │    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                     │
                     ▼
         ┌───────────────────────────────┐
         │  JwtService                   │
         │  - validateToken()            │
         │  - extractUsername()          │
         └───────────┬───────────────────┘
                     │
                     │ 2. Token valid? Extract username
                     │
                     ▼
         ┌───────────────────────────────┐
         │  CustomUserDetailsService     │
         │  - loadUserByUsername()       │
         └───────────┬───────────────────┘
                     │
                     │ 3. Load user from database
                     │
                     ▼
         ┌───────────────────────────────┐
         │  CustomUserDetails            │
         │  (UserDetails implementation) │
         └───────────┬───────────────────┘
                     │
                     │ 4. Create Authentication object
                     │
                     ▼
         ┌───────────────────────────────┐
         │  SecurityContextHolder        │
         │  - Set Authentication         │
         └───────────┬───────────────────┘
                     │
                     │ 5. User is now authenticated
                     │
                     ▼
         ┌───────────────────────────────┐
         │  SecurityFilterChain          │
         │  - Check authorization rules  │
         └───────────┬───────────────────┘
                     │
                     │ 6. Does user have required role?
                     │
                     ▼
         ┌───────────────────────────────┐
         │  Your Controller              │
         │  - Handle business logic      │
         └───────────────────────────────┘
```

### 6.3 Step-by-Step Flow Example

**Scenario**: Admin wants to create a product

#### Step 1: Login
```
POST /api/auth/login
Body: { "username": "admin", "password": "admin123" }

→ AuthenticationServiceImpl.login()
  → Validate credentials
  → JwtService.generateToken(userDetails)
  → Return JWT token

Response: {
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "username": "admin",
  "role": "ADMIN",
  "success": true
}
```

#### Step 2: Create Product (with JWT)
```
POST /api/products
Header: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Body: { "name": "Laptop", "price": 999.99, ... }

→ JwtAuthenticationFilter intercepts
  → Extract token from header
  → JwtService.validateToken(token)
  → JwtService.extractUsername(token) → "admin"
  → CustomUserDetailsService.loadUserByUsername("admin")
  → Create Authentication object with ADMIN role
  → Set in SecurityContext

→ SecurityFilterChain checks authorization
  → Endpoint requires ADMIN role
  → Current user has ADMIN role ✓
  → Allow request to proceed

→ ProductController.createProduct()
  → Business logic executes
  → Product created

Response: { "id": 1, "name": "Laptop", ... }
```

#### Step 3: What if CLIENT tries to create product?
```
POST /api/products
Header: Authorization: Bearer <CLIENT_JWT_TOKEN>
Body: { "name": "Laptop", "price": 999.99, ... }

→ JwtAuthenticationFilter intercepts
  → Token valid, user authenticated as CLIENT

→ SecurityFilterChain checks authorization
  → Endpoint requires ADMIN role
  → Current user has CLIENT role ✗
  → Reject request

Response: 403 Forbidden
{
  "error": "Access Denied",
  "message": "You don't have permission to access this resource"
}
```

### 6.4 Key Implementation Details

#### A. JWT Token Generation
```java
// In JwtService
public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
    
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
}
```

#### B. JWT Token Validation
```java
// In JwtService
public boolean validateToken(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
}
```

#### C. Security Configuration
```java
// In SecurityConfig
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/login", "/api/clients").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/products/**").authenticated()
            .requestMatchers("/api/products/**").hasRole("ADMIN")
            .requestMatchers("/api/orders/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}
```

#### D. Custom UserDetails
```java
// In CustomUserDetails
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private UserRole role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    // Other UserDetails methods...
}
```

### 6.5 Common Pitfalls & Solutions

#### Pitfall 1: "Access Denied" even with valid token
**Cause**: Role name mismatch
```java
// Wrong
.hasRole("ROLE_ADMIN")  // Spring adds "ROLE_" prefix automatically

// Correct
.hasRole("ADMIN")       // Just use "ADMIN"
```

#### Pitfall 2: Token not being extracted
**Cause**: Wrong header format
```
// Wrong
Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

// Correct
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Pitfall 3: CORS issues
**Solution**: Configure CORS in SecurityConfig
```java
.cors(cors -> cors.configurationSource(corsConfigurationSource()))
```

#### Pitfall 4: Password not matching
**Cause**: Not using same PasswordEncoder
```java
// During registration
user.setPassword(passwordEncoder.encode(plainPassword));

// During authentication
passwordEncoder.matches(plainPassword, encodedPassword);
```

---

## 7. Testing Your Implementation

### 7.1 Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Expected: JWT token in response
```

### 7.2 Test Protected Endpoint (Without Token)
```bash
curl -X GET http://localhost:8080/api/products

# Expected: 401 Unauthorized
```

### 7.3 Test Protected Endpoint (With Token)
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"

# Expected: 200 OK with products list
```

### 7.4 Test Authorization (CLIENT accessing ADMIN endpoint)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <CLIENT_JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","price":999.99}'

# Expected: 403 Forbidden
```

---

## 8. Summary

### What We Learned:
1. **Spring Security Architecture**: Filter chain, SecurityContext, Authentication
2. **JWT Basics**: Structure, benefits, security considerations
3. **Authentication Flow**: From login to token generation
4. **Authorization Flow**: From token validation to role checking
5. **Implementation Strategy**: Components and their interactions

### What We're Building:
1. **JwtService**: Generate and validate JWT tokens
2. **JwtAuthenticationFilter**: Intercept requests and authenticate users
3. **CustomUserDetails**: Adapt our User entity to Spring Security
4. **CustomUserDetailsService**: Load users from database
5. **SecurityConfig**: Configure security rules and filters

### Key Takeaways:
- JWT is stateless (server doesn't store sessions)
- Every request carries the JWT token
- Token is validated on each request
- SecurityContext holds current user information
- Roles determine what users can access

---

## 9. Next Steps

Now that you understand the concepts, we'll implement:
1. Add dependencies to pom.xml
2. Create JWT configuration
3. Build JWT service
4. Implement UserDetails components
5. Create authentication filter
6. Configure Spring Security
7. Update authentication service
8. Test everything

**Remember**: Security is not optional. Take time to understand each component before moving to the next!

---

## Additional Resources

- [Spring Security Official Docs](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/) - Decode and verify JWT tokens
- [OWASP Security Guidelines](https://owasp.org/)

---

**Good luck with the implementation! 🚀**
