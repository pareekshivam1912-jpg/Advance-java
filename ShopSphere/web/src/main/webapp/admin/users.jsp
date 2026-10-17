<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Manage Users — ShopSphere Admin" scope="request"/>
<c:set var="activeTab" value="users" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/admin-navbar.jsp"/>

<div class="container-fluid px-4 py-4">

    <!-- Flash Alerts -->
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i><c:out value="${sessionScope.flashSuccess}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashSuccess" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><c:out value="${sessionScope.flashError}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashError" scope="session"/>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold mb-0 text-dark"><i class="bi bi-people text-danger me-2"></i>User &amp; Customer Directory</h3>
            <span class="text-muted small">Manage account authentication privileges and status locks</span>
        </div>
    </div>

    <!-- Users Table -->
    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-dark small text-uppercase">
                    <tr>
                        <th scope="col" class="ps-4">ID</th>
                        <th scope="col">Name</th>
                        <th scope="col">Email</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Role</th>
                        <th scope="col">Account Status</th>
                        <th scope="col">Registered</th>
                        <th scope="col" class="text-end pe-4">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${users}">
                        <tr>
                            <td class="ps-4 py-3 small text-muted">#${u.id}</td>
                            <td>
                                <strong class="small text-dark"><c:out value="${u.name}"/></strong>
                            </td>
                            <td class="small"><c:out value="${u.email}"/></td>
                            <td class="small text-muted"><c:out value="${not empty u.phone ? u.phone : '—'}"/></td>
                            <td>
                                <span class="badge ${u.role == 'ADMIN' ? 'bg-danger' : 'bg-primary'} small">
                                    ${u.role}
                                </span>
                            </td>
                            <td>
                                <span class="badge ${u.status == 'ACTIVE' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'} small">
                                    ${u.status}
                                </span>
                            </td>
                            <td class="small text-muted"><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></td>
                            <td class="text-end pe-4">
                                <c:choose>
                                    <c:when test="${u.status == 'ACTIVE'}">
                                        <form action="${pageContext.request.contextPath}/admin/users" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="toggleStatus">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <input type="hidden" name="newStatus" value="INACTIVE">
                                            <button type="submit" class="btn btn-outline-danger btn-sm py-0 px-2 small" onclick="return confirm('Deactivate user #${u.id}? User will not be able to log in.');">
                                                Deactivate
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/admin/users" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="toggleStatus">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <input type="hidden" name="newStatus" value="ACTIVE">
                                            <button type="submit" class="btn btn-outline-success btn-sm py-0 px-2 small">
                                                Activate
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
