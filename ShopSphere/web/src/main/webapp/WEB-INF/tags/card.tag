<%@ tag language="java" pageEncoding="UTF-8" description="Wrapper card component demonstrating jsp:doBody and jsp:invoke fragment" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="title" required="false" type="java.lang.String" description="Card header title" %>
<%@ attribute name="icon" required="false" type="java.lang.String" description="Bootstrap icon class" %>
<%@ attribute name="headerClass" required="false" type="java.lang.String" description="Optional extra CSS classes for header" %>
<%@ attribute name="footer" fragment="true" required="false" description="Optional footer fragment invoked via jsp:invoke" %>

<div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-4">
    <c:if test="${not empty title}">
        <div class="card-header bg-white py-3 px-4 ${headerClass}">
            <h5 class="fw-bold mb-0">
                <c:if test="${not empty icon}"><i class="${icon} me-2 text-primary"></i></c:if>
                <c:out value="${title}"/>
            </h5>
        </div>
    </c:if>

    <div class="card-body p-4">
        <!-- Renders the nested tag body -->
        <jsp:doBody/>
    </div>

    <!-- Renders the fragment attribute if provided -->
    <c:if test="${not empty footer}">
        <div class="card-footer bg-light py-2 px-4 border-top">
            <jsp:invoke fragment="footer"/>
        </div>
    </c:if>
</div>
