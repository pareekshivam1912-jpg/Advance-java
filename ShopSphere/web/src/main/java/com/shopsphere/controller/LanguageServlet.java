package com.shopsphere.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;

import java.io.IOException;
import java.util.Locale;

/**
 * RTU Advanced Java Syllabus: Dynamic Locale Switching Servlet.
 * Maps /lang endpoint. Supports English ('en') and Hindi ('hi').
 * Persists locale in both HttpSession and persistent Cookie.
 */
@WebServlet(name = "LanguageServlet", urlPatterns = {"/lang"})
public class LanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lang = request.getParameter("set");
        if (lang == null || (!"hi".equalsIgnoreCase(lang) && !"en".equalsIgnoreCase(lang))) {
            lang = "en";
        }
        lang = lang.toLowerCase();

        Locale chosenLocale = "hi".equalsIgnoreCase(lang) ? Locale.of("hi", "IN") : Locale.of("en", "IN");

        // 1. Store in Session
        HttpSession session = request.getSession();
        session.setAttribute("lang", lang);
        session.setAttribute("currentLocale", chosenLocale);

        // 2. Configure Jakarta JSTL fmt:message runtime locale
        Config.set(session, Config.FMT_LOCALE, chosenLocale);

        // 3. Write persistent Cookie (30 days retention)
        Cookie langCookie = new Cookie("shopsphere_lang", lang);
        langCookie.setMaxAge(30 * 24 * 60 * 60);
        langCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        langCookie.setHttpOnly(true);
        response.addCookie(langCookie);

        // 4. Redirect back to Referer or Home
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.trim().isEmpty() && !referer.contains("/lang")) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
