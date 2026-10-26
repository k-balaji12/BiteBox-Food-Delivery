<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.app.model.Menu" %>
<%@ page import="com.app.model.Cart" %>
<%@ page import="com.app.model.CartItem" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>BiteBox | Menu</title>

<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/navbar.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/menu.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/footer.css">
</head>

<body>

<!-- SHARED NAVBAR -->
<jsp:include page="/includes/navbar.jsp" />

<%
    int currentRestaurantId = 0;

    try {
        currentRestaurantId = Integer.parseInt(
            request.getParameter("restaurantId")
        );
    } catch (Exception ignored) {
        // Restaurant ID normally comes from MenuController.
    }

    Cart cart = (Cart) session.getAttribute("cart");

    int cartRestaurantId = (cart != null)
        ? cart.getRestaurantId()
        : 0;

    int totalCartCount = 0;

    if (cart != null && cart.getItems() != null) {
        for (CartItem ci : cart.getItems().values()) {
            totalCartCount += ci.getQuantity();
        }
    }

    boolean cartHasItems =
        cart != null
        && cart.getItems() != null
        && !cart.getItems().isEmpty();
%>

<!-- CART ICON -->
<div class="main-cart-row">
    <a href="<%= request.getContextPath() %>/cart"
       class="main-cart-link"
       aria-label="Open cart">

        <svg viewBox="0 0 24 24"
             fill="none"
             stroke="currentColor"
             stroke-width="1.8"
             stroke-linecap="round"
             stroke-linejoin="round"
             aria-hidden="true">
            <circle cx="9" cy="21" r="1"></circle>
            <circle cx="20" cy="21" r="1"></circle>
            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
        </svg>

        <% if (totalCartCount > 0) { %>
            <span class="cart-count"><%= totalCartCount %></span>
        <% } %>
    </a>
</div>

<!-- PAGE HEADING -->
<section class="menu-hero">
    <div class="eyebrow">MADE FRESH • SERVED WITH LOVE</div>

    <h1>Explore Our Menu</h1>

    <p>
        Discover delicious dishes, explore your favourites,
        and find something wonderful for your next meal.
    </p>
</section>

<!-- MENU ITEMS -->
<main class="menu-container">

<%
    List<Menu> menus =
        (List<Menu>) request.getAttribute("MenuByRestaurant");

    if (menus != null && !menus.isEmpty()) {
%>

    <div class="menu-grid">

    <%
        for (Menu item : menus) {
            String rawImagePath = item.getImagePath();
            boolean available = item.isAvailable();

            String finalImagePath;

            if (rawImagePath != null && !rawImagePath.trim().isEmpty()) {
                if (rawImagePath.startsWith("http://")
                        || rawImagePath.startsWith("https://")) {
                    finalImagePath = rawImagePath;
                } else if (rawImagePath.startsWith("/")) {
                    finalImagePath =
                        request.getContextPath() + rawImagePath;
                } else {
                    finalImagePath =
                        request.getContextPath() + "/" + rawImagePath;
                }
            } else {
                finalImagePath =
                    request.getContextPath() + "/images/biryani.png";
            }

            CartItem existingCartItem = null;

            if (cart != null && cart.getItems() != null) {
                existingCartItem =
                    cart.getItems().get(item.getMenuId());
            }

            int currentQuantity =
                (existingCartItem != null)
                    ? existingCartItem.getQuantity()
                    : 0;
    %>

        <article class="menu-card">

            <img class="menu-image"
                 src="<%= finalImagePath %>"
                 alt="<%= item.getItemName() %>"
                 loading="lazy">

            <div class="menu-info">

                <h2><%= item.getItemName() %></h2>

                <p class="description"><%=
                    (item.getDescription() == null
                        || item.getDescription().trim().isEmpty())
                        ? "Freshly prepared and ready to enjoy."
                        : item.getDescription()
                %></p>

                <div class="menu-meta">
                    <span class="price">
                        &#8377;<%= String.format(
                            java.util.Locale.US,
                            "%.2f",
                            item.getPrice()
                        ) %>
                    </span>

                    <span class="rating">
                        &#9733; <%= item.getRatings() %>
                    </span>
                </div>

                <div class="menu-footer">

                    <% if (available) { %>
                        <span class="availability">Available</span>
                    <% } else { %>
                        <span class="availability unavailable">
                            Unavailable
                        </span>
                    <% } %>

                    <% if (!available) { %>

                        <button class="add-btn"
                                type="button"
                                disabled>
                            Unavailable
                        </button>

                    <% } else if (currentQuantity == 0) { %>

                        <form class="add-to-cart-form"
                              action="<%= request.getContextPath() %>/cart"
                              method="post"
                              data-menu-id="<%= item.getMenuId() %>"
                              data-restaurant-id="<%= item.getRestaurantId() %>">

                            <input type="hidden"
                                   name="action"
                                   value="add">

                            <input type="hidden"
                                   name="menuId"
                                   value="<%= item.getMenuId() %>">

                            <button class="add-btn" type="submit">
                                Add to Cart
                            </button>
                        </form>

                    <% } else { %>

                        <div class="menu-quantity">

                            <!-- DECREASE QUANTITY -->
                            <form class="quantity-form"
                                  action="<%= request.getContextPath() %>/cart"
                                  method="post">

                                <input type="hidden"
                                       name="action"
                                       value="<%= (currentQuantity == 1)
                                           ? "remove" : "update" %>">

                                <input type="hidden"
                                       name="menuId"
                                       value="<%= item.getMenuId() %>">

                                <input type="hidden"
                                       name="quantity"
                                       value="<%= currentQuantity - 1 %>">

                                <input type="hidden"
                                       name="restaurantId"
                                       value="<%= item.getRestaurantId() %>">

                                <input type="hidden"
                                       name="returnTo"
                                       value="menu">

                                <button type="submit"
                                        class="quantity-btn"
                                        aria-label="Decrease quantity">
                                    −
                                </button>
                            </form>

                            <span class="quantity-value">
                                <%= currentQuantity %>
                            </span>

                            <!-- INCREASE QUANTITY -->
                            <form class="quantity-form"
                                  action="<%= request.getContextPath() %>/cart"
                                  method="post">

                                <input type="hidden"
                                       name="action"
                                       value="add">

                                <input type="hidden"
                                       name="menuId"
                                       value="<%= item.getMenuId() %>">

                                <button type="submit"
                                        class="quantity-btn"
                                        aria-label="Increase quantity">
                                    +
                                </button>
                            </form>

                        </div>

                    <% } %>

                </div>
            </div>
        </article>

    <% } %>

    </div>

