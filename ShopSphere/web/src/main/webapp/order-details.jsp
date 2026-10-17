<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Order #${order.id} Details — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/orders" class="text-decoration-none">My Orders</a></li>
            <li class="breadcrumb-item active" aria-current="page">Order #${order.id}</li>
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

    <!-- Header Banner -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-3">
            <div>
                <span class="badge bg-secondary-subtle text-secondary small mb-1">ORDER IDENTIFIER</span>
                <h3 class="fw-bold mb-1 text-dark">Order #<c:out value="${order.id}"/></h3>
                <small class="text-muted">
                    Placed on <fmt:formatDate value="${order.createdAt}" pattern="EEEE, dd MMMM yyyy 'at' hh:mm a"/>
                </small>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="badge ${order.orderStatus == 'DELIVERED' ? 'bg-success' : (order.orderStatus == 'CANCELLED' ? 'bg-danger' : (order.orderStatus == 'SHIPPED' ? 'bg-warning text-dark' : 'bg-primary'))} fs-6 px-3 py-2">
                    <c:out value="${order.orderStatus}"/>
                </span>
                <c:if test="${order.canCancel()}">
                    <form action="${pageContext.request.contextPath}/order" method="post" class="d-inline">
                        <input type="hidden" name="action" value="cancel">
                        <input type="hidden" name="orderId" value="${order.id}">
                        <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('Cancel this order? This will restock all order items.');">
                            <i class="bi bi-x-circle me-1"></i>Cancel Order
                        </button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Status Timeline -->
    <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
        <h5 class="fw-bold mb-4"><i class="bi bi-truck me-2 text-primary"></i>Delivery Status Timeline</h5>

        <c:choose>
            <c:when test="${order.orderStatus == 'CANCELLED'}">
                <div class="alert alert-danger d-flex align-items-center mb-0" role="alert">
                    <i class="bi bi-x-octagon-fill fs-3 me-3"></i>
                    <div>
                        <h6 class="fw-bold mb-1">Order Cancelled</h6>
                        <span class="small">This order has been cancelled and inventory was safely restored to the catalog.</span>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row text-center position-relative g-3">
                    <div class="col-3">
                        <div class="timeline-step">
                            <div class="avatar-circle mx-auto d-flex align-items-center justify-content-center rounded-circle mb-2 ${order.orderStatus == 'PLACED' || order.orderStatus == 'CONFIRMED' || order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bg-primary text-white' : 'bg-light text-muted'}" style="width: 48px; height: 48px;">
                                <i class="bi bi-receipt"></i>
                            </div>
                            <h6 class="small fw-bold mb-0">1. Placed</h6>
                            <span class="x-small text-muted">Order received</span>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="timeline-step">
                            <div class="avatar-circle mx-auto d-flex align-items-center justify-content-center rounded-circle mb-2 ${order.orderStatus == 'CONFIRMED' || order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bg-primary text-white' : 'bg-light text-muted'}" style="width: 48px; height: 48px;">
                                <i class="bi bi-check2-circle"></i>
                            </div>
                            <h6 class="small fw-bold mb-0">2. Confirmed</h6>
                            <span class="x-small text-muted">Payment verified</span>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="timeline-step">
                            <div class="avatar-circle mx-auto d-flex align-items-center justify-content-center rounded-circle mb-2 ${order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bg-primary text-white' : 'bg-light text-muted'}" style="width: 48px; height: 48px;">
                                <i class="bi bi-truck"></i>
                            </div>
                            <h6 class="small fw-bold mb-0">3. Shipped</h6>
                            <span class="x-small text-muted">In transit</span>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="timeline-step">
                            <div class="avatar-circle mx-auto d-flex align-items-center justify-content-center rounded-circle mb-2 ${order.orderStatus == 'DELIVERED' ? 'bg-success text-white' : 'bg-light text-muted'}" style="width: 48px; height: 48px;">
                                <i class="bi bi-house-check"></i>
                            </div>
                            <h6 class="small fw-bold mb-0">4. Delivered</h6>
                            <span class="x-small text-muted">Package arrived</span>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="row g-4">
        <!-- Ordered Items -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-4">
                <div class="card-header bg-light py-3 px-4">
                    <h5 class="fw-bold mb-0">Items in this Order</h5>
                </div>
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small text-uppercase">
                            <tr>
                                <th scope="col" class="ps-4">Product Name</th>
                                <th scope="col">Unit Price</th>
                                <th scope="col" class="text-center">Quantity</th>
                                <th scope="col" class="text-end pe-4">Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td class="ps-4 py-3">
                                        <a href="${pageContext.request.contextPath}/product?id=${item.productId}" class="text-decoration-none text-dark fw-bold small">
                                            <c:out value="${item.productName}"/>
                                        </a>
                                    </td>
                                    <td>
                                        <span class="small">₹<fmt:formatNumber value="${item.unitPrice}" minFractionDigits="2"/></span>
                                    </td>
                                    <td class="text-center">
                                        <span class="badge bg-secondary-subtle text-secondary">${item.quantity}</span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <span class="fw-bold text-primary">₹<fmt:formatNumber value="${item.totalPrice}" minFractionDigits="2"/></span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Delivery Address Details -->
            <c:if test="${not empty order.address}">
                <div class="card border-0 shadow-sm rounded-4 p-4">
                    <h6 class="fw-bold mb-3"><i class="bi bi-geo-alt-fill text-danger me-2"></i>Shipping Delivery Address</h6>
                    <div class="text-dark fw-semibold"><c:out value="${order.address.fullName}"/></div>
                    <div class="text-secondary small mt-1">
                        <c:out value="${order.address.streetAddress}"/>, <c:out value="${order.address.city}"/>, <c:out value="${order.address.state}"/> - <c:out value="${order.address.postalCode}"/>
                    </div>
                    <div class="text-muted small mt-1">
                        <i class="bi bi-telephone me-1"></i>Contact: <c:out value="${order.address.phone}"/>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Payment & Cost Summary -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                <h5 class="fw-bold mb-3 border-bottom pb-2">Payment Details</h5>

                <div class="mb-2 d-flex justify-content-between">
                    <span class="text-muted small">Method:</span>
                    <span class="fw-semibold small"><c:out value="${order.paymentMethod}"/></span>
                </div>

                <div class="mb-2 d-flex justify-content-between">
                    <span class="text-muted small">Payment Status:</span>
                    <span class="badge ${order.paymentStatus == 'PAID' ? 'bg-success' : 'bg-warning text-dark'} small">
                        <c:out value="${order.paymentStatus}"/>
                    </span>
                </div>

                <c:if test="${not empty order.payment}">
                    <div class="mb-3 d-flex justify-content-between">
                        <span class="text-muted small">Txn Reference:</span>
                        <code class="small"><c:out value="${order.payment.transactionReference}"/></code>
                    </div>
                </c:if>

                <hr class="text-muted">

                <div class="d-flex justify-content-between mb-2">
                    <span class="text-muted small">Subtotal:</span>
                    <span class="fw-semibold small">₹<fmt:formatNumber value="${order.subtotal}" minFractionDigits="2"/></span>
                </div>

                <c:if test="${order.discountAmount > 0}">
                    <div class="d-flex justify-content-between mb-2 text-success">
                        <span class="small">Coupon Discount:</span>
                        <span class="fw-semibold small">- ₹<fmt:formatNumber value="${order.discountAmount}" minFractionDigits="2"/></span>
                    </div>
                </c:if>

                <div class="d-flex justify-content-between mb-3">
                    <span class="text-muted small">Shipping:</span>
                    <c:choose>
                        <c:when test="${order.shippingFee == 0}">
                            <span class="text-success small fw-semibold">FREE</span>
                        </c:when>
                        <c:otherwise>
                            <span class="small fw-semibold">₹<fmt:formatNumber value="${order.shippingFee}" minFractionDigits="2"/></span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <hr class="text-muted">

                <div class="d-flex justify-content-between align-items-baseline mb-2">
                    <span class="fs-5 fw-bold">Grand Total:</span>
                    <span class="fs-4 fw-bold text-primary">₹<fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2"/></span>
                </div>
            </div>

            <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary w-100 py-2">
                <i class="bi bi-arrow-left me-1"></i>Back to Orders
            </a>
        </div>
    </div>

</div>

<jsp:include page="fragments/footer.jsp"/>
