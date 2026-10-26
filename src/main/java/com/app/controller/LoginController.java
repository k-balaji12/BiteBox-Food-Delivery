package com.app.controller;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import com.app.DaoIn.UserDao;
import com.app.DaoImp.UserDaoImpl;
import com.app.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoImpl();
    }

    // Display the login page
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

    	if ("checkout".equals(req.getParameter("returnTo"))) {
    	    req.getSession().setAttribute("postLoginRedirect", "checkout");
    	} 
        RequestDispatcher dispatcher =
                req.getRequestDispatcher("/login.jsp");

        dispatcher.forward(req, res);
    }

    // Process login form submission
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String identifier = req.getParameter("identifier");
        String password = req.getParameter("password");

        // Validate required fields
        if (identifier == null || identifier.trim().isEmpty()
                || password == null || password.isEmpty()) {

            req.setAttribute("errorMessage",
                    "Please enter your username/email and password.");

            req.getRequestDispatcher("/login.jsp").forward(req, res);
            return;
        }

        identifier = identifier.trim();

        User user;

        try {
            // Allow login using either email or username.
            if (identifier.contains("@")) {
                user = userDao.getUser(identifier, password);
            } else {
                user = userDao.getUserByUsername(identifier, password);
            }

        } catch (Exception e) {
            throw new ServletException("Unable to process login.", e);
        }

        // Invalid credentials
        if (user == null) {
            req.setAttribute("errorMessage",
                    "Invalid username/email or password.");

            req.getRequestDispatcher("/login.jsp").forward(req, res);
            return;
        }

        // Create or reuse the session and prevent session fixation.
        if(user!=null && BCrypt.checkpw(password, user.getPassword())) {
        	
        	HttpSession session = req.getSession();
        	
        	req.changeSessionId();
        	
        	// Store the authenticated user.
        	session.setAttribute("loggedInUser", user);
        	
        	// Redirect to checkout if login was initiated from checkout.
        	String destination =
        			(String) session.getAttribute("postLoginRedirect");
        	
        	session.removeAttribute("postLoginRedirect");
        	
        	if ("checkout".equals(destination)) {
        		res.sendRedirect(req.getContextPath() + "/checkout");
        	} else {
        		res.sendRedirect(req.getContextPath() + "/restaurant");
        	}
        }
    }
}