package com.app.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private int restaurantId;
    private Map<Integer, CartItem> items;

    public Cart() {
        items = new HashMap<>();
    }

    public Cart(int restaurantId) {
        this.restaurantId = restaurantId;
        items = new HashMap<>();
    }

    // Add item to cart
    public void addItem(CartItem cartItem) {

        int menuId = cartItem.getMenu().getMenuId();

        if (items.containsKey(menuId)) {
            CartItem existingItem = items.get(menuId);
            existingItem.setQuantity(
                existingItem.getQuantity() + cartItem.getQuantity()
            );
        } else {
            items.put(menuId, cartItem);
        }
    }

    // Update quantity of an existing item
    public void updateQuantity(int menuId, int quantity) {

        if (items.containsKey(menuId)) {

            if (quantity <= 0) {
                items.remove(menuId);
            } else {
                items.get(menuId).setQuantity(quantity);
            }
        }
    }

    // Remove item completely from cart
    public void removeItem(int menuId) {
        items.remove(menuId);
    }

    // Clear the entire cart
    public void clearCart() {
        items.clear();
        restaurantId = 0;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, CartItem> items) {
        this.items = items;
    }
}