
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | BiteBox</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/common.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/navbar.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/signup.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/footer.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="signup-page">
        <section class="signup-container">

            <div class="signup-intro">
                <span class="signup-label">WELCOME TO BITEBOX</span>
                <h1>Your next favourite meal is just a few clicks away.</h1>
                <p>
                    Create your account to explore restaurants, order your
                    favourite dishes, and manage your orders in one place.
                </p>

                <div class="signup-benefit">
                    <span class="benefit-icon">&#10003;</span>
                    <span>Discover restaurants and delicious meals</span>
                </div>

                <div class="signup-benefit">
                    <span class="benefit-icon">&#10003;</span>
                    <span>Keep track of your orders</span>
                </div>

                <div class="signup-benefit">
                    <span class="benefit-icon">&#10003;</span>
                    <span>Enjoy a simple ordering experience</span>
                </div>
            </div>

            <div class="signup-form-section">
                <div class="form-heading">
                    <h2>Create your account</h2>
                    <p>Enter your details to get started.</p>
                </div>

                <%
                    String signupError =
                        (String) request.getAttribute("signupError");

                    if (signupError != null && !signupError.trim().isEmpty()) {
                        String safeError = signupError
                            .replace("&", "&amp;")
                            .replace("<", "&lt;")
                            .replace(">", "&gt;")
                            .replace("\"", "&quot;")
                            .replace("'", "&#39;");
                %>
                    <div class="signup-error" role="alert">
                        <%= safeError %>
                    </div>
                <%
                    }
                %>

                <form action="${pageContext.request.contextPath}/register"
                      method="post"
                      class="signup-form">

                    <div class="form-group">
                        <label for="name">Full name</label>
                        <input type="text"
                               id="name"
                               name="name"
                               placeholder="Enter your full name"
                               maxlength="100"
                               autocomplete="name"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text"
                               id="username"
                               name="username"
                               placeholder="Choose a username"
                               minlength="3"
                               maxlength="50"
                               pattern="[A-Za-z0-9_]{3,50}"
                               title="Use 3–50 letters, numbers, or underscores."
                               autocomplete="username"
                               required>
                        <small>Use letters, numbers, or underscores.</small>
                    </div>

                    <div class="form-group">
                        <label for="email">Email address</label>
                        <input type="email"
                               id="email"
                               name="email"
                               placeholder="you@example.com"
                               maxlength="100"
                               autocomplete="email"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="phoneNumber">Phone number</label>
                        <input type="tel"
                               id="phoneNumber"
                               name="phoneNumber"
                               placeholder="Enter your phone number"
                               minlength="10"
                               maxlength="15"
                               pattern="[0-9]{10,15}"
                               title="Enter 10–15 digits."
                               autocomplete="tel"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="address">Address</label>
                        <textarea id="address"
                                  name="address"
                                  placeholder="Enter your address"
                                  maxlength="255"
                                  rows="3"
                                  autocomplete="street-address"
                                  required></textarea>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password"
                               id="password"
                               name="password"
                               placeholder="Create a password"
                               maxlength="255"
                               autocomplete="new-password"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirm password</label>
                        <input type="password"
                               id="confirmPassword"
                               name="confirmPassword"
                               placeholder="Re-enter your password"
                               maxlength="255"
                               autocomplete="new-password"
                               required>
                    </div>

                    <button type="submit" class="signup-button">
                        Create Account
                    </button>
                </form>

                <p class="login-prompt">
                    Already have an account?
                    <a href="${pageContext.request.contextPath}/login">
                        Log in
                    </a>
                </p>
            </div>

        </section>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
