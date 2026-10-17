<%@ tag language="java" pageEncoding="UTF-8" description="Renders contextual color badges for order lifecycle states" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="status" required="true" type="java.lang.String" description="Order status string" %>

<c:choose>
    <c:when test="${status == 'DELIVERED'}">
        <span class="badge bg-success px-3 py-2 small"><i class="bi bi-house-check me-1"></i>Delivered</span>
    </c:when>
    <c:when test="${status == 'SHIPPED'}">
        <span class="badge bg-warning text-dark px-3 py-2 small"><i class="bi bi-truck me-1"></i>Shipped</span>
    </c:when>
    <c:when test="${status == 'CONFIRMED'}">
        <span class="badge bg-info text-dark px-3 py-2 small"><i class="bi bi-check2-circle me-1"></i>Confirmed</span>
    </c:when>
    <c:when test="${status == 'CANCELLED'}">
        <span class="badge bg-danger px-3 py-2 small"><i class="bi bi-x-circle me-1"></i>Cancelled</span>
    </c:when>
    <c:otherwise>
        <span class="badge bg-primary px-3 py-2 small"><i class="bi bi-receipt me-1"></i>Placed</span>
    </c:otherwise>
</c:choose>
