<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Wishlist — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Catalog</a></li>
            <li class="breadcrumb-item active" aria-current="page">My Wishlist</li>
        </ol>
    </nav>

    <!-- Flash Alerts -->
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.flashSuccess}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashSuccess" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.flashError}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashError" scope="session"/>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="fw-bold mb-0"><i class="bi bi-heart-fill me-2 text-danger"></i>Saved Wishlist</h3>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-sm">
            <i class="bi bi-plus-circle me-1"></i>Explore More
        </a>
    </div>

    <c:choose>
        <c:when test="${empty wishlistItems}">
            <div class="card border-0 shadow-sm rounded-4 p-5 text-center my-4">
                <div class="mb-3 text-danger">
                    <i class="bi bi-heartbreak display-1"></i>
                </div>
                <h4 class="fw-bold">Your Wishlist is Empty</h4>
                <p class="text-muted small mb-4">Save favorite products here while browsing so you can purchase them anytime later!</p>
                <div>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary px-4 py-2 fw-semibold">
                        <i class="bi bi-bag-plus me-2"></i>Discover Items
                    </a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="item" items="${wishlistItems}">
                    <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                        <div class="card h-100 border-0 shadow-sm rounded-3 product-card position-relative overflow-hidden">
                            <!-- Discount Badge -->
                            <c:if test="${item.product.discountPercentage > 0}">
                                <span class="position-absolute top-0 start-0 m-2 badge bg-danger shadow-sm">
                                    <fmt:formatNumber value="${item.product.discountPercentage}" maxFractionDigits="0"/>% OFF
                                </span>
                            </c:if>

                            <!-- Product Image -->
                            <div class="product-img-container bg-light text-center p-3 d-flex align-items-center justify-content-center" style="height: 180px;">
                                <c:choose>
                                    <c:when test="${not empty item.product.imageUrl}">
                                        <img src="<c:out value='${item.product.imageUrl}'/>" class="img-fluid" style="max-height: 150px; object-fit: contain;" alt="<c:out value='${item.product.name}'/>" onerror="this.src='https://placehold.co/300x200?text=ShopSphere';">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-image text-muted fs-1"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="card-body p-3 d-flex flex-column">
                                <span class="badge bg-secondary-subtle text-secondary small align-self-start mb-1">
                                    <c:out value="${item.product.brand}"/>
                                </span>
                                <h6 class="card-title fw-semibold text-truncate mb-2" title="<c:out value='${item.product.name}'/>">
                                    <a href="${pageContext.request.contextPath}/product?id=${item.productId}" class="text-decoration-none text-dark">
                                        <c:out value="${item.product.name}"/>
                                    </a>
                                </h6>

                                <!-- Stock Status -->
                                <div class="mb-2">
                                    <c:choose>
                                        <c:when test="${item.product.outOfStock}">
                                            <span class="badge bg-danger-subtle text-danger small">Out of stock</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-success-subtle text-success small">In stock (${item.product.stock})</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Pricing -->
                                <div class="mt-auto pt-2 border-top">
                                    <div class="d-flex align-items-baseline gap-2 mb-3">
                                        <span class="fs-5 fw-bold text-primary">
                                            ₹<fmt:formatNumber value="${item.product.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/>
                                        </span>
                                        <c:if test="${item.product.discountPercentage > 0}">
                                            <span class="text-muted text-decoration-line-through small">
                                                ₹<fmt:formatNumber value="${item.product.price}" minFractionDigits="2" maxFractionDigits="2"/>
                                            </span>
                                        </c:if>
                                    </div>

                                    <!-- Actions -->
                                    <div class="d-grid gap-2">
                                        <c:choose>
                                            <c:when test="${item.product.outOfStock}">
                                                <button class="btn btn-secondary btn-sm disabled" disabled>
                                                    Out of Stock
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                                    <input type="hidden" name="action" value="moveToCart">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button type="submit" class="btn btn-primary btn-sm w-100">
                                                        <i class="bi bi-cart-plus me-1"></i>Move to Cart
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>

                                        <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm w-100">
                                                <i class="bi bi-trash me-1"></i>Remove
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</div>

<jsp:include page="fragments/footer.jsp"/>
