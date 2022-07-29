package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service    // @Service역시 ComponentScan의 대상이다(@Service 눌러보면 @Component 어노테이션 있다.) 그러므로 스프링 빈에 등록된다.
// javax가 아니라 스프링이 제공하는 Transactional을 사용하는게 옵션이 더 많다
@Transactional(readOnly = true) // 읽기에서 (readOnly = true)는 jpa가 조회하는 곳에서는 성능을 최적화 한다
@RequiredArgsConstructor // final 있는 필드만 가지고 생성자를 만들어준다.
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     **/
    @Transactional  // 쓰기는 readOnly = true하면 안된다. default는 false이다.
    public Long join(Member member){
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    /*
    WAS가 동시에 여러개 떠서 memberA가 db에 동시에 insert를 하게되면 validateDuplicateMember 를 동시에 호출할 수 있기때문에
    실무에서는 한번더 방어를 해야한다. 즉, 멀티스레드 상황을 고려해서 db의 member.getName()을 유니크 제약조건으로 잡는게 안전히디
     */
    private void validateDuplicateMember(Member member) {
        // exception

        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // Id로 회원 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        /* 종료되면 스프링이 aop가 동작하면서
           트랜잭션 어노테이션에 의해서 트랜잭션 관련된 aop가 끝나는 시점에 트랜잭션 커밋이 되면서
           jpa가 영속성 컨텍스트를 flish하고 데이터베이스 트랜잭션 commit한다.
           jpa가 변경감지를 해서 쿼리를 날려준다.
        */

        /* 반환타입으로 member를 반환해도 되는데
        걔를 반환하면 영속상태가 끊긴 member를 반환하게 된다.
        즉, 커맨드와 쿼리를 분리해하는게 좋다
        */
    }
}
