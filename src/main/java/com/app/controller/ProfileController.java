
package com.app.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import com.app.DaoImp.UserDaoImpl;
import com.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDaoImpl userDao;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("[0-9+() -]{10,15}");

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoImpl();
    }

    // Display the logged-in user's profile
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null ||
                !(session.getAttribute("loggedInUser") instanceof User)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User sessionUser = (User) session.getAttribute("loggedInUser");

        try {
            User user = userDao.getUser(sessionUser.getUserId());

            if (user == null) {
                session.removeAttribute("loggedInUser");
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            // Refresh the session with the latest database details.
            session.setAttribute("loggedInUser", user);

            req.setAttribute("profileUser", user);

            if ("1".equals(req.getParameter("updated"))) {
                req.setAttribute("profileMessage",
                        "Your profile has been updated successfully.");
                req.setAttribute("profileMessageType", "success");
            }

            req.getRequestDispatcher("/profile.jsp")
               .forward(req, res);

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to retrieve profile details.", e);
        }
    }

    // Save the edited profile details
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        if (session == null ||
                !(session.getAttribute("loggedInUser") instanceof User)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User sessionUser = (User) session.getAttribute("loggedInUser");

        String name = trim(req.getParameter("name"));
        String username = trim(req.getParameter("username"));
        String email = trim(req.getParameter("email"));
        String phoneNumber = trim(req.getParameter("phoneNumber"));
        String address = trim(req.getParameter("address"));

        // Load the trusted database record.
        User user;

        try {
            user = userDao.getUser(sessionUser.getUserId());
        } catch (RuntimeException e) {
            throw new ServletException(
                    "Unable to retrieve profile details.", e);
        }

        if (user == null) {
            session.removeAttribute("loggedInUser");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Preserve submitted values if validation fails.
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);

        String error = validateProfile(
                name, username, email, phoneNumber, address);

        if (error != null) {
            showError(req, res, user, error);
            return;
        }

        // Check uniqueness only when a value has changed.
        try {
            if (!username.equals(sessionUser.getUsername())
                    && userDao.isUsernameTaken(username)) {
                showError(req, res, user,
                        "That username is already in use.");
                return;
            }

            if (!email.equalsIgnoreCase(sessionUser.getEmail())
                    && userDao.isEmailTaken(email)) {
                showError(req, res, user,
                        "That email address is already registered.");
                return;
            }

            /*
             * Password and role were loaded from the database.
             * Do not accept either value from the submitted form.
             */
            userDao.updateUser(user);

            // Refresh the session with the updated user.
            session.setAttribute("loggedInUser", user);

            // Post/Redirect/Get prevents accidental resubmission.
            res.sendRedirect(req.getContextPath()
                    + "/profile?updated=1");

        } catch (RuntimeException e) {
            getServletContext().log(
                    "Unable to update user profile.", e);

            showError(req, res, user,
                    "Unable to update your profile. Please try again.");
        }
    }

    private String validateProfile(
            String name,
            String username,
            String email,
            String phoneNumber,
            String address) {

        if (name.isEmpty() || username.isEmpty()
                || email.isEmpty() || phoneNumber.isEmpty()
                || address.isEmpty()) {
            return "Please fill in all profile fields.";
        }

        if (name.length() > 100 || username.length() > 100
                || email.length() > 254
                || phoneNumber.length() > 15
                || address.length() > 500) {
            return "One or more fields exceed the allowed length.";
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Please enter a valid email address.";
        }

        if (!PHONE_PATTERN.matcher(phoneNumber).matches()
                || phoneNumber.replaceAll("[^0-9]", "").length() < 10
                || phoneNumber.replaceAll("[^0-9]", "").length() > 15) {
            return "Please enter a valid phone number.";
        }

        return null;
    }

    private void showError(
            HttpServletRequest req,
            HttpServletResponse res,
            User user,
            String message)
            throws ServletException, IOException {

        req.setAttribute("profileUser", user);
        req.setAttribute("profileMessage", message);
        req.setAttribute("profileMessageType", "error");

        req.getRequestDispatcher("/profile.jsp")
           .forward(req, res);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
