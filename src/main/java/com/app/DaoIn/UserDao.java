
package com.app.DaoIn;

import java.util.List;
import com.app.model.User;

public interface UserDao {

    void addUser(User user);

    User getUser(int userId);

    User getUser(String email, String password);

    User getUserByUsername(String username, String password);

    void updateUser(User user);

    void deleteUser(int userId);

    List<User> getAllUsers();
}