<% } else { %>

    <div class="empty-state">
        <h2>No menu items found</h2>

        <p>
            Please return to the restaurants page
            and select a restaurant.
        </p>

        <a href="<%= request.getContextPath() %>/restaurant"
           class="back-btn">
            View Restaurants
        </a>
    </div>

<% } %>

</main>

<!-- RESTAURANT SWITCH POPUP -->
<div id="switchRestaurantModal"
     class="switch-modal"
     role="dialog"
     aria-modal="true"
     aria-labelledby="switchModalTitle"
     aria-describedby="switchModalMessage"
     aria-hidden="true">

    <div class="switch-modal-card">

        <h2 id="switchModalTitle">Switch Restaurant?</h2>

        <p id="switchModalMessage">
            Your cart already contains items from another restaurant.
            If you continue, your existing cart will be cleared and
            the selected item will be added.
        </p>

        <div class="switch-modal-actions">

            <button type="button"
                    id="cancelRestaurantSwitch"
                    class="switch-cancel-btn">
                Cancel
            </button>

            <button type="button"
                    id="confirmRestaurantSwitch"
                    class="switch-confirm-btn">
                Clear Cart &amp; Continue
            </button>

        </div>
    </div>
</div>

<!-- CONFIRM SWITCH FORM -->
<form id="confirmSwitchForm"
      action="<%= request.getContextPath() %>/cart"
      method="post"
      hidden>

    <input type="hidden"
           name="action"
           value="confirmSwitch">

    <input type="hidden"
           name="menuId"
           id="pendingSwitchMenuId"
           value="">

    <input type="hidden"
           name="restaurantId"
           id="pendingSwitchRestaurantId"
           value="">
</form>

<!-- SHARED FOOTER -->
<jsp:include page="/includes/footer.jsp" />

<script>
(function () {
    "use strict";

    const modal =
        document.getElementById("switchRestaurantModal");

    const confirmButton =
        document.getElementById("confirmRestaurantSwitch");

    const cancelButton =
        document.getElementById("cancelRestaurantSwitch");

    const confirmForm =
        document.getElementById("confirmSwitchForm");

    const pendingMenuId =
        document.getElementById("pendingSwitchMenuId");

    const pendingRestaurantId =
        document.getElementById("pendingSwitchRestaurantId");

    const cartRestaurantId = <%= cartRestaurantId %>;

    const cartIsNotEmpty =
        <%= cartHasItems ? "true" : "false" %>;

    let selectedAddForm = null;

    function openModal(form) {
        selectedAddForm = form;

        pendingMenuId.value = form.dataset.menuId;
        pendingRestaurantId.value = form.dataset.restaurantId;

        modal.classList.add("show");
        modal.setAttribute("aria-hidden", "false");
        document.body.classList.add("modal-open");

        confirmButton.focus();
    }

    function closeModal() {
        modal.classList.remove("show");
        modal.setAttribute("aria-hidden", "true");
        document.body.classList.remove("modal-open");

        selectedAddForm = null;
        pendingMenuId.value = "";
        pendingRestaurantId.value = "";
    }

    document.querySelectorAll(".add-to-cart-form").forEach(function (form) {
        form.addEventListener("submit", function (event) {
            const targetRestaurantId =
                Number(form.dataset.restaurantId);

            if (
                cartIsNotEmpty &&
                cartRestaurantId !== 0 &&
                targetRestaurantId !== cartRestaurantId
            ) {
                event.preventDefault();
                openModal(form);
            }
        });
    });

    confirmButton.addEventListener("click", function () {
        if (
            selectedAddForm &&
            pendingMenuId.value &&
            pendingRestaurantId.value
        ) {
            confirmButton.disabled = true;
            confirmForm.submit();
        }
    });

    cancelButton.addEventListener("click", closeModal);

    modal.addEventListener("click", function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    document.addEventListener("keydown", function (event) {
        if (
            event.key === "Escape" &&
            modal.classList.contains("show")
        ) {
            closeModal();
        }
    });
})();
</script>

</body>
</html>