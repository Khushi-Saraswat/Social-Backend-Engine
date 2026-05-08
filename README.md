# Grid07 Assignment — AI Social Backend Engine

---

# Overview

This project is a backend system developed using:

- Java 17
- Spring Boot 3.5.14
- PostgreSQL
- Redis
- Docker

The application simulates a social media backend where:

- Users and Bots can create posts
- Bots can interact with posts/comments
- Redis is used for concurrency-safe atomic operations
- Notification spam is prevented using Redis throttling and batching

The assignment primarily focuses on:

- Redis atomic operations
- Concurrency handling
- Stateless backend architecture
- Distributed system fundamentals
- Notification batching
- Thread safety

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend language |
| Spring Boot 3.5.14 | REST API framework |
| PostgreSQL | Persistent database |
| Redis | In-memory real-time engine |
| Docker | Local container setup |
| Spring Data JPA | ORM layer |
| Hibernate | Database mapping |
| Maven | Dependency management |

---

# Java 17 Features Used

## 1. Records

Java Records were used for DTOs to reduce boilerplate code.

### Example

```java
public record CreatePostRequest(
        Long authorId,
        String authorType,
        String content
) {}
```

### Benefits

- Immutable objects
- Cleaner syntax
- Auto-generated constructor/getters
- Improved readability

---

## 2. var Keyword

Used for local variable type inference.

### Example

```java
var post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found"));
```

### Benefits

- Cleaner code
- Less verbosity
- Easier readability

---

# Thread Safety & Atomic Locks Approach

One of the primary goals of this assignment was to guarantee concurrency-safe behavior while handling multiple bot interactions simultaneously.

The implementation uses Redis as a distributed concurrency control system because Redis operations are atomic by nature.

---

# What is Thread Safety?

Thread safety means:

> Multiple requests can access and modify shared resources simultaneously without causing inconsistent or corrupted data.

### Example Problem

- 200 bots try to comment on the same post at the exact same millisecond
- Without proper synchronization, multiple requests could bypass validation simultaneously
- Database could incorrectly store more than 100 bot comments

This situation is called a:

- Race Condition

---

# Why Redis Was Used

Redis was used because:

- It is extremely fast
- Supports atomic operations
- Works well for distributed systems
- Avoids storing shared state inside Java memory

The application remains completely stateless because:

- No HashMap
- No synchronized blocks
- No static variables
- No in-memory counters

All real-time state management was delegated to Redis.

---

# Atomic Operations Used

| Operation | Purpose |
|---|---|
| INCR | Increment counters safely |
| EXISTS | Check cooldown locks |
| SET with TTL | Create temporary locks |
| Redis List | Queue pending notifications |

---

# Horizontal Cap Implementation

## Requirement

A single post cannot exceed:

- 100 bot replies

### Redis Key

```text
post:{id}:bot_count
```

### Implementation

```java
Long botCount = redisTemplate
        .opsForValue()
        .increment(key);
```

The Redis `INCR` command is atomic.

### This guarantees:

- Only one request updates the counter at a time
- No corrupted values
- Exact concurrency-safe counting

If the counter exceeds 100:

- The request is rejected immediately
- Database transaction is not committed

### This ensures:

- Maximum limit is strictly enforced
- Even under heavy concurrent traffic

---

# Cooldown Lock Implementation

## Requirement

A Bot cannot interact with the same Human more than once every 10 minutes.

### Redis Key

```text
cooldown:bot_{botId}:human_{humanId}
```

### Implementation

```java
redisTemplate.opsForValue()
    .set(key, "active", Duration.ofMinutes(10));
```

Before allowing interaction:

- Redis checks whether the key already exists

If key exists:

- Interaction is blocked immediately

If key does not exist:

- Cooldown lock is created

---

# TTL (Time To Live)

TTL automatically removes cooldown keys after:

- 10 minutes

This prevents:

- Manual cleanup logic
- Stale lock accumulation

Redis handles expiration automatically.

---

# Vertical Cap Implementation

## Requirement

Comment thread depth cannot exceed 20 levels.

### Validation

```java
if(request.depthLevel() > 20)
```

If depth exceeds 20:

- Request is rejected immediately

### This prevents:

- Infinitely deep comment chains
- Excessive recursive bot conversations

---

# Database Integrity Protection

PostgreSQL acts as:

- Source of truth

Redis acts as:

- Concurrency gatekeeper

Database writes only occur if:

- All Redis guardrails pass successfully

### This guarantees:

- Data consistency
- Concurrency-safe validation
- No invalid database commits

---

# Final Concurrency Result

The implementation guarantees:

- No race conditions
- No counter corruption
- Strict enforcement of bot interaction limits
- Thread-safe concurrent request handling
- Stateless backend behavior

This design closely resembles real-world distributed backend systems where Redis is commonly used for:

- Distributed locks
- Throttling
- Counters
- Concurrency control
- Rate limiting
