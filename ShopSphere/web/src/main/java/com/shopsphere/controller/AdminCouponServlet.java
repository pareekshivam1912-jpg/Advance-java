package com.shopsphere.controller;

import com.shopsphere.exception.ShopSphereException;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CouponService;
import com.shopsphere.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller managing promotional discount coupons for administrators.
 */
@WebServlet(urlPatterns = {"/admin/coupons"})
public class AdminCouponServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CouponService couponService;

    @Override
    public void init() throws ServletException {
        this.couponService = ServiceFactory.getCouponService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getContextPath() + "/admin/coupons");
            return;
        }

        List<Coupon> coupons = couponService.getAllCoupons();
        req.setAttribute("coupons", coupons);
        req.getRequestDispatcher("/admin/coupons.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "create";
        }

        try {
            if ("create".equals(action)) {
                String code = req.getParameter("code");
                String discountType = req.getParameter("discountType");
                BigDecimal discountValue = new BigDecimal(req.getParameter("discountValue"));
                BigDecimal minOrder = new BigDecimal(req.getParameter("minOrderAmount"));
                String maxDiscountStr = req.getParameter("maxDiscountAmount");
                BigDecimal maxDiscount = (maxDiscountStr != null && !maxDiscountStr.trim().isEmpty()) ? new BigDecimal(maxDiscountStr.trim()) : null;

                String expiryStr = req.getParameter("expiryDate");
                Date expiryDate = (expiryStr != null && !expiryStr.trim().isEmpty()) ? Date.valueOf(expiryStr) : Date.valueOf(LocalDate.now().plusMonths(3));
                Date startDate = Date.valueOf(LocalDate.now());

                Coupon coupon = new Coupon(code, discountType, discountValue, minOrder, maxDiscount, startDate, expiryDate, "ACTIVE");
                couponService.addCoupon(coupon);
                session.setAttribute("flashSuccess", "Coupon '" + code.toUpperCase() + "' created successfully.");

            } else if ("toggleStatus".equals(action)) {
                int id = Integer.parseInt(req.getParameter("couponId"));
                String status = req.getParameter("status");
                couponService.toggleCouponStatus(id, status);
                session.setAttribute("flashSuccess", "Coupon status updated to " + status + ".");
            }
        } catch (ShopSphereException e) {
            session.setAttribute("flashError", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("flashError", "Failed to process coupon request: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/coupons");
    }
}
