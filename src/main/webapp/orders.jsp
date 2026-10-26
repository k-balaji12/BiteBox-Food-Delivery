
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.Map" %>
<%@ page import="com.app.model.Order,com.app.model.OrderItem" %>
<%@ page import="com.app.model.Menu,com.app.model.Restaurant" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    private String imageUrl(String path, String contextPath) {
        if (path == null || path.trim().isEmpty()) return "";
        path = path.trim();

        if (path.startsWith("https://") || path.startsWith("http://")) {
            return path;
        }

        while (path.startsWith("/")) {
            path = path.substring(1);
        }

        return contextPath + "/" + path;
    }
%>

<%
    String contextPath = request.getContextPath();

    List<Order> orders =
        (List<Order>) request.getAttribute("orders");

    Map<Integer, Restaurant> restaurants =
        (Map<Integer, Restaurant>) request.getAttribute("restaurants");

    Map<Integer, List<Map<String, Object>>> itemsByOrder =
        (Map<Integer, List<Map<String, Object>>>)
            request.getAttribute("itemsByOrder");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Orders | BiteBox</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/common.css">
    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/navbar.css">
    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/footer.css">
    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/orders.css">
</head>

<body>

    <%@ include file="/includes/navbar.jsp" %>

    <main class="orders-page">

        <header class="orders-heading">
            <div>
                <p class="orders-eyebrow">YOUR BITEBOX ACCOUNT</p>
                <h1>My Orders</h1>
                <p class="orders-subtitle">
                    Your food journey, all in one place.
                </p>
            </div>

            <a class="orders-shop-button"
               href="<%= contextPath %>/restaurant">
                Continue Shopping
            </a>
        </header>

        <% if (orders == null || orders.isEmpty()) { %>

            <section class="orders-empty">
                <div class="empty-icon" aria-hidden="true">&#127869;</div>
                <h2>No orders yet</h2>
                <p>
                    Looks like you haven't ordered anything.
                    Discover a restaurant and find your next favourite meal.
                </p>
                <a class="orders-primary-button"
                   href="<%= contextPath %>/restaurant">
                    Explore Restaurants
                </a>
            </section>

        <% } else { %>

            <div class="orders-count">
                <%= orders.size() %>
                <%= orders.size() == 1 ? "order" : "orders" %>
            </div>

            <section class="orders-list">

                <% for (Order order : orders) {
                    Restaurant restaurant = restaurants == null
                        ? null : restaurants.get(order.getOrderId());

                    List<Map<String, Object>> displayItems =
                        itemsByOrder == null ? null
                        : itemsByOrder.get(order.getOrderId());
                %>

                    <article class="order-card">

                        <div class="order-card-top">
                            <div class="restaurant-info">
                                <div class="restaurant-symbol"
                                     aria-hidden="true">&#127860;</div>

                                <div>
                                    <h2>
                                        <%= restaurant != null
                                            ? escapeHtml(restaurant.getName())
                                            : "Restaurant details unavailable" %>
                                    </h2>

                                    <% if (restaurant != null
                                            && restaurant.getAddress() != null
                                            && !restaurant.getAddress().trim().isEmpty()) { %>
                                        <p class="restaurant-address">
                                            <span aria-hidden="true">&#128205;</span>
                                            <%= escapeHtml(restaurant.getAddress()) %>
                                        </p>
                                    <% } %>
                                </div>
                            </div>

                            <span class="order-status">
                                <%= escapeHtml(order.getStatus()) %>
                            </span>
                        </div>

                        <div class="order-meta">
                            <div>
                                <span class="meta-label">ORDER NUMBER</span>
                                <strong>#<%= order.getOrderId() %></strong>
                            </div>

                            <div>
                                <span class="meta-label">ORDER DATE</span>
                                <strong>
                                    <%= order.getOrderDate() == null
                                        ? "Not available"
                                        : order.getOrderDate()
                                            .toString().replace("T", " ") %>
                                </strong>
                            </div>

                            <div>
                                <span class="meta-label">PAYMENT</span>
                                <strong>
                                    <%= escapeHtml(order.getPaymentMode()) %>
                                </strong>
                            </div>
                        </div>

                        <div class="ordered-items">
                            <h3>Items ordered</h3>

                            <% if (displayItems != null && !displayItems.isEmpty()) {
                                for (Map<String, Object> itemDetails : displayItems) {

                                    OrderItem orderItem =
                                        (OrderItem) itemDetails.get("orderItem");

                                    Menu menu = (Menu) itemDetails.get("menu");
                            %>

                                <div class="ordered-item">

                                    <div class="ordered-item-image">
                                        <% if (menu != null
                                                && menu.getImagePath() != null
                                                && !menu.getImagePath().trim().isEmpty()) { %>

                                            <img
                                                src="<%= escapeHtml(imageUrl(
                                                    menu.getImagePath(), contextPath)) %>"
                                                alt="<%= escapeHtml(menu.getItemName()) %>"
                                                loading="lazy"
                                                onerror="this.style.display='none';">
                                        <% } else { %>
                                            <span class="image-placeholder"
                                                  aria-label="Food image unavailable">
                                                &#127829;
                                            </span>
                                        <% } %>
                                    </div>

                                    <div class="ordered-item-info">
                                        <h4>
                                            <%= menu != null
                                                ? escapeHtml(menu.getItemName())
                                                : "Menu item #" + orderItem.getMenuId() %>
                                        </h4>
                                        <p>Quantity: <%= orderItem.getQuantity() %></p>
                                    </div>

                                    <strong class="ordered-item-total">
                                        &#8377;<%= String.format("%.2f",
                                            orderItem.getTotalAmount()) %>
                                    </strong>

                                </div>

                            <%  }
                               } else { %>

                                <p class="items-unavailable">
                                    Item details are currently unavailable.
                                </p>

                            <% } %>
                        </div>

                        <div class="order-card-bottom">
                            <span>Order total</span>
                            <strong>
                                &#8377;<%= String.format("%.2f",
                                    order.getTotalAmount()) %>
                            </strong>
                        </div>

                    </article>

                <% } %>

            </section>

        <% } %>

    </main>

    <%@ include file="/includes/footer.jsp" %>

</body>
</html>
