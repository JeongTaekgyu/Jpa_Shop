package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    // DTO를 직접 조회하는 V4 방법
    public List<OrderSimpleQueryDto> findOrderDtos() {
        // 내가 원하는 것만 select 할 수 있다. v3와 log 비교해보면 알 수 있다.
        // 그런데 무조건 v4 가 좋다고 볼수 없다 trade off 관계이다.
        // 즉 v4는 원하는 값만 가져오기 때문에 재사용성이 떨어진다. 하지만 v3는 테이블 차체 정보를 다 가져와서 재사용성이 높다.
        // 즉, v4는 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점이 있다.
        // 하지만 성능 최적화에서는 v4가 v3보다는 조금 더 좋다.
        // (성능이 그렇게 차이 나지는 않는다. 데이터 사이즈가 클 때는 차이가 좀 있을 수 있다. 또는 굉장히 요청이 많으면 v4를 선택하는 게 좋을 수도 있다.)
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
