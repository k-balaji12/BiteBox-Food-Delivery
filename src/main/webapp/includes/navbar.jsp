
<%@ page import="com.app.model.User" %>
<%@ page import="com.app.model.Cart" %>
<%@ page import="com.app.model.CartItem" %>

<%
    User loggedInUser =
        (User) session.getAttribute("loggedInUser");

    Cart navbarCart = (Cart) session.getAttribute("cart");
    int navbarCartCount = 0;

    if (navbarCart != null && navbarCart.getItems() != null) {
        for (CartItem cartItem : navbarCart.getItems().values()) {
            if (cartItem != null) {
                navbarCartCount += cartItem.getQuantity();
            }
        }
    }

    String navbarContext = request.getContextPath();

    String navbarUserName = "";

    if (loggedInUser != null && loggedInUser.getName() != null) {
        navbarUserName = loggedInUser.getName()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
%>

<header class="bb-navbar">
    <div class="bb-nav-container">

        <!-- BiteBox brand -->
        <a class="bb-brand"
           href="<%= navbarContext %>/restaurant">
            <span class="bb-brand-icon">B</span>
            <span>Bite<span class="bb-brand-highlight">Box</span></span>
        </a>

        <!-- Main navigation -->
        <nav class="bb-nav-links" aria-label="Main navigation">

            <a href="<%= navbarContext %>/restaurant">
                Restaurants
            </a>

            <a href="<%= navbarContext %>/cart" class="bb-cart-link">
                Cart
                <% if (navbarCartCount > 0) { %>
                    <span class="bb-cart-count">
                        <%= navbarCartCount %>
                    </span>
                <% } %>
            </a>

            <a href="<%= navbarContext %>/orders">
                Orders
            </a>

        </nav>

        <!-- Account section -->
        <div class="bb-nav-account">

            <% if (loggedInUser != null) { %>

                <!-- Profile dropdown for logged-in users -->
                <details class="bb-profile-menu">

                    <summary class="bb-profile-trigger"
                             aria-label="Open profile menu">

                        <span class="bb-profile-icon"
                              aria-hidden="true">
                            <svg viewBox="0 0 24 24">
                                <circle cx="12" cy="8" r="4"></circle>
                                <path d="M4 21c0-4.4 3.6-7 8-7s8 2.6 8 7"></path>
                            </svg>
                        </span>

                        <span class="bb-profile-name">
                            <%= navbarUserName %>
                        </span>

                        <span class="bb-profile-chevron"
                              aria-hidden="true">&#9662;</span>

                    </summary>

                    <!-- Compact dropdown anchored to the profile -->
                    <div class="bb-profile-dropdown">

                        <a href="<%= navbarContext %>/profile"
                           class="bb-dropdown-item">
                            <span aria-hidden="true">&#128100;</span>
                            View Profile
                        </a>

                        <div class="bb-dropdown-divider"></div>

                        <form action="${pageContext.request.contextPath}/logout"
      										method="post">
                            <button type="submit"
                                    class="bb-dropdown-item bb-logout-button">
                                <span aria-hidden="true">&#10162;</span>
                                Logout
                            </button>

                        </form>

                    </div>

                </details>

            <% } else { %>

                <!-- Login for logged-out users -->
                <a class="bb-login-link"
                   href="<%= navbarContext %>/login">
                    Login
                </a>

            <% } %>

        </div>

    </div>
</header>
