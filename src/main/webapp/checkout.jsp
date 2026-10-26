<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="com.app.model.Cart" %>
<%@ page import="com.app.model.CartItem" %>
<%@ page import="com.app.model.User" %>
<%@ page import="java.util.Map" %>

<%!
    private String escapeHtml(Object value) {
        if (value == null) return "";
        return value.toString()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
%>

<%
    String contextPath = request.getContextPath();

    Cart cart = (Cart) request.getAttribute("cart");
    User checkoutUser = (User) request.getAttribute("checkoutUser");

    if (cart == null || cart.getItems() == null
            || cart.getItems().isEmpty()) {
        response.sendRedirect(contextPath + "/cart");
        return;
    }

    Map<Integer, CartItem> items = cart.getItems();
    boolean isLoggedIn = checkoutUser != null;

    double total = 0.0;
    int totalQuantity = 0;

    for (CartItem item : items.values()) {
        total += item.getTotalPrice();
        totalQuantity += item.getQuantity();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Checkout | BiteBox</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/common.css">
    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/navbar.css">
    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/checkout.css">
    <link rel="stylesheet"
          href="<%= contextPath %>/CSS/footer.css">
</head>

<body>

<!-- Keep the existing navbar unchanged -->
<jsp:include page="/includes/navbar.jsp" />

<main class="checkout-page">

    <header class="checkout-header">
        <h1>Checkout</h1>
        <p>Review your delivery details and complete your order.</p>
    </header>

    <!-- GUEST LOGIN NOTICE -->
    <% if (!isLoggedIn) { %>
    <div class="checkout-login-notice">
        <span class="login-lock" aria-hidden="true">&#128274;</span>

        <p>Please log in to continue with your order.</p>

        <a class="checkout-login-btn"
           href="<%= contextPath %>/login?returnTo=checkout">
            Log In
        </a>
    </div>
    <% } %>

	  <form id="checkoutForm"
	      action="${pageContext.request.contextPath}/checkout"
	      method="post">	

        <!-- 1. DELIVERY DETAILS -->
        <section class="checkout-section">

            <h2 class="section-title">1. Delivery Details</h2>

            <div class="delivery-grid">

                <div class="checkout-form-group">
                    <label for="fullName">Full Name</label>

                    <input type="text"
                           id="fullName"
                           name="fullName"
                           maxlength="100"
                           autocomplete="name"
                           value="<%= isLoggedIn
                               ? escapeHtml(checkoutUser.getName()) : "" %>"
                           placeholder="Enter your full name"
                           required>
                </div>

                <div class="checkout-form-group">
                    <label for="phone">Mobile Number</label>

                    <input type="tel"
                           id="phone"
                           name="phone"
                           maxlength="15"
                           autocomplete="tel"
                           pattern="[0-9+ -]{10,15}"
                           value="<%= isLoggedIn
                               ? escapeHtml(checkoutUser.getPhoneNumber()) : "" %>"
                           placeholder="Enter your mobile number"
                           required>
                </div>

                <div class="checkout-form-group span-2">
                    <label for="address">Delivery Address</label>

                    <textarea id="address"
                              name="address"
                              maxlength="500"
                              autocomplete="street-address"
                              placeholder="House number, street, area and city"
                              required><%= isLoggedIn
                                  ? escapeHtml(checkoutUser.getAddress()) : "" %></textarea>
                </div>

                <div class="checkout-form-group span-2">
                    <label for="pinCode">Delivery PIN Code</label>

                    <input type="text"
                           id="pinCode"
                           name="pinCode"
                           inputmode="numeric"
                           autocomplete="postal-code"
                           pattern="[0-9]{6}"
                           minlength="6"
                           maxlength="6"
                           placeholder="Enter 6-digit PIN code"
                           title="Enter a six-digit PIN code"
                           required>

                    <small class="field-hint">
                        Enter the PIN code for your delivery address.
                    </small>
                </div>

            </div>
        </section>

        <!-- 2. PAYMENT METHOD -->
        <section class="checkout-section">

            <h2 class="section-title">2. Payment Method</h2>

            <div class="payment-options">

                <label class="payment-option">
                    <input type="radio"
                           name="paymentMethod"
                           value="COD"
                           checked>

                    <span class="payment-icon" aria-hidden="true">
                        &#128181;
                    </span>

                    <span class="payment-text">
                        <strong>Cash on Delivery</strong>
                        <small>Pay when your order arrives.</small>
                    </span>

                    <span class="payment-indicator"></span>
                </label>

                <label class="payment-option">
                    <input type="radio"
                           name="paymentMethod"
                           value="UPI">

                    <span class="payment-icon" aria-hidden="true">
                        &#128241;
                    </span>

                    <span class="payment-text">
                        <strong>UPI</strong>
                        <small>Enter your UPI ID to complete payment.</small>
                    </span>

                    <span class="payment-indicator"></span>
                </label>

                <div id="upiFields" class="payment-details" hidden>
                    <div class="checkout-form-group">
                        <label for="upiId">UPI ID</label>
                        <input type="text"
                               id="upiId"
                               placeholder="example@upi"
                               maxlength="100"
                               autocomplete="off">
                    </div>
                </div>

                <label class="payment-option">
                    <input type="radio"
                           name="paymentMethod"
                           value="CARD">

                    <span class="payment-icon" aria-hidden="true">
                        &#128179;
                    </span>

                    <span class="payment-text">
                        <strong>Credit / Debit Card</strong>
                        <small>Enter card details to complete payment.</small>
                    </span>

                    <span class="payment-indicator"></span>
                </label>

                <div id="cardFields" class="payment-details" hidden>
                    <div class="card-fields-grid">

                        <div class="checkout-form-group">
                            <label for="demoCardNumber">Card Number</label>
                            <input type="text"
                                   id="demoCardNumber"
                                   inputmode="numeric"
                                   maxlength="19"
                                   placeholder="1234 5678 9012 3456"
                                   autocomplete="off">
                        </div>

                        <div class="checkout-form-group">
                            <label for="demoExpiry">Expiry Date</label>
                            <input type="text"
                                   id="demoExpiry"
                                   maxlength="5"
                                   placeholder="MM/YY"
                                   autocomplete="off">
                        </div>

                        <div class="checkout-form-group">
                            <label for="demoCvv">CVV</label>
                            <input type="password"
                                   id="demoCvv"
                                   inputmode="numeric"
                                   maxlength="4"
                                   placeholder="123"
                                   autocomplete="off">
                        </div>

                        <div class="checkout-form-group">
                            <label for="demoCardName">Cardholder Name</label>
                            <input type="text"
                                   id="demoCardName"
                                   maxlength="100"
                                   placeholder="Name on card"
                                   autocomplete="off">
                        </div>

                    </div>
                </div>

            </div>
        </section>

        <!-- 3. ORDER SUMMARY -->
        <section class="checkout-section">

            <div class="order-heading">
                <h2 class="section-title">3. Your Order</h2>
                <span><%= totalQuantity %> item(s)</span>
            </div>

            <div class="order-items">

                <% for (CartItem item : items.values()) {
                    String itemName = item.getMenu().getItemName();
                    String imagePath = item.getMenu().getImagePath();
                %>

                <article class="order-item">

                    <img class="order-item-image"
                         src="<%= escapeHtml(imagePath) %>"
                         alt="<%= escapeHtml(itemName) %>"
                         loading="lazy"
                         onerror="this.onerror=null;this.classList.add('image-unavailable');this.removeAttribute('src');">

                    <div class="order-item-info">
                        <h3><%= escapeHtml(itemName) %></h3>

                        <p class="unit-price">
                            &#8377;<%= String.format("%.2f",
                                item.getMenu().getPrice()) %>
                            <span>each</span>
                            <span class="quantity-text">
                                | Quantity: <%= item.getQuantity() %>
                            </span>
                        </p>
                    </div>

                    <div class="order-item-total">
                        <span>Item total</span>
                        <strong>
                            &#8377;<%= String.format("%.2f",
                                item.getTotalPrice()) %>
                        </strong>
                    </div>

                </article>

                <% } %>

            </div>

            <div class="order-bottom">

                <div class="order-totals">

                    <div class="total-line">
                        <span>Subtotal</span>
                        <strong>
                            &#8377;<%= String.format("%.2f", total) %>
                        </strong>
                    </div>

                    <div class="total-line">
                        <span>Delivery Fee</span>
                        <strong>&#8377;0.00</strong>
                    </div>

                    <div class="total-line grand-total">
                        <span>Total Amount</span>
                        <strong>
                            &#8377;<%= String.format("%.2f", total) %>
                        </strong>
                    </div>

                </div>

                <% if (isLoggedIn) { %>

                    <button type="submit" class="place-order-btn">
                        Place Order &middot;
                        &#8377;<%= String.format("%.2f", total) %>
                    </button>

                <% } else { %>

                    <a class="place-order-btn login-place-order-btn"
                       href="<%= contextPath %>/login?returnTo=checkout">
                        Log in to place order
                    </a>

                <% } %>

                <a class="back-to-cart"
                   href="<%= contextPath %>/cart">
                    &#8592; Back to Cart
                </a>

            </div>

        </section>

    </form>
