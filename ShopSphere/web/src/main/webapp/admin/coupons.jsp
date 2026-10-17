<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Manage Coupons — ShopSphere Admin" scope="request"/>
<c:set var="activeTab" value="coupons" scope="request"/>
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

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold mb-0 text-dark"><i class="bi bi-ticket-perforated text-danger me-2"></i>Discount Coupon Management</h3>
            <span class="text-muted small">Configure percentage or flat promo discounts with eligibility rules</span>
        </div>
        <button class="btn btn-primary btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#addCouponCollapse">
            <i class="bi bi-plus-circle me-1"></i>Create New Coupon
        </button>
    </div>

    <!-- Collapsible Add Coupon Form -->
    <div class="collapse mb-4" id="addCouponCollapse">
        <div class="card border border-primary-subtle shadow-sm rounded-4 p-4 bg-light">
            <h5 class="fw-bold mb-3"><i class="bi bi-tag-fill text-primary me-2"></i>Configure Promotional Coupon</h5>
            <form action="${pageContext.request.contextPath}/admin/coupons" method="post">
                <input type="hidden" name="action" value="create">

                <div class="row g-3">
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Coupon Code</label>
                        <input type="text" class="form-control form-control-sm text-uppercase" name="code" required placeholder="e.g. SUMMER30">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Discount Type</label>
                        <select class="form-select form-select-sm" name="discountType" required>
                            <option value="PERCENTAGE">PERCENTAGE (%)</option>
                            <option value="FLAT">FLAT AMOUNT (₹)</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Discount Value</label>
                        <input type="number" step="0.01" class="form-control form-control-sm" name="discountValue" required placeholder="20.00">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Min Order Amount (₹)</label>
                        <input type="number" step="0.01" class="form-control form-control-sm" name="minOrderAmount" value="0.00" required>
                    </div>

                    <div class="col-md-4">
                        <label class="form-label small fw-semibold">Max Discount Cap (₹, optional)</label>
                        <input type="number" step="0.01" class="form-control form-control-sm" name="maxDiscountAmount" placeholder="e.g. 500.00">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label small fw-semibold">Expiry Date</label>
                        <input type="date" class="form-control form-control-sm" name="expiryDate">
                    </div>
                    <div class="col-md-4 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary btn-sm w-100 fw-bold">Publish Promo Coupon</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Coupons Table -->
    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-dark small text-uppercase">
                    <tr>
                        <th scope="col" class="ps-4">ID</th>
                        <th scope="col">Code</th>
                        <th scope="col">Type</th>
                        <th scope="col">Value</th>
                        <th scope="col">Min Order</th>
                        <th scope="col">Max Cap</th>
                        <th scope="col">Expiry Date</th>
                        <th scope="col">Status</th>
                        <th scope="col" class="text-end pe-4">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${coupons}">
                        <tr>
                            <td class="ps-4 py-3 small text-muted">#${c.id}</td>
                            <td>
                                <strong class="badge bg-primary-subtle text-primary fs-6"><c:out value="${c.code}"/></strong>
                            </td>
                            <td class="small"><c:out value="${c.discountType}"/></td>
                            <td class="small fw-bold">
                                <c:choose>
                                    <c:when test="${c.discountType == 'PERCENTAGE'}">
                                        <fmt:formatNumber value="${c.discountValue}" maxFractionDigits="0"/>%
                                    </c:when>
                                    <c:otherwise>
                                        ₹<fmt:formatNumber value="${c.discountValue}" minFractionDigits="2"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="small">₹<fmt:formatNumber value="${c.minOrderAmount}" minFractionDigits="2"/></td>
                            <td class="small">
                                <c:choose>
                                    <c:when test="${not empty c.maxDiscountAmount}">
                                        ₹<fmt:formatNumber value="${c.maxDiscountAmount}" minFractionDigits="2"/>
                                    </c:when>
                                    <c:otherwise>No Cap</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="small text-muted"><fmt:formatDate value="${c.expiryDate}" pattern="dd MMM yyyy"/></td>
                            <td>
                                <span class="badge ${c.status == 'ACTIVE' ? 'bg-success' : 'bg-secondary'} small">
                                    ${c.status}
                                </span>
                            </td>
                            <td class="text-end pe-4">
                                <form action="${pageContext.request.contextPath}/admin/coupons" method="post" class="d-inline">
                                    <input type="hidden" name="action" value="toggleStatus">
                                    <input type="hidden" name="couponId" value="${c.id}">
                                    <input type="hidden" name="status" value="${c.status == 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'}">
                                    <button type="submit" class="btn ${c.status == 'ACTIVE' ? 'btn-outline-danger' : 'btn-outline-success'} btn-sm py-0 px-2 small">
                                        ${c.status == 'ACTIVE' ? 'Deactivate' : 'Activate'}
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
