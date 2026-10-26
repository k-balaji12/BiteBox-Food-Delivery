package com.app.DaoIn;

import java.util.List;
import com.app.model.Order;

public interface OrderDao {

   int addOrder(Order order);

    Order getOrder(int orderId);

    void updateOrder(Order order);

    void deleteOrder(int orderId);

    List<Order> getAllOrders();

    List<Order> getOrdersByUserId(int userId);

    List<Order> getOrdersByRestaurantId(int restaurantId);
}