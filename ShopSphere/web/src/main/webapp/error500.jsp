<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Server Error (500) — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="main-content container py-5 text-center my-auto">
    <div class="py-5">
        <div class="display-1 fw-bold text-danger mb-3">500</div>
        <h2 class="fw-bold mb-3">Internal Server Error</h2>
        <p class="text-muted mb-4 lead">
            An unexpected error occurred while processing your request. Please try again later.
        </p>
        <c:if test="${not empty pageContext.exception}">
            <div class="card bg-light border-0 text-start mx-auto p-3 mb-4" style="max-width: 700px;">
                <h6 class="fw-bold text-danger">Exception Details:</h6>
                <code class="text-dark small"><c:out value="${pageContext.exception.message}"/></code>
            </div>
        </c:if>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary px-4 py-2 fw-semibold">
            <i class="bi bi-house-door me-1"></i> Return to Homepage
        </a>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
