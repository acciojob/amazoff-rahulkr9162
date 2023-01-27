package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String, Order> OrderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> DeliveryPartnerDb = new HashMap<>();
    HashMap<String, List<Order>> OrderPartnerPairDb = new HashMap<>();
    HashMap<String, Order> unAssignedDb = new HashMap<>();

    public void addOrder(Order order) {
        OrderDb.put(order.getId(), order);
        unAssignedDb.put(order.getId(), order);
    }

    public void addPartner(String PartnerId) {
        DeliveryPartner partner = new DeliveryPartner(PartnerId);
        DeliveryPartnerDb.put(PartnerId, partner);
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

    public Order getOrderById(String orderId) {
        if (OrderDb.containsKey(orderId))
            return OrderDb.get(orderId);
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (DeliveryPartnerDb.containsKey(partnerId))
            return DeliveryPartnerDb.get(partnerId);
        return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if (DeliveryPartnerDb.containsKey(partnerId)) {
            return DeliveryPartnerDb.get(partnerId).getNumberOfOrders();
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

    public List<String> getAllOrders() {
        List<Order> list = (List<Order>) OrderDb.values();
        List<String> list1 = new ArrayList<>();
        for (Order order : list) {
            list1.add(order.getId());
        }
        return list1;
    }

    public Integer getCountOfUnassignedOrders() {
        return unAssignedDb.keySet().size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String hr = time.substring(0, 2);
        String min = time.substring(3, 5);

        int totalTime = (Integer.parseInt(hr) * 60) + Integer.parseInt(min);
        int count = 0;
        if (OrderPartnerPairDb.containsKey(partnerId)) {
            for (Order o : OrderPartnerPairDb.get(partnerId)) {
                if (o.getDeliveryTime() > totalTime) {
                    count++;
                }
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int maxTime = Integer.MIN_VALUE;
        if (OrderPartnerPairDb.containsKey(partnerId)) {
            for (Order o : OrderPartnerPairDb.get(partnerId)) {
                if (o.getDeliveryTime() > maxTime)
                    maxTime = o.getDeliveryTime();
            }
        }
        int hr = maxTime / 60;
        int min = maxTime % 60;
        String h = String.valueOf(hr);
        if (h.length() == 1)
            h = "0" + h;
        String m = String.valueOf(min);
        if (m.length() == 1)
            m = "0" + m;
        String str = h + ":" + m;
        return str;
    }

    public void deletePartnerById(String partnerId) {
        if (OrderPartnerPairDb.containsKey(partnerId)) {
            for (Order o : OrderPartnerPairDb.get(partnerId)) {
                unAssignedDb.put(o.getId(), o);
            }
            OrderPartnerPairDb.remove(partnerId);
        }
        DeliveryPartnerDb.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        for (String s : OrderPartnerPairDb.keySet()) {
            List<Order> list = OrderPartnerPairDb.get(s);
            Iterator<Order> iterator = list.iterator();

            while (iterator.hasNext()) {
                Order o = iterator.next();
                if (o.getId().equals(orderId)) {
                    iterator.remove();
                    DeliveryPartnerDb.get(s).setNumberOfOrders(OrderPartnerPairDb.get(s).size());
                }
            }
        }
    }
}
