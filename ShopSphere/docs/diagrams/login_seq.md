# ShopSphere — Authentication & Login Sequence Diagram

This sequence diagram depicts the end-to-end execution flow of user authentication, including HTTP interceptor filters, PBKDF2 credential verification, and session fixation defense.

```mermaid
sequenceDiagram
    autonumber
    actor User as Client Browser
    participant EF as EncodingFilter
    participant SF as SecurityFilter
    participant LS as LoginServlet
    participant US as UserService
    participant PU as PasswordUtil
    participant UD as UserDAO
    participant DB as MySQL Database
    participant HS as HttpSession

    User->>EF: POST /login (email, password)
    EF->>EF: setCharacterEncoding("UTF-8")
    EF->>SF: doFilter(req, resp)
    SF->>SF: setSecurityHeaders(CSP, X-Frame, XSS)
    SF->>LS: doPost(req, resp)

    LS->>LS: Read email & password params
    LS->>US: authenticate(email, password)
    US->>UD: findByEmail(email)
    UD->>DB: PreparedStatement: SELECT * FROM users WHERE email=?
    DB-->>UD: ResultSet (user row)
    UD-->>US: User entity (with stored salt & hash)

    US->>PU: verifyPassword(password, storedHash)
    PU->>PU: PBKDF2WithHmacSHA256 (65,536 iterations)
    PU-->>US: boolean (true / matches)
    US-->>LS: User object authenticated

    LS->>LS: req.changeSessionId() [Prevents Session Fixation]
    LS->>HS: session.setAttribute("currentUser", user)
    LS->>HS: session.setAttribute("role", user.getRole())
    LS-->>User: 302 Redirect to /home (or return target URL)
```
