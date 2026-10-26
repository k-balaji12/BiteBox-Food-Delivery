
package com.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.DaoImp.MenuDaoImpl;
import com.app.DaoImp.OrderDaoImpl;
import com.app.DaoImp.OrderItemDaoImpl;
import com.app.DaoIn.OrderDao;
import com.app.DaoIn.OrderItemDao;
import com.app.model.Menu;
import com.app.model.Order;
import com.app.model.OrderItem;
import com.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order-confirmation")
public class OrderConfirmationController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderDao orderDao = new OrderDaoImpl();
    private final OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private final MenuDaoImpl menuDao = new MenuDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        int orderId;

        try {
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Order ID is required."
                );
                return;
            }

            orderId = Integer.parseInt(orderIdParam);

            if (orderId <= 0) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid order ID."
                );
                return;
            }

        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid order ID."
            );
            return;
        }

        try {
            Order order = orderDao.getOrder(orderId);

            if (order == null) {
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Order not found."
                );
                return;
            }

            // Customers can view only their own orders.
            if (order.getUserId() != loggedInUser.getUserId()) {
                response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You are not authorized to view this order."
                );
                return;
            }

            List<OrderItem> orderItems =
                    orderItemDao.getOrderItemsByOrderId(orderId);

            // Combine each order item with its corresponding menu details.
            List<Map<String, Object>> orderDisplayItems =
                    new ArrayList<>();

            if (orderItems != null) {
                for (OrderItem orderItem : orderItems) {
                    Menu menu = menuDao.getMenu(orderItem.getMenuId());

                    Map<String, Object> displayItem = new HashMap<>();
                    displayItem.put("orderItem", orderItem);
                    displayItem.put("menu", menu);

                    orderDisplayItems.add(displayItem);
                }
            }

            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);
            request.setAttribute("orderDisplayItems", orderDisplayItems);

            request.getRequestDispatcher("/order-confirmation.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            log("Failed to load order confirmation for order ID: "
                    + orderId, e);

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Unable to load your order confirmation."
            );
        }
    }
}
