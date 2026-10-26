package com.app.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import com.app.DaoImp.OrderDaoImpl;
import com.app.DaoImp.OrderItemDaoImpl;
import com.app.DaoImp.UserDaoImpl;
import com.app.DaoIn.OrderDao;
import com.app.DaoIn.OrderItemDao;
import com.app.DaoIn.UserDao;
import com.app.model.Cart;
import com.app.model.CartItem;
import com.app.model.Order;
import com.app.model.OrderItem;
import com.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderDao orderDao = new OrderDaoImpl();
    private final OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems() == null
                || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        User checkoutUser = null;

        if (loggedInUser != null) {
            checkoutUser = userDao.getUser(loggedInUser.getUserId());

            if (checkoutUser == null) {
                session.removeAttribute("loggedInUser");
                response.sendRedirect(request.getContextPath()
                        + "/login?returnTo=checkout");
                return;
            }
        }

        request.setAttribute("cart", cart);
        request.setAttribute("checkoutUser", checkoutUser);

        request.getRequestDispatcher("/checkout.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath()
                    + "/login?returnTo=checkout");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        // Login is required to place an order.
        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath()
                    + "/login?returnTo=checkout");
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems() == null
                || cart.getItems().isEmpty()
                || cart.getRestaurantId() <= 0) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Fetch current user details using userId.
        User user = userDao.getUser(loggedInUser.getUserId());

        if (user == null) {
            session.removeAttribute("loggedInUser");
            response.sendRedirect(request.getContextPath()
                    + "/login?returnTo=checkout");
            return;
        }

        String deliveryName = trim(request.getParameter("fullName"));
        String phone = trim(request.getParameter("phone"));
        String address = trim(request.getParameter("address"));
        String pinCode = trim(request.getParameter("pinCode"));
        String paymentMethod = trim(request.getParameter("paymentMethod"));

        if (deliveryName.isEmpty()
                || phone.isEmpty()
                || address.isEmpty()
                || !pinCode.matches("[0-9]{6}")) {

            request.setAttribute("checkoutError",
                    "Please enter your delivery details and a valid 6-digit PIN code.");
            doGet(request, response);
            return;
        }

        if (!phone.matches("[0-9+() -]{10,15}")) {
            request.setAttribute("checkoutError",
                    "Please enter a valid phone number.");
            doGet(request, response);
            return;
        }

        if (!("COD".equals(paymentMethod)
                || "UPI".equals(paymentMethod)
                || "CARD".equals(paymentMethod))) {

            request.setAttribute("checkoutError",
                    "Please select a valid payment method.");
            doGet(request, response);
            return;
        }

        double totalAmount = 0.0;

        for (Map.Entry<Integer, CartItem> entry
                : cart.getItems().entrySet()) {

            CartItem item = entry.getValue();

            if (item == null || item.getMenu() == null
                    || item.getQuantity() <= 0
                    || item.getMenu().getMenuId() != entry.getKey()
                    || item.getMenu().getPrice() < 0) {

                request.setAttribute("checkoutError",
                        "Your cart contains an invalid item. Please review your cart.");
                doGet(request, response);
                return;
            }

            totalAmount += item.getMenu().getPrice()
                    * item.getQuantity();
        }

        if (totalAmount <= 0) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        try {
            // Create the order first.
            Order order = new Order();

            order.setRestaurantId(cart.getRestaurantId());
            order.setUserId(user.getUserId());
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(totalAmount);
            order.setStatus("PLACED");
            order.setPaymentMode(paymentMethod);

            // Your DAO returns the generated order ID.
            int generatedOrderId = orderDao.addOrder(order);

            if (generatedOrderId <= 0) {
                throw new IllegalStateException(
                        "Order ID was not generated.");
            }

            // Store the returned ID in the Order model.
            order.setOrderId(generatedOrderId);

            // Insert order items using the same Order object.
            for (CartItem cartItem : cart.getItems().values()) {

                OrderItem orderItem = new OrderItem();

                orderItem.setOrderId(order.getOrderId());
                orderItem.setMenuId(
                        cartItem.getMenu().getMenuId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setTotalAmount(
                        cartItem.getMenu().getPrice()
                        * cartItem.getQuantity());

                orderItemDao.addOrderItem(orderItem);
            }

            // Clear cart only after all DAO calls succeed.
            cart.clearCart();

            response.sendRedirect(
            	    request.getContextPath()
            	    + "/order-confirmation?orderId=" + generatedOrderId
            	);
            	return;

        } catch (Exception e) {
            log("Failed to place order.", e);

            request.setAttribute("checkoutError",
                    "Unable to place your order. Please try again.");
            doGet(request, response);
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