</main>

<!-- Keep the existing footer include -->
<jsp:include page="/includes/footer.jsp" />

<script>
(function () {
    const form = document.getElementById("checkoutForm");

    const radios = document.querySelectorAll(
        'input[name="paymentMethod"]'
    );

    const upiFields = document.getElementById("upiFields");
    const cardFields = document.getElementById("cardFields");

    const upiId = document.getElementById("upiId");
    const cardName = document.getElementById("demoCardName");
    const cardNumber = document.getElementById("demoCardNumber");
    const expiry = document.getElementById("demoExpiry");
    const cvv = document.getElementById("demoCvv");

    function updatePaymentFields() {
        const selected = document.querySelector(
            'input[name="paymentMethod"]:checked'
        );

        const method = selected ? selected.value : "COD";

        upiFields.hidden = method !== "UPI";
        cardFields.hidden = method !== "CARD";

        if (upiId) upiId.required = method === "UPI";
        if (cardName) cardName.required = method === "CARD";
        if (cardNumber) cardNumber.required = method === "CARD";
        if (expiry) expiry.required = method === "CARD";
        if (cvv) cvv.required = method === "CARD";
    }

    radios.forEach(function (radio) {
        radio.addEventListener("change", updatePaymentFields);
    });

    form.addEventListener("submit", function (event) {
        const selected = document.querySelector(
            'input[name="paymentMethod"]:checked'
        );

        if (!selected) {
            event.preventDefault();
            alert("Please select a payment method.");
            return;
        }

        if (selected.value === "UPI") {
            if (!/^[\w.-]+@[\w.-]+$/.test(upiId.value.trim())) {
                event.preventDefault();
                alert("Enter a valid demo UPI ID, such as example@upi.");
                upiId.focus();
                return;
            }
        }

        if (selected.value === "CARD") {
            const number = cardNumber.value.replace(/\s/g, "");

            if (
                cardName.value.trim().length < 2 ||
                !/^\d{16}$/.test(number) ||
                !/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiry.value.trim()) ||
                !/^\d{3,4}$/.test(cvv.value.trim())
            ) {
                event.preventDefault();
                alert("Enter valid fictional demo card details.");
            }
        }
    });

    updatePaymentFields();
})();
</script>

</body>
</html>