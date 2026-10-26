
package com.app.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get the current session without creating a new one.
        HttpSession session = req.getSession(false);

        // Invalidate the session if it exists.
        if (session != null) {
            session.invalidate();
        }

        // Redirect to the restaurant listing after logout.
        res.sendRedirect(req.getContextPath() + "/restaurant");
    }

    // Handle accidental GET requests safely.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Please use the logout form to log out.");
    }
}
