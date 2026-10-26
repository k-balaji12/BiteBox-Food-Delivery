
package com.app.DaoImp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.app.DaoIn.OrderDao;
import com.app.model.Order;
import com.app.util.DBConnection;

public class OrderDaoImpl implements OrderDao {

    private static final String INSERT_ORDER_QUERY =
            "INSERT INTO `Order` (RestaurantId, userId, orderDate, " +
            "totalamount, status, paymentMode) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_ORDER_BY_ID_QUERY =
            "SELECT * FROM `Order` WHERE orderId = ?";

    private static final String UPDATE_ORDER_QUERY =
            "UPDATE `Order` SET RestaurantId = ?, userId = ?, " +
            "orderDate = ?, totalamount = ?, status = ?, paymentMode = ? " +
            "WHERE orderId = ?";

    private static final String DELETE_ORDER_QUERY =
            "DELETE FROM `Order` WHERE orderId = ?";

    private static final String GET_ALL_ORDERS_QUERY =
            "SELECT * FROM `Order`";

    private static final String GET_ORDERS_BY_USER_ID_QUERY =
            "SELECT * FROM `Order` WHERE userId = ?";

    private static final String GET_ORDERS_BY_RESTAURANT_ID_QUERY =
            "SELECT * FROM `Order` WHERE RestaurantId = ?";


    @Override
    public int addOrder(Order order) {

        int generatedKey = -1;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     INSERT_ORDER_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getRestaurantId());
            ps.setInt(2, order.getUserId());

            if (order.getOrderDate() != null) {
                ps.setTimestamp(
                    3, Timestamp.valueOf(order.getOrderDate()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setDouble(4, order.getTotalAmount());
            ps.setString(5, order.getStatus());
            ps.setString(6, order.getPaymentMode());

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("Creating order failed.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getInt(1);
                    order.setOrderId(generatedKey);
                } else {
                    throw new SQLException(
                        "Creating order failed: no generated order ID.");
                }
            }

            return generatedKey;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add order.", e);
        }
    }


    @Override
    public Order getOrder(int orderId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ORDER_BY_ID_QUERY)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();

                    order.setOrderId(rs.getInt("orderId"));
                    order.setRestaurantId(rs.getInt("RestaurantId"));
                    order.setUserId(rs.getInt("userId"));

                    Timestamp orderDate = rs.getTimestamp("orderDate");
                    if (orderDate != null) {
                        order.setOrderDate(orderDate.toLocalDateTime());
                    }

                    order.setTotalAmount(rs.getDouble("totalamount"));
                    order.setStatus(rs.getString("status"));
                    order.setPaymentMode(rs.getString("paymentMode"));

                    return order;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public void updateOrder(Order order) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ORDER_QUERY)) {

            ps.setInt(1, order.getRestaurantId());
            ps.setInt(2, order.getUserId());

            if (order.getOrderDate() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setDouble(4, order.getTotalAmount());
            ps.setString(5, order.getStatus());
            ps.setString(6, order.getPaymentMode());
            ps.setInt(7, order.getOrderId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteOrder(int orderId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_ORDER_QUERY)) {

            ps.setInt(1, orderId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_ORDERS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();

                order.setOrderId(rs.getInt("orderId"));
                order.setRestaurantId(rs.getInt("RestaurantId"));
                order.setUserId(rs.getInt("userId"));

                Timestamp orderDate = rs.getTimestamp("orderDate");
                if (orderDate != null) {
                    order.setOrderDate(orderDate.toLocalDateTime());
                }

                order.setTotalAmount(rs.getDouble("totalamount"));
                order.setStatus(rs.getString("status"));
                order.setPaymentMode(rs.getString("paymentMode"));

                orders.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }


    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_ORDERS_BY_USER_ID_QUERY)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();

                    order.setOrderId(rs.getInt("orderId"));
                    order.setRestaurantId(rs.getInt("RestaurantId"));
                    order.setUserId(rs.getInt("userId"));

                    Timestamp orderDate = rs.getTimestamp("orderDate");
                    if (orderDate != null) {
                        order.setOrderDate(orderDate.toLocalDateTime());
                    }

                    order.setTotalAmount(rs.getDouble("totalamount"));
                    order.setStatus(rs.getString("status"));
                    order.setPaymentMode(rs.getString("paymentMode"));

                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }


    @Override
    public List<Order> getOrdersByRestaurantId(int restaurantId) {
        List<Order> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_ORDERS_BY_RESTAURANT_ID_QUERY)) {

            ps.setInt(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();

                    order.setOrderId(rs.getInt("orderId"));
                    order.setRestaurantId(rs.getInt("RestaurantId"));
                    order.setUserId(rs.getInt("userId"));

                    Timestamp orderDate = rs.getTimestamp("orderDate");
                    if (orderDate != null) {
                        order.setOrderDate(orderDate.toLocalDateTime());
                    }

                    order.setTotalAmount(rs.getDouble("totalamount"));
                    order.setStatus(rs.getString("status"));
                    order.setPaymentMode(rs.getString("paymentMode"));

                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }
}
