package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;

        String hr=deliveryTime.substring(0,2);
        String min=deliveryTime.substring(3,5);

        int totalTime=(Integer.parseInt(hr)*60)+Integer.parseInt(min);

        this.deliveryTime=totalTime;

    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}