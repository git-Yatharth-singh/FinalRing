# Final Ring

Final Ring is a real-time multiplayer arena game backend built with Java and Spring Boot.

The project is being developed incrementally to explore backend engineering, real-time communication, distributed systems, scalability, concurrency, and system reliability.

The long-term goal is to evolve Final Ring from a simple REST backend into a scalable multiplayer game backend capable of handling real-time gameplay and distributed workloads.

---

## Current Stack

- Java
- Spring Boot
- Spring Security
- JWT
- BCrypt
- PostgreSQL
- JPA / Hibernate
- Maven

---

## Current Progress

### V1 — Core Backend

#### Authentication & Players
- [x] Player registration
- [x] BCrypt password hashing
- [x] Player login
- [x] JWT generation
- [x] JWT validation
- [x] Spring Security integration
- [x] Protected endpoints
- [x] Player profile
- [x] Request validation
- [x] Global exception handling

#### Match Management
- [x] Match creation
- [x] Match creator tracking
- [x] Automatic creator joining
- [x] Match player tracking
- [x] Get match
- [x] Player joining
- [x] Player leaving
- [x] Match capacity validation
- [x] Match lifecycle

#### Match Lifecycle
- [x] `WAITING` state
- [x] `RUNNING` state
- [x] `FINISHED` state
- [x] Creator-only match start
- [x] Minimum player requirement
- [x] Match start timestamp
- [x] Match finish timestamp

#### Match Results
- [x] Player kill tracking
- [x] Player death tracking
- [x] Player ranking
- [x] Kill-based ranking
- [x] Deaths used as tie-breaker
- [x] Match result DTO
- [x] Match results endpoint

---

## Match Rules

Final Ring currently uses a short free-for-all arena match model.

- Minimum players: **2**
- Maximum players: **6**
- Match duration: **5 minutes**
- Players respawn after being killed
- Players are not permanently eliminated
- The player with the most kills at the end of the match wins
- If players have equal kills, the player with fewer deaths ranks higher
- If both kills and deaths are equal, the player who appears first in the sorted result receives the higher rank
- Only the match creator can start the match

### Ranking

Results are ordered by:

1. Kills — descending
2. Deaths — ascending

Example:

```text
Player A → 15 kills, 8 deaths → Rank 2
Player B → 15 kills, 5 deaths → Rank 1
Player C → 12 kills, 3 deaths → Rank 3
Player D →  8 kills, 6 deaths → Rank 4
