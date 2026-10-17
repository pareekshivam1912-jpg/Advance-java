<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Admin Executive Dashboard — ShopSphere" scope="request"/>
<c:set var="activeTab" value="dashboard" scope="request"/>
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
            <h3 class="fw-bold mb-0 text-dark"><i class="bi bi-speedometer2 text-danger me-2"></i>Executive Storefront Analytics</h3>
            <span class="text-muted small">Real-time aggregate data computed directly from MySQL database</span>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="badge bg-dark border border-secondary px-3 py-2 small" title="Tracked by SessionListener">
                <i class="bi bi-broadcast text-success me-1"></i>Active Sessions: <strong>${applicationScope.activeSessionCount != null ? applicationScope.activeSessionCount : 1}</strong>
            </span>
            <a href="${pageContext.request.contextPath}/admin/products?filter=lowStock" class="btn btn-outline-danger btn-sm">
                <i class="bi bi-exclamation-diamond me-1"></i>Low Stock (${lowStockCount})
            </a>
            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline-primary btn-sm">
                <i class="bi bi-receipt me-1"></i>Manage Orders
            </a>
        </div>
    </div>

    <!-- KPI Metric Cards -->
    <div class="row g-4 mb-4">
        <!-- Total Sales -->
        <div class="col-12 col-sm-6 col-xl-3">
            <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
                <div class="d-flex align-items-center justify-content-between">
                    <div>
                        <span class="text-muted small fw-semibold text-uppercase">Total Sales Revenue</span>
                        <h3 class="fw-bold text-success mb-0 mt-1">₹<fmt:formatNumber value="${totalSales}" minFractionDigits="2"/></h3>
                    </div>
                    <div class="bg-success-subtle text-success rounded-circle p-3">
                        <i class="bi bi-currency-rupee fs-3"></i>
                    </div>
                </div>
                <div class="mt-2 text-muted x-small">Cumulative completed purchases</div>
            </div>
        </div>

        <!-- Total Orders -->
        <div class="col-12 col-sm-6 col-xl-3">
            <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
                <div class="d-flex align-items-center justify-content-between">
                    <div>
                        <span class="text-muted small fw-semibold text-uppercase">Total Orders</span>
                        <h3 class="fw-bold text-primary mb-0 mt-1"><c:out value="${totalOrders}"/></h3>
                    </div>
                    <div class="bg-primary-subtle text-primary rounded-circle p-3">
                        <i class="bi bi-receipt-cutoff fs-3"></i>
                    </div>
                </div>
                <div class="mt-2 text-muted x-small">All-time customer checkouts</div>
            </div>
        </div>

        <!-- Total Customers -->
        <div class="col-12 col-sm-6 col-xl-3">
            <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
                <div class="d-flex align-items-center justify-content-between">
                    <div>
                        <span class="text-muted small fw-semibold text-uppercase">Registered Customers</span>
                        <h3 class="fw-bold text-info mb-0 mt-1"><c:out value="${totalCustomers}"/></h3>
                    </div>
                    <div class="bg-info-subtle text-info rounded-circle p-3">
                        <i class="bi bi-people-fill fs-3"></i>
                    </div>
                </div>
                <div class="mt-2 text-muted x-small">Active buyer accounts</div>
            </div>
        </div>

        <!-- Low Stock Items -->
        <div class="col-12 col-sm-6 col-xl-3">
            <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
                <div class="d-flex align-items-center justify-content-between">
                    <div>
                        <span class="text-muted small fw-semibold text-uppercase">Low Stock Alerts</span>
                        <h3 class="fw-bold text-danger mb-0 mt-1"><c:out value="${lowStockCount}"/></h3>
                    </div>
                    <div class="bg-danger-subtle text-danger rounded-circle p-3">
                        <i class="bi bi-box-seam-fill fs-3"></i>
                    </div>
                </div>
                <div class="mt-2 text-muted x-small">Products with &le; 5 units</div>
            </div>
        </div>
    </div>

    <div class="row g-4">

        <!-- Recent Orders Table -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-4">
                <div class="card-header bg-white py-3 px-4 d-flex justify-content-between align-items-center">
                    <h5 class="fw-bold mb-0">Recent Customer Orders</h5>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline-primary btn-sm">View All Orders</a>
                </div>
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead class="table-light small text-uppercase">
                            <tr>
                                <th scope="col" class="ps-4">Order ID</th>
                                <th scope="col">Date</th>
                                <th scope="col">Total</th>
                                <th scope="col">Order Status</th>
                                <th scope="col">Payment</th>
                                <th scope="col" class="text-end pe-4">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty recentOrders}">
                                    <tr>
                                        <td colspan="6" class="text-center py-4 text-muted">No orders available.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="ord" items="${recentOrders}">
                                        <tr>
                                            <td class="ps-4 py-3 fw-bold text-primary">#${ord.id}</td>
                                            <td class="small text-muted"><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM, hh:mm a"/></td>
                                            <td class="fw-semibold">₹<fmt:formatNumber value="${ord.totalAmount}" minFractionDigits="2"/></td>
                                            <td>
                                                <span class="badge ${ord.orderStatus == 'DELIVERED' ? 'bg-success' : (ord.orderStatus == 'CANCELLED' ? 'bg-danger' : (ord.orderStatus == 'SHIPPED' ? 'bg-warning text-dark' : 'bg-primary'))} small">
                                                    ${ord.orderStatus}
                                                </span>
                                            </td>
                                            <td>
                                                <span class="badge ${ord.paymentStatus == 'PAID' ? 'bg-success-subtle text-success' : 'bg-warning-subtle text-warning-emphasis'} small">
                                                    ${ord.paymentStatus}
                                                </span>
                                            </td>
                                            <td class="text-end pe-4">
                                                <a href="${pageContext.request.contextPath}/admin/orders?status=${ord.orderStatus}" class="btn btn-outline-secondary btn-sm">
                                                    Manage
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

        <!-- Low Stock Inventory Warnings -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0 text-danger"><i class="bi bi-exclamation-triangle me-1"></i>Low Stock Alert</h5>
                    <span class="badge bg-danger">${lowStockCount} items</span>
                </div>
                <c:choose>
                    <c:when test="${empty lowStockProducts}">
                        <div class="text-center py-4 text-muted">
                            <i class="bi bi-check2-all text-success fs-2 d-block mb-1"></i>
                            <span class="small">All catalog items have healthy stock levels (&gt; 5 units).</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group list-group-flush">
                            <c:forEach var="p" items="${lowStockProducts}">
                                <div class="list-group-item px-0 py-2 d-flex justify-content-between align-items-center">
                                    <div class="text-truncate" style="max-width: 200px;">
                                        <h6 class="small fw-semibold mb-0 text-truncate"><c:out value="${p.name}"/></h6>
                                        <span class="x-small text-muted"><c:out value="${p.brand}"/> &bull; ₹<fmt:formatNumber value="${p.discountedPrice}" minFractionDigits="0"/></span>
                                    </div>
                                    <div class="text-end">
                                        <span class="badge ${p.stock <= 0 ? 'bg-danger' : 'bg-warning text-dark'} mb-1">${p.stock} units</span>
                                        <form action="${pageContext.request.contextPath}/admin/products" method="post">
                                            <input type="hidden" name="action" value="updateStock">
                                            <input type="hidden" name="id" value="${p.id}">
                                            <input type="hidden" name="quantityChange" value="20">
                                            <button type="submit" class="btn btn-outline-success btn-sm py-0 px-2 x-small" title="Quick restock +20">
                                                +20 Stock
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            <!-- RTU Syllabus Demonstration: Optional Distributed RMI Inventory Widget -->
            <%
                boolean rmiAvailable = false;
                int rmiLowStockCount = 0;
                try {
                    java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
                    Object rmiObj = reg.lookup("InventoryService");
                    if (rmiObj instanceof com.shopsphere.rmi.InventoryService) {
                        com.shopsphere.rmi.InventoryService rmiService = (com.shopsphere.rmi.InventoryService) rmiObj;
                        java.util.List<?> rmiItems = rmiService.getLowStockProducts(10);
                        rmiLowStockCount = (rmiItems != null) ? rmiItems.size() : 0;
                        rmiAvailable = true;
                    }
                } catch (Exception ignored) {
                    // Gracefully handles when standalone RMI server is offline
                }
                request.setAttribute("rmiAvailable", rmiAvailable);
                request.setAttribute("rmiLowStockCount", rmiLowStockCount);
            %>
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4 bg-light">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="x-small fw-bold text-uppercase text-muted">Distributed Architecture</span>
                    <span class="badge ${rmiAvailable ? 'bg-success' : 'bg-secondary'} small">
                        ${rmiAvailable ? 'RMI ONLINE' : 'RMI STANDBY'}
                    </span>
                </div>
                <h6 class="fw-bold mb-1"><i class="bi bi-broadcast me-2 text-primary"></i>RMI Remote Inventory Service</h6>
                <c:choose>
                    <c:when test="${rmiAvailable}">
                        <p class="x-small text-success mb-2">Connected to <code>rmi://localhost:1099/InventoryService</code></p>
                        <div class="small text-muted">Remote Low-Stock Items (&le;10): <strong>${rmiLowStockCount}</strong></div>
                    </c:when>
                    <c:otherwise>
                        <p class="x-small text-muted mb-0">
                            Standalone daemon offline. Launch via: <br>
                            <code>mvn exec:java -pl rmi ...</code>
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
