<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Manage Products — ShopSphere Admin" scope="request"/>
<c:set var="activeTab" value="products" scope="request"/>
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

    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
        <div>
            <h3 class="fw-bold mb-0 text-dark"><i class="bi bi-box-seam me-2 text-danger"></i>Product Catalog Management</h3>
            <span class="text-muted small">Add, edit, adjust stock levels, or deactivate products</span>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-secondary btn-sm ${empty currentFilter ? 'active' : ''}">
                All Products
            </a>
            <a href="${pageContext.request.contextPath}/admin/products?filter=lowStock" class="btn btn-outline-danger btn-sm ${currentFilter == 'lowStock' ? 'active' : ''}">
                <i class="bi bi-exclamation-triangle me-1"></i>Low Stock Only
            </a>
            <button class="btn btn-primary btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#addProductCollapse">
                <i class="bi bi-plus-circle me-1"></i>Add New Product
            </button>
        </div>
    </div>

    <!-- Collapsible Add Product Form -->
    <div class="collapse mb-4" id="addProductCollapse">
        <div class="card border border-primary-subtle shadow-sm rounded-4 p-4 bg-light">
            <h5 class="fw-bold mb-3"><i class="bi bi-plus-square text-primary me-2"></i>New Product Information</h5>
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="add">

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label small fw-semibold">Product Title</label>
                        <input type="text" class="form-control form-control-sm" name="name" required placeholder="e.g. Sony WH-1000XM5 Wireless Headphones">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Brand</label>
                        <input type="text" class="form-control form-control-sm" name="brand" required placeholder="e.g. Sony">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Category</label>
                        <select class="form-select form-select-sm" name="categoryId" required>
                            <c:forEach var="c" items="${categories}">
                                <option value="${c.id}"><c:out value="${c.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-12">
                        <label class="form-label small fw-semibold">Detailed Description</label>
                        <textarea class="form-control form-control-sm" name="description" rows="3" required placeholder="Comprehensive specifications and technical details..."></textarea>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Price (₹)</label>
                        <input type="number" step="0.01" class="form-control form-control-sm" name="price" required placeholder="29999.00">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Discount (%)</label>
                        <input type="number" step="0.01" class="form-control form-control-sm" name="discountPercentage" value="0.0" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Initial Stock</label>
                        <input type="number" class="form-control form-control-sm" name="stock" required placeholder="25">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small fw-semibold">Status</label>
                        <select class="form-select form-select-sm" name="status">
                            <option value="ACTIVE" selected>ACTIVE</option>
                            <option value="INACTIVE">INACTIVE</option>
                        </select>
                    </div>

                    <div class="col-12">
                        <label class="form-label small fw-semibold">Image URL</label>
                        <input type="url" class="form-control form-control-sm" name="imageUrl" placeholder="https://images.unsplash.com/photo-...">
                    </div>

                    <div class="col-12 mt-3">
                        <button type="submit" class="btn btn-primary btn-sm px-4 fw-bold">Save and Publish Product</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Products Table -->
    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-dark small text-uppercase">
                    <tr>
                        <th scope="col" class="ps-4">ID</th>
                        <th scope="col">Product Name</th>
                        <th scope="col">Brand</th>
                        <th scope="col">Category</th>
                        <th scope="col">Price</th>
                        <th scope="col">Discount</th>
                        <th scope="col">Selling Price</th>
                        <th scope="col">Stock</th>
                        <th scope="col">Status</th>
                        <th scope="col" class="text-end pe-4">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td class="ps-4 py-3 small text-muted">#${p.id}</td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="bg-light rounded p-1 text-center me-2" style="width: 40px; height: 40px;">
                                        <img src="<c:out value='${p.imageUrl}'/>" style="max-height: 32px; max-width: 100%;" alt="" onerror="this.src='https://placehold.co/40x40?text=P';">
                                    </div>
                                    <div class="text-truncate" style="max-width: 200px;">
                                        <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="text-decoration-none text-dark fw-semibold small">
                                            <c:out value="${p.name}"/>
                                        </a>
                                    </div>
                                </div>
                            </td>
                            <td class="small"><c:out value="${p.brand}"/></td>
                            <td class="small"><c:out value="${p.categoryName}"/></td>
                            <td class="small">₹<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></td>
                            <td class="small"><fmt:formatNumber value="${p.discountPercentage}" maxFractionDigits="0"/>%</td>
                            <td class="small fw-bold text-primary">₹<fmt:formatNumber value="${p.discountedPrice}" minFractionDigits="2"/></td>
                            <td>
                                <span class="badge ${p.stock <= 0 ? 'bg-danger' : (p.stock <= 5 ? 'bg-warning text-dark' : 'bg-success-subtle text-success')}">
                                    ${p.stock}
                                </span>
                            </td>
                            <td>
                                <span class="badge ${p.status == 'ACTIVE' ? 'bg-success' : 'bg-secondary'} small">
                                    ${p.status}
                                </span>
                            </td>
                            <td class="text-end pe-4">
                                <div class="d-flex justify-content-end gap-1">
                                    <!-- Quick stock adjuster -->
                                    <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-flex align-items-center gap-1">
                                        <input type="hidden" name="action" value="updateStock">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <input type="number" name="quantityChange" value="10" class="form-control form-control-sm text-center p-0" style="width: 45px; height: 26px;" title="Stock delta">
                                        <button type="submit" class="btn btn-outline-success btn-sm p-1" title="Adjust stock" style="line-height: 1;">
                                            <i class="bi bi-plus-lg"></i>
                                        </button>
                                    </form>

                                    <!-- Deactivate -->
                                    <c:if test="${p.status == 'ACTIVE'}">
                                        <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${p.id}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm p-1" title="Deactivate product" onclick="return confirm('Deactivate this product?');" style="line-height: 1;">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>
