<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="My Profile — ShopSphere" scope="request"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container py-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb small">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">My Profile</li>
        </ol>
    </nav>

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

    <div class="row g-4">

        <!-- Left Column: User Summary & Change Password -->
        <div class="col-lg-4">
            <!-- User Info Card -->
            <div class="card border-0 shadow-sm rounded-4 p-4 text-center mb-4">
                <div class="avatar-circle bg-primary-subtle text-primary mx-auto d-flex align-items-center justify-content-center rounded-circle mb-3" style="width: 80px; height: 80px;">
                    <i class="bi bi-person-fill display-5"></i>
                </div>
                <h5 class="fw-bold mb-1"><c:out value="${profileUser.name}"/></h5>
                <p class="text-muted small mb-2"><c:out value="${profileUser.email}"/></p>
                <div>
                    <span class="badge ${profileUser.role == 'ADMIN' ? 'bg-danger' : 'bg-primary'} px-3 py-1">
                        <c:out value="${profileUser.role}"/> ACCOUNT
                    </span>
                </div>
                <hr class="text-muted my-3">
                <div class="text-secondary small text-start">
                    <div class="mb-2"><i class="bi bi-telephone me-2"></i><c:out value="${not empty profileUser.phone ? profileUser.phone : 'Not provided'}"/></div>
                    <div><i class="bi bi-calendar-check me-2"></i>Member since <fmt:formatDate value="${profileUser.createdAt}" pattern="MMM yyyy"/></div>
                </div>
            </div>

            <!-- Change Password Card -->
            <div class="card border-0 shadow-sm rounded-4 p-4">
                <h6 class="fw-bold mb-3"><i class="bi bi-shield-lock me-2 text-primary"></i>Change Password</h6>
                <form action="${pageContext.request.contextPath}/profile" method="post">
                    <input type="hidden" name="action" value="changePassword">

                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Current Password</label>
                        <input type="password" class="form-control form-control-sm" name="oldPassword" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-semibold">New Password</label>
                        <input type="password" class="form-control form-control-sm" name="newPassword" minlength="6" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Confirm New Password</label>
                        <input type="password" class="form-control form-control-sm" name="confirmPassword" minlength="6" required>
                    </div>

                    <button type="submit" class="btn btn-outline-primary btn-sm w-100 fw-semibold">
                        Update Password
                    </button>
                </form>
            </div>
        </div>

        <!-- Right Column: Personal Information & Address Book -->
        <div class="col-lg-8">

            <!-- Personal Profile Edit -->
            <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                <h5 class="fw-bold mb-3"><i class="bi bi-pencil-square me-2 text-primary"></i>Personal Information</h5>
                <form action="${pageContext.request.contextPath}/profile" method="post">
                    <input type="hidden" name="action" value="updateProfile">

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Full Name</label>
                            <input type="text" class="form-control" name="name" value="<c:out value='${profileUser.name}'/>" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Email Address</label>
                            <input type="email" class="form-control bg-light" value="<c:out value='${profileUser.email}'/>" readonly disabled title="Email cannot be changed">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Mobile Phone</label>
                            <input type="text" class="form-control" name="phone" value="<c:out value='${profileUser.phone}'/>" placeholder="10-digit number">
                        </div>
                        <div class="col-12 mt-4">
                            <button type="submit" class="btn btn-primary btn-sm px-4 fw-semibold">
                                Save Profile Changes
                            </button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Address Book Management -->
            <div class="card border-0 shadow-sm rounded-4 p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0"><i class="bi bi-geo-alt me-2 text-primary"></i>Saved Delivery Addresses</h5>
                    <button class="btn btn-outline-primary btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#profileAddAddressCollapse" aria-expanded="false">
                        <i class="bi bi-plus-circle me-1"></i>Add New Address
                    </button>
                </div>

                <!-- Collapsible New Address Form -->
                <div class="collapse mb-4" id="profileAddAddressCollapse">
                    <div class="p-4 bg-light rounded-3 border">
                        <h6 class="fw-bold mb-3">New Address Details</h6>
                        <form action="${pageContext.request.contextPath}/profile" method="post">
                            <input type="hidden" name="action" value="addAddress">

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Recipient Full Name</label>
                                    <input type="text" class="form-control form-control-sm" name="fullName" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Mobile Phone</label>
                                    <input type="text" class="form-control form-control-sm" name="phone" required>
                                </div>
                                <div class="col-12">
                                    <label class="form-label small fw-semibold">Street / Flat / Locality</label>
                                    <input type="text" class="form-control form-control-sm" name="streetAddress" required>
                                </div>
                                <div class="col-md-5">
                                    <label class="form-label small fw-semibold">City</label>
                                    <input type="text" class="form-control form-control-sm" name="city" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">State</label>
                                    <input type="text" class="form-control form-control-sm" name="state" required>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">PIN Code</label>
                                    <input type="text" class="form-control form-control-sm" name="postalCode" required>
                                </div>
                                <div class="col-12">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="isDefault" id="profileDefaultCheck" value="true">
                                        <label class="form-check-label small" for="profileDefaultCheck">Set as default delivery address</label>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <button type="submit" class="btn btn-primary btn-sm fw-semibold">Save Address</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${empty addresses}">
                        <div class="text-center py-4 text-muted">
                            <i class="bi bi-pin-map display-6 d-block mb-2 text-secondary"></i>
                            <p class="small mb-0">No delivery addresses saved yet. Click above to add your first address.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row g-3">
                            <c:forEach var="addr" items="${addresses}">
                                <div class="col-12">
                                    <div class="border rounded-3 p-3 d-flex justify-content-between align-items-center ${addr.default ? 'border-primary bg-primary-subtle bg-opacity-10' : ''}">
                                        <div>
                                            <div class="d-flex align-items-center gap-2 mb-1">
                                                <strong class="text-dark small"><c:out value="${addr.fullName}"/></strong>
                                                <c:if test="${addr.default}">
                                                    <span class="badge bg-primary x-small">Default</span>
                                                </c:if>
                                            </div>
                                            <div class="text-secondary small">
                                                <c:out value="${addr.streetAddress}"/>, <c:out value="${addr.city}"/>, <c:out value="${addr.state}"/> - <c:out value="${addr.postalCode}"/>
                                            </div>
                                            <div class="text-muted x-small mt-1">
                                                <i class="bi bi-telephone me-1"></i><c:out value="${addr.phone}"/>
                                            </div>
                                        </div>
                                        <div class="d-flex gap-2">
                                            <c:if test="${!addr.default}">
                                                <form action="${pageContext.request.contextPath}/profile" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="setDefaultAddress">
                                                    <input type="hidden" name="addressId" value="${addr.id}">
                                                    <button type="submit" class="btn btn-outline-secondary btn-sm" title="Set as Default">
                                                        Set Default
                                                    </button>
                                                </form>
                                            </c:if>
                                            <form action="${pageContext.request.contextPath}/profile" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="deleteAddress">
                                                <input type="hidden" name="addressId" value="${addr.id}">
                                                <button type="submit" class="btn btn-outline-danger btn-sm" title="Delete Address" onclick="return confirm('Delete this address?');">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>

    </div>

</div>

<jsp:include page="fragments/footer.jsp"/>
