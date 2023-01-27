package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String, Order> OrderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> DeliveryPartnerDb = new HashMap<>();

    HashMap<String, List<Order>> OrderPartnerPairDb= new HashMap<>();

    public void addOrder(Order order){
        OrderDb.put(order.getId(), order);
    }
    public void addPartner(String PartnerId){
        DeliveryPartner partner = new DeliveryPartner(PartnerId);
        DeliveryPartnerDb.put(PartnerId,partner );
    }

    public void addOrderPartnerPair(String orderId, String PartnerId){
        List<Order> list = OrderPartnerPairDb.get(PartnerId);
        list.add(OrderDb.get(orderId));
        OrderPartnerPairDb.put(PartnerId, list);
    }

    public Order getOrderById(String orderId){
        return OrderDb.get(orderId);

    }

    public DeliveryPartner getPartnerById(String partnerId){
        return DeliveryPartnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        List<Order> list = OrderPartnerPairDb.get(partnerId);
        return list.size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<Order> list = OrderPartnerPairDb.get(partnerId);
        List<String> list1 = new ArrayList<>();
        for(Order order : list){
            String str = order.getId();
            list1.add(str);
        }
        return list1;
    }

    public List<String>  getAllOrders(){
        List<Order> list = (List<Order>) OrderDb.values();
        List<String> list1 = new ArrayList<>();
        for(Order order : list){
            list1.add(order.getId());
        }
        return list1;
    }
}
