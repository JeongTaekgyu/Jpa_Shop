package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")   // 단방향이라 Item 쪽에는 코드 없음
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")  // 양방향이라 Order 쪽에 코드 있음
    private Order order;
    
    private int orderPrice; // 주문 가격
    private int count;  // 주문 수량

    // ==생성 메서드== //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }


    // ==비즈니스 로직== //
    public void cancel() {
        getItem().addStock(count);  // 재고 수량을 원복해 준다.
    }

    // ==조회 로직== //
    /*
    * 주문상품 전체 가격 조회
    * */
    public int getTotalPrice() {
        return getOrderPrice() * getCount(); // 주문 가격 * 주문 수량
    }
}
