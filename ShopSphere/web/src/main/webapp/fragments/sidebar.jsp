<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
    =============================================================================
    RTU Syllabus Core Topic: Static Include vs Dynamic Include
    1. Static Include Directive: <%@ include file="..." %>
       - Translation-time inclusion: The source code of the included file is merged
         directly into the parent JSP before javac compilation.
       - Best for static headers, shared directives, and taglib imports.
    2. Dynamic Include Action: <jsp:include page="..." flush="true"/>
       - Runtime request dispatch: The target page is executed as a separate servlet
         and its generated response output is inserted into the response buffer.
       - Best for dynamic widgets, navigation bars, and modular views.
    =============================================================================
--%>

<div class="col-md-3 col-lg-2 d-md-block bg-white sidebar shadow-sm p-3 rounded mb-4">
    <div class="text-center mb-4">
        <div class="avatar-circle bg-primary text-white mx-auto d-flex align-items-center justify-content-center rounded-circle" style="width: 55px; height: 55px;">
            <i class="bi bi-shield-lock-fill fs-3"></i>
        </div>
        <h6 class="mt-2 mb-0 fw-bold"><c:out value="${sessionScope.user.name}"/></h6>
        <span class="badge bg-danger">Administrator</span>
    </div>

    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link ${activeTab == 'dashboard' ? 'active' : 'text-dark'}">
                <i class="bi bi-speedometer2 me-2"></i>Dashboard
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/products" class="nav-link ${activeTab == 'products' ? 'active' : 'text-dark'}">
                <i class="bi bi-box-seam me-2"></i>Products
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/categories" class="nav-link ${activeTab == 'categories' ? 'active' : 'text-dark'}">
                <i class="bi bi-tags me-2"></i>Categories
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link ${activeTab == 'orders' ? 'active' : 'text-dark'}">
                <i class="bi bi-cart-check me-2"></i>Orders
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/users" class="nav-link ${activeTab == 'users' ? 'active' : 'text-dark'}">
                <i class="bi bi-people me-2"></i>Customers
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/coupons" class="nav-link ${activeTab == 'coupons' ? 'active' : 'text-dark'}">
                <i class="bi bi-ticket-perforated me-2"></i>Coupons
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/inventory" class="nav-link ${activeTab == 'inventory' ? 'active' : 'text-dark'}">
                <i class="bi bi-clipboard2-pulse me-2"></i>Inventory
            </a>
        </li>
        <li class="nav-item mt-3 pt-3 border-top">
            <a href="${pageContext.request.contextPath}/" class="nav-link text-primary">
                <i class="bi bi-arrow-left-circle me-2"></i>Customer Store
            </a>
        </li>
    </ul>
</div>
