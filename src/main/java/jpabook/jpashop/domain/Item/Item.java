package jpabook.jpashop.domain.Item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
// 상속관계 매핑은 상속관계 전략을 지정해야한다. 이 전략을 부모클래스에 입력해야한다.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글테이블 전략(한 테이블에 다 넣겠다) - ppt의 회원 테이블 분석에 있는 ITEM 테이블 참고
@DiscriminatorColumn(name = "dtype") // 싱글 테이블이기 때문에 db에 저장할 때 구분해야한다.
@Getter @Setter
public abstract class Item {    // 구현체를 가지고할거라서 Item은 추상클래스로 만든다

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
