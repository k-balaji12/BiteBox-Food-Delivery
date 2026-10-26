
package com.app.DaoImp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.app.DaoIn.RestaurantDao;
import com.app.model.Restaurant;
import com.app.util.DBConnection;

public class RestaurantDaoImpl implements RestaurantDao {

    private static final String INSERT_RESTAURANT_QUERY =
            "INSERT INTO Restaurant (Name, address, `phone number`, " +
            "CuisineType, DeliveryTime, AdminUserId, Rating, IsActive, " +
            "ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_RESTAURANT_BY_ID_QUERY =
            "SELECT * FROM Restaurant WHERE RestaurantId = ?";

    private static final String UPDATE_RESTAURANT_QUERY =
            "UPDATE Restaurant SET Name = ?, address = ?, " +
            "`phone number` = ?, CuisineType = ?, DeliveryTime = ?, " +
            "AdminUserId = ?, Rating = ?, IsActive = ?, ImagePath = ? " +
            "WHERE RestaurantId = ?";

    private static final String DELETE_RESTAURANT_QUERY =
            "DELETE FROM Restaurant WHERE RestaurantId = ?";

    private static final String GET_ALL_RESTAURANTS_QUERY =
            "SELECT * FROM Restaurant";


    @Override
    public void addRestaurant(Restaurant restaurant) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(INSERT_RESTAURANT_QUERY)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getAddress());
            ps.setString(3, restaurant.getPhoneNumber());
            ps.setString(4, restaurant.getCuisineType());
            ps.setString(5, restaurant.getDeliveryTime());
            ps.setInt(6, restaurant.getAdminUserId());
            ps.setDouble(7, restaurant.getRating());
            ps.setBoolean(8, restaurant.isActive());
            ps.setString(9, restaurant.getImagePath());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Restaurant getRestaurant(int restaurantId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_RESTAURANT_BY_ID_QUERY)) {

            ps.setInt(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Restaurant restaurant = new Restaurant();

                    restaurant.setRestaurantId(rs.getInt("RestaurantId"));
                    restaurant.setName(rs.getString("Name"));
                    restaurant.setAddress(rs.getString("address"));
                    restaurant.setPhoneNumber(rs.getString("phone number"));
                    restaurant.setCuisineType(rs.getString("CuisineType"));
                    restaurant.setDeliveryTime(rs.getString("DeliveryTime"));
                    restaurant.setAdminUserId(rs.getInt("AdminUserId"));
                    restaurant.setRating(rs.getDouble("Rating"));
                    restaurant.setActive(rs.getBoolean("IsActive"));
                    restaurant.setImagePath(rs.getString("ImagePath"));

                    return restaurant;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public void updateRestaurant(Restaurant restaurant) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(UPDATE_RESTAURANT_QUERY)) {

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getAddress());
            ps.setString(3, restaurant.getPhoneNumber());
            ps.setString(4, restaurant.getCuisineType());
            ps.setString(5, restaurant.getDeliveryTime());
            ps.setInt(6, restaurant.getAdminUserId());
            ps.setDouble(7, restaurant.getRating());
            ps.setBoolean(8, restaurant.isActive());
            ps.setString(9, restaurant.getImagePath());
            ps.setInt(10, restaurant.getRestaurantId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteRestaurant(int restaurantId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(DELETE_RESTAURANT_QUERY)) {

            ps.setInt(1, restaurantId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_ALL_RESTAURANTS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Restaurant restaurant = new Restaurant();

                restaurant.setRestaurantId(rs.getInt("RestaurantId"));
                restaurant.setName(rs.getString("Name"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setPhoneNumber(rs.getString("phone number"));
                restaurant.setCuisineType(rs.getString("CuisineType"));
                restaurant.setDeliveryTime(rs.getString("DeliveryTime"));
                restaurant.setAdminUserId(rs.getInt("AdminUserId"));
                restaurant.setRating(rs.getDouble("Rating"));
                restaurant.setActive(rs.getBoolean("IsActive"));
                restaurant.setImagePath(rs.getString("ImagePath"));

                restaurants.add(restaurant);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return restaurants;
    }
}
