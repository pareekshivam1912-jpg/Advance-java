<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="ShopSphere — Enterprise E-Commerce Storefront" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<!-- Hero Carousel / Banner -->
<div class="bg-dark text-white py-5 mb-5 shadow-sm" style="background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);">
    <div class="container py-4">
        <div class="row align-items-center g-5">
            <div class="col-lg-7">
                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill fw-bold mb-3 shadow-sm">
                    <i class="bi bi-cpu-fill me-1"></i> Jakarta EE 10 &bull; Tomcat 11 &bull; OpenJDK 21
                </span>
                <h1 class="display-4 fw-extrabold mb-3 lh-sm">
                    Modern E-Commerce Built on Enterprise Java Architecture
                </h1>
                <p class="lead text-secondary mb-4">
                    Explore high-performance electronics, gadgets, and accessories powered by clean MVC Servlets, PreparedStatement JDBC, and secure session management.
                </p>
                <div class="d-flex flex-wrap gap-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg px-4 fw-semibold shadow-sm">
                        <i class="bi bi-bag-fill me-2"></i>Explore Catalog
                    </a>
                    <a href="${pageContext.request.contextPath}/demo/index.jsp" class="btn btn-outline-warning btn-lg px-4 fw-semibold shadow-sm">
                        <i class="bi bi-mortarboard me-2"></i>Syllabus Demos
                    </a>
                </div>
            </div>
            <div class="col-lg-5 text-center d-none d-lg-block">
                <div class="card bg-secondary bg-opacity-10 border border-secondary border-opacity-25 rounded-4 p-4 text-start shadow">
                    <div class="d-flex align-items-center mb-3">
                        <div class="bg-primary rounded-circle p-2 text-white me-3">
                            <i class="bi bi-diagram-3-fill fs-4"></i>
                        </div>
                        <div>
                            <h6 class="mb-0 text-white fw-bold">N-Tier Layered Design</h6>
                            <small class="text-secondary">Controller &rarr; Service &rarr; DAO &rarr; MySQL</small>
                        </div>
                    </div>
                    <ul class="list-unstyled text-light small mb-0">
                        <li class="mb-2"><i class="bi bi-check2-circle text-success me-2"></i>Pure JDBC with PreparedStatement (Zero SQL Injection)</li>
                        <li class="mb-2"><i class="bi bi-check2-circle text-success me-2"></i>Jakarta Servlet 6.1 &amp; JSP 3.1 with JSTL</li>
                        <li class="mb-2"><i class="bi bi-check2-circle text-success me-2"></i>Atomic Multi-Table Checkout Transactions</li>
                        <li><i class="bi bi-check2-circle text-success me-2"></i>Swing MVC Desktop Admin + Sockets &amp; RMI</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Main Storefront Content -->
