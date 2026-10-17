<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Syllabus Demo: JSTL SQL Tag Library (sql:) — ShopSphere" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/demo/index.jsp" class="text-decoration-none">Syllabus Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">JSTL SQL Tag Library</li>
        </ol>
    </nav>

    <!-- Prominent Syllabus Disclaimer Banner (Mandatory Academic Constraint) -->
    <div class="alert alert-warning border-warning shadow-sm rounded-4 p-4 mb-4">
        <div class="d-flex align-items-center mb-2">
            <i class="bi bi-exclamation-triangle-fill fs-2 text-warning me-3"></i>
            <div>
                <h4 class="fw-bold mb-0 text-dark">Syllabus Demo Only — Architectural Anti-Pattern Warning</h4>
                <span class="small text-muted">RTU Curriculum Practical: Direct Database Queries via <code>&lt;sql:query&gt;</code></span>
            </div>
        </div>
        <hr class="text-warning opacity-25 my-2">
        <p class="small text-dark mb-0">
            <strong>Architecture Notice:</strong> The real ShopSphere production application strictly enforces clean N-Tier separation (<code>Servlet Controller &rarr; Service Layer &rarr; PreparedStatement DAO &rarr; MySQL</code>). This page demonstrates <code>jakarta.tags.sql</code> purely to fulfill university examination and syllabus evaluation requirements.
        </p>
    </div>

    <!-- Configure DataSource via JSTL SQL -->
    <sql:setDataSource var="demoDataSource"
                       driver="com.mysql.cj.jdbc.Driver"
                       url="jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
                       user="root"
                       password="Shivam@1912#"/>

    <!-- Execute Read-Only SQL Query via JSTL SQL Tag -->
    <sql:query dataSource="${demoDataSource}" var="catalogSummary">
        SELECT c.name AS category_name, COUNT(p.id) AS product_count, AVG(p.price) AS avg_price
        FROM categories c
        LEFT JOIN products p ON c.id = p.category_id
        GROUP BY c.id, c.name
        ORDER BY c.id ASC
    </sql:query>

    <!-- Render Query Results Table -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-bold mb-0"><i class="bi bi-database-check text-primary me-2"></i>Live Query Results from MySQL (via <code>&lt;sql:query&gt;</code>)</h5>
            <span class="badge bg-success-subtle text-success">${catalogSummary.rowCount} Categories Aggregated</span>
        </div>

        <div class="table-responsive">
            <table class="table table-bordered table-striped align-middle small mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>Category Name</th>
                        <th class="text-center">Live Product Count</th>
                        <th class="text-end">Average Price (₹)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${catalogSummary.rows}">
                        <tr>
                            <td><strong><c:out value="${row.category_name}"/></strong></td>
                            <td class="text-center">
                                <span class="badge bg-primary rounded-pill"><c:out value="${row.product_count}"/></span>
                            </td>
                            <td class="text-end fw-bold text-primary">
                                ₹<fmt:formatNumber value="${row.avg_price != null ? row.avg_price : 0}" minFractionDigits="2" maxFractionDigits="2"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Comparison Table: JSTL SQL vs Enterprise MVC DAO -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-diagram-3 text-primary me-2"></i>Why Enterprise Applications Avoid Direct SQL in JSP</h5>
        <div class="table-responsive">
            <table class="table table-bordered small align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Attribute</th>
                        <th>JSTL SQL in JSP (Anti-Pattern)</th>
                        <th>ShopSphere MVC Architecture (Best Practice)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Separation of Concerns</strong></td>
                        <td>Violated: UI view layer contains SQL queries.</td>
                        <td>Preserved: JSPs only render DTO models; zero SQL.</td>
                    </tr>
                    <tr>
                        <td><strong>Security &amp; Credentials</strong></td>
                        <td>DB password exposed or stored in presentation tier.</td>
                        <td>Credentials isolated in protected <code>db.properties</code>.</td>
                    </tr>
                    <tr>
                        <td><strong>Transactions</strong></td>
                        <td>Difficult to coordinate multi-table atomic rollbacks.</td>
                        <td>Coordinated cleanly in Service layer via JDBC transactions.</td>
                    </tr>
                    <tr>
                        <td><strong>Testability</strong></td>
                        <td>Impossible to unit test without running web container.</td>
                        <td>Services and DAOs easily tested with JUnit 5.</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Navigation -->
    <div class="d-flex justify-content-between align-items-center">
        <a href="${pageContext.request.contextPath}/demo/jstl-xml.jsp" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-arrow-left me-1"></i>Previous: JSTL XML Demo
        </a>
        <a href="${pageContext.request.contextPath}/demo/index.jsp" class="btn btn-primary btn-sm">
            Back to Syllabus Demo Hub <i class="bi bi-house ms-1"></i>
        </a>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
