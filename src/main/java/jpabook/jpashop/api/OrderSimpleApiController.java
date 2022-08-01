package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        // 원하는 것 들만 강제로 Lazy 시킨다.
        for (Order order : all) {
            /* order.getMember 까지는 프록시 객체이다.
            즉, db에 쿼리가 아직 안날라 갔다. 그런데 .getName까지 하면 Lazy가 장제 초기화 돼서
            Member에 쿼리를 날려서 hibernate가 데이터를 다 가지고 온다.
            order.getDelivery().getAddress(); 도 마찬가지
            */
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all; // 참고로 이대로 반환하면 배열 형식 [] 으로 반환돼서 안좋다.
    }

}
