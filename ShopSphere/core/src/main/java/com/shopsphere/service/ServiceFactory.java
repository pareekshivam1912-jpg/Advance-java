package com.shopsphere.service;

import com.shopsphere.dao.*;

/**
 * Lightweight Service Locator / Factory wiring DAO and Service layers.
 * Avoids third-party dependency injection frameworks while providing uniform service access.
 */
public class ServiceFactory {

    private static final UserDAO userDAO = new UserDAOImpl();
    private static final AddressDAO addressDAO = new AddressDAOImpl();
    private static final CategoryDAO categoryDAO = new CategoryDAOImpl();
    private static final ProductDAO productDAO = new ProductDAOImpl();
    private static final CartDAO cartDAO = new CartDAOImpl();
    private static final WishlistDAO wishlistDAO = new WishlistDAOImpl();
    private static final CouponDAO couponDAO = new CouponDAOImpl();
    private static final OrderDAO orderDAO = new OrderDAOImpl();
    private static final OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
    private static final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private static final ReviewDAO reviewDAO = new ReviewDAOImpl();

    private static final UserService userService = new UserService(userDAO);
    private static final CategoryService categoryService = new CategoryService(categoryDAO);
    private static final ProductService productService = new ProductService(productDAO);
    private static final CartService cartService = new CartService(cartDAO, productDAO);
    private static final WishlistService wishlistService = new WishlistService(wishlistDAO, cartService);
    private static final AddressService addressService = new AddressService(addressDAO);
    private static final CouponService couponService = new CouponService(couponDAO);
    private static final CheckoutService checkoutService = new CheckoutService(cartDAO, productDAO, orderDAO, orderItemDAO, paymentDAO, couponDAO, couponService);
    private static final OrderService orderService = new OrderService(orderDAO, orderItemDAO, addressDAO, paymentDAO, couponDAO, productDAO);
    private static final ReviewService reviewService = new ReviewService(reviewDAO, productDAO);
    private static final LocalInventoryService localInventoryService = new LocalInventoryService(productDAO);
    private static final ReportService reportService = new ReportService(orderDAO, userDAO, productDAO);

    private ServiceFactory() {
        // Utility singleton factory
    }

    public static UserService getUserService() { return userService; }
    public static CategoryService getCategoryService() { return categoryService; }
    public static ProductService getProductService() { return productService; }
    public static CartService getCartService() { return cartService; }
    public static WishlistService getWishlistService() { return wishlistService; }
    public static AddressService getAddressService() { return addressService; }
    public static CouponService getCouponService() { return couponService; }
    public static CheckoutService getCheckoutService() { return checkoutService; }
    public static OrderService getOrderService() { return orderService; }
    public static ReviewService getReviewService() { return reviewService; }
    public static LocalInventoryService getLocalInventoryService() { return localInventoryService; }
    public static ReportService getReportService() { return reportService; }

    public static UserDAO getUserDAO() { return userDAO; }
    public static ProductDAO getProductDAO() { return productDAO; }
    public static CategoryDAO getCategoryDAO() { return categoryDAO; }
    public static OrderDAO getOrderDAO() { return orderDAO; }
    public static CouponDAO getCouponDAO() { return couponDAO; }
}
