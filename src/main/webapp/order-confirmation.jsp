
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.app.model.Order" %>
<%@ page import="com.app.model.OrderItem" %>
<%@ page import="com.app.model.Menu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%
    Order order = (Order) request.getAttribute("order");

    List<Map<String, Object>> orderDisplayItems =
        (List<Map<String, Object>>) request.getAttribute("orderDisplayItems");

    if (order == null) {
        response.sendRedirect(request.getContextPath() + "/restaurant");
        return;
    }

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Order Confirmed | BiteBox</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/common.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/navbar.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/footer.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/order-confirmation.css">
</head>

<body>

    <%@ include file="/includes/navbar.jsp" %>

    <main class="confirmation-page">

        <!-- Success heading -->
        <section class="success-heading">
            <div class="success-icon" aria-hidden="true">&#10003;</div>

            <p class="eyebrow">THANK YOU FOR CHOOSING BITEBOX</p>

            <h1>Order placed successfully!</h1>

            <p class="success-message">
                Your order is confirmed. Get ready for something delicious!
            </p>
        </section>

        <!-- Delivery animation -->
        <section class="delivery-animation"
                 aria-label="BiteBox delivery animation">

            <video class="delivery-video"
                   autoplay
                   muted
                   loop
                   playsinline
                   preload="metadata">

                <source
                    src="<%= contextPath %>/videos/video.mp4"
                    type="video/mp4">

            </video>
        </section>

        <!-- Order details card -->
        <section class="order-card">

            <div class="order-card-header">
                <div>
                    <p class="card-label">ORDER NUMBER</p>
                    <h2>#<%= order.getOrderId() %></h2>
                </div>

                <span class="order-status">Confirmed</span>
            </div>

            <div class="order-details">

                <div class="detail-row">
                    <span>Order date</span>
                    <strong><%= order.getOrderDate() %></strong>
                </div>

                <div class="detail-row">
                    <span>Payment method</span>
                    <strong><%= order.getPaymentMode() %></strong>
                </div>

                <div class="detail-row">
                    <span>Order status</span>
                    <strong><%= order.getStatus() %></strong>
                </div>

            </div>

            <!-- Ordered food items -->
            <div class="items-section">

                <h3>Order summary</h3>

                <% if (orderDisplayItems != null
                        && !orderDisplayItems.isEmpty()) { %>

                    <% for (Map<String, Object> displayItem
                            : orderDisplayItems) {

                        OrderItem item =
                            (OrderItem) displayItem.get("orderItem");

                        Menu menu =
                            (Menu) displayItem.get("menu");

                        String itemName = "Menu item #" + item.getMenuId();

                        String imagePath = null;

                        if (menu != null) {
                            if (menu.getItemName() != null
                                    && !menu.getItemName().trim().isEmpty()) {
                                itemName = menu.getItemName();
                            }

                            imagePath = menu.getImagePath();
                        }
                    %>

                        <div class="item-row">

                            <!-- Food image -->
                            <div class="item-image">

                                <% if (imagePath != null
                                        && !imagePath.trim().isEmpty()) { %>

                                    <img
                                        src="<%= imagePath %>"
                                        alt="<%= itemName %>"
                                        loading="lazy"
                                        onerror="this.style.display='none';
                                            this.nextElementSibling.style.display='flex';">

                                    <div class="item-image-placeholder"
                                         style="display:none;"
                                         aria-label="Image unavailable">
                                        &#127829;
                                    </div>

                                <% } else { %>

                                    <div class="item-image-placeholder"
                                         aria-label="Image unavailable">
                                        &#127829;
                                    </div>

                                <% } %>

                            </div>

                            <!-- Food name and quantity -->
                            <div class="item-info">

                                <span class="item-name">
                                    <%= itemName %>
                                </span>

                                <span class="item-quantity">
                                    Qty: <%= item.getQuantity() %>
                                </span>

                            </div>

                            <!-- Line total -->
                            <strong class="item-price">
                                &#8377;<%= String.format(
                                    java.util.Locale.US,
                                    "%.2f",
                                    item.getTotalAmount()
                                ) %>
                            </strong>

                        </div>

                    <% } %>

                <% } else { %>

                    <p class="empty-items">
                        Your order has been placed, but item details
                        are unavailable.
                    </p>

                <% } %>

            </div>

            <!-- Grand total -->
            <div class="total-row">
                <span>Total paid / payable</span>

                <strong>
                    &#8377;<%= String.format(
                        java.util.Locale.US,
                        "%.2f",
                        order.getTotalAmount()
                    ) %>
                </strong>
            </div>

        </section>

        <!-- Navigation buttons -->
        <section class="confirmation-actions">

            <a class="btn btn-primary"
               href="<%= contextPath %>/restaurant">
                Continue Shopping
            </a>

            <a class="btn btn-secondary"
               href="<%= contextPath %>/orders">
                View My Orders
            </a>

        </section>

        <p class="closing-note">
            Made with care. Delivered with love.
            <strong>BiteBox</strong>
        </p>

    </main>

    <%@ include file="/includes/footer.jsp" %>

</body>
</html>
