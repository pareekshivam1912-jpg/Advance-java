<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Date, java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%!
    // =========================================================================
    // 1. JSP DECLARATION (syntax: < % ! ... % >)
    // Declares class-level member variables and methods in the generated Servlet.
    // Notice: These variables belong to the servlet instance and persist across
    // multiple HTTP requests, demonstrating that JSP instances are reused!
    // =========================================================================
    private int pageVisitCounter = 0;

    public synchronized int incrementVisitCounter() {
        return ++pageVisitCounter;
    }

    public String formatTimestamp(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z");
        return sdf.format(d);
    }

    public long calculateFactorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
%>

<%
    // =========================================================================
    // 2. JSP SCRIPTLET (syntax: < % ... % >)
    // Arbitrary Java code executed on every request inside the _jspService() method.
    // Local variables are created per request on the call stack.
    // =========================================================================
    int currentHits = incrementVisitCounter();
    Date requestTime = new Date();
    String formattedTime = formatTimestamp(requestTime);

    // Calculate sample computation
    int sampleNumber = 7;
    long factorialResult = calculateFactorial(sampleNumber);

    // Set request attribute for EL comparison
    request.setAttribute("demoTopic", "RTU Advanced Java - JSP Architecture & Syntax Elements");
%>

<%--
    ============================================================================
    3. JSP COMMENT (<%-- ... --%>)
    Unlike HTML comments (<!-- -->), JSP comments are stripped during translation
    and NEVER sent to the client browser or visible in page source!
    ============================================================================
--%>