<div class="container pb-5">

    <!-- Value Highlights -->
    <div class="row g-4 mb-5 text-center">
        <div class="col-6 col-md-3">
            <div class="p-3 border rounded-3 bg-white shadow-sm h-100">
                <i class="bi bi-truck fs-1 text-primary mb-2 d-block"></i>
                <h6 class="fw-bold mb-1">Fast Delivery</h6>
                <p class="text-muted small mb-0">Direct express dispatch</p>
            </div>
        </div>
        <div class="col-6 col-md-3">
            <div class="p-3 border rounded-3 bg-white shadow-sm h-100">
                <i class="bi bi-shield-lock-fill fs-1 text-success mb-2 d-block"></i>
                <h6 class="fw-bold mb-1">Secure Payments</h6>
                <p class="text-muted small mb-0">PBKDF2 Salted Cryptography</p>
            </div>
        </div>
        <div class="col-6 col-md-3">
            <div class="p-3 border rounded-3 bg-white shadow-sm h-100">
                <i class="bi bi-tag-fill fs-1 text-danger mb-2 d-block"></i>
                <h6 class="fw-bold mb-1">Coupon Discounts</h6>
                <p class="text-muted small mb-0">Server-side verified pricing</p>
            </div>
        </div>
        <div class="col-6 col-md-3">
            <div class="p-3 border rounded-3 bg-white shadow-sm h-100">
                <i class="bi bi-headset fs-1 text-info mb-2 d-block"></i>
                <h6 class="fw-bold mb-1">24/7 Support</h6>
                <p class="text-muted small mb-0">Academic &amp; Student friendly</p>
            </div>
        </div>
    </div>

    <!-- Featured Categories -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-0">Shop by Category</h3>
                <p class="text-muted small mb-0">Explore premium electronics and consumer products</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-sm">
                View All <i class="bi bi-arrow-right ms-1"></i>
            </a>
        </div>

        <div class="row g-3">
            <c:forEach var="cat" items="${categories}">
                <div class="col-6 col-md-4 col-lg-2">
                    <a href="${pageContext.request.contextPath}/products?category=${cat.id}" class="text-decoration-none text-dark">
                        <div class="card h-100 border text-center p-3 hover-shadow rounded-3 transition-all">
                            <div class="category-icon-wrapper mx-auto mb-2 bg-light rounded-circle d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                <c:choose>
                                    <c:when test="${cat.id == 1}"><i class="bi bi-tv fs-3 text-primary"></i></c:when>
                                    <c:when test="${cat.id == 2}"><i class="bi bi-laptop fs-3 text-success"></i></c:when>
                                    <c:when test="${cat.id == 3}"><i class="bi bi-phone fs-3 text-danger"></i></c:when>
                                    <c:when test="${cat.id == 4}"><i class="bi bi-bag-heart fs-3 text-warning"></i></c:when>
                                    <c:when test="${cat.id == 5}"><i class="bi bi-book fs-3 text-info"></i></c:when>
                                    <c:otherwise><i class="bi bi-house-door fs-3 text-secondary"></i></c:otherwise>
                                </c:choose>
                            </div>
                            <h6 class="card-title fw-bold mb-1 small"><c:out value="${cat.name}"/></h6>
                            <span class="text-muted x-small">Browse items &rarr;</span>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Featured Products -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-0">Featured Products</h3>
                <p class="text-muted small mb-0">Handpicked top deals and trending selections</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-sm">
                Browse All <i class="bi bi-arrow-right ms-1"></i>
            </a>
        </div>

        <div class="row g-4">
            <c:forEach var="prod" items="${featuredProducts}">
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="card h-100 border-0 shadow-sm rounded-3 product-card position-relative overflow-hidden">
                        <!-- Discount Badge -->
                        <c:if test="${prod.discountPercentage > 0}">
                            <span class="position-absolute top-0 start-0 m-2 badge bg-danger shadow-sm">
                                <fmt:formatNumber value="${prod.discountPercentage}" maxFractionDigits="0"/>% OFF
                            </span>
                        </c:if>

                        <!-- Product Image -->
                        <div class="product-img-container bg-light text-center p-3 d-flex align-items-center justify-content-center" style="height: 190px;">
                            <c:choose>
                                <c:when test="${not empty prod.imageUrl}">
                                    <img src="<c:out value='${prod.imageUrl}'/>" class="img-fluid" style="max-height: 160px; object-fit: contain;" alt="<c:out value='${prod.name}'/>" onerror="this.src='https://placehold.co/300x200?text=ShopSphere';">
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-image text-muted fs-1"></i>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="card-body p-3 d-flex flex-column">
                            <span class="badge bg-secondary-subtle text-secondary small align-self-start mb-1">
                                <c:out value="${prod.brand}"/>
                            </span>
                            <h6 class="card-title fw-semibold text-truncate mb-1" title="<c:out value='${prod.name}'/>">
                                <a href="${pageContext.request.contextPath}/product?id=${prod.id}" class="text-decoration-none text-dark">
                                    <c:out value="${prod.name}"/>
                                </a>
                            </h6>

                            <!-- Ratings -->
                            <div class="d-flex align-items-center small text-warning mb-2">
                                <i class="bi bi-star-fill"></i>
                                <span class="ms-1 fw-bold text-dark"><fmt:formatNumber value="${prod.rating}" minFractionDigits="1" maxFractionDigits="1"/></span>
                                <span class="text-muted ms-1 small">/ 5</span>
                            </div>

                            <!-- Pricing -->
                            <div class="mt-auto pt-2 border-top">
                                <div class="d-flex align-items-baseline gap-2 mb-2">
                                    <span class="fs-5 fw-bold text-primary">
                                        ₹<fmt:formatNumber value="${prod.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/>
                                    </span>
                                    <c:if test="${prod.discountPercentage > 0}">
                                        <span class="text-muted text-decoration-line-through small">
                                            ₹<fmt:formatNumber value="${prod.price}" minFractionDigits="2" maxFractionDigits="2"/>
                                        </span>
                                    </c:if>
                                </div>

                                <!-- Action Buttons -->
                                <div class="d-grid gap-2">
                                    <c:choose>
                                        <c:when test="${prod.outOfStock}">
                                            <button class="btn btn-secondary btn-sm disabled" disabled>
                                                <i class="bi bi-x-circle me-1"></i>Out of Stock
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="btn-group btn-group-sm">
                                                <a href="${pageContext.request.contextPath}/product?id=${prod.id}" class="btn btn-outline-secondary">
                                                    View Details
                                                </a>
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline flex-grow-1">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="productId" value="${prod.id}">
                                                    <input type="hidden" name="quantity" value="1">
                                                    <button type="submit" class="btn btn-primary w-100 rounded-start-0">
                                                        <i class="bi bi-cart-plus me-1"></i>Add
                                                    </button>
                                                </form>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Recent New Arrivals -->
    <div class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-0">New Arrivals</h3>
                <p class="text-muted small mb-0">Freshly stocked items in our catalogue</p>
            </div>
            <a href="${pageContext.request.contextPath}/products?sort=newest" class="btn btn-outline-primary btn-sm">
                View All New <i class="bi bi-arrow-right ms-1"></i>
            </a>
        </div>

        <div class="row g-4">
            <c:forEach var="prod" items="${recentProducts}">
                <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                    <div class="card h-100 border-0 shadow-sm rounded-3 product-card position-relative overflow-hidden">
                        <c:if test="${prod.discountPercentage > 0}">
                            <span class="position-absolute top-0 start-0 m-2 badge bg-danger shadow-sm">
                                <fmt:formatNumber value="${prod.discountPercentage}" maxFractionDigits="0"/>% OFF
                            </span>
                        </c:if>

                        <div class="product-img-container bg-light text-center p-3 d-flex align-items-center justify-content-center" style="height: 190px;">
                            <c:choose>
                                <c:when test="${not empty prod.imageUrl}">
                                    <img src="<c:out value='${prod.imageUrl}'/>" class="img-fluid" style="max-height: 160px; object-fit: contain;" alt="<c:out value='${prod.name}'/>" onerror="this.src='https://placehold.co/300x200?text=ShopSphere';">
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-image text-muted fs-1"></i>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="card-body p-3 d-flex flex-column">
                            <span class="badge bg-secondary-subtle text-secondary small align-self-start mb-1">
                                <c:out value="${prod.brand}"/>
                            </span>
                            <h6 class="card-title fw-semibold text-truncate mb-1" title="<c:out value='${prod.name}'/>">
                                <a href="${pageContext.request.contextPath}/product?id=${prod.id}" class="text-decoration-none text-dark">
                                    <c:out value="${prod.name}"/>
                                </a>
                            </h6>

                            <div class="d-flex align-items-center small text-warning mb-2">
                                <i class="bi bi-star-fill"></i>
                                <span class="ms-1 fw-bold text-dark"><fmt:formatNumber value="${prod.rating}" minFractionDigits="1" maxFractionDigits="1"/></span>
                                <span class="text-muted ms-1 small">/ 5</span>
                            </div>

                            <div class="mt-auto pt-2 border-top">
                                <div class="d-flex align-items-baseline gap-2 mb-2">
                                    <span class="fs-5 fw-bold text-primary">
                                        ₹<fmt:formatNumber value="${prod.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/>
                                    </span>
                                    <c:if test="${prod.discountPercentage > 0}">
                                        <span class="text-muted text-decoration-line-through small">
                                            ₹<fmt:formatNumber value="${prod.price}" minFractionDigits="2" maxFractionDigits="2"/>
                                        </span>
                                    </c:if>
                                </div>

                                <div class="d-grid gap-2">
                                    <c:choose>
                                        <c:when test="${prod.outOfStock}">
                                            <button class="btn btn-secondary btn-sm disabled" disabled>
                                                <i class="bi bi-x-circle me-1"></i>Out of Stock
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="btn-group btn-group-sm">
                                                <a href="${pageContext.request.contextPath}/product?id=${prod.id}" class="btn btn-outline-secondary">
                                                    View Details
                                                </a>
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline flex-grow-1">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="productId" value="${prod.id}">
                                                    <input type="hidden" name="quantity" value="1">
                                                    <button type="submit" class="btn btn-primary w-100 rounded-start-0">
                                                        <i class="bi bi-cart-plus me-1"></i>Add
                                                    </button>
                                                </form>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Recently Viewed Products (Cookie Based) -->
    <c:if test="${not empty recentlyViewed}">
        <div class="mb-5 p-4 bg-light rounded-4 border">
            <div class="d-flex align-items-center mb-3">
                <i class="bi bi-clock-history fs-4 text-primary me-2"></i>
                <h4 class="fw-bold mb-0">Recently Viewed by You</h4>
                <span class="badge bg-secondary ms-2 small">Saved via HTTP Cookie</span>
            </div>
            <div class="row g-3">
                <c:forEach var="prod" items="${recentlyViewed}">
                    <div class="col-6 col-md-3">
                        <div class="card h-100 border-0 shadow-sm p-2 text-center">
                            <a href="${pageContext.request.contextPath}/product?id=${prod.id}" class="text-decoration-none text-dark">
                                <div class="bg-white p-2 rounded mb-2" style="height: 100px; display: flex; align-items: center; justify-content: center;">
                                    <img src="<c:out value='${prod.imageUrl}'/>" style="max-height: 80px; max-width: 100%; object-fit: contain;" alt="<c:out value='${prod.name}'/>" onerror="this.src='https://placehold.co/100x80?text=ShopSphere';">
                                </div>
                                <h6 class="small fw-semibold text-truncate mb-1"><c:out value="${prod.name}"/></h6>
                                <span class="fw-bold text-primary small">₹<fmt:formatNumber value="${prod.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/></span>
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>

</div>

<jsp:include page="fragments/footer.jsp"/>
