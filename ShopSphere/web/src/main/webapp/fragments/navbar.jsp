<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'en'}" scope="session"/>
<fmt:setBundle basename="messages" scope="session"/>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container">
        <!-- Brand -->
        <a class="navbar-brand text-primary fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/">
            <i class="bi bi-shop me-2 text-warning"></i>Shop<span class="text-white">Sphere</span>
        </a>

        <!-- Mobile Toggle -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent"
                aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <!-- Search Form -->
            <form class="d-flex mx-lg-4 my-2 my-lg-0 flex-grow-1" action="${pageContext.request.contextPath}/products" method="get">
                <div class="input-group">
                    <fmt:message key="nav.search_placeholder" var="searchPlaceholder"/>
                    <input class="form-control" type="search" name="q" value="<c:out value='${param.q}'/>"
                           placeholder="${searchPlaceholder}" aria-label="Search">
                    <button class="btn btn-primary" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <!-- Nav Links -->
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-lg-center">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">
                        <i class="bi bi-grid me-1"></i><fmt:message key="nav.products"/>
                    </a>
                </li>

                <!-- Academic Demo Hub Link -->
                <li class="nav-item">
                    <a class="nav-link text-warning fw-semibold" href="${pageContext.request.contextPath}/demo/index.jsp">
                        <i class="bi bi-mortarboard-fill me-1"></i>Syllabus Demos
                    </a>
                </li>

                <!-- Language Switcher -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-translate me-1"></i>${sessionScope.lang == 'hi' ? 'हिंदी' : 'English'}
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/lang?set=en">English (en)</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/lang?set=hi">हिंदी (hi)</a></li>
                    </ul>
                </li>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <!-- Wishlist -->
                        <li class="nav-item">
                            <a class="nav-link position-relative me-2" href="${pageContext.request.contextPath}/wishlist" title="Wishlist">
                                <i class="bi bi-heart fs-5"></i>
                            </a>
                        </li>

                        <!-- Cart -->
                        <li class="nav-item">
                            <a class="nav-link position-relative me-3" href="${pageContext.request.contextPath}/cart" title="Shopping Cart">
                                <i class="bi bi-cart3 fs-5"></i>
                                <c:if test="${sessionScope.cartCount > 0}">
                                    <span class="position-absolute top-1 start-100 translate-middle badge rounded-pill bg-danger">
                                        <c:out value="${sessionScope.cartCount}"/>
                                    </span>
                                </c:if>
                            </a>
                        </li>

                        <!-- User Profile Menu -->
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="bi bi-person-circle fs-5 me-1 text-primary"></i>
                                <span class="d-none d-lg-inline"><c:out value="${sessionScope.user.name}"/></span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow">
                                <li class="dropdown-header">
                                    Signed in as <br><strong><c:out value="${sessionScope.user.email}"/></strong>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="bi bi-person me-2"></i><fmt:message key="nav.profile"/></a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders"><i class="bi bi-bag-check me-2"></i><fmt:message key="nav.orders"/></a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/wishlist"><i class="bi bi-heart me-2"></i><fmt:message key="nav.wishlist"/></a></li>
                                <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-danger fw-semibold" href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-speedometer2 me-2"></i><fmt:message key="nav.admin"/></a></li>
                                </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right me-2"></i><fmt:message key="nav.logout"/></a></li>
                            </ul>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <!-- Guest Links -->
                        <li class="nav-item ms-lg-2">
                            <a class="btn btn-outline-light btn-sm me-2" href="${pageContext.request.contextPath}/login">
                                <i class="bi bi-box-arrow-in-right me-1"></i><fmt:message key="nav.login"/>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/register">
                                <i class="bi bi-person-plus me-1"></i><fmt:message key="nav.register"/>
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Flash Messages Container -->
<div class="container mt-3">
    <c:if test="${not empty sessionScope.flashMessage}">
        <div class="alert alert-${sessionScope.flashType != null ? sessionScope.flashType : 'info'} alert-dismissible fade show shadow-sm" role="alert">
            <c:choose>
                <c:when test="${sessionScope.flashType == 'success'}"><i class="bi bi-check-circle-fill me-2"></i></c:when>
                <c:when test="${sessionScope.flashType == 'danger'}"><i class="bi bi-exclamation-triangle-fill me-2"></i></c:when>
                <c:otherwise><i class="bi bi-info-circle-fill me-2"></i></c:otherwise>
            </c:choose>
            <c:out value="${sessionScope.flashMessage}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashMessage" scope="session"/>
        <c:remove var="flashType" scope="session"/>
    </c:if>
</div>
