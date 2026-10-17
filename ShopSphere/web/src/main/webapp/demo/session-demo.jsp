<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="Sessions & Cookies Inspector — RTU Advanced Java"/>
</jsp:include>
<jsp:include page="/fragments/navbar.jsp"/>

<div class="container py-5">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/demo/index.jsp">Demo Hub</a></li>
            <li class="breadcrumb-item active" aria-current="page">Session & Cookie Inspector</li>
        </ol>
    </nav>

    <!-- Header Banner -->
    <div class="p-4 mb-4 rounded-3 text-white" style="background: linear-gradient(135deg, #1e1b4b 0%, #312e81 100%);">
        <div class="d-flex align-items-center justify-content-between">
            <div>
                <span class="badge bg-warning text-dark mb-2">RTU Advanced Java Syllabus</span>
                <h1 class="h3 fw-bold mb-1">HTTP Session, Cookie &amp; Listener Diagnostics</h1>
                <p class="text-light mb-0 small">
                    Live inspection of <code>HttpSession</code> lifecycle attributes, client-side HTTP Cookies, and container event listeners.
                </p>
            </div>
            <i class="bi bi-shield-lock-fill fs-1 text-warning opacity-75"></i>
        </div>
    </div>

    <%
        // Process interactive demo actions
        String action = request.getParameter("action");
        if ("addAttr".equals(action)) {
            String key = request.getParameter("key");
            String val = request.getParameter("val");
            if (key != null && val != null && !key.trim().isEmpty()) {
                session.setAttribute(key.trim(), val.trim());
            }
        } else if ("removeAttr".equals(action)) {
            String key = request.getParameter("key");
            if (key != null) {
                session.removeAttribute(key);
            }
        } else if ("addCookie".equals(action)) {
            String cName = request.getParameter("cookieName");
            String cVal = request.getParameter("cookieVal");
            if (cName != null && cVal != null && !cName.trim().isEmpty()) {
                Cookie c = new Cookie(cName.trim(), cVal.trim());
                c.setMaxAge(3600); // 1 hour
                c.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
                response.addCookie(c);
            }
        }
    %>

    <div class="row g-4 mb-4">
        <!-- Session State Card -->
        <div class="col-lg-6">
            <div class="card h-100 shadow-sm border-primary">
                <div class="card-header bg-primary text-white fw-bold d-flex justify-content-between align-items-center">
                    <span><i class="bi bi-clock-history me-2"></i>Active HttpSession Details</span>
                    <span class="badge bg-light text-primary">ID: <%= session.getId().substring(0, Math.min(10, session.getId().length())) %>...</span>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush small mb-3">
                        <li class="list-group-item d-flex justify-content-between">
                            <span class="text-muted">Full Session ID:</span>
                            <code class="text-break"><%= session.getId() %></code>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span class="text-muted">Creation Timestamp:</span>
                            <span><%= new Date(session.getCreationTime()) %></span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span class="text-muted">Last Accessed Time:</span>
                            <span><%= new Date(session.getLastAccessedTime()) %></span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span class="text-muted">Max Inactive Interval:</span>
                            <span><%= session.getMaxInactiveInterval() %> seconds (<%= session.getMaxInactiveInterval() / 60 %> mins)</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span class="text-muted">Is New Session:</span>
                            <span class="badge bg-<%= session.isNew() ? "success" : "secondary" %>"><%= session.isNew() %></span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span class="text-muted">Active Sessions Count (SessionListener):</span>
                            <span class="badge bg-info text-dark">${applicationScope.activeSessionCount != null ? applicationScope.activeSessionCount : 1} concurrent</span>
                        </li>
                    </ul>

                    <h6 class="fw-bold mb-2">Stored Session Attributes:</h6>
                    <div class="table-responsive">
                        <table class="table table-sm table-bordered small">
                            <thead class="table-light">
                                <tr><th>Attribute Key</th><th>Value</th><th>Action</th></tr>
                            </thead>
                            <tbody>
                                <%
                                    Enumeration<String> attrNames = session.getAttributeNames();
                                    boolean hasAttrs = false;
                                    while (attrNames.hasMoreElements()) {
                                        hasAttrs = true;
                                        String name = attrNames.nextElement();
                                        Object value = session.getAttribute(name);
                                %>
                                <tr>
                                    <td><strong><%= name %></strong></td>
                                    <td><span class="text-truncate d-inline-block" style="max-width: 200px;"><%= value %></span></td>
                                    <td>
                                        <a href="?action=removeAttr&key=<%= name %>" class="btn btn-outline-danger btn-sm py-0 px-1 x-small">Remove</a>
                                    </td>
                                </tr>
                                <%
                                    }
                                    if (!hasAttrs) {
                                %>
                                <tr><td colspan="3" class="text-center text-muted">No custom attributes bound.</td></tr>
                                <%  } %>
                            </tbody>
                        </table>
                    </div>

                    <!-- Add Test Attribute -->
                    <form method="post" action="?action=addAttr" class="row g-2 mt-2">
                        <div class="col-5">
                            <input type="text" name="key" class="form-control form-control-sm" placeholder="Attribute Key" required>
                        </div>
                        <div class="col-5">
                            <input type="text" name="val" class="form-control form-control-sm" placeholder="Value" required>
                        </div>
                        <div class="col-2">
                            <button type="submit" class="btn btn-primary btn-sm w-100">Set</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Cookies Card -->
        <div class="col-lg-6">
            <div class="card h-100 shadow-sm border-info">
                <div class="card-header bg-info text-dark fw-bold d-flex justify-content-between align-items-center">
                    <span><i class="bi bi-cookie me-2"></i>Incoming HTTP Request Cookies</span>
                    <span class="badge bg-dark text-white">Client-Side State</span>
                </div>
                <div class="card-body">
                    <p class="small text-muted">
                        Cookies received in the <code>Cookie</code> HTTP header from the client browser:
                    </p>

                    <div class="table-responsive mb-3">
                        <table class="table table-sm table-bordered small align-middle">
                            <thead class="table-light">
                                <tr><th>Cookie Name</th><th>Value</th><th>Max-Age</th></tr>
                            </thead>
                            <tbody>
                                <%
                                    Cookie[] cookies = request.getCookies();
                                    if (cookies != null && cookies.length > 0) {
                                        for (Cookie c : cookies) {
                                %>
                                <tr>
                                    <td><code><%= c.getName() %></code></td>
                                    <td><span class="text-break"><%= c.getValue() %></span></td>
                                    <td><%= c.getMaxAge() == -1 ? "Session" : c.getMaxAge() + "s" %></td>
                                </tr>
                                <%
                                        }
                                    } else {
                                %>
                                <tr><td colspan="3" class="text-center text-muted">No cookies found in request header.</td></tr>
                                <%  } %>
                            </tbody>
                        </table>
                    </div>

                    <h6 class="fw-bold mb-2">Create / Update a Test Cookie:</h6>
                    <form method="post" action="?action=addCookie" class="row g-2">
                        <div class="col-5">
                            <input type="text" name="cookieName" class="form-control form-control-sm" placeholder="Cookie Name" required>
                        </div>
                        <div class="col-5">
                            <input type="text" name="cookieVal" class="form-control form-control-sm" placeholder="Cookie Value" required>
                        </div>
                        <div class="col-2">
                            <button type="submit" class="btn btn-info btn-sm w-100">Add</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Syllabus Architecture Theory -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-dark text-white fw-bold">
            <i class="bi bi-mortarboard-fill me-2"></i>RTU Viva Reference: Session vs Cookie Comparison
        </div>
        <div class="card-body small">
            <div class="row g-4">
                <div class="col-md-6">
                    <h6 class="fw-bold text-primary">HttpSession (Server-Side)</h6>
                    <ul class="text-muted ps-3 mb-0">
                        <li>Data stored strictly in server memory (heap).</li>
                        <li>Can store complex arbitrary Java objects (POJOs, Collections).</li>
                        <li>Identified on the client via a random cryptographic token (<code>JSESSIONID</code>).</li>
                        <li>High security: sensitive data (e.g. user details, permissions) never sent across network.</li>
                    </ul>
                </div>
                <div class="col-md-6">
                    <h6 class="fw-bold text-info">HTTP Cookie (Client-Side)</h6>
                    <ul class="text-muted ps-3 mb-0">
                        <li>Small key-value text pairs (max ~4 KB) stored in the browser.</li>
                        <li>Sent automatically in every subsequent HTTP request to matching domain/path.</li>
                        <li>Can persist across browser restarts if <code>maxAge &gt; 0</code>.</li>
                        <li>Used for non-sensitive preferences (e.g., recently viewed items, language preference).</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="text-center">
        <a href="${pageContext.request.contextPath}/demo/index.jsp" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left me-1"></i> Back to Syllabus Demo Hub
        </a>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp"/>
