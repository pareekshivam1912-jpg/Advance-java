<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Order Placed Successfully — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-5 my-3">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-6 text-center">
            <div class="card border-0 shadow-sm rounded-4 p-5">
                <div class="mb-4">
                    <div class="bg-success-subtle text-success rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 90px; height: 90px;">
                        <i class="bi bi-check-lg display-3"></i>
                    </div>
                </div>

                <h2 class="fw-bold text-dark mb-2">Order Confirmed!</h2>
                <p class="text-muted mb-4">
                    Thank you for your purchase. Your transaction was processed in an atomic JDBC transaction with automatic stock reduction.
                </p>

                <div class="p-3 bg-light rounded-3 mb-4 text-start">
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted small">Order Reference:</span>
                        <span class="fw-bold text-primary">#<c:out value="${orderId}"/></span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted small">Order Status:</span>
                        <span class="badge bg-primary">PLACED</span>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span class="text-muted small">Delivery Timeline:</span>
                        <span class="text-dark small">Expected in 3-5 business days</span>
                    </div>
                </div>

                <div class="d-grid gap-2">
                    <c:if test="${not empty orderId}">
                        <a href="${pageContext.request.contextPath}/order?id=${orderId}" class="btn btn-primary fw-bold py-2">
                            <i class="bi bi-receipt me-1"></i>View Order Details &amp; Status Timeline
                        </a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-secondary py-2">
                        <i class="bi bi-bag-check me-1"></i>All My Orders
                    </a>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-link text-decoration-none">
                        Continue Shopping &rarr;
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
