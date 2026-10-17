<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Secure Checkout — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cart" class="text-decoration-none">Shopping Cart</a></li>
            <li class="breadcrumb-item active" aria-current="page">Checkout</li>
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

    <div class="d-flex align-items-center mb-4">
        <h3 class="fw-bold mb-0"><i class="bi bi-shield-lock-fill text-success me-2"></i>Order Checkout</h3>
        <span class="badge bg-success-subtle text-success ms-3 px-3 py-2 small">
            <i class="bi bi-lock me-1"></i>Server-side Recalculated
        </span>
    </div>

    <div class="row g-4">

        <!-- Left Column: Address & Payment Selection -->
        <div class="col-lg-7">
            <form action="${pageContext.request.contextPath}/checkout" method="post" id="placeOrderForm">
                <input type="hidden" name="action" value="placeOrder">
                <input type="hidden" name="couponCode" value="<c:out value='${couponCode}'/>">

                <!-- 1. Delivery Address Card -->
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold mb-0">1. Select Delivery Address</h5>
                        <button class="btn btn-outline-primary btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#newAddressCollapse" aria-expanded="false">
                            <i class="bi bi-plus-circle me-1"></i>Add New Address
                        </button>
                    </div>

                    <c:choose>
                        <c:when test="${empty addresses}">
                            <div class="alert alert-warning small mb-3">
                                <i class="bi bi-info-circle me-1"></i>You don't have any saved delivery addresses. Please fill in the address form below.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="address-options mb-3">
                                <c:forEach var="addr" items="${addresses}" varStatus="status">
                                    <div class="form-check p-3 border rounded-3 mb-2 ${addr.default ? 'border-primary bg-primary-subtle bg-opacity-10' : ''}">
                                        <input class="form-check-input ms-0 me-3" type="radio" name="addressId" id="addr_${addr.id}" value="${addr.id}" ${addr.default || status.first ? 'checked' : ''} required>
                                        <label class="form-check-label w-100" for="addr_${addr.id}">
                                            <div class="d-flex justify-content-between">
                                                <strong class="text-dark"><c:out value="${addr.fullName}"/></strong>
                                                <c:if test="${addr.default}">
                                                    <span class="badge bg-primary">Default</span>
                                                </c:if>
                                            </div>
                                            <div class="text-secondary small mt-1">
                                                <c:out value="${addr.streetAddress}"/>, <c:out value="${addr.city}"/>, <c:out value="${addr.state}"/> - <c:out value="${addr.postalCode}"/>
                                            </div>
                                            <div class="text-muted small mt-1">
                                                <i class="bi bi-telephone me-1"></i><c:out value="${addr.phone}"/>
                                            </div>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- 2. Payment Method Card -->
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3">2. Select Payment Method</h5>

                    <div class="form-check p-3 border rounded-3 mb-2">
                        <input class="form-check-input ms-0 me-3" type="radio" name="paymentMethod" id="pay_cod" value="COD" checked>
                        <label class="form-check-label w-100" for="pay_cod">
                            <div class="d-flex align-items-center">
                                <i class="bi bi-cash-stack fs-4 text-success me-2"></i>
                                <div>
                                    <strong class="text-dark">Cash on Delivery (COD)</strong>
                                    <div class="text-muted small">Pay with cash or UPI upon package arrival at your doorstep.</div>
                                </div>
                            </div>
                        </label>
                    </div>

                    <div class="form-check p-3 border rounded-3 mb-2">
                        <input class="form-check-input ms-0 me-3" type="radio" name="paymentMethod" id="pay_card" value="CARD">
                        <label class="form-check-label w-100" for="pay_card">
                            <div class="d-flex align-items-center">
                                <i class="bi bi-credit-card-2-front fs-4 text-primary me-2"></i>
                                <div>
                                    <strong class="text-dark">Credit / Debit Card (Simulated)</strong>
                                    <div class="text-muted small">Instant simulated gateway authorization with secure mock transaction reference.</div>
                                </div>
                            </div>
                        </label>
                    </div>

                    <div class="form-check p-3 border rounded-3">
                        <input class="form-check-input ms-0 me-3" type="radio" name="paymentMethod" id="pay_upi" value="UPI">
                        <label class="form-check-label w-100" for="pay_upi">
                            <div class="d-flex align-items-center">
                                <i class="bi bi-qr-code-scan fs-4 text-warning me-2"></i>
                                <div>
                                    <strong class="text-dark">UPI Instant Payment (Simulated)</strong>
                                    <div class="text-muted small">Google Pay / PhonePe / Paytm mock transaction.</div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>

                <!-- Submit Button -->
                <div class="d-grid mb-4">
                    <button type="submit" class="btn btn-success btn-lg fw-bold shadow-sm py-3">
                        <i class="bi bi-check-circle-fill me-2"></i>Confirm &amp; Place Order (₹<fmt:formatNumber value="${summary.totalAmount}" minFractionDigits="2" maxFractionDigits="2"/>)
                    </button>
                    <small class="text-muted text-center mt-2">
                        By placing this order, you agree to ShopSphere's Terms &amp; Conditions.
                    </small>
                </div>
            </form>

            <!-- Collapsible Add Address Form -->
            <div class="collapse ${empty addresses ? 'show' : ''} mb-4" id="newAddressCollapse">
                <div class="card border border-primary-subtle shadow-sm rounded-4 p-4 bg-light">
                    <h6 class="fw-bold mb-3"><i class="bi bi-geo-alt-fill text-primary me-2"></i>Add New Delivery Address</h6>
                    <form action="${pageContext.request.contextPath}/checkout" method="post">
                        <input type="hidden" name="action" value="addAddress">
                        <input type="hidden" name="coupon" value="<c:out value='${couponCode}'/>">

                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-semibold">Recipient Full Name</label>
                                <input type="text" class="form-control form-control-sm" name="fullName" placeholder="e.g. Rahul Sharma" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-semibold">Mobile Phone Number</label>
                                <input type="text" class="form-control form-control-sm" name="phone" placeholder="e.g. 9876543210" required>
                            </div>
                            <div class="col-12">
                                <label class="form-label small fw-semibold">Street / Flat / Locality</label>
                                <input type="text" class="form-control form-control-sm" name="streetAddress" placeholder="e.g. 102, Shanti Vihar, Tonk Road" required>
                            </div>
                            <div class="col-md-5">
                                <label class="form-label small fw-semibold">City</label>
                                <input type="text" class="form-control form-control-sm" name="city" placeholder="e.g. Jaipur" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-semibold">State</label>
                                <input type="text" class="form-control form-control-sm" name="state" placeholder="e.g. Rajasthan" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-semibold">PIN Code</label>
                                <input type="text" class="form-control form-control-sm" name="postalCode" placeholder="e.g. 302018" required>
                            </div>
                            <div class="col-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="isDefault" id="isDefaultCheck" value="true">
                                    <label class="form-check-label small" for="isDefaultCheck">Make this my default address</label>
                                </div>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-primary btn-sm fw-semibold">
                                    Save Address
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Right Column: Order Items & Pricing Breakdown -->
        <div class="col-lg-5">
            <div class="card border-0 shadow-sm rounded-4 p-4 sticky-top" style="top: 80px;">
                <h5 class="fw-bold mb-3 border-bottom pb-2">Order Breakdown</h5>

                <!-- Items mini-list -->
                <div class="mb-3" style="max-height: 220px; overflow-y: auto;">
                    <c:forEach var="item" items="${summary.items}">
                        <div class="d-flex align-items-center justify-content-between py-2 border-bottom">
                            <div class="d-flex align-items-center">
                                <div class="bg-light rounded p-1 me-2 text-center" style="width: 40px; height: 40px;">
                                    <img src="<c:out value='${item.product.imageUrl}'/>" style="max-height: 32px; max-width: 100%;" alt="item" onerror="this.src='https://placehold.co/40x40?text=P';">
                                </div>
                                <div>
                                    <h6 class="small fw-semibold mb-0 text-truncate" style="max-width: 180px;"><c:out value="${item.product.name}"/></h6>
                                    <span class="text-muted x-small">Qty: ${item.quantity} &times; ₹<fmt:formatNumber value="${item.product.discountedPrice}" minFractionDigits="2"/></span>
                                </div>
                            </div>
                            <span class="small fw-bold">₹<fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2"/></span>
                        </div>
                    </c:forEach>
                </div>

                <!-- Coupon Form -->
                <div class="mb-4">
                    <label class="form-label small fw-semibold">Have a Promo Coupon?</label>
                    <form action="${pageContext.request.contextPath}/checkout" method="get" class="input-group input-group-sm">
                        <input type="text" class="form-control text-uppercase" name="coupon" placeholder="WELCOME10, FESTIVE20" value="<c:out value='${couponCode}'/>">
                        <button class="btn btn-primary" type="submit">Apply</button>
                    </form>
                    <c:if test="${not empty summary.appliedCoupon}">
                        <div class="badge bg-success-subtle text-success mt-2 p-2 w-100 d-flex justify-content-between align-items-center">
                            <span><i class="bi bi-tag-fill me-1"></i>Coupon Applied: <strong><c:out value="${summary.appliedCoupon.code}"/></strong></span>
                            <a href="${pageContext.request.contextPath}/checkout" class="text-danger text-decoration-none fw-bold">&times; Remove</a>
                        </div>
                    </c:if>
                    <small class="text-muted d-block mt-1 x-small">Available demo coupons: <code>WELCOME10</code>, <code>FESTIVE20</code>, <code>FLAT500</code></small>
                </div>

                <!-- Calculations -->
                <div class="d-flex justify-content-between mb-2">
                    <span class="text-muted small">Items Subtotal:</span>
                    <span class="fw-semibold">₹<fmt:formatNumber value="${summary.subtotal}" minFractionDigits="2" maxFractionDigits="2"/></span>
                </div>

                <c:if test="${summary.discountAmount > 0}">
                    <div class="d-flex justify-content-between mb-2 text-success">
                        <span class="small">Coupon Discount:</span>
                        <span class="fw-semibold">- ₹<fmt:formatNumber value="${summary.discountAmount}" minFractionDigits="2" maxFractionDigits="2"/></span>
                    </div>
                </c:if>

                <div class="d-flex justify-content-between mb-3">
                    <span class="text-muted small">Shipping Delivery Fee:</span>
                    <c:choose>
                        <c:when test="${summary.shippingFee == 0}">
                            <span class="text-success fw-semibold small">FREE</span>
                        </c:when>
                        <c:otherwise>
                            <span class="fw-semibold small">₹<fmt:formatNumber value="${summary.shippingFee}" minFractionDigits="2" maxFractionDigits="2"/></span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <hr class="text-muted">

                <div class="d-flex justify-content-between align-items-baseline mb-4">
                    <span class="fs-5 fw-bold">Total Payable:</span>
                    <span class="fs-4 fw-bold text-primary">₹<fmt:formatNumber value="${summary.totalAmount}" minFractionDigits="2" maxFractionDigits="2"/></span>
                </div>

                <div class="p-3 bg-light rounded-3 text-center small text-secondary">
                    <i class="bi bi-shield-fill-check text-success fs-5 me-1 align-middle"></i>
                    <span>Atomic Database Transaction &bull; Stock auto-reserved</span>
                </div>
            </div>
        </div>

    </div>

</div>

<jsp:include page="fragments/footer.jsp"/>
