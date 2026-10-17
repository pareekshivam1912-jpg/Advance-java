# ShopSphere — Architecture & Design Decisions Log

This document records key design decisions, rationale, and compromises made during the development of ShopSphere.

---

### ADR-001: Java Version and Jakarta EE Namespace
- **Decision:** Target Java 17+ (using JDK 21 in local environment) and Jakarta EE 10 (`jakarta.*` namespace: Servlet 6.1, JSP 3.1, JSTL 3.0).
- **Rationale:** Tomcat 11 requires Java 17+ and strictly mandates Jakarta EE 10 (`jakarta.*`). Legacy `javax.*` packages are obsolete in modern Jakarta EE containers.

### ADR-002: Multi-Module Maven Structure
- **Decision:** Split into `core`, `web`, `swing-admin`, `network`, and `rmi` modules with parent `pom.xml`.
- **Rationale:** Prevents code duplication. Swing Admin, Network Socket Server, and RMI Server directly reuse the shared DAO and Service business logic in `core`.

### ADR-003: Pure JDBC without ORM Frameworks
- **Decision:** Plain JDBC utilizing `PreparedStatement` and `try-with-resources` throughout the DAO layer. No Hibernate/JPA or Spring.
- **Rationale:** Conforms directly to the RTU Advanced Java syllabus requirements, demonstrating fundamental JDBC connection, statement, transaction, and metadata mechanics.

### ADR-004: Password Security
- **Decision:** PBKDF2WithHmacSHA256 salted hashing using standard JDK `javax.crypto.SecretKeyFactory` with secure random salt.
- **Rationale:** Provides strong cryptographic protection out-of-the-box without requiring external heavy third-party dependencies.

### ADR-005: Strict Separation of Concerns (N-Tier)
- **Decision:** JSPs contain zero business logic or raw SQL scriptlets. JSPs only display data using EL and JSTL tags. Servlets handle HTTP orchestration, calling Services for domain logic and DAOs for database persistence.
- **Rationale:** Ensures clean MVC architecture and aligns with modern Java Enterprise standards.
