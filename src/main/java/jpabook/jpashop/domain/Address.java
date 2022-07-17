package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // jpa 내장 타입
@Getter
public class Address {
    // 값 타입 자체는 변경이 되게 설계되면 안된다 즉 immutable 하게 설계 돼야한다.
    // 생성할 때만 값이 생성이 되고 변경이 발구능 하게끔 설계 돼야한다.
    private String city;
    private String street;
    private String zipcode;

    // Embeddable 은 기본 생성자가 있어야 한다.
    protected Address() {

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }


}
