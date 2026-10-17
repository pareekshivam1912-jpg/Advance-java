<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Shopping Cart — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Catalog</a></li>
            <li class="breadcrumb-item active" aria-current="page">Shopping Cart</li>
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

    <h3 class="fw-bold mb-4"><i class="bi bi-cart3 me-2 text-primary"></i>Your Shopping Cart</h3>

    <c:choose>
        <c:when test="${empty cartItems}">
            <!-- Empty Cart State -->
            <div class="card border-0 shadow-sm rounded-4 p-5 text-center my-4">
                <div class="mb-3 text-secondary">
                    <i class="bi bi-cart-x display-1"></i>
                </div>
                <h4 class="fw-bold">Your cart is currently empty</h4>
                <p class="text-muted small mb-4">Looks like you haven't added anything to your cart yet. Discover trending gadgets and electronics now!</p>
                <div>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary px-4 py-2 fw-semibold">
                        <i class="bi bi-bag-plus me-2"></i>Start Shopping
                    </a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <!-- Cart Items Table -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-4">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0">
                                <thead class="table-light small text-uppercase">
                                    <tr>
                                        <th scope="col" class="ps-4">Product</th>
                                        <th scope="col">Price</th>
                                        <th scope="col" style="width: 130px;">Quantity</th>
                                        <th scope="col">Total</th>
                                        <th scope="col" class="text-end pe-4">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <!-- Product Info -->
                                            <td class="ps-4 py-3">
                                                <div class="d-flex align-items-center">
                                                    <div class="bg-light rounded p-2 text-center me-3" style="width: 70px; height: 70px; flex-shrink: 0;">
                                                        <img src="<c:out value='${item.product.imageUrl}'/>" style="max-height: 54px; max-width: 100%; object-fit: contain;" alt="<c:out value='${item.product.name}'/>" onerror="this.src='https://placehold.co/70x70?text=ShopSphere';">
                                                    </div>
                                                    <div>
                                                        <span class="badge bg-secondary-subtle text-secondary x-small mb-1"><c:out value="${item.product.brand}"/></span>
                                                        <h6 class="mb-0 small fw-bold">
                                                            <a href="${pageContext.request.contextPath}/product?id=${item.productId}" class="text-decoration-none text-dark">
                                                                <c:out value="${item.product.name}"/>
                                                            </a>
                                                        </h6>
                                                        <c:choose>
                                                            <c:when test="${item.product.outOfStock}">
                                                                <span class="badge bg-danger-subtle text-danger x-small">Out of stock</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted x-small">In stock: ${item.product.stock} units</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </td>

                                            <!-- Unit Price -->
                                            <td>
                                                <span class="fw-semibold">
                                                    ₹<fmt:formatNumber value="${item.product.discountedPrice}" minFractionDigits="2" maxFractionDigits="2"/>
                                                </span>
                                                <c:if test="${item.product.discountPercentage > 0}">
                                                    <div class="text-muted text-decoration-line-through x-small">
                                                        ₹<fmt:formatNumber value="${item.product.price}" minFractionDigits="2" maxFractionDigits="2"/>
                                                    </div>
                                                </c:if>
                                            </td>

                                            <!-- Quantity Controls Form -->
                                            <td>
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-flex align-items-center gap-1">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}" class="form-control form-control-sm text-center" style="width: 65px;" required>
                                                    <button type="submit" class="btn btn-outline-secondary btn-sm" title="Update Quantity">
                                                        <i class="bi bi-arrow-repeat"></i>
                                                    </button>
                                                </form>
                                            </td>

                                            <!-- Line Total -->
                                            <td>
                                                <span class="fw-bold text-primary">
                                                    ₹<fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2" maxFractionDigits="2"/>
                                                </span>
                                            </td>

                                            <!-- Remove Action -->
                                            <td class="text-end pe-4">
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm" title="Remove Item" onclick="return confirm('Remove this item from your cart?');">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between align-items-center">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary btn-sm">
                            <i class="bi bi-arrow-left me-1"></i>Continue Shopping
                        </a>
                        <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                            <input type="hidden" name="action" value="clear">
                            <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('Are you sure you want to clear your entire cart?');">
                                <i class="bi bi-trash3 me-1"></i>Clear Shopping Cart
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Order Summary Card -->
                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm rounded-4 p-4 sticky-top" style="top: 80px;">
                        <h5 class="fw-bold mb-3 border-bottom pb-2">Order Summary</h5>

                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted small">Cart Subtotal:</span>
                            <span class="fw-semibold">₹<fmt:formatNumber value="${subtotal}" minFractionDigits="2" maxFractionDigits="2"/></span>
                        </div>

                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted small">Shipping Estimate:</span>
                            <span class="text-success fw-semibold small">FREE</span>
                        </div>

                        <div class="d-flex justify-content-between mb-3">
                            <span class="text-muted small">Taxes:</span>
                            <span class="text-muted small">Included</span>
                        </div>

                        <hr class="text-muted">

                        <div class="d-flex justify-content-between align-items-baseline mb-4">
                            <span class="fs-5 fw-bold">Grand Total:</span>
                            <span class="fs-4 fw-bold text-primary">₹<fmt:formatNumber value="${subtotal}" minFractionDigits="2" maxFractionDigits="2"/></span>
                        </div>

                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-lg fw-bold w-100 shadow-sm mb-3">
                            <i class="bi bi-credit-card me-2"></i>Proceed to Checkout
                        </a>

                        <div class="p-3 bg-light rounded-3 text-center small text-secondary">
                            <i class="bi bi-shield-check text-success fs-5 me-1 align-middle"></i>
                            <span>100% Secure Checkout Guarantee &bull; PreparedStatement Protected</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

</div>

<jsp:include page="fragments/footer.jsp"/>
