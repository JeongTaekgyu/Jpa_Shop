package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")   // 단방향이라 Item 쪽에는 코드 없음
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")  // 양방향이라 Order 쪽에 코드 있음
    private Order order;
    
    private int orderPrice; // 주문 가격
    private int count;  // 주문 수량
}
