<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Product Catalog — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumbs -->
    <nav aria-label="breadcrumb" class="mb-3">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">Catalog</li>
            <c:if test="${not empty searchQuery}">
                <li class="breadcrumb-item active text-truncate" style="max-width: 200px;">Search: "<c:out value='${searchQuery}'/>"</li>
            </c:if>
        </ol>
    </nav>

    <div class="row g-4">

        <!-- Left Filter Sidebar -->
        <div class="col-lg-3">
            <div class="card border-0 shadow-sm rounded-3 p-3">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0"><i class="bi bi-funnel me-1 text-primary"></i>Filters</h5>
                    <a href="${pageContext.request.contextPath}/products" class="text-decoration-none small text-danger">Reset All</a>
                </div>

                <form action="${pageContext.request.contextPath}/products" method="get" id="filterForm">
                    <input type="hidden" name="sort" value="<c:out value='${selectedSort}'/>">

                    <!-- Search Keyword -->
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Keyword</label>
                        <div class="input-group input-group-sm">
                            <input type="text" class="form-control" name="q" value="<c:out value='${searchQuery}'/>" placeholder="Search title, brand...">
                            <button class="btn btn-outline-primary" type="submit"><i class="bi bi-search"></i></button>
                        </div>
                    </div>

                    <hr class="text-muted">

                    <!-- Categories -->
                    <div class="mb-3">
                        <label class="form-label small fw-semibold d-block mb-2">Category</label>
                        <div class="form-check mb-1">
                            <input class="form-check-input" type="radio" name="category" id="cat_all" value="" ${empty selectedCategory ? 'checked' : ''} onchange="this.form.submit()">
                            <label class="form-check-label small" for="cat_all">All Categories</label>
                        </div>
                        <c:forEach var="c" items="${categories}">
                            <div class="form-check mb-1">
                                <input class="form-check-input" type="radio" name="category" id="cat_${c.id}" value="${c.id}" ${selectedCategory == c.id ? 'checked' : ''} onchange="this.form.submit()">
                                <label class="form-check-label small" for="cat_${c.id}">
                                    <c:out value="${c.name}"/>
                                </label>
                            </div>
                        </c:forEach>
                    </div>

                    <hr class="text-muted">

                    <!-- Price Range -->
                    <div class="mb-3">
                        <label class="form-label small fw-semibold mb-2">Price Range (₹)</label>
                        <div class="row g-2 align-items-center">
                            <div class="col-6">
                                <input type="number" class="form-control form-control-sm" name="minPrice" placeholder="Min" value="<c:out value='${minPrice}'/>" min="0">
                            </div>
                            <div class="col-6">
                                <input type="number" class="form-control form-control-sm" name="maxPrice" placeholder="Max" value="<c:out value='${maxPrice}'/>" min="0">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm w-100 mt-2">
                            <i class="bi bi-arrow-right-short me-1"></i>Apply Price
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Right Product Grid Area -->
        <div class="col-lg-9">

            <!-- Catalog Control Bar -->
            <div class="card border-0 shadow-sm rounded-3 p-3 mb-4">
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
                    <div>
                        <span class="fw-semibold"><c:out value="${totalProducts}"/></span> products found
                        <c:if test="${not empty searchQuery}">
                            for query "<span class="text-primary fw-semibold"><c:out value="${searchQuery}"/></span>"
                        </c:if>
                    </div>

                    <!-- Sort Options -->
                    <div class="d-flex align-items-center gap-2">
                        <label for="sortSelect" class="form-label small fw-semibold mb-0 text-nowrap">Sort by:</label>
                        <select id="sortSelect" class="form-select form-select-sm" style="width: auto;" onchange="updateSort(this.value)">
                            <option value="newest" ${selectedSort == 'newest' ? 'selected' : ''}>Newest Arrivals</option>
                            <option value="price_asc" ${selectedSort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                            <option value="price_desc" ${selectedSort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                            <option value="rating" ${selectedSort == 'rating' ? 'selected' : ''}>Highest Rated</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Product Cards Grid -->
            <c:choose>
                <c:when test="${empty products}">
                    <div class="card border-0 shadow-sm rounded-3 p-5 text-center my-4">
                        <div class="mb-3 text-secondary">
                            <i class="bi bi-search fs-1"></i>
                        </div>
                        <h4 class="fw-bold">No Products Found</h4>
                        <p class="text-muted small mb-4">We couldn't find any items matching your selected criteria. Try adjusting or clearing your filters.</p>
                        <div>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-sm px-4">
                                Clear All Filters
                            </a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row g-4 mb-4">
                        <c:forEach var="prod" items="${products}">
                            <div class="col-12 col-md-6 col-xl-4">
                                <div class="card h-100 border-0 shadow-sm rounded-3 product-card position-relative overflow-hidden">
                                    <!-- Discount Badge -->
                                    <c:if test="${prod.discountPercentage > 0}">
                                        <span class="position-absolute top-0 start-0 m-2 badge bg-danger shadow-sm">
                                            <fmt:formatNumber value="${prod.discountPercentage}" maxFractionDigits="0"/>% OFF
                                        </span>
                                    </c:if>

                                    <!-- Product Image -->
                                    <div class="product-img-container bg-light text-center p-3 d-flex align-items-center justify-content-center" style="height: 200px;">
                                        <c:choose>
                                            <c:when test="${not empty prod.imageUrl}">
                                                <img src="<c:out value='${prod.imageUrl}'/>" class="img-fluid" style="max-height: 170px; object-fit: contain;" alt="<c:out value='${prod.name}'/>" onerror="this.src='https://placehold.co/300x200?text=ShopSphere';">
                                            </c:when>
                                            <c:otherwise>
                                                <i class="bi bi-image text-muted fs-1"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="card-body p-3 d-flex flex-column">
                                        <div class="d-flex justify-content-between align-items-center mb-1">
                                            <span class="badge bg-secondary-subtle text-secondary small">
                                                <c:out value="${prod.brand}"/>
                                            </span>
                                            <small class="text-muted"><c:out value="${prod.categoryName}"/></small>
                                        </div>

                                        <h6 class="card-title fw-semibold text-truncate mb-1" title="<c:out value='${prod.name}'/>">
                                            <a href="${pageContext.request.contextPath}/product?id=${prod.id}" class="text-decoration-none text-dark">
                                                <c:out value="${prod.name}"/>
                                            </a>
                                        </h6>

                                        <div class="d-flex align-items-center small text-warning mb-2">
                                            <i class="bi bi-star-fill"></i>
                                            <span class="ms-1 fw-bold text-dark"><fmt:formatNumber value="${prod.rating}" minFractionDigits="1" maxFractionDigits="1"/></span>
                                            <span class="text-muted ms-1 small">/ 5</span>
                                            <c:choose>
                                                <c:when test="${prod.outOfStock}">
                                                    <span class="ms-auto badge bg-danger-subtle text-danger small">Out of Stock</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="ms-auto badge bg-success-subtle text-success small">In Stock (${prod.stock})</span>
                                                </c:otherwise>
                                            </c:choose>
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
                                                                Details
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

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Catalog pagination" class="d-flex justify-content-center my-4">
                            <ul class="pagination pagination-sm shadow-sm mb-0">
                                <!-- Previous Page -->
                                <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage - 1}&q=<c:out value='${searchQuery}'/>&category=<c:out value='${selectedCategory}'/>&minPrice=<c:out value='${minPrice}'/>&maxPrice=<c:out value='${maxPrice}'/>&sort=<c:out value='${selectedSort}'/>" aria-label="Previous">
                                        <span aria-hidden="true">&laquo; Prev</span>
                                    </a>
                                </li>

                                <c:forEach var="pageNum" begin="1" end="${totalPages}">
                                    <li class="page-item ${pageNum == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?page=${pageNum}&q=<c:out value='${searchQuery}'/>&category=<c:out value='${selectedCategory}'/>&minPrice=<c:out value='${minPrice}'/>&maxPrice=<c:out value='${maxPrice}'/>&sort=<c:out value='${selectedSort}'/>">
                                            ${pageNum}
                                        </a>
                                    </li>
                                </c:forEach>

                                <!-- Next Page -->
                                <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage + 1}&q=<c:out value='${searchQuery}'/>&category=<c:out value='${selectedCategory}'/>&minPrice=<c:out value='${minPrice}'/>&maxPrice=<c:out value='${maxPrice}'/>&sort=<c:out value='${selectedSort}'/>" aria-label="Next">
                                        <span aria-hidden="true">Next &raquo;</span>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
</div>

<script>
    function updateSort(sortValue) {
        const url = new URL(window.location.href);
        url.searchParams.set('sort', sortValue);
        url.searchParams.set('page', '1');
        window.location.href = url.toString();
    }
</script>

<jsp:include page="fragments/footer.jsp"/>
