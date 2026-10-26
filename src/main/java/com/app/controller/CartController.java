
package com.app.controller;

import java.io.IOException;

import com.app.DaoImp.MenuDaoImpl;
import com.app.model.Cart;
import com.app.model.CartItem;
import com.app.model.Menu;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // ADD ITEM
        if ("add".equals(action)) {

            int menuId = Integer.parseInt(req.getParameter("menuId"));

            MenuDaoImpl mdao = new MenuDaoImpl();
            Menu menu = mdao.getMenu(menuId);

            if (menu != null) {

                int restaurantId = menu.getRestaurantId();

                if (cart.getItems().isEmpty()) {

                    cart.setRestaurantId(restaurantId);
                    cart.addItem(new CartItem(menu, 1));

                } else if (cart.getRestaurantId() == restaurantId) {

                    cart.addItem(new CartItem(menu, 1));

                } else {

                    // Store the item for which confirmation is required.
                    session.setAttribute("pendingMenuId", menuId);
                    session.setAttribute("pendingRestaurantId", restaurantId);

                    session.setAttribute(
                        "cartMessage",
                        "Your cart contains items from another restaurant. "
                        + "Do you want to clear your existing cart and continue?"
                    );
                }

                res.sendRedirect(
                    req.getContextPath()
                    + "/menu?restaurantId="
                    + restaurantId
                );
                return;
            }
        }

        // CONFIRM SWITCHING TO ANOTHER RESTAURANT
        else if ("confirmSwitch".equals(action)) {

            String menuIdParam = req.getParameter("menuId");
            String restaurantIdParam = req.getParameter("restaurantId");

            HttpSession session1 = req.getSession();

            try {
                int menuId = Integer.parseInt(menuIdParam);
                int restaurantId = Integer.parseInt(restaurantIdParam);

                MenuDaoImpl mdao = new MenuDaoImpl();
                Menu menu = mdao.getMenu(menuId);

                // Validate the selected menu item and restaurant.
                if (menu != null
                        && menu.getRestaurantId() == restaurantId) {

                    // Clear the previous restaurant's cart.
                    cart.clearCart();

                    // Set the new restaurant and add the selected item.
                    cart.setRestaurantId(restaurantId);
                    cart.addItem(new CartItem(menu, 1));

                    // Remove any old pending confirmation data.
                    session.removeAttribute("pendingMenuId");
                    session.removeAttribute("pendingRestaurantId");
                    session.removeAttribute("cartMessage");

                    // Return to the selected restaurant's menu.
                    res.sendRedirect(
                        req.getContextPath()
                        + "/menu?restaurantId="
                        + restaurantId
                    );
                    return;

                } else {
                    // Invalid menu item or restaurant.
                    res.sendRedirect(req.getContextPath() + "/cart");
                    return;
                }

            } catch (NumberFormatException e) {
                // Missing or invalid request parameters.
                res.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
        }
        // CANCEL RESTAURANT SWITCH
        else if ("cancelSwitch".equals(action)) {

            Object pendingRestaurant =
                    session.getAttribute("pendingRestaurantId");

            session.removeAttribute("pendingMenuId");
            session.removeAttribute("pendingRestaurantId");
            session.removeAttribute("cartMessage");

            if (pendingRestaurant instanceof Integer) {
                res.sendRedirect(
                    req.getContextPath()
                    + "/menu?restaurantId="
                    + pendingRestaurant
                );
            } else {
                res.sendRedirect(req.getContextPath() + "/cart");
            }

            return;
        }

        // UPDATE QUANTITY
        else if ("update".equals(action)) {

            int menuId = Integer.parseInt(req.getParameter("menuId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            cart.updateQuantity(menuId, quantity);

            String returnTo = req.getParameter("returnTo");
            String restaurantId = req.getParameter("restaurantId");

            if ("menu".equals(returnTo) && restaurantId != null) {
                res.sendRedirect(
                    req.getContextPath()
                    + "/menu?restaurantId="
                    + restaurantId
                );
                return;
            }

            res.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // REMOVE ITEM
        else if ("remove".equals(action)) {

            int menuId = Integer.parseInt(req.getParameter("menuId"));

            cart.removeItem(menuId);

            String returnTo = req.getParameter("returnTo");
            String restaurantId = req.getParameter("restaurantId");

            if ("menu".equals(returnTo) && restaurantId != null) {
                res.sendRedirect(
                    req.getContextPath()
                    + "/menu?restaurantId="
                    + restaurantId
                );
                return;
            }

            res.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // CLEAR CART
        else if ("clear".equals(action)) {

            cart.clearCart();

            res.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        res.sendRedirect(req.getContextPath() + "/cart");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        req.setAttribute("cart", cart);

        req.getRequestDispatcher("cart.jsp").forward(req, res);
    }
}
