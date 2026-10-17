<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.DatabaseMetaData" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/fragments/header.jsp">
    <jsp:param name="title" value="JNDI & DataSource Demo — RTU Advanced Java"/>
</jsp:include>
<jsp:include page="/fragments/navbar.jsp"/>

<div class="container py-5">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/demo/index.jsp">Demo Hub</a></li>
            <li class="breadcrumb-item active" aria-current="page">JNDI & Naming Service</li>
        </ol>
    </nav>

    <!-- Header Banner -->
    <div class="p-4 mb-4 rounded-3 text-white" style="background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 100%);">
        <div class="d-flex align-items-center justify-content-between">
            <div>
                <span class="badge bg-warning text-dark mb-2">RTU Advanced Java Syllabus</span>
                <h1 class="h3 fw-bold mb-1">Java Naming and Directory Interface (JNDI) Demonstration</h1>
                <p class="text-light mb-0 small">
                    Demonstrating JNDI <code>InitialContext</code>, <code>java:comp/env</code> namespace, Environment Entries, and Tomcat Connection Pooling.
                </p>
            </div>
            <i class="bi bi-diagram-3-fill fs-1 text-info opacity-75"></i>
        </div>
    </div>

    <%
        boolean jndiSuccess = false;
        String errorMessage = null;
        String academicEnv = null;
        Integer maxUpload = null;
        int totalProducts = 0;
        double avgPrice = 0.0;
        String dbProductName = null;
        String dbProductVersion = null;
        String driverName = null;

        try {
            // 1. Obtain root JNDI Context
            Context initCtx = new InitialContext();

            // 2. Lookup standard Jakarta EE web application environment context
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            // 3. Lookup environment entries configured in web.xml / context.xml
            try {
                academicEnv = (String) envCtx.lookup("academicEnvironment");
            } catch (Exception ignored) {
                academicEnv = "Default Local Dev";
            }

            try {
                maxUpload = (Integer) envCtx.lookup("maxUploadSizeMB");
            } catch (Exception ignored) {
                maxUpload = 10;
            }

            // 4. Lookup Managed DataSource connection pool
            DataSource ds = (DataSource) envCtx.lookup("jdbc/shopsphereDS");

            // 5. Borrow connection from pool and query database
            if (ds != null) {
                try (Connection conn = ds.getConnection()) {
                    DatabaseMetaData meta = conn.getMetaData();
                    dbProductName = meta.getDatabaseProductName();
                    dbProductVersion = meta.getDatabaseProductVersion();
                    driverName = meta.getDriverName();

                    String sql = "SELECT COUNT(*) AS total_count, COALESCE(AVG(price), 0) AS avg_val FROM products";
                    try (PreparedStatement ps = conn.prepareStatement(sql);
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            totalProducts = rs.getInt("total_count");
                            avgPrice = rs.getDouble("avg_val");
                            jndiSuccess = true;
                        }
                    }
                }
            }
        } catch (NamingException ne) {
            errorMessage = "JNDI NamingException: " + ne.getMessage() + 
                " (Ensure the application is running inside Apache Tomcat container with META-INF/context.xml loaded).";
        } catch (Exception e) {
            errorMessage = "Database / SQL Exception via JNDI: " + e.getMessage();
        }

        pageContext.setAttribute("jndiSuccess", jndiSuccess);
        pageContext.setAttribute("errorMessage", errorMessage);
        pageContext.setAttribute("academicEnv", academicEnv);
        pageContext.setAttribute("maxUpload", maxUpload);
        pageContext.setAttribute("totalProducts", totalProducts);
        pageContext.setAttribute("avgPrice", avgPrice);
        pageContext.setAttribute("dbProductName", dbProductName);
        pageContext.setAttribute("dbProductVersion", dbProductVersion);
        pageContext.setAttribute("driverName", driverName);
    %>

    <div class="row g-4 mb-4">
        <!-- Status Card -->
        <div class="col-md-6">
            <div class="card h-100 shadow-sm border-${jndiSuccess ? 'success' : 'warning'}">
                <div class="card-header bg-transparent fw-bold d-flex justify-content-between align-items-center">
                    <span><i class="bi bi-search me-2"></i>JNDI Lookup Status</span>
                    <span class="badge bg-${jndiSuccess ? 'success' : 'warning'}">${jndiSuccess ? 'RESOLVED' : 'CONTAINER NOTICE'}</span>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${jndiSuccess}">
                            <div class="alert alert-success d-flex align-items-center mb-3">
                                <i class="bi bi-check-circle-fill fs-4 me-3"></i>
                                <div>
                                    <strong>DataSource Lookup Successful!</strong><br>
                                    Retrieved managed connection pool from <code>java:comp/env/jdbc/shopsphereDS</code>.
                                </div>
                            </div>
                            <ul class="list-group list-group-flush small">
                                <li class="list-group-item d-flex justify-content-between">
                                    <span class="text-muted">Target RDBMS:</span>
                                    <strong class="text-primary">${dbProductName} (${dbProductVersion})</strong>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <span class="text-muted">JDBC Driver:</span>
                                    <span>${driverName}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <span class="text-muted">Catalog Product Count:</span>
                                    <span class="badge bg-primary rounded-pill">${totalProducts} records</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <span class="text-muted">Average Catalog Price:</span>
                                    <strong class="text-success">₹<fmt:formatNumber value="${avgPrice}" pattern="#,##0.00"/></strong>
                                </li>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info">
                                <h6 class="fw-bold"><i class="bi bi-info-circle me-1"></i> Container Context Explanation</h6>
                                <p class="small mb-2">${errorMessage}</p>
                                <hr>
                                <p class="small text-muted mb-0">
                                    Tomcat automatically registers JNDI resources defined in <code>META-INF/context.xml</code> upon container boot. When deployed as <code>shopsphere.war</code> in Tomcat 11, the connection pool binds to the JNDI naming hierarchy.
                                </p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Environment Entries Card -->
        <div class="col-md-6">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-transparent fw-bold">
                    <i class="bi bi-sliders me-2"></i>JNDI Environment Entries (<code>&lt;env-entry&gt;</code>)
                </div>
                <div class="card-body">
                    <p class="small text-muted">
                        JNDI allows decoupling operational configuration from application bytecode. Developers configure parameters in <code>web.xml</code> or <code>context.xml</code>, accessed via standard string keys:
                    </p>
                    <table class="table table-bordered table-sm small align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>JNDI Lookup Path</th>
                                <th>Type</th>
                                <th>Resolved Value</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><code>java:comp/env/academicEnvironment</code></td>
                                <td><code>String</code></td>
                                <td><span class="badge bg-secondary">${academicEnv != null ? academicEnv : 'Not Bound'}</span></td>
                            </tr>
                            <tr>
                                <td><code>java:comp/env/maxUploadSizeMB</code></td>
                                <td><code>Integer</code></td>
                                <td><span class="badge bg-info text-dark">${maxUpload != null ? maxUpload : '10'} MB</span></td>
                            </tr>
                            <tr>
                                <td><code>java:comp/env/jdbc/shopsphereDS</code></td>
                                <td><code>DataSource</code></td>
                                <td><span class="badge bg-${jndiSuccess ? 'success' : 'secondary'}">${jndiSuccess ? 'Active Pool' : 'Configured'}</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Syllabus Architecture Theory -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-dark text-white fw-bold">
            <i class="bi bi-mortarboard-fill me-2"></i>RTU Viva & Theory Reference: JNDI vs RMI Naming vs DriverManager
        </div>
        <div class="card-body small">
            <div class="row g-4">
                <div class="col-md-4">
                    <h6 class="fw-bold text-primary">1. Traditional DriverManager</h6>
                    <p class="text-muted">
                        Direct point-to-point connection creation. Every call creates an expensive physical socket handshake with MySQL. Credentials and DB URLs are hardcoded in application code or configuration properties files.
                    </p>
                </div>
                <div class="col-md-4">
                    <h6 class="fw-bold text-success">2. JNDI Managed Connection Pool</h6>
                    <p class="text-muted">
                        The Servlet container (Tomcat) creates and maintains a pool of pre-warmed database connections. Applications perform a lightweight directory lookup using <code>new InitialContext().lookup(...)</code>, borrow a connection, and return it upon <code>close()</code>.
                    </p>
                </div>
                <div class="col-md-4">
                    <h6 class="fw-bold text-info">3. RMI Naming Service</h6>
                    <p class="text-muted">
                        While JNDI is an enterprise directory SPI (supporting LDAP, DNS, and Container Environment), <code>java.rmi.Naming</code> is a lightweight naming service specifically designed to bind and look up RMI remote object stubs across JVM processes.
                    </p>
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
