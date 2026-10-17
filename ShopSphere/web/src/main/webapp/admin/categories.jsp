<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Manage Categories — ShopSphere Admin" scope="request"/>
<c:set var="activeTab" value="categories" scope="request"/>
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
            <h3 class="fw-bold mb-0 text-dark"><i class="bi bi-tags text-danger me-2"></i>Storefront Categories</h3>
            <span class="text-muted small">Manage catalog departments and category classifications</span>
        </div>
        <button class="btn btn-primary btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#addCategoryCollapse">
            <i class="bi bi-plus-circle me-1"></i>Add New Category
        </button>
    </div>

    <!-- Collapsible Add Category Form -->
    <div class="collapse mb-4" id="addCategoryCollapse">
        <div class="card border border-primary-subtle shadow-sm rounded-4 p-4 bg-light">
            <h5 class="fw-bold mb-3"><i class="bi bi-folder-plus text-primary me-2"></i>Create New Category</h5>
            <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                <input type="hidden" name="action" value="add">

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label small fw-semibold">Category Name</label>
                        <input type="text" class="form-control form-control-sm" name="name" required placeholder="e.g. Gaming &amp; VR">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label small fw-semibold">Image URL (Optional)</label>
                        <input type="url" class="form-control form-control-sm" name="imageUrl" placeholder="https://images.unsplash.com/...">
                    </div>
                    <div class="col-12">
                        <label class="form-label small fw-semibold">Description</label>
                        <textarea class="form-control form-control-sm" name="description" rows="2" placeholder="Category purpose and scope..."></textarea>
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary btn-sm px-4 fw-semibold">Save Category</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Categories List Table -->
    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-dark small text-uppercase">
                    <tr>
                        <th scope="col" class="ps-4">ID</th>
                        <th scope="col">Category Name</th>
                        <th scope="col">Description</th>
                        <th scope="col">Status</th>
                        <th scope="col" class="text-end pe-4">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cat" items="${categories}">
                        <tr>
                            <td class="ps-4 py-3 small text-muted">#${cat.id}</td>
                            <td>
                                <strong class="text-dark small"><c:out value="${cat.name}"/></strong>
                            </td>
                            <td class="small text-secondary"><c:out value="${cat.description}"/></td>
                            <td>
                                <span class="badge ${cat.status == 'ACTIVE' ? 'bg-success' : 'bg-secondary'} small">
                                    ${cat.status}
                                </span>
                            </td>
                            <td class="text-end pe-4">
                                <c:if test="${cat.status == 'ACTIVE'}">
                                    <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${cat.id}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm p-1" title="Deactivate" onclick="return confirm('Deactivate category #${cat.id}?');" style="line-height: 1;">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
