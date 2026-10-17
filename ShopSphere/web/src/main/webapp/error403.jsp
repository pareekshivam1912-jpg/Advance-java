<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="403 Forbidden — Access Denied — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-5 my-5 text-center">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <div class="card border-0 shadow-sm rounded-4 p-5">
                <div class="mb-3 text-danger">
                    <i class="bi bi-shield-lock-fill display-1"></i>
                </div>
                <h1 class="display-4 fw-bold text-dark">403</h1>
                <h4 class="fw-bold mb-3">Access Forbidden</h4>
                <p class="text-muted small mb-4">
                    <c:out value="${errorMessage != null ? errorMessage : 'You do not have administrative permissions to access the requested resource.'}"/>
                </p>
                <div class="d-grid gap-2">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary fw-semibold py-2">
                        <i class="bi bi-house me-1"></i>Return to Storefront
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">
                        Switch Account / Sign Out
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
