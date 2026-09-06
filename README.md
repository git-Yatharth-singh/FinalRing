# Final Ring

Final Ring is a real-time multiplayer battle royale backend built with Java and Spring Boot.

The project is being developed incrementally to explore backend engineering, real-time communication, distributed systems, scalability, and system reliability.

## Current Stack

- Java
- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- JPA / Hibernate
- Maven

## Current Progress

### V1 — Core Backend

- [x] Player registration
- [x] BCrypt password hashing
- [x] Player login
- [x] JWT authentication
- [x] Protected endpoints
- [x] Player profile
- [x] Request validation
- [x] Global exception handling
- [x] Match creation
- [x] Automatic creator joining
- [x] Match player tracking
- [x] Match response DTO

### Upcoming

- [ ] Match joining
- [ ] Match leaving
- [ ] Match lifecycle
- [ ] Match results
- [ ] Automated testing
- [ ] V1 completion

## Planned Architecture Evolution

Final Ring will evolve through multiple stages:

```text
V1  → Spring Boot + PostgreSQL + REST
V2  → WebSockets
V3  → Kafka
V4  → Redis
V5  → Microservices
V6  → gRPC
V7  → Docker
V8  → Kubernetes
V9  → Failure / Load / Distributed Testing
