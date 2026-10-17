<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!-- Initialize Sample Scoped Variables for Demonstration -->
<c:set var="scopedVar" value="Value Defined in PAGE SCOPE" scope="page"/>
<c:set var="scopedVar" value="Value Defined in REQUEST SCOPE" scope="request"/>
<c:set var="demoNumberA" value="25" scope="page"/>
<c:set var="demoNumberB" value="10" scope="page"/>
<c:set var="sampleText" value="ShopSphere Advanced Java Capstone" scope="page"/>

<c:set var="pageTitle" value="Syllabus Demo: Expression Language (EL) & Scopes — ShopSphere" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/demo/index.jsp" class="text-decoration-none">Syllabus Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">Expression Language (EL)</li>
        </ol>
    </nav>

    <!-- Header Alert -->
    <div class="alert alert-info border-info shadow-sm rounded-4 p-4 mb-4">
        <div class="d-flex align-items-center mb-2">
            <i class="bi bi-braces-asterisk fs-2 text-info me-3"></i>
            <div>
                <h4 class="fw-bold mb-0 text-dark">RTU Syllabus Practical Demo: Expression Language (EL) &amp; Scopes</h4>
                <span class="small text-muted">Demonstrates EL Syntax, Scope Resolution Hierarchy, Operators, and JSTL Function Integrations</span>
            </div>
        </div>
        <hr class="text-info opacity-25 my-2">
        <p class="small text-dark mb-0">
            Expression Language (EL) provides an elegant, scriptlet-free mechanism to extract data from JavaBeans, HTTP parameters, collections, and the four servlet scopes.
        </p>
    </div>

    <!-- 1. The 4 Scopes Hierarchy -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-layers text-primary me-2"></i>1. The 4 Scope Hierarchies in Jakarta EE</h5>
        <p class="small text-muted mb-3">
            When you write <code>\${variable}</code> without an explicit scope prefix, the container evaluates scopes in order:
            <span class="badge bg-secondary">pageScope</span> &rarr;
            <span class="badge bg-primary">requestScope</span> &rarr;
            <span class="badge bg-success">sessionScope</span> &rarr;
            <span class="badge bg-dark">applicationScope</span>.
        </p>

        <div class="table-responsive">
            <table class="table table-bordered table-striped small align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Scope</th>
                        <th>Explicit EL Identifier</th>
                        <th>Object Backing</th>
                        <th>Evaluated Value on this Page</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Page Scope</strong></td>
                        <td><code>\${pageScope.scopedVar}</code></td>
                        <td><code>PageContext</code></td>
                        <td><strong class="text-secondary">${pageScope.scopedVar}</strong></td>
                    </tr>
                    <tr>
                        <td><strong>Request Scope</strong></td>
                        <td><code>\${requestScope.scopedVar}</code></td>
                        <td><code>HttpServletRequest</code></td>
                        <td><strong class="text-primary">${requestScope.scopedVar}</strong></td>
                    </tr>
                    <tr>
                        <td><strong>Session Scope</strong></td>
                        <td><code>\${sessionScope.user.name}</code></td>
                        <td><code>HttpSession</code></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <strong class="text-success">${sessionScope.user.name} (Role: ${sessionScope.user.role})</strong>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted"><em>(Anonymous / Guest session)</em></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td><strong>Application Scope</strong></td>
                        <td><code>\${applicationScope.appName}</code></td>
                        <td><code>ServletContext</code></td>
                        <td><strong class="text-dark">${applicationScope.appName} v${applicationScope.appVersion}</strong></td>
                    </tr>
                    <tr class="table-info">
                        <td><strong>Unscoped Fallback</strong></td>
                        <td><code>\${scopedVar}</code></td>
                        <td>First match in hierarchy</td>
                        <td><span class="badge bg-secondary">${scopedVar}</span> <em>(Resolved from pageScope first)</em></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 2. Operators & Logical Expressions -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-calculator text-primary me-2"></i>2. Arithmetic, Relational &amp; Logical Operators</h5>
        <div class="row g-3">
            <div class="col-md-4">
                <div class="p-3 bg-light rounded-3 border h-100">
                    <h6 class="fw-bold mb-2">Arithmetic Operators</h6>
                    <ul class="list-unstyled small mb-0">
                        <li class="mb-1"><code>\${demoNumberA + demoNumberB}</code> = <strong>${demoNumberA + demoNumberB}</strong></li>
                        <li class="mb-1"><code>\${demoNumberA - demoNumberB}</code> = <strong>${demoNumberA - demoNumberB}</strong></li>
                        <li class="mb-1"><code>\${demoNumberA * demoNumberB}</code> = <strong>${demoNumberA * demoNumberB}</strong></li>
                        <li class="mb-1"><code>\${demoNumberA / demoNumberB}</code> = <strong>${demoNumberA / demoNumberB}</strong></li>
                        <li><code>\${demoNumberA % demoNumberB}</code> = <strong>${demoNumberA % demoNumberB}</strong></li>
                    </ul>
                </div>
            </div>

            <div class="col-md-4">
                <div class="p-3 bg-light rounded-3 border h-100">
                    <h6 class="fw-bold mb-2">Relational Operators</h6>
                    <ul class="list-unstyled small mb-0">
                        <li class="mb-1"><code>\${demoNumberA &gt; demoNumberB}</code> = <strong>${demoNumberA > demoNumberB}</strong></li>
                        <li class="mb-1"><code>\${demoNumberA == 25}</code> = <strong>${demoNumberA == 25}</strong></li>
                        <li class="mb-1"><code>\${demoNumberA eq 25}</code> = <strong>${demoNumberA eq 25}</strong></li>
                        <li class="mb-1"><code>\${demoNumberA != demoNumberB}</code> = <strong>${demoNumberA != demoNumberB}</strong></li>
                        <li><code>\${demoNumberA ne demoNumberB}</code> = <strong>${demoNumberA ne demoNumberB}</strong></li>
                    </ul>
                </div>
            </div>

            <div class="col-md-4">
                <div class="p-3 bg-light rounded-3 border h-100">
                    <h6 class="fw-bold mb-2">Logical &amp; Empty Operators</h6>
                    <ul class="list-unstyled small mb-0">
                        <li class="mb-1"><code>\${demoNumberA &gt; 20 &amp;&amp; demoNumberB &lt; 20}</code> = <strong>${demoNumberA > 20 && demoNumberB < 20}</strong></li>
                        <li class="mb-1"><code>\${empty param.test}</code> = <strong>${empty param.test}</strong></li>
                        <li class="mb-1"><code>\${not empty sampleText}</code> = <strong>${not empty sampleText}</strong></li>
                        <li><code>\${demoNumberA &gt; 18 ? 'Adult' : 'Minor'}</code> = <strong>${demoNumberA > 18 ? 'Adult' : 'Minor'}</strong></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <!-- 3. JSTL Functions Library (fn:) Integration -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-tools text-primary me-2"></i>3. JSTL Functions Library (<code>fn:</code>) with EL</h5>
        <div class="table-responsive">
            <table class="table table-bordered small align-middle mb-0">
                <thead class="table-secondary">
                    <tr>
                        <th>Function Syntax</th>
                        <th>Description</th>
                        <th>Target String</th>
                        <th>Result</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><code>\${fn:length(sampleText)}</code></td>
                        <td>Calculates string length</td>
                        <td>"${sampleText}"</td>
                        <td><strong>${fn:length(sampleText)} characters</strong></td>
                    </tr>
                    <tr>
                        <td><code>\${fn:toUpperCase(sampleText)}</code></td>
                        <td>Converts to uppercase</td>
                        <td>"${sampleText}"</td>
                        <td><strong>${fn:toUpperCase(sampleText)}</strong></td>
                    </tr>
                    <tr>
                        <td><code>\${fn:toLowerCase(sampleText)}</code></td>
                        <td>Converts to lowercase</td>
                        <td>"${sampleText}"</td>
                        <td><strong>${fn:toLowerCase(sampleText)}</strong></td>
                    </tr>
                    <tr>
                        <td><code>\${fn:contains(sampleText, 'Java')}</code></td>
                        <td>Case-sensitive substring check</td>
                        <td>"${sampleText}"</td>
                        <td><span class="badge bg-success">${fn:contains(sampleText, 'Java')}</span></td>
                    </tr>
                    <tr>
                        <td><code>\${fn:substring(sampleText, 0, 10)}</code></td>
                        <td>Extracts substring index [0, 10)</td>
                        <td>"${sampleText}"</td>
                        <td><code>${fn:substring(sampleText, 0, 10)}</code></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 4. HTTP Request Parameters and Headers Extraction via EL -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-globe text-primary me-2"></i>4. Implicit EL Objects for Web Environment</h5>
        <div class="table-responsive">
            <table class="table table-bordered small align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>EL Expression</th>
                        <th>Target Web Construct</th>
                        <th>Evaluated Value</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><code>\${pageContext.request.contextPath}</code></td>
                        <td>Application Context Path</td>
                        <td><code>${pageContext.request.contextPath}</code></td>
                    </tr>
                    <tr>
                        <td><code>\${pageContext.request.method}</code></td>
                        <td>HTTP Request Method</td>
                        <td><span class="badge bg-primary">${pageContext.request.method}</span></td>
                    </tr>
                    <tr>
                        <td><code>\${header['user-agent']}</code></td>
                        <td>Client Browser User-Agent Header</td>
                        <td class="text-truncate" style="max-width: 400px;"><code>${header['user-agent']}</code></td>
                    </tr>
                    <tr>
                        <td><code>\${param.q}</code></td>
                        <td>URL Query Parameter <code>?q=...</code></td>
                        <td><code>${empty param.q ? '(null / omitted)' : param.q}</code></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Navigation -->
    <div class="d-flex justify-content-between align-items-center">
        <a href="${pageContext.request.contextPath}/demo/jsp-elements.jsp" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-arrow-left me-1"></i>Previous: JSP Elements
        </a>
        <a href="${pageContext.request.contextPath}/demo/index.jsp" class="btn btn-primary btn-sm">
            Back to Syllabus Demo Hub <i class="bi bi-house ms-1"></i>
        </a>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
