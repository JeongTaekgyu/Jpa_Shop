/*
package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

// JUnit4 로 생성함
@RunWith(SpringRunner.class)    // 스프링과 관련된걸 테스트할 거다. ( JUnit4만 되는건가>)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    // 엔티티매니저를 통한 모든 데이터 변경은 항상 transaction 안에서 일어나야한다.

    @Test
    @Transactional  // Spring Annotaion 을 import함
    @Rollback(false)
    public void testMember() throws Exception{
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long saveId = memberRepository.save(member);  // ctrl + alt + v : extractor 에서 변수 뽑아오는 단축키
        Member findMember = memberRepository.find(saveId);

        // then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);    // 같은 트랜잭션안에서 저장을 하고 조회하면 영속성 컨텍스트가 같다.
                                                                // 같은 영속성 컨텍스트 안에서는 id값이 같으면 같은 엔티티로 식별한다.
        System.out.println("~~~"+ findMember);
        System.out.println("~~~"+ member);
    }
}*/
