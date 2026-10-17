<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<footer class="mt-auto py-4 bg-dark text-white border-top border-secondary">
    <div class="container">
        <div class="row g-4">
            <div class="col-12 col-md-4">
                <h5 class="text-primary fw-bold"><i class="bi bi-shop me-2 text-warning"></i>ShopSphere</h5>
                <p class="text-secondary small mb-2">
                    An Enterprise E-Commerce Management System demonstrating Jakarta EE 10, Servlet 6.1, JSP 3.1, JDBC, Swing MVC, RMI, and Sockets.
                </p>
                <p class="text-secondary small mb-0">
                    RTU Advanced Java Comprehensive Practical Project
                </p>
            </div>
            <div class="col-6 col-md-2">
                <h6 class="text-uppercase fw-semibold mb-3 text-light">Catalog</h6>
                <ul class="list-unstyled small mb-0">
                    <li><a href="${pageContext.request.contextPath}/products" class="text-secondary text-decoration-none">All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=1" class="text-secondary text-decoration-none">Electronics</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=2" class="text-secondary text-decoration-none">Laptops</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=3" class="text-secondary text-decoration-none">Mobiles</a></li>
                </ul>
            </div>
            <div class="col-6 col-md-2">
                <h6 class="text-uppercase fw-semibold mb-3 text-light">Customer</h6>
                <ul class="list-unstyled small mb-0">
                    <li><a href="${pageContext.request.contextPath}/cart" class="text-secondary text-decoration-none">Shopping Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders" class="text-secondary text-decoration-none">Order Tracking</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist" class="text-secondary text-decoration-none">Wishlist</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile" class="text-secondary text-decoration-none">My Account</a></li>
                </ul>
            </div>
            <div class="col-12 col-md-4">
                <h6 class="text-uppercase fw-semibold mb-3 text-light">Academic Viva Demos</h6>
                <ul class="list-unstyled small mb-0">
                    <li><a href="${pageContext.request.contextPath}/demo/jsp-elements.jsp" class="text-secondary text-decoration-none">JSP Traditional Elements Lifecycle</a></li>
                    <li><a href="${pageContext.request.contextPath}/demo/el-demo.jsp" class="text-secondary text-decoration-none">Expression Language (EL) Scopes</a></li>
                    <li><a href="${pageContext.request.contextPath}/demo/jstl-xml.jsp" class="text-secondary text-decoration-none">JSTL XML Feed Processing</a></li>
                    <li><a href="${pageContext.request.contextPath}/demo/jstl-sql.jsp" class="text-secondary text-decoration-none">JSTL SQL Tag Library Demo</a></li>
                    <li><a href="${pageContext.request.contextPath}/demo/jndi-demo.jsp" class="text-secondary text-decoration-none">JNDI InitialContext DataSource</a></li>
                </ul>
            </div>
        </div>
        <hr class="my-4 border-secondary">
        <div class="d-flex flex-column flex-sm-row justify-content-between align-items-center small text-secondary">
            <span>&copy; 2026 ShopSphere System. All rights reserved.</span>
            <span>Apache Tomcat 11 &bull; OpenJDK 21 &bull; MySQL 9</span>
        </div>
    </div>
</footer>

<!-- Bootstrap 5 Bundle JS with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- ShopSphere Custom JS -->
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
