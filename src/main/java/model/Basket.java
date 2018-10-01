package model;

import java.util.HashMap;
import java.util.Map;

public class Basket {
    /**
     * Map that describes the basket
     * K is the id of Product
     * V is the quantity added
     * One object corresponds one user

     */

    private Map<Integer, Integer> basket = new HashMap<>();

    public Basket(Map<Integer, Integer> basket) {
        this.basket = basket;
    }

    public Basket () {}

    public Map<Integer, Integer> getBasket() {
        return basket;
    }

    public void setBasket(Map<Integer, Integer> basket) {
        this.basket = basket;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "basket=" + basket +
                '}';
    }
}
