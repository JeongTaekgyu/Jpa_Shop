package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // JUnit 실행할 때 스프링이랑 엮어서 실행한다.
@SpringBootTest // SpringBoot를 띄운 상태로 테스트를 한다.
@Transactional  // 트랜잭션 걸고 테스트 한다음에 테스트 끝나면 롤백한다.(서비스, 리포지토리에서는 롤백하지 않는다. 테스트에서만 롤백한다.)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    public void 회원가입() throws Exception{
        // given - 이런게 주어졌을 떄
        Member member = new Member();
        member.setName("kim");

        // when - 이렇게 하면
        Long saveId = memberService.join(member);

        // then - 이렇게 된다.
//        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));
        // jpa에서 같은 트랜잭션 안에서 같은 엔티티 아이디(pk) 값이 똑같으면
        // 같은 영속성 컨텍스트에서 똑같은게 관리된다. ,즉 하나로만 관리된다.
    }

    // expected = IllegalStateException.class가 있으면 아래 try catch문 사용 안해도 된다.
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2);    // 예외가 발생해야함
        /*try{
            memberService.join(member2); // 예외가 발생해야함
        } catch (IllegalStateException e){
            return;
        }*/

        // then
        fail("예외가 발생해야 한다.");
    }

}