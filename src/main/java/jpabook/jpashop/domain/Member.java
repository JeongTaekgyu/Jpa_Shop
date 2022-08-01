package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded   // jpa 내장타입
    private Address address;

    @JsonIgnore // 양 방향 연관관계에서는 명시해야한다. 안그러면 무한 루프로 참조한다
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
