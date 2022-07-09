package jpabook.jpashop.domain.Item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // 싱글 테이블이기 때문에 db에 저장할 때 구분해야한다.
@Getter @Setter
public class Book extends Item{

    private String author;
    private String isbn;
}
