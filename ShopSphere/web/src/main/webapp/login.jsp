<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Sign In — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="main-content container py-5">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-5">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <div class="avatar-circle bg-primary-subtle text-primary mx-auto d-flex align-items-center justify-content-center rounded-circle mb-3" style="width: 60px; height: 60px;">
                            <i class="bi bi-person-fill-lock fs-2"></i>
                        </div>
                        <h3 class="fw-bold">Sign In</h3>
                        <p class="text-muted small">Enter your email and password to access your account</p>
                        <c:if test="${not empty loginNotice}">
                            <div class="badge bg-light text-secondary border px-3 py-2 text-wrap">
                                <i class="bi bi-info-circle me-1"></i><c:out value="${loginNotice}"/>
                            </div>
                        </c:if>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <div><c:out value="${errorMessage}"/></div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post" autocomplete="on">
                        <input type="hidden" name="redirect" value="<c:out value='${redirectUrl}'/>">

                        <div class="mb-3">
                            <label for="email" class="form-label fw-semibold small">Email Address</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="<c:out value='${email}'/>" placeholder="name@example.com" required autofocus>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label fw-semibold small">Password</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="••••••••" required>
                            </div>
                        </div>

                        <div class="d-grid mt-4">
                            <button type="submit" class="btn btn-primary btn-lg fw-bold">
                                <i class="bi bi-box-arrow-in-right me-1"></i> Sign In
                            </button>
                        </div>
                    </form>

                    <!-- Quick Demo Credentials Box -->
                    <div class="mt-4 p-3 bg-light rounded-3 border small">
                        <div class="fw-bold text-secondary mb-1"><i class="bi bi-lightbulb text-warning me-1"></i>Demo Logins:</div>
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <span>Admin: <code>admin@shopsphere.com</code> / <code>Admin@123</code></span>
                            <button type="button" class="btn btn-sm btn-outline-secondary py-0 px-2"
                                    onclick="document.getElementById('email').value='admin@shopsphere.com'; document.getElementById('password').value='Admin@123';">Fill</button>
                        </div>
                        <div class="d-flex justify-content-between align-items-center">
                            <span>Customer: <code>customer@shopsphere.com</code> / <code>Customer@123</code></span>
                            <button type="button" class="btn btn-sm btn-outline-secondary py-0 px-2"
                                    onclick="document.getElementById('email').value='customer@shopsphere.com'; document.getElementById('password').value='Customer@123';">Fill</button>
                        </div>
                    </div>

                    <div class="text-center mt-4 text-muted small">
                        Don't have an account?
                        <a href="${pageContext.request.contextPath}/register" class="text-primary fw-semibold text-decoration-none">Register here</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
