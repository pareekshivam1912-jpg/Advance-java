<%@ tag language="java" pageEncoding="UTF-8" description="Renders star icons for numerical ratings" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="value" required="true" type="java.lang.Object" description="Rating value (e.g. 4.5)" %>
<%@ attribute name="max" required="false" type="java.lang.Integer" description="Max stars (default 5)" %>

<c:set var="maxStars" value="${max != null ? max : 5}"/>
<span class="text-warning small d-inline-flex align-items-center">
    <c:forEach begin="1" end="${maxStars}" var="starIndex">
        <c:choose>
            <c:when test="${value >= starIndex}">
                <i class="bi bi-star-fill me-1"></i>
            </c:when>
            <c:otherwise>
                <i class="bi bi-star me-1"></i>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <span class="text-dark fw-bold ms-1"><fmt:formatNumber value="${value}" minFractionDigits="1" maxFractionDigits="1"/></span>
</span>
