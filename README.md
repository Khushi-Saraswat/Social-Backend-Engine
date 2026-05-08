# Grid07 Assignment — AI Social Backend Engine

## Overview

This project is a backend system developed using:

* Java 17
* Spring Boot 3.5.14
* PostgreSQL
* Redis
* Docker

The application simulates a social media backend where:

* Users and Bots can create posts
* Bots can interact with posts/comments
* Redis is used for concurrency-safe atomic operations
* Notification spam is prevented using Redis throttling and batching

The assignment primarily focuses on:

* Redis atomic operations
* concurrency handling
* stateless backend architecture
* distributed system fundamentals
* notification batching
* thread safety

---

# Tech Stack

| Technology         | Purpose                    |
| ------------------ | -------------------------- |
| Java 17            | Backend language           |
| Spring Boot 3.5.14 | REST API framework         |
| PostgreSQL         | Persistent database        |
| Redis              | In-memory real-time engine |
| Docker             | Local container setup      |
| Spring Data JPA    | ORM layer                  |
| Hibernate          | Database mapping           |
| Maven              | Dependency management      |

---

# Java 17 Features Used

## 1. Records

Java Records were used for DTOs to reduce boilerplate code.

Example:

```java
public record CreatePostRequest(
        Long authorId,
        String authorType,
        String content
) {}

Benefits
immutable objects
cleaner syntax
auto-generated constructor/getters
improved readability
2. var Keyword

Used for local variable type inference.

Example:

var post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found"));
Benefits
cleaner code
less verbosity
easier readability
Thread Safety & Atomic Locks Approach

One of the primary goals of this assignment was to guarantee concurrency-safe behavior while handling multiple bot interactions simultaneously.

The implementation uses Redis as a distributed concurrency control system because Redis operations are atomic by nature.

What is Thread Safety?

Thread safety means:

multiple requests can access and modify shared resources simultaneously without causing inconsistent or corrupted data.

Example problem:

200 bots try to comment on the same post at the exact same millisecond
without proper synchronization, multiple requests could bypass validation simultaneously
database could incorrectly store more than 100 bot comments

This situation is called a:

Race Condition
Why Redis Was Used

Redis was used because:

it is extremely fast
supports atomic operations
works well for distributed systems
avoids storing shared state inside Java memory

The application remains completely stateless because:

no HashMap
no synchronized blocks
no static variables
no in-memory counters

All real-time state management was delegated to Redis.

Atomic Operations Used

The following Redis atomic operations were used:

Operation	Purpose
INCR	Increment counters safely
EXISTS	Check cooldown locks
SET with TTL	Create temporary locks
Redis List	Queue pending notifications
Horizontal Cap Implementation
Requirement

A single post cannot exceed:

100 bot replies

Redis Key:

post:{id}:bot_count

Implementation:

Long botCount = redisTemplate
        .opsForValue()
        .increment(key);

The Redis INCR command is atomic.

This guarantees:

only one request updates the counter at a time
no corrupted values
exact concurrency-safe counting

If the counter exceeds 100:

the request is rejected immediately
database transaction is not committed

This ensures:

maximum limit is strictly enforced
even under heavy concurrent traffic
Cooldown Lock Implementation
Requirement

A Bot cannot interact with the same Human more than once every 10 minutes.

Redis Key:

cooldown:bot_{botId}:human_{humanId}

Implementation:

redisTemplate.opsForValue()
    .set(key, "active", Duration.ofMinutes(10));

Before allowing interaction:

Redis checks whether the key already exists

If key exists:

interaction is blocked immediately

If key does not exist:

cooldown lock is created
TTL (Time To Live)

TTL automatically removes cooldown keys after:

10 minutes

This prevents:

manual cleanup logic
stale lock accumulation

Redis handles expiration automatically.

Vertical Cap Implementation
Requirement

Comment thread depth cannot exceed 20 levels.

Validation:

if(request.depthLevel() > 20)

If depth exceeds 20:

request is rejected immediately

This prevents:

infinitely deep comment chains
excessive recursive bot conversations
Database Integrity Protection

PostgreSQL acts as:

source of truth

Redis acts as:

concurrency gatekeeper

Database writes only occur if:

all Redis guardrails pass successfully

This guarantees:

data consistency
concurrency-safe validation
no invalid database commits
Final Concurrency Result

The implementation guarantees:

no race conditions
no counter corruption
strict enforcement of bot interaction limits
thread-safe concurrent request handling
stateless backend behavior

This design closely resembles real-world distributed backend systems where Redis is commonly used for:

distributed locks
throttling
counters
concurrency control
rate limiting


