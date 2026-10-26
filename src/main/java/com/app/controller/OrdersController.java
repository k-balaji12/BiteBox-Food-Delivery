
package com.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.app.DaoImp.MenuDaoImpl;
import com.app.DaoImp.OrderDaoImpl;
import com.app.DaoImp.OrderItemDaoImpl;
import com.app.DaoImp.RestaurantDaoImpl;

import com.app.model.Menu;
import com.app.model.Order;
import com.app.model.OrderItem;
import com.app.model.Restaurant;
import com.app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/orders")
public class OrdersController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final OrderDaoImpl orderDao = new OrderDaoImpl();
    private final OrderItemDaoImpl orderItemDao = new OrderItemDaoImpl();
    private final MenuDaoImpl menuDao = new MenuDaoImpl();
    private final RestaurantDaoImpl restaurantDao = new RestaurantDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getContextPath();
        HttpSession session = request.getSession(false);

        // 1. Require login.
        if (session == null ||
                !(session.getAttribute("loggedInUser") instanceof User)) {

            response.sendRedirect(
                contextPath + "/login?returnTo=orders"
            );
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        try {
            // 2. Retrieve only this user's orders.
            List<Order> orders =
                    orderDao.getOrdersByUserId(loggedInUser.getUserId());

            if (orders == null) {
                orders = new ArrayList<>();
            }

            // 3. Prepare restaurant, order-item and menu details.
            Map<Integer, Restaurant> restaurants = new HashMap<>();
            Map<Integer, List<Map<String, Object>>> itemsByOrder =
                    new LinkedHashMap<>();

            for (Order order : orders) {

                // Restaurant details for this order.
                Restaurant restaurant =
                        restaurantDao.getRestaurant(order.getRestaurantId());

                if (restaurant != null) {
                    restaurants.put(order.getOrderId(), restaurant);
                }

                // Items belonging to this specific order.
                List<OrderItem> orderItems =
                        orderItemDao.getOrderItemsByOrderId(order.getOrderId());

                List<Map<String, Object>> displayItems = new ArrayList<>();

                if (orderItems != null) {
                    for (OrderItem orderItem : orderItems) {
                        Menu menu = menuDao.getMenu(orderItem.getMenuId());

                        Map<String, Object> itemDetails = new HashMap<>();
                        itemDetails.put("orderItem", orderItem);
                        itemDetails.put("menu", menu);

                        displayItems.add(itemDetails);
                    }
                }

                itemsByOrder.put(order.getOrderId(), displayItems);
            }

            // 4. Send the data to orders.jsp.
            request.setAttribute("orders", orders);
            request.setAttribute("restaurants", restaurants);
            request.setAttribute("itemsByOrder", itemsByOrder);

            request.getRequestDispatcher("/orders.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            log("Failed to load orders for user ID: "
                    + loggedInUser.getUserId(), e);

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Unable to load your orders. Please try again."
            );
        }
    }
}
