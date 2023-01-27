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
    HashMap<String, Order> unAssignedDb= new HashMap<>();

    public void addOrder(Order order){
        OrderDb.put(order.getId(), order);
        unAssignedDb.put(order.getId(), order);
    }
    public void addPartner(String PartnerId){
        DeliveryPartner partner = new DeliveryPartner(PartnerId);
        DeliveryPartnerDb.put(PartnerId,partner );
    }

    public void addOrderPartnerPair(String orderId, String PartnerId) {
        if (OrderDb.containsKey(orderId) && DeliveryPartnerDb.containsKey(PartnerId)) {
            if (OrderPartnerPairDb.containsKey(PartnerId)) {
                OrderPartnerPairDb.get(PartnerId).add(OrderDb.get(orderId));
                unAssignedDb.remove(orderId);
                DeliveryPartnerDb.get(PartnerId).setNumberOfOrders(OrderPartnerPairDb.get(PartnerId).size());
            } else {
                List<Order> list = new ArrayList<>();
                list.add(OrderDb.get(orderId));
                OrderPartnerPairDb.put(PartnerId, list);
                unAssignedDb.remove(orderId);
                DeliveryPartnerDb.get(PartnerId).setNumberOfOrders(1);
            }
        }
    }

    public Order getOrderById(String orderId){
        if(OrderDb.containsKey(orderId))
           return OrderDb.get(orderId);
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId){
        if(DeliveryPartnerDb.containsKey(partnerId))
            return DeliveryPartnerDb.get(partnerId);
        return  null;
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        if(OrderPartnerPairDb.containsKey(partnerId)) {
            List<Order> list = OrderPartnerPairDb.get(partnerId);
            return list.size();
        }
        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> list1 = new ArrayList<>();
        if (OrderPartnerPairDb.containsKey(partnerId)) {
            List<Order> list = OrderPartnerPairDb.get(partnerId);
            for (Order order : list) {
                String str = order.getId();
                list1.add(str);
            }
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

    public Integer getCountOfUnassignedOrders(){
        return unAssignedDb.keySet().size();
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        List<Order> orderList = OrderPartnerPairDb.get(partnerId);
        Order order = orderList.get(orderList.size()-1);
        return null;
    }
    public void deleteOrderById(String orderId){
        OrderDb.remove(orderId);
    }
}
