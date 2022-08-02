package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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
            즉, db에 쿼리가 아직 안날라 갔다. 그런데 .getName()까지 하면 Lazy가 강제 초기화 돼서
            Member에 쿼리를 날려서 hibernate가 데이터를 다 가지고 온다.
            order.getDelivery().getAddress(); 도 마찬가지
            */
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all; // 참고로 이대로 반환하면 배열 형식 [] 으로 반환돼서 안좋다.
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // 참고로 List로 반환하면 안되고 result로 감싸야함
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        // List로 그대로 반환 하면 안됨 ( Entity를 -> Dto로 변환 해야함)
        // map은 a를 b로 바꿈 즉 여기선 order를 SimpleOrderDto로 바꿈
        // .collect는 List 형태로 바꿈
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        // 이것도 엔티티 조회 한다음에 dto로 변환했다.

        // fetch join 했기 때문에 query가 한 번 나간다.
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화 - 영속성 컨텍스트가 멤버ID를 가지고 영속성 컨텍스트에서 찾아본다. 근데 없으면 DB에 쿼리를 날린다.
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화 -> 마찬가지
        }
    }

}
