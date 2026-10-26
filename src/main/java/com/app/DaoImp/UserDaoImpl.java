
package com.app.DaoImp;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.app.DaoIn.UserDao;
import com.app.model.User;
import com.app.util.DBConnection;

public class UserDaoImpl implements UserDao {

    private static final String INSERT_USER_QUERY =
            "INSERT INTO `User` (name, Username, Password, Email, " +
            "`phone number`, Address, Role) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_USER_BY_ID_QUERY =
            "SELECT * FROM `User` WHERE UserId = ?";

    private static final String GET_USER_BY_EMAIL_QUERY =
            "SELECT * FROM `User` WHERE Email = ?";

    private static final String GET_USER_BY_USERNAME_QUERY =
            "SELECT * FROM `User` WHERE Username = ?";

    private static final String UPDATE_USER_QUERY =
            "UPDATE `User` SET name = ?, Username = ?, Password = ?, " +
            "Email = ?, `phone number` = ?, Address = ?, Role = ? " +
            "WHERE UserId = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM `User` WHERE UserId = ?";

    private static final String GET_ALL_USERS_QUERY =
            "SELECT * FROM `User`";


    @Override
    public void addUser(User user) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_USER_QUERY)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public User getUser(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USER_BY_ID_QUERY)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();

                    user.setUserId(rs.getInt("UserId"));
                    user.setName(rs.getString("name"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhoneNumber(rs.getString("phone number"));
                    user.setAddress(rs.getString("Address"));
                    user.setRole(rs.getString("Role"));

                    Timestamp createDate = rs.getTimestamp("CreateDate");
                    if (createDate != null) {
                        user.setCreateDate(createDate.toLocalDateTime());
                    }

                    Timestamp lastLoginDate = rs.getTimestamp("LastLoginDate");
                    if (lastLoginDate != null) {
                        user.setLastLoginDate(lastLoginDate.toLocalDateTime());
                    }

                    return user;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public User getUser(String email, String password) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USER_BY_EMAIL_QUERY)) {

        	ps.setString(1, email);

        	try (ResultSet rs = ps.executeQuery()) {
        	    if (rs.next()) {

        	        String storedHash = rs.getString("Password");

        	        if (!isPasswordValid(password, storedHash)) {
        	            return null;
        	        }

        	        User user = new User();
                    user.setUserId(rs.getInt("UserId"));
                    user.setName(rs.getString("name"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhoneNumber(rs.getString("phone number"));
                    user.setAddress(rs.getString("Address"));
                    user.setRole(rs.getString("Role"));

                    Timestamp createDate = rs.getTimestamp("CreateDate");
                    if (createDate != null) {
                        user.setCreateDate(createDate.toLocalDateTime());
                    }

                    Timestamp lastLoginDate = rs.getTimestamp("LastLoginDate");
                    if (lastLoginDate != null) {
                        user.setLastLoginDate(lastLoginDate.toLocalDateTime());
                    }

                    return user;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public User getUserByUsername(String username, String password) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USER_BY_USERNAME_QUERY)) {

        	ps.setString(1, username);

        	try (ResultSet rs = ps.executeQuery()) {
        	    if (rs.next()) {

        	        String storedHash = rs.getString("Password");

        	        if (!isPasswordValid(password, storedHash)) {
        	            return null;
        	        }

        	        User user = new User();

                    user.setUserId(rs.getInt("UserId"));
                    user.setName(rs.getString("name"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhoneNumber(rs.getString("phone number"));
                    user.setAddress(rs.getString("Address"));
                    user.setRole(rs.getString("Role"));

                    Timestamp createDate = rs.getTimestamp("CreateDate");
                    if (createDate != null) {
                        user.setCreateDate(createDate.toLocalDateTime());
                    }

                    Timestamp lastLoginDate = rs.getTimestamp("LastLoginDate");
                    if (lastLoginDate != null) {
                        user.setLastLoginDate(lastLoginDate.toLocalDateTime());
                    }

                    return user;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public void updateUser(User user) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_USER_QUERY)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());
            ps.setInt(8, user.getUserId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteUser(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_USER_QUERY)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_USERS_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("UserId"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                user.setPhoneNumber(rs.getString("phone number"));
                user.setAddress(rs.getString("Address"));
                user.setRole(rs.getString("Role"));

                Timestamp createDate = rs.getTimestamp("CreateDate");
                if (createDate != null) {
                    user.setCreateDate(createDate.toLocalDateTime());
                }

                Timestamp lastLoginDate = rs.getTimestamp("LastLoginDate");
                if (lastLoginDate != null) {
                    user.setLastLoginDate(lastLoginDate.toLocalDateTime());
                }

                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return users;
    }
    
  
    public boolean isUsernameTaken(String username) {
        String query = "SELECT 1 FROM `User` WHERE Username = ? LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check username.", e);
        }
    }

 
    public boolean isEmailTaken(String email) {
        String query = "SELECT 1 FROM `User` WHERE Email = ? LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check email.", e);
        }
    }
    private boolean isPasswordValid(
            String enteredPassword, String storedHash) {

        if (enteredPassword == null || storedHash == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(enteredPassword, storedHash);
        } catch (IllegalArgumentException e) {
            // Invalid hash format, including legacy plaintext values.
            return false;
        }
    }
}
