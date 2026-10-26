
package com.app.DaoImp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.app.DaoIn.MenuDao;
import com.app.model.Menu;
import com.app.util.DBConnection;

public class MenuDaoImpl implements MenuDao {

    private static final String INSERT_MENU_QUERY =
            "INSERT INTO Menu (RestaurantId, ItemName, Description, " +
            "Price, IsAvailable, ratings, ImagePath) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_MENU_BY_ID_QUERY =
            "SELECT * FROM Menu WHERE MenuId = ?";

    private static final String UPDATE_MENU_QUERY =
            "UPDATE Menu SET RestaurantId = ?, ItemName = ?, " +
            "Description = ?, Price = ?, IsAvailable = ?, " +
            "ratings = ?, ImagePath = ? WHERE MenuId = ?";

    private static final String DELETE_MENU_QUERY =
            "DELETE FROM Menu WHERE MenuId = ?";

    private static final String GET_ALL_MENUS_QUERY =
            "SELECT * FROM Menu";

    private static final String GET_MENUS_BY_RESTAURANT_ID_QUERY =
            "SELECT * FROM Menu WHERE RestaurantId = ?";


    @Override
    public void addMenu(Menu menu) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_MENU_QUERY)) {

            ps.setInt(1, menu.getRestaurantId());
            ps.setString(2, menu.getItemName());
            ps.setString(3, menu.getDescription());
            ps.setDouble(4, menu.getPrice());
            ps.setBoolean(5, menu.isAvailable());
            ps.setDouble(6, menu.getRatings());
            ps.setString(7, menu.getImagePath());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Menu getMenu(int menuId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_MENU_BY_ID_QUERY)) {

            ps.setInt(1, menuId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Menu menu = new Menu();

                    menu.setMenuId(rs.getInt("MenuId"));
                    menu.setRestaurantId(rs.getInt("RestaurantId"));
                    menu.setItemName(rs.getString("ItemName"));
                    menu.setDescription(rs.getString("Description"));
                    menu.setPrice(rs.getDouble("Price"));
                    menu.setAvailable(rs.getBoolean("IsAvailable"));
                    menu.setRatings(rs.getDouble("ratings"));
                    menu.setImagePath(rs.getString("ImagePath"));

                    return menu;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public void updateMenu(Menu menu) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_MENU_QUERY)) {

            ps.setInt(1, menu.getRestaurantId());
            ps.setString(2, menu.getItemName());
            ps.setString(3, menu.getDescription());
            ps.setDouble(4, menu.getPrice());
            ps.setBoolean(5, menu.isAvailable());
            ps.setDouble(6, menu.getRatings());
            ps.setString(7, menu.getImagePath());
            ps.setInt(8, menu.getMenuId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteMenu(int menuId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_MENU_QUERY)) {

            ps.setInt(1, menuId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Menu> getAllMenus() {
        List<Menu> menus = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_MENUS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Menu menu = new Menu();

                menu.setMenuId(rs.getInt("MenuId"));
                menu.setRestaurantId(rs.getInt("RestaurantId"));
                menu.setItemName(rs.getString("ItemName"));
                menu.setDescription(rs.getString("Description"));
                menu.setPrice(rs.getDouble("Price"));
                menu.setAvailable(rs.getBoolean("IsAvailable"));
                menu.setRatings(rs.getDouble("ratings"));
                menu.setImagePath(rs.getString("ImagePath"));

                menus.add(menu);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return menus;
    }


    @Override
    public List<Menu> getMenusByRestaurantId(int restaurantId) {
        List<Menu> menus = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_MENUS_BY_RESTAURANT_ID_QUERY)) {

            ps.setInt(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Menu menu = new Menu();

                    menu.setMenuId(rs.getInt("MenuId"));
                    menu.setRestaurantId(rs.getInt("RestaurantId"));
                    menu.setItemName(rs.getString("ItemName"));
                    menu.setDescription(rs.getString("Description"));
                    menu.setPrice(rs.getDouble("Price"));
                    menu.setAvailable(rs.getBoolean("IsAvailable"));
                    menu.setRatings(rs.getDouble("ratings"));
                    menu.setImagePath(rs.getString("ImagePath"));

                    menus.add(menu);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return menus;
    }
}
