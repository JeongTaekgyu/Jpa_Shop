package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memder_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // Order만 persist하면 orderItems 까지 persist 된다. 물론 ALL이기 때문에 다른 조건들도 적용된다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // Order만 persist하면 Delivery 까지 persist 된다. 물론 ALL이기 때문에 다른 조건들도 적용된다.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;    // 주문시간, 자바8에서 hibernate가 지원해준다.

    @Enumerated(EnumType.STRING) // 만약에 EnumType 이 ORDINAL 이면 새로운 타입이 추가 되면 숫자가 꼬여서 반드시 STRING으로 해야한다.
    private OrderStatus status; // 주문 상태 [ORDER ,CANDEL]

    // ==연관관계(편의)메서드== // - 양방향에서는 연관관계 메서드가 있으면 좋다.
    // 연관관계 편의 메서드의 위치는 양방향에서 핵심적으로 컨트롤하는 쪽이 들고있으면 좋다.
    // ( addOrderItem 에시를 보면 무조건 주인이 가지고 있는건 아닌듯 )
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDeliver(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // ==생성 메서드== //
    // createOrderItem 에서 removeStock 으로 재고를 줄이고 올거다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
                                             // 참고로 ...(가변인자)은 여러개의 매개변수를 받을 수 있다는 말이다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // ==비즈니스 로직== //
    /*
     * 주문 취소
     */
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {   // 배송 완료이면
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능 합니다.");
        }

        // 배송 완료 아니면 상태를 CANCEL로 바꿈
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: orderItems ) {
            orderItem.cancel();
        }
    }

    // ==조회 로직== //
    /*
    * 전체 주문 가격 조회
    * */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;

        // 위에 로직을 Replace with sum() 으로 바꾸고 -> inline variable 한 결과이다.
        /*
        return orderItems.stream().
                mapToInt(OrderItem::getTotalPrice).
                sum();
        */
    }

}
