<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Syllabus Demo: JSTL XML Tag Library (x:) — ShopSphere" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/demo/index.jsp" class="text-decoration-none">Syllabus Demos</a></li>
            <li class="breadcrumb-item active" aria-current="page">JSTL XML Processing</li>
        </ol>
    </nav>

    <!-- Header Alert -->
    <div class="alert alert-danger border-danger shadow-sm rounded-4 p-4 mb-4">
        <div class="d-flex align-items-center mb-2">
            <i class="bi bi-filetype-xml fs-2 text-danger me-3"></i>
            <div>
                <h4 class="fw-bold mb-0 text-dark">RTU Syllabus Practical Demo: JSTL XML Tag Library (<code>jakarta.tags.xml</code>)</h4>
                <span class="small text-muted">Demonstrates XPath Parsing, Nodeset Iteration, Filtering, and Conditionals</span>
            </div>
        </div>
        <hr class="text-danger opacity-25 my-2">
        <p class="small text-dark mb-0">
            The JSTL XML library enables declarative XPath querying over XML data sources inside JSP pages without writing DOM or SAX parser boilerplate.
        </p>
    </div>

    <!-- XML Feed Stream -->
    <c:set var="xmlFeed">
<products>
    <product id="101">
        <name>Apple iPhone 16 Pro Max</name>
        <category>Mobiles</category>
        <price>144900.00</price>
        <stock>18</stock>
        <rating>4.9</rating>
    </product>
    <product id="102">
        <name>Samsung Galaxy S24 Ultra</name>
        <category>Mobiles</category>
        <price>129999.00</price>
        <stock>22</stock>
        <rating>4.8</rating>
    </product>
    <product id="103">
        <name>MacBook Pro 16-inch M3 Max</name>
        <category>Laptops</category>
        <price>349900.00</price>
        <stock>7</stock>
        <rating>4.9</rating>
    </product>
    <product id="104">
        <name>Sony WH-1000XM5 Noise Cancelling</name>
        <category>Electronics</category>
        <price>29990.00</price>
        <stock>45</stock>
        <rating>4.7</rating>
    </product>
</products>
    </c:set>
    <x:parse var="parsedDoc" xml="${xmlFeed}"/>

    <!-- Live XML Parsing Table -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-table text-primary me-2"></i>Parsed XML Products Stream (from <code>products-feed.xml</code>)</h5>

        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle small mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>XML ID Attribute</th>
                        <th>Product Name (<code>&lt;x:out&gt;</code>)</th>
                        <th>Category</th>
                        <th>Price (₹)</th>
                        <th>Stock Status (<code>&lt;x:if&gt;</code>)</th>
                        <th>Pricing Tier (<code>&lt;x:choose&gt;</code>)</th>
                    </tr>
                </thead>
                <tbody>
                    <x:forEach select="$parsedDoc/products/product" var="item">
                        <tr>
                            <td><code>#<x:out select="$item/@id"/></code></td>
                            <td><strong><x:out select="$item/name"/></strong></td>
                            <td><span class="badge bg-secondary-subtle text-secondary"><x:out select="$item/category"/></span></td>
                            <td class="fw-bold text-primary">₹<x:out select="$item/price"/></td>
                            <td>
                                <x:if select="$item/stock = 0">
                                    <span class="badge bg-danger">Out of stock (0 units)</span>
                                </x:if>
                                <x:if select="$item/stock > 0">
                                    <span class="badge bg-success">In stock (<x:out select="$item/stock"/> units)</span>
                                </x:if>
                            </td>
                            <td>
                                <x:choose>
                                    <x:when select="$item/price >= 100000">
                                        <span class="badge bg-warning text-dark"><i class="bi bi-gem me-1"></i>Ultra-Premium</span>
                                    </x:when>
                                    <x:when select="$item/price >= 50000">
                                        <span class="badge bg-info text-dark">Mid-Tier Pro</span>
                                    </x:when>
                                    <x:otherwise>
                                        <span class="badge bg-light text-dark border">Consumer Grade</span>
                                    </x:otherwise>
                                </x:choose>
                            </td>
                        </tr>
                    </x:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Code Explanation Card -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-3"><i class="bi bi-code-square text-primary me-2"></i>JSTL XML Core Syntax Reference</h5>
        <div class="table-responsive">
            <table class="table table-bordered small align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Tag</th>
                        <th>Attribute</th>
                        <th>Purpose</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><code>&lt;x:parse&gt;</code></td>
                        <td><code>var="parsedDoc" xml="\${xmlFeed}"</code></td>
                        <td>Parses XML string into an in-memory XPath-navigable document model.</td>
                    </tr>
                    <tr>
                        <td><code>&lt;x:forEach&gt;</code></td>
                        <td><code>select="$parsedDoc/products/product"</code></td>
                        <td>Iterates over the evaluated XPath nodeset collection.</td>
                    </tr>
                    <tr>
                        <td><code>&lt;x:out&gt;</code></td>
                        <td><code>select="name"</code></td>
                        <td>Extracts text content or attribute value from current XPath context.</td>
                    </tr>
                    <tr>
                        <td><code>&lt;x:if&gt;</code></td>
                        <td><code>select="stock = 0"</code></td>
                        <td>Evaluates boolean XPath predicate and renders body conditionally.</td>
                    </tr>
                    <tr>
                        <td><code>&lt;x:choose&gt;</code></td>
                        <td>Nested <code>&lt;x:when&gt;</code>, <code>&lt;x:otherwise&gt;</code></td>
                        <td>Multi-branch XPath switch condition evaluation.</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Navigation -->
    <div class="d-flex justify-content-between align-items-center">
        <a href="${pageContext.request.contextPath}/demo/el-demo.jsp" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-arrow-left me-1"></i>Previous: EL Demo
        </a>
        <a href="${pageContext.request.contextPath}/demo/jstl-sql.jsp" class="btn btn-primary btn-sm">
            Next: JSTL SQL Tag Library Demo <i class="bi bi-arrow-right ms-1"></i>
        </a>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
