package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // EnumType은 @Enumerated 어노테이션을 넣어야 한다.
    @Enumerated(EnumType.STRING)   // ORDINAL과 STRING 타입 차이를 알고있어야한다.
    private DeliveryStatus status;  // READY, COMP
    // 만약에 EnumType 이 ORDINAL 이면 새로운 타입이 추가 되면 숫자가 꼬여서 반드시 STRING으로 해야한다.
}
