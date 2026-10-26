
package com.app.DaoImp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.app.DaoIn.OrderItemDao;
import com.app.model.OrderItem;
import com.app.util.DBConnection;

public class OrderItemDaoImpl implements OrderItemDao {

    private static final String INSERT_ORDER_ITEM_QUERY =
            "INSERT INTO OrderItem (orderId, menuid, quantity, totalamount) " +
            "VALUES (?, ?, ?, ?)";

    private static final String GET_ORDER_ITEM_BY_ID_QUERY =
            "SELECT * FROM OrderItem WHERE orderitemid = ?";

    private static final String UPDATE_ORDER_ITEM_QUERY =
            "UPDATE OrderItem SET orderId = ?, menuid = ?, quantity = ?, " +
            "totalamount = ? WHERE orderitemid = ?";

    private static final String DELETE_ORDER_ITEM_QUERY =
            "DELETE FROM OrderItem WHERE orderitemid = ?";

    private static final String GET_ALL_ORDER_ITEMS_QUERY =
            "SELECT * FROM OrderItem";

    private static final String GET_ORDER_ITEMS_BY_ORDER_ID_QUERY =
            "SELECT * FROM OrderItem WHERE orderId = ?";


    @Override
    public void addOrderItem(OrderItem orderItem) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(INSERT_ORDER_ITEM_QUERY)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getTotalAmount());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public OrderItem getOrderItem(int orderItemId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_ORDER_ITEM_BY_ID_QUERY)) {

            ps.setInt(1, orderItemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrderItem orderItem = new OrderItem();

                    orderItem.setOrderItemId(rs.getInt("orderitemid"));
                    orderItem.setOrderId(rs.getInt("orderId"));
                    orderItem.setMenuId(rs.getInt("menuid"));
                    orderItem.setQuantity(rs.getInt("quantity"));
                    orderItem.setTotalAmount(rs.getDouble("totalamount"));

                    return orderItem;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public void updateOrderItem(OrderItem orderItem) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(UPDATE_ORDER_ITEM_QUERY)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getTotalAmount());
            ps.setInt(5, orderItem.getOrderItemId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteOrderItem(int orderItemId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(DELETE_ORDER_ITEM_QUERY)) {

            ps.setInt(1, orderItemId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_ALL_ORDER_ITEMS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderItem orderItem = new OrderItem();

                orderItem.setOrderItemId(rs.getInt("orderitemid"));
                orderItem.setOrderId(rs.getInt("orderId"));
                orderItem.setMenuId(rs.getInt("menuid"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setTotalAmount(rs.getDouble("totalamount"));

                orderItems.add(orderItem);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderItems;
    }


    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_ORDER_ITEMS_BY_ORDER_ID_QUERY)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem orderItem = new OrderItem();

                    orderItem.setOrderItemId(rs.getInt("orderitemid"));
                    orderItem.setOrderId(rs.getInt("orderId"));
                    orderItem.setMenuId(rs.getInt("menuid"));
                    orderItem.setQuantity(rs.getInt("quantity"));
                    orderItem.setTotalAmount(rs.getDouble("totalamount"));

                    orderItems.add(orderItem);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderItems;
    }
}
