package jpabook.jpashop.domain.Item;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // 싱글 테이블이기 때문에 db에 저장할 때 구분해야한다.
@Getter
//@SuperBuilder   // 상속받는 부모클래스 필드도 빌더패턴을 적용시킨다.
@NoArgsConstructor
@AllArgsConstructor
public class Book extends Item{

    private String author;
    private String isbn;

    @Builder
    public Book(String name, int price, int stockQuantity, String author, String isbn){
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }
}
