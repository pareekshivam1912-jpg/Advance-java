<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="pageTitle" value="My Orders — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">My Orders</li>
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
        <h3 class="fw-bold mb-0"><i class="bi bi-box-seam me-2 text-primary"></i>My Order History</h3>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-sm">
            <i class="bi bi-bag-plus me-1"></i>Shop More
        </a>
    </div>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="card border-0 shadow-sm rounded-4 p-5 text-center my-4">
                <div class="mb-3 text-secondary">
                    <i class="bi bi-receipt-cutoff display-1"></i>
                </div>
                <h4 class="fw-bold">No Orders Placed Yet</h4>
                <p class="text-muted small mb-4">You have not placed any orders yet. Check out our high-performance electronics catalogue!</p>
                <div>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary px-4 py-2 fw-semibold">
                        <i class="bi bi-bag me-2"></i>Explore Products
                    </a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="ord" items="${orders}">
                    <div class="col-12">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                            <!-- Card Header -->
                            <div class="card-header bg-light py-3 px-4 d-flex flex-wrap justify-content-between align-items-center gap-2">
                                <div class="d-flex align-items-center gap-3">
                                    <div>
                                        <span class="text-muted x-small d-block">ORDER PLACED</span>
                                        <span class="small fw-semibold"><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></span>
                                    </div>
                                    <div class="border-start ps-3">
                                        <span class="text-muted x-small d-block">ORDER NUMBER</span>
                                        <span class="small fw-bold text-primary">#<c:out value="${ord.id}"/></span>
                                    </div>
                                </div>
                                <div class="d-flex align-items-center gap-2">
                                    <span class="badge ${ord.orderStatus == 'DELIVERED' ? 'bg-success' : (ord.orderStatus == 'CANCELLED' ? 'bg-danger' : (ord.orderStatus == 'SHIPPED' ? 'bg-warning text-dark' : 'bg-primary'))} px-3 py-2 small">
                                        <c:out value="${ord.orderStatus}"/>
                                    </span>
                                    <span class="badge ${ord.paymentStatus == 'PAID' ? 'bg-success-subtle text-success' : 'bg-warning-subtle text-warning-emphasis'} px-3 py-2 small">
                                        <c:out value="${ord.paymentMethod}"/> &bull; <c:out value="${ord.paymentStatus}"/>
                                    </span>
                                </div>
                            </div>

                            <!-- Card Body -->
                            <div class="card-body p-4">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <div class="d-flex flex-column gap-2">
                                            <c:forEach var="item" items="${ord.items}">
                                                <div class="d-flex align-items-center justify-content-between py-1">
                                                    <div>
                                                        <strong class="small text-dark"><c:out value="${item.productName}"/></strong>
                                                        <span class="text-muted small"> &times; ${item.quantity}</span>
                                                    </div>
                                                    <span class="small fw-semibold text-secondary">
                                                        ₹<fmt:formatNumber value="${item.totalPrice}" minFractionDigits="2"/>
                                                    </span>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <div class="col-md-4 text-md-end border-start pt-3 pt-md-0 mt-3 mt-md-0">
                                        <span class="text-muted small d-block">Total Amount</span>
                                        <span class="fs-4 fw-bold text-primary d-block mb-3">
                                            ₹<fmt:formatNumber value="${ord.totalAmount}" minFractionDigits="2"/>
                                        </span>
                                        <div class="d-flex justify-content-md-end gap-2">
                                            <a href="${pageContext.request.contextPath}/order?id=${ord.id}" class="btn btn-outline-primary btn-sm fw-semibold">
                                                <i class="bi bi-eye me-1"></i>Track &amp; Details
                                            </a>
                                            <c:if test="${ord.canCancel()}">
                                                <form action="${pageContext.request.contextPath}/order" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="cancel">
                                                    <input type="hidden" name="orderId" value="${ord.id}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('Cancel this order? Restores item inventory.');">
                                                        Cancel
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
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
