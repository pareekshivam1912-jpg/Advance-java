<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="pageTitle" value="${product.name} — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Catalog</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products?category=${product.categoryId}" class="text-decoration-none"><c:out value="${product.categoryName}"/></a></li>
            <li class="breadcrumb-item active text-truncate" style="max-width: 300px;" aria-current="page"><c:out value="${product.name}"/></li>
        </ol>
    </nav>

    <!-- Feedback Alerts -->
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

    <!-- Product Main Detail Card -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-5">
        <div class="row g-5 align-items-center">

            <!-- Product Image Showcase -->
            <div class="col-lg-5 text-center">
                <div class="bg-light rounded-4 p-4 position-relative d-flex align-items-center justify-content-center" style="min-height: 380px;">
                    <c:if test="${product.discountPercentage > 0}">
                        <span class="position-absolute top-0 start-0 m-3 badge bg-danger shadow-sm fs-6">
                            <fmt:formatNumber value="${product.discountPercentage}" maxFractionDigits="0"/>% OFF
                        </span>
                    </c:if>
                    <c:choose>
                        <c:when test="${not empty product.imageUrl}">
                            <img src="<c:out value='${product.imageUrl}'/>" class="img-fluid rounded" style="max-height: 320px; object-fit: contain;" alt="<c:out value='${product.name}'/>" onerror="this.src='https://placehold.co/400x300?text=ShopSphere';">
                        </c:when>
                        <c:otherwise>
                            <i class="bi bi-image text-muted display-1"></i>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Product Specs & Actions -->
            <div class="col-lg-7">
                <div class="d-flex align-items-center gap-2 mb-2">
                    <span class="badge bg-secondary-subtle text-secondary px-3 py-1 fw-semibold">
                        <c:out value="${product.brand}"/>
                    </span>
                    <span class="badge bg-light text-muted border px-2 py-1">
                        Category: <c:out value="${product.categoryName}"/>
                    </span>
                    <span class="text-muted small ms-auto">SKU: PROD-${product.id}</span>
                </div>

                <h2 class="fw-bold mb-2 text-dark"><c:out value="${product.name}"/></h2>

                <!-- Rating and Reviews Count -->
                <div class="d-flex align-items-center gap-2 mb-3">
                    <div class="text-warning">
                        <c:forEach begin="1" end="5" var="star">
                            <c:choose>
                                <c:when test="${product.rating >= star}">
                                    <i class="bi bi-star-fill"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-star"></i>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <span class="fw-bold text-dark"><fmt:formatNumber value="${product.rating}" minFractionDigits="1" maxFractionDigits="1"/></span>
                    <span class="text-muted small">(${fn:length(reviews)} customer reviews)</span>
                </div>

                <hr class="text-muted">

                <!-- Pricing Display -->
                <div class="mb-4">
                    <div class="d-flex align-items-baseline gap-3">
                        <span class="display-6 fw-bold text-primary">
                            ₹<fmt:formatNumber value="${product.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/>
                        </span>
                        <c:if test="${product.discountPercentage > 0}">
                            <span class="fs-5 text-muted text-decoration-line-through">
                                ₹<fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/>
                            </span>
                            <span class="badge bg-success-subtle text-success fw-bold px-2 py-1">
                                Save ₹<fmt:formatNumber value="${product.price - product.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/>
                            </span>
                        </c:if>
                    </div>
                    <small class="text-muted">Inclusive of all taxes &bull; Free standard shipping</small>
                </div>

                <!-- Stock Badge -->
                <div class="mb-4">
                    <c:choose>
                        <c:when test="${product.outOfStock}">
                            <div class="badge bg-danger-subtle text-danger px-3 py-2 fs-6">
                                <i class="bi bi-x-circle me-1"></i>Currently Out of Stock
                            </div>
                        </c:when>
                        <c:when test="${product.isLowStock(5)}">
                            <div class="badge bg-warning-subtle text-warning-emphasis px-3 py-2 fs-6">
                                <i class="bi bi-exclamation-triangle me-1"></i>Only ${product.stock} items left in stock!
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="badge bg-success-subtle text-success px-3 py-2 fs-6">
                                <i class="bi bi-check-circle me-1"></i>In Stock (${product.stock} available)
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Add to Cart & Actions Form -->
                <div class="mb-4">
                    <c:choose>
                        <c:when test="${product.outOfStock}">
                            <button class="btn btn-secondary btn-lg disabled px-5" disabled>
                                Out of Stock
                            </button>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="row g-3 align-items-center">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.id}">

                                <div class="col-auto">
                                    <label for="quantitySelect" class="form-label small fw-semibold mb-0">Qty:</label>
                                </div>
                                <div class="col-auto">
                                    <select class="form-select" id="quantitySelect" name="quantity" style="width: 80px;">
                                        <c:forEach begin="1" end="${product.stock > 10 ? 10 : product.stock}" var="q">
                                            <option value="${q}">${q}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-auto flex-grow-1">
                                    <button type="submit" class="btn btn-primary btn-lg w-100 fw-bold shadow-sm">
                                        <i class="bi bi-cart-plus me-2"></i>Add to Cart
                                    </button>
                                </div>
                            </form>
                        </c:otherwise>
                    </c:choose>

                    <!-- Wishlist Button -->
                    <form action="${pageContext.request.contextPath}/wishlist" method="post" class="mt-2">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit" class="btn btn-outline-danger btn-sm">
                            <i class="bi bi-heart me-1"></i>Save to Wishlist
                        </button>
                    </form>
                </div>

                <!-- Description Overview -->
                <div class="p-3 bg-light rounded-3">
                    <h6 class="fw-bold mb-2">Description</h6>
                    <p class="text-secondary small mb-0 lh-base">
                        <c:out value="${product.description}"/>
                    </p>
                </div>

            </div>
        </div>
    </div>

    <!-- Customer Reviews Section -->
    <div class="row g-4 mb-5">
        <div class="col-lg-7">
            <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4 class="fw-bold mb-0">Customer Reviews (${fn:length(reviews)})</h4>
                    <span class="badge bg-primary fs-6">Avg: <fmt:formatNumber value="${product.rating}" minFractionDigits="1" maxFractionDigits="1"/> / 5</span>
                </div>

                <c:choose>
                    <c:when test="${empty reviews}">
                        <div class="text-center py-5 text-muted">
                            <i class="bi bi-chat-square-quote display-4 d-block mb-2 text-secondary"></i>
                            <p class="mb-0">No reviews yet for this product. Be the first to share your thoughts!</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="reviews-list">
                            <c:forEach var="rev" items="${reviews}">
                                <div class="border-bottom pb-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <span class="fw-bold text-dark">
                                            <i class="bi bi-person-circle text-primary me-1"></i><c:out value="${rev.userName}"/>
                                        </span>
                                        <div class="text-warning small">
                                            <c:forEach begin="1" end="5" var="star">
                                                <i class="bi bi-star${rev.rating >= star ? '-fill' : ''}"></i>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <p class="text-secondary small mb-1"><c:out value="${rev.comment}"/></p>
                                    <small class="text-muted"><fmt:formatDate value="${rev.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></small>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Add Review Form -->
        <div class="col-lg-5">
            <div class="card border-0 shadow-sm rounded-4 p-4 h-100">
                <h5 class="fw-bold mb-3"><i class="bi bi-pencil-square me-2 text-primary"></i>Write a Review</h5>

                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <div class="p-4 bg-light rounded-3 text-center my-auto">
                            <p class="text-muted small mb-3">You must be logged in to rate and review products.</p>
                            <a href="${pageContext.request.contextPath}/login?redirect=${pageContext.request.contextPath}/product?id=${product.id}" class="btn btn-outline-primary btn-sm px-4">
                                Sign In to Review
                            </a>
                        </div>
                    </c:when>
                    <c:when test="${not empty userReview}">
                        <div class="alert alert-info small mb-0">
                            <i class="bi bi-info-circle-fill me-1"></i>You reviewed this item on <fmt:formatDate value="${userReview.createdAt}" pattern="dd MMM yyyy"/> with <strong>${userReview.rating}/5 stars</strong>.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <form action="${pageContext.request.contextPath}/review" method="post">
                            <input type="hidden" name="productId" value="${product.id}">

                            <div class="mb-3">
                                <label for="ratingInput" class="form-label small fw-semibold">Your Rating</label>
                                <select class="form-select form-select-sm" id="ratingInput" name="rating" required>
                                    <option value="5">★★★★★ (5 - Excellent)</option>
                                    <option value="4">★★★★☆ (4 - Good)</option>
                                    <option value="3">★★★☆☆ (3 - Average)</option>
                                    <option value="2">★★☆☆☆ (2 - Below Average)</option>
                                    <option value="1">★☆☆☆☆ (1 - Poor)</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="commentInput" class="form-label small fw-semibold">Your Feedback</label>
                                <textarea class="form-control" id="commentInput" name="comment" rows="4" placeholder="How was your experience with this product?" required></textarea>
                            </div>

                            <button type="submit" class="btn btn-primary btn-sm fw-bold w-100">
                                Submit Review
                            </button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Related Products -->
    <c:if test="${not empty relatedProducts}">
        <div class="mb-5">
            <h4 class="fw-bold mb-4">Related Products in ${product.categoryName}</h4>
            <div class="row g-4">
                <c:forEach var="rel" items="${relatedProducts}">
                    <c:if test="${rel.id != product.id}">
                        <div class="col-12 col-sm-6 col-md-3">
                            <div class="card h-100 border-0 shadow-sm rounded-3 product-card position-relative overflow-hidden">
                                <div class="product-img-container bg-light text-center p-3 d-flex align-items-center justify-content-center" style="height: 160px;">
                                    <img src="<c:out value='${rel.imageUrl}'/>" class="img-fluid" style="max-height: 130px; object-fit: contain;" alt="<c:out value='${rel.name}'/>" onerror="this.src='https://placehold.co/200x150?text=ShopSphere';">
                                </div>
                                <div class="card-body p-3 d-flex flex-column">
                                    <span class="badge bg-secondary-subtle text-secondary small align-self-start mb-1"><c:out value="${rel.brand}"/></span>
                                    <h6 class="card-title fw-semibold text-truncate mb-2" title="<c:out value='${rel.name}'/>">
                                        <a href="${pageContext.request.contextPath}/product?id=${rel.id}" class="text-decoration-none text-dark">
                                            <c:out value="${rel.name}"/>
                                        </a>
                                    </h6>
                                    <div class="mt-auto d-flex justify-content-between align-items-center">
                                        <span class="fw-bold text-primary">₹<fmt:formatNumber value="${rel.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/></span>
                                        <a href="${pageContext.request.contextPath}/product?id=${rel.id}" class="btn btn-outline-secondary btn-sm">View</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </c:if>

</div>

<jsp:include page="fragments/footer.jsp"/>
