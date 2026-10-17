<%@ tag language="java" pageEncoding="UTF-8" description="Reusable product card component utilizing custom PriceTag and rating tag" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="shop" uri="http://com.shopsphere/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" description="Product model entity" %>

<div class="card h-100 border-0 shadow-sm rounded-3 product-card position-relative overflow-hidden">
    <!-- Discount Badge -->
    <c:if test="${product.discountPercentage > 0}">
        <span class="position-absolute top-0 start-0 m-2 badge bg-danger shadow-sm">
            <fmt:formatNumber value="${product.discountPercentage}" maxFractionDigits="0"/>% OFF
        </span>
    </c:if>

    <!-- Product Image -->
    <div class="product-img-container bg-light text-center p-3 d-flex align-items-center justify-content-center" style="height: 190px;">
        <c:choose>
            <c:when test="${not empty product.imageUrl}">
                <img src="<c:out value='${product.imageUrl}'/>" class="img-fluid" style="max-height: 160px; object-fit: contain;" alt="<c:out value='${product.name}'/>" onerror="this.src='https://placehold.co/300x200?text=ShopSphere';">
            </c:when>
            <c:otherwise>
                <i class="bi bi-image text-muted fs-1"></i>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="card-body p-3 d-flex flex-column">
        <span class="badge bg-secondary-subtle text-secondary small align-self-start mb-1">
            <c:out value="${product.brand}"/>
        </span>
        <h6 class="card-title fw-semibold text-truncate mb-1" title="<c:out value='${product.name}'/>">
            <a href="${pageContext.request.contextPath}/product?id=${product.id}" class="text-decoration-none text-dark">
                <c:out value="${product.name}"/>
            </a>
        </h6>

        <!-- Nested Tag File Demonstration: rating.tag -->
        <div class="mb-2">
            <t:rating value="${product.rating}"/>
        </div>

        <div class="mt-auto pt-2 border-top">
            <div class="d-flex align-items-baseline gap-2 mb-2">
                <!-- Custom Classic Tag Handler Demonstration: shop:price -->
                <shop:price amount="${product.discountedPrice}"/>
                <c:if test="${product.discountPercentage > 0}">
                    <shop:price amount="${product.price}" strikethrough="true"/>
                </c:if>
            </div>

            <div class="d-grid gap-2">
                <c:choose>
                    <c:when test="${product.outOfStock}">
                        <button class="btn btn-secondary btn-sm disabled" disabled>
                            <i class="bi bi-x-circle me-1"></i>Out of Stock
                        </button>
                    </c:when>
                    <c:otherwise>
                        <div class="btn-group btn-group-sm">
                            <a href="${pageContext.request.contextPath}/product?id=${product.id}" class="btn btn-outline-secondary">
                                Details
                            </a>
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline flex-grow-1">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.id}">
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
