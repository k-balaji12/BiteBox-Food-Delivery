
package com.app.controller;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.app.DaoImp.UserDaoImpl;
import com.app.model.User;

@WebServlet("/register")
public class SignupController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDaoImpl userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoImpl();
    }

    // Display the signup page
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/signup.jsp")
               .forward(request, response);
    }

    // Register a new customer
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = getTrimmedParameter(request, "name");
        String username = getTrimmedParameter(request, "username");
        String email = getTrimmedParameter(request, "email");
        String phoneNumber = getTrimmedParameter(request, "phoneNumber");
        String address = getTrimmedParameter(request, "address");

        // Do not trim passwords; spaces may be intentional.
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Validate required fields
        if (isBlank(name) || isBlank(username) || isBlank(email)
                || isBlank(phoneNumber) || isBlank(address)
                || isBlank(password) || isBlank(confirmPassword)) {

            showError(request, response, "All fields are required.");
            return;
        }

        // 2. Validate field lengths and formats
        if (name.length() > 100 || name.length() < 2) {
            showError(request, response,
                    "Name must contain 2-100 characters.");
            return;
        }

        if (!username.matches("[A-Za-z0-9_]{3,50}")) {
            showError(request, response,
                    "Username must contain 3-50 letters, numbers, or underscores.");
            return;
        }

        if (email.length() > 100
                || !email.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            showError(request, response, "Enter a valid email address.");
            return;
        }

        if (!phoneNumber.matches("[0-9]{10,15}")) {
            showError(request, response,
                    "Phone number must contain 10-15 digits.");
            return;
        }

        if (address.length() > 255) {
            showError(request, response,
                    "Address must not exceed 255 characters.");
            return;
        }

        if (password.length() > 255) {
            showError(request, response, "Password is too long.");
            return;
        }

        // 3. Confirm passwords match
        if (!password.equals(confirmPassword)) {
            showError(request, response, "Passwords do not match.");
            return;
        }

        try {
            // 4. Check for duplicate username
            if (userDao.isUsernameTaken(username)) {
                showError(request, response,
                        "Username already exists. Choose another.");
                return;
            }

            // 5. Check for duplicate email
            if (userDao.isEmailTaken(email)) {
                showError(request, response,
                        "Email already registered. Please use another.");
                return;
            }
            String hashedPassword = BCrypt.hashpw(
                    password,
                    BCrypt.gensalt(12)
            );
            // 6. Create the User model
            User user = new User();

            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);
            user.setPassword(hashedPassword);

            // Role is assigned on the server, not by the form.
            user.setRole("Customer");

            // 7. Save using the existing DAO method
            userDao.addUser(user);

            // 8. Redirect to the existing login page
            response.sendRedirect(
                    request.getContextPath()
                    + "/login?message=registered");

        } catch (RuntimeException e) {
            getServletContext().log("Signup failed.", e);

            showError(request, response,
                    "Registration failed. Please try again.");
        }
    }

    private String getTrimmedParameter(HttpServletRequest request,
                                       String parameterName) {
        String value = request.getParameter(parameterName);
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showError(HttpServletRequest request,
                           HttpServletResponse response,
                           String message)
            throws ServletException, IOException {

        request.setAttribute("signupError", message);

        request.getRequestDispatcher("/signup.jsp")
               .forward(request, response);
    }
}