<c:set var="pageTitle" value="Syllabus Demo: Traditional JSP Elements & Lifecycle — ShopSphere" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/demo/index.jsp" class="text-decoration-none">Syllabus Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">JSP Elements &amp; Lifecycle</li>
        </ol>
    </nav>

    <!-- Header Alert -->
    <div class="alert alert-warning border-warning shadow-sm rounded-4 p-4 mb-4">
        <div class="d-flex align-items-center mb-2">
            <i class="bi bi-mortarboard-fill fs-2 text-warning me-3"></i>
            <div>
                <h4 class="fw-bold mb-0 text-dark">RTU Syllabus Practical Demo: JSP Elements &amp; Execution Lifecycle</h4>
                <span class="small text-muted">Demonstrates Declarations, Scriptlets, Expressions, Directives, Comments, and 9 Implicit Objects</span>
            </div>
        </div>
        <hr class="text-warning opacity-25 my-2">
        <p class="small text-dark mb-0">
            <strong>Architecture Note:</strong> In production enterprise applications (like the rest of ShopSphere), business logic resides strictly in Servlets and DAOs, with JSTL/EL in views. This page is dedicated to demonstrating the classical low-level JSP translation mechanisms for university exam questions and viva evaluation.
        </p>
    </div>

    <!-- 1. Lifecycle Visualizer & Instance Reuse Proof -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-arrow-repeat text-primary me-2"></i>1. JSP Lifecycle &amp; Servlet Reuse Evidence</h5>
        <div class="p-3 bg-light rounded-3 mb-3 border">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <span class="text-muted small d-block">Declaration Variable (<code class="text-danger">&lt;%! private int pageVisitCounter; %&gt;</code>) Persistence:</span>
                    <h2 class="fw-bold text-primary mb-0">
                        Hit #<%= currentHits %>
                    </h2>
                    <small class="text-muted">
                        Reload this page (<kbd>Ctrl+R</kbd> or <kbd>F5</kbd>). Notice that this counter increments on every reload because the Web Container (Tomcat 11) creates exactly ONE Servlet instance and reuses it across requests!
                    </small>
                </div>
                <div class="col-md-4 text-md-end mt-3 mt-md-0">
                    <span class="badge bg-success-subtle text-success p-2 small">
                        <i class="bi bi-check-circle me-1"></i>Instance Reused: Thread-Safe
                    </span>
                </div>
            </div>
        </div>

        <h6 class="fw-bold text-dark mt-2 mb-3">JSP Translation &amp; Execution Phases:</h6>
        <div class="table-responsive">
            <table class="table table-bordered table-sm small align-middle mb-0">
                <thead class="table-secondary">
                    <tr>
                        <th>Phase</th>
                        <th>File / Action</th>
                        <th>Invoked Method</th>
                        <th>Timing</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>1. Translation</strong></td>
                        <td><code>jsp-elements.jsp</code> &rarr; <code>jsp_elements_jsp.java</code></td>
                        <td>Jasper Parser</td>
                        <td>First request (or precompilation)</td>
                    </tr>
                    <tr>
                        <td><strong>2. Compilation</strong></td>
                        <td><code>jsp_elements_jsp.java</code> &rarr; <code>jsp_elements_jsp.class</code></td>
                        <td>Java Compiler (javac)</td>
                        <td>First request</td>
                    </tr>
                    <tr>
                        <td><strong>3. Loading &amp; Instantiation</strong></td>
                        <td>JVM loads byte-code into memory</td>
                        <td><code>ClassLoader</code></td>
                        <td>Once per deployment</td>
                    </tr>
                    <tr>
                        <td><strong>4. Initialization</strong></td>
                        <td>Servlet lifecycle init</td>
                        <td><code>jspInit()</code> (or <code>_jspInit()</code>)</td>
                        <td>Once during instantiation</td>
                    </tr>
                    <tr>
                        <td><strong>5. Request Service</strong></td>
                        <td>Processes incoming HTTP request</td>
                        <td><code>_jspService(HttpServletRequest, HttpServletResponse)</code></td>
                        <td>Executed on <strong>every HTTP request</strong></td>
                    </tr>
                    <tr>
                        <td><strong>6. Destruction</strong></td>
                        <td>Undeployment / Tomcat shutdown</td>
                        <td><code>jspDestroy()</code> (or <code>_jspDestroy()</code>)</td>
                        <td>Once during server stop</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 2. Traditional JSP Scripting Elements -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-code-slash text-primary me-2"></i>2. Core JSP Scripting Elements Comparison</h5>

        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle small mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>Element Type</th>
                        <th>JSP Syntax</th>
                        <th>Translated Code Location</th>
                        <th>Live Output Demo</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Declaration</strong></td>
                        <td><code>&lt;%! int counter; long fact(int n) {...} %&gt;</code></td>
                        <td>Class body (outside <code>_jspService</code>)</td>
                        <td><code>calculateFactorial(7)</code> = <strong><%= factorialResult %></strong></td>
                    </tr>
                    <tr>
                        <td><strong>Scriptlet</strong></td>
                        <td><code>&lt;% int x = 10; ... %&gt;</code></td>
                        <td>Inside <code>_jspService()</code> method body</td>
                        <td>Calculated timestamp: <strong><%= formattedTime %></strong></td>
                    </tr>
                    <tr>
                        <td><strong>Expression</strong></td>
                        <td><code>&lt;%= expression %&gt;</code> (No semicolon!)</td>
                        <td>Converted to <code>out.print(expression);</code></td>
                        <td>Server Date: <strong><%= requestTime.toString() %></strong></td>
                    </tr>
                    <tr>
                        <td><strong>Directives</strong></td>
                        <td><code>&lt;%@ page ... %&gt;</code>, <code>&lt;%@ include ... %&gt;</code></td>
                        <td>Informs translation engine (imports, encoding)</td>
                        <td>Page encoding: <strong><%= response.getCharacterEncoding() %></strong></td>
                    </tr>
                    <tr>
                        <td><strong>JSP Comment</strong></td>
                        <td><code>&lt;%-- hidden server note --%&gt;</code></td>
                        <td>Completely discarded during translation</td>
                        <td><span class="text-success"><i class="bi bi-check-lg"></i> Verified: Never sent in HTTP response</span></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 3. All 9 Implicit Objects Reference -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-box-arrow-in-down-right text-primary me-2"></i>3. Comprehensive 9 Implicit Objects Table</h5>
        <p class="text-muted small">
            The Jakarta Web Container automatically declares and injects these 9 variables into the <code>_jspService()</code> method of every translated JSP page.
        </p>

        <div class="table-responsive">
            <table class="table table-bordered table-striped align-middle small mb-0">
                <thead class="table-primary">
                    <tr>
                        <th>Implicit Variable</th>
                        <th>Underlying Jakarta Type</th>
                        <th>Scope</th>
                        <th>Live Evaluated Value on this Request</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><code>request</code></td>
                        <td><code>jakarta.servlet.http.HttpServletRequest</code></td>
                        <td>Request</td>
                        <td>Method: <strong><%= request.getMethod() %></strong> | URI: <strong><%= request.getRequestURI() %></strong></td>
                    </tr>
                    <tr>
                        <td><code>response</code></td>
                        <td><code>jakarta.servlet.http.HttpServletResponse</code></td>
                        <td>Page</td>
                        <td>Status: <strong><%= response.getStatus() %></strong> | Content-Type: <strong><%= response.getContentType() %></strong></td>
                    </tr>
                    <tr>
                        <td><code>session</code></td>
                        <td><code>jakarta.servlet.http.HttpSession</code></td>
                        <td>Session</td>
                        <td>Session ID: <code><%= session.getId() %></code> | New? <strong><%= session.isNew() %></strong></td>
                    </tr>
                    <tr>
                        <td><code>application</code></td>
                        <td><code>jakarta.servlet.ServletContext</code></td>
                        <td>Application</td>
                        <td>Server: <strong><%= application.getServerInfo() %></strong> | Context: <code><%= application.getContextPath() %></code></td>
                    </tr>
                    <tr>
                        <td><code>out</code></td>
                        <td><code>jakarta.servlet.jsp.JspWriter</code></td>
                        <td>Page</td>
                        <td>Buffer Size: <strong><%= out.getBufferSize() %> bytes</strong> | Remaining: <strong><%= out.getRemaining() %> bytes</strong></td>
                    </tr>
                    <tr>
                        <td><code>config</code></td>
                        <td><code>jakarta.servlet.ServletConfig</code></td>
                        <td>Page</td>
                        <td>Servlet Name: <strong><%= config.getServletName() %></strong></td>
                    </tr>
                    <tr>
                        <td><code>pageContext</code></td>
                        <td><code>jakarta.servlet.jsp.PageContext</code></td>
                        <td>Page</td>
                        <td>Has Session? <strong><%= pageContext.getSession() != null %></strong></td>
                    </tr>
                    <tr>
                        <td><code>page</code></td>
                        <td><code>java.lang.Object</code> (reference to <code>this</code>)</td>
                        <td>Page</td>
                        <td>Class: <code><%= page.getClass().getName() %></code></td>
                    </tr>
                    <tr>
                        <td><code>exception</code></td>
                        <td><code>java.lang.Throwable</code></td>
                        <td>Page</td>
                        <td><em>Available only on error pages configured with <code>&lt;%@ page isErrorPage="true" %&gt;</code> (e.g. <code>error500.jsp</code>).</em></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Navigation Buttons -->
    <div class="d-flex justify-content-between align-items-center">
        <a href="${pageContext.request.contextPath}/demo/index.jsp" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-arrow-left me-1"></i>Back to Syllabus Demo Hub
        </a>
        <a href="${pageContext.request.contextPath}/demo/el-demo.jsp" class="btn btn-primary btn-sm">
            Next: Expression Language (EL) &amp; Scopes Demo <i class="bi bi-arrow-right ms-1"></i>
        </a>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
