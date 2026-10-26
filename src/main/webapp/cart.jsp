
<%@ page import="com.app.model.Cart" %>
<%@ page import="com.app.model.CartItem" %>
<%@ page import="com.app.model.Menu" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>

<%
    Cart cart = (Cart) session.getAttribute("cart");

    if (cart == null) {
        cart = new Cart();
        session.setAttribute("cart", cart);
    }

    Map<Integer, CartItem> items = cart.getItems();

    double grandTotal = 0.0;
    int totalQuantity = 0;

    if (items != null) {
        for (CartItem cartItem : items.values()) {
            if (cartItem != null && cartItem.getMenu() != null) {
                grandTotal += cartItem.getMenu().getPrice()
                        * cartItem.getQuantity();
                totalQuantity += cartItem.getQuantity();
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Your Cart | BiteBox</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/common.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/navbar.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/cart.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/CSS/footer.css">
</head>

<body>

    <%@ include file="/includes/navbar.jsp" %>

    <main class="cart-page">

        <div class="cart-container">

            <div class="cart-heading">
                <div>
                    <p class="cart-eyebrow">YOUR BITEBOX ORDER</p>
                    <h1>My Cart</h1>
                    <p class="cart-subtitle">
                        Review your items before checking out.
                    </p>
                </div>

                <% if (items != null && !items.isEmpty()) { %>
                    <form action="<%= request.getContextPath() %>/cart"
                          method="post"
                          class="clear-cart-form">

                        <input type="hidden"
                               name="action"
                               value="clear">

                        <button type="submit" class="clear-cart-btn">
                            Clear Cart
                        </button>
                    </form>
                <% } %>
            </div>

            <% if (items == null || items.isEmpty()) { %>

                <section class="empty-cart">
                    <div class="empty-cart-icon">&#128722;</div>

                    <h2>Your cart is empty</h2>

                    <p>
                        Looks like you haven't added any delicious food yet.
                    </p>

                    <a href="<%= request.getContextPath() %>/restaurant"
                       class="continue-shopping-btn">
                        Browse Restaurants
                    </a>
                </section>

            <% } else { %>

                <div class="cart-layout">

                    <section class="cart-items-section">

                        <div class="cart-section-heading">
                            <h2>Your Items</h2>
                            <span><%= items.size() %> item(s)</span>
                        </div>

                        <div class="cart-items-list">

                            <% for (CartItem cartItem : items.values()) {
                                if (cartItem == null || cartItem.getMenu() == null) {
                                    continue;
                                }

                                Menu menu = cartItem.getMenu();
                                int quantity = cartItem.getQuantity();

                                String imagePath = menu.getImagePath();
                                String imageUrl;

                                if (imagePath == null || imagePath.trim().isEmpty()) {
                                    imageUrl = request.getContextPath()
                                            + "/images/biryani.png";
                                } else if (imagePath.startsWith("http://")
                                        || imagePath.startsWith("https://")) {
                                    imageUrl = imagePath;
                                } else if (imagePath.startsWith("/")) {
                                    imageUrl = request.getContextPath()
                                            + imagePath;
                                } else {
                                    imageUrl = request.getContextPath()
                                            + "/" + imagePath;
                                }

                                double itemTotal = menu.getPrice() * quantity;
                            %>

                                <article class="cart-item">

                                    <img
                                        class="cart-item-image"
                                        src="<%= imageUrl %>"
                                        alt="<%= menu.getItemName() %>"
                                        onerror="this.onerror=null;this.src='<%= request.getContextPath() %>/images/biryani.png';">

                                    <div class="cart-item-details">

                                        <h3>
                                            <%= menu.getItemName() %>
                                        </h3>

                                        <p class="cart-item-price">
                                            &#8377;<%= String.format("%.2f", menu.getPrice()) %>
                                            <span>each</span>
                                        </p>

                                        <div class="cart-item-actions">

                                            <form
                                                action="<%= request.getContextPath() %>/cart"
                                                method="post"
                                                class="quantity-form">

                                                <input type="hidden"
                                                       name="action"
                                                       value="update">

                                                <input type="hidden"
                                                       name="menuId"
                                                       value="<%= menu.getMenuId() %>">

                                                <button
                                                    type="submit"
                                                    name="quantity"
                                                    value="<%= quantity - 1 %>"
                                                    class="quantity-btn"
                                                    aria-label="Decrease quantity"
                                                    <%= quantity <= 1 ? "disabled" : "" %>>
                                                    &minus;
                                                </button>

                                                <span class="quantity-value">
                                                    <%= quantity %>
                                                </span>

                                                <button
                                                    type="submit"
                                                    name="quantity"
                                                    value="<%= quantity + 1 %>"
                                                    class="quantity-btn"
                                                    aria-label="Increase quantity">
                                                    +
                                                </button>

                                            </form>

                                            <form
                                                action="<%= request.getContextPath() %>/cart"
                                                method="post"
                                                class="remove-form">

                                                <input type="hidden"
                                                       name="action"
                                                       value="remove">

                                                <input type="hidden"
                                                       name="menuId"
                                                       value="<%= menu.getMenuId() %>">

                                                <button type="submit"
                                                        class="remove-btn">
                                                    Remove
                                                </button>

                                            </form>

                                        </div>
                                    </div>

                                    <div class="cart-item-total">
                                        <span>Item total</span>
                                        <strong>
                                            &#8377;<%= String.format("%.2f", itemTotal) %>
                                        </strong>
                                    </div>

                                </article>

                            <% } %>

                        </div>

                        <a href="<%= request.getContextPath() %>/menu?restaurantId=<%= cart.getRestaurantId() %>"
                           class="continue-shopping-link">
                            &#8592; Continue Shopping
                        </a>

                    </section>

                    <aside class="cart-summary">

                        <h2>Order Summary</h2>

                        <div class="summary-row">
                            <span>Different items</span>
                            <span><%= items.size() %></span>
                        </div>

                        <div class="summary-row">
                            <span>Total quantity</span>
                            <span><%= totalQuantity %></span>
                        </div>

                        <div class="summary-divider"></div>

                        <div class="summary-row summary-total">
                            <span>Subtotal</span>
                            <strong>
                                &#8377;<%= String.format("%.2f", grandTotal) %>
                            </strong>
                        </div>

                        <p class="summary-note">
                            Delivery charges and applicable taxes, if any,
                            will be shown during checkout.
                        </p>

                        <a href="<%= request.getContextPath() %>/checkout"
                           class="checkout-btn">
                            Proceed to Checkout
                        </a>

                    </aside>

                </div>

            <% } %>

        </div>

    </main>

    <%@ include file="/includes/footer.jsp" %>

</body>
</html>
