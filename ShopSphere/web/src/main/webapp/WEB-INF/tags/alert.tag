<%@ tag language="java" pageEncoding="UTF-8" description="Renders contextual dismissible alerts" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="type" required="false" type="java.lang.String" description="Alert severity: success, danger, warning, info (default: info)" %>
<%@ attribute name="message" required="true" type="java.lang.String" description="Alert body message text" %>
<%@ attribute name="dismissible" required="false" type="java.lang.Boolean" description="Show close button (default: true)" %>

<c:set var="alertType" value="${not empty type ? type : 'info'}"/>
<c:set var="canDismiss" value="${dismissible != null ? dismissible : true}"/>

<div class="alert alert-${alertType} ${canDismiss ? 'alert-dismissible' : ''} fade show shadow-sm rounded-3 mb-3" role="alert">
    <c:choose>
        <c:when test="${alertType == 'success'}"><i class="bi bi-check-circle-fill me-2"></i></c:when>
        <c:when test="${alertType == 'danger'}"><i class="bi bi-exclamation-triangle-fill me-2"></i></c:when>
        <c:when test="${alertType == 'warning'}"><i class="bi bi-exclamation-diamond-fill me-2"></i></c:when>
        <c:otherwise><i class="bi bi-info-circle-fill me-2"></i></c:otherwise>
    </c:choose>
    <span><c:out value="${message}"/></span>
    <c:if test="${canDismiss}">
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </c:if>
</div>
