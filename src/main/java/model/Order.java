package model;

import java.util.Date;

public class Order {

    private int orderId;
    private long customerId;
    private String orderDate;

    public Order(int orderId, long customerId, String orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
    }

    public Order() {}

    public int getOrderId() {
        return orderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
