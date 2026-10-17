<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Page Not Found (404) — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="main-content container py-5 text-center my-auto">
    <div class="py-5">
        <div class="display-1 fw-bold text-primary mb-3">404</div>
        <h2 class="fw-bold mb-3">Page Not Found</h2>
        <p class="text-muted mb-4 lead">
            The page you are looking for does not exist or may have been relocated.
        </p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary px-4 py-2 fw-semibold">
            <i class="bi bi-house-door me-1"></i> Return to Homepage
        </a>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
