package com.app.DaoIn;

import java.util.List;
import com.app.model.OrderItem;

public interface OrderItemDao {

    void addOrderItem(OrderItem orderItem);

    OrderItem getOrderItem(int orderItemId);

    void updateOrderItem(OrderItem orderItem);

    void deleteOrderItem(int orderItemId);

    List<OrderItem> getAllOrderItems();

    List<OrderItem> getOrderItemsByOrderId(int orderId);
}