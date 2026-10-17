<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Create Account — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="main-content container py-5">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-6">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <div class="avatar-circle bg-primary-subtle text-primary mx-auto d-flex align-items-center justify-content-center rounded-circle mb-3" style="width: 60px; height: 60px;">
                            <i class="bi bi-person-plus-fill fs-2"></i>
                        </div>
                        <h3 class="fw-bold">Create Account</h3>
                        <p class="text-muted small">Join ShopSphere to enjoy personalized shopping and fast checkout</p>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <div><c:out value="${errorMessage}"/></div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="post" autocomplete="on">
                        <div class="mb-3">
                            <label for="name" class="form-label fw-semibold small">Full Name</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                                <input type="text" class="form-control" id="name" name="name"
                                       value="<c:out value='${name}'/>" placeholder="e.g. Amit Kumar" required autofocus>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label fw-semibold small">Email Address</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="<c:out value='${email}'/>" placeholder="name@example.com" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label fw-semibold small">Mobile Number</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-phone"></i></span>
                                <input type="tel" class="form-control" id="phone" name="phone"
                                       value="<c:out value='${phone}'/>" placeholder="9876543210" required>
                            </div>
                        </div>

                        <div class="row g-2 mb-3">
                            <div class="col-md-6">
                                <label for="password" class="form-label fw-semibold small">Password</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="bi bi-lock"></i></span>
                                    <input type="password" class="form-control" id="password" name="password"
                                           placeholder="Min 6 characters" minlength="6" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label for="confirmPassword" class="form-label fw-semibold small">Confirm Password</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="bi bi-lock-fill"></i></span>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                                           placeholder="Re-type password" minlength="6" required>
                                </div>
                            </div>
                        </div>

                        <div class="d-grid mt-4">
                            <button type="submit" class="btn btn-primary btn-lg fw-bold">
                                <i class="bi bi-check2-circle me-1"></i> Register Account
                            </button>
                        </div>
                    </form>

                    <div class="text-center mt-4 text-muted small">
                        Already have an account?
                        <a href="${pageContext.request.contextPath}/login" class="text-primary fw-semibold text-decoration-none">Sign In here</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
