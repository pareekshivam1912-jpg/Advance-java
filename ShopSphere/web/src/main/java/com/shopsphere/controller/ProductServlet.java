package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.model.User;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ReviewService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller managing catalog browsing, faceted filtering, searching, and product details views.
 * Configured in web.xml with ServletConfig init-params for syllabus demonstration.
 */
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService;
    private CategoryService categoryService;
    private ReviewService reviewService;
    private int defaultPageSize = 12;
    private String catalogSupportEmail = "catalog@shopsphere.com";

    @Override
    public void init() throws ServletException {
        this.productService = ServiceFactory.getProductService();
        this.categoryService = ServiceFactory.getCategoryService();
        this.reviewService = ServiceFactory.getReviewService();

        // Syllabus requirement: Demonstrate ServletConfig reading <init-param> from web.xml
        String pageSizeConfig = getServletConfig().getInitParameter("defaultPageSize");
        if (pageSizeConfig != null && !pageSizeConfig.trim().isEmpty()) {
            try {
                this.defaultPageSize = Integer.parseInt(pageSizeConfig.trim());
            } catch (NumberFormatException ignored) {}
        }
        String emailConfig = getServletConfig().getInitParameter("supportEmail");
        if (emailConfig != null && !emailConfig.trim().isEmpty()) {
            this.catalogSupportEmail = emailConfig.trim();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/product".equals(path)) {
            handleProductDetails(req, resp);
        } else {
            handleProductCatalog(req, resp);
        }
    }

    private void handleProductDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam.trim());
            Product product = productService.getProductById(productId);
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            List<Review> reviews = reviewService.getProductReviews(productId);
            List<Product> related = productService.getRelatedProducts(product.getCategoryId(), 4);

            // Check if authenticated user already reviewed
            Review userReview = null;
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                userReview = reviewService.getUserReview(user.getId(), productId);
            }

            // Update recently viewed products cookie (IDs formatted as id1-id2-id3)
            updateRecentlyViewedCookie(req, resp, productId);

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("relatedProducts", related);
            req.setAttribute("userReview", userReview);

            req.getRequestDispatcher("/product-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }

    private void handleProductCatalog(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("q");
        String categoryParam = req.getParameter("category");
        String minPriceParam = req.getParameter("minPrice");
        String maxPriceParam = req.getParameter("maxPrice");
        String sort = req.getParameter("sort");
        String pageParam = req.getParameter("page");

        Integer categoryId = null;
        if (categoryParam != null && !categoryParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        BigDecimal minPrice = null;
        if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
            try {
                minPrice = new BigDecimal(minPriceParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        BigDecimal maxPrice = null;
        if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
            try {
                maxPrice = new BigDecimal(maxPriceParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        int page = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam.trim()));
            } catch (NumberFormatException ignored) {}
        }

        int pageSize = this.defaultPageSize;
        List<Product> products = productService.searchProducts(keyword, categoryId, minPrice, maxPrice, sort, page, pageSize);
        int totalProducts = productService.getSearchCount(keyword, categoryId, minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Category> categories = categoryService.getActiveCategories();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("totalProducts", totalProducts);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("selectedCategory", categoryId);
        req.setAttribute("selectedSort", sort);
        req.setAttribute("minPrice", minPriceParam);
        req.setAttribute("maxPrice", maxPriceParam);
        req.setAttribute("searchQuery", keyword);

        req.getRequestDispatcher("/products.jsp").forward(req, resp);
    }

    private void updateRecentlyViewedCookie(HttpServletRequest req, HttpServletResponse resp, int currentId) {
        String existingVal = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("recent_products".equals(c.getName())) {
                    existingVal = c.getValue();
                    break;
                }
            }
        }

        List<String> idList = new ArrayList<>();
        idList.add(String.valueOf(currentId));
        if (!existingVal.isEmpty()) {
            String[] tokens = existingVal.split("-");
            for (String t : tokens) {
                if (!t.equals(String.valueOf(currentId)) && idList.size() < 5) {
                    idList.add(t);
                }
            }
        }

        String cookieValue = String.join("-", idList);
        Cookie recentCookie = new Cookie("recent_products", cookieValue);
        recentCookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        recentCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        recentCookie.setHttpOnly(true);
        resp.addCookie(recentCookie);
    }
}
