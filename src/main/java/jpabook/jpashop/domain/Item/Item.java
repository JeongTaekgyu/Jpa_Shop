package jpabook.jpashop.domain.Item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@BatchSize(size = 100)
@Entity
// 상속관계 매핑은 상속관계 전략을 지정해야한다. 이 전략을 부모클래스에 입력해야한다.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글테이블 전략(한 테이블에 다 넣겠다) - ppt의 회원 테이블 분석에 있는 ITEM 테이블 참고
@DiscriminatorColumn(name = "dtype") // 싱글 테이블이기 때문에 db에 저장할 때 구분해야한다.
@Getter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Item {    // 구현체를 가지고할거라서 Item은 추상클래스로 만든다

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    public Item(String name, int price, int stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // ==비즈니스 로직== // 엔티티안에 비즈니스 로직을 넣네 -> ★응집도가 있다.★ -> 객체지향적이다.

    /*
    * stock 증가
    */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /*
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

    public void changeItem(String name, int price, int stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
