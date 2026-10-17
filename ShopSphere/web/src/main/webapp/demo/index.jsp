<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="RTU Syllabus Academic Demo Hub — ShopSphere" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Syllabus Demo Hub</li>
        </ol>
    </nav>

    <!-- Header Banner -->
    <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5 mb-5 bg-dark text-white" style="background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);">
        <div class="row align-items-center g-4">
            <div class="col-lg-8">
                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill fw-bold mb-3">
                    <i class="bi bi-mortarboard-fill me-1"></i> RTU 6CS4-04 / Advanced Java Practical Examination
                </span>
                <h2 class="display-6 fw-bold mb-2">Comprehensive Syllabus Demo Hub</h2>
                <p class="lead text-secondary mb-0 small">
                    Every university syllabus requirement, Jakarta EE specification concept, JSP lifecycle mechanism, and network technology is live and testable below.
                </p>
            </div>
            <div class="col-lg-4 text-lg-end">
                <span class="badge bg-primary fs-6 px-3 py-2">
                    Jakarta EE 10 &bull; Tomcat 11
                </span>
            </div>
        </div>
    </div>

    <!-- Demos Grid -->
    <div class="row g-4 mb-5">

        <!-- Demo 1: Traditional JSP Elements -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 p-4 hover-shadow transition-all">
                <div class="avatar-circle bg-warning-subtle text-warning-emphasis rounded-3 p-3 d-inline-flex mb-3" style="width: 50px; height: 50px; align-items: center; justify-content: center;">
                    <i class="bi bi-code-slash fs-4"></i>
                </div>
                <h5 class="fw-bold mb-2">JSP Traditional Elements</h5>
                <p class="text-muted small mb-3">
                    Declarations (<code>&lt;%! %&gt;</code>), scriptlets (<code>&lt;% %&gt;</code>), expressions (<code>&lt;%= %&gt;</code>), directives, comments, and the 9 implicit objects.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/jsp-elements.jsp" class="btn btn-outline-primary btn-sm w-100 fw-semibold">
                        Launch JSP Elements Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Demo 2: Expression Language (EL) -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 p-4 hover-shadow transition-all">
                <div class="avatar-circle bg-info-subtle text-info rounded-3 p-3 d-inline-flex mb-3" style="width: 50px; height: 50px; align-items: center; justify-content: center;">
                    <i class="bi bi-braces-asterisk fs-4"></i>
                </div>
                <h5 class="fw-bold mb-2">Expression Language (EL)</h5>
                <p class="text-muted small mb-3">
                    Property access, 4-tier scope resolution (<code>pageScope</code> &rarr; <code>requestScope</code> &rarr; <code>sessionScope</code> &rarr; <code>applicationScope</code>), and JSTL functions.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/el-demo.jsp" class="btn btn-outline-info btn-sm w-100 fw-semibold">
                        Launch EL Scopes Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Demo 3: JSTL XML Feed Parsing -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 p-4 hover-shadow transition-all">
                <div class="avatar-circle bg-danger-subtle text-danger rounded-3 p-3 d-inline-flex mb-3" style="width: 50px; height: 50px; align-items: center; justify-content: center;">
                    <i class="bi bi-filetype-xml fs-4"></i>
                </div>
                <h5 class="fw-bold mb-2">JSTL XML Processing</h5>
                <p class="text-muted small mb-3">
                    Parsing XML feeds directly within JSP views using <code>x:parse</code>, <code>x:out</code>, <code>x:forEach</code>, <code>x:if</code>, and XPath expressions.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/jstl-xml.jsp" class="btn btn-outline-danger btn-sm w-100 fw-semibold">
                        Launch JSTL XML Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Demo 4: JSTL SQL Tag Library -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 p-4 hover-shadow transition-all">
                <div class="avatar-circle bg-success-subtle text-success rounded-3 p-3 d-inline-flex mb-3" style="width: 50px; height: 50px; align-items: center; justify-content: center;">
                    <i class="bi bi-database-fill-gear fs-4"></i>
                </div>
                <h5 class="fw-bold mb-2">JSTL SQL Tag Library</h5>
                <p class="text-muted small mb-3">
                    Direct SQL queries using <code>sql:query</code> and <code>sql:setDataSource</code> with educational comparison against MVC architecture.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/jstl-sql.jsp" class="btn btn-outline-success btn-sm w-100 fw-semibold">
                        Launch JSTL SQL Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Demo 5: JNDI DataSource -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 p-4 hover-shadow transition-all">
                <div class="avatar-circle bg-primary-subtle text-primary rounded-3 p-3 d-inline-flex mb-3" style="width: 50px; height: 50px; align-items: center; justify-content: center;">
                    <i class="bi bi-diagram-3-fill fs-4"></i>
                </div>
                <h5 class="fw-bold mb-2">JNDI Naming &amp; Directory</h5>
                <p class="text-muted small mb-3">
                    <code>InitialContext</code> lookup for managed <code>java:comp/env/jdbc/shopsphereDS</code> connection pools and environment entries.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/jndi-demo.jsp" class="btn btn-outline-primary btn-sm w-100 fw-semibold">
                        Launch JNDI Lookup Demo &rarr;
                    </a>
                </div>
            </div>
        </div>

        <!-- Demo 6: i18n Internationalization -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 p-4 hover-shadow transition-all">
                <div class="avatar-circle bg-secondary-subtle text-secondary rounded-3 p-3 d-inline-flex mb-3" style="width: 50px; height: 50px; align-items: center; justify-content: center;">
                    <i class="bi bi-translate fs-4"></i>
                </div>
                <h5 class="fw-bold mb-2">Internationalization (i18n)</h5>
                <p class="text-muted small mb-3">
                    Bilingual English and Hindi (हिंदी) dynamic locale switching backed by <code>ResourceBundle</code> and <code>fmt:message</code>.
                </p>
                <div class="mt-auto">
                    <div class="btn-group btn-group-sm w-100">
                        <a href="${pageContext.request.contextPath}/lang?set=en" class="btn btn-outline-secondary">English</a>
                        <a href="${pageContext.request.contextPath}/lang?set=hi" class="btn btn-outline-secondary">हिंदी (Hindi)</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Demo 7: Session & Cookie Inspector -->
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 border-0 shadow-sm rounded-4 p-4 hover-shadow transition-all">
                <div class="avatar-circle bg-dark-subtle text-dark rounded-3 p-3 d-inline-flex mb-3" style="width: 50px; height: 50px; align-items: center; justify-content: center;">
                    <i class="bi bi-cookie fs-4"></i>
                </div>
                <h5 class="fw-bold mb-2">Session &amp; Cookie Inspector</h5>
                <p class="text-muted small mb-3">
                    Active <code>HttpSession</code> state, creation/access timestamps, timeout management, client cookies inspection, and session invalidation.
                </p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/demo/session-demo.jsp" class="btn btn-outline-dark btn-sm w-100 fw-semibold">
                        Launch Session Inspector &rarr;
                    </a>
                </div>
            </div>
        </div>

    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
