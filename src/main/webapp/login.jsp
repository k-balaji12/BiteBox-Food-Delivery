
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Login | BiteBox</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/navbar.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/login.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/CSS/footer.css">
</head>

<body>

    <%@ include file="/includes/navbar.jsp" %>

    <main class="login-page">
        <section class="login-card">

            <div class="login-heading">
                <span class="login-eyebrow">WELCOME BACK</span>
                <h1>Good to see you again!</h1>
                <p>Sign in to continue your BiteBox experience.</p>
            </div>

            <% if ("invalid".equals(request.getParameter("error"))) { %>
                <div class="message error" role="alert">
                    Invalid email/username or password.
                </div>
            <% } %>

            <% if ("loginRequired".equals(request.getParameter("message"))) { %>
                <div class="message error" role="alert">
                    Please login before proceeding to checkout.
                </div>
            <% } %>

            <% if ("registered".equals(request.getParameter("message"))) { %>
                <div class="message success" role="status">
                    Registration successful! Please login.
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login"
                  method="post">

                <div class="form-group">
                    <label for="identifier">Email or Username</label>
                    <input
                        type="text"
                        id="identifier"
                        name="identifier"
                        placeholder="Enter your email or username"
                        autocomplete="username"
                        required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Enter your password"
                        autocomplete="current-password"
                        required>
                </div>

                <button type="submit" class="login-btn">
                    Login to BiteBox <span aria-hidden="true">→</span>
                </button>

            </form>

            <div class="register-link">
                Don't have an account?
                <a href="${pageContext.request.contextPath}/register">
                    Create an account
                </a>
            </div>

            <div class="login-note">
                Fresh food, good moments, right at your doorstep.
            </div>

        </section>
    </main>

    <%@ include file="/includes/footer.jsp" %>

</body>
</html>
