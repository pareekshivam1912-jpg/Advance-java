<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-black sticky-top border-bottom border-secondary shadow-sm">
    <div class="container-fluid px-4">
        <!-- Admin Brand -->
        <a class="navbar-brand text-danger fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/admin/dashboard">
            <i class="bi bi-shield-shaded me-2 fs-4"></i>ShopSphere <span class="badge bg-danger ms-2 small">ADMIN CONSOLE</span>
        </a>

        <!-- Mobile Toggle -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNavbarContent"
                aria-controls="adminNavbarContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="adminNavbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'dashboard' ? 'active fw-bold text-white' : ''}" href="${pageContext.request.contextPath}/admin/dashboard">
                        <i class="bi bi-speedometer2 me-1"></i>Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'products' ? 'active fw-bold text-white' : ''}" href="${pageContext.request.contextPath}/admin/products">
                        <i class="bi bi-box-seam me-1"></i>Products
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'categories' ? 'active fw-bold text-white' : ''}" href="${pageContext.request.contextPath}/admin/categories">
                        <i class="bi bi-tags me-1"></i>Categories
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'orders' ? 'active fw-bold text-white' : ''}" href="${pageContext.request.contextPath}/admin/orders">
                        <i class="bi bi-receipt me-1"></i>Orders
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'users' ? 'active fw-bold text-white' : ''}" href="${pageContext.request.contextPath}/admin/users">
                        <i class="bi bi-people me-1"></i>Customers
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${activeTab == 'coupons' ? 'active fw-bold text-white' : ''}" href="${pageContext.request.contextPath}/admin/coupons">
                        <i class="bi bi-ticket-perforated me-1"></i>Coupons
                    </a>
                </li>
            </ul>

            <ul class="navbar-nav ms-auto align-items-lg-center">
                <li class="nav-item me-2">
                    <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/">
                        <i class="bi bi-shop me-1"></i>View Storefront
                    </a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-light" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle me-1 text-danger"></i>Administrator
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="bi bi-person me-2"></i>My Profile</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right me-2"></i>Sign Out</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
