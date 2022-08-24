package jpabook.jpashop.service;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderSearch;

import java.util.List;

public interface OrderService {
    Long order(Long memberId, Long itemId, int count);
    void cancelOrder(Long orderId);
    List<Order> findOrders(OrderSearch orderSearch);
}
