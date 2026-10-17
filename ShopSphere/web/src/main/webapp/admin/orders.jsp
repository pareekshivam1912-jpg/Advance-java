<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Manage Orders — ShopSphere Admin" scope="request"/>
<c:set var="activeTab" value="orders" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/admin-navbar.jsp"/>

<div class="container-fluid px-4 py-4">

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

    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
        <div>
            <h3 class="fw-bold mb-0 text-dark"><i class="bi bi-receipt text-danger me-2"></i>Customer Orders Dispatch</h3>
            <span class="text-muted small">Update delivery lifecycle (PLACED &rarr; CONFIRMED &rarr; SHIPPED &rarr; DELIVERED)</span>
        </div>

        <!-- Filter Buttons -->
        <div class="btn-group btn-group-sm">
            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline-secondary ${empty selectedStatus ? 'active' : ''}">All</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=PLACED" class="btn btn-outline-primary ${selectedStatus == 'PLACED' ? 'active' : ''}">Placed</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=CONFIRMED" class="btn btn-outline-info ${selectedStatus == 'CONFIRMED' ? 'active' : ''}">Confirmed</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=SHIPPED" class="btn btn-outline-warning ${selectedStatus == 'SHIPPED' ? 'active' : ''}">Shipped</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=DELIVERED" class="btn btn-outline-success ${selectedStatus == 'DELIVERED' ? 'active' : ''}">Delivered</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=CANCELLED" class="btn btn-outline-danger ${selectedStatus == 'CANCELLED' ? 'active' : ''}">Cancelled</a>
        </div>
    </div>

    <!-- Orders Table -->
    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-dark small text-uppercase">
                    <tr>
                        <th scope="col" class="ps-4">Order #</th>
                        <th scope="col">User ID</th>
                        <th scope="col">Created Date</th>
                        <th scope="col">Total Amount</th>
                        <th scope="col">Payment Method</th>
                        <th scope="col">Payment Status</th>
                        <th scope="col">Delivery Status</th>
                        <th scope="col" class="text-end pe-4">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty orders}">
                            <tr>
                                <td colspan="8" class="text-center py-5 text-muted">
                                    <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                    No orders found matching status filter.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="ord" items="${orders}">
                                <tr>
                                    <td class="ps-4 py-3 fw-bold text-primary">#${ord.id}</td>
                                    <td class="small">User #${ord.userId}</td>
                                    <td class="small text-muted"><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                                    <td class="small fw-bold">₹<fmt:formatNumber value="${ord.totalAmount}" minFractionDigits="2"/></td>
                                    <td class="small"><span class="badge bg-light text-dark border">${ord.paymentMethod}</span></td>

                                    <!-- Payment Status Form -->
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="updatePayment">
                                            <input type="hidden" name="orderId" value="${ord.id}">
                                            <select name="paymentStatus" class="form-select form-select-sm py-0 px-2 small" style="width: 105px; height: 28px;" onchange="this.form.submit()">
                                                <option value="PENDING" ${ord.paymentStatus == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                                <option value="PAID" ${ord.paymentStatus == 'PAID' ? 'selected' : ''}>PAID</option>
                                                <option value="REFUNDED" ${ord.paymentStatus == 'REFUNDED' ? 'selected' : ''}>REFUNDED</option>
                                            </select>
                                        </form>
                                    </td>

                                    <!-- Order Status Form -->
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="updateStatus">
                                            <input type="hidden" name="orderId" value="${ord.id}">
                                            <select name="orderStatus" class="form-select form-select-sm py-0 px-2 fw-semibold small" style="width: 130px; height: 28px;" onchange="this.form.submit()">
                                                <option value="PLACED" ${ord.orderStatus == 'PLACED' ? 'selected' : ''}>PLACED</option>
                                                <option value="CONFIRMED" ${ord.orderStatus == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                                <option value="SHIPPED" ${ord.orderStatus == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                                <option value="DELIVERED" ${ord.orderStatus == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                                <option value="CANCELLED" ${ord.orderStatus == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                            </select>
                                        </form>
                                    </td>

                                    <td class="text-end pe-4">
                                        <a href="${pageContext.request.contextPath}/order?id=${ord.id}" class="btn btn-outline-secondary btn-sm" title="View customer-facing details">
                                            <i class="bi bi-eye"></i> View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
