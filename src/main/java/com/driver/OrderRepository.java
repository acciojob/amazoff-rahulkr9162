package com.driver;

import org.springframework.stereotype.Repository;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderHashMap=new HashMap<>();

    HashMap<String, DeliveryPartner> partnerHashMap=new HashMap<>();

    HashMap<String, List<Order>> orderPartnerPairHashMap= new HashMap<>();

    HashMap<String,Order> unassignedOrder=new HashMap<>();

    public void addOrderToDb(Order order){
        orderHashMap.put(order.getId(), order);
        unassignedOrder.put(order.getId(), order);
    }

    public void addPartnerToDb(String partnerId){
        partnerHashMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPairToDb(String orderId,String partnerId){
        if(orderHashMap.containsKey(orderId) && partnerHashMap.containsKey(partnerId)){
            if(orderPartnerPairHashMap.containsKey(partnerId)){
                orderPartnerPairHashMap.get(partnerId).add(orderHashMap.get(orderId));
                unassignedOrder.remove(orderId);
                partnerHashMap.get(partnerId).setNumberOfOrders(orderPartnerPairHashMap.get(partnerId).size());
            }
            else {
                List<Order> list=new ArrayList<>();
                list.add(orderHashMap.get(orderId));
                orderPartnerPairHashMap.put(partnerId,list);
                unassignedOrder.remove(orderId);
                partnerHashMap.get(partnerId).setNumberOfOrders(1);
            }
        }
    }

    public Order getOrderById(String orderId){
        return orderHashMap.getOrDefault(orderId,null);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return partnerHashMap.getOrDefault(partnerId,null);
    }

    public int getOrderCountByPartnerId(String partnerId){

        if(partnerHashMap.containsKey(partnerId)){
            return partnerHashMap.get(partnerId).getNumberOfOrders();
        }

        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> result=new ArrayList<>();

        if(orderPartnerPairHashMap.containsKey(partnerId)) {
            List<Order> orders = orderPartnerPairHashMap.get(partnerId);

            for (Order o : orders) {
                result.add(o.getId());
            }
        }
        return result;
    }

    public List<String> getAllOrders(){
        List<String> orders=new ArrayList<>();
        for(String s:orderHashMap.keySet()){
            orders.add(s);
        }
        return orders;
    }


    public int getCountOfUnassignedOrders(){
//        int count=0;
//        for(String s:orderHashMap.keySet()){
//            for(List<Order> orders:orderPartnerPairHashMap.values()){
//                for(Order o:orders){
//                    if(o.getId().equals(s)){
//                        count++;
//                    }
//                }
//            }
//
//        }
//        return orderHashMap.size()-count;
        return unassignedOrder.size();
    }


    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){

        String hr=time.substring(0,2);
        String min=time.substring(3,5);

        int totalTime=(Integer.parseInt(hr)*60)+Integer.parseInt(min);
        int count=0;
        if(orderPartnerPairHashMap.containsKey(partnerId)) {
            for (Order o : orderPartnerPairHashMap.get(partnerId)) {
                if (o.getDeliveryTime() > totalTime) {
                    count++;
                }
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int maxTime=Integer.MIN_VALUE;
        if(orderPartnerPairHashMap.containsKey(partnerId)) {
            for (Order o : orderPartnerPairHashMap.get(partnerId)) {
                if (o.getDeliveryTime() > maxTime)
                    maxTime = o.getDeliveryTime();
            }
        }
        int hr=maxTime/60;
        int min=maxTime%60;
        String h=String.valueOf(hr);
        if(h.length()==1)
            h="0"+h;
        String m=String.valueOf(min);
        if(m.length()==1)
            m="0"+m;
        String str=h+":"+m;
        //System.out.println(str);

        return str;

    }

    public void deletePartnerById(String partnerId){
        if(orderPartnerPairHashMap.containsKey(partnerId)) {
            for (Order o : orderPartnerPairHashMap.get(partnerId)) {
                unassignedOrder.put(o.getId(), o);
            }
            orderPartnerPairHashMap.remove(partnerId);
        }
        partnerHashMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        for(String s:orderPartnerPairHashMap.keySet()){
            List<Order> list=orderPartnerPairHashMap.get(s);
            Iterator<Order> iterator=list.iterator();

//            for(Order o:list){
//                if(o.getId().equals(orderId)){
//                    list.remove(o);
//                }
//            }
            while(iterator.hasNext()){
                Order o=iterator.next();
                if(o.getId().equals(orderId)){
                    iterator.remove();
                    partnerHashMap.get(s).setNumberOfOrders(orderPartnerPairHashMap.get(s).size());
                }
            }



        }
        orderHashMap.remove(orderId);
        unassignedOrder.remove(orderId);
    }
}