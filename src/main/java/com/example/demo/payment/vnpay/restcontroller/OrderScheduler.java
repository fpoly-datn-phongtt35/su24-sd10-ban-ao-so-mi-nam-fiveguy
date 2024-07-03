//package com.example.demo.payment.vnpay.restcontroller;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.concurrent.*;
//
//public class OrderScheduler {
//    private ScheduledExecutorService scheduler;
//
//    public OrderScheduler() {
//        this.scheduler = Executors.newScheduledThreadPool(1);
//    }
//
//    public void scheduleOrderDeletion(Order order) {
//        long delay = 1; // 20 phút
//        ScheduledFuture<?> deletion = scheduler.schedule(() -> deleteOrder(order), delay, TimeUnit.MINUTES);
//    }
//
//    private void deleteOrder(Order order) {
//        // Thực hiện xóa đơn hàng ở đây
//        System.out.println("Đã xóa đơn hàng: " + order.getId());
//    }
//
//    public static void main(String[] args) {
//
//        LocalDateTime currentTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedTime = currentTime.format(formatter);
//        // Tạo một đơn hàng và lập lịch xóa
//        Order order = new Order("12345", formattedTime); // Ví dụ thông tin đơn hàng
//        OrderScheduler orderScheduler = new OrderScheduler();
//        orderScheduler.scheduleOrderDeletion(order);
//    }
//}
//
//class Order {
//    private String id;
//    private String orderTime;
//
//    public Order(String id, String orderTime) {
//        this.id = id;
//        this.orderTime = orderTime;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getOrderTime() {
//        return orderTime;
//    }
//}
